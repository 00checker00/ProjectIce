package de.project.ice.editor.editors;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.lang.reflect.Field;

public class OrthographicCameraEditor extends BaseEditor
{
    private float viewportScale = 1.0f;
    private float lastViewportScale = 1.0f;
    private OrthographicCamera camera;

    @Override
    public BaseEditor bind(Field field, Object target)
    {
        try
        {
            add(field.getName() + ":").row();
            camera = (OrthographicCamera) field.get(target);
            Field position = OrthographicCamera.class.getField("position");
            add(new Vector3Editor().bind(position, camera)).padLeft(10f).row();
            Field viewportScale = OrthographicCameraEditor.class.getDeclaredField("viewportScale");
            add(new NumberEditor.FloatEditor().bind(viewportScale, this)).padLeft(10f).row();
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

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if (lastViewportScale != viewportScale) // scale has been changed in Editor
        {
            // Update target values
            camera.viewportWidth = 16 * viewportScale;
            camera.viewportHeight = 9 * viewportScale;
        }
        else
        {
            float scale = camera.viewportWidth / 16f;
            if (scale != viewportScale)
            {
                lastViewportScale = scale;
                viewportScale = scale;

            }
        }

    }
}
