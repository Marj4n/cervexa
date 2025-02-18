package com.baidu.mapapi.http;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.baidu.mapapi.JNIInitializer;
import com.baidu.mapapi.common.Logger;
import com.baidu.platform.comapi.util.e;
import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class HttpClient {
    HttpURLConnection a;
    private String b = null;
    private String c = null;
    private int d;
    private int e;
    private String f;
    private ProtoResultCallback g;

    public enum HttpStateError {
        NO_ERROR,
        NETWORK_ERROR,
        INNER_ERROR,
        REQUEST_ERROR,
        SERVER_ERROR
    }

    public static abstract class ProtoResultCallback {
        public abstract void onFailed(HttpStateError httpStateError);

        public abstract void onSuccess(String str);
    }

    public HttpClient(String str, ProtoResultCallback protoResultCallback) {
        this.f = str;
        this.g = protoResultCallback;
    }

    private HttpURLConnection a() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.b).openConnection();
            httpURLConnection.setRequestMethod(this.f);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(this.d);
            httpURLConnection.setReadTimeout(this.e);
            return httpURLConnection;
        } catch (Exception e) {
            if (Logger.debugEnable()) {
                e.printStackTrace();
                return null;
            }
            Logger.logW("HttpClient", e.getMessage());
            return null;
        }
    }

    public static String getAuthToken() {
        return e.z;
    }

    public static String getPhoneInfo() {
        return e.c();
    }

    protected boolean checkNetwork() {
        NetworkInfo activeNetworkInfo;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) JNIInitializer.getCachedContext().getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
                return false;
            }
            return activeNetworkInfo.isAvailable();
        } catch (Exception e) {
            if (Logger.debugEnable()) {
                e.printStackTrace();
            } else {
                Logger.logW("HttpClient", e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [int] */
    /* JADX WARN: Type inference failed for: r1v17, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.lang.StringBuilder] */
    protected void request(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        InputStream inputStream;
        BufferedReader bufferedReader2;
        Exception e;
        ?? r1;
        this.b = str;
        if (!checkNetwork()) {
            this.g.onFailed(HttpStateError.NETWORK_ERROR);
            return;
        }
        HttpURLConnection a = a();
        this.a = a;
        if (a == null) {
            this.g.onFailed(HttpStateError.INNER_ERROR);
            return;
        }
        if (TextUtils.isEmpty(this.b)) {
            this.g.onFailed(HttpStateError.REQUEST_ERROR);
            return;
        }
        try {
            this.a.connect();
            try {
                try {
                    r1 = this.a.getResponseCode();
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    if (200 != r1) {
                        HttpStateError httpStateError = HttpStateError.NO_ERROR;
                        HttpStateError httpStateError2 = r1 >= 500 ? HttpStateError.SERVER_ERROR : r1 >= 400 ? HttpStateError.REQUEST_ERROR : HttpStateError.INNER_ERROR;
                        if (Logger.debugEnable()) {
                            Logger.logW("HttpClient", this.a.getErrorStream().toString());
                        } else {
                            Logger.logW("HttpClient", "Get response from server failed, http response code=" + r1 + ", error=" + httpStateError2);
                        }
                        this.g.onFailed(httpStateError2);
                        if (this.a != null) {
                            this.a.disconnect();
                            return;
                        }
                        return;
                    }
                    r1 = this.a.getInputStream();
                    bufferedReader2 = new BufferedReader(new InputStreamReader((InputStream) r1, Key.STRING_CHARSET_NAME));
                    try {
                        StringBuffer stringBuffer = new StringBuffer();
                        while (true) {
                            int read = bufferedReader2.read();
                            if (read == -1) {
                                break;
                            } else {
                                stringBuffer.append((char) read);
                            }
                        }
                        this.c = stringBuffer.toString();
                        if (r1 != 0) {
                            bufferedReader2.close();
                            r1.close();
                        }
                        if (this.a != null) {
                            this.a.disconnect();
                        }
                        this.g.onSuccess(this.c);
                    } catch (Exception e2) {
                        e = e2;
                        if (Logger.debugEnable()) {
                            e.printStackTrace();
                        } else {
                            Logger.logW("HttpClient", e.getMessage());
                        }
                        this.g.onFailed(HttpStateError.INNER_ERROR);
                        if (r1 != 0 && bufferedReader2 != null) {
                            bufferedReader2.close();
                            r1.close();
                        }
                        if (this.a != null) {
                            this.a.disconnect();
                        }
                    }
                } catch (Exception e3) {
                    bufferedReader2 = null;
                    e = e3;
                } catch (Throwable th3) {
                    bufferedReader = null;
                    th = th3;
                    inputStream = r1;
                    if (inputStream != null && bufferedReader != null) {
                        bufferedReader.close();
                        inputStream.close();
                    }
                    if (this.a != null) {
                        this.a.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                bufferedReader2 = null;
                e = e4;
                r1 = 0;
            } catch (Throwable th4) {
                bufferedReader = null;
                th = th4;
                inputStream = null;
            }
        } catch (Exception e5) {
            if (Logger.debugEnable()) {
                e5.printStackTrace();
            } else {
                Logger.logW("HttpClient", e5.getMessage());
            }
            this.g.onFailed(HttpStateError.INNER_ERROR);
        }
    }

    public void setMaxTimeOut(int i) {
        this.d = i;
    }

    public void setReadTimeOut(int i) {
        this.e = i;
    }
}
