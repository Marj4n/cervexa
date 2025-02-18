package com.tencent.open.c;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

/* compiled from: ProGuard */
/* loaded from: classes2.dex */
public class a extends RelativeLayout {
    private static final String a = a.class.getName();
    private Rect b;
    private boolean c;
    private InterfaceC0044a d;

    /* compiled from: ProGuard */
    /* renamed from: com.tencent.open.c.a$a, reason: collision with other inner class name */
    public interface InterfaceC0044a {
        void a();

        void a(int i);
    }

    public a(Context context) {
        super(context);
        this.b = null;
        this.c = false;
        this.d = null;
        if (0 == 0) {
            this.b = new Rect();
        }
    }

    public void a(InterfaceC0044a interfaceC0044a) {
        this.d = interfaceC0044a;
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i2);
        Activity activity = (Activity) getContext();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(this.b);
        int height = (activity.getWindowManager().getDefaultDisplay().getHeight() - this.b.top) - size;
        InterfaceC0044a interfaceC0044a = this.d;
        if (interfaceC0044a != null && size != 0) {
            if (height > 100) {
                interfaceC0044a.a((Math.abs(this.b.height()) - getPaddingBottom()) - getPaddingTop());
            } else {
                interfaceC0044a.a();
            }
        }
        super.onMeasure(i, i2);
    }
}
