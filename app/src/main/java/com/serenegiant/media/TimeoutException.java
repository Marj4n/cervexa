package com.serenegiant.media;

/* loaded from: classes2.dex */
public class TimeoutException extends RuntimeException {
    private static final long serialVersionUID = -7207769104864850593L;

    public TimeoutException() {
    }

    public TimeoutException(String str) {
        super(str);
    }

    public TimeoutException(Throwable th) {
        super(th);
    }

    public TimeoutException(String str, Throwable th) {
        super(str, th);
    }
}
