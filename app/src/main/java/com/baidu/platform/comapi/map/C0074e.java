package com.baidu.platform.comapi.map;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.baidu.mapapi.UIMsg;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.ParcelItem;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j;
import com.baidu.platform.comjni.map.basemap.BaseMapCallback;
import com.baidu.platform.comjni.map.basemap.JNIBaseMap;
import com.github.mikephil.charting.utils.Utils;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.TopicKey;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.baidu.platform.comapi.map.e, reason: case insensitive filesystem */
/* loaded from: classes.dex */
public class C0074e implements com.baidu.platform.comjni.map.basemap.b {
    private static int N;
    private static int O;
    private static List<JNIBaseMap> ai;
    private Context A;
    private List<AbstractC0073d> B;
    private A C;
    private C0076g D;
    private o E;
    private H F;
    private K G;
    private s H;
    private C0083n I;
    private p J;
    private C0070a K;
    private q L;
    private I M;
    private int P;
    private int Q;
    private int R;
    private VelocityTracker T;
    private long U;
    private long V;
    private long W;
    private long X;
    private int Y;
    private float Z;
    private float aa;
    private boolean ab;
    private long ac;
    private long ad;
    private C0075f ae;
    private String af;
    private C0071b ag;
    private C0072c ah;
    com.baidu.platform.comjni.map.basemap.a g;
    long h;
    boolean i;
    public int j;
    boolean l;
    boolean m;
    boolean n;
    private boolean p;
    private boolean q;
    private M y;
    private L z;
    private static final String o = GestureDetectorOnDoubleTapListenerC0079j.class.getSimpleName();
    static long k = 0;
    public float a = 22.0f;
    public float b = 3.0f;
    public float c = 22.0f;
    private boolean r = true;
    private boolean s = false;
    private boolean t = false;
    private boolean u = false;
    private boolean v = true;
    boolean d = true;
    boolean e = true;
    private boolean w = true;
    private boolean x = false;
    private GestureDetectorOnDoubleTapListenerC0079j.a S = new GestureDetectorOnDoubleTapListenerC0079j.a();
    List<InterfaceC0081l> f = new ArrayList();

    public C0074e(Context context, String str) {
        this.A = context;
        this.af = str;
    }

    private void N() {
        if (!this.t && !this.q && !this.p && !this.u) {
            this.a = this.c;
            return;
        }
        if (this.a > 20.0f) {
            this.a = 20.0f;
        }
        if (D().a > 20.0f) {
            E D = D();
            D.a = 20.0f;
            a(D);
        }
    }

    private Activity a(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return a(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    private void a(String str, String str2, long j) {
        try {
            Class<?> cls = Class.forName(str);
            cls.getMethod(str2, Long.TYPE).invoke(cls.newInstance(), Long.valueOf(j));
        } catch (Exception unused) {
        }
    }

    private boolean e(Bundle bundle) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return false;
        }
        return aVar.e(bundle);
    }

