package com.gizthon.camera.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gizthon.camera.R;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.Formatter;
import java.util.Locale;

/* loaded from: classes.dex */
public class PlayerMediaController extends FrameLayout {
    private static final int FADE_OUT = 1;
    protected static int IC_MEDIA_PAUSE_ID = Resources.getSystem().getIdentifier("ic_media_pause", "drawable", IConstant.ANDROID_DIR);
    protected static int IC_MEDIA_PLAY_ID = Resources.getSystem().getIdentifier("ic_media_play", "drawable", IConstant.ANDROID_DIR);
    private static final int PROGRESS = 3;
    private static final int SHOW_PROGRESS = 2;
    private ViewGroup mAnchor;
    private final Context mContext;
    protected TextView mCurrentTime;
    private boolean mDragging;
    protected TextView mEndTime;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean mFromXml;
    private final Handler mHandler;
    private final View.OnLayoutChangeListener mLayoutChangeListener;
    protected ImageView mPauseButton;
    private final View.OnClickListener mPauseListener;
    private MediaController.MediaPlayerControl mPlayer;
    protected SeekBar mProgress;
    private View mRoot;
    private final SeekBar.OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private final boolean mUseFastForward;
    private boolean needHide;
    private int sDefaultTimeout;

    protected int getControllerLayoutId() {
        return R.layout.ijk_media_control;
    }

    protected void posCallBack(int i) {
    }

