package com.android.volley;

/* loaded from: classes.dex */
public class RedirectError extends VolleyError {
    public RedirectError() {
    }

    public RedirectError(Throwable th) {
        super(th);
    }

    public RedirectError(NetworkResponse networkResponse) {
        super(networkResponse);
    }
}
