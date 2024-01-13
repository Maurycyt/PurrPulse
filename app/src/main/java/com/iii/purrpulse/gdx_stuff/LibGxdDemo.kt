package com.iii.purrpulse.gdx_stuff


import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
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

val point_count: Int = 30

var position_array = FloatArray(point_count * 2);
var color_array = FloatArray(point_count * 3)

fun fragment_shader() =
    """
    #ifdef GL_ES
    precision mediump float;
    #endif
    
    const int n = ${point_count};
    
    uniform vec2 u_positions[n];
    uniform vec3 u_colors[n];
            
    void main() {
        vec2 position = gl_FragCoord.xy;
        float total_dist = 0.0;
        vec3 final_color = vec3(0.1, 0.1, 0.1);
        for (int i = 0; i < n; i++) {
            total_dist += 1.0 / (1.0 + distance(u_positions[i], position));
        }
        total_dist *= 1.25;
        for (int i = 0; i < n; i++) {
            float dist = 1.0 / (1.0 + distance(u_positions[i], position));
            final_color += u_colors[i] * (dist/total_dist);
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
        gl_Position =  u_projModelView * ${ShaderProgram.POSITION_ATTRIBUTE};
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
    var color: Vector3 = Vector3(MathUtils.random(), MathUtils.random(), MathUtils.random())
    fun repel(from: Vector2) {
        var direction = pos.cpy().sub(from)
        val distance2 = direction.len2()/1000f + 0.1f

        direction.nor()
        velocity.add(direction.scl((1f / distance2)))
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
        touching = true
        point_list.addFirst(Point(Vector2(screenX.toFloat(), screenY.toFloat()), nextPointId))
        nextPointId += 1
        if (point_list.size > point_count) {
            point_list.removeLast()
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
        batch = SpriteBatch()
        font = BitmapFont()

        controller = MyController()
        Gdx.input.inputProcessor = controller

//        ShaderProgram.pedantic = false
        shaderProgram = ShaderProgram(vertex_shader(), fragment_shader())


        for (i in 0..point_count-1) {
            controller.point_list.addFirst(Point(Vector2(i.toFloat()*7, i.toFloat()*7), controller.nextPointId))
            controller.nextPointId += 1
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

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shaderProgram.begin();

        // set shader input:
        for (i in 0..point_count - 1) {
            position_array[i*2 + 0] = controller.point_list.get(i).pos.x;
            position_array[i*2 + 1] = screen_y - controller.point_list.get(i).pos.y;
            color_array[i*3 + 0] = controller.point_list.get(i).color.x;
            color_array[i*3 + 1] = controller.point_list.get(i).color.y;
            color_array[i*3 + 2] = controller.point_list.get(i).color.z;
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
        }

        shapeRenderer.begin(ShapeType.Filled);

        for (point in controller.point_list) {
            point.velocity.scl(0.997f)
            point.pos.add(point.velocity)

            // shapeRenderer.setColor(point.color.x, point.color.y, point.color.z, 1.0f);

//            shapeRenderer.circle(point.pos.x, screen_y - point.pos.y, 15f);

        }

        shapeRenderer.rect(0f, 0f, screen_x.toFloat(), screen_y.toFloat());


        shapeRenderer.end();
        batch.end()
        shaderProgram.end()

    }

    override fun dispose() {
        batch.dispose()
//        img.dispose()
    }


}