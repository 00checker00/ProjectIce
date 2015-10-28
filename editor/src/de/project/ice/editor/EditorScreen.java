package de.project.ice.editor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
import de.project.ice.editor.undoredo.UndoRedoManager;
import de.project.ice.screens.BaseScreenAdapter;
import de.project.ice.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

import static de.project.ice.utils.SceneLoader.SceneProperties;
import static de.project.ice.utils.SceneLoader.ScenePropertiesBuilder;

public class EditorScreen extends BaseScreenAdapter implements EntitiesWindow.SelectionListener
{
    private static final String VERSION = "0.0.1";
    private final PathScreen pathScreen;
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
    @NotNull
    private UndoRedoManager undoRedoManager = new UndoRedoManager();
    @NotNull
    private SceneProperties sceneProperties;
    private String filename = null;
    private String storedState = null;
    private VisTextButton redoBtn;
    private VisTextButton undoBtn;

    public EditorScreen(@NotNull final IceGame game)
    {
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

        pathScreen = new PathScreen(game);
        if (storage.getBoolean("editor_pathscreen_visible", true))
        {
            game.addScreen(pathScreen);
        }

        entitiesWindow = new EntitiesWindow(game.engine, undoRedoManager);
        entitiesWindow.setSelectionListener(this);
        entitiesWindow.setPosition(storage.getFloat("editor_entities_x", 0f), storage.getFloat("editor_entities_y", 0f));
        entitiesWindow.setSize(storage.getFloat("editor_entities_width", 200f), storage.getFloat("editor_entities_height", 400f));
        entitiesWindow.setVisible(storage.getBoolean("editor_entities_visible", true));
        stage.addActor(entitiesWindow);

        componentsWindow = new ComponentsWindow(game.engine, undoRedoManager);
        componentsWindow.setPosition(storage.getFloat("editor_components_x", 0f), storage.getFloat("editor_components_y", 0f));
        componentsWindow.setSize(storage.getFloat("editor_components_width", 400f), storage.getFloat("editor_components_height", 400f));
        componentsWindow.setVisible(storage.getBoolean("editor_components_visible", true));
        stage.addActor(componentsWindow);

        sceneProperties = new ScenePropertiesBuilder().engine(game.engine).create();

        inputProcessor = new DelegatingInputProcessor(stage)
        {
            @Override
            public boolean keyDown(int keycode)
            {
                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                {
                    switch (keycode)
                    {
                        case Input.Keys.S:
                            save();
                            return true;

                        case Input.Keys.O:
                            open();
                            return true;

                        case Input.Keys.N:
                            newScene();
                            return true;

                        case Input.Keys.P:
                            if (game.isScreenVisible(pathScreen))
                            {
                                game.removeScreen(pathScreen, false);
                            }
                            else
                            {
                                game.addScreen(pathScreen);
                            }
                            return true;

                        case Input.Keys.Z:
                            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                            {
                                undoRedoManager.redo();
                            }
                            else
                            {
                                undoRedoManager.undo();
                            }
                            return true;

                        case Input.Keys.H:
                            hideAllWindows();
                            return true;

                        case Input.Keys.A:
                            showAllWindows();
                            return true;

                    }
                }
                return super.keyDown(keycode);
            }
        };

        Gdx.graphics.setDisplayMode(storage.getInteger("editor_screen_width", 800),
                storage.getInteger("editor_screen_height", 600),
                false);
    }

    private void hideAllWindows()
    {
        entitiesWindow.setVisible(false);
        componentsWindow.setVisible(false);
        game.removeScreen(pathScreen, false);
    }

    private void showAllWindows()
    {
        entitiesWindow.setVisible(true);
        componentsWindow.setVisible(true);
        game.addScreen(pathScreen);
    }

