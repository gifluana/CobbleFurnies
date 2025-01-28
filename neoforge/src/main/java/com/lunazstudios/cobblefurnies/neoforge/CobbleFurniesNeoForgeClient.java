package com.lunazstudios.cobblefurnies.neoforge;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.client.CobbleFurniesClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = CobbleFurnies.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CobbleFurniesNeoForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        CobbleFurniesClient.init();

    }
}
