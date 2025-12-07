package com.lunazstudios.cobblefurnies.block.entity;

import com.lunazstudios.cobblefurnies.registry.CFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StatueBlockEntity extends BlockEntity {
    public StatueBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CFBlockEntityTypes.STATUE.get(), blockPos, blockState);
    }
}
