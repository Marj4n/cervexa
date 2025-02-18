package com.baidu.mapapi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.baidu.mapapi.common.AppTools;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.poi.DispathcPoiData;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapframework.open.aidl.IComOpenClient;
import com.baidu.platform.comapi.location.CoordinateType;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a {
    private static com.baidu.mapframework.open.aidl.a d;
    private static IComOpenClient e;
    private static int f;
    private static RouteParaOption.EBusStrategyType o;
    private static Thread v;
    private static final String c = a.class.getName();
    public static int a = -1;
    private static String g = null;
    private static String h = null;
    private static String i = null;
    private static List<DispathcPoiData> j = new ArrayList();
    private static LatLng k = null;
    private static LatLng l = null;
    private static String m = null;
    private static String n = null;
    private static String p = null;
    private static String q = null;
    private static LatLng r = null;
    private static int s = 0;
    private static boolean t = false;
    private static boolean u = false;
    static ServiceConnection b = new c();

    public static String a() {
        return AppTools.getBaiduMapToken();
    }

    public static void a(int i2, Context context) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                c(context, i2);
                break;
            case 3:
                c(context);
                break;
            case 4:
                d(context);
                break;
            case 5:
                e(context);
                break;
            case 7:
                f(context);
                break;
            case 8:
                g(context);
                break;
        }
    }

    public static void a(Context context) {
        if (u) {
            context.unbindService(b);
            u = false;
        }
    }

    private static void a(List<DispathcPoiData> list, Context context) {
        g = context.getPackageName();
        h = b(context);
        i = "";
        List<DispathcPoiData> list2 = j;
        if (list2 != null) {
            list2.clear();
        }
        Iterator<DispathcPoiData> it = list.iterator();
        while (it.hasNext()) {
            j.add(it.next());
        }
    }

    public static boolean a(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                return g();
            case 3:
                return h();
            case 4:
                return m();
            case 5:
                return j();
            case 6:
                return i();
            case 7:
                return k();
            case 8:
                return l();
            default:
                return false;
        }
    }

    public static boolean a(Context context, int i2) {
        try {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (!com.baidu.platform.comapi.a.a.a(context)) {
            Log.d(c, "package sign verify failed");
            return false;
        }
        t = false;
        switch (i2) {
            case 0:
                a = 0;
                break;
            case 1:
                a = 1;
                break;
            case 2:
                a = 2;
                break;
            case 3:
                a = 3;
                break;
            case 4:
                a = 4;
                break;
            case 5:
                a = 5;
                break;
            case 6:
                a = 6;
                break;
            case 7:
                a = 7;
                break;
            case 8:
                a = 8;
                break;
        }
        if (d == null || !u) {
            b(context, i2);
        } else {
            if (e != null) {
                t = true;
                return a(i2);
            }
            d.a(new b(i2));
        }
        return true;
    }

    public static boolean a(NaviParaOption naviParaOption, Context context, int i2) {
        b(naviParaOption, context, i2);
        return a(context, i2);
    }

    public static boolean a(PoiParaOption poiParaOption, Context context, int i2) {
        b(poiParaOption, context, i2);
        return a(context, i2);
    }

    public static boolean a(RouteParaOption routeParaOption, Context context, int i2) {
        b(routeParaOption, context, i2);
        return a(context, i2);
    }

    public static boolean a(List<DispathcPoiData> list, Context context, int i2) {
        a(list, context);
        return a(context, i2);
    }

    public static String b(Context context) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            packageManager = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    private static void b(Context context, int i2) {
        Intent intent = new Intent();
        String a2 = a();
        if (a2 == null) {
            return;
        }
        intent.putExtra("api_token", a2);
        intent.setAction("com.baidu.map.action.OPEN_SERVICE");
        intent.setPackage("com.baidu.BaiduMap");
        boolean bindService = context.bindService(intent, b, 1);
        u = bindService;
        if (!bindService) {
            Log.e("baidumapsdk", "bind service failed，call openapi");
            a(i2, context);
        } else {
            Thread thread = new Thread(new e(context, i2));
            v = thread;
            thread.setDaemon(true);
            v.start();
        }
    }

    private static void b(NaviParaOption naviParaOption, Context context, int i2) {
        g = context.getPackageName();
        m = null;
        k = null;
        n = null;
        l = null;
        if (naviParaOption.getStartPoint() != null) {
            k = naviParaOption.getStartPoint();
        }
        if (naviParaOption.getEndPoint() != null) {
            l = naviParaOption.getEndPoint();
        }
        if (naviParaOption.getStartName() != null) {
            m = naviParaOption.getStartName();
        }
        if (naviParaOption.getEndName() != null) {
            n = naviParaOption.getEndName();
        }
    }

    private static void b(PoiParaOption poiParaOption, Context context, int i2) {
        p = null;
        q = null;
        r = null;
        s = 0;
        g = context.getPackageName();
        if (poiParaOption.getUid() != null) {
            p = poiParaOption.getUid();
        }
        if (poiParaOption.getKey() != null) {
            q = poiParaOption.getKey();
        }
        if (poiParaOption.getCenter() != null) {
            r = poiParaOption.getCenter();
        }
        if (poiParaOption.getRadius() != 0) {
            s = poiParaOption.getRadius();
        }
    }

    private static void b(RouteParaOption routeParaOption, Context context, int i2) {
        int i3;
        m = null;
        k = null;
        n = null;
        l = null;
        g = context.getPackageName();
        if (routeParaOption.getStartPoint() != null) {
            k = routeParaOption.getStartPoint();
        }
        if (routeParaOption.getEndPoint() != null) {
            l = routeParaOption.getEndPoint();
        }
        if (routeParaOption.getStartName() != null) {
            m = routeParaOption.getStartName();
        }
        if (routeParaOption.getEndName() != null) {
            n = routeParaOption.getEndName();
        }
        if (routeParaOption.getBusStrategyType() != null) {
            o = routeParaOption.getBusStrategyType();
        }
        if (i2 != 0) {
            i3 = 1;
            if (i2 != 1) {
                i3 = 2;
                if (i2 != 2) {
                    return;
                }
            }
        } else {
            i3 = 0;
        }
        f = i3;
    }

    private static void c(Context context) {
        Thread thread = v;
        if (thread != null) {
            thread.interrupt();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/place/detail?");
        sb.append("uid=");
        sb.append(p);
        sb.append("&show_type=");
        sb.append("detail_page");
        sb.append("&src=");
        sb.append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a0, code lost:
    
        if (r2 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005f, code lost:
    
        if (r2 != null) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void c(android.content.Context r8, int r9) {
        /*
            java.lang.Thread r0 = com.baidu.mapapi.utils.a.v
            if (r0 == 0) goto L7
            r0.interrupt()
        L7:
            java.lang.String r0 = "driving"
            java.lang.String r1 = "transit"
            java.lang.String r2 = "walking"
            java.lang.String[] r0 = new java.lang.String[]{r0, r1, r2}
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "baidumap://map/direction?"
            r1.append(r2)
            java.lang.String r2 = "origin="
            r1.append(r2)
            java.lang.String r2 = com.baidu.mapapi.utils.a.m
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            java.lang.String r3 = "|latlng:"
            java.lang.String r4 = "name:"
            java.lang.String r5 = ","
            if (r2 != 0) goto L4f
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.k
            if (r2 == 0) goto L4f
            r1.append(r4)
            java.lang.String r2 = com.baidu.mapapi.utils.a.m
            r1.append(r2)
            r1.append(r3)
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.k
        L3f:
            double r6 = r2.latitude
            r1.append(r6)
            r1.append(r5)
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.k
            double r6 = r2.longitude
            r1.append(r6)
            goto L62
        L4f:
            java.lang.String r2 = com.baidu.mapapi.utils.a.m
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L5d
            java.lang.String r2 = com.baidu.mapapi.utils.a.m
            r1.append(r2)
            goto L62
        L5d:
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.k
            if (r2 == 0) goto L62
            goto L3f
        L62:
            java.lang.String r2 = "&destination="
            r1.append(r2)
            java.lang.String r2 = com.baidu.mapapi.utils.a.n
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L90
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.l
            if (r2 == 0) goto L90
            r1.append(r4)
            java.lang.String r2 = com.baidu.mapapi.utils.a.n
            r1.append(r2)
            r1.append(r3)
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.l
        L80:
            double r2 = r2.latitude
            r1.append(r2)
            r1.append(r5)
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.l
            double r2 = r2.longitude
            r1.append(r2)
            goto La3
        L90:
            java.lang.String r2 = com.baidu.mapapi.utils.a.n
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L9e
            java.lang.String r2 = com.baidu.mapapi.utils.a.n
            r1.append(r2)
            goto La3
        L9e:
            com.baidu.mapapi.model.LatLng r2 = com.baidu.mapapi.utils.a.l
            if (r2 == 0) goto La3
            goto L80
        La3:
            java.lang.String r2 = "&mode="
            r1.append(r2)
            r9 = r0[r9]
            r1.append(r9)
            java.lang.String r9 = "&target="
            r1.append(r9)
            java.lang.String r9 = "1"
            r1.append(r9)
            java.lang.String r9 = "&src="
            r1.append(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r0 = "sdk_["
            r9.append(r0)
            java.lang.String r0 = com.baidu.mapapi.utils.a.g
            r9.append(r0)
            java.lang.String r0 = "]"
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            android.net.Uri r9 = android.net.Uri.parse(r9)
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "android.intent.action.VIEW"
            r0.<init>(r1, r9)
            r9 = 268435456(0x10000000, float:2.524355E-29)
            r0.setFlags(r9)
            r8.startActivity(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.utils.a.c(android.content.Context, int):void");
    }

    private static void d(Context context) {
        Thread thread = v;
        if (thread != null) {
            thread.interrupt();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/nearbysearch?");
        sb.append("center=");
        sb.append(r.latitude);
        sb.append(",");
        sb.append(r.longitude);
        sb.append("&query=");
        sb.append(q);
        sb.append("&radius=");
        sb.append(s);
        sb.append("&src=");
        sb.append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void e(Context context) {
        Thread thread = v;
        if (thread != null) {
            thread.interrupt();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/navi?");
        sb.append("origin=");
        sb.append(k.latitude);
        sb.append(",");
        sb.append(k.longitude);
        sb.append("&location=");
        sb.append(l.latitude);
        sb.append(",");
        sb.append(l.longitude);
        sb.append("&src=");
        sb.append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void f(Context context) {
        Thread thread = v;
        if (thread != null) {
            thread.interrupt();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/walknavi?");
        sb.append("origin=");
        sb.append(k.latitude);
        sb.append(",");
        sb.append(k.longitude);
        sb.append("&destination=");
        sb.append(l.latitude);
        sb.append(",");
        sb.append(l.longitude);
        sb.append("&src=");
        sb.append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void g(Context context) {
        Thread thread = v;
        if (thread != null) {
            thread.interrupt();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/bikenavi?");
        sb.append("origin=");
        sb.append(k.latitude);
        sb.append(",");
        sb.append(k.longitude);
        sb.append("&destination=");
        sb.append(l.latitude);
        sb.append(",");
        sb.append(l.longitude);
        sb.append("&src=");
        sb.append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static boolean g() {
        String a2;
        try {
            Log.d(c, "callDispatchTakeOutRoute");
            a2 = e.a("map.android.baidu.mainmap");
        } catch (RemoteException e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
        }
        if (a2 == null) {
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("target", "route_search_page");
        Bundle bundle2 = new Bundle();
        bundle2.putInt("route_type", f);
        bundle2.putInt("bus_strategy", o.ordinal());
        bundle2.putInt("cross_city_bus_strategy", 5);
        if (k != null) {
            bundle2.putInt("start_type", 1);
            bundle2.putInt("start_longitude", (int) CoordUtil.ll2mc(k).getLongitudeE6());
            bundle2.putInt("start_latitude", (int) CoordUtil.ll2mc(k).getLatitudeE6());
        } else {
            bundle2.putInt("start_type", 2);
            bundle2.putInt("start_longitude", 0);
            bundle2.putInt("start_latitude", 0);
        }
        if (m != null) {
            bundle2.putString("start_keyword", m);
        } else {
            bundle2.putString("start_keyword", "地图上的点");
        }
        bundle2.putString("start_uid", "");
        if (l != null) {
            bundle2.putInt("end_type", 1);
            bundle2.putInt("end_longitude", (int) CoordUtil.ll2mc(l).getLongitudeE6());
            bundle2.putInt("end_latitude", (int) CoordUtil.ll2mc(l).getLatitudeE6());
        } else {
            bundle2.putInt("end_type", 2);
            bundle2.putInt("end_longitude", 0);
            bundle2.putInt("end_latitude", 0);
        }
        if (n != null) {
            bundle2.putString("end_keyword", n);
        } else {
            bundle2.putString("end_keyword", "地图上的点");
        }
        bundle2.putString("end_uid", "");
        bundle.putBundle("base_params", bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("launch_from", "sdk_[" + g + "]");
        bundle.putBundle("ext_params", bundle3);
        return e.a("map.android.baidu.mainmap", a2, bundle);
    }

    private static boolean h() {
        try {
            Log.d(c, "callDispatchTakeOutPoiDetials");
            String a2 = e.a("map.android.baidu.mainmap");
            if (a2 == null) {
                Log.d(c, "callDispatchTakeOut com not found");
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("target", "request_poi_detail_page");
            Bundle bundle2 = new Bundle();
            bundle2.putString("uid", p != null ? p : "");
            bundle.putBundle("base_params", bundle2);
            Bundle bundle3 = new Bundle();
            bundle3.putString("launch_from", "sdk_[" + g + "]");
            bundle.putBundle("ext_params", bundle3);
            return e.a("map.android.baidu.mainmap", a2, bundle);
        } catch (RemoteException e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
            return false;
        }
    }

    private static boolean i() {
        List<DispathcPoiData> list = j;
        if (list != null && list.size() > 0) {
            try {
                Log.d(c, "callDispatchPoiToBaiduMap");
                String a2 = e.a("map.android.baidu.mainmap");
                if (a2 != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("target", "favorite_page");
                    Bundle bundle2 = new Bundle();
                    JSONArray jSONArray = new JSONArray();
                    int i2 = 0;
                    for (int i3 = 0; i3 < j.size(); i3++) {
                        if (j.get(i3).name != null && !j.get(i3).name.equals("") && j.get(i3).pt != null) {
                            JSONObject jSONObject = new JSONObject();
                            try {
                                jSONObject.put(BaseFragment.DATA_NAME, j.get(i3).name);
                                GeoPoint ll2mc = CoordUtil.ll2mc(j.get(i3).pt);
                                jSONObject.put("ptx", ll2mc.getLongitudeE6());
                                jSONObject.put("pty", ll2mc.getLatitudeE6());
                                jSONObject.put("addr", j.get(i3).addr);
                                jSONObject.put("uid", j.get(i3).uid);
                                i2++;
                                jSONArray.put(jSONObject);
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    if (i2 == 0) {
                        return false;
                    }
                    bundle2.putString("data", jSONArray.toString());
                    bundle2.putString("from", h);
                    bundle2.putString("pkg", g);
                    bundle2.putString("cls", i);
                    bundle2.putInt("count", i2);
                    bundle.putBundle("base_params", bundle2);
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("launch_from", "sdk_[" + g + "]");
                    bundle.putBundle("ext_params", bundle3);
                    return e.a("map.android.baidu.mainmap", a2, bundle);
                }
                Log.d(c, "callDispatchPoiToBaiduMap com not found");
            } catch (RemoteException e3) {
                Log.d(c, "callDispatchPoiToBaiduMap exception", e3);
            }
        }
        return false;
    }

    private static boolean j() {
        String a2;
        try {
            Log.d(c, "callDispatchTakeOutRouteNavi");
            a2 = e.a("map.android.baidu.mainmap");
        } catch (RemoteException e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
        }
        if (a2 == null) {
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("target", "navigation_page");
        Bundle bundle2 = new Bundle();
        bundle2.putString("coord_type", CoordinateType.BD09LL);
        bundle2.putString("type", "DIS");
        StringBuffer stringBuffer = new StringBuffer();
        if (m != null) {
            stringBuffer.append("name:" + m + "|");
        }
        stringBuffer.append(String.format("latlng:%f,%f", Double.valueOf(k.latitude), Double.valueOf(k.longitude)));
        StringBuffer stringBuffer2 = new StringBuffer();
        if (n != null) {
            stringBuffer2.append("name:" + n + "|");
        }
        stringBuffer2.append(String.format("latlng:%f,%f", Double.valueOf(l.latitude), Double.valueOf(l.longitude)));
        bundle2.putString("origin", stringBuffer.toString());
        bundle2.putString("destination", stringBuffer2.toString());
        bundle.putBundle("base_params", bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("launch_from", "sdk_[" + g + "]");
        bundle.putBundle("ext_params", bundle3);
        return e.a("map.android.baidu.mainmap", a2, bundle);
    }

    private static boolean k() {
        String a2;
        try {
            Log.d(c, "callDispatchTakeOutRouteNavi");
            a2 = e.a("map.android.baidu.mainmap");
        } catch (Exception e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
        }
        if (a2 == null) {
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("target", "walknavi_page");
        Bundle bundle2 = new Bundle();
        bundle2.putString("coord_type", CoordinateType.BD09LL);
        StringBuffer stringBuffer = new StringBuffer();
        if (m != null) {
            stringBuffer.append("name:" + m + "|");
        }
        stringBuffer.append(String.format("latlng:%f,%f", Double.valueOf(k.latitude), Double.valueOf(k.longitude)));
        StringBuffer stringBuffer2 = new StringBuffer();
        if (n != null) {
            stringBuffer2.append("name:" + n + "|");
        }
        stringBuffer2.append(String.format("latlng:%f,%f", Double.valueOf(l.latitude), Double.valueOf(l.longitude)));
        bundle2.putString("origin", stringBuffer.toString());
        bundle2.putString("destination", stringBuffer2.toString());
        bundle.putBundle("base_params", bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("launch_from", "sdk_[" + g + "]");
        bundle.putBundle("ext_params", bundle3);
        return e.a("map.android.baidu.mainmap", a2, bundle);
    }

    private static boolean l() {
        String a2;
        try {
            Log.d(c, "callDispatchTakeOutRouteRidingNavi");
            a2 = e.a("map.android.baidu.mainmap");
        } catch (RemoteException e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
        }
        if (a2 == null) {
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("target", "bikenavi_page");
        Bundle bundle2 = new Bundle();
        bundle2.putString("coord_type", CoordinateType.BD09LL);
        StringBuffer stringBuffer = new StringBuffer();
        if (m != null) {
            stringBuffer.append("name:" + m + "|");
        }
        stringBuffer.append(String.format("latlng:%f,%f", Double.valueOf(k.latitude), Double.valueOf(k.longitude)));
        StringBuffer stringBuffer2 = new StringBuffer();
        if (n != null) {
            stringBuffer2.append("name:" + n + "|");
        }
        stringBuffer2.append(String.format("latlng:%f,%f", Double.valueOf(l.latitude), Double.valueOf(l.longitude)));
        bundle2.putString("origin", stringBuffer.toString());
        bundle2.putString("destination", stringBuffer2.toString());
        bundle.putBundle("base_params", bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("launch_from", "sdk_[" + g + "]");
        bundle.putBundle("ext_params", bundle3);
        return e.a("map.android.baidu.mainmap", a2, bundle);
    }

    private static boolean m() {
        try {
            Log.d(c, "callDispatchTakeOutPoiNearbySearch");
            String a2 = e.a("map.android.baidu.mainmap");
            if (a2 == null) {
                Log.d(c, "callDispatchTakeOut com not found");
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("target", "poi_search_page");
            Bundle bundle2 = new Bundle();
            if (q != null) {
                bundle2.putString("search_key", q);
            } else {
                bundle2.putString("search_key", "");
            }
            if (r != null) {
                bundle2.putInt("center_pt_x", (int) CoordUtil.ll2mc(r).getLongitudeE6());
                bundle2.putInt("center_pt_y", (int) CoordUtil.ll2mc(r).getLatitudeE6());
            } else {
                bundle2.putString("search_key", "");
            }
            if (s != 0) {
                bundle2.putInt("search_radius", s);
            } else {
                bundle2.putInt("search_radius", 1000);
            }
            bundle2.putBoolean("is_direct_search", true);
            bundle2.putBoolean("is_direct_area_search", true);
            bundle.putBundle("base_params", bundle2);
            Bundle bundle3 = new Bundle();
            bundle3.putString("launch_from", "sdk_[" + g + "]");
            bundle.putBundle("ext_params", bundle3);
            return e.a("map.android.baidu.mainmap", a2, bundle);
        } catch (RemoteException e2) {
            Log.d(c, "callDispatchTakeOut exception", e2);
            return false;
        }
    }
}
