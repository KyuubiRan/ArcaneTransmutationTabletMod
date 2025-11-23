package me.kyuubiran.arcanetablet.util

import moze_intel.projecte.api.ItemInfo
import moze_intel.projecte.api.capabilities.PECapabilities
import moze_intel.projecte.config.ProjectEConfig
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.math.BigInteger

internal object ATExtensions {

    val Player.eTransmutationProxy get() = ATUtils.TransmutationProxy.getKnowledgeProviderFor(this.uuid)

    fun Player.eHasKnowledge(stack: ItemStack): Boolean = ATUtils.playerHasKnowledge(this, stack)

    val Player.eKnowledgeProvider get() = ATUtils.TransmutationProxy.getKnowledgeProviderFor(this.uuid)

    val ItemStack.eHasEmcValue get() = ATUtils.EMCProxy.hasValue(this)

    val ItemStack.eEmc get() = ATUtils.EMCProxy.getValue(this)

    val ItemInfo.persistentInfo get() = ATUtils.EMCProxy.getPersistentInfo(this)

    val ItemStack.eInfo: ItemInfo get() = ItemInfo.fromStack(this)

    val Player.eEmc: BigInteger get() = this.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY)?.emc ?: BigInteger.ZERO

    val ItemStack.persistentInfo get() = ATUtils.EMCProxy.getPersistentInfo(this.eInfo)

    fun Player.eAddKnowledge(stack: ItemStack): ATUtils.KnowledgeAddResult = ATUtils.addKnowledge(this, stack)

    fun ItemStack.calcTotalEmc(count: Int = this.count): Long =
        (ATUtils.EMCProxy.getValue(this) * count * ProjectEConfig.server.difficulty.covalenceLoss.get()).toLong()
}