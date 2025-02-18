package com.baidu.trace.model;

/* loaded from: classes.dex */
public class ProcessOption {
    private boolean a;
    private boolean b;
    private boolean c;
    private int d;
    private TransportMode e;

    public ProcessOption() {
        this.a = true;
        this.b = true;
        this.c = false;
        this.d = 0;
        this.e = TransportMode.driving;
    }

    public ProcessOption(boolean z, boolean z2, boolean z3, int i, TransportMode transportMode) {
        this.a = true;
        this.b = true;
        this.c = false;
        this.d = 0;
        this.e = TransportMode.driving;
        this.a = z;
        this.b = z2;
        this.c = z3;
        this.d = i;
        this.e = transportMode;
    }

    public int getRadiusThreshold() {
        return this.d;
    }

    public TransportMode getTransportMode() {
        return this.e;
    }

    public boolean isNeedDenoise() {
        return this.a;
    }

    public boolean isNeedMapMatch() {
        return this.c;
    }

    public boolean isNeedVacuate() {
        return this.b;
    }

    public ProcessOption setNeedDenoise(boolean z) {
        this.a = z;
        return this;
    }

    public ProcessOption setNeedMapMatch(boolean z) {
        this.c = z;
        return this;
    }

    public ProcessOption setNeedVacuate(boolean z) {
        this.b = z;
        return this;
    }

    public ProcessOption setRadiusThreshold(int i) {
        this.d = i;
        return this;
    }

    public ProcessOption setTransportMode(TransportMode transportMode) {
        this.e = transportMode;
        return this;
    }

    public String toString() {
        return "ProcessOption [needDenoise=" + this.a + ", needVacuate=" + this.b + ", needMapMatch=" + this.c + ", radiusThreshold=" + this.d + ", transportMode=" + this.e + "]";
    }
}
