package com.baidu.trace.api.analysis;

/* loaded from: classes.dex */
public final class ThresholdOption {
    private double a;
    private double b;
    private double c;
    private double d;

    public ThresholdOption() {
    }

    public ThresholdOption(double d, double d2, double d3, double d4) {
        this.a = d;
        this.b = d2;
        this.c = d3;
        this.d = d4;
    }

    public final double getHarshAccelerationThreshold() {
        return this.b;
    }

    public final double getHarshBreakingThreshold() {
        return this.c;
    }

    public final double getHarshSteeringThreshold() {
        return this.d;
    }

    public final double getSpeedingThreshold() {
        return this.a;
    }

    public final ThresholdOption setHarshAccelerationThreshold(double d) {
        this.b = d;
        return this;
    }

    public final ThresholdOption setHarshBreakingThreshold(double d) {
        this.c = d;
        return this;
    }

    public final ThresholdOption setHarshSteeringThreshold(double d) {
        this.d = d;
        return this;
    }

    public final ThresholdOption setSpeedingThreshold(double d) {
        this.a = d;
        return this;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("ThresholdOption{");
        stringBuffer.append("speedingThreshold=");
        stringBuffer.append(this.a);
        stringBuffer.append(", harshAccelerationThreshold=");
        stringBuffer.append(this.b);
        stringBuffer.append(", harshBreakingThreshold=");
        stringBuffer.append(this.c);
        stringBuffer.append(", harshSteeringThreshold=");
        stringBuffer.append(this.d);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
