package com.google.zxing;

import com.google.zxing.aztec.AztecReader;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class MultiFormatReader implements Reader {
    private Hashtable hints;
    private Vector readers;

    private Result decodeInternal(BinaryBitmap binaryBitmap) throws NotFoundException {
        int size = this.readers.size();
        for (int i = 0; i < size; i++) {
            try {
                return ((Reader) this.readers.elementAt(i)).decode(binaryBitmap, this.hints);
            } catch (ReaderException unused) {
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException {
        setHints(null);
        return decodeInternal(binaryBitmap);
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException {
        setHints(hashtable);
        return decodeInternal(binaryBitmap);
    }

    public Result decodeWithState(BinaryBitmap binaryBitmap) throws NotFoundException {
        if (this.readers == null) {
            setHints(null);
        }
        return decodeInternal(binaryBitmap);
    }

    @Override // com.google.zxing.Reader
    public void reset() {
        int size = this.readers.size();
        for (int i = 0; i < size; i++) {
            ((Reader) this.readers.elementAt(i)).reset();
        }
    }

    public void setHints(Hashtable hashtable) {
        this.hints = hashtable;
        boolean z = true;
        boolean z2 = hashtable != null && hashtable.containsKey(DecodeHintType.TRY_HARDER);
        Vector vector = hashtable == null ? null : (Vector) hashtable.get(DecodeHintType.POSSIBLE_FORMATS);
        this.readers = new Vector();
        if (vector != null) {
            if (!vector.contains(BarcodeFormat.UPC_A) && !vector.contains(BarcodeFormat.UPC_E) && !vector.contains(BarcodeFormat.EAN_13) && !vector.contains(BarcodeFormat.EAN_8) && !vector.contains(BarcodeFormat.CODE_39) && !vector.contains(BarcodeFormat.CODE_93) && !vector.contains(BarcodeFormat.CODE_128) && !vector.contains(BarcodeFormat.ITF) && !vector.contains(BarcodeFormat.RSS_14) && !vector.contains(BarcodeFormat.RSS_EXPANDED)) {
                z = false;
            }
            if (z && !z2) {
                this.readers.addElement(new MultiFormatOneDReader(hashtable));
            }
            if (vector.contains(BarcodeFormat.QR_CODE)) {
                this.readers.addElement(new QRCodeReader());
            }
            if (vector.contains(BarcodeFormat.DATA_MATRIX)) {
                this.readers.addElement(new DataMatrixReader());
            }
            if (vector.contains(BarcodeFormat.AZTEC)) {
                this.readers.addElement(new AztecReader());
            }
            if (vector.contains(BarcodeFormat.PDF_417)) {
                this.readers.addElement(new PDF417Reader());
            }
            if (z && z2) {
                this.readers.addElement(new MultiFormatOneDReader(hashtable));
            }
        }
        if (this.readers.isEmpty()) {
            if (!z2) {
                this.readers.addElement(new MultiFormatOneDReader(hashtable));
            }
            this.readers.addElement(new QRCodeReader());
            this.readers.addElement(new DataMatrixReader());
            this.readers.addElement(new AztecReader());
            this.readers.addElement(new PDF417Reader());
            if (z2) {
                this.readers.addElement(new MultiFormatOneDReader(hashtable));
            }
        }
    }
}
