package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectTint extends MediaEffect {
    public MediaEffectTint(EffectContext effectContext, int i) {
        super(effectContext, "android.media.effect.effects.TintEffect");
        setParameter(i);
    }

    public MediaEffectTint setParameter(int i) {
        setParameter("tint", Integer.valueOf(i));
        return this;
    }
}
