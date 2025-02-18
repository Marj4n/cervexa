package com.jieli.stream.dv.running2.bean;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class ThumbnailInfo extends FileInfo {
    private Bitmap bitmap;
    private String saveUrl;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setSaveUrl(String str) {
        this.saveUrl = str;
    }

    public String getSaveUrl() {
        return this.saveUrl;
    }
}
