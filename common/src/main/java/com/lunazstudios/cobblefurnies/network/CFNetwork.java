package com.lunazstudios.cobblefurnies.network;

import com.lunazstudios.cobblefurnies.network.message.CraftRecipeMessage;
import com.lunazstudios.cobblefurnies.network.message.SyncCraftableRecipesMessage;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CFNetwork {
    public static final ResourceLocation CRAFT_RECIPE = ResourceLocation.fromNamespaceAndPath("cobblefurnies", "craft_recipe");
    public static final ResourceLocation CRAFTABLE_RECIPES_SYNC = ResourceLocation.fromNamespaceAndPath("cobblefurnies", "craftable_recipes_sync");

    public static void registerPackets() {
        // Register the client-to-server packet
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, CRAFT_RECIPE, (RegistryFriendlyByteBuf buf, NetworkManager.PacketContext ctx) -> {
            CraftRecipeMessage msg = CraftRecipeMessage.CODEC.decode(buf);
            CraftRecipeMessage.handle(msg, ctx);
        });

        // Register the server-to-client packet
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CRAFTABLE_RECIPES_SYNC, (RegistryFriendlyByteBuf buf, NetworkManager.PacketContext ctx) -> {
            SyncCraftableRecipesMessage msg = SyncCraftableRecipesMessage.CODEC.decode(buf);
            SyncCraftableRecipesMessage.handle(msg, ctx);
        });
    }
}
