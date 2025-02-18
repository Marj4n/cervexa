package com.xyzlf.share.library.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.xyzlf.share.library.interfaces.ShareConstant;

/* loaded from: classes2.dex */
public class ChannelUtil {
    public static boolean isAppInstall(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWeixinInstall(Context context) {
        return isAppInstall(context, "com.tencent.mm");
    }

    public static boolean isQQInstall(Context context) {
        return isAppInstall(context, "com.tencent.mobileqq");
    }

    public static boolean isWeiboInstall(Context context) {
        return isAppInstall(context, ShareConstant.SINA_WEIBO_PACKAGE_NAME);
    }

    public static boolean isWeiboLiteInstall(Context context) {
        return isAppInstall(context, ShareConstant.SINA_WEIBO_LITE_PACKAGE_NAME);
    }
}
