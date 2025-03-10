package com.jieli.stream.dv.running2.ui.widget;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gizthon.camera.R;

/* loaded from: classes.dex */
public class VolumeToast {
    private AudioManager mAudioManager;
    private Context mContext;
    private int maxVol;
    private Toast toast;

    public VolumeToast(Context context) {
        this.mContext = context;
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.mAudioManager = audioManager;
        this.maxVol = audioManager.getStreamMaxVolume(3);
    }

    public void show(int i) {
        int streamVolume = this.mAudioManager.getStreamVolume(3);
        if (this.toast == null) {
            this.toast = new Toast(this.mContext);
            View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.view_volume, (ViewGroup) null);
            this.toast.setView(inflate);
            this.toast.setGravity(17, 0, 0);
            this.toast.setDuration(0);
        }
        int min = Math.min(i + streamVolume, this.maxVol);
        if (min >= 0) {
            this.mAudioManager.setStreamVolume(3, min, 0);
        } else {
        }
        this.toast.show();
    }

    public int getMaxVol() {
        return this.maxVol;
    }
}
