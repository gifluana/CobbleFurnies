package com.lunazstudios.cobblefurnies.fabric.client;

import com.lunazstudios.cobblefurnies.client.CobbleFurniesClient;
import com.lunazstudios.cobblefurnies.client.screen.FurniCrafterScreen;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import com.lunazstudios.cobblefurnies.registry.CFMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public final class CobbleFurniesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleFurniesClient.init();

        MenuScreens.register(CFMenus.FURNI_CRAFTER_MENU.get(), FurniCrafterScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                CFBlocks.LIGHT_TOILET.get(), CFBlocks.DARK_TOILET.get()
                );

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                CFBlocks.MINI_TOPIARY.get(), CFBlocks.POTTED_POTHOS.get(),

                CFBlocks.RED_LAMP.get(), CFBlocks.ORANGE_LAMP.get(), CFBlocks.YELLOW_LAMP.get(), CFBlocks.LIME_LAMP.get(),
                CFBlocks.GREEN_LAMP.get(), CFBlocks.CYAN_LAMP.get(), CFBlocks.LIGHT_BLUE_LAMP.get(), CFBlocks.BLUE_LAMP.get(),
                CFBlocks.MAGENTA_LAMP.get(), CFBlocks.PURPLE_LAMP.get(), CFBlocks.PINK_LAMP.get(), CFBlocks.WHITE_LAMP.get(),
                CFBlocks.LIGHT_GRAY_LAMP.get(), CFBlocks.GRAY_LAMP.get(), CFBlocks.BLACK_LAMP.get(), CFBlocks.BROWN_LAMP.get(),

                CFBlocks.RED_UPPER_CABINET.get(), CFBlocks.ORANGE_UPPER_CABINET.get(), CFBlocks.YELLOW_UPPER_CABINET.get(), CFBlocks.LIME_UPPER_CABINET.get(),
                CFBlocks.GREEN_UPPER_CABINET.get(), CFBlocks.CYAN_UPPER_CABINET.get(), CFBlocks.LIGHT_BLUE_UPPER_CABINET.get(), CFBlocks.BLUE_UPPER_CABINET.get(),
                CFBlocks.MAGENTA_UPPER_CABINET.get(), CFBlocks.PURPLE_UPPER_CABINET.get(), CFBlocks.PINK_UPPER_CABINET.get(), CFBlocks.WHITE_UPPER_CABINET.get(),
                CFBlocks.LIGHT_GRAY_UPPER_CABINET.get(), CFBlocks.GRAY_UPPER_CABINET.get(), CFBlocks.BLACK_UPPER_CABINET.get(), CFBlocks.BROWN_UPPER_CABINET.get(),

                CFBlocks.RED_CURTAIN.get(), CFBlocks.ORANGE_CURTAIN.get(), CFBlocks.YELLOW_CURTAIN.get(), CFBlocks.LIME_CURTAIN.get(),
                CFBlocks.GREEN_CURTAIN.get(), CFBlocks.CYAN_CURTAIN.get(), CFBlocks.LIGHT_BLUE_CURTAIN.get(), CFBlocks.BLUE_CURTAIN.get(),
                CFBlocks.MAGENTA_CURTAIN.get(), CFBlocks.PURPLE_CURTAIN.get(), CFBlocks.PINK_CURTAIN.get(), CFBlocks.WHITE_CURTAIN.get(),
                CFBlocks.LIGHT_GRAY_CURTAIN.get(), CFBlocks.GRAY_CURTAIN.get(), CFBlocks.BLACK_CURTAIN.get(), CFBlocks.BROWN_CURTAIN.get(),

                CFBlocks.OAK_CHAIR.get(), CFBlocks.SPRUCE_CHAIR.get(), CFBlocks.BIRCH_CHAIR.get(), CFBlocks.DARK_OAK_CHAIR.get(),
                CFBlocks.JUNGLE_CHAIR.get(), CFBlocks.MANGROVE_CHAIR.get(), CFBlocks.BAMBOO_CHAIR.get(), CFBlocks.ACACIA_CHAIR.get(),
                CFBlocks.CHERRY_CHAIR.get(), CFBlocks.CRIMSON_CHAIR.get(), CFBlocks.WARPED_CHAIR.get(), CFBlocks.APRICORN_CHAIR.get(),
                CFBlocks.SACCHARINE_CHAIR.get(),

                CFBlocks.RED_SINK.get(), CFBlocks.ORANGE_SINK.get(), CFBlocks.YELLOW_SINK.get(), CFBlocks.LIME_SINK.get(),
                CFBlocks.GREEN_SINK.get(), CFBlocks.CYAN_SINK.get(), CFBlocks.LIGHT_BLUE_SINK.get(), CFBlocks.BLUE_SINK.get(),
                CFBlocks.MAGENTA_SINK.get(), CFBlocks.PURPLE_SINK.get(), CFBlocks.PINK_SINK.get(), CFBlocks.WHITE_SINK.get(),
                CFBlocks.LIGHT_GRAY_SINK.get(), CFBlocks.GRAY_SINK.get(), CFBlocks.BLACK_SINK.get(), CFBlocks.BROWN_SINK.get(),

                CFBlocks.RED_STOVE.get(), CFBlocks.ORANGE_STOVE.get(), CFBlocks.YELLOW_STOVE.get(), CFBlocks.LIME_STOVE.get(),
                CFBlocks.GREEN_STOVE.get(), CFBlocks.CYAN_STOVE.get(), CFBlocks.LIGHT_BLUE_STOVE.get(), CFBlocks.BLUE_STOVE.get(),
                CFBlocks.MAGENTA_STOVE.get(), CFBlocks.PURPLE_STOVE.get(), CFBlocks.PINK_STOVE.get(), CFBlocks.WHITE_STOVE.get(),
                CFBlocks.LIGHT_GRAY_STOVE.get(), CFBlocks.GRAY_STOVE.get(), CFBlocks.BLACK_STOVE.get(), CFBlocks.BROWN_STOVE.get(),

                CFBlocks.TV.get(), CFBlocks.CES.get(), CFBlocks.LIGHT_FRIDGE.get(), CFBlocks.LIGHT_FREEZER.get(), CFBlocks.DARK_FREEZER.get()
                );
    }
}
