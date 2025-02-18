package com.baidu.trace;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.baidu.lbsapi.auth.tracesdk.LBSAuthManager;
import com.baidu.trace.IService;
import com.baidu.trace.api.fence.CircleFence;
import com.baidu.trace.c.f;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.ProtocolType;
import com.baidu.trace.model.TraceLocation;
import com.github.mikephil.charting.utils.Utils;
import com.serenegiant.net.NetworkChangedReceiver;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.apache.commons.net.imap.IMAP;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class LBSTraceService extends Service {
    private Context e;
    private s f;
    private a a = new a(this);
    private ServiceBinder b = new ServiceBinder(this);
    private com.baidu.trace.b.d c = null;
    private bd d = null;
    private boolean g = false;
    private IListener h = null;
    private boolean i = false;
    private boolean j = false;
    private com.baidu.trace.b.c k = new com.baidu.trace.b.c();
    private int l = LocationMode.High_Accuracy.ordinal();
    private int m = ProtocolType.HTTPS.ordinal();
    private f.b n = f.b.NOT_START;

    /* renamed from: com.baidu.trace.LBSTraceService$1, reason: invalid class name */
    final /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[f.b.values().length];
            a = iArr;
            try {
                iArr[f.b.LOGGING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[f.b.LOGOUTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[f.b.NOT_START.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[f.b.STARTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    static class ServiceBinder extends IService.Stub {
        private WeakReference<LBSTraceService> service;

        public ServiceBinder(LBSTraceService lBSTraceService) {
            this.service = null;
            this.service = new WeakReference<>(lBSTraceService);
        }

        @Override // com.baidu.trace.IService
        public void handleExtendedOperate(int i) throws RemoteException {
            if (this.service.get().g) {
                g.b();
                this.service.get().a((short) 18, com.baidu.trace.c.e.a(), null);
            }
        }

        @Override // com.baidu.trace.IService
        public boolean handleLocalFence(int i, long j, String str) throws RemoteException {
            SharedPreferences.Editor edit = this.service.get().e.getSharedPreferences("com.baidu.trace_fence_status", 0).edit();
            if (i == 0) {
                JSONObject jSONObject = new JSONObject(str);
                CircleFence buildLocalFence = CircleFence.buildLocalFence(j, null, null, null, Utils.DOUBLE_EPSILON, 0, CoordType.bd09ll);
                com.baidu.trace.a.a(jSONObject, buildLocalFence);
                if (ar.a != null) {
                    ar.a.a(j, buildLocalFence);
                }
            } else if (i == 1) {
                JSONObject jSONObject2 = new JSONObject(str);
                CircleFence buildLocalFence2 = CircleFence.buildLocalFence(j, null, null, null, Utils.DOUBLE_EPSILON, 0, CoordType.bd09ll);
                com.baidu.trace.a.a(jSONObject2, buildLocalFence2);
                if (ar.a != null) {
                    ar.a.a(j, buildLocalFence2);
                    edit.remove(String.valueOf(j));
                    edit.apply();
                }
            } else if (i != 2) {
                if (i == 3 && ar.a != null) {
                    ar.a.c();
                    edit.clear();
                    edit.apply();
                }
            } else if (ar.a != null) {
                try {
                    for (String str2 : str.split(",")) {
                        ar.a.a(Long.valueOf(str2).longValue());
                        edit.remove(str2);
                        edit.apply();
                    }
                } catch (JSONException | Exception unused) {
                }
            }
            return false;
        }

        @Override // com.baidu.trace.IService
        public boolean queryRealTimeLoc(TraceLocation traceLocation) throws RemoteException {
            return false;
        }

        @Override // com.baidu.trace.IService
        public void registerListener(IListener iListener) throws RemoteException {
            this.service.get().setClientListener(iListener);
            if (this.service.get().i) {
                iListener.startTraceCallback(19);
                LBSTraceService.a(this.service.get(), false);
            }
        }

        @Override // com.baidu.trace.IService
        public boolean setCacheSize(int i) throws RemoteException {
            return this.service.get().b(i);
        }

        @Override // com.baidu.trace.IService
        public boolean setInterval(int i, int i2) throws RemoteException {
            this.service.get().a(i, i2);
            return true;
        }

        @Override // com.baidu.trace.IService
        public boolean setLocTimeOffset(int i) throws RemoteException {
            return this.service.get().c(i);
        }

        @Override // com.baidu.trace.IService
        public boolean setLocationMode(int i) throws RemoteException {
            return this.service.get().d(i);
        }

        @Override // com.baidu.trace.IService
        public boolean setProtocolType(int i) throws RemoteException {
            com.baidu.trace.c.a.b = i == 0 ? ProtocolType.HTTP : ProtocolType.HTTPS;
            return true;
        }

        @Override // com.baidu.trace.IService
        public int startGather() throws RemoteException {
            return LBSTraceService.c(this.service.get());
        }

        @Override // com.baidu.trace.IService
        public int stopGather() throws RemoteException {
            return LBSTraceService.b(this.service.get());
        }

        @Override // com.baidu.trace.IService
        public void stopTrace(long j, String str) throws RemoteException {
            try {
                this.service.get().f();
            } catch (RemoteException unused) {
            }
        }

        @Override // com.baidu.trace.IService
        public void unregisterListener() throws RemoteException {
            this.service.get().setClientListener(null);
        }
    }

    static class a extends Handler {
        private WeakReference<LBSTraceService> a;

        public a(LBSTraceService lBSTraceService) {
            this.a = null;
            this.a = new WeakReference<>(lBSTraceService);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            super.handleMessage(message);
            LBSTraceService lBSTraceService = this.a.get();
            if (lBSTraceService == null) {
            }
            int i = message.what;
            if (i == 1) {
                lBSTraceService.a();
                return;
            }
            if (i == 4) {
                lBSTraceService.d();
                return;
            }
            if (i == 19) {
                lBSTraceService.e();
                return;
            }
            if (i == 141) {
                lBSTraceService.e(message.arg1);
                return;
            }
            if (i == 161) {
                lBSTraceService.a(message);
                return;
            }
            if (i == 163) {
                lBSTraceService.b(message);
                return;
            }
            if (i == 23) {
                lBSTraceService.g();
                return;
            }
            if (i == 24) {
                lBSTraceService.h();
                return;
            }
            switch (i) {
                case 28:
                    lBSTraceService.b();
                    break;
                case 29:
                    lBSTraceService.c();
                    break;
                case 30:
                    lBSTraceService.a(message.arg1);
                    break;
                case 31:
                    if (lBSTraceService.g) {
                        lBSTraceService.a((short) 18, com.baidu.trace.c.e.a(), null);
                        break;
                    }
                    break;
                case 32:
                    LBSTraceService.a(lBSTraceService, message.arg1);
                    break;
            }
        }
    }

    static {
        LBSTraceService.class.getSimpleName();
    }

    private static HashMap<String, String> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String[] split = str.split("-\\|-");
            if (split != null && split.length >= 10) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ak", split[0]);
                hashMap.put("serviceId", split[1]);
                hashMap.put("entityName", split[2]);
                hashMap.put("mcode", split[3]);
                hashMap.put("pcn", split[4]);
                hashMap.put("md", split[5]);
                hashMap.put("cpu", split[6]);
                hashMap.put("locationMode", split[7]);
                hashMap.put("protocolType", split[8]);
                hashMap.put("isNeedObjectStorage", split[9]);
                return hashMap;
            }
        } catch (Exception unused) {
        }
        return null;
    }

    static /* synthetic */ void a(LBSTraceService lBSTraceService, int i) {
        String str = null;
        if (i == 0) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("accessKey", g.a);
                jSONObject.put("secretKey", g.c);
                jSONObject.put("token", g.d);
                jSONObject.put("expireTime", g.b);
                str = jSONObject.toString();
            } catch (JSONException unused) {
                i = 1;
            }
        }
        IListener iListener = lBSTraceService.h;
        if (iListener != null) {
            try {
                iListener.extendedOperationCallback(i, str);
            } catch (RemoteException unused2) {
            }
        }
    }

    private void a(boolean z, boolean z2) {
        if (Trace.b != 30 && Trace.b >= 2) {
            bd.i = Trace.b * 1000;
        }
        if (Trace.a != 5 && Trace.a >= ar.c) {
            ar.b = Trace.a * 1000;
        }
        bd.d();
        bd bdVar = this.d;
        if (bdVar != null) {
            if (bdVar.isAlive()) {
                this.d.a(z, z2);
                return;
            } else {
                this.d.c();
                this.d = null;
            }
        }
        bd bdVar2 = new bd(this);
        this.d = bdVar2;
        bdVar2.a(z, z2);
        this.d.start();
    }

    private boolean a(int i, long j) {
        StringBuilder sb;
        String a2 = this.f.a("pushInfo");
        boolean z = true;
        if (a2 == null) {
            this.f.a("pushInfo", j + "_" + i + ";");
            this.f.a();
            return true;
        }
        String[] split = a2.split(";");
        int length = split.length;
        int i2 = 0;
        while (i2 < length) {
            String str = split[i2];
            if (TextUtils.isEmpty(str)) {
                return z;
            }
            String[] split2 = str.split("_");
            if (split2.length < 2) {
                sb = new StringBuilder();
            } else {
                try {
                    long longValue = Long.valueOf(split2[0]).longValue();
                    if (i == Integer.valueOf(split2[1]).intValue()) {
                        return false;
                    }
                    if (j - longValue >= 900) {
                        sb = new StringBuilder();
                    } else {
                        i2++;
                        z = true;
                    }
                } catch (Exception unused) {
                    return false;
                }
            }
            sb.append(str);
            sb.append(";");
            a2 = a2.replace(sb.toString(), "");
            i2++;
            z = true;
        }
        this.f.a("pushInfo", a2 + j + "_" + i + ";");
        this.f.a();
        return true;
    }

    static /* synthetic */ boolean a(LBSTraceService lBSTraceService, boolean z) {
        lBSTraceService.i = false;
        return false;
    }

    static /* synthetic */ int b(LBSTraceService lBSTraceService) {
        if (lBSTraceService.d == null) {
            return 56;
        }
        if (!bd.d) {
            return 57;
        }
        bd.d = false;
        lBSTraceService.f.a("is_gather", String.valueOf(false));
        lBSTraceService.f.a();
        return 55;
    }

    private void b(boolean z, boolean z2) {
        bd bdVar = this.d;
        if (bdVar != null) {
            bdVar.a(z, z2);
        } else {
            a(z, z2);
        }
    }

    static /* synthetic */ int c(LBSTraceService lBSTraceService) {
        if (lBSTraceService.d == null) {
            return 53;
        }
        if (bd.d) {
            return 54;
        }
        bd.d = true;
        lBSTraceService.f.a("is_gather", String.valueOf(true));
        lBSTraceService.f.a();
        return 52;
    }

    private void f(int i) {
        IListener iListener = this.h;
        if (iListener != null) {
            try {
                iListener.startTraceCallback(19);
            } catch (RemoteException unused) {
            }
            this.i = false;
        } else {
            this.i = true;
        }
        this.n = f.b.LOGGING;
        if (this.j) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetworkChangedReceiver.ACTION_GLOBAL_CONNECTIVITY_CHANGE);
        intentFilter.addAction("com.baidu.trace.action.SOCKET_RECONNECT");
        registerReceiver(this.k, intentFilter);
        this.j = true;
    }

    private void i() {
        TraceJniInterface.a().socketDisconnection();
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.b();
        }
    }

    protected final void a() {
        if (f.b.LOGOUTING == this.n) {
            i();
            return;
        }
        if (this.j) {
            unregisterReceiver(this.k);
            this.j = false;
        }
        com.baidu.trace.b.d.a(false);
        bd bdVar = this.d;
        if (bdVar != null) {
            bdVar.a(0L);
        }
        if (f.b.LOGOUTING == this.n) {
            i();
            return;
        }
        ax.a();
        aw.b();
        if (!a((short) 16, com.baidu.trace.c.e.a(), f.a.a)) {
            a(10000);
            return;
        }
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.g();
        }
    }

    protected final void a(int i) {
        IListener iListener = this.h;
        if (iListener != null) {
            try {
                iListener.startTraceCallback(i);
            } catch (RemoteException unused) {
            }
        }
        this.n = f.b.NOT_START;
        TraceJniInterface.a().socketDisconnection();
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.b();
        }
        g();
        bd bdVar = this.d;
        if (bdVar != null) {
            bdVar.a();
            if (this.d.a != null) {
                this.d.a.c();
            }
            this.d.c();
            this.d = null;
        }
        bd.b();
    }

    protected final void a(int i, int i2) {
        if (i >= ar.c && i2 >= 2) {
            ar.b = i * 1000;
            bd.i = i2 * 1000;
            this.f.a("gather_interval", String.valueOf(i));
            this.f.a("pack_interval", String.valueOf(i2));
            this.f.a();
        }
        bd.d();
        Trace.a(i, i2);
    }

    protected final void a(Message message) {
        IListener iListener;
        com.baidu.trace.b.d dVar;
        Bundle data = message.getData();
        int i = data.getInt("pushId");
        byte b = data.getByte("infoType");
        String string = data.getString("infoContent");
        long j = data.getLong("timeStamp");
        byte[] a2 = ax.a((short) 13, com.baidu.trace.c.e.a());
        if (a2 != null && (dVar = this.c) != null) {
            dVar.a(a2, (f.a) null);
        }
        if (!a(i, j) || (iListener = this.h) == null) {
            return;
        }
        try {
            iListener.pushCallback(b, string);
        } catch (RemoteException unused) {
        }
    }

    protected final boolean a(short s, int i, f.a aVar) {
        if (18 == s && !TextUtils.isEmpty(g.a) && !TextUtils.isEmpty(g.c) && !TextUtils.isEmpty(g.d)) {
            return true;
        }
        byte[] a2 = ax.a(s, i);
        if (a2 == null) {
            return false;
        }
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.a(a2, aVar);
        }
        return true;
    }

    protected final void b() {
        if (f.b.LOGOUTING == this.n) {
            i();
        } else {
            if (a((short) 17, com.baidu.trace.c.e.a(), f.a.b)) {
                return;
            }
            a(10000);
        }
    }

    protected final void b(Message message) {
        IListener iListener = this.h;
        if (iListener != null) {
            try {
                iListener.pushCallback((byte) 4, (String) message.obj);
            } catch (RemoteException unused) {
            }
        }
    }

    protected final boolean b(int i) {
        am.a(i);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.e).edit();
        edit.putInt("cacheSize", i);
        edit.apply();
        return true;
    }

    protected final void c() {
        if (f.b.LOGOUTING == this.n) {
            i();
            return;
        }
        this.n = f.b.STARTED;
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.f();
            this.c.a(z.c);
        }
        bd bdVar = this.d;
        if (bdVar != null) {
            bdVar.a(true, false);
        } else {
            a(true, false);
        }
        IListener iListener = this.h;
        if (iListener != null) {
            try {
                iListener.startTraceCallback(124);
            } catch (RemoteException unused) {
            }
        }
        if (this.g) {
            a((short) 18, com.baidu.trace.c.e.a(), null);
        }
    }

    protected final boolean c(int i) {
        if (i <= 0) {
            return false;
        }
        Trace.b(i);
        ar.d = i;
        this.f.a("loc_time_offset", String.valueOf(i));
        this.f.a();
        return true;
    }

    protected final void d() {
        TraceJniInterface.a().socketDisconnection();
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.b();
        }
        com.baidu.trace.c.e.d = 0;
        if (f.b.LOGOUTING == this.n) {
            e(0);
            return;
        }
        b(false, true);
        if (!com.baidu.trace.b.d.a(this.e)) {
            f(19);
        } else {
            this.n = f.b.LOGGING;
            this.c.c();
        }
    }

    protected final boolean d(int i) {
        bd bdVar = this.d;
        if (bdVar == null || bdVar.a == null) {
            return false;
        }
        this.d.a.a(i);
        return true;
    }

    protected final void e() {
        com.baidu.trace.b.d.a(false);
        if (f.b.LOGOUTING == this.n) {
            e(0);
        } else {
            f(19);
            b(false, true);
        }
    }

    protected final void e(int i) {
        IListener iListener;
        g();
        if (this.j) {
            unregisterReceiver(this.k);
            this.j = false;
        }
        this.n = f.b.NOT_START;
        com.baidu.trace.c.e.d = 0;
        if (bd.e() > 0) {
            am.a(true);
        }
        if (!PreferenceManager.getDefaultSharedPreferences(this.e).getBoolean("same_process", true)) {
            am.a();
        }
        try {
            if (this.h != null) {
                int i2 = IMAP.DEFAULT_PORT;
                if (143 != i && !bd.h && 141 == i) {
                    iListener = this.h;
                    i2 = 142;
                    iListener.stopTraceCallback(i2);
                }
                iListener = this.h;
                iListener.stopTraceCallback(i2);
            }
        } catch (RemoteException unused) {
        }
        stopSelf();
    }

    protected final void f() throws RemoteException {
        if (f.b.NOT_START == this.n) {
            IListener iListener = this.h;
            if (iListener != null) {
                iListener.stopTraceCallback(27);
                return;
            }
            return;
        }
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.e();
        }
        if (f.b.LOGOUTING == this.n) {
            this.d.a(false, false);
            bd.d = false;
            this.h.stopTraceCallback(182);
        }
        this.n = f.b.LOGOUTING;
        this.d.a(false, false);
        bd.d = false;
        this.d.e = true;
    }

    protected final void g() {
        this.f.a("client", null);
        this.f.a("gather_interval", null);
        this.f.a("pack_interval", null);
        this.f.a("loc_time_offset", null);
        this.f.a();
    }

    public final IListener getClientListener() {
        return this.h;
    }

    public final int getLocationMode() {
        return this.l;
    }

    public final Context getServiceContext() {
        return this.e;
    }

    public final a getTraceHandler() {
        return this.a;
    }

    protected final void h() {
        if (f.b.STARTED == this.n || f.b.LOGOUTING == this.n || this.c == null) {
            return;
        }
        this.n = f.b.LOGGING;
        this.c.b();
        this.c.c();
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return this.b;
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
        Context applicationContext = getApplicationContext();
        this.e = applicationContext;
        this.f = new s(applicationContext);
        av.a(this.e);
        bc.a().a(this.a);
        com.baidu.trace.b.d a2 = com.baidu.trace.b.d.a();
        this.c = a2;
        a2.a(this.a, this.e);
        if (d.b == null) {
            d.b = LBSAuthManager.getInstance(this.e);
        }
        q.a(this.e, d.b);
        com.baidu.trace.c.e.a(this.e);
        am.a(this.e);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.e);
        if (defaultSharedPreferences.contains("cacheSize")) {
            am.a(defaultSharedPreferences.getInt("cacheSize", 0));
        }
        String str = com.baidu.trace.c.e.c;
        com.baidu.trace.c.h.b();
        if (d.a == null) {
            d.a();
        }
    }

    @Override // android.app.Service
    public final void onDestroy() {
        super.onDestroy();
        if (this.c != null && f.b.LOGOUTING != this.n) {
            this.c.b();
        }
        if (this.j) {
            unregisterReceiver(this.k);
            this.j = false;
        }
        try {
            stopForeground(true);
        } catch (Exception unused) {
        }
        bd bdVar = this.d;
        if (bdVar != null) {
            bdVar.a();
            if (this.d.a != null) {
                this.d.a.c();
            }
            this.d.c();
            this.d = null;
        }
        com.baidu.trace.b.d dVar = this.c;
        if (dVar != null) {
            dVar.b();
            this.c = null;
        }
        s sVar = this.f;
        if (sVar != null) {
            sVar.b();
            this.f = null;
        }
        if (!PreferenceManager.getDefaultSharedPreferences(this.e).getBoolean("same_process", true)) {
            d.b();
        }
        ax.b();
        c.b();
        g.b();
        aw.d();
        bd.b();
    }

    /* JADX WARN: Code restructure failed: missing block: B:163:0x012f, code lost:
    
        if (r3 != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x02b8, code lost:
    
        if (r2 == null) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02cb, code lost:
    
        r0 = false;
        r2 = false;
        r11 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x02c8, code lost:
    
        r2.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x02c6, code lost:
    
        if (r2 == null) goto L116;
     */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int onStartCommand(android.content.Intent r27, int r28, int r29) {
        /*
            Method dump skipped, instructions count: 1133
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.LBSTraceService.onStartCommand(android.content.Intent, int, int):int");
    }

    @Override // android.app.Service
    public final boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public final void setClientListener(IListener iListener) {
        this.h = iListener;
    }
}
