package com.lunazstudios.cobblefurnies.registry.neoforge;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import com.lunazstudios.cobblefurnies.registry.CFRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class CFTabsImpl {
    public static final Supplier<CreativeModeTab> CF_TAB = CFRegistryImpl.MOD_TABS.register(CobbleFurnies.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(CFBlocks.LIGHT_BLUE_SOFA.get().asItem()))
            .title(Component.translatable("item_group." + CobbleFurnies.MOD_ID + ".tab"))
            .displayItems(((parameters, output) -> {
                output.acceptAll(CFRegistry.getAllModItems());
            })).build());

    public static void register(IEventBus eventBus) {
        CFRegistryImpl.MOD_TABS.register(eventBus);
    }
}
