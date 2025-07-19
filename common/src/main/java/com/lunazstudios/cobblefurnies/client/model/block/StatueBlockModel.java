package com.lunazstudios.cobblefurnies.client.model.block;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.block.entity.StatueBlockEntity;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.model.GeoModel;

public class StatueBlockModel extends GeoModel<StatueBlockEntity> {

    @Override
    public ResourceLocation getModelResource(StatueBlockEntity entity) {
        return CobbleFurnies.id(getModelPath(entity));
    }

    @Override
    public ResourceLocation getTextureResource(StatueBlockEntity entity) {
        return CobbleFurnies.id(getTexturePath(entity));
    }

    @Override
    public ResourceLocation getAnimationResource(StatueBlockEntity entity) {
        return null;
    }

    private String getModelPath(StatueBlockEntity entity) {
        BlockState state = entity.getBlockState();
        Block block = state.getBlock();

        if (block == CFBlocks.STATUE_SQUIRTLE.get()) {
            return "geo/statue_squirtle.geo.json";
        } else if (block == CFBlocks.STATUE_BULBASAUR.get()) {
            return "geo/statue_bulbasaur.geo.json";
        } else if (block == CFBlocks.STATUE_CHARMANDER.get()) {
            return "geo/statue_charmander.geo.json";
        } else if (block == CFBlocks.STATUE_PIKACHU.get()) {
            return "geo/statue_pikachu.geo.json";
        } else {
            return "geo/statue_ancient.geo.json";
        }
    }

    private String getTexturePath(StatueBlockEntity entity) {
        BlockState state = entity.getBlockState();
        Block block = state.getBlock();

        if (block == CFBlocks.STATUE_SQUIRTLE.get()) {
            return "textures/block/statue/statue_squirtle.png";
        } else if (block == CFBlocks.STATUE_BULBASAUR.get()) {
            return "textures/block/statue/statue_bulbasaur.png";
        } else if (block == CFBlocks.STATUE_CHARMANDER.get()) {
            return "textures/block/statue/statue_charmander.png";
        } else if (block == CFBlocks.STATUE_PIKACHU.get()) {
            return "textures/block/statue/statue_pikachu.png";
        } else {
            return "textures/block/statue/statue_ancient.png";
        }
    }
}
