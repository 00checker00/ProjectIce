package de.project.ice.editor.editors;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.lang.reflect.Field;

public class OrthographicCameraEditor extends BaseEditor
{

    @Override
    public BaseEditor bind(Field field, Object target)
    {
        try
        {
            add(field.getName() + ":").row();
            OrthographicCamera camera = (OrthographicCamera) field.get(target);
            Field position = OrthographicCamera.class.getField("position");
            add(new Vector3Editor().bind(position, camera)).padLeft(10f).row();
            Field viewportWidth = OrthographicCamera.class.getField("viewportWidth");
            add(new NumberEditor.FloatEditor().bind(viewportWidth, camera)).padLeft(10f).row();
            Field viewportHeight = OrthographicCamera.class.getField("viewportHeight");
            add(new NumberEditor.FloatEditor().bind(viewportHeight, camera)).padLeft(10f).row();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return this;
    }
}
