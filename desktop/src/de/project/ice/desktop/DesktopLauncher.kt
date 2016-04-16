package de.project.ice.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import de.project.ice.IceGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.fullscreen = true
        config.width = 1920
        config.height = 1080
        LwjglApplication(IceGame(), config)
    }
}
