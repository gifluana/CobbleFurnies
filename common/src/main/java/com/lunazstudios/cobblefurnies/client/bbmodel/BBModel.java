package com.lunazstudios.cobblefurnies.client.bbmodel;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BBModel {
    public static class UVRect { public int u1,v1,u2,v2; }
    public static class Cube {
        public Vector3f from = new Vector3f();
        public Vector3f size = new Vector3f();
        public Map<String, UVRect> faceUV = new HashMap<>();
        public Vector3f origin = new Vector3f();
        public float offset = 0f;
        public Vector3f rotateDeg = new Vector3f();
    }
    public static class Group {
        public String name;
        public String parent;
        public Vector3f origin = new Vector3f();
        public List<Cube> cubes = new ArrayList<>();
        public List<Group> children = new ArrayList<>();
        public Vector3f rotateDeg = new Vector3f();
    }

    public static class TranslateKey {
        public float time;
        public String interp;
        public Vector3f value;
    }
    public static class RotateKey {
        public float time;
        public String interp;
        public Vector3f value;
    }
    public static class AnimTrack {
        public String group;
        public List<TranslateKey> translate = new ArrayList<>();
        public List<RotateKey> rotate = new ArrayList<>();
    }
    public static class AnimationClip {
        public String name;
        public float durationSec = 0f;
        public Map<String, AnimTrack> tracks = new HashMap<>();
    }

    public int texW = 64, texH = 64;
    public Map<String, Group> groups = new HashMap<>();
    public List<Group> roots = new ArrayList<>();

    public Map<String, AnimationClip> clips = new HashMap<>();

    public static BBModel load(ResourceLocation rl) {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(rl).get().open();
             InputStreamReader reader = new InputStreamReader(stream)) {

            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            BBModel m = new BBModel();

            if (root.has("model")) {
                JsonObject model = root.getAsJsonObject("model");
                if (model.has("texture")) {
                    JsonArray t = model.getAsJsonArray("texture");
                    m.texW = t.get(0).getAsInt();
                    m.texH = t.get(1).getAsInt();
                }

                JsonObject gr = model.getAsJsonObject("groups");
                for (Map.Entry<String, JsonElement> e : gr.entrySet()) {
                    String name = e.getKey();
                    JsonObject gObj = e.getValue().getAsJsonObject();
                    Group g = new Group();
                    g.name = name;

                    if (gObj.has("origin")) {
                        JsonArray a = gObj.getAsJsonArray("origin");
                        g.origin.set(a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat());
                    }
                    if (gObj.has("parent"))
                        g.parent = gObj.get("parent").getAsString();

                    if (gObj.has("rotate")) {
                        JsonArray r = gObj.getAsJsonArray("rotate");
                        g.rotateDeg.set(r.get(0).getAsFloat(), r.get(1).getAsFloat(), r.get(2).getAsFloat());
                    }

                    if (gObj.has("cubes")) {
                        JsonArray arr = gObj.getAsJsonArray("cubes");
                        for (JsonElement ce : arr) {
                            JsonObject cObj = ce.getAsJsonObject();
                            Cube c = new Cube();
                            if (cObj.has("origin")) {
                                JsonArray a = cObj.getAsJsonArray("origin");
                                c.origin.set(a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat());
                            }
                            if (cObj.has("from")) {
                                JsonArray a = cObj.getAsJsonArray("from");
                                c.from.set(a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat());
                            }
                            if (cObj.has("size")) {
                                JsonArray a = cObj.getAsJsonArray("size");
                                c.size.set(a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat());
                            }
                            if (cObj.has("offset")) {
                                c.offset = cObj.get("offset").getAsFloat();
                            }
                            if (cObj.has("rotate")) {
                                JsonArray r = cObj.getAsJsonArray("rotate");
                                c.rotateDeg.set(r.get(0).getAsFloat(), r.get(1).getAsFloat(), r.get(2).getAsFloat());
                            }
                            if (cObj.has("uvs")) {
                                JsonObject u = cObj.getAsJsonObject("uvs");
                                for (String face : List.of("front","back","right","left","bottom","top")) {
                                    if (u.has(face)) {
                                        JsonArray r = u.getAsJsonArray(face);
                                        UVRect uv = new UVRect();
                                        uv.u1 = r.get(0).getAsInt();
                                        uv.v1 = r.get(1).getAsInt();
                                        uv.u2 = r.get(2).getAsInt();
                                        uv.v2 = r.get(3).getAsInt();
                                        c.faceUV.put(face, uv);
                                    }
                                }
                            }
                            g.cubes.add(c);
                        }
                    }

                    m.groups.put(name, g);
                }

                for (Group g : m.groups.values()) {
                    if (g.parent != null && m.groups.containsKey(g.parent)) {
                        m.groups.get(g.parent).children.add(g);
                    }
                }
                for (Group g : m.groups.values()) {
                    if (g.parent == null) m.roots.add(g);
                }
            }

            if (root.has("animations")) {
                JsonObject anims = root.getAsJsonObject("animations");
                for (Map.Entry<String, JsonElement> e : anims.entrySet()) {
                    String clipName = e.getKey();
                    JsonObject aObj = e.getValue().getAsJsonObject();
                    AnimationClip clip = new AnimationClip();
                    clip.name = clipName;
                    if (aObj.has("duration"))
                        clip.durationSec = aObj.get("duration").getAsFloat();

                    if (aObj.has("groups")) {
                        JsonObject gmap = aObj.getAsJsonObject("groups");
                        for (Map.Entry<String, JsonElement> ge : gmap.entrySet()) {
                            String gname = ge.getKey();
                            JsonObject gDef = ge.getValue().getAsJsonObject();
                            AnimTrack track = new AnimTrack();
                            track.group = gname;

                            if (gDef.has("translate")) {
                                JsonArray tarr = gDef.getAsJsonArray("translate");
                                for (JsonElement te : tarr) {
                                    JsonArray row = te.getAsJsonArray();
                                    TranslateKey k = new TranslateKey();
                                    k.time = row.get(0).getAsFloat();
                                    k.interp = row.get(1).getAsString();
                                    k.value = new Vector3f(
                                            row.get(2).getAsFloat(),
                                            row.get(3).getAsFloat(),
                                            row.get(4).getAsFloat()
                                    );
                                    track.translate.add(k);
                                }
                                track.translate.sort(Comparator.comparingDouble(k -> k.time));
                            }

                            if (gDef.has("rotate")) {
                                JsonArray rarr = gDef.getAsJsonArray("rotate");
                                for (JsonElement re : rarr) {
                                    JsonArray row = re.getAsJsonArray();
                                    RotateKey k = new RotateKey();
                                    k.time   = row.get(0).getAsFloat();
                                    k.interp = row.get(1).getAsString();
                                    k.value  = new Vector3f(
                                            row.get(2).getAsFloat(),
                                            row.get(3).getAsFloat(),
                                            row.get(4).getAsFloat()
                                    );
                                    track.rotate.add(k);
                                }
                                track.rotate.sort(Comparator.comparingDouble(k -> k.time));
                            }
                            clip.tracks.put(gname, track);
                        }
                    }
                    m.clips.put(clipName, clip);
                }
            }

            return m;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load BBModel: " + rl + " -> " + ex, ex);
        }
    }
}
