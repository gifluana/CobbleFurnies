package com.lunazstudios.cobblefurnies.menu;

import com.lunazstudios.cobblefurnies.block.entity.FurniCrafterBlockEntity;
import com.lunazstudios.cobblefurnies.network.CFNetwork;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.lunazstudios.cobblefurnies.registry.CFMenus;
import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FurniCrafterMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final Player player;
    private final Level level;
    private final FurniCrafterBlockEntity blockEntity;
    private final SimpleContainer outputContainer;
    private List<RecipeHolder<FurniCraftingRecipe>> availableRecipes;
    private List<Boolean> canCraftRecipes;

    private static final int OUTPUT_SLOT_INDEX = 0;

    public FurniCrafterMenu(int id, Inventory inventory, Level level, BlockPos pos, SimpleContainer outputContainer) {
        super(CFMenus.FURNI_CRAFTER_MENU.get(), id);
        this.access = ContainerLevelAccess.create(level, pos);
        this.player = inventory.player;
        this.level = level;
        this.blockEntity = level.getBlockEntity(pos) instanceof FurniCrafterBlockEntity entity ? entity : null;
        this.outputContainer = outputContainer;

        fetchAvailableRecipes();
        addPlayerInventorySlots(inventory);
        addOutputSlot();

        updateCraftableRecipes();
    }

    public FurniCrafterMenu(int id, Inventory inventory, Level level, BlockPos pos) {
        this(id, inventory, level, pos, new SimpleContainer(1));
    }

    public FurniCrafterMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, inventory.player.level(), buf.readBlockPos(), new SimpleContainer(1));
    }

    public FurniCrafterMenu(int id, Inventory inventory) {
        this(id, inventory, inventory.player.level(), BlockPos.ZERO, new SimpleContainer(1));
    }


    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        updateCraftableRecipes();
    }

    public void updateCraftableRecipes() {
        this.canCraftRecipes = availableRecipes.stream()
                .map(r -> canCraft(r.value()))
                .collect(Collectors.toList());
        syncCraftableRecipesToClient();
    }

    private void syncCraftableRecipesToClient() {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(
                Unpooled.buffer(),
                serverPlayer.registryAccess()
        );

        buf.writeInt(containerId);
        buf.writeInt(canCraftRecipes.size());

        for (boolean canCraft : canCraftRecipes) {
            buf.writeBoolean(canCraft);
        }

        NetworkManager.sendToPlayer(serverPlayer, CFNetwork.CRAFTABLE_RECIPES_SYNC, buf);
    }

    private void fetchAvailableRecipes() {
        this.availableRecipes = level.getRecipeManager()
                .getAllRecipesFor(CFRecipes.FURNI_CRAFTING_RECIPE_TYPE)
                .stream()
                .sorted(Comparator.comparing(holder -> holder.value().getResultItem(null).getHoverName().getString()))
                .collect(Collectors.toList());
    }

    public List<RecipeHolder<FurniCraftingRecipe>> getAvailableRecipes() {
        return availableRecipes;
    }

    public boolean canCraft(FurniCraftingRecipe recipe) {
        for (CountedIngredient ci : recipe.getMaterials()) {
            int requiredCount = ci.count();
            int totalCount = 0;
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.isEmpty() && ci.ingredient().test(stack)) {
                    totalCount += stack.getCount();
                }
            }
            if (totalCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

    private void addPlayerInventorySlots(Inventory inventory) {
        int yOffset = 38;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + yOffset + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142 + yOffset));
        }
    }


    private void addOutputSlot() {
        this.addSlot(new Slot(outputContainer, OUTPUT_SLOT_INDEX, 149, 86) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }

    public void craftSelectedRecipe(int recipeIndex) {
        if (recipeIndex < 0 || recipeIndex >= availableRecipes.size() || blockEntity == null) {
            return;
        }

        RecipeHolder<FurniCraftingRecipe> recipeHolder = availableRecipes.get(recipeIndex);
        FurniCraftingRecipe recipe = recipeHolder.value();

        if (!canCraft(recipe)) {
            return;
        }

        ItemStack result = recipe.getResultItem(null);
        ItemStack outputSlot = outputContainer.getItem(OUTPUT_SLOT_INDEX);

        if (!outputSlot.isEmpty()) {
            if (!outputSlot.is(result.getItem()) ||
                    outputSlot.getCount() + result.getCount() > outputSlot.getMaxStackSize()) {
                return;
            }
        }

        for (CountedIngredient ci : recipe.getMaterials()) {
            removeItemsFromInventory(ci);
        }

        if (outputSlot.isEmpty()) {
            outputContainer.setItem(OUTPUT_SLOT_INDEX, result.copy());
        } else {
            outputSlot.grow(result.getCount());
        }
    }

    private void removeItemsFromInventory(CountedIngredient ci) {
        int requiredCount = ci.count();
        int remaining = requiredCount;
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (!stack.isEmpty() && ci.ingredient().test(stack)) {
                int take = Math.min(stack.getCount(), remaining);
                stack.shrink(take);
                remaining -= take;
                if (remaining <= 0) {
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> level.getBlockEntity(pos) instanceof FurniCrafterBlockEntity, true);
    }

    public void setCraftableRecipes(boolean[] canCraft) {
        for (int i = 0; i < canCraft.length && i < this.canCraftRecipes.size(); i++) {
            this.canCraftRecipes.set(i, canCraft[i]);
        }
    }

    public boolean hasMaterials(CountedIngredient material, Map<Integer, Integer> counted) {
        int required = material.count();
        // Loop over every item in the player's inventory.
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && material.ingredient().test(stack)) {
                // Use the item's hashCode as a key.
                int key = stack.getItem().hashCode();
                int alreadyUsed = counted.getOrDefault(key, 0);
                // Determine how many of this item are still available.
                int available = stack.getCount() - alreadyUsed;
                if (available > 0) {
                    // Use up as many items as needed from this stack.
                    int used = Math.min(required, available);
                    required -= used;
                    counted.put(key, alreadyUsed + used);
                    if (required <= 0) {
                        return true; // Requirement satisfied.
                    }
                }
            }
        }
        return false; // Not enough items found.
    }
}
