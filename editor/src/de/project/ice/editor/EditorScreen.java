package de.project.ice.editor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import de.project.ice.IceGame;
import de.project.ice.Storage;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.TextureComponent;
import de.project.ice.ecs.components.TransformComponent;
import de.project.ice.ecs.systems.RenderingSystem;
import de.project.ice.editor.editors.AudioWindow;
import de.project.ice.screens.BaseScreenAdapter;
import de.project.ice.scripting.Script;
import de.project.ice.utils.Assets;
import de.project.ice.utils.DelegatingInputProcessor;
import de.project.ice.utils.SceneLoader;
import de.project.ice.utils.SceneWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.project.ice.config.Config.*;

import java.io.*;

public class EditorScreen extends BaseScreenAdapter implements EntitiesWindow.SelectionListener {
    private static final String VERSION = "0.0.1";
    private final AudioWindow audioWindow;
    @NotNull
    private Stage stage;
    @NotNull
    private VisTable root;
    private MenuBar menuBar;
    private MenuItem stopPlaytest;
    private MenuItem startPlaytest;
    @NotNull
    EntitiesWindow entitiesWindow;
    @NotNull
    ComponentsWindow componentsWindow;

    private String filename = null;
    private String storedState = null;

    public EditorScreen(@NotNull final IceGame game) {
        super(game);
        VisUI.load();
        FileChooser.setFavoritesPrefsName("de.project.ice.editor");

        Storage.Global storage = Storage.getGlobal();
        stage = new Stage();
        //stage.setDebugAll(true);
        stage.setViewport(new ScreenViewport());

        root = new VisTable(true);
        root.setFillParent(true);
        stage.addActor(root);

        menuBar = new MenuBar();
        root.add(menuBar.getTable()).expandX().fillX().row();
        root.add().expand().fill();

        createMenus();

        entitiesWindow = new EntitiesWindow(game.engine);
        entitiesWindow.setSelectionListener(this);
        entitiesWindow.setPosition(storage.getFloat("editor_entities_x", 0f), storage.getFloat("editor_entities_y", 0f));
        entitiesWindow.setSize(storage.getFloat("editor_entities_width", 200f), storage.getFloat("editor_entities_height", 400f));
        entitiesWindow.setVisible(storage.getBoolean("editor_entities_visible", true));
        stage.addActor(entitiesWindow);

        componentsWindow = new ComponentsWindow(game.engine);
        componentsWindow.setPosition(storage.getFloat("editor_components_x", 0f), storage.getFloat("editor_components_y", 0f));
        componentsWindow.setSize(storage.getFloat("editor_components_width", 400f), storage.getFloat("editor_components_height", 400f));
        componentsWindow.setVisible(storage.getBoolean("editor_components_visible", true));
        stage.addActor(componentsWindow);

        audioWindow = new AudioWindow(game.engine);
        stage.addActor(audioWindow);


        inputProcessor = new DelegatingInputProcessor(stage) {
            private TransformComponent dragComponent = null;
            private float dragOriginX = 0f;
            private float dragOriginY = 0f;
            private Vector2 cameraDragDown = new Vector2();
            private OrthographicCamera cameraDrag = null;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dragComponent = null;
                cameraDrag = null;
                return super.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if ((!super.touchDown(screenX, screenY, pointer, button)) && storedState == null) {
                    ImmutableArray<Entity> cameras = game.engine.getEntitiesFor(Families.camera);
                    if (cameras.size() == 0)
                        return false;

                    Entity activeCamera = cameras.first();
                    CameraComponent cameraComponent = Components.camera.get(activeCamera);
                    Vector3 coords = cameraComponent.camera.unproject(new Vector3(screenX, screenY, 0f));
                    if (button == Input.Buttons.LEFT) {
                        Array <Entity> entities = new Array<Entity>(game.engine.getEntitiesFor(Families.renderable).toArray());
                        entities.sort(new RenderingSystem.RenderingComparator());
                        entities.reverse();
                        for(Entity entity : entities) {
                            TransformComponent transform = Components.transform.get(entity);
                            TextureComponent texture = Components.texture.get(entity);

                            if (!texture.region.isValid())
                                continue;

                            float width = texture.region.data.getRegionWidth() * PIXELS_TO_METRES;
                            float height = texture.region.data.getRegionHeight() * PIXELS_TO_METRES;

                            if (new Rectangle(transform.pos.x, transform.pos.y, width, height).contains(coords.x, coords.y)) {
                                dragComponent = transform;
                                dragOriginX = coords.x - transform.pos.x;
                                dragOriginY = coords.y - transform.pos.y;
                                return true;
                            }
                        }
                        return true;
                    } else if (button == Input.Buttons.MIDDLE) {
                        cameraDragDown.set(screenX, screenY);
                        cameraDrag = cameraComponent.camera;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                    if (dragComponent != null) {
                        ImmutableArray<Entity> cameras = game.engine.getEntitiesFor(Families.camera);
                        if (cameras.size() == 0)
                            return false;

                        Entity activeCamera = cameras.first();
                        CameraComponent cameraComponent = Components.camera.get(activeCamera);
                        Vector3 coords = cameraComponent.camera.unproject(new Vector3(screenX, screenY, 0f));

                        dragComponent.pos.set(new Vector2(coords.x - dragOriginX, coords.y - dragOriginY));
                    } else if (cameraDrag != null) {
                        cameraDrag.translate(new Vector2(screenX, screenY).sub(cameraDragDown).scl(PIXELS_TO_METRES).scl(-1, 1));
                        cameraDragDown.set(screenX, screenY);
                }
                return super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean keyDown(int keycode) {
                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    switch (keycode) {
                        case Input.Keys.S:
                            save();
                            break;

                        case Input.Keys.O:
                            open();
                            break;

                        case Input.Keys.N:
                            newScene();
                            break;
                    }
                    return true;
                }
                return super.keyDown(keycode);
            }
        };

        Gdx.graphics.setDisplayMode(storage.getInteger("editor_screen_width", 800),
                storage.getInteger("editor_screen_height", 600),
                false);
    }

    private void createMenus () {
        Menu fileMenu = new Menu("File");
        Menu windowMenu = new Menu("Window");
        Menu testMenu = new Menu("Test");
        Menu helpMenu = new Menu("Help");

        fileMenu.addItem(new MenuItem("New Scene", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newScene();
            }
        }).setShortcut("Ctrl + N"));
        fileMenu.addItem(new MenuItem("Open Scene", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                open();
            }
        }).setShortcut("Ctrl + O"));
        fileMenu.addItem(new MenuItem("Save Scene", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save(false);
            }
        }).setShortcut("Ctrl + S"));
        fileMenu.addItem(new MenuItem("Save Scene As", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save(true);
            }
        }).setShortcut("Ctrl + Shift + S"));
        fileMenu.addItem(new MenuItem("Screenshot", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenshotFactory.saveScreenshot();
            }
        }));
        fileMenu.addSeparator();
        fileMenu.addItem(new MenuItem("Quit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                quit();
            }
        }));

        windowMenu.addItem(new MenuItem("Show/Hide Entities Window", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entitiesWindow.setVisible(!entitiesWindow.isVisible());
            }
        }));
        windowMenu.addItem(new MenuItem("Show/Hide Components Window", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                componentsWindow.setVisible(!componentsWindow.isVisible());
            }
        }));

        startPlaytest = new MenuItem("Start PlayTest", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stopPlaytest.setDisabled(false);
                startPlaytest.setDisabled(true);
                storeState();
                game.resumeGame();
            }
        });
        testMenu.addItem(startPlaytest);

        stopPlaytest = new MenuItem("Stop PlayTest", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stopPlaytest.setDisabled(true);
                startPlaytest.setDisabled(false);
                restoreState();
                game.pauseGame();
            }
        });
        stopPlaytest.setDisabled(true);
        testMenu.addItem(stopPlaytest);

        helpMenu.addItem(new MenuItem("About", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                DialogUtils.showOKDialog(stage, "about", "ProjectIce Editor version " + VERSION);
            }
        }));

        menuBar.addMenu(fileMenu);
        menuBar.addMenu(windowMenu);
        menuBar.addMenu(testMenu);
        menuBar.addMenu(helpMenu);
    }

    private void storeState() {
        StringWriter writer = new StringWriter();
        XmlWriter xml = new XmlWriter(writer);
        serializeScene(xml);
        storedState = writer.toString();
        try {
            FileWriter fileWriter = new FileWriter("$stored_state$");
            fileWriter.write(storedState);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restoreState() {
        game.inventory.items.clear();
        if (storedState == null)
            return;
        game.engine.removeAllEntities();
        try {
            SceneLoader.loadScene(game.engine, storedState);
            storedState = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SceneLoader.LoadException e) {
            e.printStackTrace();
        }
    }

    private void clear(){
        filename = null;
        game.engine.removeAllEntities();

        game.pauseGame();
    }

    private void newScene() {
        clear();
        DialogUtils.showInputDialog(stage, "Scene Name", "Name", new InputDialogListener() {
            @Override
            public void finished(String input) {
                Assets.loadScene(input);
            }

            @Override
            public void canceled() {
            }
        });
    }

    private void open() {
        clear();

        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(new FileHandle("."));
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".scene") || pathname.isDirectory();
            }
        });
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected (FileHandle file) {
                try {
                    SceneLoader.loadScene(game.engine, file.read());
                    audioWindow.setValues(game.engine.soundSystem.getMusic(), game.engine.soundSystem.getSounds());
                    filename = file.file().getCanonicalFile().getAbsolutePath();
                } catch (IOException e) {
                    DialogUtils.showErrorDialog(stage, "Couldn't save the scene.", e);
                } catch (SceneLoader.LoadException e) {
                    DialogUtils.showErrorDialog(stage, "Scene files is invalid", e);
                }
            }
        });
        stage.addActor(fileChooser.fadeIn());
    }

    private void save(){
        save(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
    }

    private void save(boolean forceFileDialog) {
        if (filename == null || forceFileDialog) {
            FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
            fileChooser.setDirectory(filename != null ? new FileHandle(filename).parent() : new FileHandle("."));
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".scene") || pathname.isDirectory();
                }
            });
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected (FileHandle file) {
                    try {
                        filename = file.file().getCanonicalFile().getAbsolutePath();
                        if (!filename.endsWith(".scene"))
                            filename += ".scene";
                        save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            stage.addActor(fileChooser.fadeIn());
            return;
        }

        try {
            if (storedState == null) {
                File file = new File(filename);
                if (file.exists())
                    file.renameTo(new File(file.getParent() + "/" + file.getName() + ".bak"));
                XmlWriter xml = new XmlWriter(new FileWriter(filename));
                serializeScene(xml);
            } else {
                FileWriter writer = new FileWriter(filename);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(storedState);
                bufferedWriter.close();
            }
        } catch (IOException e) {
            DialogUtils.showErrorDialog(stage, "Couldn't access the file.", e);
        }

    }

    private void serializeScene(XmlWriter xml) {
        try {
            SceneWriter.serializeScene(Gdx.files.absolute(filename).nameWithoutExtension(), game.engine, xml);
        } catch (IOException e) {
            DialogUtils.showErrorDialog(stage, "Couldn't save the scene.", e);
        }
    }



    private void quit() {
        Gdx.app.exit();
    }

    @Override
    public int getPriority () {
        return 1;
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
        Storage.Global storage = Storage.getGlobal();
        storage.put("editor_screen_width", width);
        storage.put("editor_screen_height", height);
        storage.save();
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        if (filename == null)
            Gdx.graphics.setTitle("Editor (Here be dragons)");
        else
            Gdx.graphics.setTitle("Editor (Here be dragons) [" + filename + "]");
    }

    public void render () {
        stage.draw();
    }

    public void dispose() {
        Storage.Global storage = Storage.getGlobal();
        storage.put("editor_components_x", componentsWindow.getX());
        storage.put("editor_components_y", componentsWindow.getY());
        storage.put("editor_components_width", componentsWindow.getWidth());
        storage.put("editor_components_height", componentsWindow.getHeight());
        storage.put("editor_components_visible", componentsWindow.isVisible());
        storage.put("editor_entities_x", entitiesWindow.getX());
        storage.put("editor_entities_y", entitiesWindow.getY());
        storage.put("editor_entities_width", entitiesWindow.getWidth());
        storage.put("editor_entities_height", entitiesWindow.getHeight());
        storage.put("editor_entities_visible", entitiesWindow.isVisible());
        storage.save();
        stage.dispose();
        VisUI.dispose();
    }

    @Override
    public void selectionChanged(@Nullable Entity entity) {
        componentsWindow.setEntity(entity);
    }
}
