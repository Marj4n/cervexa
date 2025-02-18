package com.serenegiant.mediaeffect;

/* loaded from: classes2.dex */
public class MediaEffectEmboss extends MediaEffectKernel {
    private float mIntensity;

    public MediaEffectEmboss() {
        this(1.0f);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaEffectEmboss(float r5) {
        /*
            r4 = this;
            r0 = 9
            float[] r0 = new float[r0]
            r1 = -1073741824(0xffffffffc0000000, float:-2.0)
            float r1 = r1 * r5
            r2 = 0
            r0[r2] = r1
            float r1 = -r5
            r2 = 1
            r0[r2] = r1
            r2 = 2
            r3 = 0
            r0[r2] = r3
            r2 = 3
            r0[r2] = r1
            r1 = 4
            r2 = 1065353216(0x3f800000, float:1.0)
            r0[r1] = r2
            r1 = 5
            r0[r1] = r5
            r1 = 6
            r0[r1] = r3
            r1 = 7
            r0[r1] = r5
            r1 = 1073741824(0x40000000, float:2.0)
            float r1 = r1 * r5
            r2 = 8
            r0[r2] = r1
            r4.<init>(r0)
            r4.mIntensity = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.mediaeffect.MediaEffectEmboss.<init>(float):void");
    }

    public MediaEffectEmboss setParameter(float f) {
        if (this.mIntensity != f) {
            this.mIntensity = f;
            float f2 = -f;
            setParameter(new float[]{(-2.0f) * f, f2, 0.0f, f2, 1.0f, f, 0.0f, f, f * 2.0f}, 0.0f);
        }
        return this;
    }
}
