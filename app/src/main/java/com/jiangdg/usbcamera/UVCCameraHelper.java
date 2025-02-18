package com.jiangdg.usbcamera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Environment;
import com.jiangdg.libusbcamera.R;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.common.UVCCameraHandler;
import com.serenegiant.usb.encoder.RecordParams;
import com.serenegiant.usb.widget.CameraViewInterface;
import java.io.File;
import java.util.List;
import java.util.Objects;
import org.easydarwin.sw.TxtOverlay;

/* loaded from: classes.dex */
public class UVCCameraHelper {
    public static final int FRAME_FORMAT_MJPEG = 1;
    public static final int FRAME_FORMAT_YUYV = 0;
    public static final int MODE_BRIGHTNESS = -2147483647;
    public static final int MODE_CONTRAST = -2147483646;
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    public static final String SUFFIX_JPEG = ".jpg";
    public static final String SUFFIX_MP4 = ".mp4";
    private static final String TAG = "UVCCameraHelper";
    private static UVCCameraHelper mCameraHelper;
    private Activity mActivity;
    private CameraViewInterface mCamView;
    private UVCCameraHandler mCameraHandler;
    private USBMonitor.UsbControlBlock mCtrlBlock;
    private USBMonitor mUSBMonitor;
    private int previewWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
    private int previewHeight = 480;
    private int mFrameFormat = 1;

    public interface OnMyDevConnectListener {
        void onAttachDev(UsbDevice usbDevice);

        void onConnectDev(UsbDevice usbDevice, boolean z);

        void onDettachDev(UsbDevice usbDevice);

        void onDisConnectDev(UsbDevice usbDevice);
    }

    private UVCCameraHelper() {
    }

    public static UVCCameraHelper getInstance() {
        if (mCameraHelper == null) {
            mCameraHelper = new UVCCameraHelper();
        }
        return mCameraHelper;
    }

