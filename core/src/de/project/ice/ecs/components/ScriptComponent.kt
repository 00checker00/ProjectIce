package de.project.ice.ecs.components

import de.project.ice.annotations.Property
import de.project.ice.scripting.Script

class ScriptComponent : CopyableIceComponent {
    @Property("The name if the Script (Classname)")
    var script: Script? = null
    var scriptName = ""
        set(scriptName) {
            field = scriptName
            script = null
        }

    override fun reset() {
        script = null
        scriptName = ""
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is ScriptComponent) {
            copy.scriptName = scriptName
        }
    }
}
