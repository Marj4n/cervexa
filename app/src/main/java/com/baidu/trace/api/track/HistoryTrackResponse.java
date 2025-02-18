package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;
import com.baidu.trace.model.Point;
import java.util.List;

/* loaded from: classes.dex */
public final class HistoryTrackResponse extends BaseResponse {
    private int a;
    private int b;
    private String c;
    private double d;
    private double e;
    private Point f;
    private Point g;
    public List<TrackPoint> trackPoints;

    public HistoryTrackResponse() {
    }

    public HistoryTrackResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public HistoryTrackResponse(int i, int i2, String str, int i3, int i4, String str2, double d, double d2, Point point, Point point2, List<TrackPoint> list) {
        super(i, i2, str);
        this.a = i3;
        this.b = i4;
        this.c = str2;
        this.d = d;
        this.e = d2;
        this.f = point;
        this.g = point2;
        this.trackPoints = list;
    }

    public final double getDistance() {
        return this.d;
    }

    public final Point getEndPoint() {
        return this.g;
    }

    public final String getEntityName() {
        return this.c;
    }

    public final int getSize() {
        return this.b;
    }

    public final Point getStartPoint() {
        return this.f;
    }

    public final double getTollDistance() {
        return this.e;
    }

    public final int getTotal() {
        return this.a;
    }

    public final List<TrackPoint> getTrackPoints() {
        return this.trackPoints;
    }

    public final void setDistance(double d) {
        this.d = d;
    }

    public final void setEndPoint(Point point) {
        this.g = point;
    }

    public final void setEntityName(String str) {
        this.c = str;
    }

    public final void setSize(int i) {
        this.b = i;
    }

    public final void setStartPoint(Point point) {
        this.f = point;
    }

    public final void setTollDistance(double d) {
        this.e = d;
    }

    public final void setTotal(int i) {
        this.a = i;
    }

    public final void setTrackPoints(List<TrackPoint> list) {
        this.trackPoints = list;
    }

    public final String toString() {
        return "HistoryTrackResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", total=" + this.a + ", size=" + this.b + ", entityName=" + this.c + ", distance=" + this.d + ", tollDistance=" + this.e + ", startPoint=" + this.f + ", endPoint=" + this.g + ", trackPoints=" + this.trackPoints + "]";
    }
}
