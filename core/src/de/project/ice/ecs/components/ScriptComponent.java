package de.project.ice.ecs.components;

import de.project.ice.scripting.Script;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptComponent implements IceComponent
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
