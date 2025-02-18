package org.apache.commons.net.telnet;

/* loaded from: classes2.dex */
public class SuppressGAOptionHandler extends TelnetOptionHandler {
    public SuppressGAOptionHandler(boolean z, boolean z2, boolean z3, boolean z4) {
        super(3, z, z2, z3, z4);
    }

    public SuppressGAOptionHandler() {
        super(3, false, false, false, false);
    }
}
