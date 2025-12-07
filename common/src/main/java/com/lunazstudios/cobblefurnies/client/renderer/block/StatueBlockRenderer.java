package com.lunazstudios.cobblefurnies.client.renderer.block;

import com.lunazstudios.cobblefurnies.block.entity.StatueBlockEntity;
import com.lunazstudios.cobblefurnies.client.bbmodel.BBModel;
import com.lunazstudios.cobblefurnies.client.bbmodel.BBRenderer;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatueBlockRenderer implements BlockEntityRenderer<StatueBlockEntity> {

    private static final DirectionProperty H_FACING = HorizontalDirectionalBlock.FACING;

    private final Map<String, BBModel> modelCache = new HashMap<>();
    private final Map<String, ResourceLocation> textureCache = new HashMap<>();

    public StatueBlockRenderer(BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void render(StatueBlockEntity be, float partialTicks, PoseStack ps,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (be == null || be.getLevel() == null) return;

        BlockState state = be.getBlockState();
        Block block = state.getBlock();

        String key = typeKey(block);

        BBModel model = modelCache.computeIfAbsent(key, k -> {
            ResourceLocation rl = modelPathFor(k);
            return BBModel.load(rl);
        });

        ResourceLocation texture = textureCache.computeIfAbsent(key, this::texturePathFor);

        ps.pushPose();

        float yawDeg = 0f;
        if (state.hasProperty(H_FACING)) {
            yawDeg = state.getValue(H_FACING).toYRot();
        } else if (state.getProperties().stream().anyMatch(p -> Objects.equals(p.getName(), "facing"))) {
            try {
                var prop = (DirectionProperty) state.getProperties().stream()
                        .filter(p -> Objects.equals(p.getName(), "facing"))
                        .findFirst().orElse(null);
                if (prop != null) yawDeg = state.getValue(prop).toYRot();
            } catch (Exception ignored) { }
        }

        // CORRIGIDO: Remove o 180f - para que o modelo fique virado na direção correta
        ps.translate(0.5, 0, 0.5);
        ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yawDeg));
        ps.translate(-0.5, 0, -0.5);

        var opts = BBRenderer.Opts.defaults()
                .clip("idle")
                .loop(false)
                .center(true)
                .autoLight(true)
                .time(0f);

        BBRenderer.render(model, be, texture, ps, buffers, partialTicks, packedOverlay, opts);

        ps.popPose();
    }

    private String typeKey(Block block) {
        if (block == CFBlocks.STATUE_SQUIRTLE.get())   return "squirtle";
        if (block == CFBlocks.STATUE_BULBASAUR.get())  return "bulbasaur";
        if (block == CFBlocks.STATUE_CHARMANDER.get()) return "charmander";
        if (block == CFBlocks.STATUE_PIKACHU.get())    return "pikachu";
        return "ancient";
    }

    private ResourceLocation modelPathFor(String key) {
        return ResourceLocation.fromNamespaceAndPath("cobblefurnies", "models/bb/statue_" + key + ".bbs.json");
    }

    private ResourceLocation texturePathFor(String key) {
        return ResourceLocation.fromNamespaceAndPath("cobblefurnies", "textures/block/statue/statue_" + key + ".png");
    }
}