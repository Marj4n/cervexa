package com.jieli.stream.dv.running2.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class ManifestUtil {
    public static final String SINA_WEIBO_KEY = "sina_weibo_key";
    public static final String SINA_WEIBO_REDIRECTURI = "sina_weibo_redirecturi";
    public static final String TENCENT_QQ_APPID = "tencent_qq_appid";
    public static final String WEIXIN_KEY = "weixin_key";
    public static final String WEIXIN_REDIRECTURI_KEY = "weixin_redirecturi";
    private static final String tag = ManifestUtil.class.getSimpleName();

    public static String getSinaWeiboKey(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return "";
            }
            Bundle bundle = applicationInfo.metaData;
            String valueOf = bundle != null ? String.valueOf(bundle.get("sina_weibo_key")) : null;
            return !TextUtils.isEmpty(valueOf) ? valueOf : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSinaWeiboRedirecturi(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return "";
            }
            Bundle bundle = applicationInfo.metaData;
            String string = bundle != null ? bundle.getString("sina_weibo_redirecturi") : null;
            return !TextUtils.isEmpty(string) ? string : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWeixinKey(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return "";
            }
            Bundle bundle = applicationInfo.metaData;
            String valueOf = bundle != null ? String.valueOf(bundle.get("weixin_key")) : null;
            return !TextUtils.isEmpty(valueOf) ? valueOf : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWeixinRedirecturi(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return "";
            }
            Bundle bundle = applicationInfo.metaData;
            String string = bundle != null ? bundle.getString("weixin_redirecturi") : null;
            return !TextUtils.isEmpty(string) ? string : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTencentQQAppId(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return "222222";
            }
            Bundle bundle = applicationInfo.metaData;
            String str = null;
            if (bundle != null) {
                str = String.valueOf(bundle.get("tencent_qq_appid"));
                Log.i(tag, "getTencentQQAppId=" + str);
            }
            return !TextUtils.isEmpty(str) ? str : "222222";
        } catch (Exception e) {
            e.printStackTrace();
            return "222222";
        }
    }
}
