package com.serenegiant.utils;

import android.content.ComponentName;
import android.content.Context;

/* loaded from: classes2.dex */
public class ComponentUtils {
    public static void disable(Context context, Class<?> cls) {
        setComponentState(context, cls, false);
    }

    public static void enable(Context context, Class<?> cls) {
        setComponentState(context, cls, true);
    }

    public static void setComponentState(Context context, Class<?> cls, boolean z) {
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, cls), z ? 1 : 2, 1);
    }
}
