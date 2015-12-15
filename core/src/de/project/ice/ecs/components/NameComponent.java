package de.project.ice.ecs.components;


public class NameComponent implements IceComponent<NameComponent>
{
    public String name = "";

    @Override
    public void reset()
    {
        name = "";
    }

    @Override
    public void copyTo(NameComponent copy)
    {
        copy.name = name;
    }
}
