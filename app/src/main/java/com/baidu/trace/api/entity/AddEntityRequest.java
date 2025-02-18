package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseRequest;
import java.util.Map;

/* loaded from: classes.dex */
public final class AddEntityRequest extends BaseRequest {
    private String a;
    private String b;
    private Map<String, String> c;

    public AddEntityRequest() {
    }

    public AddEntityRequest(int i, long j) {
        super(i, j);
    }

    public AddEntityRequest(int i, long j, String str) {
        super(i, j);
        this.a = str;
    }

    public AddEntityRequest(int i, long j, String str, String str2, Map<String, String> map) {
        super(i, j);
        this.a = str;
        this.b = str2;
        this.c = map;
    }

    public final Map<String, String> getColumns() {
        return this.c;
    }

    public final String getEntityDesc() {
        return this.b;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final void setColumns(Map<String, String> map) {
        this.c = map;
    }

    public final void setEntityDesc(String str) {
        this.b = str;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final String toString() {
        return "AddEntityRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + ", entityDesc=" + this.b + ", columns=" + this.c + "]";
    }
}
