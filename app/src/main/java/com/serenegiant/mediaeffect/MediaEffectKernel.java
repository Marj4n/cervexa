package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectKernel extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectKernel";

    public MediaEffectKernel() {
        super(new MediaEffectKernel3x3Drawer(false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D));
    }

    public MediaEffectKernel(float[] fArr) {
        this();
        setParameter(fArr, 0.0f);
    }

    public MediaEffectKernel(float[] fArr, float f) {
        this();
        setParameter(fArr, f);
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public MediaEffectKernel resize(int i, int i2) {
        super.resize(i, i2);
        setTexSize(i, i2);
        return this;
    }

    public void setKernel(float[] fArr, float f) {
        ((MediaEffectKernel3x3Drawer) this.mDrawer).setKernel(fArr, f);
    }

    public void setColorAdjust(float f) {
        ((MediaEffectKernel3x3Drawer) this.mDrawer).setColorAdjust(f);
    }

    public void setTexSize(int i, int i2) {
        this.mDrawer.setTexSize(i, i2);
    }

    public MediaEffectKernel setParameter(float[] fArr, float f) {
        setKernel(fArr, f);
        return this;
    }
}
