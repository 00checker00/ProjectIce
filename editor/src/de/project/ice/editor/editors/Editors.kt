package de.project.ice.editor.editors

import de.project.ice.utils.Assets

public class Editors {
    companion object {
        fun editorForClass(type: Class<*>): BaseEditor {
            if (Float::class.java == type || type == java.lang.Float.TYPE) {
                return NumberEditor.FloatEditor()
            } else if (Double::class.java == type || type == java.lang.Double.TYPE) {
                return NumberEditor.DoubleEditor()
            } else if (Short::class.java == type || type == java.lang.Short.TYPE) {
                return NumberEditor.ShortEditor()
            } else if (Int::class.java == type || type == Integer.TYPE) {
                return NumberEditor.IntegerEditor()
            } else if (Long::class.java == type || type == java.lang.Long.TYPE) {
                return NumberEditor.LongEditor()
            } else if (Vector2::class.java == type) {
                return Vector2Editor()
            } else if (Vector3::class.java == type) {
                return Vector3Editor()
            } else if (String::class.java == type) {
                return StringEditor()
            } else if (Assets.Holder.TextureRegion::class.java == type) {
                return TextureRegionHolderEditor()
            } else if (Assets.Holder.Animation::class.java == type) {
                return AnimationHolderEditor()
            } else if (IntMap::class.java == type) {
                return IntMapEditor()
            } else if (OrthographicCamera::class.java == type) {
                return OrthographicCameraEditor()
            }
            return BaseEditor()
        }
    }
}
