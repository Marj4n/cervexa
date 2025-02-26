package com.roughike.bottombar;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

/* loaded from: classes.dex */
class MiscUtils {
    MiscUtils() {
    }

    protected static int getColor(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.data;
    }

    protected static int dpToPixel(Context context, float f) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        try {
            return (int) (f * displayMetrics.density);
        } catch (NoSuchFieldError unused) {
            return (int) TypedValue.applyDimension(1, f, displayMetrics);
        }
    }

    protected static int pixelToDp(Context context, int i) {
        return Math.round(i / context.getResources().getDisplayMetrics().density);
    }

    protected static int getScreenWidth(Context context) {
        DisplayMetrics r1 = null;
        return (int) (r1.widthPixels / context.getResources().getDisplayMetrics().density);
    }

    protected static void setTextAppearance(TextView textView, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(i);
        } else {
            textView.setTextAppearance(textView.getContext(), i);
        }
    }

    protected static boolean isNightMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & 48) == 32;
    }
}
