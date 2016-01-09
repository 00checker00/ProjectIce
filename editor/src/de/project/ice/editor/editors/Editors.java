package de.project.ice.editor.editors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import de.project.ice.utils.Assets;

public abstract class Editors
{
    public static BaseEditor editorForClass(Class type)
    {
        if (Float.class.equals(type) || type == float.class)
        {
            return new NumberEditor.FloatEditor();
        }
        else if (Double.class.equals(type) || type == double.class)
        {
            return new NumberEditor.DoubleEditor();
        }
        else if (Short.class.equals(type) || type == short.class)
        {
            return new NumberEditor.ShortEditor();
        }
        else if (Integer.class.equals(type) || type == int.class)
        {
            return new NumberEditor.IntegerEditor();
        }
        else if (Long.class.equals(type) || type == long.class)
        {
            return new NumberEditor.LongEditor();
        }
        else if (Vector2.class.equals(type))
        {
            return new Vector2Editor();
        }
        else if (Vector3.class.equals(type))
        {
            return new Vector3Editor();
        }
        else if (String.class.equals(type))
        {
            return new StringEditor();
        }
        else if (Assets.Holder.TextureRegion.class.equals(type))
        {
            return new TextureRegionHolderEditor();
        }
        else if (Assets.Holder.Animation.class.equals(type))
        {
            return new AnimationHolderEditor();
        }
        else if (IntMap.class.equals(type))
        {
            return new IntMapEditor();
        }
        else if (OrthographicCamera.class.equals(type))
        {
            return new OrthographicCameraEditor();
        }
        return new BaseEditor();
    }

}
