package com.google.zxing;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

/* loaded from: classes.dex */
public abstract class Binarizer {
    private final LuminanceSource source;

    protected Binarizer(LuminanceSource luminanceSource) {
        if (luminanceSource == null) {
            throw new IllegalArgumentException("Source must be non-null.");
        }
        this.source = luminanceSource;
    }

    public abstract Binarizer createBinarizer(LuminanceSource luminanceSource);

    public abstract BitMatrix getBlackMatrix() throws NotFoundException;

    public abstract BitArray getBlackRow(int i, BitArray bitArray) throws NotFoundException;

    public LuminanceSource getLuminanceSource() {
        return this.source;
    }
}
