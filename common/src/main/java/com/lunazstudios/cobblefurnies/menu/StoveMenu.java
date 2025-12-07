package com.lunazstudios.cobblefurnies.menu;

import com.cobblemon.mod.common.CobblemonRecipeTypes;
import com.cobblemon.mod.common.api.cooking.Seasonings;
import com.cobblemon.mod.common.item.crafting.CookingPotRecipe;
import com.cobblemon.mod.common.item.crafting.CookingPotRecipeBase;
import com.lunazstudios.cobblefurnies.menu.container.StoveContainer;
import com.lunazstudios.cobblefurnies.menu.slot.SeasoningSlot;
import com.lunazstudios.cobblefurnies.menu.slot.StoveResultSlot;
import com.lunazstudios.cobblefurnies.registry.CFMenus;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoveMenu extends RecipeBookMenu<CraftingInput, CookingPotRecipeBase> implements ContainerListener {

    private final Player player;
    private final Level level;
    private final Inventory playerInventory;
    public final CraftingContainer container;
    private final ResultContainer resultContainer;
    public final ContainerData containerData;
    private final RecipeType<CookingPotRecipe> recipeType =
            CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING();
    private final RecipeManager.CachedCheck<CraftingInput, CookingPotRecipe> quickCheck =
            RecipeManager.createCheck(CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING());
    public RecipeHolder<CookingPotRecipeBase> currentActiveRecipe = null;
    public ItemStack previewItem = ItemStack.EMPTY;

    public static final int RESULT_SLOT = 0;

    public static final int CRAFTING_GRID_WIDTH = 3;
    public static final int ITEMS_SIZE = 20;

    public static final int CRAFTING_GRID_START = 1;
    public static final int CRAFTING_GRID_END = 9;
    public static final int SEASONING_START = 10;
    public static final int SEASONING_END = 12;

    public static final int PLAYER_INV_START = 13;
    public static final int PLAYER_INV_END = 39;
    public static final int PLAYER_HOTBAR_START = 40;
    public static final int PLAYER_HOTBAR_END = 48;

    public StoveMenu(int containerId, Inventory playerInventory) {
        super(CFMenus.STOVE_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.container = new StoveContainer(this, CRAFTING_GRID_WIDTH, CRAFTING_GRID_WIDTH);
        this.resultContainer = new ResultContainer();
        this.containerData = new SimpleContainerData(4);
        this.addDataSlots(containerData);
        this.player = playerInventory.player;
        this.level = playerInventory.player.level();
        initializeSlots(playerInventory);
        this.addSlotListener(this);
    }

    public StoveMenu(int containerId, Inventory playerInventory, CraftingContainer container, ContainerData containerData) {
        super(CFMenus.STOVE_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.container = container;
        this.containerData = containerData;
        this.addDataSlots(containerData);
        this.resultContainer = new ResultContainer();
        this.player = playerInventory.player;
        this.level = playerInventory.player.level();
        container.startOpen(playerInventory.player);
        initializeSlots(playerInventory);
        this.addSlotListener(this);
    }

    private void initializeSlots(Inventory playerInventory) {
        int resultSlotX = 128;
        int resultSlotY = 55;

        addSlot(new StoveResultSlot(this.container, RESULT_SLOT, resultSlotX, resultSlotY));

        for (int i = 0; i < 9; i++) {
            int row = i / CRAFTING_GRID_WIDTH;
            int col = i % CRAFTING_GRID_WIDTH;
            addSlot(new Slot(this.container, CRAFTING_GRID_START + i, 33 + col * 18, 18 + row * 18));
        }

        for (int i = 0; i < 3; i++) {
            addSlot(new SeasoningSlot(this, this.container, SEASONING_START + i, 110 + i * 18, 18));
        }

        for (int i = 0; i < 27; i++) {
            int row = i / 9;
            int col = i % 9;
            addSlot(new Slot(playerInventory, i + 9, 8 + col * 18, 84 + row * 18));
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
    }

    @Override
    public void handlePlacement(boolean placeAll, RecipeHolder<?> recipe, ServerPlayer player) {
        Object recipeValue = recipe.value();
        if (recipeValue instanceof CookingPotRecipeBase) {
            @SuppressWarnings("unchecked")
            RecipeHolder<CookingPotRecipeBase> castedRecipe =
                    (RecipeHolder<CookingPotRecipeBase>) recipe;

            List<ItemStack> preservedSeasonings = new ArrayList<>();
            for (int i = SEASONING_START; i <= SEASONING_END; i++) {
                preservedSeasonings.add(container.getItem(i).copy());
            }

            this.beginPlacingRecipe();
            try {
                ServerPlaceRecipe<CraftingInput, CookingPotRecipeBase> serverPlaceRecipe =
                        new ServerPlaceRecipe<>(this);
                serverPlaceRecipe.recipeClicked(player, castedRecipe, placeAll);
            } finally {
                this.finishPlacingRecipe(castedRecipe);
            }

            for (int i = 0; i < preservedSeasonings.size(); i++) {
                container.setItem(SEASONING_START + i, preservedSeasonings.get(i));
            }
        } else {
            throw new IllegalArgumentException(
                    "Unsupported recipe type: " + recipeValue.getClass().getName());
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        this.container.fillStackedContents(itemHelper);
    }

    @Override
    public void clearCraftingContent() {
        container.clearContent();
    }

    @Override
    public boolean recipeMatches(RecipeHolder<CookingPotRecipeBase> recipe) {
        List<ItemStack> craftingItems =
                container.getItems().subList(CRAFTING_GRID_START, CRAFTING_GRID_END + 1);
        CraftingInput craftInput = CraftingInput.of(3, 3, craftingItems);
        CookingPotRecipeBase recipeValue = recipe.value();
        return recipeValue.matches(craftInput, level);
    }

    private void recalculateRecipe() {
        List<ItemStack> craftingItems =
                container.getItems().subList(CRAFTING_GRID_START, CRAFTING_GRID_END + 1);
        CraftingInput craftInput = CraftingInput.of(3, 3, craftingItems);

        Optional<RecipeHolder<CookingPotRecipeBase>> recipe =
                fetchRecipe(CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_COOKING())
                        .or(() -> fetchRecipe(CobblemonRecipeTypes.INSTANCE.getCOOKING_POT_SHAPELESS()));

        currentActiveRecipe = recipe.orElse(null);
        if (recipe.isPresent()) {
            CookingPotRecipeBase recipeValue = recipe.get().value();
            previewItem = recipeValue.assemble(craftInput, level.registryAccess());

            List<ItemStack> seasonings = new ArrayList<>();
            for (int i = SEASONING_START; i <= SEASONING_END; i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty() && stack.is(recipeValue.getSeasoningTag())) {
                    seasonings.add(stack);
                }
            }
            recipeValue.applySeasoning(previewItem, seasonings);
        } else {
            previewItem = ItemStack.EMPTY;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CookingPotRecipeBase> Optional<RecipeHolder<CookingPotRecipeBase>> fetchRecipe(
            RecipeType<T> recipeType) {
        List<ItemStack> craftingItems =
                container.getItems().subList(CRAFTING_GRID_START, CRAFTING_GRID_END + 1);
        CraftingInput craftInput = CraftingInput.of(3, 3, craftingItems);
        Optional<RecipeHolder<T>> optional =
                level.getRecipeManager().getRecipeFor(recipeType, craftInput, level);
        return optional.map(holder -> (RecipeHolder<CookingPotRecipeBase>) holder);
    }

    @Override
    public int getResultSlotIndex() {
        return RESULT_SLOT;
    }

    @Override
    public int getGridWidth() {
        return CRAFTING_GRID_WIDTH;
    }

    @Override
    public int getGridHeight() {
        return CRAFTING_GRID_WIDTH;
    }

    @Override
    public int getSize() {
        return ITEMS_SIZE;
    }

    public float getBurnProgress() {
        int current = this.containerData.get(0);
        int total = this.containerData.get(1);
        if (total != 0 && current != 0) {
            return Mth.clamp(current / (float) total, 0.0F, 1.0F);
        }
        return 0.0F;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("COBBLEMON_COOKING_POT");
    }

    @Override
    public boolean shouldMoveToInventory(int slotIndex) {
        return !(slotIndex >= SEASONING_START && slotIndex <= SEASONING_END);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            itemStack = slotItemStack.copy();

            if (index == RESULT_SLOT) {
                if (!this.moveItemStackTo(
                        slotItemStack,
                        PLAYER_INV_START,
                        PLAYER_HOTBAR_END + 1,
                        false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItemStack, itemStack);
            } else if (index >= PLAYER_INV_START && index <= PLAYER_HOTBAR_END) {
                if (Seasonings.INSTANCE.isSeasoning(slotItemStack)) {
                    if (!this.moveItemStackTo(
                            slotItemStack,
                            CRAFTING_GRID_START,
                            CRAFTING_GRID_END + 1,
                            false)
                            && !this.moveItemStackTo(
                            slotItemStack,
                            SEASONING_START,
                            SEASONING_END + 1,
                            false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(
                        slotItemStack,
                        CRAFTING_GRID_START,
                        CRAFTING_GRID_END + 1,
                        false)) {
                    return ItemStack.EMPTY;
                }
            } else if ((index >= CRAFTING_GRID_START && index <= CRAFTING_GRID_END)
                    || (index >= SEASONING_START && index <= SEASONING_END)) {
                if (!this.moveItemStackTo(
                        slotItemStack,
                        PLAYER_INV_START,
                        PLAYER_HOTBAR_END + 1,
                        false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotItemStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItemStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItemStack);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
        if ((dataSlotIndex >= CRAFTING_GRID_START && dataSlotIndex <= CRAFTING_GRID_END)
                || (dataSlotIndex >= SEASONING_START && dataSlotIndex <= SEASONING_END)) {
            recalculateRecipe();
        }
        broadcastChanges();
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
    }
}
