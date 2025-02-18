package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.map.Overlay;
import com.baidu.platform.comapi.map.C0074e;
import java.util.List;

/* loaded from: classes.dex */
class a implements Overlay.a {
    final /* synthetic */ BaiduMap a;

    a(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.mapapi.map.Overlay.a
    public void a(Overlay overlay) {
        List list;
        List list2;
        List list3;
        C0074e c0074e;
        C0074e c0074e2;
        List list4;
        List list5;
        List list6;
        C0074e c0074e3;
        List list7;
        C0074e c0074e4;
        if (overlay != null) {
            list6 = this.a.k;
            if (list6.contains(overlay)) {
                Bundle a = overlay.a();
                c0074e3 = this.a.i;
                if (c0074e3 != null) {
                    c0074e4 = this.a.i;
                    c0074e4.d(a);
                }
                list7 = this.a.k;
                list7.remove(overlay);
            }
        }
        if (overlay != null) {
            list4 = this.a.m;
            if (list4.contains(overlay)) {
                list5 = this.a.m;
                list5.remove(overlay);
            }
        }
        if (overlay != null) {
            list = this.a.l;
            if (list.contains(overlay)) {
                Marker marker = (Marker) overlay;
                if (marker.n != null) {
                    list2 = this.a.l;
                    list2.remove(marker);
                    list3 = this.a.l;
                    if (list3.size() == 0) {
                        c0074e = this.a.i;
                        if (c0074e != null) {
                            c0074e2 = this.a.i;
                            c0074e2.b(false);
                        }
                    }
                }
            }
        }
    }

    @Override // com.baidu.mapapi.map.Overlay.a
    public void b(Overlay overlay) {
        List list;
        List list2;
        List list3;
        List list4;
        C0074e c0074e;
        C0074e c0074e2;
        List list5;
        List list6;
        C0074e c0074e3;
        C0074e c0074e4;
        List list7;
        if (overlay != null) {
            list4 = this.a.k;
            if (list4.contains(overlay)) {
                if (overlay instanceof Marker) {
                    Marker marker = (Marker) overlay;
                    if (marker.n != null && marker.n.size() != 0) {
                        list5 = this.a.l;
                        if (list5.contains(marker)) {
                            list7 = this.a.l;
                            list7.remove(marker);
                        }
                        list6 = this.a.l;
                        list6.add(marker);
                        c0074e3 = this.a.i;
                        if (c0074e3 != null) {
                            c0074e4 = this.a.i;
                            c0074e4.b(true);
                        }
                    }
                }
                Bundle bundle = new Bundle();
                c0074e = this.a.i;
                if (c0074e != null) {
                    c0074e2 = this.a.i;
                    c0074e2.c(overlay.a(bundle));
                }
            }
        }
        list = this.a.m;
        if (list.contains(overlay)) {
            list3 = this.a.m;
            list3.remove(overlay);
        }
        if (overlay instanceof Marker) {
            list2 = this.a.m;
            list2.add((Marker) overlay);
        }
    }
}
