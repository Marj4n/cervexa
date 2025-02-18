package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectGrain extends MediaEffect {
    public MediaEffectGrain(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.GrainEffect");
        setParameter(f);
    }

    public MediaEffectGrain setParameter(float f) {
        if (f >= 0.0f && f <= 1.0f) {
            setParameter("strength", Float.valueOf(f));
        }
        return this;
    }
}
