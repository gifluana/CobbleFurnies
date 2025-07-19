package com.lunazstudios.cobblefurnies.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lunazstudios.cobblefurnies.block.properties.CabinetryShape;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CabinetryBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<CabinetryShape> SHAPE = EnumProperty.create("shape", CabinetryShape.class);

    protected static final VoxelShape TOP_SHAPE_NORTH = Block.box(0, 11, 0, 16, 16, 16);

    protected static final VoxelShape BOTTOM_DEFAULT_SHAPE_NORTH = Block.box(0, 0, 1, 16, 11, 15);
    protected static final VoxelShape BOTTOM_DEFAULT_SHAPE_EAST = ShapeUtil.rotateShape(BOTTOM_DEFAULT_SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_DEFAULT_SHAPE_SOUTH = ShapeUtil.rotateShape(BOTTOM_DEFAULT_SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_DEFAULT_SHAPE_WEST = ShapeUtil.rotateShape(BOTTOM_DEFAULT_SHAPE_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_INNER_CORNER_LEFT_NORTH = Shapes.or(Block.box(15, 0, 1, 16, 11, 15), Block.box(1, 0, 0, 15, 11, 15));
    protected static final VoxelShape BOTTOM_INNER_CORNER_LEFT_EAST = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_LEFT_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_INNER_CORNER_LEFT_SOUTH = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_LEFT_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_INNER_CORNER_LEFT_WEST = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_LEFT_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_INNER_CORNER_RIGHT_NORTH = Shapes.or(Block.box(0, 0, 1, 1, 11, 15), Block.box(1, 0, 0, 15, 11, 15));
    protected static final VoxelShape BOTTOM_INNER_CORNER_RIGHT_EAST = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_RIGHT_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_INNER_CORNER_RIGHT_SOUTH = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_RIGHT_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_INNER_CORNER_RIGHT_WEST = ShapeUtil.rotateShape(BOTTOM_INNER_CORNER_RIGHT_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_OUTER_CORNER_LEFT_NORTH = Shapes.or(Block.box(1, 0, 1, 15, 11, 15), Block.box(1, 0, 15, 15, 11, 16), Block.box(15, 0, 1, 16, 11, 15));
    protected static final VoxelShape BOTTOM_OUTER_CORNER_LEFT_EAST = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_LEFT_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_OUTER_CORNER_LEFT_SOUTH = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_LEFT_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_OUTER_CORNER_LEFT_WEST = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_LEFT_NORTH, Direction.WEST);

    protected static final VoxelShape BOTTOM_OUTER_CORNER_RIGHT_NORTH = Shapes.or(Block.box(1, 0, 1, 15, 11, 15), Block.box(0, 0, 1, 1, 11, 15), Block.box(1, 0, 15, 15, 11, 16));
    protected static final VoxelShape BOTTOM_OUTER_CORNER_RIGHT_EAST = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_RIGHT_NORTH, Direction.EAST);
    protected static final VoxelShape BOTTOM_OUTER_CORNER_RIGHT_SOUTH = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_RIGHT_NORTH, Direction.SOUTH);
    protected static final VoxelShape BOTTOM_OUTER_CORNER_RIGHT_WEST = ShapeUtil.rotateShape(BOTTOM_OUTER_CORNER_RIGHT_NORTH, Direction.WEST);


    public CabinetryBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, CabinetryShape.DEFAULT)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state.setValue(SHAPE, this.getShape(state, context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return state.setValue(SHAPE, this.getShape(state, level, pos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        CabinetryShape shape = state.getValue(SHAPE);

        VoxelShape topShape = TOP_SHAPE_NORTH;

        VoxelShape bottomShape = switch (shape) {
            case INNER_CORNER_LEFT -> switch (facing) {
                case EAST -> BOTTOM_INNER_CORNER_LEFT_EAST;
                case SOUTH -> BOTTOM_INNER_CORNER_LEFT_SOUTH;
                case WEST -> BOTTOM_INNER_CORNER_LEFT_WEST;
                default -> BOTTOM_INNER_CORNER_LEFT_NORTH;
            };
            case INNER_CORNER_RIGHT -> switch (facing) {
                case EAST -> BOTTOM_INNER_CORNER_RIGHT_EAST;
                case SOUTH -> BOTTOM_INNER_CORNER_RIGHT_SOUTH;
                case WEST -> BOTTOM_INNER_CORNER_RIGHT_WEST;
                default -> BOTTOM_INNER_CORNER_RIGHT_NORTH;
            };
            case OUTER_CORNER_LEFT -> switch (facing) {
                case EAST -> BOTTOM_OUTER_CORNER_LEFT_EAST;
                case SOUTH -> BOTTOM_OUTER_CORNER_LEFT_SOUTH;
                case WEST -> BOTTOM_OUTER_CORNER_LEFT_WEST;
                default -> BOTTOM_OUTER_CORNER_LEFT_NORTH;
            };
            case OUTER_CORNER_RIGHT -> switch (facing) {
                case EAST -> BOTTOM_OUTER_CORNER_RIGHT_EAST;
                case SOUTH -> BOTTOM_OUTER_CORNER_RIGHT_SOUTH;
                case WEST -> BOTTOM_OUTER_CORNER_RIGHT_WEST;
                default -> BOTTOM_OUTER_CORNER_RIGHT_NORTH;
            };
            default -> switch (facing) {
                case EAST -> BOTTOM_DEFAULT_SHAPE_EAST;
                case SOUTH -> BOTTOM_DEFAULT_SHAPE_SOUTH;
                case WEST -> BOTTOM_DEFAULT_SHAPE_WEST;
                default -> BOTTOM_DEFAULT_SHAPE_NORTH;
            };
        };

        return Shapes.or(topShape, bottomShape);
    }

    private CabinetryShape getShape(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        Direction front = this.getNeighborDirection(level, pos, facing.getOpposite());
        if (front != null) {
            if (front == facing.getClockWise()) {
                return CabinetryShape.OUTER_CORNER_RIGHT;
            } else if (front == facing.getCounterClockWise()) {
                return CabinetryShape.OUTER_CORNER_LEFT;
            }
        }

        Direction behind = this.getNeighborDirection(level, pos, facing);
        if (behind != null) {
            if (behind == facing.getClockWise()) {
                return CabinetryShape.INNER_CORNER_LEFT;
            } else if (behind == facing.getCounterClockWise()) {
                return CabinetryShape.INNER_CORNER_RIGHT;
            }
        }

        return CabinetryShape.DEFAULT;
    }

    private Direction getNeighborDirection(LevelReader level, BlockPos pos, Direction side) {
        BlockState neighborState = level.getBlockState(pos.relative(side));
        if (neighborState.getBlock() instanceof CabinetryBlock) {
            return neighborState.getValue(FACING);
        }
        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}
