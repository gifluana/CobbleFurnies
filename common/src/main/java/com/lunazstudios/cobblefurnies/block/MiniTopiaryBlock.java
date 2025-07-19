package com.lunazstudios.cobblefurnies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MiniTopiaryBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);

    protected static final VoxelShape SHAPE_BOTTOM = Shapes.or(
            Block.box(3, 0, 3, 13, 9, 13),
            Block.box(2, 9, 2, 14, 13, 14),
            Block.box(7, 13, 7, 9, 16, 9)
    );

    protected static final VoxelShape SHAPE_TOP = Shapes.or(
            Block.box(7, 0, 7, 9, 1, 9),
            Block.box(4, 1, 4, 12, 6, 12),
            Block.box(2, 6, 2, 14, 16, 14)
    );

    public MiniTopiaryBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        DoubleBlockHalf half = blockState.getValue(HALF);
        BlockPos otherHalfPos = half == DoubleBlockHalf.LOWER ? blockPos.above() : blockPos.below();
        BlockState otherHalfState = level.getBlockState(otherHalfPos);

        if (otherHalfState.is(this) && otherHalfState.getValue(HALF) != half) {
            level.destroyBlock(otherHalfPos, false);
        }

        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return Collections.emptyList();
        }
        return super.getDrops(blockState, builder);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);

        if (direction == (half == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN)) {
            if (neighborState.is(this) && neighborState.getValue(HALF) != half) {
                return state;
            }
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE_BOTTOM : SHAPE_TOP;
    }
}
