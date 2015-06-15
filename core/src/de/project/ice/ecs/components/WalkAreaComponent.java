package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.pathlib.PathArea;

public class WalkAreaComponent extends Component implements Pool.Poolable {
    public String areaJSON;
    private PathArea area;


    public void setAreaJSON (String json) {
        areaJSON = json;
        area = PathArea.load(json);
    }

    @Override
    public void reset () {
        areaJSON = "";
        area = null;
    }

    public PathArea getArea () {
        return area;
    }
}