    public PlayerMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.sDefaultTimeout = 5000;
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.gizthon.camera.view.PlayerMediaController.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                PlayerMediaController.this.updateFloatingWindowLayout();
                boolean unused = PlayerMediaController.this.mShowing;
            }
        };
        this.needHide = true;
        this.mHandler = new Handler(new Handler.Callback() { // from class: com.gizthon.camera.view.PlayerMediaController.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    if (!PlayerMediaController.this.needHide) {
                        return false;
                    }
                    PlayerMediaController.this.hide();
                    return false;
                }
                if (i != 2) {
                    if (i != 3) {
                        return false;
                    }
                    int currentPosition = PlayerMediaController.this.mPlayer.getCurrentPosition();
                    if (PlayerMediaController.this.mPlayer.isPlaying()) {
                        PlayerMediaController.this.posCallBack(currentPosition);
                    }
                    PlayerMediaController.this.mHandler.sendMessageDelayed(Message.obtain(message), 2000L);
                    return false;
                }
                int progress = PlayerMediaController.this.setProgress();
                if (PlayerMediaController.this.mDragging || !PlayerMediaController.this.mShowing || !PlayerMediaController.this.mPlayer.isPlaying()) {
                    return false;
                }
                PlayerMediaController.this.mHandler.sendMessageDelayed(Message.obtain(message), 1000 - (progress % 1000));
                return false;
            }
        });
        this.mPauseListener = new View.OnClickListener() { // from class: com.gizthon.camera.view.PlayerMediaController.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlayerMediaController.this.doPauseResume();
                PlayerMediaController playerMediaController = PlayerMediaController.this;
                playerMediaController.show(playerMediaController.sDefaultTimeout);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.gizthon.camera.view.PlayerMediaController.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (PlayerMediaController.this.mPlayer.isPlaying()) {
                    PlayerMediaController.this.mPlayer.pause();
                }
                PlayerMediaController.this.show(3600000);
                PlayerMediaController.this.mDragging = true;
                PlayerMediaController.this.mHandler.removeMessages(2);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    long duration = (PlayerMediaController.this.mPlayer.getDuration() * i) / 1000;
                    if (PlayerMediaController.this.mCurrentTime != null) {
                        PlayerMediaController.this.mCurrentTime.setText(PlayerMediaController.this.stringForTime((int) duration));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerMediaController.this.mPlayer.seekTo((int) ((PlayerMediaController.this.mPlayer.getDuration() * seekBar.getProgress()) / 1000));
                PlayerMediaController.this.mPlayer.start();
                PlayerMediaController.this.mDragging = false;
                PlayerMediaController.this.setProgress();
                PlayerMediaController.this.updatePausePlay();
                PlayerMediaController playerMediaController = PlayerMediaController.this;
                playerMediaController.show(playerMediaController.sDefaultTimeout);
                PlayerMediaController.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRoot = this;
        this.mContext = context;
        this.mUseFastForward = true;
        this.mFromXml = true;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        View view = this.mRoot;
        if (view != null) {
            initControllerView(view);
        }
    }

    public PlayerMediaController(Context context, boolean z) {
        super(context);
        this.sDefaultTimeout = 5000;
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.gizthon.camera.view.PlayerMediaController.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                PlayerMediaController.this.updateFloatingWindowLayout();
                boolean unused = PlayerMediaController.this.mShowing;
            }
        };
        this.needHide = true;
        this.mHandler = new Handler(new Handler.Callback() { // from class: com.gizthon.camera.view.PlayerMediaController.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    if (!PlayerMediaController.this.needHide) {
                        return false;
                    }
                    PlayerMediaController.this.hide();
                    return false;
                }
                if (i != 2) {
                    if (i != 3) {
                        return false;
                    }
                    int currentPosition = PlayerMediaController.this.mPlayer.getCurrentPosition();
                    if (PlayerMediaController.this.mPlayer.isPlaying()) {
                        PlayerMediaController.this.posCallBack(currentPosition);
                    }
                    PlayerMediaController.this.mHandler.sendMessageDelayed(Message.obtain(message), 2000L);
                    return false;
                }
                int progress = PlayerMediaController.this.setProgress();
                if (PlayerMediaController.this.mDragging || !PlayerMediaController.this.mShowing || !PlayerMediaController.this.mPlayer.isPlaying()) {
                    return false;
                }
                PlayerMediaController.this.mHandler.sendMessageDelayed(Message.obtain(message), 1000 - (progress % 1000));
                return false;
            }
        });
        this.mPauseListener = new View.OnClickListener() { // from class: com.gizthon.camera.view.PlayerMediaController.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlayerMediaController.this.doPauseResume();
                PlayerMediaController playerMediaController = PlayerMediaController.this;
                playerMediaController.show(playerMediaController.sDefaultTimeout);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.gizthon.camera.view.PlayerMediaController.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (PlayerMediaController.this.mPlayer.isPlaying()) {
                    PlayerMediaController.this.mPlayer.pause();
                }
                PlayerMediaController.this.show(3600000);
                PlayerMediaController.this.mDragging = true;
                PlayerMediaController.this.mHandler.removeMessages(2);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z2) {
                if (z2) {
                    long duration = (PlayerMediaController.this.mPlayer.getDuration() * i) / 1000;
                    if (PlayerMediaController.this.mCurrentTime != null) {
                        PlayerMediaController.this.mCurrentTime.setText(PlayerMediaController.this.stringForTime((int) duration));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerMediaController.this.mPlayer.seekTo((int) ((PlayerMediaController.this.mPlayer.getDuration() * seekBar.getProgress()) / 1000));
                PlayerMediaController.this.mPlayer.start();
                PlayerMediaController.this.mDragging = false;
                PlayerMediaController.this.setProgress();
                PlayerMediaController.this.updatePausePlay();
                PlayerMediaController playerMediaController = PlayerMediaController.this;
                playerMediaController.show(playerMediaController.sDefaultTimeout);
                PlayerMediaController.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mContext = context;
        this.mUseFastForward = z;
        initFloatingWindow();
    }

    public PlayerMediaController(Context context) {
        this(context, true);
    }

    private void initFloatingWindow() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(262144);
        requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFloatingWindowLayout() {
        this.mAnchor.getLocationOnScreen(new int[2]);
    }

    public void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl) {
        this.mPlayer = mediaPlayerControl;
        this.mHandler.sendEmptyMessage(3);
        updatePausePlay();
    }

    public void setAnchorView(ViewGroup viewGroup) {
        ViewGroup viewGroup2 = this.mAnchor;
        if (viewGroup2 != null) {
            viewGroup2.removeOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        this.mAnchor = viewGroup;
        if (viewGroup != null) {
            viewGroup.addOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        removeAllViews();
        View makeControllerView = makeControllerView();
        addView(makeControllerView, -1, -1);
        this.mAnchor.removeView(this);
        this.mAnchor.addView(this, -1, -1);
        makeControllerView.setVisibility(View.INVISIBLE);
    }

    protected View makeControllerView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(getControllerLayoutId(), (ViewGroup) null);
        this.mRoot = inflate;
        initControllerView(inflate);
        return this.mRoot;
    }

    protected void initControllerView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.mediacontroller_play_pause);
        this.mPauseButton = imageView;
        if (imageView != null) {
            imageView.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
        }
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.mediacontroller_seekbar);
        this.mProgress = seekBar;
        if (seekBar != null) {
            if (seekBar instanceof SeekBar) {
                seekBar.setOnSeekBarChangeListener(this.mSeekListener);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) view.findViewById(R.id.mediacontroller_time_total);
        this.mCurrentTime = (TextView) view.findViewById(R.id.mediacontroller_time_current);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
    }

    public void show() {
        show(this.sDefaultTimeout);
    }

    private void disableUnsupportedButtons() {
        try {
            if (this.mPauseButton != null && !this.mPlayer.canPause()) {
                this.mPauseButton.setEnabled(false);
            }
            if (this.mProgress == null || this.mPlayer.canSeekBackward() || this.mPlayer.canSeekForward()) {
                return;
            }
            this.mProgress.setEnabled(false);
        } catch (IncompatibleClassChangeError unused) {
        }
    }

    public void show(int i) {
        if (!this.mShowing && this.mAnchor != null) {
            setProgress();
            ImageView imageView = this.mPauseButton;
            if (imageView != null) {
                imageView.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            this.mRoot.setVisibility(View.VISIBLE);
            this.mShowing = true;
        }
        updatePausePlay();
        this.mHandler.sendEmptyMessage(2);
        if (i != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), i);
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mAnchor != null && this.mShowing) {
            try {
                this.mHandler.removeMessages(2);
                this.mRoot.setVisibility(View.GONE);
            } catch (IllegalArgumentException unused) {
                Log.w("MediaController", "already removed");
            }
            this.mShowing = false;
        }
    }

    public void setNeedHide(boolean z) {
        this.needHide = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String stringForTime(int i) {
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 / 60) % 60;
        int i5 = i2 / 3600;
        this.mFormatBuilder.setLength(0);
        return i5 > 0 ? this.mFormatter.format("%d:%02d:%02d", Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i3)).toString() : this.mFormatter.format("%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3)).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setProgress() {
        MediaController.MediaPlayerControl mediaPlayerControl = this.mPlayer;
        if (mediaPlayerControl == null || this.mDragging) {
            return 0;
        }
        int currentPosition = mediaPlayerControl.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        SeekBar seekBar = this.mProgress;
        if (seekBar != null) {
            if (duration > 0) {
                seekBar.setProgress((int) ((currentPosition * 1000) / duration));
            }
            this.mProgress.setSecondaryProgress(this.mPlayer.getBufferPercentage() * 10);
        }
        TextView textView = this.mEndTime;
        if (textView != null) {
            textView.setText(stringForTime(duration));
        }
        TextView textView2 = this.mCurrentTime;
        if (textView2 != null) {
            textView2.setText(stringForTime(currentPosition));
        }
        updatePausePlay();
        return currentPosition;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (!isShowing()) {
                show(this.sDefaultTimeout);
            } else {
                hide();
            }
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        show(this.sDefaultTimeout);
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        boolean z = keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0;
        if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
            if (z) {
                doPauseResume();
                show(this.sDefaultTimeout);
                ImageView imageView = this.mPauseButton;
                if (imageView != null) {
                    imageView.requestFocus();
                }
            }
            return true;
        }
        if (keyCode == 126) {
            if (z && !this.mPlayer.isPlaying()) {
                this.mPlayer.start();
                updatePausePlay();
                show(this.sDefaultTimeout);
            }
            return true;
        }
        if (keyCode == 86 || keyCode == 127) {
            if (z && this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
                updatePausePlay();
                show(this.sDefaultTimeout);
            }
            return true;
        }
        if (keyCode == 25 || keyCode == 24 || keyCode == 164 || keyCode == 27) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (keyCode == 4 || keyCode == 82) {
            if (!this.needHide) {
                ((Activity) getContext()).finish();
                return true;
            }
            if (isShowing()) {
                hide();
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePausePlay() {
        if (this.mRoot == null || this.mPauseButton == null) {
            return;
        }
        if (this.mPlayer.isPlaying()) {
            this.mPauseButton.setImageResource(IC_MEDIA_PAUSE_ID);
        } else {
            this.mPauseButton.setImageResource(IC_MEDIA_PLAY_ID);
        }
    }

    protected void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        ImageView imageView = this.mPauseButton;
        if (imageView != null) {
            imageView.setEnabled(z);
        }
        SeekBar seekBar = this.mProgress;
        if (seekBar != null) {
            seekBar.setEnabled(z);
        }
        disableUnsupportedButtons();
        super.setEnabled(z);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return MediaController.class.getName();
    }

    public void setDefaultTimeout(int i) {
        this.sDefaultTimeout = i;
    }
}
