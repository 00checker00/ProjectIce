package de.project.ice.hotspot

import de.project.ice.utils.FileClassLoader

class HotspotLoader(parent: ClassLoader = ClassLoader.getSystemClassLoader()): FileClassLoader(parent) {
    override val prefix: String = "de.project.ice.hotspot.hotspots."
    override val path: String = "hotspots"
}