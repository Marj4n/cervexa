package com.serenegiant.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/* loaded from: classes2.dex */
public class DiskUtils {
    public static String getCacheDir(Context context, String str) {
        return ((!"mounted".equals(Environment.getExternalStorageState()) || Environment.isExternalStorageRemovable()) ? context.getCacheDir() : context.getExternalCacheDir()).getPath() + File.separator + str;
    }
}
