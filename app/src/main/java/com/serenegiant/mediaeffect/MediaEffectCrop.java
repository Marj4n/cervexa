package com.serenegiant.mediaeffect;

import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectCrop extends MediaEffect {
    public MediaEffectCrop(EffectContext effectContext, int i, int i2, int i3, int i4) {
        super(effectContext, "android.media.effect.effects.CropEffect");
        setParameter(i, i2, i3, i4);
    }

    public MediaEffectCrop setParameter(int i, int i2, int i3, int i4) {
        setParameter("xorigin", Integer.valueOf(i));
        setParameter("yorigin", Integer.valueOf(i2));
        setParameter("width", Integer.valueOf(i3));
        setParameter("height", Integer.valueOf(i4));
        return this;
    }
}
