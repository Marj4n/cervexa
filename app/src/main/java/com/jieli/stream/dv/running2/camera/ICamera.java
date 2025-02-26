package com.jieli.stream.dv.running2.camera;

import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;

/* loaded from: classes.dex */
public interface ICamera {
    void close();

    boolean isPreviewing();

    boolean open();

    void setDisplayOrientation(int i);

    void startPreview(SurfaceTexture surfaceTexture);

    void startPreview(SurfaceHolder surfaceHolder);

    void stopPreview();
}
