package de.project.ice.hotspot;

import com.badlogic.gdx.utils.ObjectMap;
import de.project.ice.IceGame;
import de.project.ice.utils.Pair;
import org.jetbrains.annotations.NotNull;

import static de.project.ice.screens.CursorScreen.Cursor;

public final class Hotspot
{
    @NotNull
    public final String id;
    @NotNull
    public final Cursor primaryCursor;
    @NotNull
    public final Cursor secondaryCursor;
    @NotNull
    public final Use cursorUse;
    @NotNull
    private final ObjectMap<String, Use> useWithMap;

    private Hotspot(@NotNull String id,
                    @NotNull Cursor primaryCursor,
                    @NotNull Cursor secondaryCursor,
                    @NotNull Use cursorUse,
                    ObjectMap<String, Use> useWithMap)
    {
        this.id = id;
        this.primaryCursor = primaryCursor;
        this.secondaryCursor = secondaryCursor;
        this.cursorUse = cursorUse;
        this.useWithMap = useWithMap;
    }

    public Hotspot(@NotNull String id,
                   @NotNull Cursor primaryCursor,
                   @NotNull Cursor secondaryCursor,
                   @NotNull Use cursorUse,
                   Pair<String, ? extends Use>... usages)
    {
        this.id = id;
        this.primaryCursor = primaryCursor;
        this.secondaryCursor = secondaryCursor;
        this.cursorUse = cursorUse;
        this.useWithMap = new ObjectMap<String, Use>();
        for (Pair<String, ? extends Use> usage : usages)
        {
            useWithMap.put(usage.getFirst(), usage.getSecond());
        }
    }

    public Hotspot(@NotNull String id,
                   @NotNull Cursor primaryCursor,
                   @NotNull Use cursorUse,
                   Pair<String, ? extends Use>... usages)
    {
        this(id, primaryCursor, Cursor.None, cursorUse, usages);
    }

    public Hotspot(@NotNull String id,
                   Pair<String, ? extends Use>... usages)
    {
        this(id,
                Cursor.None,
                Cursor.None,
                new Use()
                {
                    @Override
                    public void use(IceGame game, Cursor cursor)
                    {
                    }
                },
                usages);
    }

    @SuppressWarnings("unchecked")
    public Hotspot(@NotNull String id)
    {
        this(id,
                Cursor.None,
                Cursor.None,
                new Use()
                {
                    @Override
                    public void use(IceGame game, Cursor cursor)
                    {
                    }
                });
    }

    public boolean canUseWith(@NotNull String item)
    {
        return useWithMap.containsKey(item);
    }

    public void useWith(@NotNull IceGame game, @NotNull String item)
    {
        useWithMap.get(item, new Use.With()
        {
        }).use(game, Cursor.None);
    }

    public void use(IceGame game, Cursor cursor)
    {
        cursorUse.use(game, cursor);
    }

    public static class Builder
    {
        private String id = null;
        private Cursor primaryCursor = null;
        private Cursor secondaryCursor = Cursor.None;
        private Use cursorUse = null;
        private final ObjectMap<String, Use> useWithMap = new ObjectMap<String, Use>();

        public Builder()
        {
        }

        public
        @NotNull
        Builder id(@NotNull String id)
        {
            this.id = id;
            return this;
        }

        public
        @NotNull
        Builder primaryCursor(@NotNull Cursor primaryCursor)
        {
            this.primaryCursor = primaryCursor;
            return this;
        }

        public
        @NotNull
        Builder secondaryCursor(@NotNull Cursor secondaryCursor)
        {
            this.secondaryCursor = secondaryCursor;
            return this;
        }

        public
        @NotNull
        Builder use(@NotNull Use cursorUse)
        {
            this.cursorUse = cursorUse;
            return this;
        }

        public
        @NotNull
        Builder useWith(@NotNull String withItem, @NotNull Use use)
        {
            useWithMap.put(withItem, use);
            return this;
        }

        public
        @NotNull
        Hotspot build()
        {
            if (id == null)
            {
                throw new RuntimeException("Trying to build a hotspot without an id");
            }
            if (primaryCursor == null)
            {
                throw new RuntimeException("Trying to build hotspot without a primary cursor");
            }
            if (cursorUse == null)
            {
                throw new RuntimeException("Trying to build hotspot without a HotspotUse");
            }

            Hotspot hotspot = new Hotspot(id, primaryCursor, secondaryCursor, cursorUse, useWithMap);
            return hotspot;
        }
    }
}
