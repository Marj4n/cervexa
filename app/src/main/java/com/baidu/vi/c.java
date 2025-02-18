package com.baidu.vi;

import android.net.NetworkInfo;

/* loaded from: classes.dex */
public class c {
    public String a;
    public int b;
    public int c;

    /* renamed from: com.baidu.vi.c$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[NetworkInfo.State.values().length];
            a = iArr;
            try {
                iArr[NetworkInfo.State.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[NetworkInfo.State.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[NetworkInfo.State.DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[NetworkInfo.State.DISCONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[NetworkInfo.State.SUSPENDED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public c(NetworkInfo networkInfo) {
        this.a = networkInfo.getTypeName();
        this.b = networkInfo.getType();
        int i = AnonymousClass1.a[networkInfo.getState().ordinal()];
        if (i == 1) {
            this.c = 2;
        } else if (i != 2) {
            this.c = 0;
        } else {
            this.c = 1;
        }
    }
}
