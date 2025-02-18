package com.jieli.stream.dv.running2.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class ToastUtil {
    private static WeakReference<Context> contextWeakReference;
    private static String tag = ToastUtil.class.getSimpleName();

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        contextWeakReference = new WeakReference<>(context.getApplicationContext());
    }

    private static void showToast(String str, int i) {
        WeakReference<Context> weakReference = contextWeakReference;
        if (weakReference == null) {
            throw new RuntimeException("u have not init toast utils");
        }
        if (weakReference.get() == null) {
            Dbug.e(tag, "contextWeakReference.get is null ");
            return;
        }
        if (!TextUtils.isEmpty(str) && !str.contains("TF") && i >= 0) {
            Toast makeText = Toast.makeText(contextWeakReference.get(), str, i);
            makeText.setText(str);
            makeText.setDuration(i);
            makeText.show();
            return;
        }
        Dbug.e(tag, "Error: msg=" + str + ", duration=" + i);
    }

    public static void showToastShort(String str) {
        showToast(str, 0);
    }

    public static void showToastLong(String str) {
        showToast(str, 1);
    }
}
