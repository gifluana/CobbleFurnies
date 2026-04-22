package com.lunazstudios.cobblefurnies.client;

import com.cobblemon.mod.common.client.sound.instances.CancellableSoundInstance;
import com.lunazstudios.cobblefurnies.block.entity.StoveBlockEntity;
import com.lunazstudios.cobblefurnies.client.sound.BlockEntitySoundTracker;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import java.lang.reflect.Field;

public class StoveClientHooks {
    public static void clientTick(StoveBlockEntity stove, boolean isCooking, boolean containsItems, BlockPos pos) {
        boolean runningActive = BlockEntitySoundTracker.isActive(pos, stove.getRunningSound().getLocation());
        boolean ambientActive = BlockEntitySoundTracker.isActive(pos, stove.getAmbientSound().getLocation());

        if (containsItems) {
            if (isCooking) {
                cancelAndStop(pos, stove.getAmbientSound().getLocation());
                if (!runningActive) {
                    BlockEntitySoundTracker.play(
                            pos,
                            new CancellableSoundInstance(stove.getRunningSound(), pos, true, 1.0F, 1.0F)
                    );
                }
            } else {
                cancelAndStop(pos, stove.getRunningSound().getLocation());
                if (!ambientActive) {
                    BlockEntitySoundTracker.play(
                            pos,
                            new CancellableSoundInstance(stove.getAmbientSound(), pos, true, 1.0F, 1.0F)
                    );
                }
            }
        } else {
            cancelAndStop(pos, stove.getRunningSound().getLocation());
            cancelAndStop(pos, stove.getAmbientSound().getLocation());
        }
    }

    public static void stopSounds(BlockPos pos, StoveBlockEntity stove) {
        cancelAndStop(pos, stove.getRunningSound().getLocation());
        cancelAndStop(pos, stove.getAmbientSound().getLocation());
    }

    private static final Field DONE_FIELD;
    static {
        Field f = null;
        try {
            f = CancellableSoundInstance.class.getDeclaredField("done");
            f.setAccessible(true);
        } catch (NoSuchFieldException ignored) {}
        DONE_FIELD = f;
    }

    private static void cancelAndStop(BlockPos pos, ResourceLocation location) {
        SoundInstance instance = BlockEntitySoundTracker.get(pos, location);
        if (instance instanceof CancellableSoundInstance cancellable && DONE_FIELD != null) {
            try {
                DONE_FIELD.set(cancellable, true);
            } catch (IllegalAccessException ignored) {}
        }
        BlockEntitySoundTracker.stop(pos, location);
    }
}
