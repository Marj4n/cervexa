package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.ShaderConst;

/* loaded from: classes2.dex */
public class MediaEffectExtraction extends MediaEffectGLESBase {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaEffectExtraction";
    private final float[] mLimit;
    private static final String FRAGMENT_SHADER_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    vec3 min = vec3(uKernel[0], uKernel[2], uKernel[4]);\n    vec3 max = vec3(uKernel[1], uKernel[3], uKernel[5]);\n    vec3 add = vec3(uKernel[6], uKernel[7], uKernel[8]);\n    float e = 1e-10;\n    vec3 eps = vec3(e, e, e);\n    vec3 v = hsv;\n    if (hsv.r < min.r || hsv.r > max.r || hsv.g < min.g || hsv.g > max.g || hsv.b < min.b || hsv.b > max.b) {\n        v = vec3(0.0);\n    }\n    hsv = v + add;\n    if (uColorAdjust > 0.0) {\n        hsv = step(vec3(1.0, 1.0, uColorAdjust), hsv);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
    private static final String FRAGMENT_SHADER = String.format(FRAGMENT_SHADER_BASE, "", ShaderConst.SAMPLER_2D);
    private static final String FRAGMENT_SHADER_EXT = String.format(FRAGMENT_SHADER_BASE, ShaderConst.HEADER_OES, ShaderConst.SAMPLER_OES);

    public MediaEffectExtraction() {
        super(new MediaEffectKernel3x3Drawer(FRAGMENT_SHADER));
        this.mLimit = new float[]{0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f};
        ((MediaEffectKernel3x3Drawer) this.mDrawer).setKernel(this.mLimit, 0.0f);
    }

    public MediaEffectExtraction setParameter(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        return setParameter(f, f2, f3, f4, f5, f6, 0.0f, 0.0f, 0.0f, f7);
    }

    public MediaEffectExtraction setParameter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        this.mLimit[0] = Math.min(f, f2);
        this.mLimit[1] = Math.max(f, f2);
        this.mLimit[2] = Math.min(f3, f4);
        this.mLimit[3] = Math.max(f3, f4);
        this.mLimit[4] = Math.min(f5, f6);
        this.mLimit[5] = Math.max(f5, f6);
        float[] fArr = this.mLimit;
        fArr[6] = f7;
        fArr[7] = f8;
        fArr[8] = f9;
        ((MediaEffectKernel3x3Drawer) this.mDrawer).setKernel(this.mLimit, f10);
        return this;
    }

    public MediaEffectExtraction setParameter(float[] fArr, float f) {
        if (fArr == null || fArr.length < 6) {
            throw new IllegalArgumentException("limit is null or short");
        }
        System.arraycopy(fArr, 0, this.mLimit, 0, 6);
        ((MediaEffectKernel3x3Drawer) this.mDrawer).setKernel(this.mLimit, f);
        return this;
    }
}
