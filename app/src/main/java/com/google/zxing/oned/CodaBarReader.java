package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class CodaBarReader extends OneDReader {
    private static final int minCharacterLength = 6;
    private static final String ALPHABET_STRING = "0123456789-$:/.+ABCDTN";
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private static final int[] CHARACTER_ENCODINGS = {3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14, 26, 41};
    private static final char[] STARTEND_ENCODING = {'E', '*', 'A', 'B', 'C', 'D', 'T', 'N'};

    private static boolean arrayContains(char[] cArr, char c) {
        if (cArr != null) {
            for (char c2 : cArr) {
                if (c2 == c) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] findAsteriskPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int i = 0;
        while (i < size && !bitArray.get(i)) {
            i++;
        }
        int[] iArr = new int[7];
        int i2 = i;
        boolean z = false;
        int i3 = 0;
        while (i < size) {
            if (bitArray.get(i) ^ z) {
                iArr[i3] = iArr[i3] + 1;
            } else {
                if (i3 == 6) {
                    try {
                        if (arrayContains(STARTEND_ENCODING, toNarrowWidePattern(iArr)) && bitArray.isRange(Math.max(0, i2 - ((i - i2) / 2)), i2, false)) {
                            return new int[]{i2, i};
                        }
                    } catch (IllegalArgumentException unused) {
                    }
                    i2 += iArr[0] + iArr[1];
                    for (int i4 = 2; i4 < 7; i4++) {
                        iArr[i4 - 2] = iArr[i4];
                    }
                    iArr[5] = 0;
                    iArr[6] = 0;
                    i3--;
                } else {
                    i3++;
                }
                iArr[i3] = 1;
                z = !z;
            }
            i++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static char toNarrowWidePattern(int[] iArr) {
        int length = iArr.length;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            if (iArr[i3] < i) {
                i = iArr[i3];
            }
            if (iArr[i3] > i2) {
                i2 = iArr[i3];
            }
        }
        do {
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < length; i6++) {
                if (iArr[i6] > i2) {
                    i5 |= 1 << ((length - 1) - i6);
                    i4++;
                }
            }
            if (i4 == 2 || i4 == 3) {
                int i7 = 0;
                while (true) {
                    int[] iArr2 = CHARACTER_ENCODINGS;
                    if (i7 >= iArr2.length) {
                        break;
                    }
                    if (iArr2[i7] == i5) {
                        return ALPHABET[i7];
                    }
                    i7++;
                }
            }
            i2--;
        } while (i2 > i);
        return '!';
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        int i2;
        int[] findAsteriskPattern = findAsteriskPattern(bitArray);
        findAsteriskPattern[1] = 0;
        int i3 = findAsteriskPattern[1];
        int size = bitArray.getSize();
        while (i3 < size && !bitArray.get(i3)) {
            i3++;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int[] iArr = {0, 0, 0, 0, 0, 0, 0};
            recordPattern(bitArray, i3, iArr);
            char narrowWidePattern = toNarrowWidePattern(iArr);
            if (narrowWidePattern == '!') {
                throw NotFoundException.getNotFoundInstance();
            }
            stringBuffer.append(narrowWidePattern);
            int i4 = i3;
            for (int i5 = 0; i5 < 7; i5++) {
                i4 += iArr[i5];
            }
            while (i4 < size && !bitArray.get(i4)) {
                i4++;
            }
            if (i4 >= size) {
                int i6 = 0;
                for (int i7 = 0; i7 < 7; i7++) {
                    i6 += iArr[i7];
                }
                int i8 = (i4 - i3) - i6;
                if (i4 != size && i8 / 2 < i6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (stringBuffer.length() < 2) {
                    throw NotFoundException.getNotFoundInstance();
                }
                char charAt = stringBuffer.charAt(0);
                if (!arrayContains(STARTEND_ENCODING, charAt)) {
                    throw NotFoundException.getNotFoundInstance();
                }
                int i9 = 1;
                while (i9 < stringBuffer.length()) {
                    if (stringBuffer.charAt(i9) == charAt && (i2 = i9 + 1) != stringBuffer.length()) {
                        stringBuffer.delete(i2, stringBuffer.length() - 1);
                        i9 = stringBuffer.length();
                    }
                    i9++;
                }
                if (stringBuffer.length() <= 6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBuffer.deleteCharAt(0);
                float f = i;
                return new Result(stringBuffer.toString(), null, new ResultPoint[]{new ResultPoint((findAsteriskPattern[1] + findAsteriskPattern[0]) / 2.0f, f), new ResultPoint((i4 + i3) / 2.0f, f)}, BarcodeFormat.CODABAR);
            }
            i3 = i4;
        }
    }
}
