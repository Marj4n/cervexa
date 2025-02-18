package com.serenegiant.glutils;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

/* loaded from: classes.dex */
public class TextureOffscreen {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_ADJUST_POWER2 = false;
    private static final String TAG = "TextureOffscreen";
    private final int TEX_TARGET;
    private final int TEX_UNIT;
    private final boolean mAdjustPower2;
    private int mDepthBufferObj;
    private int mFBOTextureName;
    private int mFrameBufferObj;
    private final boolean mHasDepthBuffer;
    private int mHeight;
    private final float[] mResultMatrix;
    private int mTexHeight;
    private final float[] mTexMatrix;
    private int mTexWidth;
    private int mWidth;

    public TextureOffscreen(int i, int i2) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, -1, i, i2, false, false);
    }

    public TextureOffscreen(int i, int i2, int i3) {
        this(ShaderConst.GL_TEXTURE_2D, i, -1, i2, i3, false, false);
    }

    public TextureOffscreen(int i, int i2, boolean z) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, -1, i, i2, z, false);
    }

    public TextureOffscreen(int i, int i2, int i3, boolean z) {
        this(ShaderConst.GL_TEXTURE_2D, i, -1, i2, i3, z, false);
    }

    public TextureOffscreen(int i, int i2, boolean z, boolean z2) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, -1, i, i2, z, z2);
    }

    public TextureOffscreen(int i, int i2, int i3, boolean z, boolean z2) {
        this(ShaderConst.GL_TEXTURE_2D, i, -1, i2, i3, z, z2);
    }

    public TextureOffscreen(int i, int i2, int i3, int i4) {
        this(ShaderConst.GL_TEXTURE_2D, i, i2, i3, i4, false, false);
    }

    public TextureOffscreen(int i, int i2, int i3, int i4, boolean z) {
        this(ShaderConst.GL_TEXTURE_2D, i, i2, i3, i4, z, false);
    }

    public TextureOffscreen(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        this.mFBOTextureName = -1;
        this.mDepthBufferObj = -1;
        this.mFrameBufferObj = -1;
        this.mTexMatrix = new float[16];
        this.mResultMatrix = new float[16];
        this.TEX_TARGET = i;
        this.TEX_UNIT = i2;
        this.mWidth = i4;
        this.mHeight = i5;
        this.mHasDepthBuffer = z;
        this.mAdjustPower2 = z2;
        createFrameBuffer(i4, i5);
        assignTexture(i3 < 0 ? genTexture(i, i2, this.mTexWidth, this.mTexHeight) : i3, i4, i5);
    }

    public void release() {
        releaseFrameBuffer();
    }

    public void bind() {
        GLES20.glActiveTexture(this.TEX_UNIT);
        GLES20.glBindTexture(this.TEX_TARGET, this.mFBOTextureName);
        GLES20.glBindFramebuffer(36160, this.mFrameBufferObj);
        GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
    }

    public void unbind() {
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glActiveTexture(this.TEX_UNIT);
        GLES20.glBindTexture(this.TEX_TARGET, 0);
    }

    public float[] getTexMatrix() {
        System.arraycopy(this.mTexMatrix, 0, this.mResultMatrix, 0, 16);
        return this.mResultMatrix;
    }

    public float[] getRawTexMatrix() {
        return this.mTexMatrix;
    }

    public void getTexMatrix(float[] fArr, int i) {
        float[] fArr2 = this.mTexMatrix;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    public int getTexture() {
        return this.mFBOTextureName;
    }

    public void assignTexture(int i, int i2, int i3) {
        if (i2 > this.mTexWidth || i3 > this.mTexHeight) {
            this.mWidth = i2;
            this.mHeight = i3;
            releaseFrameBuffer();
            createFrameBuffer(i2, i3);
        }
        this.mFBOTextureName = i;
        GLES20.glActiveTexture(this.TEX_UNIT);
        GLES20.glBindFramebuffer(36160, this.mFrameBufferObj);
        GLHelper.checkGlError("glBindFramebuffer " + this.mFrameBufferObj);
        GLES20.glFramebufferTexture2D(36160, 36064, this.TEX_TARGET, this.mFBOTextureName, 0);
        GLHelper.checkGlError("glFramebufferTexture2D");
        if (this.mHasDepthBuffer) {
            GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.mDepthBufferObj);
            GLHelper.checkGlError("glFramebufferRenderbuffer");
        }
        int glCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus != 36053) {
            throw new RuntimeException("Framebuffer not complete, status=" + glCheckFramebufferStatus);
        }
        GLES20.glBindFramebuffer(36160, 0);
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = i2 / this.mTexWidth;
        fArr[5] = i3 / this.mTexHeight;
    }

    public void loadBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > this.mTexWidth || height > this.mTexHeight) {
            this.mWidth = width;
            this.mHeight = height;
            releaseFrameBuffer();
            createFrameBuffer(width, height);
        }
        GLES20.glActiveTexture(this.TEX_UNIT);
        GLES20.glBindTexture(this.TEX_TARGET, this.mFBOTextureName);
        GLUtils.texImage2D(this.TEX_TARGET, 0, bitmap, 0);
        GLES20.glBindTexture(this.TEX_TARGET, 0);
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = width / this.mTexWidth;
        fArr[5] = height / this.mTexHeight;
    }

    private static int genTexture(int i, int i2, int i3, int i4) {
        int initTex = GLHelper.initTex(i, i2, 9729, 9729, 33071);
        GLES20.glTexImage2D(i, 0, 6408, i3, i4, 0, 6408, 5121, null);
        GLHelper.checkGlError("glTexImage2D");
        return initTex;
    }

    private final void createFrameBuffer(int i, int i2) {
        int[] iArr = new int[1];
        if (this.mAdjustPower2) {
            int i3 = 1;
            while (i3 < i) {
                i3 <<= 1;
            }
            int i4 = 1;
            while (i4 < i2) {
                i4 <<= 1;
            }
            if (this.mTexWidth != i3 || this.mTexHeight != i4) {
                this.mTexWidth = i3;
                this.mTexHeight = i4;
            }
        } else {
            this.mTexWidth = i;
            this.mTexHeight = i2;
        }
        if (this.mHasDepthBuffer) {
            GLES20.glGenRenderbuffers(1, iArr, 0);
            int i5 = iArr[0];
            this.mDepthBufferObj = i5;
            GLES20.glBindRenderbuffer(36161, i5);
            GLES20.glRenderbufferStorage(36161, 33189, this.mTexWidth, this.mTexHeight);
        }
        GLES20.glGenFramebuffers(1, iArr, 0);
        GLHelper.checkGlError("glGenFramebuffers");
        int i6 = iArr[0];
        this.mFrameBufferObj = i6;
        GLES20.glBindFramebuffer(36160, i6);
        GLHelper.checkGlError("glBindFramebuffer " + this.mFrameBufferObj);
        GLES20.glBindFramebuffer(36160, 0);
    }

    private final void releaseFrameBuffer() {
        int[] iArr = new int[1];
        int i = this.mDepthBufferObj;
        if (i >= 0) {
            iArr[0] = i;
            GLES20.glDeleteRenderbuffers(1, iArr, 0);
            this.mDepthBufferObj = -1;
        }
        int i2 = this.mFBOTextureName;
        if (i2 >= 0) {
            iArr[0] = i2;
            GLES20.glDeleteTextures(1, iArr, 0);
            this.mFBOTextureName = -1;
        }
        int i3 = this.mFrameBufferObj;
        if (i3 >= 0) {
            iArr[0] = i3;
            GLES20.glDeleteFramebuffers(1, iArr, 0);
            this.mFrameBufferObj = -1;
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getTexWidth() {
        return this.mTexWidth;
    }

    public int getTexHeight() {
        return this.mTexHeight;
    }

    public int getTexTarget() {
        return this.TEX_TARGET;
    }

    public int getTexUnit() {
        return this.TEX_UNIT;
    }
}
