package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.entity.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

/**
 * Original Author: StarfishStudios
 * Project: Another Furniture
 */
public class CFEntityTypes {
    public static final Supplier<EntityType<SeatEntity>> SEAT = CFRegistry.registerEntityType("seat", (type, world) -> new SeatEntity(world), MobCategory.MISC, 0.0F, 0.0F);

    public static void init() {}
}
