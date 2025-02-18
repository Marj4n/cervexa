package com.baidu.mapapi;

import android.content.Context;
import com.baidu.platform.comapi.c;

/* loaded from: classes.dex */
public class SDKInitializer {
    public static final String SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR = "network error";
    public static final String SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR = "permission check error";
    public static final String SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK = "permission check ok";
    public static final String SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE = "error_code";

    private SDKInitializer() {
    }

    public static void initialize(Context context) {
        initialize(null, context);
    }

    public static void initialize(String str, Context context) {
        c.a(str, context);
    }
}
