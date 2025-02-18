package com.serenegiant.glutils;

import android.view.Surface;

/* loaded from: classes.dex */
public interface RenderHolderCallback {
    void onCreate(Surface surface);

    void onDestroy();

    void onFrameAvailable();
}
