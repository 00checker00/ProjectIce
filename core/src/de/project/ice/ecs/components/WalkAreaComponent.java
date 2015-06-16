package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.pathlib.PathArea;

import static de.project.ice.config.Config.*;

public class WalkAreaComponent extends Component implements Pool.Poolable {
    public float scale = 1f;
    public String areaJSON;
    private PathArea area;

    public void setAreaJSON (String json) {
        areaJSON = json;
        try {
            area = PathArea.load(json, scale*PIXELS_TO_METRES);
        }catch (Exception ignore){}
    }

    public void setScale(float scale) {
        this.scale = scale;
        setAreaJSON(areaJSON);
    }

    @Override
    public void reset () {
        areaJSON = "";
        area = null;
        scale = 1f;
    }

    public PathArea getArea () {
        return area;
    }
}
