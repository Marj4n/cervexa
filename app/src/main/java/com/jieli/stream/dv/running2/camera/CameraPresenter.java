package com.jieli.stream.dv.running2.camera;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import com.jieli.lib.dv.control.projection.StreamingPush;
import com.jieli.stream.dv.running2.ui.widget.verticalseekbar.VerticalSeekBar;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class CameraPresenter implements ICamera, Camera.PreviewCallback {
    private static final String tag = CameraPresenter.class.getSimpleName();
    private boolean isReady;
    private Camera mCamera;
    private byte[] mPreviewBuffer;
    private SurfaceTexture mSurfaceTexture;
    private boolean previewing;
    private int mCurrentCamIndex = 0;
    private StreamingPush mStreaming = new StreamingPush(StreamingPush.Protocol.TCP);

    public void setDeviceState(boolean z) {
        this.isReady = z;
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public boolean isPreviewing() {
        return this.previewing;
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public void startPreview(SurfaceHolder surfaceHolder) {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mCamera.startPreview();
            this.previewing = true;
        }
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public void startPreview(SurfaceTexture surfaceTexture) {
        Dbug.i(tag, "startPreview ");
        this.mSurfaceTexture = surfaceTexture;
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                this.mCamera.release();
                this.mCamera = null;
                e.printStackTrace();
            }
            int i = this.mCamera.getParameters().getPreviewSize().width;
            int i2 = this.mCamera.getParameters().getPreviewSize().height;
            Dbug.i(tag, "startPreview: w=" + i + ", h=" + i2);
            byte[] bArr = new byte[((i * i2) * ImageFormat.getBitsPerPixel(17)) / 8];
            this.mPreviewBuffer = bArr;
            this.mCamera.addCallbackBuffer(bArr);
            this.mCamera.setPreviewCallbackWithBuffer(this);
            this.mCamera.startPreview();
            this.previewing = true;
        }
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public void stopPreview() {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.stopPreview();
        }
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public boolean open() {
        Dbug.i(tag, "open ");
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                try {
                    this.mCamera = Camera.open(i);
                    this.mCurrentCamIndex = i;
                } catch (RuntimeException e) {
                    Log.e(tag, "Camera failed to open: " + e.getLocalizedMessage());
                    return false;
                }
            }
        }
        this.mStreaming.create(ClientManager.getClient().getAddress());
        this.mCamera.setPreviewCallback(this);
        return true;
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public void close() {
        StreamingPush streamingPush = this.mStreaming;
        if (streamingPush != null) {
            streamingPush.close();
        }
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
        this.previewing = false;
    }

    @Override // com.jieli.stream.dv.running2.camera.ICamera
    public void setDisplayOrientation(int i) {
        int i2;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(this.mCurrentCamIndex, cameraInfo);
        int i3 = 0;
        if (i != 0) {
            if (i == 1) {
                i3 = 90;
            } else if (i == 2) {
                i3 = 180;
            } else if (i == 3) {
                i3 = VerticalSeekBar.ROTATION_ANGLE_CW_270;
            }
        }
        if (cameraInfo.facing == 1) {
            i2 = (360 - ((cameraInfo.orientation + i3) % 360)) % 360;
        } else {
            i2 = ((cameraInfo.orientation - i3) + 360) % 360;
        }
        this.mCamera.setDisplayOrientation(i2);
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        if (this.mCamera == null || camera == null) {
            Dbug.e(tag, "onPreviewFrame: camera null");
            return;
        }
        if (this.mSurfaceTexture != null) {
            camera.addCallbackBuffer(this.mPreviewBuffer);
            this.mSurfaceTexture.getTimestamp();
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            Dbug.e(tag, "onPreviewFrame: parameters null");
            return;
        }
        Camera.Size previewSize = parameters.getPreviewSize();
        YuvImage yuvImage = new YuvImage(bArr, 17, previewSize.width, previewSize.height, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (yuvImage.compressToJpeg(new Rect(0, 0, 480, IConstant.PROJECTION_HEIGHT), 60, byteArrayOutputStream)) {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            StreamingPush streamingPush = this.mStreaming;
            if (streamingPush == null || !this.isReady) {
                return;
            }
            streamingPush.send(2, byteArray);
            return;
        }
        Dbug.e(tag, "compressToJpeg failed");
    }
}
