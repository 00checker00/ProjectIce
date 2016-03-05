package de.project.ice.ecs.components


class TimeoutComponent: IceComponent {
    var function: (()->Unit)? = null
    var timeout: Float = 0f;

    override fun reset() {
        timeout = 0f
        function = null
    }
}