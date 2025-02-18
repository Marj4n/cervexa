package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectPosterize extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectBrightness";
    private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = floor((tex * uColorAdjust) + vec4(0.5)) / uColorAdjust;\n}\n";
    private static final String FRAGMENT_SHADER = String.format(FRAGMENT_SHADER_BASE, "", ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT = String.format(FRAGMENT_SHADER_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES);

    public MediaEffectPosterize() {
        this(10.0f);
    }

    public MediaEffectPosterize(float f) {
        super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER));
        setParameter(f);
    }

    public MediaEffectPosterize setParameter(float f) {
        ((MediaEffectColorAdjustDrawer) this.mDrawer).setColorAdjust(f);
        return this;
    }
}
