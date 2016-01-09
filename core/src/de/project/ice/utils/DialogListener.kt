package de.project.ice.utils

interface DialogListener<T> {
    fun onResult(result: T)
    fun onChange(result: T)
    fun onCancel()
}
