package com.jieli.stream.dv.running2.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import com.baidu.mapapi.UIMsg;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.model.SortType;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.gps.OnGpsListener;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.model.GpsInfo;
import com.jieli.lib.dv.control.model.MediaInfo;
import com.jieli.lib.dv.control.player.OnBufferingListener;
import com.jieli.lib.dv.control.player.OnPlaybackListener;
import com.jieli.lib.dv.control.player.OnProgressListener;
import com.jieli.lib.dv.control.player.PlaybackStream;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.baidu.utils.BitmapUtil;
import com.jieli.stream.dv.running2.baidu.utils.MapUtil;
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.data.OnRecordStateListener;
import com.jieli.stream.dv.running2.data.VideoRecord;
import com.jieli.stream.dv.running2.task.DebugHelper;
import com.jieli.stream.dv.running2.task.IDebugListener;
import com.jieli.stream.dv.running2.task.SDPServer;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.widget.media.IMediaController;
import com.jieli.stream.dv.running2.ui.widget.media.IjkVideoView;
import com.jieli.stream.dv.running2.ui.widget.media.InfoHudViewHolder;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.TimeFormate;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class PlaybackDialogActivity extends BaseActivity implements View.OnClickListener, OnGpsListener {
    private static final int MSG_CLEAR_TRACK_POINT = 257;
    private static final int MSG_FPS_COUNT = 258;
    private static final int REFRESH_VIDEO_TIME_INTERVAL = 500;
    private static final int TAG_REFRESH_VIDEO_TIME = 1;
    private int fastForwardLevel;
    private boolean isIJKPlayerOpen;
    private MainApplication mApplication;
    private ProgressBar mBufferingProg;
    private ImageView mCancelPlayback;
    private DebugHelper mDebugHelper;
    private ImageView mExpandButton;
    private ImageButton mFastForward;
    private int mFps;
    private TableLayout mHudView;
    private ImageButton mMovRecordBtn;
    private ImageButton mPlayPause;
    private ProgressBar mPlaybackProg;
    private VideoRecord mRecordVideo;
    private SDPServer mServer;
    private ImageView mShrinkButton;
    private PlaybackStream mStreamPlayer;
    private TextView mTimeTextView;
    private IjkVideoView mVideoView;
    private MapView mapView;
    private MyBroadcastReceiver receiver;
    private ImageButton stopFastForwardIbtn;
    private LinearLayout widgetParent;
    private String tag = getClass().getSimpleName();
    private final int mTCPPort = 5678;
    private boolean isRecordPrepared = false;
    private boolean isUseMap = false;
    private boolean isDebugOpen = false;
    private int[] fastForwardRes = {R.mipmap.ic_fast_forward_1, R.mipmap.ic_fast_forward_2, R.mipmap.ic_fast_forward_4, R.mipmap.ic_fast_forward_8, R.mipmap.ic_fast_forward_16, R.mipmap.ic_fast_forward_32, R.mipmap.ic_fast_forward_64};
    private List<LatLng> trackPoints = new ArrayList();
    private MapUtil mapUtil = null;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message == null) {
                return false;
            }
            int i = message.what;
            if (i == 1) {
                if (PlaybackDialogActivity.this.mStreamPlayer == null) {
                    return false;
                }
                long currentPosition = PlaybackDialogActivity.this.mStreamPlayer.getCurrentPosition();
                if (currentPosition > 10000) {
                    Date date = new Date(currentPosition);
                    PlaybackDialogActivity.this.mTimeTextView.setText(TimeFormate.yyyyMMddHHmmss.format(date));
                }
                PlaybackDialogActivity.this.mHandler.sendEmptyMessageDelayed(1, 500L);
                return false;
            }
            if (i != 257) {
                if (i != PlaybackDialogActivity.MSG_FPS_COUNT) {
                    return false;
                }
                PlaybackDialogActivity.this.updateDebugFps();
                PlaybackDialogActivity.this.mHandler.removeMessages(PlaybackDialogActivity.MSG_FPS_COUNT);
                PlaybackDialogActivity.this.mHandler.sendEmptyMessageDelayed(PlaybackDialogActivity.MSG_FPS_COUNT, 1000L);
                return false;
            }
            if (PlaybackDialogActivity.this.trackPoints != null) {
                PlaybackDialogActivity.this.trackPoints.clear();
            }
            if (PlaybackDialogActivity.this.mapUtil == null) {
                return false;
            }
            PlaybackDialogActivity.this.mapUtil.clearMap();
            return false;
        }
    });
    private IDebugListener mIDebugListener = new IDebugListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.3
        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onError(int i, String str) {
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onStartDebug(String str, int i, int i2) {
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onDebugResult(int i, int i2) {
            PlaybackDialogActivity.this.updateDebug(i, i2);
        }
    };
    private final OnBufferingListener onBufferingListener = new OnBufferingListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.4
        @Override // com.jieli.lib.dv.control.player.OnBufferingListener
        public void onBuffering(int i) {
            if (i == 1) {
                PlaybackDialogActivity.this.mBufferingProg.setVisibility(0);
            } else {
                PlaybackDialogActivity.this.mBufferingProg.setVisibility(8);
            }
        }
    };
    private OnNotifyListener mOnNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.5
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(PlaybackDialogActivity.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            int hashCode = topic.hashCode();
            if (hashCode != 218061259) {
                if (hashCode == 1021829664 && topic.equals(Topic.PLAYBACK)) {
                    c = 0;
                }
            } else if (topic.equals(Topic.PLAYBACK_FAST_FORWARD)) {
                c = 1;
            }
            if (c != 0) {
                if (c != 1) {
                    return;
                }
                if (notifyInfo.getParams() == null) {
                    Dbug.e(PlaybackDialogActivity.this.tag, "PLAYBACK_FAST_FORWARD: data params is null");
                    return;
                }
                PlaybackDialogActivity.this.fastForwardLevel = Integer.valueOf(notifyInfo.getParams().get(TopicKey.LEVEL)).intValue();
                if (PlaybackDialogActivity.this.fastForwardLevel < PlaybackDialogActivity.this.fastForwardRes.length) {
                    PlaybackDialogActivity.this.mFastForward.setImageResource(PlaybackDialogActivity.this.fastForwardRes[PlaybackDialogActivity.this.fastForwardLevel]);
                    return;
                }
                return;
            }
            if (PlaybackDialogActivity.this.mStreamPlayer == null) {
                PlaybackDialogActivity.this.mStreamPlayer = new PlaybackStream();
                PlaybackDialogActivity.this.mStreamPlayer.registerPlayerListener(PlaybackDialogActivity.this.mPlayerListener);
            }
            Dbug.i(PlaybackDialogActivity.this.tag, "playback data " + notifyInfo);
            PlaybackDialogActivity.this.mStreamPlayer.create(IConstant.RTS_TCP_PORT, ClientManager.getClient().getConnectedIP());
            PlaybackDialogActivity.this.mStreamPlayer.configure(IConstant.RTP_VIDEO_PORT1, IConstant.RTP_AUDIO_PORT1);
        }
    };
    private Runnable handlerRecord = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.8
        @Override // java.lang.Runnable
        public void run() {
            Dbug.i(PlaybackDialogActivity.this.tag, "handlerRecord isRecordPrepared ->" + PlaybackDialogActivity.this.isRecordPrepared);
            if (PlaybackDialogActivity.this.isRecordPrepared) {
                PlaybackDialogActivity.this.stopLocalRecording();
            } else {
                PlaybackDialogActivity.this.startLocalRecording();
            }
        }
    };
    private final OnProgressListener onProgressListener = new OnProgressListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.9
        @Override // com.jieli.lib.dv.control.player.OnProgressListener
        public void onStart() {
            MediaInfo currentMediaInfo = PlaybackDialogActivity.this.mStreamPlayer.getCurrentMediaInfo();
            Dbug.w(PlaybackDialogActivity.this.tag, "onStart: mediaInfo=" + currentMediaInfo);
            PlaybackDialogActivity.this.mPlaybackProg.setMax(currentMediaInfo.getDuration());
            PlaybackDialogActivity.this.mPlaybackProg.setProgress(0);
            if (PlaybackDialogActivity.this.mapUtil != null) {
                PlaybackDialogActivity.this.mHandler.removeMessages(257);
                PlaybackDialogActivity.this.mHandler.sendEmptyMessageDelayed(257, 1000L);
            }
        }

        @Override // com.jieli.lib.dv.control.player.OnProgressListener
        public void onProgress(int i) {
            if (PlaybackDialogActivity.this.mPlaybackProg != null) {
                if (PlaybackDialogActivity.this.mPlaybackProg.getMax() > 0) {
                    PlaybackDialogActivity.this.mPlaybackProg.setProgress(i);
                } else {
                    PlaybackDialogActivity.this.mPlaybackProg.setProgress(0);
                }
            }
        }

        @Override // com.jieli.lib.dv.control.player.OnProgressListener
        public void onFinish() {
            Dbug.w(PlaybackDialogActivity.this.tag, "===onFinish");
            if (PlaybackDialogActivity.this.mapUtil != null) {
                PlaybackDialogActivity.this.mapUtil.addEndPointOverlay(PlaybackDialogActivity.this.trackPoints);
            }
        }
    };
    private final OnPlaybackListener mPlayerListener = new OnPlaybackListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.10
        @Override // com.jieli.lib.dv.control.player.OnPlaybackListener
        public void onUpdate(MediaInfo mediaInfo) {
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onVideo(int i, int i2, byte[] bArr, long j, long j2) {
            if (PlaybackDialogActivity.this.isDebugOpen) {
                PlaybackDialogActivity.access$2208(PlaybackDialogActivity.this);
            }
            if (PlaybackDialogActivity.this.mRecordVideo == null || !PlaybackDialogActivity.this.isRecordPrepared || PlaybackDialogActivity.this.mRecordVideo.write(i, bArr)) {
                return;
            }
            Dbug.e(PlaybackDialogActivity.this.tag, "Write video failed");
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onAudio(int i, int i2, byte[] bArr, long j, long j2) {
            if (PlaybackDialogActivity.this.mRecordVideo == null || !PlaybackDialogActivity.this.isRecordPrepared || PlaybackDialogActivity.this.mRecordVideo.write(i, bArr)) {
                return;
            }
            Dbug.e(PlaybackDialogActivity.this.tag, "Write audio failed");
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onStateChanged(int i) {
            if (i == 1) {
                Dbug.i(PlaybackDialogActivity.this.tag, "prepare-------");
                if (PlaybackDialogActivity.this.mMovRecordBtn.getVisibility() != 0) {
                    PlaybackDialogActivity.this.mMovRecordBtn.setVisibility(0);
                }
                PlaybackDialogActivity.this.initPlayer("tcp://127.0.0.1:5678");
                return;
            }
            if (i == 2) {
                Dbug.i(PlaybackDialogActivity.this.tag, "playing-------");
                PlaybackDialogActivity.this.mPlayPause.setImageResource(R.mipmap.ic_playback_pause);
                PlaybackDialogActivity.this.mHandler.sendEmptyMessage(1);
                return;
            }
            if (i == 4) {
                Dbug.i(PlaybackDialogActivity.this.tag, "pause-------");
                PlaybackDialogActivity.this.mPlayPause.setImageResource(R.mipmap.ic_playback_play);
                PlaybackDialogActivity.this.mHandler.removeMessages(1);
            } else {
                if (i != 5) {
                    if (i != 6) {
                        return;
                    }
                    Dbug.i(PlaybackDialogActivity.this.tag, "file end-------");
                    return;
                }
                Dbug.i(PlaybackDialogActivity.this.tag, "finish-------");
                if (PlaybackDialogActivity.this.mMovRecordBtn.getVisibility() != 8) {
                    PlaybackDialogActivity.this.mMovRecordBtn.setVisibility(8);
                }
                PlaybackDialogActivity.this.stopLocalRecording();
                PlaybackDialogActivity.this.mHandler.removeCallbacksAndMessages(null);
                PlaybackDialogActivity.this.mPlayPause.setVisibility(8);
                ToastUtil.showToastShort(PlaybackDialogActivity.this.getString(R.string.play_over));
                PlaybackDialogActivity.this.onBackPressed();
            }
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onError(int i, final String str) {
            if (i == 0) {
                PlaybackDialogActivity.this.runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.10.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ToastUtil.showToastShort(PlaybackDialogActivity.this.getString(R.string.connection_timeout));
                    }
                });
            } else {
                PlaybackDialogActivity.this.runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.10.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ToastUtil.showToastShort(str);
                    }
                });
            }
        }
    };
    private IMediaController iMediaController = new IMediaController() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.11
        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setAnchorView(View view) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setEnabled(boolean z) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void hide() {
            PlaybackDialogActivity.this.hideWidget();
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public boolean isShowing() {
            return PlaybackDialogActivity.this.widgetParent != null && PlaybackDialogActivity.this.widgetParent.getVisibility() == 0;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void show(int i) {
            PlaybackDialogActivity.this.showWidget();
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void show() {
            PlaybackDialogActivity.this.showWidget();
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void showOnce(View view) {
            Dbug.i(PlaybackDialogActivity.this.tag, "show once");
        }
    };

    static /* synthetic */ int access$2208(PlaybackDialogActivity playbackDialogActivity) {
        int i = playbackDialogActivity.mFps;
        playbackDialogActivity.mFps = i + 1;
        return i;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            if (("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()) || "android.intent.action.SCREEN_OFF".equals(intent.getAction())) && !PlaybackDialogActivity.this.isFinishing()) {
                PlaybackDialogActivity.this.finish();
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_playback_dialog);
        this.mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        this.mPlayPause = (ImageButton) findViewById(R.id.play_pause);
        this.mCancelPlayback = (ImageView) findViewById(R.id.cancel_playback);
        this.mFastForward = (ImageButton) findViewById(R.id.fast_forward);
        this.mMovRecordBtn = (ImageButton) findViewById(R.id.mov_record_btn);
        this.stopFastForwardIbtn = (ImageButton) findViewById(R.id.stop_fast_forward_btn);
        this.mTimeTextView = (TextView) findViewById(R.id.play_back_time_tv);
        this.widgetParent = (LinearLayout) findViewById(R.id.playback_widget_parent);
        this.mBufferingProg = (ProgressBar) findViewById(R.id.rts_buffering);
        this.mPlaybackProg = (ProgressBar) findViewById(R.id.playback_progress);
        this.mShrinkButton = (ImageView) findViewById(R.id.shrink_button);
        this.mExpandButton = (ImageView) findViewById(R.id.expand_button);
        this.mapView = (MapView) findViewById(R.id.track_query_mapView);
        this.mHudView = (TableLayout) findViewById(R.id.hud_view);
        this.mVideoView.setMediaController(this.iMediaController);
        this.mVideoView.setAspectRatio(3);
        this.mPlayPause.setOnClickListener(this);
        this.mCancelPlayback.setOnClickListener(this);
        this.stopFastForwardIbtn.setOnClickListener(this);
        this.mFastForward.setOnClickListener(this);
        this.mMovRecordBtn.setOnClickListener(this);
        this.mShrinkButton.setOnClickListener(this);
        this.mExpandButton.setOnClickListener(this);
        this.mApplication = (MainApplication) getApplication();
        this.fastForwardLevel = 0;
        if (this.receiver == null) {
            this.receiver = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mApplication.registerReceiver(this.receiver, intentFilter);
        if (1 == this.mApplication.getSearchMode()) {
            Dbug.w(this.tag, "Current mode is STA");
            this.mExpandButton.setVisibility(0);
            MapUtil mapUtil = MapUtil.getInstance();
            this.mapUtil = mapUtil;
            mapUtil.init(this.mapView);
            BitmapUtil.init();
        } else {
            this.mExpandButton.setVisibility(8);
        }
        PlaybackStream playbackStream = new PlaybackStream();
        this.mStreamPlayer = playbackStream;
        playbackStream.registerPlayerListener(this.mPlayerListener);
        this.mStreamPlayer.setOnProgressListener(this.onProgressListener);
        if (1 == this.mApplication.getSearchMode()) {
            this.mStreamPlayer.setOnGpsListener(this);
        }
        ClientManager.getClient().registerNotifyListener(this.mOnNotifyListener);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ClientManager.getClient().tryToStartPlayback(extras.getString(IConstant.VIDEO_PATH), extras.getInt(IConstant.VIDEO_OFFSET, 0), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.2
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(PlaybackDialogActivity.this.tag, "Send failed");
                    }
                }
            });
            Date date = new Date(extras.getLong(IConstant.VIDEO_CREATE_TIME));
            this.mTimeTextView.setText(TimeFormate.yyyyMMddHHmmss.format(date));
        }
        this.mStreamPlayer.setOnBufferingListener(this.onBufferingListener);
        enableDebugMode();
    }

    private void enableDebugMode() {
        this.isDebugOpen = PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false);
        Dbug.e(this.tag, "enableDebugMode isDebugOpen " + this.isDebugOpen);
        if (this.isDebugOpen) {
            if (this.mHudView != null) {
                Dbug.e(this.tag, "11 ");
                if (this.mHudView.getVisibility() != 0) {
                    Dbug.e(this.tag, "22 ");
                    this.mHudView.setVisibility(0);
                    this.mVideoView.setHudView(this.mHudView);
                }
            }
            startDebug();
            return;
        }
        TableLayout tableLayout = this.mHudView;
        if (tableLayout == null || tableLayout.getVisibility() == 8) {
            return;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(MSG_FPS_COUNT);
        }
        this.mHudView.setVisibility(8);
        this.mVideoView.setHudView(null);
    }

    private void startDebug() {
        this.mFps = 0;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(MSG_FPS_COUNT);
        }
        if (this.mDebugHelper == null) {
            DebugHelper debugHelper = new DebugHelper();
            this.mDebugHelper = debugHelper;
            debugHelper.registerDebugListener(this.mIDebugListener);
        }
        this.mDebugHelper.startDebug();
    }

    private void closeDebug() {
        if (this.mDebugHelper != null) {
            this.mFps = 0;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(MSG_FPS_COUNT);
            }
            this.mDebugHelper.unregisterDebugListener(this.mIDebugListener);
            this.mDebugHelper.closeDebug();
            this.mDebugHelper = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDebug(int i, int i2) {
        InfoHudViewHolder hudView;
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.drop_packet_count, i + "");
        hudView.setRowValue(R.string.drop_packet_sum, i2 + "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDebugFps() {
        InfoHudViewHolder hudView;
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.fps, this.mFps + "");
        this.mFps = 0;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        MapUtil mapUtil = this.mapUtil;
        if (mapUtil != null) {
            mapUtil.onResume();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        MapUtil mapUtil = this.mapUtil;
        if (mapUtil != null) {
            mapUtil.onPause();
        }
        handleScreenOff();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        Dbug.e(this.tag, "=======on stop=======");
        stopLocalRecording();
        if (this.isDebugOpen) {
            closeDebug();
        }
        PlaybackStream playbackStream = this.mStreamPlayer;
        if (playbackStream != null) {
            playbackStream.unregisterPlayerListener(this.mPlayerListener);
            this.mStreamPlayer.release();
        }
        stopMediaPlayer();
        ClientManager.getClient().unregisterNotifyListener(this.mOnNotifyListener);
        SDPServer sDPServer = this.mServer;
        if (sDPServer != null) {
            sDPServer.stopRunning();
            this.mServer = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        MapUtil mapUtil = this.mapUtil;
        if (mapUtil != null) {
            mapUtil.clear();
            BitmapUtil.clear();
        }
        this.trackPoints.clear();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Dbug.e(this.tag, "=======on destroy=======");
        MyBroadcastReceiver myBroadcastReceiver = this.receiver;
        if (myBroadcastReceiver != null) {
            this.mApplication.unregisterReceiver(myBroadcastReceiver);
            this.receiver = null;
        }
    }

    private int getRtsFormat() {
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (deviceSettingInfo == null) {
            return 1;
        }
        if (1 == deviceSettingInfo.getCameraType()) {
            return deviceSettingInfo.getFrontFormat();
        }
        return deviceSettingInfo.getRearFormat();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayer(String str) {
        if (this.mVideoView != null && !TextUtils.isEmpty(str)) {
            this.mServer = new SDPServer(5678, getRtsFormat());
            MediaInfo currentMediaInfo = this.mStreamPlayer.getCurrentMediaInfo();
            if (currentMediaInfo != null) {
                this.mServer.setFrameRate(currentMediaInfo.getFrameRate());
                this.mServer.setSampleRate(currentMediaInfo.getSampleRate());
            }
            this.mServer.start();
            Uri parse = Uri.parse(str);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.isIJKPlayerOpen = true;
            this.mVideoView.setVideoURI(parse);
            this.mVideoView.start();
            return;
        }
        Dbug.e(this.tag, "init player fail");
    }

    private void stopMediaPlayer() {
        this.mVideoView.stopPlayback();
        this.mVideoView.release(true);
        if (this.isIJKPlayerOpen) {
            IjkMediaPlayer.native_profileEnd();
        }
        this.isIJKPlayerOpen = false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mPlayPause) {
            PlaybackStream playbackStream = this.mStreamPlayer;
            if (playbackStream != null) {
                if (playbackStream.isStreamReceiving()) {
                    this.mVideoView.pause();
                    this.mStreamPlayer.pauseStream();
                    return;
                } else {
                    if (this.mStreamPlayer.isStreamPausing()) {
                        this.mStreamPlayer.playStream();
                        this.mVideoView.start();
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (view == this.mCancelPlayback) {
            onBackPressed();
            return;
        }
        if (view == this.mFastForward) {
            if (this.isUseMap) {
                ToastUtil.showToastLong(getString(R.string.stop_trajectory_tracking));
                return;
            } else {
                fastForward();
                return;
            }
        }
        if (view == this.stopFastForwardIbtn) {
            this.fastForwardLevel = -1;
            fastForward();
            return;
        }
        if (view == this.mMovRecordBtn) {
            if (!AppUtils.isFastDoubleClick(UIMsg.m_AppUI.MSG_APP_DATA_OK)) {
                this.mHandler.post(this.handlerRecord);
                return;
            } else {
                ToastUtil.showToastShort(getString(R.string.wait_a_moment));
                return;
            }
        }
        if (view == this.mShrinkButton) {
            this.mapView.setVisibility(8);
            this.mShrinkButton.setVisibility(8);
            this.mExpandButton.setVisibility(0);
            MapUtil mapUtil = this.mapUtil;
            if (mapUtil != null) {
                mapUtil.onPause();
                this.isUseMap = false;
                return;
            }
            return;
        }
        if (view == this.mExpandButton) {
            if (this.fastForwardLevel > 0) {
                ToastUtil.showToastLong(getString(R.string.resume_normal_playback_speed));
                return;
            }
            this.mapView.setVisibility(0);
            this.mShrinkButton.setVisibility(0);
            this.mExpandButton.setVisibility(8);
            MapUtil mapUtil2 = this.mapUtil;
            if (mapUtil2 != null) {
                mapUtil2.onResume();
                this.isUseMap = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLocalRecording() {
        if (this.mRecordVideo == null) {
            this.mRecordVideo = new VideoRecord();
        }
        this.mRecordVideo.prepare(new OnRecordStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.6
            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onPrepared() {
                PlaybackDialogActivity.this.isRecordPrepared = true;
                PlaybackDialogActivity.this.handleStartRecode();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onCompletion(String str) {
                ToastUtil.showToastShort(PlaybackDialogActivity.this.getString(R.string.record_success));
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onStop() {
                Dbug.i(PlaybackDialogActivity.this.tag, "Record onStop");
                PlaybackDialogActivity.this.isRecordPrepared = false;
                PlaybackDialogActivity.this.handleStopRecode();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onError(String str) {
                Dbug.e(PlaybackDialogActivity.this.tag, "Record error:" + str);
                if (PlaybackDialogActivity.this.mRecordVideo != null) {
                    String currentFilePath = PlaybackDialogActivity.this.mRecordVideo.getCurrentFilePath();
                    if (!TextUtils.isEmpty(currentFilePath)) {
                        File file = new File(currentFilePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                PlaybackDialogActivity.this.handleStopRecode();
                ToastUtil.showToastShort(PlaybackDialogActivity.this.getString(R.string.record_fail));
                PlaybackDialogActivity.this.mRecordVideo = null;
                PlaybackDialogActivity.this.isRecordPrepared = false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLocalRecording() {
        VideoRecord videoRecord = this.mRecordVideo;
        if (videoRecord != null) {
            this.isRecordPrepared = false;
            videoRecord.close();
            this.mRecordVideo = null;
        }
    }

    private void fastForward() {
        int i = (this.fastForwardLevel + 1) % 7;
        if (i == 0) {
            this.mStreamPlayer.toggleBuffering(true);
        } else {
            this.mStreamPlayer.toggleBuffering(false);
        }
        ClientManager.getClient().tryToFastForward(i, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackDialogActivity.7
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(PlaybackDialogActivity.this.tag, "Send failed");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartRecode() {
        this.mCancelPlayback.setVisibility(4);
        this.mMovRecordBtn.setImageResource(R.mipmap.ic_cuting_mov);
        this.mFastForward.setVisibility(4);
        this.mPlayPause.setVisibility(4);
        this.stopFastForwardIbtn.setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopRecode() {
        this.mCancelPlayback.setVisibility(0);
        this.mMovRecordBtn.setImageResource(R.mipmap.ic_cut_mov);
        this.mFastForward.setVisibility(0);
        this.mPlayPause.setVisibility(0);
        this.stopFastForwardIbtn.setVisibility(0);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideWidget() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
        translateAnimation.setDuration(500L);
        translateAnimation.setInterpolator(new LinearInterpolator());
        this.widgetParent.startAnimation(translateAnimation);
        this.widgetParent.setVisibility(4);
        this.mPlaybackProg.setVisibility(8);
        if (this.mapUtil != null) {
            if (this.mapView.getVisibility() == 8) {
                this.mExpandButton.setVisibility(8);
            }
            if (this.mapView.getVisibility() == 0) {
                this.mShrinkButton.setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWidget() {
        this.mPlaybackProg.setVisibility(0);
        this.widgetParent.setVisibility(0);
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
        translateAnimation.setDuration(500L);
        translateAnimation.setInterpolator(new LinearInterpolator());
        this.widgetParent.startAnimation(translateAnimation);
        if (this.mapUtil != null) {
            if (this.mapView.getVisibility() == 8) {
                this.mExpandButton.setVisibility(0);
            }
            if (this.mapView.getVisibility() == 0) {
                this.mShrinkButton.setVisibility(0);
            }
        }
    }

    @Override // com.jieli.lib.dv.control.gps.OnGpsListener
    public void onGps(GpsInfo gpsInfo) {
        this.trackPoints.add(MapUtil.convertTrace2Map(new com.baidu.trace.model.LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude())));
        MapUtil mapUtil = this.mapUtil;
        if (mapUtil != null) {
            mapUtil.drawHistoryTrack(this.trackPoints, SortType.asc);
        }
    }

    private void handleScreenOff() {
        PowerManager powerManager = (PowerManager) getSystemService("power");
        if (powerManager != null) {
            if (Build.VERSION.SDK_INT >= 20) {
                if (powerManager.isInteractive() || isFinishing()) {
                    return;
                }
                finish();
                return;
            }
            if (powerManager.isScreenOn() || isFinishing()) {
                return;
            }
            finish();
        }
    }
}
