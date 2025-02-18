package com.serenegiant.utils;

import android.content.SharedPreferences;

/* loaded from: classes2.dex */
public class PrefHelper {
    public static short get(SharedPreferences sharedPreferences, String str, short s) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return s;
        }
        try {
            return (short) sharedPreferences.getInt(str, s);
        } catch (Exception unused) {
            return ObjectHelper.asShort(getObject(sharedPreferences, str, Short.valueOf(s)), s);
        }
    }

    public static int get(SharedPreferences sharedPreferences, String str, int i) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return i;
        }
        try {
            return sharedPreferences.getInt(str, i);
        } catch (Exception unused) {
            return ObjectHelper.asInt(getObject(sharedPreferences, str, Integer.valueOf(i)), i);
        }
    }

    public static long get(SharedPreferences sharedPreferences, String str, long j) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return j;
        }
        try {
            return sharedPreferences.getLong(str, j);
        } catch (Exception unused) {
            return ObjectHelper.asLong(getObject(sharedPreferences, str, Long.valueOf(j)), j);
        }
    }

    public static float get(SharedPreferences sharedPreferences, String str, float f) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return f;
        }
        try {
            return sharedPreferences.getFloat(str, f);
        } catch (Exception unused) {
            return ObjectHelper.asFloat(getObject(sharedPreferences, str, Float.valueOf(f)), f);
        }
    }

    public static double get(SharedPreferences sharedPreferences, String str, double d) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return d;
        }
        try {
            return Double.parseDouble(sharedPreferences.getString(str, Double.toString(d)));
        } catch (Exception unused) {
            return ObjectHelper.asDouble(getObject(sharedPreferences, str, Double.valueOf(d)), d);
        }
    }

    public static boolean get(SharedPreferences sharedPreferences, String str, boolean z) {
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return z;
        }
        try {
            return sharedPreferences.getBoolean(str, z);
        } catch (Exception unused) {
            return ObjectHelper.asBoolean(Boolean.valueOf(get(sharedPreferences, str, z)), z);
        }
    }

    public static final Object getObject(SharedPreferences sharedPreferences, String str) {
        return getObject(sharedPreferences, str, null);
    }

    public static final Object getObject(SharedPreferences sharedPreferences, String str, Object obj) {
        return (sharedPreferences == null || !sharedPreferences.contains(str)) ? obj : sharedPreferences.getAll().get(str);
    }
}
