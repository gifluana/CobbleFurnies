package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.util.StringRepresentable;

public enum CabinetryShape implements StringRepresentable {
    OUTER_CORNER_RIGHT("outer_corner_right"),
    OUTER_CORNER_LEFT("outer_corner_left"),
    INNER_CORNER_RIGHT("inner_corner_right"),
    INNER_CORNER_LEFT("inner_corner_left"),
    DEFAULT("default");

    private final String name;

    CabinetryShape(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}