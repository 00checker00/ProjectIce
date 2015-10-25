package de.project.ice.editor.editors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class HolderEditor<T> extends ValueEditor<Assets.Holder<T>>
{
    private VisTextField valueField;

    @NotNull
    protected String getHolderName()
    {
        if (value != null && value.name != null)
        {
            return value.name;
        }
        return "invalid";
    }

    @Nullable
    protected T getHolderData()
    {
        if (value != null)
        {
            return value.data;
        }
        return null;
    }

    protected void setHolderName(String name)
    {
        if (value != null)
        {
            value.name = name;
        }
    }

    protected void setHolderData(T data)
    {
        if (value != null)
        {
            value.data = data;
        }
    }

    @Override
    protected void createUi()
    {
        valueField = new VisTextField(getHolderName());
        valueField.setDisabled(true);
        add(valueField);
        VisTextButton editButton = new VisTextButton("...", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                onEdit();
            }
        });
        add(editButton);
    }

    protected void onEdit()
    {

    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        valueField.setText(getHolderName());
    }

}
