package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class QueryCacheTrackResponse extends BaseResponse {
    private List<CacheTrackInfo> a;

    public QueryCacheTrackResponse() {
    }

    public QueryCacheTrackResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public final List<CacheTrackInfo> getResult() {
        return this.a;
    }

    public final void setResult(List<CacheTrackInfo> list) {
        this.a = list;
    }

    public final String toString() {
        return "QueryCacheTrackResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", result=" + this.a + "]";
    }
}
