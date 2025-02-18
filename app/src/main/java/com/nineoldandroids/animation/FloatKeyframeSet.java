package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Keyframe;
import java.util.ArrayList;

/* loaded from: classes.dex */
class FloatKeyframeSet extends KeyframeSet {
    private float deltaValue;
    private boolean firstTime;
    private float firstValue;
    private float lastValue;

    public FloatKeyframeSet(Keyframe.FloatKeyframe... floatKeyframeArr) {
        super(floatKeyframeArr);
        this.firstTime = true;
    }

    @Override // com.nineoldandroids.animation.KeyframeSet
    public Object getValue(float f) {
        return Float.valueOf(getFloatValue(f));
    }

    @Override // com.nineoldandroids.animation.KeyframeSet
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public FloatKeyframeSet mo12clone() {
        ArrayList<Keyframe> arrayList = this.mKeyframes;
        int size = this.mKeyframes.size();
        Keyframe.FloatKeyframe[] floatKeyframeArr = new Keyframe.FloatKeyframe[size];
        for (int i = 0; i < size; i++) {
            floatKeyframeArr[i] = (Keyframe.FloatKeyframe) arrayList.get(i).mo13clone();
        }
        return new FloatKeyframeSet(floatKeyframeArr);
    }

    public float getFloatValue(float f) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.FloatKeyframe) this.mKeyframes.get(0)).getFloatValue();
                float floatValue = ((Keyframe.FloatKeyframe) this.mKeyframes.get(1)).getFloatValue();
                this.lastValue = floatValue;
                this.deltaValue = floatValue - this.firstValue;
            }
            if (this.mInterpolator != null) {
                f = this.mInterpolator.getInterpolation(f);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + (f * this.deltaValue);
            }
            return ((Number) this.mEvaluator.evaluate(f, Float.valueOf(this.firstValue), Float.valueOf(this.lastValue))).floatValue();
        }
        if (f <= 0.0f) {
            Keyframe.FloatKeyframe floatKeyframe = (Keyframe.FloatKeyframe) this.mKeyframes.get(0);
            Keyframe.FloatKeyframe floatKeyframe2 = (Keyframe.FloatKeyframe) this.mKeyframes.get(1);
            float floatValue2 = floatKeyframe.getFloatValue();
            float floatValue3 = floatKeyframe2.getFloatValue();
            float fraction = floatKeyframe.getFraction();
            float fraction2 = floatKeyframe2.getFraction();
            Interpolator interpolator = floatKeyframe2.getInterpolator();
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
            float f2 = (f - fraction) / (fraction2 - fraction);
            return this.mEvaluator == null ? floatValue2 + (f2 * (floatValue3 - floatValue2)) : ((Number) this.mEvaluator.evaluate(f2, Float.valueOf(floatValue2), Float.valueOf(floatValue3))).floatValue();
        }
        if (f >= 1.0f) {
            Keyframe.FloatKeyframe floatKeyframe3 = (Keyframe.FloatKeyframe) this.mKeyframes.get(this.mNumKeyframes - 2);
            Keyframe.FloatKeyframe floatKeyframe4 = (Keyframe.FloatKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            float floatValue4 = floatKeyframe3.getFloatValue();
            float floatValue5 = floatKeyframe4.getFloatValue();
            float fraction3 = floatKeyframe3.getFraction();
            float fraction4 = floatKeyframe4.getFraction();
            Interpolator interpolator2 = floatKeyframe4.getInterpolator();
            if (interpolator2 != null) {
                f = interpolator2.getInterpolation(f);
            }
            float f3 = (f - fraction3) / (fraction4 - fraction3);
            return this.mEvaluator == null ? floatValue4 + (f3 * (floatValue5 - floatValue4)) : ((Number) this.mEvaluator.evaluate(f3, Float.valueOf(floatValue4), Float.valueOf(floatValue5))).floatValue();
        }
        Keyframe.FloatKeyframe floatKeyframe5 = (Keyframe.FloatKeyframe) this.mKeyframes.get(0);
        int i = 1;
        while (i < this.mNumKeyframes) {
            Keyframe.FloatKeyframe floatKeyframe6 = (Keyframe.FloatKeyframe) this.mKeyframes.get(i);
            if (f < floatKeyframe6.getFraction()) {
                Interpolator interpolator3 = floatKeyframe6.getInterpolator();
                if (interpolator3 != null) {
                    f = interpolator3.getInterpolation(f);
                }
                float fraction5 = (f - floatKeyframe5.getFraction()) / (floatKeyframe6.getFraction() - floatKeyframe5.getFraction());
                float floatValue6 = floatKeyframe5.getFloatValue();
                float floatValue7 = floatKeyframe6.getFloatValue();
                return this.mEvaluator == null ? floatValue6 + (fraction5 * (floatValue7 - floatValue6)) : ((Number) this.mEvaluator.evaluate(fraction5, Float.valueOf(floatValue6), Float.valueOf(floatValue7))).floatValue();
            }
            i++;
            floatKeyframe5 = floatKeyframe6;
        }
        return ((Number) this.mKeyframes.get(this.mNumKeyframes - 1).getValue()).floatValue();
    }
}
