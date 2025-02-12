package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CESBlock extends FurnitureHorizontalBlock {
    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(2, 2, 6, 14, 4, 15),
            Block.box(3, 0, 6, 13, 2, 15),
            Block.box(12, 1, 5.25, 13, 2, 6.25),
            Block.box(10, 1, 5.25, 11, 2, 6.25),
            Block.box(3, 0, 0, 9, 1, 3),
            Block.box(3, 1, 3, 9, 1, 6)
    );
    protected static final VoxelShape SHAPE_EAST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_SOUTH = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_WEST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.WEST);

    public CESBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        switch (facing) {
            case EAST:
                return SHAPE_EAST;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }
}