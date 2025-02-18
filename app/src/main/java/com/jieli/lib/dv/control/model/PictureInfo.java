package com.jieli.lib.dv.control.model;

/* loaded from: classes.dex */
public class PictureInfo extends MediaInfo implements Cloneable {
    private long a;
    private boolean b = false;

    public long getTime() {
        return this.a;
    }

    public void setTime(long j) {
        this.a = j;
    }

    @Override // com.jieli.lib.dv.control.model.MediaInfo
    public String toString() {
        return super.toString() + ", time:" + this.a + ", isLast:" + this.b;
    }

    @Override // com.jieli.lib.dv.control.model.MediaInfo
    public Object clone() {
        return super.clone();
    }

    public boolean isLast() {
        return this.b;
    }

    public void setLast(boolean z) {
        this.b = z;
    }
}
