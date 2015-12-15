package de.project.ice.hotspot;

import com.badlogic.ashley.core.Entity;
import de.project.ice.IceGame;
import de.project.ice.screens.CursorScreen;

public interface Use
{
    void use(IceGame game, CursorScreen.Cursor cursor);

    abstract class Adapter implements Use
    {
        @Override
        public void use(IceGame game, CursorScreen.Cursor cursor)
        {
            switch (cursor)
            {
                case None:
                    break;
                case Walk:
                    walk(game);
                    break;
                case Look:
                    look(game);
                    break;
                case Speak:
                    speak(game);
                    break;
                case Take:
                    take(game);
                    break;
            }
        }

        protected void walk(IceGame game)
        {
        }

        protected void look(IceGame game)
        {
        }

        protected void speak(IceGame game)
        {
        }

        protected void take(IceGame game)
        {
        }
    }

    class Take extends Adapter
    {
        private final String entityName;
        private final String itemName;

        public Take(String entityName, String itemName)
        {
            this.entityName = entityName;
            this.itemName = itemName;
        }

        @Override
        protected void take(IceGame game)
        {
            Entity entity = game.engine.getEntityByName(entityName);
            if (entity != null)
            {
                game.engine.removeEntity(entity);
                game.inventory.addItem(itemName);
            }
            else
            {
                throw new RuntimeException("Trying to take non-existing entity: " + entityName);
            }
        }
    }

    abstract class With implements Use
    {
        @Override
        public void use(IceGame game, CursorScreen.Cursor cursor)
        {
            use(game);
        }

        protected void use(IceGame game)
        {
        }
    }
}

