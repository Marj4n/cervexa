package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.view.Surface;

/* loaded from: classes.dex */
public interface IRenderer extends IRendererCommon {
    void release();

    void requestRender(Object... objArr);

    void resize(int i, int i2);

    void setSurface(SurfaceTexture surfaceTexture);

    void setSurface(Surface surface);
}
