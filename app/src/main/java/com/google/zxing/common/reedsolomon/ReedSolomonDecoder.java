package com.google.zxing.common.reedsolomon;

/* loaded from: classes.dex */
public final class ReedSolomonDecoder {
    private final GenericGF field;

    public ReedSolomonDecoder(GenericGF genericGF) {
        this.field = genericGF;
    }

    private int[] findErrorLocations(GenericGFPoly genericGFPoly) throws ReedSolomonException {
        int degree = genericGFPoly.getDegree();
        int i = 0;
        if (degree == 1) {
            return new int[]{genericGFPoly.getCoefficient(1)};
        }
        int[] iArr = new int[degree];
        for (int i2 = 1; i2 < this.field.getSize() && i < degree; i2++) {
            if (genericGFPoly.evaluateAt(i2) == 0) {
                iArr[i] = this.field.inverse(i2);
                i++;
            }
        }
        if (i == degree) {
            return iArr;
        }
        throw new ReedSolomonException("Error locator degree does not match number of roots");
    }

    private int[] findErrorMagnitudes(GenericGFPoly genericGFPoly, int[] iArr, boolean z) {
        int length = iArr.length;
        int[] iArr2 = new int[length];
        for (int i = 0; i < length; i++) {
            int inverse = this.field.inverse(iArr[i]);
            int i2 = 1;
            for (int i3 = 0; i3 < length; i3++) {
                if (i != i3) {
                    int multiply = this.field.multiply(iArr[i3], inverse);
                    i2 = this.field.multiply(i2, (multiply & 1) == 0 ? multiply | 1 : multiply & (-2));
                }
            }
            iArr2[i] = this.field.multiply(genericGFPoly.evaluateAt(inverse), this.field.inverse(i2));
            if (z) {
                iArr2[i] = this.field.multiply(iArr2[i], inverse);
            }
        }
        return iArr2;
    }

    private GenericGFPoly[] runEuclideanAlgorithm(GenericGFPoly genericGFPoly, GenericGFPoly genericGFPoly2, int i) throws ReedSolomonException {
        if (genericGFPoly.getDegree() < genericGFPoly2.getDegree()) {
            genericGFPoly2 = genericGFPoly;
            genericGFPoly = genericGFPoly2;
        }
        GenericGFPoly one = this.field.getOne();
        GenericGFPoly zero = this.field.getZero();
        GenericGFPoly genericGFPoly3 = genericGFPoly2;
        GenericGFPoly genericGFPoly4 = genericGFPoly;
        GenericGFPoly genericGFPoly5 = genericGFPoly3;
        GenericGFPoly zero2 = this.field.getZero();
        GenericGFPoly one2 = this.field.getOne();
        while (genericGFPoly5.getDegree() >= i / 2) {
            if (genericGFPoly5.isZero()) {
                throw new ReedSolomonException("r_{i-1} was zero");
            }
            GenericGFPoly zero3 = this.field.getZero();
            int inverse = this.field.inverse(genericGFPoly5.getCoefficient(genericGFPoly5.getDegree()));
            while (genericGFPoly4.getDegree() >= genericGFPoly5.getDegree() && !genericGFPoly4.isZero()) {
                int degree = genericGFPoly4.getDegree() - genericGFPoly5.getDegree();
                int multiply = this.field.multiply(genericGFPoly4.getCoefficient(genericGFPoly4.getDegree()), inverse);
                zero3 = zero3.addOrSubtract(this.field.buildMonomial(degree, multiply));
                genericGFPoly4 = genericGFPoly4.addOrSubtract(genericGFPoly5.multiplyByMonomial(degree, multiply));
            }
            GenericGFPoly addOrSubtract = zero3.multiply(zero).addOrSubtract(one);
            GenericGFPoly addOrSubtract2 = zero3.multiply(one2).addOrSubtract(zero2);
            GenericGFPoly genericGFPoly6 = genericGFPoly4;
            genericGFPoly4 = genericGFPoly5;
            genericGFPoly5 = genericGFPoly6;
            GenericGFPoly genericGFPoly7 = zero;
            zero = addOrSubtract;
            one = genericGFPoly7;
            zero2 = one2;
            one2 = addOrSubtract2;
        }
        int coefficient = one2.getCoefficient(0);
        if (coefficient == 0) {
            throw new ReedSolomonException("sigmaTilde(0) was zero");
        }
        int inverse2 = this.field.inverse(coefficient);
        return new GenericGFPoly[]{one2.multiply(inverse2), genericGFPoly5.multiply(inverse2)};
    }

    public void decode(int[] iArr, int i) throws ReedSolomonException {
        GenericGFPoly genericGFPoly = new GenericGFPoly(this.field, iArr);
        int[] iArr2 = new int[i];
        boolean equals = this.field.equals(GenericGF.DATA_MATRIX_FIELD_256);
        boolean z = true;
        for (int i2 = 0; i2 < i; i2++) {
            int evaluateAt = genericGFPoly.evaluateAt(this.field.exp(equals ? i2 + 1 : i2));
            iArr2[(i - 1) - i2] = evaluateAt;
            if (evaluateAt != 0) {
                z = false;
            }
        }
        if (z) {
            return;
        }
        GenericGFPoly[] runEuclideanAlgorithm = runEuclideanAlgorithm(this.field.buildMonomial(i, 1), new GenericGFPoly(this.field, iArr2), i);
        GenericGFPoly genericGFPoly2 = runEuclideanAlgorithm[0];
        GenericGFPoly genericGFPoly3 = runEuclideanAlgorithm[1];
        int[] findErrorLocations = findErrorLocations(genericGFPoly2);
        int[] findErrorMagnitudes = findErrorMagnitudes(genericGFPoly3, findErrorLocations, equals);
        for (int i3 = 0; i3 < findErrorLocations.length; i3++) {
            int length = (iArr.length - 1) - this.field.log(findErrorLocations[i3]);
            if (length < 0) {
                throw new ReedSolomonException("Bad error location");
            }
            iArr[length] = GenericGF.addOrSubtract(iArr[length], findErrorMagnitudes[i3]);
        }
    }
}
