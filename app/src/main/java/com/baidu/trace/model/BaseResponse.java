package com.baidu.trace.model;

/* loaded from: classes.dex */
public class BaseResponse {
    public String message;
    public int status;
    public int tag;

    public BaseResponse() {
    }

    public BaseResponse(int i, int i2, String str) {
        this.tag = i;
        this.status = i2;
        this.message = str;
    }

    public BaseResponse(int i, String str) {
        this.status = i;
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.status;
    }

    public int getTag() {
        return this.tag;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setTag(int i) {
        this.tag = i;
    }
}
