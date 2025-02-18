package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

/* loaded from: classes.dex */
public final class GroundOverlayOptions extends OverlayOptions {
    int a;
    Bundle c;
    private BitmapDescriptor d;
    private LatLng e;
    private int f;
    private int g;
    private LatLngBounds j;
    private float h = 0.5f;
    private float i = 0.5f;
    private float k = 1.0f;
    boolean b = true;

    @Override // com.baidu.mapapi.map.OverlayOptions
    Overlay a() {
        LatLngBounds latLngBounds;
        int i;
        LatLng latLng;
        GroundOverlay groundOverlay = new GroundOverlay();
        groundOverlay.r = this.b;
        groundOverlay.q = this.a;
        groundOverlay.s = this.c;
        BitmapDescriptor bitmapDescriptor = this.d;
        if (bitmapDescriptor == null) {
            throw new IllegalStateException("when you add ground overlay, you must set the image");
        }
        groundOverlay.b = bitmapDescriptor;
        if (this.j != null || (latLng = this.e) == null) {
            if (this.e != null || (latLngBounds = this.j) == null) {
                throw new IllegalStateException("when you add ground overlay, you must set one of position or bounds");
            }
            groundOverlay.h = latLngBounds;
            i = 1;
        } else {
            if (this.f <= 0 || this.g <= 0) {
                throw new IllegalArgumentException("when you add ground overlay, the width and height must greater than 0");
            }
            groundOverlay.c = latLng;
            groundOverlay.f = this.h;
            groundOverlay.g = this.i;
            groundOverlay.d = this.f;
            groundOverlay.e = this.g;
            i = 2;
        }
        groundOverlay.a = i;
        groundOverlay.i = this.k;
        return groundOverlay;
    }

    public GroundOverlayOptions anchor(float f, float f2) {
        if (f >= 0.0f && f <= 1.0f && f2 >= 0.0f && f2 <= 1.0f) {
            this.h = f;
            this.i = f2;
        }
        return this;
    }

    public GroundOverlayOptions dimensions(int i) {
        this.f = i;
        this.g = Integer.MAX_VALUE;
        return this;
    }

    public GroundOverlayOptions dimensions(int i, int i2) {
        this.f = i;
        this.g = i2;
        return this;
    }

    public GroundOverlayOptions extraInfo(Bundle bundle) {
        this.c = bundle;
        return this;
    }

    public float getAnchorX() {
        return this.h;
    }

    public float getAnchorY() {
        return this.i;
    }

    public LatLngBounds getBounds() {
        return this.j;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public int getHeight() {
        int i = this.g;
        return i == Integer.MAX_VALUE ? (int) ((this.f * this.d.a.getHeight()) / this.d.a.getWidth()) : i;
    }

    public BitmapDescriptor getImage() {
        return this.d;
    }

    public LatLng getPosition() {
        return this.e;
    }

    public float getTransparency() {
        return this.k;
    }

    public int getWidth() {
        return this.f;
    }

    public int getZIndex() {
        return this.a;
    }

    public GroundOverlayOptions image(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor == null) {
            throw new IllegalArgumentException("image can not be null");
        }
        this.d = bitmapDescriptor;
        return this;
    }

    public boolean isVisible() {
        return this.b;
    }

    public GroundOverlayOptions position(LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("position can not be null");
        }
        this.e = latLng;
        return this;
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        if (latLngBounds == null) {
            throw new IllegalArgumentException("bounds can not be null");
        }
        this.j = latLngBounds;
        return this;
    }

    public GroundOverlayOptions transparency(float f) {
        if (f <= 1.0f && f >= 0.0f) {
            this.k = f;
        }
        return this;
    }

    public GroundOverlayOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public GroundOverlayOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
