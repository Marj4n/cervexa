package com.tencent.mm.opensdk.diffdev.a;

import com.tencent.mm.opensdk.utils.Log;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;

/* loaded from: classes2.dex */
public final class e {
    public static byte[] a(String str, int i) throws IOException {
        String str2;
        StringBuilder sb;
        String message;
        if (str != null && str.length() != 0) {
            if (i >= 0) {
                try {
                } catch (Exception e) {
                    sb = new StringBuilder("httpGet, Exception ex = ");
                    message = e.getMessage();
                    sb.append(message);
                    str2 = sb.toString();
                    Log.e("MicroMsg.SDK.NetUtil", str2);
                    return null;
                } catch (IncompatibleClassChangeError e2) {
                    sb = new StringBuilder("httpGet, IncompatibleClassChangeError ex = ");
                    message = e2.getMessage();
                    sb.append(message);
                    str2 = sb.toString();
                    Log.e("MicroMsg.SDK.NetUtil", str2);
                    return null;
                } catch (Throwable th) {
                    sb = new StringBuilder("httpGet, Throwable ex = ");
                    message = th.getMessage();
                    sb.append(message);
                    str2 = sb.toString();
                    Log.e("MicroMsg.SDK.NetUtil", str2);
                    return null;
                }
            }
            return null;
        }
        str2 = "httpGet, url is null";
        Log.e("MicroMsg.SDK.NetUtil", str2);
        return null;
    }
}
