package de.project.ice.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;

public class PauseScreen extends MainMenuScreen {
    protected static final String BUTTON_CONTINUE_ID = "BTN_CONTINUE";

    public PauseScreen(@NotNull IceGame game) {
        super(game);
        insertMenuButton(BUTTON_CONTINUE_ID, "Continue", new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                PauseScreen.this.game.removeScreen(PauseScreen.this);
                return true;
            }
        }, BUTTON_SETTINGS_ID);
    }

    @Override
    public int getPriority () {
        return 500;
    }
}
