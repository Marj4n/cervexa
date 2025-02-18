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
import android.util.Log;
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
import com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j;
import com.baidu.platform.comapi.map.InterfaceC0081l;
import com.baidu.platform.comapi.map.N;
import com.gizthon.camera.core.OnCameraConnectedListener;
import java.io.File;

/* loaded from: classes.dex */
public final class MapView extends ViewGroup {
    private static final String a = MapView.class.getSimpleName();
    private static String b;
    private static final SparseArray<Integer> n;
    private GestureDetectorOnDoubleTapListenerC0079j c;
    private BaiduMap d;
    private ImageView e;
    private Bitmap f;
    private N g;
    private Point h;
    private Point i;
    private RelativeLayout j;
    private TextView k;
    private TextView l;
    private ImageView m;
    private int o;
    private boolean p;
    private boolean q;
    private float r;
    private InterfaceC0081l s;
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

    public MapView(Context context) {
        super(context);
        this.o = LogoPosition.logoPostionleftBottom.ordinal();
        this.p = true;
        this.q = true;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.o = LogoPosition.logoPostionleftBottom.ordinal();
        this.p = true;
        this.q = true;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.o = LogoPosition.logoPostionleftBottom.ordinal();
        this.p = true;
        this.q = true;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.o = LogoPosition.logoPostionleftBottom.ordinal();
        this.p = true;
        this.q = true;
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
            r9.f = r0
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
            r9.f = r2
        L41:
            android.graphics.Bitmap r0 = r9.f
            if (r0 == 0) goto L56
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.e = r0
            android.graphics.Bitmap r10 = r9.f
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.e
            r9.addView(r10)
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.MapView.a(android.content.Context):void");
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        C0078i.a();
        BMapManager.init();
        a(context, baiduMapOptions, b);
        this.d = new BaiduMap(this.c);
        a(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.g.setVisibility(4);
        }
        c(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.j.setVisibility(4);
        }
        if (baiduMapOptions != null && baiduMapOptions.j != null) {
            this.o = baiduMapOptions.j.ordinal();
        }
        if (baiduMapOptions != null && baiduMapOptions.l != null) {
            this.i = baiduMapOptions.l;
        }
        if (baiduMapOptions == null || baiduMapOptions.k == null) {
            return;
        }
        this.h = baiduMapOptions.k;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        if (baiduMapOptions == null) {
            this.c = new GestureDetectorOnDoubleTapListenerC0079j(context, null, str);
        } else {
            this.c = new GestureDetectorOnDoubleTapListenerC0079j(context, baiduMapOptions.a(), str);
        }
        addView(this.c);
        this.s = new i(this);
        this.c.a().a(this.s);
    }

