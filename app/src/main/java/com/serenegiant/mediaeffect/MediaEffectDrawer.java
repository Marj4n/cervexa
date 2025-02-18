package com.serenegiant.mediaeffect;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.serenegiant.glutils.GLHelper;
import com.serenegiant.glutils.ShaderConst;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaEffectDrawer {
    private static final int FLOAT_SZ = 4;
    private static final int VERTEX_NUM = 4;
    private static final int VERTEX_SZ = 8;
    protected int hProgram;
    protected boolean mEnabled;
    protected final float[] mMvpMatrix;
    protected final Object mSync;
    protected final int mTexTarget;
    protected final int muMVPMatrixLoc;
    protected final int[] muTexLoc;
    protected final int muTexMatrixLoc;
    private static final float[] VERTICES = {1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] TEXCOORD = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

    public void setTexSize(int i, int i2) {
    }

    public static MediaEffectDrawer newInstance() {
        return new MediaEffectSingleDrawer(false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
    }

    public static MediaEffectDrawer newInstance(int i) {
        if (i <= 1) {
            return new MediaEffectSingleDrawer(false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
        }
        return new MediaEffectDrawer(i, false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
    }

    public static MediaEffectDrawer newInstance(String str) {
        return new MediaEffectSingleDrawer(false, ShaderConst.VERTEX_SHADER, str);
    }

    public static MediaEffectDrawer newInstance(int i, String str) {
        if (i <= 1) {
            return new MediaEffectSingleDrawer(false, ShaderConst.VERTEX_SHADER, str);
        }
        return new MediaEffectDrawer(i, false, ShaderConst.VERTEX_SHADER, str);
    }

    public static MediaEffectDrawer newInstance(boolean z, String str) {
        return new MediaEffectSingleDrawer(z, ShaderConst.VERTEX_SHADER, str);
    }

    public static MediaEffectDrawer newInstance(int i, boolean z, String str) {
        if (i <= 1) {
            return new MediaEffectSingleDrawer(z, ShaderConst.VERTEX_SHADER, str);
        }
        return new MediaEffectDrawer(i, z, ShaderConst.VERTEX_SHADER, str);
    }

    public static MediaEffectDrawer newInstance(boolean z, String str, String str2) {
        return new MediaEffectSingleDrawer(z, ShaderConst.VERTEX_SHADER, str2);
    }

    public static MediaEffectDrawer newInstance(int i, boolean z, String str, String str2) {
        if (i <= 1) {
            return new MediaEffectSingleDrawer(z, str, str2);
        }
        return new MediaEffectDrawer(i, z, str, str2);
    }

    protected static class MediaEffectSingleDrawer extends MediaEffectDrawer {
        protected MediaEffectSingleDrawer(boolean z, String str, String str2) {
            super(1, z, str, str2);
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        protected void bindTexture(int[] iArr) {
            GLES20.glActiveTexture(ShaderConst.TEX_NUMBERS[0]);
            if (iArr[0] != -1) {
                GLES20.glBindTexture(this.mTexTarget, iArr[0]);
                GLES20.glUniform1i(this.muTexLoc[0], 0);
            }
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        protected void unbindTexture() {
            GLES20.glActiveTexture(ShaderConst.TEX_NUMBERS[0]);
            GLES20.glBindTexture(this.mTexTarget, 0);
        }
    }

    protected MediaEffectDrawer() {
        this(1, false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
    }

    protected MediaEffectDrawer(int i) {
        this(i, false, ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
    }

    protected MediaEffectDrawer(String str) {
        this(1, false, ShaderConst.VERTEX_SHADER, str);
    }

    protected MediaEffectDrawer(int i, String str) {
        this(i, false, ShaderConst.VERTEX_SHADER, str);
    }

    protected MediaEffectDrawer(boolean z, String str) {
        this(1, z, ShaderConst.VERTEX_SHADER, str);
    }

    protected MediaEffectDrawer(int i, boolean z, String str) {
        this(i, z, ShaderConst.VERTEX_SHADER, str);
    }

    protected MediaEffectDrawer(boolean z, String str, String str2) {
        this(1, z, ShaderConst.VERTEX_SHADER, str2);
    }

    protected MediaEffectDrawer(int i, boolean z, String str, String str2) {
        this.mEnabled = true;
        this.mSync = new Object();
        this.mMvpMatrix = new float[16];
        this.mTexTarget = z ? ShaderConst.GL_TEXTURE_EXTERNAL_OES : ShaderConst.GL_TEXTURE_2D;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        asFloatBuffer.put(VERTICES);
        asFloatBuffer.flip();
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        asFloatBuffer2.put(TEXCOORD);
        asFloatBuffer2.flip();
        this.muTexLoc = new int[i > 0 ? i : 1];
        int loadShader = GLHelper.loadShader(str, str2);
        this.hProgram = loadShader;
        GLES20.glUseProgram(loadShader);
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.hProgram, "aPosition");
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.hProgram, "aTextureCoord");
        this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uMVPMatrix");
        this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uTexMatrix");
        this.muTexLoc[0] = GLES20.glGetUniformLocation(this.hProgram, "sTexture");
        int i2 = 1;
        while (i2 < i) {
            int i3 = i2 + 1;
            this.muTexLoc[i2] = GLES20.glGetUniformLocation(this.hProgram, String.format(Locale.US, "sTexture%d", Integer.valueOf(i3)));
            i2 = i3;
        }
        Matrix.setIdentityM(this.mMvpMatrix, 0);
        int i4 = this.muMVPMatrixLoc;
        if (i4 >= 0) {
            GLES20.glUniformMatrix4fv(i4, 1, false, this.mMvpMatrix, 0);
        }
        int i5 = this.muTexMatrixLoc;
        if (i5 >= 0) {
            GLES20.glUniformMatrix4fv(i5, 1, false, this.mMvpMatrix, 0);
        }
        GLES20.glVertexAttribPointer(glGetAttribLocation, 2, 5126, false, 8, (Buffer) asFloatBuffer);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation);
        GLES20.glVertexAttribPointer(glGetAttribLocation2, 2, 5126, false, 8, (Buffer) asFloatBuffer2);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation2);
    }

    public void release() {
        GLES20.glUseProgram(0);
        int i = this.hProgram;
        if (i >= 0) {
            GLES20.glDeleteProgram(i);
        }
        this.hProgram = -1;
    }

    protected int getProgram() {
        return this.hProgram;
    }

    public float[] getMvpMatrix() {
        return this.mMvpMatrix;
    }

    public void setMvpMatrix(float[] fArr, int i) {
        synchronized (this.mSync) {
            System.arraycopy(fArr, i, this.mMvpMatrix, 0, this.mMvpMatrix.length);
        }
    }

    public void getMvpMatrix(float[] fArr, int i) {
        float[] fArr2 = this.mMvpMatrix;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    public void apply(int[] iArr, float[] fArr, int i) {
        synchronized (this.mSync) {
            GLES20.glUseProgram(this.hProgram);
            preDraw(iArr, fArr, i);
            draw(iArr, fArr, i);
            postDraw();
        }
    }

    protected void preDraw(int[] iArr, float[] fArr, int i) {
        int i2 = this.muTexMatrixLoc;
        if (i2 >= 0 && fArr != null) {
            GLES20.glUniformMatrix4fv(i2, 1, false, fArr, i);
        }
        int i3 = this.muMVPMatrixLoc;
        if (i3 >= 0) {
            GLES20.glUniformMatrix4fv(i3, 1, false, this.mMvpMatrix, 0);
        }
        bindTexture(iArr);
    }

    protected void bindTexture(int[] iArr) {
        int length = iArr.length;
        int[] iArr2 = this.muTexLoc;
        int length2 = length < iArr2.length ? iArr.length : iArr2.length;
        for (int i = 0; i < length2; i++) {
            if (iArr[i] != -1) {
                GLES20.glActiveTexture(ShaderConst.TEX_NUMBERS[i]);
                GLES20.glBindTexture(this.mTexTarget, iArr[i]);
                GLES20.glUniform1i(this.muTexLoc[i], i);
            }
        }
    }

    protected void draw(int[] iArr, float[] fArr, int i) {
        GLES20.glDrawArrays(5, 0, 4);
    }

    protected void postDraw() {
        unbindTexture();
        GLES20.glUseProgram(0);
    }

    protected void unbindTexture() {
        for (int i = 0; i < this.muTexLoc.length; i++) {
            GLES20.glActiveTexture(ShaderConst.TEX_NUMBERS[i]);
            GLES20.glBindTexture(this.mTexTarget, 0);
        }
    }
}
