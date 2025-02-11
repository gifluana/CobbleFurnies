package com.lunazstudios.cobblefurnies.fabric;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.network.CFNetwork;
import com.lunazstudios.cobblefurnies.registry.fabric.CFTabsImpl;
import net.fabricmc.api.ModInitializer;

public final class CobbleFurniesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CobbleFurnies.init();
        CFTabsImpl.register();

    }
}
