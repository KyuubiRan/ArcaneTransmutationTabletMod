package me.kyuubiran.arcanetablet.network

import moze_intel.projecte.api.capabilities.PECapabilities
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.tick.ServerTickEvent
import java.util.UUID

object EmcSyncHandler {

    private var tickCounter = 0

    private val shouldSyncPlayers: HashSet<UUID> = HashSet()

    fun ensureSync(p: Player) {
        shouldSyncPlayers.add(p.uuid)
    }

    fun ensureSync(uuid: UUID) {
        shouldSyncPlayers.add(uuid)
    }

    fun onServerTick(event: ServerTickEvent.Post) {
        if (++tickCounter >= 20) {

            shouldSyncPlayers
                .mapNotNull { event.server.playerList.getPlayer(it) }
                .filter { it.isAlive }
                .forEach { it.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY)?.sync(it) }

            shouldSyncPlayers.clear()
            tickCounter = 0
        }
    }
}