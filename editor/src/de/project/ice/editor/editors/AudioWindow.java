package de.project.ice.editor.editors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import de.project.ice.ecs.IceEngine;
import org.jetbrains.annotations.Nullable;


public class AudioWindow extends VisWindow {

    private IceEngine engine;
    private VisTextField musictext;
    private VisTextArea soundtext;

    public AudioWindow(IceEngine engine) throws IllegalStateException {
        super("Audio");
        this.engine = engine;

        TableUtils.setSpacingDefaults(this);
        createWidgets();

        setPosition(0, 0, Align.topRight);
        setResizable(true);
        setSize(200, 400);
    }


    public void setValues(String music, Array<String> sounds){
        StringBuilder sb = new StringBuilder();

        for(String sound: sounds){
            sb.append(sound);
            sb.append("\n");
        }

        soundtext.setText(sb.toString());
        musictext.setText(music);

    }

    private void createWidgets() {



        VisLabel soundlabel = new VisLabel("Sounds:");
        add(soundlabel).expandX().fill().row();
        soundtext = new VisTextArea();
        add(soundtext).expandX().expandY().fill().row();

        VisLabel musiclabel = new VisLabel("Musik:");
        add(musiclabel).expandX().fill().row();
        musictext = new VisTextField();
        add(musictext).expandX().fill().row();



        VisTextButton savebutton = new VisTextButton("Set", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                engine.soundSystem.unloadSounds();
                engine.soundSystem.stopMusic();
                engine.soundSystem.playMusic(musictext.getText());

                if(!soundtext.getText().isEmpty()) {
                    for (String sound : soundtext.getText().split("\n")) {
                        engine.soundSystem.loadSound(sound);
                    }
                }


            }
        });
        add(savebutton).expandX().fill().row();

    }


}