    public void closeCamera() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.close();
        }
    }

    public void initUSBMonitor(Activity activity, CameraViewInterface cameraViewInterface, final OnMyDevConnectListener onMyDevConnectListener) {
        this.mActivity = activity;
        this.mCamView = cameraViewInterface;
        this.mUSBMonitor = new USBMonitor(activity.getApplicationContext(), new USBMonitor.OnDeviceConnectListener() { // from class: com.jiangdg.usbcamera.UVCCameraHelper.1
            @Override // com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
            public void onCancel(UsbDevice usbDevice) {
            }

            @Override // com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
            public void onAttach(UsbDevice usbDevice) {
                OnMyDevConnectListener onMyDevConnectListener2 = onMyDevConnectListener;
                if (onMyDevConnectListener2 != null) {
                    onMyDevConnectListener2.onAttachDev(usbDevice);
                }
            }

            @Override // com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
            public void onDettach(UsbDevice usbDevice) {
                OnMyDevConnectListener onMyDevConnectListener2 = onMyDevConnectListener;
                if (onMyDevConnectListener2 != null) {
                    onMyDevConnectListener2.onDettachDev(usbDevice);
                }
            }

            @Override // com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
            public void onConnect(UsbDevice usbDevice, USBMonitor.UsbControlBlock usbControlBlock, boolean z) {
                UVCCameraHelper.this.mCtrlBlock = usbControlBlock;
                UVCCameraHelper.this.openCamera(usbControlBlock);
                new Thread(new Runnable() { // from class: com.jiangdg.usbcamera.UVCCameraHelper.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        UVCCameraHelper.this.startPreview(UVCCameraHelper.this.mCamView);
                    }
                }).start();
                OnMyDevConnectListener onMyDevConnectListener2 = onMyDevConnectListener;
                if (onMyDevConnectListener2 != null) {
                    onMyDevConnectListener2.onConnectDev(usbDevice, true);
                }
            }

            @Override // com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
            public void onDisconnect(UsbDevice usbDevice, USBMonitor.UsbControlBlock usbControlBlock) {
                OnMyDevConnectListener onMyDevConnectListener2 = onMyDevConnectListener;
                if (onMyDevConnectListener2 != null) {
                    onMyDevConnectListener2.onDisConnectDev(usbDevice);
                }
            }
        });
        createUVCCamera();
    }

    public void createUVCCamera() {
        if (this.mCamView == null) {
            throw new NullPointerException("CameraViewInterface cannot be null!");
        }
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.release();
            this.mCameraHandler = null;
        }
        this.mCamView.setAspectRatio(this.previewWidth / this.previewHeight);
        this.mCameraHandler = UVCCameraHandler.createHandler(this.mActivity, this.mCamView, 2, this.previewWidth, this.previewHeight, this.mFrameFormat);
    }

    public void updateResolution(int i, int i2) {
        if (this.previewWidth == i && this.previewHeight == i2) {
            return;
        }
        this.previewWidth = i;
        this.previewHeight = i2;
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.release();
            this.mCameraHandler = null;
        }
        this.mCamView.setAspectRatio(this.previewWidth / this.previewHeight);
        this.mCameraHandler = UVCCameraHandler.createHandler(this.mActivity, this.mCamView, 2, this.previewWidth, this.previewHeight, this.mFrameFormat);
        openCamera(this.mCtrlBlock);
        new Thread(new Runnable() { // from class: com.jiangdg.usbcamera.UVCCameraHelper.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UVCCameraHelper uVCCameraHelper = UVCCameraHelper.this;
                uVCCameraHelper.startPreview(uVCCameraHelper.mCamView);
            }
        }).start();
    }

    public void registerUSB() {
        USBMonitor uSBMonitor = this.mUSBMonitor;
        if (uSBMonitor != null) {
            uSBMonitor.register();
        }
    }

    public void unregisterUSB() {
        USBMonitor uSBMonitor = this.mUSBMonitor;
        if (uSBMonitor != null) {
            uSBMonitor.unregister();
        }
    }

    public boolean checkSupportFlag(int i) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        return uVCCameraHandler != null && uVCCameraHandler.checkSupportFlag((long) i);
    }

    public int getModelValue(int i) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            return uVCCameraHandler.getValue(i);
        }
        return 0;
    }

    public int setModelValue(int i, int i2) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            return uVCCameraHandler.setValue(i, i2);
        }
        return 0;
    }

    public int resetModelValue(int i) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            return uVCCameraHandler.resetValue(i);
        }
        return 0;
    }

    public void requestPermission(int i) {
        List<UsbDevice> usbDeviceList = getUsbDeviceList();
        if (usbDeviceList == null || usbDeviceList.size() == 0) {
            return;
        }
        if (i >= usbDeviceList.size()) {
            new IllegalArgumentException("index illegal,should be < devList.size()");
        }
        USBMonitor uSBMonitor = this.mUSBMonitor;
        if (uSBMonitor != null) {
            uSBMonitor.requestPermission(getUsbDeviceList().get(i));
        }
    }

    public int getUsbDeviceCount() {
        List<UsbDevice> usbDeviceList = getUsbDeviceList();
        if (usbDeviceList == null || usbDeviceList.size() == 0) {
            return 0;
        }
        return usbDeviceList.size();
    }

    public List<UsbDevice> getUsbDeviceList() {
        List<DeviceFilter> deviceFilters = DeviceFilter.getDeviceFilters(this.mActivity.getApplicationContext(), R.xml.device_filter);
        USBMonitor uSBMonitor = this.mUSBMonitor;
        if (uSBMonitor == null || deviceFilters == null) {
            return null;
        }
        return uSBMonitor.getDeviceList(deviceFilters);
    }

    public void capturePicture(String str, AbstractUVCCameraHandler.OnCaptureListener onCaptureListener) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler == null || !uVCCameraHandler.isOpened()) {
            return;
        }
        File file = new File(str);
        if (!((File) Objects.requireNonNull(file.getParentFile())).exists()) {
            file.getParentFile().mkdirs();
        }
        this.mCameraHandler.captureStill(str, onCaptureListener);
    }

    public void startPusher(AbstractUVCCameraHandler.OnEncodeResultListener onEncodeResultListener) {
        if (this.mCameraHandler == null || isPushing()) {
            return;
        }
        this.mCameraHandler.startRecording(null, onEncodeResultListener);
    }

    public void startPusher(RecordParams recordParams, AbstractUVCCameraHandler.OnEncodeResultListener onEncodeResultListener) {
        if (this.mCameraHandler == null || isPushing()) {
            return;
        }
        if (recordParams.isSupportOverlay()) {
            TxtOverlay.install(this.mActivity.getApplicationContext());
        }
        this.mCameraHandler.startRecording(recordParams, onEncodeResultListener);
    }

    public void stopPusher() {
        if (this.mCameraHandler == null || !isPushing()) {
            return;
        }
        this.mCameraHandler.stopRecording();
    }

    public boolean isPushing() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            return uVCCameraHandler.isRecording();
        }
        return false;
    }

    public boolean isCameraOpened() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            return uVCCameraHandler.isOpened();
        }
        return false;
    }

    public void release() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.release();
            this.mCameraHandler = null;
        }
        USBMonitor uSBMonitor = this.mUSBMonitor;
        if (uSBMonitor != null) {
            uSBMonitor.destroy();
            this.mUSBMonitor = null;
        }
    }

    public USBMonitor getUSBMonitor() {
        return this.mUSBMonitor;
    }

    public void setOnPreviewFrameListener(AbstractUVCCameraHandler.OnPreViewResultListener onPreViewResultListener) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.setOnPreViewResultListener(onPreViewResultListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openCamera(USBMonitor.UsbControlBlock usbControlBlock) {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.open(usbControlBlock);
        }
    }

    public void startPreview(CameraViewInterface cameraViewInterface) {
        SurfaceTexture surfaceTexture = cameraViewInterface.getSurfaceTexture();
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler == null || surfaceTexture == null) {
            return;
        }
        uVCCameraHandler.startPreview(surfaceTexture);
    }

    public void stopPreview() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.stopPreview();
        }
    }

    public void startCameraFoucs() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler != null) {
            uVCCameraHandler.startCameraFoucs();
        }
    }

    public List<Size> getSupportedPreviewSizes() {
        UVCCameraHandler uVCCameraHandler = this.mCameraHandler;
        if (uVCCameraHandler == null) {
            return null;
        }
        return uVCCameraHandler.getSupportedPreviewSizes();
    }

    public void setDefaultPreviewSize(int i, int i2) {
        if (this.mUSBMonitor != null) {
            throw new IllegalStateException("setDefaultPreviewSize should be call before initMonitor");
        }
        this.previewWidth = i;
        this.previewHeight = i2;
    }

    public void setDefaultFrameFormat(int i) {
        if (this.mUSBMonitor != null) {
            throw new IllegalStateException("setDefaultFrameFormat should be call before initMonitor");
        }
        this.mFrameFormat = i;
    }

    public int getPreviewWidth() {
        return this.previewWidth;
    }

    public int getPreviewHeight() {
        return this.previewHeight;
    }

    public void setIButtonCallback(IButtonCallback iButtonCallback) {
        if (iButtonCallback != null) {
            this.mCameraHandler.setIButtonCallback(iButtonCallback);
        }
    }
}
