package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectFishEye extends MediaEffect {
    public MediaEffectFishEye(EffectContext effectContext, float f) {
        super(effectContext, "android.media.effect.effects.FisheyeEffect");
        setParameter(f);
    }

    public MediaEffectFishEye setParameter(float f) {
        setParameter("scale", Float.valueOf(f));
        return this;
    }
}
