package me.kyuubiran.arcanetablet.client.gui.button

class ATHighlightButton : AbstractATButton {

    constructor(x: Int, y: Int) : super(x, y, 14, 14)

    constructor(x: Int, y: Int, w: Int, h: Int, onPress: OnPress = OnPress {}) : super(x, y, w, h, onPress)
}