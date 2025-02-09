package com.lunazstudios.cobblefurnies.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.datafixers.util.Either;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.registries.BuiltInRegistries;

public record CountedIngredient(Ingredient ingredient, int count) {
    public static final CountedIngredient EMPTY = new CountedIngredient(Ingredient.EMPTY, 1);

    // Create a custom codec for Ingredient that accepts either an object (via Ingredient.CODEC)
    // or a string. In the string case, we parse it as a ResourceLocation and look up the item.
    public static final Codec<Ingredient> CUSTOM_INGREDIENT_CODEC = Codec.either(Ingredient.CODEC, Codec.STRING)
            .xmap(
                    (Either<Ingredient, String> either) ->
                            // When decoding, if the Either holds an Ingredient use it; otherwise, if it holds a string, parse it.
                            either.map(
                                    Function.identity(),
                                    (String s) -> {
                                        ResourceLocation res = ResourceLocation.parse(s);
                                        Item item = BuiltInRegistries.ITEM.get(res);
                                        return Ingredient.of(new ItemStack(item));
                                    }
                            ),
                    // When encoding, always choose the left branch (i.e. encode the Ingredient using Ingredient.CODEC)
                    (Ingredient ingredient) -> Either.left(ingredient)
            );

    // Use "item" as the field name so that your JSON like
    // { "count": 10, "item": "minecraft:oak_planks" }
    // is parsed correctly.
    public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CUSTOM_INGREDIENT_CODEC.fieldOf("item").forGetter(CountedIngredient::ingredient),
                    Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)
            ).apply(instance, CountedIngredient::new)
    );
}
