package com.jieli.stream.dv.running2.interfaces;

import com.jieli.media.codec.bean.MediaMeta;

/* loaded from: classes.dex */
public interface OnAviThumbListener {
    void onCompleted(byte[] bArr, MediaMeta mediaMeta);

    void onError(String str);
}
