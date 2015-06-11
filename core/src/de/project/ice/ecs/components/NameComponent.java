package de.project.ice.ecs.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class NameComponent extends Component implements Pool.Poolable {
    public String name = "";

    @Override
    public void reset() {
        name = "";
    }
}
