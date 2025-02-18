package com.serenegiant.utils;

import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes2.dex */
public class BufferHelper {
    private static final int BUF_LEN = 256;
    private static final int SIZEOF_FLOAT = 4;
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final byte[] ANNEXB_START_MARK = {0, 0, 0, 1};

    public static final void dump(String str, ByteBuffer byteBuffer, int i, int i2) {
        dump(str, byteBuffer, i, i2, false);
    }

    public static final void dump(String str, ByteBuffer byteBuffer, int i, int i2, boolean z) {
        byte[] bArr = new byte[256];
        if (byteBuffer == null) {
            return;
        }
        ByteBuffer duplicate = byteBuffer.duplicate();
        int limit = duplicate.limit();
        duplicate.position();
        if (i2 > limit) {
            i2 = limit;
        }
        duplicate.position(i);
        StringBuilder sb = new StringBuilder();
        while (i < i2) {
            int i3 = i + 256;
            int i4 = i3 < i2 ? 256 : i2 - i;
            duplicate.get(bArr, 0, i4);
            for (int i5 = 0; i5 < i4; i5++) {
                sb.append(String.format("%02x", Byte.valueOf(bArr[i5])));
            }
            if (z) {
                int i6 = -1;
                do {
                    byte[] bArr2 = ANNEXB_START_MARK;
                    i6 = byteComp(bArr, i6 + 1, bArr2, bArr2.length);
                    if (i6 >= 0) {
                        Log.i(str, "found ANNEXB: start index=" + i6);
                    }
                } while (i6 >= 0);
            }
            i = i3;
        }
        Log.i(str, "dump:" + sb.toString());
    }

    public static final void dump(String str, byte[] bArr, int i, int i2, boolean z) {
        int length = bArr != null ? bArr.length : 0;
        if (length == 0) {
            return;
        }
        if (i2 > length) {
            i2 = length;
        }
        StringBuilder sb = new StringBuilder();
        while (i < i2) {
            sb.append(String.format("%02x", Byte.valueOf(bArr[i])));
            i++;
        }
        if (z) {
            int i3 = -1;
            do {
                byte[] bArr2 = ANNEXB_START_MARK;
                i3 = byteComp(bArr, i3 + 1, bArr2, bArr2.length);
                if (i3 >= 0) {
                    Log.i(str, "found ANNEXB: start index=" + i3);
                }
            } while (i3 >= 0);
        }
        Log.i(str, "dump:" + sb.toString());
    }

    public static final int byteComp(byte[] bArr, int i, byte[] bArr2, int i2) {
        int length = bArr.length;
        int length2 = bArr2.length;
        if (length >= i + i2 && length2 >= i2) {
            while (i < length - i2) {
                int i3 = i2 - 1;
                while (i3 >= 0 && bArr[i + i3] == bArr2[i3]) {
                    i3--;
                }
                if (i3 < 0) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public static final int findAnnexB(byte[] bArr, int i) {
        if (bArr == null) {
            return -1;
        }
        int length = bArr.length - 5;
        for (int i2 = i; i2 < length; i2++) {
            if (bArr[i2] == 0 && bArr[i2 + 1] == 0 && bArr[i2 + 2] == 0 && bArr[i2 + 3] == 1) {
                return i2;
            }
        }
        int length2 = bArr.length - 4;
        while (i < length2) {
            if (bArr[i] == 0 && bArr[i + 1] == 0 && bArr[i + 2] == 1) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static FloatBuffer createFloatBuffer(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    public static ByteBuffer from(String str) throws NumberFormatException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        int length = !TextUtils.isEmpty(str) ? str.length() : 0;
        while (i < length) {
            int i2 = i + 2;
            byteArrayOutputStream.write(Integer.parseInt(str.substring(i, i2), 16));
            i = i2;
        }
        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    }

    public static String toHexString(byte[] bArr) {
        return toHexString(bArr, 0, bArr.length);
    }

    public static String toHexString(byte[] bArr, int i, int i2) {
        int length = bArr != null ? bArr.length : 0;
        int i3 = i2 + i;
        if (length <= i3) {
            i3 = length;
        }
        StringBuilder sb = new StringBuilder((length * 2) + 2);
        while (i < i3) {
            byte b = bArr[i];
            sb.append(HEX[(b & 240) >>> 4]);
            sb.append(HEX[b & 15]);
            i++;
        }
        return sb.toString();
    }

    public static String toHexString(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        ByteBuffer duplicate = byteBuffer.duplicate();
        int remaining = duplicate.remaining();
        StringBuilder sb = new StringBuilder((remaining * 2) + 2);
        for (int i = 0; i < remaining; i++) {
            byte b = duplicate.get();
            sb.append(HEX[(b & 240) >>> 4]);
            sb.append(HEX[b & 15]);
        }
        return sb.toString();
    }
}
