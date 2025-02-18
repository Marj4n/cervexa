package com.baidu.mapapi.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/* loaded from: classes.dex */
class x extends AnimatorListenerAdapter {
    final /* synthetic */ View a;
    final /* synthetic */ WearMapView b;

    x(WearMapView wearMapView, View view) {
        this.b = wearMapView;
        this.a = view;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.a.setVisibility(4);
        super.onAnimationEnd(animator);
    }
}
