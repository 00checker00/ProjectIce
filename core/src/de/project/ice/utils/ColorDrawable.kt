package de.project.ice.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable

class ColorDrawable(private val color: Color, private val blendingEnabled: Boolean = false) : BaseDrawable() {
    private val shapeRenderer = ShapeRenderer()

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        batch.end()
        if (blendingEnabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.rect(x, y, width, height)
        shapeRenderer.end()
        batch.begin()
    }
}
