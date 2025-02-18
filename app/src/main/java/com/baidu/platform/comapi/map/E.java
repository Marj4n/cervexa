package com.baidu.platform.comapi.map;

import android.os.Bundle;
import com.baidu.mapapi.map.WinRound;
import com.baidu.mapapi.model.inner.Point;
import com.jieli.lib.dv.control.utils.TopicKey;

/* loaded from: classes.dex */
public class E {
    private static final String t = E.class.getSimpleName();
    public double m;
    public double n;
    public int o;
    public String p;
    public float q;
    public boolean r;
    public int s;
    public float a = 12.0f;
    public int b = 0;
    public int c = 0;
    public double d = 1.2958162E7d;
    public double e = 4825907.0d;
    public long h = 0;
    public long i = 0;
    public int f = -1;
    public int g = -1;
    public WinRound j = new WinRound();
    public a k = new a();
    public boolean l = false;

    public class a {
        public long a = 0;
        public long b = 0;
        public long c = 0;
        public long d = 0;
        public Point e = new Point(0, 0);
        public Point f = new Point(0, 0);
        public Point g = new Point(0, 0);
        public Point h = new Point(0, 0);

        public a() {
        }
    }

    public Bundle a(C0074e c0074e) {
        int i;
        if (this.a < c0074e.b) {
            this.a = c0074e.b;
        }
        if (this.a > c0074e.a) {
            this.a = c0074e.a;
        }
        while (true) {
            i = this.b;
            if (i >= 0) {
                break;
            }
            this.b = i + 360;
        }
        this.b = i % 360;
        if (this.c > 0) {
            this.c = 0;
        }
        if (this.c < -45) {
            this.c = -45;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble(TopicKey.LEVEL, this.a);
        bundle.putDouble("rotation", this.b);
        bundle.putDouble("overlooking", this.c);
        bundle.putDouble("centerptx", this.d);
        bundle.putDouble("centerpty", this.e);
        bundle.putInt(TopicKey.LEFT, this.j.left);
        bundle.putInt("right", this.j.right);
        bundle.putInt("top", this.j.top);
        bundle.putInt("bottom", this.j.bottom);
        int i2 = this.f;
        if (i2 >= 0 && this.g >= 0 && i2 <= this.j.right && this.g <= this.j.bottom && this.j.right > 0 && this.j.bottom > 0) {
            int i3 = (this.j.right - this.j.left) / 2;
            int i4 = (this.j.bottom - this.j.top) / 2;
            int i5 = this.f - i3;
            int i6 = this.g - i4;
            long j = i5;
            this.h = j;
            this.i = -i6;
            bundle.putLong("xoffset", j);
            bundle.putLong("yoffset", this.i);
        }
        bundle.putInt("lbx", this.k.e.x);
        bundle.putInt("lby", this.k.e.y);
        bundle.putInt("ltx", this.k.f.x);
        bundle.putInt("lty", this.k.f.y);
        bundle.putInt("rtx", this.k.g.x);
        bundle.putInt("rty", this.k.g.y);
        bundle.putInt("rbx", this.k.h.x);
        bundle.putInt("rby", this.k.h.y);
        bundle.putInt("bfpp", this.l ? 1 : 0);
        bundle.putInt("animation", 1);
        bundle.putInt("animatime", this.o);
        bundle.putString("panoid", this.p);
        bundle.putInt("autolink", 0);
        bundle.putFloat("siangle", this.q);
        bundle.putInt("isbirdeye", this.r ? 1 : 0);
        bundle.putInt("ssext", this.s);
        return bundle;
    }

    public void a(Bundle bundle) {
        this.a = (float) bundle.getDouble(TopicKey.LEVEL);
        this.b = (int) bundle.getDouble("rotation");
        this.c = (int) bundle.getDouble("overlooking");
        this.d = bundle.getDouble("centerptx");
        this.e = bundle.getDouble("centerpty");
        this.j.left = bundle.getInt(TopicKey.LEFT);
        this.j.right = bundle.getInt("right");
        this.j.top = bundle.getInt("top");
        this.j.bottom = bundle.getInt("bottom");
        this.h = bundle.getLong("xoffset");
        this.i = bundle.getLong("yoffset");
        if (this.j.right != 0 && this.j.bottom != 0) {
            int i = (this.j.right - this.j.left) / 2;
            int i2 = (this.j.bottom - this.j.top) / 2;
            int i3 = (int) this.h;
            int i4 = (int) (-this.i);
            this.f = i3 + i;
            this.g = i4 + i2;
        }
        this.k.a = bundle.getLong("gleft");
        this.k.b = bundle.getLong("gright");
        this.k.c = bundle.getLong("gtop");
        this.k.d = bundle.getLong("gbottom");
        if (this.k.a <= -20037508) {
            this.k.a = -20037508L;
        }
        if (this.k.b >= 20037508) {
            this.k.b = 20037508L;
        }
        if (this.k.c >= 20037508) {
            this.k.c = 20037508L;
        }
        if (this.k.d <= -20037508) {
            this.k.d = -20037508L;
        }
        this.k.e.x = bundle.getInt("lbx");
        this.k.e.y = bundle.getInt("lby");
        this.k.f.x = bundle.getInt("ltx");
        this.k.f.y = bundle.getInt("lty");
        this.k.g.x = bundle.getInt("rtx");
        this.k.g.y = bundle.getInt("rty");
        this.k.h.x = bundle.getInt("rbx");
        this.k.h.y = bundle.getInt("rby");
        this.l = bundle.getInt("bfpp") == 1;
        this.m = bundle.getDouble("adapterzoomunit");
        this.n = bundle.getDouble("zoomunit");
        this.p = bundle.getString("panoid");
        this.q = bundle.getFloat("siangle");
        this.r = bundle.getInt("isbirdeye") != 0;
        this.s = bundle.getInt("ssext");
    }
}
