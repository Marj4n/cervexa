package com.serenegiant.mediaeffect;

import android.opengl.GLES20;
import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectAlphaBlend extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectAlphaBlend";
    private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform %s    sTexture2;\nuniform float uMixRate;\nvoid main() {\n    highp vec4 tex1 = texture2D(sTexture, vTextureCoord);\n    highp vec4 tex2 = texture2D(sTexture2, vTextureCoord);\n    gl_FragColor = vec4(mix(tex1.rgb, tex2.rgb, tex2.a * uMixRate), tex1.a);\n}\n";
    private static final String FRAGMENT_SHADER = String.format(FRAGMENT_SHADER_BASE, "", ShaderConst.SAMPLER_2D, ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT = String.format(FRAGMENT_SHADER_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES, ShaderConst.SAMPLER_OES);

    private static class MediaEffectAlphaBlendDrawer extends MediaEffectDrawer {
        private float mMixRate;
        private final int muMixRate;

        protected MediaEffectAlphaBlendDrawer(float f) {
            this(f, false);
        }

        protected MediaEffectAlphaBlendDrawer(float f, boolean z) {
            super(2, z, z ? MediaEffectAlphaBlend.FRAGMENT_SHADER_EXT : MediaEffectAlphaBlend.FRAGMENT_SHADER);
            int glGetUniformLocation = GLES20.glGetUniformLocation(getProgram(), "uMixRate");
            this.muMixRate = glGetUniformLocation < 0 ? -1 : glGetUniformLocation;
            setMixRate(f);
        }

        public void setMixRate(float f) {
            synchronized (this.mSync) {
                this.mMixRate = f;
            }
        }

        @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
        protected void preDraw(int[] iArr, float[] fArr, int i) {
            super.preDraw(iArr, fArr, i);
            if (this.muMixRate >= 0) {
                synchronized (this.mSync) {
                    GLES20.glUniform1f(this.muMixRate, this.mMixRate);
                }
            }
        }
    }

    public MediaEffectAlphaBlend() {
        this(0.5f);
    }

    public MediaEffectAlphaBlend(float f) {
        super(new MediaEffectAlphaBlendDrawer(f));
        setParameter(f);
    }

    public MediaEffectAlphaBlend setParameter(float f) {
        setEnable(true);
        ((MediaEffectAlphaBlendDrawer) this.mDrawer).setMixRate(f);
        return this;
    }
}
