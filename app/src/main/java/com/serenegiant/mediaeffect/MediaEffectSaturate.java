package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectSaturate extends MediaEffect {
    public MediaEffectSaturate(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.SaturateEffect");
        setParameter(f);
    }

    public MediaEffectSaturate setParameter(float f) {
        setEnable(f != 0.0f);
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
