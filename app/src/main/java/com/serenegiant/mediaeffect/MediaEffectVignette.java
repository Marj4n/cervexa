package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectVignette extends MediaEffect {
    public MediaEffectVignette(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.SharpenEffect");
        setParameter(f);
    }

    public MediaEffectVignette setParameter(float f) {
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
