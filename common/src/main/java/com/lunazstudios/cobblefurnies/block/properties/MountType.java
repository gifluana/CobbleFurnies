package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

public enum MountType implements StringRepresentable {
    FLOOR, WALL;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}