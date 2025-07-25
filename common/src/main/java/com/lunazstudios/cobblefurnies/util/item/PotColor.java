package com.lunazstudios.cobblefurnies.util.item;

import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum PotColor implements StringRepresentable {
    RED(() -> CobblemonItems.CAMPFIRE_POT_RED),
    YELLOW(() -> CobblemonItems.CAMPFIRE_POT_YELLOW),
    WHITE(() -> CobblemonItems.CAMPFIRE_POT_WHITE),
    PINK(() -> CobblemonItems.CAMPFIRE_POT_PINK),
    GREEN(() -> CobblemonItems.CAMPFIRE_POT_GREEN),
    BLUE(() -> CobblemonItems.CAMPFIRE_POT_BLUE),
    BLACK(() -> CobblemonItems.CAMPFIRE_POT_BLACK);

    private final Supplier<Item> itemSupplier;

    PotColor(Supplier<Item> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public Item getItem() {
        return itemSupplier.get();
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public static @Nullable PotColor fromItem(Item item) {
        for (PotColor color : values()) {
            if (color.getItem() == item) return color;
        }
        return null;
    }
}