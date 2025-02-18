package com.baidu.platform.comapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.baidu.mapapi.common.SysOSUtil;

/* loaded from: classes.dex */
public class N extends LinearLayout implements View.OnTouchListener {
    private ImageView a;
    private ImageView b;
    private Context c;
    private Bitmap d;
    private Bitmap e;
    private Bitmap f;
    private Bitmap g;
    private Bitmap h;
    private Bitmap i;
    private Bitmap j;
    private Bitmap k;
    private int l;
    private boolean m;
    private boolean n;

    @Deprecated
    public N(Context context) {
        super(context);
        this.m = false;
        this.n = false;
        this.c = context;
        c();
        if (this.d == null || this.e == null || this.f == null || this.g == null) {
            return;
        }
        this.a = new ImageView(this.c);
        this.b = new ImageView(this.c);
        this.a.setImageBitmap(this.d);
        this.b.setImageBitmap(this.f);
        this.l = a(this.f.getHeight() / 6);
        a(this.a, "main_topbtn_up.9.png");
        a(this.b, "main_bottombtn_up.9.png");
        this.a.setId(0);
        this.b.setId(1);
        this.a.setClickable(true);
        this.b.setClickable(true);
        this.a.setOnTouchListener(this);
        this.b.setOnTouchListener(this);
        setOrientation(1);
        setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        addView(this.a);
        addView(this.b);
        this.n = true;
    }

    public N(Context context, boolean z) {
        super(context);
        this.m = false;
        this.n = false;
        this.c = context;
        this.m = z;
        this.a = new ImageView(this.c);
        this.b = new ImageView(this.c);
        if (z) {
            d();
            if (this.h == null || this.i == null || this.j == null || this.k == null) {
                return;
            }
            this.a.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            this.b.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            this.a.setImageBitmap(this.h);
            this.b.setImageBitmap(this.j);
            setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            setOrientation(0);
        } else {
            c();
            Bitmap bitmap = this.d;
            if (bitmap == null || this.e == null || this.f == null || this.g == null) {
                return;
            }
            this.a.setImageBitmap(bitmap);
            this.b.setImageBitmap(this.f);
            this.l = a(this.f.getHeight() / 6);
            a(this.a, "main_topbtn_up.9.png");
            a(this.b, "main_bottombtn_up.9.png");
            setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            setOrientation(1);
        }
        this.a.setId(0);
        this.b.setId(1);
        this.a.setClickable(true);
        this.b.setClickable(true);
        this.a.setOnTouchListener(this);
        this.b.setOnTouchListener(this);
        addView(this.a);
        addView(this.b);
        this.n = true;
    }

    private int a(int i) {
        return (int) ((this.c.getResources().getDisplayMetrics().density * i) + 0.5f);
    }

    private Bitmap a(String str) {
        Matrix matrix = new Matrix();
        int densityDpi = SysOSUtil.getDensityDpi();
        float f = densityDpi > 480 ? 1.8f : (densityDpi <= 320 || densityDpi > 480) ? 1.2f : 1.5f;
        matrix.postScale(f, f);
        Bitmap a = com.baidu.platform.comapi.commonutils.a.a(str, this.c);
        return Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
    }

    private void a(View view, String str) {
        Bitmap a = com.baidu.platform.comapi.commonutils.a.a(str, this.c);
        byte[] ninePatchChunk = a.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        view.setBackgroundDrawable(new NinePatchDrawable(a, ninePatchChunk, new Rect(), null));
        int i = this.l;
        view.setPadding(i, i, i, i);
    }

    private void c() {
        this.d = a("main_icon_zoomin.png");
        this.e = a("main_icon_zoomin_dis.png");
        this.f = a("main_icon_zoomout.png");
        this.g = a("main_icon_zoomout_dis.png");
    }

    private void d() {
        this.h = a("wear_zoom_in.png");
        this.i = a("wear_zoom_in_pressed.png");
        this.j = a("wear_zoon_out.png");
        this.k = a("wear_zoom_out_pressed.png");
    }

    public void a(View.OnClickListener onClickListener) {
        this.a.setOnClickListener(onClickListener);
    }

    public void a(boolean z) {
        ImageView imageView;
        Bitmap bitmap;
        this.a.setEnabled(z);
        if (z) {
            imageView = this.a;
            bitmap = this.d;
        } else {
            imageView = this.a;
            bitmap = this.e;
        }
        imageView.setImageBitmap(bitmap);
    }

    public boolean a() {
        return this.n;
    }

    public void b() {
        Bitmap bitmap = this.d;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.d.recycle();
            this.d = null;
        }
        Bitmap bitmap2 = this.e;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.e.recycle();
            this.e = null;
        }
        Bitmap bitmap3 = this.f;
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            this.f.recycle();
            this.f = null;
        }
        Bitmap bitmap4 = this.g;
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            this.g.recycle();
            this.g = null;
        }
        Bitmap bitmap5 = this.h;
        if (bitmap5 != null && !bitmap5.isRecycled()) {
            this.h.recycle();
            this.h = null;
        }
        Bitmap bitmap6 = this.i;
        if (bitmap6 != null && !bitmap6.isRecycled()) {
            this.i.recycle();
            this.i = null;
        }
        Bitmap bitmap7 = this.j;
        if (bitmap7 != null && !bitmap7.isRecycled()) {
            this.j.recycle();
            this.j = null;
        }
        Bitmap bitmap8 = this.k;
        if (bitmap8 == null || bitmap8.isRecycled()) {
            return;
        }
        this.k.recycle();
        this.k = null;
    }

    public void b(View.OnClickListener onClickListener) {
        this.b.setOnClickListener(onClickListener);
    }

    public void b(boolean z) {
        ImageView imageView;
        Bitmap bitmap;
        this.b.setEnabled(z);
        if (z) {
            imageView = this.b;
            bitmap = this.f;
        } else {
            imageView = this.b;
            bitmap = this.g;
        }
        imageView.setImageBitmap(bitmap);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageView imageView;
        Bitmap bitmap;
        ImageView imageView2;
        String str;
        if (!(view instanceof ImageView)) {
            return false;
        }
        int id = ((ImageView) view).getId();
        if (id == 0) {
            if (motionEvent.getAction() == 0) {
                if (this.m) {
                    imageView = this.a;
                    bitmap = this.i;
                    imageView.setImageBitmap(bitmap);
                    return false;
                }
                imageView2 = this.a;
                str = "main_topbtn_down.9.png";
                a(imageView2, str);
                return false;
            }
            if (motionEvent.getAction() != 1) {
                return false;
            }
            if (this.m) {
                imageView = this.a;
                bitmap = this.h;
                imageView.setImageBitmap(bitmap);
                return false;
            }
            imageView2 = this.a;
            str = "main_topbtn_up.9.png";
            a(imageView2, str);
            return false;
        }
        if (id != 1) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            if (this.m) {
                imageView = this.b;
                bitmap = this.k;
                imageView.setImageBitmap(bitmap);
                return false;
            }
            imageView2 = this.b;
            str = "main_bottombtn_down.9.png";
            a(imageView2, str);
            return false;
        }
        if (motionEvent.getAction() != 1) {
            return false;
        }
        if (this.m) {
            imageView = this.b;
            bitmap = this.j;
            imageView.setImageBitmap(bitmap);
            return false;
        }
        imageView2 = this.b;
        str = "main_bottombtn_up.9.png";
        a(imageView2, str);
        return false;
    }
}
