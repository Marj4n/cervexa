package com.serenegiant.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ScreenInfo {
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x00b6 -> B:29:0x00ca). Please report as a decompilation issue!!! */
    public static JSONObject get(Activity activity) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            try {
                jSONObject.put("widthPixels", displayMetrics.widthPixels);
            } catch (Exception e) {
                jSONObject.put("widthPixels", e.getMessage());
            }
            try {
                jSONObject.put("heightPixels", displayMetrics.heightPixels);
            } catch (Exception e2) {
                jSONObject.put("heightPixels", e2.getMessage());
            }
            try {
                jSONObject.put("density", displayMetrics.density);
            } catch (Exception e3) {
                jSONObject.put("density", e3.getMessage());
            }
            try {
                jSONObject.put("densityDpi", displayMetrics.densityDpi);
            } catch (Exception e4) {
                jSONObject.put("densityDpi", e4.getMessage());
            }
            try {
                jSONObject.put("scaledDensity", displayMetrics.scaledDensity);
            } catch (Exception e5) {
                jSONObject.put("scaledDensity", e5.getMessage());
            }
            try {
                jSONObject.put("xdpi", displayMetrics.xdpi);
            } catch (Exception e6) {
                jSONObject.put("xdpi", e6.getMessage());
            }
            try {
                jSONObject.put("ydpi", displayMetrics.ydpi);
            } catch (Exception e7) {
                jSONObject.put("ydpi", e7.getMessage());
            }
            try {
                Point point = new Point();
                if (BuildCheck.isAndroid4_2()) {
                    defaultDisplay.getRealSize(point);
                    jSONObject.put("width", point.x);
                    jSONObject.put("height", point.y);
                } else {
                    jSONObject.put("width", defaultDisplay.getWidth());
                    jSONObject.put("height", defaultDisplay.getHeight());
                }
            } catch (Exception e8) {
                jSONObject.put("size", e8.getMessage());
            }
        } catch (Exception e9) {
            jSONObject.put("EXCEPTION", e9.getMessage());
        }
        return jSONObject;
    }
}
