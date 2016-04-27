package de.project.ice.editor.editors

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import de.project.ice.ecs.components.ActivationDirection
import de.project.ice.ecs.systems.CameraSystem
import de.project.ice.screens.CursorScreen
import de.project.ice.utils.Assets;

class Editors {
    companion object {
        fun editorForClass(type: Class<*>): BaseEditor {
            return when(type) {
                Float::class.java, java.lang.Float.TYPE     -> NumberEditor.FloatEditor()
                Double::class.java, java.lang.Double.TYPE   -> NumberEditor.DoubleEditor()
                Short::class.java, java.lang.Short.TYPE     -> NumberEditor.ShortEditor()
                Int::class.java, java.lang.Integer.TYPE     -> NumberEditor.IntegerEditor()
                Long::class.java, java.lang.Long.TYPE       -> NumberEditor.LongEditor()
                Boolean::class.java, java.lang.Boolean.TYPE -> BoolEditor()
                Vector2::class.java                         -> Vector2Editor()
                Vector3::class.java                         -> Vector3Editor()
                String::class.java                          -> StringEditor()
                Assets.Holder.TextureRegion::class.java     -> TextureRegionHolderEditor()
                Assets.Holder.Animation::class.java         -> AnimationHolderEditor()
                IntMap::class.java                          -> IntMapEditor()
                OrthographicCamera::class.java              -> OrthographicCameraEditor()
                CursorScreen.Cursor::class.java             -> CursorEditor()
                CameraSystem.FollowType::class.java         -> FollowTypeEditor()
                ActivationDirection::class.java             -> ActivationDirectionEditor()
                else                                        -> BaseEditor()

            }
        }
    }
}
