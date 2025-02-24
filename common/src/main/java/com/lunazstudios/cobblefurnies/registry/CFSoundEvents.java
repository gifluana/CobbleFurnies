package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class CFSoundEvents {
    public static final Supplier<SoundEvent> FRIDGE_OPEN = register("block.fridge.open");
    public static final Supplier<SoundEvent> FRIDGE_CLOSE = register("block.fridge.close");

    public static Supplier<SoundEvent> register(String name) {
        return CFRegistry.registerSoundEvent(name, () -> SoundEvent.createVariableRangeEvent(CobbleFurnies.id(name)));
    }

    public static void init() {}
}
