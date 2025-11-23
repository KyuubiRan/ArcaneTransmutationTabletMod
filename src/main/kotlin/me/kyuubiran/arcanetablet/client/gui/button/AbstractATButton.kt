package me.kyuubiran.arcanetablet.client.gui.button

import net.minecraft.client.gui.components.Button
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer


abstract class AbstractATButton : Button, TooltipProvider {

    protected var texture: ResourceLocation? = null
    protected var textureX: Int = 0
    protected var textureY: Int = 0
    private var tag = ""
    protected val tooltips = mutableListOf<Component>()

    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        onPress: OnPress = OnPress {},
        createNarration: CreateNarration = CreateNarration { Component.literal("") }
    ) : super(x, y, width, height, Component.literal(""), onPress, createNarration)


    fun withTag(tag: String) = this.apply { this.tag = tag }

    fun withTexture(
        texture: ResourceLocation,
        textureX: Int,
        textureY: Int
    ) = this.apply {
        this.texture = texture
        this.textureX = textureX
        this.textureY = textureY
    }

    fun withTooltip(tooltip: Component) = this.apply { this.tooltips.add(tooltip) }
    fun withTooltip(text: String) = this.apply { this.tooltips.add(Component.literal(text)) }

    override fun playDownSound(handler: SoundManager) {
    }

    override fun onPress() {
        super.onPress()
    }

    override fun addToTooltip(ctx: Item.TooltipContext, tips: Consumer<Component>, flag: TooltipFlag) {
        tooltips.forEach { tips.accept(it) }
    }
}