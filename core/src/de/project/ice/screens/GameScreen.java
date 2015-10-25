package de.project.ice.screens;

import com.badlogic.ashley.core.EntitySystem;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;

import static de.project.ice.config.Config.INVENTORY_KEY;
import static de.project.ice.config.Config.MENU_KEY;

/**
 * Input Handling here...
 */
public class GameScreen extends BaseScreenAdapter
{
    @NotNull
    private final IceEngine engine;
    private final EntitySystem[] SystemsToPause;

    public GameScreen(@NotNull final IceGame game, @NotNull final IceEngine engine)
    {
        super(game);
        this.engine = engine;

        // Load "Scene01" by loading the scene3_load script
        // this.scriptManager.loadScript(scene3_load.class);

        inputProcessor = new DelegatingBlockingInputProcessor(engine.controlSystem)
        {
            @Override
            public boolean mouseMoved(int screenX, int screenY)
            {
                super.mouseMoved(screenX, screenY);
                game.setPrimaryCursor(engine.controlSystem.primaryCursor);
                game.setSecondaryCursor(engine.controlSystem.secondaryCursor);
                return true;
            }

            @Override
            public boolean keyDown(int keycode)
            {
                switch (keycode)
                {
                    case MENU_KEY:
                        GameScreen.this.game.addScreen(new PauseScreen(GameScreen.this.game));
                        return true;

                    case INVENTORY_KEY:
                        GameScreen.this.game.addScreen(new InventoryScreen(GameScreen.this.game));
                        return true;

                    default:
                        return super.keyDown(keycode);
                }
            }
        };
        SystemsToPause = new EntitySystem[]{
                engine.stateSystem,
                engine.cameraSystem,
                engine.animationSystem,
                engine.controlSystem,
                engine.movementSystem,
                engine.scriptingSystem
        };
    }

    @Override
    public void update(float delta)
    {
        engine.update(delta);
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void pause()
    {
    }

    public void pauseGame()
    {
        for (EntitySystem system : SystemsToPause)
        {
            system.setProcessing(false);
        }
    }

    public void resumeGame()
    {
        for (EntitySystem system : SystemsToPause)
        {
            system.setProcessing(true);
        }
    }

    @Override
    public int getPriority()
    {
        return 1000;
    }
}
