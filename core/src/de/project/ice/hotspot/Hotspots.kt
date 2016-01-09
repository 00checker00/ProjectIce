package de.project.ice.hotspot


import com.badlogic.gdx.utils.ObjectMap
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.ecs.components.InvisibilityComponent

import de.project.ice.screens.CursorScreen.Cursor

object Hotspots {
    val hotspots = ObjectMap<String, Hotspot>()

    operator fun get(id: String): Hotspot? {
        var hotspot: Hotspot? = hotspots.get(id, null)
        if (hotspot == null) {
            hotspot = Hotspot("invalid_id")
        }
        return hotspot
    }

    private fun addHotspot(hotspot: Hotspot): Hotspot {
        hotspots.put(hotspot.id, hotspot)
        return hotspot
    }

    fun loadAllHotspots() {
        addHotspot(
                Hotspot.Builder().id("Oven").primaryCursor(Cursor.Look).use(object : Use.Adapter() {
                    override fun look(game: IceGame) {
                        game.showMessages(game.strings.get("s3_oven_no_wood"))
                    }
                }).useWith("Wood", object : Use.With() {
                    override fun use(game: IceGame) {
                        game.inventory.removeItem("Wood")
                        val fire = game.engine.getEntityByName("oven_fire")
                        fire?.remove(InvisibilityComponent::class.java)
                        Storage.SAVESTATE.put("scene_03_oven_fire", true)
                    }
                }).build())
        addHotspot(
                Hotspot.Builder().id("PrincessIgloo").primaryCursor(Cursor.Speak).secondaryCursor(Cursor.Look).use(object : Use.Adapter() {
                    override fun speak(game: IceGame) {
                        game.showDialog("s2_dlg_nathan")
                    }

                    override fun look(game: IceGame) {
                        game.showMessages(game.strings.get("s3_princess_desc"))
                    }
                }).useWith("Teapot", object : Use.With() {
                    override fun use(game: IceGame) {
                        Storage.SAVESTATE.put("PrincessGiveTea", true).flush()
                        game.showDialog("PrincessIgloo")
                        game.inventory.removeItem("Teapot")
                        game.engine.controlSystem.active_item = null
                    }
                }).build())
        addHotspot(
                Hotspot.Builder().id("Teapot").primaryCursor(Cursor.Take).secondaryCursor(Cursor.Look).use(object : Use.Take("teekanne", "Teapot") {

                    override fun take(game: IceGame) {
                        if (Storage.SAVESTATE.getBoolean("scene_03_tea_ready")) {
                            super.take(game)
                        }
                    }

                    override fun look(game: IceGame) {
                        if (Storage.SAVESTATE.getBoolean("scene_03_tea_ready")) {
                            game.showMessages(game.strings.get("s3_tea_not_ready"))
                        } else {
                            game.showMessages(game.strings.get("s3_tea_ready"))
                        }
                    }
                }).build())
        addHotspot(
                Hotspot.Builder().id("Wood").primaryCursor(Cursor.Take).secondaryCursor(Cursor.Look).use(object : Use.Take("wood", "Wood") {
                    override fun look(game: IceGame) {
                        game.showMessages(game.strings.get("s3_wood_desc"))
                    }
                }).build())
    }

    init {
        // TODO: background loading
        loadAllHotspots()
    }
}
