package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectSharpen extends MediaEffect {
    public MediaEffectSharpen(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.SharpenEffect");
        setParameter(f);
    }

    public MediaEffectSharpen setParameter(float f) {
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
