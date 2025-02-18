package com.jieli.stream.dv.running2.util;

import android.text.TextUtils;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.model.MediaInfo;
import com.jieli.lib.dv.control.player.MovWrapper;
import com.jieli.lib.dv.control.player.OnPlaybackListener;
import com.jieli.lib.dv.control.player.OnRecordListener;
import com.jieli.lib.dv.control.player.PlaybackStream;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.interfaces.OnDownloadListener;
import com.jieli.stream.dv.running2.util.json.JSonManager;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class VideoManager {
    public static final int ERROR_CLOSE_RECORD = 228;
    public static final int ERROR_PARAMS = 227;
    private static boolean isDownloading;
    private static String tag = VideoManager.class.getSimpleName();
    private int duration;
    private OnDownloadListener listener;
    private Timer mTimerThread;
    private MovWrapper movWrapper;
    private String outPath;
    private PlaybackStream playbackStream;
    private long startTime;
    private volatile int mTimer = 0;
    private volatile long mLastTime = 0;
    private final OnPlaybackListener onPlaybackListener = new OnPlaybackListener() { // from class: com.jieli.stream.dv.running2.util.VideoManager.2
        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onError(int i, String str) {
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onStateChanged(int i) {
        }

        @Override // com.jieli.lib.dv.control.player.OnPlaybackListener
        public void onUpdate(MediaInfo mediaInfo) {
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onVideo(int i, int i2, byte[] bArr, long j, long j2) {
            if (VideoManager.isDownloading) {
                VideoManager.this.movWrapper.write(i, bArr);
            }
        }

        @Override // com.jieli.lib.dv.control.player.IPlayerListener
        public void onAudio(int i, int i2, byte[] bArr, long j, long j2) {
            if (VideoManager.isDownloading) {
                VideoManager.this.movWrapper.write(i, bArr);
            }
        }
    };
    private OnRecordListener mOnRecordListener = new OnRecordListener() { // from class: com.jieli.stream.dv.running2.util.VideoManager.3
        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onError(int i, String str) {
            super.onError(i, str);
            VideoManager.this.tryToStopDownload();
            if (VideoManager.this.listener != null) {
                VideoManager.this.listener.onError(i, str);
            }
        }

        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onStateChanged(int i, String str) {
            super.onStateChanged(i, str);
            Dbug.e(VideoManager.tag, "-onStateChanged- state : " + i);
            if (i == 1) {
                boolean unused = VideoManager.isDownloading = true;
                VideoManager.this.mTimer = 0;
                VideoManager.this.mLastTime = 0L;
                VideoManager.this.mTimerThread = new Timer();
                VideoManager.this.mTimerThread.schedule(new MyTimeTask(), 0L, 1000L);
                if (VideoManager.this.listener != null) {
                    VideoManager.this.listener.onStartLoad();
                    return;
                }
                return;
            }
            if (i == 2) {
                if (VideoManager.this.listener != null) {
                    VideoManager.this.listener.onCompletion();
                }
                if (VideoManager.this.mTimerThread != null) {
                    VideoManager.this.mTimerThread.cancel();
                    VideoManager.this.mTimerThread = null;
                }
                boolean unused2 = VideoManager.isDownloading = false;
                VideoManager.this.mTimer = 0;
                VideoManager.this.mLastTime = 0L;
                VideoManager.this.duration = 0;
            }
        }
    };

    static /* synthetic */ int access$408(VideoManager videoManager) {
        int i = videoManager.mTimer;
        videoManager.mTimer = i + 1;
        return i;
    }

    public VideoManager() {
        MovWrapper movWrapper = new MovWrapper();
        this.movWrapper = movWrapper;
        movWrapper.setOnRecordListener(this.mOnRecordListener);
    }

    public synchronized MovWrapper getMovWrapper() {
        return this.movWrapper;
    }

    public void setPlaybackStream(PlaybackStream playbackStream) {
        this.playbackStream = playbackStream;
        playbackStream.registerPlayerListener(this.onPlaybackListener);
    }

    public static boolean isDownloading() {
        return isDownloading;
    }

    public String getOutPath() {
        return this.outPath;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.listener = onDownloadListener;
    }

    public boolean setRecordTimerType(int i) {
        MovWrapper movWrapper = this.movWrapper;
        return movWrapper != null && movWrapper.setTimeMaster(i);
    }

    public void startDownload(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.outPath = str;
        if (this.movWrapper.create(str)) {
            return;
        }
        OnDownloadListener onDownloadListener = this.listener;
        if (onDownloadListener != null) {
            onDownloadListener.onError(227, "create output path failed.");
        }
        isDownloading = false;
    }

    public void tryToStopDownload() {
        OnDownloadListener onDownloadListener;
        if (!isDownloading || this.movWrapper.close() || (onDownloadListener = this.listener) == null) {
            return;
        }
        onDownloadListener.onError(ERROR_CLOSE_RECORD, "close recording failed, please try later.");
    }

    public static FileInfo getDownloadFileInfo(long j, long j2) {
        if (j <= 0 || j2 <= 0 || j >= j2) {
            Dbug.e(tag, "Illegal argument exception. start=" + j + ", end=" + j2);
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Dbug.i(tag, "start:" + simpleDateFormat.format(Long.valueOf(j)) + ", end:" + simpleDateFormat.format(Long.valueOf(j2)));
        FileInfo selectedMedia = getSelectedMedia(j);
        if (selectedMedia == null) {
            Dbug.e(tag, "Start VideoInfo not found.");
            return null;
        }
        long downloadVideoDuration = getDownloadVideoDuration(j, j2) / 1000;
        FileInfo fileInfo = (FileInfo) selectedMedia.clone();
        fileInfo.setDuration((int) downloadVideoDuration);
        fileInfo.setOffset(selectedMedia.getOffset());
        fileInfo.setPath(selectedMedia.getPath());
        Dbug.i(tag, "download duration=" + downloadVideoDuration);
        if (downloadVideoDuration >= 0) {
            return fileInfo;
        }
        Dbug.e(tag, "Total time error:" + downloadVideoDuration);
        return null;
    }

    @Deprecated
    public boolean tryToDownload(long j, long j2, String str) {
        if (j > 0 && !TextUtils.isEmpty(str)) {
            this.outPath = str;
            FileInfo selectedMedia = getSelectedMedia(j);
            if (selectedMedia != null) {
                this.startTime = j;
                if (j2 > 0 && j2 > j) {
                    this.duration = (int) (getDownloadVideoDuration(j, j2) / 1000);
                    Dbug.w(tag, "-tryToDownload- duration : " + this.duration);
                    if (this.duration > this.movWrapper.getVideoDuration() * 60) {
                        this.movWrapper.setVideoDuration((this.duration / 60) + 2);
                    }
                }
                int offset = selectedMedia.getOffset();
                Dbug.w(tag, "-tryToDownload- offset : " + offset);
                if (!this.playbackStream.isStreamReceiving()) {
                    ClientManager.getClient().tryToStartPlayback(selectedMedia.getPath(), offset, new SendResponse() { // from class: com.jieli.stream.dv.running2.util.VideoManager.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                boolean unused = VideoManager.isDownloading = false;
                                return;
                            }
                            if (VideoManager.this.movWrapper.create(VideoManager.this.outPath)) {
                                return;
                            }
                            if (VideoManager.this.listener != null) {
                                VideoManager.this.listener.onError(227, "create output path failed.");
                            }
                            boolean unused2 = VideoManager.isDownloading = false;
                            VideoManager.this.mTimer = 0;
                            VideoManager.this.mLastTime = 0L;
                        }
                    });
                    return true;
                }
                if (this.movWrapper.create(this.outPath)) {
                    return true;
                }
                OnDownloadListener onDownloadListener = this.listener;
                if (onDownloadListener != null) {
                    onDownloadListener.onError(227, "create output path failed.");
                }
                isDownloading = false;
                return true;
            }
        }
        return false;
    }

    private static synchronized FileInfo getSelectedMedia(long j) {
        synchronized (VideoManager.class) {
            if (j <= 0) {
                Dbug.e(tag, "milliseconds <= 0: " + j);
                return null;
            }
            List<FileInfo> videoInfoList = JSonManager.getInstance().getVideoInfoList();
            if (videoInfoList != null) {
                AppUtils.descSortWay(videoInfoList);
                for (FileInfo fileInfo : videoInfoList) {
                    if (j >= fileInfo.getStartTime().getTimeInMillis() && j <= fileInfo.getEndTime().getTimeInMillis()) {
                        fileInfo.setOffset((int) (j - fileInfo.getStartTime().getTimeInMillis()));
                        return fileInfo;
                    }
                }
                Dbug.w(tag, "Can not find out milliseconds=" + j);
            }
            return null;
        }
    }

    private static long getDownloadVideoDuration(long j, long j2) {
        long timeInMillis;
        if (j2 <= j) {
            throw new IllegalArgumentException("Start time >= end time.");
        }
        List<FileInfo> videoInfoList = JSonManager.getInstance().getVideoInfoList();
        long j3 = 0;
        if (videoInfoList != null) {
            for (FileInfo fileInfo : videoInfoList) {
                if (j <= fileInfo.getEndTime().getTimeInMillis() && j2 >= fileInfo.getStartTime().getTimeInMillis()) {
                    if (j >= fileInfo.getStartTime().getTimeInMillis() && j <= fileInfo.getEndTime().getTimeInMillis() && j2 > fileInfo.getEndTime().getTimeInMillis()) {
                        timeInMillis = fileInfo.getEndTime().getTimeInMillis() - j;
                    } else {
                        if (j >= fileInfo.getStartTime().getTimeInMillis() && j2 <= fileInfo.getEndTime().getTimeInMillis()) {
                            return j2 - j;
                        }
                        if (fileInfo.getStartTime().getTimeInMillis() > j && fileInfo.getEndTime().getTimeInMillis() <= j2) {
                            timeInMillis = fileInfo.getDuration() * 1000;
                        } else if (fileInfo.getStartTime().getTimeInMillis() > j && fileInfo.getEndTime().getTimeInMillis() >= j2) {
                            timeInMillis = j2 - fileInfo.getStartTime().getTimeInMillis();
                        }
                    }
                    j3 += timeInMillis;
                }
            }
        } else {
            Dbug.w(tag, "Can not find out thumbnail at " + j);
        }
        return j3;
    }

    public void release() {
        tryToStopDownload();
        this.mOnRecordListener = null;
        this.listener = null;
        this.movWrapper = null;
        PlaybackStream playbackStream = this.playbackStream;
        if (playbackStream != null) {
            playbackStream.unregisterPlayerListener(this.onPlaybackListener);
            this.playbackStream = null;
        }
    }

    private class MyTimeTask extends TimerTask {
        private MyTimeTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (VideoManager.this.playbackStream == null || !VideoManager.isDownloading || VideoManager.this.duration <= 0) {
                return;
            }
            long currentPosition = VideoManager.this.playbackStream.getCurrentPosition();
            if (currentPosition < VideoManager.this.startTime || VideoManager.this.mLastTime == currentPosition) {
                return;
            }
            VideoManager.this.mLastTime = currentPosition;
            VideoManager.access$408(VideoManager.this);
            int i = (VideoManager.this.mTimer * 100) / VideoManager.this.duration;
            if (i < 100) {
                if (VideoManager.this.listener != null) {
                    VideoManager.this.listener.onProgress(i);
                    return;
                }
                return;
            }
            VideoManager.this.tryToStopDownload();
            boolean unused = VideoManager.isDownloading = false;
            ClientManager.getClient().tryToChangePlaybackState(2, new SendResponse() { // from class: com.jieli.stream.dv.running2.util.VideoManager.MyTimeTask.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                }
            });
            if (VideoManager.this.mTimerThread != null) {
                VideoManager.this.mTimerThread.cancel();
                VideoManager.this.mTimerThread = null;
            }
        }
    }
}
