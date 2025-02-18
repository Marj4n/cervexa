package com.serenegiant.media;

import java.io.IOException;

/* loaded from: classes2.dex */
public class InsufficientFreespaceException extends IOException {
    public InsufficientFreespaceException() {
    }

    public InsufficientFreespaceException(String str) {
        super(str);
    }

    public InsufficientFreespaceException(String str, Throwable th) {
        super(str, th);
    }

    public InsufficientFreespaceException(Throwable th) {
        super(th);
    }
}
