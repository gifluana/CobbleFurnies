package com.lunazstudios.cobblefurnies.fabric.compat;

import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class CobbleFurniesREIClient implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FurniCrafterCategory());

        registry.addWorkstations(FurniCrafterCategory.FURNICRAFTER, EntryStacks.of(CFBlocks.FURNI_CRAFTER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FurniCraftingRecipe.class, CFRecipes.FURNI_CRAFTING_RECIPE_TYPE,
                FurniCrafterDisplay::new);
    }
}
