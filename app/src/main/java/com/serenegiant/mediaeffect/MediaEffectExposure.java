package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectExposure extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectExposure";
    private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb * pow(2.0, uColorAdjust), tex.w);\n}\n";
    private static final String FRAGMENT_SHADER = String.format(FRAGMENT_SHADER_BASE, "", ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT = String.format(FRAGMENT_SHADER_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES);

    public MediaEffectExposure() {
        super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER));
    }

    public MediaEffectExposure(float f) {
        this();
        setParameter(f);
    }

    public MediaEffectExposure setParameter(float f) {
        setEnable(f != 0.0f);
        ((MediaEffectColorAdjustDrawer) this.mDrawer).setColorAdjust(f);
        return this;
    }
}
