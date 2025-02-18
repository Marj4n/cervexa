package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectContrast extends MediaEffect {
    public MediaEffectContrast(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.ContrastEffect");
        setParameter(f);
    }

    public MediaEffectContrast setParameter(float f) {
        setParameter("contrast", Float.valueOf(f));
        return this;
    }
}
