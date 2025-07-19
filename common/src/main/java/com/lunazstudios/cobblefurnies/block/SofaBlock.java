package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.block.properties.SofaType;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SofaBlock extends SeatBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<SofaType> TYPE = CFBlockStateProperties.SOFA_TYPE;

    protected static final VoxelShape SOFA_NORTH = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(13, 7, 0, 16, 11, 12),
            Block.box(0, 7, 0, 3, 11, 12),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(0, 0, 0, 2, 3, 2),
            Block.box(14, 0, 0, 16, 3, 2),
            Block.box(14, 0, 14, 16, 3, 16),
            Block.box(0, 0, 14, 2, 3, 16)
    );
    protected static final VoxelShape SOFA_EAST = ShapeUtil.rotateShape(SOFA_NORTH, Direction.EAST);
    protected static final VoxelShape SOFA_SOUTH = ShapeUtil.rotateShape(SOFA_NORTH, Direction.SOUTH);
    protected static final VoxelShape SOFA_WEST = ShapeUtil.rotateShape(SOFA_NORTH, Direction.WEST);

    protected static final VoxelShape SOFA_LEFT_NORTH = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(13, 7, 0, 16, 11, 12),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(14, 0, 0, 16, 3, 2),
            Block.box(14, 0, 14, 16, 3, 16)
    );
    protected static final VoxelShape SOFA_LEFT_EAST = ShapeUtil.rotateShape(SOFA_LEFT_NORTH, Direction.EAST);
    protected static final VoxelShape SOFA_LEFT_SOUTH = ShapeUtil.rotateShape(SOFA_LEFT_NORTH, Direction.SOUTH);
    protected static final VoxelShape SOFA_LEFT_WEST = ShapeUtil.rotateShape(SOFA_LEFT_NORTH, Direction.WEST);

    protected static final VoxelShape SOFA_MIDDLE_NORTH = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(0, 3, 0, 16, 7, 16)
    );
    protected static final VoxelShape SOFA_MIDDLE_EAST = ShapeUtil.rotateShape(SOFA_MIDDLE_NORTH, Direction.EAST);
    protected static final VoxelShape SOFA_MIDDLE_SOUTH = ShapeUtil.rotateShape(SOFA_MIDDLE_NORTH, Direction.SOUTH);
    protected static final VoxelShape SOFA_MIDDLE_WEST = ShapeUtil.rotateShape(SOFA_MIDDLE_NORTH, Direction.WEST);

    protected static final VoxelShape SOFA_RIGHT_NORTH = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(0, 7, 0, 3, 11, 12),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(0, 0, 0, 2, 3, 2),
            Block.box(0, 0, 14, 2, 3, 16)
    );
    protected static final VoxelShape SOFA_RIGHT_EAST = ShapeUtil.rotateShape(SOFA_RIGHT_NORTH, Direction.EAST);
    protected static final VoxelShape SOFA_RIGHT_SOUTH = ShapeUtil.rotateShape(SOFA_RIGHT_NORTH, Direction.SOUTH);
    protected static final VoxelShape SOFA_RIGHT_WEST = ShapeUtil.rotateShape(SOFA_RIGHT_NORTH, Direction.WEST);

    protected static final VoxelShape SOFA_INNER_NORTH_LEFT = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(12, 7, 0, 16, 16, 12),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(0, 0, 0, 2, 3, 2),
            Block.box(14, 0, 0, 16, 3, 2),
            Block.box(14, 0, 14, 16, 3, 16),
            Block.box(0, 0, 14, 2, 3, 16)
    );
    protected static final VoxelShape SOFA_INNER_EAST_LEFT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_LEFT, Direction.EAST);
    protected static final VoxelShape SOFA_INNER_SOUTH_LEFT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_LEFT, Direction.SOUTH);
    protected static final VoxelShape SOFA_INNER_WEST_LEFT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_LEFT, Direction.WEST);

    protected static final VoxelShape SOFA_OUTER_NORTH_LEFT = Shapes.or(
            Block.box(12, 7, 12, 16, 16, 16),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(0, 0, 0, 2, 3, 2),
            Block.box(14, 0, 14, 16, 3, 16)
    );
    protected static final VoxelShape SOFA_OUTER_EAST_LEFT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_LEFT, Direction.EAST);
    protected static final VoxelShape SOFA_OUTER_SOUTH_LEFT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_LEFT, Direction.SOUTH);
    protected static final VoxelShape SOFA_OUTER_WEST_LEFT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_LEFT, Direction.WEST);

    protected static final VoxelShape SOFA_INNER_NORTH_RIGHT = Shapes.or(
            Block.box(0, 7, 12, 16, 16, 16),
            Block.box(0, 7, 0, 4, 16, 12),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(14, 0, 0, 16, 3, 2),
            Block.box(0, 0, 0, 2, 3, 2),
            Block.box(0, 0, 14, 2, 3, 16),
            Block.box(14, 0, 14, 16, 3, 16)
    );
    protected static final VoxelShape SOFA_INNER_EAST_RIGHT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_RIGHT, Direction.EAST);
    protected static final VoxelShape SOFA_INNER_SOUTH_RIGHT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_RIGHT, Direction.SOUTH);
    protected static final VoxelShape SOFA_INNER_WEST_RIGHT = ShapeUtil.rotateShape(SOFA_INNER_NORTH_RIGHT, Direction.WEST);

    protected static final VoxelShape SOFA_OUTER_NORTH_RIGHT = Shapes.or(
            Block.box(0, 7, 12, 4, 16, 16),
            Block.box(0, 3, 0, 16, 7, 16),
            Block.box(14, 0, 0, 16, 3, 2),
            Block.box(0, 0, 14, 2, 3, 16)
    );
    protected static final VoxelShape SOFA_OUTER_EAST_RIGHT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_RIGHT, Direction.EAST);
    protected static final VoxelShape SOFA_OUTER_SOUTH_RIGHT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_RIGHT, Direction.SOUTH);
    protected static final VoxelShape SOFA_OUTER_WEST_RIGHT = ShapeUtil.rotateShape(SOFA_OUTER_NORTH_RIGHT, Direction.WEST);


    public SofaBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public float seatHeight(BlockState state) {
        return 0.35F;
    }

    @Override
    public BlockPos primaryDismountLocation(Level level, BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockState stateRelative = level.getBlockState(pos.relative(facing));

        if (stateRelative.getBlock() instanceof SofaBlock) {
            Direction facingRelative = stateRelative.getValue(FACING);
            if (facing != facingRelative) {
                return pos.relative(facing).relative(facingRelative);
            }
        }
        return pos.relative(facing);
    }

    @Override
    public float setRiderRotation(BlockState state, Entity entity) {
        float corner = switch (state.getValue(TYPE)) {
            case INNER_LEFT, OUTER_LEFT -> -45.0F;
            case INNER_RIGHT, OUTER_RIGHT -> 45.0F;
            default -> 0.0F;
        };
        return state.getValue(FACING).toYRot() + corner;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return blockState.setValue(TYPE, getConnection(blockState, context.getLevel(), blockPos));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (direction.getAxis().isHorizontal() && level instanceof Level realLevel) {
            return state.setValue(TYPE, getConnection(state, realLevel, currentPos));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        SofaType type = state.getValue(TYPE);

        return switch (type) {
            case SINGLE -> switch (facing) {
                case NORTH -> SOFA_NORTH;
                case EAST -> SOFA_EAST;
                case SOUTH -> SOFA_SOUTH;
                case WEST -> SOFA_WEST;
                default -> SOFA_NORTH;
            };
            case LEFT -> switch (facing) {
                case NORTH -> SOFA_LEFT_NORTH;
                case EAST -> SOFA_LEFT_EAST;
                case SOUTH -> SOFA_LEFT_SOUTH;
                case WEST -> SOFA_LEFT_WEST;
                default -> SOFA_NORTH;
            };
            case MIDDLE -> switch (facing) {
                case NORTH -> SOFA_MIDDLE_NORTH;
                case EAST -> SOFA_MIDDLE_EAST;
                case SOUTH -> SOFA_MIDDLE_SOUTH;
                case WEST -> SOFA_MIDDLE_WEST;
                default -> SOFA_NORTH;
            };
            case RIGHT -> switch (facing) {
                case NORTH -> SOFA_RIGHT_NORTH;
                case EAST -> SOFA_RIGHT_EAST;
                case SOUTH -> SOFA_RIGHT_SOUTH;
                case WEST -> SOFA_RIGHT_WEST;
                default -> SOFA_NORTH;
            };
            case INNER_LEFT -> switch (facing) {
                case NORTH -> SOFA_INNER_NORTH_LEFT;
                case EAST -> SOFA_INNER_EAST_LEFT;
                case SOUTH -> SOFA_INNER_SOUTH_LEFT;
                case WEST -> SOFA_INNER_WEST_LEFT;
                default -> SOFA_NORTH;
            };
            case INNER_RIGHT -> switch (facing) {
                case NORTH -> SOFA_INNER_NORTH_RIGHT;
                case EAST -> SOFA_INNER_EAST_RIGHT;
                case SOUTH -> SOFA_INNER_SOUTH_RIGHT;
                case WEST -> SOFA_INNER_WEST_RIGHT;
                default -> SOFA_NORTH;
            };
            case OUTER_LEFT -> switch (facing) {
                case NORTH -> SOFA_OUTER_NORTH_LEFT;
                case EAST -> SOFA_OUTER_EAST_LEFT;
                case SOUTH -> SOFA_OUTER_SOUTH_LEFT;
                case WEST -> SOFA_OUTER_WEST_LEFT;
                default -> SOFA_NORTH;
            };
            case OUTER_RIGHT -> switch (facing) {
                case NORTH -> SOFA_OUTER_NORTH_RIGHT;
                case EAST -> SOFA_OUTER_EAST_RIGHT;
                case SOUTH -> SOFA_OUTER_SOUTH_RIGHT;
                case WEST -> SOFA_OUTER_WEST_RIGHT;
                default -> SOFA_NORTH;
            };
        };
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    public static SofaType getConnection(BlockState state, Level level, BlockPos pos) {
        if (level == null) return SofaType.SINGLE;
        Direction facing = state.getValue(FACING);

        Direction dir1;
        BlockState state1 = level.getBlockState(pos.relative(facing));
        if (state1.getBlock() instanceof SofaBlock && (dir1 = state1.getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && isDifferentOrientation(state, level, pos, dir1.getOpposite())) {
            if (dir1 == facing.getCounterClockWise()) {
                return SofaType.INNER_LEFT;
            }
            return SofaType.INNER_RIGHT;
        }

        Direction dir2;
        BlockState state2 = level.getBlockState(pos.relative(facing.getOpposite()));
        if (state2.getBlock() instanceof SofaBlock && (dir2 = state2.getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && isDifferentOrientation(state, level, pos, dir2)) {
            if (dir2 == facing.getCounterClockWise()) {
                return SofaType.OUTER_LEFT;
            }
            return SofaType.OUTER_RIGHT;
        }

        boolean left = canConnect(level, pos, state.getValue(FACING).getCounterClockWise());
        boolean right = canConnect(level, pos, state.getValue(FACING).getClockWise());
        if (left && right) {
            return SofaType.MIDDLE;
        }
        else if (left) {
            return SofaType.LEFT;
        }
        else if (right) {
            return SofaType.RIGHT;
        }
        return SofaType.SINGLE;
    }

    public static boolean canConnect(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos.relative(direction));
        return state.getBlock() instanceof SofaBlock;
    }

    public static boolean isDifferentOrientation(BlockState state, Level level, BlockPos pos, Direction dir) {
        BlockState blockState = level.getBlockState(pos.relative(dir));
        return !(blockState.getBlock() instanceof SofaBlock) || blockState.getValue(FACING) != state.getValue(FACING);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        Direction newFacing = rotation.rotate(state.getValue(FACING));
        SofaType newType = rotateType(state.getValue(TYPE), rotation);
        return state.setValue(FACING, newFacing).setValue(TYPE, newType);
    }

    private static SofaType rotateType(SofaType type, Rotation rotation) {
        return switch (type) {
            case INNER_LEFT -> (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
                    ? SofaType.INNER_RIGHT : type;
            case INNER_RIGHT -> (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
                    ? SofaType.INNER_LEFT : type;
            case OUTER_LEFT -> (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
                    ? SofaType.OUTER_RIGHT : type;
            case OUTER_RIGHT -> (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
                    ? SofaType.OUTER_LEFT : type;
            default -> type;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        SofaType type = state.getValue(TYPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return switch (type) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.INNER_RIGHT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.INNER_LEFT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return switch (type) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.INNER_LEFT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.INNER_RIGHT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(TYPE, SofaType.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
        }
        return super.mirror(state, mirror);
    }
}
