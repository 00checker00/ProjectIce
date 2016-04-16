package de.project.ice.utils

import com.badlogic.gdx.Gdx

val DefaultSkin = Skin("ui/skin.atlas") {
    val baseFontFile = Gdx.files.internal("ui/GosmickSans.ttf")
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
        size = 22
        color = com.badlogic.gdx.graphics.Color.WHITE
        borderWidth = 3f
        borderColor = com.badlogic.gdx.graphics.Color.BLACK
    }
    FreetypeFont("dialogFont") {
        file = baseFontFile
        size = 22
        color = com.badlogic.gdx.graphics.Color.WHITE
        borderWidth = 3f
        borderColor = com.badlogic.gdx.graphics.Color.BLACK
    }
    FreetypeFont("menuFont") {
        file = Gdx.files.internal("ui/VTC-GarageSale.ttf")
        color = com.badlogic.gdx.graphics.Color.WHITE
        size = 48
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
        up = skin.getDrawable("ui_button_shadow")
        down = skin.getDrawable("ui_buttons_normal")
        fontColor = skin.getColor("dialogChoiceFont")
    }
    TextButtonStyle("menuButton") {
        font = skin.getFont("menuFont")
        up = skin.getDrawable("ui_button_shadow")
        fontColor = com.badlogic.gdx.graphics.Color.valueOf("#00ccff")
        overFontColor = com.badlogic.gdx.graphics.Color.valueOf("#ff99cc")
        downFontColor = com.badlogic.gdx.graphics.Color.valueOf("#b2e3e5")
    }

}