package de.project.ice.editor

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.*
import de.project.ice.ecs.components.*
import de.project.ice.editor.undoredo.ModifySceneAction
import de.project.ice.editor.undoredo.UndoRedoManager


class ComponentsWindow @Throws(IllegalStateException::class)
constructor(private val app: EditorApplication, private val undoRedoManager: UndoRedoManager) : VisWindow("Components") {

    private var entity: Entity? = null
    private var componentsTable: VisTable? = null

    init {

        TableUtils.setSpacingDefaults(this)
        createWidgets()

        setPosition(0f, 0f, Align.bottomRight)
        isResizable = true
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    private fun createWidgets() {
        componentsTable = VisTable()
        componentsTable!!.defaults().align(Align.topLeft)


        val menu = PopupMenu()

        for (component in components) {
            val item = MenuItem(component.simpleName, object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    undoRedoManager.addAction(ModifySceneAction.record(app.engine, app.sceneProperties) {
                        entity?.add(it.createComponent(component))
                    })
                    updateEntity()
                }
            })
            menu.addItem(item)
        }

        val btnAddComponent = VisTextButton("Add component", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val stageCoord = localToStageCoordinates(Vector2(actor.x, actor.y))
                menu.showMenu(stage, stageCoord.x, stageCoord.y)
            }
        })

        add(btnAddComponent).expandX().fill().row()
        val scrollPane = VisScrollPane(componentsTable)
        row().expand()
        add(scrollPane).fill()
    }

    fun setEntity(entity: Entity?) {
        this.entity = entity
        updateEntity()
    }

    private fun updateEntity() {
        componentsTable!!.clear()

        entity?.let {
            val entity = it
            val name = EntityEntry(entity).name
            titleLabel.setText("Components of " + name)

            for (component in entity.components.sortedBy { it.javaClass.simpleName }) {
                val componentName = component.javaClass.simpleName

                val table = ComponentTable(component)
                val tableWrapper = CollapsibleWidget(table, true)


                val collapseButton = VisTextButton("+", object : ChangeListener() {
                    override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                        tableWrapper.isCollapsed = !tableWrapper.isCollapsed
                        if (actor is VisTextButton) {
                            actor.setText(if (tableWrapper.isCollapsed) "+" else "-")
                        }
                    }
                })
                componentsTable!!.add(collapseButton).expand(false, false)


                val label = VisLabel(componentName)
                componentsTable!!.add(label).expandX()

                val removeBtn = VisTextButton("-", object : ChangeListener() {
                    override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                        undoRedoManager.addAction(ModifySceneAction.record(app.engine, app.sceneProperties) {
                            entity.remove(component.javaClass)
                        })
                        updateEntity()
                    }
                })
                componentsTable!!.add(removeBtn).padRight(20f)

                componentsTable!!.row()
                componentsTable!!.add(tableWrapper).padLeft(10f).colspan(3).row()
            }
            componentsTable!!.row()
            componentsTable!!.add(VisLabel("")).expand()
        } ?: titleLabel.setText("Components")

    }

    companion object {
        var components: Array<Class<out Component>> = arrayOf(
                AndiComponent::class.java,
                AnimationComponent::class.java,
                BlendComponent::class.java,
                BreathComponent::class.java,
                CameraComponent::class.java,
                ControlComponent::class.java,
                DistanceScaleComponent::class.java,
                HotspotComponent::class.java,
                IdleComponent::class.java,
                InvisibilityComponent::class.java,
                MoveComponent::class.java,
                NameComponent::class.java,
                ScriptComponent::class.java,
                TextureComponent::class.java,
                TransformComponent::class.java,
                WalkAreaComponent::class.java,
                WalkingComponent::class.java
        )
    }
}