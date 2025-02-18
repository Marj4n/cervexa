package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectRedEye extends MediaEffect {
    public MediaEffectRedEye(EffectContext effectContext, float[] fArr) {
        super(effectContext, "android.media.effect.effects.RedEyeEffect");
        setParameter(fArr);
    }

    public MediaEffectRedEye setParameter(float[] fArr) {
        setParameter("centers", fArr);
        return this;
    }
}
