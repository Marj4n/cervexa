package com.serenegiant.mediaeffect;

import android.graphics.Bitmap;
import android.media.effect.EffectContext;

/* loaded from: classes2.dex */
public class MediaEffectBitmapOverlay extends MediaEffect {
    public MediaEffectBitmapOverlay(EffectContext effectContext, Bitmap bitmap) {
        super(effectContext, "android.media.effect.effects.BitmapOverlayEffect");
        setParameter(bitmap);
    }

    public MediaEffectBitmapOverlay setParameter(Bitmap bitmap) {
        setParameter("bitmap", bitmap);
        return this;
    }
}
