package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for controlling the main character via input (keyboard, mouse)
 */
public class ControlComponent extends Component implements Pool.Poolable {

    public boolean isMousePressed = false;
    public boolean isInput = false;

    @Override
    public void reset(){
        isMousePressed = false;
        isInput = false;
    }
}


