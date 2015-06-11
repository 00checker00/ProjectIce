package de.project.ice.editor;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import de.project.ice.ecs.components.AnimationComponent;
import de.project.ice.editor.editors.*;
import de.project.ice.utils.Assets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ComponentTable extends VisTable {
    protected Component component;

    public ComponentTable(Component component) {
        this.component = component;
        defaults().align(Align.topLeft);
        createUi();
    }

    public Component getComponent() {
        return component;
    }

    protected void createUi() {
        addFieldsForObject(component);
        if (component instanceof AnimationComponent) {
            row();
            VisTextButton addAnimationButton = new VisTextButton("Add animation", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    AnimationComponent animationComponent = (AnimationComponent) component;
                    int lastId = 0;
                    for (IntMap.Entry entry : animationComponent.animations) {
                        lastId = entry.key;
                    }
                    animationComponent.animations.put(++lastId, new Assets.AnimationHolder());
                }
            });
            add(addAnimationButton).expandX().fill();
        }
    }

    private void addFieldsForObject(Object o) {
        Class clazz = o.getClass();
        for(Field f : clazz.getFields()) {
            if (Modifier.isFinal(f.getModifiers()))
                continue;

            addField(f, component);
            row();
        }
    }

    private void addField(Field f, Object target) {
        add(Editors.editorForClass(f.getType()).bind(f, target));
    }


}
