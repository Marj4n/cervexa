package com.jieli.lib.dv.control.player;

import com.jieli.lib.dv.control.base.AbstractClient;

/* loaded from: classes.dex */
abstract class IStreamClient extends AbstractClient {
    @Override // com.jieli.lib.dv.control.base.AbstractClient
    protected abstract boolean close();

    @Override // com.jieli.lib.dv.control.base.AbstractClient
    protected boolean create() {
        return false;
    }

    protected abstract boolean create(int i);

    protected abstract boolean create(int i, String str);

    protected abstract boolean create(int i, String str, boolean z);

    protected abstract boolean isReceiving();

    protected abstract void onError(int i, String str);

    protected abstract void onStateChanged(int i);

    protected abstract void registerStreamListener(IPlayerListener iPlayerListener);

    protected abstract boolean setFrameRate(int i);

    protected abstract boolean setResolution(int i, int i2);

    protected abstract boolean setSampleRate(int i);

    protected abstract void unregisterStreamListener(IPlayerListener iPlayerListener);

    IStreamClient() {
    }
}
