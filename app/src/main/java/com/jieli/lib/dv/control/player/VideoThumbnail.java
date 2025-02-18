package com.jieli.lib.dv.control.player;

import com.jieli.lib.dv.control.model.PictureInfo;
import java.util.Calendar;

/* loaded from: classes.dex */
public class VideoThumbnail {
    private long mNativeContext;
    String tag = getClass().getSimpleName();
    private OnFrameListener mOnFrameListener = null;
    private OnCompletedListener mOnCompletedListener = null;
    private PictureInfo mMediaInfo = null;

    private native boolean nativeCloseClient();

    private native boolean nativeCreateClient(int i, String str);

    private native boolean nativeInit();

    private native boolean nativeIsReceiving();

    private native void nativeRelease();

    private void onError(int i, String str) {
    }

    public VideoThumbnail() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        nativeInit();
    }

    public boolean isReceiving() {
        return nativeIsReceiving();
    }

    public boolean create(int i, String str) {
        this.mMediaInfo = new PictureInfo();
        return nativeCreateClient(i, str);
    }

    public boolean close() {
        boolean nativeCloseClient;
        synchronized (this) {
            this.mMediaInfo = null;
            nativeCloseClient = nativeCloseClient();
        }
        return nativeCloseClient;
    }

    public boolean release() {
        synchronized (this) {
            this.mOnFrameListener = null;
            this.mOnCompletedListener = null;
            nativeRelease();
            this.mNativeContext = 0L;
        }
        return true;
    }

    protected void finalize() {
        this.mOnFrameListener = null;
        this.mOnCompletedListener = null;
        this.mMediaInfo = null;
        nativeRelease();
        this.mNativeContext = 0L;
    }

    public void setOnFrameListener(OnFrameListener onFrameListener) {
        this.mOnFrameListener = onFrameListener;
    }

    protected void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.mOnCompletedListener = onCompletedListener;
    }

    protected void onFrameReceived(int i, byte[] bArr) {
        OnFrameListener onFrameListener = this.mOnFrameListener;
        PictureInfo pictureInfo = this.mMediaInfo;
        if (onFrameListener == null || pictureInfo == null) {
            return;
        }
        onFrameListener.onFrame(bArr, pictureInfo);
    }

    protected void onMediaMetaUpdate(int i, int i2, int i3, int i4, int i5, String str) {
        if (this.mMediaInfo == null) {
            this.mMediaInfo = new PictureInfo();
        }
        this.mMediaInfo.setWidth(i);
        this.mMediaInfo.setHeight(i2);
        this.mMediaInfo.setPath(str);
        this.mMediaInfo.setFrameRate(i3);
        this.mMediaInfo.setSampleRate(i4);
        this.mMediaInfo.setDuration(i5);
    }

    protected void onTimeUpdate(int i, int i2, int i3, int i4, int i5, int i6) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i + 2015, i2 - 1, i3, i4, i5, i6);
        PictureInfo pictureInfo = this.mMediaInfo;
        if (pictureInfo != null) {
            pictureInfo.setTime(calendar.getTimeInMillis());
        }
    }

    protected void onThumbnailEnd() {
        PictureInfo pictureInfo = this.mMediaInfo;
        if (pictureInfo != null) {
            pictureInfo.setLast(true);
        }
    }
}
