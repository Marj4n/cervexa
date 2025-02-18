package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

/* loaded from: classes.dex */
final class AI01392xDecoder extends AI01decoder {
    private static final int headerSize = 8;
    private static final int lastDigitSize = 2;

    AI01392xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        if (this.information.size < 48) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuffer stringBuffer = new StringBuffer();
        encodeCompressedGtin(stringBuffer, 8);
        int extractNumericValueFromBitArray = this.generalDecoder.extractNumericValueFromBitArray(48, 2);
        stringBuffer.append("(392");
        stringBuffer.append(extractNumericValueFromBitArray);
        stringBuffer.append(')');
        stringBuffer.append(this.generalDecoder.decodeGeneralPurposeField(50, null).getNewString());
        return stringBuffer.toString();
    }
}
