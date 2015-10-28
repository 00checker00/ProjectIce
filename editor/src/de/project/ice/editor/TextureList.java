package de.project.ice.editor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.kotcrab.vis.ui.widget.VisList;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class TextureList extends VisList<TextureAtlas.AtlasRegion>
{
    private ObjectSet<SelectionChangedListener> selectionChangedListeners = new ObjectSet<SelectionChangedListener>();

    public void addListener(@NotNull SelectionChangedListener listener)
    {
        selectionChangedListeners.add(listener);
    }

    public void removeListener(@NotNull SelectionChangedListener listener)
    {
        selectionChangedListeners.remove(listener);
    }

    public TextureList()
    {
        ObjectSet<String> set = new ObjectSet<String>();
        Array<TextureAtlas.AtlasRegion> array = new Array<TextureAtlas.AtlasRegion>();
        for (TextureAtlas.AtlasRegion region : Assets.getAllRegions())
        {
            if(!set.contains(region.name))
            {
                set.add(region.name);
                array.add(region);
            }
        }
        array.sort(new Comparator<TextureAtlas.AtlasRegion>()
        {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2)
            {
                return o1.name.compareTo(o2.name);
            }
        });
        setItems(array);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                for(SelectionChangedListener listener : selectionChangedListeners)
                {
                    listener.selectionChanged(getSelected(), getSelectedIndex());
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public interface SelectionChangedListener {
        void selectionChanged(TextureAtlas.AtlasRegion newSelection, int newIndex);
    }
}
