package com.lunazstudios.cobblefurnies.recipe;

import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Credits: MrCrayfish
 */
public class FurniCraftingRecipe implements Recipe<SingleRecipeInput> {
    private final NonNullList<CountedIngredient> materials;
    private final ItemStack result;
    private final boolean notification;

    public FurniCraftingRecipe(NonNullList<CountedIngredient> materials, ItemStack result, boolean notification) {
        this.materials = materials;
        this.result = result;
        this.notification = notification;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        ItemStack inputStack = input.getItem(0);
        if (inputStack.isEmpty()) return false;

        for (CountedIngredient ci : materials) {
            if (ci.ingredient().test(inputStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CFRecipes.FURNI_CRAFTING_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CFRecipes.FURNI_CRAFTING_RECIPE_TYPE;
    }

    public NonNullList<CountedIngredient> getMaterials() {
        return this.materials;
    }

    // Returns an array of ItemStacks that include the proper count from each CountedIngredient.
    public ItemStack[] getMaterialStacks() {
        return this.materials.stream()
                .map(ci -> {
                    ItemStack[] stacks = ci.ingredient().getItems();
                    if (stacks.length > 0) {
                        ItemStack stack = stacks[0].copy();
                        stack.setCount(ci.count());
                        return stack;
                    } else {
                        return ItemStack.EMPTY;
                    }
                }).toArray(ItemStack[]::new);
    }

    public ItemStack getResult() {
        return this.result;
    }

    /**
     * Serializer for the recipe.
     */
    public static class Serializer implements RecipeSerializer<FurniCraftingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<FurniCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CountedIngredient.CODEC.listOf().fieldOf("materials")
                                .flatXmap(
                                        list -> DataResult.success(NonNullList.of(CountedIngredient.EMPTY, list.toArray(new CountedIngredient[0]))),
                                        nonNullList -> DataResult.success(nonNullList.stream().toList())
                                ).forGetter(recipe -> recipe.materials),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", false).forGetter(recipe -> recipe.notification)
                ).apply(instance, FurniCraftingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FurniCraftingRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeVarInt(recipe.materials.size());
                    for (CountedIngredient ci : recipe.materials) {
                        buf.writeVarInt(ci.count());
                        // Use the built-in Ingredient stream codec to write the ingredient.
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ci.ingredient());
                    }
                    ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                    buf.writeBoolean(recipe.notification);
                },
                buf -> {
                    int size = buf.readVarInt();
                    NonNullList<CountedIngredient> materials = NonNullList.create();
                    for (int i = 0; i < size; i++) {
                        int count = buf.readVarInt();
                        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                        materials.add(new CountedIngredient(ingredient, count));
                    }
                    ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                    boolean notification = buf.readBoolean();
                    return new FurniCraftingRecipe(materials, result, notification);
                }
        );

        @Override
        public MapCodec<FurniCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FurniCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    /**
     * Recipe Type for the crafting system.
     */
    public static class Type implements RecipeType<FurniCraftingRecipe> {
        public static final Type INSTANCE = new Type();
    }
}
