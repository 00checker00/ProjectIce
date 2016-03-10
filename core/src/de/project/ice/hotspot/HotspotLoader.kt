package de.project.ice.hotspot

import java.io.File
import java.util.*

class HotspotLoader(parent: ClassLoader = ClassLoader.getSystemClassLoader()): ClassLoader(parent) {
    val loadedClass = HashMap<String, Class<*>>()

    override fun loadClass(name: String): Class<*> {
        return loadClass(name, false)
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        if (!name.startsWith(PREFIX))
            return super.loadClass(name, resolve)

        synchronized(getClassLoadingLock(name)) {

            val clazz = getLoadedClass(name) ?: findClass(name)

            if (resolve)
                resolveClass(clazz)

            return clazz
        }
    }

    override fun findClass(name: String): Class<*> {
        if (!name.startsWith(PREFIX))
            return super.findClass(name)

        val data = loadClassData(name.substring(PREFIX.length))
        return defineClass(name, data, 0, data.size).apply { loadedClass[name] = this }
    }

    private fun loadClassData(name: String): ByteArray {
              val file = File("hotspots/${name.replace(".", "/")}.class")

        if (!file.exists())
            throw ClassNotFoundException("Class for Hotspot \"$name\" not found")

        return file.readBytes()
    }

    private fun getLoadedClass(name: String): Class<*>? = loadedClass[name]

    companion object {
        private val PREFIX = "de.project.ice.hotspot.hotspots."
    }
}