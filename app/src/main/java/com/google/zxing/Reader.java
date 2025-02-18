package com.google.zxing;

import java.util.Hashtable;

/* loaded from: classes.dex */
public interface Reader {
    Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException;

    Result decode(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException, ChecksumException, FormatException;

    void reset();
}
