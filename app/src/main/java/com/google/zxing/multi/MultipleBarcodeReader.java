package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import java.util.Hashtable;

/* loaded from: classes.dex */
public interface MultipleBarcodeReader {
    Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException;

    Result[] decodeMultiple(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException;
}
