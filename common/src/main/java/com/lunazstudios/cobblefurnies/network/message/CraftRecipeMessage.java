package com.lunazstudios.cobblefurnies.network.message;

import com.lunazstudios.cobblefurnies.network.CFNetwork;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import net.minecraft.network.codec.StreamCodec;

public record CraftRecipeMessage(int containerId, int recipeIndex, int amount) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftRecipeMessage> CODEC =
            StreamCodec.of(
                    (buf, msg) -> {
                        buf.writeInt(msg.containerId);
                        buf.writeInt(msg.recipeIndex);
                        buf.writeInt(msg.amount);
                    },
                    buf -> new CraftRecipeMessage(buf.readInt(), buf.readInt(), buf.readInt())
            );

    public static void handle(CraftRecipeMessage message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer() instanceof ServerPlayer serverPlayer &&
                    serverPlayer.containerMenu instanceof FurniCrafterMenu menu &&
                    menu.containerId == message.containerId) {
                menu.craftSelectedRecipe(message.recipeIndex, message.amount);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CFNetwork.CRAFT_RECIPE_TYPE;
    }
}