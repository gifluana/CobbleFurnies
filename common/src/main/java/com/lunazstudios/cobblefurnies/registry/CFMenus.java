package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class CFMenus {

    public static final MenuType<FurniCrafterMenu> FURNI_CRAFTER_MENU =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "furni_crafter"),
                    new MenuType<>(FurniCrafterMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );

    public static void register() {}
}
