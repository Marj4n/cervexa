package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j;
import com.baidu.platform.comapi.map.InterfaceC0081l;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
class u implements InterfaceC0081l {
    final /* synthetic */ WearMapView a;

    u(WearMapView wearMapView) {
        this.a = wearMapView;
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a() {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j;
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j2;
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j3;
        float f;
        SparseArray sparseArray;
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j4;
        ImageView imageView;
        String format;
        TextView textView;
        TextView textView2;
        gestureDetectorOnDoubleTapListenerC0079j = this.a.d;
        if (gestureDetectorOnDoubleTapListenerC0079j != null) {
            gestureDetectorOnDoubleTapListenerC0079j2 = this.a.d;
            if (gestureDetectorOnDoubleTapListenerC0079j2.a() == null) {
                return;
            }
            gestureDetectorOnDoubleTapListenerC0079j3 = this.a.d;
            float f2 = gestureDetectorOnDoubleTapListenerC0079j3.a().D().a;
            f = this.a.x;
            if (f != f2) {
                sparseArray = WearMapView.u;
                int intValue = ((Integer) sparseArray.get((int) f2)).intValue();
                gestureDetectorOnDoubleTapListenerC0079j4 = this.a.d;
                int i = (int) (intValue / gestureDetectorOnDoubleTapListenerC0079j4.a().D().m);
                imageView = this.a.p;
                int i2 = i / 2;
                imageView.setPadding(i2, 0, i2, 0);
                Object[] objArr = new Object[1];
                if (intValue >= 1000) {
                    objArr[0] = Integer.valueOf(intValue / 1000);
                    format = String.format(" %d公里 ", objArr);
                } else {
                    objArr[0] = Integer.valueOf(intValue);
                    format = String.format(" %d米 ", objArr);
                }
                textView = this.a.n;
                textView.setText(format);
                textView2 = this.a.o;
                textView2.setText(format);
                this.a.x = f2;
            }
            this.a.requestLayout();
        }
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(Bitmap bitmap) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(MotionEvent motionEvent) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(E e) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(String str) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(GL10 gl10, E e) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a(boolean z) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b() {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void b(E e) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public boolean b(String str) {
        return false;
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c() {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void c(E e) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void d() {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void d(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void e() {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void e(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void f() {
    }
}
