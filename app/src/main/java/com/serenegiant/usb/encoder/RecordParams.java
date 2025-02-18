package com.serenegiant.usb.encoder;

/* loaded from: classes2.dex */
public class RecordParams {
    private boolean isAutoSave;
    private boolean isSupportOverlay;
    private int recordDuration;
    private String recordPath;
    private boolean voiceClose;

    public boolean isSupportOverlay() {
        return this.isSupportOverlay;
    }

    public void setSupportOverlay(boolean z) {
        this.isSupportOverlay = z;
    }

    public boolean isVoiceClose() {
        return this.voiceClose;
    }

    public void setVoiceClose(boolean z) {
        this.voiceClose = z;
    }

    public String getRecordPath() {
        return this.recordPath;
    }

    public void setRecordPath(String str) {
        this.recordPath = str;
    }

    public int getRecordDuration() {
        return this.recordDuration;
    }

    public void setRecordDuration(int i) {
        this.recordDuration = i;
    }

    public boolean isAutoSave() {
        return this.isAutoSave;
    }

    public void setAutoSave(boolean z) {
        this.isAutoSave = z;
    }
}
