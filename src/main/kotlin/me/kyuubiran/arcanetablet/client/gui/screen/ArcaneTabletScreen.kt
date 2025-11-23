package me.kyuubiran.arcanetablet.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import me.kyuubiran.arcanetablet.ArcaneTabletMod
import me.kyuubiran.arcanetablet.client.gui.button.ATArrowButton
import me.kyuubiran.arcanetablet.client.gui.button.ATHighlightButton
import me.kyuubiran.arcanetablet.client.gui.button.ATItemButton
import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import me.kyuubiran.arcanetablet.util.ATExtensions.eEmc
import me.kyuubiran.arcanetablet.util.ATUtils
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.Rect2i
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import org.lwjgl.glfw.GLFW

class ArcaneTabletScreen(
    menu: ArcaneTabletMenu,
    inventory: Inventory,
) : AbstractContainerScreen<ArcaneTabletMenu>(
    menu, menu.inventory,
    Component.literal("")
) {
    private val textureRes by lazy {
        ResourceLocation.parse("${ArcaneTabletMod.ID}:textures/gui/arcane_tablet_gui.png")
    }

    private lateinit var searchFieldPos: Rect2i
    private lateinit var searchTextField: EditBox

    private val extractionButtons = mutableListOf<ATItemButton>()
    private val validItems = mutableListOf<ItemStack>()

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
        val emc = player.eEmc
        val text = ATUtils.formatEmc(emc, hasShiftDown())
        guiGraphics.drawString(font, text, ((imageWidth - font.width(text)) / 2f), -9f, 0xFFB5B5B5.toInt(), false)
    }

    private fun changePage(isNext: Boolean) {

    }

    @Suppress("UsePropertyAccessSyntax")
    override fun init() {
        super.init()

        searchFieldPos = Rect2i(leftPos + 8, topPos + 7, 160, 11)

        // Search Text Field
        searchTextField = addRenderableWidget(
            EditBox(
                font, searchFieldPos.x, searchFieldPos.y,
                searchFieldPos.width, searchFieldPos.height,
                Component.translatable("item.arcanetablet.arcane_tablet")
            ).apply {
                setTextColor(0xFFFFFFFF.toInt())
                setTextColorUneditable(0xFF808080.toInt())
                isBordered = false
                setMaxLength(35)
                value = searchTextContent
                setInitialFocus()
                isFocused = true
            }
        )

        // Change Page Buttons
        addRenderableWidget(
            ATArrowButton(leftPos + 7, topPos + 20) { changePage(false) }.withTexture(
                textureRes,
                196,
                0
            )
        )
        addRenderableWidget(
            ATArrowButton(leftPos + 151, topPos + 20) { changePage(true) }.withTexture(
                textureRes,
                215,
                0
            )
        )

        addRenderableWidget(ATHighlightButton(leftPos + 80, topPos + 68).withTag("burn"))

        addRenderableWidget(
            ATHighlightButton(leftPos + 9, topPos + 116)
                .withTag("learn").withTooltip(Component.translatable("gui.arcanetablet.arcane_tablet.learn"))
        )
        addRenderableWidget(
            ATHighlightButton(leftPos + 153, topPos + 116)
                .withTag("unlearn").withTooltip(Component.translatable("gui.arcanetablet.arcane_tablet.unlearn"))
        )

        // Craft Button
        addRenderableWidget(
            ATHighlightButton(leftPos - 71, topPos + 16, 9, 9)
                .withTag("rotate").withTooltip(Component.translatable("gui.arcanetablet.arcane_tablet.rotate"))
        )
        addRenderableWidget(
            ATHighlightButton(leftPos - 71, topPos + 26, 9, 9)
                .withTag("balance").withTooltip(Component.translatable("gui.arcanetablet.arcane_tablet.balance"))
        )
        addRenderableWidget(
            ATHighlightButton(leftPos - 71, topPos + 61, 9, 9)
                .withTag("clear").withTooltip(Component.translatable("gui.arcanetablet.arcane_tablet.clear"))
        )
        // TODO: Search type
        addRenderableWidget(ATHighlightButton(leftPos - 71, topPos + 36, 9, 9))

        // Item Slot
        addExtractButton(leftPos + 80, topPos + 20)
        addExtractButton(leftPos + 105, topPos + 26)
        addExtractButton(leftPos + 55, topPos + 26)
        addExtractButton(leftPos + 123, topPos + 44)
        addExtractButton(leftPos + 37, topPos + 44)
        addExtractButton(leftPos + 128, topPos + 68)
        addExtractButton(leftPos + 32, topPos + 68)
        addExtractButton(leftPos + 123, topPos + 92)
        addExtractButton(leftPos + 37, topPos + 92)
        addExtractButton(leftPos + 105, topPos + 110)
        addExtractButton(leftPos + 55, topPos + 110)
        addExtractButton(leftPos + 80, topPos + 116)
    }

    private fun addExtractButton(x: Int, y: Int) {
        val button = ATItemButton(x, y, menu.provider)
        extractionButtons.add(button)
        addRenderableWidget(button)
    }

    private fun setTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, textureRes)
    }

    override fun render(gui: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderBackground(gui, mouseX, mouseY, partialTick)
        super.render(gui, mouseX, mouseY, partialTick)
        this.renderTooltip(gui, mouseX, mouseY)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = when (keyCode) {
        GLFW.GLFW_KEY_ESCAPE -> {
            if (searchTextField.isFocused)
                searchTextField.isFocused = false
            else
                player.closeContainer()
            true
        }

        GLFW.GLFW_KEY_TAB -> {
            searchTextField.isFocused = true
            true
        }

        else -> if (searchTextField.isFocused) searchTextField.keyPressed(keyCode, scanCode, modifiers)
        else super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(codePoint: Char, modifiers: Int): Boolean {
        return if (searchTextField.charTyped(codePoint, modifiers)) true
        else super.charTyped(codePoint, modifiers)
    }

    override fun renderBg(gui: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        setTexture()

        // table
        val xStart = (width - imageWidth) / 2
        val yStart = (height - imageHeight) / 2
        gui.blit(textureRes, xStart, yStart, 0, 0, imageWidth, imageHeight)

        // craft
        gui.blit(textureRes, leftPos - 75, topPos + 10, 180, 19, 76, 89)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        changePage(scrollY < 0)

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (searchFieldPos.contains(mouseX.toInt(), mouseY.toInt()) && button == 1) {
            searchTextField.isFocused = true
            return true
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    private fun updateItems() {

    }

    override fun containerTick() {
        super.containerTick()
    }
}