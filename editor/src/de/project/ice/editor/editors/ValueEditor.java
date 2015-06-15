package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ValueEditor<T> extends BaseEditor {
    private String title;
    protected T value;
    private T oldValue;
    protected Field field;

    protected Object target;
    private Array<ValueChangedListener<T>> listeners = new Array<ValueChangedListener<T>>(0);


    @Override
    public void act(float delta) {
        super.act(delta);
        if ((oldValue != null && !oldValue.equals(value)) || (oldValue == null && value != null)) {
            oldValue = value;
            setValue(field, target, value);
            fireValueChanged();
        } else  {
            try {
                if (!value.equals(getValue(field, target))) {
                    value = (T) getValue(field, target);
                    updateValue();
                }
            } catch (NullPointerException ignore) {}
        }
    }

    protected void updateValue() {

    }

    @Override
    public ValueEditor<T> bind(Field field, Object target) {
        this.target = target;
        this.field = field;
        try {
            this.value = (T) field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.oldValue = value;
        this.title = field.getName() + ": ";

        defaults().align(Align.left);
        add(title);
        createUi();
        return this;
    }

    protected void createUi() {

    }

    protected void fireValueChanged() {
        try {
            T t = (T) value;
            field.set(target, t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (ValueChangedListener<T> listener : listeners) {
            listener.valueChanged(this, value);
        }
    }

    public void addListener(ValueChangedListener<T> value) {
        listeners.add(value);
    }

    public boolean removeListener(ValueChangedListener<T> value) {
        return listeners.removeValue(value, true);
    }

    public interface ValueChangedListener<T> {
        void valueChanged(ValueEditor<T> sender, T value);
    }
}
