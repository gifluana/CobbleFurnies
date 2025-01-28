package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.*;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class CFBlockStateProperties {
    public static final IntegerProperty LEVEL_1_3 = IntegerProperty.create("level", 1, 3);
    public static final BooleanProperty LEG_1 = BooleanProperty.create("leg_1");
    public static final BooleanProperty LEG_2 = BooleanProperty.create("leg_2");
    public static final BooleanProperty LEG_3 = BooleanProperty.create("leg_3");
    public static final BooleanProperty LEG_4 = BooleanProperty.create("leg_4");
    public static final BooleanProperty UPDATE = BooleanProperty.create("update");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
}