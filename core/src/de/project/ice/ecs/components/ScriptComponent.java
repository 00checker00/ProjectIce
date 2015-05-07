package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.scripting.ScriptManager;
import org.jetbrains.annotations.Nullable;

public class ScriptComponent extends Component implements Pool.Poolable {
    @Nullable
    public ScriptManager.Script script = null;

    @Override
    public void reset () {
        script = null;
    }
}
