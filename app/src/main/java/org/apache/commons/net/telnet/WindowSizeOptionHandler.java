package org.apache.commons.net.telnet;

/* loaded from: classes2.dex */
public class WindowSizeOptionHandler extends TelnetOptionHandler {
    protected static final int WINDOW_SIZE = 31;
    private int m_nHeight;
    private int m_nWidth;

    public WindowSizeOptionHandler(int i, int i2, boolean z, boolean z2, boolean z3, boolean z4) {
        super(31, z, z2, z3, z4);
        this.m_nWidth = 80;
        this.m_nHeight = 24;
        this.m_nWidth = i;
        this.m_nHeight = i2;
    }

    public WindowSizeOptionHandler(int i, int i2) {
        super(31, false, false, false, false);
        this.m_nWidth = 80;
        this.m_nHeight = 24;
        this.m_nWidth = i;
        this.m_nHeight = i2;
    }

    @Override // org.apache.commons.net.telnet.TelnetOptionHandler
    public int[] startSubnegotiationLocal() {
        int i = this.m_nWidth;
        int i2 = (65536 * i) + this.m_nHeight;
        int i3 = i % 256 == 255 ? 6 : 5;
        if (this.m_nWidth / 256 == 255) {
            i3++;
        }
        if (this.m_nHeight % 256 == 255) {
            i3++;
        }
        if (this.m_nHeight / 256 == 255) {
            i3++;
        }
        int[] iArr = new int[i3];
        iArr[0] = 31;
        int i4 = 24;
        int i5 = 1;
        while (i5 < i3) {
            iArr[i5] = ((255 << i4) & i2) >>> i4;
            if (iArr[i5] == 255) {
                i5++;
                iArr[i5] = 255;
            }
            i5++;
            i4 -= 8;
        }
        return iArr;
    }
}
