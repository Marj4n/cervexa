package com.serenegiant.mediaeffect;

import android.opengl.GLES20;
import com.serenegiant.glutils.GLHelper;
import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.mediaeffect.MediaEffectDrawer;

/* loaded from: classes2.dex */
public class MediaEffectErosion extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    public static final String FRAGMENT_SHADER_1 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 minValue = texture2D(sTexture, vTextureCoord + uTexOffset[0] );\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[1] ));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[2] ));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[3] ));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[4] ));\n\ngl_FragColor = vec4(minValue.rgb, 1.0);\n}\n";
    public static final String FRAGMENT_SHADER_2 = "precision lowp float;\n\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 minValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\n\ngl_FragColor = vec4(minValue.rgb, 1.0);\n}\n";
    public static final String FRAGMENT_SHADER_3 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 minValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\n\ngl_FragColor = vec4(minValue.rgb, 1.0);\n}\n";
    public static final String FRAGMENT_SHADER_4 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 minValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[25]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[26]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[27]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[28]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[29]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[30]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[31]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[32]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[33]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[34]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[35]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[36]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[37]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[38]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[39]));\nminValue = min(minValue, texture2D(sTexture, vTextureCoord + uTexOffset[40]));\n\ngl_FragColor = vec4(minValue.rgb, 1.0);\n}\n";
    private static final String TAG = "MediaEffectErosion";

    private static String getFragmentShader(int i) {
        return (i == 0 || i == 1) ? FRAGMENT_SHADER_1 : i != 2 ? i != 3 ? FRAGMENT_SHADER_4 : FRAGMENT_SHADER_3 : FRAGMENT_SHADER_2;
    }

    private static class MediaEffectErosionDrawer extends MediaEffectDrawer.MediaEffectSingleDrawer {
        private float mTexHeight;
        private final float[] mTexOffset;
        private float mTexWidth;
        private final int muTexOffsetLoc;

        public MediaEffectErosionDrawer(String str) {
            super(false, ShaderConst.VERTEX_SHADER, str);
            this.mTexOffset = new float[82];
            int glGetUniformLocation = GLES20.glGetUniformLocation(getProgram(), "uTexOffset");
            this.muTexOffsetLoc = glGetUniformLocation;
            GLHelper.checkLocation(glGetUniformLocation, "uTexOffset");
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        protected void preDraw(int[] iArr, float[] fArr, int i) {
            super.preDraw(iArr, fArr, i);
            int i2 = this.muTexOffsetLoc;
            if (i2 >= 0) {
                GLES20.glUniform2fv(i2, 41, this.mTexOffset, 0);
            }
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        public void setTexSize(int i, int i2) {
            synchronized (this.mSync) {
                float f = i2;
                this.mTexHeight = f;
                float f2 = i;
                this.mTexWidth = f2;
                float f3 = 1.0f / f2;
                float f4 = 1.0f / f;
                this.mTexOffset[0] = 0.0f;
                this.mTexOffset[1] = 0.0f;
                this.mTexOffset[2] = 0.0f;
                float f5 = -f4;
                this.mTexOffset[3] = f5;
                this.mTexOffset[4] = 0.0f;
                this.mTexOffset[5] = f4;
                float f6 = -f3;
                this.mTexOffset[6] = f6;
                this.mTexOffset[7] = 0.0f;
                this.mTexOffset[8] = f3;
                this.mTexOffset[9] = 0.0f;
                this.mTexOffset[10] = 0.0f;
                float f7 = f5 * 2.0f;
                this.mTexOffset[11] = f7;
                this.mTexOffset[12] = 0.0f;
                float f8 = f4 * 2.0f;
                this.mTexOffset[13] = f8;
                float f9 = f6 * 2.0f;
                this.mTexOffset[14] = f9;
                this.mTexOffset[15] = 0.0f;
                float f10 = 2.0f * f3;
                this.mTexOffset[16] = f10;
                this.mTexOffset[17] = 0.0f;
                this.mTexOffset[18] = f6;
                this.mTexOffset[19] = f5;
                this.mTexOffset[20] = f6;
                this.mTexOffset[21] = f4;
                this.mTexOffset[22] = f3;
                this.mTexOffset[23] = f5;
                this.mTexOffset[24] = f3;
                this.mTexOffset[25] = f4;
                this.mTexOffset[26] = 0.0f;
                float f11 = f5 * 3.0f;
                this.mTexOffset[27] = f11;
                this.mTexOffset[28] = 0.0f;
                float f12 = f4 * 3.0f;
                this.mTexOffset[29] = f12;
                float f13 = f6 * 3.0f;
                this.mTexOffset[30] = f13;
                this.mTexOffset[31] = 0.0f;
                float f14 = 3.0f * f3;
                this.mTexOffset[32] = f14;
                this.mTexOffset[33] = 0.0f;
                this.mTexOffset[34] = f9;
                this.mTexOffset[35] = f5;
                this.mTexOffset[36] = f9;
                this.mTexOffset[37] = f4;
                this.mTexOffset[38] = f10;
                this.mTexOffset[39] = f5;
                this.mTexOffset[40] = f10;
                this.mTexOffset[41] = f4;
                this.mTexOffset[42] = f6;
                this.mTexOffset[43] = f7;
                this.mTexOffset[44] = f6;
                this.mTexOffset[45] = f8;
                this.mTexOffset[46] = f3;
                this.mTexOffset[47] = f7;
                this.mTexOffset[48] = f3;
                this.mTexOffset[49] = f8;
                this.mTexOffset[50] = 0.0f;
                this.mTexOffset[51] = f5 * 4.0f;
                this.mTexOffset[52] = 0.0f;
                this.mTexOffset[53] = f4 * 4.0f;
                this.mTexOffset[54] = f6 * 4.0f;
                this.mTexOffset[55] = 0.0f;
                this.mTexOffset[56] = 4.0f * f3;
                this.mTexOffset[57] = 0.0f;
                this.mTexOffset[58] = f13;
                this.mTexOffset[59] = f5;
                this.mTexOffset[60] = f13;
                this.mTexOffset[61] = f4;
                this.mTexOffset[62] = f14;
                this.mTexOffset[63] = f5;
                this.mTexOffset[64] = f14;
                this.mTexOffset[65] = f4;
                this.mTexOffset[66] = f9;
                this.mTexOffset[67] = f7;
                this.mTexOffset[68] = f9;
                this.mTexOffset[69] = f8;
                this.mTexOffset[70] = f10;
                this.mTexOffset[71] = f7;
                this.mTexOffset[72] = f10;
                this.mTexOffset[73] = f8;
                this.mTexOffset[74] = f6;
                this.mTexOffset[75] = f11;
                this.mTexOffset[76] = f6;
                this.mTexOffset[77] = f12;
                this.mTexOffset[78] = f3;
                this.mTexOffset[79] = f11;
                this.mTexOffset[80] = f3;
                this.mTexOffset[81] = f12;
            }
        }
    }

    public MediaEffectErosion() {
        this(1);
    }

    public MediaEffectErosion(int i) {
        super(new MediaEffectErosionDrawer(getFragmentShader(i)));
        setTexSize(256, 256);
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectGLESBase, com.serenegiant.mediaeffect.IEffect
    public MediaEffectErosion resize(int i, int i2) {
        super.resize(i, i2);
        setTexSize(i, i2);
        return this;
    }

    public void setTexSize(int i, int i2) {
        this.mDrawer.setTexSize(i, i2);
    }
}
