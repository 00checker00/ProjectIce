package de.project.ice.scripting

import de.project.ice.utils.FileClassLoader

class ScriptLoader(parent: ClassLoader = ClassLoader.getSystemClassLoader()): FileClassLoader(parent) {
    override val prefix: String = "de.project.ice.scripting.scripts."
    override val path: String = "scripts"
}