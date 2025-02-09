package com.lunazstudios.cobblefurnies.network.message;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import com.lunazstudios.cobblefurnies.client.screen.FurniCrafterScreen;
import net.minecraft.network.codec.StreamCodec;

public record SyncCraftableRecipesMessage(int containerId, boolean[] craftable) {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCraftableRecipesMessage> CODEC =
            StreamCodec.of(
                    (buf, msg) -> {
                        buf.writeInt(msg.containerId);
                        buf.writeInt(msg.craftable.length);
                        for (boolean b : msg.craftable) {
                            buf.writeBoolean(b);
                        }
                    },
                    buf -> {
                        int containerId = buf.readInt();
                        int len = buf.readInt();
                        boolean[] craftable = new boolean[len];
                        for (int i = 0; i < len; i++) {
                            craftable[i] = buf.readBoolean();
                        }
                        return new SyncCraftableRecipesMessage(containerId, craftable);
                    }
            );

    public static void handle(SyncCraftableRecipesMessage message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (Minecraft.getInstance().player.containerMenu instanceof FurniCrafterMenu menu &&
                    menu.containerId == message.containerId) {
                menu.setCraftableRecipes(message.craftable);
                if (Minecraft.getInstance().screen instanceof FurniCrafterScreen screen) {
                    screen.updateRecipeButtons();
                }
            }
        });
    }
}
