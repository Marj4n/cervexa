package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.map.C0074e;
import com.baidu.platform.comapi.map.D;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.EnumC0077h;
import com.baidu.platform.comapi.map.F;
import com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.opengles.GL10;
import org.apache.commons.net.nntp.NNTPReply;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
public class BaiduMap {
    public static final int MAP_TYPE_NONE = 3;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    private static final String e = BaiduMap.class.getSimpleName();
    private OnMapDrawFrameCallback A;
    private OnBaseIndoorMapListener B;
    private TileOverlay C;
    private HeatMap D;
    private Lock E;
    private Lock F;
    private InfoWindow G;
    private Marker H;
    private View I;
    private Marker J;
    private MyLocationData K;
    private MyLocationConfiguration L;
    private boolean M;
    private boolean N;
    private boolean O;
    private boolean P;
    private Point Q;
    MapView a;
    TextureMapView b;
    WearMapView c;
    D d;
    private Projection f;
    private UiSettings g;
    private GestureDetectorOnDoubleTapListenerC0079j h;
    private C0074e i;
    private F j;
    private List<Overlay> k;
    private List<Marker> l;
    private List<Marker> m;
    private Overlay.a n;
    private OnMapStatusChangeListener o;
    private OnMapTouchListener p;
    private OnMapClickListener q;
    private OnMapLoadedCallback r;
    private OnMapRenderCallback s;
    private OnMapDoubleClickListener t;
    private OnMapLongClickListener u;
    private CopyOnWriteArrayList<OnMarkerClickListener> v;
    private CopyOnWriteArrayList<OnPolylineClickListener> w;
    private OnMarkerDragListener x;
    private OnMyLocationClickListener y;
    private SnapshotReadyCallback z;

