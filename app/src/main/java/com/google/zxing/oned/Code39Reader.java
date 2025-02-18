package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.Hashtable;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: classes.dex */
public final class Code39Reader extends OneDReader {
    private static final int ASTERISK_ENCODING;
    static final int[] CHARACTER_ENCODINGS;
    private final boolean extendedMode;
    private final boolean usingCheckDigit;
    static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();

    static {
        int[] iArr = {52, 289, 97, 352, 49, 304, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, NNTPReply.SERVICE_DISCONTINUED, 208, 133, 388, 196, 148, IConstant.OP_CANCEL_TASK, IConstant.OP_EXIT_EDIT_MODE, 138, 42};
        CHARACTER_ENCODINGS = iArr;
        ASTERISK_ENCODING = iArr[39];
    }

    public Code39Reader() {
        this.usingCheckDigit = false;
        this.extendedMode = false;
    }

    public Code39Reader(boolean z) {
        this.usingCheckDigit = z;
        this.extendedMode = false;
    }

    public Code39Reader(boolean z, boolean z2) {
        this.usingCheckDigit = z;
        this.extendedMode = z2;
    }

    private static String decodeExtended(StringBuffer stringBuffer) throws FormatException {
        int i;
        int length = stringBuffer.length();
        StringBuffer stringBuffer2 = new StringBuffer(length);
        int i2 = 0;
        while (i2 < length) {
            char charAt = stringBuffer.charAt(i2);
            if (charAt == '+' || charAt == '$' || charAt == '%' || charAt == '/') {
                i2++;
                char charAt2 = stringBuffer.charAt(i2);
                if (charAt != '$') {
                    if (charAt != '%') {
                        if (charAt != '+') {
                            if (charAt != '/') {
                                charAt = 0;
                            } else if (charAt2 >= 'A' && charAt2 <= 'O') {
                                i = charAt2 - ' ';
                            } else {
                                if (charAt2 != 'Z') {
                                    throw FormatException.getFormatInstance();
                                }
                                charAt = ':';
                            }
                        } else {
                            if (charAt2 < 'A' || charAt2 > 'Z') {
                                throw FormatException.getFormatInstance();
                            }
                            i = charAt2 + ' ';
                        }
                    } else if (charAt2 >= 'A' && charAt2 <= 'E') {
                        i = charAt2 - '&';
                    } else {
                        if (charAt2 < 'F' || charAt2 > 'W') {
                            throw FormatException.getFormatInstance();
                        }
                        i = charAt2 - 11;
                    }
                } else {
                    if (charAt2 < 'A' || charAt2 > 'Z') {
                        throw FormatException.getFormatInstance();
                    }
                    i = charAt2 - '@';
                }
                charAt = (char) i;
            }
            stringBuffer2.append(charAt);
            i2++;
        }
        return stringBuffer2.toString();
    }

    private static int[] findAsteriskPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int i = 0;
        while (i < size && !bitArray.get(i)) {
            i++;
        }
        int[] iArr = new int[9];
        int i2 = i;
        boolean z = false;
        int i3 = 0;
        while (i < size) {
            if (bitArray.get(i) ^ z) {
                iArr[i3] = iArr[i3] + 1;
            } else {
                if (i3 == 8) {
                    if (toNarrowWidePattern(iArr) == ASTERISK_ENCODING && bitArray.isRange(Math.max(0, i2 - ((i - i2) / 2)), i2, false)) {
                        return new int[]{i2, i};
                    }
                    i2 += iArr[0] + iArr[1];
                    for (int i4 = 2; i4 < 9; i4++) {
                        iArr[i4 - 2] = iArr[i4];
                    }
                    iArr[7] = 0;
                    iArr[8] = 0;
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

    private static char patternToChar(int i) throws NotFoundException {
        int i2 = 0;
        while (true) {
            int[] iArr = CHARACTER_ENCODINGS;
            if (i2 >= iArr.length) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (iArr[i2] == i) {
                return ALPHABET[i2];
            }
            i2++;
        }
    }

    private static int toNarrowWidePattern(int[] iArr) {
        int length = iArr.length;
        int i = 0;
        while (true) {
            int i2 = Integer.MAX_VALUE;
            for (int i3 : iArr) {
                if (i3 < i2 && i3 > i) {
                    i2 = i3;
                }
            }
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            for (int i7 = 0; i7 < length; i7++) {
                int i8 = iArr[i7];
                if (iArr[i7] > i2) {
                    i5 |= 1 << ((length - 1) - i7);
                    i4++;
                    i6 += i8;
                }
            }
            if (i4 == 3) {
                for (int i9 = 0; i9 < length && i4 > 0; i9++) {
                    int i10 = iArr[i9];
                    if (iArr[i9] > i2) {
                        i4--;
                        if ((i10 << 1) >= i6) {
                            return -1;
                        }
                    }
                }
                return i5;
            }
            if (i4 <= 3) {
                return -1;
            }
            i = i2;
        }
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException, ChecksumException, FormatException {
        int i2 = findAsteriskPattern(bitArray)[1];
        int size = bitArray.getSize();
        while (i2 < size && !bitArray.get(i2)) {
            i2++;
        }
        StringBuffer stringBuffer = new StringBuffer(20);
        int[] iArr = new int[9];
        while (true) {
            recordPattern(bitArray, i2, iArr);
            int narrowWidePattern = toNarrowWidePattern(iArr);
            if (narrowWidePattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            char patternToChar = patternToChar(narrowWidePattern);
            stringBuffer.append(patternToChar);
            int i3 = i2;
            for (int i4 = 0; i4 < 9; i4++) {
                i3 += iArr[i4];
            }
            while (i3 < size && !bitArray.get(i3)) {
                i3++;
            }
            if (patternToChar == '*') {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                int i5 = 0;
                for (int i6 = 0; i6 < 9; i6++) {
                    i5 += iArr[i6];
                }
                int i7 = (i3 - i2) - i5;
                if (i3 != size && i7 / 2 < i5) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (this.usingCheckDigit) {
                    int length = stringBuffer.length() - 1;
                    int i8 = 0;
                    for (int i9 = 0; i9 < length; i9++) {
                        i8 += ALPHABET_STRING.indexOf(stringBuffer.charAt(i9));
                    }
                    if (stringBuffer.charAt(length) != ALPHABET[i8 % 43]) {
                        throw ChecksumException.getChecksumInstance();
                    }
                    stringBuffer.deleteCharAt(length);
                }
                if (stringBuffer.length() == 0) {
                    throw NotFoundException.getNotFoundInstance();
                }
                float f = i;
                return new Result(this.extendedMode ? decodeExtended(stringBuffer) : stringBuffer.toString(), null, new ResultPoint[]{new ResultPoint((r14[1] + r14[0]) / 2.0f, f), new ResultPoint((i3 + i2) / 2.0f, f)}, BarcodeFormat.CODE_39);
            }
            i2 = i3;
        }
    }
}
