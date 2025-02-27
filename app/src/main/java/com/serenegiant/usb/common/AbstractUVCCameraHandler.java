package com.serenegiant.usb.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.usb.UsbDevice;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.IFrameCallback;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.encoder.MediaEncoder;
import com.serenegiant.usb.encoder.MediaMuxerWrapper;
import com.serenegiant.usb.encoder.MediaSurfaceEncoder;
import com.serenegiant.usb.encoder.MediaVideoBufferEncoder;
import com.serenegiant.usb.encoder.MediaVideoEncoder;
import com.serenegiant.usb.encoder.RecordParams;
import com.serenegiant.usb.encoder.biz.AACEncodeConsumer;
import com.serenegiant.usb.encoder.biz.H264EncodeConsumer;
import com.serenegiant.usb.encoder.biz.Mp4MediaMuxer;
import com.serenegiant.usb.widget.CameraViewInterface;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.easydarwin.sw.TxtOverlay;

/* loaded from: classes2.dex */
public abstract class AbstractUVCCameraHandler extends Handler {
    private static final boolean DEBUG = true;
    private static final int MSG_CAMERA_FOUCS = 10;
    private static final int MSG_CAPTURE_START = 5;
    private static final int MSG_CAPTURE_STILL = 4;
    private static final int MSG_CAPTURE_STOP = 6;
    private static final int MSG_CLOSE = 1;
    private static final int MSG_MEDIA_UPDATE = 7;
    private static final int MSG_OPEN = 0;
    private static final int MSG_PREVIEW_START = 2;
    private static final int MSG_PREVIEW_STOP = 3;
    private static final int MSG_RELEASE = 9;
    private static final String TAG = "AbsUVCCameraHandler";
    public static IButtonCallback iButtonCallback;
    protected static boolean isCaptureStill;
    public static OnCaptureListener mCaptureListener;
    public static OnEncodeResultListener mListener;
    public static OnPreViewResultListener mPreviewListener;
    private volatile boolean mReleased;
    private final WeakReference<CameraThread> mWeakThread;

    public interface CameraCallback {
        void onClose();

        void onError(Exception exc);

        void onOpen();

        void onStartPreview();

        void onStartRecording();

        void onStopPreview();

        void onStopRecording();
    }

    public interface OnCaptureListener {
        void onCaptureResult(String str);
    }

