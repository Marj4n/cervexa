package com.baidu.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.UIMsg;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.C0078i;
import com.baidu.platform.comapi.map.F;
import com.baidu.platform.comapi.map.InterfaceC0081l;
import com.baidu.platform.comapi.map.N;
import com.gizthon.camera.core.OnCameraConnectedListener;
import java.io.File;

/* loaded from: classes.dex */
public final class TextureMapView extends ViewGroup {
    private static final String a = TextureMapView.class.getSimpleName();
    private static String i;
    private static final SparseArray<Integer> n;
    private F b;
    private BaiduMap c;
    private ImageView d;
    private Bitmap e;
    private N f;
    private Point g;
    private Point h;
    private RelativeLayout j;
    private TextView k;
    private TextView l;
    private ImageView m;
    private float o;
    private InterfaceC0081l p;
    private int q;
    private boolean r;
    private boolean s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;

    static {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        n = sparseArray;
        sparseArray.append(3, 2000000);
        n.append(4, 1000000);
        n.append(5, 500000);
        n.append(6, 200000);
        n.append(7, 100000);
        n.append(8, 50000);
        n.append(9, 25000);
        n.append(10, Integer.valueOf(OnCameraConnectedListener.ADMIN_ACTIVE));
        n.append(11, 10000);
        n.append(12, 5000);
        n.append(13, Integer.valueOf(UIMsg.m_AppUI.MSG_APP_DATA_OK));
        n.append(14, 1000);
        n.append(15, 500);
        n.append(16, 200);
        n.append(17, 100);
        n.append(18, 50);
        n.append(19, 20);
        n.append(20, 10);
        n.append(21, 5);
        n.append(22, 2);
    }

