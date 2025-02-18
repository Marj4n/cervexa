package com.github.mikephil.charting.data;

/* loaded from: classes.dex */
public abstract class BaseEntry {
    private Object mData;
    private float y;

    public BaseEntry() {
        this.y = 0.0f;
        this.mData = null;
    }

    public BaseEntry(float f) {
        this.y = 0.0f;
        this.mData = null;
        this.y = f;
    }

    public BaseEntry(float f, Object obj) {
        this(f);
        this.mData = obj;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f) {
        this.y = f;
    }

    public Object getData() {
        return this.mData;
    }

    public void setData(Object obj) {
        this.mData = obj;
    }
}
