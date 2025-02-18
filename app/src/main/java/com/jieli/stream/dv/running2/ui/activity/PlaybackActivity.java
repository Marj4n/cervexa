package com.jieli.stream.dv.running2.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.intercom.IntercomManager;
import com.jieli.lib.dv.control.player.OnRealTimeListener;
import com.jieli.lib.dv.control.player.RealtimeStream;
import com.jieli.lib.dv.control.player.VideoThumbnail;
import com.jieli.lib.dv.control.projection.OnSendStateListener;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.media.codec.FrameCodec;
import com.jieli.media.codec.bean.MediaMeta;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.audio.AudioRecordManager;
import com.jieli.stream.dv.running2.bean.DeviceDesc;
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.RequestFileInfo;
import com.jieli.stream.dv.running2.bean.ThumbnailInfo;
import com.jieli.stream.dv.running2.data.OnRecordStateListener;
import com.jieli.stream.dv.running2.data.VideoCapture;
import com.jieli.stream.dv.running2.data.VideoRecord;
import com.jieli.stream.dv.running2.interfaces.OnClickStateListener;
import com.jieli.stream.dv.running2.task.DebugHelper;
import com.jieli.stream.dv.running2.task.IDebugListener;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.adapter.CoverAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.dialog.DownloadDialog;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.ui.widget.PlaybackSeekbar;
import com.jieli.stream.dv.running2.ui.widget.PopupMenu;
import com.jieli.stream.dv.running2.ui.widget.coverflow.CoverFlowLayoutManger;
import com.jieli.stream.dv.running2.ui.widget.coverflow.RecyclerCoverFlow;
import com.jieli.stream.dv.running2.ui.widget.media.IMediaController;
import com.jieli.stream.dv.running2.ui.widget.media.IjkVideoView;
import com.jieli.stream.dv.running2.ui.widget.media.InfoHudViewHolder;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ThumbnailRequestQueue;
import com.jieli.stream.dv.running2.util.TimeFormate;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.json.JSonManager;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;
import com.serenegiant.usb.UVCCamera;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class PlaybackActivity extends BaseActivity implements View.OnClickListener, AudioRecordManager.RecorderListener, OnSendStateListener {
    private static final long DELAY_TIME = 100;
    private static final int MODE_BROWSE = 258;
    private static final int MODE_PREVIEW = 257;
    private static final int MSG_CYC_SAVE_VIDEO = 2564;
    private static final int MSG_FPS_COUNT = 2565;
    private static final int MSG_PROJECTION_CONTROL = 2562;
    private static final int MSG_RT_VOICE_CONTROL = 2563;
    private static final int MSG_TAKE_PHOTO = 2561;
    private static final int MSG_TAKE_VIDEO = 2560;
    private static final int OP_DELETE_FILE = 0;
    private static final int OP_LOCK_FILE = 1;
    private static final SimpleDateFormat yyyyMMddHHmmss = TimeFormate.yyyyMMddHHmmss;
    private LinearLayout centerControlBar;
    private int fps;
    private IntercomManager intercomManager;
    private boolean isAdjustResolution;
    private boolean isIJKPlayerOpen;
    private boolean isProjection;
    private boolean isRtVoiceOpen;
    private boolean isRtspEnable;
    private boolean isStartDebug;
    private boolean isSwitchCamera;
    private RelativeLayout layoutBrowse;
    private RelativeLayout layoutPreview;
    private LinearLayout leftControlBar;
    private CoverAdapter mAdapter;
    private ImageView mAdjustResolutionBtn;
    private WaitingDialog mAdjustingDialog;
    private AudioRecordManager mAudioManager;
    private ImageView mBrowseGallery;
    private ImageView mCancel;
    private RecyclerCoverFlow mCoverFlowCarousel;
    private ImageView mCycSaveVideo;
    private DebugHelper mDebugHelper;
    private ImageView mDeleteFileBtn;
    private NotifyDialog mDeleteFileDialog;
    private NotifyDialog mErrorDialog;
    private List<FileInfo> mFileInfoList;
    private TableLayout mHudView;
    private NotifyDialog mLoadingDialog;
    private ProgressBar mLoadingView;
    private NotifyDialog mLocalRecordingDialog;
    private ImageView mLockFileBtn;
    private ImageView mPhotoBtn;
    private TextView mPositionTime;
    private ImageView mProjectionBtn;
    private ImageView mProjectionFlag;
    private ImageButton mRTSPlayButton;
    private RealtimeStream mRealtimeStream;
    private ImageView mRecordFlag;
    private VideoRecord mRecordVideo;
    private ImageView mReturn;
    private NotifyDialog mStopLocalRecordingDialog;
    private IjkVideoView mStreamView;
    private ImageView mSwitchCameraBtn;
    private ImageView mVideoBtn;
    private VideoCapture mVideoCapture;
    private VideoThumbnail mVideoThumbnail;
    private ImageView mVoiceBtn;
    private WaitingDialog mWaitingDialog;
    private NotifyDialog operationFileDialog;
    private int playbackMode;
    private PlaybackSeekbar playbackSeekbar;
    private PopupMenu popupMenu;
    private int threshold;
    private RelativeLayout topBar;
    private TextView tvAutoPlayTip;
    private TextView tvTop;
    private PowerManager.WakeLock wakeLock;
    private String tag = getClass().getSimpleName();
    private FrameCodec mFrameCodec = null;
    private List<String> mReady2DeleteList = new ArrayList();
    private int mCameraType = 1;
    private int recordStatus = -1;
    private boolean isRecordPrepared = false;
    private boolean isCapturePrepared = false;
    private boolean isChecked = false;
    private boolean isDeleteAll = false;
    private boolean isOnPause = false;
    private ThumbnailRequestQueue thumbnailRequestQueue = new ThumbnailRequestQueue();
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0166, code lost:
        
            if (r8 != 2) goto L66;
         */
        @Override // android.os.Handler.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean handleMessage(android.os.Message r8) {
            /*
                Method dump skipped, instructions count: 484
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.AnonymousClass1.handleMessage(android.os.Message):boolean");
        }
    });
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (context == null || TextUtils.isEmpty(action)) {
                return;
            }
            char c = 65535;
            if (action.hashCode() == 1935762049 && action.equals(IActions.ACTION_FORMAT_TF_CARD)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            PlaybackActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    PlaybackActivity.this.dismissWaitingDialog();
                }
            }, 1000L);
            PlaybackActivity.this.handleTFOffline();
        }
    };
    private Runnable autoPlayRunnable = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.8
        @Override // java.lang.Runnable
        public void run() {
            if (PlaybackActivity.this.playbackMode != PlaybackActivity.MODE_BROWSE) {
                return;
            }
            if (PlaybackActivity.this.mLoadingDialog == null || !PlaybackActivity.this.mLoadingDialog.isShowing()) {
                ThumbnailInfo item = PlaybackActivity.this.mAdapter.getItem(PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos());
                if (item == null) {
                    if (PlaybackActivity.this.mAdapter.getItemCount() > 0) {
                        ToastUtil.showToastShort(PlaybackActivity.this.mAdapter.getItem(0).getName());
                        PlaybackActivity playbackActivity = PlaybackActivity.this;
                        playbackActivity.showPlaybackDialog(playbackActivity.mAdapter.getItem(0).getPath(), PlaybackActivity.this.mAdapter.getItem(0).getStartTime().getTimeInMillis());
                        return;
                    }
                    return;
                }
                ToastUtil.showToastShort(item.getName());
                PlaybackActivity.this.showPlaybackDialog(item.getPath(), item.getStartTime().getTimeInMillis());
                return;
            }
            PlaybackActivity.this.handleAutoPlay();
        }
    };
    private PlaybackSeekbar.OnStatechangeListener onStatechangeListener = new PlaybackSeekbar.OnStatechangeListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.9
        @Override // com.jieli.stream.dv.running2.ui.widget.PlaybackSeekbar.OnStatechangeListener
        public void onBrowseCoverChange(int i) {
            if (PlaybackActivity.this.mAdapter.getItemCount() < 1) {
                PlaybackActivity.this.mHandler.removeCallbacks(PlaybackActivity.this.autoPlayRunnable);
            } else if (Math.abs(i - PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos()) > 3) {
                PlaybackActivity.this.mCoverFlowCarousel.setSelectPosition(i);
            } else if (i < PlaybackActivity.this.mAdapter.getItemCount()) {
                PlaybackActivity.this.mCoverFlowCarousel.setSelectPositionByScroll(i);
            }
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.PlaybackSeekbar.OnStatechangeListener
        public void onBrowseContentChange(long j, int i) {
            PlaybackActivity.this.mPositionTime.setText(PlaybackActivity.yyyyMMddHHmmss.format(Long.valueOf(j)));
            ThumbnailInfo item = PlaybackActivity.this.mAdapter.getItem(PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos());
            if (item != null && (PlaybackActivity.this.mVideoThumbnail == null || !PlaybackActivity.this.mVideoThumbnail.isReceiving())) {
                PlaybackActivity.this.requestVideoContentThumbnail(item, i, 1);
            }
            PlaybackActivity.this.handleAutoPlay();
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.PlaybackSeekbar.OnStatechangeListener
        public void onModeChange(int i) {
            Dbug.i(PlaybackActivity.this.tag, "onModeChange=" + PlaybackSeekbar.modeToString(i));
            if (i == 0) {
                if (PlaybackActivity.this.mApplication.isSdcardExist()) {
                    PlaybackActivity.this.enterVideosBrowserMode();
                    return;
                } else {
                    ToastUtil.showToastLong(PlaybackActivity.this.getString(R.string.sdcard_offline));
                    return;
                }
            }
            if (i == 1) {
                PlaybackActivity.this.mCancel.setVisibility(0);
            } else {
                if (i != 2) {
                    return;
                }
                Dbug.i(PlaybackActivity.this.tag, "real time mode");
                PlaybackActivity.this.mHandler.removeCallbacks(PlaybackActivity.this.autoPlayRunnable);
                PlaybackActivity.this.updateModeUI(257);
            }
        }
    };
    private final FrameCodec.OnFrameCodecListener mOnFrameCodecListener = new FrameCodec.OnFrameCodecListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.10
        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onCompleted(byte[] bArr, MediaMeta mediaMeta) {
            Dbug.i(PlaybackActivity.this.tag, "mOnFrameCodecListener");
            if (mediaMeta == null) {
                return;
            }
            RequestFileInfo peek = PlaybackActivity.this.thumbnailRequestQueue.peek();
            if (peek == null || (peek.getFileInfo() != null && !mediaMeta.getPath().equals(peek.getFileInfo().getPath()))) {
                Dbug.i(PlaybackActivity.this.tag, " mediaMeta.getPath err=" + mediaMeta.getPath());
                PlaybackActivity.this.stopThumbnailReceive();
                return;
            }
            PlaybackActivity.this.thumbnailRequestQueue.poll();
            if (PlaybackActivity.this.thumbnailRequestQueue.isEmpty()) {
                PlaybackActivity.this.stopThumbnailReceive();
            }
            if (peek.isContent()) {
                int selectedPos = PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos();
                if (selectedPos < PlaybackActivity.this.mAdapter.getItemCount() && PlaybackActivity.this.mAdapter.getItem(selectedPos).getPath().equals(mediaMeta.getPath())) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    PlaybackActivity.this.mAdapter.getItem(selectedPos).setBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options));
                    PlaybackActivity.this.mAdapter.setContentThumbnailFlag(selectedPos);
                }
            } else {
                File file = new File(AppUtils.splicingFilePath(PlaybackActivity.this.mApplication.getAppName(), PlaybackActivity.this.mApplication.getUUID(), AppUtils.getMediaDirectory(peek.getFileInfo().getCameraType()), IConstant.DIR_THUMB), AppUtils.getVideoThumbName(peek.getFileInfo()));
                if (file.exists()) {
                    return;
                } else {
                    AppUtils.bytesToFile(bArr, file.getPath());
                }
            }
            PlaybackActivity.this.runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.10.1
                @Override // java.lang.Runnable
                public void run() {
                    PlaybackActivity.this.updateCoverFlowCarousel();
                    PlaybackActivity.this.playbackSeekbar.setFileInfoCount(PlaybackActivity.this.mAdapter.getItemCount());
                }
            });
        }

        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onError(String str) {
            Dbug.i(PlaybackActivity.this.tag, "mOnFrameCodecListener err=" + str);
            PlaybackActivity.this.thumbnailRequestQueue.poll();
            if (PlaybackActivity.this.thumbnailRequestQueue.isEmpty()) {
                PlaybackActivity.this.stopThumbnailReceive();
            }
        }
    };
    private Runnable deleteFileRunnable = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.15
        @Override // java.lang.Runnable
        public void run() {
            if (PlaybackActivity.this.mAdapter != null) {
                ThumbnailInfo item = PlaybackActivity.this.mAdapter.getItem(PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos());
                if (item != null) {
                    if (item.getType() == 2) {
                        PlaybackActivity.this.showOperationFileDialog(0, item);
                        return;
                    } else {
                        PlaybackActivity.this.showDeleteFileDialog();
                        return;
                    }
                }
                ToastUtil.showToastShort(PlaybackActivity.this.getString(R.string.null_data));
            }
        }
    };
    private Runnable switchCameraRunnable = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.16
        @Override // java.lang.Runnable
        public void run() {
            if (!PlaybackActivity.this.isSwitchCamera) {
                PlaybackActivity.this.closeRTS();
                PlaybackActivity.this.isSwitchCamera = true;
                PlaybackActivity.this.mApplication.getDeviceSettingInfo().setCameraType(PlaybackActivity.this.mApplication.getDeviceSettingInfo().getCameraType() == 1 ? 2 : 1);
                if (PlaybackActivity.this.isRtspEnable) {
                    PlaybackActivity.this.openRTS();
                    PlaybackActivity.this.updateResolutionIcon();
                    return;
                }
                return;
            }
            Dbug.w(PlaybackActivity.this.tag, "switchCameraRunnable: isSwitchCamera is true");
        }
    };
    private final OnNotifyListener onNotifyListener = new AnonymousClass17();
    private PopupMenu.OnPopItemClickListener mOnPopItemClickListener = new PopupMenu.OnPopItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.23
        @Override // com.jieli.stream.dv.running2.ui.widget.PopupMenu.OnPopItemClickListener
        public void onItemClick(int i, Integer num, int i2) {
            PlaybackActivity.this.switchStreamResolution(i);
        }
    };
    private IMediaController iMediaController = new IMediaController() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.37
        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void hide() {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setAnchorView(View view) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void show() {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void show(int i) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void showOnce(View view) {
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public boolean isShowing() {
            return PlaybackActivity.this.centerControlBar != null && PlaybackActivity.this.centerControlBar.getVisibility() == 0;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
        public void setEnabled(boolean z) {
            Dbug.i(PlaybackActivity.this.tag, "setEnabled : " + z);
            if (!z || PlaybackActivity.this.mLoadingView == null || PlaybackActivity.this.mLoadingView.getVisibility() == 8) {
                return;
            }
            PlaybackActivity.this.mLoadingView.setVisibility(8);
        }
    };
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.38
        private boolean isClick = true;
        private int startX;
        private int startY;

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int action = motionEvent.getAction();
            if (action == 0) {
                this.startX = (int) x;
                this.startY = (int) y;
            } else if (action == 1) {
                if (Math.abs(x - this.startX) > PlaybackActivity.this.threshold || Math.abs(y - this.startY) > PlaybackActivity.this.threshold) {
                    this.isClick = false;
                }
                if (this.isClick) {
                    PlaybackActivity.this.handlerControlBarUI();
                }
                this.isClick = true;
            }
            return true;
        }
    };
    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.39
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
            Dbug.e(PlaybackActivity.this.tag, "Error: framework_err=" + i + ",impl_err=" + i2);
            if (i == -10000) {
                PlaybackActivity.this.mLoadingView.setVisibility(8);
                return true;
            }
            PlaybackActivity.this.closeRTS();
            PlaybackActivity playbackActivity = PlaybackActivity.this;
            playbackActivity.showErrorDialog(playbackActivity.getString(R.string.fail_to_play));
            return true;
        }
    };
    private final IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.40
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            PlaybackActivity.this.mLoadingView.setVisibility(8);
        }
    };
    private final IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.41
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            PlaybackActivity.this.mRTSPlayButton.setVisibility(0);
        }
    };
    private OnRealTimeListener realtimePlayerListener = new OnRealTimeListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.42
        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onVideo(int i, int i2, byte[] bArr, long j, long j2) {
            if (PlaybackActivity.this.isStartDebug && PreferencesHelper.getSharedPreferences(PlaybackActivity.this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
                PlaybackActivity.access$1608(PlaybackActivity.this);
            }
            if (PlaybackActivity.this.mRecordVideo != null && PlaybackActivity.this.isRecordPrepared && !PlaybackActivity.this.mRecordVideo.write(i, bArr)) {
                Dbug.e(PlaybackActivity.this.tag, "Write video failed");
            }
            if (PlaybackActivity.this.mVideoCapture == null || !PlaybackActivity.this.isCapturePrepared) {
                return;
            }
            PlaybackActivity.this.mVideoCapture.capture(bArr);
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onAudio(int i, int i2, byte[] bArr, long j, long j2) {
            if (PlaybackActivity.this.mRecordVideo == null || !PlaybackActivity.this.isRecordPrepared || PlaybackActivity.this.mRecordVideo.write(i, bArr)) {
                return;
            }
            Dbug.e(PlaybackActivity.this.tag, "Write audio failed");
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onStateChanged(int i) {
            Dbug.i(PlaybackActivity.this.tag, "onStateChanged:state=" + i);
            if (i == 5) {
                PlaybackActivity.this.stopLocalRecording();
                if (PlaybackActivity.this.mRTSPlayButton.getVisibility() != 0) {
                    PlaybackActivity.this.mRTSPlayButton.setVisibility(0);
                }
            }
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onError(int i, String str) {
            Dbug.e(PlaybackActivity.this.tag, "code=" + i + ", message=" + str);
            PlaybackActivity.this.closeRTS();
        }
    };
    private final NotifyDialog.OnCheckedChangeListener onCheckedChangeListener = new NotifyDialog.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.49
        @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnCheckedChangeListener
        public void onCheckedChanged(boolean z) {
            PlaybackActivity.this.isChecked = z;
        }
    };
    private IDebugListener mIDebugListener = new IDebugListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.53
        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onStartDebug(String str, int i, int i2) {
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onDebugResult(int i, int i2) {
            PlaybackActivity.this.updateDebug(i, i2);
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onError(int i, String str) {
            Dlog.w(PlaybackActivity.this.tag, str);
        }
    };

    static /* synthetic */ int access$1608(PlaybackActivity playbackActivity) {
        int i = playbackActivity.fps;
        playbackActivity.fps = i + 1;
        return i;
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Dbug.i(this.tag, "==================CREATE===============");
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        requestWindowFeature(1);
        attributes.flags = 1024;
        getWindow().setAttributes(attributes);
        setContentView(R.layout.activity_playback);
        this.isRtspEnable = PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.KEY_RTSP, false);
        PowerManager powerManager = (PowerManager) getSystemService("power");
        if (powerManager != null) {
            this.wakeLock = powerManager.newWakeLock(6, this.tag);
        }
        this.wakeLock.setReferenceCounted(false);
        this.threshold = AppUtils.dp2px(this, 20);
        initUI();
        Intent intent = getIntent();
        if ((intent != null ? (FileInfo) intent.getSerializableExtra(IConstant.KEY_FILE_INFO) : null) != null) {
            this.playbackMode = MODE_BROWSE;
            this.playbackSeekbar.setModeNotCallback(0);
        } else {
            this.playbackMode = 257;
            this.playbackSeekbar.setModeNotCallback(2);
        }
        if (!this.mApplication.isSdcardExist()) {
            this.playbackSeekbar.setVisibility(4);
        }
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
        requestFileMsgText();
        this.mAdapter.setOnItemClickListener(new CoverAdapter.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.3
            @Override // com.jieli.stream.dv.running2.ui.adapter.CoverAdapter.OnItemClickListener
            public void onItemClick(View view, int i) {
                PlaybackActivity.this.recordStatus = MainApplication.getApplication().getDeviceSettingInfo().getRecordState();
                if (PlaybackActivity.this.recordStatus == 1) {
                    PlaybackActivity.this.mHandler.removeCallbacks(PlaybackActivity.this.autoPlayRunnable);
                    PlaybackActivity.this.tryToStopRecording(i);
                } else {
                    ThumbnailInfo item = PlaybackActivity.this.mAdapter.getItem(i);
                    ToastUtil.showToastShort(item.getName());
                    PlaybackActivity.this.showPlaybackDialog(item.getPath(), item.getStartTime().getTimeInMillis());
                }
            }

            @Override // com.jieli.stream.dv.running2.ui.adapter.CoverAdapter.OnItemClickListener
            public void onItemLongClick(View view, int i) {
                PlaybackActivity.this.mHandler.removeCallbacks(PlaybackActivity.this.autoPlayRunnable);
                ThumbnailInfo item = PlaybackActivity.this.mAdapter.getItem(i);
                if (item != null) {
                    PlaybackActivity.this.showProgressDialog(item);
                }
            }
        });
        this.mCoverFlowCarousel.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.4
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i != 1 || PlaybackActivity.this.mAdapter.getItemCount() <= 0) {
                    return;
                }
                PlaybackActivity.this.mHandler.removeCallbacks(PlaybackActivity.this.autoPlayRunnable);
                PlaybackActivity.this.playbackSeekbar.setMode(1);
                PlaybackActivity.this.mCancel.setVisibility(0);
            }
        });
        this.mCoverFlowCarousel.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.5
            @Override // com.jieli.stream.dv.running2.ui.widget.coverflow.CoverFlowLayoutManger.OnSelected
            public void onItemSelected(int i) {
                if (i < 0 || i >= PlaybackActivity.this.mAdapter.getItemCount() || i >= PlaybackActivity.this.mFileInfoList.size()) {
                    return;
                }
                Dbug.i(PlaybackActivity.this.tag, "selected position=" + i);
                if (PlaybackActivity.this.mAdapter.getItem(i) == null) {
                    Dbug.e(PlaybackActivity.this.tag, "The position of file info is null");
                } else {
                    PlaybackActivity.this.handleSelectedFile(i);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSelectedFile(int i) {
        updateCoverFlowCarousel();
        if (i == this.mAdapter.getItemCount() - 1 && this.mAdapter.getItemCount() < this.mFileInfoList.size() && this.thumbnailRequestQueue.isEmpty()) {
            Dbug.i(this.tag, "position=" + i + ", getItemCount=" + this.mAdapter.getItemCount());
            requestVideoThumbnail(this.mFileInfoList.subList(i + 1, this.mAdapter.getItemCount() + 10 < this.mFileInfoList.size() ? this.mAdapter.getItemCount() + 10 : this.mFileInfoList.size()));
        }
        ThumbnailInfo item = this.mAdapter.getItem(this.mCoverFlowCarousel.getSelectedPos());
        if (item != null) {
            Dbug.i(this.tag, "mSelectedFileInfo--> name=" + item.getName() + "  time=" + item.getCreateTime());
            this.mPositionTime.setText(yyyyMMddHHmmss.format(Long.valueOf(item.getStartTime().getTimeInMillis())));
            this.mAdapter.clearContentThumbnail();
            this.playbackSeekbar.setFileInfo(item);
            handlerFileLockState(item);
            handleAutoPlay();
        }
    }

    private void initUI() {
        this.topBar = (RelativeLayout) findViewById(R.id.playback_top_bar);
        this.mReturn = (ImageView) findViewById(R.id.return_back);
        this.tvTop = (TextView) findViewById(R.id.playback_top_tv);
        this.mCancel = (ImageView) findViewById(R.id.cancle_time_select_ibtn);
        IjkVideoView ijkVideoView = (IjkVideoView) findViewById(R.id.playback_rt_stream_view);
        this.mStreamView = ijkVideoView;
        ijkVideoView.setAspectRatio(3);
        this.mStreamView.setOnErrorListener(this.mOnErrorListener);
        this.mStreamView.setMediaController(this.iMediaController);
        this.mStreamView.setOnTouchListener(this.mOnTouchListener);
        this.mStreamView.setOnCompletionListener(this.onCompletionListener);
        this.mStreamView.setOnPreparedListener(this.onPreparedListener);
        this.mLoadingView = (ProgressBar) findViewById(R.id.playback_rt_stream_loading);
        this.layoutPreview = (RelativeLayout) findViewById(R.id.playback_preview_mode);
        this.leftControlBar = (LinearLayout) findViewById(R.id.preview_mode_left_bar);
        this.mAdjustResolutionBtn = (ImageView) findViewById(R.id.left_bar_adjust_resolution_btn);
        this.mProjectionBtn = (ImageView) findViewById(R.id.left_bar_projection_btn);
        this.mBrowseGallery = (ImageView) findViewById(R.id.left_bar_browse_photo_btn);
        this.centerControlBar = (LinearLayout) findViewById(R.id.preview_mode_center_bar);
        this.mPhotoBtn = (ImageView) findViewById(R.id.center_bar_photo_btn);
        this.mVoiceBtn = (ImageView) findViewById(R.id.center_bar_voice_btn);
        this.mVideoBtn = (ImageView) findViewById(R.id.center_bar_video_btn);
        this.mRecordFlag = (ImageView) findViewById(R.id.status_bar_record_flag);
        this.mProjectionFlag = (ImageView) findViewById(R.id.status_bar_projection_flag);
        this.mSwitchCameraBtn = (ImageView) findViewById(R.id.left_bar_switch_camera_btn);
        this.mCycSaveVideo = (ImageView) findViewById(R.id.left_bar_cyc_save_video);
        this.mRTSPlayButton = (ImageButton) findViewById(R.id.rts_play);
        this.layoutBrowse = (RelativeLayout) findViewById(R.id.playback_browse_mode);
        this.mPositionTime = (TextView) findViewById(R.id.position_of_time);
        this.tvAutoPlayTip = (TextView) findViewById(R.id.auto_play_tip);
        this.mCoverFlowCarousel = (RecyclerCoverFlow) findViewById(R.id.carousel);
        this.playbackSeekbar = (PlaybackSeekbar) findViewById(R.id.time_seekbar);
        this.mLockFileBtn = (ImageView) findViewById(R.id.left_bar_lock_btn);
        this.mDeleteFileBtn = (ImageView) findViewById(R.id.left_bar_delete_file_btn);
        this.mHudView = (TableLayout) findViewById(R.id.hud_view);
        this.tvAutoPlayTip.setVisibility(4);
        this.mReturn.setOnClickListener(this);
        this.mCancel.setOnClickListener(this);
        this.mAdjustResolutionBtn.setOnClickListener(this);
        this.mProjectionBtn.setOnClickListener(this);
        this.mBrowseGallery.setOnClickListener(this);
        this.mPhotoBtn.setOnClickListener(this);
        this.mVoiceBtn.setOnClickListener(this);
        this.mVideoBtn.setOnClickListener(this);
        this.playbackSeekbar.setOnStatechangeListener(this.onStatechangeListener);
        this.mSwitchCameraBtn.setOnClickListener(this);
        this.mLockFileBtn.setOnClickListener(this);
        this.mDeleteFileBtn.setOnClickListener(this);
        this.mCycSaveVideo.setOnClickListener(this);
        this.mRTSPlayButton.setOnClickListener(this);
        DeviceDesc deviceDesc = MainApplication.getApplication().getDeviceDesc();
        if (deviceDesc != null) {
            if (!deviceDesc.isSupport_projection()) {
                this.mProjectionBtn.setVisibility(8);
            } else {
                this.mProjectionBtn.setVisibility(0);
            }
        }
        CoverAdapter coverAdapter = new CoverAdapter(this);
        this.mAdapter = coverAdapter;
        this.mCoverFlowCarousel.setAdapter(coverAdapter);
        NotifyDialog newInstance = NotifyDialog.newInstance(R.string.loading, true, -1, R.string.dialog_cancel, new NotifyDialog.OnConfirmClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.6
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnConfirmClickListener
            public void onClick() {
                PlaybackActivity.this.mLoadingDialog.dismiss();
                PlaybackActivity.this.onBackPressed();
            }
        });
        this.mLoadingDialog = newInstance;
        newInstance.setOnDismissDialogListener(new NotifyDialog.OnDismissDialogListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.7
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnDismissDialogListener
            public void onDismiss() {
                ThumbnailInfo item;
                Dbug.w(PlaybackActivity.this.tag, "mLoadingDialog is dismiss.");
                Intent intent = PlaybackActivity.this.getIntent();
                FileInfo fileInfo = intent != null ? (FileInfo) intent.getSerializableExtra(IConstant.KEY_FILE_INFO) : null;
                if (fileInfo == null) {
                    Dbug.e(PlaybackActivity.this.tag, "fileInfo is null.");
                    return;
                }
                int findFileInfoIndex = PlaybackActivity.this.findFileInfoIndex(fileInfo);
                if (PlaybackActivity.this.playbackMode != PlaybackActivity.MODE_BROWSE || PlaybackActivity.this.mFileInfoList == null || findFileInfoIndex <= -1 || findFileInfoIndex >= PlaybackActivity.this.mAdapter.getItemCount() || (item = PlaybackActivity.this.mAdapter.getItem(findFileInfoIndex)) == null) {
                    return;
                }
                PlaybackActivity.this.mCoverFlowCarousel.setSelectPosition(findFileInfoIndex);
                PlaybackActivity.this.playbackSeekbar.setBrowsePostion(findFileInfoIndex);
                PlaybackActivity.this.playbackSeekbar.setFileInfo(item);
                PlaybackActivity.this.playbackSeekbar.setMode(1);
                if (findFileInfoIndex == 0) {
                    PlaybackActivity.this.handleSelectedFile(findFileInfoIndex);
                }
            }
        });
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        registerBroadcast();
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IActions.ACTION_FORMAT_TF_CARD);
        MainApplication.getApplication().registerReceiver(this.mReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAutoPlay() {
        if (this.isOnPause || this.mHandler == null || this.mAdapter.getItemCount() <= 0) {
            return;
        }
        Dbug.i(this.tag, "handleAutoPlay");
        this.tvAutoPlayTip.setVisibility(0);
        this.mHandler.removeCallbacks(this.autoPlayRunnable);
        this.mHandler.postDelayed(this.autoPlayRunnable, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enterVideosBrowserMode() {
        this.mCancel.setVisibility(4);
        if (this.playbackMode == 257) {
            if (this.mAdapter.getItemCount() > 0) {
                ThumbnailInfo item = this.mAdapter.getItem(this.mCoverFlowCarousel.getSelectedPos());
                this.playbackSeekbar.setFileInfo(item);
                this.playbackSeekbar.setFileInfoCount(this.mAdapter.getItemCount());
                this.mPositionTime.setText(yyyyMMddHHmmss.format(Long.valueOf(item.getStartTime().getTimeInMillis())));
                handleAutoPlay();
            } else {
                this.mHandler.removeCallbacks(this.autoPlayRunnable);
            }
        }
        updateModeUI(MODE_BROWSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveBitmapAndUpdateUI(byte[] bArr) {
        RequestFileInfo poll = this.thumbnailRequestQueue.poll();
        if (poll == null) {
            Dbug.e(this.tag, "saveBitmapAndUpdateUI is null");
            stopThumbnailReceive();
            return;
        }
        if (this.thumbnailRequestQueue.isEmpty()) {
            stopThumbnailReceive();
        }
        if (poll.isContent()) {
            int selectedPos = this.mCoverFlowCarousel.getSelectedPos();
            if (selectedPos < this.mAdapter.getItemCount() && this.mAdapter.getItem(selectedPos).getPath().equals(poll.getFileInfo().getPath())) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                this.mAdapter.getItem(selectedPos).setBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options));
                this.mAdapter.setContentThumbnailFlag(selectedPos);
            }
        } else {
            File file = new File(AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.getMediaDirectory(poll.getFileInfo().getCameraType()), IConstant.DIR_THUMB), AppUtils.getVideoThumbName(poll.getFileInfo()));
            if (file.exists()) {
                Dbug.w(this.tag, "File exist=" + file.getAbsolutePath());
                return;
            }
            AppUtils.bytesToFile(bArr, file.getPath());
        }
        runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.11
            @Override // java.lang.Runnable
            public void run() {
                PlaybackActivity.this.updateCoverFlowCarousel();
                PlaybackActivity.this.playbackSeekbar.setFileInfoCount(PlaybackActivity.this.mAdapter.getItemCount());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCoverFlowCarousel() {
        if (this.mCoverFlowCarousel.getScrollState() != 0 || this.mCoverFlowCarousel.isComputingLayout() || isDestroyed()) {
            return;
        }
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestFileMsgText() {
        NotifyDialog notifyDialog = this.mLoadingDialog;
        if (notifyDialog != null && !notifyDialog.isShowing()) {
            this.mLoadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
        }
        this.mCameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        Dbug.w(this.tag, "requestFileMsgText********************************mCameraType=" + this.mCameraType);
        ClientManager.getClient().tryToRequestMediaFiles(this.mCameraType, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.12
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(PlaybackActivity.this.tag, "Send failed");
                    if (PlaybackActivity.this.mLoadingDialog == null || !PlaybackActivity.this.mLoadingDialog.isShowing()) {
                        return;
                    }
                    PlaybackActivity.this.mLoadingDialog.dismiss();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Handler handler;
        ThumbnailInfo item;
        if (view == this.mReturn) {
            if (this.isRecordPrepared) {
                showStopRecordingDialog(new OnClickStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.13
                    @Override // com.jieli.stream.dv.running2.interfaces.OnClickStateListener
                    public void onCancel() {
                    }

                    @Override // com.jieli.stream.dv.running2.interfaces.OnClickStateListener
                    public void onConfirm() {
                        PlaybackActivity.this.stopLocalRecording();
                        PlaybackActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.13.1
                            @Override // java.lang.Runnable
                            public void run() {
                                PlaybackActivity.this.onBackPressed();
                            }
                        }, 100L);
                    }
                });
                return;
            } else {
                onBackPressed();
                return;
            }
        }
        if (view == this.mCancel) {
            this.mCoverFlowCarousel.stopScroll();
            this.playbackSeekbar.setMode(0);
            this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.14
                @Override // java.lang.Runnable
                public void run() {
                    Dbug.i(PlaybackActivity.this.tag, " click mCancle btn setBrowsePostion ");
                    PlaybackActivity.this.playbackSeekbar.setBrowsePostion(PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos());
                }
            }, 50L);
            return;
        }
        if (view == this.mAdjustResolutionBtn) {
            if (this.mStreamView.isPlaying() || isPlaying()) {
                if ("1".equals(this.mApplication.getDeviceDesc().getDevice_type()) && this.recordStatus == 1) {
                    ToastUtil.showToastShort(getString(R.string.stop_recording_first));
                    return;
                } else {
                    showPopupMenu(view);
                    return;
                }
            }
            ToastUtil.showToastShort(getString(R.string.open_rts_tip));
            return;
        }
        if (view == this.mProjectionBtn) {
            Handler handler2 = this.mHandler;
            if (handler2 != null) {
                handler2.removeMessages(MSG_PROJECTION_CONTROL);
                this.mHandler.sendEmptyMessageDelayed(MSG_PROJECTION_CONTROL, 500L);
                return;
            }
            return;
        }
        if (view == this.mBrowseGallery) {
            String videosDescription = JSonManager.getInstance().getVideosDescription();
            if (!isSdOnline() || TextUtils.isEmpty(videosDescription)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(IConstant.KEY_VIDEO_LIST, videosDescription);
            Intent intent = new Intent(this, (Class<?>) GenericActivity.class);
            intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 6);
            intent.putExtra(IConstant.KEY_DATA, bundle);
            startActivityForResult(intent, IConstant.CODE_BROWSE_FILE);
            return;
        }
        if (view == this.mPhotoBtn) {
            Handler handler3 = this.mHandler;
            if (handler3 != null) {
                handler3.removeMessages(MSG_TAKE_PHOTO);
                this.mHandler.sendEmptyMessageDelayed(MSG_TAKE_PHOTO, 100L);
                return;
            }
            return;
        }
        if (view == this.mVoiceBtn) {
            Handler handler4 = this.mHandler;
            if (handler4 != null) {
                handler4.removeMessages(MSG_RT_VOICE_CONTROL);
                this.mHandler.sendEmptyMessageDelayed(MSG_RT_VOICE_CONTROL, 100L);
                return;
            }
            return;
        }
        if (view == this.mVideoBtn) {
            Handler handler5 = this.mHandler;
            if (handler5 != null) {
                handler5.removeMessages(MSG_TAKE_VIDEO);
                this.mHandler.sendEmptyMessageDelayed(MSG_TAKE_VIDEO, 100L);
                return;
            }
            return;
        }
        if (view == this.mSwitchCameraBtn) {
            Handler handler6 = this.mHandler;
            if (handler6 != null) {
                handler6.removeCallbacks(this.switchCameraRunnable);
                this.mHandler.postDelayed(this.switchCameraRunnable, 300L);
                return;
            }
            return;
        }
        if (view == this.mLockFileBtn) {
            Handler handler7 = this.mHandler;
            if (handler7 != null) {
                handler7.removeCallbacks(this.autoPlayRunnable);
            }
            CoverAdapter coverAdapter = this.mAdapter;
            if (coverAdapter == null || (item = coverAdapter.getItem(this.mCoverFlowCarousel.getSelectedPos())) == null) {
                return;
            }
            showOperationFileDialog(1, item);
            return;
        }
        if (view == this.mDeleteFileBtn) {
            Handler handler8 = this.mHandler;
            if (handler8 != null) {
                handler8.removeCallbacks(this.autoPlayRunnable);
                this.mHandler.removeCallbacks(this.deleteFileRunnable);
                this.mHandler.postDelayed(this.deleteFileRunnable, 100L);
                return;
            }
            return;
        }
        if (view == this.mCycSaveVideo) {
            if (!isSdOnline() || (handler = this.mHandler) == null) {
                return;
            }
            handler.removeMessages(MSG_CYC_SAVE_VIDEO);
            this.mHandler.sendEmptyMessageDelayed(MSG_CYC_SAVE_VIDEO, 100L);
            return;
        }
        if (view == this.mRTSPlayButton) {
            openRTS();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
        setResult(-1);
        finish();
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity$17, reason: invalid class name */
    class AnonymousClass17 implements OnNotifyListener {
        AnonymousClass17() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:373:0x0237, code lost:
        
            if (r2.equals(com.jieli.lib.dv.control.utils.Topic.CLOSE_RT_STREAM) != false) goto L131;
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x00ab, code lost:
        
            if (r2.equals("MULTI_COVER_FIGURE") != false) goto L37;
         */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Removed duplicated region for block: B:321:0x0a45  */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo r18) {
            /*
                Method dump skipped, instructions count: 3026
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.AnonymousClass17.onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopThumbnailReceive() {
        Dbug.i(this.tag, "--------------------------------------stopThumbnailReceive--------------------------");
        VideoThumbnail videoThumbnail = this.mVideoThumbnail;
        if (videoThumbnail != null) {
            videoThumbnail.close();
        }
        List<FileInfo> fileInfos = this.thumbnailRequestQueue.getFileInfos();
        if (fileInfos == null || fileInfos.size() <= 0) {
            return;
        }
        requestVideoThumbnail(fileInfos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTFOffline() {
        if (this.playbackSeekbar.getMode() == 2) {
            List<FileInfo> list = this.mFileInfoList;
            if (list != null) {
                list.clear();
            }
            this.mAdapter.clear();
            this.mCoverFlowCarousel.setAdapter(this.mAdapter);
            this.playbackSeekbar.setFileInfoCount(0);
            this.thumbnailRequestQueue.clear();
            this.mPositionTime.setVisibility(4);
            this.tvAutoPlayTip.setVisibility(4);
            this.playbackSeekbar.setVisibility(4);
            return;
        }
        Dbug.w(this.tag, "onBackPressed ...........>");
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToParseData(String str) {
        if (TextUtils.isEmpty(str)) {
            Dbug.e(this.tag, "tryToParseData: desc is empty!!");
        } else {
            JSonManager.getInstance().parseJSonData(str, new OnCompletedListener<Boolean>() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.18
                @Override // com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener
                public void onCompleted(Boolean bool) {
                    Dbug.i(PlaybackActivity.this.tag, "data state=" + bool);
                    if (bool.booleanValue()) {
                        PlaybackActivity.this.mFileInfoList = JSonManager.getInstance().getVideoInfoList();
                        if (PlaybackActivity.this.mFileInfoList == null || PlaybackActivity.this.mFileInfoList.size() <= 0) {
                            Dbug.e(PlaybackActivity.this.tag, "video fileInfoList is 0");
                            if (PlaybackActivity.this.mLoadingDialog == null || !PlaybackActivity.this.mLoadingDialog.isShowing()) {
                                return;
                            }
                            PlaybackActivity.this.mLoadingDialog.dismiss();
                            return;
                        }
                        Dbug.d(PlaybackActivity.this.tag, "total thumbnail count =" + PlaybackActivity.this.mFileInfoList.size());
                        AppUtils.descSortWay(PlaybackActivity.this.mFileInfoList);
                        int size = PlaybackActivity.this.mFileInfoList.size() <= 10 ? PlaybackActivity.this.mFileInfoList.size() : 10;
                        PlaybackActivity playbackActivity = PlaybackActivity.this;
                        playbackActivity.requestVideoThumbnail(playbackActivity.mFileInfoList.subList(0, size));
                        return;
                    }
                    if (PlaybackActivity.this.mLoadingDialog == null || !PlaybackActivity.this.mLoadingDialog.isShowing()) {
                        return;
                    }
                    PlaybackActivity.this.mLoadingDialog.dismiss();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestVideoThumbnail(List<FileInfo> list) {
        if (list != null) {
            Dbug.e(this.tag, "requestVideoThumbnail " + list.size());
            ArrayList arrayList = new ArrayList();
            for (FileInfo fileInfo : list) {
                String str = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), checkCameraDir(fileInfo), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(fileInfo);
                ThumbnailInfo thumbnailInfo = new ThumbnailInfo();
                thumbnailInfo.setCameraType(fileInfo.getCameraType());
                thumbnailInfo.setPath(fileInfo.getPath());
                thumbnailInfo.setName(fileInfo.getName());
                thumbnailInfo.setVideo(true);
                thumbnailInfo.setDuration(fileInfo.getDuration());
                thumbnailInfo.setSaveUrl(str);
                thumbnailInfo.setStartTime(fileInfo.getStartTime());
                thumbnailInfo.setCreateTime(fileInfo.getCreateTime());
                thumbnailInfo.setEndTime(fileInfo.getEndTime());
                thumbnailInfo.setType(fileInfo.getType());
                this.mAdapter.addData(thumbnailInfo);
                if (!AppUtils.checkFileExist(str)) {
                    arrayList.add(fileInfo.getPath());
                    this.thumbnailRequestQueue.put(thumbnailInfo, false);
                } else {
                    Dbug.e(this.tag, "fileInfo has save ->" + thumbnailInfo.getSaveUrl());
                }
                runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.19
                    @Override // java.lang.Runnable
                    public void run() {
                        PlaybackActivity.this.playbackSeekbar.setFileInfoCount(PlaybackActivity.this.mAdapter.getItemCount());
                        PlaybackActivity.this.updateCoverFlowCarousel();
                    }
                });
            }
            if (arrayList.size() != 0) {
                ClientManager.getClient().tryToRequestVideoCover(arrayList, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.20
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(PlaybackActivity.this.tag, "Send failed");
                            PlaybackActivity.this.thumbnailRequestQueue.clear();
                        }
                    }
                });
            } else {
                Dbug.i(this.tag, "request list =null");
                runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.21
                    @Override // java.lang.Runnable
                    public void run() {
                        if (PlaybackActivity.this.mLoadingDialog == null || !PlaybackActivity.this.mLoadingDialog.isShowing()) {
                            return;
                        }
                        PlaybackActivity.this.mLoadingDialog.dismiss();
                    }
                });
            }
        }
    }

    private String checkCameraDir(FileInfo fileInfo) {
        return (fileInfo == null || !"1".equals(fileInfo.getCameraType())) ? IConstant.DIR_FRONT : IConstant.DIR_REAR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestVideoContentThumbnail(final FileInfo fileInfo, int i, final int i2) {
        if (fileInfo != null) {
            Dbug.e(this.tag, "requestVideoContentThumbnail " + fileInfo.getPath() + ", offset " + i + ", num " + i2);
            ClientManager.getClient().tryToRequestVideoContentThumbnail(fileInfo.getPath(), i, 1000, i2, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.22
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(PlaybackActivity.this.tag, "Send failed");
                        return;
                    }
                    Dbug.e(PlaybackActivity.this.tag, "Send success");
                    for (int i3 = 0; i3 < i2; i3++) {
                        PlaybackActivity.this.thumbnailRequestQueue.put(fileInfo, true);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlaybackDialog(String str, long j) {
        this.mHandler.removeCallbacks(this.autoPlayRunnable);
        Bundle bundle = new Bundle();
        bundle.putString(IConstant.VIDEO_PATH, str);
        bundle.putLong(IConstant.VIDEO_CREATE_TIME, j);
        bundle.putInt(IConstant.VIDEO_OFFSET, this.playbackSeekbar.getOffset());
        Intent intent = new Intent(this, (Class<?>) PlaybackDialogActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        this.tvAutoPlayTip.setVisibility(4);
    }

    private void showPopupMenu(View view) {
        int streamResolutionLevel;
        String[] streamResolutions;
        if (this.isRtspEnable) {
            streamResolutionLevel = AppUtils.getRtspResolutionLevel();
            streamResolutions = AppUtils.getRtspResolutions();
        } else {
            streamResolutionLevel = AppUtils.getStreamResolutionLevel();
            streamResolutions = AppUtils.getStreamResolutions();
        }
        if (streamResolutions == null || streamResolutions.length <= 1) {
            Dbug.e(this.tag, "showPopupMenu <=1");
            return;
        }
        int cameraLevel = getCameraLevel(this.mApplication.getDeviceSettingInfo().getCameraType());
        Dbug.w(this.tag, "levels=" + streamResolutions.length + ", currentRecordLevel=" + cameraLevel + ", currentLevel====== " + streamResolutionLevel);
        HashMap hashMap = new HashMap();
        for (String str : streamResolutions) {
            if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
                int intValue = Integer.valueOf(str).intValue();
                if (this.recordStatus != 1) {
                    if (intValue != streamResolutionLevel) {
                        hashMap.put(Integer.valueOf(intValue), Integer.valueOf(getLevelResId(intValue)));
                    }
                } else if (cameraLevel != 2 || intValue >= 2) {
                    if (cameraLevel < 2 && intValue != streamResolutionLevel) {
                        hashMap.put(Integer.valueOf(intValue), Integer.valueOf(getLevelResId(intValue)));
                    }
                } else if (intValue != streamResolutionLevel) {
                    hashMap.put(Integer.valueOf(intValue), Integer.valueOf(getLevelResId(intValue)));
                }
            }
        }
        dismissPopMenu();
        PopupMenu popupMenu = new PopupMenu(this.mApplication, hashMap);
        this.popupMenu = popupMenu;
        popupMenu.setOnPopItemClickListener(this.mOnPopItemClickListener);
        this.popupMenu.showAsRight(view);
    }

    private void dismissPopMenu() {
        PopupMenu popupMenu = this.popupMenu;
        if (popupMenu != null) {
            if (popupMenu.isShowing()) {
                this.popupMenu.dismiss();
            }
            this.popupMenu = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchStreamResolution(int i) {
        if (isPlaying()) {
            closeRTS();
        }
        if (this.isRtspEnable) {
            AppUtils.saveRtspResolutionLevel(i);
            this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.24
                @Override // java.lang.Runnable
                public void run() {
                    PlaybackActivity.this.openRTS();
                }
            }, 200L);
        } else {
            this.isAdjustResolution = true;
            AppUtils.saveStreamResolutionLevel(i);
            showWaitingDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOperationFileDialog(int i, FileInfo fileInfo) {
        int i2;
        if (fileInfo != null) {
            NotifyDialog notifyDialog = this.operationFileDialog;
            if (notifyDialog != null && notifyDialog.isShowing()) {
                this.operationFileDialog.dismiss();
                this.operationFileDialog = null;
            }
            if (i != 0) {
                if (i == 1) {
                    if (fileInfo.getType() == 2) {
                        i2 = R.string.unlock_file_tip;
                    } else {
                        i2 = R.string.lock_file_tip;
                    }
                    this.operationFileDialog = NotifyDialog.newInstance(R.string.dialog_tips, i2, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.27
                        @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                        public void onClick() {
                            if (PlaybackActivity.this.operationFileDialog == null || !PlaybackActivity.this.operationFileDialog.isShowing()) {
                                return;
                            }
                            PlaybackActivity.this.operationFileDialog.dismiss();
                            PlaybackActivity.this.operationFileDialog = null;
                        }
                    }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.28
                        @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                        public void onClick() {
                            FileInfo fileInfo2;
                            Bundle bundle = PlaybackActivity.this.operationFileDialog.getBundle();
                            if (bundle != null && (fileInfo2 = (FileInfo) bundle.getSerializable(IConstant.KEY_FILE_INFO)) != null) {
                                ClientManager.getClient().tryToFileLock(fileInfo2.getPath(), !(fileInfo2.getType() == 2), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.28.1
                                    @Override // com.jieli.lib.dv.control.connect.response.Response
                                    public void onResponse(Integer num) {
                                    }
                                });
                            }
                            if (PlaybackActivity.this.operationFileDialog == null || !PlaybackActivity.this.operationFileDialog.isShowing()) {
                                return;
                            }
                            PlaybackActivity.this.operationFileDialog.dismiss();
                            PlaybackActivity.this.operationFileDialog = null;
                        }
                    });
                }
            } else {
                this.operationFileDialog = NotifyDialog.newInstance(R.string.dialog_warning, R.string.delete_emergency_video_tip, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.25
                    @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                    public void onClick() {
                        if (PlaybackActivity.this.operationFileDialog == null || !PlaybackActivity.this.operationFileDialog.isShowing()) {
                            return;
                        }
                        PlaybackActivity.this.operationFileDialog.dismiss();
                        PlaybackActivity.this.operationFileDialog = null;
                    }
                }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.26
                    @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                    public void onClick() {
                        Bundle bundle = PlaybackActivity.this.operationFileDialog.getBundle();
                        if (bundle != null) {
                            PlaybackActivity.this.deleteFile((FileInfo) bundle.getSerializable(IConstant.KEY_FILE_INFO));
                        }
                        if (PlaybackActivity.this.operationFileDialog == null || !PlaybackActivity.this.operationFileDialog.isShowing()) {
                            return;
                        }
                        PlaybackActivity.this.operationFileDialog.dismiss();
                        PlaybackActivity.this.operationFileDialog = null;
                    }
                });
            }
            if (this.operationFileDialog != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IConstant.KEY_FILE_INFO, fileInfo);
                this.operationFileDialog.setBundle(bundle);
                if (this.operationFileDialog.isShowing()) {
                    return;
                }
                this.operationFileDialog.show(getSupportFragmentManager(), "operation_dialog");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteFile(FileInfo fileInfo) {
        if (fileInfo != null) {
            this.mReady2DeleteList.clear();
            this.mReady2DeleteList.add(fileInfo.getPath());
            ClientManager.getClient().tryToDeleteFile(this.mReady2DeleteList, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.29
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() == 1) {
                        PlaybackActivity.this.showWaitingDialog();
                    } else {
                        PlaybackActivity.this.dismissWaitingDialog();
                    }
                }
            });
            return;
        }
        Dbug.e(this.tag, "delete file failed");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteAllFiles() {
        this.mReady2DeleteList.clear();
        Iterator<FileInfo> it = JSonManager.getInstance().getVideoInfoList().iterator();
        while (it.hasNext()) {
            this.mReady2DeleteList.add(it.next().getPath());
        }
        if (this.mReady2DeleteList.size() > 0) {
            ClientManager.getClient().tryToDeleteFile(this.mReady2DeleteList, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.30
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() == 1) {
                        PlaybackActivity.this.isDeleteAll = true;
                        PlaybackActivity.this.showWaitingDialog();
                    } else {
                        PlaybackActivity.this.dismissWaitingDialog();
                    }
                }
            });
        } else {
            Dbug.e(this.tag, "delete files failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWaitingDialog() {
        if (isDestroyed()) {
            return;
        }
        if (this.mWaitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.mWaitingDialog = waitingDialog;
            waitingDialog.setNotifyContent(getString(R.string.deleting_tip));
        }
        if (this.mWaitingDialog.isShowing()) {
            return;
        }
        this.mWaitingDialog.show(getSupportFragmentManager(), "waiting_dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing() && !isDestroyed()) {
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissAdjustingDialog() {
        WaitingDialog waitingDialog = this.mAdjustingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing() && !isDestroyed()) {
                this.mAdjustingDialog.dismiss();
            }
            this.mAdjustingDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrorDialog(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        NotifyDialog newInstance = NotifyDialog.newInstance(getString(R.string.dialog_tips), str, R.string.dialog_ok, new NotifyDialog.OnConfirmClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.31
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnConfirmClickListener
            public void onClick() {
                PlaybackActivity.this.dismissErrorDialog();
                PlaybackActivity.this.onBackPressed();
            }
        });
        this.mErrorDialog = newInstance;
        if (newInstance.isShowing()) {
            return;
        }
        this.mErrorDialog.show(getSupportFragmentManager(), "ViewDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissErrorDialog() {
        NotifyDialog notifyDialog = this.mErrorDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing() && !isDestroyed()) {
                this.mErrorDialog.dismiss();
            }
            this.mErrorDialog = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4169 && i2 == -1 && intent != null) {
            ClientManager.getClient().tryToStreamingPush(true, UVCCamera.DEFAULT_PREVIEW_WIDTH, 480, 30, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.32
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() == 1) {
                        PlaybackActivity.this.handlerProjectionUI();
                    }
                }
            });
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.isOnPause = false;
        updateModeUI(this.playbackMode);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.isOnPause = true;
        closeRTS();
        RealtimeStream realtimeStream = this.mRealtimeStream;
        if (realtimeStream != null) {
            realtimeStream.unregisterStreamListener(this.realtimePlayerListener);
            this.mRealtimeStream.release();
            this.mRealtimeStream = null;
        }
        if (this.isRtVoiceOpen) {
            this.mVoiceBtn.performClick();
        }
        this.mHandler.removeCallbacks(this.autoPlayRunnable);
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        MainApplication.getApplication().unregisterReceiver(this.mReceiver);
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.isOnPause = false;
        dismissPopMenu();
        NotifyDialog notifyDialog = this.operationFileDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.operationFileDialog.dismiss();
            }
            this.operationFileDialog = null;
        }
        closeRTS();
        dismissErrorDialog();
        dismissWaitingDialog();
        dismissAdjustingDialog();
        super.onDestroy();
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        VideoThumbnail videoThumbnail = this.mVideoThumbnail;
        if (videoThumbnail != null) {
            videoThumbnail.close();
        }
        FrameCodec frameCodec = this.mFrameCodec;
        if (frameCodec != null) {
            frameCodec.setOnFrameCodecListener(null);
            this.mFrameCodec.destroy();
            this.mFrameCodec = null;
        }
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        AudioRecordManager audioRecordManager = this.mAudioManager;
        if (audioRecordManager != null) {
            audioRecordManager.release();
            this.mAudioManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRTS() {
        if (isPlaying()) {
            Dbug.e(this.tag, "rts is playing, please stop it first.");
            return;
        }
        if (PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
            TableLayout tableLayout = this.mHudView;
            if (tableLayout != null && tableLayout.getVisibility() != 0) {
                this.mHudView.setVisibility(0);
                this.mStreamView.setHudView(this.mHudView);
            }
            startDebug();
        } else {
            TableLayout tableLayout2 = this.mHudView;
            if (tableLayout2 != null && tableLayout2.getVisibility() != 8) {
                this.fps = 0;
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.removeMessages(MSG_FPS_COUNT);
                }
                this.mHudView.setVisibility(8);
                this.mStreamView.setHudView(null);
            }
        }
        if (this.isRtspEnable) {
            initPlayer(AppUtils.getRtspUrl());
            this.isSwitchCamera = false;
            return;
        }
        int rtsFormat = AppUtils.getRtsFormat();
        int cameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        int netMode = this.mApplication.getDeviceDesc().getNetMode();
        if (netMode == 0) {
            createStream(netMode, IConstant.VIDEO_SERVER_PORT);
        }
        Dbug.i(this.tag, "open rts...........");
        int[] rtsResolution = AppUtils.getRtsResolution(AppUtils.getStreamResolutionLevel());
        ClientManager.getClient().tryToOpenRTStream(cameraType, rtsFormat, rtsResolution[0], rtsResolution[1], getVideoRate(cameraType), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.33
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(PlaybackActivity.this.tag, "Send failed!!!");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeRTS() {
        ImageButton imageButton;
        Dbug.i(this.tag, "close rts................");
        deinitPlayer();
        if (PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
            closeDebug();
        }
        if (!this.isRtspEnable && (isPlaying() || this.isSwitchCamera)) {
            ClientManager.getClient().tryToCloseRTStream(this.mApplication.getDeviceSettingInfo().getCameraType(), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.34
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(PlaybackActivity.this.tag, "Send failed!!!");
                    }
                }
            });
        }
        stopLocalRecording();
        VideoCapture videoCapture = this.mVideoCapture;
        if (videoCapture != null) {
            videoCapture.destroy();
            this.mVideoCapture = null;
        }
        RealtimeStream realtimeStream = this.mRealtimeStream;
        if (realtimeStream != null) {
            realtimeStream.close();
            this.mRealtimeStream = null;
        }
        ProgressBar progressBar = this.mLoadingView;
        if (progressBar != null && progressBar.getVisibility() != 8) {
            this.mLoadingView.setVisibility(8);
        }
        if (this.playbackMode != 257 || (imageButton = this.mRTSPlayButton) == null || imageButton.getVisibility() == 0) {
            return;
        }
        this.mRTSPlayButton.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayer(String str) {
        if (this.mStreamView != null && !TextUtils.isEmpty(str)) {
            Uri parse = Uri.parse(str);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.isIJKPlayerOpen = true;
            this.mStreamView.setRealtime(true);
            this.mStreamView.setVideoURI(parse);
            this.mStreamView.start();
            this.mRTSPlayButton.setVisibility(8);
            this.mLoadingView.setVisibility(0);
            return;
        }
        Dbug.e(this.tag, "init player fail");
    }

    private void deinitPlayer() {
        Dbug.w(this.tag, "deinit player");
        IjkVideoView ijkVideoView = this.mStreamView;
        if (ijkVideoView != null) {
            ijkVideoView.stopPlayback();
            this.mStreamView.release(true);
            this.mStreamView.stopBackgroundPlay();
        }
        if (this.isIJKPlayerOpen) {
            IjkMediaPlayer.native_profileEnd();
        }
        this.isIJKPlayerOpen = false;
    }

    public void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < 21) {
            ToastUtil.showToastLong(getString(R.string.projection_not_support));
            return;
        }
        this.isProjection = true;
        this.mApplication.getDeviceSettingInfo().setOpenProjection(true);
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        if (mediaProjectionManager != null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), IConstant.REQUEST_MEDIA_PROJECTION);
        }
    }

    private boolean isSdOnline() {
        if (!MainApplication.getApplication().isSdcardExist()) {
            ToastUtil.showToastShort(getResources().getString(R.string.sdcard_offline));
        }
        return MainApplication.getApplication().isSdcardExist();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findFileInfoIndex(FileInfo fileInfo) {
        int i;
        if (fileInfo != null && this.mFileInfoList != null) {
            i = 0;
            while (i < this.mFileInfoList.size()) {
                if (this.mFileInfoList.get(i).getName().equals(fileInfo.getName())) {
                    break;
                }
                i++;
            }
        }
        i = -1;
        Dbug.i(this.tag, "find fileinof index=" + i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shootSound() {
        AudioManager audioManager = (AudioManager) getSystemService("audio");
        int streamVolume = audioManager != null ? audioManager.getStreamVolume(4) : 0;
        Dbug.i(this.tag, "volume=:" + streamVolume);
        if (streamVolume != 0) {
            MediaPlayer create = MediaPlayer.create(this, R.raw.camera_click);
            create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.35
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
            });
            create.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerProjectionUI() {
        ImageView imageView;
        if (this.playbackMode == 257 && (imageView = this.mProjectionFlag) != null) {
            if (this.isProjection) {
                this.mProjectionBtn.setImageResource(R.mipmap.ic_projection_selected);
                this.mProjectionFlag.setVisibility(0);
            } else {
                imageView.setVisibility(8);
                this.mProjectionBtn.setImageResource(R.drawable.drawable_projection);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerRTVoiceUI() {
        if (this.playbackMode != 257) {
            return;
        }
        if (this.isRtVoiceOpen) {
            this.mVoiceBtn.setImageResource(R.mipmap.ic_hor_voice_selected);
        } else {
            this.mVoiceBtn.setImageResource(R.drawable.drawable_hor_voice);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerVideoUI() {
        if (this.playbackMode != 257) {
            return;
        }
        if (this.recordStatus == 1) {
            showVideoUI();
        } else {
            hideVideoUI();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showVideoUI() {
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null) {
            wakeLock.acquire(216000L);
        }
        this.mRecordFlag.setVisibility(0);
        this.mVideoBtn.setImageResource(R.mipmap.ic_record_selected);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500L);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(2);
        this.mRecordFlag.startAnimation(alphaAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideVideoUI() {
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        this.mRecordFlag.clearAnimation();
        this.mRecordFlag.setVisibility(8);
        this.mVideoBtn.setImageResource(R.drawable.drawable_record_control);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerControlBarUI() {
        LinearLayout linearLayout = this.centerControlBar;
        boolean z = linearLayout == null || linearLayout.getVisibility() != 0;
        boolean isSdcardExist = this.mApplication.isSdcardExist();
        if (z) {
            RelativeLayout relativeLayout = this.topBar;
            if (relativeLayout != null && relativeLayout.getVisibility() != 0) {
                this.topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_top));
                this.topBar.setVisibility(0);
            }
            LinearLayout linearLayout2 = this.leftControlBar;
            if (linearLayout2 != null && linearLayout2.getVisibility() != 0) {
                this.leftControlBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_enter));
                this.leftControlBar.setVisibility(0);
            }
            LinearLayout linearLayout3 = this.centerControlBar;
            if (linearLayout3 != null && linearLayout3.getVisibility() != 0) {
                this.centerControlBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_enter));
                this.centerControlBar.setVisibility(0);
            }
            PlaybackSeekbar playbackSeekbar = this.playbackSeekbar;
            if (playbackSeekbar == null || playbackSeekbar.getVisibility() == 0 || !isSdcardExist) {
                return;
            }
            this.playbackSeekbar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
            this.playbackSeekbar.setVisibility(0);
            return;
        }
        if (this.isProjection || this.playbackMode != 257) {
            return;
        }
        RelativeLayout relativeLayout2 = this.topBar;
        if (relativeLayout2 != null && relativeLayout2.getVisibility() != 4) {
            this.topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
            this.topBar.setVisibility(4);
        }
        LinearLayout linearLayout4 = this.leftControlBar;
        if (linearLayout4 != null && linearLayout4.getVisibility() != 4) {
            this.leftControlBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_exit));
            this.leftControlBar.setVisibility(4);
        }
        LinearLayout linearLayout5 = this.centerControlBar;
        if (linearLayout5 != null && linearLayout5.getVisibility() != 4) {
            this.centerControlBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_exit));
            this.centerControlBar.setVisibility(4);
        }
        PlaybackSeekbar playbackSeekbar2 = this.playbackSeekbar;
        if (playbackSeekbar2 == null || playbackSeekbar2.getVisibility() == 4) {
            return;
        }
        this.playbackSeekbar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom));
        this.playbackSeekbar.setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateModeUI(int i) {
        if (this.playbackMode != i) {
            this.playbackMode = i;
        }
        updateTopTv();
        if (i != 257) {
            if (i != MODE_BROWSE) {
                return;
            }
            Dbug.i(this.tag, "updateModeUI");
            closeRTS();
            IjkVideoView ijkVideoView = this.mStreamView;
            if (ijkVideoView != null && ijkVideoView.getVisibility() != 4) {
                this.mStreamView.setVisibility(4);
            }
            RelativeLayout relativeLayout = this.layoutPreview;
            if (relativeLayout != null && relativeLayout.getVisibility() != 8) {
                this.layoutPreview.setVisibility(8);
            }
            RelativeLayout relativeLayout2 = this.layoutBrowse;
            if (relativeLayout2 == null || relativeLayout2.getVisibility() == 0) {
                return;
            }
            this.layoutBrowse.setVisibility(0);
            return;
        }
        RelativeLayout relativeLayout3 = this.layoutBrowse;
        if (relativeLayout3 != null && relativeLayout3.getVisibility() != 8) {
            this.layoutBrowse.setVisibility(8);
        }
        IjkVideoView ijkVideoView2 = this.mStreamView;
        if (ijkVideoView2 != null && ijkVideoView2.getVisibility() != 0) {
            this.mStreamView.setVisibility(0);
        }
        RelativeLayout relativeLayout4 = this.layoutPreview;
        if (relativeLayout4 != null && relativeLayout4.getVisibility() != 0) {
            this.layoutPreview.setVisibility(0);
        }
        syncDeviceState();
        if (isPlaying()) {
            return;
        }
        if (this.isProjection) {
            handlerControlBarUI();
        } else {
            openRTS();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerFileLockState(FileInfo fileInfo) {
        int i;
        if (fileInfo == null || this.mLockFileBtn == null) {
            return;
        }
        if (fileInfo.getType() == 2) {
            i = R.drawable.drawable_unlock;
        } else {
            i = R.drawable.drawable_lock;
        }
        this.mLockFileBtn.setImageResource(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncDeviceState() {
        DeviceSettingInfo deviceSettingInfo;
        if (this.playbackMode == 257 && (deviceSettingInfo = this.mApplication.getDeviceSettingInfo()) != null) {
            if (this.recordStatus != deviceSettingInfo.getRecordState()) {
                this.recordStatus = deviceSettingInfo.getRecordState();
                handlerVideoUI();
            }
            if (this.isProjection != deviceSettingInfo.isOpenProjection()) {
                this.isProjection = deviceSettingInfo.isOpenProjection();
                handlerProjectionUI();
            }
            if (this.isRtVoiceOpen != deviceSettingInfo.isRTVoice()) {
                this.isRtVoiceOpen = deviceSettingInfo.isRTVoice();
                handlerRTVoiceUI();
            }
            updateResolutionIcon();
            if (deviceSettingInfo.isExistRearView()) {
                this.mSwitchCameraBtn.setVisibility(0);
            } else {
                this.mSwitchCameraBtn.setVisibility(8);
            }
        }
    }

    private void updateTopTv() {
        String string;
        if (this.playbackMode == 257) {
            string = getString(R.string.live_preview);
        } else {
            string = getString(R.string.playback);
        }
        String str = "(" + getString(R.string.front_view) + ")";
        if (this.mCameraType == 2) {
            str = "(" + getString(R.string.rear_view) + ")";
        }
        this.tvTop.setText(string + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCameraType() {
        int cameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        if (this.mCameraType != cameraType) {
            this.mCameraType = cameraType;
            updateTopTv();
            handleTFOffline();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.36
                    @Override // java.lang.Runnable
                    public void run() {
                        PlaybackActivity.this.updateResolutionIcon();
                        PlaybackActivity.this.mPositionTime.setVisibility(0);
                        PlaybackActivity.this.tvAutoPlayTip.setVisibility(0);
                        PlaybackActivity.this.requestFileMsgText();
                    }
                }, 200L);
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.audio.AudioRecordManager.RecorderListener
    public void onRecord(byte[] bArr, int i) {
        IntercomManager intercomManager;
        if (bArr == null || (intercomManager = this.intercomManager) == null) {
            return;
        }
        intercomManager.send(bArr);
    }

    @Override // com.jieli.lib.dv.control.projection.OnSendStateListener
    public void onState(int i, String str) {
        IntercomManager intercomManager = this.intercomManager;
        if (intercomManager != null) {
            intercomManager.stopSendDataThread();
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.43
                @Override // java.lang.Runnable
                public void run() {
                    PlaybackActivity.this.isRtVoiceOpen = false;
                    PlaybackActivity.this.handlerRTVoiceUI();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLevelResId(int i) {
        if (i == 0) {
            return R.drawable.drawable_resolution_sd;
        }
        if (i == 2) {
            return R.drawable.drawable_resolution_fhd;
        }
        return R.drawable.drawable_resolution_hd;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCameraLevel(int i) {
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (deviceSettingInfo == null) {
            return 1;
        }
        if (i == 2) {
            return deviceSettingInfo.getRearLevel();
        }
        return deviceSettingInfo.getFrontLevel();
    }

    private int getVideoRate(int i) {
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (deviceSettingInfo == null) {
            return 30;
        }
        if (i == 2) {
            return deviceSettingInfo.getRearRate();
        }
        return deviceSettingInfo.getFrontRate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateResolutionUI(boolean z, int i, int i2) {
        if (this.isAdjustResolution) {
            Dbug.w(this.tag, "adjust resolution step 006. isRear " + z + ", w " + i + ", h " + i2);
            int adjustRtsResolution = AppUtils.adjustRtsResolution(i, i2);
            if (adjustRtsResolution != getCameraLevel(this.mApplication.getDeviceSettingInfo().getCameraType())) {
                if (z) {
                    this.mApplication.getDeviceSettingInfo().setRearLevel(adjustRtsResolution);
                } else {
                    this.mApplication.getDeviceSettingInfo().setFrontLevel(adjustRtsResolution);
                }
            }
            this.isAdjustResolution = false;
            dismissAdjustingDialog();
        }
        updateResolutionIcon();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateResolutionIcon() {
        int streamResolutionLevel;
        String[] streamResolutions;
        if (this.isRtspEnable) {
            streamResolutionLevel = AppUtils.getRtspResolutionLevel();
            streamResolutions = AppUtils.getRtspResolutions();
        } else {
            streamResolutionLevel = AppUtils.getStreamResolutionLevel();
            streamResolutions = AppUtils.getStreamResolutions();
        }
        if (streamResolutions == null || streamResolutions.length <= 0) {
            Dbug.w(this.tag, "resolutions is null");
            this.mAdjustResolutionBtn.setVisibility(8);
            return;
        }
        this.mAdjustResolutionBtn.setVisibility(0);
        this.mAdjustResolutionBtn.setImageResource(getLevelResId(streamResolutionLevel));
        Dbug.i(this.tag, "updateResolutionIcon currentLevel=" + streamResolutionLevel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createStream(int i, int i2) {
        Dbug.i(this.tag, "createStream==========mode=" + i);
        if (i == 0 || i == 1) {
            if (this.mRealtimeStream == null) {
                RealtimeStream realtimeStream = new RealtimeStream(i);
                this.mRealtimeStream = realtimeStream;
                realtimeStream.registerStreamListener(this.realtimePlayerListener);
            }
            Dbug.i(this.tag, "Net mode=" + i + ", is receiving " + this.mRealtimeStream.isReceiving());
            if (!this.mRealtimeStream.isReceiving()) {
                if (i == 0) {
                    this.mRealtimeStream.create(i2, ClientManager.getClient().getAddress());
                } else {
                    this.mRealtimeStream.create(i2);
                }
                this.mRealtimeStream.setSoTimeout(5000);
                this.mRealtimeStream.useDeviceTimestamp(true);
                this.mRealtimeStream.configure(IConstant.RTP_VIDEO_PORT1, IConstant.RTP_AUDIO_PORT1);
                return;
            }
            Dbug.w(this.tag, "stream not receiving");
            return;
        }
        Dbug.e(this.tag, "Create stream failed!!!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLocalRecordDialog() {
        if (this.mLocalRecordingDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.no_card_record_tip, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.44
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    PlaybackActivity.this.mLocalRecordingDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.45
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    PlaybackActivity.this.mLocalRecordingDialog.dismiss();
                    PlaybackActivity.this.startLocalRecording();
                }
            });
            this.mLocalRecordingDialog = newInstance;
            newInstance.setCancelable(false);
        }
        if (this.mLocalRecordingDialog.isShowing()) {
            return;
        }
        this.mLocalRecordingDialog.show(getSupportFragmentManager(), "No_Card_Record");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLocalRecording() {
        if (this.mRecordVideo == null) {
            this.mRecordVideo = new VideoRecord();
        }
        this.mRecordVideo.prepare(true, new OnRecordStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.46
            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onCompletion(String str) {
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onPrepared() {
                PlaybackActivity.this.isRecordPrepared = true;
                PlaybackActivity.this.showVideoUI();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onStop() {
                PlaybackActivity.this.stopLocalRecording();
                PlaybackActivity.this.hideVideoUI();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onError(String str) {
                Dbug.e(PlaybackActivity.this.tag, "Record error:" + str);
                PlaybackActivity.this.stopLocalRecording();
                PlaybackActivity.this.hideVideoUI();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLocalRecording() {
        VideoRecord videoRecord = this.mRecordVideo;
        if (videoRecord != null) {
            videoRecord.close();
            this.mRecordVideo = null;
        }
        this.isRecordPrepared = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        if (this.isRtspEnable) {
            IjkVideoView ijkVideoView = this.mStreamView;
            return ijkVideoView != null && ijkVideoView.isPlaying();
        }
        RealtimeStream realtimeStream = this.mRealtimeStream;
        return realtimeStream != null && realtimeStream.isReceiving();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeleteFileDialog() {
        if (this.mDeleteFileDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.sure_to_delete, true, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.47
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    PlaybackActivity.this.mDeleteFileDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.48
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    PlaybackActivity.this.mDeleteFileDialog.dismiss();
                    if (PlaybackActivity.this.isChecked) {
                        PlaybackActivity.this.deleteAllFiles();
                    } else {
                        PlaybackActivity.this.deleteFile(PlaybackActivity.this.mAdapter.getItem(PlaybackActivity.this.mCoverFlowCarousel.getSelectedPos()));
                    }
                }
            });
            this.mDeleteFileDialog = newInstance;
            newInstance.setOnCheckedChangeListener(this.onCheckedChangeListener);
        }
        if (this.mDeleteFileDialog.isShowing()) {
            return;
        }
        this.mDeleteFileDialog.show(getSupportFragmentManager(), "Delete_File_Dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgressDialog(FileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }
        DownloadDialog.newInstance(fileInfo).show(getSupportFragmentManager(), "DownloadDialog");
    }

    private void showStopRecordingDialog(final OnClickStateListener onClickStateListener) {
        NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.recording_will_stop, R.string.dialog_no, R.string.dialog_yes, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.50
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
            public void onClick() {
                PlaybackActivity.this.mStopLocalRecordingDialog.dismiss();
                PlaybackActivity.this.mStopLocalRecordingDialog = null;
                OnClickStateListener onClickStateListener2 = onClickStateListener;
                if (onClickStateListener2 != null) {
                    onClickStateListener2.onCancel();
                }
            }
        }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.51
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
            public void onClick() {
                PlaybackActivity.this.mStopLocalRecordingDialog.dismiss();
                PlaybackActivity.this.mStopLocalRecordingDialog = null;
                OnClickStateListener onClickStateListener2 = onClickStateListener;
                if (onClickStateListener2 != null) {
                    onClickStateListener2.onConfirm();
                }
            }
        });
        this.mStopLocalRecordingDialog = newInstance;
        newInstance.setCancelable(false);
        if (this.mStopLocalRecordingDialog.isShowing()) {
            return;
        }
        this.mStopLocalRecordingDialog.show(getSupportFragmentManager(), "Stop_local_recording");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToStopRecording(int i) {
        final ThumbnailInfo item = this.mAdapter.getItem(i);
        showStopRecordingDialog(new OnClickStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.52
            @Override // com.jieli.stream.dv.running2.interfaces.OnClickStateListener
            public void onCancel() {
                PlaybackActivity.this.handleAutoPlay();
            }

            @Override // com.jieli.stream.dv.running2.interfaces.OnClickStateListener
            public void onConfirm() {
                if (item == null) {
                    Dbug.e(PlaybackActivity.this.tag, "Select fileInfo is null");
                } else {
                    ClientManager.getClient().tryToRecordVideo(false, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.PlaybackActivity.52.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                Dbug.e(PlaybackActivity.this.tag, "Send failed");
                            } else {
                                ToastUtil.showToastShort(item.getName());
                                PlaybackActivity.this.showPlaybackDialog(item.getPath(), item.getStartTime().getTimeInMillis());
                            }
                        }
                    });
                }
            }
        });
    }

    private void startDebug() {
        this.fps = 0;
        this.isStartDebug = true;
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
            this.isStartDebug = false;
            this.fps = 0;
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
        IjkVideoView ijkVideoView = this.mStreamView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.drop_packet_count, i + "");
        hudView.setRowValue(R.string.drop_packet_sum, i2 + "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDebugFps(int i) {
        InfoHudViewHolder hudView;
        IjkVideoView ijkVideoView = this.mStreamView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.fps, i + "");
    }
}
