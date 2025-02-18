package com.roughike.bottombar;

import android.content.Context;
import android.content.res.Resources;
import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
final class NavbarUtils {
    private static final int RESOURCE_NOT_FOUND = 0;

    NavbarUtils() {
    }

    static int getNavbarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", IConstant.ANDROID_DIR);
        if (identifier != 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    static boolean shouldDrawBehindNavbar(Context context) {
        return isPortrait(context) && hasSoftKeys(context);
    }

    private static boolean isPortrait(Context context) {
        return context.getResources().getBoolean(R.bool.bb_bottom_bar_is_portrait_mode);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x004a, code lost:
    
        if (r0 == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0030, code lost:
    
        if ((r3 - r5) <= 0) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean hasSoftKeys(android.content.Context r5) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 1
            r3 = 17
            if (r0 < r3) goto L35
            java.lang.String r0 = "window"
            java.lang.Object r5 = r5.getSystemService(r0)
            android.view.WindowManager r5 = (android.view.WindowManager) r5
            android.view.Display r5 = r5.getDefaultDisplay()
            android.util.DisplayMetrics r0 = new android.util.DisplayMetrics
            r0.<init>()
            r5.getRealMetrics(r0)
            int r3 = r0.heightPixels
            int r0 = r0.widthPixels
            android.util.DisplayMetrics r4 = new android.util.DisplayMetrics
            r4.<init>()
            r5.getMetrics(r4)
            int r5 = r4.heightPixels
            int r4 = r4.widthPixels
            int r0 = r0 - r4
            if (r0 > 0) goto L32
            int r3 = r3 - r5
            if (r3 <= 0) goto L33
        L32:
            r1 = 1
        L33:
            r2 = r1
            goto L4d
        L35:
            int r0 = android.os.Build.VERSION.SDK_INT
            r3 = 14
            if (r0 < r3) goto L4d
            android.view.ViewConfiguration r5 = android.view.ViewConfiguration.get(r5)
            boolean r5 = r5.hasPermanentMenuKey()
            r0 = 4
            boolean r0 = android.view.KeyCharacterMap.deviceHasKey(r0)
            if (r5 != 0) goto L33
            if (r0 != 0) goto L33
            goto L32
        L4d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.roughike.bottombar.NavbarUtils.hasSoftKeys(android.content.Context):boolean");
    }
}
