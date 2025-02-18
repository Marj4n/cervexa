package com.jieli.stream.dv.running2.data;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import com.generalplus.GoPlusDrone.Fragment.BitmapUtils;
import com.generalplus.GoPlusDrone.Fragment.ThreadUtils;
import com.jieli.media.codec.FrameCodec;
import com.jieli.media.codec.bean.MediaMeta;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import java.io.File;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class VideoCapture {
    private static volatile boolean isReady = true;
    private int height;
    private int width;
    private final String tag = getClass().getSimpleName();
    private FrameCodec mFrameCodec = null;
    private OnVideoCaptureListener mOnCaptureListener = null;
    private int mRetryTime = 0;
    private final FrameCodec.OnFrameCodecListener mOnFrameCodecListener = new FrameCodec.OnFrameCodecListener() { // from class: com.jieli.stream.dv.running2.data.VideoCapture.3
        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onCompleted(byte[] bArr, MediaMeta mediaMeta) {
            String str = AppUtils.splicingFilePath("MergeCamera", "Media", "Photo", "Screen") + File.separator + AppUtils.getLocalPhotoName();
            boolean bytesToFile = (bArr == null || TextUtils.isEmpty(str)) ? false : AppUtils.bytesToFile(bArr, str);
            Dbug.w(VideoCapture.this.tag, "result " + bytesToFile + ", outPath " + str);
        }

        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onError(String str) {
            Dbug.e(VideoCapture.this.tag, "Codec error:" + str);
            if (VideoCapture.this.mOnCaptureListener != null) {
                VideoCapture.this.mHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoCapture.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        VideoCapture.this.mOnCaptureListener.onFailed();
                    }
                });
            }
        }
    };
    private MyHandler mHandler = new MyHandler();

    private static class MyHandler extends Handler {
        private MyHandler() {
        }
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public void setOnCaptureListener(OnVideoCaptureListener onVideoCaptureListener) {
        this.mOnCaptureListener = onVideoCaptureListener;
    }

    public void capture(byte[] bArr) {
        if (bArr == null) {
            return;
        }
        if (MainApplication.getApplication().getDeviceDesc().getVideoType() == 0) {
            if (isReady) {
                isReady = false;
                String splicingFilePath = AppUtils.splicingFilePath("MergeCamera", "Media", "Photo", "Screen");
                String str = splicingFilePath + File.separator + AppUtils.getLocalPhotoName();
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                AppUtils.bytesToFile(bArr, str);
                saveFile(str, this.width, this.height, splicingFilePath + File.separator + AppUtils.getLocalPhotoName());
                return;
            }
            return;
        }
        if (AppUtils.checkFrameType(bArr) == 41377) {
            this.mRetryTime = 0;
            OnVideoCaptureListener onVideoCaptureListener = this.mOnCaptureListener;
            if (onVideoCaptureListener != null) {
                onVideoCaptureListener.onCompleted(null);
            }
            if (this.mFrameCodec == null) {
                FrameCodec frameCodec = new FrameCodec();
                this.mFrameCodec = frameCodec;
                frameCodec.setOnFrameCodecListener(this.mOnFrameCodecListener);
            }
            int[] rtsResolution = AppUtils.getRtsResolution(AppUtils.getStreamResolutionLevel());
            this.mFrameCodec.convertToJPG(bArr, rtsResolution[0], rtsResolution[1], null);
            return;
        }
        int i = this.mRetryTime + 1;
        this.mRetryTime = i;
        if (i <= 30 || this.mOnCaptureListener == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoCapture.1
            @Override // java.lang.Runnable
            public void run() {
                VideoCapture.this.mOnCaptureListener.onFailed();
            }
        });
        this.mRetryTime = 0;
    }

    public void capture(byte[] bArr, String str) {
        if (bArr == null) {
            return;
        }
        if (MainApplication.getApplication().getDeviceDesc().getVideoType() == 0) {
            if (isReady) {
                isReady = false;
                String str2 = AppUtils.splicingFilePath("MergeCamera", "Media", "Video", "thumbnails") + File.separator + str;
                if (TextUtils.isEmpty(str2)) {
                    return;
                }
                AppUtils.bytesToFile(bArr, str2);
                isReady = true;
                return;
            }
            return;
        }
        if (AppUtils.checkFrameType(bArr) == 41377) {
            this.mRetryTime = 0;
            OnVideoCaptureListener onVideoCaptureListener = this.mOnCaptureListener;
            if (onVideoCaptureListener != null) {
                onVideoCaptureListener.onCompleted(null);
            }
            if (this.mFrameCodec == null) {
                FrameCodec frameCodec = new FrameCodec();
                this.mFrameCodec = frameCodec;
                frameCodec.setOnFrameCodecListener(this.mOnFrameCodecListener);
            }
            int[] rtsResolution = AppUtils.getRtsResolution(AppUtils.getStreamResolutionLevel());
            this.mFrameCodec.convertToJPG(bArr, rtsResolution[0], rtsResolution[1], null);
            return;
        }
        int i = this.mRetryTime + 1;
        this.mRetryTime = i;
        if (i <= 30 || this.mOnCaptureListener == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoCapture.2
            @Override // java.lang.Runnable
            public void run() {
                VideoCapture.this.mOnCaptureListener.onFailed();
            }
        });
        this.mRetryTime = 0;
    }

    public void destroy() {
        FrameCodec frameCodec = this.mFrameCodec;
        if (frameCodec != null) {
            frameCodec.destroy();
            this.mFrameCodec.setOnFrameCodecListener(null);
            this.mFrameCodec = null;
        }
        this.mOnCaptureListener = null;
    }

    public void saveFile(final String str, final int i, final int i2, final String str2) {
        ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<Integer>() { // from class: com.jieli.stream.dv.running2.data.VideoCapture.4
            @Override // com.generalplus.GoPlusDrone.Fragment.ThreadUtils.SimpleTask, com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task
            public void onFail(Throwable th) {
            }

            @Override // com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task
            public Integer doInBackground() throws Throwable {
                int i3 = 0;
                do {
                    i3++;
                    if (!new File(str).exists()) {
                        Thread.sleep(200L);
                    } else {
                        Bitmap bitmapFromSDCard = BitmapUtils.getBitmapFromSDCard(str);
                        BitmapUtils.compressSize(bitmapFromSDCard, new File(str2), i, i2);
                        new File(str).delete();
                        bitmapFromSDCard.recycle();
                        return 1;
                    }
                } while (i3 < 10);
                return -1;
            }

            @Override // com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task
            public void onSuccess(Integer num) {
                if (num.intValue() == -1 || VideoCapture.this.mOnCaptureListener == null) {
                    return;
                }
                boolean unused = VideoCapture.isReady = true;
                VideoCapture.this.mOnCaptureListener.onCompleted(str2);
            }
        }, 1L, TimeUnit.SECONDS);
    }
}
