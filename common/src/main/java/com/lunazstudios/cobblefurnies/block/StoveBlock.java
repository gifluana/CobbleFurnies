package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.entity.StoveBlockEntity;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFBlockTags;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoveBlock extends BaseEntityBlock {
    public static final MapCodec<StoveBlock> CODEC = simpleCodec(StoveBlock::new);
    public MapCodec<StoveBlock> codec() {
        return CODEC;
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = CFBlockStateProperties.CONNECTED_LEFT;
    public static final BooleanProperty CONNECTED_RIGHT = CFBlockStateProperties.CONNECTED_RIGHT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

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
                .setValue(WATERLOGGED, false)
                .setValue(OPEN, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity stoveBE) {
            player.openMenu(stoveBE);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(
                type,
                CFBlockEntityTypes.STOVE.get(),
                StoveBlockEntity::serverTick
        );
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(level, pos, (Container)blockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntity(pos, state);
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

        boolean isOpen = false;

        return getConnections(this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, waterlogged)
                .setValue(OPEN, isOpen), context.getLevel(), context.getClickedPos());
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
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, WATERLOGGED, OPEN);
    }

    private BlockState getConnections(BlockState state, @Nullable LevelAccessor level, @Nullable BlockPos pos) {
        if (level == null || pos == null) {
            return state.setValue(CONNECTED_LEFT, false).setValue(CONNECTED_RIGHT, false);
        }

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

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        Direction newFacing = rotation.rotate(state.getValue(FACING));
        return getConnections(
                state.setValue(FACING, newFacing),
                null,
                null
        );
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}
