package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.*
import de.project.ice.hotspot.Hotspot
import de.project.ice.inventory.Inventory
import de.project.ice.pathlib.PathCalculator
import de.project.ice.screens.CursorScreen
import de.project.ice.ecs.getComponent

import de.project.ice.config.Config.PIXELS_TO_METRES
import de.project.ice.utils.plus

class ControlSystem
@SuppressWarnings("unchecked")
constructor() : IteratingIceSystem(Families.controllable), InputProcessor {
    private var cameras: ImmutableArray<Entity>? = null
    private var hotspots: ImmutableArray<Entity>? = null
    private var controllables: ImmutableArray<Entity>? = null
    internal var active_camera: OrthographicCamera? = null
    internal var active_hotspot: Hotspot? = null
    var hotspot_entity: Entity? = null

    private val pointerPos = Vector2()
    private var pointerDown = false
    private var pointerWasDown = false
    private var mouseDown = false

    private val mouseCalculator = PathCalculator(0f)

    var primaryCursor: CursorScreen.Cursor = CursorScreen.Cursor.None
    var secondaryCursor: CursorScreen.Cursor = CursorScreen.Cursor.None
    var active_item: Inventory.Item? = null
    var cursorText: String = ""
    override fun setProcessing(processing: Boolean) {
        super.setProcessing(processing)
    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val texture = Components.texture.get(entity)
        val transform = Components.transform.get(entity)

        if (mouseDown) {
            entity.remove(UseComponent::class.java)
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && active_item != null) {
                    active_item = null
                }

                activateHotspot(entity)
                if (activeCursor!= CursorScreen.Cursor.None) {

                    var target: Vector2
                    if (hotspot_entity != null) {
                        val hotspotComponent = Components.hotspot.get(hotspot_entity)
                        target = Components.transform.get(hotspot_entity).pos + hotspotComponent.origin + hotspotComponent.targetPos
                    } else {
                        val mouse_target = active_camera!!.unproject(Vector3(pointerPos.x, pointerPos.y, 0f)) // unprojects UI coordinates to camera coordinates
                        target = Vector2(mouse_target.x, mouse_target.y)
                    }

                    val start = transform.pos.cpy()

                    val pathPlanningComponent = engine.createComponent(PathPlanningComponent::class.java)
                    pathPlanningComponent.target = target
                    pathPlanningComponent.start = start

                    entity.add(pathPlanningComponent)


                }
            }
        }
    }

    private fun activateHotspot(entity: Entity?) {
        if (active_hotspot != null) {
            val useComponent = engine!!.createComponent(UseComponent::class.java)
            useComponent.target = hotspot_entity
            useComponent.cursor = activeCursor
            useComponent.withItem = active_item
            entity?.add(useComponent)
        }
        active_item = null
    }

    private val activeCursor: CursorScreen.Cursor
        get() = if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || secondaryCursor === CursorScreen.Cursor.None) primaryCursor else secondaryCursor

    override fun update(deltaTime: Float) {
        // update the active camera
        if (cameras!!.size() > 0) {
            active_camera = Components.camera.get(cameras!!.first()).camera
        }

        if (active_camera == null) {
            return
        }

        if (pointerDown && !pointerWasDown) {
            mouseDown = true
        }

        super.update(deltaTime)

        if (controllables!!.size() == 0 && mouseDown && active_hotspot != null) {
            activateHotspot(hotspot_entity)
        }

        mouseDown = false
        pointerWasDown = pointerDown
    }

    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
        cameras = engine.getEntitiesFor(Families.camera)
        hotspots = engine.getEntitiesFor(Families.hotspot)
        controllables = engine.getEntitiesFor(Families.controllable)
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
        pointerPos.set(screenX.toFloat(), screenY.toFloat())
        pointerDown = true
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        pointerPos.set(screenX.toFloat(), screenY.toFloat())
        pointerDown = false
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        pointerPos.set(screenX.toFloat(), screenY.toFloat())
        primaryCursor = CursorScreen.Cursor.None
        secondaryCursor = CursorScreen.Cursor.None
        active_hotspot = null
        hotspot_entity = null

        if (active_camera != null) {
            val coords = active_camera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

            val walkarea = engine!!.pathSystem.walkArea
            if (mouseCalculator.IsInside(walkarea, Vector2(coords.x, coords.y))) {
                primaryCursor = CursorScreen.Cursor.Walk
            }

            val entity = hotspots?.filter {
                val transform = it.getComponent(Components.transform)
                val hotspot = it.getComponent(Components.hotspot)

                val pos = transform.pos + hotspot.origin
                val origin = pos + hotspot.origin

                if (Rectangle(origin.x, origin.y, hotspot.width, hotspot.height).contains(coords.x, coords.y)) {
                    return@filter true
                }
                return@filter false
            }?.sortedBy {
                it.getComponent(Components.transform)?.z?:0
            }?.firstOrNull()
            if (entity != null) {
                val hotspot = entity.getComponent(Components.hotspot)

                active_hotspot = Hotspot[hotspot.script]
                if (active_hotspot != null) {
                    val text: String
                    try {
                        text = engine.game.strings["${hotspot.cursorText}_text"]
                    } catch (ex: Exception) {
                        text = active_hotspot?.id ?: ""
                    }
                    hotspot_entity = entity
                    primaryCursor = hotspot.primaryCursor
                    secondaryCursor = hotspot.secondaryCursor
                    cursorText = text
                }
            } else {
                cursorText = ""
            }
        }
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }
}