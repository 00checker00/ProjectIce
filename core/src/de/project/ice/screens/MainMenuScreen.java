package de.project.ice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.project.ice.IceGame;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MainMenuScreen extends BaseScreenAdapter
{
    protected static final String BUTTON_NEW_GAME_ID = "BTN_NEW_GAME";
    protected static final String BUTTON_SAVE_LOAD_ID = "BTN_SAVE_LOAD";
    protected static final String BUTTON_SETTINGS_ID = "BTN_SETTINGS";
    protected static final String BUTTON_EXIT_ID = "BTN_EXIT";

    @NotNull
    private Stage stage;
    @NotNull
    private Skin skin;
    @NotNull
    private Table root;
    @NotNull
    private VerticalGroup menuLayout;
    @NotNull
    private HashMap<String, TextButton> buttons = new HashMap<String, TextButton>();

    public MainMenuScreen(@NotNull IceGame game)
    {
        super(game);

        stage = new Stage();
        stage.setDebugAll(true);
        stage.setViewport(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("ui/skin.json"));

        root = new Table();
        stage.addActor(root);

        root.setFillParent(true);
        root.setBackground(skin.getTiledDrawable("menu_bg"));

        Image logo = new Image(skin, "menu_logo");
        root.add(logo);

        root.row();
        menuLayout = new VerticalGroup();
        menuLayout.fill();
        menuLayout.space(5f);
        root.add(menuLayout);

        createMenuButton(BUTTON_NEW_GAME_ID, "New Game", new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                MainMenuScreen.this.game.startNewGame();
                MainMenuScreen.this.game.removeScreen(MainMenuScreen.this);
                return true;
            }
        });
        createMenuButton(BUTTON_SAVE_LOAD_ID, "Save/Load", new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                System.out.println("Save/Load");
                return true;
            }
        });
        createMenuButton(BUTTON_SETTINGS_ID, "Settings", new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                System.out.println("Settings");
                return true;
            }
        });
        createMenuButton(BUTTON_EXIT_ID, "Exit", new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                MainMenuScreen.this.game.exit();
                return true;
            }
        });
        inputProcessor = new DelegatingBlockingInputProcessor(stage);
    }

    @Override
    public int getPriority()
    {
        return 100;
    }

    protected void insertMenuButton(@NotNull String id, @NotNull String text, @Nullable InputListener listener, String idAfter)
    {
        TextButton button = new TextButton(text, skin);
        if (listener != null)
        {
            button.addListener(listener);
        }
        menuLayout.addActorAfter(buttons.get(idAfter), button);
        buttons.put(id, button);
    }

    protected void createMenuButton(@NotNull String id, @NotNull String text, @Nullable InputListener listener)
    {
        TextButton button = new TextButton(text, skin);
        if (listener != null)
        {
            button.addListener(listener);
        }
        menuLayout.addActor(button);
        buttons.put(id, button);
    }

    protected TextButton getButton(@NotNull String id)
    {
        return buttons.get(id);
    }

    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
    }

    public void render()
    {
        stage.getViewport().apply();
        stage.draw();
    }

    public void dispose()
    {
        stage.dispose();
    }
}
