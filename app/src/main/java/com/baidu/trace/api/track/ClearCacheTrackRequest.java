package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import java.util.List;

/* loaded from: classes.dex */
public final class ClearCacheTrackRequest extends BaseRequest {
    private List<String> a;
    private List<CacheTrackInfo> b;

    public ClearCacheTrackRequest() {
        this.a = null;
        this.b = null;
    }

    public ClearCacheTrackRequest(int i, long j) {
        super(i, j);
        this.a = null;
        this.b = null;
    }

    public ClearCacheTrackRequest(int i, long j, List<String> list) {
        super(i, j);
        this.a = null;
        this.b = null;
        this.a = list;
    }

    public final List<CacheTrackInfo> getCacheTrackInfos() {
        return this.b;
    }

    public final List<String> getEntityNames() {
        return this.a;
    }

    public final void setCacheTrackInfos(List<CacheTrackInfo> list) {
        this.b = list;
    }

    public final void setEntityNames(List<String> list) {
        this.a = list;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("ClearCacheTrackRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", entityNames=");
        stringBuffer.append(this.a);
        stringBuffer.append(", cacheTrackInfos=");
        stringBuffer.append(this.b);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
