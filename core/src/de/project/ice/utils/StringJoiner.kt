package de.project.ice.utils

fun Iterable<Any>.join(delimiter: String): String {
    val iterator = this.iterator()
    if (!iterator.hasNext()) {
        return ""
    }
    val sb = java.lang.StringBuilder(iterator.next().toString())
    while (iterator.hasNext()) {
        sb.append(delimiter)
        sb.append(iterator.next().toString())
    }
    return sb.toString()
}