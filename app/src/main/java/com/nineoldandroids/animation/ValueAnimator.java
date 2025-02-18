package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AndroidRuntimeException;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ValueAnimator extends Animator {
    static final int ANIMATION_FRAME = 1;
    static final int ANIMATION_START = 0;
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    static final int RUNNING = 1;
    static final int SEEKED = 2;
    static final int STOPPED = 0;
    private long mDelayStartTime;
    long mStartTime;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;
    private static ThreadLocal<AnimationHandler> sAnimationHandler = new ThreadLocal<>();
    private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations = new ThreadLocal<ArrayList<ValueAnimator>>() { // from class: com.nineoldandroids.animation.ValueAnimator.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ValueAnimator> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations = new ThreadLocal<ArrayList<ValueAnimator>>() { // from class: com.nineoldandroids.animation.ValueAnimator.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ValueAnimator> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims = new ThreadLocal<ArrayList<ValueAnimator>>() { // from class: com.nineoldandroids.animation.ValueAnimator.3
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ValueAnimator> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims = new ThreadLocal<ArrayList<ValueAnimator>>() { // from class: com.nineoldandroids.animation.ValueAnimator.4
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ValueAnimator> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims = new ThreadLocal<ArrayList<ValueAnimator>>() { // from class: com.nineoldandroids.animation.ValueAnimator.5
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ValueAnimator> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final Interpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
    private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
    private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
    private static final long DEFAULT_FRAME_DELAY = 10;
    private static long sFrameDelay = DEFAULT_FRAME_DELAY;
    long mSeekTime = -1;
    private boolean mPlayingBackwards = false;
    private int mCurrentIteration = 0;
    private float mCurrentFraction = 0.0f;
    private boolean mStartedDelay = false;
    int mPlayingState = 0;
    private boolean mRunning = false;
    private boolean mStarted = false;
    boolean mInitialized = false;
    private long mDuration = 300;
    private long mStartDelay = 0;
    private int mRepeatCount = 0;
    private int mRepeatMode = 1;
    private Interpolator mInterpolator = sDefaultInterpolator;
    private ArrayList<AnimatorUpdateListener> mUpdateListeners = null;

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimator valueAnimator);
    }

    public static ValueAnimator ofInt(int... iArr) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(iArr);
        return valueAnimator;
    }

    public static ValueAnimator ofFloat(float... fArr) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(fArr);
        return valueAnimator;
    }

    public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... propertyValuesHolderArr) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(propertyValuesHolderArr);
        return valueAnimator;
    }

    public static ValueAnimator ofObject(TypeEvaluator typeEvaluator, Object... objArr) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(objArr);
        valueAnimator.setEvaluator(typeEvaluator);
        return valueAnimator;
    }

    public void setIntValues(int... iArr) {
        if (iArr == null || iArr.length == 0) {
            return;
        }
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr == null || propertyValuesHolderArr.length == 0) {
            setValues(PropertyValuesHolder.ofInt("", iArr));
        } else {
            propertyValuesHolderArr[0].setIntValues(iArr);
        }
        this.mInitialized = false;
    }

    public void setFloatValues(float... fArr) {
        if (fArr == null || fArr.length == 0) {
            return;
        }
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr == null || propertyValuesHolderArr.length == 0) {
            setValues(PropertyValuesHolder.ofFloat("", fArr));
        } else {
            propertyValuesHolderArr[0].setFloatValues(fArr);
        }
        this.mInitialized = false;
    }

    public void setObjectValues(Object... objArr) {
        if (objArr == null || objArr.length == 0) {
            return;
        }
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr == null || propertyValuesHolderArr.length == 0) {
            setValues(PropertyValuesHolder.ofObject("", (TypeEvaluator) null, objArr));
        } else {
            propertyValuesHolderArr[0].setObjectValues(objArr);
        }
        this.mInitialized = false;
    }

    public void setValues(PropertyValuesHolder... propertyValuesHolderArr) {
        int length = propertyValuesHolderArr.length;
        this.mValues = propertyValuesHolderArr;
        this.mValuesMap = new HashMap<>(length);
        for (PropertyValuesHolder propertyValuesHolder : propertyValuesHolderArr) {
            this.mValuesMap.put(propertyValuesHolder.getPropertyName(), propertyValuesHolder);
        }
        this.mInitialized = false;
    }

    public PropertyValuesHolder[] getValues() {
        return this.mValues;
    }

    void initAnimation() {
        if (this.mInitialized) {
            return;
        }
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].init();
        }
        this.mInitialized = true;
    }

    @Override // com.nineoldandroids.animation.Animator
    public ValueAnimator setDuration(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + j);
        }
        this.mDuration = j;
        return this;
    }

    @Override // com.nineoldandroids.animation.Animator
    public long getDuration() {
        return this.mDuration;
    }

    public void setCurrentPlayTime(long j) {
        initAnimation();
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        if (this.mPlayingState != 1) {
            this.mSeekTime = j;
            this.mPlayingState = 2;
        }
        this.mStartTime = currentAnimationTimeMillis - j;
        animationFrame(currentAnimationTimeMillis);
    }

    public long getCurrentPlayTime() {
        if (!this.mInitialized || this.mPlayingState == 0) {
            return 0L;
        }
        return AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
    }

    private static class AnimationHandler extends Handler {
        private AnimationHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z;
            ArrayList arrayList = (ArrayList) ValueAnimator.sAnimations.get();
            ArrayList arrayList2 = (ArrayList) ValueAnimator.sDelayedAnims.get();
            int i = message.what;
            if (i == 0) {
                ArrayList arrayList3 = (ArrayList) ValueAnimator.sPendingAnimations.get();
                z = arrayList.size() <= 0 && arrayList2.size() <= 0;
                while (arrayList3.size() > 0) {
                    ArrayList arrayList4 = (ArrayList) arrayList3.clone();
                    arrayList3.clear();
                    int size = arrayList4.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        ValueAnimator valueAnimator = (ValueAnimator) arrayList4.get(i2);
                        if (valueAnimator.mStartDelay == 0) {
                            valueAnimator.startAnimation();
                        } else {
                            arrayList2.add(valueAnimator);
                        }
                    }
                }
            } else if (i != 1) {
                return;
            } else {
                z = true;
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            ArrayList arrayList5 = (ArrayList) ValueAnimator.sReadyAnims.get();
            ArrayList arrayList6 = (ArrayList) ValueAnimator.sEndingAnims.get();
            int size2 = arrayList2.size();
            for (int i3 = 0; i3 < size2; i3++) {
                ValueAnimator valueAnimator2 = (ValueAnimator) arrayList2.get(i3);
                if (valueAnimator2.delayedAnimationFrame(currentAnimationTimeMillis)) {
                    arrayList5.add(valueAnimator2);
                }
            }
            int size3 = arrayList5.size();
            if (size3 > 0) {
                for (int i4 = 0; i4 < size3; i4++) {
                    ValueAnimator valueAnimator3 = (ValueAnimator) arrayList5.get(i4);
                    valueAnimator3.startAnimation();
                    valueAnimator3.mRunning = true;
                    arrayList2.remove(valueAnimator3);
                }
                arrayList5.clear();
            }
            int size4 = arrayList.size();
            int i5 = 0;
            while (i5 < size4) {
                ValueAnimator valueAnimator4 = (ValueAnimator) arrayList.get(i5);
                if (valueAnimator4.animationFrame(currentAnimationTimeMillis)) {
                    arrayList6.add(valueAnimator4);
                }
                if (arrayList.size() == size4) {
                    i5++;
                } else {
                    size4--;
                    arrayList6.remove(valueAnimator4);
                }
            }
            if (arrayList6.size() > 0) {
                for (int i6 = 0; i6 < arrayList6.size(); i6++) {
                    ((ValueAnimator) arrayList6.get(i6)).endAnimation();
                }
                arrayList6.clear();
            }
            if (z) {
                if (arrayList.isEmpty() && arrayList2.isEmpty()) {
                    return;
                }
                sendEmptyMessageDelayed(1, Math.max(0L, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - currentAnimationTimeMillis)));
            }
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public long getStartDelay() {
        return this.mStartDelay;
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setStartDelay(long j) {
        this.mStartDelay = j;
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long j) {
        sFrameDelay = j;
    }

    public Object getAnimatedValue() {
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr == null || propertyValuesHolderArr.length <= 0) {
            return null;
        }
        return propertyValuesHolderArr[0].getAnimatedValue();
    }

    public Object getAnimatedValue(String str) {
        PropertyValuesHolder propertyValuesHolder = this.mValuesMap.get(str);
        if (propertyValuesHolder != null) {
            return propertyValuesHolder.getAnimatedValue();
        }
        return null;
    }

    public void setRepeatCount(int i) {
        this.mRepeatCount = i;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatMode(int i) {
        this.mRepeatMode = i;
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void addUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        if (this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList<>();
        }
        this.mUpdateListeners.add(animatorUpdateListener);
    }

    public void removeAllUpdateListeners() {
        ArrayList<AnimatorUpdateListener> arrayList = this.mUpdateListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.clear();
        this.mUpdateListeners = null;
    }

    public void removeUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        ArrayList<AnimatorUpdateListener> arrayList = this.mUpdateListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(animatorUpdateListener);
        if (this.mUpdateListeners.size() == 0) {
            this.mUpdateListeners = null;
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void setInterpolator(Interpolator interpolator) {
        if (interpolator != null) {
            this.mInterpolator = interpolator;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setEvaluator(TypeEvaluator typeEvaluator) {
        PropertyValuesHolder[] propertyValuesHolderArr;
        if (typeEvaluator == null || (propertyValuesHolderArr = this.mValues) == null || propertyValuesHolderArr.length <= 0) {
            return;
        }
        propertyValuesHolderArr[0].setEvaluator(typeEvaluator);
    }

    private void start(boolean z) {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        }
        this.mPlayingBackwards = z;
        this.mCurrentIteration = 0;
        this.mPlayingState = 0;
        this.mStarted = true;
        this.mStartedDelay = false;
        sPendingAnimations.get().add(this);
        if (this.mStartDelay == 0) {
            setCurrentPlayTime(getCurrentPlayTime());
            this.mPlayingState = 0;
            this.mRunning = true;
            if (this.mListeners != null) {
                ArrayList arrayList = (ArrayList) this.mListeners.clone();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ((Animator.AnimatorListener) arrayList.get(i)).onAnimationStart(this);
                }
            }
        }
        AnimationHandler animationHandler = sAnimationHandler.get();
        if (animationHandler == null) {
            animationHandler = new AnimationHandler();
            sAnimationHandler.set(animationHandler);
        }
        animationHandler.sendEmptyMessage(0);
    }

    @Override // com.nineoldandroids.animation.Animator
    public void start() {
        start(false);
    }

    @Override // com.nineoldandroids.animation.Animator
    public void cancel() {
        if (this.mPlayingState != 0 || sPendingAnimations.get().contains(this) || sDelayedAnims.get().contains(this)) {
            if (this.mRunning && this.mListeners != null) {
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((Animator.AnimatorListener) it.next()).onAnimationCancel(this);
                }
            }
            endAnimation();
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    public void end() {
        if (!sAnimations.get().contains(this) && !sPendingAnimations.get().contains(this)) {
            this.mStartedDelay = false;
            startAnimation();
        } else if (!this.mInitialized) {
            initAnimation();
        }
        int i = this.mRepeatCount;
        if (i > 0 && (i & 1) == 1) {
            animateValue(0.0f);
        } else {
            animateValue(1.0f);
        }
        endAnimation();
    }

    @Override // com.nineoldandroids.animation.Animator
    public boolean isRunning() {
        return this.mPlayingState == 1 || this.mRunning;
    }

    @Override // com.nineoldandroids.animation.Animator
    public boolean isStarted() {
        return this.mStarted;
    }

    public void reverse() {
        this.mPlayingBackwards = !this.mPlayingBackwards;
        if (this.mPlayingState == 1) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = currentAnimationTimeMillis - (this.mDuration - (currentAnimationTimeMillis - this.mStartTime));
            return;
        }
        start(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endAnimation() {
        sAnimations.get().remove(this);
        sPendingAnimations.get().remove(this);
        sDelayedAnims.get().remove(this);
        this.mPlayingState = 0;
        if (this.mRunning && this.mListeners != null) {
            ArrayList arrayList = (ArrayList) this.mListeners.clone();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((Animator.AnimatorListener) arrayList.get(i)).onAnimationEnd(this);
            }
        }
        this.mRunning = false;
        this.mStarted = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation() {
        initAnimation();
        sAnimations.get().add(this);
        if (this.mStartDelay <= 0 || this.mListeners == null) {
            return;
        }
        ArrayList arrayList = (ArrayList) this.mListeners.clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((Animator.AnimatorListener) arrayList.get(i)).onAnimationStart(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean delayedAnimationFrame(long j) {
        if (!this.mStartedDelay) {
            this.mStartedDelay = true;
            this.mDelayStartTime = j;
            return false;
        }
        long j2 = j - this.mDelayStartTime;
        long j3 = this.mStartDelay;
        if (j2 <= j3) {
            return false;
        }
        this.mStartTime = j - (j2 - j3);
        this.mPlayingState = 1;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean animationFrame(long r10) {
        /*
            r9 = this;
            int r0 = r9.mPlayingState
            r1 = 0
            r3 = 1
            if (r0 != 0) goto L1a
            r9.mPlayingState = r3
            long r4 = r9.mSeekTime
            int r0 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r0 >= 0) goto L12
            r9.mStartTime = r10
            goto L1a
        L12:
            long r4 = r10 - r4
            r9.mStartTime = r4
            r4 = -1
            r9.mSeekTime = r4
        L1a:
            int r0 = r9.mPlayingState
            r4 = 2
            r5 = 0
            if (r0 == r3) goto L23
            if (r0 == r4) goto L23
            goto L84
        L23:
            long r6 = r9.mDuration
            r0 = 1065353216(0x3f800000, float:1.0)
            int r8 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r8 <= 0) goto L32
            long r1 = r9.mStartTime
            long r10 = r10 - r1
            float r10 = (float) r10
            float r11 = (float) r6
            float r10 = r10 / r11
            goto L34
        L32:
            r10 = 1065353216(0x3f800000, float:1.0)
        L34:
            int r11 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r11 < 0) goto L79
            int r11 = r9.mCurrentIteration
            int r1 = r9.mRepeatCount
            if (r11 < r1) goto L47
            r11 = -1
            if (r1 != r11) goto L42
            goto L47
        L42:
            float r10 = java.lang.Math.min(r10, r0)
            goto L7a
        L47:
            java.util.ArrayList<com.nineoldandroids.animation.Animator$AnimatorListener> r11 = r9.mListeners
            if (r11 == 0) goto L62
            java.util.ArrayList<com.nineoldandroids.animation.Animator$AnimatorListener> r11 = r9.mListeners
            int r11 = r11.size()
            r1 = 0
        L52:
            if (r1 >= r11) goto L62
            java.util.ArrayList<com.nineoldandroids.animation.Animator$AnimatorListener> r2 = r9.mListeners
            java.lang.Object r2 = r2.get(r1)
            com.nineoldandroids.animation.Animator$AnimatorListener r2 = (com.nineoldandroids.animation.Animator.AnimatorListener) r2
            r2.onAnimationRepeat(r9)
            int r1 = r1 + 1
            goto L52
        L62:
            int r11 = r9.mRepeatMode
            if (r11 != r4) goto L6b
            boolean r11 = r9.mPlayingBackwards
            r11 = r11 ^ r3
            r9.mPlayingBackwards = r11
        L6b:
            int r11 = r9.mCurrentIteration
            int r1 = (int) r10
            int r11 = r11 + r1
            r9.mCurrentIteration = r11
            float r10 = r10 % r0
            long r1 = r9.mStartTime
            long r3 = r9.mDuration
            long r1 = r1 + r3
            r9.mStartTime = r1
        L79:
            r3 = 0
        L7a:
            boolean r11 = r9.mPlayingBackwards
            if (r11 == 0) goto L80
            float r10 = r0 - r10
        L80:
            r9.animateValue(r10)
            r5 = r3
        L84:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nineoldandroids.animation.ValueAnimator.animationFrame(long):boolean");
    }

    public float getAnimatedFraction() {
        return this.mCurrentFraction;
    }

    void animateValue(float f) {
        float interpolation = this.mInterpolator.getInterpolation(f);
        this.mCurrentFraction = interpolation;
        int length = this.mValues.length;
        for (int i = 0; i < length; i++) {
            this.mValues[i].calculateValue(interpolation);
        }
        ArrayList<AnimatorUpdateListener> arrayList = this.mUpdateListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mUpdateListeners.get(i2).onAnimationUpdate(this);
            }
        }
    }

    @Override // com.nineoldandroids.animation.Animator
    /* renamed from: clone */
    public ValueAnimator mo10clone() {
        ValueAnimator valueAnimator = (ValueAnimator) super.mo10clone();
        ArrayList<AnimatorUpdateListener> arrayList = this.mUpdateListeners;
        if (arrayList != null) {
            valueAnimator.mUpdateListeners = new ArrayList<>();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                valueAnimator.mUpdateListeners.add(arrayList.get(i));
            }
        }
        valueAnimator.mSeekTime = -1L;
        valueAnimator.mPlayingBackwards = false;
        valueAnimator.mCurrentIteration = 0;
        valueAnimator.mInitialized = false;
        valueAnimator.mPlayingState = 0;
        valueAnimator.mStartedDelay = false;
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr != null) {
            int length = propertyValuesHolderArr.length;
            valueAnimator.mValues = new PropertyValuesHolder[length];
            valueAnimator.mValuesMap = new HashMap<>(length);
            for (int i2 = 0; i2 < length; i2++) {
                PropertyValuesHolder mo14clone = propertyValuesHolderArr[i2].mo14clone();
                valueAnimator.mValues[i2] = mo14clone;
                valueAnimator.mValuesMap.put(mo14clone.getPropertyName(), mo14clone);
            }
        }
        return valueAnimator;
    }

    public static int getCurrentAnimationsCount() {
        return sAnimations.get().size();
    }

    public static void clearAllAnimations() {
        sAnimations.get().clear();
        sPendingAnimations.get().clear();
        sDelayedAnims.get().clear();
    }

    public String toString() {
        String str = "ValueAnimator@" + Integer.toHexString(hashCode());
        if (this.mValues != null) {
            for (int i = 0; i < this.mValues.length; i++) {
                str = str + "\n    " + this.mValues[i].toString();
            }
        }
        return str;
    }
}