    public interface OnEncodeResultListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j, int i3);

        void onRecordResult(String str);
    }

    public interface OnPreViewResultListener {
        void onPreviewResult(byte[] bArr);
    }

    protected AbstractUVCCameraHandler(CameraThread cameraThread) {
        this.mWeakThread = new WeakReference<>(cameraThread);
    }

    public int getWidth() {
        CameraThread cameraThread = this.mWeakThread.get();
        if (cameraThread != null) {
            return cameraThread.getWidth();
        }
        return 0;
    }

    public int getHeight() {
        CameraThread cameraThread = this.mWeakThread.get();
        if (cameraThread != null) {
            return cameraThread.getHeight();
        }
        return 0;
    }

    public boolean isOpened() {
        CameraThread cameraThread = this.mWeakThread.get();
        return cameraThread != null && cameraThread.isCameraOpened();
    }

    public boolean isPreviewing() {
        CameraThread cameraThread = this.mWeakThread.get();
        return cameraThread != null && cameraThread.isPreviewing();
    }

    public boolean isRecording() {
        CameraThread cameraThread = this.mWeakThread.get();
        return cameraThread != null && cameraThread.isRecording();
    }

    public boolean isEqual(UsbDevice usbDevice) {
        CameraThread cameraThread = this.mWeakThread.get();
        return cameraThread != null && cameraThread.isEqual(usbDevice);
    }

    protected boolean isCameraThread() {
        CameraThread cameraThread = this.mWeakThread.get();
        return cameraThread != null && cameraThread.getId() == Thread.currentThread().getId();
    }

    protected boolean isReleased() {
        return this.mReleased || this.mWeakThread.get() == null;
    }

    protected void checkReleased() {
        if (isReleased()) {
            throw new IllegalStateException("already released");
        }
    }

    public void open(USBMonitor.UsbControlBlock usbControlBlock) {
        checkReleased();
        sendMessage(obtainMessage(0, usbControlBlock));
    }

    public void close() {
        Log.v(TAG, "close:");
        if (isOpened()) {
            stopPreview();
            sendEmptyMessage(1);
        }
        Log.v(TAG, "close:finished");
    }

    public void resize(int i, int i2) {
        checkReleased();
        throw new UnsupportedOperationException("does not support now");
    }

    public void startPreview(Object obj) {
        checkReleased();
        if (!(obj instanceof SurfaceHolder) && !(obj instanceof Surface) && !(obj instanceof SurfaceTexture)) {
            throw new IllegalArgumentException("surface should be one of SurfaceHolder, Surface or SurfaceTexture: " + obj);
        }
        sendMessage(obtainMessage(2, obj));
    }

    public void setOnPreViewResultListener(OnPreViewResultListener onPreViewResultListener) {
        mPreviewListener = onPreViewResultListener;
    }

    public void setIButtonCallback(IButtonCallback iButtonCallback2) {
        iButtonCallback = iButtonCallback2;
    }

    public void stopPreview() {
        Log.v(TAG, "stopPreview:");
        removeMessages(2);
        if (isRecording()) {
            stopRecording();
        }
        if (isPreviewing()) {
            CameraThread cameraThread = this.mWeakThread.get();
            if (cameraThread == null) {
                return;
            }
            synchronized (cameraThread.mSync) {
                sendEmptyMessage(3);
                if (!isCameraThread()) {
                    try {
                        cameraThread.mSync.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
        Log.v(TAG, "stopPreview:finished");
    }

    public void captureStill(String str, OnCaptureListener onCaptureListener) {
        mCaptureListener = onCaptureListener;
        checkReleased();
        sendMessage(obtainMessage(4, str));
        isCaptureStill = true;
    }

    public void startRecording(RecordParams recordParams, OnEncodeResultListener onEncodeResultListener) {
        mListener = onEncodeResultListener;
        checkReleased();
        sendMessage(obtainMessage(5, recordParams));
    }

    public void stopRecording() {
        sendEmptyMessage(6);
    }

    public void startCameraFoucs() {
        sendEmptyMessage(10);
    }

    public List<Size> getSupportedPreviewSizes() {
        return this.mWeakThread.get().getSupportedSizes();
    }

    public void release() {
        this.mReleased = true;
        close();
        sendEmptyMessage(9);
    }

    public void addCallback(CameraCallback cameraCallback) {
        CameraThread cameraThread;
        checkReleased();
        if (this.mReleased || cameraCallback == null || (cameraThread = this.mWeakThread.get()) == null) {
            return;
        }
        cameraThread.mCallbacks.add(cameraCallback);
    }

    public void removeCallback(CameraCallback cameraCallback) {
        CameraThread cameraThread;
        if (cameraCallback == null || (cameraThread = this.mWeakThread.get()) == null) {
            return;
        }
        cameraThread.mCallbacks.remove(cameraCallback);
    }

    protected void updateMedia(String str) {
        sendMessage(obtainMessage(7, str));
    }

    public boolean checkSupportFlag(long j) {
        checkReleased();
        CameraThread cameraThread = this.mWeakThread.get();
        return (cameraThread == null || cameraThread.mUVCCamera == null || !cameraThread.mUVCCamera.checkSupportFlag(j)) ? false : true;
    }

    public int getValue(int i) {
        checkReleased();
        CameraThread cameraThread = this.mWeakThread.get();
        UVCCamera uVCCamera = cameraThread != null ? cameraThread.mUVCCamera : null;
        if (uVCCamera != null) {
            if (i == -2147483647) {
                return uVCCamera.getBrightness();
            }
            if (i == -2147483646) {
                return uVCCamera.getContrast();
            }
        }
        throw new IllegalStateException();
    }

    public int setValue(int i, int i2) {
        checkReleased();
        CameraThread cameraThread = this.mWeakThread.get();
        UVCCamera uVCCamera = cameraThread != null ? cameraThread.mUVCCamera : null;
        if (uVCCamera != null) {
            if (i == -2147483647) {
                uVCCamera.setBrightness(i2);
                return uVCCamera.getBrightness();
            }
            if (i == -2147483646) {
                uVCCamera.setContrast(i2);
                return uVCCamera.getContrast();
            }
        }
        throw new IllegalStateException();
    }

    public int resetValue(int i) {
        checkReleased();
        CameraThread cameraThread = this.mWeakThread.get();
        UVCCamera uVCCamera = cameraThread != null ? cameraThread.mUVCCamera : null;
        if (uVCCamera != null) {
            if (i == -2147483647) {
                uVCCamera.resetBrightness();
                return uVCCamera.getBrightness();
            }
            if (i == -2147483646) {
                uVCCamera.resetContrast();
                return uVCCamera.getContrast();
            }
        }
        throw new IllegalStateException();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        CameraThread cameraThread = this.mWeakThread.get();
        if (cameraThread == null) {
            return;
        }
        switch (message.what) {
            case 0:
                cameraThread.handleOpen((USBMonitor.UsbControlBlock) message.obj);
                return;
            case 1:
                cameraThread.handleClose();
                return;
            case 2:
                cameraThread.handleStartPreview(message.obj);
                return;
            case 3:
                cameraThread.handleStopPreview();
                return;
            case 4:
                cameraThread.handleStillPicture((String) message.obj);
                return;
            case 5:
                cameraThread.handleStartPusher((RecordParams) message.obj);
                return;
            case 6:
                cameraThread.handleStopPusher();
                return;
            case 7:
                cameraThread.handleUpdateMedia((String) message.obj);
                return;
            case 8:
            default:
                throw new RuntimeException("unsupported message:what=" + message.what);
            case 9:
                cameraThread.handleRelease();
                return;
            case 10:
                cameraThread.handleCameraFoucs();
                return;
        }
    }

    public static final class CameraThread extends Thread {
        private static final String TAG_THREAD = "CameraThread";
        private boolean isPushing;
        private boolean isSupportOverlay;
        private AACEncodeConsumer mAacConsumer;
        private float mBandwidthFactor;
        private final Set<CameraCallback> mCallbacks;
        private final int mEncoderType;
        private H264EncodeConsumer mH264Consumer;
        private AbstractUVCCameraHandler mHandler;
        private final Class<? extends AbstractUVCCameraHandler> mHandlerClass;
        private int mHeight;
        private final IFrameCallback mIFrameCallback;
        private boolean mIsPreviewing;
        private boolean mIsRecording;
        private final MediaEncoder.MediaEncoderListener mMediaEncoderListener;
        private Mp4MediaMuxer mMuxer;
        private int mPreviewMode;
        private final Object mSync;
        private UVCCamera mUVCCamera;
        private MediaVideoBufferEncoder mVideoEncoder;
        private final WeakReference<CameraViewInterface> mWeakCameraView;
        private final WeakReference<Activity> mWeakParent;
        private int mWidth;
        private String picPath;
        private String videoPath;

        CameraThread(Class<? extends AbstractUVCCameraHandler> cls, Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3, int i4, float f) {
            super(TAG_THREAD);
            this.mSync = new Object();
            this.mCallbacks = new CopyOnWriteArraySet();
            this.picPath = null;
            this.mIFrameCallback = new IFrameCallback() { // from class: com.serenegiant.usb.common.AbstractUVCCameraHandler.CameraThread.3
                @Override // com.serenegiant.usb.IFrameCallback
                public void onFrame(ByteBuffer byteBuffer) {
                    final byte[] bArr = new byte[byteBuffer.capacity()];
                    byteBuffer.get(bArr);
                    if (AbstractUVCCameraHandler.mPreviewListener != null) {
                        AbstractUVCCameraHandler.mPreviewListener.onPreviewResult(bArr);
                    }
                    if (AbstractUVCCameraHandler.isCaptureStill && !TextUtils.isEmpty(CameraThread.this.picPath)) {
                        AbstractUVCCameraHandler.isCaptureStill = false;
                        new Thread(new Runnable() { // from class: com.serenegiant.usb.common.AbstractUVCCameraHandler.CameraThread.3.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CameraThread.this.saveYuv2Jpeg(CameraThread.this.picPath, bArr);
                            }
                        }).start();
                    }
                    if (CameraThread.this.mH264Consumer != null) {
                        if (CameraThread.this.isSupportOverlay) {
                            TxtOverlay.getInstance().overlay(bArr, new SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss").format(new Date()));
                        }
                        CameraThread.this.mH264Consumer.setRawYuv(bArr, CameraThread.this.mWidth, CameraThread.this.mHeight);
                    }
                }
            };
            this.mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() { // from class: com.serenegiant.usb.common.AbstractUVCCameraHandler.CameraThread.4
                @Override // com.serenegiant.usb.encoder.MediaEncoder.MediaEncoderListener
                public void onPrepared(MediaEncoder mediaEncoder) {
                    Log.v(AbstractUVCCameraHandler.TAG, "onPrepared:encoder=" + mediaEncoder);
                    CameraThread.this.mIsRecording = true;
                    if (mediaEncoder instanceof MediaVideoEncoder) {
                        try {
                            ((CameraViewInterface) CameraThread.this.mWeakCameraView.get()).setVideoEncoder((MediaVideoEncoder) mediaEncoder);
                        } catch (Exception e) {
                            Log.e(AbstractUVCCameraHandler.TAG, "onPrepared:", e);
                        }
                    }
                    if (mediaEncoder instanceof MediaSurfaceEncoder) {
                        try {
                            ((CameraViewInterface) CameraThread.this.mWeakCameraView.get()).setVideoEncoder((MediaSurfaceEncoder) mediaEncoder);
                            CameraThread.this.mUVCCamera.startCapture(((MediaSurfaceEncoder) mediaEncoder).getInputSurface());
                        } catch (Exception e2) {
                            Log.e(AbstractUVCCameraHandler.TAG, "onPrepared:", e2);
                        }
                    }
                }

                @Override // com.serenegiant.usb.encoder.MediaEncoder.MediaEncoderListener
                public void onStopped(MediaEncoder mediaEncoder) {
                    Log.v(CameraThread.TAG_THREAD, "onStopped:encoder=" + mediaEncoder);
                    if ((mediaEncoder instanceof MediaVideoEncoder) || (mediaEncoder instanceof MediaSurfaceEncoder)) {
                        try {
                            CameraThread.this.mIsRecording = false;
                            Activity activity2 = (Activity) CameraThread.this.mWeakParent.get();
                            ((CameraViewInterface) CameraThread.this.mWeakCameraView.get()).setVideoEncoder(null);
                            synchronized (CameraThread.this.mSync) {
                                if (CameraThread.this.mUVCCamera != null) {
                                    CameraThread.this.mUVCCamera.stopCapture();
                                }
                            }
                            String outputPath = mediaEncoder.getOutputPath();
                            if (!TextUtils.isEmpty(outputPath)) {
                                CameraThread.this.mHandler.sendMessageDelayed(CameraThread.this.mHandler.obtainMessage(7, outputPath), 1000L);
                                return;
                            }
                            if ((CameraThread.this.mHandler == null || CameraThread.this.mHandler.mReleased) || activity2 == null || activity2.isDestroyed()) {
                                CameraThread.this.handleRelease();
                            }
                        } catch (Exception e) {
                            Log.e(AbstractUVCCameraHandler.TAG, "onPrepared:", e);
                        }
                    }
                }

                @Override // com.serenegiant.usb.encoder.MediaEncoder.MediaEncoderListener
                public void onEncodeResult(byte[] bArr, int i5, int i6, long j, int i7) {
                    if (AbstractUVCCameraHandler.mListener != null) {
                        AbstractUVCCameraHandler.mListener.onEncodeResult(bArr, i5, i6, j, i7);
                    }
                }
            };
            this.mHandlerClass = cls;
            this.mEncoderType = i;
            this.mWidth = i2;
            this.mHeight = i3;
            this.mPreviewMode = i4;
            this.mBandwidthFactor = f;
            this.mWeakParent = new WeakReference<>(activity);
            this.mWeakCameraView = new WeakReference<>(cameraViewInterface);
        }

        protected void finalize() throws Throwable {
            Log.i(AbstractUVCCameraHandler.TAG, "CameraThread#finalize");
            super.finalize();
        }

        public AbstractUVCCameraHandler getHandler() {
            Log.v(TAG_THREAD, "getHandler:");
            synchronized (this.mSync) {
                if (this.mHandler == null) {
                    try {
                        this.mSync.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            return this.mHandler;
        }

        public int getWidth() {
            int i;
            synchronized (this.mSync) {
                i = this.mWidth;
            }
            return i;
        }

        public int getHeight() {
            int i;
            synchronized (this.mSync) {
                i = this.mHeight;
            }
            return i;
        }

        public boolean isCameraOpened() {
            boolean z;
            synchronized (this.mSync) {
                z = this.mUVCCamera != null;
            }
            return z;
        }

        public boolean isPreviewing() {
            boolean z;
            synchronized (this.mSync) {
                z = this.mUVCCamera != null && this.mIsPreviewing;
            }
            return z;
        }

        public boolean isRecording() {
            boolean z;
            synchronized (this.mSync) {
                z = (this.mUVCCamera == null || this.mH264Consumer == null) ? false : true;
            }
            return z;
        }

        public boolean isEqual(UsbDevice usbDevice) {
            UVCCamera uVCCamera = this.mUVCCamera;
            return (uVCCamera == null || uVCCamera.getDevice() == null || !this.mUVCCamera.getDevice().equals(usbDevice)) ? false : true;
        }

        public void handleOpen(USBMonitor.UsbControlBlock usbControlBlock) {
            Log.v(TAG_THREAD, "handleOpen:");
            handleClose();
            try {
                UVCCamera uVCCamera = new UVCCamera();
                uVCCamera.open(usbControlBlock);
                synchronized (this.mSync) {
                    this.mUVCCamera = uVCCamera;
                }
                callOnOpen();
            } catch (Exception e) {
                callOnError(e);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("supportedSize:");
            UVCCamera uVCCamera2 = this.mUVCCamera;
            sb.append(uVCCamera2 != null ? uVCCamera2.getSupportedSize() : null);
            Log.i(AbstractUVCCameraHandler.TAG, sb.toString());
        }

        public void handleClose() {
            UVCCamera uVCCamera;
            Log.v(TAG_THREAD, "handleClose:");
            handleStopPusher();
            synchronized (this.mSync) {
                uVCCamera = this.mUVCCamera;
                this.mUVCCamera = null;
            }
            if (uVCCamera != null) {
                uVCCamera.stopPreview();
                uVCCamera.destroy();
                callOnClose();
            }
        }

        public void handleStartPreview(Object obj) {
            Log.v(TAG_THREAD, "handleStartPreview:");
            UVCCamera uVCCamera = this.mUVCCamera;
            if (uVCCamera == null || this.mIsPreviewing) {
                return;
            }
            try {
                try {
                    uVCCamera.setPreviewSize(this.mWidth, this.mHeight, 1, 31, this.mPreviewMode, this.mBandwidthFactor);
                    this.mUVCCamera.setFrameCallback(this.mIFrameCallback, 4);
                    if (AbstractUVCCameraHandler.iButtonCallback != null) {
                        this.mUVCCamera.setButtonCallback(AbstractUVCCameraHandler.iButtonCallback);
                    }
                } catch (IllegalArgumentException unused) {
                    this.mUVCCamera.setPreviewSize(this.mWidth, this.mHeight, 1, 31, 0, this.mBandwidthFactor);
                    this.mUVCCamera.setFrameCallback(this.mIFrameCallback, 4);
                    if (AbstractUVCCameraHandler.iButtonCallback != null) {
                        this.mUVCCamera.setButtonCallback(AbstractUVCCameraHandler.iButtonCallback);
                    }
                }
                if (obj instanceof SurfaceHolder) {
                    this.mUVCCamera.setPreviewDisplay((SurfaceHolder) obj);
                }
                if (obj instanceof Surface) {
                    this.mUVCCamera.setPreviewDisplay((Surface) obj);
                } else {
                    this.mUVCCamera.setPreviewTexture((SurfaceTexture) obj);
                }
                this.mUVCCamera.startPreview();
                this.mUVCCamera.updateCameraParams();
                synchronized (this.mSync) {
                    this.mIsPreviewing = true;
                }
                callOnStartPreview();
            } catch (IllegalArgumentException e) {
                callOnError(e);
            }
        }

        public void handleStopPreview() {
            Log.v(TAG_THREAD, "handleStopPreview:");
            if (this.mIsPreviewing) {
                UVCCamera uVCCamera = this.mUVCCamera;
                if (uVCCamera != null) {
                    uVCCamera.stopPreview();
                    this.mUVCCamera.setFrameCallback(null, 0);
                }
                synchronized (this.mSync) {
                    this.mIsPreviewing = false;
                    this.mSync.notifyAll();
                }
                callOnStopPreview();
            }
            Log.v(TAG_THREAD, "handleStopPreview:finished");
        }

        public void handleCaptureStill(String str) {
            Log.v(TAG_THREAD, "handleCaptureStill:");
            if (this.mWeakParent.get() == null) {
                return;
            }
            try {
                Bitmap captureStillImage = this.mWeakCameraView.get().captureStillImage(this.mWidth, this.mHeight);
                File captureFile = TextUtils.isEmpty(str) ? MediaMuxerWrapper.getCaptureFile(Environment.DIRECTORY_DCIM, UVCCameraHelper.SUFFIX_JPEG) : new File(str);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(captureFile));
                try {
                    captureStillImage.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                    bufferedOutputStream.flush();
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(7, captureFile.getPath()));
                } catch (IOException unused) {
                } catch (Throwable th) {
                    bufferedOutputStream.close();
                    throw th;
                }
                bufferedOutputStream.close();
                if (AbstractUVCCameraHandler.mCaptureListener != null) {
                    AbstractUVCCameraHandler.mCaptureListener.onCaptureResult(str);
                }
            } catch (Exception e) {
                callOnError(e);
            }
        }

        public void handleStartPusher(RecordParams recordParams) {
            if (this.mUVCCamera == null || this.mH264Consumer != null) {
                return;
            }
            if (recordParams != null) {
                boolean isSupportOverlay = recordParams.isSupportOverlay();
                this.isSupportOverlay = isSupportOverlay;
                if (isSupportOverlay) {
                    TxtOverlay.getInstance().init(this.mWidth, this.mHeight);
                }
                this.videoPath = recordParams.getRecordPath();
                File file = new File(this.videoPath);
                if (!((File) Objects.requireNonNull(file.getParentFile())).exists()) {
                    file.getParentFile().mkdirs();
                }
                this.mMuxer = new Mp4MediaMuxer(recordParams.getRecordPath(), recordParams.getRecordDuration() * 60 * 1000, recordParams.isVoiceClose());
            }
            startVideoRecord();
            if (recordParams != null && !recordParams.isVoiceClose()) {
                startAudioRecord();
            }
            callOnStartRecording();
        }

        public void handleStopPusher() {
            Mp4MediaMuxer mp4MediaMuxer = this.mMuxer;
            if (mp4MediaMuxer != null) {
                mp4MediaMuxer.release();
                this.mMuxer = null;
                Log.i(AbstractUVCCameraHandler.TAG, "AbsUVCCameraHandler---->停止本地录制");
            }
            stopAudioRecord();
            stopVideoRecord();
            if (this.isSupportOverlay) {
                TxtOverlay.getInstance().release();
            }
            this.mWeakCameraView.get().setVideoEncoder(null);
            callOnStopRecording();
            if (AbstractUVCCameraHandler.mListener == null || TextUtils.isEmpty(this.videoPath)) {
                return;
            }
            AbstractUVCCameraHandler.mListener.onRecordResult(this.videoPath + UVCCameraHelper.SUFFIX_MP4);
        }

        private void startVideoRecord() {
            H264EncodeConsumer h264EncodeConsumer;
            H264EncodeConsumer h264EncodeConsumer2 = new H264EncodeConsumer(getWidth(), getHeight());
            this.mH264Consumer = h264EncodeConsumer2;
            h264EncodeConsumer2.setOnH264EncodeResultListener(new H264EncodeConsumer.OnH264EncodeResultListener() { // from class: com.serenegiant.usb.common.AbstractUVCCameraHandler.CameraThread.1
                @Override // com.serenegiant.usb.encoder.biz.H264EncodeConsumer.OnH264EncodeResultListener
                public void onEncodeResult(byte[] bArr, int i, int i2, long j) {
                    if (AbstractUVCCameraHandler.mListener != null) {
                        AbstractUVCCameraHandler.mListener.onEncodeResult(bArr, i, i2, j, 1);
                    }
                }
            });
            this.mH264Consumer.start();
            Mp4MediaMuxer mp4MediaMuxer = this.mMuxer;
            if (mp4MediaMuxer == null || (h264EncodeConsumer = this.mH264Consumer) == null) {
                return;
            }
            h264EncodeConsumer.setTmpuMuxer(mp4MediaMuxer);
        }

        private void stopVideoRecord() {
            H264EncodeConsumer h264EncodeConsumer = this.mH264Consumer;
            if (h264EncodeConsumer != null) {
                h264EncodeConsumer.exit();
                this.mH264Consumer.setTmpuMuxer(null);
                try {
                    H264EncodeConsumer h264EncodeConsumer2 = this.mH264Consumer;
                    this.mH264Consumer = null;
                    if (h264EncodeConsumer2 != null) {
                        h264EncodeConsumer2.interrupt();
                        h264EncodeConsumer2.join();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void startAudioRecord() {
            AACEncodeConsumer aACEncodeConsumer;
            AACEncodeConsumer aACEncodeConsumer2 = new AACEncodeConsumer();
            this.mAacConsumer = aACEncodeConsumer2;
            aACEncodeConsumer2.setOnAACEncodeResultListener(new AACEncodeConsumer.OnAACEncodeResultListener() { // from class: com.serenegiant.usb.common.AbstractUVCCameraHandler.CameraThread.2
                @Override // com.serenegiant.usb.encoder.biz.AACEncodeConsumer.OnAACEncodeResultListener
                public void onEncodeResult(byte[] bArr, int i, int i2, long j) {
                    if (AbstractUVCCameraHandler.mListener != null) {
                        AbstractUVCCameraHandler.mListener.onEncodeResult(bArr, i, i2, j, 0);
                    }
                }
            });
            this.mAacConsumer.start();
            Mp4MediaMuxer mp4MediaMuxer = this.mMuxer;
            if (mp4MediaMuxer == null || (aACEncodeConsumer = this.mAacConsumer) == null) {
                return;
            }
            aACEncodeConsumer.setTmpuMuxer(mp4MediaMuxer);
        }

        private void stopAudioRecord() {
            AACEncodeConsumer aACEncodeConsumer = this.mAacConsumer;
            if (aACEncodeConsumer != null) {
                aACEncodeConsumer.exit();
                this.mAacConsumer.setTmpuMuxer(null);
                try {
                    AACEncodeConsumer aACEncodeConsumer2 = this.mAacConsumer;
                    this.mAacConsumer = null;
                    if (aACEncodeConsumer2 != null) {
                        aACEncodeConsumer2.interrupt();
                        aACEncodeConsumer2.join();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void handleStillPicture(String str) {
            this.picPath = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void saveYuv2Jpeg(String str, byte[] bArr) {
            YuvImage yuvImage = new YuvImage(bArr, 17, this.mWidth, this.mHeight, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
            if (yuvImage.compressToJpeg(new Rect(0, 0, this.mWidth, this.mHeight), 100, byteArrayOutputStream)) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
                    fileOutputStream.write(byteArray);
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (AbstractUVCCameraHandler.mCaptureListener != null) {
                    AbstractUVCCameraHandler.mCaptureListener.onCaptureResult(str);
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }

        public void handleUpdateMedia(String str) {
            Log.v(TAG_THREAD, "handleUpdateMedia:path=" + str);
            Activity activity = this.mWeakParent.get();
            AbstractUVCCameraHandler abstractUVCCameraHandler = this.mHandler;
            boolean z = abstractUVCCameraHandler == null || abstractUVCCameraHandler.mReleased;
            if (activity != null && activity.getApplicationContext() != null) {
                try {
                    Log.i(AbstractUVCCameraHandler.TAG, "MediaScannerConnection#scanFile");
                    MediaScannerConnection.scanFile(activity.getApplicationContext(), new String[]{str}, null, null);
                } catch (Exception e) {
                    Log.e(AbstractUVCCameraHandler.TAG, "handleUpdateMedia:", e);
                }
                if (z || activity.isDestroyed()) {
                    handleRelease();
                    return;
                }
                return;
            }
            Log.w(AbstractUVCCameraHandler.TAG, "MainActivity already destroyed");
            handleRelease();
        }

        public void handleRelease() {
            Log.v(TAG_THREAD, "handleRelease:mIsRecording=" + this.mIsRecording);
            handleClose();
            this.mCallbacks.clear();
            if (!this.mIsRecording) {
                this.mHandler.mReleased = true;
                Looper.myLooper().quit();
            }
            Log.v(TAG_THREAD, "handleRelease:finished");
        }

        public void handleCameraFoucs() {
            Log.v(TAG_THREAD, "handleStartPreview:");
            UVCCamera uVCCamera = this.mUVCCamera;
            if (uVCCamera == null || !this.mIsPreviewing) {
                return;
            }
            uVCCamera.setAutoFocus(true);
        }

        public List<Size> getSupportedSizes() {
            UVCCamera uVCCamera = this.mUVCCamera;
            if (uVCCamera == null || !this.mIsPreviewing) {
                return null;
            }
            return uVCCamera.getSupportedSizeList();
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x005c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:5:0x003b  */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            Looper.prepare();
            AbstractUVCCameraHandler handler = null;
            try {
                final Constructor<? extends AbstractUVCCameraHandler> constructor = mHandlerClass.getDeclaredConstructor(CameraThread.class);
                handler = constructor.newInstance(this);
            } catch (final NoSuchMethodException e) {
                Log.w(TAG, e);
            } catch (final IllegalAccessException e) {
                Log.w(TAG, e);
            } catch (final InstantiationException e) {
                Log.w(TAG, e);
            } catch (final InvocationTargetException e) {
                Log.w(TAG, e);
            }
            if (handler != null) {
                synchronized (mSync) {
                    mHandler = handler;
                    mSync.notifyAll();
                }
                Looper.loop();
//				if (mSoundPool != null) {
//					mSoundPool.release();
//					mSoundPool = null;
//				}
                if (mHandler != null) {
                    mHandler.mReleased = true;
                }
            }
            mCallbacks.clear();
            synchronized (mSync) {
                mHandler = null;
                mSync.notifyAll();
            }
        }

        private void callOnOpen() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onOpen();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnClose() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onClose();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnStartPreview() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onStartPreview();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnStopPreview() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onStopPreview();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnStartRecording() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onStartRecording();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnStopRecording() {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onStopRecording();
                } catch (Exception e) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, e);
                }
            }
        }

        private void callOnError(Exception exc) {
            for (CameraCallback cameraCallback : this.mCallbacks) {
                try {
                    cameraCallback.onError(exc);
                } catch (Exception unused) {
                    this.mCallbacks.remove(cameraCallback);
                    Log.w(AbstractUVCCameraHandler.TAG, exc);
                }
            }
        }
    }
}
