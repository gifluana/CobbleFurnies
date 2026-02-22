package com.lunazstudios.cobblefurnies.block.entity;

import com.cobblemon.mod.common.CobblemonRecipeTypes;
import com.cobblemon.mod.common.item.components.PotComponent;
import com.cobblemon.mod.common.item.crafting.CookingPotRecipeBase;
import com.lunazstudios.cobblefurnies.block.StoveBlock;
import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StoveBlockEntity extends BaseContainerBlockEntity
        implements CraftingContainer, RecipeCraftingHolder, StackedContentsCompatible {

    public static final int RESULT_SLOT = 0;
    public static final int[] CRAFTING_SLOTS = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] SEASONING_SLOTS = {10, 11, 12};
    public static final int PREVIEW_SLOT = 13;
    public static final int TOTAL_SLOTS = 14;

    private final NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private PotComponent potComponent;

    private int cookingProgress = 0;
    private int cookingTotalTime = 200;
    private boolean isLidOpen = true;

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
   // Manually overwrite indices to avoid structural changes to the list object itself
      this.items.clear(); // Ensure we are clearing OUR local list, not the passed one
      for (int i = 0; i < TOTAL_SLOTS; i++) {
            // Safely copy elements one by one
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

        CraftingInput input = CraftingInput.of(3, 3, stove.items.subList(1, 10));
        Optional<RecipeHolder<CookingPotRecipeBase>> recipeOpt =
                fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING());
        if (recipeOpt.isEmpty()) {
            recipeOpt = fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_SHAPELESS());
        }

        if (recipeOpt.isEmpty()) {
            stove.cookingProgress = 0;
            stove.setItem(PREVIEW_SLOT, ItemStack.EMPTY);
            return;
        }

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
            return;
        }

        stove.cookingProgress += 2;
        if (stove.cookingProgress >= stove.cookingTotalTime) {
            stove.cookingProgress = 0;

            stove.setRecipeUsed(recipeHolder);

            if (resultSlot.isEmpty()) {
                stove.setItem(RESULT_SLOT, result.copy());
            } else {
                resultSlot.grow(result.getCount());
            }
            stove.consumeIngredients(recipe);
            setChanged(level, pos, state);
        }
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

    private void consumeIngredients(CookingPotRecipeBase recipe) {
        for (int slot : CRAFTING_SLOTS) {
            consumeItem(slot);
        }

        for (int slot : SEASONING_SLOTS) {
            ItemStack seasoningStack = getItem(slot);
            if (!seasoningStack.isEmpty() && seasoningStack.is(recipe.getSeasoningTag())) {
                if (recipe.getSeasoningProcessors().stream().anyMatch(p -> p.consumesItem(seasoningStack))) {
                    consumeItem(slot);
                }
            }
        }
    }

    private void consumeItem(int slot) {
        ItemStack stack = getItem(slot);
        if (!stack.isEmpty()) {
            if (stack.getItem().hasCraftingRemainingItem()) {
                Item remainderItem = stack.getItem().getCraftingRemainingItem();
                stack.shrink(1);
                if (stack.isEmpty() && remainderItem != null) {
                    setItem(slot, new ItemStack(remainderItem));
                }
            } else {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    public List<ItemStack> getSeasonings() {
        return items.subList(10, 13).stream().filter(stack -> !stack.isEmpty()).toList();
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
        tag.putBoolean("IsLidOpen", isLidOpen);
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
        cookingProgress = tag.getInt("CookingProgress");
        isLidOpen = tag.getBoolean("IsLidOpen");

        // Instead of clearing/adding, just reset the existing slots
        for (int i = 0; i < this.items.size(); i++) {
           this.items.set(i, ItemStack.EMPTY); 
        }
        ContainerHelper.loadAllItems(tag, items, registries);

        if (tag.contains("PotComponent")) {
           PotComponent.Companion.getCODEC().parse(NbtOps.INSTANCE, tag.get("PotComponent"))
                    .result()
                    .ifPresent(component -> this.potComponent = component);
        }
    }
}
