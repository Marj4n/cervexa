package com.google.zxing.pdf417.decoder;

import kotlin.text.Typography;

/* loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final int AL = 28;
    private static final int ALPHA = 0;
    private static final int ALPHA_SHIFT = 4;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final int LL = 27;
    private static final int LOWER = 1;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final int MIXED = 2;
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final int PUNCT = 3;
    private static final int PUNCT_SHIFT = 5;
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;
    private static final char[] PUNCT_CHARS = {';', Typography.less, Typography.greater, '@', '[', '\\', '}', '_', '`', '~', '!', '\r', '\t', ',', ':', '\n', '-', '.', Typography.dollar, '/', Typography.quote, '|', '*', '(', ')', '?', '{', '}', '\''};
    private static final char[] MIXED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', Typography.amp, '\r', '\t', ',', ':', '#', '-', '.', Typography.dollar, '/', '+', '%', '*', '=', '^'};
    private static final String[] EXP900 = {"000000000000000000000000000000000000000000001", "000000000000000000000000000000000000000000900", "000000000000000000000000000000000000000810000", "000000000000000000000000000000000000729000000", "000000000000000000000000000000000656100000000", "000000000000000000000000000000590490000000000", "000000000000000000000000000531441000000000000", "000000000000000000000000478296900000000000000", "000000000000000000000430467210000000000000000", "000000000000000000387420489000000000000000000", "000000000000000348678440100000000000000000000", "000000000000313810596090000000000000000000000", "000000000282429536481000000000000000000000000", "000000254186582832900000000000000000000000000", "000228767924549610000000000000000000000000000", "205891132094649000000000000000000000000000000"};

    private DecodedBitStreamParser() {
    }

    private static StringBuffer add(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer(5);
        StringBuffer stringBuffer2 = new StringBuffer(5);
        StringBuffer stringBuffer3 = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            stringBuffer3.append('0');
        }
        int length = str.length() - 3;
        int i2 = 0;
        while (length > -1) {
            stringBuffer.setLength(0);
            stringBuffer.append(str.charAt(length));
            int i3 = length + 1;
            stringBuffer.append(str.charAt(i3));
            int i4 = length + 2;
            stringBuffer.append(str.charAt(i4));
            stringBuffer2.setLength(0);
            stringBuffer2.append(str2.charAt(length));
            stringBuffer2.append(str2.charAt(i3));
            stringBuffer2.append(str2.charAt(i4));
            int parseInt = Integer.parseInt(stringBuffer.toString()) + Integer.parseInt(stringBuffer2.toString()) + i2;
            int i5 = parseInt % 1000;
            stringBuffer3.setCharAt(i4, (char) ((i5 % 10) + 48));
            stringBuffer3.setCharAt(i3, (char) (((i5 / 10) % 10) + 48));
            stringBuffer3.setCharAt(length, (char) ((i5 / 100) + 48));
            length -= 3;
            i2 = parseInt / 1000;
        }
        return stringBuffer3;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0057 A[LOOP:1: B:15:0x0055->B:16:0x0057, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int byteCompaction(int r24, int[] r25, int r26, java.lang.StringBuffer r27) {
        /*
            Method dump skipped, instructions count: 253
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.byteCompaction(int, int[], int, java.lang.StringBuffer):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002f A[LOOP:0: B:2:0x000b->B:13:0x002f, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0034 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.google.zxing.common.DecoderResult decode(int[] r4) throws com.google.zxing.FormatException {
        /*
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r1 = 100
            r0.<init>(r1)
            r1 = 1
            r1 = r4[r1]
            r2 = 2
        Lb:
            r3 = 0
            r3 = r4[r3]
            if (r2 >= r3) goto L39
            r3 = 913(0x391, float:1.28E-42)
            if (r1 == r3) goto L23
            r3 = 924(0x39c, float:1.295E-42)
            if (r1 == r3) goto L23
            switch(r1) {
                case 900: goto L28;
                case 901: goto L23;
                case 902: goto L1e;
                default: goto L1b;
            }
        L1b:
            int r2 = r2 + (-1)
            goto L28
        L1e:
            int r1 = numericCompaction(r4, r2, r0)
            goto L2c
        L23:
            int r1 = byteCompaction(r1, r4, r2, r0)
            goto L2c
        L28:
            int r1 = textCompaction(r4, r2, r0)
        L2c:
            int r2 = r4.length
            if (r1 >= r2) goto L34
            int r2 = r1 + 1
            r1 = r4[r1]
            goto Lb
        L34:
            com.google.zxing.FormatException r4 = com.google.zxing.FormatException.getFormatInstance()
            throw r4
        L39:
            com.google.zxing.common.DecoderResult r4 = new com.google.zxing.common.DecoderResult
            java.lang.String r0 = r0.toString()
            r1 = 0
            r4.<init>(r1, r0, r1, r1)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decode(int[]):com.google.zxing.common.DecoderResult");
    }

    private static String decodeBase900toBase10(int[] iArr, int i) {
        int i2 = 0;
        String str = null;
        StringBuffer stringBuffer = null;
        for (int i3 = 0; i3 < i; i3++) {
            StringBuffer multiply = multiply(EXP900[(i - i3) - 1], iArr[i3]);
            stringBuffer = stringBuffer == null ? multiply : add(stringBuffer.toString(), multiply.toString());
        }
        while (true) {
            if (i2 >= stringBuffer.length()) {
                break;
            }
            if (stringBuffer.charAt(i2) == '1') {
                str = stringBuffer.toString().substring(i2 + 1);
                break;
            }
            i2++;
        }
        return str == null ? stringBuffer.toString() : str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x002f, code lost:
    
        if (r6 == 29) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void decodeTextCompaction(int[] r16, int[] r17, int r18, java.lang.StringBuffer r19) {
        /*
            Method dump skipped, instructions count: 185
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decodeTextCompaction(int[], int[], int, java.lang.StringBuffer):void");
    }

    private static StringBuffer multiply(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer(str.length());
        for (int i2 = 0; i2 < str.length(); i2++) {
            stringBuffer.append('0');
        }
        int i3 = i / 100;
        int i4 = (i / 10) % 10;
        int i5 = i % 10;
        for (int i6 = 0; i6 < i5; i6++) {
            stringBuffer = add(stringBuffer.toString(), str);
        }
        for (int i7 = 0; i7 < i4; i7++) {
            String stringBuffer2 = stringBuffer.toString();
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append('0');
            stringBuffer = add(stringBuffer2, stringBuffer3.toString().substring(1));
        }
        for (int i8 = 0; i8 < i3; i8++) {
            String stringBuffer4 = stringBuffer.toString();
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(str);
            stringBuffer5.append("00");
            stringBuffer = add(stringBuffer4, stringBuffer5.toString().substring(2));
        }
        return stringBuffer;
    }

    private static int numericCompaction(int[] iArr, int i, StringBuffer stringBuffer) {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i3 == iArr[0]) {
                z = true;
            }
            if (i4 < 900) {
                iArr2[i2] = i4;
                i2++;
            } else if (i4 == 900 || i4 == 901 || i4 == BYTE_COMPACTION_MODE_LATCH_6 || i4 == BEGIN_MACRO_PDF417_CONTROL_BLOCK || i4 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i4 == MACRO_PDF417_TERMINATOR) {
                i3--;
                z = true;
            }
            if (i2 % 15 == 0 || i4 == 902 || z) {
                stringBuffer.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i3;
        }
        return i;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:16:0x0031. Please report as an issue. */
    private static int textCompaction(int[] iArr, int i, StringBuffer stringBuffer) {
        int[] iArr2 = new int[iArr[0] << 1];
        int[] iArr3 = new int[iArr[0] << 1];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i4 < 900) {
                iArr2[i2] = i4 / 30;
                iArr2[i2 + 1] = i4 % 30;
                i2 += 2;
            } else if (i4 != MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                if (i4 != BYTE_COMPACTION_MODE_LATCH_6) {
                    switch (i4) {
                    }
                }
                i = i3 - 1;
                z = true;
            } else {
                iArr2[i2] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                i = i3 + 1;
                iArr3[i2] = iArr[i3];
                i2++;
            }
            i = i3;
        }
        decodeTextCompaction(iArr2, iArr3, i2, stringBuffer);
        return i;
    }
}
