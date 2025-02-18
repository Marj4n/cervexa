package com.baidu.trace;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
final class at implements Runnable {
    private /* synthetic */ com.baidu.trace.a.g a;
    private /* synthetic */ long b;
    private /* synthetic */ ar c;

    at(ar arVar, com.baidu.trace.a.g gVar, long j) {
        this.c = arVar;
        this.a = gVar;
        this.b = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ar.a(this.c, new LatLng(this.a.i / 600000.0d, this.a.h / 600000.0d), this.b, CoordType.wgs84, this.a.d);
    }
}
