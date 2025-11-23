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
import java.math.BigDecimal
import java.math.BigInteger


internal object ATUtils {

    enum class KnowledgeAddResult {
        NOT_ADDED,
        ALREADY_KNOWN,
        ADDED
    }

    val EMCProxy: IEMCProxy get() = IEMCProxy.INSTANCE
    val TransmutationProxy: ITransmutationProxy get() = ITransmutationProxy.INSTANCE

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

        return KnowledgeAddResult.ALREADY_KNOWN
    }

    fun formatExtractionCount(d: BigDecimal): String {
        var s = d.toString()
        if (s.length >= 25) {
            s = s.dropLast(24) + "Y"
        } else if (s.length >= 22) {
            s = s.dropLast(21) + "Z"
        } else if (s.length >= 19) {
            s = s.dropLast(18) + "E"
        } else if (s.length >= 16) {
            s = s.dropLast(15) + "P"
        } else if (s.length >= 13) {
            s = s.dropLast(12) + "T"
        } else if (s.length >= 10) {
            s = s.dropLast(9) + "G"
        } else if (s.length >= 7) {
            s = s.dropLast(6) + "M"
        }

        return s
    }

    fun formatEmc(emc: BigInteger, showSpecified: Boolean = false): String {
        fun specifiedFormat(): String = buildString {
            val d = emc.toString().reversed()
            for (i in d.indices) {
                append(d[i])
                if ((i + 1) % 3 == 0 && i != d.length - 1) {
                    append(',')
                }
            }
        }.reversed()

        val d = emc.toDouble()

        return when {
            showSpecified -> specifiedFormat()
            d >= 1e21 -> String.format("%.2f Z", d / 1e21)
            d >= 1e18 -> String.format("%.2f E", d / 1e18)
            d >= 1e15 -> String.format("%.2f P", d / 1e15)
            d >= 1e12 -> String.format("%.2f T", d / 1e12)
            d >= 1e9 -> String.format("%.2f G", d / 1e9)
            d >= 1e6 -> String.format("%.2f M", d / 1_000_000.0)
            d >= 1e3 -> specifiedFormat()
            else -> emc.toString()
        }
    }
}