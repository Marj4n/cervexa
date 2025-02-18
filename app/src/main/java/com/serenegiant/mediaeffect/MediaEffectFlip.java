package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectFlip extends MediaEffect {
    public MediaEffectFlip(EffectContext effectContext, boolean z, boolean z2) {
        super(effectContext, "android.media.effect.effects.FlipEffect");
        setParameter(z, z2);
    }

    public MediaEffectFlip setParameter(boolean z, boolean z2) {
        setParameter("vertical", Boolean.valueOf(z));
        setParameter("horizontal", Boolean.valueOf(z2));
        return this;
    }
}
