package de.project.ice.editor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.project.ice.IceGame;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.TextureComponent;
import de.project.ice.ecs.components.TransformComponent;
import de.project.ice.ecs.systems.RenderingSystem;
import de.project.ice.screens.BaseScreenAdapter;
import org.jetbrains.annotations.NotNull;

import static de.project.ice.config.Config.PIXELS_TO_METRES;

public class EditGameScreen extends BaseScreenAdapter
{
    @NotNull
    private InputProcessor inputProcessor;

    public EditGameScreen(@NotNull final IceGame game)
    {
        super(game);
        inputProcessor = new InputAdapter()
        {
            private TransformComponent dragComponent = null;
            private float dragOriginX = 0f;
            private float dragOriginY = 0f;
            private Vector2 cameraDragDown = new Vector2();
            private OrthographicCamera cameraDrag = null;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button)
            {
                dragComponent = null;
                cameraDrag = null;
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                if (game.isGamePaused())
                {
                    ImmutableArray<Entity> cameras = game.engine.getEntitiesFor(Families.camera);
                    if (cameras.size() == 0)
                    {
                        return false;
                    }

                    Entity activeCamera = cameras.first();
                    CameraComponent cameraComponent = Components.camera.get(activeCamera);
                    Vector3 coords = cameraComponent.camera.unproject(new Vector3(screenX, screenY, 0f));
                    if (button == Input.Buttons.LEFT)
                    {
                        Array<Entity> entities = new Array<Entity>(game.engine.getEntitiesFor(Families.renderable).toArray());
                        entities.sort(new RenderingSystem.RenderingComparator());
                        entities.reverse();
                        for (Entity entity : entities)
                        {
                            TransformComponent transform = Components.transform.get(entity);
                            TextureComponent texture = Components.texture.get(entity);

                            if (!texture.region.isValid())
                            {
                                continue;
                            }

                            float width = texture.region.data.getRegionWidth() * PIXELS_TO_METRES;
                            float height = texture.region.data.getRegionHeight() * PIXELS_TO_METRES;

                            if (new Rectangle(transform.pos.x, transform.pos.y, width, height).contains(coords.x, coords.y))
                            {
                                dragComponent = transform;
                                dragOriginX = coords.x - transform.pos.x;
                                dragOriginY = coords.y - transform.pos.y;
                                return false;
                            }
                        }
                        return false;
                    }
                    else if (button == Input.Buttons.MIDDLE)
                    {
                        cameraDragDown.set(screenX, screenY);
                        cameraDrag = cameraComponent.camera;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer)
            {
                if (dragComponent != null)
                {
                    ImmutableArray<Entity> cameras = game.engine.getEntitiesFor(Families.camera);
                    if (cameras.size() == 0)
                    {
                        return false;
                    }

                    Entity activeCamera = cameras.first();
                    CameraComponent cameraComponent = Components.camera.get(activeCamera);
                    Vector3 coords = cameraComponent.camera.unproject(new Vector3(screenX, screenY, 0f));

                    dragComponent.pos.set(new Vector2(coords.x - dragOriginX, coords.y - dragOriginY));
                }
                else if (cameraDrag != null)
                {
                    cameraDrag.translate(new Vector2(screenX, screenY).sub(cameraDragDown).scl(PIXELS_TO_METRES).scl(-1, 1));
                    cameraDragDown.set(screenX, screenY);
                }
                return true;
            }
        };
    }

    @Override
    public int getPriority()
    {
        return 3;
    }

    @NotNull
    @Override
    public InputProcessor getInputProcessor()
    {
        return inputProcessor;
    }
}
