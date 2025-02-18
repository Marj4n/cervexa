package com.jieli.stream.dv.running2.ui.widget.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TableLayout;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.task.DebugHelper;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

/* loaded from: classes.dex */
public class InfoHudViewHolder {
    private static final int MSG_UPDATE_HUD = 1;
    private static int count;
    private Context mContext;
    private IMediaPlayer mMediaPlayer;
    private TableLayoutBinder mTableLayoutBinder;
    private SparseArray<View> mRowMap = new SparseArray<>();
    private long mLoadCost = 0;
    private long mSeekCost = 0;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.widget.media.InfoHudViewHolder.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            IMediaPlayer internalMediaPlayer;
            String str;
            String str2;
            int i;
            if (message.what != 1) {
                return false;
            }
            IjkMediaPlayer ijkMediaPlayer = null;
            if (InfoHudViewHolder.this.mMediaPlayer == null) {
                return false;
            }
            if (InfoHudViewHolder.this.mMediaPlayer instanceof IjkMediaPlayer) {
                ijkMediaPlayer = (IjkMediaPlayer) InfoHudViewHolder.this.mMediaPlayer;
            } else if ((InfoHudViewHolder.this.mMediaPlayer instanceof MediaPlayerProxy) && (internalMediaPlayer = ((MediaPlayerProxy) InfoHudViewHolder.this.mMediaPlayer).getInternalMediaPlayer()) != null && (internalMediaPlayer instanceof IjkMediaPlayer)) {
                ijkMediaPlayer = (IjkMediaPlayer) internalMediaPlayer;
            }
            if (ijkMediaPlayer == null) {
                return false;
            }
            int videoDecoder = ijkMediaPlayer.getVideoDecoder();
            if (videoDecoder == 1) {
                InfoHudViewHolder.this.setRowValue(R.string.vdec, "avcodec");
            } else if (videoDecoder == 2) {
                InfoHudViewHolder.this.setRowValue(R.string.vdec, "MediaCodec");
            } else {
                InfoHudViewHolder.this.setRowValue(R.string.vdec, "");
            }
            InfoHudViewHolder.this.setRowValue(R.string.audio_decoder, ijkMediaPlayer.getMediaInfo().mAudioDecoder);
            long videoCachedDuration = ijkMediaPlayer.getVideoCachedDuration();
            long audioCachedDuration = ijkMediaPlayer.getAudioCachedDuration();
            long videoCachedBytes = ijkMediaPlayer.getVideoCachedBytes();
            long audioCachedBytes = ijkMediaPlayer.getAudioCachedBytes();
            int videoWidth = ijkMediaPlayer.getVideoWidth();
            int videoHeight = ijkMediaPlayer.getVideoHeight();
            if (ijkMediaPlayer.getMediaInfo().mMeta != null) {
                if (ijkMediaPlayer.getMediaInfo().mMeta.mAudioStream != null) {
                    i = ijkMediaPlayer.getMediaInfo().mMeta.mAudioStream.mSampleRate;
                    str = ijkMediaPlayer.getMediaInfo().mMeta.mAudioStream.mCodecName;
                } else {
                    str = "";
                    i = 0;
                }
                str2 = ijkMediaPlayer.getMediaInfo().mMeta.mVideoStream != null ? ijkMediaPlayer.getMediaInfo().mMeta.mVideoStream.mCodecName : "";
            } else {
                str = "";
                str2 = str;
                i = 0;
            }
            InfoHudViewHolder.this.setRowValue(R.string.video_rate, videoWidth + "x" + videoHeight);
            if (i > 0) {
                InfoHudViewHolder.this.setRowValue(R.string.audio_sample_rate, i + "");
            }
            if (!TextUtils.isEmpty(str2)) {
                InfoHudViewHolder.this.setRowValue(R.string.v_codec, str2);
            }
            if (!TextUtils.isEmpty(str)) {
                InfoHudViewHolder.this.setRowValue(R.string.a_codec, str);
            }
            InfoHudViewHolder.this.setRowValue(R.string.v_cache, String.format(Locale.getDefault(), "%s, %s", InfoHudViewHolder.formatedDurationMilli(videoCachedDuration), InfoHudViewHolder.formatedSize(videoCachedBytes)));
            InfoHudViewHolder.this.setRowValue(R.string.a_cache, String.format(Locale.getDefault(), "%s, %s", InfoHudViewHolder.formatedDurationMilli(audioCachedDuration), InfoHudViewHolder.formatedSize(audioCachedBytes)));
            InfoHudViewHolder.access$308();
            if (InfoHudViewHolder.count >= 2) {
                int unused = InfoHudViewHolder.count = 0;
                InfoHudViewHolder.this.setRowValue(R.string.bit_rate, DebugHelper.getNetSpeed(InfoHudViewHolder.this.mContext));
            }
            InfoHudViewHolder.this.mHandler.removeMessages(1);
            InfoHudViewHolder.this.mHandler.sendEmptyMessageDelayed(1, 500L);
            return false;
        }
    });

    static /* synthetic */ int access$308() {
        int i = count;
        count = i + 1;
        return i;
    }

    public InfoHudViewHolder(Context context, TableLayout tableLayout) {
        this.mContext = context;
        this.mTableLayoutBinder = new TableLayoutBinder(context, tableLayout);
    }

    private void appendSection(int i) {
        this.mTableLayoutBinder.appendSection(i);
    }

    private void appendRow(int i) {
        this.mRowMap.put(i, this.mTableLayoutBinder.appendRow2(i, (String) null));
    }

    public void setRowValue(int i, String str) {
        View view = this.mRowMap.get(i);
        if (view == null) {
            this.mRowMap.put(i, this.mTableLayoutBinder.appendRow2(i, str));
        } else {
            this.mTableLayoutBinder.setValueText(view, str);
        }
    }

    public void setMediaPlayer(IMediaPlayer iMediaPlayer) {
        this.mMediaPlayer = iMediaPlayer;
        if (iMediaPlayer != null) {
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        } else {
            this.mHandler.removeMessages(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatedDurationMilli(long j) {
        return j >= 1000 ? String.format(Locale.getDefault(), "%.2f sec", Float.valueOf(j / 1000.0f)) : String.format(Locale.getDefault(), "%d msec", Long.valueOf(j));
    }

    private static String formatedSpeed(long j, long j2) {
        if (j2 <= 0 || j <= 0) {
            return "0 B/s";
        }
        float f = (j * 1000.0f) / j2;
        return f >= 1000000.0f ? String.format(Locale.getDefault(), "%.2f MB/s", Float.valueOf((f / 1000.0f) / 1000.0f)) : f >= 1000.0f ? String.format(Locale.getDefault(), "%.1f KB/s", Float.valueOf(f / 1000.0f)) : String.format(Locale.getDefault(), "%d B/s", Long.valueOf((long) f));
    }

    public void updateLoadCost(long j) {
        this.mLoadCost = j;
    }

    public void updateSeekCost(long j) {
        this.mSeekCost = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatedSize(long j) {
        return j >= 100000 ? String.format(Locale.getDefault(), "%.2f MB", Float.valueOf((j / 1000.0f) / 1000.0f)) : j >= 100 ? String.format(Locale.getDefault(), "%.1f KB", Float.valueOf(j / 1000.0f)) : String.format(Locale.getDefault(), "%d B", Long.valueOf(j));
    }
}
