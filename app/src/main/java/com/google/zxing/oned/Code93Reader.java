package com.google.zxing.oned;

import com.baidu.mapapi.UIMsg;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Hashtable;
import okhttp3.internal.http.StatusLine;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.smtp.SMTPReply;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
public final class Code93Reader extends OneDReader {
    private static final int ASTERISK_ENCODING;
    private static final int[] CHARACTER_ENCODINGS;
    private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();

    static {
        int[] iArr = {276, 328, 324, 322, 296, 292, 290, 336, 274, 266, 424, NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 418, UIMsg.l_ErrorNo.NETWORK_ERROR_404, 402, 394, 360, 356, SMTPReply.START_MAIL_INPUT, StatusLine.HTTP_PERM_REDIRECT, 282, 344, FTPReply.NEED_ACCOUNT, 326, IjkMediaCodecInfo.RANK_SECURE, 278, NNTPReply.TRANSFER_FAILED, 434, 428, NNTPReply.NO_PREVIOUS_ARTICLE, 406, 410, 364, 358, 310, 314, 302, 468, 466, 458, 366, 374, NNTPReply.NO_SUCH_ARTICLE_FOUND, 294, 474, 470, 306, FTPReply.FILE_ACTION_PENDING};
        CHARACTER_ENCODINGS = iArr;
        ASTERISK_ENCODING = iArr[47];
    }

    private static void checkChecksums(StringBuffer stringBuffer) throws ChecksumException {
        int length = stringBuffer.length();
        checkOneChecksum(stringBuffer, length - 2, 20);
        checkOneChecksum(stringBuffer, length - 1, 15);
    }

    private static void checkOneChecksum(StringBuffer stringBuffer, int i, int i2) throws ChecksumException {
        int i3 = 0;
        int i4 = 1;
        for (int i5 = i - 1; i5 >= 0; i5--) {
            i3 += ALPHABET_STRING.indexOf(stringBuffer.charAt(i5)) * i4;
            i4++;
            if (i4 > i2) {
                i4 = 1;
            }
        }
        if (stringBuffer.charAt(i) != ALPHABET[i3 % 47]) {
            throw ChecksumException.getChecksumInstance();
        }
    }

    private static String decodeExtended(StringBuffer stringBuffer) throws FormatException {
        int i;
        int length = stringBuffer.length();
        StringBuffer stringBuffer2 = new StringBuffer(length);
        int i2 = 0;
        while (i2 < length) {
            char charAt = stringBuffer.charAt(i2);
            if (charAt >= 'a' && charAt <= 'd') {
                i2++;
                char charAt2 = stringBuffer.charAt(i2);
                switch (charAt) {
                    case 'a':
                        if (charAt2 >= 'A' && charAt2 <= 'Z') {
                            i = charAt2 - '@';
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case 'b':
                        if (charAt2 >= 'A' && charAt2 <= 'E') {
                            i = charAt2 - '&';
                            break;
                        } else if (charAt2 >= 'F' && charAt2 <= 'W') {
                            i = charAt2 - 11;
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case 'c':
                        if (charAt2 >= 'A' && charAt2 <= 'O') {
                            i = charAt2 - ' ';
                            break;
                        } else {
                            if (charAt2 != 'Z') {
                                throw FormatException.getFormatInstance();
                            }
                            charAt = ':';
                            break;
                        }
                        break;
                    case 'd':
                        if (charAt2 >= 'A' && charAt2 <= 'Z') {
                            i = charAt2 + ' ';
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    default:
                        charAt = 0;
                        continue;
                }
                charAt = (char) i;
            }
            stringBuffer2.append(charAt);
            i2++;
        }
        return stringBuffer2.toString();
    }

    private static int[] findAsteriskPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int i = 0;
        while (i < size && !bitArray.get(i)) {
            i++;
        }
        int[] iArr = new int[6];
        int i2 = i;
        boolean z = false;
        int i3 = 0;
        while (i < size) {
            if (bitArray.get(i) ^ z) {
                iArr[i3] = iArr[i3] + 1;
            } else {
                if (i3 == 5) {
                    if (toPattern(iArr) == ASTERISK_ENCODING) {
                        return new int[]{i2, i};
                    }
                    i2 += iArr[0] + iArr[1];
                    for (int i4 = 2; i4 < 6; i4++) {
                        iArr[i4 - 2] = iArr[i4];
                    }
                    iArr[4] = 0;
                    iArr[5] = 0;
                    i3--;
                } else {
                    i3++;
                }
                iArr[i3] = 1;
                z = !z;
            }
            i++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static char patternToChar(int i) throws NotFoundException {
        int i2 = 0;
        while (true) {
            int[] iArr = CHARACTER_ENCODINGS;
            if (i2 >= iArr.length) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (iArr[i2] == i) {
                return ALPHABET[i2];
            }
            i2++;
        }
    }

    private static int toPattern(int[] iArr) {
        int length = iArr.length;
        int i = 0;
        for (int i2 : iArr) {
            i += i2;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            int i5 = ((iArr[i4] << 8) * 9) / i;
            int i6 = i5 >> 8;
            if ((i5 & 255) > 127) {
                i6++;
            }
            if (i6 < 1 || i6 > 4) {
                return -1;
            }
            if ((i4 & 1) == 0) {
                for (int i7 = 0; i7 < i6; i7++) {
                    i3 = (i3 << 1) | 1;
                }
            } else {
                i3 <<= i6;
            }
        }
        return i3;
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException, ChecksumException, FormatException {
        int i2 = findAsteriskPattern(bitArray)[1];
        int size = bitArray.getSize();
        while (i2 < size && !bitArray.get(i2)) {
            i2++;
        }
        StringBuffer stringBuffer = new StringBuffer(20);
        int[] iArr = new int[6];
        while (true) {
            recordPattern(bitArray, i2, iArr);
            int pattern = toPattern(iArr);
            if (pattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            char patternToChar = patternToChar(pattern);
            stringBuffer.append(patternToChar);
            int i3 = i2;
            for (int i4 = 0; i4 < 6; i4++) {
                i3 += iArr[i4];
            }
            while (i3 < size && !bitArray.get(i3)) {
                i3++;
            }
            if (patternToChar == '*') {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                if (i3 == size || !bitArray.get(i3)) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (stringBuffer.length() < 2) {
                    throw NotFoundException.getNotFoundInstance();
                }
                checkChecksums(stringBuffer);
                stringBuffer.setLength(stringBuffer.length() - 2);
                float f = i;
                return new Result(decodeExtended(stringBuffer), null, new ResultPoint[]{new ResultPoint((r14[1] + r14[0]) / 2.0f, f), new ResultPoint((i3 + i2) / 2.0f, f)}, BarcodeFormat.CODE_93);
            }
            i2 = i3;
        }
    }
}
