package com.lunazstudios.cobblefurnies.menu.container;

import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoveContainer extends TransientCraftingContainer {

    public final StoveMenu menu;
    public final NonNullList<ItemStack> items;

    public StoveContainer(StoveMenu menu, int width, int height) {
        super(menu, width, height);
        this.menu = menu;
        this.items = NonNullList.withSize(StoveMenu.ITEMS_SIZE, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        if (index >= this.getContainerSize()) {
            return ItemStack.EMPTY;
        }
        return this.items.get(index);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        if (index >= 0 && index < this.getContainerSize()) {
            this.items.set(index, stack);
            if (this.menu != null) {
                this.menu.slotsChanged(this);
            }
        }
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(this.items, index, count);
        if (!stack.isEmpty()) {
            if (this.menu != null) {
                this.menu.slotsChanged(this);
            }
        }
        return stack;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.items.size(); i++) {
            this.items.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public int getHeight() {
        return StoveMenu.CRAFTING_GRID_WIDTH;
    }

    @Override
    public int getWidth() {
        return StoveMenu.CRAFTING_GRID_WIDTH;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents helper) {
        for (ItemStack stack : this.items) {
            helper.accountSimpleStack(stack);
        }
    }
}