package com.lunazstudios.cobblefurnies;

import com.lunazstudios.cobblefurnies.registry.*;
import net.minecraft.resources.ResourceLocation;

public final class CobbleFurnies {
    public static final String MOD_ID = "cobblefurnies";

    public static void init() {
        CFBlocks.register();
        CFBlockTags.init();
        CFEntityTypes.init();
        CFEntityTypeTags.init();
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
