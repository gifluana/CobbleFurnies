package com.lunazstudios.cobblefurnies.client;

import com.lunazstudios.cobblefurnies.client.renderer.block.StatueBlockRenderer;
import com.lunazstudios.cobblefurnies.client.renderer.entity.SeatRenderer;
import com.lunazstudios.cobblefurnies.network.CFNetwork;
import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFEntityTypes;
import com.lunazstudios.cobblefurnies.registry.CFRegistry;

public class CobbleFurniesClient {

    public static void init() {
        CFRegistry.registerEntityRenderers(CFEntityTypes.SEAT, SeatRenderer::new);
        CFRegistry.registerBlockEntityRenderer(CFBlockEntityTypes.STATUE, StatueBlockRenderer::new);
    }
}
