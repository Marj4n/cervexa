package com.xyzlf.share.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/* loaded from: classes2.dex */
public class ToastUtil {
    public static void showToast(Context context, int i, boolean z) {
        if (context == null) {
            return;
        }
        String str = null;
        try {
            str = context.getString(i);
        } catch (Exception unused) {
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        showToast(context, str, z);
    }

    public static void showToast(Context context, String str, boolean z) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        Toast.makeText(context, str, !z ? 1 : 0).show();
    }
}
