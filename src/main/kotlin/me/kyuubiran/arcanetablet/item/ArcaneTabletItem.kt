package me.kyuubiran.arcanetablet.item

import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ArcaneTabletItem : Item(Properties().stacksTo(1)), MenuProvider {

    override fun use(
        world: Level,
        player: Player,
        hand: InteractionHand
    ): InteractionResultHolder<ItemStack?> {
        val stack = player.getItemInHand(hand)

        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(stack);

        if (!world.isClientSide) {
            player.openMenu(this)
        }

        return InteractionResultHolder.success(stack)
    }

    override fun getDisplayName(): Component =
        Component.translatable("item.arcanetransmutationtablet.arcane_tablet")

    override fun createMenu(
        id: Int,
        inv: Inventory,
        player: Player
    ): AbstractContainerMenu = ArcaneTabletMenu(
        id,
        inv,
    )
}
