package com.iii.purrpulse.gdx_stuff


import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.iii.purrpulse.LauncherMode
import com.iii.purrpulse.launcher_mode
import kotlin.math.max
import kotlin.math.min


val point_count: Int = 50

var position_array = FloatArray(point_count * 2);
var color_array = FloatArray(point_count * 3)

val use_highp = false
var delete_closest = false


var central_hue_setting = 300f
var hue_spread_setting = 100f

var central_hue = 300f
var hue_spread = 100f

var clicked = false

fun light_shader() =
    """
    #ifdef GL_ES
    precision ${if (use_highp) "highp" else "mediump"} float;
    #endif
    
    const int n = ${point_count};
    const float threshold = 25.0;
    
    uniform vec2 u_positions[n];
    uniform vec3 u_colors[n];
            
    void main() {
        vec2 position = gl_FragCoord.xy;
        float min_dist = distance(u_positions[0], position);
        for (int i = 1; i < n; i++) {
            if (distance(u_positions[i], position) < min_dist) {
                min_dist = distance(u_positions[i], position);
            }
        }
        float counter = 0.0;
        vec3 total_color = vec3(0.0, 0.0, 0.0);
        for (int i = 0; i < n; i++) {
            float border = min_dist + threshold;
            float dist = distance(u_positions[i], position); 
            if (dist < border) {
                total_color += u_colors[i] * (border - dist);
                counter += border - dist;
            }
        }
        vec3 final_color = vec3(total_color.x/counter, total_color.y/counter, total_color.z/counter);
        gl_FragColor = vec4(final_color, 1.0);
    }
    """.trimIndent()

fun tesselation_shader() =
    """
    #ifdef GL_ES
    precision ${if (use_highp) "highp" else "mediump"} float;
    #endif
    
    const int n = ${point_count};
    
    uniform vec2 u_positions[n];
    uniform vec3 u_colors[n];
            
        void main() {
        vec2 position = gl_FragCoord.xy;
        float min_dist = distance(u_positions[0], position);
        vec3 final_color = u_colors[0];
        for (int i = 1; i < n; i++) {
            float dist = distance(u_positions[i], position);
            if (dist < min_dist) {
                min_dist = dist;
                final_color = u_colors[i];
            }
        }
        gl_FragColor = vec4(final_color, 1.0);
    }
    """.trimIndent()

fun balls_shader() =
    """
    #ifdef GL_ES
    precision ${if (use_highp) "highp" else "mediump"} float;
    #endif
    
    const int n = ${point_count};
    
    uniform vec2 u_positions[n];
    uniform vec3 u_colors[n];
            
        void main() {
        vec2 position = gl_FragCoord.xy;
        float min_dist = distance(u_positions[0], position);
        vec3 final_color = u_colors[0];
        for (int i = 1; i < n; i++) {
            float dist = distance(u_positions[i], position);
            if (dist < min_dist) {
                min_dist = dist;
                final_color = u_colors[i];
            }
        }
        if (min_dist > 150.0) {
            final_color = vec3(0.0, 0.0, 0.0);
        }
        gl_FragColor = vec4(final_color, 1.0);
    }
    """.trimIndent()

fun vertex_shader() =
    """

    attribute vec4 ${ShaderProgram.POSITION_ATTRIBUTE};
    attribute vec4 ${ShaderProgram.COLOR_ATTRIBUTE};
    attribute vec4 ${ShaderProgram.NORMAL_ATTRIBUTE};
    
    uniform mat4 u_projModelView;

    void main() {
        gl_Position = u_projModelView * ${ShaderProgram.POSITION_ATTRIBUTE};
    }   
     
    """.trimIndent()


fun getFile(path: String): String {
//    This does not want to work :(
    var handle = Gdx.files.internal(path)
    var text = handle.readString()
    return text
}
class Point(var pos: Vector2, val id: Int) {
    var velocity: Vector2 = Vector2(0f, 0f)
    var color: Color = Color().fromHsv((central_hue + (MathUtils.random() - 0.5f) * hue_spread).mod(360f), 1f, 0.35f + MathUtils.random() * 0.5f)
    fun repel(from: Vector2) {
        var direction = pos.cpy().sub(from)
        val distance2 = direction.len2()/1000f + 0.3f

        direction.nor()
        velocity.add(direction.scl((1f / distance2)))
    }

    fun repelSpawn(from: Vector2) {
        var direction = pos.cpy().sub(from)
//        val distance2 = direction.len() + 10f
        direction.nor()
        velocity.add(direction.scl(10f));
    }

}

class MyController : InputProcessor {
    var touching: Boolean = false

    var point_list: ArrayDeque<Point> = ArrayDeque()
    var color_list: ArrayDeque<Vector3> = ArrayDeque()
    var nextPointId = 0
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touching = false
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        clicked = true
        touching = true
        val new_point = Point(Vector2(screenX.toFloat(), screenY.toFloat()), nextPointId)
        new_point.velocity = Vector2(MathUtils.random()*5, MathUtils.random()*5)
        nextPointId += 1

        if (point_list.size > point_count - 1) {
            if (delete_closest) {
                var candidate = point_list.get(0)
                var min_dist = new_point.pos.cpy().sub(candidate.pos).len2()
                for (point in point_list) {
                    var point_dist = new_point.pos.cpy().sub(point.pos).len2()
                    if (point_dist < min_dist) {
                        min_dist = point_dist
                        candidate = point
                    }
                }
                point_list.remove(candidate)
            }
            else {
                point_list.removeLast()
            }
        }

