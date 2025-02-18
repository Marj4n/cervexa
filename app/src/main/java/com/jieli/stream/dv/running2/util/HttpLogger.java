package com.jieli.stream.dv.running2.util;

import okhttp3.logging.HttpLoggingInterceptor;

/* loaded from: classes.dex */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private static boolean isLog;

    public static void setLog(boolean z) {
        isLog = z;
    }

    @Override // okhttp3.logging.HttpLoggingInterceptor.Logger
    public void log(String str) {
        if (isLog) {
            Dbug.i("okHttp", str);
        }
    }
}
