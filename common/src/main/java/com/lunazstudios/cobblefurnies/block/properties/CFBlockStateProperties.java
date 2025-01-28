package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.*;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class CFBlockStateProperties {
    public static final EnumProperty<SlabType> HALF = EnumProperty.create("half", SlabType.class);
    public static final DirectionProperty FACING_EXCEPT_DOWN = DirectionProperty.create("facing", (direction) -> direction != Direction.DOWN);
    public static final DirectionProperty VERTICAL_CONNECTION_TYPE_UP_DOWN = DirectionProperty.create("vertical", (direction) -> direction.getAxis().isVertical());
    public static final IntegerProperty LEVEL_1_3 = IntegerProperty.create("level", 1, 3);
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 1, 11);
    public static final BooleanProperty BACK = BooleanProperty.create("back");
    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty LOW = BooleanProperty.create("low");
    public static final BooleanProperty TUCKED = BooleanProperty.create("tucked");
    public static final BooleanProperty LEG_1 = BooleanProperty.create("leg_1");
    public static final BooleanProperty LEG_2 = BooleanProperty.create("leg_2");
    public static final BooleanProperty LEG_3 = BooleanProperty.create("leg_3");
    public static final BooleanProperty LEG_4 = BooleanProperty.create("leg_4");
    public static final BooleanProperty UPDATE = BooleanProperty.create("update");
    public static final BooleanProperty ON = BooleanProperty.create("on");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
}