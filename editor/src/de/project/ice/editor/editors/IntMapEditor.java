package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.lang.reflect.Field;

public class IntMapEditor extends BaseEditor
{
    private Field field;
    private Object target;
    private IntMap<Object> map;
    private Array<Binder> binders = new Array<Binder>();
    private String description;

    @Override
    public void act(float delta)
    {
        super.act(delta);
        for (Binder binder : binders)
        {
            if (binder.oldKey != binder.key)
            {
                Binder tmp = null;
                if (map.containsKey(binder.key))
                {
                    for (int i = 0; i < binders.size; i++)
                    {
                        if (binders.get(i).oldKey == binder.key)
                        {
                            tmp = binders.get(i);
                            break;
                        }
                    }
                }
                map.remove(binder.oldKey);
                map.put(binder.key, binder.value);
                if (tmp != null)
                {
                    map.put(binder.oldKey, tmp.value);
                    tmp.key = binder.oldKey;
                    tmp.oldKey = binder.oldKey;
                }
                binder.oldKey = binder.key;
                bind(field, target, "");
            }
            if (binder.value != map.get(binder.key))
            {
                map.put(binder.key, binder.value);
            }
        }
        if (map.size != binders.size)
        {
            bind(field, target, "");
        }
    }

    @Override
    public void clear()
    {
        super.clear();
        binders.clear();
    }

    @Override
    public IntMapEditor bind(Field field, Object target, String description)
    {
        this.field = field;
        this.target = target;
        this.description = description;
        clear();
        try
        {
            map = (IntMap<Object>) field.get(target);
            for (IntMap.Entry<Object> entry : map)
            {
                Binder binder = new Binder(entry.key, entry.value);
                add(new NumberEditor.IntegerEditor().bind(Binder.class.getField("key"), binder, "The " + entry.key + ". entry"));
                add(Editors.Companion.editorForClass(entry.value.getClass())
                        .bind(Binder.class.getField("value"), binder, description))
                        .row();
                binders.add(binder);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public class Binder
    {
        public int oldKey;
        public int key;
        public Object value;

        public Binder(int key, Object value)
        {
            this.key = key;
            this.oldKey = key;
            this.value = value;
        }
    }
}