    private boolean f(Bundle bundle) {
        com.baidu.platform.comjni.map.basemap.a aVar;
        if (bundle == null || (aVar = this.g) == null) {
            return false;
        }
        boolean d = aVar.d(bundle);
        if (d) {
            c(d);
            this.g.b(this.y.a);
        }
        return d;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x005a, code lost:
    
        r0 = r4.H.a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0058, code lost:
    
        if (r0 == com.baidu.platform.comapi.map.EnumC0077h.popup.ordinal()) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x002e, code lost:
    
        if (r0 == com.baidu.platform.comapi.map.EnumC0077h.popup.ordinal()) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x005f, code lost:
    
        r0 = r4.G.a;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void g(android.os.Bundle r5) {
        /*
            r4 = this;
            java.lang.String r0 = "param"
            java.lang.Object r1 = r5.get(r0)
            java.lang.String r2 = "type"
            java.lang.String r3 = "layer_addr"
            if (r1 == 0) goto L31
            java.lang.Object r5 = r5.get(r0)
            android.os.Bundle r5 = (android.os.Bundle) r5
            int r0 = r5.getInt(r2)
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.ground
            int r1 = r1.ordinal()
            if (r0 != r1) goto L1f
            goto L3d
        L1f:
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.arc
            int r1 = r1.ordinal()
            if (r0 < r1) goto L28
            goto L4d
        L28:
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.popup
            int r1 = r1.ordinal()
            if (r0 != r1) goto L5f
            goto L5a
        L31:
            int r0 = r5.getInt(r2)
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.ground
            int r1 = r1.ordinal()
            if (r0 != r1) goto L45
        L3d:
            com.baidu.platform.comapi.map.o r0 = r4.E
            long r0 = r0.a
        L41:
            r5.putLong(r3, r0)
            goto L64
        L45:
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.arc
            int r1 = r1.ordinal()
            if (r0 < r1) goto L52
        L4d:
            com.baidu.platform.comapi.map.n r0 = r4.I
            long r0 = r0.a
            goto L41
        L52:
            com.baidu.platform.comapi.map.h r1 = com.baidu.platform.comapi.map.EnumC0077h.popup
            int r1 = r1.ordinal()
            if (r0 != r1) goto L5f
        L5a:
            com.baidu.platform.comapi.map.s r0 = r4.H
            long r0 = r0.a
            goto L41
        L5f:
            com.baidu.platform.comapi.map.K r0 = r4.G
            long r0 = r0.a
            goto L41
        L64:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.C0074e.g(android.os.Bundle):void");
    }

    public static void j(boolean z) {
        List<JNIBaseMap> d = com.baidu.platform.comjni.map.basemap.a.d();
        ai = d;
        if (d == null || d.size() == 0) {
            com.baidu.platform.comjni.map.basemap.a.b(0L, z);
            return;
        }
        com.baidu.platform.comjni.map.basemap.a.b(ai.get(0).a, z);
        for (JNIBaseMap jNIBaseMap : ai) {
            jNIBaseMap.ClearLayer(jNIBaseMap.a, -1L);
        }
    }

    void A() {
        this.m = false;
        this.l = false;
        Iterator<InterfaceC0081l> it = this.f.iterator();
        while (it.hasNext()) {
            it.next().c(D());
        }
    }

    public boolean B() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            return aVar.a(this.F.a);
        }
        return false;
    }

    public boolean C() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            return aVar.a(this.ah.a);
        }
        return false;
    }

    public E D() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return null;
        }
        Bundle j = aVar.j();
        E e = new E();
        e.a(j);
        return e;
    }

    public LatLngBounds E() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return null;
        }
        Bundle k2 = aVar.k();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(CoordUtil.mc2ll(new GeoPoint(k2.getInt("minCoory"), k2.getInt("maxCoorx")))).include(CoordUtil.mc2ll(new GeoPoint(k2.getInt("maxCoory"), k2.getInt("minCoorx"))));
        return builder.build();
    }

    public int F() {
        return this.P;
    }

    public int G() {
        return this.Q;
    }

    public E H() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return null;
        }
        Bundle l = aVar.l();
        E e = new E();
        e.a(l);
        return e;
    }

    public double I() {
        return D().m;
    }

    void J() {
        if (this.l) {
            return;
        }
        this.l = true;
        this.m = false;
        Iterator<InterfaceC0081l> it = this.f.iterator();
        while (it.hasNext()) {
            it.next().a(D());
        }
    }

    void K() {
        this.l = false;
        if (this.m) {
            return;
        }
        Iterator<InterfaceC0081l> it = this.f.iterator();
        while (it.hasNext()) {
            it.next().c(D());
        }
    }

    void L() {
        this.R = 0;
        this.S.e = false;
        this.S.h = Utils.DOUBLE_EPSILON;
    }

    void M() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            aVar.b();
            this.g = null;
        }
    }

    public float a(int i, int i2, int i3, int i4, int i5, int i6) {
        if (!this.i) {
            return 12.0f;
        }
        if (this.g == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(TopicKey.LEFT, i);
        bundle.putInt("right", i3);
        bundle.putInt("bottom", i4);
        bundle.putInt("top", i2);
        bundle.putInt("hasHW", 1);
        bundle.putInt("width", i5);
        bundle.putInt("height", i6);
        return this.g.c(bundle);
    }

    int a(int i, int i2, int i3) {
        return com.baidu.platform.comjni.map.basemap.a.a(this.h, i, i2, i3);
    }

    @Override // com.baidu.platform.comjni.map.basemap.b
    public int a(Bundle bundle, long j, int i, Bundle bundle2) {
        if (j == this.D.a) {
            bundle.putString("jsondata", this.D.a());
            bundle.putBundle(Constants.JSON_PARAM, this.D.b());
            return this.D.g;
        }
        if (j == this.C.a) {
            bundle.putString("jsondata", this.C.a());
            bundle.putBundle(Constants.JSON_PARAM, this.C.b());
            return this.C.g;
        }
        if (j == this.J.a) {
            bundle.putBundle(Constants.JSON_PARAM, this.L.a(bundle2.getInt("x"), bundle2.getInt("y"), bundle2.getInt("zoom")));
            return this.J.g;
        }
        if (j != this.y.a) {
            return 0;
        }
        bundle.putBundle(Constants.JSON_PARAM, this.z.a(bundle2.getInt("x"), bundle2.getInt("y"), bundle2.getInt("zoom"), this.A));
        return this.y.g;
    }

    public Point a(GeoPoint geoPoint) {
        return this.M.a(geoPoint);
    }

    void a() {
        com.baidu.platform.comjni.map.basemap.a aVar = new com.baidu.platform.comjni.map.basemap.a();
        this.g = aVar;
        aVar.a();
        long c = this.g.c();
        this.h = c;
        a("com.baidu.platform.comapi.wnplatform.walkmap.WNaviBaiduMap", "setId", c);
        this.j = SysOSUtil.getDensityDpi() < 180 ? 18 : SysOSUtil.getDensityDpi() < 240 ? 25 : SysOSUtil.getDensityDpi() < 320 ? 37 : 50;
        String moduleFileName = SysOSUtil.getModuleFileName();
        String appSDCardPath = EnvironmentUtilities.getAppSDCardPath();
        String appCachePath = EnvironmentUtilities.getAppCachePath();
        String appSecondCachePath = EnvironmentUtilities.getAppSecondCachePath();
        int mapTmpStgMax = EnvironmentUtilities.getMapTmpStgMax();
        int domTmpStgMax = EnvironmentUtilities.getDomTmpStgMax();
        int itsTmpStgMax = EnvironmentUtilities.getItsTmpStgMax();
        String str = SysOSUtil.getDensityDpi() >= 180 ? "/h/" : "/l/";
        String str2 = moduleFileName + "/cfg";
        String str3 = appSDCardPath + "/vmp";
        String str4 = str2 + str;
        String str5 = str2 + "/a/";
        String str6 = str2 + "/idrres/";
        String str7 = str3 + str;
        String str8 = str3 + str;
        String str9 = appCachePath + "/tmp/";
        String str10 = appSecondCachePath + "/tmp/";
        Activity a = a(this.A);
        if (a == null) {
            throw new RuntimeException("Please give the right context.");
        }
        Display defaultDisplay = a.getWindowManager().getDefaultDisplay();
        this.g.a(str4, str7, str9, str10, str8, str5, this.af, str6, defaultDisplay.getWidth(), defaultDisplay.getHeight(), SysOSUtil.getDensityDpi(), mapTmpStgMax, domTmpStgMax, itsTmpStgMax, 0);
        this.g.f();
    }

    public void a(float f, float f2) {
        this.a = f;
        this.c = f;
        this.b = f2;
    }

    void a(int i, int i2) {
        this.P = i;
        this.Q = i2;
    }

    public void a(Bitmap bitmap) {
        Bundle bundle;
        if (this.g == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put("type", 0);
            jSONObject2.put("x", N);
            jSONObject2.put("y", O);
            jSONObject2.put("hidetime", 1000);
            jSONArray.put(jSONObject2);
            jSONObject.put("data", jSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            bundle = null;
        } else {
            Bundle bundle2 = new Bundle();
            ArrayList arrayList = new ArrayList();
            ParcelItem parcelItem = new ParcelItem();
            Bundle bundle3 = new Bundle();
            ByteBuffer allocate = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle3.putByteArray("imgdata", allocate.array());
            bundle3.putInt("imgindex", bitmap.hashCode());
            bundle3.putInt("imgH", bitmap.getHeight());
            bundle3.putInt("imgW", bitmap.getWidth());
            bundle3.putInt("hasIcon", 1);
            parcelItem.setBundle(bundle3);
            arrayList.add(parcelItem);
            if (arrayList.size() > 0) {
                ParcelItem[] parcelItemArr = new ParcelItem[arrayList.size()];
                for (int i = 0; i < arrayList.size(); i++) {
                    parcelItemArr[i] = (ParcelItem) arrayList.get(i);
                }
                bundle2.putParcelableArray("icondata", parcelItemArr);
            }
            bundle = bundle2;
        }
        b(jSONObject.toString(), bundle);
        this.g.b(this.D.a);
    }

    void a(Handler handler) {
        MessageCenter.registMessage(UIMsg.m_AppUI.MSG_APP_SAVESCREEN, handler);
        MessageCenter.registMessage(39, handler);
        MessageCenter.registMessage(41, handler);
        MessageCenter.registMessage(49, handler);
        MessageCenter.registMessage(UIMsg.m_AppUI.V_WM_VDATAENGINE, handler);
        MessageCenter.registMessage(50, handler);
        MessageCenter.registMessage(999, handler);
        BaseMapCallback.addLayerDataInterface(this.h, this);
    }

    public void a(LatLngBounds latLngBounds) {
        if (latLngBounds == null || this.g == null) {
            return;
        }
        LatLng latLng = latLngBounds.northeast;
        LatLng latLng2 = latLngBounds.southwest;
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        GeoPoint ll2mc2 = CoordUtil.ll2mc(latLng2);
        int longitudeE6 = (int) ll2mc.getLongitudeE6();
        int latitudeE6 = (int) ll2mc2.getLatitudeE6();
        int longitudeE62 = (int) ll2mc2.getLongitudeE6();
        int latitudeE62 = (int) ll2mc.getLatitudeE6();
        Bundle bundle = new Bundle();
        bundle.putInt("maxCoorx", longitudeE6);
        bundle.putInt("minCoory", latitudeE6);
        bundle.putInt("minCoorx", longitudeE62);
        bundle.putInt("maxCoory", latitudeE62);
        this.g.b(bundle);
    }

    void a(C c) {
        new E();
        if (c == null) {
            c = new C();
        }
        E e = c.a;
        this.v = c.f;
        this.w = c.d;
        this.d = c.e;
        this.e = c.g;
        this.g.a(e.a(this));
        this.g.a(B.DEFAULT.ordinal());
        this.r = c.b;
        if (c.b) {
            N = (int) (SysOSUtil.getDensity() * 40.0f);
            O = (int) (SysOSUtil.getDensity() * 40.0f);
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("x", N);
                jSONObject2.put("y", O);
                jSONObject2.put("hidetime", 1000);
                jSONArray.put(jSONObject2);
                jSONObject.put("data", jSONArray);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            this.D.a(jSONObject.toString());
            this.g.a(this.D.a, true);
        } else {
            this.g.a(this.D.a, false);
        }
        int i = c.c;
        if (i == 2) {
            a(true);
        }
        if (i == 3) {
            this.g.a(this.ae.a, false);
        }
    }

    public void a(E e) {
        if (this.g == null) {
            return;
        }
        Bundle a = e.a(this);
        a.putInt("animation", 0);
        a.putInt("animatime", 0);
        this.g.a(a);
    }

    public void a(E e, int i) {
        if (this.g == null) {
            return;
        }
        Bundle a = e.a(this);
        a.putInt("animation", 1);
        a.putInt("animatime", i);
        z();
        this.g.a(a);
    }

    public void a(L l) {
        this.z = l;
    }

    void a(AbstractC0073d abstractC0073d) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        abstractC0073d.a = aVar.a(abstractC0073d.c, abstractC0073d.d, abstractC0073d.b);
        this.B.add(abstractC0073d);
    }

    public void a(InterfaceC0081l interfaceC0081l) {
        this.f.add(interfaceC0081l);
    }

    public void a(q qVar) {
        this.L = qVar;
    }

    public void a(String str, Bundle bundle) {
        if (this.g == null) {
            return;
        }
        this.C.a(str);
        this.C.a(bundle);
        this.g.b(this.C.a);
    }

    public void a(List<Bundle> list) {
        if (this.g == null || list == null) {
            return;
        }
        int size = list.size();
        Bundle[] bundleArr = new Bundle[list.size()];
        for (int i = 0; i < size; i++) {
            g(list.get(i));
            bundleArr[i] = list.get(i);
        }
        this.g.a(bundleArr);
    }

    public void a(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        if (!aVar.a(this.ae.a)) {
            this.g.a(this.ae.a, true);
        }
        this.q = z;
        N();
        this.g.a(this.q);
    }

    @Override // com.baidu.platform.comjni.map.basemap.b
    public boolean a(long j) {
        Iterator<AbstractC0073d> it = this.B.iterator();
        while (it.hasNext()) {
            if (it.next().a == j) {
                return true;
            }
        }
        return false;
    }

    public boolean a(Point point) {
        if (point == null || this.g == null || point.x < 0 || point.y < 0) {
            return false;
        }
        N = point.x;
        O = point.y;
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("x", N);
            jSONObject2.put("y", O);
            jSONObject2.put("hidetime", 1000);
            jSONArray.put(jSONObject2);
            jSONObject.put("data", jSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.D.a(jSONObject.toString());
        this.g.b(this.D.a);
        return true;
    }

    public boolean a(Bundle bundle) {
        if (this.g == null) {
            return false;
        }
        M m = new M();
        this.y = m;
        long a = this.g.a(m.c, this.y.d, this.y.b);
        if (a != 0) {
            this.y.a = a;
            this.B.add(this.y);
            bundle.putLong("sdktileaddr", a);
            if (e(bundle) && f(bundle)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0358  */
    /* JADX WARN: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean a(android.view.MotionEvent r23) {
        /*
            Method dump skipped, instructions count: 968
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.C0074e.a(android.view.MotionEvent):boolean");
    }

    public boolean a(String str, String str2) {
        return this.g.a(str, str2);
    }

    public GeoPoint b(int i, int i2) {
        return this.M.a(i, i2);
    }

    void b() {
        this.B = new ArrayList();
        C0075f c0075f = new C0075f();
        this.ae = c0075f;
        a(c0075f);
        C0071b c0071b = new C0071b();
        this.ag = c0071b;
        a(c0071b);
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            aVar.e(false);
        }
        o oVar = new o();
        this.E = oVar;
        a(oVar);
        p pVar = new p();
        this.J = pVar;
        a(pVar);
        C0070a c0070a = new C0070a();
        this.K = c0070a;
        a(c0070a);
        a(new r());
        H h = new H();
        this.F = h;
        a(h);
        C0072c c0072c = new C0072c();
        this.ah = c0072c;
        a(c0072c);
        C0083n c0083n = new C0083n();
        this.I = c0083n;
        a(c0083n);
        K k2 = new K();
        this.G = k2;
        a(k2);
        C0076g c0076g = new C0076g();
        this.D = c0076g;
        a(c0076g);
        A a = new A();
        this.C = a;
        a(a);
        s sVar = new s();
        this.H = sVar;
        a(sVar);
    }

    public void b(Bundle bundle) {
        if (this.g == null) {
            return;
        }
        g(bundle);
        this.g.f(bundle);
    }

    void b(Handler handler) {
        MessageCenter.unregistMessage(UIMsg.m_AppUI.MSG_APP_SAVESCREEN, handler);
        MessageCenter.unregistMessage(41, handler);
        MessageCenter.unregistMessage(49, handler);
        MessageCenter.unregistMessage(39, handler);
        MessageCenter.unregistMessage(UIMsg.m_AppUI.V_WM_VDATAENGINE, handler);
        MessageCenter.unregistMessage(50, handler);
        MessageCenter.unregistMessage(999, handler);
        BaseMapCallback.removeLayerDataInterface(this.h);
    }

    void b(MotionEvent motionEvent) {
        if (this.S.e) {
            return;
        }
        long downTime = motionEvent.getDownTime();
        this.ad = downTime;
        if (downTime - this.ac < 400) {
            downTime = (Math.abs(motionEvent.getX() - this.Z) >= 120.0f || Math.abs(motionEvent.getY() - this.aa) >= 120.0f) ? this.ad : 0L;
        }
        this.ac = downTime;
        this.Z = motionEvent.getX();
        this.aa = motionEvent.getY();
        a(4, 0, (((int) motionEvent.getY()) << 16) | ((int) motionEvent.getX()));
        this.ab = true;
    }

    public void b(String str, Bundle bundle) {
        if (this.g == null) {
            return;
        }
        this.D.a(str);
        this.D.a(bundle);
        this.g.b(this.D.a);
    }

    public void b(boolean z) {
        this.x = z;
    }

    public void c(Bundle bundle) {
        if (this.g == null) {
            return;
        }
        g(bundle);
        this.g.g(bundle);
    }

    public void c(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.a(this.y.a, z);
    }

    public boolean c() {
        return this.x;
    }

    boolean c(int i, int i2) {
        return i >= 0 && i <= this.P + 0 && i2 >= 0 && i2 <= this.Q + 0;
    }

    boolean c(MotionEvent motionEvent) {
        if (this.S.e || System.currentTimeMillis() - k < 300) {
            return true;
        }
        if (this.n) {
            Iterator<InterfaceC0081l> it = this.f.iterator();
            while (it.hasNext()) {
                it.next().d(b((int) motionEvent.getX(), (int) motionEvent.getY()));
            }
            return true;
        }
        float abs = Math.abs(motionEvent.getX() - this.Z);
        float abs2 = Math.abs(motionEvent.getY() - this.aa);
        double density = SysOSUtil.getDensity();
        double density2 = SysOSUtil.getDensity();
        if (density > 1.5d) {
            density2 *= 1.5d;
        }
        float f = (float) density2;
        if (this.ab && abs / f <= 3.0f && abs2 / f <= 3.0f) {
            return true;
        }
        this.ab = false;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (this.d) {
            J();
            a(3, 0, (y << 16) | x);
        }
        return false;
    }

    public void d(Bundle bundle) {
        if (this.g == null) {
            return;
        }
        g(bundle);
        this.g.h(bundle);
    }

    public void d(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.a(this.ae.a, z);
    }

    public boolean d() {
        com.baidu.platform.comjni.map.basemap.a aVar;
        M m = this.y;
        if (m == null || (aVar = this.g) == null) {
            return false;
        }
        return aVar.c(m.a);
    }

    boolean d(MotionEvent motionEvent) {
        if (this.n) {
            Iterator<InterfaceC0081l> it = this.f.iterator();
            while (it.hasNext()) {
                it.next().e(b((int) motionEvent.getX(), (int) motionEvent.getY()));
            }
            this.n = false;
            return true;
        }
        boolean z = !this.S.e && motionEvent.getEventTime() - this.ad < 400 && Math.abs(motionEvent.getX() - this.Z) < 10.0f && Math.abs(motionEvent.getY() - this.aa) < 10.0f;
        L();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (z) {
            return false;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        a(5, 0, (y << 16) | x);
        return true;
    }

    void e() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.M = new I(aVar);
    }

    public void e(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.u = z;
        aVar.b(z);
    }

    public void f(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.p = z;
        aVar.c(z);
    }

    public boolean f() {
        return this.p;
    }

    public String g() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return null;
        }
        return aVar.e(this.D.a);
    }

    public void g(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.d(z);
    }

    public void h(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.r = z;
        aVar.a(this.D.a, z);
    }

    public boolean h() {
        return this.u;
    }

    public void i(boolean z) {
        this.g.e(z);
        this.g.d(this.ag.a);
        this.g.d(this.ah.a);
    }

    public boolean i() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return false;
        }
        return aVar.m();
    }

    public boolean j() {
        return this.q;
    }

    public void k(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.s = z;
        aVar.a(this.C.a, z);
    }

    public boolean k() {
        return this.g.a(this.ae.a);
    }

    public void l(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        this.t = z;
        aVar.a(this.J.a, z);
    }

    public boolean l() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return false;
        }
        return aVar.q();
    }

    public void m() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.d(this.E.a);
        this.g.d(this.I.a);
        this.g.d(this.G.a);
        this.g.d(this.H.a);
    }

    public void m(boolean z) {
        this.d = z;
    }

    public void n() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.r();
        this.g.b(this.J.a);
    }

    public void n(boolean z) {
        this.e = z;
    }

    public MapBaseIndoorMapInfo o() {
        return this.g.s();
    }

    public void o(boolean z) {
        this.w = z;
    }

    public void p(boolean z) {
        this.v = z;
    }

    public boolean p() {
        return this.g.t();
    }

    public void q(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            aVar.a(this.F.a, z);
        }
    }

    public boolean q() {
        return this.r;
    }

    public void r(boolean z) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar != null) {
            aVar.a(this.ah.a, z);
        }
    }

    public boolean r() {
        return this.s;
    }

    public void s() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.b(this.J.a);
    }

    public void t() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.g();
    }

    public void u() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.g;
        if (aVar == null) {
            return;
        }
        aVar.h();
    }

    public boolean v() {
        return this.d;
    }

    public boolean w() {
        return this.e;
    }

    public boolean x() {
        return this.w;
    }

    public boolean y() {
        return this.v;
    }

    void z() {
        if (this.l || this.m) {
            return;
        }
        this.m = true;
        Iterator<InterfaceC0081l> it = this.f.iterator();
        while (it.hasNext()) {
            it.next().a(D());
        }
    }
}
