package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class ClearCacheTrackResponse extends BaseResponse {
    public ClearCacheTrackResponse() {
    }

    public ClearCacheTrackResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public ClearCacheTrackResponse(int i, String str) {
        super(i, str);
    }

    public final String toString() {
        return "ClearCacheTrackResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + "]";
    }
}
