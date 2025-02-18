package com.baidu.platform.comapi.map;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.MapRenderer;
import java.util.Iterator;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* renamed from: com.baidu.platform.comapi.map.j, reason: case insensitive filesystem */
/* loaded from: classes.dex */
public class GestureDetectorOnDoubleTapListenerC0079j extends GLSurfaceView implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener, MapRenderer.a {
    private static final String a = GestureDetectorOnDoubleTapListenerC0079j.class.getSimpleName();
    private Handler b;
    private MapRenderer c;
    private int d;
    private int e;
    private GestureDetector f;
    private C0074e g;

    /* renamed from: com.baidu.platform.comapi.map.j$a */
    static class a {
        float a;
        float b;
        float c;
        float d;
        boolean e;
        float f;
        float g;
        double h;

        a() {
        }
    }

    public GestureDetectorOnDoubleTapListenerC0079j(Context context, C c, String str) {
        super(context);
        if (context == null) {
            throw new RuntimeException("when you create an mapview, the context can not be null");
        }
        setEGLContextClientVersion(2);
        this.f = new GestureDetector(context, this);
        EnvironmentUtilities.initAppDirectory(context);
        if (this.g == null) {
            this.g = new C0074e(context, str);
        }
        this.g.a();
        f();
        this.g.b();
        this.g.a(c);
        g();
        this.g.a(this.b);
        this.g.e();
        setBackgroundColor(0);
    }

