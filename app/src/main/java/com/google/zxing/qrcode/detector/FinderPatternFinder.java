package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.jieli.stream.dv.running2.util.VideoManager;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public class FinderPatternFinder {
    private static final int CENTER_QUORUM = 2;
    private static final int INTEGER_MATH_SHIFT = 8;
    protected static final int MAX_MODULES = 57;
    protected static final int MIN_SKIP = 3;
    private final int[] crossCheckStateCount;
    private boolean hasSkipped;
    private final BitMatrix image;
    private final Vector possibleCenters;
    private final ResultPointCallback resultPointCallback;

    private static class CenterComparator implements Comparator {
        private final float average;

        private CenterComparator(float f) {
            this.average = f;
        }

        @Override // com.google.zxing.common.Comparator
        public int compare(Object obj, Object obj2) {
            FinderPattern finderPattern = (FinderPattern) obj2;
            FinderPattern finderPattern2 = (FinderPattern) obj;
            if (finderPattern.getCount() != finderPattern2.getCount()) {
                return finderPattern.getCount() - finderPattern2.getCount();
            }
            float abs = Math.abs(finderPattern.getEstimatedModuleSize() - this.average);
            float abs2 = Math.abs(finderPattern2.getEstimatedModuleSize() - this.average);
            if (abs < abs2) {
                return 1;
            }
            return abs == abs2 ? 0 : -1;
        }
    }

    private static class FurthestFromAverageComparator implements Comparator {
        private final float average;

        private FurthestFromAverageComparator(float f) {
            this.average = f;
        }

        @Override // com.google.zxing.common.Comparator
        public int compare(Object obj, Object obj2) {
            float abs = Math.abs(((FinderPattern) obj2).getEstimatedModuleSize() - this.average);
            float abs2 = Math.abs(((FinderPattern) obj).getEstimatedModuleSize() - this.average);
            if (abs < abs2) {
                return -1;
            }
            return abs == abs2 ? 0 : 1;
        }
    }

    public FinderPatternFinder(BitMatrix bitMatrix) {
        this(bitMatrix, null);
    }

    public FinderPatternFinder(BitMatrix bitMatrix, ResultPointCallback resultPointCallback) {
        this.image = bitMatrix;
        this.possibleCenters = new Vector();
        this.crossCheckStateCount = new int[5];
        this.resultPointCallback = resultPointCallback;
    }

    private static float centerFromEnd(int[] iArr, int i) {
        return ((i - iArr[4]) - iArr[3]) - (iArr[2] / 2.0f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
    
        if (r2[1] <= r13) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003f, code lost:
    
        if (r3 < 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0045, code lost:
    
        if (r0.get(r3, r12) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0049, code lost:
    
        if (r2[0] > r13) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x004b, code lost:
    
        r2[0] = r2[0] + 1;
        r3 = r3 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0055, code lost:
    
        if (r2[0] <= r13) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0057, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0058, code lost:
    
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
    
        if (r11 >= r1) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x005f, code lost:
    
        if (r0.get(r11, r12) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0061, code lost:
    
        r2[2] = r2[2] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0069, code lost:
    
        if (r11 != r1) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006d, code lost:
    
        if (r11 >= r1) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0073, code lost:
    
        if (r0.get(r11, r12) != false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0077, code lost:
    
        if (r2[3] >= r13) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0079, code lost:
    
        r2[3] = r2[3] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0081, code lost:
    
        if (r11 == r1) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0085, code lost:
    
        if (r2[3] < r13) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0089, code lost:
    
        if (r11 >= r1) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008f, code lost:
    
        if (r0.get(r11, r12) == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0093, code lost:
    
        if (r2[4] >= r13) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0095, code lost:
    
        r2[4] = r2[4] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009f, code lost:
    
        if (r2[4] < r13) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a1, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b7, code lost:
    
        if ((java.lang.Math.abs(((((r2[0] + r2[1]) + r2[2]) + r2[3]) + r2[4]) - r14) * 5) < r14) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b9, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00be, code lost:
    
        if (foundPatternCross(r2) == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c4, code lost:
    
        return centerFromEnd(r2, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:?, code lost:
    
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private float crossCheckHorizontal(int r11, int r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 197
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.detector.FinderPatternFinder.crossCheckHorizontal(int, int, int, int):float");
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
    
        if (r2[1] <= r13) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003f, code lost:
    
        if (r3 < 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0045, code lost:
    
        if (r0.get(r12, r3) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0049, code lost:
    
        if (r2[0] > r13) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x004b, code lost:
    
        r2[0] = r2[0] + 1;
        r3 = r3 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0055, code lost:
    
        if (r2[0] <= r13) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0057, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0058, code lost:
    
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
    
        if (r11 >= r1) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x005f, code lost:
    
        if (r0.get(r12, r11) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0061, code lost:
    
        r2[2] = r2[2] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0069, code lost:
    
        if (r11 != r1) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006d, code lost:
    
        if (r11 >= r1) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0073, code lost:
    
        if (r0.get(r12, r11) != false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0077, code lost:
    
        if (r2[3] >= r13) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0079, code lost:
    
        r2[3] = r2[3] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0081, code lost:
    
        if (r11 == r1) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0085, code lost:
    
        if (r2[3] < r13) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0089, code lost:
    
        if (r11 >= r1) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008f, code lost:
    
        if (r0.get(r12, r11) == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0093, code lost:
    
        if (r2[4] >= r13) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0095, code lost:
    
        r2[4] = r2[4] + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009f, code lost:
    
        if (r2[4] < r13) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a1, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b9, code lost:
    
        if ((java.lang.Math.abs(((((r2[0] + r2[1]) + r2[2]) + r2[3]) + r2[4]) - r14) * 5) < (r14 * 2)) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00bb, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00c0, code lost:
    
        if (foundPatternCross(r2) == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c6, code lost:
    
        return centerFromEnd(r2, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:?, code lost:
    
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private float crossCheckVertical(int r11, int r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 199
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.detector.FinderPatternFinder.crossCheckVertical(int, int, int, int):float");
    }

    private int findRowSkip() {
        int size = this.possibleCenters.size();
        if (size <= 1) {
            return 0;
        }
        FinderPattern finderPattern = null;
        for (int i = 0; i < size; i++) {
            FinderPattern finderPattern2 = (FinderPattern) this.possibleCenters.elementAt(i);
            if (finderPattern2.getCount() >= 2) {
                if (finderPattern != null) {
                    this.hasSkipped = true;
                    return ((int) (Math.abs(finderPattern.getX() - finderPattern2.getX()) - Math.abs(finderPattern.getY() - finderPattern2.getY()))) / 2;
                }
                finderPattern = finderPattern2;
            }
        }
        return 0;
    }

    protected static boolean foundPatternCross(int[] iArr) {
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < 5; i4++) {
            int i5 = iArr[i4];
            if (i5 == 0) {
                return false;
            }
            i3 += i5;
        }
        return i3 >= 7 && Math.abs(i - (iArr[0] << 8)) < (i2 = (i = (i3 << 8) / 7) / 2) && Math.abs(i - (iArr[1] << 8)) < i2 && Math.abs((i * 3) - (iArr[2] << 8)) < i2 * 3 && Math.abs(i - (iArr[3] << 8)) < i2 && Math.abs(i - (iArr[4] << 8)) < i2;
    }

    private int[] getCrossCheckStateCount() {
        int[] iArr = this.crossCheckStateCount;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        iArr[4] = 0;
        return iArr;
    }

    private boolean haveMultiplyConfirmedCenters() {
        int size = this.possibleCenters.size();
        float f = 0.0f;
        int i = 0;
        float f2 = 0.0f;
        for (int i2 = 0; i2 < size; i2++) {
            FinderPattern finderPattern = (FinderPattern) this.possibleCenters.elementAt(i2);
            if (finderPattern.getCount() >= 2) {
                i++;
                f2 += finderPattern.getEstimatedModuleSize();
            }
        }
        if (i < 3) {
            return false;
        }
        float f3 = f2 / size;
        for (int i3 = 0; i3 < size; i3++) {
            f += Math.abs(((FinderPattern) this.possibleCenters.elementAt(i3)).getEstimatedModuleSize() - f3);
        }
        return f <= f2 * 0.05f;
    }

    private FinderPattern[] selectBestPatterns() throws NotFoundException {
        int size = this.possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        float f = 0.0f;
        if (size > 3) {
            float f2 = 0.0f;
            float f3 = 0.0f;
            for (int i = 0; i < size; i++) {
                float estimatedModuleSize = ((FinderPattern) this.possibleCenters.elementAt(i)).getEstimatedModuleSize();
                f2 += estimatedModuleSize;
                f3 += estimatedModuleSize * estimatedModuleSize;
            }
            float f4 = f2 / size;
            float sqrt = (float) Math.sqrt((f3 / r0) - (f4 * f4));
            Collections.insertionSort(this.possibleCenters, new FurthestFromAverageComparator(f4));
            float max = Math.max(0.2f * f4, sqrt);
            int i2 = 0;
            while (i2 < this.possibleCenters.size() && this.possibleCenters.size() > 3) {
                if (Math.abs(((FinderPattern) this.possibleCenters.elementAt(i2)).getEstimatedModuleSize() - f4) > max) {
                    this.possibleCenters.removeElementAt(i2);
                    i2--;
                }
                i2++;
            }
        }
        if (this.possibleCenters.size() > 3) {
            for (int i3 = 0; i3 < this.possibleCenters.size(); i3++) {
                f += ((FinderPattern) this.possibleCenters.elementAt(i3)).getEstimatedModuleSize();
            }
            Collections.insertionSort(this.possibleCenters, new CenterComparator(f / this.possibleCenters.size()));
            this.possibleCenters.setSize(3);
        }
        return new FinderPattern[]{(FinderPattern) this.possibleCenters.elementAt(0), (FinderPattern) this.possibleCenters.elementAt(1), (FinderPattern) this.possibleCenters.elementAt(2)};
    }

    FinderPatternInfo find(Hashtable hashtable) throws NotFoundException {
        boolean z = hashtable != null && hashtable.containsKey(DecodeHintType.TRY_HARDER);
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = (height * 3) / VideoManager.ERROR_CLOSE_RECORD;
        if (i < 3 || z) {
            i = 3;
        }
        int[] iArr = new int[5];
        int i2 = i - 1;
        boolean z2 = false;
        while (i2 < height && !z2) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = 0;
            iArr[3] = 0;
            iArr[4] = 0;
            int i3 = 0;
            int i4 = 0;
            while (i3 < width) {
                if (this.image.get(i3, i2)) {
                    if ((i4 & 1) == 1) {
                        i4++;
                    }
                    iArr[i4] = iArr[i4] + 1;
                } else if ((i4 & 1) != 0) {
                    iArr[i4] = iArr[i4] + 1;
                } else if (i4 == 4) {
                    if (!foundPatternCross(iArr)) {
                        iArr[0] = iArr[2];
                        iArr[1] = iArr[3];
                        iArr[2] = iArr[4];
                        iArr[3] = 1;
                        iArr[4] = 0;
                    } else if (handlePossibleCenter(iArr, i2, i3)) {
                        if (this.hasSkipped) {
                            z2 = haveMultiplyConfirmedCenters();
                        } else {
                            int findRowSkip = findRowSkip();
                            if (findRowSkip > iArr[2]) {
                                i2 += (findRowSkip - iArr[2]) - 2;
                                i3 = width - 1;
                            }
                        }
                        iArr[0] = 0;
                        iArr[1] = 0;
                        iArr[2] = 0;
                        iArr[3] = 0;
                        iArr[4] = 0;
                        i = 2;
                        i4 = 0;
                    } else {
                        iArr[0] = iArr[2];
                        iArr[1] = iArr[3];
                        iArr[2] = iArr[4];
                        iArr[3] = 1;
                        iArr[4] = 0;
                    }
                    i4 = 3;
                } else {
                    i4++;
                    iArr[i4] = iArr[i4] + 1;
                }
                i3++;
            }
            if (foundPatternCross(iArr) && handlePossibleCenter(iArr, i2, width)) {
                i = iArr[0];
                if (this.hasSkipped) {
                    z2 = haveMultiplyConfirmedCenters();
                }
            }
            i2 += i;
        }
        FinderPattern[] selectBestPatterns = selectBestPatterns();
        ResultPoint.orderBestPatterns(selectBestPatterns);
        return new FinderPatternInfo(selectBestPatterns);
    }

    protected BitMatrix getImage() {
        return this.image;
    }

    protected Vector getPossibleCenters() {
        return this.possibleCenters;
    }

    protected boolean handlePossibleCenter(int[] iArr, int i, int i2) {
        boolean z = false;
        int i3 = iArr[0] + iArr[1] + iArr[2] + iArr[3] + iArr[4];
        int centerFromEnd = (int) centerFromEnd(iArr, i2);
        float crossCheckVertical = crossCheckVertical(i, centerFromEnd, iArr[2], i3);
        if (!Float.isNaN(crossCheckVertical)) {
            float crossCheckHorizontal = crossCheckHorizontal(centerFromEnd, (int) crossCheckVertical, iArr[2], i3);
            if (!Float.isNaN(crossCheckHorizontal)) {
                float f = i3 / 7.0f;
                int size = this.possibleCenters.size();
                int i4 = 0;
                while (true) {
                    if (i4 >= size) {
                        break;
                    }
                    FinderPattern finderPattern = (FinderPattern) this.possibleCenters.elementAt(i4);
                    if (finderPattern.aboutEquals(f, crossCheckVertical, crossCheckHorizontal)) {
                        finderPattern.incrementCount();
                        z = true;
                        break;
                    }
                    i4++;
                }
                if (!z) {
                    FinderPattern finderPattern2 = new FinderPattern(crossCheckHorizontal, crossCheckVertical, f);
                    this.possibleCenters.addElement(finderPattern2);
                    ResultPointCallback resultPointCallback = this.resultPointCallback;
                    if (resultPointCallback != null) {
                        resultPointCallback.foundPossibleResultPoint(finderPattern2);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
