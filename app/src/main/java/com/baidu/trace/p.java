package com.baidu.trace;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class p extends PhoneStateListener {
    public static int a;
    private TelephonyManager b;
    private a f;
    private int h;
    private int j;
    private int k;
    private List<Integer> c = new ArrayList();
    private List<Integer> d = new ArrayList();
    private int e = 0;
    private int g = 0;
    private String i = "";

    public class a extends PhoneStateListener {
        public a(p pVar) {
        }

        @Override // android.telephony.PhoneStateListener
        public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            p.a = signalStrength.isGsm() ? 99 != signalStrength.getGsmSignalStrength() ? (signalStrength.getGsmSignalStrength() << 1) - 113 : 0 : signalStrength.getCdmaDbm();
        }
    }

    public p(Context context) {
        this.f = null;
        this.b = (TelephonyManager) context.getSystemService("phone");
        a aVar = new a(this);
        this.f = aVar;
        TelephonyManager telephonyManager = this.b;
        if (telephonyManager != null) {
            telephonyManager.listen(aVar, 256);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void a(com.baidu.trace.a.c r10) {
        /*
            Method dump skipped, instructions count: 400
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.p.a(com.baidu.trace.a.c):void");
    }

    protected final boolean a() {
        return this.b != null;
    }

    protected final void b() {
        List<Integer> list = this.c;
        if (list != null) {
            list.clear();
            this.c = null;
        }
        List<Integer> list2 = this.d;
        if (list2 != null) {
            list2.clear();
            this.d = null;
        }
    }
}
