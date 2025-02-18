package com.baidu.trace.model;

/* loaded from: classes.dex */
public interface OnTraceListener {
    void onBindServiceCallback(int i, String str);

    void onInitBOSCallback(int i, String str);

    void onPushCallback(byte b, PushMessage pushMessage);

    void onStartGatherCallback(int i, String str);

    void onStartTraceCallback(int i, String str);

    void onStopGatherCallback(int i, String str);

    void onStopTraceCallback(int i, String str);
}
