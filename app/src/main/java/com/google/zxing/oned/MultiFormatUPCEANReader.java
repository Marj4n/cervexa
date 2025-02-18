package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class MultiFormatUPCEANReader extends OneDReader {
    private final Vector readers;

    /* JADX WARN: Removed duplicated region for block: B:12:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MultiFormatUPCEANReader(java.util.Hashtable r3) {
        /*
            r2 = this;
            r2.<init>()
            if (r3 != 0) goto L7
            r3 = 0
            goto Lf
        L7:
            com.google.zxing.DecodeHintType r0 = com.google.zxing.DecodeHintType.POSSIBLE_FORMATS
            java.lang.Object r3 = r3.get(r0)
            java.util.Vector r3 = (java.util.Vector) r3
        Lf:
            java.util.Vector r0 = new java.util.Vector
            r0.<init>()
            r2.readers = r0
            if (r3 == 0) goto L5f
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.EAN_13
            boolean r0 = r3.contains(r0)
            if (r0 == 0) goto L2b
            java.util.Vector r0 = r2.readers
            com.google.zxing.oned.EAN13Reader r1 = new com.google.zxing.oned.EAN13Reader
            r1.<init>()
        L27:
            r0.addElement(r1)
            goto L3b
        L2b:
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.UPC_A
            boolean r0 = r3.contains(r0)
            if (r0 == 0) goto L3b
            java.util.Vector r0 = r2.readers
            com.google.zxing.oned.UPCAReader r1 = new com.google.zxing.oned.UPCAReader
            r1.<init>()
            goto L27
        L3b:
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.EAN_8
            boolean r0 = r3.contains(r0)
            if (r0 == 0) goto L4d
            java.util.Vector r0 = r2.readers
            com.google.zxing.oned.EAN8Reader r1 = new com.google.zxing.oned.EAN8Reader
            r1.<init>()
            r0.addElement(r1)
        L4d:
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.UPC_E
            boolean r3 = r3.contains(r0)
            if (r3 == 0) goto L5f
            java.util.Vector r3 = r2.readers
            com.google.zxing.oned.UPCEReader r0 = new com.google.zxing.oned.UPCEReader
            r0.<init>()
            r3.addElement(r0)
        L5f:
            java.util.Vector r3 = r2.readers
            boolean r3 = r3.isEmpty()
            if (r3 == 0) goto L85
            java.util.Vector r3 = r2.readers
            com.google.zxing.oned.EAN13Reader r0 = new com.google.zxing.oned.EAN13Reader
            r0.<init>()
            r3.addElement(r0)
            java.util.Vector r3 = r2.readers
            com.google.zxing.oned.EAN8Reader r0 = new com.google.zxing.oned.EAN8Reader
            r0.<init>()
            r3.addElement(r0)
            java.util.Vector r3 = r2.readers
            com.google.zxing.oned.UPCEReader r0 = new com.google.zxing.oned.UPCEReader
            r0.<init>()
            r3.addElement(r0)
        L85:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.MultiFormatUPCEANReader.<init>(java.util.Hashtable):void");
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        int[] findStartGuardPattern = UPCEANReader.findStartGuardPattern(bitArray);
        int size = this.readers.size();
        for (int i2 = 0; i2 < size; i2++) {
            try {
                Result decodeRow = ((UPCEANReader) this.readers.elementAt(i2)).decodeRow(i, bitArray, findStartGuardPattern, hashtable);
                boolean z = BarcodeFormat.EAN_13.equals(decodeRow.getBarcodeFormat()) && decodeRow.getText().charAt(0) == '0';
                Vector vector = hashtable == null ? null : (Vector) hashtable.get(DecodeHintType.POSSIBLE_FORMATS);
                return (z && (vector == null || vector.contains(BarcodeFormat.UPC_A))) ? new Result(decodeRow.getText().substring(1), null, decodeRow.getResultPoints(), BarcodeFormat.UPC_A) : decodeRow;
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
