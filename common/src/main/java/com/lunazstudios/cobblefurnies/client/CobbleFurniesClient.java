package com.lunazstudios.cobblefurnies.client;

import com.lunazstudios.cobblefurnies.client.renderer.entity.SeatRenderer;
import com.lunazstudios.cobblefurnies.registry.CFEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFRegistry;

public class CobbleFurniesClient {

    public static void init() {
        CFRegistry.registerEntityRenderers(CFEntityTypes.SEAT, SeatRenderer::new);
    }
}
