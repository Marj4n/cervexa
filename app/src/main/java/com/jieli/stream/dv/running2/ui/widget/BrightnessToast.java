package com.jieli.stream.dv.running2.ui.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.widget.verticalseekbar.VerticalSeekBar;
import com.jieli.stream.dv.running2.util.AppUtils;

/* loaded from: classes.dex */
public class BrightnessToast {
    private static final int maxValue = 255;
    private Activity context;
    private VerticalSeekBar sbBrightness;
    private Toast toast;

    public static int getMaxValue() {
        return 255;
    }

    public BrightnessToast(Activity activity) {
        this.context = activity;
    }

    public void show(int i) {
        int screenBrightness = AppUtils.getScreenBrightness(this.context);
        if (this.toast == null) {
            this.toast = new Toast(this.context);
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.view_brightness, (ViewGroup) null);
            VerticalSeekBar verticalSeekBar = (VerticalSeekBar) inflate.findViewById(R.id.view_brightness_seek_progress);
            this.sbBrightness = verticalSeekBar;
            verticalSeekBar.setMax(255);
            this.sbBrightness.setProgress(screenBrightness);
            this.toast.setView(inflate);
            this.toast.setGravity(17, 0, 0);
            this.toast.setDuration(0);
        }
        int min = Math.min(i + screenBrightness, 255);
        if (min >= 0) {
            this.sbBrightness.setProgress(min);
            AppUtils.setBrightness(this.context, min);
        } else {
            this.sbBrightness.setProgress(screenBrightness);
        }
        this.toast.show();
    }

    public void showBrightnessProgress(int i) {
        Window window = this.context.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i2 = (int) (attributes.screenBrightness * 100.0f);
        int i3 = 0;
        if (this.toast == null) {
            this.toast = new Toast(this.context);
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.view_brightness, (ViewGroup) null);
            VerticalSeekBar verticalSeekBar = (VerticalSeekBar) inflate.findViewById(R.id.view_brightness_seek_progress);
            this.sbBrightness = verticalSeekBar;
            verticalSeekBar.setMax(100);
            this.sbBrightness.setProgress(i2);
            this.toast.setView(inflate);
            this.toast.setGravity(17, 0, 0);
            this.toast.setDuration(0);
        }
        int min = Math.min(i2 + i, 100);
        if (min >= 0) {
            this.sbBrightness.setProgress(min);
            i3 = min;
        } else {
            this.sbBrightness.setProgress(0);
        }
        attributes.screenBrightness = i3 / 100.0f;
        window.setAttributes(attributes);
        this.toast.show();
    }
}
