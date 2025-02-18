package com.baidu.trace.api.bos;

/* loaded from: classes.dex */
public class ImageProcessCommand {
    private int a;
    private int b;
    private int c;
    private int d;
    private ImageFormat e;
    private int f;
    private ImageDisplay g;
    private boolean h;
    private boolean i;
    private int j;
    private int k;
    private int l;
    private int m;

    public ImageProcessCommand() {
        this.a = 0;
        this.e = ImageFormat.jpg;
        this.f = 0;
        this.g = ImageDisplay.baseline;
        this.h = false;
        this.i = false;
        this.j = 0;
        this.k = 0;
    }

    public ImageProcessCommand(int i, int i2, int i3, int i4, ImageFormat imageFormat, int i5, ImageDisplay imageDisplay, boolean z, boolean z2, int i6, int i7, int i8, int i9) {
        this.a = 0;
        this.e = ImageFormat.jpg;
        this.f = 0;
        this.g = ImageDisplay.baseline;
        this.h = false;
        this.i = false;
        this.j = 0;
        this.k = 0;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        this.e = imageFormat;
        this.f = i5;
        this.g = imageDisplay;
        this.h = z;
        this.i = z2;
        this.j = i6;
        this.k = i7;
        this.l = i8;
        this.m = i9;
    }

    public int getAngle() {
        return this.f;
    }

    public int getCropHeight() {
        return this.m;
    }

    public int getCropWidth() {
        return this.l;
    }

    public ImageDisplay getDisplay() {
        return this.g;
    }

    public ImageFormat getFormat() {
        return this.e;
    }

    public int getMaxHeight() {
        return this.c;
    }

    public int getMaxWidth() {
        return this.b;
    }

    public int getOffsetX() {
        return this.j;
    }

    public int getOffsetY() {
        return this.k;
    }

    public int getQuality() {
        return this.d;
    }

    public int getScale() {
        return this.a;
    }

    public boolean isCrop() {
        return this.i;
    }

    public boolean isLimit() {
        return this.h;
    }

    public void setAngle(int i) {
        this.f = i;
    }

    public void setCrop(boolean z) {
        this.i = z;
    }

    public void setCropHeight(int i) {
        this.m = i;
    }

    public void setCropWidth(int i) {
        this.l = i;
    }

    public void setDisplay(ImageDisplay imageDisplay) {
        this.g = imageDisplay;
    }

    public void setFormat(ImageFormat imageFormat) {
        this.e = imageFormat;
    }

    public void setLimit(boolean z) {
        this.h = z;
    }

    public void setMaxHeight(int i) {
        this.c = i;
    }

    public void setMaxWidth(int i) {
        this.b = i;
    }

    public void setOffsetX(int i) {
        this.j = i;
    }

    public void setOffsetY(int i) {
        this.k = i;
    }

    public void setQuality(int i) {
        this.d = i;
    }

    public void setScale(int i) {
        this.a = i;
    }

    public String toString() {
        return "ImageProcessCommand [scale=" + this.a + ", maxWidth=" + this.b + ", maxHeight=" + this.c + ", quality=" + this.d + ", format=" + this.e + ", angle=" + this.f + ", display=" + this.g + ", limit=" + this.h + ", crop=" + this.i + ", offsetX=" + this.j + ", offsetY=" + this.k + ", cropWidth=" + this.l + ", cropHeight=" + this.m + "]";
    }
}
