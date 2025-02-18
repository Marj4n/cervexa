package com.baidu.platform.comapi.map;

import android.graphics.SurfaceTexture;
import android.opengl.GLUtils;
import com.serenegiant.glutils.EGLBase;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/* renamed from: com.baidu.platform.comapi.map.m, reason: case insensitive filesystem */
/* loaded from: classes.dex */
public class C0082m extends Thread {
    private AtomicBoolean a;
    private SurfaceTexture b;
    private a c;
    private EGL10 d;
    private GL10 h;
    private final F k;
    private EGLDisplay e = EGL10.EGL_NO_DISPLAY;
    private EGLContext f = EGL10.EGL_NO_CONTEXT;
    private EGLSurface g = EGL10.EGL_NO_SURFACE;
    private int i = 1;
    private boolean j = false;

    /* renamed from: com.baidu.platform.comapi.map.m$a */
    public interface a {
        int a();
    }

    public C0082m(SurfaceTexture surfaceTexture, a aVar, AtomicBoolean atomicBoolean, F f) {
        this.b = surfaceTexture;
        this.c = aVar;
        this.a = atomicBoolean;
        this.k = f;
    }

    private boolean a(int i, int i2, int i3, int i4, int i5, int i6) {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        this.d = egl10;
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.e = eglGetDisplay;
        if (eglGetDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetdisplay failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
        }
        if (!this.d.eglInitialize(this.e, new int[2])) {
            throw new RuntimeException("eglInitialize failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[100];
        int[] iArr = new int[1];
        if (!this.d.eglChooseConfig(this.e, new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344}, eGLConfigArr, 100, iArr) || iArr[0] <= 0) {
            return false;
        }
        this.f = this.d.eglCreateContext(this.e, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, new int[]{EGLBase.EGL_CONTEXT_CLIENT_VERSION, 2, 12344});
        EGLSurface eglCreateWindowSurface = this.d.eglCreateWindowSurface(this.e, eGLConfigArr[0], this.b, null);
        this.g = eglCreateWindowSurface;
        if (eglCreateWindowSurface == EGL10.EGL_NO_SURFACE || this.f == EGL10.EGL_NO_CONTEXT) {
            if (this.d.eglGetError() == 12299) {
                throw new RuntimeException("eglCreateWindowSurface returned  EGL_BAD_NATIVE_WINDOW. ");
            }
            GLUtils.getEGLErrorString(this.d.eglGetError());
        }
        EGL10 egl102 = this.d;
        EGLDisplay eGLDisplay = this.e;
        EGLSurface eGLSurface = this.g;
        if (egl102.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.f)) {
            this.h = (GL10) this.f.getGL();
            return true;
        }
        GLUtils.getEGLErrorString(this.d.eglGetError());
        throw new RuntimeException("eglMakeCurrent failed : " + GLUtils.getEGLErrorString(this.d.eglGetError()));
    }

    private static boolean b(int i, int i2, int i3, int i4, int i5, int i6) {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglGetDisplay, new int[2]);
        int[] iArr = new int[1];
        return egl10.eglChooseConfig(eglGetDisplay, new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344}, new EGLConfig[100], 100, iArr) && iArr[0] > 0;
    }

    private void d() {
        try {
            if (b(5, 6, 5, 0, 24, 0)) {
                a(5, 6, 5, 0, 24, 0);
            } else {
                a(8, 8, 8, 0, 16, 0);
            }
        } catch (IllegalArgumentException unused) {
            a(8, 8, 8, 0, 16, 0);
        }
        MapRenderer.nativeInit(this.k.b().h);
        MapRenderer.nativeResize(this.k.b().h, F.a, F.b);
    }

    private void e() {
        this.d.eglDestroyContext(this.e, this.f);
        this.d.eglDestroySurface(this.e, this.g);
        this.d.eglTerminate(this.e);
        this.f = EGL10.EGL_NO_CONTEXT;
        this.g = EGL10.EGL_NO_SURFACE;
    }

    public void a() {
        this.i = 1;
        synchronized (this) {
            if (getState() == Thread.State.WAITING) {
                notify();
            }
        }
    }

    public void b() {
        this.i = 0;
    }

    public void c() {
        this.j = true;
        synchronized (this) {
            if (getState() == Thread.State.WAITING) {
                notify();
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        d();
        while (this.c != null) {
            if (this.i != 1) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (this.k.b() == null) {
                    break;
                }
                synchronized (this.k.b()) {
                    this.i = this.c.a();
                    for (InterfaceC0081l interfaceC0081l : this.k.b().f) {
                        E H = this.k.b().H();
                        if (this.h == null) {
                            return;
                        } else {
                            interfaceC0081l.a(this.h, H);
                        }
                    }
                    this.d.eglSwapBuffers(this.e, this.g);
                }
            }
            if (this.j) {
                break;
            }
        }
        e();
    }
}
