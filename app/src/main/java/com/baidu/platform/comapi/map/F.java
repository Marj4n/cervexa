package com.baidu.platform.comapi.map;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0082m;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
public class F extends TextureView implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener, TextureView.SurfaceTextureListener, C0082m.a {
    public static int a;
    public static int b;
    private GestureDetector c;
    private Handler d;
    private C0082m e;
    private C0074e f;

    public F(Context context, C c, String str) {
        super(context);
        this.e = null;
        a(context, c, str);
    }

    private void a(Context context, C c, String str) {
        setSurfaceTextureListener(this);
        if (context == null) {
            throw new RuntimeException("when you create an mapview, the context can not be null");
        }
        this.c = new GestureDetector(context, this);
        EnvironmentUtilities.initAppDirectory(context);
        if (this.f == null) {
            this.f = new C0074e(context, str);
        }
        this.f.a();
        this.f.b();
        this.f.a(c);
        f();
        this.f.a(this.d);
        this.f.e();
    }

    private void f() {
        this.d = new G(this);
    }

    @Override // com.baidu.platform.comapi.map.C0082m.a
    public int a() {
        C0074e c0074e = this.f;
        if (c0074e == null) {
            return 0;
        }
        return MapRenderer.nativeRender(c0074e.h);
    }

    public void a(String str, Rect rect) {
        C0082m c0082m;
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        if (rect != null) {
            int i = rect.left;
            int i2 = b < rect.bottom ? 0 : b - rect.bottom;
            int width = rect.width();
            int height = rect.height();
            if (i < 0 || i2 < 0 || width <= 0 || height <= 0) {
                return;
            }
            if (width > a) {
                width = Math.abs(rect.width()) - (rect.right - a);
            }
            if (height > b) {
                height = Math.abs(rect.height()) - (rect.bottom - b);
            }
            if (i > SysOSUtil.getScreenSizeX() || i2 > SysOSUtil.getScreenSizeY()) {
                this.f.g.a(str, (Bundle) null);
                C0082m c0082m2 = this.e;
                if (c0082m2 != null) {
                    c0082m2.a();
                    return;
                }
                return;
            }
            a = width;
            b = height;
            Bundle bundle = new Bundle();
            bundle.putInt("x", i);
            bundle.putInt("y", i2);
            bundle.putInt("width", width);
            bundle.putInt("height", height);
            this.f.g.a(str, bundle);
            c0082m = this.e;
            if (c0082m == null) {
                return;
            }
        } else {
            this.f.g.a(str, (Bundle) null);
            c0082m = this.e;
            if (c0082m == null) {
                return;
            }
        }
        c0082m.a();
    }

    public C0074e b() {
        return this.f;
    }

    public void c() {
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        Iterator<InterfaceC0081l> it = this.f.f.iterator();
        while (it.hasNext()) {
            it.next().d();
        }
        this.f.g.i();
        this.f.g.f();
        this.f.g.p();
        C0082m c0082m = this.e;
        if (c0082m != null) {
            c0082m.a();
        }
    }

