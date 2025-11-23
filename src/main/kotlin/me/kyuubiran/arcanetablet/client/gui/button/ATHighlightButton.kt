package me.kyuubiran.arcanetablet.client.gui.button

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import org.lwjgl.opengl.GL11

class ATHighlightButton : AbstractATButton {

    constructor(x: Int, y: Int) : super(x, y, 14, 14)

    constructor(x: Int, y: Int, w: Int, h: Int, onPress: OnPress = OnPress {}) : super(x, y, w, h, onPress)

    override fun renderWidget(gui: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (isHoveredOrFocused) {
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            gui.fill(this.x, this.y, this.x + width, this.y + height, 0x80FFFFFF.toInt())
            RenderSystem.disableBlend();
        }
    }
}