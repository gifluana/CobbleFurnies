package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.block.properties.MountType;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LampBlock extends FurnitureHorizontalBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<MountType> MOUNT_TYPE = CFBlockStateProperties.MOUNT_TYPE;


    protected static final VoxelShape SHAPE_FLOOR = Shapes.or(
            Block.box(5, 0, 5, 11, 6, 11),
            Block.box(3, 6, 3, 13, 16, 13));

    protected static final VoxelShape SHAPE_WALL_NORTH = Shapes.or(
            Block.box(5, 2, 15, 11, 8, 16),
            Block.box(7, 4, 8, 9, 6, 15),
            Block.box(3, 6, 4, 13, 16, 14));
    protected static final VoxelShape SHAPE_WALL_EAST = ShapeUtil.rotateShape(SHAPE_WALL_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_WALL_SOUTH = ShapeUtil.rotateShape(SHAPE_WALL_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_WALL_WEST = ShapeUtil.rotateShape(SHAPE_WALL_NORTH, Direction.WEST);

    protected static final VoxelShape[] WALL_SHAPES = new VoxelShape[] {
            SHAPE_WALL_SOUTH, SHAPE_WALL_WEST, SHAPE_WALL_NORTH, SHAPE_WALL_EAST
    };

    public LampBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(MOUNT_TYPE, MountType.FLOOR)
                .setValue(LIT, Boolean.FALSE)
                .setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        Direction horizontal = context.getHorizontalDirection().getOpposite();

        if (face == Direction.UP || face == Direction.DOWN) {
            return defaultBlockState()
                    .setValue(MOUNT_TYPE, MountType.FLOOR)
                    .setValue(FACING, horizontal)
                    .setValue(LIT, false);
        } else {
            return defaultBlockState()
                    .setValue(MOUNT_TYPE, MountType.WALL)
                    .setValue(FACING, face)
                    .setValue(LIT, false);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(MOUNT_TYPE) == MountType.FLOOR) return SHAPE_FLOOR;
        return WALL_SHAPES[blockState.getValue(FACING).get2DDataValue()];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            boolean powered = blockState.getValue(POWERED);
            level.setBlock(blockPos, blockState.setValue(LIT, !blockState.getValue(LIT) && !powered), 3);
            level.playSound(null, blockPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean isPowered = level.hasNeighborSignal(pos);
            level.setBlock(pos, state.setValue(POWERED, isPowered).setValue(LIT, isPowered), 2);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MOUNT_TYPE, LIT, POWERED);
    }
}
