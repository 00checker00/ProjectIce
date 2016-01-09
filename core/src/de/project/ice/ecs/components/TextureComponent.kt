package de.project.ice.ecs.components

import de.project.ice.utils.Assets

class TextureComponent : CopyableIceComponent {
    var region = Assets.Holder.TextureRegion()

    override fun reset() {
        region = Assets.Holder.TextureRegion()
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is TextureComponent) {
            copy.region = Assets.findRegion(region.name)
        }
    }
}
