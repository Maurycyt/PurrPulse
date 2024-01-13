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

class Point(var posX: Float, var posY: Float) { }

class MyController : InputProcessor {
    var touching: Boolean = false

    var point_list: ArrayDeque<Point> = ArrayDeque()
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
        point_list.addFirst(Point(screenX.toFloat(), screenY.toFloat()))
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

        for (point in controller.point_list) {
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.circle(point.posX, screen_y - point.posY, 10f);
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