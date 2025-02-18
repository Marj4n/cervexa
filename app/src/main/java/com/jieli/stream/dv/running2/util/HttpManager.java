package com.jieli.stream.dv.running2.util;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/* loaded from: classes.dex */
public class HttpManager {
    public static void downloadFile(String str, Callback callback) {
        if (TextUtils.isEmpty(str)) {
            Dbug.e("downloadFile", "url is null");
            return;
        }
        Request build = new Request.Builder().url(str).build();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        new OkHttpClient().newBuilder().writeTimeout(20L, TimeUnit.SECONDS).retryOnConnectionFailure(true).addInterceptor(httpLoggingInterceptor).build().newCall(build).enqueue(callback);
    }

    public static void uploadFile(String str, String str2, Callback callback) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        File file = new File(str2);
        if (file.exists() && file.isFile()) {
            String formatUrl = AppUtils.formatUrl(ClientManager.getClient().getConnectedIP(), 8080, str);
            if (TextUtils.isEmpty(formatUrl)) {
                return;
            }
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            new OkHttpClient().newBuilder().writeTimeout(20L, TimeUnit.SECONDS).retryOnConnectionFailure(true).addInterceptor(httpLoggingInterceptor).build().newCall(new Request.Builder().url(formatUrl).post(RequestBody.create(MediaType.parse(getMimeType(formatUrl)), file)).build()).enqueue(callback);
        }
    }

    private static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }
}
