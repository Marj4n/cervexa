package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Keyframe;
import java.util.ArrayList;

/* loaded from: classes.dex */
class IntKeyframeSet extends KeyframeSet {
    private int deltaValue;
    private boolean firstTime;
    private int firstValue;
    private int lastValue;

    public IntKeyframeSet(Keyframe.IntKeyframe... intKeyframeArr) {
        super(intKeyframeArr);
        this.firstTime = true;
    }

    @Override // com.nineoldandroids.animation.KeyframeSet
    public Object getValue(float f) {
        return Integer.valueOf(getIntValue(f));
    }

    @Override // com.nineoldandroids.animation.KeyframeSet
    /* renamed from: clone */
    public IntKeyframeSet mo12clone() {
        ArrayList<Keyframe> arrayList = this.mKeyframes;
        int size = this.mKeyframes.size();
        Keyframe.IntKeyframe[] intKeyframeArr = new Keyframe.IntKeyframe[size];
        for (int i = 0; i < size; i++) {
            intKeyframeArr[i] = (Keyframe.IntKeyframe) arrayList.get(i).mo13clone();
        }
        return new IntKeyframeSet(intKeyframeArr);
    }

    public int getIntValue(float f) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.IntKeyframe) this.mKeyframes.get(0)).getIntValue();
                int intValue = ((Keyframe.IntKeyframe) this.mKeyframes.get(1)).getIntValue();
                this.lastValue = intValue;
                this.deltaValue = intValue - this.firstValue;
            }
            if (this.mInterpolator != null) {
                f = this.mInterpolator.getInterpolation(f);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + ((int) (f * this.deltaValue));
            }
            return ((Number) this.mEvaluator.evaluate(f, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
        }
        if (f <= 0.0f) {
            Keyframe.IntKeyframe intKeyframe = (Keyframe.IntKeyframe) this.mKeyframes.get(0);
            Keyframe.IntKeyframe intKeyframe2 = (Keyframe.IntKeyframe) this.mKeyframes.get(1);
            int intValue2 = intKeyframe.getIntValue();
            int intValue3 = intKeyframe2.getIntValue();
            float fraction = intKeyframe.getFraction();
            float fraction2 = intKeyframe2.getFraction();
            Interpolator interpolator = intKeyframe2.getInterpolator();
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
            float f2 = (f - fraction) / (fraction2 - fraction);
            return this.mEvaluator == null ? intValue2 + ((int) (f2 * (intValue3 - intValue2))) : ((Number) this.mEvaluator.evaluate(f2, Integer.valueOf(intValue2), Integer.valueOf(intValue3))).intValue();
        }
        if (f >= 1.0f) {
            Keyframe.IntKeyframe intKeyframe3 = (Keyframe.IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 2);
            Keyframe.IntKeyframe intKeyframe4 = (Keyframe.IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            int intValue4 = intKeyframe3.getIntValue();
            int intValue5 = intKeyframe4.getIntValue();
            float fraction3 = intKeyframe3.getFraction();
            float fraction4 = intKeyframe4.getFraction();
            Interpolator interpolator2 = intKeyframe4.getInterpolator();
            if (interpolator2 != null) {
                f = interpolator2.getInterpolation(f);
            }
            float f3 = (f - fraction3) / (fraction4 - fraction3);
            return this.mEvaluator == null ? intValue4 + ((int) (f3 * (intValue5 - intValue4))) : ((Number) this.mEvaluator.evaluate(f3, Integer.valueOf(intValue4), Integer.valueOf(intValue5))).intValue();
        }
        Keyframe.IntKeyframe intKeyframe5 = (Keyframe.IntKeyframe) this.mKeyframes.get(0);
        int i = 1;
        while (i < this.mNumKeyframes) {
            Keyframe.IntKeyframe intKeyframe6 = (Keyframe.IntKeyframe) this.mKeyframes.get(i);
            if (f < intKeyframe6.getFraction()) {
                Interpolator interpolator3 = intKeyframe6.getInterpolator();
                if (interpolator3 != null) {
                    f = interpolator3.getInterpolation(f);
                }
                float fraction5 = (f - intKeyframe5.getFraction()) / (intKeyframe6.getFraction() - intKeyframe5.getFraction());
                int intValue6 = intKeyframe5.getIntValue();
                return this.mEvaluator == null ? intValue6 + ((int) (fraction5 * (r1 - intValue6))) : ((Number) this.mEvaluator.evaluate(fraction5, Integer.valueOf(intValue6), Integer.valueOf(intKeyframe6.getIntValue()))).intValue();
            }
            i++;
            intKeyframe5 = intKeyframe6;
        }
        return ((Number) this.mKeyframes.get(this.mNumKeyframes - 1).getValue()).intValue();
    }
}
