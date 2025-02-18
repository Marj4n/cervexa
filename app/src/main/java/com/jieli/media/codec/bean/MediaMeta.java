package com.jieli.media.codec.bean;

/* loaded from: classes.dex */
public class MediaMeta {
    private int duration;
    private int height;
    private String path;
    private int width;

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public String toString() {
        return "[ width:" + this.width + ", height:" + this.height + ", duration:" + this.duration + ", path=" + this.path + " ]";
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }
}
