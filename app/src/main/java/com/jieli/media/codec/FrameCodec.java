package com.jieli.media.codec;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.jieli.media.codec.bean.MediaMeta;
import com.serenegiant.usb.UVCCamera;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FrameCodec extends AbstractCodec {
    private HashSet<OnFrameCodecListener> mOnFrameCodecListeners;
    String tag = getClass().getSimpleName();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public interface OnFrameCodecListener {
        void onCompleted(byte[] bArr, MediaMeta mediaMeta);

        void onError(String str);
    }

    private native boolean nativeConvert(byte[] bArr, int i, int i2, int i3, String str);

    private native boolean nativeConvertVideo(String str);

    private native int nativeGetDuration();

    private native boolean nativeInit();

    private native boolean nativeRelease();

    public FrameCodec() {
        AbstractCodec.loadLibrariesOnce(sLocalLibLoader);
        nativeInit();
        this.mOnFrameCodecListeners = new HashSet<>();
    }

    public boolean destroy() {
        HashSet<OnFrameCodecListener> hashSet = this.mOnFrameCodecListeners;
        if (hashSet != null) {
            hashSet.clear();
            this.mOnFrameCodecListeners = null;
        }
        this.mHandler.removeCallbacksAndMessages(null);
        return nativeRelease();
    }

    public void setOnFrameCodecListener(OnFrameCodecListener onFrameCodecListener) {
        HashSet<OnFrameCodecListener> hashSet = this.mOnFrameCodecListeners;
        if (hashSet == null || hashSet.contains(onFrameCodecListener)) {
            return;
        }
        this.mOnFrameCodecListeners.add(onFrameCodecListener);
    }

    protected void onCodecSuccess(final byte[] bArr, int i, int i2, int i3, String str) {
        Log.i(this.tag, "onCodecSuccess: path=" + str);
        HashSet<OnFrameCodecListener> hashSet = this.mOnFrameCodecListeners;
        if (hashSet == null || hashSet.size() <= 0) {
            Log.w(this.tag, "You haven't implemented OnFrameCodecListener yet.");
            return;
        }
        Log.w(this.tag, "mOnFrameCodecListeners size=" + this.mOnFrameCodecListeners.size());
        final MediaMeta mediaMeta = new MediaMeta();
        if (i >= 0) {
            mediaMeta.setWidth(i);
        }
        if (i2 >= 0) {
            mediaMeta.setHeight(i2);
        }
        if (i3 >= 0) {
            mediaMeta.setDuration(i3);
        }
        mediaMeta.setPath(str);
        Iterator<OnFrameCodecListener> it = this.mOnFrameCodecListeners.iterator();
        while (it.hasNext()) {
            final OnFrameCodecListener next = it.next();
            this.mHandler.post(new Runnable() { // from class: com.jieli.media.codec.FrameCodec.1
                @Override // java.lang.Runnable
                public void run() {
                    OnFrameCodecListener onFrameCodecListener = next;
                    if (onFrameCodecListener != null) {
                        onFrameCodecListener.onCompleted(bArr, mediaMeta);
                    } else {
                        Log.w(FrameCodec.this.tag, "OnFrameCodecListener is null.");
                    }
                }
            });
        }
    }

    protected void onCodecError(final String str) {
        Iterator<OnFrameCodecListener> it = this.mOnFrameCodecListeners.iterator();
        while (it.hasNext()) {
            final OnFrameCodecListener next = it.next();
            this.mHandler.post(new Runnable() { // from class: com.jieli.media.codec.FrameCodec.2
                @Override // java.lang.Runnable
                public void run() {
                    OnFrameCodecListener onFrameCodecListener = next;
                    if (onFrameCodecListener != null) {
                        onFrameCodecListener.onError(str);
                    }
                }
            });
        }
    }

    public boolean convertToJPG(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new NullPointerException("Param data is null");
        }
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Param width or height is illegal");
        }
        return nativeConvert(bArr, bArr.length, i, i2, null);
    }

    public boolean convertToJPG(byte[] bArr, int i, int i2, String str) {
        if (bArr == null) {
            throw new NullPointerException("Param data is null");
        }
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Param width or height is illegal");
        }
        return nativeConvert(bArr, bArr.length, i, i2, str);
    }

    public boolean convertToJPG(byte[] bArr) {
        return nativeConvert(bArr, bArr.length, UVCCamera.DEFAULT_PREVIEW_WIDTH, 480, null);
    }

    public boolean convertToJPG(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("path is null");
        }
        return nativeConvertVideo(str);
    }

    public int getDuration() {
        return nativeGetDuration();
    }
}
