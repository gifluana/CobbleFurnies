package com.lunazstudios.cobblefurnies.client.bbmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class BBRenderer {
    private static final float PX = 1f / 16f;

    // CORRIGIDO: Conversões de coordenadas do Blockbench para Minecraft
    // Blockbench: X=direita, Y=cima, Z=frente
    // Minecraft: X=leste, Y=cima, Z=sul
    private static float convX(float x){ return -x; }  // Inverte X
    private static float convY(float y){ return y; }   // Y permanece igual (ambos são "up")
    private static float convZ(float z){ return -z; }  // Inverte Z

    // Conversões de translação (mesma lógica)
    private static float convTX(float x){ return -x; }
    private static float convTY(float y){ return y; }
    private static float convTZ(float z){ return -z; }

    public static final class Opts {
        public String clip = "idle";
        public boolean loop = true;
        public boolean centerOnBlock = true;
        public boolean autoLight = true;
        public float fixedTimeSec = -1f;
        public int lightOverride = -1;

        public Opts clip(String c){ this.clip = c; return this; }
        public Opts loop(boolean v){ this.loop = v; return this; }
        public Opts center(boolean v){ this.centerOnBlock = v; return this; }
        public Opts autoLight(boolean v){ this.autoLight = v; return this; }
        public Opts time(float sec){ this.fixedTimeSec = sec; return this; }
        public Opts light(int packed){ this.lightOverride = packed; return this; }

        public static Opts defaults(){ return new Opts(); }
    }

    public static void render(
            BBModel model,
            BlockEntity be,
            ResourceLocation texture,
            PoseStack ps,
            MultiBufferSource buffers,
            float partialTicks,
            int packedOverlay,
            Opts opts
    ) {
        if (be == null || be.getLevel() == null) return;
        final Level level = be.getLevel();
        final BlockPos pos = be.getBlockPos();

        float timeSec = (opts.fixedTimeSec >= 0f)
                ? opts.fixedTimeSec
                : computeTime(level, pos, partialTicks, model, opts.clip, opts.loop);

        int packedLight = (opts.autoLight && opts.lightOverride < 0)
                ? getMaxNeighborLight(level, pos)
                : (opts.lightOverride >= 0 ? opts.lightOverride : LightTexture.pack(0,0));

        ps.pushPose();
        if (opts.centerOnBlock) ps.translate(0.5, 0, 0.5);

        RenderType rt = RenderType.entityCutoutNoCull(texture);
        VertexConsumer vc = buffers.getBuffer(rt);

        var clipName = opts.clip;
        var clip = model.clips.get(clipName);
        Map<String, Vector3f> rot0 = new HashMap<>();
        Map<String, Vector3f> pos0 = new HashMap<>();
        if (clip != null) {
            for (var e : clip.tracks.entrySet()) {
                var t = e.getValue();
                if (t.rotate != null && !t.rotate.isEmpty()) rot0.put(e.getKey(), BBAnimSampler.sampleRotate(t.rotate, 0f));
                if (t.translate != null && !t.translate.isEmpty()) pos0.put(e.getKey(), BBAnimSampler.sampleTranslate(t.translate, 0f));
            }
        }
        for (BBModel.Group root : model.roots) {
            renderGroupRecursive(model, clip, timeSec, rot0, pos0, root, ps, vc, packedLight, packedOverlay, model.texW, model.texH);
        }

        ps.popPose();
    }

    private static float computeTime(Level level, BlockPos pos, float partialTicks, BBModel model, String clipName, boolean loop) {
        long gt = level.getGameTime();
        float baseSec = (gt + partialTicks) / 20f;

        float duration = 0f;
        var clip = model.clips.get(clipName);
        if (clip != null) duration = clip.durationSec;

        long h = pos.asLong();
        float offsetSec = ((h ^ (h >>> 32)) & 0xFF) / 255f * (duration > 0f ? duration : 2f);

        if (loop && duration > 0f) {
            return (baseSec + offsetSec) % duration;
        } else {
            return (duration > 0f) ? Math.min(baseSec + offsetSec, duration) : baseSec + offsetSec;
        }
    }

    private static int getMaxNeighborLight(Level level, BlockPos pos) {
        int block = level.getBrightness(LightLayer.BLOCK, pos);
        int sky = level.getBrightness(LightLayer.SKY, pos);
        for (Direction d : Direction.values()) {
            BlockPos n = pos.relative(d);
            block = Math.max(block, level.getBrightness(LightLayer.BLOCK, n));
            sky = Math.max(sky, level.getBrightness(LightLayer.SKY, n));
        }
        return LightTexture.pack(block, sky);
    }

    private static void renderGroupRecursive(BBModel model,
                                             BBModel.AnimationClip clip, float tSec,
                                             Map<String, Vector3f> rot0, Map<String, Vector3f> pos0,
                                             BBModel.Group g, PoseStack ps, VertexConsumer vc,
                                             int light, int overlay, int texW, int texH) {

        ps.pushPose();

        // Translada para o pivot/origin do grupo
        ps.translate(convTX(g.origin.x) * PX, convTY(g.origin.y) * PX, convTZ(g.origin.z) * PX);

        // Pega valores de animação se existirem
        Vector3f rotAnim = null, transAnim = null;
        if (clip != null) {
            var track = clip.tracks.get(g.name);
            if (track != null) {
                if (track.rotate != null && !track.rotate.isEmpty())
                    rotAnim = BBAnimSampler.sampleRotate(track.rotate, tSec);
                if (track.translate != null && !track.translate.isEmpty())
                    transAnim = BBAnimSampler.sampleTranslate(track.translate, tSec);
            }
        }

        // Aplica rotações
        float rx, ry, rz;
        if (rotAnim != null) {
            // Usa valores da animação
            rx = convX(rotAnim.x);
            ry = convY(rotAnim.y);
            rz = convZ(rotAnim.z);
        } else {
            // Usa rotação estática do grupo
            rx = convX(g.rotateDeg.x);
            ry = convY(g.rotateDeg.y);
            rz = convZ(g.rotateDeg.z);
        }

        // CORRIGIDO: Ordem de rotação para corresponder ao Blockbench
        // Blockbench aplica rotações na ordem ZYX
        ps.mulPose(Axis.ZP.rotationDegrees(rz));
        ps.mulPose(Axis.YP.rotationDegrees(ry));
        ps.mulPose(Axis.XP.rotationDegrees(rx));

        // Aplica translação de animação se existir
        if (transAnim != null) {
            ps.translate(convTX(transAnim.x) * PX,
                    convTY(transAnim.y) * PX,
                    convTZ(transAnim.z) * PX);
        }

        // Volta para a posição local
        ps.translate(-convTX(g.origin.x) * PX, -convTY(g.origin.y) * PX, -convTZ(g.origin.z) * PX);

        // Renderiza cubos deste grupo
        for (BBModel.Cube c : g.cubes) {
            renderCubeLocal(c, ps, vc, light, overlay, texW, texH);
        }

        // Renderiza grupos filhos recursivamente
        for (BBModel.Group child : g.children) {
            renderGroupRecursive(model, clip, tSec, rot0, pos0, child, ps, vc, light, overlay, texW, texH);
        }
        ps.popPose();
    }

    private static void renderCubeLocal(BBModel.Cube c,
                                        PoseStack ps,
                                        VertexConsumer vc,
                                        int light, int overlay,
                                        int texW, int texH) {
        ps.pushPose();

        // Translada para o origin do cubo
        ps.translate(convTX(c.origin.x) * PX, convTY(c.origin.y) * PX, convTZ(c.origin.z) * PX);

        // Calcula as posições dos vértices do cubo em coordenadas locais
        float x1 = convTX(c.from.x - c.origin.x) * PX;
        float y1 = convTY(c.from.y - c.origin.y) * PX;
        float z1 = convTZ(c.from.z - c.origin.z) * PX;
        float x2 = convTX(c.from.x + c.size.x - c.origin.x) * PX;
        float y2 = convTY(c.from.y + c.size.y - c.origin.y) * PX;
        float z2 = convTZ(c.from.z + c.size.z - c.origin.z) * PX;

        // Aplica offset (inflate) se existir
        if (c.offset != 0f) {
            float off = c.offset * PX;
            x1 -= off; y1 -= off; z1 -= off;
            x2 += off; y2 += off; z2 += off;
        }

        // Renderiza as 6 faces do cubo
        // NOTA: As faces podem precisar de ajuste dependendo da convenção do Blockbench
        drawFace(ps, vc, light, overlay, texW, texH,
                x2, y1, z1,  x1, y1, z1,  x1, y2, z1,  x2, y2, z1, c.faceUV.get("front"));

        drawFace(ps, vc, light, overlay, texW, texH,
                x1, y1, z2,  x2, y1, z2,  x2, y2, z2,  x1, y2, z2, c.faceUV.get("back"));

        drawFace(ps, vc, light, overlay, texW, texH,
                x2, y1, z1,  x2, y1, z2,  x2, y2, z2,  x2, y2, z1, c.faceUV.get("right"));

        drawFace(ps, vc, light, overlay, texW, texH,
                x1, y1, z2,  x1, y1, z1,  x1, y2, z1,  x1, y2, z2, c.faceUV.get("left"));

        drawFace(ps, vc, light, overlay, texW, texH,
                x1, y2, z1,  x2, y2, z1,  x2, y2, z2,  x1, y2, z2, c.faceUV.get("top"));

        drawFace(ps, vc, light, overlay, texW, texH,
                x1, y1, z2,  x2, y1, z2,  x2, y1, z1,  x1, y1, z1, c.faceUV.get("bottom"));

        ps.popPose();
    }

    private static void drawFace(PoseStack ps, VertexConsumer vc, int packedLight, int packedOverlay,
                                 int texW, int texH,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3,
                                 float x4, float y4, float z4,
                                 BBModel.UVRect uv) {
        if (uv == null) return;

        float u1 = uv.u1 / (float) texW;
        float v1 = uv.v1 / (float) texH;
        float u2 = uv.u2 / (float) texW;
        float v2 = uv.v2 / (float) texH;

        PoseStack.Pose pose = ps.last();

        // Calcula a normal da face usando produto vetorial
        float ax = x2 - x1, ay = y2 - y1, az = z2 - z1;
        float bx = x4 - x1, by = y4 - y1, bz = z4 - z1;
        float nx = ay * bz - az * by;
        float ny = az * bx - ax * bz;
        float nz = ax * by - ay * bx;
        float len = (float)Math.sqrt(nx*nx + ny*ny + nz*nz);
        if (len > 1e-6f) { nx /= len; ny /= len; nz /= len; } else { nx = ny = 0f; nz = 1f; }

        // Emite os 4 vértices da face
        emit(vc, pose, x1, y1, z1, u1, v2, packedOverlay, packedLight, nx, ny, nz);
        emit(vc, pose, x2, y2, z2, u2, v2, packedOverlay, packedLight, nx, ny, nz);
        emit(vc, pose, x3, y3, z3, u2, v1, packedOverlay, packedLight, nx, ny, nz);
        emit(vc, pose, x4, y4, z4, u1, v1, packedOverlay, packedLight, nx, ny, nz);
    }

    private static void emit(VertexConsumer vc, PoseStack.Pose pose,
                             float x, float y, float z,
                             float u, float v,
                             int overlay, int light,
                             float nx, float ny, float nz) {
        vc.addVertex(pose, x, y, z)
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, -nx, -ny, -nz)
                .setWhiteAlpha(255);
    }
}