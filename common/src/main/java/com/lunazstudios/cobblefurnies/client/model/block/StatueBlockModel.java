package com.lunazstudios.cobblefurnies.client.model.block;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.block.entity.StatueBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StatueBlockModel extends GeoModel<StatueBlockEntity> {
    @Override
    public ResourceLocation getModelResource(StatueBlockEntity statueBlockEntity) {
        return CobbleFurnies.id("geo/statue_ancient.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StatueBlockEntity statueBlockEntity) {
        return CobbleFurnies.id("textures/block/statue/statue_ancient.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StatueBlockEntity statueBlockEntity) {
        return null;
    }
}
