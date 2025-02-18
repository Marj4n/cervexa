package com.serenegiant.glutils;

import android.opengl.GLES20;
import android.view.MotionEvent;
import java.nio.Buffer;
import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class Texture2dProgram {
    private static final boolean DEBUG = false;
    private static final String TAG = "Texture2dProgram";
    private float mColorAdjust;
    private final int[] mFlags;
    protected boolean mHasKernel2;
    private final float[] mKernel;
    private final float[] mLastTouchPosition;
    private int mProgramHandle;
    private final ProgramType mProgramType;
    private final float[] mSummedTouchPosition;
    private final Object mSync;
    private float mTexHeight;
    private float[] mTexOffset;
    private float mTexWidth;
    private int mTextureTarget;
    private final int maPositionLoc;
    private final int maTextureCoordLoc;
    private int muColorAdjustLoc;
    private int muFlagsLoc;
    private int muKernelLoc;
    private final int muMVPMatrixLoc;
    private final int muTexMatrixLoc;
    private int muTexOffsetLoc;
    private int muTouchPositionLoc;

    public enum ProgramType {
        TEXTURE_2D,
        TEXTURE_FILT3x3,
        TEXTURE_CUSTOM,
        TEXTURE_EXT,
        TEXTURE_EXT_BW,
        TEXTURE_EXT_NIGHT,
        TEXTURE_EXT_CHROMA_KEY,
        TEXTURE_EXT_SQUEEZE,
        TEXTURE_EXT_TWIRL,
        TEXTURE_EXT_TUNNEL,
        TEXTURE_EXT_BULGE,
        TEXTURE_EXT_DENT,
        TEXTURE_EXT_FISHEYE,
        TEXTURE_EXT_STRETCH,
        TEXTURE_EXT_MIRROR,
        TEXTURE_EXT_FILT3x3
    }

    public Texture2dProgram(int i, String str) {
        this(ProgramType.TEXTURE_CUSTOM, i, ShaderConst.VERTEX_SHADER, str);
    }

    public Texture2dProgram(int i, String str, String str2) {
        this(ProgramType.TEXTURE_CUSTOM, i, str, str2);
    }

    public Texture2dProgram(ProgramType programType) {
        this(programType, 0, null, null);
    }

    protected Texture2dProgram(ProgramType programType, int i, String str, String str2) {
        this.mSync = new Object();
        this.mKernel = new float[18];
        this.mSummedTouchPosition = new float[2];
        this.mLastTouchPosition = new float[2];
        this.mFlags = new int[4];
        this.mProgramType = programType;
        switch (AnonymousClass1.$SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[programType.ordinal()]) {
            case 1:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_2D;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_2D);
                break;
            case 2:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_2D;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_FILT3x3);
                break;
            case 3:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT);
                break;
            case 4:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_BW);
                break;
            case 5:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_NIGHT);
                break;
            case 6:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_CHROMA_KEY);
                break;
            case 7:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_SQUEEZE);
                break;
            case 8:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_TWIRL);
                break;
            case 9:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_TUNNEL);
                break;
            case 10:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_BULGE);
                break;
            case 11:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_FISHEYE);
                break;
            case 12:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_DENT);
                break;
            case 13:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_MIRROR);
                break;
            case 14:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_STRETCH);
                break;
            case 15:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_EXT_FILT3x3);
                break;
            case 16:
                if (i != 3553 && i != 36197) {
                    throw new IllegalArgumentException("target should be GL_TEXTURE_2D or GL_TEXTURE_EXTERNAL_OES");
                }
                this.mTextureTarget = i;
                this.mProgramHandle = GLHelper.loadShader(str, str2);
                break;
            default:
                throw new RuntimeException("Unhandled type " + programType);
        }
        int i2 = this.mProgramHandle;
        if (i2 == 0) {
            throw new RuntimeException("Unable to create program");
        }
        int glGetAttribLocation = GLES20.glGetAttribLocation(i2, "aPosition");
        this.maPositionLoc = glGetAttribLocation;
        GLHelper.checkLocation(glGetAttribLocation, "aPosition");
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.mProgramHandle, "aTextureCoord");
        this.maTextureCoordLoc = glGetAttribLocation2;
        GLHelper.checkLocation(glGetAttribLocation2, "aTextureCoord");
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.mProgramHandle, "uMVPMatrix");
        this.muMVPMatrixLoc = glGetUniformLocation;
        GLHelper.checkLocation(glGetUniformLocation, "uMVPMatrix");
        this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexMatrix");
        initLocation(null, null);
    }

    /* renamed from: com.serenegiant.glutils.Texture2dProgram$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType;

        static {
            int[] iArr = new int[ProgramType.values().length];
            $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType = iArr;
            try {
                iArr[ProgramType.TEXTURE_2D.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_FILT3x3.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_BW.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_NIGHT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_CHROMA_KEY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_SQUEEZE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_TWIRL.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_TUNNEL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_BULGE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_FISHEYE.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_DENT.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_MIRROR.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_STRETCH.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_EXT_FILT3x3.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$serenegiant$glutils$Texture2dProgram$ProgramType[ProgramType.TEXTURE_CUSTOM.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
        }
    }

    public void release() {
        GLES20.glDeleteProgram(this.mProgramHandle);
        this.mProgramHandle = -1;
    }

    public ProgramType getProgramType() {
        return this.mProgramType;
    }

    public int getProgramHandle() {
        return this.mProgramHandle;
    }

    public int createTextureObject() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLHelper.checkGlError("glGenTextures");
        int i = iArr[0];
        GLES20.glBindTexture(this.mTextureTarget, i);
        GLHelper.checkGlError("glBindTexture " + i);
        GLES20.glTexParameterf(this.mTextureTarget, 10241, 9728.0f);
        GLES20.glTexParameterf(this.mTextureTarget, 10240, 9729.0f);
        GLES20.glTexParameteri(this.mTextureTarget, 10242, 33071);
        GLES20.glTexParameteri(this.mTextureTarget, 10243, 33071);
        GLHelper.checkGlError("glTexParameter");
        return i;
    }

    public void handleTouchEvent(MotionEvent motionEvent) {
        synchronized (this.mSync) {
            if (motionEvent.getAction() == 2) {
                if (this.mTexHeight != 0.0f && this.mTexWidth != 0.0f) {
                    float[] fArr = this.mSummedTouchPosition;
                    fArr[0] = fArr[0] + (((motionEvent.getX() - this.mLastTouchPosition[0]) * 2.0f) / this.mTexWidth);
                    float[] fArr2 = this.mSummedTouchPosition;
                    fArr2[1] = fArr2[1] + (((motionEvent.getY() - this.mLastTouchPosition[1]) * 2.0f) / (-this.mTexHeight));
                    this.mLastTouchPosition[0] = motionEvent.getX();
                    this.mLastTouchPosition[1] = motionEvent.getY();
                }
            } else if (motionEvent.getAction() == 0) {
                this.mLastTouchPosition[0] = motionEvent.getX();
                this.mLastTouchPosition[1] = motionEvent.getY();
            }
        }
    }

    public void setKernel(float[] fArr, float f) {
        if (fArr.length < 9) {
            throw new IllegalArgumentException("Kernel size is " + fArr.length + " vs. 9");
        }
        System.arraycopy(fArr, 0, this.mKernel, 0, 9);
        this.mColorAdjust = f;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0014 A[Catch: all -> 0x000d, TryCatch #0 {all -> 0x000d, blocks: (B:18:0x0008, B:6:0x0010, B:8:0x0014, B:9:0x0019), top: B:17:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setKernel2(float[] r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mSync
            monitor-enter(r0)
            r1 = 0
            r2 = 9
            if (r5 == 0) goto Lf
            int r3 = r5.length     // Catch: java.lang.Throwable -> Ld
            if (r3 != r2) goto Lf
            r3 = 1
            goto L10
        Ld:
            r5 = move-exception
            goto L1b
        Lf:
            r3 = 0
        L10:
            r4.mHasKernel2 = r3     // Catch: java.lang.Throwable -> Ld
            if (r3 == 0) goto L19
            float[] r3 = r4.mKernel     // Catch: java.lang.Throwable -> Ld
            java.lang.System.arraycopy(r5, r1, r3, r2, r2)     // Catch: java.lang.Throwable -> Ld
        L19:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Ld
            return
        L1b:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Ld
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.glutils.Texture2dProgram.setKernel2(float[]):void");
    }

    public void setColorAdjust(float f) {
        synchronized (this.mSync) {
            this.mColorAdjust = f;
        }
    }

    public void setTexSize(int i, int i2) {
        float f = i2;
        this.mTexHeight = f;
        float f2 = i;
        this.mTexWidth = f2;
        float f3 = 1.0f / f2;
        float f4 = 1.0f / f;
        synchronized (this.mSync) {
            float f5 = -f3;
            float f6 = -f4;
            this.mTexOffset = new float[]{f5, f6, 0.0f, f6, f3, f6, f5, 0.0f, 0.0f, 0.0f, f3, 0.0f, f5, f4, 0.0f, f4, f3, f4};
        }
    }

    public void setFlags(int[] iArr) {
        int min = Math.min(4, iArr != null ? iArr.length : 0);
        if (min > 0) {
            synchronized (this.mSync) {
                System.arraycopy(iArr, 0, this.mFlags, 0, min);
            }
        }
    }

    public void setFlag(int i, int i2) {
        if (i < 0 || i >= this.mFlags.length) {
            return;
        }
        synchronized (this.mSync) {
            this.mFlags[i] = i2;
        }
    }

    public void draw(float[] fArr, int i, FloatBuffer floatBuffer, int i2, int i3, int i4, int i5, float[] fArr2, int i6, FloatBuffer floatBuffer2, int i7, int i8) {
        GLHelper.checkGlError("draw start");
        GLES20.glUseProgram(this.mProgramHandle);
        GLHelper.checkGlError("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(this.mTextureTarget, i7);
        GLHelper.checkGlError("glBindTexture");
        synchronized (this.mSync) {
            GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, fArr, i);
            GLHelper.checkGlError("glUniformMatrix4fv");
            if (this.muTexMatrixLoc >= 0) {
                GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, fArr2, i6);
                GLHelper.checkGlError("glUniformMatrix4fv");
            }
            GLES20.glEnableVertexAttribArray(this.maPositionLoc);
            GLHelper.checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(this.maPositionLoc, i4, 5126, false, i5, (Buffer) floatBuffer);
            GLHelper.checkGlError("glVertexAttribPointer");
            GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
            GLHelper.checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, i8, (Buffer) floatBuffer2);
            GLHelper.checkGlError("glVertexAttribPointer");
            if (this.muKernelLoc >= 0) {
                if (!this.mHasKernel2) {
                    GLES20.glUniform1fv(this.muKernelLoc, 9, this.mKernel, 0);
                } else {
                    GLES20.glUniform1fv(this.muKernelLoc, 18, this.mKernel, 0);
                }
                GLHelper.checkGlError("set kernel");
            }
            if (this.muTexOffsetLoc >= 0 && this.mTexOffset != null) {
                GLES20.glUniform2fv(this.muTexOffsetLoc, 9, this.mTexOffset, 0);
            }
            if (this.muColorAdjustLoc >= 0) {
                GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust);
            }
            if (this.muTouchPositionLoc >= 0) {
                GLES20.glUniform2fv(this.muTouchPositionLoc, 1, this.mSummedTouchPosition, 0);
            }
            if (this.muFlagsLoc >= 0) {
                GLES20.glUniform1iv(this.muFlagsLoc, 4, this.mFlags, 0);
            }
        }
        internal_draw(i2, i3);
        GLES20.glDisableVertexAttribArray(this.maPositionLoc);
        GLES20.glDisableVertexAttribArray(this.maTextureCoordLoc);
        GLES20.glBindTexture(this.mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }

    protected void initLocation(float[] fArr, float[] fArr2) {
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.mProgramHandle, "uKernel");
        this.muKernelLoc = glGetUniformLocation;
        if (glGetUniformLocation < 0) {
            this.muKernelLoc = -1;
            this.muTexOffsetLoc = -1;
        } else {
            int glGetUniformLocation2 = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexOffset");
            this.muTexOffsetLoc = glGetUniformLocation2;
            if (glGetUniformLocation2 < 0) {
                this.muTexOffsetLoc = -1;
            }
            if (fArr == null) {
                fArr = ShaderConst.KERNEL_NULL;
            }
            setKernel(fArr, 0.0f);
            setTexSize(256, 256);
        }
        if (fArr2 != null) {
            setKernel2(fArr2);
        }
        int glGetUniformLocation3 = GLES20.glGetUniformLocation(this.mProgramHandle, "uColorAdjust");
        this.muColorAdjustLoc = glGetUniformLocation3;
        if (glGetUniformLocation3 < 0) {
            this.muColorAdjustLoc = -1;
        }
        int glGetUniformLocation4 = GLES20.glGetUniformLocation(this.mProgramHandle, "uPosition");
        this.muTouchPositionLoc = glGetUniformLocation4;
        if (glGetUniformLocation4 < 0) {
            this.muTouchPositionLoc = -1;
        }
        int glGetUniformLocation5 = GLES20.glGetUniformLocation(this.mProgramHandle, "uFlags");
        this.muFlagsLoc = glGetUniformLocation5;
        if (glGetUniformLocation5 < 0) {
            this.muFlagsLoc = -1;
        }
    }

    protected void internal_draw(int i, int i2) {
        GLES20.glDrawArrays(5, i, i2);
        GLHelper.checkGlError("glDrawArrays");
    }
}
