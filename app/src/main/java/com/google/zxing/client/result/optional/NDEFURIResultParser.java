package com.google.zxing.client.result.optional;

import com.google.zxing.Result;
import com.google.zxing.client.result.URIParsedResult;
import kotlin.UByte;

/* loaded from: classes.dex */
final class NDEFURIResultParser extends AbstractNDEFResultParser {
    private static final String[] URI_PREFIXES = {null, "http://www.", "https://www.", "http://", "https://", "tel:", "mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://", "dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://", "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:", "urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:"};

    NDEFURIResultParser() {
    }

    static String decodeURIPayload(byte[] bArr) {
        int i = bArr[0] & UByte.MAX_VALUE;
        String[] strArr = URI_PREFIXES;
        String str = i < strArr.length ? strArr[i] : null;
        String bytesToString = bytesToString(bArr, 1, bArr.length - 1, "UTF8");
        if (str == null) {
            return bytesToString;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(bytesToString);
        return stringBuffer.toString();
    }

    public static URIParsedResult parse(Result result) {
        NDEFRecord readRecord;
        byte[] rawBytes = result.getRawBytes();
        if (rawBytes != null && (readRecord = NDEFRecord.readRecord(rawBytes, 0)) != null && readRecord.isMessageBegin() && readRecord.isMessageEnd() && readRecord.getType().equals(NDEFRecord.URI_WELL_KNOWN_TYPE)) {
            return new URIParsedResult(decodeURIPayload(readRecord.getPayload()), null);
        }
        return null;
    }
}
