package com.lunazstudios.cobblefurnies.menu.slot;

import com.cobblemon.mod.common.item.crafting.CookingPotRecipeBase;
import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

public class SeasoningSlot extends Slot {

    private final StoveMenu menu;

    public SeasoningSlot(StoveMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        RecipeHolder<CookingPotRecipeBase> recipe = menu.currentActiveRecipe;
        if (recipe == null) {
            return false;
        }
        return stack.is(recipe.value().getSeasoningTag()) && super.mayPlace(stack);
    }
}