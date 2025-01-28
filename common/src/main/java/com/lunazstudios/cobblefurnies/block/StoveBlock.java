package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockTags;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoveBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = CFBlockStateProperties.CONNECTED_LEFT;
    public static final BooleanProperty CONNECTED_RIGHT = CFBlockStateProperties.CONNECTED_RIGHT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape TOP_SHAPE_NORTH = Shapes.or(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(5, 16, 5, 11, 17, 11),
            Block.box(4, 16.5, 8, 12, 18.5, 8),
            Block.box(4, 16.5, 8, 12, 18.5, 8)
    );
    protected static final VoxelShape TOP_SHAPE_EAST = ShapeUtil.rotateShape(TOP_SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape TOP_SHAPE_SOUTH = ShapeUtil.rotateShape(TOP_SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape TOP_SHAPE_WEST = ShapeUtil.rotateShape(TOP_SHAPE_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_SELF_SHAPE = Block.box(1, 0, 1, 15, 11, 15);

    protected static final VoxelShape BOTTOM_MIDDLE_SHAPE_NORTH = Block.box(0, 0, 1, 16, 11, 15);
    protected static final VoxelShape BOTTOM_MIDDLE_SHAPE_EAST = ShapeUtil.rotateShape(BOTTOM_MIDDLE_SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_MIDDLE_SHAPE_SOUTH = ShapeUtil.rotateShape(BOTTOM_MIDDLE_SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_MIDDLE_SHAPE_WEST = ShapeUtil.rotateShape(BOTTOM_MIDDLE_SHAPE_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_RIGHT_SHAPE_NORTH = Block.box(1, 0, 1, 16, 11, 15);
    protected static final VoxelShape BOTTOM_RIGHT_SHAPE_EAST = ShapeUtil.rotateShape(BOTTOM_RIGHT_SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_RIGHT_SHAPE_SOUTH = ShapeUtil.rotateShape(BOTTOM_RIGHT_SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_RIGHT_SHAPE_WEST = ShapeUtil.rotateShape(BOTTOM_RIGHT_SHAPE_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_LEFT_SHAPE_NORTH = Block.box(0, 0, 1, 15, 11, 15);
    protected static final VoxelShape BOTTOM_LEFT_SHAPE_EAST = ShapeUtil.rotateShape(BOTTOM_LEFT_SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_LEFT_SHAPE_SOUTH = ShapeUtil.rotateShape(BOTTOM_LEFT_SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_LEFT_SHAPE_WEST = ShapeUtil.rotateShape(BOTTOM_LEFT_SHAPE_NORTH, Direction.WEST);

    public StoveBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONNECTED_LEFT, false)
                .setValue(CONNECTED_RIGHT, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean connectedLeft = state.getValue(CONNECTED_LEFT);
        boolean connectedRight = state.getValue(CONNECTED_RIGHT);

        VoxelShape topShape;
        VoxelShape bottomShape;

        switch (facing) {
            case EAST:
                topShape = TOP_SHAPE_EAST;
                break;
            case SOUTH:
                topShape = TOP_SHAPE_SOUTH;
                break;
            case WEST:
                topShape = TOP_SHAPE_WEST;
                break;
            default:
                topShape = TOP_SHAPE_NORTH;
                break;
        }

        if (connectedLeft && connectedRight) {
            switch (facing) {
                case EAST:
                    bottomShape = BOTTOM_MIDDLE_SHAPE_EAST;
                    break;
                case SOUTH:
                    bottomShape = BOTTOM_MIDDLE_SHAPE_SOUTH;
                    break;
                case WEST:
                    bottomShape = BOTTOM_MIDDLE_SHAPE_WEST;
                    break;
                default:
                    bottomShape = BOTTOM_MIDDLE_SHAPE_NORTH;
                    break;
            }
        } else if (connectedLeft) {
            switch (facing) {
                case EAST:
                    bottomShape = BOTTOM_LEFT_SHAPE_EAST;
                    break;
                case SOUTH:
                    bottomShape = BOTTOM_LEFT_SHAPE_SOUTH;
                    break;
                case WEST:
                    bottomShape = BOTTOM_LEFT_SHAPE_WEST;
                    break;
                default:
                    bottomShape = BOTTOM_LEFT_SHAPE_NORTH;
                    break;
            }
        } else if (connectedRight) {
            switch (facing) {
                case EAST:
                    bottomShape = BOTTOM_RIGHT_SHAPE_EAST;
                    break;
                case SOUTH:
                    bottomShape = BOTTOM_RIGHT_SHAPE_SOUTH;
                    break;
                case WEST:
                    bottomShape = BOTTOM_RIGHT_SHAPE_WEST;
                    break;
                default:
                    bottomShape = BOTTOM_RIGHT_SHAPE_NORTH;
                    break;
            }
        } else {
            bottomShape = BOTTOM_SELF_SHAPE;
        }

        return Shapes.or(topShape, bottomShape);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        Direction facing = context.getHorizontalDirection().getOpposite();
        return getConnections(this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, waterlogged), context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return getConnections(state, level, currentPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, WATERLOGGED);
    }

    private BlockState getConnections(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean left = validConnection(level.getBlockState(pos.relative(facing.getCounterClockWise())));
        boolean right = validConnection(level.getBlockState(pos.relative(facing.getClockWise())));
        return state.setValue(CONNECTED_LEFT, left).setValue(CONNECTED_RIGHT, right);
    }

    private boolean validConnection(BlockState state) {
        return state.is(CFBlockTags.SINKS_CONNECTABLE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
