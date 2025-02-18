package com.baidu.trace;

import android.text.TextUtils;
import com.baidu.trace.ar;
import java.util.Deque;
import java.util.LinkedList;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class aw implements com.baidu.trace.c.g {
    public static Deque<ar.a> a = null;
    protected static long b = 2;
    protected static int c = 0;
    private static byte d = 1;
    private static Deque<ar.a> e = null;
    private static int f = 0;
    private static String g = "";

    public static void a(int i) {
        if (z.s == 1) {
            LinkedList linkedList = new LinkedList();
            while (e.size() > 0) {
                linkedList.offer(e.poll());
            }
            bd.a(i, linkedList);
        }
        f = 0;
    }

    private void a(Deque<ar.a> deque) {
        int i = d == 1 ? (z.r << 5) * 3 : z.r << 5;
        int size = deque.size();
        while (f < i && size > 0 && e.size() < 10) {
            ar.a poll = deque.poll();
            size--;
            if (poll != null && poll.b != null) {
                if (i - f < poll.b.length) {
                    deque.offerFirst(poll);
                    return;
                } else {
                    e.offer(poll);
                    f += poll.b.length;
                }
            }
        }
    }

    protected static void b() {
        if (e == null) {
            e = new LinkedList();
        }
        if (a == null) {
            a = new LinkedList();
        }
    }

    public static void b(int i) {
        if (i <= 0) {
            return;
        }
        if ((bd.g || (!bd.d && bd.h)) && c > 0 && !TextUtils.isEmpty(g)) {
            am.a(g, c);
        }
        bd.a(i);
    }

    public static void c() {
        while (e.size() > 0) {
            a.offerLast(e.poll());
        }
        f = 0;
    }

    protected static void d() {
        Deque<ar.a> deque = e;
        if (deque != null) {
            deque.clear();
            e = null;
        }
        Deque<ar.a> deque2 = a;
        if (deque2 != null) {
            deque2.clear();
            a = null;
        }
    }

    private static byte[] e() {
        int i = f;
        byte[] bArr = new byte[i];
        int size = e.size();
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            ar.a poll = e.poll();
            if (poll != null && poll.b != null) {
                if (poll.b.length + i2 > i) {
                    return bArr;
                }
                for (int i4 = 0; i4 < poll.b.length; i4++) {
                    bArr[i2 + i4] = poll.b[i4];
                }
                i2 += poll.b.length;
                e.offer(poll);
            }
        }
        return bArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0031, code lost:
    
        if (com.baidu.trace.aw.c > 0) goto L5;
     */
    @Override // com.baidu.trace.c.g
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a() {
        /*
            r3 = this;
            b()
            r0 = 0
            com.baidu.trace.aw.f = r0
            java.util.Deque<com.baidu.trace.ar$a> r0 = com.baidu.trace.aw.a
            int r0 = r0.size()
            if (r0 <= 0) goto L14
            java.util.Deque<com.baidu.trace.ar$a> r0 = com.baidu.trace.aw.a
        L10:
            r3.a(r0)
            goto L3b
        L14:
            boolean r0 = com.baidu.trace.bd.g
            if (r0 != 0) goto L20
            boolean r0 = com.baidu.trace.bd.d
            if (r0 != 0) goto L34
            boolean r0 = com.baidu.trace.bd.h
            if (r0 == 0) goto L34
        L20:
            java.util.LinkedList r0 = new java.util.LinkedList
            r0.<init>()
            java.lang.String r1 = com.baidu.trace.z.c
            r2 = 10
            java.lang.String r1 = com.baidu.trace.am.a(r1, r2, r0)
            com.baidu.trace.aw.g = r1
            int r1 = com.baidu.trace.aw.c
            if (r1 <= 0) goto L34
            goto L10
        L34:
            java.lang.String r0 = com.baidu.trace.z.c
            com.baidu.trace.aw.g = r0
            java.util.Deque<com.baidu.trace.ar$a> r0 = com.baidu.trace.bd.c
            goto L10
        L3b:
            com.baidu.trace.TraceJniInterface r0 = com.baidu.trace.TraceJniInterface.a()
            byte r1 = com.baidu.trace.aw.d
            byte[] r2 = e()
            r0.setPackData(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.aw.a():void");
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        com.baidu.trace.a.h hVar = new com.baidu.trace.a.h();
        try {
            hVar.a = jSONObject.getInt("msg_flag");
            bc.a();
            bc.a(hVar);
        } catch (Exception unused) {
        }
    }
}
