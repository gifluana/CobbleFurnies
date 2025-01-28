package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class CFEntityTypeTags {
    public static final TagKey<EntityType<?>> CAN_SIT_IN_SEATS = entityTypeTag("can_sit_in_seats");

    private static TagKey<EntityType<?>> entityTypeTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, CobbleFurnies.id(name));
    }

    public static void init() {}
}
