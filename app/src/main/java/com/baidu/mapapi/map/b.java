package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0074e;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.InterfaceC0081l;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
class b implements InterfaceC0081l {
    final /* synthetic */ BaiduMap a;

    b(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a() {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(Bitmap bitmap) {
        BaiduMap.SnapshotReadyCallback snapshotReadyCallback;
        BaiduMap.SnapshotReadyCallback snapshotReadyCallback2;
        snapshotReadyCallback = this.a.z;
        if (snapshotReadyCallback != null) {
            snapshotReadyCallback2 = this.a.z;
            snapshotReadyCallback2.onSnapshotReady(bitmap);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(MotionEvent motionEvent) {
        BaiduMap.OnMapTouchListener onMapTouchListener;
        BaiduMap.OnMapTouchListener onMapTouchListener2;
        onMapTouchListener = this.a.p;
        if (onMapTouchListener != null) {
            onMapTouchListener2 = this.a.p;
            onMapTouchListener2.onTouch(motionEvent);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(GeoPoint geoPoint) {
        BaiduMap.OnMapClickListener onMapClickListener;
        BaiduMap.OnMapClickListener onMapClickListener2;
        onMapClickListener = this.a.q;
        if (onMapClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapClickListener2 = this.a.q;
            onMapClickListener2.onMapClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(E e) {
        View view;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        View view2;
        view = this.a.I;
        if (view != null) {
            view2 = this.a.I;
            view2.setVisibility(4);
        }
        onMapStatusChangeListener = this.a.o;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(e);
            onMapStatusChangeListener2 = this.a.o;
            onMapStatusChangeListener2.onMapStatusChangeStart(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(String str) {
        C0074e c0074e;
        List<Overlay> list;
        CopyOnWriteArrayList copyOnWriteArrayList;
        CopyOnWriteArrayList copyOnWriteArrayList2;
        InfoWindow infoWindow;
        List<Overlay> list2;
        CopyOnWriteArrayList copyOnWriteArrayList3;
        CopyOnWriteArrayList copyOnWriteArrayList4;
        Marker marker;
        InfoWindow infoWindow2;
        C0074e c0074e2;
        C0074e c0074e3;
        C0074e c0074e4;
        BaiduMap.OnMyLocationClickListener onMyLocationClickListener;
        BaiduMap.OnMyLocationClickListener onMyLocationClickListener2;
        BaiduMap.OnMapClickListener onMapClickListener;
        BaiduMap.OnMapClickListener onMapClickListener2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject optJSONObject = jSONObject.optJSONArray("dataset").optJSONObject(0);
            c0074e = this.a.i;
            GeoPoint b = c0074e.b(jSONObject.optInt("px"), jSONObject.optInt("py"));
            int optInt = optJSONObject.optInt("ty");
            if (optInt == 17) {
                onMapClickListener = this.a.q;
                if (onMapClickListener != null) {
                    MapPoi mapPoi = new MapPoi();
                    mapPoi.a(optJSONObject);
                    onMapClickListener2 = this.a.q;
                    onMapClickListener2.onMapPoiClick(mapPoi);
                    return;
                }
                return;
            }
            if (optInt != 18) {
                if (optInt == 19) {
                    c0074e2 = this.a.i;
                    if (c0074e2 != null) {
                        c0074e3 = this.a.i;
                        E D = c0074e3.D();
                        D.c = 0;
                        D.b = 0;
                        c0074e4 = this.a.i;
                        c0074e4.a(D, IjkMediaCodecInfo.RANK_SECURE);
                        return;
                    }
                    return;
                }
                if (optInt != 90909) {
                    if (optInt == 90910) {
                        String optString = optJSONObject.optString("polyline_id");
                        list = this.a.k;
                        for (Overlay overlay : list) {
                            if ((overlay instanceof Polyline) && overlay.p.equals(optString)) {
                                copyOnWriteArrayList = this.a.w;
                                if (copyOnWriteArrayList.isEmpty()) {
                                    a(b);
                                } else {
                                    copyOnWriteArrayList2 = this.a.w;
                                    Iterator it = copyOnWriteArrayList2.iterator();
                                    while (it.hasNext()) {
                                        ((BaiduMap.OnPolylineClickListener) it.next()).onPolylineClick((Polyline) overlay);
                                    }
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                String optString2 = optJSONObject.optString("marker_id");
                infoWindow = this.a.G;
                if (infoWindow != null) {
                    marker = this.a.H;
                    if (optString2.equals(marker.p)) {
                        infoWindow2 = this.a.G;
                        InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = infoWindow2.d;
                        if (onInfoWindowClickListener != null) {
                            onInfoWindowClickListener.onInfoWindowClick();
                            return;
                        }
                    }
                }
                list2 = this.a.k;
                for (Overlay overlay2 : list2) {
                    if ((overlay2 instanceof Marker) && overlay2.p.equals(optString2)) {
                        copyOnWriteArrayList3 = this.a.v;
                        if (!copyOnWriteArrayList3.isEmpty()) {
                            copyOnWriteArrayList4 = this.a.v;
                            Iterator it2 = copyOnWriteArrayList4.iterator();
                            while (it2.hasNext()) {
                                ((BaiduMap.OnMarkerClickListener) it2.next()).onMarkerClick((Marker) overlay2);
                            }
                            return;
                        }
                        a(b);
                    }
                }
                return;
            }
            onMyLocationClickListener = this.a.y;
            if (onMyLocationClickListener != null) {
                onMyLocationClickListener2 = this.a.y;
                onMyLocationClickListener2.onMyLocationClick();
                return;
            }
            a(b);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(GL10 gl10, E e) {
        BaiduMap.OnMapDrawFrameCallback onMapDrawFrameCallback;
        BaiduMap.OnMapDrawFrameCallback onMapDrawFrameCallback2;
        onMapDrawFrameCallback = this.a.A;
        if (onMapDrawFrameCallback != null) {
            MapStatus a = MapStatus.a(e);
            onMapDrawFrameCallback2 = this.a.A;
            onMapDrawFrameCallback2.onMapDrawFrame(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(boolean z) {
        BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener;
        BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener2;
        onBaseIndoorMapListener = this.a.B;
        if (onBaseIndoorMapListener != null) {
            MapBaseIndoorMapInfo focusedBaseIndoorMapInfo = this.a.getFocusedBaseIndoorMapInfo();
            onBaseIndoorMapListener2 = this.a.B;
            onBaseIndoorMapListener2.onBaseIndoorMapMode(z, focusedBaseIndoorMapInfo);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b() {
        C0074e c0074e;
        BaiduMap.OnMapLoadedCallback onMapLoadedCallback;
        BaiduMap.OnMapLoadedCallback onMapLoadedCallback2;
        BaiduMap baiduMap = this.a;
        c0074e = baiduMap.i;
        baiduMap.f = new Projection(c0074e);
        this.a.P = true;
        onMapLoadedCallback = this.a.r;
        if (onMapLoadedCallback != null) {
            onMapLoadedCallback2 = this.a.r;
            onMapLoadedCallback2.onMapLoaded();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b(GeoPoint geoPoint) {
        BaiduMap.OnMapDoubleClickListener onMapDoubleClickListener;
        BaiduMap.OnMapDoubleClickListener onMapDoubleClickListener2;
        onMapDoubleClickListener = this.a.t;
        if (onMapDoubleClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapDoubleClickListener2 = this.a.t;
            onMapDoubleClickListener2.onMapDoubleClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b(E e) {
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        onMapStatusChangeListener = this.a.o;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(e);
            onMapStatusChangeListener2 = this.a.o;
            onMapStatusChangeListener2.onMapStatusChange(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public boolean b(String str) {
        Marker marker;
        List<Overlay> list;
        Projection projection;
        Marker marker2;
        Projection projection2;
        Marker marker3;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker4;
        Marker marker5;
        try {
            JSONObject optJSONObject = new JSONObject(str).optJSONArray("dataset").optJSONObject(0);
            if (optJSONObject.optInt("ty") != 90909) {
                return false;
            }
            String optString = optJSONObject.optString("marker_id");
            marker = this.a.H;
            if (marker != null) {
                marker5 = this.a.H;
                if (optString.equals(marker5.p)) {
                    return false;
                }
            }
            list = this.a.k;
            for (Overlay overlay : list) {
                if ((overlay instanceof Marker) && overlay.p.equals(optString)) {
                    Marker marker6 = (Marker) overlay;
                    if (!marker6.f) {
                        return false;
                    }
                    this.a.J = marker6;
                    projection = this.a.f;
                    marker2 = this.a.J;
                    Point point = new Point(projection.toScreenLocation(marker2.a).x, r5.y - 60);
                    projection2 = this.a.f;
                    LatLng fromScreenLocation = projection2.fromScreenLocation(point);
                    marker3 = this.a.J;
                    marker3.setPosition(fromScreenLocation);
                    onMarkerDragListener = this.a.x;
                    if (onMarkerDragListener != null) {
                        onMarkerDragListener2 = this.a.x;
                        marker4 = this.a.J;
                        onMarkerDragListener2.onMarkerDragStart(marker4);
                    }
                    return true;
                }
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c() {
        BaiduMap.OnMapRenderCallback onMapRenderCallback;
        BaiduMap.OnMapRenderCallback onMapRenderCallback2;
        onMapRenderCallback = this.a.s;
        if (onMapRenderCallback != null) {
            onMapRenderCallback2 = this.a.s;
            onMapRenderCallback2.onMapRenderFinished();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c(GeoPoint geoPoint) {
        BaiduMap.OnMapLongClickListener onMapLongClickListener;
        BaiduMap.OnMapLongClickListener onMapLongClickListener2;
        onMapLongClickListener = this.a.u;
        if (onMapLongClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapLongClickListener2 = this.a.u;
            onMapLongClickListener2.onMapLongClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c(E e) {
        View view;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        View view2;
        view = this.a.I;
        if (view != null) {
            view2 = this.a.I;
            view2.setVisibility(0);
        }
        onMapStatusChangeListener = this.a.o;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(e);
            onMapStatusChangeListener2 = this.a.o;
            onMapStatusChangeListener2.onMapStatusChangeFinish(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void d() {
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        HeatMap heatMap2;
        lock = this.a.E;
        lock.lock();
        try {
            heatMap = this.a.D;
            if (heatMap != null) {
                heatMap2 = this.a.D;
                heatMap2.a();
            }
        } finally {
            lock2 = this.a.E;
            lock2.unlock();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void d(GeoPoint geoPoint) {
        Marker marker;
        Marker marker2;
        Projection projection;
        Projection projection2;
        Marker marker3;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        Marker marker4;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker5;
        marker = this.a.J;
        if (marker != null) {
            marker2 = this.a.J;
            if (marker2.f) {
                LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
                projection = this.a.f;
                Point point = new Point(projection.toScreenLocation(mc2ll).x, r3.y - 60);
                projection2 = this.a.f;
                LatLng fromScreenLocation = projection2.fromScreenLocation(point);
                marker3 = this.a.J;
                marker3.setPosition(fromScreenLocation);
                onMarkerDragListener = this.a.x;
                if (onMarkerDragListener != null) {
                    marker4 = this.a.J;
                    if (marker4.f) {
                        onMarkerDragListener2 = this.a.x;
                        marker5 = this.a.J;
                        onMarkerDragListener2.onMarkerDrag(marker5);
                    }
                }
            }
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void e() {
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        HeatMap heatMap2;
        C0074e c0074e;
        lock = this.a.E;
        lock.lock();
        try {
            heatMap = this.a.D;
            if (heatMap != null) {
                heatMap2 = this.a.D;
                heatMap2.a();
                c0074e = this.a.i;
                c0074e.n();
            }
        } finally {
            lock2 = this.a.E;
            lock2.unlock();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void e(GeoPoint geoPoint) {
        Marker marker;
        Marker marker2;
        Projection projection;
        Projection projection2;
        Marker marker3;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        Marker marker4;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker5;
        marker = this.a.J;
        if (marker != null) {
            marker2 = this.a.J;
            if (marker2.f) {
                LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
                projection = this.a.f;
                Point point = new Point(projection.toScreenLocation(mc2ll).x, r3.y - 60);
                projection2 = this.a.f;
                LatLng fromScreenLocation = projection2.fromScreenLocation(point);
                marker3 = this.a.J;
                marker3.setPosition(fromScreenLocation);
                onMarkerDragListener = this.a.x;
                if (onMarkerDragListener != null) {
                    marker4 = this.a.J;
                    if (marker4.f) {
                        onMarkerDragListener2 = this.a.x;
                        marker5 = this.a.J;
                        onMarkerDragListener2.onMarkerDragEnd(marker5);
                    }
                }
                this.a.J = null;
            }
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void f() {
        C0074e c0074e;
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        HeatMap heatMap2;
        c0074e = this.a.i;
        c0074e.b(false);
        lock = this.a.E;
        lock.lock();
        try {
            heatMap = this.a.D;
            if (heatMap != null) {
                BaiduMap baiduMap = this.a;
                heatMap2 = this.a.D;
                baiduMap.a(heatMap2);
            }
        } finally {
            lock2 = this.a.E;
            lock2.unlock();
        }
    }
}
