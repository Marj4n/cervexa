package com.serenegiant.media;

import android.view.Surface;

/* loaded from: classes2.dex */
public interface EncoderListener {
    void onDestroy(Encoder encoder);

    void onError(Exception exc);

    void onStartEncode(Encoder encoder, Surface surface, int i, boolean z);

    void onStopEncode(Encoder encoder);
}
