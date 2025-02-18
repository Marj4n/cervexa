package com.serenegiant.mediaeffect;

import android.graphics.Matrix;
import android.opengl.GLES20;
import com.serenegiant.glutils.GLHelper;
import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.mediaeffect.MediaEffectDrawer;

/* loaded from: classes2.dex */
public class MediaEffectTexProjection extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    public static final String PROJ_VERTEX_SHADER = "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nuniform mat3 uTexMatrix2;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\ngl_Position = uMVPMatrix * aPosition;\nvec3 tex_coord = vec3((uTexMatrix * aTextureCoord).xy, 1.0);\nvec3 temp = uTexMatrix2 * tex_coord;\nvTextureCoord = temp.xy / temp.z;\n}\n";
    private static final String TAG = "MediaEffectTexProjection";
    private final float[] m;
    private final Matrix mat;
    private static final String FRAGMENT_SHADER_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final String FRAGMENT_SHADER = String.format(FRAGMENT_SHADER_BASE, "", ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT = String.format(FRAGMENT_SHADER_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES);

    private static class MediaEffectTexProjectionDrawer extends MediaEffectDrawer.MediaEffectSingleDrawer {
        private final int muTexMatrixLoc2;
        private float[] texMatrix2;

        public MediaEffectTexProjectionDrawer(String str, String str2) {
            super(false, str, str2);
            this.texMatrix2 = new float[9];
            int glGetUniformLocation = GLES20.glGetUniformLocation(getProgram(), "uTexMatrix2");
            this.muTexMatrixLoc2 = glGetUniformLocation;
            GLHelper.checkLocation(glGetUniformLocation, "uTexMatrix2");
            reset();
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        protected void preDraw(int[] iArr, float[] fArr, int i) {
            int i2 = this.muTexMatrixLoc2;
            if (i2 >= 0) {
                GLES20.glUniformMatrix3fv(i2, 1, false, this.texMatrix2, 0);
                GLHelper.checkGlError("glUniformMatrix3fv");
            }
            super.preDraw(iArr, fArr, i);
        }

        public void reset() {
            setTexProjection(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        }

        public void setTexProjection(float[] fArr) {
            synchronized (this.mSync) {
            }
        }
    }

    public MediaEffectTexProjection() {
        super(new MediaEffectTexProjectionDrawer(PROJ_VERTEX_SHADER, FRAGMENT_SHADER));
        this.mat = new Matrix();
        this.m = new float[9];
    }

    public void calcPerspectiveTransform(float[] fArr, float[] fArr2) {
        this.mat.reset();
        this.mat.setPolyToPoly(fArr, 0, fArr2, 0, 4);
        this.mat.getValues(this.m);
        ((MediaEffectTexProjectionDrawer) this.mDrawer).setTexProjection(this.m);
    }
}
