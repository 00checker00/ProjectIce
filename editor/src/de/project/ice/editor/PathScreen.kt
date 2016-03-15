package de.project.ice.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import de.project.ice.IceGame
import de.project.ice.pathlib.PathArea
import de.project.ice.pathlib.Shape
import de.project.ice.screens.BaseScreenAdapter

class PathScreen(game: IceGame) : BaseScreenAdapter(game) {

    private var pathArea: PathArea
    override val inputProcessor: InputProcessor
    private var camera: Camera? = null
    private val shapeRenderer: FilledPolygonShapeRenderer
    private var highlight: Vector2? = null
    private var selection: Vector2? = null
    private var mouseDown: Vector2? = null
    private var insertPos: Vector2? = null
    private var insertIndex = 0


    init {

        pathArea = PathArea()
        pathArea.shape = Shape()

        shapeRenderer = FilledPolygonShapeRenderer()

        inputProcessor = object : InputAdapter() {
            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                insertPos = null
                if (camera != null && game.isGamePaused) {
                    val pos = camera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

                    highlight = pointAtPos(Vector2(pos.x, pos.y))

                    if (highlight == null) {
                        for (i in 0..pathArea.shape.vertices.size - 1) {
                            val cur = pathArea.shape.vertices.get(i)
                            val next = pathArea.shape.vertices.get((i + 1) % pathArea.shape.vertices.size)

                            val dir = next.cpy().sub(cur).nor()
                            val rot = dir.cpy().rotate90(0).scl(epsilon)

                            val bounds = Array(arrayOf(cur.cpy().add(rot), cur.cpy().sub(rot), next.cpy().sub(rot), next.cpy().add(rot)))

                            if (Intersector.isPointInPolygon(bounds, Vector2(pos.x, pos.y))) {
                                insertPos = cur.cpy().add(dir.cpy().scl(Vector2(pos.x, pos.y).sub(cur).len()))
                                insertIndex = i + 1
                                break
                            }
                        }
                    }
                }

                return false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                selection = null
                if (camera != null && game.isGamePaused) {
                    val pos = camera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

                    mouseDown = Vector2(pos.x, pos.y)

                    val pointUnderMouse = pointAtPos(Vector2(pos.x, pos.y))

                    if (button == Input.Buttons.LEFT) {
                        selection = pointUnderMouse
                    } else if (button == Input.Buttons.RIGHT) {
                        if (insertPos != null) {
                            pathArea.shape.vertices.insert(insertIndex, insertPos)
                            insertPos = null
                        } else if (pointUnderMouse == null) {
                            pathArea.shape.vertices.add(Vector2(pos.x, pos.y))
                        } else {
                            pathArea.shape.vertices.removeValue(pointUnderMouse, true)
                            highlight = null
                        }
                        pathArea.shape.closed = pathArea.shape.vertices.size >= 3
                    }

                    if (pointUnderMouse != null) {
                        return false
                    }
                }
                return false
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                if (selection != null && game.isGamePaused) {
                    val pos = camera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                    selection!!.set(pos.x, pos.y)
                    return true
                }
                return false
            }
        }

    }

    private fun pointAtPos(pos: Vector2): Vector2? {
        for (vector in pathArea.shape.vertices) {
            if (pos.x >= vector.x - epsilon && pos.x <= vector.x + epsilon && pos.y >= vector.y - epsilon && pos.y <= vector.y + epsilon) {
                return vector
            }
        }
        return null
    }

    override val priority: Int
        get() = 2

    override fun render() {
        if ((camera == null)) {
            return
        }

        val blendEnabled = Gdx.gl.glIsEnabled(GL20.GL_BLEND)

        if (!blendEnabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)

        if (pathArea.shape.vertices.size > 0) {
            shapeRenderer.projectionMatrix = camera!!.combined

            if (pathArea.shape.closed) {
                shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Filled)

                shapeRenderer.color = Color(0f, 0f, 1f, 0.1f)

                val vertices = FloatArray(pathArea.shape.vertices.size * 2)
                for (i in 0..pathArea.shape.vertices.size - 1) {
                    val vector = pathArea.shape.vertices.get(i)
                    vertices[2 * i] = vector.x
                    vertices[2 * i + 1] = vector.y
                }
                shapeRenderer.polygon(vertices)
                shapeRenderer.end()
            }

            var previous = pathArea.shape.vertices.first()

            shapeRenderer.color = Color.CYAN
            shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Line)
            for (vector in pathArea.shape.vertices) {
                shapeRenderer.line(previous.x, previous.y, vector.x, vector.y)

                previous = vector
            }
            shapeRenderer.end()

            shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Filled)
            for (vector in pathArea.shape.vertices) {
                shapeRenderer.circle(vector.x, vector.y, 0.05f, 10)
            }

            if (highlight != null) {
                val colorBefore = shapeRenderer.color.cpy()
                shapeRenderer.color = Color.RED
                shapeRenderer.circle(highlight!!.x, highlight!!.y, 0.05f, 10)
                shapeRenderer.color = colorBefore
            }

            if (insertPos != null) {
                val colorBefore = shapeRenderer.color.cpy()
                shapeRenderer.color = Color.GREEN
                shapeRenderer.circle(insertPos!!.x, insertPos!!.y, 0.05f, 10)
                shapeRenderer.color = colorBefore
            }

            shapeRenderer.end()

            if (pathArea.shape.closed) {
                shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Line)
                val first = pathArea.shape.vertices.first()
                val last = pathArea.shape.vertices.get(pathArea.shape.vertices.size - 1)

                shapeRenderer.line(last.x, last.y, first.x, first.y)
                shapeRenderer.end()
            }
        }

        if (!blendEnabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

    }

    override fun update(delta: Float) {
        camera = game.engine.renderingSystem.activeCamera?.camera
        pathArea = game.engine.pathSystem.walkArea
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
    }

    companion object {
        private val epsilon = 0.05f
    }
}