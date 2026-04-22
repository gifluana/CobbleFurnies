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

    
    
    public static final Codec<Ingredient> CUSTOM_INGREDIENT_CODEC = Codec.either(Ingredient.CODEC, Codec.STRING)
            .xmap(
                    (Either<Ingredient, String> either) ->
                            
                            either.map(
                                    Function.identity(),
                                    (String s) -> {
                                        ResourceLocation res = ResourceLocation.parse(s);
                                        Item item = BuiltInRegistries.ITEM.get(res);
                                        return Ingredient.of(new ItemStack(item));
                                    }
                            ),
                    
                    (Ingredient ingredient) -> Either.left(ingredient)
            );

    
    
    
    public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CUSTOM_INGREDIENT_CODEC.fieldOf("item").forGetter(CountedIngredient::ingredient),
                    Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)
            ).apply(instance, CountedIngredient::new)
    );
}
