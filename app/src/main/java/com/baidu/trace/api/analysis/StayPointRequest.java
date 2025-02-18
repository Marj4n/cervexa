package com.baidu.trace.api.analysis;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
public final class StayPointRequest extends BaseRequest {
    private String a;
    private long b;
    private long c;
    private int d;
    private int e;
    private ProcessOption f;
    private CoordType g;

    public StayPointRequest() {
        this.d = IjkMediaCodecInfo.RANK_LAST_CHANCE;
        this.e = 20;
        this.g = CoordType.bd09ll;
    }

    public StayPointRequest(int i, long j) {
        super(i, j);
        this.d = IjkMediaCodecInfo.RANK_LAST_CHANCE;
        this.e = 20;
        this.g = CoordType.bd09ll;
    }

    public StayPointRequest(int i, long j, String str) {
        super(i, j);
        this.d = IjkMediaCodecInfo.RANK_LAST_CHANCE;
        this.e = 20;
        this.g = CoordType.bd09ll;
        this.a = str;
    }

    public StayPointRequest(int i, long j, String str, long j2, long j3, int i2, int i3, ProcessOption processOption, CoordType coordType) {
        super(i, j);
        this.d = IjkMediaCodecInfo.RANK_LAST_CHANCE;
        this.e = 20;
        this.g = CoordType.bd09ll;
        this.a = str;
        this.b = j2;
        this.c = j3;
        this.d = i2;
        this.e = i3;
        this.f = processOption;
        this.g = coordType;
    }

    public StayPointRequest(int i, long j, String str, long j2, long j3, int i2, ProcessOption processOption, CoordType coordType) {
        super(i, j);
        this.d = IjkMediaCodecInfo.RANK_LAST_CHANCE;
        this.e = 20;
        this.g = CoordType.bd09ll;
        this.a = str;
        this.b = j2;
        this.c = j3;
        this.d = i2;
        this.f = processOption;
        this.g = coordType;
    }

    public final CoordType getCoordTypeOutput() {
        return this.g;
    }

    public final long getEndTime() {
        return this.c;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final ProcessOption getProcessOption() {
        return this.f;
    }

    public final long getStartTime() {
        return this.b;
    }

    public final int getStayRadius() {
        return this.e;
    }

    public final int getStayTime() {
        return this.d;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        this.g = coordType;
    }

    public final void setEndTime(long j) {
        this.c = j;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setProcessOption(ProcessOption processOption) {
        this.f = processOption;
    }

    public final void setStartTime(long j) {
        this.b = j;
    }

    public final void setStayRadius(int i) {
        this.e = i;
    }

    public final void setStayTime(int i) {
        this.d = i;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("StayPointRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", entityName='");
        stringBuffer.append(this.a);
        stringBuffer.append('\'');
        stringBuffer.append(", startTime=");
        stringBuffer.append(this.b);
        stringBuffer.append(", endTime=");
        stringBuffer.append(this.c);
        stringBuffer.append(", stayTime=");
        stringBuffer.append(this.d);
        stringBuffer.append(", stayRadius=");
        stringBuffer.append(this.e);
        stringBuffer.append(", processOption=");
        stringBuffer.append(this.f);
        stringBuffer.append(", coordTypeOutput=");
        stringBuffer.append(this.g);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
