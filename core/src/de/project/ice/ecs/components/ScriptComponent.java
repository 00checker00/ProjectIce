package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.scripting.Script;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptComponent extends Component implements Pool.Poolable
{
    @Nullable
    public Script script = null;
    @NotNull
    public String scriptName = "";

    public void setScriptName(String scriptName)
    {
        scriptName = scriptName;
        script = null;
    }

    @Override
    public void reset()
    {
        script = null;
        scriptName = "";
    }
}
