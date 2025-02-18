package com.baidu.mapapi.utils.poi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.platform.comapi.pano.PanoStateError;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

/* loaded from: classes.dex */
public class BaiduMapPoiSearch {
    private static boolean a = true;

    /* renamed from: com.baidu.mapapi.utils.poi.BaiduMapPoiSearch$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[HttpClient.HttpStateError.values().length];
            b = iArr;
            try {
                iArr[HttpClient.HttpStateError.NETWORK_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[HttpClient.HttpStateError.INNER_ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[PanoStateError.values().length];
            a = iArr2;
            try {
                iArr2[PanoStateError.PANO_UID_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[PanoStateError.PANO_NOT_FOUND.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[PanoStateError.PANO_NO_TOKEN.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[PanoStateError.PANO_NO_ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private static void a(PoiParaOption poiParaOption, Context context) {
        Uri parse = Uri.parse("http://api.map.baidu.com/place/detail?uid=" + poiParaOption.a + "&output=html&src=" + context.getPackageName());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    private static void b(PoiParaOption poiParaOption, Context context) {
        Uri parse = Uri.parse("http://api.map.baidu.com/place/search?query=" + poiParaOption.b + "&location=" + poiParaOption.c.latitude + "," + poiParaOption.c.longitude + "&radius=" + poiParaOption.d + "&output=html&src=" + context.getPackageName());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(String str, Context context) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("pano id can not be null.");
        }
        if (context == null) {
            throw new RuntimeException("context cannot be null.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/streetscape?");
        sb.append("panoid=");
        sb.append(str);
        sb.append("&pid=");
        sb.append(str);
        sb.append("&panotype=");
        sb.append("street");
        sb.append("&src=");
        sb.append("sdk_[" + context.getPackageName() + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            throw new RuntimeException("BaiduMap app is not installed.");
        }
        context.startActivity(intent);
    }

    public static boolean dispatchPoiToBaiduMap(List<DispathcPoiData> list, Context context) throws Exception {
        String str;
        if (list.isEmpty() || list.size() <= 0) {
            throw new NullPointerException("dispatch poidata is null");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            str = "BaiduMap app is not installed.";
        } else {
            if (baiduMapVersion >= 840) {
                return com.baidu.mapapi.utils.a.a(list, context, 6);
            }
            str = "Baidumap app version is too lowl.Version is greater than 8.4";
        }
        Log.e("baidumapsdk", str);
        return false;
    }

    public static void finish(Context context) {
        if (context != null) {
            com.baidu.mapapi.utils.a.a(context);
        }
    }

    public static void openBaiduMapPanoShow(String str, Context context) {
        new com.baidu.platform.comapi.pano.a().a(str, new a(context));
    }

    public static boolean openBaiduMapPoiDetialsPage(PoiParaOption poiParaOption, Context context) {
        if (poiParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        }
        if (poiParaOption.a == null) {
            throw new IllegalPoiSearchArgumentException("poi uid can not be null.");
        }
        if (poiParaOption.a.equals("")) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi uid can not be empty string");
            return false;
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            }
            a(poiParaOption, context);
            return true;
        }
        if (baiduMapVersion >= 810) {
            return com.baidu.mapapi.utils.a.a(poiParaOption, context, 3);
        }
        Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
        if (!a) {
            throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
        }
        a(poiParaOption, context);
        return true;
    }

    public static boolean openBaiduMapPoiNearbySearch(PoiParaOption poiParaOption, Context context) {
        if (poiParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        }
        if (poiParaOption.b == null) {
            throw new IllegalPoiSearchArgumentException("poi search key can not be null.");
        }
        if (poiParaOption.c == null) {
            throw new IllegalPoiSearchArgumentException("poi search center can not be null.");
        }
        if (poiParaOption.c.longitude == Utils.DOUBLE_EPSILON || poiParaOption.c.latitude == Utils.DOUBLE_EPSILON) {
            throw new IllegalPoiSearchArgumentException("poi search center longitude or latitude can not be 0.");
        }
        if (poiParaOption.d == 0) {
            throw new IllegalPoiSearchArgumentException("poi search radius larger than 0.");
        }
        if (poiParaOption.b.equals("")) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi key can not be empty string");
            return false;
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            }
            b(poiParaOption, context);
            return true;
        }
        if (baiduMapVersion >= 810) {
            return com.baidu.mapapi.utils.a.a(poiParaOption, context, 4);
        }
        Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
        if (!a) {
            throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
        }
        b(poiParaOption, context);
        return true;
    }

    public static void setSupportWebPoi(boolean z) {
        a = z;
    }
}
