package com.google.zxing.oned.rss.expanded.decoders;

/* loaded from: classes.dex */
abstract class DecodedObject {
    protected final int newPosition;

    DecodedObject(int i) {
        this.newPosition = i;
    }

    int getNewPosition() {
        return this.newPosition;
    }
}
