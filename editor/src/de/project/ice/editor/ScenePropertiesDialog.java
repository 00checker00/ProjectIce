package de.project.ice.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.ecs.IceEngine;
import de.project.ice.utils.DialogListener;
import de.project.ice.utils.SceneLoader;
import de.project.ice.utils.StringJoiner;


public class ScenePropertiesDialog extends VisDialog
{
    private IceEngine engine;
    private VisTextField musicText;
    private VisTextArea soundText;
    private VisTextArea spritesheetsText;
    private VisTextField onloadText;
    private VisTextField sceneNameText;
    private Array<DialogListener<SceneLoader.SceneProperties>> listeners = new Array<DialogListener<SceneLoader.SceneProperties>>();

    public ScenePropertiesDialog(SceneLoader.SceneProperties properties) throws IllegalStateException
    {
        super("Scene Properties");
        this.engine = properties.engine();

        TableUtils.setSpacingDefaults(this);
        createWidgets();

        sceneNameText.setText(properties.name());
        musicText.setText(properties.music());
        onloadText.setText(properties.onloadScript());
        soundText.setText(StringJoiner.join(properties.sounds(), "\n"));
        spritesheetsText.setText(StringJoiner.join(properties.spritesheets(), "\n"));

        setPosition(0, 0, Align.topRight);
        setResizable(true);
        setSize(300, 400);
    }

    public ScenePropertiesDialog addListener(DialogListener<SceneLoader.SceneProperties> listener)
    {
        listeners.add(listener);
        return this;
    }

    public ScenePropertiesDialog removeListener(DialogListener<SceneLoader.SceneProperties> listener)
    {
        listeners.removeValue(listener, true);
        return this;
    }

    private void createWidgets()
    {
        Table contentTable = getContentTable();

        VisLabel sceneNamelabel = new VisLabel("Name:");
        contentTable.add(sceneNamelabel);
        sceneNameText = new VisTextField();
        contentTable.add(sceneNameText).expandX().fill().row();

        VisLabel spritesheetslabel = new VisLabel("Spritesheets:");
        contentTable.add(spritesheetslabel).top();
        spritesheetsText = new VisTextArea();
        contentTable.add(spritesheetsText).minHeight(100).expand().fill().row();

        VisLabel onloadlabel = new VisLabel("Onload Script:");
        contentTable.add(onloadlabel);
        onloadText = new VisTextField();
        contentTable.add(onloadText).expandX().fill().row();

        VisLabel soundlabel = new VisLabel("Sounds:");
        contentTable.add(soundlabel).top();
        soundText = new VisTextArea();
        contentTable.add(soundText).minHeight(100).expand().fill().row();

        VisLabel musiclabel = new VisLabel("Musik:");
        contentTable.add(musiclabel);
        musicText = new VisTextField();
        contentTable.add(musicText).expandX().fill().row();


        button("Ok", "ok");
        button("Cancel", "cancel");
    }

    @Override
    protected void result(Object object)
    {
        if("ok".equals(object))
        {
            SceneLoader.SceneProperties sceneProperties = new SceneLoader.ScenePropertiesBuilder()
                    .engine(engine)
                    .name(sceneNameText.getText())
                    .music(musicText.getText())
                    .onloadScript(onloadText.getText())
                    .sounds(new Array<String>(soundText.getText().split("\n")))
                    .spritesheets(new Array<String>(spritesheetsText.getText().split("\n")))
                    .create();

            for(DialogListener<SceneLoader.SceneProperties> listener: listeners)
            {
                listener.onResult(sceneProperties);
            }
        }
        else
        {
            for(DialogListener<SceneLoader.SceneProperties> listener: listeners)
            {
                listener.onCancel();
            }
        }
    }
}