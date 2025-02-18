package com.jieli.stream.dv.running2.data;

/* loaded from: classes.dex */
public interface OnRecordStateListener {
    void onCompletion(String str);

    void onError(String str);

    void onPrepared();

    void onStop();
}
