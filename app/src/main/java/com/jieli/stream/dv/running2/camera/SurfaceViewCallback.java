package com.jieli.stream.dv.running2.camera;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public final class SurfaceViewCallback implements SurfaceHolder.Callback {
    private static final String TAG = SurfaceViewCallback.class.getSimpleName();
    private Context context;
    private CameraPresenter mPresenter = new CameraPresenter();

    public SurfaceViewCallback(Context context) {
        this.context = context;
    }

    public void setDeviceState(boolean z) {
        this.mPresenter.setDeviceState(z);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mPresenter.open();
        this.mPresenter.setDisplayOrientation(((Activity) this.context).getWindowManager().getDefaultDisplay().getRotation());
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.mPresenter.isPreviewing()) {
            Dbug.i(TAG, "stop preview");
            this.mPresenter.stopPreview();
        }
        Dbug.i(TAG, "start preview");
        this.mPresenter.startPreview(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Dbug.i(TAG, "close camera");
        this.mPresenter.close();
    }
}
