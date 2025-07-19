package com.lunazstudios.cobblefurnies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PokeDeskBlock extends FurnitureHorizontalBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(7, 0, 2, 9, 2, 14),
            Block.box(7, 2, 7, 9, 13, 9),
            Block.box(1, 13, 4, 2, 16, 12),
            Block.box(14, 13, 4, 15, 16, 12),
            Block.box(2, 13, 2, 14, 16, 14),
            Block.box(4, 13, 1, 12, 16, 2),
            Block.box(4, 13, 14, 12, 16, 15),
            Block.box(9, 0, 7, 14, 2, 9),
            Block.box(2, 0, 7, 7, 2, 9)
    );

    public PokeDeskBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
