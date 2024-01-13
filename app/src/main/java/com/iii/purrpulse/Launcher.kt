package com.iii.purrpulse

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.iii.purrpulse.gdx_stuff.LibGdxDemo

enum class LauncherMode {
    None, Balls, Tesselation, Flames,
}

var luancher_mode = LauncherMode.None

class Launcher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        if (luancher_mode == LauncherMode.Tesselation) {
            initialize(LibGdxDemo(), config)
        }
        else if (luancher_mode == LauncherMode.Flames) {
            initialize(LibGdxDemo(), config)
        }
        else if (luancher_mode == LauncherMode.Balls) {
            initialize(LibGdxDemo(), config)
        }
    }
}