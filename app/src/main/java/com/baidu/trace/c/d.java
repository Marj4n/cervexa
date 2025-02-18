package com.baidu.trace.c;

import android.text.TextUtils;
import kotlin.UByte;

/* loaded from: classes.dex */
public final class d {
    static {
        "0123456789abcdef".getBytes();
    }

    public static short a(byte[] bArr, int i) {
        if (bArr == null || 2 > bArr.length) {
            return (short) 0;
        }
        return (short) ((bArr[1] & UByte.MAX_VALUE) | (bArr[0] << 8));
    }

    public static byte[] a(String str) {
        return str == null ? new byte[0] : str.getBytes();
    }

    public static byte[] b(String str) {
        int i = 6;
        byte[] bArr = new byte[6];
        if (TextUtils.isEmpty(str)) {
            return bArr;
        }
        try {
            String[] split = str.split(":");
            if (6 > split.length) {
                i = split.length;
            }
            for (int i2 = 0; i2 < i; i2++) {
                bArr[i2] = (byte) Integer.parseInt(split[i2], 16);
            }
        } catch (Exception unused) {
        }
        return bArr;
    }
}
