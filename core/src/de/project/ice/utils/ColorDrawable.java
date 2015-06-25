package de.project.ice.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class ColorDrawable extends BaseDrawable {
    private Color color;
    private ShapeRenderer shapeRenderer;
    private boolean blendingEnabled = false;

    public ColorDrawable(Color color) {
        this.color = color;
        shapeRenderer = new ShapeRenderer();
    }

    public ColorDrawable(Color color, boolean blendingEnabled) {
        this(color);
        this.blendingEnabled = blendingEnabled;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.end();
        if (blendingEnabled)
            Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        batch.begin();
    }
}
