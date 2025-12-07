package com.lunazstudios.cobblefurnies.client.bbmodel;

import org.joml.Vector3f;

import java.util.List;

public class BBAnimSampler {
    public static Vector3f sampleTranslate(List<BBModel.TranslateKey> keys, float tSec) {
        if (keys == null || keys.isEmpty()) return new Vector3f();
        if (tSec <= keys.get(0).time) return new Vector3f(keys.get(0).value);
        if (tSec >= keys.get(keys.size()-1).time) return new Vector3f(keys.get(keys.size()-1).value);

        int hi = 1;
        while (hi < keys.size() && keys.get(hi).time < tSec) hi++;
        int lo = hi - 1;
        var a = keys.get(lo);
        var b = keys.get(hi);
        float dt = b.time - a.time;
        float alpha = dt == 0 ? 0f : (tSec - a.time) / dt;

        return new Vector3f(
                a.value.x + (b.value.x - a.value.x) * alpha,
                a.value.y + (b.value.y - a.value.y) * alpha,
                a.value.z + (b.value.z - a.value.z) * alpha
        );
    }

    public static Vector3f sampleRotate(List<BBModel.RotateKey> keys, float t) {
        if (keys.isEmpty()) return new Vector3f();
        BBModel.RotateKey a = keys.get(0), b = keys.get(keys.size()-1);
        for (int i=0;i<keys.size()-1;i++) {
            if (t >= keys.get(i).time && t <= keys.get(i+1).time) { a=keys.get(i); b=keys.get(i+1); break; }
        }
        if (a==b || "step".equalsIgnoreCase(a.interp)) return new Vector3f(a.value);
        float u = (t - a.time) / Math.max(1e-6f, (b.time - a.time));
        return new Vector3f(
                a.value.x + (b.value.x - a.value.x)*u,
                a.value.y + (b.value.y - a.value.y)*u,
                a.value.z + (b.value.z - a.value.z)*u
        );
    }
}