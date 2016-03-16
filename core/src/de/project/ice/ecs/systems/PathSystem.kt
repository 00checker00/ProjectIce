package de.project.ice.ecs.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.utils.Array
import de.project.ice.ecs.Components
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.MoveComponent
import de.project.ice.ecs.components.PathPlanningComponent
import de.project.ice.ecs.components.WalkAreaComponent
import de.project.ice.pathlib.*
import de.project.ice.ecs.getComponent

class PathSystem : IceSystem() {
    private var walkareaEntities = ImmutableArray(Array<Entity>(0))
    private var pathplanningEntities = ImmutableArray(Array<Entity>(0))
    private val pathCalculator = PathCalculator(0.01f)


    @SuppressWarnings("unchecked")
    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
        walkareaEntities = engine.getEntitiesFor(Family.all(WalkAreaComponent::class.java).get())
        pathplanningEntities = engine.getEntitiesFor(Family.all(PathPlanningComponent::class.java).get())
    }

    override fun removedFromEngine(engine: IceEngine) {
        super.removedFromEngine(engine)
        walkareaEntities = ImmutableArray(Array<Entity>(0))
        pathplanningEntities = ImmutableArray(Array<Entity>(0))
    }

    override fun update(deltaTime: Float) {
        if (walkareaEntities.size() == 0 || pathplanningEntities.size() == 0) {
            return
        }

        val walkarea = Components.walkarea.get(walkareaEntities.first()).area

        val waypoints = Array<PathNode>(pathplanningEntities.size() * 2)
        for (i in 0..pathplanningEntities.size() - 1) {
            val pathPlanningComponent = Components.pathplanning.get(pathplanningEntities.get(i))
            waypoints.addAll(PathNode(pathPlanningComponent.start), PathNode(pathPlanningComponent.target))
        }

        val graph = pathCalculator.computeGraph(walkarea, waypoints)

        val astar = IndexedAStarPathFinder(graph)

        val result = DefaultGraphPath<PathNode>()
        for (i in 0..pathplanningEntities.size() - 1) {
            val entity = pathplanningEntities.get(i)
            val pathPlanningComponent = entity.getComponent(Components.pathplanning)
            astar.searchNodePath(waypoints.get(2 * i), waypoints.get(2 * i + 1), PathHeuristic(), result)
            val moveComponent = engine!!.createComponent(MoveComponent::class.java)
            moveComponent.callback = pathPlanningComponent.callback
            moveComponent.speed = pathPlanningComponent.speed
            entity.remove(PathPlanningComponent::class.java)
            entity.add(moveComponent)

            for (node in result) {
                moveComponent.targetPositions.add(node.pos.cpy())
            }

            result.clear()
        }
    }

    val walkArea: PathArea
        get() {
            if (walkareaEntities.size() > 0) {
                return Components.walkarea.get(walkareaEntities.first()).area
            }
            return PathArea()
        }
}
