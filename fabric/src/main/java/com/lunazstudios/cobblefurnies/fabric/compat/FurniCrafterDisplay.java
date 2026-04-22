package com.lunazstudios.cobblefurnies.fabric.compat;

import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FurniCrafterDisplay extends BasicDisplay {

    public FurniCrafterDisplay(RecipeHolder<FurniCraftingRecipe> recipeHolder) {
        super(createInputs(recipeHolder.value()), createOutputs(recipeHolder.value()));
    }

    private static List<EntryIngredient> createInputs(FurniCraftingRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        
        for (CountedIngredient ci : recipe.getMaterials()) {
            
            ItemStack[] alternatives = ci.ingredient().getItems();
            
            List<EntryStack<?>> stacks = Arrays.stream(alternatives)
                    .map(stack -> {
                        ItemStack copy = stack.copy();
                        copy.setCount(ci.count());
                        return EntryStacks.of(copy);
                    })
                    .collect(Collectors.toList());
            
            inputs.add(EntryIngredient.of(stacks));
        }
        return inputs;
    }

    private static List<EntryIngredient> createOutputs(FurniCraftingRecipe recipe) {
        return Collections.singletonList(EntryIngredient.of(EntryStacks.of(recipe.getResult())));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FurniCrafterCategory.FURNICRAFTER;
    }
}