    private void a(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        int i = layoutParams.width;
        int makeMeasureSpec = i > 0 ? View.MeasureSpec.makeMeasureSpec(i, BasicMeasure.EXACTLY) : View.MeasureSpec.makeMeasureSpec(0, 0);
        int i2 = layoutParams.height;
        view.measure(makeMeasureSpec, i2 > 0 ? View.MeasureSpec.makeMeasureSpec(i2, BasicMeasure.EXACTLY) : View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        float f = this.c.a().D().a;
        if (this.g.a()) {
            this.g.b(f > this.c.a().b);
            this.g.a(f < this.c.a().a);
        }
    }

    private void b(Context context) {
        N n2 = new N(context, false);
        this.g = n2;
        if (n2.a()) {
            this.g.b(new j(this));
            this.g.a(new k(this));
            addView(this.g);
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
        b = str;
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
        int i = this.o;
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? LogoPosition.logoPostionleftBottom : LogoPosition.logoPostionRightTop : LogoPosition.logoPostionRightBottom : LogoPosition.logoPostionCenterTop : LogoPosition.logoPostionCenterBottom : LogoPosition.logoPostionleftTop;
    }

    public final BaiduMap getMap() {
        this.d.a = this;
        return this.d;
    }

    public final int getMapLevel() {
        return n.get((int) this.c.a().D().a).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.x;
    }

    public int getScaleControlViewWidth() {
        return this.y;
    }

    public void onCreate(Context context, Bundle bundle) {
        BaiduMapOptions mapStatus;
        if (bundle == null) {
            return;
        }
        b = bundle.getString("customMapPath");
        if (bundle == null) {
            mapStatus = new BaiduMapOptions();
        } else {
            MapStatus mapStatus2 = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.h != null) {
                this.h = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.i != null) {
                this.i = (Point) bundle.getParcelable("zoomPosition");
            }
            this.p = bundle.getBoolean("mZoomControlEnabled");
            this.q = bundle.getBoolean("mScaleControlEnabled");
            this.o = bundle.getInt("logoPosition");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            mapStatus = new BaiduMapOptions().mapStatus(mapStatus2);
        }
        a(context, mapStatus);
    }

    public final void onDestroy() {
        this.c.b();
        Bitmap bitmap = this.f;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.f.recycle();
            this.f = null;
        }
        this.g.b();
        BMapManager.destroy();
        C0078i.b();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int measuredHeight;
        int measuredWidth;
        int childCount = getChildCount();
        a(this.e);
        float f2 = 1.0f;
        if (((getWidth() - this.t) - this.u) - this.e.getMeasuredWidth() <= 0 || ((getHeight() - this.v) - this.w) - this.e.getMeasuredHeight() <= 0) {
            this.t = 0;
            this.u = 0;
            this.w = 0;
            this.v = 0;
            f = 1.0f;
        } else {
            f2 = ((getWidth() - this.t) - this.u) / getWidth();
            f = ((getHeight() - this.v) - this.w) / getHeight();
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt != null) {
                GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.c;
                if (childAt == gestureDetectorOnDoubleTapListenerC0079j) {
                    gestureDetectorOnDoubleTapListenerC0079j.layout(0, 0, getWidth(), getHeight());
                } else {
                    ImageView imageView = this.e;
                    if (childAt == imageView) {
                        float f3 = f2 * 5.0f;
                        int i6 = (int) (this.t + f3);
                        int i7 = (int) (this.u + f3);
                        float f4 = 5.0f * f;
                        int i8 = (int) (this.v + f4);
                        int i9 = (int) (this.w + f4);
                        int i10 = this.o;
                        if (i10 != 1) {
                            if (i10 == 2) {
                                measuredHeight = getHeight() - i9;
                                i8 = measuredHeight - this.e.getMeasuredHeight();
                            } else if (i10 != 3) {
                                if (i10 == 4) {
                                    measuredHeight = getHeight() - i9;
                                    i8 = measuredHeight - this.e.getMeasuredHeight();
                                } else if (i10 != 5) {
                                    measuredHeight = getHeight() - i9;
                                    measuredWidth = this.e.getMeasuredWidth() + i6;
                                    i8 = measuredHeight - this.e.getMeasuredHeight();
                                } else {
                                    measuredHeight = i8 + imageView.getMeasuredHeight();
                                }
                                measuredWidth = getWidth() - i7;
                                i6 = measuredWidth - this.e.getMeasuredWidth();
                            } else {
                                measuredHeight = i8 + imageView.getMeasuredHeight();
                            }
                            i6 = (((getWidth() - this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                            measuredWidth = (((getWidth() + this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                        } else {
                            measuredHeight = imageView.getMeasuredHeight() + i8;
                            measuredWidth = this.e.getMeasuredWidth() + i6;
                        }
                        this.e.layout(i6, i8, measuredWidth, measuredHeight);
                    } else {
                        N n2 = this.g;
                        if (childAt != n2) {
                            RelativeLayout relativeLayout = this.j;
                            if (childAt == relativeLayout) {
                                a(relativeLayout);
                                Point point = this.h;
                                if (point == null) {
                                    this.y = this.j.getMeasuredWidth();
                                    this.x = this.j.getMeasuredHeight();
                                    int i11 = (int) (this.t + (5.0f * f2));
                                    int height = (getHeight() - ((int) ((this.w + (f * 5.0f)) + 56.0f))) - this.e.getMeasuredHeight();
                                    this.j.layout(i11, height, this.y + i11, this.x + height);
                                } else {
                                    this.j.layout(point.x, this.h.y, this.h.x + this.j.getMeasuredWidth(), this.h.y + this.j.getMeasuredHeight());
                                }
                            } else {
                                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                                if (layoutParams == null) {
                                    Log.e("test", "lp == null");
                                }
                                if (layoutParams instanceof MapViewLayoutParams) {
                                    MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                                    Point a2 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.c.a().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                                    a(childAt);
                                    int measuredWidth2 = childAt.getMeasuredWidth();
                                    int measuredHeight2 = childAt.getMeasuredHeight();
                                    int i12 = (int) (a2.x - (mapViewLayoutParams.d * measuredWidth2));
                                    int i13 = ((int) (a2.y - (mapViewLayoutParams.e * measuredHeight2))) + mapViewLayoutParams.f;
                                    childAt.layout(i12, i13, measuredWidth2 + i12, measuredHeight2 + i13);
                                }
                            }
                        } else if (n2.a()) {
                            a(this.g);
                            Point point2 = this.i;
                            if (point2 == null) {
                                int height2 = (int) (((getHeight() - 15) * f) + this.v);
                                int width = (int) (((getWidth() - 15) * f2) + this.t);
                                int measuredWidth3 = width - this.g.getMeasuredWidth();
                                int measuredHeight3 = height2 - this.g.getMeasuredHeight();
                                if (this.o == 4) {
                                    height2 -= this.e.getMeasuredHeight();
                                    measuredHeight3 -= this.e.getMeasuredHeight();
                                }
                                this.g.layout(measuredWidth3, measuredHeight3, width, height2);
                            } else {
                                this.g.layout(point2.x, this.i.y, this.i.x + this.g.getMeasuredWidth(), this.i.y + this.g.getMeasuredHeight());
                            }
                        }
                    }
                }
            }
        }
    }

    public final void onPause() {
        this.c.onPause();
    }

    public final void onResume() {
        this.c.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.d) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        Point point = this.h;
        if (point != null) {
            bundle.putParcelable("scalePosition", point);
        }
        Point point2 = this.i;
        if (point2 != null) {
            bundle.putParcelable("zoomPosition", point2);
        }
        bundle.putBoolean("mZoomControlEnabled", this.p);
        bundle.putBoolean("mScaleControlEnabled", this.q);
        bundle.putInt("logoPosition", this.o);
        bundle.putInt("paddingLeft", this.t);
        bundle.putInt("paddingTop", this.v);
        bundle.putInt("paddingRight", this.u);
        bundle.putInt("paddingBottom", this.w);
        bundle.putString("customMapPath", b);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.e) {
            return;
        }
        super.removeView(view);
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            this.o = LogoPosition.logoPostionleftBottom.ordinal();
        }
        this.o = logoPosition.ordinal();
        requestLayout();
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.t = i;
        this.v = i2;
        this.u = i3;
        this.w = i4;
    }

    public void setScaleControlPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.h = point;
            requestLayout();
        }
    }

    public final void setZOrderMediaOverlay(boolean z) {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.c;
        if (gestureDetectorOnDoubleTapListenerC0079j == null) {
            return;
        }
        gestureDetectorOnDoubleTapListenerC0079j.setZOrderMediaOverlay(z);
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.i = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.j.setVisibility(z ? 0 : 8);
        this.q = z;
    }

    public void showZoomControls(boolean z) {
        if (this.g.a()) {
            this.g.setVisibility(z ? 0 : 8);
            this.p = z;
        }
    }
}