    public void d() {
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null) {
            return;
        }
        this.f.g.e();
        synchronized (this.f) {
            this.f.g.e();
            if (this.e != null) {
                this.e.b();
            }
        }
    }

    public void e() {
        synchronized (this.f) {
            Iterator<InterfaceC0081l> it = this.f.f.iterator();
            while (it.hasNext()) {
                it.next().f();
            }
            if (this.f != null) {
                this.f.b(this.d);
                this.f.M();
                this.f = null;
            }
            this.d.removeCallbacksAndMessages(null);
        }
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent motionEvent) {
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null || !this.f.i) {
            return true;
        }
        GeoPoint b2 = this.f.b((int) motionEvent.getX(), (int) motionEvent.getY());
        if (b2 != null) {
            Iterator<InterfaceC0081l> it = this.f.f.iterator();
            while (it.hasNext()) {
                it.next().b(b2);
            }
            if (this.f.e) {
                E D = this.f.D();
                D.a += 1.0f;
                D.d = b2.getLongitudeE6();
                D.e = b2.getLatitudeE6();
                this.f.a(D, IjkMediaCodecInfo.RANK_SECURE);
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
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null || !this.f.i) {
            return true;
        }
        if (!this.f.d) {
            return false;
        }
        float sqrt = (float) Math.sqrt((f * f) + (f2 * f2));
        if (sqrt <= 500.0f) {
            return false;
        }
        this.f.z();
        this.f.a(34, (int) (sqrt * 0.6f), ((int) motionEvent2.getX()) | (((int) motionEvent2.getY()) << 16));
        this.f.L();
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null || !this.f.i) {
            return;
        }
        String a2 = this.f.g.a(-1, (int) motionEvent.getX(), (int) motionEvent.getY(), this.f.j);
        if (a2 == null || a2.equals("")) {
            Iterator<InterfaceC0081l> it = this.f.f.iterator();
            while (it.hasNext()) {
                it.next().c(this.f.b((int) motionEvent.getX(), (int) motionEvent.getY()));
            }
        } else {
            for (InterfaceC0081l interfaceC0081l : this.f.f) {
                if (interfaceC0081l.b(a2)) {
                    this.f.n = true;
                } else {
                    interfaceC0081l.c(this.f.b((int) motionEvent.getX(), (int) motionEvent.getY()));
                }
            }
        }
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
            com.baidu.platform.comapi.map.e r0 = r6.f
            r1 = 1
            if (r0 == 0) goto L9b
            com.baidu.platform.comjni.map.basemap.a r0 = r0.g
            if (r0 == 0) goto L9b
            com.baidu.platform.comapi.map.e r0 = r6.f
            boolean r0 = r0.i
            if (r0 != 0) goto L11
            goto L9b
        L11:
            com.baidu.platform.comapi.map.e r0 = r6.f
            com.baidu.platform.comjni.map.basemap.a r0 = r0.g
            r2 = -1
            float r3 = r7.getX()
            int r3 = (int) r3
            float r4 = r7.getY()
            int r4 = (int) r4
            com.baidu.platform.comapi.map.e r5 = r6.f
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
            com.baidu.platform.comapi.map.e r7 = r6.f
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
            com.baidu.platform.comapi.map.e r0 = r6.f
            java.util.List<com.baidu.platform.comapi.map.l> r0 = r0.f
            java.util.Iterator r0 = r0.iterator()
        L7b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L9b
            java.lang.Object r2 = r0.next()
            com.baidu.platform.comapi.map.l r2 = (com.baidu.platform.comapi.map.InterfaceC0081l) r2
            com.baidu.platform.comapi.map.e r3 = r6.f
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
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.F.onSingleTapConfirmed(android.view.MotionEvent):boolean");
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (this.f == null) {
            return;
        }
        C0082m c0082m = new C0082m(surfaceTexture, this, new AtomicBoolean(true), this);
        this.e = c0082m;
        c0082m.start();
        a = i;
        b = i2;
        E D = this.f.D();
        if (D == null) {
            return;
        }
        if (D.f == 0 || D.f == -1 || D.f == (D.j.left - D.j.right) / 2) {
            D.f = -1;
        }
        if (D.g == 0 || D.g == -1 || D.g == (D.j.bottom - D.j.top) / 2) {
            D.g = -1;
        }
        D.j.left = 0;
        D.j.top = 0;
        D.j.bottom = i2;
        D.j.right = i;
        this.f.a(D);
        this.f.a(a, b);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        C0082m c0082m = this.e;
        if (c0082m == null) {
            return true;
        }
        c0082m.c();
        this.e = null;
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        C0074e c0074e = this.f;
        if (c0074e == null) {
            return;
        }
        a = i;
        b = i2;
        c0074e.a(i, i2);
        MapRenderer.nativeResize(this.f.h, i, i2);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        C0074e c0074e = this.f;
        if (c0074e == null || c0074e.g == null) {
            return true;
        }
        super.onTouchEvent(motionEvent);
        Iterator<InterfaceC0081l> it = this.f.f.iterator();
        while (it.hasNext()) {
            it.next().a(motionEvent);
        }
        if (this.c.onTouchEvent(motionEvent)) {
            return true;
        }
        return this.f.a(motionEvent);
    }
}
