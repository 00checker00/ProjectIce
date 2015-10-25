package de.project.ice.editor.editors;

import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Field;

class Vector3Editor extends BaseEditor
{
    @Override
    public BaseEditor bind(Field field, Object target)
    {
        try
        {
            add(field.getName() + ":").row();
            Vector3 vector = (Vector3) field.get(target);
            Field x = Vector3.class.getField("x");
            add(new NumberEditor.FloatEditor().bind(x, vector)).padLeft(10f).row();
            Field y = Vector3.class.getField("y");
            add(new NumberEditor.FloatEditor().bind(y, vector)).padLeft(10f).row();
            Field z = Vector3.class.getField("z");
            add(new NumberEditor.FloatEditor().bind(z, vector)).padLeft(10f);
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
