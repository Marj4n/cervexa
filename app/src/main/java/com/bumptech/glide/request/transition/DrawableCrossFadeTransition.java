package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.request.transition.Transition;

/* loaded from: classes.dex */
public class DrawableCrossFadeTransition implements Transition<Drawable> {
    private final Transition<Drawable> defaultAnimation;
    private final int duration;
    private final boolean isCrossFadeEnabled;

    public DrawableCrossFadeTransition(Transition<Drawable> transition, int i, boolean z) {
        this.defaultAnimation = transition;
        this.duration = i;
        this.isCrossFadeEnabled = z;
    }

    @Override // com.bumptech.glide.request.transition.Transition
    public boolean transition(Drawable drawable, Transition.ViewAdapter viewAdapter) {
        Drawable currentDrawable = viewAdapter.getCurrentDrawable();
        if (currentDrawable != null) {
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{currentDrawable, drawable});
            transitionDrawable.setCrossFadeEnabled(this.isCrossFadeEnabled);
            transitionDrawable.startTransition(this.duration);
            viewAdapter.setDrawable(transitionDrawable);
            return true;
        }
        this.defaultAnimation.transition(drawable, viewAdapter);
        return false;
    }
}
