package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.block.properties.WallSupportShape;
import com.lunazstudios.cobblefurnies.registry.CFBlockTags;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallSupportBlock extends FurnitureHorizontalBlock {

    protected static final VoxelShape SINGLE_SHAPE = Shapes.or(
            Block.box(1, 8, 14, 3, 11, 15),
            Block.box(1, 9, 13, 3, 12, 14),
            Block.box(1, 10, 12, 3, 13, 13),
            Block.box(1, 11, 11, 3, 13, 12),
            Block.box(1, 12, 10, 3, 13, 11),
            Block.box(0, 13, 0, 16, 16, 16),
            Block.box(12, 7, 15, 16, 12, 16),
            Block.box(0, 7, 15, 4, 12, 16),
            Block.box(13, 12, 10, 15, 13, 11),
            Block.box(13, 11, 11, 15, 13, 12),
            Block.box(13, 10, 12, 15, 13, 13),
            Block.box(13, 9, 13, 15, 12, 14),
            Block.box(13, 8, 14, 15, 11, 15)
    );

    protected static final VoxelShape LEFT_SHAPE = Shapes.or(
            Block.box(1, 8, 14, 3, 11, 15),
            Block.box(1, 9, 13, 3, 12, 14),
            Block.box(1, 10, 12, 3, 13, 13),
            Block.box(1, 11, 11, 3, 13, 12),
            Block.box(1, 12, 10, 3, 13, 11),
            Block.box(0, 7, 15, 4, 12, 16),
            Block.box(0, 13, 0, 16, 16, 16)
    );

    protected static final VoxelShape RIGHT_SHAPE = Shapes.or(
            Block.box(13, 8, 14, 15, 11, 15),
            Block.box(13, 9, 13, 15, 12, 14),
            Block.box(13, 10, 12, 15, 13, 13),
            Block.box(13, 11, 11, 15, 13, 12),
            Block.box(13, 12, 10, 15, 13, 11),
            Block.box(12, 7, 15, 16, 12, 16),
            Block.box(0, 13, 0, 16, 16, 16)
    );

    protected static final VoxelShape MIDDLE_SHAPE = Block.box(0, 13, 0, 16, 16, 16);

    public WallSupportBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(CFBlockStateProperties.WALL_SUPPORT_SHAPE, WallSupportShape.SINGLE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CFBlockStateProperties.WALL_SUPPORT_SHAPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(CFBlockStateProperties.WALL_SUPPORT_SHAPE, getWallSupportShape(level, pos, facing));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState blockState2, LevelAccessor level, BlockPos pos, BlockPos blockPos2) {
        Direction facing = state.getValue(FACING);
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(CFBlockStateProperties.WALL_SUPPORT_SHAPE, getWallSupportShape(level, pos, facing));
        }
        return super.updateShape(state, direction, blockState2, level, pos, blockPos2);
    }

    private WallSupportShape getWallSupportShape(BlockGetter level, BlockPos pos, Direction facing) {
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        boolean hasLeft = validConnection(level.getBlockState(pos.relative(left)));
        boolean hasRight = validConnection(level.getBlockState(pos.relative(right)));

        if (hasLeft && hasRight) return WallSupportShape.MIDDLE;
        if (hasLeft) return WallSupportShape.LEFT;
        if (hasRight) return WallSupportShape.RIGHT;
        return WallSupportShape.SINGLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction facing = state.getValue(FACING);
        WallSupportShape shape = state.getValue(CFBlockStateProperties.WALL_SUPPORT_SHAPE);

        return switch (shape) {
            case SINGLE -> getRotatedShape(SINGLE_SHAPE, facing);
            case LEFT -> getRotatedShape(LEFT_SHAPE, facing);
            case RIGHT -> getRotatedShape(RIGHT_SHAPE, facing);
            case MIDDLE -> getRotatedShape(MIDDLE_SHAPE, facing);
        };
    }

    private VoxelShape getRotatedShape(VoxelShape shape, Direction facing) {
        return switch (facing) {
            case EAST -> ShapeUtil.rotateShape(shape, Direction.EAST);
            case SOUTH -> ShapeUtil.rotateShape(shape, Direction.SOUTH);
            case WEST -> ShapeUtil.rotateShape(shape, Direction.WEST);
            default -> shape;
        };
    }

    public boolean validConnection(BlockState state) {
        return state.is(CFBlockTags.WALL_SUPPORT_CONNECTABLE);
    }
}
