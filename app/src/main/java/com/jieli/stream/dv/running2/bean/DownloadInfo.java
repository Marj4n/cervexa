package com.jieli.stream.dv.running2.bean;

import java.io.Serializable;

@Deprecated
/* loaded from: classes.dex */
public class DownloadInfo implements Serializable {
    private int duration;
    private int offset;
    private String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.offset = i;
    }
}
