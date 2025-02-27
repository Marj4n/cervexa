package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.NotificationManagerCompat;

import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.widget.BrightnessToast;
import com.jieli.stream.dv.running2.ui.widget.VideoProgressToast;
import com.jieli.stream.dv.running2.ui.widget.VolumeToast;
import com.jieli.stream.dv.running2.ui.widget.media.IjkVideoView;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.TimeFormate;
import com.jieli.stream.dv.running2.util.ToastUtil;

import java.io.File;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class VideoPlayerFragment extends BaseFragment implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final int MSG_HIDE_BAR_UI = 177;
    private static final int MSG_UPDATE_PROGRESS = 178;
    private static final int MSG_VIDEO_PAUSE = 179;
    private static final int MSG_VIDEO_RESUME = 180;
    public static final int OP_DOWN = 60914;
    public static final int OP_UP = 60913;
    private static final long TIME_UPDATE = 200;
    private RelativeLayout bottomBar;
    private BrightnessToast brightnessToast;
    private boolean isFastPlay;
    private boolean isPausing;
    private boolean isPreparing;
    private ImageView ivFullScreen;
    private ImageView ivPlayOrPause;
    private float mLastMotionX;
    private float mLastMotionY;
    private int pauseTime;
    private boolean playerSupport;
    private SeekBar sbProgress;
    private int screenHeight;
    private int screenWidth;
    private int startX;
    private int startY;
    private int threshold;
    private RelativeLayout topBar;
    private TextView tvCurrentTime;
    private TextView tvTitle;
    private TextView tvTotalTime;
    private String videoPath;
    private VideoProgressToast videoProgressToast;
    private IjkVideoView videoView;
    private VolumeToast volumeToast;
    private boolean isClick = true;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (VideoPlayerFragment.this.getActivity() != null && message != null) {
                switch (message.what) {
                    case VideoPlayerFragment.MSG_HIDE_BAR_UI /* 177 */:
                        VideoPlayerFragment.this.showOrHideBar();
                        break;
                    case VideoPlayerFragment.MSG_UPDATE_PROGRESS /* 178 */:
                        if (message.arg1 == VideoPlayerFragment.this.videoView.getDuration()) {
                            VideoPlayerFragment.this.ivPlayOrPause.setImageResource(R.drawable.drawable_btn_play);
                        }
                        int duration = VideoPlayerFragment.this.videoView.getDuration();
                        int floor = (int) Math.floor(duration / 1000.0f);
                        VideoPlayerFragment.this.sbProgress.setProgress(floor);
                        VideoPlayerFragment.this.tvCurrentTime.setText(TimeFormate.getTimeFormatValue(floor));
                        if (VideoPlayerFragment.this.isPlaying() && VideoPlayerFragment.this.mHandler != null) {
                            VideoPlayerFragment.this.mHandler.sendMessageDelayed(VideoPlayerFragment.this.mHandler.obtainMessage(VideoPlayerFragment.MSG_UPDATE_PROGRESS, VideoPlayerFragment.this.videoView.getCurrentPosition(), 0), VideoPlayerFragment.TIME_UPDATE);
                            break;
                        }
                        break;
                    case VideoPlayerFragment.MSG_VIDEO_PAUSE /* 179 */:
                        VideoPlayerFragment.this.ivPlayOrPause.setImageResource(R.drawable.drawable_btn_play);
                        VideoPlayerFragment.this.videoView.requestFocus();
                        break;
                    case VideoPlayerFragment.MSG_VIDEO_RESUME /* 180 */:
                        VideoPlayerFragment.this.ivPlayOrPause.setImageResource(R.drawable.drawable_btn_pause);
                        VideoPlayerFragment.this.videoView.requestFocus();
                        VideoPlayerFragment.this.tvCurrentTime.setText(VideoPlayerFragment.this.getString(R.string.default_time_format));
                        break;
                }
            }
            return false;
        }
    });

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_player, viewGroup, false);
        this.videoView = (IjkVideoView) inflate.findViewById(R.id.video_player_media);
        this.topBar = (RelativeLayout) inflate.findViewById(R.id.video_player_top_layout);
        this.bottomBar = (RelativeLayout) inflate.findViewById(R.id.video_player_bottom_bar);
        this.tvTitle = (TextView) inflate.findViewById(R.id.video_player_top_tv);
        this.tvCurrentTime = (TextView) inflate.findViewById(R.id.video_player_current_time);
        this.tvTotalTime = (TextView) inflate.findViewById(R.id.video_player_total_time);
        this.sbProgress = (SeekBar) inflate.findViewById(R.id.video_player_progress);
        this.ivPlayOrPause = (ImageView) inflate.findViewById(R.id.video_player_play_btn);
        this.ivFullScreen = (ImageView) inflate.findViewById(R.id.video_player_full_screen);
        this.ivPlayOrPause.setOnClickListener(this);
        this.ivFullScreen.setOnClickListener(this);
        this.sbProgress.setOnSeekBarChangeListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            this.screenWidth = AppUtils.getScreenWidth(getContext());
            this.screenHeight = AppUtils.getScreenHeight(getContext());
            this.threshold = AppUtils.dp2px(getContext(), 20);
            initPlayer();
            if (this.playerSupport) {
                String string = getBundle().getString(IConstant.KEY_PATH_LIST);
                this.videoPath = string;
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                playVideo(this.videoPath);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 1) {
            this.screenWidth = AppUtils.getScreenWidth(getContext());
            this.screenHeight = AppUtils.getScreenHeight(getContext());
        } else if (configuration.orientation == 2) {
            this.screenWidth = AppUtils.getScreenWidth(getContext());
            this.screenHeight = AppUtils.getScreenHeight(getContext());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isPausing()) {
            onResumeVideo();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        pauseVideo();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void initPlayer() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.playerSupport = true;
        } catch (Throwable th) {
            Dbug.e(this.TAG, "loadLibraries error : " + th.getMessage());
        }
        if (!this.playerSupport) {
            ToastUtil.showToastShort(getString(R.string.player_not_support_dev));
            if (getActivity() != null) {
                getActivity().finish();
                return;
            }
            return;
        }
        this.videoView.setOnTouchListener(this);
        this.videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (VideoPlayerFragment.this.mHandler != null) {
                    VideoPlayerFragment.this.mHandler.removeMessages(VideoPlayerFragment.MSG_UPDATE_PROGRESS);
                    VideoPlayerFragment.this.mHandler.sendMessageDelayed(VideoPlayerFragment.this.mHandler.obtainMessage(VideoPlayerFragment.MSG_UPDATE_PROGRESS, VideoPlayerFragment.this.videoView.getDuration(), 0), VideoPlayerFragment.TIME_UPDATE);
                }
                VideoPlayerFragment.this.isPausing = true;
            }
        });
        this.videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
                if (i2 == -10000) {
                    return true;
                }
                ToastUtil.showToastShort(VideoPlayerFragment.this.getString(R.string.player_not_support_media));
                if (VideoPlayerFragment.this.getActivity() != null) {
                    VideoPlayerFragment.this.getActivity().finish();
                }
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        IjkVideoView ijkVideoView = this.videoView;
        return ijkVideoView != null && ijkVideoView.isPlaying();
    }

    private boolean isPausing() {
        return this.videoView != null && this.isPausing;
    }

    private boolean isPreparing() {
        return this.videoView != null && this.isPreparing;
    }

    private void playVideo(String str) {
        if (!this.playerSupport || TextUtils.isEmpty(str)) {
            return;
        }
        this.videoView.setVideoPath(str);
        this.isPreparing = true;
        this.videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Dbug.w(VideoPlayerFragment.this.TAG, "onPrepared is ok! start playing.");
                VideoPlayerFragment.this.videoView.start();
                VideoPlayerFragment.this.updatePlayingUI();
                if (VideoPlayerFragment.this.mHandler != null) {
                    VideoPlayerFragment.this.mHandler.sendMessage(VideoPlayerFragment.this.mHandler.obtainMessage(VideoPlayerFragment.MSG_UPDATE_PROGRESS, VideoPlayerFragment.this.videoView.getCurrentPosition(), 0));
                }
                VideoPlayerFragment.this.isPreparing = false;
                VideoPlayerFragment.this.isPausing = false;
            }
        });
    }

    private void pauseVideo() {
        if (this.playerSupport && this.videoView.isPlaying() && this.videoView.canPause()) {
            this.videoView.pause();
            this.isPausing = true;
            this.pauseTime = this.videoView.getCurrentPosition();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(MSG_UPDATE_PROGRESS);
                this.mHandler.sendEmptyMessage(MSG_VIDEO_PAUSE);
            }
        }
    }

    private void onResumeVideo() {
        if (this.playerSupport && isPausing()) {
            this.videoView.seekTo(this.pauseTime);
            this.videoView.start();
            this.isPausing = false;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessage(MSG_VIDEO_RESUME);
                Handler handler2 = this.mHandler;
                handler2.sendMessage(handler2.obtainMessage(MSG_UPDATE_PROGRESS, this.videoView.getCurrentPosition(), 0));
            }
        }
    }

    private void playOrPause() {
        if (isPreparing()) {
            return;
        }
        if (isPlaying()) {
            pauseVideo();
        } else if (isPausing()) {
            onResumeVideo();
        } else {
            if (TextUtils.isEmpty(this.videoPath)) {
                return;
            }
            playVideo(this.videoPath);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayingUI() {
        if (TextUtils.isEmpty(this.videoPath)) {
            return;
        }
        this.ivPlayOrPause.setImageResource(R.drawable.drawable_btn_pause);
        this.videoView.requestFocus();
        this.sbProgress.setMax(this.videoView.getDuration() / 1000);
        this.sbProgress.setProgress(0);
        this.tvTitle.setText(formatTitle(this.videoPath));
        this.tvCurrentTime.setText(getString(R.string.default_time_format));
        this.tvTotalTime.setText(TimeFormate.getTimeFormatValue((int) Math.floor(this.videoView.getDuration() / 1000.0f)));
    }

    private String formatTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.contains(File.separator)) {
            return str;
        }
     return str.split(File.separator)[str.split(File.separator).length - 1];
    }

    private void changeOrientation() {
        if (getActivity().getRequestedOrientation() == 1) {
            this.videoView.setAspectRatio(3);
            getActivity().setRequestedOrientation(6);
            this.ivFullScreen.setImageResource(R.mipmap.ic_no_fullscreen);
        } else {
            this.videoView.setAspectRatio(0);
            getActivity().setRequestedOrientation(1);
            this.ivFullScreen.setImageResource(R.mipmap.ic_fullscreen);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0036, code lost:
    
        if (r6 < r7) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0083  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouch(View r11, android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 243
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private void showVolumeToastUI(int i, float f) {
        if (getActivity() != null) {
            if (this.volumeToast == null) {
                this.volumeToast = new VolumeToast(getActivity().getApplicationContext());
            }
            int i2 = -1;
            switch (i) {
                case OP_UP /* 60913 */:
                    i2 = (int) ((f / this.screenHeight) * this.volumeToast.getMaxVol() * 3.0f);
                    break;
                case OP_DOWN /* 60914 */:
                    i2 = -((int) ((f / this.screenHeight) * this.volumeToast.getMaxVol() * 3.0f));
                    break;
            }
            this.volumeToast.show(i2);
        }
    }

    private void showBrightnessToast(int i, float f) {
        if (getActivity() != null) {
            if (this.brightnessToast == null) {
                this.brightnessToast = new BrightnessToast(getActivity());
            }
            int i2 = -1;
            switch (i) {
                case OP_UP /* 60913 */:
                    i2 = (int) ((f / this.screenHeight) * BrightnessToast.getMaxValue() * 3.0f);
                    break;
                case OP_DOWN /* 60914 */:
                    i2 = -((int) ((f / this.screenHeight) * BrightnessToast.getMaxValue() * 3.0f));
                    break;
            }
            this.brightnessToast.showBrightnessProgress(i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOrHideBar() {
        if (this.topBar.getVisibility() == 0) {
            this.topBar.setVisibility(8);
        } else {
            this.topBar.setVisibility(0);
        }
        if (this.bottomBar.getVisibility() == 0) {
            this.bottomBar.setVisibility(8);
        } else {
            this.bottomBar.setVisibility(0);
        }
        if (this.topBar.getVisibility() == 0 && this.bottomBar.getVisibility() == 0) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(MSG_HIDE_BAR_UI);
                this.mHandler.sendEmptyMessageDelayed(MSG_HIDE_BAR_UI, 5000L);
                return;
            }
            return;
        }
        this.mHandler.removeMessages(MSG_HIDE_BAR_UI);
    }

    private void showFastForwardToast(float f) {
        if (getActivity() != null) {
            int currentPosition = this.videoView.getCurrentPosition() + ((int) ((f / (this.screenWidth / 2)) * (this.videoView.getDuration() - this.videoView.getCurrentPosition())));
            if (currentPosition >= this.videoView.getDuration()) {
                currentPosition = this.videoView.getDuration() + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
            }
            if (this.videoView.canSeekForward()) {
                this.videoView.seekTo(currentPosition);
                if (isPausing()) {
                    this.pauseTime = currentPosition;
                }
                currentPosition /= 1000;
                this.sbProgress.setProgress(currentPosition);
                this.tvCurrentTime.setText(TimeFormate.getTimeFormatValue(currentPosition));
            }
            showVideoProgressToast(VideoProgressToast.FAST_FORWARD, TimeFormate.getTimeFormatValue(currentPosition));
        }
    }

    private void showFastBackward(float f) {
        if (getActivity() != null) {
            int currentPosition = this.videoView.getCurrentPosition() - ((int) ((f / (this.screenWidth / 2)) * (this.videoView.getDuration() - this.videoView.getCurrentPosition())));
            if (currentPosition <= 0) {
                currentPosition = 1000;
            }
            if (this.videoView.canSeekBackward()) {
                this.videoView.seekTo(currentPosition);
                if (isPausing()) {
                    this.pauseTime = currentPosition;
                }
                currentPosition /= 1000;
                this.sbProgress.setProgress(currentPosition);
                this.tvCurrentTime.setText(TimeFormate.getTimeFormatValue(currentPosition));
            }
            showVideoProgressToast(VideoProgressToast.FAST_BACKWARD, TimeFormate.getTimeFormatValue(currentPosition));
        }
    }

    private void showVideoProgressToast(int i, String str) {
        if (getActivity() != null) {
            if (this.videoProgressToast == null) {
                this.videoProgressToast = new VideoProgressToast(getActivity().getApplicationContext());
            }
            this.videoProgressToast.show(i, str);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null || view == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.video_player_play_btn) {
            playOrPause();
        } else if (id == R.id.video_player_full_screen) {
            changeOrientation();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isPlaying() || isPausing()) {
            int progress = seekBar.getProgress();
            int i = progress * 1000;
            this.videoView.seekTo(i);
            if (isPausing()) {
                this.pauseTime = i;
            }
            this.tvCurrentTime.setText(TimeFormate.getTimeFormatValue(progress));
            return;
        }
        this.sbProgress.setProgress(0);
        this.tvCurrentTime.setText(getString(R.string.default_time_format));
    }
}
