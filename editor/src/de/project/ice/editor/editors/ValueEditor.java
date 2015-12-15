package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Align;

import java.lang.reflect.Field;

public class ValueEditor<T> extends BaseEditor
{
    private String title;
    protected T value;
    private T oldValue;
    protected Field field;

    protected Object target;

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if ((oldValue != null && !oldValue.equals(value)) || (oldValue == null && value != null))
        {
            oldValue = value;
            setValue(field, target, value, setter);
            fireValueChanged();
        }
        else
        {
            try
            {
                if (!value.equals(getValue(field, target, getter)))
                {
                    value = (T) getValue(field, target, getter);
                    updateValue();
                }
            }
            catch (NullPointerException ignore)
            {
            }
        }
    }

    protected void updateValue()
    {

    }

    @Override
    public ValueEditor<T> bind(Field field, Object target)
    {
        this.target = target;
        this.field = field;
        this.field.setAccessible(true);
        try
        {
            this.value = (T) field.get(target);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        this.oldValue = value;
        this.title = field.getName() + ": ";

        defaults().align(Align.left);
        add(title);
        createUi();
        return this;
    }

    protected void createUi()
    {

    }

    protected void fireValueChanged()
    {
        try
        {
            T t = value;
            field.set(target, t);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
