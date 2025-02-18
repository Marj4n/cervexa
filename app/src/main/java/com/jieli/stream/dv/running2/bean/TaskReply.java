package com.jieli.stream.dv.running2.bean;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class TaskReply {
    private int code;
    private byte[] data;
    private String msg;
    private int result;

    public int getResult() {
        return this.result;
    }

    public TaskReply setResult(int i) {
        this.result = i;
        return this;
    }

    public int getCode() {
        return this.code;
    }

    public TaskReply setCode(int i) {
        this.code = i;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public TaskReply setMsg(String str) {
        this.msg = str;
        return this;
    }

    public byte[] getData() {
        return this.data;
    }

    public TaskReply setData(byte[] bArr) {
        this.data = bArr;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"result\": ");
        sb.append(this.result);
        sb.append(",\n\"code\": ");
        sb.append(this.code);
        sb.append(",\n\"msg\":\"");
        sb.append(TextUtils.isEmpty(this.msg) ? "" : this.msg);
        sb.append("\",\n\"data\":\"");
        sb.append(this.data != null ? new String(this.data) : "");
        sb.append("\"}");
        return sb.toString();
    }
}
