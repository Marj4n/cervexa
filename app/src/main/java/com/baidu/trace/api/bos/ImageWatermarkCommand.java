package com.baidu.trace.api.bos;

/* loaded from: classes.dex */
public class ImageWatermarkCommand {
    private String a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public ImageWatermarkCommand() {
        this.b = 9;
        this.c = 10;
        this.d = 10;
        this.e = 0;
        this.f = 100;
    }

    public ImageWatermarkCommand(String str, int i, int i2, int i3, int i4, int i5) {
        this.b = 9;
        this.c = 10;
        this.d = 10;
        this.e = 0;
        this.f = 100;
        this.a = str;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
        this.f = i5;
    }

    public int getAngle() {
        return this.e;
    }

    public int getGravity() {
        return this.b;
    }

    public int getGravityX() {
        return this.c;
    }

    public int getGravityY() {
        return this.d;
    }

    public String getObjectKey() {
        return this.a;
    }

    public int getOpacity() {
        return this.f;
    }

    public void setAngle(int i) {
        this.e = i;
    }

    public void setGravity(int i) {
        this.b = i;
    }

    public void setGravityX(int i) {
        this.c = i;
    }

    public void setGravityY(int i) {
        this.d = i;
    }

    public void setObjectKey(String str) {
        this.a = str;
    }

    public void setOpacity(int i) {
        this.f = i;
    }

    public String toString() {
        return "ImageWatermarkCommand [objectKey=" + this.a + ", gravity=" + this.b + ", gravityX=" + this.c + ", gravityY=" + this.d + ", angle=" + this.e + ", opacity=" + this.f + "]";
    }
}
