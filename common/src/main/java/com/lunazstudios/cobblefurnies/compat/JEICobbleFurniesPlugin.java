package com.lunazstudios.cobblefurnies.compat;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEICobbleFurniesPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FurniCrafterRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FurniCraftingRecipe> furniCraftingRecipes = recipeManager
                .getAllRecipesFor(CFRecipes.FURNI_CRAFTING_RECIPE_TYPE)
                .stream()
                .map(RecipeHolder::value)
                .collect(Collectors.toList());

        registration.addRecipes(FurniCrafterRecipeCategory.RECIPE_TYPE, furniCraftingRecipes);
    }
}
