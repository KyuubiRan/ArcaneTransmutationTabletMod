package me.kyuubiran.arcanetablet.client.gui.button

import com.mojang.blaze3d.systems.RenderSystem
import me.kyuubiran.arcanetablet.util.ATExtensions.eEmc
import me.kyuubiran.arcanetablet.util.ATUtils
import moze_intel.projecte.api.capabilities.IKnowledgeProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import org.lwjgl.opengl.GL11
import java.math.BigDecimal
import java.math.RoundingMode

class ATItemButton : AbstractATButton {

    companion object {
        private val ONE_TENTH: BigDecimal = BigDecimal.valueOf(1L, 1)
    }

    private var provider: IKnowledgeProvider

    var item: ItemStack = ItemStack.EMPTY

    constructor(x: Int, y: Int, provider: IKnowledgeProvider) : super(x, y, 18, 18, OnPress { }) {
        this.provider = provider
    }

    private val extractionCount: String
        get() {
            val emc = item.eEmc.takeIf { it > 0 } ?: return "???"

            val d: BigDecimal = BigDecimal(provider.emc).setScale(1, RoundingMode.DOWN)
                .divide(BigDecimal.valueOf(emc), RoundingMode.DOWN)

            val label = if (d >= BigDecimal.ONE) {
                ATUtils.formatExtractionCount(d.setScale(0, RoundingMode.DOWN))
            } else if (d >= ONE_TENTH) {
                d.toString()
            } else {
                ""
            }

            return label
        }

    override fun renderWidget(gui: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (!visible) return

        if (item.isEmpty) {
            if (isHoveredOrFocused) {
                RenderSystem.enableBlend()
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                gui.fill(x, y, x + width, y + height, 0x80FFFFFF.toInt())
                RenderSystem.disableBlend();
            }
        } else {
            gui.renderItem(item, x, y)
            val text = extractionCount
            val font = Minecraft.getInstance().font
            gui.pose().run {
                pushPose()

                translate((x + 17).toDouble(), (y + 12).toDouble(), 200.0)
                scale(0.5f, 0.5f, 0.5f)
                gui.drawString(font, text, -font.width(text), 0, 0xFFFFFFFF.toInt())

                popPose()
            }
        }

    }
}