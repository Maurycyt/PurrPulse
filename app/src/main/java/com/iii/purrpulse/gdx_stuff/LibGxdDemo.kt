package com.iii.purrpulse.gdx_stuff


import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2

class Point(var pos: Vector2, val id: Int) {
    var velocity: Vector2 = Vector2(0f, 0f)
}

class MyController : InputProcessor {
    var touching: Boolean = false

    var point_list: ArrayDeque<Point> = ArrayDeque()
    private var nextPointId = 0
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
        if (point_list.size > 10) {
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

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()

        controller = MyController()
        Gdx.input.inputProcessor = controller

        shapeRenderer = ShapeRenderer()
    }

    var count = 0;

    override fun render() {
        val screen_y =  Gdx.graphics.getHeight()

        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
//        if (font.scaleX < 30) {
//            font.data.scale(1.1f)
//        }
//        if (controller.touching) {
//            font.data.setScale(1f)
//        }

        // physics:
        for (lhs in controller.point_list) {
            for (rhs in controller.point_list) {
                if (lhs.id != rhs.id) {
                    // apply force on rhs:
                    val direction = rhs.pos.cpy().sub(lhs.pos).nor()
                    val distance2 = direction.len2() + 0.1f

                    rhs.velocity.add(direction.scl((1f / distance2) / 10f))


                }
            }
        }

        for (point in controller.point_list) {
            point.pos.add(point.velocity)

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.circle(point.pos.x, screen_y - point.pos.y, 10f);
            shapeRenderer.end();
        }

//        font.draw(batch, "Hello", 100f, 40f)
//        font.draw(batch, "Hello", controller.posX, Gdx.graphics.getHeight()-controller.posY)
        batch.end()

        count += 1
        if (count > 1000) {
            count = 0
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        batch.dispose()
//        img.dispose()
    }


}