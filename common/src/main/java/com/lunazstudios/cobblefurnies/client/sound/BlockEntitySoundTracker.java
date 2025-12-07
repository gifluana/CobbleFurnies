package com.lunazstudios.cobblefurnies.client.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public final class BlockEntitySoundTracker {

    private static final SoundTracker<BlockPos> INSTANCE = new SoundTracker<>() {};

    private BlockEntitySoundTracker() {
    }

    public static boolean isActive(BlockPos pos, ResourceLocation location) {
        return INSTANCE.isActive(pos, location);
    }

    public static void play(BlockPos pos, SoundInstance sound) {
        INSTANCE.play(pos, sound);
    }

    public static void stop(BlockPos pos, SoundInstance sound) {
        INSTANCE.stop(pos, sound);
    }

    public static void stop(BlockPos pos, ResourceLocation location) {
        INSTANCE.stop(pos, location);
    }

    public static void stopAll() {
        INSTANCE.stopAll();
    }

    public static void stopAll(BlockPos pos) {
        INSTANCE.stopAll(pos);
    }

    public static void clear() {
        INSTANCE.clear();
    }

    public static void clear(BlockPos pos) {
        INSTANCE.clear(pos);
    }
}