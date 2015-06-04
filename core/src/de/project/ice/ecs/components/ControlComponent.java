package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for controlling the main character via input (keyboard, mouse)
 */
public class ControlComponent extends Component implements Pool.Poolable {
    @Override
    public void reset(){
    }
}


