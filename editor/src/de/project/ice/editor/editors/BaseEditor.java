package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseEditor extends VisTable {
    public BaseEditor() {
        defaults().align(Align.topLeft);
        create();
    }

    protected void create() {

    }

    public BaseEditor bind(Field field, Object target){
        return this;
    }

    @Nullable
    protected static boolean setValue(Field field, Object target, Object value) {
        try {
            String name = field.getName();

            try {
                // Try to find and call a setter for the field
                String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method setter = target.getClass().getMethod(setterName, value.getClass());
                setter.invoke(target, value);
                return true;
            } catch (NoSuchMethodException ignore) {
            } catch (InvocationTargetException ignore) {
            } catch (IllegalAccessException ignore) {
            }
            // No setter, just set the value
            field.set(target, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    protected static Object getValue(Field field, Object target) {

        try {
            String name = field.getName();

            try {
                // Try to find and call a getter for the field
                String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method setter = target.getClass().getMethod(getterName);
                return setter.invoke(target);
            } catch (NoSuchMethodException ignore) {
            } catch (InvocationTargetException ignore) {
            } catch (IllegalAccessException ignore) {
            }
            // No getter, just get the value
            return field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
