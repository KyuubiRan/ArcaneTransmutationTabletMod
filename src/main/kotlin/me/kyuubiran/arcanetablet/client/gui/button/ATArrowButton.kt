package me.kyuubiran.arcanetablet.client.gui.button

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import org.lwjgl.opengl.GL11

class ATArrowButton : AbstractATButton {
    constructor(x: Int, y: Int, onPress: OnPress) : super(x, y, 18, 18, onPress)

    override fun renderWidget(gui: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (isHoveredOrFocused) {
            texture?.let {
                RenderSystem.enableBlend()
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                gui.blit(it, this.x, this.y, textureX, textureY, width, height)
                RenderSystem.disableBlend()
            }
        }
    }
}