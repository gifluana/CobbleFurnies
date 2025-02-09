package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
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
        Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath("cobblefurnies", "furni_crafting"), FURNI_CRAFTING_RECIPE_TYPE);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath("cobblefurnies", "furni_crafting"), FURNI_CRAFTING_SERIALIZER);
    }
}
