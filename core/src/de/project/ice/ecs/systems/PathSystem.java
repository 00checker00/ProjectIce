package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.MoveComponent;
import de.project.ice.ecs.components.PathPlanningComponent;
import de.project.ice.ecs.components.WalkAreaComponent;
import de.project.ice.pathlib.*;
import org.jetbrains.annotations.NotNull;

public class PathSystem extends IceSystem
{
    @NotNull
    private ImmutableArray<Entity> walkareaEntities = new ImmutableArray<Entity>(new Array<Entity>(0));
    @NotNull
    private ImmutableArray<Entity> pathplanningEntities = new ImmutableArray<Entity>(new Array<Entity>(0));
    @NotNull
    private PathCalculator pathCalculator = new PathCalculator(0.01f);


    @SuppressWarnings("unchecked")
    @Override
    public void addedToEngine(IceEngine engine)
    {
        super.addedToEngine(engine);
        walkareaEntities = engine.getEntitiesFor(Family.all(WalkAreaComponent.class).get());
        pathplanningEntities = engine.getEntitiesFor(Family.all(PathPlanningComponent.class).get());
    }

    @Override
    public void removedFromEngine(IceEngine engine)
    {
        super.removedFromEngine(engine);
        walkareaEntities = new ImmutableArray<Entity>(new Array<Entity>(0));
        pathplanningEntities = new ImmutableArray<Entity>(new Array<Entity>(0));
    }

    @Override
    public void update(float deltaTime)
    {
        if (walkareaEntities.size() == 0 || pathplanningEntities.size() == 0)
        {
            return;
        }

        PathArea walkarea = Components.walkarea.get(walkareaEntities.first()).area;

        Array<PathNode> waypoints = new Array<PathNode>(pathplanningEntities.size() * 2);
        for (int i = 0; i < pathplanningEntities.size(); ++i)
        {
            PathPlanningComponent pathPlanningComponent = Components.pathplanning.get(pathplanningEntities.get(i));
            waypoints.addAll(new PathNode(pathPlanningComponent.start), new PathNode(pathPlanningComponent.target));
        }

        PathGraph graph = pathCalculator.computeGraph(walkarea, waypoints);

        IndexedAStarPathFinder<PathNode> astar = new IndexedAStarPathFinder<PathNode>(graph);

        GraphPath<PathNode> result = new DefaultGraphPath<PathNode>();
        for (int i = 0; i < pathplanningEntities.size(); ++i)
        {
            Entity entity = pathplanningEntities.get(i);
            astar.searchNodePath(waypoints.get(2 * i), waypoints.get(2 * i + 1), new PathHeuristic(), result);
            entity.remove(PathPlanningComponent.class);
            MoveComponent moveComponent = getEngine().createComponent(MoveComponent.class);
            entity.add(moveComponent);

            for (PathNode node : result)
            {
                moveComponent.targetPositions.add(node.getPos().cpy());
            }

            result.clear();
        }
    }

    @NotNull
    public PathArea getWalkArea()
    {
        if (walkareaEntities.size() > 0)
        {
            return Components.walkarea.get(walkareaEntities.first()).area;
        }
        return new PathArea();
    }
}
