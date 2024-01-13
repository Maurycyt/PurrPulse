package com.iii.purrpulse

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.iii.purrpulse.gdx_stuff.LibGdxDemo

enum class LauncherMode {
    None, Balls, Tesselation, Canvas, Flames,
}

var launcher_mode = LauncherMode.None

class Launcher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        if (launcher_mode == LauncherMode.Tesselation) {
            initialize(LibGdxDemo(), config)
        }
        else if (launcher_mode == LauncherMode.Flames) {
            initialize(LibGdxDemo(), config)
        }
        else if (launcher_mode == LauncherMode.Balls) {
            initialize(LibGdxDemo(), config)
        }
        else if (launcher_mode == LauncherMode.Canvas) {
            initialize(LibGdxDemo(), config)
        }
    }
}