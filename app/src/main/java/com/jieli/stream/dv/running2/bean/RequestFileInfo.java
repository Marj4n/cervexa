package com.jieli.stream.dv.running2.bean;

/* loaded from: classes.dex */
public class RequestFileInfo {
    private FileInfo fileInfo;
    private boolean isContent;

    public RequestFileInfo(FileInfo fileInfo, boolean z) {
        this.fileInfo = fileInfo;
        this.isContent = z;
    }

    public RequestFileInfo() {
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileInfo getFileInfo() {
        return this.fileInfo;
    }

    public boolean isContent() {
        return this.isContent;
    }

    public void setContent(boolean z) {
        this.isContent = z;
    }
}
