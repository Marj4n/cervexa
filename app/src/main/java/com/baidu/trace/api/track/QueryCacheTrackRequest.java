package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;

/* loaded from: classes.dex */
public final class QueryCacheTrackRequest extends BaseRequest {
    private String a;

    public QueryCacheTrackRequest() {
    }

    public QueryCacheTrackRequest(int i, long j) {
        super(i, j);
    }

    public QueryCacheTrackRequest(int i, long j, String str) {
        super(i, j);
        this.a = str;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final String toString() {
        return "QueryCacheTrackRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + "]";
    }
}
