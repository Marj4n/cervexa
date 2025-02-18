package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;

/* loaded from: classes.dex */
public final class HistoryTrackRequest extends BaseRequest {
    private String a;
    private long b;
    private long c;
    private boolean d;
    private ProcessOption e;
    private SupplementMode f;
    private SortType g;
    private CoordType h;
    private int i;
    private int j;

    public HistoryTrackRequest() {
        this.d = false;
        this.f = SupplementMode.no_supplement;
        this.g = SortType.asc;
        this.h = CoordType.bd09ll;
    }

    public HistoryTrackRequest(int i, long j) {
        super(i, j);
        this.d = false;
        this.f = SupplementMode.no_supplement;
        this.g = SortType.asc;
        this.h = CoordType.bd09ll;
    }

    public HistoryTrackRequest(int i, long j, String str) {
        this(i, j);
        this.a = str;
    }

    public HistoryTrackRequest(int i, long j, String str, long j2, long j3, boolean z, ProcessOption processOption, SupplementMode supplementMode, SortType sortType, CoordType coordType, int i2, int i3) {
        this(i, j, str);
        this.b = j2;
        this.c = j3;
        this.d = z;
        this.e = processOption;
        this.f = supplementMode;
        this.g = sortType;
        this.h = coordType;
        this.i = i2;
        this.j = i3;
    }

    public final CoordType getCoordTypeOutput() {
        return this.h;
    }

    public final long getEndTime() {
        return this.c;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final int getPageIndex() {
        return this.i;
    }

    public final int getPageSize() {
        return this.j;
    }

    public final ProcessOption getProcessOption() {
        return this.e;
    }

    public final SortType getSortType() {
        return this.g;
    }

    public final long getStartTime() {
        return this.b;
    }

    public final SupplementMode getSupplementMode() {
        return this.f;
    }

    public final boolean isProcessed() {
        return this.d;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        this.h = coordType;
    }

    public final void setEndTime(long j) {
        this.c = j;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setPageIndex(int i) {
        this.i = i;
    }

    public final void setPageSize(int i) {
        this.j = i;
    }

    public final void setProcessOption(ProcessOption processOption) {
        this.e = processOption;
    }

    public final void setProcessed(boolean z) {
        this.d = z;
    }

    public final void setSortType(SortType sortType) {
        this.g = sortType;
    }

    public final void setStartTime(long j) {
        this.b = j;
    }

    public final void setSupplementMode(SupplementMode supplementMode) {
        this.f = supplementMode;
    }

    public final String toString() {
        return "HistoryTrackRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + ", startTime=" + this.b + ", endTime=" + this.c + ", isProcessed=" + this.d + ", processOption=" + this.e + ", supplementMode=" + this.f + ", sortType=" + this.g + ", coordTypeOutput=" + this.h + ", pageIndex=" + this.i + ", pageSize=" + this.j + "]";
    }
}
