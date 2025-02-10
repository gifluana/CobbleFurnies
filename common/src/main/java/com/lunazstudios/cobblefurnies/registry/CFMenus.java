package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class CFMenus {

    public static final Supplier<MenuType<FurniCrafterMenu>> FURNI_CRAFTER_MENU =
            CFRegistry.registerMenuType("furni_crafter", () ->
                    new MenuType<>(FurniCrafterMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );

    public static void register() {}
}
