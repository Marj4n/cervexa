package com.generalplus.ffmpegLib;

import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: classes.dex */
public class ffmpegWrapper implements GLSurfaceView.Renderer {
    public static final int CODEC_ID_H264 = 28;
    public static final int CODEC_ID_MJPEG = 8;
    public static final int CODEC_ID_NONE = 0;
    public static final int EXTRACTOR_BUSY = 1;
    public static final int EXTRACTOR_DECODEFAILED = 3;
    public static final int EXTRACTOR_NOSUCHFRAME = 4;
    public static final int EXTRACTOR_OK = 0;
    public static final int EXTRACTOR_READFILEFAILED = 2;
    public static final int FFMPEG_STATUS_PLAYING = 0;
    public static final int FFMPEG_STATUS_SAVESNAPSHOTCOMPLETE = 2;
    public static final int FFMPEG_STATUS_SAVEVIDEOCOMPLETE = 3;
    public static final int FFMPEG_STATUS_STOPPED = 1;
    public static final String LOW_LOADING_TRANSCODE_OPTIONS = "qmin=15;qmax=35;b=400000;g=15;bf=0;refs=2;weightp=simple;level=2.2;x264-params=lookahead-threads=3:subme=4:chroma_qp_offset=0";
    private static final String TAG = "ffmpegWrapper";
    private static ffmpegWrapper m_Instance;
    private static Handler m_NowViewHandler;
    public int width = 0;
    public int height = 0;
    private boolean mIsResize = false;
    private long frameSeq = 0;
    private int viewportOffset = 20;
    private int maxOffset = NNTPReply.SERVICE_DISCONTINUED;
    private int mSurfaceViewScale = 0;

    public enum eDisplayScale {
        E_DisplayScale_Fit,
        E_DisplayScale_Fill,
        E_DisplayScale_Stretch
    }

    public enum eEncodeContainer {
        E_EncodeContainer_MP4,
        E_EncodeContainer_AVI
    }

    public enum eFFMPEG_ERRCODE {
        FFMPEGPLAYER_NOERROR,
        FFMPEGPLAYER_INITMEDIAFAILED,
        FFMPEGPLAYER_MEDIAISPLAYING,
        FFMPEGPLAYER_CREATESAVESTREAMFAILED,
        FFMPEGPLAYER_SAVESNAPSHOTFAILED,
        FFMPEGPLAYER_SAVEVIDEOFAILED
    }

    public enum ePlayerStatus {
        E_PlayerStatus_Stoped,
        E_PlayerStatus_Playing,
        E_PlayerStatus_Stoping
    }

    public static native int naDrawFrame();

    public static native int naExtractFrame(String str, String str2, long j);

    public static native ffDecodeFrame naGetDecodeFrame();

    public static native long naGetDuration();

    public static native long naGetFrameCnt();

    public static native long naGetPosition();

    public static native long naGetRevSizeCnt();

    public static native int naGetStreamCodecID();

    public static native int[] naGetVideoRes();

    public static native int naInitAndPlay(String str, String str2);

    public static native int naInitDrawFrame();

    public static native int naPause();

    public static native int naPlay();

    public static native int naResume();

    public static native int naSaveSnapshot(String str, int i);

    public static native int naSaveVideo(String str, int i);

    public static native int naSeek(long j);

    public static native int naSetCovertDecodeFrameFormat(int i);

    public static native int naSetDebugMessage(boolean z);

    public static native int naSetDecodeOptions(String str);

    public static native int naSetEncodeByLocalTime(boolean z);

    public static native int naSetForceToTranscode(boolean z);

    public static native int naSetRepeat(boolean z);

    public static native int naSetScaleMode(int i);

    public static native int naSetStreaming(boolean z);

    public static native int naSetTransCodeOptions(String str);

    public static native int naSetZoomInRatio(float f);

    public static native int naSetup(int i, int i2);

    public static native int naStatus();

    public static native int naStop();

    public static native int naStopSaveVideo();

    static {
        try {
            Log.i(TAG, "Trying to load ffmpeg.so ...");
            System.loadLibrary("ffmpeg");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Cannot load ffmpeg.so ...");
            e.printStackTrace();
        }
    }

    public ffmpegWrapper() {
        m_Instance = this;
    }

    public static ffmpegWrapper getInstance() {
        return m_Instance;
    }

    public void setSizeUp() {
        this.mIsResize = true;
        this.mSurfaceViewScale++;
    }

    public void setSizeNormal() {
        this.mIsResize = false;
        this.maxOffset = 0;
        this.mSurfaceViewScale = 0;
    }

    public void setSizeDown() {
        this.mIsResize = true;
        this.mSurfaceViewScale--;
    }

    private void changeGLViewport(GL10 gl10) {
        Log.e("changeGLViewport", "Time:" + System.currentTimeMillis() + ",Width:" + this.width + ",Height:" + this.height + ",viewportOffset:" + this.viewportOffset + ",mSurfaceViewScale:" + this.mSurfaceViewScale);
        if (this.width < 1 || this.height < 1) {
            Log.e("changeGLViewport", "ErrorWidth:" + this.width + ",ErrorHeight:" + this.height);
            return;
        }
        long j = this.frameSeq + 1;
        this.frameSeq = j;
        if (j > 1000000) {
            this.frameSeq = 0L;
        }
        if (this.frameSeq % 100 == 0) {
            gl10.glViewport(0, 0, this.width, this.height);
            return;
        }
        int i = this.maxOffset;
        int i2 = this.viewportOffset;
        int i3 = this.mSurfaceViewScale;
        gl10.glViewport((-i) + (i2 * i3), (-i) + (i2 * i3), (this.width - ((i2 * 2) * i3)) + (i * 2), (this.height - ((i2 * 2) * i3)) + (i * 2));
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        Log.e(TAG, "onSurfaceCreated ... ");
        naInitDrawFrame();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        naDrawFrame();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Log.e(TAG, "onSurfaceChanged ... ");
        this.width = i;
        this.height = i2;
        naSetup(i, i2);
    }

    public void SetViewHandler(Handler handler) {
        m_NowViewHandler = handler;
    }

    void StatusChange(int i) {
        if (m_NowViewHandler != null) {
            Message message = new Message();
            message.what = i;
            m_NowViewHandler.sendMessage(message);
        }
    }
}
