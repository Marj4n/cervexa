package com.google.zxing.multi.qrcode.detector;

import com.github.mikephil.charting.utils.Utils;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
final class MultiFinderPatternFinder extends FinderPatternFinder {
    private static final float DIFF_MODSIZE_CUTOFF = 0.5f;
    private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05f;
    private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
    private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0f;
    private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0f;

    private static class ModuleSizeComparator implements Comparator {
        private ModuleSizeComparator() {
        }

        @Override // com.google.zxing.common.Comparator
        public int compare(Object obj, Object obj2) {
            double estimatedModuleSize = ((FinderPattern) obj2).getEstimatedModuleSize() - ((FinderPattern) obj).getEstimatedModuleSize();
            if (estimatedModuleSize < Utils.DOUBLE_EPSILON) {
                return -1;
            }
            return estimatedModuleSize > Utils.DOUBLE_EPSILON ? 1 : 0;
        }
    }

    MultiFinderPatternFinder(BitMatrix bitMatrix) {
        super(bitMatrix);
    }

    MultiFinderPatternFinder(BitMatrix bitMatrix, ResultPointCallback resultPointCallback) {
        super(bitMatrix, resultPointCallback);
    }

    private FinderPattern[][] selectBestPatterns() throws NotFoundException {
        Vector possibleCenters = getPossibleCenters();
        int size = possibleCenters.size();
        int i = 3;
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        char c = 0;
        if (size == 3) {
            return new FinderPattern[][]{new FinderPattern[]{(FinderPattern) possibleCenters.elementAt(0), (FinderPattern) possibleCenters.elementAt(1), (FinderPattern) possibleCenters.elementAt(2)}};
        }
        Collections.insertionSort(possibleCenters, new ModuleSizeComparator());
        Vector vector = new Vector();
        int i2 = 0;
        while (i2 < size - 2) {
            FinderPattern finderPattern = (FinderPattern) possibleCenters.elementAt(i2);
            if (finderPattern != null) {
                int i3 = i2 + 1;
                while (i3 < size - 1) {
                    FinderPattern finderPattern2 = (FinderPattern) possibleCenters.elementAt(i3);
                    if (finderPattern2 != null) {
                        float estimatedModuleSize = (finderPattern.getEstimatedModuleSize() - finderPattern2.getEstimatedModuleSize()) / Math.min(finderPattern.getEstimatedModuleSize(), finderPattern2.getEstimatedModuleSize());
                        float abs = Math.abs(finderPattern.getEstimatedModuleSize() - finderPattern2.getEstimatedModuleSize());
                        float f = DIFF_MODSIZE_CUTOFF_PERCENT;
                        float f2 = 0.5f;
                        if (abs > 0.5f && estimatedModuleSize >= DIFF_MODSIZE_CUTOFF_PERCENT) {
                            break;
                        }
                        int i4 = i3 + 1;
                        while (i4 < size) {
                            FinderPattern finderPattern3 = (FinderPattern) possibleCenters.elementAt(i4);
                            if (finderPattern3 != null) {
                                float estimatedModuleSize2 = (finderPattern2.getEstimatedModuleSize() - finderPattern3.getEstimatedModuleSize()) / Math.min(finderPattern2.getEstimatedModuleSize(), finderPattern3.getEstimatedModuleSize());
                                if (Math.abs(finderPattern2.getEstimatedModuleSize() - finderPattern3.getEstimatedModuleSize()) > f2 && estimatedModuleSize2 >= f) {
                                    break;
                                }
                                FinderPattern[] finderPatternArr = new FinderPattern[i];
                                finderPatternArr[c] = finderPattern;
                                finderPatternArr[1] = finderPattern2;
                                finderPatternArr[2] = finderPattern3;
                                ResultPoint.orderBestPatterns(finderPatternArr);
                                FinderPatternInfo finderPatternInfo = new FinderPatternInfo(finderPatternArr);
                                float distance = ResultPoint.distance(finderPatternInfo.getTopLeft(), finderPatternInfo.getBottomLeft());
                                float distance2 = ResultPoint.distance(finderPatternInfo.getTopRight(), finderPatternInfo.getBottomLeft());
                                float distance3 = ResultPoint.distance(finderPatternInfo.getTopLeft(), finderPatternInfo.getTopRight());
                                float estimatedModuleSize3 = (distance + distance3) / (finderPattern.getEstimatedModuleSize() * 2.0f);
                                if (estimatedModuleSize3 <= MAX_MODULE_COUNT_PER_EDGE && estimatedModuleSize3 >= MIN_MODULE_COUNT_PER_EDGE && Math.abs((distance - distance3) / Math.min(distance, distance3)) < 0.1f) {
                                    float sqrt = (float) Math.sqrt((distance * distance) + (distance3 * distance3));
                                    if (Math.abs((distance2 - sqrt) / Math.min(distance2, sqrt)) < 0.1f) {
                                        vector.addElement(finderPatternArr);
                                    }
                                }
                            }
                            i4++;
                            i = 3;
                            c = 0;
                            f = DIFF_MODSIZE_CUTOFF_PERCENT;
                            f2 = 0.5f;
                        }
                    }
                    i3++;
                    i = 3;
                    c = 0;
                }
            }
            i2++;
            i = 3;
            c = 0;
        }
        if (vector.isEmpty()) {
            throw NotFoundException.getNotFoundInstance();
        }
        FinderPattern[][] finderPatternArr2 = new FinderPattern[vector.size()][];
        for (int i5 = 0; i5 < vector.size(); i5++) {
            finderPatternArr2[i5] = (FinderPattern[]) vector.elementAt(i5);
        }
        return finderPatternArr2;
    }

