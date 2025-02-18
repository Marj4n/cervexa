package com.google.zxing.oned.rss;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class RSS14Reader extends AbstractRSSReader {
    private final Vector possibleLeftPairs = new Vector();
    private final Vector possibleRightPairs = new Vector();
    private static final int[] OUTSIDE_EVEN_TOTAL_SUBSET = {1, 10, 34, 70, 126};
    private static final int[] INSIDE_ODD_TOTAL_SUBSET = {4, 20, 48, 81};
    private static final int[] OUTSIDE_GSUM = {0, IConstant.OP_ENTER_EDIT_MODE, 961, 2015, 2715};
    private static final int[] INSIDE_GSUM = {0, 336, 1036, 1516};
    private static final int[] OUTSIDE_ODD_WIDEST = {8, 6, 4, 3, 1};
    private static final int[] INSIDE_ODD_WIDEST = {2, 4, 6, 8};
    private static final int[][] FINDER_PATTERNS = {new int[]{3, 8, 2, 1}, new int[]{3, 5, 5, 1}, new int[]{3, 3, 7, 1}, new int[]{3, 1, 9, 1}, new int[]{2, 7, 4, 1}, new int[]{2, 5, 6, 1}, new int[]{2, 3, 8, 1}, new int[]{1, 5, 7, 1}, new int[]{1, 3, 9, 1}};

    private static void addOrTally(Vector vector, Pair pair) {
        if (pair == null) {
            return;
        }
        Enumeration elements = vector.elements();
        boolean z = false;
        while (true) {
            if (!elements.hasMoreElements()) {
                break;
            }
            Pair pair2 = (Pair) elements.nextElement();
            if (pair2.getValue() == pair.getValue()) {
                pair2.incrementCount();
                z = true;
                break;
            }
        }
        if (z) {
            return;
        }
        vector.addElement(pair);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0032, code lost:
    
        if (r1 < 4) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004e, code lost:
    
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x004b, code lost:
    
        r10 = false;
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0049, code lost:
    
        if (r1 < 4) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void adjustOddEvenCounts(boolean r10, int r11) throws com.google.zxing.NotFoundException {
        /*
            Method dump skipped, instructions count: 209
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.rss.RSS14Reader.adjustOddEvenCounts(boolean, int):void");
    }

    private static boolean checkChecksum(Pair pair, Pair pair2) {
        pair.getFinderPattern().getValue();
        pair2.getFinderPattern().getValue();
        int checksumPortion = (pair.getChecksumPortion() + (pair2.getChecksumPortion() * 16)) % 79;
        int value = (pair.getFinderPattern().getValue() * 9) + pair2.getFinderPattern().getValue();
        if (value > 72) {
            value--;
        }
        if (value > 8) {
            value--;
        }
        return checksumPortion == value;
    }

    private static Result constructResult(Pair pair, Pair pair2) {
        String valueOf = String.valueOf((pair.getValue() * 4537077) + pair2.getValue());
        StringBuffer stringBuffer = new StringBuffer(14);
        for (int length = 13 - valueOf.length(); length > 0; length--) {
            stringBuffer.append('0');
        }
        stringBuffer.append(valueOf);
        int i = 0;
        for (int i2 = 0; i2 < 13; i2++) {
            int charAt = stringBuffer.charAt(i2) - '0';
            if ((i2 & 1) == 0) {
                charAt *= 3;
            }
            i += charAt;
        }
        int i3 = 10 - (i % 10);
        if (i3 == 10) {
            i3 = 0;
        }
        stringBuffer.append(i3);
        ResultPoint[] resultPoints = pair.getFinderPattern().getResultPoints();
        ResultPoint[] resultPoints2 = pair2.getFinderPattern().getResultPoints();
        return new Result(String.valueOf(stringBuffer.toString()), null, new ResultPoint[]{resultPoints[0], resultPoints[1], resultPoints2[0], resultPoints2[1]}, BarcodeFormat.RSS_14);
    }

    private DataCharacter decodeDataCharacter(BitArray bitArray, FinderPattern finderPattern, boolean z) throws NotFoundException {
        int[] iArr = this.dataCharacterCounters;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        iArr[4] = 0;
        iArr[5] = 0;
        iArr[6] = 0;
        iArr[7] = 0;
        int[] startEnd = finderPattern.getStartEnd();
        if (z) {
            recordPatternInReverse(bitArray, startEnd[0], iArr);
        } else {
            recordPattern(bitArray, startEnd[1] + 1, iArr);
            int i = 0;
            for (int length = iArr.length - 1; i < length; length--) {
                int i2 = iArr[i];
                iArr[i] = iArr[length];
                iArr[length] = i2;
                i++;
            }
        }
        int i3 = z ? 16 : 15;
        float count = count(iArr) / i3;
        int[] iArr2 = this.oddCounts;
        int[] iArr3 = this.evenCounts;
        float[] fArr = this.oddRoundingErrors;
        float[] fArr2 = this.evenRoundingErrors;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            float f = iArr[i4] / count;
            int i5 = (int) (f + 0.5f);
            if (i5 < 1) {
                i5 = 1;
            } else if (i5 > 8) {
                i5 = 8;
            }
            int i6 = i4 >> 1;
            if ((i4 & 1) == 0) {
                iArr2[i6] = i5;
                fArr[i6] = f - i5;
            } else {
                iArr3[i6] = i5;
                fArr2[i6] = f - i5;
            }
        }
        adjustOddEvenCounts(z, i3);
        int i7 = 0;
        int i8 = 0;
        for (int length2 = iArr2.length - 1; length2 >= 0; length2--) {
            i7 = (i7 * 9) + iArr2[length2];
            i8 += iArr2[length2];
        }
        int i9 = 0;
        int i10 = 0;
        for (int length3 = iArr3.length - 1; length3 >= 0; length3--) {
            i9 = (i9 * 9) + iArr3[length3];
            i10 += iArr3[length3];
        }
        int i11 = i7 + (i9 * 3);
        if (!z) {
            if ((i10 & 1) != 0 || i10 > 10 || i10 < 4) {
                throw NotFoundException.getNotFoundInstance();
            }
            int i12 = (10 - i10) / 2;
            int i13 = INSIDE_ODD_WIDEST[i12];
            return new DataCharacter((RSSUtils.getRSSvalue(iArr3, 9 - i13, false) * INSIDE_ODD_TOTAL_SUBSET[i12]) + RSSUtils.getRSSvalue(iArr2, i13, true) + INSIDE_GSUM[i12], i11);
        }
        if ((i8 & 1) != 0 || i8 > 12 || i8 < 4) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i14 = (12 - i8) / 2;
        int i15 = OUTSIDE_ODD_WIDEST[i14];
        return new DataCharacter((RSSUtils.getRSSvalue(iArr2, i15, false) * OUTSIDE_EVEN_TOTAL_SUBSET[i14]) + RSSUtils.getRSSvalue(iArr3, 9 - i15, true) + OUTSIDE_GSUM[i14], i11);
    }

    private Pair decodePair(BitArray bitArray, boolean z, int i, Hashtable hashtable) {
        try {
            FinderPattern parseFoundFinderPattern = parseFoundFinderPattern(bitArray, i, z, findFinderPattern(bitArray, 0, z));
            ResultPointCallback resultPointCallback = hashtable == null ? null : (ResultPointCallback) hashtable.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
            if (resultPointCallback != null) {
                float f = (r2[0] + r2[1]) / 2.0f;
                if (z) {
                    f = (bitArray.getSize() - 1) - f;
                }
                resultPointCallback.foundPossibleResultPoint(new ResultPoint(f, i));
            }
            DataCharacter decodeDataCharacter = decodeDataCharacter(bitArray, parseFoundFinderPattern, true);
            DataCharacter decodeDataCharacter2 = decodeDataCharacter(bitArray, parseFoundFinderPattern, false);
            return new Pair((decodeDataCharacter.getValue() * 1597) + decodeDataCharacter2.getValue(), decodeDataCharacter.getChecksumPortion() + (decodeDataCharacter2.getChecksumPortion() * 4), parseFoundFinderPattern);
        } catch (NotFoundException unused) {
            return null;
        }
    }

    private int[] findFinderPattern(BitArray bitArray, int i, boolean z) throws NotFoundException {
        int[] iArr = this.decodeFinderCounters;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        int size = bitArray.getSize();
        boolean z2 = false;
        while (i < size) {
            z2 = !bitArray.get(i);
            if (z == z2) {
                break;
            }
            i++;
        }
        int i2 = i;
        int i3 = 0;
        while (i < size) {
            if (bitArray.get(i) ^ z2) {
                iArr[i3] = iArr[i3] + 1;
            } else {
                if (i3 != 3) {
                    i3++;
                } else {
                    if (isFinderPattern(iArr)) {
                        return new int[]{i2, i};
                    }
                    i2 += iArr[0] + iArr[1];
                    iArr[0] = iArr[2];
                    iArr[1] = iArr[3];
                    iArr[2] = 0;
                    iArr[3] = 0;
                    i3--;
                }
                iArr[i3] = 1;
                z2 = !z2;
            }
            i++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private FinderPattern parseFoundFinderPattern(BitArray bitArray, int i, boolean z, int[] iArr) throws NotFoundException {
        int i2;
        int i3;
        boolean z2 = bitArray.get(iArr[0]);
        int i4 = iArr[0] - 1;
        while (i4 >= 0 && (bitArray.get(i4) ^ z2)) {
            i4--;
        }
        int i5 = i4 + 1;
        int i6 = iArr[0] - i5;
        int[] iArr2 = this.decodeFinderCounters;
        for (int length = iArr2.length - 1; length > 0; length--) {
            iArr2[length] = iArr2[length - 1];
        }
        iArr2[0] = i6;
        int parseFinderValue = parseFinderValue(iArr2, FINDER_PATTERNS);
        int i7 = iArr[1];
        if (z) {
            int size = (bitArray.getSize() - 1) - i5;
            i2 = (bitArray.getSize() - 1) - i7;
            i3 = size;
        } else {
            i2 = i7;
            i3 = i5;
        }
        return new FinderPattern(parseFinderValue, new int[]{i5, iArr[1]}, i3, i2, i);
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        addOrTally(this.possibleLeftPairs, decodePair(bitArray, false, i, hashtable));
        bitArray.reverse();
        addOrTally(this.possibleRightPairs, decodePair(bitArray, true, i, hashtable));
        bitArray.reverse();
        int size = this.possibleLeftPairs.size();
        int size2 = this.possibleRightPairs.size();
        for (int i2 = 0; i2 < size; i2++) {
            Pair pair = (Pair) this.possibleLeftPairs.elementAt(i2);
            if (pair.getCount() > 1) {
                for (int i3 = 0; i3 < size2; i3++) {
                    Pair pair2 = (Pair) this.possibleRightPairs.elementAt(i3);
                    if (pair2.getCount() > 1 && checkChecksum(pair, pair2)) {
                        return constructResult(pair, pair2);
                    }
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    @Override // com.google.zxing.oned.OneDReader, com.google.zxing.Reader
    public void reset() {
        this.possibleLeftPairs.setSize(0);
        this.possibleRightPairs.setSize(0);
    }
}