    public TextureMapView(Context context) {
        super(context);
        this.q = LogoPosition.logoPostionleftBottom.ordinal();
        this.r = true;
        this.s = true;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.q = LogoPosition.logoPostionleftBottom.ordinal();
        this.r = true;
        this.s = true;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.q = LogoPosition.logoPostionleftBottom.ordinal();
        this.r = true;
        this.s = true;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.q = LogoPosition.logoPostionleftBottom.ordinal();
        this.r = true;
        this.s = true;
        a(context, baiduMapOptions);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(android.content.Context r10) {
        /*
            r9 = this;
            int r0 = com.baidu.mapapi.common.SysOSUtil.getDensityDpi()
            r1 = 180(0xb4, float:2.52E-43)
            if (r0 >= r1) goto Lb
            java.lang.String r1 = "logo_l.png"
            goto Ld
        Lb:
            java.lang.String r1 = "logo_h.png"
        Ld:
            android.graphics.Bitmap r2 = com.baidu.platform.comapi.commonutils.a.a(r1, r10)
            r1 = 480(0x1e0, float:6.73E-43)
            if (r0 <= r1) goto L31
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1073741824(0x40000000, float:2.0)
        L1c:
            r7.postScale(r0, r0)
            r3 = 0
            r4 = 0
            int r5 = r2.getWidth()
            int r6 = r2.getHeight()
            r8 = 1
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r2, r3, r4, r5, r6, r7, r8)
            r9.e = r0
            goto L41
        L31:
            r3 = 320(0x140, float:4.48E-43)
            if (r0 <= r3) goto L3f
            if (r0 > r1) goto L3f
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1069547520(0x3fc00000, float:1.5)
            goto L1c
        L3f:
            r9.e = r2
        L41:
            android.graphics.Bitmap r0 = r9.e
            if (r0 == 0) goto L56
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.d = r0
            android.graphics.Bitmap r10 = r9.e
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.d
            r9.addView(r10)
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.TextureMapView.a(android.content.Context):void");
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        setBackgroundColor(-1);
        C0078i.a();
        BMapManager.init();
        a(context, baiduMapOptions, i);
        this.c = new BaiduMap(this.b);
        a(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.f.setVisibility(4);
        }
        c(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.j.setVisibility(4);
        }
        if (baiduMapOptions != null && baiduMapOptions.j != null) {
            this.q = baiduMapOptions.j.ordinal();
        }
        if (baiduMapOptions != null && baiduMapOptions.l != null) {
            this.h = baiduMapOptions.l;
        }
        if (baiduMapOptions == null || baiduMapOptions.k == null) {
            return;
        }
        this.g = baiduMapOptions.k;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        i = str;
        if (baiduMapOptions == null) {
            this.b = new F(context, null, str);
        } else {
            this.b = new F(context, baiduMapOptions.a(), str);
        }
        addView(this.b);
        this.p = new q(this);
        this.b.b().a(this.p);
    }

    private void a(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        int i2 = layoutParams.width;
        int makeMeasureSpec = i2 > 0 ? View.MeasureSpec.makeMeasureSpec(i2, BasicMeasure.EXACTLY) : View.MeasureSpec.makeMeasureSpec(0, 0);
        int i3 = layoutParams.height;
        view.measure(makeMeasureSpec, i3 > 0 ? View.MeasureSpec.makeMeasureSpec(i3, BasicMeasure.EXACTLY) : View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        float f = this.b.b().D().a;
        if (this.f.a()) {
            this.f.b(f > this.b.b().b);
            this.f.a(f < this.b.b().a);
        }
    }

    private void b(Context context) {
        N n2 = new N(context);
        this.f = n2;
        if (n2.a()) {
            this.f.b(new r(this));
            this.f.a(new s(this));
            addView(this.f);
        }
    }

    private void c(Context context) {
        this.j = new RelativeLayout(context);
        this.j.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.k = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.k.setTextColor(Color.parseColor("#FFFFFF"));
        this.k.setTextSize(2, 11.0f);
        TextView textView = this.k;
        textView.setTypeface(textView.getTypeface(), 1);
        this.k.setLayoutParams(layoutParams);
        this.k.setId(Integer.MAX_VALUE);
        this.j.addView(this.k);
        this.l = new TextView(context);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.addRule(14);
        this.l.setTextColor(Color.parseColor("#000000"));
        this.l.setTextSize(2, 11.0f);
        this.l.setLayoutParams(layoutParams2);
        this.j.addView(this.l);
        this.m = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.width = -2;
        layoutParams3.height = -2;
        layoutParams3.addRule(14);
        layoutParams3.addRule(3, this.k.getId());
        this.m.setLayoutParams(layoutParams3);
        Bitmap a2 = com.baidu.platform.comapi.commonutils.a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a2.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.m.setBackgroundDrawable(new NinePatchDrawable(a2, ninePatchChunk, new Rect(), null));
        this.j.addView(this.m);
        addView(this.j);
    }

    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("customMapStylePath String is illegal");
        }
        if (!new File(str).exists()) {
            throw new RuntimeException("please check whether the customMapStylePath file exits");
        }
        i = str;
    }

    public static void setMapCustomEnable(boolean z) {
        C0078i.a(z);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof MapViewLayoutParams) {
            super.addView(view, layoutParams);
        }
    }

    public final LogoPosition getLogoPosition() {
        int i2 = this.q;
        return i2 != 1 ? i2 != 2 ? i2 != 3 ? i2 != 4 ? i2 != 5 ? LogoPosition.logoPostionleftBottom : LogoPosition.logoPostionRightTop : LogoPosition.logoPostionRightBottom : LogoPosition.logoPostionCenterTop : LogoPosition.logoPostionCenterBottom : LogoPosition.logoPostionleftTop;
    }

    public final BaiduMap getMap() {
        this.c.b = this;
        return this.c;
    }

    public final int getMapLevel() {
        return n.get((int) this.b.b().D().a).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.y;
    }

    public int getScaleControlViewWidth() {
        return this.y;
    }

    public void onCreate(Context context, Bundle bundle) {
        BaiduMapOptions mapStatus;
        if (bundle == null) {
            return;
        }
        i = bundle.getString("customMapPath");
        if (bundle == null) {
            mapStatus = new BaiduMapOptions();
        } else {
            MapStatus mapStatus2 = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.g != null) {
                this.g = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.h != null) {
                this.h = (Point) bundle.getParcelable("zoomPosition");
            }
            this.r = bundle.getBoolean("mZoomControlEnabled");
            this.s = bundle.getBoolean("mScaleControlEnabled");
            this.q = bundle.getInt("logoPosition");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            mapStatus = new BaiduMapOptions().mapStatus(mapStatus2);
        }
        a(context, mapStatus);
    }

    public final void onDestroy() {
        this.b.e();
        Bitmap bitmap = this.e;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.e.recycle();
        }
        this.f.b();
        BMapManager.destroy();
        C0078i.b();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected final void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        float f;
        int measuredHeight;
        int measuredWidth;
        int childCount = getChildCount();
        a(this.d);
        float f2 = 1.0f;
        if (((getWidth() - this.t) - this.u) - this.d.getMeasuredWidth() <= 0 || ((getHeight() - this.v) - this.w) - this.d.getMeasuredHeight() <= 0) {
            this.t = 0;
            this.u = 0;
            this.w = 0;
            this.v = 0;
            f = 1.0f;
        } else {
            f2 = ((getWidth() - this.t) - this.u) / getWidth();
            f = ((getHeight() - this.v) - this.w) / getHeight();
        }
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            F f3 = this.b;
            if (childAt == f3) {
                f3.layout(0, 0, getWidth(), getHeight());
            } else {
                ImageView imageView = this.d;
                if (childAt == imageView) {
                    float f4 = f2 * 5.0f;
                    int i7 = (int) (this.t + f4);
                    int i8 = (int) (this.u + f4);
                    float f5 = 5.0f * f;
                    int i9 = (int) (this.v + f5);
                    int i10 = (int) (this.w + f5);
                    int i11 = this.q;
                    if (i11 != 1) {
                        if (i11 == 2) {
                            measuredHeight = getHeight() - i10;
                            i9 = measuredHeight - this.d.getMeasuredHeight();
                        } else if (i11 != 3) {
                            if (i11 == 4) {
                                measuredHeight = getHeight() - i10;
                                i9 = measuredHeight - this.d.getMeasuredHeight();
                            } else if (i11 != 5) {
                                measuredHeight = getHeight() - i10;
                                measuredWidth = this.d.getMeasuredWidth() + i7;
                                i9 = measuredHeight - this.d.getMeasuredHeight();
                            } else {
                                measuredHeight = i9 + imageView.getMeasuredHeight();
                            }
                            measuredWidth = getWidth() - i8;
                            i7 = measuredWidth - this.d.getMeasuredWidth();
                        } else {
                            measuredHeight = i9 + imageView.getMeasuredHeight();
                        }
                        i7 = (((getWidth() - this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                        measuredWidth = (((getWidth() + this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                    } else {
                        measuredHeight = imageView.getMeasuredHeight() + i9;
                        measuredWidth = this.d.getMeasuredWidth() + i7;
                    }
                    this.d.layout(i7, i9, measuredWidth, measuredHeight);
                } else {
                    N n2 = this.f;
                    if (childAt != n2) {
                        RelativeLayout relativeLayout = this.j;
                        if (childAt == relativeLayout) {
                            a(relativeLayout);
                            Point point = this.g;
                            if (point == null) {
                                this.y = this.j.getMeasuredWidth();
                                this.x = this.j.getMeasuredHeight();
                                int i12 = (int) (this.t + (5.0f * f2));
                                int height = (getHeight() - ((int) ((this.w + (f * 5.0f)) + 56.0f))) - this.d.getMeasuredHeight();
                                this.j.layout(i12, height, this.y + i12, this.x + height);
                            } else {
                                this.j.layout(point.x, this.g.y, this.g.x + this.j.getMeasuredWidth(), this.g.y + this.j.getMeasuredHeight());
                            }
                        } else {
                            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                            if (layoutParams instanceof MapViewLayoutParams) {
                                MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                                Point a2 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.b.b().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                                a(childAt);
                                int measuredWidth2 = childAt.getMeasuredWidth();
                                int measuredHeight2 = childAt.getMeasuredHeight();
                                int i13 = (int) (a2.x - (mapViewLayoutParams.d * measuredWidth2));
                                int i14 = ((int) (a2.y - (mapViewLayoutParams.e * measuredHeight2))) + mapViewLayoutParams.f;
                                childAt.layout(i13, i14, measuredWidth2 + i13, measuredHeight2 + i14);
                            }
                        }
                    } else if (n2.a()) {
                        a(this.f);
                        Point point2 = this.h;
                        if (point2 == null) {
                            int height2 = (int) (((getHeight() - 15) * f) + this.v);
                            int width = (int) (((getWidth() - 15) * f2) + this.t);
                            int measuredWidth3 = width - this.f.getMeasuredWidth();
                            int measuredHeight3 = height2 - this.f.getMeasuredHeight();
                            if (this.q == 4) {
                                height2 -= this.d.getMeasuredHeight();
                                measuredHeight3 -= this.d.getMeasuredHeight();
                            }
                            this.f.layout(measuredWidth3, measuredHeight3, width, height2);
                        } else {
                            this.f.layout(point2.x, this.h.y, this.h.x + this.f.getMeasuredWidth(), this.h.y + this.f.getMeasuredHeight());
                        }
                    }
                }
            }
        }
    }

    public final void onPause() {
        this.b.d();
    }

    public final void onResume() {
        this.b.c();
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.c) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        Point point = this.g;
        if (point != null) {
            bundle.putParcelable("scalePosition", point);
        }
        Point point2 = this.h;
        if (point2 != null) {
            bundle.putParcelable("zoomPosition", point2);
        }
        bundle.putBoolean("mZoomControlEnabled", this.r);
        bundle.putBoolean("mScaleControlEnabled", this.s);
        bundle.putInt("logoPosition", this.q);
        bundle.putInt("paddingLeft", this.t);
        bundle.putInt("paddingTop", this.v);
        bundle.putInt("paddingRight", this.u);
        bundle.putInt("paddingBottom", this.w);
        bundle.putString("customMapPath", i);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.d) {
            return;
        }
        super.removeView(view);
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            this.q = LogoPosition.logoPostionleftBottom.ordinal();
        }
        this.q = logoPosition.ordinal();
        requestLayout();
    }

    @Override // android.view.View
    public void setPadding(int i2, int i3, int i4, int i5) {
        this.t = i2;
        this.v = i3;
        this.u = i4;
        this.w = i5;
    }

    public void setScaleControlPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.g = point;
            requestLayout();
        }
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.h = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.j.setVisibility(z ? 0 : 8);
        this.s = z;
    }

    public void showZoomControls(boolean z) {
        if (this.f.a()) {
            this.f.setVisibility(z ? 0 : 8);
            this.r = z;
        }
    }
}
