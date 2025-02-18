package com.serenegiant.mediaeffect;

import android.opengl.GLES20;
import com.serenegiant.glutils.GLHelper;
import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectKernel3x3Drawer extends MediaEffectColorAdjustDrawer {
    public static final int KERNEL_SIZE = 9;
    private final float[] mKernel;
    private float mTexHeight;
    private final float[] mTexOffset;
    private float mTexWidth;
    private final int muKernelLoc;
    private final int muTexOffsetLoc;
    public static final float[] KERNEL_NULL = {0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public static final float[] KERNEL_SOBEL_H = {1.0f, 0.0f, -1.0f, 2.0f, 0.0f, -2.0f, 1.0f, 0.0f, -1.0f};
    public static final float[] KERNEL_SOBEL_V = {1.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0f, -1.0f};
    public static final float[] KERNEL_SOBEL2_H = {3.0f, 0.0f, -3.0f, 10.0f, 0.0f, -10.0f, 3.0f, 0.0f, -3.0f};
    public static final float[] KERNEL_SOBEL2_V = {3.0f, 10.0f, 3.0f, 0.0f, 0.0f, 0.0f, -3.0f, -10.0f, -3.0f};
    public static final float[] KERNEL_SHARPNESS = {0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f};
    public static final float[] KERNEL_EDGE_DETECT = {-1.0f, -1.0f, -1.0f, -1.0f, 8.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    public static final float[] KERNEL_EMBOSS = {2.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f};
    public static final float[] KERNEL_SMOOTH = {0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f, 0.11111111f};
    public static final float[] KERNEL_GAUSSIAN = {0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f};
    public static final float[] KERNEL_BRIGHTEN = {1.0f, 1.0f, 1.0f, 1.0f, 2.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    public static final float[] KERNEL_LAPLACIAN = {1.0f, 1.0f, 1.0f, 1.0f, -8.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private static final String FRAGMENT_SHADER_FILT3x3_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n";
    private static final String FRAGMENT_SHADER_FILT3x3 = String.format(FRAGMENT_SHADER_FILT3x3_BASE, "", ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT_FILT3x3 = String.format(FRAGMENT_SHADER_FILT3x3_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES);

    public MediaEffectKernel3x3Drawer(String str) {
        this(false, ShaderConst.VERTEX_SHADER, str);
    }

    public MediaEffectKernel3x3Drawer(boolean z, String str) {
        this(z, ShaderConst.VERTEX_SHADER, str);
    }

    public MediaEffectKernel3x3Drawer(boolean z, String str, String str2) {
        super(z, str, str2);
        this.mKernel = new float[18];
        this.mTexOffset = new float[18];
        int glGetUniformLocation = GLES20.glGetUniformLocation(getProgram(), "uKernel");
        this.muKernelLoc = glGetUniformLocation;
        if (glGetUniformLocation < 0) {
            this.muTexOffsetLoc = -1;
            return;
        }
        this.muTexOffsetLoc = GLES20.glGetUniformLocation(getProgram(), "uTexOffset");
        setKernel(KERNEL_NULL, 0.0f);
        setTexSize(256, 256);
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectColorAdjustDrawer, com.serenegiant.mediaeffect.MediaEffectDrawer
    protected void preDraw(int[] iArr, float[] fArr, int i) {
        super.preDraw(iArr, fArr, i);
        int i2 = this.muKernelLoc;
        if (i2 >= 0) {
            GLES20.glUniform1fv(i2, 9, this.mKernel, 0);
            GLHelper.checkGlError("set kernel");
        }
        int i3 = this.muTexOffsetLoc;
        if (i3 >= 0) {
            GLES20.glUniform2fv(i3, 9, this.mTexOffset, 0);
        }
    }

    public void setKernel(float[] fArr, float f) {
        if (fArr == null || fArr.length < 9) {
            StringBuilder sb = new StringBuilder();
            sb.append("Kernel size is ");
            sb.append(fArr != null ? fArr.length : 0);
            sb.append(" vs. ");
            sb.append(9);
            throw new IllegalArgumentException(sb.toString());
        }
        synchronized (this.mSync) {
            System.arraycopy(fArr, 0, this.mKernel, 0, 9);
            setColorAdjust(f);
        }
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
    public void setTexSize(int i, int i2) {
        synchronized (this.mSync) {
            float f = i;
            if (this.mTexWidth != f || this.mTexHeight != i2) {
                float f2 = i2;
                this.mTexHeight = f2;
                this.mTexWidth = f;
                float f3 = 1.0f / f;
                float f4 = 1.0f / f2;
                float f5 = -f3;
                this.mTexOffset[0] = f5;
                float f6 = -f4;
                this.mTexOffset[1] = f6;
                this.mTexOffset[2] = 0.0f;
                this.mTexOffset[3] = f6;
                this.mTexOffset[4] = f3;
                this.mTexOffset[5] = f6;
                this.mTexOffset[6] = f5;
                this.mTexOffset[7] = 0.0f;
                this.mTexOffset[8] = 0.0f;
                this.mTexOffset[9] = 0.0f;
                this.mTexOffset[10] = f3;
                this.mTexOffset[11] = 0.0f;
                this.mTexOffset[12] = f5;
                this.mTexOffset[13] = f4;
                this.mTexOffset[14] = 0.0f;
                this.mTexOffset[15] = f4;
                this.mTexOffset[16] = f3;
                this.mTexOffset[17] = f4;
            }
        }
    }
}
