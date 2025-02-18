package com.serenegiant.cameracommon;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ViewHelper {
    public static void setBackgroundAll(ViewGroup viewGroup, int i) {
        int childCount = viewGroup.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = viewGroup.getChildAt(i2);
            childAt.setBackgroundColor(i);
            if (childAt instanceof ViewGroup) {
                setBackgroundAll((ViewGroup) childAt, i);
            }
        }
    }

    public static void setBackgroundAll(ViewGroup viewGroup, Drawable drawable) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            childAt.setBackground(drawable);
            if (childAt instanceof ViewGroup) {
                setBackgroundAll((ViewGroup) childAt, drawable);
            }
        }
    }
}
