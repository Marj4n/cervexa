package com.jieli.lib.dv.control.player;

/* loaded from: classes.dex */
public abstract class AbsRecording extends IStreamClient {
    public static final int REC_STATE_COMPLETION = 2;

    @Deprecated
    public static final int REC_STATE_END = 2;
    public static final int REC_STATE_START = 1;
    public static final int REC_STATE_STOP = 3;

    @Override // com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    protected boolean close() {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean create(int i, String str) {
        return false;
    }

    boolean create(String str) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onError(int i, String str) {
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onStateChanged(int i) {
    }

    protected void onStateChanged(int i, String str) {
    }
}
