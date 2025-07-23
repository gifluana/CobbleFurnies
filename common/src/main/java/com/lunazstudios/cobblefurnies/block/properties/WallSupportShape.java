package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum WallSupportShape implements StringRepresentable {
    SINGLE,
    LEFT,
    MIDDLE,
    RIGHT;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}