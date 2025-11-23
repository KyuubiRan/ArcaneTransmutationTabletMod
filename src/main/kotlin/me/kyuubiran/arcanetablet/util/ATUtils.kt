package me.kyuubiran.arcanetablet.util

import me.kyuubiran.arcanetablet.util.ATExtensions.eHasEmcValue
import me.kyuubiran.arcanetablet.util.ATExtensions.eKnowledgeProvider
import me.kyuubiran.arcanetablet.util.ATExtensions.eTransmutationProxy
import me.kyuubiran.arcanetablet.util.ATExtensions.persistentInfo
import moze_intel.projecte.api.ItemInfo
import moze_intel.projecte.api.event.PlayerAttemptLearnEvent
import moze_intel.projecte.api.proxy.IEMCProxy
import moze_intel.projecte.api.proxy.ITransmutationProxy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.NeoForge.EVENT_BUS


internal object ATUtils {

    enum class KnowledgeAddResult {
        NOT_ADDED,
        ALREADY_KNOWN,
        ADDED
    }

    val EMCProxy get() = IEMCProxy.INSTANCE
    val TransmutationProxy get() = ITransmutationProxy.INSTANCE

    fun playerHasKnowledge(player: Player, stack: ItemStack): Boolean = player.eTransmutationProxy.hasKnowledge(stack)

    fun addKnowledge(player: Player, stack: ItemStack): KnowledgeAddResult {
        if (stack.isEmpty || !stack.eHasEmcValue)
            return KnowledgeAddResult.NOT_ADDED

        val provider = player.eKnowledgeProvider
        if (provider.hasKnowledge(stack)) {
            val info = ItemInfo.fromStack(stack)
            val cleaned = info.persistentInfo

            val result = EVENT_BUS.post(PlayerAttemptLearnEvent(player, info, cleaned))
            if (result.isCanceled) {
                return KnowledgeAddResult.NOT_ADDED
            }

            return if (provider.addKnowledge(stack)) KnowledgeAddResult.ADDED else KnowledgeAddResult.NOT_ADDED
        }

        return KnowledgeAddResult.ADDED
    }
}