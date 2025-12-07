package com.lunazstudios.cobblefurnies.neoforge;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.client.screen.FurniCrafterScreen;
import com.lunazstudios.cobblefurnies.client.screen.StoveScreen;
import com.lunazstudios.cobblefurnies.registry.CFMenus;
import com.lunazstudios.cobblefurnies.registry.neoforge.CFRegistryImpl;
import com.lunazstudios.cobblefurnies.registry.neoforge.CFTabsImpl;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(CobbleFurnies.MOD_ID)
public final class CobbleFurniesNeoForge {

    public CobbleFurniesNeoForge(IEventBus bus) {
        CobbleFurnies.init();
        CFRegistryImpl.BLOCKS.register(bus);
        CFRegistryImpl.ITEMS.register(bus);
        CFRegistryImpl.ENTITY_TYPES.register(bus);
        CFRegistryImpl.BLOCK_ENTITY_TYPES.register(bus);
        CFRegistryImpl.MENUS.register(bus);
        CFRegistryImpl.RECIPE_TYPES.register(bus);
        CFRegistryImpl.RECIPE_SERIALIZERS.register(bus);
        CFRegistryImpl.SOUND_EVENTS.register(bus);
        CFTabsImpl.register(bus);
    }

    @EventBusSubscriber(modid = CobbleFurnies.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(CFMenus.FURNI_CRAFTER_MENU.get(), FurniCrafterScreen::new);
            event.register(CFMenus.STOVE_MENU.get(), StoveScreen::new);
        }
    }
}
