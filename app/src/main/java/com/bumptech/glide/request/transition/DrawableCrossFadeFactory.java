package com.bumptech.glide.request.transition;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.ViewTransition;

/* loaded from: classes.dex */
public class DrawableCrossFadeFactory implements TransitionFactory<Drawable> {
    private final int duration;
    private DrawableCrossFadeTransition firstResourceTransition;
    private final boolean isCrossFadeEnabled;
    private DrawableCrossFadeTransition secondResourceTransition;
    private final ViewAnimationFactory<Drawable> viewAnimationFactory;

    protected DrawableCrossFadeFactory(ViewAnimationFactory<Drawable> viewAnimationFactory, int i, boolean z) {
        this.viewAnimationFactory = viewAnimationFactory;
        this.duration = i;
        this.isCrossFadeEnabled = z;
    }

    @Override // com.bumptech.glide.request.transition.TransitionFactory
    public Transition<Drawable> build(DataSource dataSource, boolean z) {
        if (dataSource == DataSource.MEMORY_CACHE) {
            return NoTransition.get();
        }
        if (z) {
            return getFirstResourceTransition(dataSource);
        }
        return getSecondResourceTransition(dataSource);
    }

    private Transition<Drawable> getFirstResourceTransition(DataSource dataSource) {
        if (this.firstResourceTransition == null) {
            this.firstResourceTransition = buildTransition(dataSource, true);
        }
        return this.firstResourceTransition;
    }

    private Transition<Drawable> getSecondResourceTransition(DataSource dataSource) {
        if (this.secondResourceTransition == null) {
            this.secondResourceTransition = buildTransition(dataSource, false);
        }
        return this.secondResourceTransition;
    }

    private DrawableCrossFadeTransition buildTransition(DataSource dataSource, boolean z) {
        return new DrawableCrossFadeTransition(this.viewAnimationFactory.build(dataSource, z), this.duration, this.isCrossFadeEnabled);
    }

    private static final class DefaultViewTransitionAnimationFactory implements ViewTransition.ViewTransitionAnimationFactory {
        private final int durationMillis;

        DefaultViewTransitionAnimationFactory(int i) {
            this.durationMillis = i;
        }

        @Override // com.bumptech.glide.request.transition.ViewTransition.ViewTransitionAnimationFactory
        public Animation build(Context context) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(this.durationMillis);
            return alphaAnimation;
        }
    }

    public static class Builder {
        private static final int DEFAULT_DURATION_MS = 300;
        private int durationMillis;
        private ViewAnimationFactory<Drawable> factory;
        private boolean isCrossFadeEnabled;

        public Builder() {
            this(300);
        }

        public Builder(int i) {
            this.durationMillis = i;
            this.factory = new ViewAnimationFactory<>(new DefaultViewTransitionAnimationFactory(i));
        }

        public Builder setCrossFadeEnabled(boolean z) {
            this.isCrossFadeEnabled = z;
            return this;
        }

        public Builder setDefaultAnimationId(int i) {
            return setDefaultAnimationFactory(new ViewAnimationFactory<>(i));
        }

        public Builder setDefaultAnimation(Animation animation) {
            return setDefaultAnimationFactory(new ViewAnimationFactory<>(animation));
        }

        public Builder setDefaultAnimationFactory(ViewAnimationFactory<Drawable> viewAnimationFactory) {
            this.factory = viewAnimationFactory;
            return this;
        }

        public DrawableCrossFadeFactory build() {
            return new DrawableCrossFadeFactory(this.factory, this.durationMillis, this.isCrossFadeEnabled);
        }
    }
}
