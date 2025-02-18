package okhttp3.internal.http;

import com.android.volley.toolbox.HttpClientStack;
import com.jieli.lib.dv.control.utils.Operation;
import com.tencent.connect.common.Constants;

/* loaded from: classes2.dex */
public final class HttpMethod {
    public static boolean invalidatesCache(String str) {
        return str.equals(Constants.HTTP_POST) || str.equals(HttpClientStack.HttpPatch.METHOD_NAME) || str.equals(Operation.TYPE_PUT) || str.equals("DELETE") || str.equals("MOVE");
    }

    public static boolean requiresRequestBody(String str) {
        return str.equals(Constants.HTTP_POST) || str.equals(Operation.TYPE_PUT) || str.equals(HttpClientStack.HttpPatch.METHOD_NAME) || str.equals("PROPPATCH") || str.equals("REPORT");
    }

    public static boolean permitsRequestBody(String str) {
        return requiresRequestBody(str) || str.equals("OPTIONS") || str.equals("DELETE") || str.equals("PROPFIND") || str.equals("MKCOL") || str.equals("LOCK");
    }

    public static boolean redirectsWithBody(String str) {
        return str.equals("PROPFIND");
    }

    public static boolean redirectsToGet(String str) {
        return !str.equals("PROPFIND");
    }

    private HttpMethod() {
    }
}
