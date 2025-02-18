package com.baidu.trace;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.baidu.trace.ar;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/* loaded from: classes.dex */
public final class bd extends Thread {
    protected static int b = ar.b;
    protected static Deque<ar.a> c = null;
    protected static boolean d = false;
    protected static int f = 0;
    protected static boolean g = false;
    protected static boolean h = false;
    protected static int i = 30000;
    private static Map<Integer, Integer> q;
    private static Map<Integer, Queue<ar.a>> r;
    protected ar a;
    private Context j;
    private WeakReference<LBSTraceService> k;
    private b m;
    private Handler n;
    private a l = null;
    private int o = 0;
    private long p = 0;
    private boolean s = false;
    private boolean t = false;
    protected boolean e = false;
    private long u = 0;
    private long v = System.currentTimeMillis();
    private long w = System.currentTimeMillis();

    static class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    class b implements Runnable {
        private b() {
        }

        /* synthetic */ b(bd bdVar, byte b) {
            this();
        }

        /* JADX WARN: Code restructure failed: missing block: B:54:0x0157, code lost:
        
            r7.a.w = java.lang.System.currentTimeMillis();
            com.baidu.trace.bd.l(r7.a);
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x0155, code lost:
        
            if ((java.lang.System.currentTimeMillis() - r7.a.w) >= (com.baidu.trace.z.p * 1000)) goto L60;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 603
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.bd.b.run():void");
        }
    }

    public bd(LBSTraceService lBSTraceService) {
        this.j = null;
        this.k = null;
        this.m = null;
        this.n = null;
        this.a = null;
        WeakReference<LBSTraceService> weakReference = new WeakReference<>(lBSTraceService);
        this.k = weakReference;
        this.n = weakReference.get().getTraceHandler();
        this.j = this.k.get().getServiceContext();
        this.a = new ar(this.k, this.n);
        this.m = new b(this, (byte) 0);
        q = new LinkedHashMap();
        r = new LinkedHashMap();
        c = new LinkedList();
    }

    static /* synthetic */ int a(bd bdVar, int i2) {
        bdVar.o = 0;
        return 0;
    }

    protected static void a(int i2) {
        r.remove(Integer.valueOf(i2));
        q.remove(Integer.valueOf(i2));
    }

    protected static void a(int i2, Queue<ar.a> queue) {
        r.put(Integer.valueOf(i2), queue);
        q.put(Integer.valueOf(i2), Integer.valueOf(com.baidu.trace.c.e.a()));
    }

    protected static void a(Handler handler) {
        if (com.baidu.trace.c.e.d <= 0 || z.s != 1 || com.baidu.trace.c.e.a() - com.baidu.trace.c.e.d <= 60 || handler == null) {
            return;
        }
        handler.obtainMessage(4).sendToTarget();
    }

    protected static void a(boolean z) {
        Integer num;
        Map<Integer, Queue<ar.a>> map = r;
        if (map == null || map.size() == 0) {
            return;
        }
        Iterator<Map.Entry<Integer, Queue<ar.a>>> it = r.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Queue<ar.a>> next = it.next();
            if (next != null) {
                Queue<ar.a> value = next.getValue();
                Integer key = next.getKey();
                if (value != null && key != null && (num = q.get(key)) != null) {
                    int a2 = com.baidu.trace.c.e.a();
                    if (z && a2 - num.intValue() < aw.b) {
                        value.clear();
                        return;
                    }
                    while (value.size() > 0) {
                        aw.a.offerLast(value.poll());
                    }
                    try {
                        q.remove(key);
                        it.remove();
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    protected static void b() {
        Map<Integer, Integer> map = q;
        if (map != null) {
            map.clear();
            q = null;
        }
        Map<Integer, Queue<ar.a>> map2 = r;
        if (map2 != null) {
            map2.clear();
            r = null;
        }
        Deque<ar.a> deque = c;
        if (deque != null) {
            deque.clear();
            c = null;
        }
    }

    protected static void d() {
        int i2;
        int i3 = i / ar.b;
        if (ar.b <= z.p * 1000) {
            i2 = ar.b;
        } else {
            int i4 = (ar.b / 5000) * 5000;
            ar.b = i4;
            if (i4 % (z.p * 1000) != 0) {
                b = 5000;
                i = ar.b * i3;
            }
            i2 = z.p * 1000;
        }
        b = i2;
        i = ar.b * i3;
    }

    protected static int e() {
        Deque<ar.a> deque = c;
        int size = deque != null ? 0 + deque.size() : 0;
        return aw.a != null ? size + aw.a.size() : size;
    }

    static /* synthetic */ int e(bd bdVar) {
        int i2 = bdVar.o;
        bdVar.o = i2 + 1;
        return i2;
    }

    static /* synthetic */ boolean j(bd bdVar) {
        int a2 = com.baidu.trace.c.e.a();
        byte[] a3 = ax.a((short) 9, a2);
        if (a3 == null || a3.length <= 0) {
            aw.c();
            return false;
        }
        aw.a(a2);
        com.baidu.trace.b.a.a(a3, bdVar.n);
        return true;
    }

    static /* synthetic */ void l(bd bdVar) {
        byte[] buildHeartbeatData = TraceJniInterface.a().buildHeartbeatData();
        if (buildHeartbeatData != null) {
            com.baidu.trace.b.a.a(buildHeartbeatData, bdVar.n);
        }
    }

    protected final void a() {
        f = 0;
        g = false;
        h = false;
        d = false;
        this.t = false;
        this.s = false;
        this.e = false;
    }

    protected final void a(long j) {
        this.p = 0L;
    }

    protected final void a(boolean z, boolean z2) {
        this.t = z;
        this.s = z2;
        if (z2) {
            return;
        }
        this.o = 0;
    }

    protected final void c() {
        b bVar;
        a aVar = this.l;
        if (aVar != null && (bVar = this.m) != null) {
            aVar.removeCallbacks(bVar);
            if (Build.VERSION.SDK_INT >= 18) {
                this.l.getLooper().quitSafely();
            } else {
                this.l.getLooper().quit();
            }
        }
        this.m = null;
        this.l = null;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        Looper.prepare();
        a aVar = new a();
        this.l = aVar;
        aVar.post(this.m);
        Looper.loop();
    }
}
