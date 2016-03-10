package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.hotspot.Use

class Teapot: Use.Take("teekanne", "Teapot") {
    override fun take(game: IceGame) {
        if (Storage.SAVESTATE.getBoolean("scene_03_tea_ready")) {
            super.take(game)
        }
    }

    override fun look(game: IceGame) {
        if (Storage.SAVESTATE.getBoolean("scene_03_tea_ready")) {
            game.showMessages("s3_tea_not_ready")
        } else {
            game.showMessages("s3_tea_ready")
        }
    }
}

