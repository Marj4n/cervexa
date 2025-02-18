package com.jieli.lib.dv.control.player;

import com.jieli.lib.dv.control.gps.GpsParser;
import com.jieli.lib.dv.control.gps.OnGpsListener;
import com.jieli.lib.dv.control.model.MediaInfo;
import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.lib.dv.control.utils.Dlog;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public final class PlaybackStream extends Stream {
    private float mDownloadDuration;
    private GpsParser mGpsParser;
    private long mNativeContext;
    private OnBufferingListener mOnBufferingListener;
    private OnDownloadListener mOnDownloadListener;
    private OnProgressListener mOnProgressListener;
    private HashSet<OnPlaybackListener> mPlaybackListeners;
    private int mProgress;
    static final String tag = PlaybackStream.class.getSimpleName();
    private static int DEFAULT_CHANNEL = 0;
    private final AtomicLong mCurrentTimeAtomic = new AtomicLong();
    private int mCurrentMode = 1;
    private MediaInfo mMediaInfo = null;
    private int mCurrentFrames = 0;
    private long mFileStartTime = 0;

    public static final class BufferingState {
        public static final int END = 2;
        protected static final int NONE = 3;
        public static final int START = 1;
    }

    public static final class Mode {
        public static final int CLIP = 3;
        public static final int DOWNLOAD = 2;
        public static final int PLAYBACK = 1;
    }

    private native boolean nativeCloseClient();

    private native boolean nativeConfigureRtp(int[] iArr, int[] iArr2, String str);

    private native boolean nativeCreateClient(int i, String str, int i2);

    private native boolean nativeInit();

    private native boolean nativeIsBuffering();

    private native boolean nativeIsPausing();

    private native boolean nativeIsPlaying();

    private native boolean nativePause();

    private native boolean nativePlay();

    private native void nativeRelease();

    private native boolean nativeSetMode(int i);

    private native void nativeSetMute(boolean z);

    private native void nativeSetQueueSize(int i);

    private native boolean nativeSetSoTimeout(int i);

    private native boolean nativeToggleBuffer(int i);

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean create(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean isReceiving() {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void registerStreamListener(IPlayerListener iPlayerListener) {
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setFrameRate(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setResolution(int i, int i2) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setSampleRate(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void unregisterStreamListener(IPlayerListener iPlayerListener) {
    }

    public PlaybackStream() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        nativeInit();
        this.mPlaybackListeners = new HashSet<>();
        this.mGpsParser = new GpsParser();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean create(int i, String str) {
        return create(i, str, false);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    @Deprecated
    public boolean create(int i, String str, boolean z) {
        return create(i, str, z ? 2 : 1);
    }

    public boolean create(int i, String str, int i2) {
        GpsParser gpsParser = this.mGpsParser;
        if (gpsParser != null) {
            gpsParser.create();
        }
        this.mCurrentMode = i2;
        return nativeCreateClient(i, str, i2);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    public boolean close() {
        synchronized (this) {
            if (this.mNativeContext == 0) {
                return false;
            }
            return nativeCloseClient();
        }
    }

    @Deprecated
    public boolean destroy() {
        return release();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public void onError(final int i, final String str) {
        int i2 = this.mCurrentMode;
        if (2 == i2 || 3 == i2) {
            if (this.mOnDownloadListener != null) {
                ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (PlaybackStream.this.mOnDownloadListener != null) {
                            PlaybackStream.this.mOnDownloadListener.onError(i, str);
                        }
                    }
                });
                return;
            }
            return;
        }
        HashSet<OnPlaybackListener> hashSet = this.mPlaybackListeners;
        if (hashSet != null) {
            Iterator<OnPlaybackListener> it = hashSet.iterator();
            while (it.hasNext()) {
                final OnPlaybackListener next = it.next();
                ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPlayerListener iPlayerListener = next;
                        if (iPlayerListener != null) {
                            iPlayerListener.onError(i, str);
                        }
                    }
                });
            }
        }
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public void onStateChanged(final int i) {
        int i2 = this.mCurrentMode;
        if (i2 == 2 || i2 == 3) {
            if (i != 6) {
                return;
            }
            this.mProgress = 0;
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.3
                @Override // java.lang.Runnable
                public void run() {
                    if (PlaybackStream.this.mOnDownloadListener != null) {
                        PlaybackStream.this.mOnDownloadListener.onStop();
                    }
                }
            });
            return;
        }
        HashSet<OnPlaybackListener> hashSet = this.mPlaybackListeners;
        if (hashSet != null) {
            Iterator<OnPlaybackListener> it = hashSet.iterator();
            while (it.hasNext()) {
                final OnPlaybackListener next = it.next();
                ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.4
                    @Override // java.lang.Runnable
                    public void run() {
                        IPlayerListener iPlayerListener = next;
                        if (iPlayerListener != null) {
                            iPlayerListener.onStateChanged(i);
                        }
                    }
                });
            }
        }
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected void onFrameReceived(int i, byte[] bArr, long j, long j2) {
        GpsParser gpsParser;
        HashSet<OnPlaybackListener> hashSet = this.mPlaybackListeners;
        if (hashSet == null) {
            return;
        }
        int i2 = this.mCurrentMode;
        if (i2 == 2 || 3 == i2) {
            OnDownloadListener onDownloadListener = this.mOnDownloadListener;
            if (onDownloadListener != null) {
                onDownloadListener.onReceived(i, bArr);
                return;
            }
            return;
        }
        if (i == 1) {
            Iterator<OnPlaybackListener> it = hashSet.iterator();
            while (it.hasNext()) {
                it.next().onAudio(i, DEFAULT_CHANNEL, bArr, j, j2);
            }
        } else if (i == 2 || i == 3) {
            Iterator<OnPlaybackListener> it2 = this.mPlaybackListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onVideo(i, DEFAULT_CHANNEL, bArr, j, j2);
            }
        } else if (i == 8 && (gpsParser = this.mGpsParser) != null) {
            gpsParser.parse(bArr);
        }
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean release() {
        synchronized (this) {
            if (this.mPlaybackListeners != null) {
                this.mPlaybackListeners.clear();
                this.mPlaybackListeners = null;
            }
            this.mOnDownloadListener = null;
            this.mOnBufferingListener = null;
            this.mDownloadDuration = 0.0f;
            if (this.mGpsParser != null) {
                this.mGpsParser.close();
            }
            nativeRelease();
            this.mNativeContext = 0L;
        }
        return true;
    }

    protected void finalize() {
        nativeRelease();
        this.mNativeContext = 0L;
    }

    public void registerPlayerListener(OnPlaybackListener onPlaybackListener) {
        HashSet<OnPlaybackListener> hashSet;
        if (onPlaybackListener == null || (hashSet = this.mPlaybackListeners) == null || hashSet.contains(onPlaybackListener)) {
            return;
        }
        this.mPlaybackListeners.add(onPlaybackListener);
    }

    public void unregisterPlayerListener(OnPlaybackListener onPlaybackListener) {
        HashSet<OnPlaybackListener> hashSet = this.mPlaybackListeners;
        if (hashSet != null) {
            hashSet.remove(onPlaybackListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float round(float f) {
        return Math.round(f * 100.0f) / 100.0f;
    }

    protected void onTimeUpdate(int i, int i2, int i3, int i4, int i5, int i6) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i + 2015, i2 - 1, i3, i4, i5, i6);
        this.mCurrentTimeAtomic.set(calendar.getTimeInMillis());
        int i7 = this.mCurrentMode;
        if (i7 != 2 && 3 != i7) {
            if (this.mCurrentFrames == 0) {
                this.mFileStartTime = calendar.getTimeInMillis() / 1000;
            }
            OnProgressListener onProgressListener = this.mOnProgressListener;
            if (onProgressListener != null) {
                this.mCurrentFrames++;
                onProgressListener.onProgress((int) (((calendar.getTimeInMillis() / 1000) - this.mFileStartTime) + 1));
                return;
            }
            return;
        }
        if (this.mDownloadDuration <= 0.0f) {
            throw new IllegalArgumentException("Download duration " + this.mDownloadDuration);
        }
        if (this.mOnDownloadListener != null) {
            this.mProgress++;
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.5
                @Override // java.lang.Runnable
                public void run() {
                    if (PlaybackStream.this.mOnDownloadListener == null) {
                        return;
                    }
                    PlaybackStream.this.mOnDownloadListener.onTime(PlaybackStream.this.mCurrentTimeAtomic.get());
                    float round = PlaybackStream.round(PlaybackStream.this.mProgress / PlaybackStream.this.mDownloadDuration);
                    Dlog.i(PlaybackStream.tag, "==duration " + PlaybackStream.this.mDownloadDuration + ", progress " + round + ", mProgress=" + PlaybackStream.this.mProgress);
                    if (PlaybackStream.this.mProgress <= 0 || PlaybackStream.this.mProgress != ((int) PlaybackStream.this.mDownloadDuration)) {
                        if (PlaybackStream.this.mProgress <= PlaybackStream.this.mDownloadDuration) {
                            PlaybackStream.this.mOnDownloadListener.onProgress(round);
                        }
                    } else {
                        PlaybackStream.this.mOnDownloadListener.onProgress(round);
                        if (3 == PlaybackStream.this.mCurrentMode) {
                            PlaybackStream.this.mOnDownloadListener.onStop();
                            PlaybackStream.this.mProgress = 0;
                        }
                    }
                }
            });
        }
    }

    public void setDownloadDuration(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("duration " + i);
        }
        this.mDownloadDuration = i;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.mOnDownloadListener = onDownloadListener;
    }

    public void setOnBufferingListener(OnBufferingListener onBufferingListener) {
        this.mOnBufferingListener = onBufferingListener;
    }

    protected void onBuffering(final int i) {
        if (this.mOnBufferingListener != null) {
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.6
                @Override // java.lang.Runnable
                public void run() {
                    if (PlaybackStream.this.mOnBufferingListener != null) {
                        PlaybackStream.this.mOnBufferingListener.onBuffering(i);
                    }
                }
            });
        }
    }

    public long getCurrentPosition() {
        return this.mCurrentTimeAtomic.get();
    }

    protected void onMediaMetaUpdate(int i, int i2, int i3, int i4, int i5, String str) {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setWidth(i);
        mediaInfo.setHeight(i2);
        mediaInfo.setPath(str);
        mediaInfo.setFrameRate(i3);
        mediaInfo.setSampleRate(i4);
        mediaInfo.setDuration(i5);
        this.mMediaInfo = mediaInfo;
        int i6 = this.mCurrentMode;
        if (2 == i6 || 3 == i6) {
            if (this.mOnDownloadListener != null) {
                ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.PlaybackStream.7
                    @Override // java.lang.Runnable
                    public void run() {
                        if (PlaybackStream.this.mOnDownloadListener != null) {
                            PlaybackStream.this.mOnDownloadListener.onStart();
                        }
                    }
                });
                return;
            }
            return;
        }
        HashSet<OnPlaybackListener> hashSet = this.mPlaybackListeners;
        if (hashSet != null) {
            Iterator<OnPlaybackListener> it = hashSet.iterator();
            while (it.hasNext()) {
                it.next().onUpdate(mediaInfo);
            }
        }
        OnProgressListener onProgressListener = this.mOnProgressListener;
        if (onProgressListener != null) {
            onProgressListener.onStart();
        }
    }

    public MediaInfo getCurrentMediaInfo() {
        return this.mMediaInfo;
    }

    protected void onFileEnd() {
        onStateChanged(6);
    }

    protected void onPlayFileEnd() {
        this.mCurrentFrames = 0;
        OnProgressListener onProgressListener = this.mOnProgressListener;
        if (onProgressListener != null) {
            onProgressListener.onFinish();
        }
    }

    public boolean playStream() {
        return nativePlay();
    }

    public boolean pauseStream() {
        return nativePause();
    }

    public boolean isStreamReceiving() {
        return nativeIsPlaying();
    }

    public boolean isStreamPausing() {
        return nativeIsPausing();
    }

    public boolean toggleBuffering(boolean z) {
        if (z) {
            return nativeToggleBuffer(2);
        }
        return nativeToggleBuffer(3);
    }

    public boolean isBuffering() {
        return nativeIsBuffering();
    }

    @Deprecated
    protected boolean setStreamMode(int i) {
        this.mCurrentMode = i;
        return nativeSetMode(i);
    }

    public int getStreamMode() {
        return this.mCurrentMode;
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean setSoTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        }
        return nativeSetSoTimeout(i);
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }

    public void setOnGpsListener(OnGpsListener onGpsListener) {
        GpsParser gpsParser = this.mGpsParser;
        if (gpsParser != null) {
            gpsParser.setOnGpsListener(onGpsListener);
        }
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean configure(int[] iArr, int[] iArr2) {
        return nativeConfigureRtp(iArr, iArr2, PlayerConstants.RTP_ADDRESS);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean configure(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Can not be negative or zero");
        }
        return configure(new int[]{i}, new int[]{i2});
    }

    private void setQueueSize(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Queue size error:" + i);
        }
        nativeSetQueueSize(i);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public void setMute(boolean z) {
        nativeSetMute(z);
    }
}
