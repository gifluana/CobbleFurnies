package com.lunazstudios.cobblefurnies.block.properties;

import com.lunazstudios.cobblefurnies.util.item.PotColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.*;

public class CFBlockStateProperties {
    public static final IntegerProperty LEVEL_1_3 = IntegerProperty.create("level", 1, 3);
    public static final IntegerProperty CHANNEL_1_3 = IntegerProperty.create("channel", 1, 3);
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final EnumProperty<CabinetryShape> CABINETRY_SHAPE = EnumProperty.create("shape", CabinetryShape.class);
    public static final EnumProperty<SofaType> SOFA_TYPE = EnumProperty.create("type", SofaType.class);
    public static final EnumProperty<CurtainVerticalPart> CURTAIN_VERTICAL = EnumProperty.create("vertical_part", CurtainVerticalPart.class);
    public static final EnumProperty<CurtainHorizontalPart> CURTAIN_HORIZONTAL = EnumProperty.create("horizontal_part", CurtainHorizontalPart.class);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final EnumProperty<WallSupportShape> WALL_SUPPORT_SHAPE = EnumProperty.create("shape", WallSupportShape.class);
    public static final BooleanProperty HAS_POT = BooleanProperty.create("has_pot");
    public static final EnumProperty<PotColor> POT_COLOR = EnumProperty.create("pot_color", PotColor.class);
    public static final EnumProperty<MountType> MOUNT_TYPE = EnumProperty.create("mount_type", MountType.class);
    public static final BooleanProperty HAS_CONCRETE = BooleanProperty.create("has_concrete");
    public static final EnumProperty<DyeColor> CONCRETE_COLOR = EnumProperty.create("concrete_color", DyeColor.class);
    public static final BooleanProperty HAS_GLASS = BooleanProperty.create("has_glass");

}