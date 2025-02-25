package com.jieli.stream.dv.running2.bean;

import java.io.Serializable;
import java.util.Calendar;

/* loaded from: classes.dex */
public class FileInfo implements Serializable, Cloneable {
    private int duration;
    private Calendar endTime;
    private int height;
    private boolean isVideo;
    private int offset;
    private int rate;
    private long size;
    private int source;
    private Calendar startTime;
    private int type;
    private int width;
    private String name = "";
    private String path = "";
    private String createTime = "";
    private String fileEndTime = "";
    private boolean isSelected = false;
    private String cameraType = "0";

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

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String str) {
        this.createTime = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public int getSource() {
        return this.source;
    }

    public void setSource(int i) {
        this.source = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public Calendar getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Calendar calendar) {
        this.startTime = calendar;
    }

    public Calendar getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Calendar calendar) {
        this.endTime = calendar;
    }

    public int getRate() {
        return this.rate;
    }

    public void setRate(int i) {
        this.rate = i;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.offset = i;
    }

    public String getCameraType() {
        return this.cameraType;
    }

    public void setCameraType(String str) {
        this.cameraType = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[name:");
        sb.append(this.name);
        sb.append(", path:");
        sb.append(this.path);
        sb.append(", createTime:");
        sb.append(this.createTime);
        sb.append(", file end time:");
        sb.append(this.fileEndTime);
        sb.append(", size:");
        sb.append(this.size);
        sb.append(", isVideo:");
        sb.append(this.isVideo);
        sb.append(", duration:");
        sb.append(this.duration);
        sb.append(", width:");
        sb.append(this.width);
        sb.append(", height:");
        sb.append(this.height);
        sb.append(", rate:");
        sb.append(this.rate);
        sb.append(", source:");
        sb.append(this.source);
        sb.append(", isSelected:");
        sb.append(this.isSelected);
        sb.append(", type:");
        sb.append(this.type);
        sb.append(", offset:");
        sb.append(this.offset);
        sb.append(", cameraType:");
        sb.append(this.cameraType);
        sb.append(", startTime:");
        Calendar calendar = this.startTime;
        sb.append(calendar != null ? calendar.toString() : null);
        sb.append(", endTime:");
        Calendar calendar2 = this.endTime;
        sb.append(calendar2 != null ? calendar2.toString() : null);
        sb.append("]");
        return sb.toString();
    }

    public Object clone() {
        FileInfo fileInfo;
        CloneNotSupportedException e;
        try {
            fileInfo = (FileInfo) super.clone();
            fileInfo.setStartTime((Calendar) this.startTime.clone());
            fileInfo.setEndTime((Calendar) this.endTime.clone());
        } catch (CloneNotSupportedException e3) {
            fileInfo = null;
            e = e3;
        }
        return fileInfo;
    }

    public String getFileEndTime() {
        return this.fileEndTime;
    }

    public void setFileEndTime(String str) {
        this.fileEndTime = str;
    }
}
