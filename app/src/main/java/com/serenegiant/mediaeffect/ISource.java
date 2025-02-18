package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public interface ISource {
    ISource apply(IEffect iEffect);

    int getHeight();

    int getOutputTexId();

    TextureOffscreen getOutputTexture();

    int[] getSourceTexId();

    float[] getTexMatrix();

    int getWidth();

    void release();

    ISource reset();

    ISource resize(int i, int i2);
}
