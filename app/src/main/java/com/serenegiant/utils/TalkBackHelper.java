package com.serenegiant.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/* loaded from: classes2.dex */
public class TalkBackHelper {
    public static boolean isEnabled(Context context) {
        return ((AccessibilityManager) context.getSystemService("accessibility")).isEnabled();
    }

    public static void announceText(Context context, CharSequence charSequence) throws IllegalStateException {
        if (TextUtils.isEmpty(charSequence) || context == null) {
            return;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            if (obtain != null) {
                obtain.setEventType(16384);
                obtain.setClassName(TalkBackHelper.class.getName());
                obtain.setPackageName(context.getPackageName());
                obtain.getText().add(charSequence);
                accessibilityManager.sendAccessibilityEvent(obtain);
                return;
            }
            throw new IllegalStateException("failed to obtain AccessibilityEvent");
        }
        throw new IllegalStateException("AccessibilityManager is not available/or disabled");
    }

    public static void announceText(Context context, CharSequence[] charSequenceArr) throws IllegalStateException {
        if (charSequenceArr == null || charSequenceArr.length == 0 || context == null) {
            return;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            if (obtain != null) {
                obtain.setEventType(16384);
                obtain.setClassName(TalkBackHelper.class.getName());
                obtain.setPackageName(context.getPackageName());
                for (CharSequence charSequence : charSequenceArr) {
                    obtain.getText().add(charSequence);
                }
                accessibilityManager.sendAccessibilityEvent(obtain);
                return;
            }
            throw new IllegalStateException("failed to obtain AccessibilityEvent");
        }
        throw new IllegalStateException("AccessibilityManager is not available/or disabled");
    }
}
