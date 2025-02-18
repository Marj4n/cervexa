package com.google.zxing.oned.rss.expanded;

import com.google.zxing.common.BitArray;
import java.util.Vector;

/* loaded from: classes.dex */
final class BitArrayBuilder {
    private BitArrayBuilder() {
    }

    static BitArray buildBitArray(Vector vector) {
        int size = (vector.size() << 1) - 1;
        if (((ExpandedPair) vector.lastElement()).getRightChar() == null) {
            size--;
        }
        BitArray bitArray = new BitArray(size * 12);
        int i = 0;
        int value = ((ExpandedPair) vector.elementAt(0)).getRightChar().getValue();
        for (int i2 = 11; i2 >= 0; i2--) {
            if (((1 << i2) & value) != 0) {
                bitArray.set(i);
            }
            i++;
        }
        for (int i3 = 1; i3 < vector.size(); i3++) {
            ExpandedPair expandedPair = (ExpandedPair) vector.elementAt(i3);
            int value2 = expandedPair.getLeftChar().getValue();
            for (int i4 = 11; i4 >= 0; i4--) {
                if (((1 << i4) & value2) != 0) {
                    bitArray.set(i);
                }
                i++;
            }
            if (expandedPair.getRightChar() != null) {
                int value3 = expandedPair.getRightChar().getValue();
                for (int i5 = 11; i5 >= 0; i5--) {
                    if (((1 << i5) & value3) != 0) {
                        bitArray.set(i);
                    }
                    i++;
                }
            }
        }
        return bitArray;
    }
}
