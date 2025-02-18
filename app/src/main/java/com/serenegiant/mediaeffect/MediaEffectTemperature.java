package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectTemperature extends MediaEffect {
    public MediaEffectTemperature(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.ColorTemperatureEffect");
        setParameter(f);
    }

    public MediaEffectTemperature setParameter(float f) {
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
