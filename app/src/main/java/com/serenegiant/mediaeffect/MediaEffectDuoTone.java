package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectDuoTone extends MediaEffect {
    public MediaEffectDuoTone(EffectContext effectContext, int i, int i2) {
        super(effectContext, "android.media.effect.effects.DuotoneEffect");
        setParameter(i, i2);
    }

    public MediaEffectDuoTone setParameter(int i, int i2) {
        setParameter("first_color", Integer.valueOf(i));
        setParameter("second_color", Integer.valueOf(i2));
        return this;
    }
}
