package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectStraighten extends MediaEffect {
    public MediaEffectStraighten(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.StraightenEffect");
        setParameter(f);
    }

    public MediaEffectStraighten setParameter(float f) {
        setParameter("angle", Float.valueOf(f));
        return this;
    }
}
