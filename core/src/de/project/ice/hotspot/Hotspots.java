package de.project.ice.hotspot;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import de.project.ice.IceGame;
import de.project.ice.Storage;
import de.project.ice.ecs.components.InvisibilityComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.project.ice.screens.CursorScreen.Cursor;

public abstract class Hotspots
{
    @NotNull
    public static final ObjectMap<String, Hotspot> hotspots = new ObjectMap<String, Hotspot>();

    @Nullable
    public static Hotspot get(String id)
    {
        Hotspot hotspot = hotspots.get(id, null);
        if (hotspot == null)
        {
            hotspot = new Hotspot("invalid_id");
        }
        return hotspot;
    }

    private static Hotspot addHotspot(Hotspot hotspot)
    {
        hotspots.put(hotspot.id, hotspot);
        return hotspot;
    }

    public static void loadAllHotspots()
    {
        addHotspot(
                new Hotspot.Builder()
                        .id("Oven")
                        .primaryCursor(Cursor.Look)
                        .use(new Use.Adapter()
                        {
                            @Override
                            protected void look(IceGame game)
                            {
                                game.showMessages(game.strings.get("s3_oven_no_wood"));
                            }
                        })
                        .useWith("Wood", new Use.With()
                        {
                            @Override
                            protected void use(IceGame game)
                            {
                                game.inventory.removeItem("Wood");
                                Entity fire = game.engine.getEntityByName("oven_fire");
                                if (fire != null)
                                {
                                    fire.remove(InvisibilityComponent.class);
                                }
                                Storage.getSavestate().put("scene_03_oven_fire", true);
                            }
                        })
                        .build()
        );
        addHotspot(
                new Hotspot.Builder()
                        .id("PrincessIgloo")
                        .primaryCursor(Cursor.Speak)
                        .secondaryCursor(Cursor.Look)
                        .use(new Use.Adapter()
                        {
                            @Override
                            protected void speak(IceGame game)
                            {
                                game.showDialog("PrincessIgloo");
                            }

                            @Override
                            protected void look(IceGame game)
                            {
                                game.showMessages(game.strings.get("s3_princess_desc"));
                            }
                        })
                        .useWith("Teapot", new Use.With()
                        {
                            @Override
                            protected void use(IceGame game)
                            {
                                Storage.getSavestate().put("PrincessGiveTea", true).flush();
                                game.showDialog("PrincessIgloo");
                                game.inventory.removeItem("Teapot");
                                game.engine.controlSystem.active_item = null;
                            }
                        })
                        .build()
        );
        addHotspot(
                new Hotspot.Builder()
                        .id("Teapot")
                        .primaryCursor(Cursor.Take)
                        .secondaryCursor(Cursor.Look)
                        .use(new Use.Take("teekanne", "Teapot")
                        {

                            @Override
                            protected void take(IceGame game)
                            {
                                if (Storage.getSavestate().getBoolean("scene_03_tea_ready"))
                                {
                                    super.take(game);
                                }
                            }

                            @Override
                            protected void look(IceGame game)
                            {
                                if (Storage.getSavestate().getBoolean("scene_03_tea_ready"))
                                {
                                    game.showMessages(game.strings.get("s3_tea_not_ready"));
                                }
                                else
                                {
                                    game.showMessages(game.strings.get("s3_tea_ready"));
                                }
                            }
                        })
                        .build()
        );
        addHotspot(
                new Hotspot.Builder()
                        .id("Wood")
                        .primaryCursor(Cursor.Take)
                        .secondaryCursor(Cursor.Look)
                        .use(new Use.Take("wood", "Wood")
                        {
                            @Override
                            protected void look(IceGame game)
                            {
                                game.showMessages(game.strings.get("s3_wood_desc"));
                            }
                        })
                        .build()
        );
    }

    static
    {
        // TODO: background loading
        loadAllHotspots();
    }
}
