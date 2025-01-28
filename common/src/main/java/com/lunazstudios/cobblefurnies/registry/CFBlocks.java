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
        public static BlockBehaviour.Properties MARBLE = Block.Properties.of().strength(1.5F, 6.0F).sound(SoundType.CALCITE).ignitedByLava().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM);
        public static BlockBehaviour.Properties LAMP = WOOD.lightLevel((blockState) -> blockState.hasProperty(BlockStateProperties.LIT) && blockState.getValue(BlockStateProperties.LIT) ? (blockState.getValue(CFBlockStateProperties.LEVEL_1_3) * 5) : 0);
    }
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
