package com.google.zxing.common;

import java.util.Vector;

/* loaded from: classes.dex */
public final class Collections {
    private Collections() {
    }

    public static void insertionSort(Vector vector, Comparator comparator) {
        int size = vector.size();
        for (int i = 1; i < size; i++) {
            Object elementAt = vector.elementAt(i);
            int i2 = i - 1;
            while (i2 >= 0) {
                Object elementAt2 = vector.elementAt(i2);
                if (comparator.compare(elementAt2, elementAt) > 0) {
                    vector.setElementAt(elementAt2, i2 + 1);
                    i2--;
                }
            }
            vector.setElementAt(elementAt, i2 + 1);
        }
    }
}
