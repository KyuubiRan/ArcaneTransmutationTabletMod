package me.kyuubiran.arcanetablet.menu

import me.kyuubiran.arcanetablet.util.ATExtensions.calcTotalEmc
import me.kyuubiran.arcanetablet.util.ATExtensions.eAddKnowledge
import me.kyuubiran.arcanetablet.util.ATExtensions.eHasEmcValue
import me.kyuubiran.arcanetablet.util.ATExtensions.eInfo
import me.kyuubiran.arcanetablet.util.ATExtensions.eKnowledgeProvider
import me.kyuubiran.arcanetablet.util.ATExtensions.persistentInfo
import me.kyuubiran.arcanetablet.util.ATUtils
import moze_intel.projecte.api.capabilities.IKnowledgeProvider
import moze_intel.projecte.api.capabilities.PECapabilities
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.math.BigInteger

class ArcaneTabletMenu : AbstractContainerMenu {

    companion object {
        private val ROTATION_SLOTS: IntArray = intArrayOf(0, 1, 2, 5, 8, 7, 6, 3)
    }

    val player: Player
    val serverPlayer get() = player as ServerPlayer
    val provider: IKnowledgeProvider

    val inventory: Inventory

    private fun addPlayerSlots(posX: Int, posY: Int) {
        // Add the player's inventory slots to the container
        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(inventory, col + row * 9 + 9, posX + col * 18, posY + row * 18))
            }
        }

        // Add the player's action bar slots to the container
        for (idx in 0..8) {
            addSlot(object : Slot(inventory, idx, posX + idx * 18, posY + 58) {
                override fun mayPickup(player: Player): Boolean {
                    return index != inventory.selected
                }
            })
        }
    }

    constructor(id: Int, inventory: Inventory) : super(
        ATModMenus.ARCANE_TABLET_MENU.get(),
        id,
    ) {
        player = inventory.player
        provider = player.eKnowledgeProvider
        this.inventory = inventory

        addPlayerSlots(8, 135)
    }


    override fun slotsChanged(container: Container) {
        super.slotsChanged(container)
    }

    override fun stillValid(p0: Player): Boolean {
        return true
    }

    private fun tryAddKnowledge(stack: ItemStack) {
        if (player.eAddKnowledge(stack) == ATUtils.KnowledgeAddResult.ADDED) {
            provider.syncKnowledgeChange(serverPlayer, stack.eInfo, true)
            // TODO: NetworkHandler.sendToPlayer((ServerPlayer) player, new PacketNotifyKnowledgeChange());
        }
    }

    private fun learnItem() {
        val carriedItem = carried
        if (carriedItem.isEmpty) return

        val fixedStack = carriedItem.eInfo.persistentInfo.createStack()
        tryAddKnowledge(fixedStack)
    }

    private fun unlearnItem() {
        val carriedItem = carried
        if (carriedItem.isEmpty) return

        val fixedStack = carriedItem.eInfo.persistentInfo.createStack()
        if (provider.removeKnowledge(fixedStack)) {
            provider.syncKnowledgeChange(player as ServerPlayer, fixedStack.eInfo, false)
            // TODO: NetworkHandler.sendToPlayer((ServerPlayer) player, new PacketNotifyKnowledgeChange());
        }
    }

    private fun burnItem(shiftPressed: Boolean) {
        val carriedItem = carried
        if (carriedItem.isEmpty) return

        val capability = carriedItem.getCapability(PECapabilities.EMC_HOLDER_ITEM_CAPABILITY)
        if (shiftPressed && capability != null) {
            val extracted = capability.extractEmc(
                carriedItem,
                capability.getMaximumEmc(carriedItem),
                IEmcStorage.EmcAction.EXECUTE
            )

            provider.emc = provider.emc.add(BigInteger.valueOf(extracted))
            provider.syncEmc(serverPlayer)
        } else if (carriedItem.eHasEmcValue) {
            val fixedStack = carriedItem.eInfo.persistentInfo.createStack()
            val toAdd = fixedStack.calcTotalEmc(carriedItem.count)

            provider.emc = provider.emc.add(BigInteger.valueOf(toAdd))
            provider.syncEmc(serverPlayer)
            carried = ItemStack.EMPTY
        }
    }

    private fun tryExtractItem(name: String, shiftPressed: Boolean) {

    }

    override fun quickMoveStack(
        p0: Player,
        p1: Int
    ): ItemStack {
        return ItemStack.EMPTY
    }

    fun handleButtonClick(tag: String, shiftPressed: Boolean, sp: ServerPlayer) {
        when (tag) {
            "learn" -> learnItem()
            "unlearn" -> unlearnItem()
            "burn" -> burnItem(shiftPressed)
            else -> if (tag.startsWith("extract:")) {
                tryExtractItem(tag.substring(8), shiftPressed)
            }
        }
    }
}