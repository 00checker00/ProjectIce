package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public interface IceComponent<T> extends Component, Pool.Poolable
{
    void copyTo(T copy);
}
