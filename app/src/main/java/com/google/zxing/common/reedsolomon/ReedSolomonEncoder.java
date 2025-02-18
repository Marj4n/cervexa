package com.google.zxing.common.reedsolomon;

import java.util.Vector;

/* loaded from: classes.dex */
public final class ReedSolomonEncoder {
    private final Vector cachedGenerators;
    private final GenericGF field;

    public ReedSolomonEncoder(GenericGF genericGF) {
        if (!GenericGF.QR_CODE_FIELD_256.equals(genericGF)) {
            throw new IllegalArgumentException("Only QR Code is supported at this time");
        }
        this.field = genericGF;
        Vector vector = new Vector();
        this.cachedGenerators = vector;
        vector.addElement(new GenericGFPoly(genericGF, new int[]{1}));
    }

    private GenericGFPoly buildGenerator(int i) {
        if (i >= this.cachedGenerators.size()) {
            Vector vector = this.cachedGenerators;
            GenericGFPoly genericGFPoly = (GenericGFPoly) vector.elementAt(vector.size() - 1);
            for (int size = this.cachedGenerators.size(); size <= i; size++) {
                GenericGF genericGF = this.field;
                genericGFPoly = genericGFPoly.multiply(new GenericGFPoly(genericGF, new int[]{1, genericGF.exp(size - 1)}));
                this.cachedGenerators.addElement(genericGFPoly);
            }
        }
        return (GenericGFPoly) this.cachedGenerators.elementAt(i);
    }

    public void encode(int[] iArr, int i) {
        if (i == 0) {
            throw new IllegalArgumentException("No error correction bytes");
        }
        int length = iArr.length - i;
        if (length <= 0) {
            throw new IllegalArgumentException("No data bytes provided");
        }
        GenericGFPoly buildGenerator = buildGenerator(i);
        int[] iArr2 = new int[length];
        System.arraycopy(iArr, 0, iArr2, 0, length);
        int[] coefficients = new GenericGFPoly(this.field, iArr2).multiplyByMonomial(i, 1).divide(buildGenerator)[1].getCoefficients();
        int length2 = i - coefficients.length;
        for (int i2 = 0; i2 < length2; i2++) {
            iArr[length + i2] = 0;
        }
        System.arraycopy(coefficients, 0, iArr, length + length2, coefficients.length);
    }
}
