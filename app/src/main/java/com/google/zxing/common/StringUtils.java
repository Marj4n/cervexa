package com.google.zxing.common;

import com.google.zxing.DecodeHintType;
import java.util.Hashtable;
import kotlin.UByte;

/* loaded from: classes.dex */
public final class StringUtils {
    private static final boolean ASSUME_SHIFT_JIS;
    private static final String EUC_JP = "EUC_JP";
    public static final String GB2312 = "GB2312";
    private static final String ISO88591 = "ISO8859_1";
    private static final String PLATFORM_DEFAULT_ENCODING;
    public static final String SHIFT_JIS = "SJIS";
    private static final String UTF8 = "UTF8";

    static {
        String property = System.getProperty("file.encoding");
        PLATFORM_DEFAULT_ENCODING = property;
        ASSUME_SHIFT_JIS = SHIFT_JIS.equalsIgnoreCase(property) || EUC_JP.equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING);
    }

    private StringUtils() {
    }

    public static String guessEncoding(byte[] bArr, Hashtable hashtable) {
        int i;
        int i2;
        String str;
        if (hashtable != null && (str = (String) hashtable.get(DecodeHintType.CHARACTER_SET)) != null) {
            return str;
        }
        if (bArr.length > 3 && bArr[0] == -17 && bArr[1] == -69 && bArr[2] == -65) {
            return UTF8;
        }
        int length = bArr.length;
        boolean z = true;
        boolean z2 = true;
        boolean z3 = true;
        int i3 = 0;
        boolean z4 = false;
        int i4 = 0;
        int i5 = 0;
        boolean z5 = false;
        boolean z6 = false;
        for (int i6 = 0; i6 < length && (z || z2 || z3); i6++) {
            int i7 = bArr[i6] & UByte.MAX_VALUE;
            if (i7 < 128 || i7 > 191) {
                if (i3 > 0) {
                    z3 = false;
                }
                if (i7 >= 192 && i7 <= 253) {
                    for (int i8 = i7; (i8 & 64) != 0; i8 <<= 1) {
                        i3++;
                    }
                    z4 = true;
                }
            } else if (i3 > 0) {
                i3--;
            }
            if ((i7 == 194 || i7 == 195) && i6 < length - 1 && (i = bArr[i6 + 1] & UByte.MAX_VALUE) <= 191 && ((i7 == 194 && i >= 160) || (i7 == 195 && i >= 128))) {
                z5 = true;
            }
            if (i7 >= 127 && i7 <= 159) {
                z = false;
            }
            if (i7 >= 161 && i7 <= 223 && !z6) {
                i5++;
            }
            if (!z6 && ((i7 >= 240 && i7 <= 255) || i7 == 128 || i7 == 160)) {
                z2 = false;
            }
            if (((i7 < 129 || i7 > 159) && (i7 < 224 || i7 > 239)) || z6) {
                z6 = false;
            } else {
                if (i6 < bArr.length - 1 && (i2 = bArr[i6 + 1] & UByte.MAX_VALUE) >= 64 && i2 <= 252) {
                    i4++;
                } else {
                    z2 = false;
                }
                z6 = true;
            }
        }
        return (z2 && ASSUME_SHIFT_JIS) ? SHIFT_JIS : ((i3 > 0 ? false : z3) && z4) ? UTF8 : (!z2 || (i4 < 3 && i5 * 20 <= length)) ? (z5 || !z) ? PLATFORM_DEFAULT_ENCODING : ISO88591 : SHIFT_JIS;
    }
}
