package com.google.zxing.common.reedsolomon;

/* loaded from: classes.dex */
public final class GenericGF {
    public static final GenericGF AZTEC_DATA_8;
    public static final GenericGF DATA_MATRIX_FIELD_256;
    private static final int INITIALIZATION_THRESHOLD = 0;
    private int[] expTable;
    private boolean initialized = false;
    private int[] logTable;
    private GenericGFPoly one;
    private final int primitive;
    private final int size;
    private GenericGFPoly zero;
    public static final GenericGF AZTEC_DATA_12 = new GenericGF(4201, 4096);
    public static final GenericGF AZTEC_DATA_10 = new GenericGF(1033, 1024);
    public static final GenericGF AZTEC_DATA_6 = new GenericGF(67, 64);
    public static final GenericGF AZTEC_PARAM = new GenericGF(19, 16);
    public static final GenericGF QR_CODE_FIELD_256 = new GenericGF(285, 256);

    static {
        GenericGF genericGF = new GenericGF(301, 256);
        DATA_MATRIX_FIELD_256 = genericGF;
        AZTEC_DATA_8 = genericGF;
    }

    public GenericGF(int i, int i2) {
        this.primitive = i;
        this.size = i2;
        if (i2 <= 0) {
            initialize();
        }
    }

    static int addOrSubtract(int i, int i2) {
        return i ^ i2;
    }

    private void checkInit() {
        if (this.initialized) {
            return;
        }
        initialize();
    }

    private void initialize() {
        int i = this.size;
        this.expTable = new int[i];
        this.logTable = new int[i];
        int i2 = 0;
        int i3 = 1;
        while (true) {
            int i4 = this.size;
            if (i2 >= i4) {
                break;
            }
            this.expTable[i2] = i3;
            i3 <<= 1;
            if (i3 >= i4) {
                i3 = (i3 ^ this.primitive) & (i4 - 1);
            }
            i2++;
        }
        for (int i5 = 0; i5 < this.size - 1; i5++) {
            this.logTable[this.expTable[i5]] = i5;
        }
        this.zero = new GenericGFPoly(this, new int[]{0});
        this.one = new GenericGFPoly(this, new int[]{1});
        this.initialized = true;
    }

    GenericGFPoly buildMonomial(int i, int i2) {
        checkInit();
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        if (i2 == 0) {
            return this.zero;
        }
        int[] iArr = new int[i + 1];
        iArr[0] = i2;
        return new GenericGFPoly(this, iArr);
    }

    int exp(int i) {
        checkInit();
        return this.expTable[i];
    }

    GenericGFPoly getOne() {
        checkInit();
        return this.one;
    }

    public int getSize() {
        return this.size;
    }

    GenericGFPoly getZero() {
        checkInit();
        return this.zero;
    }

    int inverse(int i) {
        checkInit();
        if (i != 0) {
            return this.expTable[(this.size - this.logTable[i]) - 1];
        }
        throw new ArithmeticException();
    }

    int log(int i) {
        checkInit();
        if (i != 0) {
            return this.logTable[i];
        }
        throw new IllegalArgumentException();
    }

    int multiply(int i, int i2) {
        int i3;
        checkInit();
        if (i == 0 || i2 == 0) {
            return 0;
        }
        if (i < 0 || i2 < 0 || i >= (i3 = this.size) || i2 >= i3) {
            i++;
        }
        int[] iArr = this.logTable;
        int i4 = iArr[i] + iArr[i2];
        int[] iArr2 = this.expTable;
        int i5 = this.size;
        return iArr2[(i4 % i5) + (i4 / i5)];
    }
}
