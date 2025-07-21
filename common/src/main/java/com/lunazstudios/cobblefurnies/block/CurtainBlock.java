package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.block.properties.CurtainHorizontalPart;
import com.lunazstudios.cobblefurnies.block.properties.CurtainVerticalPart;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CurtainBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<CurtainVerticalPart> VERTICAL = CFBlockStateProperties.CURTAIN_VERTICAL;
    public static final EnumProperty<CurtainHorizontalPart> HORIZONTAL = CFBlockStateProperties.CURTAIN_HORIZONTAL;
    public static final BooleanProperty OPEN = CFBlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    protected static final VoxelShape SHAPE_TOP_NORTH = Shapes.or(
            Block.box(0, 14, 14, 16, 16, 16),
            Block.box(0, 0, 14.5, 16, 14, 15.5));
    protected static final VoxelShape SHAPE_TOP_EAST = ShapeUtil.rotateShape(SHAPE_TOP_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_TOP_SOUTH = ShapeUtil.rotateShape(SHAPE_TOP_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_TOP_WEST = ShapeUtil.rotateShape(SHAPE_TOP_NORTH, Direction.WEST);

    protected static final VoxelShape[] SHAPES_TOP = new VoxelShape[] {
            SHAPE_TOP_SOUTH, SHAPE_TOP_WEST, SHAPE_TOP_NORTH, SHAPE_TOP_EAST
    };

    protected static final VoxelShape SHAPE_BOTTOM_NORTH = Block.box(0, 0, 14.5, 16, 16, 15.5);
    protected static final VoxelShape SHAPE_BOTTOM_EAST = ShapeUtil.rotateShape(SHAPE_BOTTOM_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_BOTTOM_SOUTH = ShapeUtil.rotateShape(SHAPE_BOTTOM_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_BOTTOM_WEST = ShapeUtil.rotateShape(SHAPE_BOTTOM_NORTH, Direction.WEST);

    protected static final VoxelShape[] SHAPES_BOTTOM = new VoxelShape[] {
            SHAPE_BOTTOM_SOUTH, SHAPE_BOTTOM_WEST, SHAPE_BOTTOM_NORTH, SHAPE_BOTTOM_EAST
    };

    public CurtainBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL, CurtainVerticalPart.BOTTOM)
                .setValue(HORIZONTAL, CurtainHorizontalPart.MIDDLE)
                .setValue(OPEN, false)
                .setValue(HINGE, DoorHingeSide.LEFT));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int facingIndex = facing.get2DDataValue();

        if (state.getValue(VERTICAL) == CurtainVerticalPart.TOP) {
            return SHAPES_TOP[facingIndex];
        } else {
            return SHAPES_BOTTOM[facingIndex];
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            CurtainVerticalPart vertical = state.getValue(VERTICAL);
            BlockPos otherPos = vertical == CurtainVerticalPart.TOP ? pos.below() : pos.above();
            BlockState otherState = level.getBlockState(otherPos);

            if (otherState.getBlock() == this &&
                    otherState.getValue(VERTICAL) != vertical &&
                    otherState.getValue(FACING) == state.getValue(FACING)) {
                level.destroyBlock(otherPos, !player.isCreative());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : getShape(state, world, pos, context);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VERTICAL, HORIZONTAL, OPEN, HINGE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos topPos = ctx.getClickedPos();
        BlockPos bottomPos = topPos.below();
        Level level = ctx.getLevel();

        if (!level.getBlockState(bottomPos).canBeReplaced(ctx)) return null;

        Direction facing = ctx.getHorizontalDirection().getOpposite();
        Vec3 clickVec = ctx.getClickLocation().subtract(Vec3.atLowerCornerOf(topPos));
        Direction right = facing.getClockWise();
        double side = right.getAxis().choose(clickVec.x, 0, clickVec.z);
        DoorHingeSide hinge = side > 0.5 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;

        BlockState topState = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(VERTICAL, CurtainVerticalPart.TOP)
                .setValue(OPEN, false)
                .setValue(HINGE, hinge);

        CurtainHorizontalPart horizontal = getCurtainHorizontalType(topState, level, topPos);

        BlockState bottomState = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(VERTICAL, CurtainVerticalPart.BOTTOM)
                .setValue(OPEN, false)
                .setValue(HINGE, hinge)
                .setValue(HORIZONTAL, horizontal);
        level.setBlock(bottomPos, bottomState, 3);

        return topState.setValue(HORIZONTAL, horizontal);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        CurtainVerticalPart vertical = state.getValue(VERTICAL);

        level.setBlock(pos, state.setValue(HORIZONTAL, getCurtainHorizontalType(state, level, pos)), 3);

        BlockPos leftPos = pos.relative(facing.getCounterClockWise());
        BlockPos rightPos = pos.relative(facing.getClockWise());

        for (BlockPos sidePos : List.of(leftPos, rightPos)) {
            BlockState sideState = level.getBlockState(sidePos);
            if (sideState.getBlock() == this && sideState.getValue(VERTICAL) == vertical && sideState.getValue(FACING) == facing) {
                level.setBlock(sidePos, sideState.setValue(HORIZONTAL, getCurtainHorizontalType(sideState, level, sidePos)), 3);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (dir.getAxis().isHorizontal()) {
            return state.setValue(HORIZONTAL, getCurtainHorizontalType(state, level, pos));
        }
        return state;
    }

    private CurtainHorizontalPart getCurtainHorizontalType(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        boolean hasLeft = isSameCurtain(level.getBlockState(pos.relative(left)), state);
        boolean hasRight = isSameCurtain(level.getBlockState(pos.relative(right)), state);

        if (hasLeft && hasRight) return CurtainHorizontalPart.MIDDLE;
        if (hasLeft) return CurtainHorizontalPart.RIGHT;
        if (hasRight) return CurtainHorizontalPart.LEFT;
        return CurtainHorizontalPart.SINGLE;
    }

    private boolean isSameCurtain(BlockState neighbor, BlockState base) {
        return neighbor.getBlock() == base.getBlock()
                && neighbor.getValue(FACING) == base.getValue(FACING)
                && neighbor.getValue(VERTICAL) == base.getValue(VERTICAL);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (state.getValue(VERTICAL) == CurtainVerticalPart.BOTTOM) {
            toggleCurtain(level, pos.above(), state.getValue(FACING));
        } else {
            toggleCurtain(level, pos, state.getValue(FACING));
        }

        return InteractionResult.CONSUME;
    }

    private void toggleCurtain(Level level, BlockPos origin, Direction facing) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(origin);

        BlockState originState = level.getBlockState(origin);
        boolean targetOpen = !originState.getValue(OPEN);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof CurtainBlock) || state.getValue(FACING) != facing) continue;

            if (state.getValue(OPEN) != targetOpen) {
                level.setBlock(pos, state.setValue(OPEN, targetOpen), 3);
            }

            Direction verticalDir = state.getValue(VERTICAL) == CurtainVerticalPart.TOP ? Direction.DOWN : Direction.UP;
            BlockPos verticalPos = pos.relative(verticalDir);
            BlockState verticalState = level.getBlockState(verticalPos);
            if (verticalState.getBlock() == this && verticalState.getValue(FACING) == facing) {
                queue.add(verticalPos);
            }

            for (Direction side : List.of(facing.getClockWise(), facing.getCounterClockWise())) {
                BlockPos sidePos = pos.relative(side);
                BlockState sideState = level.getBlockState(sidePos);
                if (sideState.getBlock() == this &&
                        sideState.getValue(FACING) == facing &&
                        sideState.getValue(VERTICAL) == state.getValue(VERTICAL)) {
                    queue.add(sidePos);
                }
            }
        }
    }
}