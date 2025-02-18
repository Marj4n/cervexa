package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public class MediaEffectGLESTwoPassBase extends MediaEffectGLESBase {
    protected final MediaEffectKernel3x3Drawer mDrawer2;
    protected TextureOffscreen mOutputOffscreen2;

    public MediaEffectGLESTwoPassBase(int i, boolean z, String str) {
        super(i, z, str);
        this.mDrawer2 = null;
    }

    public MediaEffectGLESTwoPassBase(int i, String str, String str2) {
        super(i, false, str, str2);
        this.mDrawer2 = null;
    }

    public MediaEffectGLESTwoPassBase(int i, boolean z, String str, String str2) {
        super(i, z, str, str2);
        this.mDrawer2 = null;
    }

    public MediaEffectGLESTwoPassBase(int i, boolean z, String str, String str2, String str3, String str4) {
        super(i, z, str, str2);
        if (!str.equals(str3) || !str2.equals(str4)) {
            this.mDrawer2 = new MediaEffectKernel3x3Drawer(z, str3, str4);
        } else {
            this.mDrawer2 = null;
        }
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public void release() {
        MediaEffectKernel3x3Drawer mediaEffectKernel3x3Drawer = this.mDrawer2;
        if (mediaEffectKernel3x3Drawer != null) {
            mediaEffectKernel3x3Drawer.release();
        }
        TextureOffscreen textureOffscreen = this.mOutputOffscreen2;
        if (textureOffscreen != null) {
            textureOffscreen.release();
            this.mOutputOffscreen2 = null;
        }
        super.release();
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public MediaEffectGLESBase resize(int i, int i2) {
        super.resize(i, i2);
        MediaEffectKernel3x3Drawer mediaEffectKernel3x3Drawer = this.mDrawer2;
        if (mediaEffectKernel3x3Drawer != null) {
            mediaEffectKernel3x3Drawer.setTexSize(i, i2);
        }
        return this;
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, int i, int i2, int i3) {
        if (this.mEnabled) {
            if (this.mOutputOffscreen == null) {
                this.mOutputOffscreen = new TextureOffscreen(i, i2, false);
            }
            this.mOutputOffscreen.bind();
            try {
                this.mDrawer.apply(iArr, this.mOutputOffscreen.getTexMatrix(), 0);
                this.mOutputOffscreen.unbind();
                if (this.mOutputOffscreen2 == null) {
                    this.mOutputOffscreen2 = new TextureOffscreen(i, i2, false);
                }
                if (i3 != this.mOutputOffscreen2.getTexture() || i != this.mOutputOffscreen2.getWidth() || i2 != this.mOutputOffscreen2.getHeight()) {
                    this.mOutputOffscreen2.assignTexture(i3, i, i2);
                }
                this.mOutputOffscreen2.bind();
                int[] iArr2 = {this.mOutputOffscreen.getTexture()};
                try {
                    if (this.mDrawer2 != null) {
                        this.mDrawer2.apply(iArr2, this.mOutputOffscreen2.getTexMatrix(), 0);
                    } else {
                        this.mDrawer.apply(iArr2, this.mOutputOffscreen2.getTexMatrix(), 0);
                    }
                } finally {
                    this.mOutputOffscreen2.unbind();
                }
            } catch (Throwable th) {
                this.mOutputOffscreen.unbind();
                throw th;
            }
        }
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, TextureOffscreen textureOffscreen) {
        if (this.mEnabled) {
            if (this.mOutputOffscreen == null) {
                this.mOutputOffscreen = new TextureOffscreen(textureOffscreen.getWidth(), textureOffscreen.getHeight(), false);
            }
            this.mOutputOffscreen.bind();
            try {
                this.mDrawer.apply(iArr, this.mOutputOffscreen.getTexMatrix(), 0);
                this.mOutputOffscreen.unbind();
                textureOffscreen.bind();
                int[] iArr2 = {this.mOutputOffscreen.getTexture()};
                try {
                    if (this.mDrawer2 != null) {
                        this.mDrawer2.apply(iArr2, textureOffscreen.getTexMatrix(), 0);
                    } else {
                        this.mDrawer.apply(iArr2, textureOffscreen.getTexMatrix(), 0);
                    }
                } finally {
                    textureOffscreen.unbind();
                }
            } catch (Throwable th) {
                this.mOutputOffscreen.unbind();
                throw th;
            }
        }
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public void apply(ISource iSource) {
        if (this.mEnabled) {
            TextureOffscreen outputTexture = iSource.getOutputTexture();
            int[] sourceTexId = iSource.getSourceTexId();
            int width = iSource.getWidth();
            int height = iSource.getHeight();
            if (this.mOutputOffscreen == null) {
                this.mOutputOffscreen = new TextureOffscreen(width, height, false);
            }
            this.mOutputOffscreen.bind();
            try {
                this.mDrawer.apply(sourceTexId, this.mOutputOffscreen.getTexMatrix(), 0);
                this.mOutputOffscreen.unbind();
                outputTexture.bind();
                int[] iArr = {this.mOutputOffscreen.getTexture()};
                try {
                    if (this.mDrawer2 != null) {
                        this.mDrawer2.apply(iArr, outputTexture.getTexMatrix(), 0);
                    } else {
                        this.mDrawer.apply(iArr, outputTexture.getTexMatrix(), 0);
                    }
                } finally {
                    outputTexture.unbind();
                }
            } catch (Throwable th) {
                this.mOutputOffscreen.unbind();
                throw th;
            }
        }
    }
}
