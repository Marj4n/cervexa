package com.serenegiant.mediaeffect;

import android.graphics.Bitmap;
import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public class MediaImageSource extends MediaSource {
    private boolean isReset;
    private TextureOffscreen mImageOffscreen;

    public MediaImageSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        this.mImageOffscreen = new TextureOffscreen(this.mWidth, this.mHeight, false);
        setSource(bitmap);
    }

    public ISource setSource(Bitmap bitmap) {
        this.mImageOffscreen.loadBitmap(bitmap);
        reset();
        return this;
    }

    @Override // com.serenegiant.mediaeffect.MediaSource, com.serenegiant.mediaeffect.ISource
    public ISource reset() {
        super.reset();
        this.isReset = true;
        this.mSrcTexIds[0] = this.mImageOffscreen.getTexture();
        return this;
    }

    @Override // com.serenegiant.mediaeffect.MediaSource, com.serenegiant.mediaeffect.ISource
    public ISource apply(IEffect iEffect) {
        if (this.mSourceScreen != null) {
            if (this.isReset) {
                this.isReset = false;
                this.needSwap = true;
            } else {
                if (this.needSwap) {
                    TextureOffscreen textureOffscreen = this.mSourceScreen;
                    this.mSourceScreen = this.mOutputScreen;
                    this.mOutputScreen = textureOffscreen;
                    this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
                }
                this.needSwap = !this.needSwap;
            }
            iEffect.apply(this.mSrcTexIds, this.mOutputScreen.getTexWidth(), this.mOutputScreen.getTexHeight(), this.mOutputScreen.getTexture());
        }
        return this;
    }
}
