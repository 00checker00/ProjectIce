package de.project.ice.ecs.components;


public class NameComponent implements IceComponent
{
    public String name = "";

    @Override
    public void reset()
    {
        name = "";
    }
}
