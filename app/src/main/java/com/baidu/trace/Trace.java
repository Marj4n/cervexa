package com.baidu.trace;

import android.app.Notification;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class Trace {
    protected static int a = 5;
    protected static int b = 30;
    protected static int c = 0;
    protected static int d = 600000;
    protected static String e = "";
    private long f;
    private String g;
    private boolean h;
    private Notification i;

    public Trace() {
        this.f = 0L;
        this.g = "";
        this.h = false;
        this.i = null;
    }

    public Trace(long j, String str) {
        this.f = 0L;
        this.g = "";
        this.h = false;
        this.i = null;
        this.f = j;
        setEntityName(str);
    }

    public Trace(long j, String str, boolean z) {
        this.f = 0L;
        this.g = "";
        this.h = false;
        this.i = null;
        this.f = j;
        this.h = z;
        setEntityName(str);
    }

    public Trace(long j, String str, boolean z, Notification notification) {
        this.f = 0L;
        this.g = "";
        this.h = false;
        this.i = null;
        this.f = j;
        this.g = str;
        this.h = z;
        this.i = notification;
    }

    protected static boolean a(int i) {
        if (i < 50) {
            return false;
        }
        c = i;
        return true;
    }

    protected static boolean a(int i, int i2) {
        if (i <= 0 || i > 300 || i2 < i || i2 < 2 || i2 > 300 || i2 % i != 0) {
            return false;
        }
        a = i;
        b = i2;
        return true;
    }

    protected static boolean b(int i) {
        if (i <= 0) {
            return false;
        }
        d = i;
        return true;
    }

    public String getEntityName() {
        return this.g;
    }

    public Notification getNotification() {
        return this.i;
    }

    public long getServiceId() {
        return this.f;
    }

    public boolean isNeedObjectStorage() {
        return this.h;
    }

    public void setEntityName(String str) {
        this.g = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        e = str;
    }

    public void setNeedObjectStorage(boolean z) {
        this.h = z;
    }

    public void setNotification(Notification notification) {
        this.i = notification;
    }

    public void setServiceId(long j) {
        this.f = j;
    }
}
