package com.baidu.platform.comapi.map;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.util.Iterator;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class MapRenderer implements GLSurfaceView.Renderer {
    private static final String d = MapRenderer.class.getSimpleName();
    public int a;
    public int b;
    public int c;
    private long e;
    private a f;
    private final GestureDetectorOnDoubleTapListenerC0079j g;

    public interface a {
        void e();
    }

    public MapRenderer(GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j, a aVar) {
        this.f = aVar;
        this.g = gestureDetectorOnDoubleTapListenerC0079j;
    }

    private void a(GL10 gl10) {
        GLES20.glClear(16640);
        GLES20.glClearColor(0.85f, 0.8f, 0.8f, 0.0f);
    }

    private boolean a() {
        return this.e != 0;
    }

    public static native void nativeInit(long j);

    public static native int nativeRender(long j);

    public static native void nativeResize(long j, int i, int i2);

    public void a(long j) {
        this.e = j;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        if (!a()) {
            a(gl10);
            return;
        }
        if (this.c <= 1) {
            nativeResize(this.e, this.a, this.b);
            this.c++;
        }
        this.f.e();
        int nativeRender = nativeRender(this.e);
        Iterator<InterfaceC0081l> it = this.g.a().f.iterator();
        while (it.hasNext()) {
            it.next().a(gl10, this.g.a().H());
        }
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.g;
        if (nativeRender == 1) {
            gestureDetectorOnDoubleTapListenerC0079j.requestRender();
            return;
        }
        if (gestureDetectorOnDoubleTapListenerC0079j.a().c()) {
            if (gestureDetectorOnDoubleTapListenerC0079j.getRenderMode() != 1) {
                gestureDetectorOnDoubleTapListenerC0079j.setRenderMode(1);
            }
        } else if (gestureDetectorOnDoubleTapListenerC0079j.getRenderMode() != 0) {
            gestureDetectorOnDoubleTapListenerC0079j.setRenderMode(0);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        long j = this.e;
        if (j != 0) {
            nativeResize(j, i, i2);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        nativeInit(this.e);
        if (a()) {
            this.f.e();
        }
    }
}
