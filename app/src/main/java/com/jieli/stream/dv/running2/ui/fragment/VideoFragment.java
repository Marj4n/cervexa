package com.jieli.stream.dv.running2.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.mapapi.UIMsg;
import com.generalplus.GoPlusDrone.Activity.ResolutionAdapter;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.intercom.IntercomManager;
import com.jieli.lib.dv.control.model.PictureInfo;
import com.jieli.lib.dv.control.player.OnFrameListener;
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
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.camera.CameraPresenter;
import com.jieli.stream.dv.running2.data.OnRecordStateListener;
import com.jieli.stream.dv.running2.data.OnVideoCaptureListener;
import com.jieli.stream.dv.running2.data.VideoCapture;
import com.jieli.stream.dv.running2.data.VideoRecord;
import com.jieli.stream.dv.running2.interfaces.OnClickStateListener;
import com.jieli.stream.dv.running2.task.DebugHelper;
import com.jieli.stream.dv.running2.task.IDebugListener;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.activity.GenericActivity;
import com.jieli.stream.dv.running2.ui.activity.PlaybackActivity;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.ui.widget.PopupMenu;
import com.jieli.stream.dv.running2.ui.widget.media.IjkVideoView;
import com.jieli.stream.dv.running2.ui.widget.media.InfoHudViewHolder;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;
import com.jieli.stream.dv.running2.util.WifiP2pHelper;
import com.jieli.stream.dv.running2.util.json.JSonManager;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;
import com.serenegiant.usb.UVCCamera;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class VideoFragment extends BaseFragment implements View.OnClickListener, OnFrameListener, AdapterView.OnItemClickListener, AudioRecordManager.RecorderListener, OnSendStateListener {
    private static final int DELAY_TIME = 100;
    private static final int MSG_CAMERA_SWITCH = 2568;
    private static final int MSG_CYC_SAVE_VIDEO = 2565;
    private static final int MSG_FPS_COUNT = 2566;
    private static final int MSG_LOAD_DEV_THUMBS = 2562;
    private static final int MSG_PROJECTION_CONTROL = 2563;
    private static final int MSG_REQUEST_THUMB = 2567;
    private static final int MSG_RT_VOICE_CONTROL = 2564;
    private static final int MSG_TAKE_PHOTO = 2561;
    private static final int MSG_TAKE_VIDEO = 2560;
    private Set<SaveVideoThumb> collections;
    private List<FileInfo> countList;
    private int fps;
    private IntercomManager intercomManager;
    private boolean isAdjustResolution;
    private boolean isProjection;
    private boolean isRtVoiceOpen;
    private boolean isRtspEnable;
    private boolean isStartDebug;
    private boolean isSwitchCamera;
    private TextView ivBroken;
    private ImageView iv_focus_img;
    private ImageView iv_picture;
    private ImageView iv_record;
    private ImageView iv_resolution;
    private ImageView iv_zoom_focus;
    private ImageView iv_zoom_in;
    private ImageView iv_zoom_out;
    private FrameLayout ll_content;
    private LinearLayout ll_resolution;
    private AudioRecordManager mAudioManager;
    private DebugHelper mDebugHelper;
    private NotifyDialog mLocalRecordingDialog;
    private CameraPresenter mPresenter;
    private RealtimeStream mRealtimeStream;
    private MyBroadcastReceiver mReceiver;
    private VideoRecord mRecordVideo;
    private NotifyDialog mStopLocalRecordingDialog;
    private VideoCapture mVideoCapture;
    private VideoThumbnail mVideoThumbnail;
    private IjkVideoView mVideoView;
    private View mView;
    private WaitingDialog mWaitingDialog;
    private RecyclerView rc_resolution;
    private int recordStatus;
    boolean save;
    private long startTime;
    private ImageView take_photo;
    private List<FileInfo> thumbList;
    private List<FileInfo> totalList;
    private TextView tv_record_time;
    private TextView tv_resolution;
    private int viewHeight;
    private int viewWidth;
    private PowerManager.WakeLock wakeLock;
    private String tag = getClass().getSimpleName();
    private FrameCodec mFrameCodec = null;
    private int mCameraType = 1;
    private boolean isIJKPlayerOpen = false;
    private boolean isFragmentStop = false;
    public boolean isRecordPrepared = false;
    private boolean isCapturePrepared = false;
    private boolean isSentOpenRtsCmd = false;
    private int mWidth = -1;
    private int mHeight = -1;
    private float mScale = 1.0f;
    int selectedWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
    int selectedHeight = 480;
    private Handler mHandler = new Handler(new AnonymousClass1());
    private final IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.15
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
        public void onPrepared(IMediaPlayer iMediaPlayer) {
        }
    };
    private final IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.16
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
        public void onCompletion(IMediaPlayer iMediaPlayer) {
        }
    };
    private final OnNotifyListener onNotifyResponse = new AnonymousClass17();
    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.18
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
            Dbug.e(VideoFragment.this.tag, "Error: framework_err=" + i + ",impl_err=" + i2);
            VideoFragment.this.closeRTS();
            if (i == -10000) {
                return true;
            }
            VideoFragment.this.getActivity().onBackPressed();
            return true;
        }
    };
    protected Runnable openRts = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.19
        @Override // java.lang.Runnable
        public void run() {
            VideoFragment.this.openRTS();
            VideoFragment.this.mHandler.postDelayed(this, 200L);
        }
    };
    private Runnable dismissDialogRunnable = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.20
        @Override // java.lang.Runnable
        public void run() {
            if (VideoFragment.this.isRtspEnable) {
                VideoFragment.this.updateDeviceMsg();
                VideoFragment.this.updateResolutionIcon();
                if (VideoFragment.this.mHandler != null) {
                    VideoFragment.this.mHandler.sendMessage(VideoFragment.this.mHandler.obtainMessage(VideoFragment.MSG_LOAD_DEV_THUMBS, 1, 0));
                }
            }
            VideoFragment.this.dismissWaitingDialog();
            Dbug.e(VideoFragment.this.tag, "dismissDialogRunnable isSwitchCamera=" + VideoFragment.this.isSwitchCamera);
        }
    };
    private Runnable updateUIFromDev = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.22
        @Override // java.lang.Runnable
        public void run() {
            VideoFragment.this.updateDeviceMsg();
            VideoFragment.this.syncDeviceState();
            VideoFragment.this.isPlaying();
        }
    };
    private final FrameCodec.OnFrameCodecListener mOnFrameCodecListener = new FrameCodec.OnFrameCodecListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.24
        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onCompleted(byte[] bArr, MediaMeta mediaMeta) {
            if (bArr != null) {
                Dbug.w(VideoFragment.this.tag, "-onCompleted- bytes size=" + bArr.length + ",mediaMeta=" + mediaMeta);
                VideoFragment videoFragment = VideoFragment.this;
                SaveVideoThumb saveVideoThumb = videoFragment.new SaveVideoThumb(bArr, videoFragment.mHandler);
                VideoFragment.this.collections.add(saveVideoThumb);
                saveVideoThumb.start();
            }
        }

        @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
        public void onError(String str) {
            if (VideoFragment.this.mVideoThumbnail != null) {
                Dbug.i(VideoFragment.this.tag, "mVideoThumbnail close - 003");
                VideoFragment.this.mVideoThumbnail.close();
            }
            if (VideoFragment.this.thumbList == null || VideoFragment.this.thumbList.size() <= 0) {
                return;
            }
            VideoFragment.this.thumbList.remove(0);
        }
    };
    protected Handler handler = new Handler();
    protected Runnable updateTimer = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.27
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override // java.lang.Runnable
        public void run() {
            Long valueOf = Long.valueOf(System.currentTimeMillis() - VideoFragment.this.startTime);
            Long valueOf2 = Long.valueOf(((valueOf.longValue() / 1000) / 60) / 60);
            Long valueOf3 = Long.valueOf(((valueOf.longValue() / 1000) / 60) % 60);
            Long valueOf4 = Long.valueOf((valueOf.longValue() / 1000) % 60);
            VideoFragment.this.tv_record_time.setText(
                    String.format("%02d", valueOf2) + ":" +
                            String.format("%02d", valueOf3) + ":" +
                            String.format("%02d", valueOf4)
            );
            VideoFragment.this.handler.postDelayed(this, 1000L);
        }
    };
    private PopupMenu.OnPopItemClickListener mOnPopItemClickListener = new PopupMenu.OnPopItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.28
        @Override // com.jieli.stream.dv.running2.ui.widget.PopupMenu.OnPopItemClickListener
        public void onItemClick(int i, Integer num, int i2) {
            VideoFragment.this.switchStreamResolution(i);
        }
    };
    VideoCapture tempCapture = new VideoCapture();
    byte[] firstData = null;
    private OnRealTimeListener realtimePlayerListener = new OnRealTimeListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.30
        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onVideo(int i, int i2, byte[] bArr, long j, long j2) {
            if (VideoFragment.this.isStartDebug && PreferencesHelper.getSharedPreferences(VideoFragment.this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
                VideoFragment.access$1308(VideoFragment.this);
            }
            if (VideoFragment.this.mRecordVideo != null && VideoFragment.this.isRecordPrepared) {
                if (!VideoFragment.this.mRecordVideo.write(i, bArr)) {
                    Dbug.e(VideoFragment.this.tag, "Write video failed");
                }
                if (VideoFragment.this.save) {
                    VideoFragment.this.save = false;
                    VideoFragment.this.firstData = bArr;
                }
            }
            if (VideoFragment.this.mVideoCapture == null || !VideoFragment.this.isCapturePrepared) {
                return;
            }
            VideoFragment.this.mVideoCapture.capture(bArr);
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onAudio(int i, int i2, byte[] bArr, long j, long j2) {
            if (VideoFragment.this.mRecordVideo == null || !VideoFragment.this.isRecordPrepared || VideoFragment.this.mRecordVideo.write(i, bArr)) {
                return;
            }
            Dbug.e(VideoFragment.this.tag, "Write audio failed");
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onStateChanged(int i) {
            Dbug.e(VideoFragment.this.tag, "onStateChanged:state=" + i);
            if (i != 1 && i != 2) {
                if (i == 5) {
                    VideoFragment.this.stopLocalRecording();
                }
            } else {
                if (!VideoFragment.this.isSwitchCamera || VideoFragment.this.mHandler == null) {
                    return;
                }
                VideoFragment.this.mHandler.removeCallbacks(VideoFragment.this.dismissDialogRunnable);
                VideoFragment.this.mHandler.post(VideoFragment.this.dismissDialogRunnable);
            }
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onError(int i, String str) {
            Dbug.e(VideoFragment.this.tag, "code=" + i + ", message=" + str);
            if (VideoFragment.this.isSwitchCamera) {
                return;
            }
            VideoFragment.this.closeRTS();
        }
    };
    String aviJpegName = "";
    private IDebugListener mIDebugListener = new IDebugListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.37
        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onStartDebug(String str, int i, int i2) {
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onDebugResult(int i, int i2) {
            Dbug.w("zzc", "-onDebugResult- dropCount : " + i + ", dropSum : " + i2);
            VideoFragment.this.updateDebug(i, i2);
        }

        @Override // com.jieli.stream.dv.running2.task.IDebugListener
        public void onError(int i, String str) {
            Dlog.w(VideoFragment.this.tag, str);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerProjectionUI() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerRTVoiceUI() {
    }

    private boolean isSdOnline() {
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
    }

    static /* synthetic */ int access$1308(VideoFragment videoFragment) {
        int i = videoFragment.fps;
        videoFragment.fps = i + 1;
        return i;
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.VideoFragment$1, reason: invalid class name */
    class AnonymousClass1 implements Handler.Callback {
        AnonymousClass1() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (VideoFragment.this.getActivity() != null && message != null) {
                switch (message.what) {
                    case VideoFragment.MSG_TAKE_VIDEO /* 2560 */:
                        if (VideoFragment.this.isPlaying()) {
                            if (VideoFragment.this.isRtspEnable) {
                                ToastUtil.showToastLong(VideoFragment.this.getString(R.string.not_supported_in_rtsp_mode));
                                break;
                            } else if (!AppUtils.isFastDoubleClick(UIMsg.m_AppUI.MSG_APP_DATA_OK)) {
                                Dbug.e(VideoFragment.this.tag, "---is playing " + VideoFragment.this.isPlaying());
                                if (!VideoFragment.this.isRecordPrepared) {
                                    VideoFragment.this.startLocalRecording();
                                    break;
                                } else {
                                    VideoFragment.this.stopLocalRecording();
                                    break;
                                }
                            } else {
                                ToastUtil.showToastShort(VideoFragment.this.getString(R.string.dialod_wait));
                                break;
                            }
                        } else {
                            ToastUtil.showToastShort(VideoFragment.this.getString(R.string.open_rts_tip));
                            break;
                        }
                    case VideoFragment.MSG_TAKE_PHOTO /* 2561 */:
                        if (VideoFragment.this.isPlaying()) {
                            if (!VideoFragment.this.isRtspEnable) {
                                if (VideoFragment.this.mVideoCapture == null) {
                                    VideoFragment.this.mVideoCapture = new VideoCapture();
                                    VideoFragment.this.mVideoCapture.setOnCaptureListener(new OnVideoCaptureListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.1
                                        @Override // com.jieli.stream.dv.running2.data.OnVideoCaptureListener
                                        public void onCompleted(final String str) {
                                            VideoFragment.this.isCapturePrepared = false;
                                            VideoFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    ToastUtil.showToastShort(VideoFragment.this.getResources().getString(R.string.photos_success));
                                                    if (TextUtils.isEmpty(str)) {
                                                        return;
                                                    }
                                                    VideoFragment.this.getActivity().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str)));
                                                }
                                            });
                                        }

                                        @Override // com.jieli.stream.dv.running2.data.OnVideoCaptureListener
                                        public void onFailed() {
                                            VideoFragment.this.isCapturePrepared = false;
                                            VideoFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.1.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    ToastUtil.showToastShort(VideoFragment.this.getResources().getString(R.string.pictures_failed));
                                                }
                                            });
                                        }
                                    });
                                }
                                VideoFragment.this.mVideoCapture.setHeight(VideoFragment.this.selectedHeight);
                                VideoFragment.this.mVideoCapture.setWidth(VideoFragment.this.selectedWidth);
                                VideoFragment.this.isCapturePrepared = true;
                                break;
                            } else {
                                ToastUtil.showToastLong(VideoFragment.this.getString(R.string.not_supported_in_rtsp_mode));
                                break;
                            }
                        } else if (VideoFragment.this.isAdjustResolution || VideoFragment.this.isSwitchCamera) {
                            Dbug.i(VideoFragment.this.tag, "photo=isAdjustResolution==");
                            break;
                        } else {
                            ToastUtil.showToastShort(VideoFragment.this.getString(R.string.open_rts_tip));
                            break;
                        }
                        break;
                    case VideoFragment.MSG_LOAD_DEV_THUMBS /* 2562 */:
                        if (message.arg1 == 1) {
                            VideoFragment.this.cancelSaveThread();
                        }
                        VideoFragment.this.requestFileMsgText();
                        break;
                    case VideoFragment.MSG_PROJECTION_CONTROL /* 2563 */:
                        Dbug.i(VideoFragment.this.tag, "MSG_PROJECTION_CONTROL:  isProjection=" + VideoFragment.this.isProjection);
                        ClientManager.getClient().tryToStreamingPush(VideoFragment.this.isProjection ^ true, 480, IConstant.PROJECTION_HEIGHT, 30, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.2
                            @Override // com.jieli.lib.dv.control.connect.response.Response
                            public void onResponse(Integer num) {
                                if (num.intValue() != 1) {
                                    Dbug.e(VideoFragment.this.tag, "send failed!!!");
                                }
                            }
                        });
                        break;
                    case VideoFragment.MSG_RT_VOICE_CONTROL /* 2564 */:
                        if (!VideoFragment.this.isRtspEnable) {
                            ClientManager.getClient().tryToRTIntercom(!VideoFragment.this.isRtVoiceOpen, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.3
                                @Override // com.jieli.lib.dv.control.connect.response.Response
                                public void onResponse(Integer num) {
                                }
                            });
                            break;
                        } else {
                            ToastUtil.showToastLong(VideoFragment.this.getString(R.string.not_supported_in_rtsp_mode));
                            break;
                        }
                    case VideoFragment.MSG_CYC_SAVE_VIDEO /* 2565 */:
                        if (VideoFragment.this.recordStatus == 1) {
                            ClientManager.getClient().tryToSaveCycVideo(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.1.4
                                @Override // com.jieli.lib.dv.control.connect.response.Response
                                public void onResponse(Integer num) {
                                }
                            });
                            break;
                        } else {
                            ToastUtil.showToastShort(VideoFragment.this.getString(R.string.no_video_tip));
                            break;
                        }
                    case VideoFragment.MSG_FPS_COUNT /* 2566 */:
                        VideoFragment videoFragment = VideoFragment.this;
                        videoFragment.updateDebugFps(videoFragment.fps);
                        VideoFragment.this.fps = 0;
                        VideoFragment.this.mHandler.removeMessages(VideoFragment.MSG_FPS_COUNT);
                        VideoFragment.this.mHandler.sendEmptyMessageDelayed(VideoFragment.MSG_FPS_COUNT, 1000L);
                        break;
                    case VideoFragment.MSG_REQUEST_THUMB /* 2567 */:
                        Dbug.i(VideoFragment.this.tag, "!!! MSG_REQUEST_THUMB !!!");
                        VideoFragment.this.dismissWaitingDialog();
                        VideoFragment.this.cancelSaveThread();
                        VideoFragment.this.requestFileMsgText();
                        break;
                    case VideoFragment.MSG_CAMERA_SWITCH /* 2568 */:
                        if (!VideoFragment.this.isSwitchCamera) {
                            VideoFragment.this.isSwitchCamera = true;
                            VideoFragment.this.showWaitingDialog();
                            VideoFragment.this.closeRTS();
                            VideoFragment.this.mApplication.getDeviceSettingInfo().setCameraType(VideoFragment.this.mApplication.getDeviceSettingInfo().getCameraType() == 1 ? 2 : 1);
                            if (VideoFragment.this.isRtspEnable) {
                                VideoFragment.this.openRTS();
                                VideoFragment.this.mHandler.postDelayed(VideoFragment.this.dismissDialogRunnable, 500L);
                                break;
                            } else {
                                VideoFragment.this.mHandler.postDelayed(VideoFragment.this.dismissDialogRunnable, 15000L);
                                break;
                            }
                        } else {
                            Dbug.i(VideoFragment.this.tag, "MSG_CAMERA_SWITCH: isSwitchCamera is true");
                            break;
                        }
                }
            }
            return false;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        /* synthetic */ MyBroadcastReceiver(VideoFragment videoFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (context == null || TextUtils.isEmpty(action) || VideoFragment.this.getActivity() == null) {
                return;
            }
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != 1702481103) {
                if (hashCode == 1935762049 && action.equals(IActions.ACTION_FORMAT_TF_CARD)) {
                    c = 0;
                }
            } else if (action.equals(IActions.ACTION_LANGUAAGE_CHANGE)) {
                c = 1;
            }
            if (c == 0) {
                VideoFragment.this.clearDataAndUpdate();
            } else {
                if (c != 1) {
                    return;
                }
                VideoFragment.this.updateTextView();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTextView() {
        updateDeviceMsg();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendMessageDelayed(handler.obtainMessage(MSG_LOAD_DEV_THUMBS, 0, 0), 100L);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        this.mView = inflate;
        IjkVideoView ijkVideoView = (IjkVideoView) inflate.findViewById(R.id.video_view);
        this.mVideoView = ijkVideoView;
        ijkVideoView.setAspectRatio(3);
        this.mVideoView.setOnPreparedListener(this.onPreparedListener);
        this.mVideoView.setOnErrorListener(this.mOnErrorListener);
        this.mVideoView.setOnCompletionListener(this.onCompletionListener);
        this.ll_content = (FrameLayout) this.mView.findViewById(R.id.ll_content);
        this.ivBroken = (TextView) this.mView.findViewById(R.id.ivBroken);
        ImageView imageView = (ImageView) this.mView.findViewById(R.id.iv_zoom_in);
        this.iv_zoom_in = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoFragment videoFragment = VideoFragment.this;
                videoFragment.zoom(true, videoFragment.ll_content);
            }
        });
        ImageView imageView2 = (ImageView) this.mView.findViewById(R.id.iv_zoom_out);
        this.iv_zoom_out = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoFragment videoFragment = VideoFragment.this;
                videoFragment.zoom(false, videoFragment.ll_content);
            }
        });
        this.iv_focus_img = (ImageView) this.mView.findViewById(R.id.iv_focus_img);
        ImageView imageView3 = (ImageView) this.mView.findViewById(R.id.iv_zoom_focus);
        this.iv_zoom_focus = imageView3;
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (VideoFragment.this.iv_focus_img.getVisibility() == 0) {
                    VideoFragment.this.iv_focus_img.setVisibility(8);
                } else {
                    VideoFragment.this.iv_focus_img.setVisibility(0);
                }
            }
        });
        final boolean[] zArr = {false};
        this.iv_record = (ImageView) this.mView.findViewById(R.id.iv_record);
        this.tv_record_time = (TextView) this.mView.findViewById(R.id.tv_record_time);
        this.iv_record.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                boolean[] zArr2 = zArr;
                if (zArr2[0]) {
                    zArr2[0] = !zArr2[0];
                }
                if (zArr[0]) {
                    VideoFragment.this.iv_record.setImageResource(R.mipmap.icon_record_stop);
                } else {
                    VideoFragment.this.iv_record.setImageResource(R.mipmap.icon_record);
                }
                if (VideoFragment.this.mHandler != null) {
                    VideoFragment.this.mHandler.removeMessages(VideoFragment.MSG_TAKE_VIDEO);
                    VideoFragment.this.mHandler.sendEmptyMessageDelayed(VideoFragment.MSG_TAKE_VIDEO, 100L);
                }
            }
        });
        ImageView imageView4 = (ImageView) this.mView.findViewById(R.id.iv_picture);
        this.iv_picture = imageView4;
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.gizthon.preview.gallery.list");
                VideoFragment.this.startActivity(intent);
            }
        });
        ImageView imageView5 = (ImageView) this.mView.findViewById(R.id.iv_resolution);
        this.iv_resolution = imageView5;
        imageView5.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoFragment.this.ll_resolution.setVisibility(0);
            }
        });
        RecyclerView recyclerView = (RecyclerView) this.mView.findViewById(R.id.rc_resolution);
        this.rc_resolution = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ResolutionAdapter resolutionAdapter = new ResolutionAdapter(getActivity());
        ArrayList arrayList = new ArrayList();
        arrayList.add("640*480");
        arrayList.add("1280*720");
        arrayList.add("1920*1080");
        resolutionAdapter.setData(arrayList);
        resolutionAdapter.setOnClickItem(new ResolutionAdapter.OnClickItem() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.8
            @Override // com.generalplus.GoPlusDrone.Activity.ResolutionAdapter.OnClickItem
            public void onClick(String str) {
                String[] split = str.split("\\*");
                VideoFragment.this.selectedWidth = Integer.parseInt(split[0]);
                VideoFragment.this.selectedHeight = Integer.parseInt(split[1]);
                ToastUtil.showToastLong(VideoFragment.this.getResources().getString(R.string.haveto_switch) + str);
            }
        });
        this.rc_resolution.setAdapter(resolutionAdapter);
        LinearLayout linearLayout = (LinearLayout) this.mView.findViewById(R.id.ll_resolution);
        this.ll_resolution = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoFragment.this.ll_resolution.setVisibility(8);
            }
        });
        ((ImageView) this.mView.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoFragment.this.getActivity().finish();
            }
        });
        ImageView imageView6 = (ImageView) this.mView.findViewById(R.id.take_photo);
        this.take_photo = imageView6;
        imageView6.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (VideoFragment.this.mHandler != null) {
                    VideoFragment.this.mHandler.removeMessages(VideoFragment.MSG_TAKE_PHOTO);
                    VideoFragment.this.mHandler.sendEmptyMessageDelayed(VideoFragment.MSG_TAKE_PHOTO, 100L);
                }
            }
        });
        return this.mView;
    }

    public void zoom(boolean z, View view) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = this.mVideoView.getLayoutParams();
        if (this.mWidth < 0 || this.mHeight < 0) {
            Log.e("Scale", "-Down-,mWidthStart:" + this.mWidth + ",mHeightStart:" + this.mHeight);
            this.mWidth = width;
            this.mHeight = height;
        }
        Log.e("Scale", "-Down-,mWidthOld:" + this.mWidth + ",mHeightOld:" + this.mHeight + ",mScale:" + this.mScale);
        if (z) {
            float f = this.mScale - 0.25f;
            this.mScale = f;
            if (f > 4.0f) {
                this.mScale = 4.0f;
            } else if (f < 1.0f) {
                this.mScale = 1.0f;
            }
        } else {
            float f2 = this.mScale + 0.25f;
            this.mScale = f2;
            if (f2 > 4.0f) {
                this.mScale = 4.0f;
            } else if (f2 < 1.0f) {
                this.mScale = 1.0f;
            }
        }
        layoutParams.width = (int) (this.mWidth * this.mScale);
        layoutParams.height = (int) (this.mHeight * this.mScale);
        this.mVideoView.setPivotX(width / 2);
        this.mVideoView.setPivotY(height / 2);
        float f3 = this.mWidth;
        float f4 = this.mScale;
        this.mVideoView.setLayoutParams(new FrameLayout.LayoutParams((int) (f3 * f4), (int) (this.mHeight * f4), 17));
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        PowerManager powerManager = (PowerManager) getActivity().getSystemService("power");
        if (powerManager != null) {
            this.wakeLock = powerManager.newWakeLock(6, this.tag);
        }
        this.wakeLock.setReferenceCounted(false);
        this.collections = new HashSet();
        int screenWidth = (AppUtils.getScreenWidth(getContext()) - (AppUtils.dp2px(getContext(), 2) * 5)) / 4;
        this.viewWidth = screenWidth;
        this.viewHeight = (screenWidth * 9) / 16;
        this.isRtspEnable = PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.KEY_RTSP, false);
        ClientManager.getClient().tryToRequestRecordState(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.12
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(VideoFragment.this.tag, "Send failed!!!");
                }
            }
        });
        updateDeviceFileList();
    }

    private void initProjection() {
        handlerProjectionUI();
        MainApplication.getApplication().getDeviceDesc();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        this.isFragmentStop = false;
        registerBroadcast();
    }

    private void registerBroadcast() {
        if (this.mReceiver == null) {
            this.mReceiver = new MyBroadcastReceiver(this, null);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IActions.ACTION_FORMAT_TF_CARD);
        intentFilter.addAction(IActions.ACTION_LANGUAAGE_CHANGE);
        MainApplication.getApplication().registerReceiver(this.mReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRTS() {
        if (isPlaying()) {
            Dbug.e(this.tag, "It is playing, please stop it first.");
            return;
        }
        if (PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
            startDebug();
        }
        if (this.isRtspEnable) {
            initPlayer(AppUtils.getRtspUrl());
            this.isSwitchCamera = false;
            return;
        }
        int rtsFormat = AppUtils.getRtsFormat();
        final int cameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        int[] rtsResolution = AppUtils.getRtsResolution(AppUtils.getStreamResolutionLevel());
        String str = this.tag;
        StringBuilder sb = new StringBuilder();
        sb.append("open rts........... front=");
        sb.append(cameraType == 1);
        sb.append(", h264 ");
        sb.append(rtsFormat == 1);
        Dbug.i(str, sb.toString());
        ClientManager.getClient().tryToOpenRTStream(cameraType, rtsFormat, rtsResolution[0], rtsResolution[1], getVideoRate(cameraType), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.13
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(VideoFragment.this.tag, "Send failed!!!");
                    VideoFragment.this.ivBroken.setVisibility(0);
                    return;
                }
                VideoFragment.this.ivBroken.setVisibility(8);
                VideoFragment.this.isSentOpenRtsCmd = true;
                int netMode = VideoFragment.this.mApplication.getDeviceDesc().getNetMode();
                Dbug.i(VideoFragment.this.tag, "open rts mode " + netMode);
                Dbug.e(VideoFragment.this.tag, "open=" + cameraType);
                if (netMode == 0) {
                    VideoFragment.this.createStream(netMode, IConstant.VIDEO_SERVER_PORT);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeRTS() {
        Dbug.i(this.tag, "close rts................");
        deinitPlayer();
        if (PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false)) {
            closeDebug();
        }
        if (!this.isRtspEnable) {
            if (isPlaying() || this.isSwitchCamera) {
                final int cameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
                Dbug.i(this.tag, "cameraType = " + cameraType);
                ClientManager.getClient().tryToCloseRTStream(cameraType, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.14
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(VideoFragment.this.tag, "Send failed!!!");
                            return;
                        }
                        Dbug.e(VideoFragment.this.tag, "close=" + cameraType);
                    }
                });
            } else {
                Dbug.i(this.tag, "Not playing, isSwitchCamera=" + this.isSwitchCamera);
            }
        } else {
            Dbug.i(this.tag, "Rtsp Enable");
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        if (this.isRtspEnable) {
            IjkVideoView ijkVideoView = this.mVideoView;
            return ijkVideoView != null && ijkVideoView.isPlaying();
        }
        RealtimeStream realtimeStream = this.mRealtimeStream;
        return realtimeStream != null && realtimeStream.isReceiving();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayer(String str) {
        if (this.mVideoView != null && !TextUtils.isEmpty(str)) {
            Dbug.i(this.tag, "Init Player. url=" + str);
            Uri parse = Uri.parse(str);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.isIJKPlayerOpen = true;
            this.mVideoView.setRealtime(true);
            this.mVideoView.setVideoURI(parse);
            this.mVideoView.start();
            return;
        }
        Dbug.e(this.tag, "init player failed");
    }

    private void deinitPlayer() {
        Dbug.w(this.tag, "deinit Player");
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView != null) {
            ijkVideoView.stopPlayback();
            this.mVideoView.release(true);
            this.mVideoView.stopBackgroundPlay();
        }
        if (this.isIJKPlayerOpen) {
            IjkMediaPlayer.native_profileEnd();
        }
        this.isIJKPlayerOpen = false;
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.VideoFragment$17, reason: invalid class name */
    class AnonymousClass17 implements OnNotifyListener {
        AnonymousClass17() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:334:0x01b3, code lost:
        
            if (r2.equals(com.jieli.lib.dv.control.utils.Topic.CLOSE_RT_STREAM) != false) goto L116;
         */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo r18) {
            /*
                Method dump skipped, instructions count: 2644
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.AnonymousClass17.onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo):void");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        initProjection();
        if (JSonManager.getInstance().getInfoList().size() <= 0) {
            clearDataAndUpdate();
        }
        ClientManager.getClient().registerNotifyListener(this.onNotifyResponse);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.postDelayed(this.updateUIFromDev, 100L);
            this.mHandler.postDelayed(this.openRts, 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createStream(int i, int i2) {
        if (i == 0 || i == 1) {
            if (this.mRealtimeStream == null) {
                RealtimeStream realtimeStream = new RealtimeStream(i);
                this.mRealtimeStream = realtimeStream;
                realtimeStream.registerStreamListener(this.realtimePlayerListener);
            }
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

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        closeRTS();
        RealtimeStream realtimeStream = this.mRealtimeStream;
        if (realtimeStream != null) {
            realtimeStream.unregisterStreamListener(this.realtimePlayerListener);
            this.mRealtimeStream.release();
            this.mRealtimeStream = null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.isFragmentStop = true;
        if (this.mReceiver != null) {
            MainApplication.getApplication().unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
        cancelSaveThread();
        FrameCodec frameCodec = this.mFrameCodec;
        if (frameCodec != null) {
            frameCodec.destroy();
            this.mFrameCodec = null;
        }
        VideoThumbnail videoThumbnail = this.mVideoThumbnail;
        if (videoThumbnail != null && !videoThumbnail.close()) {
            Dbug.w(this.tag, "Close Video thumbnail failed");
        }
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyResponse);
        this.mHandler.removeMessages(MSG_LOAD_DEV_THUMBS);
        dismissWaitingDialog();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        AudioRecordManager audioRecordManager = this.mAudioManager;
        if (audioRecordManager != null) {
            audioRecordManager.release();
            this.mAudioManager = null;
        }
        IntercomManager intercomManager = this.intercomManager;
        if (intercomManager != null) {
            intercomManager.closeClient();
            this.intercomManager = null;
        }
        closeRTS();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        dismissWaitingDialog();
        System.gc();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 4133 && i == 4135) {
            this.mCameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestFileMsgText() {
        clearDataAndUpdate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToParseData(String str) {
        if (TextUtils.isEmpty(str)) {
            Dbug.e(this.tag, "tryToParseData: desc is empty!!");
        } else {
            JSonManager.getInstance().parseJSonData(str, new OnCompletedListener<Boolean>() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.21
                @Override // com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener
                public void onCompleted(Boolean bool) {
                    if (bool.booleanValue()) {
                        VideoFragment.this.totalList = JSonManager.getInstance().getInfoList();
                        VideoFragment.this.updateDeviceFileList();
                    } else {
                        Dbug.e(VideoFragment.this.TAG, "-tryToParseData- parseJSonData failed!!!");
                        VideoFragment.this.clearDataAndUpdate();
                        VideoFragment.this.dismissWaitingDialog();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceFileList() {
        List<FileInfo> list = this.totalList;
        if (list != null && list.size() > 0) {
            showWaitingDialog();
            ArrayList arrayList = new ArrayList();
            if (this.totalList.size() > 7) {
                arrayList.addAll(this.totalList.subList(0, 7));
            } else {
                arrayList.addAll(this.totalList);
            }
            requestVideoThumbnail(arrayList);
            List<FileInfo> list2 = this.countList;
            if (list2 == null) {
                this.countList = new ArrayList();
            } else {
                if (list2.size() > 0) {
                    Dbug.e(this.TAG, "updateDeviceFileList 222 ========================================================");
                }
                cancelSaveThread();
                this.countList.clear();
            }
            this.countList.addAll(arrayList);
            new ArrayList(arrayList);
            return;
        }
        Dbug.e(this.TAG, "updateDeviceFileList: total list is null");
        dismissWaitingDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceMsg() {
        String str;
        if (this.mApplication.getSearchMode() == 1) {
            str = ClientManager.getClient().getAddress();
        } else {
            WifiP2pDevice connectWifiDevice = WifiP2pHelper.getInstance(getContext()).getConnectWifiDevice();
            str = connectWifiDevice != null ? connectWifiDevice.deviceName : "";
            if (TextUtils.isEmpty(str) && this.mWifiHelper.getWifiConnectionInfo() != null) {
                str = WifiHelper.formatSSID(this.mWifiHelper.getWifiConnectionInfo().getSSID());
            }
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        str.replace(WIFI_PREFIX, "");
        String str2 = "(" + getString(R.string.front_view) + ")";
        if (this.mApplication.getDeviceSettingInfo().getCameraType() == 2) {
            String str3 = "(" + getString(R.string.rear_view) + ")";
        }
    }

    private void requestVideoThumbnail(List<FileInfo> list) {
        VideoThumbnail videoThumbnail = this.mVideoThumbnail;
        if ((videoThumbnail == null || !videoThumbnail.isReceiving()) && list != null) {
            ArrayList arrayList = new ArrayList();
            List<FileInfo> list2 = this.thumbList;
            if (list2 == null) {
                this.thumbList = new ArrayList();
            } else {
                list2.clear();
            }
            for (FileInfo fileInfo : list) {
                if (fileInfo.isVideo()) {
                    if (!AppUtils.checkFileExist(AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(fileInfo)) && !arrayList.contains(fileInfo.getPath())) {
                        arrayList.add(fileInfo.getPath());
                        this.thumbList.add(fileInfo);
                    }
                }
            }
            if (arrayList.size() > 0) {
                Dbug.i(this.tag, "-requestVideoThumbnail- tryToRequestVideoCover size = " + arrayList.size());
                ClientManager.getClient().tryToRequestVideoCover(arrayList, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.23
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(VideoFragment.this.tag, "Send failed");
                        }
                    }
                });
            }
        }
    }

    @Override // com.jieli.lib.dv.control.player.OnFrameListener
    public void onFrame(byte[] bArr, PictureInfo pictureInfo) {
        Dbug.w(this.tag, "-onFrame- start! ");
        if (bArr != null && pictureInfo != null) {
            String path = pictureInfo.getPath();
            if (!TextUtils.isEmpty(path) && (path.endsWith(".AVI") || path.endsWith(".avi"))) {
                SaveVideoThumb saveVideoThumb = new SaveVideoThumb(bArr, this.mHandler);
                this.collections.add(saveVideoThumb);
                saveVideoThumb.start();
                return;
            }
            if (this.mFrameCodec == null) {
                FrameCodec frameCodec = new FrameCodec();
                this.mFrameCodec = frameCodec;
                frameCodec.setOnFrameCodecListener(this.mOnFrameCodecListener);
            }
            Dbug.w(this.tag, "-convertToJPG- mediaInfo =" + pictureInfo.toString());
            int width = pictureInfo.getWidth();
            int height = pictureInfo.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }
            boolean convertToJPG = this.mFrameCodec.convertToJPG(bArr, width, height, path);
            Dbug.w(this.tag, "-convertToJPG- ret=" + convertToJPG);
            return;
        }
        Dbug.e(this.tag, "onFrame: object is null");
    }

    private void toDeviceGallery() {
        String videosDescription = JSonManager.getInstance().getVideosDescription();
        if (!TextUtils.isEmpty(videosDescription)) {
            Bundle bundle = new Bundle();
            bundle.putString(IConstant.KEY_VIDEO_LIST, videosDescription);
            Intent intent = new Intent(getActivity(), (Class<?>) GenericActivity.class);
            intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 6);
            intent.putExtra(IConstant.KEY_DATA, bundle);
            startActivityForResult(intent, IConstant.CODE_BROWSE_FILE);
            return;
        }
        ToastUtil.showToastShort(getString(R.string.loading));
    }

    private class MyGridViewAdapter extends BaseAdapter {
        private boolean isCancelTask;
        private Context mContext;
        private ArrayList<FileInfo> dataList = new ArrayList<>();
        private Map<String, LoadCover> taskCollection = new HashMap();
        private String mIP = ClientManager.getClient().getAddress();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyGridViewAdapter(Context context) {
            this.mContext = context;
        }

        void setDataList(ArrayList<FileInfo> arrayList) {
            ArrayList<FileInfo> arrayList2 = this.dataList;
            if (arrayList2 != null) {
                arrayList2.clear();
            } else {
                this.dataList = new ArrayList<>();
            }
            if (arrayList != null) {
                this.dataList.addAll(arrayList);
            }
        }

        private void clearDataList() {
            ArrayList<FileInfo> arrayList = this.dataList;
            if (arrayList != null) {
                arrayList.clear();
            }
        }

        private boolean checkIsBtn(int i) {
            ArrayList<FileInfo> arrayList = this.dataList;
            if (arrayList == null) {
                if (i == 0) {
                    return true;
                }
            } else if (i >= arrayList.size()) {
                return true;
            }
            return false;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            ArrayList<FileInfo> arrayList = this.dataList;
            if (arrayList == null) {
                return 1;
            }
            return 1 + arrayList.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            ArrayList<FileInfo> arrayList = this.dataList;
            if (arrayList == null || i < 0 || i >= arrayList.size()) {
                return null;
            }
            return this.dataList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.item_image, viewGroup, false);
                new ViewHolder(inflate);
                return inflate;
            }
            return view;
        }

        private void getPictureThumb(FileInfo fileInfo, int i) {
            String str = AppUtils.splicingFilePath(VideoFragment.this.mApplication.getAppName(), VideoFragment.this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD) + File.separator + AppUtils.getDownloadFilename(fileInfo);
            if (this.taskCollection.containsKey(str)) {
                return;
            }
            LoadCover loadCover = new LoadCover(this, null);
            this.taskCollection.put(str, loadCover);
            loadCover.execute(Integer.valueOf(i));
        }

        private void getVideoThumb(ImageView imageView, FileInfo fileInfo) {
            String str = AppUtils.splicingFilePath(VideoFragment.this.mApplication.getAppName(), VideoFragment.this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(fileInfo);
            if (new File(str).exists()) {
                ThumbLoader.getInstance().loadLocalThumbnail(this.mContext, str, VideoFragment.this.viewWidth, VideoFragment.this.viewHeight);
                Bitmap bitmap = ThumbLoader.getInstance().getBitmap(str);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.mipmap.ic_default_picture);
                }
                VideoFragment.this.hideLoadDialog(fileInfo);
                return;
            }
            Dbug.e(VideoFragment.this.tag, "getVideo Thumb: new=" + fileInfo.getPath());
        }

        private void getLoadVideoThumb(FileInfo fileInfo, int i) {
            String str = AppUtils.splicingFilePath(VideoFragment.this.mApplication.getAppName(), VideoFragment.this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD) + File.separator + AppUtils.getDownloadFilename(fileInfo);
            if (this.taskCollection.containsKey(str)) {
                return;
            }
            LoadCover loadCover = new LoadCover(this, null);
            this.taskCollection.put(str, loadCover);
            loadCover.execute(Integer.valueOf(i));
        }

        private class ViewHolder {
            private ImageView imageView;
            private RelativeLayout layoutVideo;
            private TextView tvDuration;

            ViewHolder(View view) {
                ImageView imageView = (ImageView) view.findViewById(R.id.item_image_iv);
                this.imageView = imageView;
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(VideoFragment.this.viewWidth, VideoFragment.this.viewHeight));
                this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                this.layoutVideo = (RelativeLayout) view.findViewById(R.id.item_image_video_layout);
                this.tvDuration = (TextView) view.findViewById(R.id.item_image_duration);
                view.setTag(this);
            }
        }

        private void cancelAllTasks() {
            this.isCancelTask = true;
            Map<String, LoadCover> map = this.taskCollection;
            if (map != null) {
                Set<String> keySet = map.keySet();
                if (keySet.size() > 0) {
                    Iterator<String> it = keySet.iterator();
                    while (it.hasNext()) {
                        LoadCover loadCover = this.taskCollection.get(it.next());
                        if (loadCover != null) {
                            loadCover.cancel(true);
                        }
                    }
                }
                this.taskCollection.clear();
            }
            this.isCancelTask = false;
        }

        private class LoadCover extends AsyncTask<Integer, Void, Bitmap> {
            private Bitmap bmp;
            private String imageUrl;
            private FileInfo info;
            private int position;

            private LoadCover() {
            }

            /* synthetic */ LoadCover(MyGridViewAdapter myGridViewAdapter, AnonymousClass1 anonymousClass1) {
                this();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Bitmap doInBackground(Integer... numArr) {
                int i = 0;
                int intValue = numArr[0].intValue();
                this.position = intValue;
                FileInfo fileInfo = (FileInfo) MyGridViewAdapter.this.getItem(intValue);
                this.info = fileInfo;
                Bitmap bitmap = null;
                if (fileInfo != null) {
                    if (fileInfo.getSource() == 1) {
                        this.imageUrl = this.info.getPath();
                    } else {
                        this.imageUrl = AppUtils.splicingFilePath(VideoFragment.this.mApplication.getAppName(), VideoFragment.this.mApplication.getUUID(), AppUtils.checkCameraDir(this.info), IConstant.DIR_DOWNLOAD) + File.separator + AppUtils.getDownloadFilename(this.info);
                    }
                    if (this.info.isVideo()) {
                        ThumbLoader.getInstance().loadLocalVideoThumb(MyGridViewAdapter.this.mContext, this.imageUrl, VideoFragment.this.viewWidth, VideoFragment.this.viewHeight, new ThumbLoader.OnLoadVideoThumbListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.MyGridViewAdapter.LoadCover.1
                            @Override // com.jieli.stream.dv.running2.util.ThumbLoader.OnLoadVideoThumbListener
                            public void onComplete(Bitmap bitmap2, int i2) {
                                LoadCover.this.bmp = bitmap2;
                                LoadCover.this.info.setDuration(i2);
                            }
                        });
                    } else {
                        String str = AppUtils.splicingFilePath(VideoFragment.this.mApplication.getAppName(), VideoFragment.this.mApplication.getUUID(), AppUtils.checkCameraDir(this.info), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(this.info);
                        if (new File(str).exists()) {
                            ThumbLoader.getInstance().loadLocalThumbnail(MyGridViewAdapter.this.mContext, str, VideoFragment.this.viewWidth, VideoFragment.this.viewHeight);
                            bitmap = ThumbLoader.getInstance().getBitmap(str);
                        }
                        if (bitmap == null) {
                            ThumbLoader.getInstance().loadWebThumbnail(MyGridViewAdapter.this.mContext, AppUtils.formatUrl(MyGridViewAdapter.this.mIP, 8080, this.info.getPath()), str, VideoFragment.this.viewWidth, VideoFragment.this.viewHeight, new ThumbLoader.OnLoadThumbListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.MyGridViewAdapter.LoadCover.2
                                @Override // com.jieli.stream.dv.running2.util.ThumbLoader.OnLoadThumbListener
                                public void onComplete(Bitmap bitmap2) {
                                    if (bitmap2 != null) {
                                        LoadCover.this.bmp = bitmap2;
                                    }
                                }
                            });
                        } else {
                            this.bmp = bitmap;
                        }
                    }
                    while (this.bmp == null && !MyGridViewAdapter.this.isCancelTask) {
                        try {
                            Thread.sleep(5L);
                            i += 5;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i > 2000) {
                            break;
                        }
                    }
                } else {
                    this.bmp = null;
                }
                return this.bmp;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Bitmap bitmap) {
                VideoFragment.this.hideLoadDialog(this.info);
                MyGridViewAdapter.this.taskCollection.remove(this.imageUrl);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWaitingDialog() {
        if (getActivity() == null || getActivity().isDestroyed()) {
            return;
        }
        if (this.mWaitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.mWaitingDialog = waitingDialog;
            waitingDialog.setCancelable(false);
            this.mWaitingDialog.setNotifyContent(getString(R.string.loading));
            this.mWaitingDialog.setOnWaitingDialog(new WaitingDialog.OnWaitingDialog() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.25
                @Override // com.jieli.stream.dv.running2.ui.dialog.WaitingDialog.OnWaitingDialog
                public void onCancelDialog() {
                    VideoFragment.this.dismissWaitingDialog();
                    if (VideoFragment.this.isAdjustResolution) {
                        VideoFragment.this.isAdjustResolution = false;
                    }
                }
            });
        }
        if (this.mWaitingDialog.isShowing()) {
            return;
        }
        Dbug.w(this.tag, "mWaitingDialog show");
        this.mWaitingDialog.show(getFragmentManager(), "mLoadingDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing()) {
                Dbug.w(this.tag, "mWaitingDialog dismiss");
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }

    private class SaveVideoThumb extends Thread {
        private byte[] data;
        private SoftReference<Handler> softReference;

        SaveVideoThumb(byte[] bArr, Handler handler) {
            this.data = bArr;
            this.softReference = new SoftReference<>(handler);
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x0134, code lost:
        
            if (r10.this$0.mVideoThumbnail.close() == false) goto L54;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x0196, code lost:
        
            com.jieli.stream.dv.running2.util.Dbug.w(r10.this$0.tag, "Close Video thumbnail failed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x0194, code lost:
        
            if (r10.this$0.mVideoThumbnail.close() == false) goto L54;
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 537
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.SaveVideoThumb.run():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelSaveThread() {
        Set<SaveVideoThumb> set = this.collections;
        if (set != null) {
            Iterator<SaveVideoThumb> it = set.iterator();
            while (it.hasNext()) {
                it.next().interrupt();
            }
            this.collections.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shootSound() {
        if (getActivity() == null) {
            return;
        }
        AudioManager audioManager = (AudioManager) getActivity().getSystemService("audio");
        int streamVolume = audioManager != null ? audioManager.getStreamVolume(4) : 0;
        Dbug.i(this.tag, "volume=:" + streamVolume);
        if (streamVolume != 0) {
            MediaPlayer create = MediaPlayer.create(getActivity(), R.raw.camera_click);
            create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.26
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
    public void handlerVideoUI() {
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
        this.iv_record.setImageResource(R.mipmap.icon_record_stop);
        this.tv_record_time.setVisibility(0);
        this.startTime = System.currentTimeMillis();
        this.handler.removeCallbacks(this.updateTimer);
        this.handler.postDelayed(this.updateTimer, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideVideoUI() {
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        this.iv_record.setImageResource(R.mipmap.icon_record);
        this.handler.removeCallbacks(this.updateTimer);
        this.tv_record_time.setVisibility(8);
        this.tv_record_time.setText("00:00:00");
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchStreamResolution(int i) {
        closeRTS();
        if (this.isRtspEnable) {
            AppUtils.saveRtspResolutionLevel(i);
            this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.29
                @Override // java.lang.Runnable
                public void run() {
                    VideoFragment.this.openRTS();
                    VideoFragment.this.updateResolutionIcon();
                }
            }, 200L);
        } else {
            this.isAdjustResolution = true;
            AppUtils.saveStreamResolutionLevel(i);
            showWaitingDialog();
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
            handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.31
                @Override // java.lang.Runnable
                public void run() {
                    VideoFragment.this.isRtVoiceOpen = false;
                    VideoFragment.this.handlerRTVoiceUI();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideLoadDialog(FileInfo fileInfo) {
        this.countList.remove(fileInfo);
        if (this.countList.size() == 0) {
            dismissWaitingDialog();
            this.mHandler.removeMessages(MSG_REQUEST_THUMB);
        } else {
            this.mHandler.removeMessages(MSG_REQUEST_THUMB);
            this.mHandler.sendEmptyMessageDelayed(MSG_REQUEST_THUMB, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncDeviceState() {
        Dbug.i(this.tag, "syncDeviceState ");
        DeviceSettingInfo deviceSettingInfo = this.mApplication.getDeviceSettingInfo();
        if (deviceSettingInfo != null) {
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
            if (this.mCameraType != deviceSettingInfo.getCameraType()) {
                this.mCameraType = deviceSettingInfo.getCameraType();
                updateDeviceMsg();
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(MSG_LOAD_DEV_THUMBS, 1, 0));
                }
            }
            updateResolutionIcon();
            clearDataAndUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDataAndUpdate() {
        List<FileInfo> list = this.totalList;
        if (list != null) {
            list.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FileInfo findFileInfo(String str) {
        List<FileInfo> list;
        if (!TextUtils.isEmpty(str) && (list = this.totalList) != null) {
            for (FileInfo fileInfo : list) {
                if (str.equals(fileInfo.getPath())) {
                    return fileInfo;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rotateDeviceMsgLayout() {
        Dbug.i(this.tag, "rotateDeviceMsgLayout");
        int cameraType = this.mApplication.getDeviceSettingInfo().getCameraType();
        if (this.mCameraType != cameraType) {
            this.mCameraType = cameraType;
            updateResolutionIcon();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(MSG_LOAD_DEV_THUMBS, 1, 0));
            }
        }
    }

    private class TurnRearViewAnim implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        private TurnRearViewAnim() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            VideoFragment.this.updateDeviceMsg();
        }
    }

    private class TurnFrontViewAnim implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        private TurnFrontViewAnim() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            VideoFragment.this.updateDeviceMsg();
        }
    }

    private int getLevelResId(int i) {
        if (i == 0) {
            return R.drawable.drawable_resolution_sd;
        }
        if (i == 2) {
            return R.drawable.drawable_resolution_fhd;
        }
        return R.drawable.drawable_resolution_hd;
    }

    private int getCameraLevel(int i) {
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

    private void goToPlayBack(FileInfo fileInfo) {
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyResponse);
        Intent intent = new Intent(getActivity(), (Class<?>) PlaybackActivity.class);
        if (fileInfo != null) {
            intent.putExtra(IConstant.KEY_FILE_INFO, fileInfo);
        }
        startActivityForResult(intent, IConstant.CODE_PLAYBACK);
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
            dismissWaitingDialog();
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
            return;
        }
        Dbug.i(this.tag, "updateResolutionIcon currentLevel=" + streamResolutionLevel);
    }

    private void showLocalRecordDialog() {
        if (this.mLocalRecordingDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.no_card_record_tip, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.32
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    VideoFragment.this.mLocalRecordingDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.33
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    VideoFragment.this.mLocalRecordingDialog.dismiss();
                    VideoFragment.this.startLocalRecording();
                }
            });
            this.mLocalRecordingDialog = newInstance;
            newInstance.setCancelable(false);
        }
        if (this.mLocalRecordingDialog.isShowing()) {
            return;
        }
        this.mLocalRecordingDialog.show(getActivity().getSupportFragmentManager(), "No_Card_Record");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLocalRecording() {
        if (this.mRecordVideo == null) {
            this.mRecordVideo = new VideoRecord();
        }
        this.mRecordVideo.prepare(true, new OnRecordStateListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.34
            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onPrepared() {
                Dbug.i(VideoFragment.this.tag, "LocalRecord: onPrepared");
                VideoFragment.this.isRecordPrepared = true;
                VideoFragment.this.save = true;
                VideoFragment.this.showVideoUI();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onCompletion(String str) {
                Dbug.i(VideoFragment.this.tag, "LocalRecord: onCompletion");
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onStop() {
                Dbug.i(VideoFragment.this.tag, "LocalRecord: onStop");
                VideoFragment.this.hideVideoUI();
                VideoFragment.this.stopLocalRecording();
            }

            @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
            public void onError(String str) {
                Dbug.e(VideoFragment.this.tag, "Record error:" + str);
                VideoFragment.this.hideVideoUI();
                VideoFragment.this.stopLocalRecording();
            }
        });
    }

    public void stopLocalRecording() {
        if (this.mRecordVideo != null) {
            String replace = new File(this.mRecordVideo.getCurrentFilePath()).getName().replace("avi", "jpg");
            this.aviJpegName = replace;
            byte[] bArr = this.firstData;
            if (bArr != null) {
                this.tempCapture.capture(bArr, replace);
            }
            getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + this.mRecordVideo.getCurrentFilePath())));
            this.mRecordVideo.close();
            this.mRecordVideo = null;
        }
        this.isRecordPrepared = false;
    }

    public void showStopLocalRecordingDialog(final OnClickStateListener onClickStateListener) {
        NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.recording_will_stop, R.string.dialog_no, R.string.dialog_yes, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.35
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
            public void onClick() {
                VideoFragment.this.mStopLocalRecordingDialog.dismiss();
                OnClickStateListener onClickStateListener2 = onClickStateListener;
                if (onClickStateListener2 != null) {
                    onClickStateListener2.onCancel();
                }
            }
        }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.VideoFragment.36
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
            public void onClick() {
                VideoFragment.this.mStopLocalRecordingDialog.dismiss();
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
        this.mStopLocalRecordingDialog.show(getActivity().getSupportFragmentManager(), "Stop_local_recording");
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
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.drop_packet_count, i + "");
        hudView.setRowValue(R.string.drop_packet_sum, i2 + "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDebugFps(int i) {
        InfoHudViewHolder hudView;
        IjkVideoView ijkVideoView = this.mVideoView;
        if (ijkVideoView == null || (hudView = ijkVideoView.getHudView()) == null) {
            return;
        }
        hudView.setRowValue(R.string.fps, i + "");
    }
}
