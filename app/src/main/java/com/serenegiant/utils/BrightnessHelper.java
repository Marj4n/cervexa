package com.serenegiant.utils;

import android.app.Activity;
import android.view.WindowManager;

/* loaded from: classes2.dex */
public class BrightnessHelper {
    public static void setBrightness(Activity activity, float f) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        if (f > 1.0f) {
            f = 1.0f;
        }
        attributes.screenBrightness = f;
        activity.getWindow().setAttributes(attributes);
    }

    public float getBrightness(Activity activity) {
        return activity.getWindow().getAttributes().screenBrightness;
    }
}
