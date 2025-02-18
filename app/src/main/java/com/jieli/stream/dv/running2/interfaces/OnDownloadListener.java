package com.jieli.stream.dv.running2.interfaces;

/* loaded from: classes.dex */
public interface OnDownloadListener {
    void onCompletion();

    void onError(int i, String str);

    void onProgress(int i);

    void onStartLoad();
}
