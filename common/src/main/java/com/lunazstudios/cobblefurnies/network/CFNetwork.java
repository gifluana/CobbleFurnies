package com.lunazstudios.cobblefurnies.network;

import com.lunazstudios.cobblefurnies.network.message.CraftRecipeMessage;
import com.lunazstudios.cobblefurnies.network.message.SyncCraftableRecipesMessage;
import com.lunazstudios.cobblefurnies.network.message.ToggleStoveLidMessage;
import dev.architectury.platform.Platform;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class CFNetwork {
    public static final CustomPacketPayload.Type<CraftRecipeMessage> CRAFT_RECIPE_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cobblefurnies", "craft_recipe"));
    public static final CustomPacketPayload.Type<SyncCraftableRecipesMessage> CRAFTABLE_RECIPES_SYNC_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cobblefurnies", "craftable_recipes_sync"));
    public static final CustomPacketPayload.Type<ToggleStoveLidMessage> TOGGLE_STOVE_LID_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cobblefurnies", "toggle_stove_lid"));

    public static void registerPackets() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                CRAFT_RECIPE_TYPE,
                CraftRecipeMessage.CODEC,
                CraftRecipeMessage::handle
        );

        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                TOGGLE_STOVE_LID_TYPE,
                ToggleStoveLidMessage.CODEC,
                ToggleStoveLidMessage::handle
        );

        if (Platform.getEnv() == EnvType.CLIENT) {
            NetworkManager.registerReceiver(
                    NetworkManager.Side.S2C,
                    CRAFTABLE_RECIPES_SYNC_TYPE,
                    SyncCraftableRecipesMessage.CODEC,
                    SyncCraftableRecipesMessage::handle
            );
        } else {
            NetworkManager.registerS2CPayloadType(
                    CRAFTABLE_RECIPES_SYNC_TYPE,
                    SyncCraftableRecipesMessage.CODEC
            );
        }
    }
}
