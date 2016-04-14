package de.project.ice.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Property(val Description: String, val debug: Boolean = false, val group: String = "default")

