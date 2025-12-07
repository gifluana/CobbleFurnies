package com.lunazstudios.cobblefurnies.network.message;

import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import com.lunazstudios.cobblefurnies.network.CFNetwork;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerData;

public record ToggleStoveLidMessage(int containerId) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleStoveLidMessage> CODEC =
            StreamCodec.of(
                    (buf, msg) -> buf.writeInt(msg.containerId),
                    buf -> new ToggleStoveLidMessage(buf.readInt())
            );

    public static void handle(ToggleStoveLidMessage message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer() instanceof ServerPlayer serverPlayer &&
                    serverPlayer.containerMenu instanceof StoveMenu menu &&
                    menu.containerId == message.containerId) {

                ContainerData data = menu.containerData;
                int current = data.get(2);
                int newValue = current == 0 ? 1 : 0;
                data.set(2, newValue);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CFNetwork.TOGGLE_STOVE_LID_TYPE;
    }
}