package com.baidu.mapapi.map;

import android.os.Bundle;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.baidu.mapapi.model.LatLng;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class PolylineOptions extends OverlayOptions {
    int a;
    Bundle d;
    private List<LatLng> f;
    private List<Integer> g;
    private List<Integer> h;
    private BitmapDescriptor j;
    private List<BitmapDescriptor> k;
    private int e = ViewCompat.MEASURED_STATE_MASK;
    private int i = 5;
    private boolean l = true;
    private boolean m = false;
    boolean b = true;
    boolean c = false;

    @Override // com.baidu.mapapi.map.OverlayOptions
    Overlay a() {
        Polyline polyline = new Polyline();
        polyline.r = this.b;
        polyline.f = this.c;
        polyline.q = this.a;
        polyline.s = this.d;
        List<LatLng> list = this.f;
        if (list == null || list.size() < 2) {
            throw new IllegalStateException("when you add polyline, you must at least supply 2 points");
        }
        polyline.b = this.f;
        polyline.a = this.e;
        polyline.e = this.i;
        polyline.i = this.j;
        polyline.j = this.k;
        polyline.g = this.l;
        polyline.h = this.m;
        List<Integer> list2 = this.g;
        if (list2 != null && list2.size() < this.f.size() - 1) {
            ArrayList arrayList = new ArrayList((this.f.size() - 1) - this.g.size());
            List<Integer> list3 = this.g;
            list3.addAll(list3.size(), arrayList);
        }
        List<Integer> list4 = this.g;
        int i = 0;
        if (list4 != null && list4.size() > 0) {
            int[] iArr = new int[this.g.size()];
            Iterator<Integer> it = this.g.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                iArr[i2] = it.next().intValue();
                i2++;
            }
            polyline.c = iArr;
        }
        List<Integer> list5 = this.h;
        if (list5 != null && list5.size() < this.f.size() - 1) {
            ArrayList arrayList2 = new ArrayList((this.f.size() - 1) - this.h.size());
            List<Integer> list6 = this.h;
            list6.addAll(list6.size(), arrayList2);
        }
        List<Integer> list7 = this.h;
        if (list7 != null && list7.size() > 0) {
            int[] iArr2 = new int[this.h.size()];
            Iterator<Integer> it2 = this.h.iterator();
            while (it2.hasNext()) {
                iArr2[i] = it2.next().intValue();
                i++;
            }
            polyline.d = iArr2;
        }
        return polyline;
    }

    public PolylineOptions color(int i) {
        this.e = i;
        return this;
    }

    public PolylineOptions colorsValues(List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("colors list can not be null");
        }
        if (list.contains(null)) {
            throw new IllegalArgumentException("colors list can not contains null");
        }
        this.h = list;
        return this;
    }

    public PolylineOptions customTexture(BitmapDescriptor bitmapDescriptor) {
        this.j = bitmapDescriptor;
        return this;
    }

    public PolylineOptions customTextureList(List<BitmapDescriptor> list) {
        if (list == null) {
            throw new IllegalArgumentException("customTexture list can not be null");
        }
        if (list.size() == 0) {
            Log.e("baidumapsdk", "custom texture list is empty,the texture will not work");
        }
        Iterator<BitmapDescriptor> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                Log.e("baidumapsdk", "the custom texture item is null,it will be discard");
            }
        }
        this.k = list;
        return this;
    }

    public PolylineOptions dottedLine(boolean z) {
        this.c = z;
        return this;
    }

    public PolylineOptions extraInfo(Bundle bundle) {
        this.d = bundle;
        return this;
    }

    public PolylineOptions focus(boolean z) {
        this.l = z;
        return this;
    }

    public int getColor() {
        return this.e;
    }

    public BitmapDescriptor getCustomTexture() {
        return this.j;
    }

    public List<BitmapDescriptor> getCustomTextureList() {
        return this.k;
    }

    public Bundle getExtraInfo() {
        return this.d;
    }

    public List<LatLng> getPoints() {
        return this.f;
    }

    public List<Integer> getTextureIndexs() {
        return this.g;
    }

    public int getWidth() {
        return this.i;
    }

    public int getZIndex() {
        return this.a;
    }

    public boolean isDottedLine() {
        return this.c;
    }

    public boolean isFocus() {
        return this.l;
    }

    public boolean isVisible() {
        return this.b;
    }

    public PolylineOptions keepScale(boolean z) {
        this.m = z;
        return this;
    }

    public PolylineOptions points(List<LatLng> list) {
        if (list == null) {
            throw new IllegalArgumentException("points list can not be null");
        }
        if (list.size() < 2) {
            throw new IllegalArgumentException("points count can not less than 2");
        }
        if (list.contains(null)) {
            throw new IllegalArgumentException("points list can not contains null");
        }
        this.f = list;
        return this;
    }

    public PolylineOptions textureIndex(List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("indexs list can not be null");
        }
        if (list.contains(null)) {
            throw new IllegalArgumentException("index list can not contains null");
        }
        this.g = list;
        return this;
    }

    public PolylineOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public PolylineOptions width(int i) {
        if (i > 0) {
            this.i = i;
        }
        return this;
    }

    public PolylineOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
