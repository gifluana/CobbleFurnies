package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class CFRecipes {

    public static final RecipeType<FurniCraftingRecipe> FURNI_CRAFTING_RECIPE_TYPE =
            new RecipeType<>() {
                @Override
                public String toString() {
                    return "cobblefurnies:furni_crafting";
                }
            };

    public static final RecipeSerializer<FurniCraftingRecipe> FURNI_CRAFTING_SERIALIZER =
            new FurniCraftingRecipe.Serializer();

    public static void register() {
        CFRegistry.registerRecipeType("furni_crafting", () -> FURNI_CRAFTING_RECIPE_TYPE);
        CFRegistry.registerRecipeSerializer("furni_crafting", () -> FURNI_CRAFTING_SERIALIZER);
    }
}
