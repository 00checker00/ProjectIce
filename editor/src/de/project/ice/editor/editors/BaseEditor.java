package de.project.ice.editor.editors;


import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import java.lang.reflect.Field;

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
}
