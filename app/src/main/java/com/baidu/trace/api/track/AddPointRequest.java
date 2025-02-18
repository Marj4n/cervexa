package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.Point;
import java.util.Map;

/* loaded from: classes.dex */
public class AddPointRequest extends BaseRequest {
    private String a;
    private Point b;
    private String c;
    private Map<String, String> d;

    public AddPointRequest() {
    }

    public AddPointRequest(int i, long j) {
        super(i, j);
    }

    public AddPointRequest(int i, long j, String str, Point point, String str2, Map<String, String> map) {
        this(i, j);
        this.a = str;
        this.b = point;
        this.c = str2;
        this.d = map;
    }

    public Map<String, String> getColumns() {
        return this.d;
    }

    public String getEntityName() {
        return this.a;
    }

    public String getObjectName() {
        return this.c;
    }

    public Point getPoint() {
        return this.b;
    }

    public void setColumns(Map<String, String> map) {
        this.d = map;
    }

    public void setEntityName(String str) {
        this.a = str;
    }

    public void setObjectName(String str) {
        this.c = str;
    }

    public void setPoint(Point point) {
        this.b = point;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AddPointRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", entityName='");
        stringBuffer.append(this.a);
        stringBuffer.append('\'');
        stringBuffer.append(", point=");
        stringBuffer.append(this.b);
        stringBuffer.append(", objectName='");
        stringBuffer.append(this.c);
        stringBuffer.append('\'');
        stringBuffer.append(", columns=");
        stringBuffer.append(this.d);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
