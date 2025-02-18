package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.EnumC0077h;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Overlay {
    protected a listener;
    String p = System.currentTimeMillis() + "_" + hashCode();
    int q;
    boolean r;
    Bundle s;
    public EnumC0077h type;

    interface a {
        void a(Overlay overlay);

        void b(Overlay overlay);
    }

    protected Overlay() {
    }

    static void a(int i, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putFloat("red", ((i >> 16) & 255) / 255.0f);
        bundle2.putFloat("green", ((i >> 8) & 255) / 255.0f);
        bundle2.putFloat("blue", (i & 255) / 255.0f);
        bundle2.putFloat("alpha", (i >>> 24) / 255.0f);
        bundle.putBundle("color", bundle2);
    }

    static void a(List<LatLng> list, Bundle bundle) {
        int size = list.size();
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        for (int i = 0; i < size; i++) {
            GeoPoint ll2mc = CoordUtil.ll2mc(list.get(i));
            dArr[i] = ll2mc.getLongitudeE6();
            dArr2[i] = ll2mc.getLatitudeE6();
        }
        bundle.putDoubleArray("x_array", dArr);
        bundle.putDoubleArray("y_array", dArr2);
    }

    Bundle a() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.p);
        bundle.putInt("type", this.type.ordinal());
        return bundle;
    }

    Bundle a(Bundle bundle) {
        bundle.putString("id", this.p);
        bundle.putInt("type", this.type.ordinal());
        bundle.putInt("visibility", this.r ? 1 : 0);
        bundle.putInt("z_index", this.q);
        return bundle;
    }

    public Bundle getExtraInfo() {
        return this.s;
    }

    public int getZIndex() {
        return this.q;
    }

    public boolean isVisible() {
        return this.r;
    }

    public void remove() {
        this.listener.a(this);
    }

    public void setExtraInfo(Bundle bundle) {
        this.s = bundle;
    }

    public void setVisible(boolean z) {
        this.r = z;
        this.listener.b(this);
    }

    public void setZIndex(int i) {
        this.q = i;
        this.listener.b(this);
    }
}
