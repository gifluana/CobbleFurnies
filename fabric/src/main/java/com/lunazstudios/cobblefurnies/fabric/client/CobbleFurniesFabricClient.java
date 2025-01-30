package com.lunazstudios.cobblefurnies.fabric.client;

import com.lunazstudios.cobblefurnies.client.CobbleFurniesClient;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public final class CobbleFurniesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleFurniesClient.init();

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                CFBlocks.OAK_CHAIR.get(), CFBlocks.SPRUCE_CHAIR.get(), CFBlocks.BIRCH_CHAIR.get(), CFBlocks.DARK_OAK_CHAIR.get(),
                CFBlocks.JUNGLE_CHAIR.get(), CFBlocks.MANGROVE_CHAIR.get(), CFBlocks.BAMBOO_CHAIR.get(), CFBlocks.CHERRY_CHAIR.get(),
                CFBlocks.CRIMSON_CHAIR.get(), CFBlocks.WARPED_CHAIR.get(),

                CFBlocks.RED_SINK.get(), CFBlocks.ORANGE_SINK.get(), CFBlocks.YELLOW_SINK.get(), CFBlocks.LIME_SINK.get(),
                CFBlocks.GREEN_SINK.get(), CFBlocks.CYAN_SINK.get(), CFBlocks.LIGHT_BLUE_SINK.get(), CFBlocks.BLUE_SINK.get(),
                CFBlocks.MAGENTA_SINK.get(), CFBlocks.PURPLE_SINK.get(), CFBlocks.PINK_SINK.get(), CFBlocks.WHITE_SINK.get(),
                CFBlocks.LIGHT_GRAY_SINK.get(), CFBlocks.GRAY_SINK.get(), CFBlocks.BLACK_SINK.get(), CFBlocks.BROWN_SINK.get(),

                CFBlocks.RED_STOVE.get(), CFBlocks.ORANGE_STOVE.get(), CFBlocks.YELLOW_STOVE.get(), CFBlocks.LIME_STOVE.get(),
                CFBlocks.GREEN_STOVE.get(), CFBlocks.CYAN_STOVE.get(), CFBlocks.LIGHT_BLUE_STOVE.get(), CFBlocks.BLUE_STOVE.get(),
                CFBlocks.MAGENTA_STOVE.get(), CFBlocks.PURPLE_STOVE.get(), CFBlocks.PINK_STOVE.get(), CFBlocks.WHITE_STOVE.get(),
                CFBlocks.LIGHT_GRAY_STOVE.get(), CFBlocks.GRAY_STOVE.get(), CFBlocks.BLACK_STOVE.get(), CFBlocks.BROWN_STOVE.get()
                );
    }
}
