package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectNull extends MediaEffect {
    public MediaEffectNull(EffectContext effectContext) {
        super(effectContext, "android.media.effect.effects.AutoFixEffect");
        setParameter("scale", Float.valueOf(0.0f));
    }
}