    private static boolean a(int i, int i2, int i3, int i4, int i5, int i6) {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglGetDisplay, new int[2]);
        int[] iArr = new int[1];
        return egl10.eglChooseConfig(eglGetDisplay, new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344}, new EGLConfig[100], 100, iArr) && iArr[0] > 0;
    }

    private void f() {
        try {
            if (a(5, 6, 5, 0, 24, 0)) {
                setEGLConfigChooser(5, 6, 5, 0, 24, 0);
            } else {
                setEGLConfigChooser(true);
            }
        } catch (IllegalArgumentException unused) {
            setEGLConfigChooser(true);
        }
        MapRenderer mapRenderer = new MapRenderer(this, this);
        this.c = mapRenderer;
        mapRenderer.a(this.g.h);
        setRenderer(this.c);
        setRenderMode(1);
    }

    private void g() {
        this.b = new HandlerC0080k(this);
    }

    public C0074e a() {
        return this.g;
    }

    public void a(int i) {
        int i2;
        if (this.g == null) {
            return;
        }
        Message message = new Message();
        message.what = 50;
        message.obj = Long.valueOf(this.g.h);
        boolean p = this.g.p();
        if (i != 3) {
            i2 = p ? 1 : 0;
            this.b.sendMessage(message);
        }
        message.arg1 = i2;
        this.b.sendMessage(message);
    }

    public void a(String str, Rect rect) {
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        if (rect != null) {
            int i = rect.left;
            int i2 = this.e < rect.bottom ? 0 : this.e - rect.bottom;
            int width = rect.width();
            int height = rect.height();
            if (i < 0 || i2 < 0 || width <= 0 || height <= 0) {
                return;
            }
            if (width > this.d) {
                width = Math.abs(rect.width()) - (rect.right - this.d);
            }
            if (height > this.e) {
                height = Math.abs(rect.height()) - (rect.bottom - this.e);
            }
            if (i > SysOSUtil.getScreenSizeX() || i2 > SysOSUtil.getScreenSizeY()) {
                this.g.g.a(str, (Bundle) null);
                requestRender();
                return;
            }
            this.d = width;
            this.e = height;
            Bundle bundle = new Bundle();
            bundle.putInt("x", i);
            bundle.putInt("y", i2);
            bundle.putInt("width", width);
            bundle.putInt("height", height);
            this.g.g.a(str, bundle);
        } else {
            this.g.g.a(str, (Bundle) null);
        }
        requestRender();
    }

    public void b() {
        C0074e c0074e = this.g;
        if (c0074e != null) {
            Iterator<InterfaceC0081l> it = c0074e.f.iterator();
            while (it.hasNext()) {
                it.next().f();
            }
            this.g.b(this.b);
            this.g.M();
            this.g = null;
        }
    }

    public void c() {
        C0074e c0074e = this.g;
        if (c0074e == null) {
            return;
        }
        c0074e.t();
    }

    public void d() {
        C0074e c0074e = this.g;
        if (c0074e == null) {
            return;
        }
        c0074e.u();
    }

    @Override // com.baidu.platform.comapi.map.MapRenderer.a
    public void e() {
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent motionEvent) {
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null || !this.g.i) {
            return true;
        }
        GeoPoint b = this.g.b((int) motionEvent.getX(), (int) motionEvent.getY());
        if (b != null) {
            Iterator<InterfaceC0081l> it = this.g.f.iterator();
            while (it.hasNext()) {
                it.next().b(b);
            }
            if (this.g.e) {
                E D = this.g.D();
                D.a += 1.0f;
                D.d = b.getLongitudeE6();
                D.e = b.getLatitudeE6();
                this.g.a(D, IjkMediaCodecInfo.RANK_SECURE);
                C0074e.k = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null || !this.g.i) {
            return true;
        }
        if (!this.g.d) {
            return false;
        }
        float sqrt = (float) Math.sqrt((f * f) + (f2 * f2));
        if (sqrt <= 500.0f) {
            return false;
        }
        this.g.z();
        this.g.a(34, (int) (sqrt * 0.6f), ((int) motionEvent2.getX()) | (((int) motionEvent2.getY()) << 16));
        this.g.L();
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null || !this.g.i) {
            return;
        }
        String a2 = this.g.g.a(-1, (int) motionEvent.getX(), (int) motionEvent.getY(), this.g.j);
        if (a2 == null || a2.equals("")) {
            Iterator<InterfaceC0081l> it = this.g.f.iterator();
            while (it.hasNext()) {
                it.next().c(this.g.b((int) motionEvent.getX(), (int) motionEvent.getY()));
            }
        } else {
            for (InterfaceC0081l interfaceC0081l : this.g.f) {
                if (interfaceC0081l.b(a2)) {
                    this.g.n = true;
                } else {
                    interfaceC0081l.c(this.g.b((int) motionEvent.getX(), (int) motionEvent.getY()));
                }
            }
        }
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        super.onPause();
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        this.g.g.e();
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        super.onResume();
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        Iterator<InterfaceC0081l> it = this.g.f.iterator();
        while (it.hasNext()) {
            it.next().d();
        }
        this.g.g.i();
        this.g.g.f();
        this.g.g.p();
        setRenderMode(1);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0063  */
    @Override // android.view.GestureDetector.OnDoubleTapListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onSingleTapConfirmed(android.view.MotionEvent r7) {
        /*
            r6 = this;
            com.baidu.platform.comapi.map.e r0 = r6.g
            r1 = 1
            if (r0 == 0) goto L9b
            com.baidu.platform.comjni.map.basemap.a r0 = r0.g
            if (r0 == 0) goto L9b
            com.baidu.platform.comapi.map.e r0 = r6.g
            boolean r0 = r0.i
            if (r0 != 0) goto L11
            goto L9b
        L11:
            com.baidu.platform.comapi.map.e r0 = r6.g
            com.baidu.platform.comjni.map.basemap.a r0 = r0.g
            r2 = -1
            float r3 = r7.getX()
            int r3 = (int) r3
            float r4 = r7.getY()
            int r4 = (int) r4
            com.baidu.platform.comapi.map.e r5 = r6.g
            int r5 = r5.j
            java.lang.String r0 = r0.a(r2, r3, r4, r5)
            r2 = 0
            if (r0 == 0) goto L73
            java.lang.String r3 = ""
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L73
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: org.json.JSONException -> L50
            r3.<init>(r0)     // Catch: org.json.JSONException -> L50
            java.lang.String r0 = "px"
            float r2 = r7.getX()     // Catch: org.json.JSONException -> L4d
            int r2 = (int) r2     // Catch: org.json.JSONException -> L4d
            r3.put(r0, r2)     // Catch: org.json.JSONException -> L4d
            java.lang.String r0 = "py"
            float r7 = r7.getY()     // Catch: org.json.JSONException -> L4d
            int r7 = (int) r7     // Catch: org.json.JSONException -> L4d
            r3.put(r0, r7)     // Catch: org.json.JSONException -> L4d
            goto L55
        L4d:
            r7 = move-exception
            r2 = r3
            goto L51
        L50:
            r7 = move-exception
        L51:
            r7.printStackTrace()
            r3 = r2
        L55:
            com.baidu.platform.comapi.map.e r7 = r6.g
            java.util.List<com.baidu.platform.comapi.map.l> r7 = r7.f
            java.util.Iterator r7 = r7.iterator()
        L5d:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L9b
            java.lang.Object r0 = r7.next()
            com.baidu.platform.comapi.map.l r0 = (com.baidu.platform.comapi.map.InterfaceC0081l) r0
            if (r3 == 0) goto L5d
            java.lang.String r2 = r3.toString()
            r0.a(r2)
            goto L5d
        L73:
            com.baidu.platform.comapi.map.e r0 = r6.g
            java.util.List<com.baidu.platform.comapi.map.l> r0 = r0.f
            java.util.Iterator r0 = r0.iterator()
        L7b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L9b
            java.lang.Object r2 = r0.next()
            com.baidu.platform.comapi.map.l r2 = (com.baidu.platform.comapi.map.InterfaceC0081l) r2
            com.baidu.platform.comapi.map.e r3 = r6.g
            float r4 = r7.getX()
            int r4 = (int) r4
            float r5 = r7.getY()
            int r5 = (int) r5
            com.baidu.mapapi.model.inner.GeoPoint r3 = r3.b(r4, r5)
            r2.a(r3)
            goto L7b
        L9b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j.onSingleTapConfirmed(android.view.MotionEvent):boolean");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null) {
            return true;
        }
        super.onTouchEvent(motionEvent);
        Iterator<InterfaceC0081l> it = this.g.f.iterator();
        while (it.hasNext()) {
            it.next().a(motionEvent);
        }
        if (this.f.onTouchEvent(motionEvent)) {
            return true;
        }
        return this.g.a(motionEvent);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        super.surfaceChanged(surfaceHolder, i, i2, i3);
        C0074e c0074e = this.g;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        this.c.a = i2;
        this.c.b = i3;
        this.d = i2;
        this.e = i3;
        this.c.c = 0;
        E D = this.g.D();
        if (D.f == 0 || D.f == -1 || D.f == (D.j.left - D.j.right) / 2) {
            D.f = -1;
        }
        if (D.g == 0 || D.g == -1 || D.g == (D.j.bottom - D.j.top) / 2) {
            D.g = -1;
        }
        D.j.left = 0;
        D.j.top = 0;
        D.j.bottom = i3;
        D.j.right = i2;
        this.g.a(D);
        this.g.a(this.d, this.e);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
        if (surfaceHolder == null || surfaceHolder.getSurface().isValid()) {
            return;
        }
        surfaceDestroyed(surfaceHolder);
    }
}
