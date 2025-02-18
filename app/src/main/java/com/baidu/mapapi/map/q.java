package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.F;
import com.baidu.platform.comapi.map.InterfaceC0081l;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
class q implements InterfaceC0081l {
    final /* synthetic */ TextureMapView a;

    q(TextureMapView textureMapView) {
        this.a = textureMapView;
    }

    @Override // com.baidu.platform.comapi.map.InterfaceC0081l
    public void a() {
        F f;
        F f2;
        F f3;
        float f4;
        SparseArray sparseArray;
        F f5;
        ImageView imageView;
        String format;
        TextView textView;
        TextView textView2;
        f = this.a.b;
        if (f != null) {
            f2 = this.a.b;
            if (f2.b() == null) {
                return;
            }
            f3 = this.a.b;
            float f6 = f3.b().D().a;
            f4 = this.a.o;
            if (f4 != f6) {
                sparseArray = TextureMapView.n;
                int intValue = ((Integer) sparseArray.get((int) f6)).intValue();
                f5 = this.a.b;
                int i = (int) (intValue / f5.b().D().m);
                imageView = this.a.m;
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
                textView = this.a.k;
                textView.setText(format);
                textView2 = this.a.l;
                textView2.setText(format);
                this.a.o = f6;
            }
            this.a.b();
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