    /* renamed from: com.baidu.mapapi.map.BaiduMap$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[D.values().length];
            b = iArr;
            try {
                iArr[D.TextureView.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[D.GLSurfaceView.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[MyLocationConfiguration.LocationMode.values().length];
            a = iArr2;
            try {
                iArr2[MyLocationConfiguration.LocationMode.COMPASS.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[MyLocationConfiguration.LocationMode.FOLLOWING.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[MyLocationConfiguration.LocationMode.NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public interface OnBaseIndoorMapListener {
        void onBaseIndoorMapMode(boolean z, MapBaseIndoorMapInfo mapBaseIndoorMapInfo);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);

        boolean onMapPoiClick(MapPoi mapPoi);
    }

    public interface OnMapDoubleClickListener {
        void onMapDoubleClick(LatLng latLng);
    }

    public interface OnMapDrawFrameCallback {
        void onMapDrawFrame(MapStatus mapStatus);

        @Deprecated
        void onMapDrawFrame(GL10 gl10, MapStatus mapStatus);
    }

    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMapRenderCallback {
        void onMapRenderFinished();
    }

    public interface OnMapStatusChangeListener {
        void onMapStatusChange(MapStatus mapStatus);

        void onMapStatusChangeFinish(MapStatus mapStatus);

        void onMapStatusChangeStart(MapStatus mapStatus);
    }

    public interface OnMapTouchListener {
        void onTouch(MotionEvent motionEvent);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationClickListener {
        boolean onMyLocationClick();
    }

    public interface OnPolylineClickListener {
        boolean onPolylineClick(Polyline polyline);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    BaiduMap(F f) {
        this.v = new CopyOnWriteArrayList<>();
        this.w = new CopyOnWriteArrayList<>();
        this.E = new ReentrantLock();
        this.F = new ReentrantLock();
        this.j = f;
        this.i = f.b();
        this.d = D.TextureView;
        c();
    }

    BaiduMap(GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j) {
        this.v = new CopyOnWriteArrayList<>();
        this.w = new CopyOnWriteArrayList<>();
        this.E = new ReentrantLock();
        this.F = new ReentrantLock();
        this.h = gestureDetectorOnDoubleTapListenerC0079j;
        this.i = gestureDetectorOnDoubleTapListenerC0079j.a();
        this.d = D.GLSurfaceView;
        c();
    }

    private Point a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int i = 0;
        int i2 = 0;
        for (String str2 : str.replaceAll("^\\{", "").replaceAll("\\}$", "").split(",")) {
            String[] split = str2.replaceAll("\"", "").split(":");
            if ("x".equals(split[0])) {
                i = Integer.valueOf(split[1]).intValue();
            }
            if ("y".equals(split[0])) {
                i2 = Integer.valueOf(split[1]).intValue();
            }
        }
        return new Point(i, i2);
    }

    private E a(MapStatusUpdate mapStatusUpdate) {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return null;
        }
        return mapStatusUpdate.a(this.i, getMapStatus()).b(c0074e.D());
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0103  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void a(com.baidu.mapapi.map.MyLocationData r21, com.baidu.mapapi.map.MyLocationConfiguration r22) {
        /*
            Method dump skipped, instructions count: 561
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.BaiduMap.a(com.baidu.mapapi.map.MyLocationData, com.baidu.mapapi.map.MyLocationConfiguration):void");
    }

    private void c() {
        this.k = new CopyOnWriteArrayList();
        this.l = new CopyOnWriteArrayList();
        this.m = new CopyOnWriteArrayList();
        this.Q = new Point((int) (SysOSUtil.getDensity() * 40.0f), (int) (SysOSUtil.getDensity() * 40.0f));
        this.g = new UiSettings(this.i);
        this.n = new a(this);
        this.i.a(new b(this));
        this.i.a(new c(this));
        this.i.a(new d(this));
        this.M = this.i.B();
        this.N = this.i.C();
    }

    void a() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return;
        }
        c0074e.s();
    }

    void a(HeatMap heatMap) {
        this.E.lock();
        try {
            if (this.D != null && this.i != null && heatMap == this.D) {
                this.D.b();
                this.D.c();
                this.D.a = null;
                this.i.n();
                this.D = null;
                this.i.l(false);
            }
        } finally {
            this.E.unlock();
        }
    }

    void a(TileOverlay tileOverlay) {
        this.F.lock();
        if (tileOverlay != null) {
            try {
                if (this.C == tileOverlay) {
                    tileOverlay.b();
                    tileOverlay.a = null;
                    if (this.i != null) {
                        this.i.c(false);
                    }
                }
            } finally {
                this.C = null;
                this.F.unlock();
            }
        }
    }

    public void addHeatMap(HeatMap heatMap) {
        if (heatMap == null) {
            return;
        }
        this.E.lock();
        try {
            if (heatMap == this.D) {
                return;
            }
            if (this.D != null) {
                this.D.b();
                this.D.c();
                this.D.a = null;
                this.i.n();
            }
            this.D = heatMap;
            heatMap.a = this;
            this.i.l(true);
        } finally {
            this.E.unlock();
        }
    }

    public final Overlay addOverlay(OverlayOptions overlayOptions) {
        if (overlayOptions == null) {
            return null;
        }
        Overlay a = overlayOptions.a();
        a.listener = this.n;
        if (a instanceof Marker) {
            Marker marker = (Marker) a;
            if (marker.n != null && marker.n.size() != 0) {
                this.l.add(marker);
                C0074e c0074e = this.i;
                if (c0074e != null) {
                    c0074e.b(true);
                }
            }
            this.m.add(marker);
        }
        Bundle bundle = new Bundle();
        a.a(bundle);
        C0074e c0074e2 = this.i;
        if (c0074e2 != null) {
            c0074e2.b(bundle);
        }
        this.k.add(a);
        return a;
    }

    public final List<Overlay> addOverlays(List<OverlayOptions> list) {
        int i;
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        Bundle[] bundleArr = new Bundle[size];
        int i2 = 0;
        for (OverlayOptions overlayOptions : list) {
            if (overlayOptions != null) {
                Bundle bundle = new Bundle();
                Overlay a = overlayOptions.a();
                a.listener = this.n;
                if (a instanceof Marker) {
                    Marker marker = (Marker) a;
                    if (marker.n != null && marker.n.size() != 0) {
                        this.l.add(marker);
                        C0074e c0074e = this.i;
                        if (c0074e != null) {
                            c0074e.b(true);
                        }
                    }
                    this.m.add(marker);
                }
                this.k.add(a);
                arrayList.add(a);
                a.a(bundle);
                bundleArr[i2] = bundle;
                i2++;
            }
        }
        int i3 = size / NNTPReply.SERVICE_DISCONTINUED;
        for (int i4 = 0; i4 < i3 + 1; i4++) {
            ArrayList arrayList2 = new ArrayList();
            for (int i5 = 0; i5 < 400 && (i = (i4 * NNTPReply.SERVICE_DISCONTINUED) + i5) < size; i5++) {
                if (bundleArr[i] != null) {
                    arrayList2.add(bundleArr[i]);
                }
            }
            C0074e c0074e2 = this.i;
            if (c0074e2 != null) {
                c0074e2.a(arrayList2);
            }
        }
        return arrayList;
    }

    public TileOverlay addTileLayer(TileOverlayOptions tileOverlayOptions) {
        if (tileOverlayOptions == null) {
            return null;
        }
        TileOverlay tileOverlay = this.C;
        if (tileOverlay != null) {
            tileOverlay.b();
            this.C.a = null;
        }
        C0074e c0074e = this.i;
        if (c0074e == null || !c0074e.a(tileOverlayOptions.a())) {
            return null;
        }
        TileOverlay a = tileOverlayOptions.a(this);
        this.C = a;
        return a;
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate) {
        animateMapStatus(mapStatusUpdate, IjkMediaCodecInfo.RANK_SECURE);
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate, int i) {
        if (mapStatusUpdate == null || i <= 0) {
            return;
        }
        E a = a(mapStatusUpdate);
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return;
        }
        if (this.P) {
            c0074e.a(a, i);
        } else {
            c0074e.a(a);
        }
    }

    boolean b() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.d();
    }

    public final void clear() {
        this.k.clear();
        this.l.clear();
        this.m.clear();
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.b(false);
            this.i.m();
        }
        hideInfoWindow();
    }

    public final Point getCompassPosition() {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            return a(c0074e.g());
        }
        return null;
    }

    public MapBaseIndoorMapInfo getFocusedBaseIndoorMapInfo() {
        return this.i.o();
    }

    public final MyLocationConfiguration getLocationConfigeration() {
        return this.L;
    }

    public final MyLocationData getLocationData() {
        return this.K;
    }

    public final MapStatus getMapStatus() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return null;
        }
        return MapStatus.a(c0074e.D());
    }

    public final LatLngBounds getMapStatusLimit() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return null;
        }
        return c0074e.E();
    }

    public final int getMapType() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return 1;
        }
        if (c0074e.k()) {
            return this.i.j() ? 2 : 1;
        }
        return 3;
    }

    public List<Marker> getMarkersInBounds(LatLngBounds latLngBounds) {
        if (getMapStatus() == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (this.m.size() == 0) {
            return null;
        }
        for (Marker marker : this.m) {
            if (latLngBounds.contains(marker.getPosition())) {
                arrayList.add(marker);
            }
        }
        return arrayList;
    }

    public final float getMaxZoomLevel() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return 0.0f;
        }
        return c0074e.a;
    }

    public final float getMinZoomLevel() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return 0.0f;
        }
        return c0074e.b;
    }

    public final Projection getProjection() {
        return this.f;
    }

    public final UiSettings getUiSettings() {
        return this.g;
    }

    public void hideInfoWindow() {
        InfoWindow infoWindow = this.G;
        if (infoWindow != null) {
            if (infoWindow.b != null) {
                int i = AnonymousClass1.b[this.d.ordinal()];
                if (i == 1) {
                    TextureMapView textureMapView = this.b;
                    if (textureMapView != null) {
                        textureMapView.removeView(this.I);
                    }
                } else if (i == 2 && this.h != null) {
                    this.a.removeView(this.I);
                }
                this.I = null;
            }
            this.G = null;
            this.H.remove();
            this.H = null;
        }
    }

    public final boolean isBaiduHeatMapEnabled() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.h();
    }

    public boolean isBaseIndoorMapMode() {
        return this.i.p();
    }

    public final boolean isBuildingsEnabled() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.l();
    }

    public final boolean isMyLocationEnabled() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.r();
    }

    public final boolean isSupportBaiduHeatMap() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.i();
    }

    public final boolean isTrafficEnabled() {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return false;
        }
        return c0074e.f();
    }

    public final void removeMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (this.v.contains(onMarkerClickListener)) {
            this.v.remove(onMarkerClickListener);
        }
    }

    public final void setBaiduHeatMapEnabled(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.e(z);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.g(z);
        }
    }

    public void setCompassIcon(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("compass's icon can not be null");
        }
        this.i.a(bitmap);
    }

    public void setCompassPosition(Point point) {
        if (this.i.a(point)) {
            this.Q = point;
        }
    }

    public final void setIndoorEnable(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            this.O = z;
            c0074e.i(z);
        }
        OnBaseIndoorMapListener onBaseIndoorMapListener = this.B;
        if (onBaseIndoorMapListener == null || z) {
            return;
        }
        onBaseIndoorMapListener.onBaseIndoorMapMode(false, null);
    }

    public final void setMapStatus(MapStatusUpdate mapStatusUpdate) {
        if (mapStatusUpdate == null) {
            return;
        }
        E a = a(mapStatusUpdate);
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return;
        }
        c0074e.a(a);
        OnMapStatusChangeListener onMapStatusChangeListener = this.o;
        if (onMapStatusChangeListener != null) {
            onMapStatusChangeListener.onMapStatusChange(getMapStatus());
        }
    }

    public final void setMapStatusLimits(LatLngBounds latLngBounds) {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return;
        }
        c0074e.a(latLngBounds);
        setMapStatus(MapStatusUpdateFactory.newLatLngBounds(latLngBounds));
    }

    public final void setMapType(int i) {
        C0074e c0074e = this.i;
        if (c0074e == null) {
            return;
        }
        if (i == 1) {
            c0074e.a(false);
            this.i.q(this.M);
            this.i.r(this.N);
            this.i.d(true);
            this.i.i(this.O);
        } else if (i == 2) {
            c0074e.a(true);
            this.i.q(this.M);
            this.i.r(this.N);
            this.i.d(true);
        } else if (i == 3) {
            if (c0074e.B()) {
                this.i.q(false);
            }
            if (this.i.C()) {
                this.i.r(false);
            }
            this.i.d(false);
            this.i.i(false);
        }
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.h;
        if (gestureDetectorOnDoubleTapListenerC0079j != null) {
            gestureDetectorOnDoubleTapListenerC0079j.a(i);
        }
    }

    public final void setMaxAndMinZoomLevel(float f, float f2) {
        C0074e c0074e;
        if (f <= 21.0f && f2 >= 3.0f && f >= f2 && (c0074e = this.i) != null) {
            c0074e.a(f, f2);
        }
    }

    public final void setMyLocationConfigeration(MyLocationConfiguration myLocationConfiguration) {
        this.L = myLocationConfiguration;
        a(this.K, myLocationConfiguration);
    }

    public final void setMyLocationData(MyLocationData myLocationData) {
        this.K = myLocationData;
        if (this.L == null) {
            this.L = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null);
        }
        a(myLocationData, this.L);
    }

    public final void setMyLocationEnabled(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.k(z);
        }
    }

    public final void setOnBaseIndoorMapListener(OnBaseIndoorMapListener onBaseIndoorMapListener) {
        this.B = onBaseIndoorMapListener;
    }

    public final void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        this.q = onMapClickListener;
    }

    public final void setOnMapDoubleClickListener(OnMapDoubleClickListener onMapDoubleClickListener) {
        this.t = onMapDoubleClickListener;
    }

    public final void setOnMapDrawFrameCallback(OnMapDrawFrameCallback onMapDrawFrameCallback) {
        this.A = onMapDrawFrameCallback;
    }

    public void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        this.r = onMapLoadedCallback;
    }

    public final void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        this.u = onMapLongClickListener;
    }

    public void setOnMapRenderCallbadk(OnMapRenderCallback onMapRenderCallback) {
        this.s = onMapRenderCallback;
    }

    public final void setOnMapStatusChangeListener(OnMapStatusChangeListener onMapStatusChangeListener) {
        this.o = onMapStatusChangeListener;
    }

    public final void setOnMapTouchListener(OnMapTouchListener onMapTouchListener) {
        this.p = onMapTouchListener;
    }

    public final void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener == null || this.v.contains(onMarkerClickListener)) {
            return;
        }
        this.v.add(onMarkerClickListener);
    }

    public final void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.x = onMarkerDragListener;
    }

    public final void setOnMyLocationClickListener(OnMyLocationClickListener onMyLocationClickListener) {
        this.y = onMyLocationClickListener;
    }

    public final void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        if (onPolylineClickListener != null) {
            this.w.add(onPolylineClickListener);
        }
    }

    @Deprecated
    public final void setPadding(int i, int i2, int i3, int i4) {
        C0074e c0074e;
        MapView mapView;
        if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0 || (c0074e = this.i) == null) {
            return;
        }
        c0074e.D();
        int i5 = AnonymousClass1.b[this.d.ordinal()];
        if (i5 != 1) {
            if (i5 == 2 && (mapView = this.a) != null) {
                MapStatusUpdate newMapStatus = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().targetScreen(new Point(((this.a.getWidth() + i) - i3) / 2, ((this.a.getHeight() + i2) - i4) / 2)).build());
                this.i.a(new Point((int) (i + (this.Q.x * (((mapView.getWidth() - i) - i3) / this.a.getWidth()))), (int) (i2 + (this.Q.y * (((this.a.getHeight() - i2) - i4) / this.a.getHeight())))));
                setMapStatus(newMapStatus);
                this.a.setPadding(i, i2, i3, i4);
                this.a.invalidate();
                return;
            }
            return;
        }
        if (this.b == null) {
            return;
        }
        MapStatusUpdate newMapStatus2 = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().targetScreen(new Point(((this.b.getWidth() + i) - i3) / 2, ((this.b.getHeight() + i2) - i4) / 2)).build());
        this.i.a(new Point((int) (i + (this.Q.x * (((r0.getWidth() - i) - i3) / this.b.getWidth()))), (int) (i2 + (this.Q.y * (((this.b.getHeight() - i2) - i4) / this.b.getHeight())))));
        setMapStatus(newMapStatus2);
        this.b.setPadding(i, i2, i3, i4);
        this.b.invalidate();
    }

    public final void setTrafficEnabled(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.f(z);
        }
    }

    public final void setViewPadding(int i, int i2, int i3, int i4) {
        MapView mapView;
        if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0 || this.i == null) {
            return;
        }
        int i5 = AnonymousClass1.b[this.d.ordinal()];
        if (i5 != 1) {
            if (i5 == 2 && (mapView = this.a) != null) {
                this.i.a(new Point((int) (i + (this.Q.x * (((mapView.getWidth() - i) - i3) / this.a.getWidth()))), (int) (i2 + (this.Q.y * (((this.a.getHeight() - i2) - i4) / this.a.getHeight())))));
                this.a.setPadding(i, i2, i3, i4);
                this.a.invalidate();
                return;
            }
            return;
        }
        if (this.b == null) {
            return;
        }
        this.i.a(new Point((int) (i + (this.Q.x * (((r0.getWidth() - i) - i3) / this.b.getWidth()))), (int) (i2 + (this.Q.y * (((this.b.getHeight() - i2) - i4) / this.b.getHeight())))));
        this.b.setPadding(i, i2, i3, i4);
        this.b.invalidate();
    }

    public void showInfoWindow(InfoWindow infoWindow) {
        if (infoWindow != null) {
            hideInfoWindow();
            if (infoWindow.b != null) {
                View view = infoWindow.b;
                this.I = view;
                view.destroyDrawingCache();
                MapViewLayoutParams build = new MapViewLayoutParams.Builder().layoutMode(MapViewLayoutParams.ELayoutMode.mapMode).position(infoWindow.c).yOffset(infoWindow.e).build();
                int i = AnonymousClass1.b[this.d.ordinal()];
                if (i == 1) {
                    TextureMapView textureMapView = this.b;
                    if (textureMapView != null) {
                        textureMapView.addView(this.I, build);
                    }
                } else if (i == 2 && this.h != null) {
                    this.a.addView(this.I, build);
                }
            }
            this.G = infoWindow;
            Overlay a = new MarkerOptions().perspective(false).icon(infoWindow.b != null ? BitmapDescriptorFactory.fromView(infoWindow.b) : infoWindow.a).position(infoWindow.c).zIndex(Integer.MAX_VALUE).a(infoWindow.e).a();
            a.listener = this.n;
            a.type = EnumC0077h.popup;
            Bundle bundle = new Bundle();
            a.a(bundle);
            C0074e c0074e = this.i;
            if (c0074e != null) {
                c0074e.b(bundle);
            }
            this.k.add(a);
            this.H = (Marker) a;
        }
    }

    public final void showMapIndoorPoi(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.r(z);
            this.N = z;
        }
    }

    public final void showMapPoi(boolean z) {
        C0074e c0074e = this.i;
        if (c0074e != null) {
            c0074e.q(z);
            this.M = z;
        }
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j;
        this.z = snapshotReadyCallback;
        int i = AnonymousClass1.b[this.d.ordinal()];
        if (i != 1) {
            if (i == 2 && (gestureDetectorOnDoubleTapListenerC0079j = this.h) != null) {
                gestureDetectorOnDoubleTapListenerC0079j.a("anything", null);
                return;
            }
            return;
        }
        F f = this.j;
        if (f != null) {
            f.a("anything", null);
        }
    }

    public final void snapshotScope(Rect rect, SnapshotReadyCallback snapshotReadyCallback) {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j;
        this.z = snapshotReadyCallback;
        int i = AnonymousClass1.b[this.d.ordinal()];
        if (i != 1) {
            if (i == 2 && (gestureDetectorOnDoubleTapListenerC0079j = this.h) != null) {
                gestureDetectorOnDoubleTapListenerC0079j.a("anything", rect);
                return;
            }
            return;
        }
        F f = this.j;
        if (f != null) {
            f.a("anything", rect);
        }
    }

    public MapBaseIndoorMapInfo.SwitchFloorError switchBaseIndoorMapFloor(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return MapBaseIndoorMapInfo.SwitchFloorError.FLOOR_INFO_ERROR;
        }
        MapBaseIndoorMapInfo focusedBaseIndoorMapInfo = getFocusedBaseIndoorMapInfo();
        if (!str2.equals(focusedBaseIndoorMapInfo.a)) {
            return MapBaseIndoorMapInfo.SwitchFloorError.FOCUSED_ID_ERROR;
        }
        ArrayList<String> floors = focusedBaseIndoorMapInfo.getFloors();
        return (floors == null || !floors.contains(str)) ? MapBaseIndoorMapInfo.SwitchFloorError.FLOOR_OVERLFLOW : this.i.a(str, str2) ? MapBaseIndoorMapInfo.SwitchFloorError.SWITCH_OK : MapBaseIndoorMapInfo.SwitchFloorError.SWITCH_ERROR;
    }
}
