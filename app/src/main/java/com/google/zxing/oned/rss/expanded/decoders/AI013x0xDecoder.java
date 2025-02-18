package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

/* loaded from: classes.dex */
abstract class AI013x0xDecoder extends AI01weightDecoder {
    private static final int headerSize = 5;
    private static final int weightSize = 15;

    AI013x0xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        if (this.information.size != 60) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuffer stringBuffer = new StringBuffer();
        encodeCompressedGtin(stringBuffer, 5);
        encodeCompressedWeight(stringBuffer, 45, 15);
        return stringBuffer.toString();
    }
}
