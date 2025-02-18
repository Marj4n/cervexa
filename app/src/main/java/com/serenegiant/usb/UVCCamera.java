package com.serenegiant.usb;

import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.serenegiant.usb.USBMonitor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UVCCamera {
    public static final int CTRL_AE = 2;
    public static final int CTRL_AE_ABS = 8;
    public static final int CTRL_AE_PRIORITY = 4;
    public static final int CTRL_AR_REL = 16;
    public static final int CTRL_FOCUS_ABS = 32;
    public static final int CTRL_FOCUS_AUTO = 131072;
    public static final int CTRL_FOCUS_REL = 64;
    public static final int CTRL_FOCUS_SIMPLE = 524288;
    public static final int CTRL_IRIS_ABS = 128;
    public static final int CTRL_IRIS_REL = 256;
    public static final int CTRL_PANTILT_ABS = 2048;
    public static final int CTRL_PANTILT_REL = 4096;
    public static final int CTRL_PRIVACY = 262144;
    public static final int CTRL_ROLL_ABS = 8192;
    public static final int CTRL_ROLL_REL = 16384;
    public static final int CTRL_SCANNING = 1;
    public static final int CTRL_WINDOW = 1048576;
    public static final int CTRL_ZOOM_ABS = 512;
    public static final int CTRL_ZOOM_REL = 1024;
    private static final boolean DEBUG = false;
    public static final float DEFAULT_BANDWIDTH = 1.0f;
    public static final int DEFAULT_PREVIEW_HEIGHT = 480;
    public static final int DEFAULT_PREVIEW_MAX_FPS = 31;
    public static final int DEFAULT_PREVIEW_MIN_FPS = 1;
    public static final int DEFAULT_PREVIEW_MODE = 0;
    public static final int DEFAULT_PREVIEW_WIDTH = 640;
    private static final String DEFAULT_USBFS = "/dev/bus/usb";
    public static final int FRAME_FORMAT_MJPEG = 1;
    public static final int FRAME_FORMAT_YUYV = 0;
    public static final int PIXEL_FORMAT_NV21 = 5;
    public static final int PIXEL_FORMAT_RAW = 0;
    public static final int PIXEL_FORMAT_RGB565 = 2;
    public static final int PIXEL_FORMAT_RGBX = 3;
    public static final int PIXEL_FORMAT_YUV = 1;
    public static final int PIXEL_FORMAT_YUV420SP = 4;
    public static final int PU_AVIDEO_LOCK = -2147352576;
    public static final int PU_AVIDEO_STD = -2147418112;
    public static final int PU_BACKLIGHT = -2147483392;
    public static final int PU_BRIGHTNESS = -2147483647;
    public static final int PU_CONTRAST = -2147483646;
    public static final int PU_CONTRAST_AUTO = -2147221504;
    public static final int PU_DIGITAL_LIMIT = -2147450880;
    public static final int PU_DIGITAL_MULT = -2147467264;
    public static final int PU_GAIN = -2147483136;
    public static final int PU_GAMMA = -2147483616;
    public static final int PU_HUE = -2147483644;
    public static final int PU_HUE_AUTO = -2147481600;
    public static final int PU_POWER_LF = -2147482624;
    public static final int PU_SATURATION = -2147483640;
    public static final int PU_SHARPNESS = -2147483632;
    public static final int PU_WB_COMPO = -2147483520;
    public static final int PU_WB_COMPO_AUTO = -2147475456;
    public static final int PU_WB_TEMP = -2147483584;
    public static final int PU_WB_TEMP_AUTO = -2147479552;
    public static final int STATUS_ATTRIBUTE_FAILURE_CHANGE = 2;
    public static final int STATUS_ATTRIBUTE_INFO_CHANGE = 1;
    public static final int STATUS_ATTRIBUTE_UNKNOWN = 255;
    public static final int STATUS_ATTRIBUTE_VALUE_CHANGE = 0;
    public static final int STATUS_CLASS_CONTROL = 16;
    public static final int STATUS_CLASS_CONTROL_CAMERA = 17;
    public static final int STATUS_CLASS_CONTROL_PROCESSING = 18;
    private static final String[] SUPPORTS_CTRL;
    private static final String[] SUPPORTS_PROC;
    private static final String TAG = UVCCamera.class.getSimpleName();
    private static boolean isLoaded;
    protected int mAnalogVideoLockStateDef;
    protected int mAnalogVideoLockStateMax;
    protected int mAnalogVideoLockStateMin;
    protected int mAnalogVideoStandardDef;
    protected int mAnalogVideoStandardMax;
    protected int mAnalogVideoStandardMin;
    protected int mAutoFocusDef;
    protected int mAutoFocusMax;
    protected int mAutoFocusMin;
    protected int mAutoWhiteBlanceCompoDef;
    protected int mAutoWhiteBlanceCompoMax;
    protected int mAutoWhiteBlanceCompoMin;
    protected int mAutoWhiteBlanceDef;
    protected int mAutoWhiteBlanceMax;
    protected int mAutoWhiteBlanceMin;
    protected int mBacklightCompDef;
    protected int mBacklightCompMax;
    protected int mBacklightCompMin;
    protected int mBrightnessDef;
    protected int mBrightnessMax;
    protected int mBrightnessMin;
    protected int mContrastDef;
    protected int mContrastMax;
    protected int mContrastMin;
    protected long mControlSupports;
    private USBMonitor.UsbControlBlock mCtrlBlock;
    protected List<Size> mCurrentSizeList;
    protected int mExposureDef;
    protected int mExposureMax;
    protected int mExposureMin;
    protected int mExposureModeDef;
    protected int mExposureModeMax;
    protected int mExposureModeMin;
    protected int mExposurePriorityDef;
    protected int mExposurePriorityMax;
    protected int mExposurePriorityMin;
    protected int mFocusDef;
    protected int mFocusMax;
    protected int mFocusMin;
    protected int mFocusRelDef;
    protected int mFocusRelMax;
    protected int mFocusRelMin;
    protected int mFocusSimpleDef;
    protected int mFocusSimpleMax;
    protected int mFocusSimpleMin;
    protected int mGainDef;
    protected int mGainMax;
    protected int mGainMin;
    protected int mGammaDef;
    protected int mGammaMax;
    protected int mGammaMin;
    protected int mHueDef;
    protected int mHueMax;
    protected int mHueMin;
    protected int mIrisDef;
    protected int mIrisMax;
    protected int mIrisMin;
    protected int mIrisRelDef;
    protected int mIrisRelMax;
    protected int mIrisRelMin;
    protected int mMultiplierDef;
    protected int mMultiplierLimitDef;
    protected int mMultiplierLimitMax;
    protected int mMultiplierLimitMin;
    protected int mMultiplierMax;
    protected int mMultiplierMin;
    protected int mPanDef;
    protected int mPanMax;
    protected int mPanMin;
    protected int mPanRelDef;
    protected int mPanRelMax;
    protected int mPanRelMin;
    protected int mPowerlineFrequencyDef;
    protected int mPowerlineFrequencyMax;
    protected int mPowerlineFrequencyMin;
    protected int mPrivacyDef;
    protected int mPrivacyMax;
    protected int mPrivacyMin;
    protected long mProcSupports;
    protected int mRollDef;
    protected int mRollMax;
    protected int mRollMin;
    protected int mRollRelDef;
    protected int mRollRelMax;
    protected int mRollRelMin;
    protected int mSaturationDef;
    protected int mSaturationMax;
    protected int mSaturationMin;
    protected int mScanningModeDef;
    protected int mScanningModeMax;
    protected int mScanningModeMin;
    protected int mSharpnessDef;
    protected int mSharpnessMax;
    protected int mSharpnessMin;
    protected int mTiltDef;
    protected int mTiltMax;
    protected int mTiltMin;
    protected int mTiltRelDef;
    protected int mTiltRelMax;
    protected int mTiltRelMin;
    protected int mWhiteBlanceCompoDef;
    protected int mWhiteBlanceCompoMax;
    protected int mWhiteBlanceCompoMin;
    protected int mWhiteBlanceDef;
    protected int mWhiteBlanceMax;
    protected int mWhiteBlanceMin;
    protected int mWhiteBlanceRelDef;
    protected int mWhiteBlanceRelMax;
    protected int mWhiteBlanceRelMin;
    protected int mZoomDef;
    protected int mZoomMax;
    protected int mZoomMin;
    protected int mZoomRelDef;
    protected int mZoomRelMax;
    protected int mZoomRelMin;
    protected int mCurrentFrameFormat = 1;
    protected int mCurrentWidth = DEFAULT_PREVIEW_WIDTH;
    protected int mCurrentHeight = 480;
    protected float mCurrentBandwidthFactor = 1.0f;
    protected long mNativePtr = nativeCreate();
    protected String mSupportedSize = null;

    private final native int nativeConnect(long j, int i, int i2, int i3, int i4, int i5, String str);

    private final native long nativeCreate();

    private final native void nativeDestroy(long j);

    private static final native int nativeGetAnalogVideoLoackState(long j);

    private static final native int nativeGetAnalogVideoStandard(long j);

    private static final native int nativeGetAutoContrast(long j);

    private static final native int nativeGetAutoFocus(long j);

    private static final native int nativeGetAutoHue(long j);

    private static final native int nativeGetAutoWhiteBlance(long j);

    private static final native int nativeGetAutoWhiteBlanceCompo(long j);

    private static final native int nativeGetBacklightComp(long j);

    private static final native int nativeGetBrightness(long j);

    private static final native int nativeGetContrast(long j);

    private static final native long nativeGetCtrlSupports(long j);

    private static final native int nativeGetDigitalMultiplier(long j);

    private static final native int nativeGetDigitalMultiplierLimit(long j);

    private static final native int nativeGetExposure(long j);

    private static final native int nativeGetExposureMode(long j);

    private static final native int nativeGetExposurePriority(long j);

    private static final native int nativeGetExposureRel(long j);

    private static final native int nativeGetFocus(long j);

    private static final native int nativeGetFocusRel(long j);

    private static final native int nativeGetGain(long j);

    private static final native int nativeGetGamma(long j);

    private static final native int nativeGetHue(long j);

    private static final native int nativeGetIris(long j);

    private static final native int nativeGetIrisRel(long j);

    private static final native int nativeGetPan(long j);

    private static final native int nativeGetPanRel(long j);

    private static final native int nativeGetPowerlineFrequency(long j);

    private static final native int nativeGetPrivacy(long j);

    private static final native long nativeGetProcSupports(long j);

    private static final native int nativeGetRoll(long j);

    private static final native int nativeGetRollRel(long j);

    private static final native int nativeGetSaturation(long j);

    private static final native int nativeGetScanningMode(long j);

    private static final native int nativeGetSharpness(long j);

    private static final native String nativeGetSupportedSize(long j);

    private static final native int nativeGetTilt(long j);

    private static final native int nativeGetTiltRel(long j);

    private static final native int nativeGetWhiteBlance(long j);

    private static final native int nativeGetWhiteBlanceCompo(long j);

    private static final native int nativeGetZoom(long j);

    private static final native int nativeGetZoomRel(long j);

    private static final native int nativeRelease(long j);

    private static final native int nativeSetAnalogVideoLoackState(long j, int i);

    private static final native int nativeSetAnalogVideoStandard(long j, int i);

    private static final native int nativeSetAutoContrast(long j, boolean z);

    private static final native int nativeSetAutoFocus(long j, boolean z);

    private static final native int nativeSetAutoHue(long j, boolean z);

    private static final native int nativeSetAutoWhiteBlance(long j, boolean z);

    private static final native int nativeSetAutoWhiteBlanceCompo(long j, boolean z);

    private static final native int nativeSetBacklightComp(long j, int i);

    private static final native int nativeSetBrightness(long j, int i);

    private static final native int nativeSetButtonCallback(long j, IButtonCallback iButtonCallback);

    private static final native int nativeSetCaptureDisplay(long j, Surface surface);

    private static final native int nativeSetContrast(long j, int i);

    private static final native int nativeSetDigitalMultiplier(long j, int i);

    private static final native int nativeSetDigitalMultiplierLimit(long j, int i);

    private static final native int nativeSetExposure(long j, int i);

    private static final native int nativeSetExposureMode(long j, int i);

    private static final native int nativeSetExposurePriority(long j, int i);

    private static final native int nativeSetExposureRel(long j, int i);

    private static final native int nativeSetFocus(long j, int i);

    private static final native int nativeSetFocusRel(long j, int i);

    private static final native int nativeSetFrameCallback(long j, IFrameCallback iFrameCallback, int i);

    private static final native int nativeSetGain(long j, int i);

    private static final native int nativeSetGamma(long j, int i);

    private static final native int nativeSetHue(long j, int i);

    private static final native int nativeSetIris(long j, int i);

    private static final native int nativeSetIrisRel(long j, int i);

    private static final native int nativeSetPan(long j, int i);

    private static final native int nativeSetPanRel(long j, int i);

    private static final native int nativeSetPowerlineFrequency(long j, int i);

    private static final native int nativeSetPreviewDisplay(long j, Surface surface);

    private static final native int nativeSetPreviewSize(long j, int i, int i2, int i3, int i4, int i5, float f);

    private static final native int nativeSetPrivacy(long j, boolean z);

    private static final native int nativeSetRoll(long j, int i);

    private static final native int nativeSetRollRel(long j, int i);

    private static final native int nativeSetSaturation(long j, int i);

    private static final native int nativeSetScanningMode(long j, int i);

    private static final native int nativeSetSharpness(long j, int i);

    private static final native int nativeSetStatusCallback(long j, IStatusCallback iStatusCallback);

    private static final native int nativeSetTilt(long j, int i);

    private static final native int nativeSetTiltRel(long j, int i);

    private static final native int nativeSetWhiteBlance(long j, int i);

    private static final native int nativeSetWhiteBlanceCompo(long j, int i);

    private static final native int nativeSetZoom(long j, int i);

    private static final native int nativeSetZoomRel(long j, int i);

    private static final native int nativeStartPreview(long j);

    private static final native int nativeStopPreview(long j);

    private final native int nativeUpdateAnalogVideoLockStateLimit(long j);

    private final native int nativeUpdateAnalogVideoStandardLimit(long j);

    private final native int nativeUpdateAutoContrastLimit(long j);

    private final native int nativeUpdateAutoFocusLimit(long j);

    private final native int nativeUpdateAutoHueLimit(long j);

    private final native int nativeUpdateAutoWhiteBlanceCompoLimit(long j);

    private final native int nativeUpdateAutoWhiteBlanceLimit(long j);

    private final native int nativeUpdateBacklightCompLimit(long j);

    private final native int nativeUpdateBrightnessLimit(long j);

    private final native int nativeUpdateContrastLimit(long j);

    private final native int nativeUpdateDigitalMultiplierLimit(long j);

    private final native int nativeUpdateDigitalMultiplierLimitLimit(long j);

    private final native int nativeUpdateExposureLimit(long j);

    private final native int nativeUpdateExposureModeLimit(long j);

    private final native int nativeUpdateExposurePriorityLimit(long j);

    private final native int nativeUpdateExposureRelLimit(long j);

    private final native int nativeUpdateFocusLimit(long j);

    private final native int nativeUpdateFocusRelLimit(long j);

    private final native int nativeUpdateGainLimit(long j);

    private final native int nativeUpdateGammaLimit(long j);

    private final native int nativeUpdateHueLimit(long j);

    private final native int nativeUpdateIrisLimit(long j);

    private final native int nativeUpdateIrisRelLimit(long j);

    private final native int nativeUpdatePanLimit(long j);

    private final native int nativeUpdatePanRelLimit(long j);

    private final native int nativeUpdatePowerlineFrequencyLimit(long j);

    private final native int nativeUpdatePrivacyLimit(long j);

    private final native int nativeUpdateRollLimit(long j);

    private final native int nativeUpdateRollRelLimit(long j);

    private final native int nativeUpdateSaturationLimit(long j);

    private final native int nativeUpdateScanningModeLimit(long j);

    private final native int nativeUpdateSharpnessLimit(long j);

    private final native int nativeUpdateTiltLimit(long j);

    private final native int nativeUpdateTiltRelLimit(long j);

    private final native int nativeUpdateWhiteBlanceCompoLimit(long j);

    private final native int nativeUpdateWhiteBlanceLimit(long j);

    private final native int nativeUpdateZoomLimit(long j);

    private final native int nativeUpdateZoomRelLimit(long j);

    static {
        if (!isLoaded) {
            System.loadLibrary("jpeg-turbo1500");
            System.loadLibrary("usb100");
            System.loadLibrary("uvc");
            System.loadLibrary("UVCCamera");
            isLoaded = true;
        }
        SUPPORTS_CTRL = new String[]{"D0:  Scanning Mode", "D1:  Auto-Exposure Mode", "D2:  Auto-Exposure Priority", "D3:  Exposure Time (Absolute)", "D4:  Exposure Time (Relative)", "D5:  Focus (Absolute)", "D6:  Focus (Relative)", "D7:  Iris (Absolute)", "D8:  Iris (Relative)", "D9:  Zoom (Absolute)", "D10: Zoom (Relative)", "D11: PanTilt (Absolute)", "D12: PanTilt (Relative)", "D13: Roll (Absolute)", "D14: Roll (Relative)", "D15: Reserved", "D16: Reserved", "D17: Focus, Auto", "D18: Privacy", "D19: Focus, Simple", "D20: Window", "D21: Region of Interest", "D22: Reserved, set to zero", "D23: Reserved, set to zero"};
        SUPPORTS_PROC = new String[]{"D0: Brightness", "D1: Contrast", "D2: Hue", "D3: Saturation", "D4: Sharpness", "D5: Gamma", "D6: White Balance Temperature", "D7: White Balance Component", "D8: Backlight Compensation", "D9: Gain", "D10: Power Line Frequency", "D11: Hue, Auto", "D12: White Balance Temperature, Auto", "D13: White Balance Component, Auto", "D14: Digital Multiplier", "D15: Digital Multiplier Limit", "D16: Analog Video Standard", "D17: Analog Video Lock Status", "D18: Contrast, Auto", "D19: Reserved. Set to zero", "D20: Reserved. Set to zero", "D21: Reserved. Set to zero", "D22: Reserved. Set to zero", "D23: Reserved. Set to zero"};
    }

    public synchronized void open(USBMonitor.UsbControlBlock usbControlBlock) {
        int i;
        StringBuilder sb = new StringBuilder();
        try {
            USBMonitor.UsbControlBlock m16clone = usbControlBlock.m16clone();
            this.mCtrlBlock = m16clone;
            i = nativeConnect(this.mNativePtr, m16clone.getVenderId(), this.mCtrlBlock.getProductId(), this.mCtrlBlock.getFileDescriptor(), this.mCtrlBlock.getBusNum(), this.mCtrlBlock.getDevNum(), getUSBFSName(this.mCtrlBlock));
            sb.append("调用nativeConnect返回值：" + i);
        } catch (Exception e) {
            Log.w(TAG, e);
            for (int i2 = 0; i2 < e.getStackTrace().length; i2++) {
                sb.append(e.getStackTrace()[i2].toString());
                sb.append("\n");
            }
            sb.append("core message ->" + e.getLocalizedMessage());
            i = -1;
        }
        if (i != 0) {
            throw new UnsupportedOperationException("open failed:result=" + i + "----->id_camera=" + this.mNativePtr + ";venderId=" + this.mCtrlBlock.getVenderId() + ";productId=" + this.mCtrlBlock.getProductId() + ";fileDescriptor=" + this.mCtrlBlock.getFileDescriptor() + ";busNum=" + this.mCtrlBlock.getBusNum() + ";devAddr=" + this.mCtrlBlock.getDevNum() + ";usbfs=" + getUSBFSName(this.mCtrlBlock) + "\nException：" + sb.toString());
        }
        if (this.mNativePtr != 0 && TextUtils.isEmpty(this.mSupportedSize)) {
            this.mSupportedSize = nativeGetSupportedSize(this.mNativePtr);
        }
        nativeSetPreviewSize(this.mNativePtr, DEFAULT_PREVIEW_WIDTH, 480, 1, 31, 0, 1.0f);
    }

    public void setStatusCallback(IStatusCallback iStatusCallback) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetStatusCallback(j, iStatusCallback);
        }
    }

    public void setButtonCallback(IButtonCallback iButtonCallback) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetButtonCallback(j, iButtonCallback);
        }
    }

    public synchronized void close() {
        stopPreview();
        if (this.mNativePtr != 0) {
            nativeRelease(this.mNativePtr);
        }
        if (this.mCtrlBlock != null) {
            this.mCtrlBlock.close();
            this.mCtrlBlock = null;
        }
        this.mProcSupports = 0L;
        this.mControlSupports = 0L;
        this.mCurrentFrameFormat = -1;
        this.mCurrentBandwidthFactor = 0.0f;
        this.mSupportedSize = null;
        this.mCurrentSizeList = null;
    }

    public UsbDevice getDevice() {
        USBMonitor.UsbControlBlock usbControlBlock = this.mCtrlBlock;
        if (usbControlBlock != null) {
            return usbControlBlock.getDevice();
        }
        return null;
    }

    public String getDeviceName() {
        USBMonitor.UsbControlBlock usbControlBlock = this.mCtrlBlock;
        if (usbControlBlock != null) {
            return usbControlBlock.getDeviceName();
        }
        return null;
    }

    public USBMonitor.UsbControlBlock getUsbControlBlock() {
        return this.mCtrlBlock;
    }

    public synchronized String getSupportedSize() {
        String nativeGetSupportedSize;
        if (TextUtils.isEmpty(this.mSupportedSize)) {
            nativeGetSupportedSize = nativeGetSupportedSize(this.mNativePtr);
            this.mSupportedSize = nativeGetSupportedSize;
        } else {
            nativeGetSupportedSize = this.mSupportedSize;
        }
        return nativeGetSupportedSize;
    }

    public Size getPreviewSize() {
        Iterator<Size> it = getSupportedSizeList().iterator();
        while (it.hasNext()) {
            Size next = it.next();
            if (next.width == this.mCurrentWidth || next.height == this.mCurrentHeight) {
                return next;
            }
        }
        return null;
    }

    public void setPreviewSize(int i, int i2) {
        setPreviewSize(i, i2, 1, 31, this.mCurrentFrameFormat, this.mCurrentBandwidthFactor);
    }

    public void setPreviewSize(int i, int i2, int i3) {
        setPreviewSize(i, i2, 1, 31, i3, this.mCurrentBandwidthFactor);
    }

    public void setPreviewSize(int i, int i2, int i3, float f) {
        setPreviewSize(i, i2, 1, 31, i3, f);
    }

    public void setPreviewSize(int i, int i2, int i3, int i4, int i5, float f) {
        if (i == 0 || i2 == 0) {
            throw new IllegalArgumentException("invalid preview size");
        }
        long j = this.mNativePtr;
        if (j != 0) {
            if (nativeSetPreviewSize(j, i, i2, i3, i4, i5, f) != 0) {
                throw new IllegalArgumentException("Failed to set preview size");
            }
            this.mCurrentFrameFormat = i5;
            this.mCurrentWidth = i;
            this.mCurrentHeight = i2;
            this.mCurrentBandwidthFactor = f;
        }
    }

    public List<Size> getSupportedSizeList() {
        return getSupportedSize(this.mCurrentFrameFormat > 0 ? 6 : 4, this.mSupportedSize);
    }

    public static List<Size> getSupportedSize(int i, String str) {
        int i2;
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray("formats");
                int length = jSONArray.length();
                for (int i3 = 0; i3 < length; i3++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i3);
                    if (jSONObject.has("type") && jSONObject.has("size") && ((i2 = jSONObject.getInt("type")) == i || i == -1)) {
                        addSize(jSONObject, i2, 0, arrayList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private static final void addSize(JSONObject jSONObject, int i, int i2, List<Size> list) throws JSONException {
        JSONArray jSONArray = jSONObject.getJSONArray("size");
        int length = jSONArray.length();
        for (int i3 = 0; i3 < length; i3++) {
            String[] split = jSONArray.getString(i3).split("x");
            try {
                list.add(new Size(i, i2, i3, Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            } catch (Exception unused) {
                return;
            }
        }
    }

    public synchronized void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        nativeSetPreviewDisplay(this.mNativePtr, surfaceHolder.getSurface());
    }

    public synchronized void setPreviewTexture(SurfaceTexture surfaceTexture) {
        nativeSetPreviewDisplay(this.mNativePtr, new Surface(surfaceTexture));
    }

    public synchronized void setPreviewDisplay(Surface surface) {
        nativeSetPreviewDisplay(this.mNativePtr, surface);
    }

    public void setFrameCallback(IFrameCallback iFrameCallback, int i) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetFrameCallback(j, iFrameCallback, i);
        }
    }

    public synchronized void startPreview() {
        if (this.mCtrlBlock != null) {
            nativeStartPreview(this.mNativePtr);
        }
    }

    public synchronized void stopPreview() {
        setFrameCallback(null, 0);
        if (this.mCtrlBlock != null) {
            nativeStopPreview(this.mNativePtr);
        }
    }

    public synchronized void destroy() {
        close();
        if (this.mNativePtr != 0) {
            nativeDestroy(this.mNativePtr);
            this.mNativePtr = 0L;
        }
    }

    public boolean checkSupportFlag(long j) {
        updateCameraParams();
        return (j & (-2147483648L)) == -2147483648L ? (this.mProcSupports & j) == (j & 2147483647L) : (this.mControlSupports & j) == j;
    }

    public synchronized void setAutoFocus(boolean z) {
        if (this.mNativePtr != 0) {
            nativeSetAutoFocus(this.mNativePtr, z);
        }
    }

    public synchronized boolean getAutoFocus() {
        boolean z;
        z = true;
        if (this.mNativePtr != 0) {
            if (nativeGetAutoFocus(this.mNativePtr) <= 0) {
                z = false;
            }
        }
        return z;
    }

    public synchronized void setFocus(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mFocusMax - this.mFocusMin);
            if (abs > 0.0f) {
                nativeSetFocus(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mFocusMin);
            }
        }
    }

    public synchronized int getFocus(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateFocusLimit(this.mNativePtr);
            float abs = Math.abs(this.mFocusMax - this.mFocusMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mFocusMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getFocus() {
        return getFocus(nativeGetFocus(this.mNativePtr));
    }

    public synchronized void resetFocus() {
        if (this.mNativePtr != 0) {
            nativeSetFocus(this.mNativePtr, this.mFocusDef);
        }
    }

    public synchronized void setAutoWhiteBlance(boolean z) {
        if (this.mNativePtr != 0) {
            nativeSetAutoWhiteBlance(this.mNativePtr, z);
        }
    }

    public synchronized boolean getAutoWhiteBlance() {
        boolean z;
        z = true;
        if (this.mNativePtr != 0) {
            if (nativeGetAutoWhiteBlance(this.mNativePtr) <= 0) {
                z = false;
            }
        }
        return z;
    }

    public synchronized void setWhiteBlance(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mWhiteBlanceMax - this.mWhiteBlanceMin);
            if (abs > 0.0f) {
                nativeSetWhiteBlance(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mWhiteBlanceMin);
            }
        }
    }

    public synchronized int getWhiteBlance(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateWhiteBlanceLimit(this.mNativePtr);
            float abs = Math.abs(this.mWhiteBlanceMax - this.mWhiteBlanceMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mWhiteBlanceMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getWhiteBlance() {
        return getFocus(nativeGetWhiteBlance(this.mNativePtr));
    }

    public synchronized void resetWhiteBlance() {
        if (this.mNativePtr != 0) {
            nativeSetWhiteBlance(this.mNativePtr, this.mWhiteBlanceDef);
        }
    }

    public synchronized void setBrightness(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mBrightnessMax - this.mBrightnessMin);
            if (abs > 0.0f) {
                nativeSetBrightness(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mBrightnessMin);
            }
        }
    }

    public synchronized int getBrightness(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateBrightnessLimit(this.mNativePtr);
            float abs = Math.abs(this.mBrightnessMax - this.mBrightnessMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mBrightnessMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getBrightness() {
        return getBrightness(nativeGetBrightness(this.mNativePtr));
    }

    public synchronized void resetBrightness() {
        if (this.mNativePtr != 0) {
            nativeSetBrightness(this.mNativePtr, this.mBrightnessDef);
        }
    }

    public synchronized void setContrast(int i) {
        if (this.mNativePtr != 0) {
            nativeUpdateContrastLimit(this.mNativePtr);
            float abs = Math.abs(this.mContrastMax - this.mContrastMin);
            if (abs > 0.0f) {
                nativeSetContrast(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mContrastMin);
            }
        }
    }

    public synchronized int getContrast(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mContrastMax - this.mContrastMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mContrastMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getContrast() {
        return getContrast(nativeGetContrast(this.mNativePtr));
    }

    public synchronized void resetContrast() {
        if (this.mNativePtr != 0) {
            nativeSetContrast(this.mNativePtr, this.mContrastDef);
        }
    }

    public synchronized void setSharpness(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mSharpnessMax - this.mSharpnessMin);
            if (abs > 0.0f) {
                nativeSetSharpness(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mSharpnessMin);
            }
        }
    }

    public synchronized int getSharpness(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateSharpnessLimit(this.mNativePtr);
            float abs = Math.abs(this.mSharpnessMax - this.mSharpnessMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mSharpnessMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getSharpness() {
        return getSharpness(nativeGetSharpness(this.mNativePtr));
    }

    public synchronized void resetSharpness() {
        if (this.mNativePtr != 0) {
            nativeSetSharpness(this.mNativePtr, this.mSharpnessDef);
        }
    }

    public synchronized void setGain(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mGainMax - this.mGainMin);
            if (abs > 0.0f) {
                nativeSetGain(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mGainMin);
            }
        }
    }

    public synchronized int getGain(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateGainLimit(this.mNativePtr);
            float abs = Math.abs(this.mGainMax - this.mGainMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mGainMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getGain() {
        return getGain(nativeGetGain(this.mNativePtr));
    }

    public synchronized void resetGain() {
        if (this.mNativePtr != 0) {
            nativeSetGain(this.mNativePtr, this.mGainDef);
        }
    }

    public synchronized void setGamma(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mGammaMax - this.mGammaMin);
            if (abs > 0.0f) {
                nativeSetGamma(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mGammaMin);
            }
        }
    }

    public synchronized int getGamma(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateGammaLimit(this.mNativePtr);
            float abs = Math.abs(this.mGammaMax - this.mGammaMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mGammaMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getGamma() {
        return getGamma(nativeGetGamma(this.mNativePtr));
    }

    public synchronized void resetGamma() {
        if (this.mNativePtr != 0) {
            nativeSetGamma(this.mNativePtr, this.mGammaDef);
        }
    }

    public synchronized void setSaturation(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mSaturationMax - this.mSaturationMin);
            if (abs > 0.0f) {
                nativeSetSaturation(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mSaturationMin);
            }
        }
    }

    public synchronized int getSaturation(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateSaturationLimit(this.mNativePtr);
            float abs = Math.abs(this.mSaturationMax - this.mSaturationMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mSaturationMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getSaturation() {
        return getSaturation(nativeGetSaturation(this.mNativePtr));
    }

    public synchronized void resetSaturation() {
        if (this.mNativePtr != 0) {
            nativeSetSaturation(this.mNativePtr, this.mSaturationDef);
        }
    }

    public synchronized void setHue(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mHueMax - this.mHueMin);
            if (abs > 0.0f) {
                nativeSetHue(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mHueMin);
            }
        }
    }

    public synchronized int getHue(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateHueLimit(this.mNativePtr);
            float abs = Math.abs(this.mHueMax - this.mHueMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mHueMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getHue() {
        return getHue(nativeGetHue(this.mNativePtr));
    }

    public synchronized void resetHue() {
        if (this.mNativePtr != 0) {
            nativeSetHue(this.mNativePtr, this.mSaturationDef);
        }
    }

    public void setPowerlineFrequency(int i) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetPowerlineFrequency(j, i);
        }
    }

    public int getPowerlineFrequency() {
        return nativeGetPowerlineFrequency(this.mNativePtr);
    }

    public synchronized void setZoom(int i) {
        if (this.mNativePtr != 0) {
            float abs = Math.abs(this.mZoomMax - this.mZoomMin);
            if (abs > 0.0f) {
                nativeSetZoom(this.mNativePtr, ((int) ((i / 100.0f) * abs)) + this.mZoomMin);
            }
        }
    }

    public synchronized int getZoom(int i) {
        int i2;
        i2 = 0;
        if (this.mNativePtr != 0) {
            nativeUpdateZoomLimit(this.mNativePtr);
            float abs = Math.abs(this.mZoomMax - this.mZoomMin);
            if (abs > 0.0f) {
                i2 = (int) (((i - this.mZoomMin) * 100.0f) / abs);
            }
        }
        return i2;
    }

    public synchronized int getZoom() {
        return getZoom(nativeGetZoom(this.mNativePtr));
    }

    public synchronized void resetZoom() {
        if (this.mNativePtr != 0) {
            nativeSetZoom(this.mNativePtr, this.mZoomDef);
        }
    }

    public synchronized void updateCameraParams() {
        if (this.mNativePtr == 0) {
            this.mProcSupports = 0L;
            this.mControlSupports = 0L;
        } else if (this.mControlSupports == 0 || this.mProcSupports == 0) {
            if (this.mControlSupports == 0) {
                this.mControlSupports = nativeGetCtrlSupports(this.mNativePtr);
            }
            if (this.mProcSupports == 0) {
                this.mProcSupports = nativeGetProcSupports(this.mNativePtr);
            }
            if (this.mControlSupports != 0 && this.mProcSupports != 0) {
                nativeUpdateBrightnessLimit(this.mNativePtr);
                nativeUpdateContrastLimit(this.mNativePtr);
                nativeUpdateSharpnessLimit(this.mNativePtr);
                nativeUpdateGainLimit(this.mNativePtr);
                nativeUpdateGammaLimit(this.mNativePtr);
                nativeUpdateSaturationLimit(this.mNativePtr);
                nativeUpdateHueLimit(this.mNativePtr);
                nativeUpdateZoomLimit(this.mNativePtr);
                nativeUpdateWhiteBlanceLimit(this.mNativePtr);
                nativeUpdateFocusLimit(this.mNativePtr);
            }
        }
    }

    private static final void dumpControls(long j) {
        Log.i(TAG, String.format("controlSupports=%x", Long.valueOf(j)));
        for (int i = 0; i < SUPPORTS_CTRL.length; i++) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(SUPPORTS_CTRL[i]);
            sb.append((((long) (1 << i)) & j) != 0 ? "=enabled" : "=disabled");
            Log.i(str, sb.toString());
        }
    }

    private static final void dumpProc(long j) {
        Log.i(TAG, String.format("procSupports=%x", Long.valueOf(j)));
        for (int i = 0; i < SUPPORTS_PROC.length; i++) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(SUPPORTS_PROC[i]);
            sb.append((((long) (1 << i)) & j) != 0 ? "=enabled" : "=disabled");
            Log.i(str, sb.toString());
        }
    }

    private final String getUSBFSName(USBMonitor.UsbControlBlock usbControlBlock) {
        String deviceName = usbControlBlock.getDeviceName();
        String str = null;
        String[] split = !TextUtils.isEmpty(deviceName) ? deviceName.split("/") : null;
        if (split != null && split.length > 2) {
            StringBuilder sb = new StringBuilder(split[0]);
            for (int i = 1; i < split.length - 2; i++) {
                sb.append("/");
                sb.append(split[i]);
            }
            str = sb.toString();
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        Log.w(TAG, "failed to get USBFS path, try to use default path:" + deviceName);
        return DEFAULT_USBFS;
    }

    public void startCapture(Surface surface) {
        if (this.mCtrlBlock != null && surface != null) {
            nativeSetCaptureDisplay(this.mNativePtr, surface);
            return;
        }
        throw new NullPointerException("startCapture");
    }

    public void stopCapture() {
        if (this.mCtrlBlock != null) {
            nativeSetCaptureDisplay(this.mNativePtr, null);
        }
    }
}
