package com.baidu.mapapi.map;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.EnumC0077h;
import vi.com.gdi.bgl.android.java.EnvDrawText;

/* loaded from: classes.dex */
public final class Text extends Overlay {
    private static final String k = Text.class.getSimpleName();
    String a;
    LatLng b;
    int c;
    int d;
    int e;
    Typeface f;
    int g;
    int h;
    float i;
    int j;

    Text() {
        this.type = EnumC0077h.text;
    }

    @Override // com.baidu.mapapi.map.Overlay
    Bundle a() {
        Typeface typeface = this.f;
        if (typeface != null) {
            EnvDrawText.removeFontCache(typeface.hashCode());
        }
        return super.a();
    }

    @Override // com.baidu.mapapi.map.Overlay
    Bundle a(Bundle bundle) {
        super.a(bundle);
        if (this.b == null) {
            throw new IllegalStateException("when you add a text overlay, you must provide text and the position info.");
        }
        bundle.putString("text", this.a);
        GeoPoint ll2mc = CoordUtil.ll2mc(this.b);
        bundle.putDouble("location_x", ll2mc.getLongitudeE6());
        bundle.putDouble("location_y", ll2mc.getLatitudeE6());
        int i = this.d;
        bundle.putInt("font_color", Color.argb(i >>> 24, i & 255, (i >> 8) & 255, (i >> 16) & 255));
        int i2 = this.c;
        bundle.putInt("bg_color", Color.argb(i2 >>> 24, i2 & 255, (i2 >> 8) & 255, (i2 >> 16) & 255));
        bundle.putInt("font_size", this.e);
        Typeface typeface = this.f;
        if (typeface != null) {
            EnvDrawText.registFontCache(typeface.hashCode(), this.f);
            bundle.putInt("type_face", this.f.hashCode());
        }
        int i3 = this.g;
        float f = 1.0f;
        bundle.putFloat("align_x", i3 != 1 ? i3 != 2 ? 0.5f : 1.0f : 0.0f);
        int i4 = this.h;
        if (i4 == 8) {
            f = 0.0f;
        } else if (i4 != 16) {
            f = 0.5f;
        }
        bundle.putFloat("align_y", f);
        bundle.putFloat("rotate", this.i);
        bundle.putInt("update", this.j);
        return bundle;
    }

    public float getAlignX() {
        return this.g;
    }

    public float getAlignY() {
        return this.h;
    }

    public int getBgColor() {
        return this.c;
    }

    public int getFontColor() {
        return this.d;
    }

    public int getFontSize() {
        return this.e;
    }

    public LatLng getPosition() {
        return this.b;
    }

    public float getRotate() {
        return this.i;
    }

    public String getText() {
        return this.a;
    }

    public Typeface getTypeface() {
        return this.f;
    }

    public void setAlign(int i, int i2) {
        this.g = i;
        this.h = i2;
        this.j = 1;
        this.listener.b(this);
    }

    public void setBgColor(int i) {
        this.c = i;
        this.j = 1;
        this.listener.b(this);
    }

    public void setFontColor(int i) {
        this.d = i;
        this.j = 1;
        this.listener.b(this);
    }

    public void setFontSize(int i) {
        this.e = i;
        this.j = 1;
        this.listener.b(this);
    }

    public void setPosition(LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("position can not be null");
        }
        this.b = latLng;
        this.j = 1;
        this.listener.b(this);
    }

    public void setRotate(float f) {
        this.i = f;
        this.j = 1;
        this.listener.b(this);
    }

    public void setText(String str) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("text can not be null or empty");
        }
        this.a = str;
        this.j = 1;
        this.listener.b(this);
    }

    public void setTypeface(Typeface typeface) {
        this.f = typeface;
        this.j = 1;
        this.listener.b(this);
    }
}
