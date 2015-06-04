package de.project.ice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.project.ice.IceGame;
import de.project.ice.Storage;
import de.project.ice.dialog.Node;
import de.project.ice.utils.ColorDrawable;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DialogScreen extends BaseScreenAdapter {
    private static final String DIALOG_STORAGE_PREFIX = "__dialog__";
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
    private Table choiceTable;

    public DialogScreen(@NotNull IceGame game,@NotNull Node dialog) {
        super(game);

        stage = new Stage();
        stage.setDebugAll(true);
        stage.setViewport(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("ui/skin.json"));

        root = new Table();
        stage.addActor(root);

        root.setFillParent(true);

        inputProcessor = new DelegatingBlockingInputProcessor(stage);

        showNode(dialog);
    }

    private void showNode(Node node) {
        if (node == null) {
            // Reached end of dialog
            game.removeScreen(this);
            return;
        } else if(node.type == Node.Type.Node || node.type == Node.Type.Start) {
            // Empty node => Skip to next
            showNode(node.next);
            return;
        } else if(node.type == Node.Type.Set) {
            // Set value then skip to next
            Storage.getSavestate().putString(DIALOG_STORAGE_PREFIX + node.variable_name, node.variable_value);
            showNode(node.next);
            return;
        } else if(node.type == Node.Type.Branch) {
            // Switch to correct branch
            Node next;
            if (node.variable_name == null || node.branch == null) {
                next = null;
            } else {
                next = node.branch.getForValue(Storage.getSavestate().getString(DIALOG_STORAGE_PREFIX + node.variable_name));
            }
            showNode(next);
            return;
        }
        clear();
        root.row().expand();

        choiceTable = new Table(skin);
        choiceTable.defaults().space(20f);
        choiceTable.defaults().align(Align.left);

        if (node.choices.size > 0) {
            float pad = 50;

            for(Node choice : node.choices) {
                choiceTable.row();
                TextButton btn = new TextButton(choice.text, skin);
                final Node next = choice.next;
                btn.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        showNode(next);
                        return true;
                    }
                });
                choiceTable.add(btn).padLeft(pad /= 2);
            }
        }

        root.add(choiceTable).top().left();

        root.row().expandX();

        final Node next = node.next;
        Label textLabel = new Label(node.text, skin);
        if (node.choices.size == 0) {
            textLabel.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    showNode(next);
                    return true;
                }
            });
        }
        textLabel.setAlignment(Align.center);
        ScrollPane scrollPane = new ScrollPane(textLabel, skin);
        root.add(scrollPane).height(50f).fill();

    }

    private void clear() {
        root.clear();
    }

    @Override
    public int getPriority () {
        return 100;
    }

    protected TextButton getButton(@NotNull String id) {
        return buttons.get(id);
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    public void render () {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
