package de.project.ice.utils

import com.badlogic.gdx.Gdx

val DefaultSkin = Skin("ui/skin.atlas") {
    val baseFontFile = Gdx.files.internal("ui/LazySpringDay.ttf")
    Color("dialogBack") {
        r = 1.0f
        g = 1.0f
        b = 1.0f
        a = 1.0f
    }
    Color("dialogChoiceBack") {
        hex = "#FFFFFFFF"
    }
    Color("dialogChoiceFont") {
        hex = "#000000"
    }
    FreetypeFont {
        file = baseFontFile
    }
    FreetypeFont("cursor") {
        file = baseFontFile
        size = 16
        color = com.badlogic.gdx.graphics.Color.WHITE
        borderWidth = 3f
        borderColor = com.badlogic.gdx.graphics.Color.BLACK
    }
    FreetypeFont("dialogFont") {
        file = baseFontFile
        color = com.badlogic.gdx.graphics.Color.BLACK
    }
    LabelStyle {
        font = skin.getDefaultFont()
    }
    LabelStyle("dialogText") {
        font = skin.getFont("dialogFont")
    }
    ScrollPaneStyle("dialogText") {
        background = ColorDrawable(skin.getColor("dialogBack"))
    }
    TextButtonStyle("dialogChoice") {
        font = skin.getFont("dialogFont")
        up = ColorDrawable(com.badlogic.gdx.graphics.Color.BLUE)
        fontColor = skin.getColor("dialogChoiceFont")
    }
}