    private void createMenus()
    {
        Menu fileMenu = new Menu("File");
        Menu windowMenu = new Menu("Window");
        Menu assetsMenu = new Menu("Assets");
        Menu testMenu = new Menu("Test");
        Menu helpMenu = new Menu("Help");

        fileMenu.addItem(new MenuItem("New Scene", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                newScene();
            }
        }).setShortcut("Ctrl + N"));
        fileMenu.addItem(new MenuItem("Open Scene", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                open();
            }
        }).setShortcut("Ctrl + O"));
        fileMenu.addItem(new MenuItem("Save Scene", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                save(false);
            }
        }).setShortcut("Ctrl + S"));
        fileMenu.addItem(new MenuItem("Save Scene As", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                save(true);
            }
        }).setShortcut("Ctrl + Shift + S"));

        fileMenu.addItem(new MenuItem("Scene Properties", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                ScenePropertiesDialog scenePropertiesDialog = new ScenePropertiesDialog(sceneProperties);
                scenePropertiesDialog.addListener(new DialogListener<SceneProperties>()
                {
                    @Override
                    public void onResult(SceneProperties result)
                    {
                        sceneProperties = result;

                        game.engine.soundSystem.unloadSounds();
                        game.engine.soundSystem.stopMusic();
                        game.engine.soundSystem.playMusic(result.music());

                        for (String sound : result.sounds())
                        {
                            game.engine.soundSystem.loadSound(sound);
                        }
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                }).show(stage);
            }
        }));
        fileMenu.addItem(new MenuItem("Screenshot", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                ScreenshotFactory.saveScreenshot();
            }
        }));
        fileMenu.addSeparator();
        fileMenu.addItem(new MenuItem("Quit", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                quit();
            }
        }));

        windowMenu.addItem(new MenuItem("Show/Hide Entities Window", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                entitiesWindow.setVisible(!entitiesWindow.isVisible());
            }
        }));
        windowMenu.addItem(new MenuItem("Show/Hide Components Window", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                componentsWindow.setVisible(!componentsWindow.isVisible());
            }
        }));

        windowMenu.addItem(new MenuItem("Show/Hide Path Screen ", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (game.isScreenVisible(pathScreen))
                {
                    game.removeScreen(pathScreen, false);
                }
                else
                {
                    game.addScreen(pathScreen);
                }

            }
        }).setShortcut("Ctrl + P"));


        windowMenu.addItem(new MenuItem("Show All Windows", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                showAllWindows();
            }
        }));

        windowMenu.addItem(new MenuItem("Hide All Windows", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                hideAllWindows();
            }
        }));

        assetsMenu.addItem(new MenuItem("Reload Assets", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Assets.clear();
                Assets.finishAll();
            }
        }));

        startPlaytest = new MenuItem("Start PlayTest", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                stopPlaytest.setDisabled(false);
                startPlaytest.setDisabled(true);
                storeState();
                game.resumeGame();
            }
        });
        testMenu.addItem(startPlaytest);

        stopPlaytest = new MenuItem("Stop PlayTest", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                stopPlaytest.setDisabled(true);
                startPlaytest.setDisabled(false);
                restoreState();
                game.pauseGame();
            }
        });
        stopPlaytest.setDisabled(true);
        testMenu.addItem(stopPlaytest);

        helpMenu.addItem(new MenuItem("About", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                DialogUtils.showOKDialog(stage, "about", "ProjectIce Editor version " + VERSION);
            }
        }));

        menuBar.addMenu(fileMenu);
        menuBar.addMenu(windowMenu);
        menuBar.addMenu(assetsMenu);
        menuBar.addMenu(testMenu);
        menuBar.addMenu(helpMenu);

        undoBtn = new VisTextButton("Undo", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                undoRedoManager.undo();
            }
        });
        menuBar.getTable().add(undoBtn);

        redoBtn = new VisTextButton("Redo", new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                undoRedoManager.redo();
            }
        });
        menuBar.getTable().add(redoBtn);
    }

    private void storeState()
    {
        StringWriter writer = new StringWriter();
        XmlWriter xml = new XmlWriter(writer);
        serializeScene(xml);
        storedState = writer.toString();
        try
        {
            FileWriter fileWriter = new FileWriter("$stored_state$");
            fileWriter.write(storedState);
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void restoreState()
    {
        game.inventory.items.clear();
        if (storedState == null)
        {
            return;
        }
        game.engine.removeAllEntities();

        // Probably not "right", but works?
        // Otherwise engine will "randomly" delete
        // entities from the new scene
        game.engine.update(0);

        try
        {
            SceneLoader.loadScene(game.engine, storedState);
            storedState = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SceneLoader.LoadException e)
        {
            e.printStackTrace();
        }
    }

    private void clear()
    {
        filename = null;
        game.engine.removeAllEntities();
        Assets.clear();

        game.pauseGame();
    }

    private void newScene()
    {
        clear();
        DialogUtils.showInputDialog(stage, "Scene Name", "Name", new InputDialogListener()
        {
            @Override
            public void finished(String name)
            {
                sceneProperties = new ScenePropertiesBuilder().engine(game.engine).name(name).create();
            }

            @Override
            public void canceled()
            {
            }
        });
    }

    private void open()
    {
        clear();

        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(new FileHandle("."));
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.getName().endsWith(".scene") || pathname.isDirectory();
            }
        });
        fileChooser.setListener(new FileChooserAdapter()
        {
            @Override
            public void selected(FileHandle file)
            {
                try
                {
                    sceneProperties = SceneLoader.loadScene(game.engine, file.read());
                    filename = file.file().getCanonicalFile().getAbsolutePath();
                }
                catch (IOException e)
                {
                    DialogUtils.showErrorDialog(stage, "Couldn't save the scene.", e);
                }
                catch (SceneLoader.LoadException e)
                {
                    DialogUtils.showErrorDialog(stage, "Scene files is invalid", e);
                }
            }
        });
        stage.addActor(fileChooser.fadeIn());
    }

    private void save()
    {
        save(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
    }

    private void save(boolean forceFileDialog)
    {
        if (filename == null || forceFileDialog)
        {
            FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
            fileChooser.setDirectory(filename != null ? new FileHandle(filename).parent() : new FileHandle("."));
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File pathname)
                {
                    return pathname.getName().endsWith(".scene") || pathname.isDirectory();
                }
            });
            fileChooser.setListener(new FileChooserAdapter()
            {
                @Override
                public void selected(FileHandle file)
                {
                    try
                    {
                        filename = file.file().getCanonicalFile().getAbsolutePath();
                        if (!filename.endsWith(".scene"))
                        {
                            filename += ".scene";
                        }
                        save();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            stage.addActor(fileChooser.fadeIn());
            return;
        }

        try
        {
            if (storedState == null)
            {
                File file = new File(filename);
                if (file.exists())
                {
                    file.renameTo(new File(file.getParent() + "/" + file.getName() + ".bak"));
                }
                XmlWriter xml = new XmlWriter(new FileWriter(filename));
                serializeScene(xml);
            }
            else
            {
                FileWriter writer = new FileWriter(filename);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(storedState);
                bufferedWriter.close();
            }
        }
        catch (IOException e)
        {
            DialogUtils.showErrorDialog(stage, "Couldn't access the file.", e);
        }

    }

    private void serializeScene(XmlWriter xml)
    {
        try
        {
            new SceneWriter.Builder()
                    .engine(game.engine)
                    .writer(xml)
                    .sceneName(sceneProperties.name())
                    .onloadScript(sceneProperties.onloadScript())
                    .create()
                    .serializeScene();
        }
        catch (IOException e)
        {
            DialogUtils.showErrorDialog(stage, "Couldn't save the scene.", e);
        }
    }


    private void quit()
    {
        Gdx.app.exit();
    }

    @Override
    public int getPriority()
    {
        return 1;
    }

    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
        Storage.Global storage = Storage.getGlobal();
        storage.put("editor_screen_width", width);
        storage.put("editor_screen_height", height);
        storage.save();
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
        if (filename == null)
        {
            Gdx.graphics.setTitle("Editor (Here be dragons)");
        }
        else
        {
            Gdx.graphics.setTitle("Editor (Here be dragons) [" + filename + "]");
        }
        undoBtn.setDisabled(!undoRedoManager.canUndo());
        redoBtn.setDisabled(!undoRedoManager.canRedo());
    }

    public void render()
    {
        stage.draw();
    }

    public void dispose()
    {
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
        storage.put("editor_pathscreen_visible", game.isScreenVisible(pathScreen));
        storage.save();
        stage.dispose();
        VisUI.dispose();
    }

    @Override
    public void selectionChanged(@Nullable Entity entity)
    {
        componentsWindow.setEntity(entity);
    }
}
