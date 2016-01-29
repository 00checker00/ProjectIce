package de.project.ice.ecs.components

import de.project.ice.annotations.Property
import de.project.ice.utils.Assets

class TextureComponent : CopyableIceComponent {
    @Property("The current textureregion of this object")
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
