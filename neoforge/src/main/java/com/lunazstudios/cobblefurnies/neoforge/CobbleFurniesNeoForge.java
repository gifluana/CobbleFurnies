package com.lunazstudios.cobblefurnies.neoforge;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.registry.CFRecipes;
import com.lunazstudios.cobblefurnies.registry.neoforge.CFRegistryImpl;
import com.lunazstudios.cobblefurnies.registry.neoforge.CFTabsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CobbleFurnies.MOD_ID)
public final class CobbleFurniesNeoForge {

    public CobbleFurniesNeoForge(IEventBus bus) {
        CobbleFurnies.init();
        CFRegistryImpl.BLOCKS.register(bus);
        CFRegistryImpl.ITEMS.register(bus);
        CFRegistryImpl.ENTITY_TYPES.register(bus);
        CFRegistryImpl.BLOCK_ENTITY_TYPES.register(bus);
        CFRegistryImpl.MENUS.register(bus);
        CFTabsImpl.register(bus);
    }
}
