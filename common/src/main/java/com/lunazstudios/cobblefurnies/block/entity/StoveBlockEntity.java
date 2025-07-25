package com.lunazstudios.cobblefurnies.block.entity;

import com.cobblemon.mod.common.CobblemonRecipeTypes;
import com.cobblemon.mod.common.block.campfirepot.CookingPotMenu;
import com.cobblemon.mod.common.item.components.PotComponent;
import com.cobblemon.mod.common.item.crafting.CookingPotRecipeBase;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StoveBlockEntity extends BaseContainerBlockEntity implements CraftingContainer {

    public static final int RESULT_SLOT = 0;
    public static final int[] CRAFTING_SLOTS = {1,2,3,4,5,6,7,8,9};
    public static final int[] SEASONING_SLOTS = {10,11,12};
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
                case 2 -> isLidOpen ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> cookingProgress = value;
                case 1 -> cookingTotalTime = value;
                case 2 -> isLidOpen = value == 1;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(CFBlockEntityTypes.STOVE.get(), pos, state);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cobblefurnies.stove");
    }

    @Override
    public Component getDisplayName() {
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
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        items.clear();
        items.addAll(newItems);
    }

    @Override
    public int getContainerSize() {
        return TOTAL_SLOTS;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CookingPotMenu(containerId, inventory, this, dataAccess);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
        if (level.isClientSide) return;

        CraftingInput input = CraftingInput.of(3, 3, stove.items.subList(1, 10));
        Optional<RecipeHolder<CookingPotRecipeBase>> recipeOpt = fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING());
        if (recipeOpt.isEmpty()) {
            recipeOpt = fetchRecipe(level, input, CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_SHAPELESS());
        }

        if (recipeOpt.isEmpty()) {
            stove.cookingProgress = 0;
            stove.setItem(PREVIEW_SLOT, ItemStack.EMPTY);
            return;
        }

        CookingPotRecipeBase recipe = recipeOpt.get().value();
        ItemStack result = recipe.assemble(input, level.registryAccess());
        recipe.applySeasoning(result, stove.getSeasonings());

        stove.setItem(PREVIEW_SLOT, result);

        ItemStack resultSlot = stove.getItem(RESULT_SLOT);
        if (stove.isLidOpen || (!resultSlot.isEmpty() && (!ItemStack.isSameItemSameComponents(resultSlot, result) || resultSlot.getCount() >= resultSlot.getMaxStackSize()))) {
            stove.cookingProgress = 0;
            return;
        }

        stove.cookingProgress += 2;
        if (stove.cookingProgress >= stove.cookingTotalTime) {
            stove.cookingProgress = 0;
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

    private static Optional<RecipeHolder<CookingPotRecipeBase>> fetchRecipe(Level level, CraftingInput input, RecipeType<? extends CookingPotRecipeBase> type) {
        return level.getRecipeManager().getRecipeFor(type, input, level)
                .map(r -> (RecipeHolder<CookingPotRecipeBase>) r);
    }

    private void consumeIngredients(CookingPotRecipeBase recipe) {
        for (int slot : CRAFTING_SLOTS) {
            consumeItem(slot);
        }
        for (int slot : SEASONING_SLOTS) {
            if (recipe.getSeasoningProcessors().stream().anyMatch(p -> p.consumesItem(getItem(slot)))) {
                consumeItem(slot);
            }
        }
    }

    private void consumeItem(int slot) {
        ItemStack stack = getItem(slot);
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.LAVA_BUCKET || stack.getItem() == Items.WATER_BUCKET || stack.getItem() == Items.MILK_BUCKET) {
                setItem(slot, new ItemStack(Items.BUCKET));
            } else if (stack.getItem() == Items.HONEY_BOTTLE) {
                stack.shrink(1);
                if (stack.isEmpty()) setItem(slot, ItemStack.EMPTY);
            } else {
                stack.shrink(1);
                if (stack.isEmpty()) setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    public List<ItemStack> getSeasonings() {
        return items.subList(10, 13).stream().filter(stack -> !stack.isEmpty()).toList();
    }

    public void setPotItem(@Nullable ItemStack stack) {
        this.potComponent = stack != null ? new PotComponent(stack) : null;
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
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
            PotComponent.Companion.getCODEC().encodeStart(NbtOps.INSTANCE, potComponent).result().ifPresent(potTag -> {
                tag.put("PotComponent", potTag);
            });
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        cookingProgress = tag.getInt("CookingProgress");
        isLidOpen = tag.getBoolean("IsLidOpen");
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);

        if (tag.contains("PotComponent")) {
            PotComponent.Companion.getCODEC().parse(NbtOps.INSTANCE, tag.get("PotComponent")).result().ifPresent(component -> {
                this.potComponent = component;
            });
        }
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {

    }
}