    public FinderPatternInfo[] findMulti(Hashtable hashtable) throws NotFoundException {
        boolean z = hashtable != null && hashtable.containsKey(DecodeHintType.TRY_HARDER);
        BitMatrix image = getImage();
        int height = image.getHeight();
        int width = image.getWidth();
        int i = (int) ((height / 228.0f) * 3.0f);
        if (i < 3 || z) {
            i = 3;
        }
        int[] iArr = new int[5];
        for (int i2 = i - 1; i2 < height; i2 += i) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = 0;
            iArr[3] = 0;
            iArr[4] = 0;
            int i3 = 0;
            int i4 = 0;
            while (i3 < width) {
                if (image.get(i3, i2)) {
                    if ((i4 & 1) == 1) {
                        i4++;
                    }
                    iArr[i4] = iArr[i4] + 1;
                } else if ((i4 & 1) != 0) {
                    iArr[i4] = iArr[i4] + 1;
                } else if (i4 != 4) {
                    i4++;
                    iArr[i4] = iArr[i4] + 1;
                } else if (foundPatternCross(iArr)) {
                    if (!handlePossibleCenter(iArr, i2, i3)) {
                        do {
                            i3++;
                            if (i3 >= width) {
                                break;
                            }
                        } while (!image.get(i3, i2));
                        i3--;
                    }
                    iArr[0] = 0;
                    iArr[1] = 0;
                    iArr[2] = 0;
                    iArr[3] = 0;
                    iArr[4] = 0;
                    i4 = 0;
                } else {
                    iArr[0] = iArr[2];
                    iArr[1] = iArr[3];
                    iArr[2] = iArr[4];
                    iArr[3] = 1;
                    iArr[4] = 0;
                    i4 = 3;
                }
                i3++;
            }
            if (foundPatternCross(iArr)) {
                handlePossibleCenter(iArr, i2, width);
            }
        }
        FinderPattern[][] selectBestPatterns = selectBestPatterns();
        Vector vector = new Vector();
        for (FinderPattern[] finderPatternArr : selectBestPatterns) {
            ResultPoint.orderBestPatterns(finderPatternArr);
            vector.addElement(new FinderPatternInfo(finderPatternArr));
        }
        if (vector.isEmpty()) {
            return EMPTY_RESULT_ARRAY;
        }
        FinderPatternInfo[] finderPatternInfoArr = new FinderPatternInfo[vector.size()];
        for (int i5 = 0; i5 < vector.size(); i5++) {
            finderPatternInfoArr[i5] = (FinderPatternInfo) vector.elementAt(i5);
        }
        return finderPatternInfoArr;
    }
}
