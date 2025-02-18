package com.baidu.trace.api.bos;

/* loaded from: classes.dex */
public class TextWatermarkCommand {
    private String a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private String g;
    private FontFamily h;
    private FontStyle i;

    public TextWatermarkCommand() {
        this.b = 9;
        this.c = 10;
        this.d = 10;
        this.e = 0;
        this.f = 30;
        this.g = "000000";
        this.h = FontFamily.SimSun;
        this.i = FontStyle.normal;
    }

    public TextWatermarkCommand(String str, int i, int i2, int i3, int i4, int i5, String str2, FontFamily fontFamily, FontStyle fontStyle) {
        this.b = 9;
        this.c = 10;
        this.d = 10;
        this.e = 0;
        this.f = 30;
        this.g = "000000";
        this.h = FontFamily.SimSun;
        this.i = FontStyle.normal;
        this.a = str;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
        this.f = i5;
        this.g = str2;
        this.h = fontFamily;
        this.i = fontStyle;
    }

    public int getAngle() {
        return this.e;
    }

    public String getFontColor() {
        return this.g;
    }

    public FontFamily getFontFamily() {
        return this.h;
    }

    public int getFontSize() {
        return this.f;
    }

    public FontStyle getFontStyle() {
        return this.i;
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

    public String getText() {
        return this.a;
    }

    public void setAngle(int i) {
        this.e = i;
    }

    public void setFontColor(String str) {
        this.g = str;
    }

    public void setFontFamily(FontFamily fontFamily) {
        this.h = fontFamily;
    }

    public void setFontSize(int i) {
        this.f = i;
    }

    public void setFontStyle(FontStyle fontStyle) {
        this.i = fontStyle;
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

    public void setText(String str) {
        this.a = str;
    }

    public String toString() {
        return "TextWatermarkCommand [text=" + this.a + ", gravity=" + this.b + ", gravityX=" + this.c + ", gravityY=" + this.d + ", angle=" + this.e + ", fontSize=" + this.f + ", fontColor=" + this.g + ", fontFamily=" + this.h + ", fontStyle=" + this.i + "]";
    }
}
