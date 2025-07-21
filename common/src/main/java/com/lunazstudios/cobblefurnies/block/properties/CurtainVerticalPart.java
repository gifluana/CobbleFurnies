package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

public enum CurtainVerticalPart implements StringRepresentable {
    TOP, BOTTOM;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}