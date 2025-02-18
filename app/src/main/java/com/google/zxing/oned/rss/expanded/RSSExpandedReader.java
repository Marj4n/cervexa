package com.google.zxing.oned.rss.expanded;

import com.gizthon.camera.BuildConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.RSSUtils;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.Hashtable;
import java.util.Vector;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.imap.IMAP;
import org.apache.commons.net.nntp.NNTPReply;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public final class RSSExpandedReader extends AbstractRSSReader {
    private static final int[][] FINDER_PATTERN_SEQUENCES;
    private static final int FINDER_PAT_A = 0;
    private static final int FINDER_PAT_B = 1;
    private static final int FINDER_PAT_C = 2;
    private static final int FINDER_PAT_D = 3;
    private static final int FINDER_PAT_E = 4;
    private static final int FINDER_PAT_F = 5;
    private static final int LONGEST_SEQUENCE_SIZE;
    private static final int MAX_PAIRS = 11;
    private static final int[] SYMBOL_WIDEST = {7, 5, 4, 3, 1};
    private static final int[] EVEN_TOTAL_SUBSET = {4, 20, 52, 104, 204};
    private static final int[] GSUM = {0, 348, 1388, 2948, 3988};
    private static final int[][] FINDER_PATTERNS = {new int[]{1, 8, 4, 1}, new int[]{3, 6, 4, 1}, new int[]{3, 4, 6, 1}, new int[]{3, 2, 8, 1}, new int[]{2, 6, 5, 1}, new int[]{2, 2, 9, 1}};
    private static final int[][] WEIGHTS = {new int[]{1, 3, 9, 27, 81, 32, 96, 77}, new int[]{20, 60, 180, 118, IMAP.DEFAULT_PORT, 7, 21, 63}, new int[]{189, 145, 13, 39, 117, 140, 209, NNTPReply.CLOSING_CONNECTION}, new int[]{193, 157, 49, 147, 19, 57, 171, 91}, new int[]{62, 186, 136, 197, 169, 85, 44, 132}, new int[]{185, 133, 188, 142, 4, 12, 36, 108}, new int[]{113, 128, 173, 97, 80, 29, 87, 50}, new int[]{FTPReply.FILE_STATUS_OK, 28, 84, 41, 123, 158, 52, 156}, new int[]{46, 138, 203, 187, 139, 206, 196, IConstant.OP_CANCEL_SELECT_ALL}, new int[]{76, 17, 51, 153, 37, 111, 122, 155}, new int[]{43, BuildConfig.VERSION_CODE, 176, 106, 107, 110, 119, 146}, new int[]{16, 48, IjkMediaMeta.FF_PROFILE_H264_HIGH_444, 10, 30, 90, 59, 177}, new int[]{109, 116, 137, 200, 178, 112, FTPReply.DATA_CONNECTION_ALREADY_OPEN, IConstant.OP_DELETE_FILES}, new int[]{70, 210, 208, 202, 184, 130, 179, 115}, new int[]{134, 191, 151, 31, 93, 68, 204, 190}, new int[]{148, 22, 66, 198, 172, 94, 71, 2}, new int[]{6, 18, 54, IConstant.OP_EXIT_EDIT_MODE, 64, 192, 154, 40}, new int[]{120, 149, 25, 75, 14, 42, 126, IConstant.OP_SHARE_FILES}, new int[]{79, 26, 78, 23, 69, 207, NNTPReply.DEBUG_OUTPUT, 175}, new int[]{103, 98, 83, 38, 114, 131, 182, 124}, new int[]{IConstant.OP_ENTER_EDIT_MODE, 61, 183, WorkQueueKt.MASK, 170, 88, 53, 159}, new int[]{55, IConstant.OP_SELECT_ALL, 73, 8, 24, 72, 5, 15}, new int[]{45, 135, 194, 160, 58, 174, 100, 89}};
    private final Vector pairs = new Vector(11);
    private final int[] startEnd = new int[2];
    private final int[] currentSequence = new int[LONGEST_SEQUENCE_SIZE];

    static {
        int[][] iArr = {new int[]{0, 0}, new int[]{0, 1, 1}, new int[]{0, 2, 1, 3}, new int[]{0, 4, 1, 3, 2}, new int[]{0, 4, 1, 3, 3, 5}, new int[]{0, 4, 1, 3, 4, 5, 5}, new int[]{0, 0, 1, 1, 2, 2, 3, 3}, new int[]{0, 0, 1, 1, 2, 2, 3, 4, 4}, new int[]{0, 0, 1, 1, 2, 2, 3, 4, 5, 5}, new int[]{0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5}};
        FINDER_PATTERN_SEQUENCES = iArr;
        LONGEST_SEQUENCE_SIZE = iArr[iArr.length - 1].length;
    }

    private void adjustOddEvenCounts(int i) throws NotFoundException {
        boolean z;
        boolean z2;
        boolean z3;
        int count = count(this.oddCounts);
        int count2 = count(this.evenCounts);
        int i2 = (count + count2) - i;
        boolean z4 = true;
        boolean z5 = (count & 1) == 1;
        boolean z6 = (count2 & 1) == 0;
        if (count > 13) {
            z = false;
            z2 = true;
        } else {
            z = count < 4;
            z2 = false;
        }
        if (count2 > 13) {
            z3 = true;
        } else {
            r3 = count2 < 4;
            z3 = false;
        }
        if (i2 == 1) {
            if (z5) {
                if (z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                z4 = z;
                z2 = true;
            } else {
                if (!z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                z4 = z;
                z3 = true;
            }
        } else if (i2 == -1) {
            if (z5) {
                if (z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
            } else {
                if (!z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                z4 = z;
                r3 = true;
            }
        } else {
            if (i2 != 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (z5) {
                if (!z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (count >= count2) {
                    z4 = z;
                    r3 = true;
                    z2 = true;
                }
                z3 = true;
            } else {
                if (z6) {
                    throw NotFoundException.getNotFoundInstance();
                }
                z4 = z;
            }
        }
        if (z4) {
            if (z2) {
                throw NotFoundException.getNotFoundInstance();
            }
            increment(this.oddCounts, this.oddRoundingErrors);
        }
        if (z2) {
            decrement(this.oddCounts, this.oddRoundingErrors);
        }
        if (r3) {
            if (z3) {
                throw NotFoundException.getNotFoundInstance();
            }
            increment(this.evenCounts, this.oddRoundingErrors);
        }
        if (z3) {
            decrement(this.evenCounts, this.evenRoundingErrors);
        }
    }

    private boolean checkChecksum() {
        ExpandedPair expandedPair = (ExpandedPair) this.pairs.elementAt(0);
        DataCharacter leftChar = expandedPair.getLeftChar();
        int checksumPortion = expandedPair.getRightChar().getChecksumPortion();
        int i = 2;
        for (int i2 = 1; i2 < this.pairs.size(); i2++) {
            ExpandedPair expandedPair2 = (ExpandedPair) this.pairs.elementAt(i2);
            checksumPortion += expandedPair2.getLeftChar().getChecksumPortion();
            i++;
            if (expandedPair2.getRightChar() != null) {
                checksumPortion += expandedPair2.getRightChar().getChecksumPortion();
                i++;
            }
        }
        return ((i + (-4)) * 211) + (checksumPortion % 211) == leftChar.getValue();
    }

    private boolean checkPairSequence(Vector vector, FinderPattern finderPattern) throws NotFoundException {
        boolean z;
        int size = vector.size() + 1;
        if (size > this.currentSequence.length) {
            throw NotFoundException.getNotFoundInstance();
        }
        for (int i = 0; i < vector.size(); i++) {
            this.currentSequence[i] = ((ExpandedPair) vector.elementAt(i)).getFinderPattern().getValue();
        }
        this.currentSequence[size - 1] = finderPattern.getValue();
        int i2 = 0;
        while (true) {
            int[][] iArr = FINDER_PATTERN_SEQUENCES;
            if (i2 >= iArr.length) {
                throw NotFoundException.getNotFoundInstance();
            }
            int[] iArr2 = iArr[i2];
            if (iArr2.length >= size) {
                int i3 = 0;
                while (true) {
                    if (i3 >= size) {
                        z = true;
                        break;
                    }
                    if (this.currentSequence[i3] != iArr2[i3]) {
                        z = false;
                        break;
                    }
                    i3++;
                }
                if (z) {
                    return size == iArr2.length;
                }
            }
            i2++;
        }
    }

    private static Result constructResult(Vector vector) throws NotFoundException {
        String parseInformation = AbstractExpandedDecoder.createDecoder(BitArrayBuilder.buildBitArray(vector)).parseInformation();
        ResultPoint[] resultPoints = ((ExpandedPair) vector.elementAt(0)).getFinderPattern().getResultPoints();
        ResultPoint[] resultPoints2 = ((ExpandedPair) vector.lastElement()).getFinderPattern().getResultPoints();
        return new Result(parseInformation, null, new ResultPoint[]{resultPoints[0], resultPoints[1], resultPoints2[0], resultPoints2[1]}, BarcodeFormat.RSS_EXPANDED);
    }

    private void findNextPair(BitArray bitArray, Vector vector, int i) throws NotFoundException {
        int[] iArr = this.decodeFinderCounters;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        int size = bitArray.getSize();
        if (i < 0) {
            i = vector.isEmpty() ? 0 : ((ExpandedPair) vector.lastElement()).getFinderPattern().getStartEnd()[1];
        }
        boolean z = vector.size() % 2 != 0;
        boolean z2 = false;
        while (i < size) {
            z2 = !bitArray.get(i);
            if (!z2) {
                break;
            } else {
                i++;
            }
        }
        boolean z3 = z2;
        int i2 = 0;
        int i3 = i;
        while (i < size) {
            if (bitArray.get(i) ^ z3) {
                iArr[i2] = iArr[i2] + 1;
            } else {
                if (i2 == 3) {
                    if (z) {
                        reverseCounters(iArr);
                    }
                    if (isFinderPattern(iArr)) {
                        int[] iArr2 = this.startEnd;
                        iArr2[0] = i3;
                        iArr2[1] = i;
                        return;
                    }
                    if (z) {
                        reverseCounters(iArr);
                    }
                    i3 += iArr[0] + iArr[1];
                    iArr[0] = iArr[2];
                    iArr[1] = iArr[3];
                    iArr[2] = 0;
                    iArr[3] = 0;
                    i2--;
                } else {
                    i2++;
                }
                iArr[i2] = 1;
                z3 = !z3;
            }
            i++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int getNextSecondBar(BitArray bitArray, int i) {
        boolean z = bitArray.get(i);
        while (i < bitArray.size && bitArray.get(i) == z) {
            i++;
        }
        boolean z2 = !z;
        while (i < bitArray.size && bitArray.get(i) == z2) {
            i++;
        }
        return i;
    }

    private static boolean isNotA1left(FinderPattern finderPattern, boolean z, boolean z2) {
        return (finderPattern.getValue() == 0 && z && z2) ? false : true;
    }

    private FinderPattern parseFoundFinderPattern(BitArray bitArray, int i, boolean z) {
        int i2;
        int i3;
        int i4;
        if (z) {
            int i5 = this.startEnd[0] - 1;
            while (i5 >= 0 && !bitArray.get(i5)) {
                i5--;
            }
            int i6 = i5 + 1;
            int[] iArr = this.startEnd;
            i4 = iArr[0] - i6;
            i2 = iArr[1];
            i3 = i6;
        } else {
            int[] iArr2 = this.startEnd;
            int i7 = iArr2[0];
            int i8 = iArr2[1] + 1;
            while (bitArray.get(i8) && i8 < bitArray.size) {
                i8++;
            }
            i2 = i8;
            i3 = i7;
            i4 = i8 - this.startEnd[1];
        }
        int[] iArr3 = this.decodeFinderCounters;
        for (int length = iArr3.length - 1; length > 0; length--) {
            iArr3[length] = iArr3[length - 1];
        }
        iArr3[0] = i4;
        try {
            return new FinderPattern(parseFinderValue(iArr3, FINDER_PATTERNS), new int[]{i3, i2}, i3, i2, i);
        } catch (NotFoundException unused) {
            return null;
        }
    }

    private static void reverseCounters(int[] iArr) {
        int length = iArr.length;
        for (int i = 0; i < length / 2; i++) {
            int i2 = iArr[i];
            int i3 = (length - i) - 1;
            iArr[i] = iArr[i3];
            iArr[i3] = i2;
        }
    }

    DataCharacter decodeDataCharacter(BitArray bitArray, FinderPattern finderPattern, boolean z, boolean z2) throws NotFoundException {
        int[] iArr = this.dataCharacterCounters;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        iArr[4] = 0;
        iArr[5] = 0;
        iArr[6] = 0;
        iArr[7] = 0;
        int[] startEnd = finderPattern.getStartEnd();
        if (z2) {
            recordPatternInReverse(bitArray, startEnd[0], iArr);
        } else {
            recordPattern(bitArray, startEnd[1] + 1, iArr);
            int i = 0;
            for (int length = iArr.length - 1; i < length; length--) {
                int i2 = iArr[i];
                iArr[i] = iArr[length];
                iArr[length] = i2;
                i++;
            }
        }
        float count = count(iArr) / 17;
        int[] iArr2 = this.oddCounts;
        int[] iArr3 = this.evenCounts;
        float[] fArr = this.oddRoundingErrors;
        float[] fArr2 = this.evenRoundingErrors;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            float f = (iArr[i3] * 1.0f) / count;
            int i4 = (int) (0.5f + f);
            if (i4 < 1) {
                i4 = 1;
            } else if (i4 > 8) {
                i4 = 8;
            }
            int i5 = i3 >> 1;
            if ((i3 & 1) == 0) {
                iArr2[i5] = i4;
                fArr[i5] = f - i4;
            } else {
                iArr3[i5] = i4;
                fArr2[i5] = f - i4;
            }
        }
        adjustOddEvenCounts(17);
        int value = (((finderPattern.getValue() * 4) + (z ? 0 : 2)) + (!z2 ? 1 : 0)) - 1;
        int i6 = 0;
        int i7 = 0;
        for (int length2 = iArr2.length - 1; length2 >= 0; length2--) {
            if (isNotA1left(finderPattern, z, z2)) {
                i6 += iArr2[length2] * WEIGHTS[value][length2 * 2];
            }
            i7 += iArr2[length2];
        }
        int i8 = 0;
        for (int length3 = iArr3.length - 1; length3 >= 0; length3--) {
            if (isNotA1left(finderPattern, z, z2)) {
                i8 += iArr3[length3] * WEIGHTS[value][(length3 * 2) + 1];
            }
            int i9 = iArr3[length3];
        }
        int i10 = i6 + i8;
        if ((i7 & 1) != 0 || i7 > 13 || i7 < 4) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i11 = (13 - i7) / 2;
        int i12 = SYMBOL_WIDEST[i11];
        return new DataCharacter((RSSUtils.getRSSvalue(iArr2, i12, true) * EVEN_TOTAL_SUBSET[i11]) + RSSUtils.getRSSvalue(iArr3, 9 - i12, false) + GSUM[i11], i10);
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException {
        reset();
        decodeRow2pairs(i, bitArray);
        return constructResult(this.pairs);
    }

    Vector decodeRow2pairs(int i, BitArray bitArray) throws NotFoundException {
        while (true) {
            ExpandedPair retrieveNextPair = retrieveNextPair(bitArray, this.pairs, i);
            this.pairs.addElement(retrieveNextPair);
            if (retrieveNextPair.mayBeLast()) {
                if (checkChecksum()) {
                    return this.pairs;
                }
                if (retrieveNextPair.mustBeLast()) {
                    throw NotFoundException.getNotFoundInstance();
                }
            }
        }
    }

    @Override // com.google.zxing.oned.OneDReader, com.google.zxing.Reader
    public void reset() {
        this.pairs.setSize(0);
    }

    ExpandedPair retrieveNextPair(BitArray bitArray, Vector vector, int i) throws NotFoundException {
        FinderPattern parseFoundFinderPattern;
        DataCharacter dataCharacter;
        boolean z = vector.size() % 2 == 0;
        int i2 = -1;
        boolean z2 = true;
        do {
            findNextPair(bitArray, vector, i2);
            parseFoundFinderPattern = parseFoundFinderPattern(bitArray, i, z);
            if (parseFoundFinderPattern == null) {
                i2 = getNextSecondBar(bitArray, this.startEnd[0]);
            } else {
                z2 = false;
            }
        } while (z2);
        boolean checkPairSequence = checkPairSequence(vector, parseFoundFinderPattern);
        DataCharacter decodeDataCharacter = decodeDataCharacter(bitArray, parseFoundFinderPattern, z, true);
        try {
            dataCharacter = decodeDataCharacter(bitArray, parseFoundFinderPattern, z, false);
        } catch (NotFoundException e) {
            if (!checkPairSequence) {
                throw e;
            }
            dataCharacter = null;
        }
        return new ExpandedPair(decodeDataCharacter, dataCharacter, parseFoundFinderPattern, checkPairSequence);
    }
}
