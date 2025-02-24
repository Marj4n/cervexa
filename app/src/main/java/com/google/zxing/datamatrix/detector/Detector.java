package com.google.zxing.datamatrix.detector;

import com.github.mikephil.charting.data.Entry;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class Detector {
    private static final Integer[] INTEGERS = {new Integer(0), new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
    private final BitMatrix image;
    private final WhiteRectangleDetector rectangleDetector;

    private static class ResultPointsAndTransitions {
        private final ResultPoint from;
        private final ResultPoint to;
        private final int transitions;

        private ResultPointsAndTransitions(ResultPoint resultPoint, ResultPoint resultPoint2, int i) {
            this.from = resultPoint;
            this.to = resultPoint2;
            this.transitions = i;
        }

        public ResultPoint getFrom() {
            return this.from;
        }

        public ResultPoint getTo() {
            return this.to;
        }

        public int getTransitions() {
            return this.transitions;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.from);
            stringBuffer.append("/");
            stringBuffer.append(this.to);
            stringBuffer.append('/');
            stringBuffer.append(this.transitions);
            return stringBuffer.toString();
        }
    }

    private static class ResultPointsAndTransitionsComparator implements Comparator {
        private ResultPointsAndTransitionsComparator() {
        }

        @Override // com.google.zxing.common.Comparator
        public int compare(Object obj, Object obj2) {
            return ((ResultPointsAndTransitions) obj).getTransitions() - ((ResultPointsAndTransitions) obj2).getTransitions();
        }
    }

    public Detector(BitMatrix bitMatrix) throws NotFoundException {
        this.image = bitMatrix;
        this.rectangleDetector = new WhiteRectangleDetector(bitMatrix);
    }

    private ResultPoint correctTopRight(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i) {
        float f = i;
        float distance = distance(resultPoint, resultPoint2) / f;
        float distance2 = distance(resultPoint3, resultPoint4);
        ResultPoint resultPoint5 = new ResultPoint(resultPoint4.getX() + (((resultPoint4.getX() - resultPoint3.getX()) / distance2) * distance), resultPoint4.getY() + (distance * ((resultPoint4.getY() - resultPoint3.getY()) / distance2)));
        float distance3 = distance(resultPoint, resultPoint2) / f;
        float distance4 = distance(resultPoint2, resultPoint4);
        ResultPoint resultPoint6 = new ResultPoint(resultPoint4.getX() + (((resultPoint4.getX() - resultPoint2.getX()) / distance4) * distance3), resultPoint4.getY() + (distance3 * ((resultPoint4.getY() - resultPoint2.getY()) / distance4)));
        if (isValid(resultPoint5)) {
            return (isValid(resultPoint6) && Math.abs(transitionsBetween(resultPoint3, resultPoint5).getTransitions() - transitionsBetween(resultPoint2, resultPoint5).getTransitions()) > Math.abs(transitionsBetween(resultPoint3, resultPoint6).getTransitions() - transitionsBetween(resultPoint2, resultPoint6).getTransitions())) ? resultPoint6 : resultPoint5;
        }
        if (isValid(resultPoint6)) {
            return resultPoint6;
        }
        return null;
    }

    private ResultPoint correctTopRightRectangular(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i, int i2) {
        float distance = distance(resultPoint, resultPoint2) / i;
        float distance2 = distance(resultPoint3, resultPoint4);
        ResultPoint resultPoint5 = new ResultPoint(resultPoint4.getX() + (((resultPoint4.getX() - resultPoint3.getX()) / distance2) * distance), resultPoint4.getY() + (distance * ((resultPoint4.getY() - resultPoint3.getY()) / distance2)));
        float distance3 = distance(resultPoint, resultPoint3) / i2;
        float distance4 = distance(resultPoint2, resultPoint4);
        ResultPoint resultPoint6 = new ResultPoint(resultPoint4.getX() + (((resultPoint4.getX() - resultPoint2.getX()) / distance4) * distance3), resultPoint4.getY() + (distance3 * ((resultPoint4.getY() - resultPoint2.getY()) / distance4)));
        if (isValid(resultPoint5)) {
            return (isValid(resultPoint6) && Math.abs(i - transitionsBetween(resultPoint3, resultPoint5).getTransitions()) + Math.abs(i2 - transitionsBetween(resultPoint2, resultPoint5).getTransitions()) > Math.abs(i - transitionsBetween(resultPoint3, resultPoint6).getTransitions()) + Math.abs(i2 - transitionsBetween(resultPoint2, resultPoint6).getTransitions())) ? resultPoint6 : resultPoint5;
        }
        if (isValid(resultPoint6)) {
            return resultPoint6;
        }
        return null;
    }

    private static int distance(ResultPoint resultPoint, ResultPoint resultPoint2) {
        return round((float) Math.sqrt(((resultPoint.getX() - resultPoint2.getX()) * (resultPoint.getX() - resultPoint2.getX())) + ((resultPoint.getY() - resultPoint2.getY()) * (resultPoint.getY() - resultPoint2.getY()))));
    }

    private static void increment(Hashtable hashtable, ResultPoint resultPoint) {
        Integer num = (Integer) hashtable.get(resultPoint);
        hashtable.put(resultPoint, num == null ? INTEGERS[1] : INTEGERS[num.intValue() + 1]);
    }

    private boolean isValid(ResultPoint resultPoint) {
        return resultPoint.getX() >= 0.0f && resultPoint.getX() < ((float) this.image.width) && resultPoint.getY() > 0.0f && resultPoint.getY() < ((float) this.image.height);
    }

    private static int round(float f) {
        return (int) (f + 0.5f);
    }

    private static BitMatrix sampleGrid(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i, int i2) throws NotFoundException {
        float f = i - 0.5f;
        float f2 = i2 - 0.5f;
        return GridSampler.getInstance().sampleGrid(bitMatrix, i, i2, 0.5f, 0.5f, f, 0.5f, f, f2, 0.5f, f2, resultPoint.getX(), resultPoint.getY(), resultPoint4.getX(), resultPoint4.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint2.getX(), resultPoint2.getY());
    }

    private ResultPointsAndTransitions transitionsBetween(ResultPoint resultPoint, ResultPoint resultPoint2) {
        int x = (int) resultPoint.getX();
        int y = (int) resultPoint.getY();
        int x2 = (int) resultPoint2.getX();
        int y2 = (int) resultPoint2.getY();
        int i = 0;
        boolean z = Math.abs(y2 - y) > Math.abs(x2 - x);
        if (z) {
            y = x;
            x = y;
            y2 = x2;
            x2 = y2;
        }
        int abs = Math.abs(x2 - x);
        int abs2 = Math.abs(y2 - y);
        int i2 = (-abs) >> 1;
        int i3 = y < y2 ? 1 : -1;
        int i4 = x >= x2 ? -1 : 1;
        boolean z2 = this.image.get(z ? y : x, z ? x : y);
        while (x != x2) {
            boolean z3 = this.image.get(z ? y : x, z ? x : y);
            if (z3 != z2) {
                i++;
                z2 = z3;
            }
            i2 += abs2;
            if (i2 > 0) {
                if (y == y2) {
                    break;
                }
                y += i3;
                i2 -= abs;
            }
            x += i4;
        }
        return new ResultPointsAndTransitions(resultPoint, resultPoint2, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v1, types: [com.google.zxing.ResultPoint] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.util.Hashtable] */
    /* JADX WARN: Type inference failed for: r14v3, types: [com.google.zxing.ResultPoint] */
    /* JADX WARN: Type inference failed for: r15v1, types: [com.google.zxing.ResultPoint] */
    /* JADX WARN: Type inference failed for: r15v3, types: [com.google.zxing.ResultPoint, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r24v0, types: [com.google.zxing.datamatrix.detector.Detector] */
    /* JADX WARN: Type inference failed for: r2v5, types: [com.google.zxing.ResultPoint[]] */
    /* JADX WARN: Type inference failed for: r4v5, types: [com.google.zxing.ResultPoint[]] */
    public DetectorResult detect() throws NotFoundException {
        int i;
        int i2;
        BitMatrix bitMatrix;
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        ResultPoint resultPoint4;
        ResultPoint[] detect = this.rectangleDetector.detect();
        ResultPoint resultPoint5 = detect[0];
        ResultPoint resultPoint6 = detect[1];
        ResultPoint resultPoint7 = detect[2];
        ResultPoint resultPoint8 = detect[3];
        Vector vector = new Vector(4);
        vector.addElement(transitionsBetween(resultPoint5, resultPoint6));
        vector.addElement(transitionsBetween(resultPoint5, resultPoint7));
        vector.addElement(transitionsBetween(resultPoint6, resultPoint8));
        vector.addElement(transitionsBetween(resultPoint7, resultPoint8));
        Entry anonymousClass1 = null;
        Collections.insertionSort(vector, new ResultPointsAndTransitionsComparator());
        ResultPointsAndTransitions resultPointsAndTransitions = (ResultPointsAndTransitions) vector.elementAt(0);
        ResultPointsAndTransitions resultPointsAndTransitions2 = (ResultPointsAndTransitions) vector.elementAt(1);
        Hashtable hashtable = new Hashtable();
        increment(hashtable, resultPointsAndTransitions.getFrom());
        increment(hashtable, resultPointsAndTransitions.getTo());
        increment(hashtable, resultPointsAndTransitions2.getFrom());
        increment(hashtable, resultPointsAndTransitions2.getTo());
        Enumeration keys = hashtable.keys();
        Object obj = null;
        Object obj2 = null;
        while (keys.hasMoreElements()) {
            Entry r15 = (Entry) keys.nextElement();
            if (((Integer) hashtable.get(r15)).intValue() == 2) {
                obj = r15;
            } else if (anonymousClass1 == null) {
                anonymousClass1 = r15;
            } else {
                obj2 = r15;
            }
        }
        if (anonymousClass1 == null || obj == null || obj2 == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        int r4 = new Entry[]{anonymousClass1, obj, obj2};
        ResultPoint.orderBestPatterns(r4);
        int r12 = r4[0];
        int r14 = r4[1];
        int r152 = r4[2];
        ResultPoint resultPoint9 = !hashtable.containsKey(resultPoint5) ? resultPoint5 : !hashtable.containsKey(resultPoint6) ? resultPoint6 : !hashtable.containsKey(resultPoint7) ? resultPoint7 : resultPoint8;
        int transitions = transitionsBetween(r152, resultPoint9).getTransitions();
        int transitions2 = transitionsBetween(r12, resultPoint9).getTransitions();
        if ((transitions & 1) == 1) {
            transitions++;
        }
        int i3 = transitions + 2;
        if ((transitions2 & 1) == 1) {
            transitions2++;
        }
        int i4 = transitions2 + 2;
        if (i3 * 4 >= i4 * 7 || i4 * 4 >= i3 * 7) {
            ResultPoint correctTopRightRectangular = correctTopRightRectangular(r14, r12, r152, resultPoint9, i3, i4);
            if (correctTopRightRectangular != null) {
                resultPoint9 = correctTopRightRectangular;
            }
            int transitions3 = transitionsBetween(r152, resultPoint9).getTransitions();
            int transitions4 = transitionsBetween(r12, resultPoint9).getTransitions();
            if ((transitions3 & 1) == 1) {
                transitions3++;
            }
            i = transitions3;
            if ((transitions4 & 1) == 1) {
                transitions4++;
            }
            i2 = transitions4;
            bitMatrix = this.image;
            resultPoint = r152;
            resultPoint2 = r14;
            resultPoint3 = r12;
            resultPoint4 = resultPoint9;
        } else {
            ResultPoint correctTopRight = correctTopRight(r14, r12, r152, resultPoint9, Math.min(i4, i3));
            if (correctTopRight != null) {
                resultPoint9 = correctTopRight;
            }
            int max = Math.max(transitionsBetween(r152, resultPoint9).getTransitions(), transitionsBetween(r12, resultPoint9).getTransitions()) + 1;
            if ((max & 1) == 1) {
                max++;
            }
            i2 = max;
            bitMatrix = this.image;
            resultPoint = r152;
            resultPoint2 = r14;
            resultPoint3 = r12;
            resultPoint4 = resultPoint9;
            i = i2;
        }
        return new DetectorResult(sampleGrid(bitMatrix, resultPoint, resultPoint2, resultPoint3, resultPoint4, i, i2), new ResultPoint[]{r152, r14, r12, resultPoint9});
    }
}
