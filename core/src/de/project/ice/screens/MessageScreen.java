package de.project.ice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.project.ice.IceGame;
import de.project.ice.utils.ColorDrawable;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;


public class MessageScreen extends BaseScreenAdapter {
    @NotNull private Array<String> messages;

    @NotNull
    private Stage stage;
    @NotNull
    private Skin skin;
    @NotNull
    private Table root;
    @NotNull
    private Label messageLabel;

    public MessageScreen(@NotNull IceGame game, String... messages) {
        super(game);

        this.messages = new Array<String>(messages);
        this.messages.reverse();

        stage = new Stage();
        stage.setViewport(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("ui/skin.json"));

        root = new Table(skin);
        stage.addActor(root);

        root.setFillParent(true);
        root.setBackground(new ColorDrawable(new Color(0f, 0f, 0f, 0.4f), true));

        root.row().expand();
        root.add("");

        messageLabel = new Label(null, skin);
        root.row().expandX();
        root.add(messageLabel);

        root.row().expand();
        root.add("");

        inputProcessor = new DelegatingBlockingInputProcessor(stage) {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                showNextMessage();
                return true;
            }
        };

        showNextMessage();
    }

    private void showNextMessage() {
        if (messages.size == 0) {
            game.removeScreen(this);
            return;
        }

        messageLabel.setText(messages.pop());
    }

    @Override
    public int getPriority () {
        return 500;
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    public void render () {
        stage.getViewport().apply();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
