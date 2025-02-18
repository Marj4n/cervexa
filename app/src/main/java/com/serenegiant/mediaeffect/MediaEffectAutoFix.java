package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectAutoFix extends MediaEffect {
    public MediaEffectAutoFix(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.AutoFixEffect");
        setParameter(f);
    }

    public MediaEffectAutoFix setParameter(float f) {
        setEnable(f != 0.0f);
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
