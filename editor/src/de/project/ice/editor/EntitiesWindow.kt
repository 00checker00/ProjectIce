package de.project.ice.editor

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisList
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.CopyableIceComponent
import de.project.ice.ecs.components.IceComponent
import de.project.ice.editor.undoredo.*


class EntitiesWindow @Throws(IllegalStateException::class)
constructor(private val app: EditorApplication, private val undoRedoManager: UndoRedoManager) : VisWindow("Entities") {
    private val entities: ImmutableArray<Entity> = app.engine.entities
    private val currentEntities = Array<EntityEntry>()
    private var entityList: VisList<EntityEntry>? = null
    private var selectedEntry: EntityEntry? = null
    private var selectionListener: SelectionListener? = null

    init {
        TableUtils.setSpacingDefaults(this)
        createWidgets()

        setPosition(0f, 0f, Align.topRight)
        isResizable = true
        setSize(200f, 400f)
    }

    fun setSelectionListener(selectionListener: SelectionListener) {
        this.selectionListener = selectionListener
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (entitiesChanged()) {
            updateEntities()
        }
        if (selectedEntry !== entityList!!.selected) {
            selectedEntry = entityList!!.selected
            if (selectionListener != null) {
                selectionListener!!.selectionChanged(if (selectedEntry == null) null else selectedEntry!!.entity)
            }

        }
    }

    private fun entitiesChanged(): Boolean {
        // Entities amount changed => we definitely need to update
        if (entities.size() != currentEntities.size) {
            return true
        }

        for (i in 0..entities.size() - 1) {
            if (entities.get(i) !== currentEntities.get(i).entity) {
                return true
            } else if (currentEntities.get(i).name != EntityEntry.generateName(entities.get(i))) {
                currentEntities.get(i).name = EntityEntry.generateName(entities.get(i))
            }
        }

        return false
    }

    private fun updateEntities() {
        currentEntities.clear()

        for (entity in entities) {
            currentEntities.add(EntityEntry(entity))
        }

        entityList!!.setItems(currentEntities)
    }

    private fun createWidgets() {

        val createEntityBtn = VisTextButton("Create", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                undoRedoManager.addAction(ModifySceneAction.record(app.engine, app.sceneProperties) {
                    it.addEntity(it.createEntity())
                })
                updateEntities()
                entityList!!.selectedIndex = entityList!!.items.size - 1
            }
        })
        add(createEntityBtn).expandX().fill()
        val duplicateEntityBtn = VisTextButton("Duplicate", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (entityList!!.selected != null) {
                    val entity = entityList!!.selected.entity
                    undoRedoManager.addAction(ModifySceneAction.record(app.engine, app.sceneProperties) {
                        val duplicate = it.createEntity()
                        for (component in entity.components) {
                            val iceComponent = component as IceComponent

                            if (iceComponent is CopyableIceComponent) {
                                val dupeComponent = it.createComponent(iceComponent.javaClass)

                                iceComponent.copyTo(dupeComponent)

                                duplicate.add(dupeComponent)
                            }
                        }
                        it.addEntity(duplicate)
                    })
                }
            }
        })
        add(duplicateEntityBtn).expandX().fill()
        val deleteEntityBtn = VisTextButton("Delete", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (entityList!!.selected != null) {
                    val entity = entityList!!.selected.entity
                    undoRedoManager.addAction(ModifySceneAction.record(app.engine, app.sceneProperties) {
                        it.removeEntity(entity)
                    })
                }
            }
        })
        add(deleteEntityBtn).expandX().fill().row()


        entityList = VisList<EntityEntry>()
        entityList!!.width = java.lang.Float.MAX_VALUE
        updateEntities()

        val scrollPane = VisScrollPane(entityList)

        row().expand()
        add(scrollPane).colspan(3).fill()
    }

    interface SelectionListener {
        fun selectionChanged(entity: Entity?)
    }
}