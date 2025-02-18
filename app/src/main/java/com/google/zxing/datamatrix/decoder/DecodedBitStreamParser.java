package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import kotlin.text.Typography;

/* loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final int ANSIX12_ENCODE = 4;
    private static final int ASCII_ENCODE = 1;
    private static final int BASE256_ENCODE = 6;
    private static final int C40_ENCODE = 2;
    private static final int EDIFACT_ENCODE = 5;
    private static final int PAD_ENCODE = 0;
    private static final int TEXT_ENCODE = 3;
    private static final char[] C40_BASIC_SET_CHARS = {'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] C40_SHIFT2_SET_CHARS = {'!', Typography.quote, '#', Typography.dollar, '%', Typography.amp, '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', Typography.less, '=', Typography.greater, '?', '@', '[', '\\', ']', '^', '_'};
    private static final char[] TEXT_BASIC_SET_CHARS = {'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] TEXT_SHIFT3_SET_CHARS = {'\'', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', 127};

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(byte[] bArr) throws FormatException {
        BitSource bitSource = new BitSource(bArr);
        StringBuffer stringBuffer = new StringBuffer(100);
        StringBuffer stringBuffer2 = new StringBuffer(0);
        Vector vector = new Vector(1);
        int i = 1;
        do {
            if (i == 1) {
                i = decodeAsciiSegment(bitSource, stringBuffer, stringBuffer2);
            } else {
                if (i == 2) {
                    decodeC40Segment(bitSource, stringBuffer);
                } else if (i == 3) {
                    decodeTextSegment(bitSource, stringBuffer);
                } else if (i == 4) {
                    decodeAnsiX12Segment(bitSource, stringBuffer);
                } else if (i == 5) {
                    decodeEdifactSegment(bitSource, stringBuffer);
                } else {
                    if (i != 6) {
                        throw FormatException.getFormatInstance();
                    }
                    decodeBase256Segment(bitSource, stringBuffer, vector);
                }
                i = 1;
            }
            if (i == 0) {
                break;
            }
        } while (bitSource.available() > 0);
        if (stringBuffer2.length() > 0) {
            stringBuffer.append(stringBuffer2.toString());
        }
        String stringBuffer3 = stringBuffer.toString();
        if (vector.isEmpty()) {
            vector = null;
        }
        return new DecoderResult(bArr, stringBuffer3, vector, null);
    }

    private static void decodeAnsiX12Segment(BitSource bitSource, StringBuffer stringBuffer) throws FormatException {
        int readBits;
        int i;
        char c;
        int[] iArr = new int[3];
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i3 == 0) {
                    c = '\r';
                } else if (i3 == 1) {
                    c = '*';
                } else if (i3 == 2) {
                    c = Typography.greater;
                } else if (i3 == 3) {
                    c = ' ';
                } else {
                    if (i3 < 14) {
                        i = i3 + 44;
                    } else {
                        if (i3 >= 40) {
                            throw FormatException.getFormatInstance();
                        }
                        i = i3 + 51;
                    }
                    c = (char) i;
                }
                stringBuffer.append(c);
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static int decodeAsciiSegment(BitSource bitSource, StringBuffer stringBuffer, StringBuffer stringBuffer2) throws FormatException {
        String str;
        boolean z = false;
        do {
            int readBits = bitSource.readBits(8);
            if (readBits == 0) {
                throw FormatException.getFormatInstance();
            }
            if (readBits <= 128) {
                if (z) {
                    readBits += 128;
                }
                stringBuffer.append((char) (readBits - 1));
                return 1;
            }
            if (readBits == 129) {
                return 0;
            }
            if (readBits <= 229) {
                int i = readBits - 130;
                if (i < 10) {
                    stringBuffer.append('0');
                }
                stringBuffer.append(i);
            } else {
                if (readBits == 230) {
                    return 2;
                }
                if (readBits == 231) {
                    return 6;
                }
                if (readBits != 232 && readBits != 233 && readBits != 234) {
                    if (readBits == 235) {
                        z = true;
                    } else {
                        if (readBits == 236) {
                            str = "[)>\u001e05\u001d";
                        } else if (readBits == 237) {
                            str = "[)>\u001e06\u001d";
                        } else {
                            if (readBits == 238) {
                                return 4;
                            }
                            if (readBits == 239) {
                                return 3;
                            }
                            if (readBits == 240) {
                                return 5;
                            }
                            if (readBits != 241 && readBits >= 242 && (readBits != 254 || bitSource.available() != 0)) {
                                throw FormatException.getFormatInstance();
                            }
                        }
                        stringBuffer.append(str);
                        stringBuffer2.insert(0, "\u001e\u0004");
                    }
                }
            }
        } while (bitSource.available() > 0);
        return 1;
    }

    private static void decodeBase256Segment(BitSource bitSource, StringBuffer stringBuffer, Vector vector) throws FormatException {
        int unrandomize255State = unrandomize255State(bitSource.readBits(8), 2);
        int i = 3;
        if (unrandomize255State == 0) {
            unrandomize255State = bitSource.available() / 8;
        } else if (unrandomize255State >= 250) {
            unrandomize255State = ((unrandomize255State - 249) * 250) + unrandomize255State(bitSource.readBits(8), 3);
            i = 4;
        }
        if (unrandomize255State < 0) {
            throw FormatException.getFormatInstance();
        }
        byte[] bArr = new byte[unrandomize255State];
        int i2 = 0;
        while (i2 < unrandomize255State) {
            if (bitSource.available() < 8) {
                throw FormatException.getFormatInstance();
            }
            bArr[i2] = unrandomize255State(bitSource.readBits(8), i);
            i2++;
            i++;
        }
        vector.addElement(bArr);
        try {
            stringBuffer.append(new String(bArr, "ISO8859_1"));
        } catch (UnsupportedEncodingException e) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Platform does not support required encoding: ");
            stringBuffer2.append(e);
            throw new RuntimeException(stringBuffer2.toString());
        }
    }

    private static void decodeC40Segment(BitSource bitSource, StringBuffer stringBuffer) throws FormatException {
        int readBits;
        int i;
        int[] iArr = new int[3];
        boolean z = false;
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            int i2 = 0;
            for (int i3 = 0; i3 < 3; i3++) {
                int i4 = iArr[i3];
                if (i2 != 0) {
                    boolean z2 = true;
                    if (i2 != 1) {
                        if (i2 == 2) {
                            char[] cArr = C40_SHIFT2_SET_CHARS;
                            if (i4 < cArr.length) {
                                char c = cArr[i4];
                                if (z) {
                                    stringBuffer.append((char) (c + 128));
                                    z = false;
                                } else {
                                    stringBuffer.append(c);
                                }
                                z2 = z;
                            } else {
                                if (i4 == 27) {
                                    throw FormatException.getFormatInstance();
                                }
                                if (i4 != 30) {
                                    throw FormatException.getFormatInstance();
                                }
                            }
                            z = z2;
                        } else {
                            if (i2 != 3) {
                                throw FormatException.getFormatInstance();
                            }
                            if (z) {
                                i = i4 + 224;
                                stringBuffer.append((char) i);
                                z = false;
                            } else {
                                stringBuffer.append((char) (i4 + 96));
                            }
                        }
                        i2 = 0;
                    } else if (z) {
                        i = i4 + 128;
                        stringBuffer.append((char) i);
                        z = false;
                        i2 = 0;
                    } else {
                        stringBuffer.append(i4);
                        i2 = 0;
                    }
                } else if (i4 < 3) {
                    i2 = i4 + 1;
                } else {
                    char[] cArr2 = C40_BASIC_SET_CHARS;
                    if (i4 >= cArr2.length) {
                        throw FormatException.getFormatInstance();
                    }
                    char c2 = cArr2[i4];
                    if (z) {
                        stringBuffer.append((char) (c2 + 128));
                        z = false;
                    } else {
                        stringBuffer.append(c2);
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeEdifactSegment(BitSource bitSource, StringBuffer stringBuffer) {
        boolean z = false;
        while (bitSource.available() > 16) {
            for (int i = 0; i < 4; i++) {
                int readBits = bitSource.readBits(6);
                if (readBits == 31) {
                    z = true;
                }
                if (!z) {
                    if ((readBits & 32) == 0) {
                        readBits |= 64;
                    }
                    stringBuffer.append(readBits);
                }
            }
            if (z || bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeTextSegment(BitSource bitSource, StringBuffer stringBuffer) throws FormatException {
        int readBits;
        char c;
        int[] iArr = new int[3];
        boolean z = false;
        int i = 0;
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i != 0) {
                    boolean z2 = true;
                    if (i != 1) {
                        if (i == 2) {
                            char[] cArr = C40_SHIFT2_SET_CHARS;
                            if (i3 < cArr.length) {
                                char c2 = cArr[i3];
                                if (z) {
                                    stringBuffer.append((char) (c2 + 128));
                                    z = false;
                                } else {
                                    stringBuffer.append(c2);
                                }
                                z2 = z;
                            } else {
                                if (i3 == 27) {
                                    throw FormatException.getFormatInstance();
                                }
                                if (i3 != 30) {
                                    throw FormatException.getFormatInstance();
                                }
                            }
                            z = z2;
                        } else {
                            if (i != 3) {
                                throw FormatException.getFormatInstance();
                            }
                            char[] cArr2 = TEXT_SHIFT3_SET_CHARS;
                            if (i3 >= cArr2.length) {
                                throw FormatException.getFormatInstance();
                            }
                            char c3 = cArr2[i3];
                            if (z) {
                                c = (char) (c3 + 128);
                                stringBuffer.append(c);
                                z = false;
                            } else {
                                stringBuffer.append(c3);
                            }
                        }
                        i = 0;
                    } else if (z) {
                        c = (char) (i3 + 128);
                        stringBuffer.append(c);
                        z = false;
                        i = 0;
                    } else {
                        stringBuffer.append(i3);
                        i = 0;
                    }
                } else if (i3 < 3) {
                    i = i3 + 1;
                } else {
                    char[] cArr3 = TEXT_BASIC_SET_CHARS;
                    if (i3 >= cArr3.length) {
                        throw FormatException.getFormatInstance();
                    }
                    char c4 = cArr3[i3];
                    if (z) {
                        stringBuffer.append((char) (c4 + 128));
                        z = false;
                    } else {
                        stringBuffer.append(c4);
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void parseTwoBytes(int i, int i2, int[] iArr) {
        int i3 = ((i << 8) + i2) - 1;
        int i4 = i3 / 1600;
        iArr[0] = i4;
        int i5 = i3 - (i4 * 1600);
        int i6 = i5 / 40;
        iArr[1] = i6;
        iArr[2] = i5 - (i6 * 40);
    }

    private static byte unrandomize255State(int i, int i2) {
        int i3 = i - (((i2 * 149) % 255) + 1);
        if (i3 < 0) {
            i3 += 256;
        }
        return (byte) i3;
    }
}
