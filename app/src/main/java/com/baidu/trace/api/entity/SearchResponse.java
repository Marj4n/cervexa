package com.baidu.trace.api.entity;

import java.util.List;

/* loaded from: classes.dex */
public final class SearchResponse extends CommonResponse {
    public SearchResponse() {
    }

    public SearchResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public SearchResponse(int i, int i2, String str, int i3, int i4, List<EntityInfo> list) {
        super(i, i2, str, i3, i4, list);
    }

    @Override // com.baidu.trace.api.entity.CommonResponse
    public final String toString() {
        return "SearchResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", total=" + this.a + ", size=" + this.b + ", entities=" + this.c + "]";
    }
}
