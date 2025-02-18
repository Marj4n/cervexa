package com.serenegiant.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class ResourceHelper {
    public static int get(Context context, String str, int i) throws Resources.NotFoundException, NumberFormatException, NullPointerException {
        if (!TextUtils.isEmpty(str) && str.startsWith("@")) {
            int identifier = context.getResources().getIdentifier(str.substring(1), null, context.getPackageName());
            return identifier > 0 ? context.getResources().getInteger(identifier) : i;
        }
        int i2 = 10;
        if (str != null && str.length() > 2 && str.charAt(0) == '0' && (str.charAt(1) == 'x' || str.charAt(1) == 'X')) {
            i2 = 16;
            str = str.substring(2);
        }
        return Integer.parseInt(str, i2);
    }

    public static boolean get(Context context, String str, boolean z) throws Resources.NotFoundException, NumberFormatException, NullPointerException {
        if (!"TRUE".equalsIgnoreCase(str)) {
            if ("FALSE".equalsIgnoreCase(str)) {
                return false;
            }
            if (!TextUtils.isEmpty(str) && str.startsWith("@")) {
                int identifier = context.getResources().getIdentifier(str.substring(1), null, context.getPackageName());
                if (identifier > 0) {
                    z = context.getResources().getBoolean(identifier);
                }
                return z;
            }
            int i = 10;
            if (str != null && str.length() > 2 && str.charAt(0) == '0' && (str.charAt(1) == 'x' || str.charAt(1) == 'X')) {
                i = 16;
                str = str.substring(2);
            }
            if (Integer.parseInt(str, i) == 0) {
                return false;
            }
        }
        return true;
    }

    public static String get(Context context, String str, String str2) throws Resources.NotFoundException, NullPointerException {
        if (str == null) {
            str = str2;
        }
        if (TextUtils.isEmpty(str) || !str.startsWith("@")) {
            return str;
        }
        int identifier = context.getResources().getIdentifier(str.substring(1), null, context.getPackageName());
        return identifier > 0 ? context.getResources().getString(identifier) : str;
    }

    public static CharSequence get(Context context, CharSequence charSequence, CharSequence charSequence2) throws Resources.NotFoundException, NullPointerException {
        if (charSequence == null) {
            charSequence = charSequence2;
        }
        if (TextUtils.isEmpty(charSequence)) {
            return charSequence;
        }
        String charSequence3 = charSequence.toString();
        if (!charSequence3.startsWith("@")) {
            return charSequence;
        }
        int identifier = context.getResources().getIdentifier(charSequence3.substring(1), null, context.getPackageName());
        return identifier > 0 ? context.getResources().getText(identifier) : charSequence;
    }
}
