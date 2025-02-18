package com.serenegiant.usb.widget;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.serenegiant.usb.encoder.IVideoEncoder;
import com.serenegiant.widget.IAspectRatioView;

/* loaded from: classes2.dex */
public interface CameraViewInterface extends IAspectRatioView {

    public interface Callback {
        void onSurfaceChanged(CameraViewInterface cameraViewInterface, Surface surface, int i, int i2);

        void onSurfaceCreated(CameraViewInterface cameraViewInterface, Surface surface);

        void onSurfaceDestroy(CameraViewInterface cameraViewInterface, Surface surface);
    }

    Bitmap captureStillImage(int i, int i2);

    Surface getSurface();

    SurfaceTexture getSurfaceTexture();

    boolean hasSurface();

    void onPause();

    void onResume();

    void setCallback(Callback callback);

    void setVideoEncoder(IVideoEncoder iVideoEncoder);
}
