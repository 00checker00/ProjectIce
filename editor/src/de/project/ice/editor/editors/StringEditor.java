package de.project.ice.editor.editors;

import com.kotcrab.vis.ui.widget.VisTextField;

public class StringEditor extends ValueEditor<String> {
    private VisTextField valueField;

    @Override
    protected void createUi() {
        valueField = new VisTextField(value);
        add(valueField);
    }

    @Override
    public void act(float delta) {
        value = valueField.getText();

        super.act(delta);
    }
}
