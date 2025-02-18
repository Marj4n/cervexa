package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

/* loaded from: classes.dex */
final class AI01AndOtherAIs extends AI01decoder {
    private static final int HEADER_SIZE = 4;

    AI01AndOtherAIs(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(01)");
        int length = stringBuffer.length();
        stringBuffer.append(this.generalDecoder.extractNumericValueFromBitArray(4, 4));
        encodeCompressedGtinWithoutAI(stringBuffer, 8, length);
        return this.generalDecoder.decodeAllCodes(stringBuffer, 48);
    }
}
