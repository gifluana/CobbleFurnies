package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CurtainHorizontalPart implements StringRepresentable {
    LEFT,
    MIDDLE,
    RIGHT,
    SINGLE;

    @Override
    public String getSerializedName() {
        // Thanks to TARIKTR1099
        return name().toLowerCase(Locale.ROOT);
    }
}