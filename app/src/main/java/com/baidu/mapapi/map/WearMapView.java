package com.baidu.mapapi.map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
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
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class WearMapView extends ViewGroup implements View.OnApplyWindowInsetsListener {
    public static final int BT_INVIEW = 1;
    private static String c;
    private static final SparseArray<Integer> u;
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    a a;
    private GestureDetectorOnDoubleTapListenerC0079j d;
    private BaiduMap e;
    private ImageView f;
    private Bitmap g;
    private N h;
    private boolean i;
    private Point j;
    private Point k;
    private RelativeLayout l;
    private SwipeDismissView m;
    public AnimationTask mTask;
    public Timer mTimer;
    public b mTimerHandler;
    private TextView n;
    private TextView o;
    private ImageView p;
    private boolean t;
    private boolean v;
    private boolean w;
    private float x;
    private InterfaceC0081l y;
    private int z;
    private static final String b = MapView.class.getSimpleName();
    private static int q = 0;
    private static int r = 0;
    private static int s = 10;

    public class AnimationTask extends TimerTask {
        public AnimationTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message message = new Message();
            message.what = 1;
            WearMapView.this.mTimerHandler.sendMessage(message);
        }
    }

    public interface OnDismissCallback {
        void onDismiss();

        void onNotify();
    }

    enum a {
        ROUND,
        RECTANGLE,
        UNDETECTED
    }

    private class b extends Handler {
        private final WeakReference<Context> b;

        public b(Context context) {
            this.b = new WeakReference<>(context);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (this.b.get() == null) {
                return;
            }
            super.handleMessage(message);
            if (message.what == 1 && WearMapView.this.h != null) {
                WearMapView.this.a(true);
            }
        }
    }

    static {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        u = sparseArray;
        sparseArray.append(3, 2000000);
        u.append(4, 1000000);
        u.append(5, 500000);
        u.append(6, 200000);
        u.append(7, 100000);
        u.append(8, 50000);
        u.append(9, 25000);
        u.append(10, Integer.valueOf(OnCameraConnectedListener.ADMIN_ACTIVE));
        u.append(11, 10000);
        u.append(12, 5000);
        u.append(13, Integer.valueOf(UIMsg.m_AppUI.MSG_APP_DATA_OK));
        u.append(14, 1000);
        u.append(15, 500);
        u.append(16, 200);
        u.append(17, 100);
        u.append(18, 50);
        u.append(19, 20);
        u.append(20, 10);
        u.append(21, 5);
        u.append(22, 2);
    }

    public WearMapView(Context context) {
        super(context);
        this.i = true;
        this.t = true;
        this.a = a.ROUND;
        this.v = true;
        this.w = true;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.i = true;
        this.t = true;
        this.a = a.ROUND;
        this.v = true;
        this.w = true;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.i = true;
        this.t = true;
        this.a = a.ROUND;
        this.v = true;
        this.w = true;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.i = true;
        this.t = true;
        this.a = a.ROUND;
        this.v = true;
        this.w = true;
        a(context, baiduMapOptions);
    }

    private int a(int i, int i2) {
        return i - ((int) Math.sqrt(Math.pow(i, 2.0d) - Math.pow(i2, 2.0d)));
    }

    private void a(int i) {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.d;
        if (gestureDetectorOnDoubleTapListenerC0079j == null) {
            return;
        }
        if (i == 0) {
            gestureDetectorOnDoubleTapListenerC0079j.onPause();
            b();
        } else {
            if (i != 1) {
                return;
            }
            gestureDetectorOnDoubleTapListenerC0079j.onResume();
            c();
        }
    }

    private static void a(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        q = point.x;
        r = point.y;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        AnimationTask animationTask;
        a(context);
        setOnApplyWindowInsetsListener(this);
        this.mTimerHandler = new b(context);
        Timer timer = new Timer();
        this.mTimer = timer;
        if (timer != null && (animationTask = this.mTask) != null) {
            animationTask.cancel();
        }
        AnimationTask animationTask2 = new AnimationTask();
        this.mTask = animationTask2;
        this.mTimer.schedule(animationTask2, 5000L);
        C0078i.a();
        BMapManager.init();
        a(context, baiduMapOptions, c);
        this.e = new BaiduMap(this.d);
        this.d.a().p(false);
        this.d.a().o(false);
        c(context);
        d(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.h.setVisibility(4);
        }
        e(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.l.setVisibility(4);
        }
        if (baiduMapOptions != null && baiduMapOptions.l != null) {
            this.k = baiduMapOptions.l;
        }
        if (baiduMapOptions == null || baiduMapOptions.k == null) {
            return;
        }
        this.j = baiduMapOptions.k;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        if (baiduMapOptions == null) {
            this.d = new GestureDetectorOnDoubleTapListenerC0079j(context, null, str);
        } else {
            this.d = new GestureDetectorOnDoubleTapListenerC0079j(context, baiduMapOptions.a(), str);
        }
        addView(this.d);
        this.y = new u(this);
        this.d.a().a(this.y);
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

    private void a(View view, boolean z) {
        AnimatorSet animatorSet;
        if (z) {
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "TranslationY", 0.0f, -50.0f), ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f));
            animatorSet.addListener(new x(this, view));
        } else {
            view.setVisibility(0);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "TranslationY", -50.0f, 0.0f), ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f));
        }
        animatorSet.setDuration(1200L);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z) {
        if (this.i) {
            a(this.h, z);
        }
    }

    private void b() {
        if (this.d == null || this.t) {
            return;
        }
        d();
        this.t = true;
    }

    private void b(Context context) {
        this.m = new SwipeDismissView(context, this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) ((context.getResources().getDisplayMetrics().density * 34.0f) + 0.5f), r);
        this.m.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.m.setLayoutParams(layoutParams);
        addView(this.m);
    }

    private void c() {
        if (this.d != null && this.t) {
            e();
            this.t = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void c(android.content.Context r10) {
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
            r9.g = r0
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
            r9.g = r2
        L41:
            android.graphics.Bitmap r0 = r9.g
            if (r0 == 0) goto L56
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.f = r0
            android.graphics.Bitmap r10 = r9.g
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.f
            r9.addView(r10)
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.WearMapView.c(android.content.Context):void");
    }

    private void d() {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.d;
        if (gestureDetectorOnDoubleTapListenerC0079j == null) {
            return;
        }
        gestureDetectorOnDoubleTapListenerC0079j.c();
    }

    private void d(Context context) {
        N n = new N(context, true);
        this.h = n;
        if (n.a()) {
            this.h.b(new v(this));
            this.h.a(new w(this));
            addView(this.h);
        }
    }

    private void e() {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.d;
        if (gestureDetectorOnDoubleTapListenerC0079j == null) {
            return;
        }
        gestureDetectorOnDoubleTapListenerC0079j.d();
    }

    private void e(Context context) {
        this.l = new RelativeLayout(context);
        this.l.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.n = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.n.setTextColor(Color.parseColor("#FFFFFF"));
        this.n.setTextSize(2, 11.0f);
        TextView textView = this.n;
        textView.setTypeface(textView.getTypeface(), 1);
        this.n.setLayoutParams(layoutParams);
        this.n.setId(Integer.MAX_VALUE);
        this.l.addView(this.n);
        this.o = new TextView(context);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.addRule(14);
        this.o.setTextColor(Color.parseColor("#000000"));
        this.o.setTextSize(2, 11.0f);
        this.o.setLayoutParams(layoutParams2);
        this.l.addView(this.o);
        this.p = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.width = -2;
        layoutParams3.height = -2;
        layoutParams3.addRule(14);
        layoutParams3.addRule(3, this.n.getId());
        this.p.setLayoutParams(layoutParams3);
        Bitmap a2 = com.baidu.platform.comapi.commonutils.a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a2.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.p.setBackgroundDrawable(new NinePatchDrawable(a2, ninePatchChunk, new Rect(), null));
        this.l.addView(this.p);
        addView(this.l);
    }

    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("customMapStylePath String is illegal");
        }
        if (!new File(str).exists()) {
            throw new RuntimeException("please check whether the customMapStylePath file exits");
        }
        c = str;
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

    public final BaiduMap getMap() {
        this.e.c = this;
        return this.e;
    }

    public final int getMapLevel() {
        return u.get((int) this.d.a().D().a).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.D;
    }

    public int getScaleControlViewWidth() {
        return this.E;
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        this.a = windowInsets.isRound() ? a.ROUND : a.RECTANGLE;
        return windowInsets;
    }

    public void onCreate(Context context, Bundle bundle) {
        BaiduMapOptions mapStatus;
        if (bundle == null) {
            return;
        }
        c = bundle.getString("customMapPath");
        if (bundle == null) {
            mapStatus = new BaiduMapOptions();
        } else {
            MapStatus mapStatus2 = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.j != null) {
                this.j = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.k != null) {
                this.k = (Point) bundle.getParcelable("zoomPosition");
            }
            this.v = bundle.getBoolean("mZoomControlEnabled");
            this.w = bundle.getBoolean("mScaleControlEnabled");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            mapStatus = new BaiduMapOptions().mapStatus(mapStatus2);
        }
        a(context, mapStatus);
    }

    public final void onDestroy() {
        this.d.b();
        Bitmap bitmap = this.g;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.g.recycle();
            this.g = null;
        }
        this.h.b();
        BMapManager.destroy();
        C0078i.b();
        AnimationTask animationTask = this.mTask;
        if (animationTask != null) {
            animationTask.cancel();
        }
    }

    public final void onDismiss() {
        removeAllViews();
    }

    public final void onEnterAmbient(Bundle bundle) {
        a(0);
    }

    public void onExitAmbient() {
        a(1);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        AnimationTask animationTask;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                Timer timer = new Timer();
                this.mTimer = timer;
                if (timer != null && (animationTask = this.mTask) != null) {
                    animationTask.cancel();
                }
                AnimationTask animationTask2 = new AnimationTask();
                this.mTask = animationTask2;
                this.mTimer.schedule(animationTask2, 5000L);
            }
        } else if (this.h.getVisibility() == 0) {
            Timer timer2 = this.mTimer;
            if (timer2 != null) {
                if (this.mTask != null) {
                    timer2.cancel();
                    this.mTask.cancel();
                }
                this.mTimer = null;
                this.mTask = null;
            }
        } else if (this.h.getVisibility() == 4) {
            if (this.mTimer != null) {
                AnimationTask animationTask3 = this.mTask;
                if (animationTask3 != null) {
                    animationTask3.cancel();
                }
                this.mTimer.cancel();
                this.mTask = null;
                this.mTimer = null;
            }
            a(false);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int i5;
        int i6;
        int i7;
        int i8;
        int childCount = getChildCount();
        a(this.f);
        float f2 = 1.0f;
        if (((getWidth() - this.z) - this.A) - this.f.getMeasuredWidth() <= 0 || ((getHeight() - this.B) - this.C) - this.f.getMeasuredHeight() <= 0) {
            this.z = 0;
            this.A = 0;
            this.C = 0;
            this.B = 0;
            f = 1.0f;
        } else {
            f2 = ((getHeight() - this.B) - this.C) / getHeight();
            f = ((getWidth() - this.z) - this.A) / getWidth();
        }
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j = this.d;
            if (childAt == gestureDetectorOnDoubleTapListenerC0079j) {
                gestureDetectorOnDoubleTapListenerC0079j.layout(0, 0, getWidth(), getHeight());
            } else if (childAt == this.f) {
                int i10 = (int) (this.C + (12.0f * f2));
                if (this.a == a.ROUND) {
                    a(this.h);
                    int i11 = q / 2;
                    i7 = a(i11, this.h.getMeasuredWidth() / 2);
                    i8 = ((q / 2) - a(i11, i11 - i7)) + s;
                } else {
                    i7 = 0;
                    i8 = 0;
                }
                int i12 = (r - i7) - i10;
                int measuredHeight = i12 - this.f.getMeasuredHeight();
                int i13 = q - i8;
                this.f.layout(i13 - this.f.getMeasuredWidth(), measuredHeight, i13, i12);
            } else {
                N n = this.h;
                if (childAt == n) {
                    if (n.a()) {
                        a(this.h);
                        Point point = this.k;
                        if (point == null) {
                            int a2 = (int) ((12.0f * f2) + this.B + (this.a == a.ROUND ? a(r / 2, this.h.getMeasuredWidth() / 2) : 0));
                            int measuredWidth = (q - this.h.getMeasuredWidth()) / 2;
                            this.h.layout(measuredWidth, a2, this.h.getMeasuredWidth() + measuredWidth, this.h.getMeasuredHeight() + a2);
                        } else {
                            this.h.layout(point.x, this.k.y, this.k.x + this.h.getMeasuredWidth(), this.k.y + this.h.getMeasuredHeight());
                        }
                    }
                } else if (childAt == this.l) {
                    if (this.a == a.ROUND) {
                        a(this.h);
                        int i14 = q / 2;
                        i5 = a(i14, this.h.getMeasuredWidth() / 2);
                        i6 = ((q / 2) - a(i14, i14 - i5)) + s;
                    } else {
                        i5 = 0;
                        i6 = 0;
                    }
                    a(this.l);
                    Point point2 = this.j;
                    if (point2 == null) {
                        this.E = this.l.getMeasuredWidth();
                        this.D = this.l.getMeasuredHeight();
                        int i15 = (int) (this.z + (5.0f * f) + i6);
                        int i16 = (r - ((int) (this.C + (12.0f * f2)))) - i5;
                        this.l.layout(i15, i16 - this.l.getMeasuredHeight(), this.E + i15, i16);
                    } else {
                        this.l.layout(point2.x, this.j.y, this.j.x + this.l.getMeasuredWidth(), this.j.y + this.l.getMeasuredHeight());
                    }
                } else {
                    SwipeDismissView swipeDismissView = this.m;
                    if (childAt == swipeDismissView) {
                        a(swipeDismissView);
                        this.m.layout(0, 0, this.m.getMeasuredWidth(), r);
                    } else {
                        ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                        if (layoutParams instanceof MapViewLayoutParams) {
                            MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                            Point a3 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.d.a().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                            a(childAt);
                            int measuredWidth2 = childAt.getMeasuredWidth();
                            int measuredHeight2 = childAt.getMeasuredHeight();
                            float f3 = mapViewLayoutParams.d;
                            int i17 = (int) (a3.x - (f3 * measuredWidth2));
                            int i18 = ((int) (a3.y - (mapViewLayoutParams.e * measuredHeight2))) + mapViewLayoutParams.f;
                            childAt.layout(i17, i18, measuredWidth2 + i17, measuredHeight2 + i18);
                        }
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.e) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        Point point = this.j;
        if (point != null) {
            bundle.putParcelable("scalePosition", point);
        }
        Point point2 = this.k;
        if (point2 != null) {
            bundle.putParcelable("zoomPosition", point2);
        }
        bundle.putBoolean("mZoomControlEnabled", this.v);
        bundle.putBoolean("mScaleControlEnabled", this.w);
        bundle.putInt("paddingLeft", this.z);
        bundle.putInt("paddingTop", this.B);
        bundle.putInt("paddingRight", this.A);
        bundle.putInt("paddingBottom", this.C);
        bundle.putString("customMapPath", c);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.f) {
            return;
        }
        super.removeView(view);
    }

    public void setOnDismissCallbackListener(OnDismissCallback onDismissCallback) {
        SwipeDismissView swipeDismissView = this.m;
        if (swipeDismissView == null) {
            return;
        }
        swipeDismissView.setCallback(onDismissCallback);
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.z = i;
        this.B = i2;
        this.A = i3;
        this.C = i4;
    }

    public void setScaleControlPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.j = point;
            requestLayout();
        }
    }

    public void setShape(a aVar) {
        this.a = aVar;
    }

    public void setViewAnimitionEnable(boolean z) {
        this.i = z;
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.k = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.l.setVisibility(z ? 0 : 8);
        this.w = z;
    }

    public void showZoomControls(boolean z) {
        if (this.h.a()) {
            this.h.setVisibility(z ? 0 : 8);
            this.v = z;
        }
    }
}
