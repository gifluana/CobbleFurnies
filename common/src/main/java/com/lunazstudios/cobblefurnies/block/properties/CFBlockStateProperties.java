package com.lunazstudios.cobblefurnies.block.properties;

import net.minecraft.world.level.block.state.properties.*;

public class CFBlockStateProperties {
    public static final IntegerProperty LEVEL_1_3 = IntegerProperty.create("level", 1, 3);
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final EnumProperty<CabinetryShape> CABINETRY_SHAPE = EnumProperty.create("shape", CabinetryShape.class);
    public static final EnumProperty<SofaType> SOFA_TYPE = EnumProperty.create("type", SofaType.class);
}