package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.block.entity.StoveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class CFBlockEntityTypes {
    public static final Supplier<BlockEntityType<StoveBlockEntity>> STOVE = CFRegistry.registerBlockEntityType("stove",
            () -> CFRegistry.createBlockEntityType(StoveBlockEntity::new,
                    CFBlocks.RED_STOVE.get(),
                    CFBlocks.ORANGE_STOVE.get(),
                    CFBlocks.YELLOW_STOVE.get(),
                    CFBlocks.BROWN_STOVE.get(),
                    CFBlocks.LIME_STOVE.get(),
                    CFBlocks.GREEN_STOVE.get(),
                    CFBlocks.CYAN_STOVE.get(),
                    CFBlocks.LIGHT_BLUE_STOVE.get(),
                    CFBlocks.BLUE_STOVE.get(),
                    CFBlocks.MAGENTA_STOVE.get(),
                    CFBlocks.PURPLE_STOVE.get(),
                    CFBlocks.PINK_STOVE.get(),
                    CFBlocks.WHITE_STOVE.get(),
                    CFBlocks.LIGHT_GRAY_STOVE.get(),
                    CFBlocks.GRAY_STOVE.get(),
                    CFBlocks.BLACK_STOVE.get()
                    ));

    public static void init() {}
}
