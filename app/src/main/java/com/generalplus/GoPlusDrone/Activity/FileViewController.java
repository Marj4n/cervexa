package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.generalplus.ffmpegLib.ffmpegWrapper;
import com.gizthon.camera.R;

import generalplus.com.GPCamLib.CamWrapper;

/* loaded from: classes.dex */
public class FileViewController extends Activity implements SurfaceHolder.Callback {
    private static final int SURFACE_16_9 = 4;
    private static final int SURFACE_4_3 = 5;
    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FILL = 3;
    private static final int SURFACE_FIT_HORIZONTAL = 1;
    private static final int SURFACE_FIT_VERTICAL = 2;
    private static final int SURFACE_ORIGINAL = 6;
    private static String TAG = "FileViewController";
    private static final int VideoSizeChanged = -1;
    private static boolean _bIsPause = false;
    private static boolean _bRunVLC = false;
    private static int mSarDen;
    private static int mSarNum;
    private static int mVideoHeight;
    private static int mVideoVisibleHeight;
    private static int mVideoVisibleWidth;
    private static int mVideoWidth;
    private int _FileFlag;
    private int _FileIndex;
    private String _urlToStream;
    private Handler handlerOverlay;
    private ImageButton imgbtn_playpause;
    private long mLastClickTime;
    private GLSurfaceView mSurfaceView;
    private Context m_Context;
    private Runnable runnableOverlay;
    private LinearLayout vlcContainer;
    private FrameLayout vlcOverlay;
    private int mCurrentSize = 0;
    private final long timeToDisappear = 10000;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_files_vlc_player);
        getWindow().addFlags(128);
        this.m_Context = this;
        this.vlcContainer = (LinearLayout) findViewById(R.id.vlc_container);
        this.mSurfaceView = (GLSurfaceView) findViewById(R.id.vlc_surface);
        this.vlcOverlay = (FrameLayout) findViewById(R.id.vlc_overlay);
        this.imgbtn_playpause = (ImageButton) findViewById(R.id.imgbtn_playpause);
        this.mSurfaceView.setEGLContextClientVersion(2);
        this.mSurfaceView.setRenderer(ffmpegWrapper.getInstance());
        this.mSurfaceView.setKeepScreenOn(true);
    }

    private void setupControls() {
        this.vlcContainer.setVisibility(0);
        this.handlerOverlay = new Handler();
        Runnable runnable = new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FileViewController.1
            @Override // java.lang.Runnable
            public void run() {
                FileViewController.this.vlcOverlay.setVisibility(8);
            }
        };
        this.runnableOverlay = runnable;
        this.handlerOverlay.postDelayed(runnable, 10000L);
        this.vlcOverlay.setVisibility(8);
        toggleFullscreen(true);
        this.vlcContainer.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FileViewController.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FileViewController.this.vlcOverlay.getVisibility() == 0) {
                    FileViewController.this.vlcOverlay.setVisibility(8);
                } else {
                    FileViewController.this.vlcOverlay.setVisibility(0);
                }
                FileViewController.this.handlerOverlay.removeCallbacks(FileViewController.this.runnableOverlay);
                FileViewController.this.handlerOverlay.postDelayed(FileViewController.this.runnableOverlay, 10000L);
            }
        });
        this.imgbtn_playpause.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FileViewController.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FileViewController.this.stopStreaming();
            }
        });
    }

    private void playLocalFile() {
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetStreaming(false);
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naInitAndPlay(this._urlToStream, "");
    }

    private void playVideoStreaming() {
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FileViewController.4
            @Override // java.lang.Runnable
            public void run() {
                ffmpegWrapper.getInstance();
                ffmpegWrapper.naStop();
                CamWrapper.getComWrapperInstance().GPCamSendRestartStreaming();
                CamWrapper.getComWrapperInstance().GPCamSendStartPlayback(FileViewController.this._FileIndex);
                ffmpegWrapper.getInstance();
                ffmpegWrapper.naInitAndPlay(CamWrapper.STREAMING_URL, "");
            }
        }).start();
    }

    private void playPictureStreaming() {
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FileViewController.5
            @Override // java.lang.Runnable
            public void run() {
                ffmpegWrapper.getInstance();
                ffmpegWrapper.naStop();
                CamWrapper.getComWrapperInstance().GPCamClearCommandQueue();
                CamWrapper.getComWrapperInstance().GPCamSendRestartStreaming();
                ffmpegWrapper.getInstance();
                ffmpegWrapper.naInitAndPlay(CamWrapper.STREAMING_URL, "");
                for (int i = 0; i < 5; i++) {
                    CamWrapper.getComWrapperInstance().GPCamSendStartPlayback(FileViewController.this._FileIndex);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopStreaming() {
        if (this._FileFlag == 1 && this._urlToStream.isEmpty()) {
            ffmpegWrapper.getInstance();
            if (ffmpegWrapper.naStatus() == ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal()) {
                CamWrapper.getComWrapperInstance().GPCamSendPausePlayback();
                return;
            } else {
                playVideoStreaming();
                return;
            }
        }
        if (this._FileFlag != 1 || this._urlToStream.isEmpty()) {
            return;
        }
        if (!_bIsPause) {
            ffmpegWrapper.getInstance();
            ffmpegWrapper.naPause();
        } else {
            ffmpegWrapper.getInstance();
            ffmpegWrapper.naResume();
        }
        _bIsPause = !_bIsPause;
    }

    private void toggleFullscreen(boolean z) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (z) {
            attributes.flags |= 1024;
        } else {
            attributes.flags &= -1025;
        }
        getWindow().setAttributes(attributes);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Log.e(TAG, "onConfigurationChanged ...");
        super.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        this._urlToStream = extras.getString(CamWrapper.GPFILECALLBACKTYPE_FILEURL, null);
        this._FileFlag = extras.getInt(CamWrapper.GPFILECALLBACKTYPE_FILEFLAG, 0);
        this._FileIndex = extras.getInt(CamWrapper.GPFILECALLBACKTYPE_FILEINDEX, 0);
        setupControls();
        initStreaming();
        this.imgbtn_playpause.setVisibility(0);
        if (this._urlToStream.isEmpty()) {
            ffmpegWrapper.getInstance();
            ffmpegWrapper.naSetStreaming(true);
            if (this._FileFlag == 2) {
                playPictureStreaming();
                this.imgbtn_playpause.setVisibility(4);
            } else {
                playVideoStreaming();
            }
        } else {
            playLocalFile();
        }
        this.mSurfaceView.onResume();
    }

    @Override // android.app.Activity
    protected void onPause() {
        Log.e(TAG, "onPause ...");
        super.onPause();
        this.mSurfaceView.onPause();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        Finish();
    }

    private void Finish() {
        Log.e(TAG, "Finish ...");
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naStop();
    }

    public void initStreaming() {
        ffmpegWrapper.getInstance();
        if (ffmpegWrapper.naStatus() == ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal()) {
        }
    }
}
