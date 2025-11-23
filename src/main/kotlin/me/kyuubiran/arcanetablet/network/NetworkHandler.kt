package me.kyuubiran.arcanetablet.network

import me.kyuubiran.arcanetablet.network.packet.PacketATButtonClick
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler


object NetworkHandler {

    private const val PROTOCOL_VERSION = "1"

    @SubscribeEvent
    fun register(event: RegisterPayloadHandlersEvent) {

        val registrar = event.registrar(PROTOCOL_VERSION)

        registrar.playBidirectional(
            PacketATButtonClick.TYPE,
            PacketATButtonClick.STREAM_CODEC,
            DirectionalPayloadHandler(
                PacketATButtonClick.ClientPayloadHandler::handle,
                PacketATButtonClick.ServerPayloadHandler::handle
            )
        )
    }
}