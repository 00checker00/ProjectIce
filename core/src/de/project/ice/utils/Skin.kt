package de.project.ice.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.*

class FreetypeFontBuilder: FreeTypeFontGenerator.FreeTypeFontParameter() {
    var file: FileHandle? = null
}

class ColorBuilder {
    private var color = Color.WHITE;

    var r: Float get() = color.a; set(value) { color.set(  value, color.g, color.b, color.a) }
    var g: Float get() = color.r; set(value) { color.set(color.r,   value, color.b, color.a) }
    var b: Float get() = color.g; set(value) { color.set(color.r, color.g,   value, color.a) }
    var a: Float get() = color.b; set(value) { color.set(color.r, color.g, color.b,   value) }
    var hex: String get() = color.toString(); set(value) { color = Color.valueOf(value) }

    internal fun build(): Color = color
}

class SkinBuilder {
    val skin: Skin

    internal constructor() {
        skin = Skin()
    }

    internal constructor(atlas: TextureAtlas) {
        skin = Skin(atlas)
    }

    fun Color(name: String = "default", init: ColorBuilder.()->Unit) {
        skin.add(name, ColorBuilder().apply { init() }.build())
    }

    fun FreetypeFont(name: String = "default", init: FreetypeFontBuilder.()->Unit) {
        val param = FreetypeFontBuilder().apply { init() }
        if (param.file == null)
            throw IllegalStateException("FreetypeFont file not set!")

        val generator = FreeTypeFontGenerator(param.file)
        val font = generator.generateFont(param)
        generator.dispose()

        skin.add(name, font)
    }

    fun ButtonStyle(name: String = "default", init: Button.ButtonStyle.()->Unit) {
        skin.add(name, Button.ButtonStyle().apply { init() })
    }
    fun CheckBoxStyle(name: String = "default", init: CheckBox.CheckBoxStyle.()->Unit) {
        skin.add(name, CheckBox.CheckBoxStyle().apply { init() })
    }
    fun ImageButtonStyle(name: String = "default", init: ImageButton.ImageButtonStyle.()->Unit) {
        skin.add(name, ImageButton.ImageButtonStyle().apply { init() })
    }
    fun ImageTextButtonStyle(name: String = "default", init: ImageTextButton.ImageTextButtonStyle.()->Unit) {
        skin.add(name, ImageTextButton.ImageTextButtonStyle().apply { init() })
    }
    fun LabelStyle(name: String = "default", init: Label.LabelStyle.()->Unit) {
        skin.add(name, Label.LabelStyle().apply { init() })
    }
    fun ProgressBarStyle(name: String = "default", init: ProgressBar.ProgressBarStyle.()->Unit) {
        skin.add(name, ProgressBar.ProgressBarStyle().apply { init() })
    }
    fun ScrollPaneStyle(name: String = "default", init: ScrollPane.ScrollPaneStyle.()->Unit) {
        skin.add(name, ScrollPane.ScrollPaneStyle().apply { init() })
    }
    fun SelectBoxStyle(name: String = "default", init: SelectBox.SelectBoxStyle.()->Unit) {
        skin.add(name, SelectBox.SelectBoxStyle().apply { init() })
    }
    fun SliderStyle(name: String = "default", init: Slider.SliderStyle.()->Unit) {
        skin.add(name, Slider.SliderStyle().apply { init() })
    }
    fun SplitPaneStyle(name: String = "default", init: SplitPane.SplitPaneStyle.()->Unit) {
        skin.add(name, SplitPane.SplitPaneStyle().apply { init() })
    }
    fun TextButtonStyle(name: String = "default", init: TextButton.TextButtonStyle.()->Unit) {
        skin.add(name, TextButton.TextButtonStyle().apply { init() })
    }
    fun TextFieldStyle(name: String = "default", init: TextField.TextFieldStyle.()->Unit) {
        skin.add(name, TextField.TextFieldStyle().apply { init() })
    }
    fun TextTooltipStyle(name: String = "default", init: TextTooltip.TextTooltipStyle.()->Unit) {
        skin.add(name, TextTooltip.TextTooltipStyle().apply { init() })
    }
    fun TouchpadStyle(name: String = "default", init: Touchpad.TouchpadStyle.()->Unit) {
        skin.add(name, Touchpad.TouchpadStyle().apply { init() })
    }
    fun TreeStyle(name: String = "default", init: Tree.TreeStyle.()->Unit) {
        skin.add(name, Tree.TreeStyle().apply { init() })
    }
    fun WindowStyle(name: String = "default", init: Window.WindowStyle.()->Unit) {
        skin.add(name, Window.WindowStyle().apply { init() })
    }
    inline fun <reified T> Resource(name: String = "default", vararg constructorArgs: Any?, init: T.()->Unit) {
        val constructor = T::class.constructors.firstOrNull { it.parameters.count() == 0 }
                ?: throw IllegalStateException("Resource type must have a default constructor!")

        var instance = constructor.call(constructorArgs)
        Resource(instance, name, init)
    }
    inline fun <T> Resource(obj: T, name: String = "default", init: T.()->Unit) {
        skin.add(name, obj.apply { init() })
    }

    internal fun build() = skin
}

fun Skin(init: SkinBuilder.()->Unit): Skin {
    return SkinBuilder().apply { init() }.build()
}

fun Skin(atlas: TextureAtlas, init: SkinBuilder.()->Unit): Skin {
    return SkinBuilder(atlas).apply { init() }.build()
}

fun Skin(atlas: String, init: SkinBuilder.()->Unit): Skin {
    return SkinBuilder(TextureAtlas(Gdx.files.internal(atlas))).apply { init() }.build()
}

fun Skin.getDefaultFont() = getFont("default")