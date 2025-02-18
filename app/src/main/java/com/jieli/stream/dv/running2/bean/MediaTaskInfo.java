package com.jieli.stream.dv.running2.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class MediaTaskInfo implements Serializable {
    private FileInfo info;
    private int op;

    public FileInfo getInfo() {
        return this.info;
    }

    public MediaTaskInfo setInfo(FileInfo fileInfo) {
        this.info = fileInfo;
        return this;
    }

    public int getOp() {
        return this.op;
    }

    public MediaTaskInfo setOp(int i) {
        this.op = i;
        return this;
    }
}
