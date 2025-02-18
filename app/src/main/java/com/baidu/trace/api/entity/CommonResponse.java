package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public class CommonResponse extends BaseResponse {
    protected int a;
    protected int b;
    protected List<EntityInfo> c;

    public CommonResponse() {
    }

    public CommonResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public CommonResponse(int i, int i2, String str, int i3, int i4, List<EntityInfo> list) {
        super(i, i2, str);
        this.a = i3;
        this.b = i4;
        this.c = list;
    }

    public List<EntityInfo> getEntities() {
        return this.c;
    }

    public int getSize() {
        return this.b;
    }

    public int getTotal() {
        return this.a;
    }

    public void setEntities(List<EntityInfo> list) {
        this.c = list;
    }

    public void setSize(int i) {
        this.b = i;
    }

    public void setTotal(int i) {
        this.a = i;
    }

    public String toString() {
        return "CommonResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", total=" + this.a + ", size=" + this.b + ", entities=" + this.c + "]";
    }
}
