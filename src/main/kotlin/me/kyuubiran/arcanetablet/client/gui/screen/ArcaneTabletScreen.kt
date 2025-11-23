package me.kyuubiran.arcanetablet.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import me.kyuubiran.arcanetablet.ArcaneTabletMod
import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.Rect2i
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import org.lwjgl.glfw.GLFW

class ArcaneTabletScreen(
    menu: ArcaneTabletMenu,
    inventory: Inventory,
) : AbstractContainerScreen<ArcaneTabletMenu>(
    menu, menu.inventory,
    Component.literal("")
) {

    private lateinit var searchField: EditBox
    private val searchFieldPos = Rect2i(leftPos + 8, topPos + 7, 160, 11)

    private val player: Player
    private val inventory: Inventory

    companion object {
        private var searchTextContent = ""
    }

    init {
        imageHeight = 217
        player = inventory.player
        this.inventory = inventory
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
    }

    @Suppress("UsePropertyAccessSyntax")
    override fun init() {
        super.init()

        searchField = addRenderableWidget(
            EditBox(
                font, searchFieldPos.x, searchFieldPos.y, searchFieldPos.width, searchFieldPos.height,
                Component.translatable("item.arcanetablet.arcane_tablet")
            ).apply {
                setTextColor(0xFFFFFFFF.toInt())
                setTextColorUneditable(0xFF808080.toInt())
                isBordered = false
                setMaxLength(35)
                value = searchTextContent
                setInitialFocus()
            }
        )
    }

    private val bgRes by lazy {
        ResourceLocation.parse("${ArcaneTabletMod.ID}:textures/gui/arcane_tablet_gui.png")
    }

    private fun setTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, bgRes)
    }

    override fun render(gui: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(gui, mouseX, mouseY, partialTick)
        super.render(gui, mouseX, mouseY, partialTick)
        this.renderTooltip(gui, mouseX, mouseY)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = when (keyCode) {
        GLFW.GLFW_KEY_ESCAPE -> {
            if (searchField.isFocused) searchField.isFocused = false
            else player.closeContainer()
            true
        }

        GLFW.GLFW_KEY_TAB -> {
            searchField.isFocused = true
            true
        }

        else -> super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun renderBg(gui: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        setTexture()

        // table
        val xStart = (width - imageWidth) / 2
        val yStart = (height - imageHeight) / 2
        gui.blit(bgRes, xStart, yStart, 0, 0, imageWidth, imageHeight)

        // craft
        gui.blit(bgRes, leftPos - 75, topPos + 10, 180, 19, 76, 89)
    }

    override fun containerTick() {
        super.containerTick()
    }

}