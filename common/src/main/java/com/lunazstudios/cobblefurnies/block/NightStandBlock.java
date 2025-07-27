package com.lunazstudios.cobblefurnies.block;

import com.lunazstudios.cobblefurnies.block.entity.NightStandBlockEntity;
import com.lunazstudios.cobblefurnies.block.properties.CFBlockStateProperties;
import com.lunazstudios.cobblefurnies.util.block.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NightStandBlock extends BaseEntityBlock {
    public static final MapCodec<NightStandBlock> CODEC = simpleCodec(NightStandBlock::new);
    public MapCodec<NightStandBlock> codec() {
        return CODEC;
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty HAS_CONCRETE = CFBlockStateProperties.HAS_CONCRETE;
    public static final EnumProperty<DyeColor> CONCRETE_COLOR = CFBlockStateProperties.CONCRETE_COLOR;

    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(0, 2, 0, 16, 16, 16),
            Block.box(14, 0, 0, 16, 2, 2),
            Block.box(0, 0, 0, 2, 2, 2),
            Block.box(0, 0, 14, 2, 2, 16),
            Block.box(14, 0, 14, 16, 2, 16)
    );
    protected static final VoxelShape SHAPE_EAST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.EAST);
    protected static final VoxelShape SHAPE_SOUTH = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.SOUTH);
    protected static final VoxelShape SHAPE_WEST = ShapeUtil.rotateShape(SHAPE_NORTH, Direction.WEST);

    public NightStandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(HAS_CONCRETE, false)
                .setValue(CONCRETE_COLOR, DyeColor.LIGHT_BLUE)
                .setValue(OPEN, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof NightStandBlockEntity nightStandBE)) return InteractionResult.PASS;

        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (player.isShiftKeyDown() && itemStack.isEmpty()) {
            if (state.getValue(CFBlockStateProperties.HAS_CONCRETE)) {
                DyeColor currentColor = state.getValue(CFBlockStateProperties.CONCRETE_COLOR);
                ResourceLocation concreteId = new ResourceLocation("minecraft", currentColor.getName() + "_concrete");
                Block concreteBlock = BuiltInRegistries.BLOCK.get(concreteId);
                ItemStack concreteStack = new ItemStack(concreteBlock.asItem());

                if (!player.getInventory().add(concreteStack)) {
                    player.drop(concreteStack, false);
                }

                level.setBlock(pos,
                        state.setValue(CFBlockStateProperties.HAS_CONCRETE, false)
                                .setValue(CFBlockStateProperties.CONCRETE_COLOR, DyeColor.WHITE),
                        3
                );

                level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 0.8F, 0.9F);
                return InteractionResult.SUCCESS;
            }
        }

        if (!state.getValue(CFBlockStateProperties.HAS_CONCRETE)) {
            Block block = Block.byItem(itemStack.getItem());
            DyeColor color = getConcreteColor(block);

            if (color != null) {
                level.setBlock(pos,
                        state.setValue(CFBlockStateProperties.HAS_CONCRETE, true)
                                .setValue(CFBlockStateProperties.CONCRETE_COLOR, color),
                        3
                );

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }

        if (blockEntity instanceof NightStandBlockEntity nighstandBE) {
            player.openMenu(nighstandBE);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    private DyeColor getConcreteColor(Block block) {
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation id = new ResourceLocation("minecraft", color.getName() + "_concrete");
            Block expected = BuiltInRegistries.BLOCK.get(id);
            if (block == expected) {
                return color;
            }
        }
        return null;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;

        if (state.getValue(HAS_CONCRETE)) {
            DyeColor color = state.getValue(CONCRETE_COLOR);
            ResourceLocation concreteId = new ResourceLocation("minecraft", color.getName() + "_concrete");
            Block concreteBlock = BuiltInRegistries.BLOCK.get(concreteId);
            ItemStack concreteStack = new ItemStack(concreteBlock.asItem());
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), concreteStack);
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(level, pos, (Container)blockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NightStandBlockEntity nighstandBE) nighstandBE.recheckOpen();
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NightStandBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        switch (facing) {
            case EAST:
                return SHAPE_EAST;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        Direction facing = context.getHorizontalDirection().getOpposite();

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, waterlogged)
                .setValue(HAS_CONCRETE, false)
                .setValue(CONCRETE_COLOR, DyeColor.LIGHT_BLUE)
                .setValue(OPEN, false);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, OPEN, HAS_CONCRETE, CONCRETE_COLOR);
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
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}