        point_list.addFirst(new_point)

        for (point in point_list) {
            if (point.id != new_point.id) {
                // apply force on rhs:
                point.repelSpawn(new_point.pos)
            }
        }


        return false
    }
}


class LibGdxDemo : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private lateinit var font: BitmapFont
    private lateinit var controller: MyController
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var shaderProgram: ShaderProgram



    override fun create() {
        clicked = false

        font = BitmapFont()

        delete_closest = false
        if ((launcher_mode == LauncherMode.Canvas) or (launcher_mode == LauncherMode.Tesselation) or (launcher_mode == LauncherMode.Flames)) {
            delete_closest = true
        }

        if (launcher_mode == LauncherMode.Canvas) {
            central_hue = 0f
            hue_spread = 360f
        }
        else {
            central_hue = central_hue_setting
            hue_spread = hue_spread_setting
        }

        val screen_y = Gdx.graphics.getHeight()
        val screen_x = Gdx.graphics.getWidth()

        batch = SpriteBatch()

        font = BitmapFont()
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        controller = MyController()
        Gdx.input.inputProcessor = controller

//        ShaderProgram.pedantic = false
        shaderProgram = ShaderProgram(vertex_shader(), tesselation_shader())
        if (launcher_mode == LauncherMode.Balls) {
            shaderProgram = ShaderProgram(vertex_shader(), balls_shader())
        } else if (launcher_mode == LauncherMode.Flames) {
            shaderProgram = ShaderProgram(vertex_shader(), light_shader())
        }


        if (launcher_mode != LauncherMode.Balls) {
            for (i in 0..point_count - 1) {
                val new_point = Point(
                    Vector2(MathUtils.random() * screen_x, MathUtils.random() * screen_y),
                    controller.nextPointId
                )

                controller.nextPointId += 1
                if ((launcher_mode == LauncherMode.Balls) or (launcher_mode == LauncherMode.Flames)) {
                    new_point.color = Color()
                }
                controller.point_list.addFirst(new_point)
            }
        }

        shapeRenderer = ShapeRenderer(5000, shaderProgram)


        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("MyTag", "shader compilation failed");
            Gdx.app.error("MyTag", shaderProgram.getLog());
        }
        else {
            Gdx.app.log("MyTag", "shader compilation ok");
        }

    }

    var count = 0;

    override fun render() {
        val screen_y = Gdx.graphics.getHeight()
        val screen_x = Gdx.graphics.getWidth()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shaderProgram.begin();

        // set shader input:
        for (i in 0..point_count-1) {
            position_array[i*2 + 0] = -7000f;
            position_array[i*2 + 1] = -7000f;
            color_array[i*3 + 0] = 0f;
            color_array[i*3 + 1] = 0f;
            color_array[i*3 + 2] = 0f;
        }
        for (i in 0..controller.point_list.size-1) {
            position_array[i*2 + 0] = controller.point_list.get(i).pos.x;
            position_array[i*2 + 1] = screen_y - controller.point_list.get(i).pos.y;
            color_array[i*3 + 0] = controller.point_list.get(i).color.r;
            color_array[i*3 + 1] = controller.point_list.get(i).color.g;
            color_array[i*3 + 2] = controller.point_list.get(i).color.b;
        }

        shaderProgram.setUniform2fv("u_positions", position_array, 0, position_array.size)
        shaderProgram.setUniform3fv("u_colors", color_array, 0, color_array.size)

        batch.begin()

        // physics:
        for (rhs in controller.point_list) {

            // apply for all points:
            for (lhs in controller.point_list) {
                if (lhs.id != rhs.id) {
                    // apply force on rhs:
                    rhs.repel(lhs.pos)
                }
            }

            // apply from walls:
            val wall1 = Vector2(rhs.pos.x, 0f)
            val wall2 = Vector2(rhs.pos.x, screen_y.toFloat())
            val wall3 = Vector2(screen_x.toFloat(), rhs.pos.y)
            val wall4 = Vector2(0f, rhs.pos.y)
            rhs.repel(wall1)
            rhs.repel(wall2)
            rhs.repel(wall3)
            rhs.repel(wall4)

            if (rhs.pos.x < 0) {
                rhs.pos.x = 0.1f;
                rhs.velocity.x = max(rhs.velocity.x, 0f)
            }
            if (rhs.pos.y < 0) {
                rhs.pos.y = 0.1f;
                rhs.velocity.y = max(rhs.velocity.y, 0f)
            }
            if (rhs.pos.x > screen_x) {
                rhs.pos.x = screen_x - 0.1f;
                rhs.velocity.x = min(rhs.velocity.x, 0f)
            }
            if (rhs.pos.y > screen_y) {
                rhs.pos.y = screen_y - 0.1f;
                rhs.velocity.y = min(rhs.velocity.y, 0f)
            }
        }

        if (launcher_mode != LauncherMode.Canvas) {
            for (point in controller.point_list) {
                point.velocity.scl(0.997f)
                point.pos.add(point.velocity)
            }
        }

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.rect(0f, 0f, screen_x.toFloat(), screen_y.toFloat());
        shapeRenderer.end();
        shaderProgram.end()

        batch.end()

        if (!clicked) {
            batch.begin()
            font.setColor(1f, 1f, 1f, 0.5f)
            font.draw(batch, "Click to start!", screen_x/2f - 280, screen_y/2f)
            font.data.setScale(7f)
            batch.end()
        }
    }

    override fun dispose() {
        batch.dispose()
//        img.dispose()
    }


}