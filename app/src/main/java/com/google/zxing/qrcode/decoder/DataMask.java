package com.google.zxing.qrcode.decoder;

import com.google.zxing.common.BitMatrix;

/* loaded from: classes.dex */
abstract class DataMask {
    private static final DataMask[] DATA_MASKS;

    private static class DataMask000 extends DataMask {
        private DataMask000() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return ((i + i2) & 1) == 0;
        }
    }

    private static class DataMask001 extends DataMask {
        private DataMask001() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i & 1) == 0;
        }
    }

    private static class DataMask010 extends DataMask {
        private DataMask010() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return i2 % 3 == 0;
        }
    }

    private static class DataMask011 extends DataMask {
        private DataMask011() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i + i2) % 3 == 0;
        }
    }

    private static class DataMask100 extends DataMask {
        private DataMask100() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (((i >>> 1) + (i2 / 3)) & 1) == 0;
        }
    }

    private static class DataMask101 extends DataMask {
        private DataMask101() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            int i3 = i * i2;
            return (i3 & 1) + (i3 % 3) == 0;
        }
    }

    private static class DataMask110 extends DataMask {
        private DataMask110() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            int i3 = i * i2;
            return (((i3 & 1) + (i3 % 3)) & 1) == 0;
        }
    }

    private static class DataMask111 extends DataMask {
        private DataMask111() {
            super();
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return ((((i + i2) & 1) + ((i * i2) % 3)) & 1) == 0;
        }
    }

    static {
        DATA_MASKS = new DataMask[]{new DataMask000(), new DataMask001(), new DataMask010(), new DataMask011(), new DataMask100(), new DataMask101(), new DataMask110(), new DataMask111()};
    }

    private DataMask() {
    }

    static DataMask forReference(int i) {
        if (i < 0 || i > 7) {
            throw new IllegalArgumentException();
        }
        return DATA_MASKS[i];
    }

    abstract boolean isMasked(int i, int i2);

    final void unmaskBitMatrix(BitMatrix bitMatrix, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            for (int i3 = 0; i3 < i; i3++) {
                if (isMasked(i2, i3)) {
                    bitMatrix.flip(i3, i2);
                }
            }
        }
    }
}
