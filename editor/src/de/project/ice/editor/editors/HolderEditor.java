package de.project.ice.editor.editors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.utils.Assets;

public abstract class HolderEditor<T> extends ValueEditor<Assets.Holder<T>> {
    private VisTextField valueField;

    @Override
    protected void createUi() {
        valueField = new VisTextField(value.name);
        valueField.setDisabled(true);
        add(valueField);
        VisTextButton editButton = new VisTextButton("...", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onEdit();
            }
        });
        add(editButton);
    }

    protected void onEdit() {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        valueField.setText(value.name);
    }

}
