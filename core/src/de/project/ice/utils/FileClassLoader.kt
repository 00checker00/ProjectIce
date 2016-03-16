package de.project.ice.utils

import java.io.File
import java.util.*

abstract class FileClassLoader(parent: ClassLoader = ClassLoader.getSystemClassLoader()): ClassLoader(parent) {
    val loadedClass = HashMap<String, Class<*>>()

    override fun loadClass(name: String): Class<*> {
        return loadClass(name, false)
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        if (!name.startsWith(prefix))
            return super.loadClass(name, resolve)

        synchronized(getClassLoadingLock(name)) {

            val clazz = getLoadedClass(name) ?: findClass(name)

            if (resolve)
                resolveClass(clazz)

            return clazz
        }
    }

    override fun findClass(name: String): Class<*> {
        if (!name.startsWith(prefix))
            return super.findClass(name)

        val data = loadClassData(name.substring(prefix.length))
        return defineClass(name, data, 0, data.size).apply { loadedClass[name] = this }
    }

    private fun loadClassData(name: String): ByteArray {
        val file = File("$path/${name.replace(".", "/")}.class")

        if (!file.exists())
            throw ClassNotFoundException("Class for Hotspot \"$name\" not found")

        return file.readBytes()
    }

    private fun getLoadedClass(name: String): Class<*>? = loadedClass[name]


    protected abstract val prefix: String
    protected abstract val path: String
}