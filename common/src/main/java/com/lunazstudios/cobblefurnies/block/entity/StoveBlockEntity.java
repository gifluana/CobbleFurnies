package com.lunazstudios.cobblefurnies.block.entity;

import com.cobblemon.mod.common.CobblemonRecipeTypes;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.item.components.PotComponent;
import com.cobblemon.mod.common.item.crafting.CookingPotRecipeBase;
import com.lunazstudios.cobblefurnies.block.StoveBlock;
import com.lunazstudios.cobblefurnies.client.StoveClientHooks;
import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StoveBlockEntity extends BaseContainerBlockEntity
        implements CraftingContainer, RecipeCraftingHolder, StackedContentsCompatible, WorldlyContainer {

    public static final int RESULT_SLOT = 0;
    public static final int[] CRAFTING_SLOTS = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] SEASONING_SLOTS = {10, 11, 12};
    public static final int PREVIEW_SLOT = 13;
    public static final int TOTAL_SLOTS = 14;

    private final NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private PotComponent potComponent;

    private int cookingProgress = 0;
    private int cookingTotalTime = 200;

    public SoundEvent getRunningSound() { return runningSound; }
    public SoundEvent getAmbientSound() { return ambientSound; }

    private final SoundEvent runningSound = CobblemonSounds.CAMPFIRE_POT_ACTIVE;
    private final SoundEvent ambientSound = CobblemonSounds.CAMPFIRE_POT_AMBIENT;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> cookingProgress;
                case 1 -> cookingTotalTime;
                case 2 -> level != null && getBlockState().getValue(StoveBlock.LID) ? 0 : 1;
                case 3 -> 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> cookingProgress = value;
                case 1 -> cookingTotalTime = value;
                case 2 -> toggleLid(value == 1);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public void toggleLid(boolean isOpen) {
        if (level == null) return;

        BlockState state = getBlockState();
        BlockState newState = state.setValue(StoveBlock.LID, !isOpen);
        level.setBlock(worldPosition, newState, Block.UPDATE_ALL);

        level.playSound(
                null,
                worldPosition,
                isOpen ? CobblemonSounds.CAMPFIRE_POT_OPEN : CobblemonSounds.CAMPFIRE_POT_CLOSE,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        level.gameEvent(
                null,
                isOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE,
                worldPosition
        );

        setChanged();
    }

    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(CFBlockEntityTypes.STOVE.get(), pos, state);
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.translatable("container.cobblefurnies.stove");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getDefaultName();
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }
	
    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            this.items.set(i, i < newItems.size() ? newItems.get(i) : ItemStack.EMPTY);
        }
    }

    @Override
    public int getContainerSize() {
        return TOTAL_SLOTS;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new StoveMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index != RESULT_SLOT && index != PREVIEW_SLOT;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{RESULT_SLOT};
        } else if (side == Direction.UP) {
            return CRAFTING_SLOTS;
        } else {
            return SEASONING_SLOTS;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == RESULT_SLOT;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {

    }

    @Override
    @Nullable
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack itemStack : this.items) {
            stackedContents.accountSimpleStack(itemStack);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
        if (level.isClientSide) return;

        boolean wasCooking = stove.cookingProgress > 0;

        CraftingInput input = CraftingInput.of(3, 3, stove.items.subList(1, 10));
        Optional<RecipeHolder<CookingPotRecipeBase>> recipeOpt =
                fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING());
        if (recipeOpt.isEmpty()) {
            recipeOpt = fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_SHAPELESS());
        }

        if (recipeOpt.isEmpty()) {
            stove.cookingProgress = 0;
            stove.setItem(PREVIEW_SLOT, ItemStack.EMPTY);
        } else {
            RecipeHolder<CookingPotRecipeBase> recipeHolder = recipeOpt.get();
            CookingPotRecipeBase recipe = recipeHolder.value();
            ItemStack result = recipe.assemble(input, level.registryAccess());

            List<ItemStack> validSeasonings = stove.getSeasonings().stream()
                    .filter(stack -> stack.is(recipe.getSeasoningTag()))
                    .toList();
            recipe.applySeasoning(result, validSeasonings);

            stove.setItem(PREVIEW_SLOT, result);

            ItemStack resultSlot = stove.getItem(RESULT_SLOT);
            boolean lidClosed = state.getValue(StoveBlock.LID);

            if (!lidClosed || (!resultSlot.isEmpty()
                    && (!ItemStack.isSameItemSameComponents(resultSlot, result)
                    || resultSlot.getCount() >= resultSlot.getMaxStackSize()))) {

                stove.cookingProgress = 0;
            } else {
                stove.cookingProgress += 2;
                if (stove.cookingProgress >= stove.cookingTotalTime) {
                    stove.cookingProgress = 0;

                    stove.setRecipeUsed(recipeHolder);

                    if (resultSlot.isEmpty()) {
                        stove.setItem(RESULT_SLOT, result.copy());
                    } else {
                        resultSlot.grow(result.getCount());
                    }
                    stove.consumeIngredients(recipe, level, pos);

                    level.playSound(
                            null,
                            pos,
                            CobblemonSounds.CAMPFIRE_POT_COOK,
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F
                    );

                    level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                    setChanged(level, pos, state);
                }
            }
        }

        boolean isCooking = stove.cookingProgress > 0;
        if (wasCooking != isCooking) {
            BlockState newState = state.setValue(StoveBlock.COOKING, isCooking);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
            setChanged(level, pos, newState);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
        if (!level.isClientSide) return;

        boolean isCooking = state.getValue(StoveBlock.COOKING);
        boolean containsItems = !stove.getSeasonings().isEmpty() || !stove.getIngredients().isEmpty();

        StoveClientHooks.clientTick(stove, isCooking, containsItems, pos);
    }

    public void handleSafeDrop() {
        if (this.cookingProgress > 0) {
            ItemStack result = this.getItem(RESULT_SLOT);
            ItemStack preview = this.getItem(PREVIEW_SLOT);

            if (!preview.isEmpty() && (result.isEmpty() || ItemStack.isSameItemSameComponents(preview, result))) {
                this.setItem(PREVIEW_SLOT, ItemStack.EMPTY);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Optional<RecipeHolder<CookingPotRecipeBase>> fetchRecipe(Level level, CraftingInput input, RecipeType<? extends CookingPotRecipeBase> type) {
        return level.getRecipeManager().getRecipeFor(type, input, level)
                .map(r -> (RecipeHolder<CookingPotRecipeBase>) r);
    }

    private void consumeIngredients(CookingPotRecipeBase recipe, Level level, BlockPos pos) {
        Map<Item, Integer> remainderItems = new HashMap<>();

        for (int slot : CRAFTING_SLOTS) {
            accumulateAndConsume(slot, remainderItems);
        }

        for (int slot : SEASONING_SLOTS) {
            ItemStack seasoningStack = getItem(slot);
            if (!seasoningStack.isEmpty() && seasoningStack.is(recipe.getSeasoningTag())
                    && recipe.getSeasoningProcessors().stream().anyMatch(p -> p.consumesItem(seasoningStack))) {
                accumulateAndConsume(slot, remainderItems);
            }
        }

        for (Map.Entry<Item, Integer> entry : remainderItems.entrySet()) {
            ItemStack remainderStack = new ItemStack(entry.getKey(), entry.getValue());
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    remainderStack
            );
            level.addFreshEntity(itemEntity);
        }
    }

    private void accumulateAndConsume(int slot, Map<Item, Integer> remainderItems) {
        ItemStack stack = getItem(slot);
        if (!stack.isEmpty()) {
            if (stack.getItem().hasCraftingRemainingItem()) {
                Item remainderItem = stack.getItem().getCraftingRemainingItem();
                if (remainderItem != null) {
                    remainderItems.merge(remainderItem, 1, Integer::sum);
                }
            }
            stack.shrink(1);
            if (stack.isEmpty()) setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void setRemoved() {
        this.cookingProgress = 0;
        super.setRemoved();

        if (level != null && level.isClientSide) {
            StoveClientHooks.stopSounds(worldPosition, this);
        }
    }

    public List<ItemStack> getSeasonings() {
        return items.subList(10, 13).stream().filter(stack -> !stack.isEmpty()).toList();
    }

    public List<ItemStack> getIngredients() {
        return items.subList(1, 10).stream()
                .filter(stack -> !stack.isEmpty())
                .toList();
    }

    public void setPotItem(@Nullable ItemStack stack) {
        this.potComponent = stack != null && !stack.isEmpty() ? new PotComponent(stack) : null;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public ItemStack getPotItem() {
        return potComponent != null ? potComponent.getPotItem() : ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("CookingProgress", cookingProgress);
        ContainerHelper.saveAllItems(tag, items, registries);

        if (potComponent != null) {
            PotComponent.Companion.getCODEC().encodeStart(NbtOps.INSTANCE, potComponent)
                    .result()
                    .ifPresent(potTag -> tag.put("PotComponent", potTag));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        for (int i = 0; i < this.items.size(); i++) {
            this.items.set(i, ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(tag, this.items, registries);
        
        cookingProgress = tag.getInt("CookingProgress");

        if (tag.contains("PotComponent")) {
            PotComponent.Companion.getCODEC().parse(NbtOps.INSTANCE, tag.get("PotComponent"))
                    .result()
                    .ifPresent(component -> this.potComponent = component);
        }
    }
}
