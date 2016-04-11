package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import de.project.ice.config.Config
import de.project.ice.ecs.components.*
import de.project.ice.utils.Assets

import java.util.Comparator

import de.project.ice.config.Config.PIXELS_TO_METRES
import de.project.ice.config.Config.RENDER_DEBUG
import de.project.ice.ecs.*

class RenderingSystem : SortedIteratingIceSystem(Families.renderable, RenderingSystem.RenderingComparator()) {
    private var hotspots: ImmutableArray<Entity>? = null
    private var walkareas: ImmutableArray<Entity>? = null

    private var cameras: ImmutableArray<Entity>? = null
    var activeCamera: CameraComponent? = null
        private set
    var activeCameraEntity: Entity? = null
        private set

    private val batch: SpriteBatch
    private val debugRenderer: ShapeRenderer

    init {
        debugRenderer = ShapeRenderer()

        this.batch = SpriteBatch()
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        cameras = engine.getEntitiesFor(Families.camera)
    }

    override fun update(deltaTime: Float) {
        // update the active camera
        if (cameras!!.size() > 0) {
            activeCameraEntity = cameras!!.first()
            activeCamera = activeCameraEntity?.getComponent(CameraComponent::class.java)
        }

        if (activeCamera == null) {
            return
        }

        activeCamera?.camera?.update()
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        batch.projectionMatrix = activeCamera!!.camera.combined
        batch.begin()
        batch.color = activeCamera!!.color
        super.update(deltaTime)

        for (hotspot in hotspots!!) {
            renderHotspot(hotspot)
        }
        batch.color = Color.WHITE
        batch.end()

        if (RENDER_DEBUG) {
            debugRenderer.projectionMatrix = activeCamera!!.camera.combined
            debugRenderer.begin(ShapeRenderer.ShapeType.Line)
            debugRenderer.color = Color.RED
            super.update(deltaTime)
            debugRenderer.color = Color.BLUE
            for (hotspot in hotspots!!) {
                renderHotspot(hotspot)
            }
            //            if (walkareas.size() > 0) {
            //                WalkAreaComponent component = Components.walkarea.get(walkareas.first());
            //                PathArea area = component.getArea();
            //
            //                debugRenderer.setColor(Color.PURPLE);
            //
            //                if (area != null && area.shape != null) {
            //
            //                    for (int i = 1; i < area.shape.vertices.size; i++)
            //                        debugRenderer.line(
            //                                area.shape.vertices.get(i).x,
            //                                area.shape.vertices.get(i).y,
            //                                area.shape.vertices.get(i - 1).x,
            //                                area.shape.vertices.get(i - 1).y);
            //
            //                    debugRenderer.line(
            //                            area.shape.vertices.get(0).x,
            //                            area.shape.vertices.get(0).y,
            //                            area.shape.vertices.get(area.shape.vertices.size - 1).x,
            //                            area.shape.vertices.get(area.shape.vertices.size - 1).y);
            //
            //                    for (Shape hole : area.holes) {
            //                        for (int i = 1; i < hole.vertices.size; i++)
            //                            debugRenderer.line(
            //                                    hole.vertices.get(i).x,
            //                                    hole.vertices.get(i).y,
            //                                    hole.vertices.get(i - 1).x,
            //                                    hole.vertices.get(i - 1).y);
            //
            //                        debugRenderer.line(
            //                                hole.vertices.get(0).x,
            //                                hole.vertices.get(0).y,
            //                                hole.vertices.get(hole.vertices.size - 1).x,
            //                                hole.vertices.get(hole.vertices.size - 1).y);
            //                    }
            //                }
            //
            //            }
            debugRenderer.color = Color.GRAY
            debugRenderer.line(0f, -1f, 0f, 1f)
            debugRenderer.line(-1f, 0f, 1f, 0f)
            debugRenderer.end()
        }
        forceSort()
    }

    private fun renderHotspot(entity: Entity) {
        val transform = Components.transform.get(entity)
        val hotspot = Components.hotspot.get(entity)

        val pos = transform.pos.cpy().add(hotspot.origin)
        val origin = pos.cpy().add(hotspot.origin)
        val center = origin.cpy().add(hotspot.width / 2, hotspot.height / 2)

        if (batch.isDrawing && Gdx.input.isKeyPressed(Config.HOTSPOT_KEY)) {
            val region = Assets.findRegion("icon_hotspot")
            if (region.isValid)
                batch.draw(region.data, center.x - 0.25f, center.y - 0.25f, 0.5f, 0.5f)
        } else if (debugRenderer.isDrawing) {
            debugRenderer.rect(origin.x, origin.y, hotspot.width, hotspot.height)
            cross(origin)
            cross(pos.cpy().sub(hotspot.origin).add(hotspot.targetPos).add(hotspot.origin))
        }
    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val tex = Components.texture.get(entity)
        val t = Components.transform.get(entity)

        if (debugRenderer.isDrawing && !Components.texture.has(entity)) {
            cross(t.pos)
        }

        if (tex?.region?.data == null) {
            return
        }

        var scaleX = t.scale.x
        var scaleY = t.scale.y

        if (entity.hasComponent(Components.distanceScale)) {
            val scale = entity.getComponent(Components.distanceScale).currentScale
            scaleY *= scale
            scaleX *= scale
        }

        if (Components.breath.has(entity)) {
            val breath = Components.breath.get(entity)
            scaleX += scaleX * breath.curScale.x
            scaleY += scaleY * breath.curScale.y
        }


        val width = tex.region.data!!.regionWidth.toFloat() * PIXELS_TO_METRES * scaleX
        val height = tex.region.data!!.regionHeight.toFloat() * PIXELS_TO_METRES * scaleY
        val originX = width * 0.5f
        val originY = height * 0.5f

        if (batch.isDrawing) {

            // draw the sprite in accordance with all calculated data (above)
            batch.draw(tex.region.data,
                    t.pos.x - width/2, t.pos.y,
                    originX, originY,
                    width, height,
                    if (t.flipHorizontal) -1f else 1f, if (t.flipVertical) -1f else 1f,
                    MathUtils.radiansToDegrees * t.rotation)

        }
    }

    private fun cross(pos: Vector2) {
        debugRenderer.line(pos.x - 0.1f, pos.y, pos.x + 0.1f, pos.y)
        debugRenderer.line(pos.x, pos.y - 0.1f, pos.x, pos.y + 0.1f)
    }

    override fun addedToEngine(engine: IceEngine) {
        hotspots = engine.getEntitiesFor(Families.hotspot)
        walkareas = engine.getEntitiesFor(Families.walkArea)
        super.addedToEngine(engine)
    }

    class RenderingComparator : Comparator<Entity> {
        override fun compare(entityA: Entity, entityB: Entity): Int {
            val z = Math.signum((Components.transform.get(entityB).z - Components.transform.get(entityA).z).toFloat()).toInt()
            if (z == 0) {
                return Math.signum(Components.transform.get(entityB).pos.y - Components.transform.get(entityA).pos.y).toInt()
            } else {
                return z
            }
        }
    }

    companion object {
        /**
         * x-axis dimension from far left to far right
         */
        val FRUSTUM_WIDTH = 16f
        /**
         * y-axis dimension from bottom to top
         */
        val FRUSTUM_HEIGHT = 9f
    }
}
