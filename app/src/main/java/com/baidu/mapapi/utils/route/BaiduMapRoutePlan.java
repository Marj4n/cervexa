package com.baidu.mapapi.utils.route;

import android.content.Context;
import android.util.Log;
import com.baidu.mapapi.navi.IllegalNaviArgumentException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.a;
import com.baidu.mapapi.utils.poi.IllegalPoiSearchArgumentException;
import com.baidu.mapapi.utils.route.RouteParaOption;

/* loaded from: classes.dex */
public class BaiduMapRoutePlan {
    private static boolean a = true;

    /* JADX WARN: Removed duplicated region for block: B:18:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0093  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void a(com.baidu.mapapi.utils.route.RouteParaOption r9, android.content.Context r10, int r11) {
        /*
            Method dump skipped, instructions count: 275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.utils.route.BaiduMapRoutePlan.a(com.baidu.mapapi.utils.route.RouteParaOption, android.content.Context, int):void");
    }

    public static void finish(Context context) {
        if (context != null) {
            a.a(context);
        }
    }

    public static boolean openBaiduMapDrivingRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        }
        if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        }
        if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        }
        if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        }
        if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        }
        if (routeParaOption.f == null) {
            routeParaOption.f = RouteParaOption.EBusStrategyType.bus_recommend_way;
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            }
            a(routeParaOption, context, 0);
            return true;
        }
        if (baiduMapVersion >= 810) {
            return a.a(routeParaOption, context, 0);
        }
        Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
        if (!a) {
            throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
        }
        a(routeParaOption, context, 0);
        return true;
    }

    public static boolean openBaiduMapTransitRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        }
        if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        }
        if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        }
        if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        }
        if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        }
        if (routeParaOption.f == null) {
            routeParaOption.f = RouteParaOption.EBusStrategyType.bus_recommend_way;
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            }
            a(routeParaOption, context, 1);
            return true;
        }
        if (baiduMapVersion >= 810) {
            return a.a(routeParaOption, context, 1);
        }
        Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
        if (!a) {
            throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
        }
        a(routeParaOption, context, 1);
        return true;
    }

    public static boolean openBaiduMapWalkingRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        }
        if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        }
        if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        }
        if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        }
        if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        }
        if (routeParaOption.f == null) {
            routeParaOption.f = RouteParaOption.EBusStrategyType.bus_recommend_way;
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            }
            a(routeParaOption, context, 2);
            return true;
        }
        if (baiduMapVersion >= 810) {
            return a.a(routeParaOption, context, 2);
        }
        Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
        if (!a) {
            throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
        }
        a(routeParaOption, context, 2);
        return true;
    }

    public static void setSupportWebRoute(boolean z) {
        a = z;
    }
}
