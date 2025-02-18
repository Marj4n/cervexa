package com.jieli.stream.dv.running2.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* loaded from: classes.dex */
public class MediaPlayerService extends Service {
    private static IMediaPlayer sMediaPlayer;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, (Class<?>) MediaPlayerService.class);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    public static void intentToStart(Context context) {
        context.startService(newIntent(context));
    }

    public static void intentToStop(Context context) {
        context.stopService(newIntent(context));
    }

    public static void setMediaPlayer(IMediaPlayer iMediaPlayer) {
        IMediaPlayer iMediaPlayer2 = sMediaPlayer;
        if (iMediaPlayer2 != null && iMediaPlayer2 != iMediaPlayer) {
            if (iMediaPlayer2.isPlaying()) {
                sMediaPlayer.stop();
            }
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
        sMediaPlayer = iMediaPlayer;
    }

    public static IMediaPlayer getMediaPlayer() {
        return sMediaPlayer;
    }
}
