package com.lunazstudios.cobblefurnies.client.renderer.block;

import com.lunazstudios.cobblefurnies.block.entity.StatueBlockEntity;
import com.lunazstudios.cobblefurnies.client.model.block.StatueBlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StatueBlockRenderer extends GeoBlockRenderer<StatueBlockEntity> {
    public StatueBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new StatueBlockModel());
    }

    @Override
    public void render(StatueBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
