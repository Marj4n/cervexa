package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public interface IEffect {
    void apply(ISource iSource);

    void apply(int[] iArr, int i, int i2, int i3);

    void apply(int[] iArr, TextureOffscreen textureOffscreen);

    boolean enabled();

    void release();

    IEffect resize(int i, int i2);

    IEffect setEnable(boolean z);
}
