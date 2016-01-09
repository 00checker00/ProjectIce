package de.project.ice.ecs.components

import de.project.ice.scripting.Script

class ScriptComponent : CopyableIceComponent {
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
