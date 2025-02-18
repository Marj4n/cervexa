package com.jieli.lib.dv.control.model;

import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public class MediaInfo implements Cloneable {
    private String a;
    private int b;
    private int c;
    private int d = 30;
    private int e = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
    private int f;

    public int getSampleRate() {
        return this.e;
    }

    public void setSampleRate(int i) {
        this.e = i;
    }

    public int getWidth() {
        return this.b;
    }

    public void setWidth(int i) {
        this.b = i;
    }

    public int getHeight() {
        return this.c;
    }

    public void setHeight(int i) {
        this.c = i;
    }

    public String toString() {
        return "width:" + this.b + ", height:" + this.c + ", path:" + this.a + ", frameRate:" + this.d + ", sampleRate:" + this.e + ", duration:" + this.f;
    }

    public String getPath() {
        return this.a;
    }

    public void setPath(String str) {
        this.a = str;
    }

    public int getFrameRate() {
        return this.d;
    }

    public void setFrameRate(int i) {
        this.d = i;
    }

    protected Object clone() {
        try {
            return (MediaInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDuration() {
        return this.f;
    }

    public void setDuration(int i) {
        this.f = i;
    }
}
