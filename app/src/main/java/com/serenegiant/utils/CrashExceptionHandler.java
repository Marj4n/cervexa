package com.serenegiant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
    static final String LOG_NAME = "crashrepo.txt";
    static final String MAIL_TO = "t_saki@serenegiant.com";
    private final Thread.UncaughtExceptionHandler mHandler;
    private final WeakReference<Context> mWeakContext;
    private final WeakReference<PackageInfo> mWeakPackageInfo;

    public static void registerCrashHandler(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(context));
    }

    private CrashExceptionHandler(Context context) {
        this.mWeakContext = new WeakReference<>(context);
        try {
            this.mWeakPackageInfo = new WeakReference<>(context.getPackageManager().getPackageInfo(context.getPackageName(), 0));
            this.mHandler = Thread.getDefaultUncaughtExceptionHandler();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0064, code lost:
    
        if (r1 == null) goto L32;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0074 A[Catch: Exception -> 0x0079, TRY_LEAVE, TryCatch #3 {Exception -> 0x0079, blocks: (B:31:0x0070, B:33:0x0074), top: B:30:0x0070 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v13, types: [java.lang.String] */
    @Override // java.lang.Thread.UncaughtExceptionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void uncaughtException(java.lang.Thread r5, java.lang.Throwable r6) {
        /*
            r4 = this;
            java.lang.ref.WeakReference<android.content.Context> r0 = r4.mWeakContext
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            if (r0 == 0) goto L70
            r1 = 0
            java.lang.String r2 = "crashrepo.txt"
            r3 = 0
            java.io.FileOutputStream r0 = r0.openFileOutput(r2, r3)     // Catch: java.lang.Throwable -> L57 org.json.JSONException -> L59 java.io.FileNotFoundException -> L60
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch: java.lang.Throwable -> L57 org.json.JSONException -> L59 java.io.FileNotFoundException -> L60
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L57 org.json.JSONException -> L59 java.io.FileNotFoundException -> L60
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r0.<init>()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            java.lang.String r1 = "Build"
            org.json.JSONObject r3 = r4.getBuildInfo()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r0.put(r1, r3)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            java.lang.String r1 = "PackageInfo"
            org.json.JSONObject r3 = r4.getPackageInfo()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r0.put(r1, r3)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            java.lang.String r1 = "Exception"
            org.json.JSONObject r3 = r4.getExceptionInfo(r6)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r0.put(r1, r3)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            java.lang.String r1 = "SharedPreferences"
            org.json.JSONObject r3 = r4.getPreferencesInfo()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r0.put(r1, r3)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r2.print(r0)     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r2.flush()     // Catch: java.lang.Throwable -> L4e org.json.JSONException -> L51 java.io.FileNotFoundException -> L54
            r2.close()
            goto L70
        L4e:
            r5 = move-exception
            r1 = r2
            goto L6a
        L51:
            r0 = move-exception
            r1 = r2
            goto L5a
        L54:
            r0 = move-exception
            r1 = r2
            goto L61
        L57:
            r5 = move-exception
            goto L6a
        L59:
            r0 = move-exception
        L5a:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L57
            if (r1 == 0) goto L70
            goto L66
        L60:
            r0 = move-exception
        L61:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L57
            if (r1 == 0) goto L70
        L66:
            r1.close()
            goto L70
        L6a:
            if (r1 == 0) goto L6f
            r1.close()
        L6f:
            throw r5
        L70:
            java.lang.Thread$UncaughtExceptionHandler r0 = r4.mHandler     // Catch: java.lang.Exception -> L79
            if (r0 == 0) goto L79
            java.lang.Thread$UncaughtExceptionHandler r0 = r4.mHandler     // Catch: java.lang.Exception -> L79
            r0.uncaughtException(r5, r6)     // Catch: java.lang.Exception -> L79
        L79:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.utils.CrashExceptionHandler.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }

    private JSONObject getBuildInfo() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("BRAND", Build.BRAND);
        jSONObject.put("MODEL", Build.MODEL);
        jSONObject.put("DEVICE", Build.DEVICE);
        jSONObject.put("MANUFACTURER", Build.MANUFACTURER);
        jSONObject.put("VERSION.SDK_INT", Build.VERSION.SDK_INT);
        jSONObject.put("VERSION.RELEASE", Build.VERSION.RELEASE);
        return jSONObject;
    }

    private JSONObject getPackageInfo() throws JSONException {
        PackageInfo packageInfo = this.mWeakPackageInfo.get();
        JSONObject jSONObject = new JSONObject();
        if (packageInfo != null) {
            jSONObject.put("packageName", packageInfo.packageName);
            jSONObject.put("versionCode", packageInfo.versionCode);
            jSONObject.put("versionName", packageInfo.versionName);
        }
        return jSONObject;
    }

    private JSONObject getExceptionInfo(Throwable th) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(BaseFragment.DATA_NAME, th.getClass().getName());
        jSONObject.put("cause", th.getCause());
        jSONObject.put("message", th.getMessage());
        JSONArray jSONArray = new JSONArray();
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            jSONArray.put("at " + LogUtils.getMetaInfo(stackTraceElement));
        }
        jSONObject.put("stacktrace", jSONArray);
        return jSONObject;
    }

    private JSONObject getPreferencesInfo() throws JSONException {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mWeakContext.get());
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, ?> entry : defaultSharedPreferences.getAll().entrySet()) {
            jSONObject.put(entry.getKey(), entry.getValue());
        }
        return jSONObject;
    }
}
