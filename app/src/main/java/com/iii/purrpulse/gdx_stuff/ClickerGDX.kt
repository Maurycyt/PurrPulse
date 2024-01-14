package com.iii.purrpulse.gdx_stuff


import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class ClickerController : InputProcessor {
    var clicks = 0
    var just_clicked = false
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
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
        clicks += 1
        just_clicked = true
        return false
    }
}


class ClickerGDX : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var controller: ClickerController


    override fun create() {

        font = BitmapFont()

        batch = SpriteBatch()

        font = BitmapFont()
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        controller = ClickerController()
        Gdx.input.inputProcessor = controller

        font.setColor(1f, 1f, 1f, 1f)
        font.data.setScale(30f)
    }


    override fun render() {
        val screen_y = Gdx.graphics.getHeight()
        val screen_x = Gdx.graphics.getWidth()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (controller.just_clicked) {
            controller.just_clicked = false
            font.data.setScale(40f)
        }
        if (font.data.scaleX > 30f) {
            font.data.setScale(font.data.scaleX * 0.93f)
        }

        val offset_x = font.data.spaceWidth

        batch.begin()
        val output = "${controller.clicks}"
        font.draw(batch, output, (screen_x / 2f) - (3.9f * font.data.scaleX * output.length), screen_y/2f + 5f * font.data.scaleX)
        batch.end()

    }

    override fun dispose() {
        batch.dispose()
    }


}