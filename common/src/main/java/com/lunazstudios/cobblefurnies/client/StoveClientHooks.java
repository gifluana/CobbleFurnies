package com.lunazstudios.cobblefurnies.client;

import com.cobblemon.mod.common.client.sound.instances.CancellableSoundInstance;
import com.lunazstudios.cobblefurnies.block.entity.StoveBlockEntity;
import com.lunazstudios.cobblefurnies.client.sound.BlockEntitySoundTracker;
import net.minecraft.core.BlockPos;

public class StoveClientHooks {
    public static void clientTick(StoveBlockEntity stove, boolean isCooking, boolean containsItems, BlockPos pos) {
        boolean runningActive = BlockEntitySoundTracker.isActive(pos, stove.getRunningSound().getLocation());
        boolean ambientActive = BlockEntitySoundTracker.isActive(pos, stove.getAmbientSound().getLocation());

        if (containsItems) {
            if (isCooking) {
                BlockEntitySoundTracker.stop(pos, stove.getAmbientSound().getLocation());
                if (!runningActive) {
                    BlockEntitySoundTracker.play(
                            pos,
                            new CancellableSoundInstance(stove.getRunningSound(), pos, true, 1.0F, 1.0F)
                    );
                }
            } else {
                BlockEntitySoundTracker.stop(pos, stove.getRunningSound().getLocation());
                if (!ambientActive) {
                    BlockEntitySoundTracker.play(
                            pos,
                            new CancellableSoundInstance(stove.getAmbientSound(), pos, true, 1.0F, 1.0F)
                    );
                }
            }
        } else {
            BlockEntitySoundTracker.stop(pos, stove.getRunningSound().getLocation());
            BlockEntitySoundTracker.stop(pos, stove.getAmbientSound().getLocation());
        }
    }

    public static void stopSounds(BlockPos pos, StoveBlockEntity stove) {
        BlockEntitySoundTracker.stop(pos, stove.getRunningSound().getLocation());
        BlockEntitySoundTracker.stop(pos, stove.getAmbientSound().getLocation());
    }
}
