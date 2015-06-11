package de.project.ice.editor.editors;

import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Field;

class Vector2Editor extends BaseEditor {
    @Override
    public BaseEditor bind(Field field, Object target) {
        try {
            add(field.getName() + ":").row();
            Vector2 vector = (Vector2) field.get(target);
            Field x = Vector2.class.getField("x");
            add(new NumberEditor.FloatEditor().bind(x, vector)).padLeft(10f).row();
            Field y = Vector2.class.getField("y");
            add(new NumberEditor.FloatEditor().bind(y, vector)).padLeft(10f);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }
}
