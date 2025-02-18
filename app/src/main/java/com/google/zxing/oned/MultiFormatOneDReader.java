package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class MultiFormatOneDReader extends OneDReader {
    private final Vector readers;

    public MultiFormatOneDReader(Hashtable hashtable) {
        Vector vector = hashtable == null ? null : (Vector) hashtable.get(DecodeHintType.POSSIBLE_FORMATS);
        boolean z = (hashtable == null || hashtable.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) == null) ? false : true;
        this.readers = new Vector();
        if (vector != null) {
            if (vector.contains(BarcodeFormat.EAN_13) || vector.contains(BarcodeFormat.UPC_A) || vector.contains(BarcodeFormat.EAN_8) || vector.contains(BarcodeFormat.UPC_E)) {
                this.readers.addElement(new MultiFormatUPCEANReader(hashtable));
            }
            if (vector.contains(BarcodeFormat.CODE_39)) {
                this.readers.addElement(new Code39Reader(z));
            }
            if (vector.contains(BarcodeFormat.CODE_93)) {
                this.readers.addElement(new Code93Reader());
            }
            if (vector.contains(BarcodeFormat.CODE_128)) {
                this.readers.addElement(new Code128Reader());
            }
            if (vector.contains(BarcodeFormat.ITF)) {
                this.readers.addElement(new ITFReader());
            }
            if (vector.contains(BarcodeFormat.CODABAR)) {
                this.readers.addElement(new CodaBarReader());
            }
            if (vector.contains(BarcodeFormat.RSS_14)) {
                this.readers.addElement(new RSS14Reader());
            }
            if (vector.contains(BarcodeFormat.RSS_EXPANDED)) {
                this.readers.addElement(new RSSExpandedReader());
            }
        }
        if (this.readers.isEmpty()) {
            this.readers.addElement(new MultiFormatUPCEANReader(hashtable));
            this.readers.addElement(new Code39Reader());
            this.readers.addElement(new Code93Reader());
            this.readers.addElement(new Code128Reader());
            this.readers.addElement(new ITFReader());
            this.readers.addElement(new RSS14Reader());
            this.readers.addElement(new RSSExpandedReader());
        }
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        for (int i2 = 0; i2 < this.readers.size(); i2++) {
            try {
                return ((OneDReader) this.readers.elementAt(i2)).decodeRow(i, bitArray, hashtable);
            } catch (ReaderException unused) {
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    @Override // com.google.zxing.oned.OneDReader, com.google.zxing.Reader
    public void reset() {
        int size = this.readers.size();
        for (int i = 0; i < size; i++) {
            ((Reader) this.readers.elementAt(i)).reset();
        }
    }
}
