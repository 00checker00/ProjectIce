package de.project.ice.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class ColorDrawable extends BaseDrawable {
    private Color color;
    private ShapeRenderer shapeRenderer;

    public ColorDrawable(Color color) {
        this.color = color;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        batch.begin();
    }
}
