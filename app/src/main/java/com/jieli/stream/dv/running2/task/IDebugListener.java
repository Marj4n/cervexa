package com.jieli.stream.dv.running2.task;

/* loaded from: classes.dex */
public interface IDebugListener {
    void onDebugResult(int i, int i2);

    void onError(int i, String str);

    void onStartDebug(String str, int i, int i2);
}
