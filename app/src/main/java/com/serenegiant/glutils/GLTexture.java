package com.serenegiant.glutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.text.TextUtils;
import java.io.IOException;

/* loaded from: classes.dex */
public class GLTexture implements ITexture {
    int mImageHeight;
    int mImageWidth;
    int mTexHeight;
    final float[] mTexMatrix;
    int mTexWidth;
    int mTextureId;
    final int mTextureTarget;
    final int mTextureUnit;

    public GLTexture(int i, int i2, int i3) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, i, i2, i3);
    }

    public GLTexture(int i, int i2, int i3, int i4, int i5) {
        this.mTexMatrix = new float[16];
        this.mTextureTarget = i;
        this.mTextureUnit = i2;
        int i6 = 32;
        int i7 = 32;
        while (i7 < i3) {
            i7 <<= 1;
        }
        while (i6 < i4) {
            i6 <<= 1;
        }
        if (this.mTexWidth != i7 || this.mTexHeight != i6) {
            this.mTexWidth = i7;
            this.mTexHeight = i6;
        }
        this.mTextureId = GLHelper.initTex(this.mTextureTarget, i5);
        GLES20.glTexImage2D(this.mTextureTarget, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = i3 / this.mTexWidth;
        fArr[5] = i4 / this.mTexHeight;
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    @Override // com.serenegiant.glutils.ITexture
    public void release() {
        int i = this.mTextureId;
        if (i > 0) {
            GLHelper.deleteTex(i);
            this.mTextureId = 0;
        }
    }

    @Override // com.serenegiant.glutils.ITexture
    public void bind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, this.mTextureId);
    }

    @Override // com.serenegiant.glutils.ITexture
    public void unbind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, 0);
    }

    @Override // com.serenegiant.glutils.ITexture
    public int getTexTarget() {
        return this.mTextureTarget;
    }

    @Override // com.serenegiant.glutils.ITexture
    public int getTexture() {
        return this.mTextureId;
    }

    @Override // com.serenegiant.glutils.ITexture
    public float[] getTexMatrix() {
        return this.mTexMatrix;
    }

    @Override // com.serenegiant.glutils.ITexture
    public void getTexMatrix(float[] fArr, int i) {
        float[] fArr2 = this.mTexMatrix;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    @Override // com.serenegiant.glutils.ITexture
    public int getTexWidth() {
        return this.mTexWidth;
    }

    @Override // com.serenegiant.glutils.ITexture
    public int getTexHeight() {
        return this.mTexHeight;
    }

    @Override // com.serenegiant.glutils.ITexture
    public void loadTexture(String str) throws NullPointerException, IOException {
        double ceil;
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("image file path should not be a null");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        if (i3 > this.mTexHeight || i2 > this.mTexWidth) {
            if (i2 > i3) {
                ceil = Math.ceil(i3 / this.mTexHeight);
            } else {
                ceil = Math.ceil(i2 / this.mTexWidth);
            }
            i = (int) ceil;
        }
        options.inSampleSize = i;
        options.inJustDecodeBounds = false;
        loadTexture(BitmapFactory.decodeFile(str, options));
    }

    @Override // com.serenegiant.glutils.ITexture
    public void loadTexture(Bitmap bitmap) throws NullPointerException {
        this.mImageWidth = bitmap.getWidth();
        this.mImageHeight = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(this.mTexWidth, this.mTexHeight, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        bitmap.recycle();
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = this.mImageWidth / this.mTexWidth;
        fArr[5] = this.mImageHeight / this.mTexHeight;
        bind();
        GLUtils.texImage2D(this.mTextureTarget, 0, createBitmap, 0);
        unbind();
        createBitmap.recycle();
    }
}
