package com.baidu.trace.api.analysis;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class StayPointResponse extends BaseResponse {
    private int a;
    private List<StayPoint> b;

    public StayPointResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public StayPointResponse(int i, int i2, String str, int i3, List<StayPoint> list) {
        super(i, i2, str);
        this.a = i3;
        this.b = list;
    }

    public final int getStayPointNum() {
        return this.a;
    }

    public final List<StayPoint> getStayPoints() {
        return this.b;
    }

    public final void setStayPointNum(int i) {
        this.a = i;
    }

    public final void setStayPoints(List<StayPoint> list) {
        this.b = list;
    }

    public final String toString() {
        return "StayPointResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", stayPointNum=" + this.a + ", stayPoints=" + this.b + "]";
    }
}
