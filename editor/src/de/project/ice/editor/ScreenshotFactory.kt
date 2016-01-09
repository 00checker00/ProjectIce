package de.project.ice.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils

object ScreenshotFactory {

    fun saveScreenshot() {
        try {
            var counter = 1
            var fh: FileHandle
            do {
                fh = FileHandle("screenshot" + counter++ + ".png")
            } while (fh.exists())
            val pixmap = getScreenshot(0, 0, Gdx.graphics.width, Gdx.graphics.height, true)
            PixmapIO.writePNG(fh, pixmap)
            pixmap.dispose()
        } catch (e: Exception) {
        }

    }

    private fun getScreenshot(x: Int, y: Int, w: Int, h: Int, yDown: Boolean): Pixmap {
        val pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h)

        if (yDown) {
            // Flip the pixmap upside down
            val pixels = pixmap.pixels
            val numBytes = w * h * 4
            val lines = ByteArray(numBytes)
            val numBytesPerLine = w * 4
            for (i in 0..h - 1) {
                pixels.position((h - i - 1) * numBytesPerLine)
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine)
            }
            pixels.clear()
            pixels.put(lines)
        }

        return pixmap
    }
}