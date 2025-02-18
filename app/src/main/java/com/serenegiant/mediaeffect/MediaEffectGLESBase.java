package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public class MediaEffectGLESBase implements IEffect {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectGLESBase";
    protected final MediaEffectDrawer mDrawer;
    protected volatile boolean mEnabled;
    protected TextureOffscreen mOutputOffscreen;

    public MediaEffectGLESBase(int i, String str) {
        this(MediaEffectDrawer.newInstance(i, false, ShaderConst.VERTEX_SHADER, str));
    }

    public MediaEffectGLESBase(int i, boolean z, String str) {
        this(MediaEffectDrawer.newInstance(i, z, ShaderConst.VERTEX_SHADER, str));
    }

    public MediaEffectGLESBase(int i, boolean z, String str, String str2) {
        this(MediaEffectDrawer.newInstance(i, z, str, str2));
    }

    public MediaEffectGLESBase(MediaEffectDrawer mediaEffectDrawer) {
        this.mEnabled = true;
        this.mDrawer = mediaEffectDrawer;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void release() {
        this.mDrawer.release();
        TextureOffscreen textureOffscreen = this.mOutputOffscreen;
        if (textureOffscreen != null) {
            textureOffscreen.release();
            this.mOutputOffscreen = null;
        }
    }

    public float[] getMvpMatrix() {
        return this.mDrawer.getMvpMatrix();
    }

    public MediaEffectGLESBase setMvpMatrix(float[] fArr, int i) {
        this.mDrawer.setMvpMatrix(fArr, i);
        return this;
    }

    public void getMvpMatrix(float[] fArr, int i) {
        this.mDrawer.getMvpMatrix(fArr, i);
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public MediaEffectGLESBase resize(int i, int i2) {
        MediaEffectDrawer mediaEffectDrawer = this.mDrawer;
        if (mediaEffectDrawer != null) {
            mediaEffectDrawer.setTexSize(i, i2);
        }
        return this;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public boolean enabled() {
        return this.mEnabled;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public IEffect setEnable(boolean z) {
        this.mEnabled = z;
        return this;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, int i, int i2, int i3) {
        if (this.mEnabled) {
            if (this.mOutputOffscreen == null) {
                this.mOutputOffscreen = new TextureOffscreen(i, i2, false);
            }
            if (i3 != this.mOutputOffscreen.getTexture() || i != this.mOutputOffscreen.getWidth() || i2 != this.mOutputOffscreen.getHeight()) {
                this.mOutputOffscreen.assignTexture(i3, i, i2);
            }
            this.mOutputOffscreen.bind();
            try {
                this.mDrawer.apply(iArr, this.mOutputOffscreen.getTexMatrix(), 0);
            } finally {
                this.mOutputOffscreen.unbind();
            }
        }
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, TextureOffscreen textureOffscreen) {
        if (this.mEnabled) {
            textureOffscreen.bind();
            try {
                this.mDrawer.apply(iArr, textureOffscreen.getTexMatrix(), 0);
            } finally {
                textureOffscreen.unbind();
            }
        }
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(ISource iSource) {
        if (this.mEnabled) {
            TextureOffscreen outputTexture = iSource.getOutputTexture();
            int[] sourceTexId = iSource.getSourceTexId();
            outputTexture.bind();
            try {
                this.mDrawer.apply(sourceTexId, outputTexture.getTexMatrix(), 0);
            } finally {
                outputTexture.unbind();
            }
        }
    }

    protected int getProgram() {
        return this.mDrawer.getProgram();
    }
}
