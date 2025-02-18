package com.serenegiant.mediaeffect;

import android.util.Log;
import com.serenegiant.glutils.GLDrawer2D;
import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public class MediaSource implements ISource {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaSource";
    protected int mHeight;
    protected TextureOffscreen mOutputScreen;
    protected TextureOffscreen mSourceScreen;
    protected int[] mSrcTexIds;
    protected int mWidth;
    protected boolean needSwap;

    public MediaSource() {
        this.mSrcTexIds = new int[1];
        resize(1, 1);
    }

    public MediaSource(int i, int i2) {
        this.mSrcTexIds = new int[1];
        resize(i, i2);
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public ISource reset() {
        this.needSwap = false;
        this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
        return this;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public ISource resize(int i, int i2) {
        if (this.mWidth != i || this.mHeight != i2) {
            TextureOffscreen textureOffscreen = this.mSourceScreen;
            if (textureOffscreen != null) {
                textureOffscreen.release();
                this.mSourceScreen = null;
            }
            TextureOffscreen textureOffscreen2 = this.mOutputScreen;
            if (textureOffscreen2 != null) {
                textureOffscreen2.release();
                this.mOutputScreen = null;
            }
            if (i > 0 && i2 > 0) {
                this.mSourceScreen = new TextureOffscreen(i, i2, false, false);
                this.mOutputScreen = new TextureOffscreen(i, i2, false, false);
                this.mWidth = i;
                this.mHeight = i2;
                this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
            }
        }
        this.needSwap = false;
        return this;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public ISource apply(IEffect iEffect) {
        TextureOffscreen textureOffscreen = this.mSourceScreen;
        if (textureOffscreen != null) {
            if (this.needSwap) {
                TextureOffscreen textureOffscreen2 = this.mOutputScreen;
                this.mSourceScreen = textureOffscreen2;
                this.mOutputScreen = textureOffscreen;
                this.mSrcTexIds[0] = textureOffscreen2.getTexture();
            }
            this.needSwap = !this.needSwap;
            iEffect.apply(this);
        }
        return this;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public int getWidth() {
        return this.mWidth;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public int getHeight() {
        return this.mHeight;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public int[] getSourceTexId() {
        return this.mSrcTexIds;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public int getOutputTexId() {
        return (this.needSwap ? this.mOutputScreen : this.mSourceScreen).getTexture();
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public float[] getTexMatrix() {
        return (this.needSwap ? this.mOutputScreen : this.mSourceScreen).getTexMatrix();
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public TextureOffscreen getOutputTexture() {
        return this.needSwap ? this.mOutputScreen : this.mSourceScreen;
    }

    @Override // com.serenegiant.mediaeffect.ISource
    public void release() {
        this.mSrcTexIds[0] = -1;
        TextureOffscreen textureOffscreen = this.mSourceScreen;
        if (textureOffscreen != null) {
            textureOffscreen.release();
            this.mSourceScreen = null;
        }
        TextureOffscreen textureOffscreen2 = this.mOutputScreen;
        if (textureOffscreen2 != null) {
            textureOffscreen2.release();
            this.mOutputScreen = null;
        }
    }

    public MediaSource bind() {
        this.mSourceScreen.bind();
        return this;
    }

    public MediaSource unbind() {
        this.mSourceScreen.unbind();
        reset();
        return this;
    }

    public MediaSource setSource(GLDrawer2D gLDrawer2D, int i, float[] fArr) {
        this.mSourceScreen.bind();
        try {
            try {
                gLDrawer2D.draw(i, fArr, 0);
            } catch (RuntimeException e) {
                Log.w(TAG, e);
            }
            reset();
            return this;
        } finally {
            this.mSourceScreen.unbind();
        }
    }
}
