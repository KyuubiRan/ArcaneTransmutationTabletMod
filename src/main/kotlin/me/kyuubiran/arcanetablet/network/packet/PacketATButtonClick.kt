package me.kyuubiran.arcanetablet.network.packet

import io.netty.buffer.ByteBuf
import me.kyuubiran.arcanetablet.ArcaneTabletMod
import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext


data class PacketATButtonClick(
    val tag: String,
    val shiftPressed: Boolean,
) : CustomPacketPayload {

    companion object {
        val TYPE: CustomPacketPayload.Type<PacketATButtonClick> =
            CustomPacketPayload.Type<PacketATButtonClick>(
                ResourceLocation.fromNamespaceAndPath(
                    ArcaneTabletMod.ID,
                    "at_button_click"
                )
            )

        val STREAM_CODEC: StreamCodec<ByteBuf?, PacketATButtonClick?> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            PacketATButtonClick::tag,
            ByteBufCodecs.BOOL,
            PacketATButtonClick::shiftPressed,
            ::PacketATButtonClick
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    object ClientPayloadHandler {

        fun handle(payload: PacketATButtonClick, context: IPayloadContext) {

        }
    }

    object ServerPayloadHandler {

        fun handle(payload: PacketATButtonClick, context: IPayloadContext) {
            context.enqueueWork {
                val sp = context.player() as ServerPlayer? ?: return@enqueueWork
                val menu = sp.containerMenu as? ArcaneTabletMenu ?: return@enqueueWork
                menu.handleButtonClick(payload.tag, payload.shiftPressed, sp)
            }
        }
    }
}
