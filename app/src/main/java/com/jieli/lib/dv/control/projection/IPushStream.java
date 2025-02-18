package com.jieli.lib.dv.control.projection;

/* loaded from: classes.dex */
public interface IPushStream {
    void close();

    void create(String str);

    void create(String str, int i);

    boolean send(int i, byte[] bArr);

    void setOnSendStateListener(OnSendStateListener onSendStateListener);
}
