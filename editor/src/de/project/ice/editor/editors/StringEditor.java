package de.project.ice.editor.editors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kotcrab.vis.ui.widget.VisTextField;

public class StringEditor extends ValueEditor<String>
{
    private VisTextField valueField;

    @Override
    protected void createUi()
    {

        valueField = new VisTextField(value != null ? value : "");
        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if (button == Input.Buttons.MIDDLE)
                {
                    valueField.setText(valueField.getText() + Gdx.app.getClipboard().getContents());
                    return true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        add(valueField);
    }

    @Override
    public void act(float delta)
    {
        value = valueField.getText();
        super.act(delta);
    }

    @Override
    protected void updateValue()
    {
        valueField.setText(value);
    }
}
