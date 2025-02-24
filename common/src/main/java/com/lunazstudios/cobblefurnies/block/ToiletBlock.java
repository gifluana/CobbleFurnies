package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.entity.SeatEntity;
import com.lunazstudios.cobblefurnies.registry.CFRegistry;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class ToiletBlock extends SeatBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    // Closed state voxel shapes
    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(2, 9, 10, 14, 18, 16),
            Block.box(11, 6, 0, 13, 9, 11),
            Block.box(3, 6, 0, 5, 9, 11),
            Block.box(5, 6, 0, 11, 9, 2),
            Block.box(5, 6, 8, 11, 9, 10),
            Block.box(4, 0, 2, 12, 6, 10),
            Block.box(5, 0, 10, 11, 9, 15),
            Block.box(5, 6.5, 2, 11, 6.5, 8),
            Block.box(3, 9, 0, 13, 10, 10),
            Block.box(14, 16, 11, 15, 17, 14)
    );
    protected static final VoxelShape SHAPE_EAST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_SOUTH = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_WEST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.WEST);

    // Open state voxel shapes (you can adjust the boxes as desired)
    protected static final VoxelShape SHAPE_OPEN_NORTH = Shapes.or(
            Block.box(2, 9, 10, 14, 18, 16),
            Block.box(11, 6, 0, 13, 9, 11),
            Block.box(3, 6, 0, 5, 9, 11),
            Block.box(5, 6, 0, 11, 9, 2),
            Block.box(5, 6, 8, 11, 9, 10),
            Block.box(4, 0, 2, 12, 6, 10),
            Block.box(5, 0, 10, 11, 9, 15),
            Block.box(5, 6.5, 2, 11, 6.5, 8),
            // Note: The difference in the open state is that one of the boxes has changed:
            Block.box(3, 9, 0, 13, 10, 10),
            Block.box(14, 16, 11, 15, 17, 14)
    );
    protected static final VoxelShape SHAPE_OPEN_EAST = ShapeUtil.rotateShape(SHAPE_OPEN_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_OPEN_SOUTH = ShapeUtil.rotateShape(SHAPE_OPEN_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_OPEN_WEST = ShapeUtil.rotateShape(SHAPE_OPEN_NORTH, Direction.WEST);

    protected static final VoxelShape[] CLOSED_SHAPES = new VoxelShape[] {
            SHAPE_SOUTH, SHAPE_WEST, SHAPE_NORTH, SHAPE_EAST
    };
    protected static final VoxelShape[] OPEN_SHAPES = new VoxelShape[] {
            SHAPE_OPEN_SOUTH, SHAPE_OPEN_WEST, SHAPE_OPEN_NORTH, SHAPE_OPEN_EAST
    };

    public ToiletBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(OPEN, false));
    }

    @Override
    public float seatHeight(BlockState state) {
        return 0.45F;
    }

    @Override
    public BlockPos primaryDismountLocation(Level level, BlockState state, BlockPos pos) {
        return pos.relative(state.getValue(FACING));
    }

    @Override
    public float setRiderRotation(BlockState state, Entity entity) {
        return state.getValue(FACING).toYRot();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        int index = state.getValue(FACING).get2DDataValue();
        return state.getValue(OPEN) ? OPEN_SHAPES[index] : CLOSED_SHAPES[index];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (CFRegistry.isFakePlayer(player)) return InteractionResult.PASS;
        if (!level.mayInteract(player, pos)) return InteractionResult.PASS;

        boolean open = state.getValue(OPEN);

        if (!open) {
            if (!player.isCrouching()) {
                return InteractionResult.PASS;
            } else {
                level.setBlock(pos, state.setValue(OPEN, true), 3);
                return InteractionResult.SUCCESS;
            }
        }
        else {
            if (player.isCrouching()) {
                level.setBlock(pos, state.setValue(OPEN, false), 3);
                return InteractionResult.SUCCESS;
            }
            if (!isSittable(state) || player.isPassenger()) return InteractionResult.PASS;
            if (isSeatBlocked(level, pos)) return InteractionResult.PASS;
            if (isSeatOccupied(level, pos)) {
                List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(pos));
                if (!seats.isEmpty() && ejectSeatedExceptPlayer(level, seats.get(0))) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
            if (level.isClientSide) return InteractionResult.SUCCESS;
            sitDown(level, pos, getLeashed(player).orElse(player));
            return InteractionResult.SUCCESS;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, waterlogged)
                .setValue(OPEN, false);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED))
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, OPEN);
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
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
