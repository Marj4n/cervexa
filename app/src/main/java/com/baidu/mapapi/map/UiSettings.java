package com.baidu.mapapi.map;

import com.baidu.platform.comapi.map.C0074e;

/* loaded from: classes.dex */
public final class UiSettings {
    private C0074e a;

    UiSettings(C0074e c0074e) {
        this.a = c0074e;
    }

    public boolean isCompassEnabled() {
        return this.a.q();
    }

    public boolean isOverlookingGesturesEnabled() {
        return this.a.y();
    }

    public boolean isRotateGesturesEnabled() {
        return this.a.x();
    }

    public boolean isScrollGesturesEnabled() {
        return this.a.v();
    }

    public boolean isZoomGesturesEnabled() {
        return this.a.w();
    }

    public void setAllGesturesEnabled(boolean z) {
        setRotateGesturesEnabled(z);
        setScrollGesturesEnabled(z);
        setOverlookingGesturesEnabled(z);
        setZoomGesturesEnabled(z);
    }

    public void setCompassEnabled(boolean z) {
        this.a.h(z);
    }

    public void setOverlookingGesturesEnabled(boolean z) {
        this.a.p(z);
    }

    public void setRotateGesturesEnabled(boolean z) {
        this.a.o(z);
    }

    public void setScrollGesturesEnabled(boolean z) {
        this.a.m(z);
    }

    public void setZoomGesturesEnabled(boolean z) {
        this.a.n(z);
    }
}
