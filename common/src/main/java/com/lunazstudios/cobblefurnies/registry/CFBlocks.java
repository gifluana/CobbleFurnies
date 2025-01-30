package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.block.*;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class CFBlocks {

    public static class Properties {
        public static BlockBehaviour.Properties WOOD = Block.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS);
        public static BlockBehaviour.Properties MARBLE = Block.Properties.of().strength(1.5F, 6.0F).sound(SoundType.CALCITE).mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM);
        public static BlockBehaviour.Properties CONCRETE = Block.Properties.of().strength(1.8F).sound(SoundType.STONE).mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM);
        public static BlockBehaviour.Properties LAMP = WOOD.lightLevel((blockState) -> blockState.hasProperty(BlockStateProperties.LIT) && blockState.getValue(BlockStateProperties.LIT) ? (blockState.getValue(CFBlockStateProperties.LEVEL_1_3) * 5) : 0);
    }

    public static final Supplier<Block> KITCHEN_FLOOR = registerBlock("kitchen_floor", () -> new Block(Properties.CONCRETE));

    public static final Supplier<Block> RED_CHAIR = registerBlock("red_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> ORANGE_CHAIR = registerBlock("orange_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> YELLOW_CHAIR = registerBlock("yellow_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> BROWN_CHAIR = registerBlock("brown_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> LIME_CHAIR = registerBlock("lime_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> GREEN_CHAIR = registerBlock("green_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> CYAN_CHAIR = registerBlock("cyan_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_BLUE_CHAIR = registerBlock("light_blue_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> BLUE_CHAIR = registerBlock("blue_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> MAGENTA_CHAIR = registerBlock("magenta_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> PURPLE_CHAIR = registerBlock("purple_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> PINK_CHAIR = registerBlock("pink_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> WHITE_CHAIR = registerBlock("white_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_GRAY_CHAIR = registerBlock("light_gray_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> GRAY_CHAIR = registerBlock("gray_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> BLACK_CHAIR = registerBlock("black_chair", () -> new ChairBlock(Properties.WOOD));

    public static final Supplier<Block> OAK_CHAIR = registerBlock("oak_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> SPRUCE_CHAIR = registerBlock("spruce_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> BIRCH_CHAIR = registerBlock("birch_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> DARK_OAK_CHAIR = registerBlock("dark_oak_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> JUNGLE_CHAIR = registerBlock("jungle_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> MANGROVE_CHAIR = registerBlock("mangrove_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> BAMBOO_CHAIR = registerBlock("bamboo_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> CHERRY_CHAIR = registerBlock("cherry_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> CRIMSON_CHAIR = registerBlock("crimson_chair", () -> new ChairBlock(Properties.WOOD));
    public static final Supplier<Block> WARPED_CHAIR = registerBlock("warped_chair", () -> new ChairBlock(Properties.WOOD));

    public static final Supplier<Block> RED_STOOL = registerBlock("red_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> ORANGE_STOOL = registerBlock("orange_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> YELLOW_STOOL = registerBlock("yellow_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> BROWN_STOOL = registerBlock("brown_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> LIME_STOOL = registerBlock("lime_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> GREEN_STOOL = registerBlock("green_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> CYAN_STOOL = registerBlock("cyan_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_BLUE_STOOL = registerBlock("light_blue_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> BLUE_STOOL = registerBlock("blue_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> MAGENTA_STOOL = registerBlock("magenta_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> PURPLE_STOOL = registerBlock("purple_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> PINK_STOOL = registerBlock("pink_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> WHITE_STOOL = registerBlock("white_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_GRAY_STOOL = registerBlock("light_gray_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> GRAY_STOOL = registerBlock("gray_stool", () -> new StoolBlock(Properties.WOOD));
    public static final Supplier<Block> BLACK_STOOL = registerBlock("black_stool", () -> new StoolBlock(Properties.WOOD));

    public static final Supplier<Block> RED_SOFA = registerBlock("red_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> ORANGE_SOFA = registerBlock("orange_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> YELLOW_SOFA = registerBlock("yellow_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> BROWN_SOFA = registerBlock("brown_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> LIME_SOFA = registerBlock("lime_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> GREEN_SOFA = registerBlock("green_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> CYAN_SOFA = registerBlock("cyan_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_BLUE_SOFA = registerBlock("light_blue_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> BLUE_SOFA = registerBlock("blue_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> MAGENTA_SOFA = registerBlock("magenta_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> PURPLE_SOFA = registerBlock("purple_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> PINK_SOFA = registerBlock("pink_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> WHITE_SOFA = registerBlock("white_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_GRAY_SOFA = registerBlock("light_gray_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> GRAY_SOFA = registerBlock("gray_sofa", () -> new SofaBlock(Properties.WOOD));
    public static final Supplier<Block> BLACK_SOFA = registerBlock("black_sofa", () -> new SofaBlock(Properties.WOOD));

    public static final Supplier<Block> RED_TABLE = registerBlock("red_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> ORANGE_TABLE = registerBlock("orange_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> YELLOW_TABLE = registerBlock("yellow_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> BROWN_TABLE = registerBlock("brown_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> LIME_TABLE = registerBlock("lime_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> GREEN_TABLE = registerBlock("green_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> CYAN_TABLE = registerBlock("cyan_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_BLUE_TABLE = registerBlock("light_blue_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> BLUE_TABLE = registerBlock("blue_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> MAGENTA_TABLE = registerBlock("magenta_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> PURPLE_TABLE = registerBlock("purple_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> PINK_TABLE = registerBlock("pink_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> WHITE_TABLE = registerBlock("white_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> LIGHT_GRAY_TABLE = registerBlock("light_gray_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> GRAY_TABLE = registerBlock("gray_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> BLACK_TABLE = registerBlock("black_table", () -> new TableBlock(Properties.WOOD));

    public static final Supplier<Block> OAK_TABLE = registerBlock("oak_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> SPRUCE_TABLE = registerBlock("spruce_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> BIRCH_TABLE = registerBlock("birch_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> DARK_OAK_TABLE = registerBlock("dark_oak_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> JUNGLE_TABLE = registerBlock("jungle_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> MANGROVE_TABLE = registerBlock("mangrove_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> BAMBOO_TABLE = registerBlock("bamboo_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> CHERRY_TABLE = registerBlock("cherry_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> CRIMSON_TABLE = registerBlock("crimson_table", () -> new TableBlock(Properties.WOOD));
    public static final Supplier<Block> WARPED_TABLE = registerBlock("warped_table", () -> new TableBlock(Properties.WOOD));

    public static final Supplier<Block> RED_SINK = registerBlock("red_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> ORANGE_SINK = registerBlock("orange_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> YELLOW_SINK = registerBlock("yellow_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> BROWN_SINK = registerBlock("brown_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> LIME_SINK = registerBlock("lime_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> GREEN_SINK = registerBlock("green_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> CYAN_SINK = registerBlock("cyan_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_BLUE_SINK = registerBlock("light_blue_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> BLUE_SINK = registerBlock("blue_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> MAGENTA_SINK = registerBlock("magenta_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> PURPLE_SINK = registerBlock("purple_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> PINK_SINK = registerBlock("pink_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> WHITE_SINK = registerBlock("white_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_GRAY_SINK = registerBlock("light_gray_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> GRAY_SINK = registerBlock("gray_sink", () -> new SinkBlock(Properties.MARBLE));
    public static final Supplier<Block> BLACK_SINK = registerBlock("black_sink", () -> new SinkBlock(Properties.MARBLE));

    public static final Supplier<Block> RED_STOVE = registerBlock("red_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> ORANGE_STOVE = registerBlock("orange_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> YELLOW_STOVE = registerBlock("yellow_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> BROWN_STOVE = registerBlock("brown_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> LIME_STOVE = registerBlock("lime_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> GREEN_STOVE = registerBlock("green_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> CYAN_STOVE = registerBlock("cyan_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_BLUE_STOVE = registerBlock("light_blue_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> BLUE_STOVE = registerBlock("blue_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> MAGENTA_STOVE = registerBlock("magenta_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> PURPLE_STOVE = registerBlock("purple_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> PINK_STOVE = registerBlock("pink_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> WHITE_STOVE = registerBlock("white_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_GRAY_STOVE = registerBlock("light_gray_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> GRAY_STOVE = registerBlock("gray_stove", () -> new StoveBlock(Properties.MARBLE));
    public static final Supplier<Block> BLACK_STOVE = registerBlock("black_stove", () -> new StoveBlock(Properties.MARBLE));

    public static final Supplier<Block> RED_CABINETRY = registerBlock("red_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> ORANGE_CABINETRY = registerBlock("orange_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> YELLOW_CABINETRY = registerBlock("yellow_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> BROWN_CABINETRY = registerBlock("brown_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> LIME_CABINETRY = registerBlock("lime_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> GREEN_CABINETRY = registerBlock("green_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> CYAN_CABINETRY = registerBlock("cyan_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_BLUE_CABINETRY = registerBlock("light_blue_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> BLUE_CABINETRY = registerBlock("blue_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> MAGENTA_CABINETRY = registerBlock("magenta_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> PURPLE_CABINETRY = registerBlock("purple_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> PINK_CABINETRY = registerBlock("pink_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> WHITE_CABINETRY = registerBlock("white_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_GRAY_CABINETRY = registerBlock("light_gray_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> GRAY_CABINETRY = registerBlock("gray_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));
    public static final Supplier<Block> BLACK_CABINETRY = registerBlock("black_cabinetry", () -> new CabinetryBlock(Properties.MARBLE));

    public static final Supplier<Block> RED_DRAWER = registerBlock("red_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> ORANGE_DRAWER = registerBlock("orange_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> YELLOW_DRAWER = registerBlock("yellow_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> BROWN_DRAWER = registerBlock("brown_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> LIME_DRAWER = registerBlock("lime_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> GREEN_DRAWER = registerBlock("green_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> CYAN_DRAWER = registerBlock("cyan_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_BLUE_DRAWER = registerBlock("light_blue_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> BLUE_DRAWER = registerBlock("blue_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> MAGENTA_DRAWER = registerBlock("magenta_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> PURPLE_DRAWER = registerBlock("purple_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> PINK_DRAWER = registerBlock("pink_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> WHITE_DRAWER = registerBlock("white_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> LIGHT_GRAY_DRAWER = registerBlock("light_gray_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> GRAY_DRAWER = registerBlock("gray_drawer", () -> new DrawerBlock(Properties.MARBLE));
    public static final Supplier<Block> BLACK_DRAWER = registerBlock("black_drawer", () -> new DrawerBlock(Properties.MARBLE));

    public static final Supplier<Block> OAK_CABINET = registerBlock("oak_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> SPRUCE_CABINET = registerBlock("spruce_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> BIRCH_CABINET = registerBlock("birch_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> DARK_OAK_CABINET = registerBlock("dark_oak_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> JUNGLE_CABINET = registerBlock("jungle_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> MANGROVE_CABINET = registerBlock("mangrove_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> BAMBOO_CABINET = registerBlock("bamboo_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> CHERRY_CABINET = registerBlock("cherry_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> CRIMSON_CABINET = registerBlock("crimson_cabinet", () -> new CabinetBlock(Properties.WOOD));
    public static final Supplier<Block> WARPED_CABINET = registerBlock("warped_cabinet", () -> new CabinetBlock(Properties.WOOD));

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> supplier = CFRegistry.registerBlock(name, block);
        CFRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()), "tab");
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockHidden(String name, Supplier<T> block) {
        Supplier<T> supplier = CFRegistry.registerBlock(name, block);
        CFRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()), null);
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockOnly(String name, Supplier<T> block) {
        return CFRegistry.registerBlock(name, block);
    }

    public static void register() {}
}
