package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.StringUtils;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import kotlin.text.Typography;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final char[] ALPHANUMERIC_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', Typography.dollar, '%', '*', '+', '-', '.', '/', ':'};
    private static final int GB2312_SUBSET = 1;

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(byte[] bArr, Version version, ErrorCorrectionLevel errorCorrectionLevel, Hashtable hashtable) throws FormatException {
        Mode forBits;
        Mode mode;
        BitSource bitSource = new BitSource(bArr);
        StringBuffer stringBuffer = new StringBuffer(50);
        Vector vector = new Vector(1);
        CharacterSetECI characterSetECI = null;
        boolean z = false;
        do {
            if (bitSource.available() < 4) {
                forBits = Mode.TERMINATOR;
            } else {
                try {
                    forBits = Mode.forBits(bitSource.readBits(4));
                } catch (IllegalArgumentException unused) {
                    throw FormatException.getFormatInstance();
                }
            }
            mode = forBits;
            if (!mode.equals(Mode.TERMINATOR)) {
                if (mode.equals(Mode.FNC1_FIRST_POSITION) || mode.equals(Mode.FNC1_SECOND_POSITION)) {
                    z = true;
                } else if (mode.equals(Mode.STRUCTURED_APPEND)) {
                    bitSource.readBits(16);
                } else if (mode.equals(Mode.ECI)) {
                    characterSetECI = CharacterSetECI.getCharacterSetECIByValue(parseECIValue(bitSource));
                    if (characterSetECI == null) {
                        throw FormatException.getFormatInstance();
                    }
                } else if (mode.equals(Mode.HANZI)) {
                    int readBits = bitSource.readBits(4);
                    int readBits2 = bitSource.readBits(mode.getCharacterCountBits(version));
                    if (readBits == 1) {
                        decodeHanziSegment(bitSource, stringBuffer, readBits2);
                    }
                } else {
                    int readBits3 = bitSource.readBits(mode.getCharacterCountBits(version));
                    if (mode.equals(Mode.NUMERIC)) {
                        decodeNumericSegment(bitSource, stringBuffer, readBits3);
                    } else if (mode.equals(Mode.ALPHANUMERIC)) {
                        decodeAlphanumericSegment(bitSource, stringBuffer, readBits3, z);
                    } else if (mode.equals(Mode.BYTE)) {
                        decodeByteSegment(bitSource, stringBuffer, readBits3, characterSetECI, vector, hashtable);
                    } else {
                        if (!mode.equals(Mode.KANJI)) {
                            throw FormatException.getFormatInstance();
                        }
                        decodeKanjiSegment(bitSource, stringBuffer, readBits3);
                    }
                }
            }
        } while (!mode.equals(Mode.TERMINATOR));
        String stringBuffer2 = stringBuffer.toString();
        if (vector.isEmpty()) {
            vector = null;
        }
        return new DecoderResult(bArr, stringBuffer2, vector, errorCorrectionLevel != null ? errorCorrectionLevel.toString() : null);
    }

    private static void decodeAlphanumericSegment(BitSource bitSource, StringBuffer stringBuffer, int i, boolean z) throws FormatException {
        while (i > 1) {
            int readBits = bitSource.readBits(11);
            stringBuffer.append(toAlphaNumericChar(readBits / 45));
            stringBuffer.append(toAlphaNumericChar(readBits % 45));
            i -= 2;
        }
        if (i == 1) {
            stringBuffer.append(toAlphaNumericChar(bitSource.readBits(6)));
        }
        if (z) {
            for (int length = stringBuffer.length(); length < stringBuffer.length(); length++) {
                if (stringBuffer.charAt(length) == '%') {
                    if (length < stringBuffer.length() - 1) {
                        int i2 = length + 1;
                        if (stringBuffer.charAt(i2) == '%') {
                            stringBuffer.deleteCharAt(i2);
                        }
                    }
                    stringBuffer.setCharAt(length, (char) 29);
                }
            }
        }
    }

    private static void decodeByteSegment(BitSource bitSource, StringBuffer stringBuffer, int i, CharacterSetECI characterSetECI, Vector vector, Hashtable hashtable) throws FormatException {
        if ((i << 3) > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) bitSource.readBits(8);
        }
        try {
            stringBuffer.append(new String(bArr, characterSetECI == null ? StringUtils.guessEncoding(bArr, hashtable) : characterSetECI.getEncodingName()));
            vector.addElement(bArr);
        } catch (UnsupportedEncodingException unused) {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeHanziSegment(BitSource bitSource, StringBuffer stringBuffer, int i) throws FormatException {
        if (i * 13 > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        byte[] bArr = new byte[i * 2];
        int i2 = 0;
        while (i > 0) {
            int readBits = bitSource.readBits(13);
            int i3 = (readBits % 96) | ((readBits / 96) << 8);
            int i4 = i3 + (i3 < 959 ? IConstant.FRAME_TYPE_I : 42657);
            bArr[i2] = (byte) ((i4 >> 8) & 255);
            bArr[i2 + 1] = (byte) (i4 & 255);
            i2 += 2;
            i--;
        }
        try {
            stringBuffer.append(new String(bArr, StringUtils.GB2312));
        } catch (UnsupportedEncodingException unused) {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeKanjiSegment(BitSource bitSource, StringBuffer stringBuffer, int i) throws FormatException {
        if (i * 13 > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        byte[] bArr = new byte[i * 2];
        int i2 = 0;
        while (i > 0) {
            int readBits = bitSource.readBits(13);
            int i3 = (readBits % 192) | ((readBits / 192) << 8);
            int i4 = i3 + (i3 < 7936 ? 33088 : 49472);
            bArr[i2] = (byte) (i4 >> 8);
            bArr[i2 + 1] = (byte) i4;
            i2 += 2;
            i--;
        }
        try {
            stringBuffer.append(new String(bArr, StringUtils.SHIFT_JIS));
        } catch (UnsupportedEncodingException unused) {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeNumericSegment(BitSource bitSource, StringBuffer stringBuffer, int i) throws FormatException {
        int readBits;
        while (i >= 3) {
            int readBits2 = bitSource.readBits(10);
            if (readBits2 >= 1000) {
                throw FormatException.getFormatInstance();
            }
            stringBuffer.append(toAlphaNumericChar(readBits2 / 100));
            stringBuffer.append(toAlphaNumericChar((readBits2 / 10) % 10));
            stringBuffer.append(toAlphaNumericChar(readBits2 % 10));
            i -= 3;
        }
        if (i == 2) {
            int readBits3 = bitSource.readBits(7);
            if (readBits3 >= 100) {
                throw FormatException.getFormatInstance();
            }
            stringBuffer.append(toAlphaNumericChar(readBits3 / 10));
            readBits = readBits3 % 10;
        } else {
            if (i != 1) {
                return;
            }
            readBits = bitSource.readBits(4);
            if (readBits >= 10) {
                throw FormatException.getFormatInstance();
            }
        }
        stringBuffer.append(toAlphaNumericChar(readBits));
    }

    private static int parseECIValue(BitSource bitSource) {
        int readBits = bitSource.readBits(8);
        if ((readBits & 128) == 0) {
            return readBits & WorkQueueKt.MASK;
        }
        if ((readBits & 192) == 128) {
            return bitSource.readBits(8) | ((readBits & 63) << 8);
        }
        if ((readBits & 224) == 192) {
            return bitSource.readBits(16) | ((readBits & 31) << 16);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bad ECI bits starting with byte ");
        stringBuffer.append(readBits);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    private static char toAlphaNumericChar(int i) throws FormatException {
        char[] cArr = ALPHANUMERIC_CHARS;
        if (i < cArr.length) {
            return cArr[i];
        }
        throw FormatException.getFormatInstance();
    }
}
