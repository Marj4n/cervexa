package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectBlackWhite extends MediaEffect {
    public MediaEffectBlackWhite(EffectContext effectContext) {
        this(effectContext, 0.0f, 1.0f);
    }

    public MediaEffectBlackWhite(EffectContext effectContext, float f, float f2) {
        super(effectContext, "android.media.effect.effects.BlackWhiteEffect");
        setParameter(f, f2);
    }

    public MediaEffectBlackWhite setParameter(float f, float f2) {
        setParameter("black", Float.valueOf(f));
        setParameter("white", Float.valueOf(f2));
        return this;
    }
}
