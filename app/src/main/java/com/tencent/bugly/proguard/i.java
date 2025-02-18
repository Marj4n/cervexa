package com.tencent.bugly.proguard;

import com.github.mikephil.charting.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class i {
    private ByteBuffer a;
    private String b = "GBK";

    /* compiled from: BUGLY */
    public static class a {
        public byte a;
        public int b;
    }

    public i() {
    }

    public i(byte[] bArr) {
        this.a = ByteBuffer.wrap(bArr);
    }

    public i(byte[] bArr, int i) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        this.a = wrap;
        wrap.position(4);
    }

    public final void a(byte[] bArr) {
        ByteBuffer byteBuffer = this.a;
        if (byteBuffer != null) {
            byteBuffer.clear();
        }
        this.a = ByteBuffer.wrap(bArr);
    }

    private static int a(a aVar, ByteBuffer byteBuffer) {
        byte b = byteBuffer.get();
        aVar.a = (byte) (b & 15);
        aVar.b = (b & 240) >> 4;
        if (aVar.b != 15) {
            return 1;
        }
        aVar.b = byteBuffer.get();
        return 2;
    }

    private boolean a(int i) {
        a aVar;
        try {
            aVar = new a();
            while (true) {
                int a2 = a(aVar, this.a.duplicate());
                if (i <= aVar.b || aVar.a == 11) {
                    break;
                }
                this.a.position(this.a.position() + a2);
                a(aVar.a);
            }
        } catch (g | BufferUnderflowException unused) {
        }
        return i == aVar.b;
    }

    private void a() {
        a aVar = new a();
        do {
            a(aVar, this.a);
            a(aVar.a);
        } while (aVar.a != 11);
    }

    private void a(byte b) {
        int i = 0;
        switch (b) {
            case 0:
                ByteBuffer byteBuffer = this.a;
                byteBuffer.position(byteBuffer.position() + 1);
                return;
            case 1:
                ByteBuffer byteBuffer2 = this.a;
                byteBuffer2.position(byteBuffer2.position() + 2);
                return;
            case 2:
                ByteBuffer byteBuffer3 = this.a;
                byteBuffer3.position(byteBuffer3.position() + 4);
                return;
            case 3:
                ByteBuffer byteBuffer4 = this.a;
                byteBuffer4.position(byteBuffer4.position() + 8);
                return;
            case 4:
                ByteBuffer byteBuffer5 = this.a;
                byteBuffer5.position(byteBuffer5.position() + 4);
                return;
            case 5:
                ByteBuffer byteBuffer6 = this.a;
                byteBuffer6.position(byteBuffer6.position() + 8);
                return;
            case 6:
                int i2 = this.a.get();
                if (i2 < 0) {
                    i2 += 256;
                }
                ByteBuffer byteBuffer7 = this.a;
                byteBuffer7.position(byteBuffer7.position() + i2);
                return;
            case 7:
                int i3 = this.a.getInt();
                ByteBuffer byteBuffer8 = this.a;
                byteBuffer8.position(byteBuffer8.position() + i3);
                return;
            case 8:
                int a2 = a(0, 0, true);
                while (i < (a2 << 1)) {
                    a aVar = new a();
                    a(aVar, this.a);
                    a(aVar.a);
                    i++;
                }
                return;
            case 9:
                int a3 = a(0, 0, true);
                while (i < a3) {
                    a aVar2 = new a();
                    a(aVar2, this.a);
                    a(aVar2.a);
                    i++;
                }
                return;
            case 10:
                a();
                return;
            case 11:
            case 12:
                return;
            case 13:
                a aVar3 = new a();
                a(aVar3, this.a);
                if (aVar3.a != 0) {
                    throw new g("skipField with invalid type, type value: " + ((int) b) + ", " + ((int) aVar3.a));
                }
                int a4 = a(0, 0, true);
                ByteBuffer byteBuffer9 = this.a;
                byteBuffer9.position(byteBuffer9.position() + a4);
                return;
            default:
                throw new g("invalid type.");
        }
    }

    public final boolean a(int i, boolean z) {
        return a((byte) 0, i, z) != 0;
    }

    public final byte a(byte b, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return b;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b2 = aVar.a;
        if (b2 == 0) {
            return this.a.get();
        }
        if (b2 == 12) {
            return (byte) 0;
        }
        throw new g("type mismatch.");
    }

    public final short a(short s, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return s;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 0) {
            return this.a.get();
        }
        if (b == 1) {
            return this.a.getShort();
        }
        if (b == 12) {
            return (short) 0;
        }
        throw new g("type mismatch.");
    }

    public final int a(int i, int i2, boolean z) {
        if (!a(i2)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return i;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 0) {
            return this.a.get();
        }
        if (b == 1) {
            return this.a.getShort();
        }
        if (b == 2) {
            return this.a.getInt();
        }
        if (b == 12) {
            return 0;
        }
        throw new g("type mismatch.");
    }

    public final long a(long j, int i, boolean z) {
        int i2;
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return j;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 0) {
            i2 = this.a.get();
        } else if (b == 1) {
            i2 = this.a.getShort();
        } else {
            if (b != 2) {
                if (b == 3) {
                    return this.a.getLong();
                }
                if (b == 12) {
                    return 0L;
                }
                throw new g("type mismatch.");
            }
            i2 = this.a.getInt();
        }
        return i2;
    }

    private float a(float f, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return f;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 4) {
            return this.a.getFloat();
        }
        if (b == 12) {
            return 0.0f;
        }
        throw new g("type mismatch.");
    }

    private double a(double d, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return d;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 4) {
            return this.a.getFloat();
        }
        if (b == 5) {
            return this.a.getDouble();
        }
        if (b == 12) {
            return Utils.DOUBLE_EPSILON;
        }
        throw new g("type mismatch.");
    }

    public final String b(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 6) {
            int i2 = this.a.get();
            if (i2 < 0) {
                i2 += 256;
            }
            byte[] bArr = new byte[i2];
            this.a.get(bArr);
            try {
                return new String(bArr, this.b);
            } catch (UnsupportedEncodingException unused) {
                return new String(bArr);
            }
        }
        if (b == 7) {
            int i3 = this.a.getInt();
            if (i3 > 104857600 || i3 < 0) {
                throw new g("String too long: " + i3);
            }
            byte[] bArr2 = new byte[i3];
            this.a.get(bArr2);
            try {
                return new String(bArr2, this.b);
            } catch (UnsupportedEncodingException unused2) {
                return new String(bArr2);
            }
        }
        throw new g("type mismatch.");
    }

    public final <K, V> HashMap<K, V> a(Map<K, V> map, int i, boolean z) {
        return (HashMap) a(new HashMap(), map, i, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <K, V> Map<K, V> a(Map<K, V> map, Map<K, V> map2, int i, boolean z) {
        if (map2 == null || map2.isEmpty()) {
            return new HashMap();
        }
        Map.Entry<K, V> next = map2.entrySet().iterator().next();
        K key = next.getKey();
        V value = next.getValue();
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.a);
            if (aVar.a == 8) {
                int a2 = a(0, 0, true);
                if (a2 < 0) {
                    throw new g("size invalid: " + a2);
                }
                for (int i2 = 0; i2 < a2; i2++) {
                    map.put(a((i) key, 0, true), a((i) value, 1, true));
                }
            } else {
                throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return map;
    }

    private boolean[] d(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            boolean[] zArr = new boolean[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                zArr[i2] = a((byte) 0, 0, true) != 0;
            }
            return zArr;
        }
        throw new g("type mismatch.");
    }

    public final byte[] c(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        byte b = aVar.a;
        if (b == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            byte[] bArr = new byte[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                bArr[i2] = a(bArr[0], 0, true);
            }
            return bArr;
        }
        if (b == 13) {
            a aVar2 = new a();
            a(aVar2, this.a);
            if (aVar2.a != 0) {
                throw new g("type mismatch, tag: " + i + ", type: " + ((int) aVar.a) + ", " + ((int) aVar2.a));
            }
            int a3 = a(0, 0, true);
            if (a3 < 0) {
                throw new g("invalid size, tag: " + i + ", type: " + ((int) aVar.a) + ", " + ((int) aVar2.a) + ", size: " + a3);
            }
            byte[] bArr2 = new byte[a3];
            this.a.get(bArr2);
            return bArr2;
        }
        throw new g("type mismatch.");
    }

    private short[] e(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            short[] sArr = new short[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                sArr[i2] = a(sArr[0], 0, true);
            }
            return sArr;
        }
        throw new g("type mismatch.");
    }

    private int[] f(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            int[] iArr = new int[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                iArr[i2] = a(iArr[0], 0, true);
            }
            return iArr;
        }
        throw new g("type mismatch.");
    }

    private long[] g(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            long[] jArr = new long[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                jArr[i2] = a(jArr[0], 0, true);
            }
            return jArr;
        }
        throw new g("type mismatch.");
    }

    private float[] h(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            float[] fArr = new float[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                fArr[i2] = a(fArr[0], 0, true);
            }
            return fArr;
        }
        throw new g("type mismatch.");
    }

    private double[] i(int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            double[] dArr = new double[a2];
            for (int i2 = 0; i2 < a2; i2++) {
                dArr[i2] = a(dArr[0], 0, true);
            }
            return dArr;
        }
        throw new g("type mismatch.");
    }

    private <T> T[] a(T[] tArr, int i, boolean z) {
        if (tArr == null || tArr.length == 0) {
            throw new g("unable to get type of key and value.");
        }
        return (T[]) b(tArr[0], i, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T[] b(T t, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        a aVar = new a();
        a(aVar, this.a);
        if (aVar.a == 9) {
            int a2 = a(0, 0, true);
            if (a2 < 0) {
                throw new g("size invalid: " + a2);
            }
            T[] tArr = (T[]) ((Object[]) Array.newInstance(t.getClass(), a2));
            for (int i2 = 0; i2 < a2; i2++) {
                tArr[i2] = a((i) t, 0, true);
            }
            return tArr;
        }
        throw new g("type mismatch.");
    }

    public final k a(k kVar, int i, boolean z) {
        if (!a(i)) {
            if (z) {
                throw new g("require field not exist.");
            }
            return null;
        }
        try {
            k kVar2 = (k) kVar.getClass().newInstance();
            a aVar = new a();
            a(aVar, this.a);
            if (aVar.a != 10) {
                throw new g("type mismatch.");
            }
            kVar2.a(this);
            a();
            return kVar2;
        } catch (Exception e) {
            throw new g(e.getMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> Object a(T t, int i, boolean z) {
        if (t instanceof Byte) {
            return Byte.valueOf(a((byte) 0, i, z));
        }
        if (t instanceof Boolean) {
            return Boolean.valueOf(a((byte) 0, i, z) != 0);
        }
        if (t instanceof Short) {
            return Short.valueOf(a((short) 0, i, z));
        }
        if (t instanceof Integer) {
            return Integer.valueOf(a(0, i, z));
        }
        if (t instanceof Long) {
            return Long.valueOf(a(0L, i, z));
        }
        if (t instanceof Float) {
            return Float.valueOf(a(0.0f, i, z));
        }
        if (t instanceof Double) {
            return Double.valueOf(a(Utils.DOUBLE_EPSILON, i, z));
        }
        if (t instanceof String) {
            return String.valueOf(b(i, z));
        }
        if (t instanceof Map) {
            return (HashMap) a(new HashMap(), (Map) t, i, z);
        }
        if (t instanceof List) {
            List list = (List) t;
            if (list == null || list.isEmpty()) {
                return new ArrayList();
            }
            Object[] b = b(list.get(0), i, z);
            if (b == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Object obj : b) {
                arrayList.add(obj);
            }
            return arrayList;
        }
        if (t instanceof k) {
            return a((k) t, i, z);
        }
        if (t.getClass().isArray()) {
            if ((t instanceof byte[]) || (t instanceof Byte[])) {
                return c(i, z);
            }
            if (t instanceof boolean[]) {
                return d(i, z);
            }
            if (t instanceof short[]) {
                return e(i, z);
            }
            if (t instanceof int[]) {
                return f(i, z);
            }
            if (t instanceof long[]) {
                return g(i, z);
            }
            if (t instanceof float[]) {
                return h(i, z);
            }
            if (t instanceof double[]) {
                return i(i, z);
            }
            return a((Object[]) t, i, z);
        }
        throw new g("read object error: unsupport type.");
    }

    public final int a(String str) {
        this.b = str;
        return 0;
    }
}
