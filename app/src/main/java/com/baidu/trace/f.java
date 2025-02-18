package com.baidu.trace;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import kotlin.UByte;

/* loaded from: classes.dex */
public final class f {
    private static Method d;
    private static Method e;
    private static Method f;
    private static Class<?> g;
    private static char[] m = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.".toCharArray();
    private Context a;
    private TelephonyManager b;
    private WifiManager h;
    private String l;
    private com.baidu.trace.a c = new com.baidu.trace.a(this, (byte) 0);
    private a i = null;
    private String j = null;
    private int k = 0;

    public class a {
        public List<ScanResult> a;
        private long c;

        public a(List<ScanResult> list) {
            this.a = null;
            this.c = 0L;
            this.a = list;
            this.c = System.currentTimeMillis();
            b();
        }

        static /* synthetic */ boolean a(a aVar) {
            long currentTimeMillis = System.currentTimeMillis() - aVar.c;
            return currentTimeMillis < 0 || currentTimeMillis > 500;
        }

        /*  JADX ERROR: JadxOverflowException in pass: LoopRegionVisitor
            jadx.core.utils.exceptions.JadxOverflowException: LoopRegionVisitor.assignOnlyInLoop endless recursion
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        private void b() {
            /*
                r7 = this;
                int r0 = r7.a()
                if (r0 > 0) goto L7
                return
            L7:
                java.util.List<android.net.wifi.ScanResult> r0 = r7.a
                int r0 = r0.size()
                r1 = 1
                int r0 = r0 - r1
                r2 = 1
            L10:
                if (r0 <= 0) goto L4d
                if (r2 == 0) goto L4d
                r2 = 0
                r3 = 0
            L16:
                if (r2 >= r0) goto L49
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                java.lang.Object r4 = r4.get(r2)
                android.net.wifi.ScanResult r4 = (android.net.wifi.ScanResult) r4
                int r4 = r4.level
                java.util.List<android.net.wifi.ScanResult> r5 = r7.a
                int r6 = r2 + 1
                java.lang.Object r5 = r5.get(r6)
                android.net.wifi.ScanResult r5 = (android.net.wifi.ScanResult) r5
                int r5 = r5.level
                if (r4 >= r5) goto L47
                java.util.List<android.net.wifi.ScanResult> r3 = r7.a
                java.lang.Object r3 = r3.get(r6)
                android.net.wifi.ScanResult r3 = (android.net.wifi.ScanResult) r3
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                java.lang.Object r5 = r4.get(r2)
                r4.set(r6, r5)
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                r4.set(r2, r3)
                r3 = 1
            L47:
                r2 = r6
                goto L16
            L49:
                int r0 = r0 + (-1)
                r2 = r3
                goto L10
            L4d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.f.a.b():void");
        }

        public final int a() {
            List<ScanResult> list = this.a;
            if (list == null) {
                return 0;
            }
            return list.size();
        }
    }

    public f(Context context) {
        String str = null;
        this.a = null;
        this.b = null;
        this.h = null;
        this.l = null;
        Context applicationContext = context.getApplicationContext();
        this.a = applicationContext;
        String packageName = applicationContext.getPackageName();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.a.getSystemService("phone");
            this.b = telephonyManager;
            str = telephonyManager.getDeviceId();
        } catch (Exception unused) {
        }
        this.l = "&" + packageName + "&" + str;
        this.h = (WifiManager) this.a.getSystemService("wifi");
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x00b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00b6 A[Catch: Exception -> 0x00e6, TRY_LEAVE, TryCatch #0 {Exception -> 0x00e6, blocks: (B:14:0x00b2, B:16:0x00b6), top: B:13:0x00b2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.baidu.trace.a a(android.telephony.CellInfo r7) {
        /*
            r6 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            int r0 = r0.intValue()
            r1 = 17
            if (r0 >= r1) goto L10
            r7 = 0
            return r7
        L10:
            com.baidu.trace.a r1 = new com.baidu.trace.a
            r2 = 0
            r1.<init>(r6, r2)
            boolean r3 = r7 instanceof android.telephony.CellInfoGsm
            r4 = 1
            r5 = 103(0x67, float:1.44E-43)
            if (r3 == 0) goto L50
            r2 = r7
            android.telephony.CellInfoGsm r2 = (android.telephony.CellInfoGsm) r2
            android.telephony.CellIdentityGsm r2 = r2.getCellIdentity()
            int r3 = r2.getMcc()
            int r3 = b(r3)
            r1.c = r3
            int r3 = r2.getMnc()
            int r3 = b(r3)
            r1.d = r3
            int r3 = r2.getLac()
            int r3 = b(r3)
            r1.a = r3
            int r2 = r2.getCid()
        L46:
            int r2 = b(r2)
            r1.b = r2
            r1.e = r5
        L4e:
            r2 = 1
            goto Lac
        L50:
            boolean r3 = r7 instanceof android.telephony.CellInfoCdma
            if (r3 == 0) goto L7e
            r2 = r7
            android.telephony.CellInfoCdma r2 = (android.telephony.CellInfoCdma) r2
            android.telephony.CellIdentityCdma r2 = r2.getCellIdentity()
            int r3 = r2.getSystemId()
            int r3 = b(r3)
            r1.d = r3
            int r3 = r2.getNetworkId()
            int r3 = b(r3)
            r1.a = r3
            int r2 = r2.getBasestationId()
            int r2 = b(r2)
            r1.b = r2
            r2 = 119(0x77, float:1.67E-43)
            r1.e = r2
            goto L4e
        L7e:
            boolean r3 = r7 instanceof android.telephony.CellInfoLte
            if (r3 == 0) goto Lac
            r2 = r7
            android.telephony.CellInfoLte r2 = (android.telephony.CellInfoLte) r2
            android.telephony.CellIdentityLte r2 = r2.getCellIdentity()
            int r3 = r2.getMcc()
            int r3 = b(r3)
            r1.c = r3
            int r3 = r2.getMnc()
            int r3 = b(r3)
            r1.d = r3
            int r3 = r2.getTac()
            int r3 = b(r3)
            r1.a = r3
            int r2 = r2.getCi()
            goto L46
        Lac:
            r3 = 18
            if (r0 < r3) goto Le6
            if (r2 != 0) goto Le6
            boolean r0 = r7 instanceof android.telephony.CellInfoWcdma     // Catch: java.lang.Exception -> Le6
            if (r0 == 0) goto Le6
            android.telephony.CellInfoWcdma r7 = (android.telephony.CellInfoWcdma) r7     // Catch: java.lang.Exception -> Le6
            android.telephony.CellIdentityWcdma r7 = r7.getCellIdentity()     // Catch: java.lang.Exception -> Le6
            int r0 = r7.getMcc()     // Catch: java.lang.Exception -> Le6
            int r0 = b(r0)     // Catch: java.lang.Exception -> Le6
            r1.c = r0     // Catch: java.lang.Exception -> Le6
            int r0 = r7.getMnc()     // Catch: java.lang.Exception -> Le6
            int r0 = b(r0)     // Catch: java.lang.Exception -> Le6
            r1.d = r0     // Catch: java.lang.Exception -> Le6
            int r0 = r7.getLac()     // Catch: java.lang.Exception -> Le6
            int r0 = b(r0)     // Catch: java.lang.Exception -> Le6
            r1.a = r0     // Catch: java.lang.Exception -> Le6
            int r7 = r7.getCid()     // Catch: java.lang.Exception -> Le6
            int r7 = b(r7)     // Catch: java.lang.Exception -> Le6
            r1.b = r7     // Catch: java.lang.Exception -> Le6
            r1.e = r5     // Catch: java.lang.Exception -> Le6
        Le6:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.f.a(android.telephony.CellInfo):com.baidu.trace.a");
    }

    /* JADX WARN: Can't wrap try/catch for region: R(18:0|1|(2:2|3)|(2:5|(1:7)(12:8|9|10|(1:12)|13|14|(1:63)|18|(8:29|(1:31)(1:62)|32|33|(4:36|(2:38|(2:(1:46)|(1:51))(2:42|43))(2:59|60)|44|34)|61|(1:53)(1:57)|(1:55))(1:20)|(1:22)|23|(1:25)(2:27|28)))|66|(6:71|(8:75|76|(3:78|(1:80)|81)|82|(2:84|(2:89|85))(1:97)|91|(1:93)|94)|99|(1:101)(2:105|(4:107|(2:120|121)|109|(4:113|114|(1:116)|117)))|102|(1:104))|9|10|(0)|13|14|(1:16)|63|18|(0)(0)|(0)|23|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0210, code lost:
    
        r1 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0213  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0228 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0150 A[Catch: Exception -> 0x0210, TryCatch #0 {Exception -> 0x0210, blocks: (B:14:0x012b, B:16:0x012f, B:18:0x0144, B:29:0x0150, B:32:0x015d, B:36:0x0171, B:38:0x017d, B:40:0x01a1, B:42:0x01ab, B:44:0x01d3, B:46:0x01b7, B:53:0x01dc, B:55:0x01fc, B:63:0x0137), top: B:13:0x012b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String a(int r18) {
        /*
            Method dump skipped, instructions count: 588
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.f.a(int):java.lang.String");
    }

    private static String a(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        byte nextInt = (byte) new Random().nextInt(255);
        byte nextInt2 = (byte) new Random().nextInt(255);
        byte[] bArr = new byte[bytes.length + 2];
        int length = bytes.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            bArr[i2] = (byte) (bytes[i] ^ nextInt);
            i++;
            i2++;
        }
        bArr[i2] = nextInt;
        bArr[i2 + 1] = nextInt2;
        return a(bArr);
    }

    private static String a(byte[] bArr) {
        boolean z;
        char[] cArr = new char[((bArr.length + 2) / 3) << 2];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            int i3 = (bArr[i] & UByte.MAX_VALUE) << 8;
            int i4 = i + 1;
            boolean z2 = true;
            if (i4 < bArr.length) {
                i3 |= bArr[i4] & UByte.MAX_VALUE;
                z = true;
            } else {
                z = false;
            }
            int i5 = i3 << 8;
            int i6 = i + 2;
            if (i6 < bArr.length) {
                i5 |= bArr[i6] & UByte.MAX_VALUE;
            } else {
                z2 = false;
            }
            int i7 = 64;
            cArr[i2 + 3] = m[z2 ? 63 - (i5 & 63) : 64];
            int i8 = i5 >> 6;
            int i9 = i2 + 2;
            char[] cArr2 = m;
            if (z) {
                i7 = 63 - (i8 & 63);
            }
            cArr[i9] = cArr2[i7];
            int i10 = i8 >> 6;
            char[] cArr3 = m;
            cArr[i2 + 1] = cArr3[63 - (i10 & 63)];
            cArr[i2] = cArr3[63 - ((i10 >> 6) & 63)];
            i += 3;
            i2 += 4;
        }
        return new String(cArr);
    }

    private static int b(int i) {
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i;
    }

    private com.baidu.trace.a b() {
        boolean b;
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 17) {
            return null;
        }
        try {
            List<CellInfo> allCellInfo = this.b.getAllCellInfo();
            if (allCellInfo == null || allCellInfo.size() <= 0) {
                return null;
            }
            com.baidu.trace.a aVar = null;
            for (CellInfo cellInfo : allCellInfo) {
                try {
                    if (cellInfo.isRegistered() && (aVar = a(cellInfo)) != null) {
                        b = aVar.b();
                        if (b) {
                            return aVar;
                        }
                        return null;
                    }
                } catch (Exception unused) {
                }
            }
            return aVar;
        } catch (Exception | NoSuchMethodError unused2) {
            return null;
        }
    }

    private boolean c() {
        this.j = null;
        this.k = 0;
        WifiInfo connectionInfo = this.h.getConnectionInfo();
        if (connectionInfo == null) {
            return false;
        }
        try {
            String bssid = connectionInfo.getBSSID();
            String replace = bssid != null ? bssid.replace(":", "") : "";
            if (replace.length() != 12) {
                return false;
            }
            this.j = new String(replace);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public final String a() {
        try {
            return a(10);
        } catch (Exception unused) {
            return null;
        }
    }
}
