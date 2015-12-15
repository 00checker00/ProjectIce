package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseEditor extends VisTable
{
    protected Method setter = null;
    protected Method getter = null;

    public BaseEditor()
    {
        defaults().align(Align.topLeft);
        create();
    }

    protected void create()
    {

    }

    public BaseEditor bind(Field field, Object target)
    {
        String name = field.getName();
        field.setAccessible(true);

        try
        {
            // Try to find setter for the field
            String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            setter = target.getClass().getMethod(setterName, field.getType());
            setter.setAccessible(true);
        }
        catch (NoSuchMethodException ignore)
        {
            // No setter
        }

        try
        {
            // Try to find getter for the field
            String setterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            getter = target.getClass().getMethod(setterName, field.getType());
            getter.setAccessible(true);
        }
        catch (NoSuchMethodException ignore)
        {
            // No getter
        }

        return this;
    }

    protected static boolean setValue(Field field, Object target, Object value, @Nullable Method setter)
    {
        try
        {
            if (setter != null)
            {
                try
                {
                    setter.invoke(target, value);
                    return true;
                }
                catch (InvocationTargetException ignore)
                {
                }
                catch (IllegalAccessException ignore)
                {
                }
            }

            // Error calling the setter, just set the value
            field.set(target, value);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    protected static Object getValue(Field field, Object target, @Nullable Method getter)
    {

        try
        {
            if (getter != null)
            {
                try
                {
                    return getter.invoke(target);
                }
                catch (InvocationTargetException ignore)
                {
                }
                catch (IllegalAccessException ignore)
                {
                }
            }
            // No getter, just get the value
            return field.get(target);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
