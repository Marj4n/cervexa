package com.baidu.trace.api.analysis;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class DrivingBehaviorResponse extends BaseResponse {
    private double a;
    private int b;
    private double c;
    private double d;
    private int e;
    private int f;
    private int g;
    private int h;
    private StartPoint i;
    private EndPoint j;
    private List<SpeedingInfo> k;
    private List<HarshAccelerationPoint> l;
    private List<HarshBreakingPoint> m;
    private List<HarshSteeringPoint> n;

    public DrivingBehaviorResponse() {
    }

    public DrivingBehaviorResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public DrivingBehaviorResponse(int i, int i2, String str, double d, int i3, double d2, double d3, int i4, int i5, int i6, int i7, StartPoint startPoint, EndPoint endPoint, List<SpeedingInfo> list, List<HarshAccelerationPoint> list2, List<HarshBreakingPoint> list3, List<HarshSteeringPoint> list4) {
        super(i, i2, str);
        this.a = d;
        this.b = i3;
        this.c = d2;
        this.d = d3;
        this.e = i4;
        this.f = i5;
        this.g = i6;
        this.h = i7;
        this.i = startPoint;
        this.j = endPoint;
        this.k = list;
        this.l = list2;
        this.m = list3;
        this.n = list4;
    }

    public final double getAverageSpeed() {
        return this.c;
    }

    public final double getDistance() {
        return this.a;
    }

    public final int getDuration() {
        return this.b;
    }

    public final EndPoint getEndPoint() {
        return this.j;
    }

    public final int getHarshAccelerationNum() {
        return this.f;
    }

    public final List<HarshAccelerationPoint> getHarshAccelerationPoints() {
        return this.l;
    }

    public final int getHarshBreakingNum() {
        return this.g;
    }

    public final List<HarshBreakingPoint> getHarshBreakingPoints() {
        return this.m;
    }

    public final int getHarshSteeringNum() {
        return this.h;
    }

    public final List<HarshSteeringPoint> getHarshSteeringPoints() {
        return this.n;
    }

    public final double getMaxSpeed() {
        return this.d;
    }

    public final int getSpeedingNum() {
        return this.e;
    }

    public final List<SpeedingInfo> getSpeedings() {
        return this.k;
    }

    public final StartPoint getStartPoint() {
        return this.i;
    }

    public final void setAverageSpeed(double d) {
        this.c = d;
    }

    public final void setDistance(double d) {
        this.a = d;
    }

    public final void setDuration(int i) {
        this.b = i;
    }

    public final void setEndPoint(EndPoint endPoint) {
        this.j = endPoint;
    }

    public final void setHarshAccelerationNum(int i) {
        this.f = i;
    }

    public final void setHarshAccelerationPoints(List<HarshAccelerationPoint> list) {
        this.l = list;
    }

    public final void setHarshBreakingNum(int i) {
        this.g = i;
    }

    public final void setHarshBreakingPoints(List<HarshBreakingPoint> list) {
        this.m = list;
    }

    public final void setHarshSteeringNum(int i) {
        this.h = i;
    }

    public final void setHarshSteeringPoints(List<HarshSteeringPoint> list) {
        this.n = list;
    }

    public final void setMaxSpeed(double d) {
        this.d = d;
    }

    public final void setSpeedingNum(int i) {
        this.e = i;
    }

    public final void setSpeedings(List<SpeedingInfo> list) {
        this.k = list;
    }

    public final void setStartPoint(StartPoint startPoint) {
        this.i = startPoint;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("[");
        List<SpeedingInfo> list = this.k;
        if (list != null && list.size() > 0) {
            int size = this.k.size();
            for (int i = 0; i < size; i++) {
                stringBuffer.append("[");
                SpeedingInfo speedingInfo = this.k.get(i);
                if (speedingInfo != null && speedingInfo.getPoints() != null && speedingInfo.getPoints().size() != 0) {
                    List<SpeedingPoint> points = speedingInfo.getPoints();
                    int size2 = points.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        stringBuffer.append(points.get(i2).toString());
                        if (i2 < size2 - 1) {
                            stringBuffer.append(",");
                        }
                    }
                    stringBuffer.append("]");
                    if (i < size - 1) {
                        stringBuffer.append(",");
                    }
                }
            }
        }
        stringBuffer.append("]");
        return "DrivingBehaviorResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", distance=" + this.a + ", duration=" + this.b + ", averageSpeed=" + this.c + ", maxSpeed=" + this.d + ", speedingNum=" + this.e + ", harshAccelerationNum=" + this.f + ", harshBreakingNum=" + this.g + ", harshSteeringNum=" + this.h + ", startPoint=" + this.i + ", endPoint=" + this.j + ", speedingPoints=" + stringBuffer.toString() + ", harshAccelerationPoints=" + this.l + ", harshBreakingPoints=" + this.m + ", harshSteeringPoints=" + this.n + "]";
    }
}
