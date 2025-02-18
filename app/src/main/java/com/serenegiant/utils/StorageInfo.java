package com.serenegiant.utils;

/* loaded from: classes2.dex */
public class StorageInfo {
    public long freeBytes;
    public long totalBytes;

    public StorageInfo(long j, long j2) {
        this.totalBytes = j;
        this.freeBytes = j2;
    }

    public String toString() {
        return "StorageInfo{totalBytes=" + this.totalBytes + ", freeBytes=" + this.freeBytes + '}';
    }
}
