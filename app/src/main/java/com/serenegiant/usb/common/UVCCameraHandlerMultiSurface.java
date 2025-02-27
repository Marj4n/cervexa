package com.serenegiant.usb.common;

import android.app.Activity;
import android.view.Surface;
import com.serenegiant.glutils.RendererHolder;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;
import java.io.FileNotFoundException;

/* loaded from: classes2.dex */
public class UVCCameraHandlerMultiSurface extends AbstractUVCCameraHandler {
    private RendererHolder mRendererHolder;

    public static final UVCCameraHandlerMultiSurface createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2) {
        return createHandler(activity, cameraViewInterface, 1, i, i2, 1, 1.0f);
    }

    public static final UVCCameraHandlerMultiSurface createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, float f) {
        return createHandler(activity, cameraViewInterface, 1, i, i2, 1, f);
    }

    public static final UVCCameraHandlerMultiSurface createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3) {
        return createHandler(activity, cameraViewInterface, i, i2, i3, 1, 1.0f);
    }

    public static final UVCCameraHandlerMultiSurface createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3, int i4) {
        return createHandler(activity, cameraViewInterface, i, i2, i3, i4, 1.0f);
    }

    public static final UVCCameraHandlerMultiSurface createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3, int i4, float f) {
        CameraThread cameraThread = new CameraThread(UVCCameraHandlerMultiSurface.class, activity, cameraViewInterface, i, i2, i3, i4, f);
        cameraThread.start();
        return (UVCCameraHandlerMultiSurface) cameraThread.getHandler();
    }

    protected UVCCameraHandlerMultiSurface(CameraThread cameraThread) {
        super(cameraThread);
        this.mRendererHolder = new RendererHolder(cameraThread.getWidth(), cameraThread.getHeight(), null);
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public synchronized void release() {
        if (this.mRendererHolder != null) {
            this.mRendererHolder.release();
            this.mRendererHolder = null;
        }
        super.release();
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public synchronized void resize(int i, int i2) {
        super.resize(i, i2);
        if (this.mRendererHolder != null) {
            this.mRendererHolder.resize(i, i2);
        }
    }

    public synchronized void startPreview() {
        checkReleased();
        if (this.mRendererHolder != null) {
            super.startPreview(this.mRendererHolder.getSurface());
        } else {
            throw new IllegalStateException();
        }
    }

    public synchronized void addSurface(int i, Surface surface, boolean z) {
        checkReleased();
        this.mRendererHolder.addSurface(i, surface, z);
    }

    public synchronized void removeSurface(int i) {
        if (this.mRendererHolder != null) {
            this.mRendererHolder.removeSurface(i);
        }
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public void captureStill(final String str, OnCaptureListener onCaptureListener) {
        checkReleased();
        post(new Runnable() { // from class: com.serenegiant.usb.common.UVCCameraHandlerMultiSurface.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (UVCCameraHandlerMultiSurface.this) {
                    if (UVCCameraHandlerMultiSurface.this.mRendererHolder != null) {
                        try {
                            UVCCameraHandlerMultiSurface.this.mRendererHolder.captureStill(str);
                            UVCCameraHandlerMultiSurface.this.updateMedia(str);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
