package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.registry.CFBlockTags;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SinkBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = CFBlockStateProperties.CONNECTED_LEFT;
    public static final BooleanProperty CONNECTED_RIGHT = CFBlockStateProperties.CONNECTED_RIGHT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape TOP_SHAPE_NORTH = Shapes.or(
            Block.box(0, 11, 0, 16, 16, 16),
            Block.box(2, 12, 12, 14, 16, 12),
            Block.box(2, 12, 2, 14, 12, 12),
            Block.box(2, 12, 2, 14, 16, 2),
            Block.box(14, 12, 2, 14, 16, 12),
            Block.box(2, 12, 2, 2, 16, 12),
            Block.box(7, 16, 13, 9, 21, 15),
            Block.box(10, 16, 13, 12, 17, 15),
            Block.box(4, 16, 13, 6, 17, 15),
            Block.box(7, 19, 9, 9, 21, 13),
            Block.box(7, 18, 9, 9, 19, 11)
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

    public SinkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONNECTED_LEFT, false)
                .setValue(CONNECTED_RIGHT, false)
                .setValue(WATERLOGGED, false));
    }

    protected boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        spawnWaterParticles(level, blockPos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    if (!player.getInventory().add(waterBucket)) {
                        player.drop(waterBucket, false);
                    }
                }

                level.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            }

            spawnWaterParticles(level, blockPos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        spawnWaterParticles(level, blockPos);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void spawnWaterParticles(Level level, BlockPos pos) {
        if (level.isClientSide) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.SPLASH,
                        pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3,
                        pos.getY() + 1.1,
                        pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3,
                        0, 0.05, 0);
            }
        }
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
