package com.lunazstudios.cobblefurnies.client.renderer.block;

import com.lunazstudios.cobblefurnies.block.UpperCabinetBlock;
import com.lunazstudios.cobblefurnies.block.entity.UpperCabinetBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class UpperCabinetBlockRenderer implements BlockEntityRenderer<UpperCabinetBlockEntity> {

    public UpperCabinetBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(UpperCabinetBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var state = blockEntity.getBlockState();
        if (!(state.getValue(UpperCabinetBlock.OPEN) || state.getValue(UpperCabinetBlock.HAS_GLASS))) return;

        Direction direction = state.getValue(UpperCabinetBlock.FACING);
        float rotation = -direction.toYRot() + 180f;

        NonNullList<ItemStack> items = blockEntity.getItems(); // assume um NonNullList<ItemStack>

        poseStack.pushPose();
        poseStack.translate(0.5, 1.18, 0.5); // altura levemente acima do centro
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        for (int j = 0; j < Math.min(4, items.size()); j++) {
            ItemStack stack = items.get(j);
            if (stack.isEmpty()) continue;

            int renderCount = getAmount(stack.getCount());
            for (int i = 0; i < renderCount; i++) {
                float fx = (-0.10375f * (i - 1) * 0.5f) % 0.09f;
                float fy = (-0.04375f * (i - 1) * 0.5f) % 0.09f;
                float fz = (-0.05375f * (i - 1) * 0.5f) % 0.09f;

                poseStack.pushPose();

                // 2x2 layout: slot 0,1 na frente; 2,3 atrás
                float xOffset = 0.15f - 0.4f * (j % 2);
                float zOffset = -0.225f + 0.4f * (j / 2);
                poseStack.translate(xOffset + fx, fy, zOffset + fz);
                poseStack.scale(0.375f, 0.375f, 0.375f);

                Minecraft.getInstance().getItemRenderer().renderStatic(
                        stack,
                        ItemDisplayContext.FIXED,
                        packedLight,
                        packedOverlay,
                        poseStack,
                        bufferSource,
                        blockEntity.getLevel(),
                        0
                );

                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    // Simula empilhamento visual leve
    private int getAmount(int count) {
        if (count > 48) return 5;
        if (count > 32) return 4;
        if (count > 16) return 3;
        if (count > 1) return 2;
        return 1;
    }
}
