package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.block.entity.*;
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

    public static final Supplier<BlockEntityType<DrawerBlockEntity>> DRAWER = CFRegistry.registerBlockEntityType("drawer",
            () -> CFRegistry.createBlockEntityType(DrawerBlockEntity::new,
                    CFBlocks.RED_DRAWER.get(),
                    CFBlocks.ORANGE_DRAWER.get(),
                    CFBlocks.YELLOW_DRAWER.get(),
                    CFBlocks.BROWN_DRAWER.get(),
                    CFBlocks.LIME_DRAWER.get(),
                    CFBlocks.GREEN_DRAWER.get(),
                    CFBlocks.CYAN_DRAWER.get(),
                    CFBlocks.LIGHT_BLUE_DRAWER.get(),
                    CFBlocks.BLUE_DRAWER.get(),
                    CFBlocks.MAGENTA_DRAWER.get(),
                    CFBlocks.PURPLE_DRAWER.get(),
                    CFBlocks.PINK_DRAWER.get(),
                    CFBlocks.WHITE_DRAWER.get(),
                    CFBlocks.LIGHT_GRAY_DRAWER.get(),
                    CFBlocks.GRAY_DRAWER.get(),
                    CFBlocks.BLACK_DRAWER.get()
            ));

    public static final Supplier<BlockEntityType<CabinetBlockEntity>> CABINET = CFRegistry.registerBlockEntityType("cabinet",
            () -> CFRegistry.createBlockEntityType(CabinetBlockEntity::new,
                    CFBlocks.OAK_CABINET.get(),
                    CFBlocks.SPRUCE_CABINET.get(),
                    CFBlocks.BIRCH_CABINET.get(),
                    CFBlocks.JUNGLE_CABINET.get(),
                    CFBlocks.ACACIA_CABINET.get(),
                    CFBlocks.DARK_OAK_CABINET.get(),
                    CFBlocks.MANGROVE_CABINET.get(),
                    CFBlocks.BAMBOO_CABINET.get(),
                    CFBlocks.CHERRY_CABINET.get(),
                    CFBlocks.WARPED_CABINET.get(),
                    CFBlocks.CRIMSON_CABINET.get(),
                    CFBlocks.APRICORN_CABINET.get()
            ));

    public static final Supplier<BlockEntityType<StatueBlockEntity>> STATUE = CFRegistry.registerBlockEntityType("statue",
            () -> CFRegistry.createBlockEntityType(StatueBlockEntity::new,
                    CFBlocks.STATUE_ANCIENT.get()
            ));

    public static final Supplier<BlockEntityType<FridgeBlockEntity>> FRIDGE = CFRegistry.registerBlockEntityType("fridge",
            () -> CFRegistry.createBlockEntityType(FridgeBlockEntity::new,
                    CFBlocks.LIGHT_FRIDGE.get(),
                    CFBlocks.DARK_FRIDGE.get()
            ));

    public static final Supplier<BlockEntityType<FreezerBlockEntity>> FREEZER = CFRegistry.registerBlockEntityType("freezer",
            () -> CFRegistry.createBlockEntityType(FreezerBlockEntity::new,
                    CFBlocks.LIGHT_FREEZER.get(),
                    CFBlocks.DARK_FREEZER.get()
            ));

    public static final Supplier<BlockEntityType<FurniCrafterBlockEntity>> FURNI_CRAFTER = CFRegistry.registerBlockEntityType("furnicrafter",
            () -> CFRegistry.createBlockEntityType(FurniCrafterBlockEntity::new,
                    CFBlocks.FURNI_CRAFTER.get()
            ));

    public static void init() {}
}
