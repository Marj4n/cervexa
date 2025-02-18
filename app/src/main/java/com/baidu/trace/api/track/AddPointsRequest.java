package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AddPointsRequest extends BaseRequest {
    private Map<String, List<TrackPoint>> a;

    public AddPointsRequest() {
        this.a = null;
    }

    public AddPointsRequest(int i, long j) {
        super(i, j);
        this.a = null;
    }

    public AddPointsRequest(int i, long j, Map<String, List<TrackPoint>> map) {
        super(i, j);
        this.a = null;
        this.a = map;
    }

    public Map<String, List<TrackPoint>> getPoints() {
        return this.a;
    }

    public void setPoints(Map<String, List<TrackPoint>> map) {
        this.a = map;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AddPointsRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", points=");
        stringBuffer.append(this.a);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
