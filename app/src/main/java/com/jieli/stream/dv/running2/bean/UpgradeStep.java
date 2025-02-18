package com.jieli.stream.dv.running2.bean;

/* loaded from: classes.dex */
public class UpgradeStep {
    private String description;
    private boolean isNeedPb;
    private int num;
    private int state;

    public int getNum() {
        return this.num;
    }

    public void setNum(int i) {
        this.num = i;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public boolean isNeedPb() {
        return this.isNeedPb;
    }

    public void setNeedPb(boolean z) {
        this.isNeedPb = z;
    }
}
