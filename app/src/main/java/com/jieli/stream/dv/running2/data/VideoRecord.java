package com.jieli.stream.dv.running2.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.jieli.lib.dv.control.model.MediaInfo;
import com.jieli.lib.dv.control.player.AviWrapper;
import com.jieli.lib.dv.control.player.MovWrapper;
import com.jieli.lib.dv.control.player.OnAviWrapperListener;
import com.jieli.lib.dv.control.player.OnRecordListener;
import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.File;

/* loaded from: classes.dex */
public class VideoRecord {
    private static final long DEFAULT_VIDEO_SIZE = 62914560;
    private static final long MIN_STORAGE_LIMIT = 209715200;
    private AviWrapper mAviWrapper;
    private String mCurrentFilePath;
    private MediaInfo mMediaInfo;
    String path;
    private final String tag = getClass().getSimpleName();
    private MovWrapper mMovWrapper = null;
    private OnRecordStateListener onStreamRecordListener = null;
    private int fileType = 1;
    private boolean isLoopRecording = false;
    private final OnRecordListener onRecordListener = new OnRecordListener() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.5
        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onStateChanged(int i, final String str) {
            if (i == 1) {
                VideoRecord.this.mCurrentFilePath = str;
            } else if (i == 2) {
                ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VideoRecord.this.onStreamRecordListener != null) {
                            VideoRecord.this.onStreamRecordListener.onCompletion(str);
                        }
                    }
                });
            } else {
                if (i != 3) {
                    return;
                }
                ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VideoRecord.this.onStreamRecordListener != null) {
                            VideoRecord.this.onStreamRecordListener.onStop();
                        }
                    }
                });
            }
        }

        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onError(int i, String str) {
            Dbug.e(VideoRecord.this.tag, "Code " + i + ", msg:" + str);
            VideoRecord.this.dispatchErrorMessage(str);
        }
    };
    private final OnAviWrapperListener onAviWrapperListener = new OnAviWrapperListener() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.6
        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onError(int i, String str) {
            Dbug.e(VideoRecord.this.tag, "Code " + i + ", msg:" + str);
            VideoRecord.this.dispatchErrorMessage(str);
        }

        @Override // com.jieli.lib.dv.control.player.OnRecordListener
        public void onStateChanged(int i, final String str) {
            if (i == 1) {
                VideoRecord.this.mCurrentFilePath = str;
            } else if (i == 2) {
                ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VideoRecord.this.onStreamRecordListener != null) {
                            VideoRecord.this.onStreamRecordListener.onCompletion(str);
                        }
                    }
                });
            } else {
                if (i != 3) {
                    return;
                }
                ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VideoRecord.this.onStreamRecordListener != null) {
                            VideoRecord.this.onStreamRecordListener.onStop();
                        }
                    }
                });
            }
        }
    };
    private String outputDirectory = AppUtils.splicingFilePath("MergeCamera", "Media", "Video", "");

    public VideoRecord() {
        this.mMediaInfo = null;
        this.mMediaInfo = null;
    }

    public VideoRecord(MediaInfo mediaInfo) {
        this.mMediaInfo = null;
        this.mMediaInfo = mediaInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchErrorMessage(final String str) {
        ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.1
            @Override // java.lang.Runnable
            public void run() {
                if (VideoRecord.this.onStreamRecordListener != null) {
                    VideoRecord.this.onStreamRecordListener.onError(str);
                }
            }
        });
    }

    public void setFileType(int i) {
        this.fileType = i;
    }

    public void prepare(boolean z, OnRecordStateListener onRecordStateListener) {
        boolean startMovWrapper;
        this.isLoopRecording = z;
        this.onStreamRecordListener = onRecordStateListener;
        MediaInfo mediaInfo = this.mMediaInfo;
        if (mediaInfo != null) {
            String path = mediaInfo.getPath();
            if (TextUtils.isEmpty(path)) {
                Dbug.e(this.tag, "Filename is null");
                return;
            } else if (path.endsWith("AVI") || path.endsWith("avi")) {
                startMovWrapper = startAviWrapper();
            } else {
                startMovWrapper = startMovWrapper();
            }
        } else if (MainApplication.getApplication().getDeviceDesc().getVideoType() == 0) {
            startMovWrapper = startAviWrapper();
        } else {
            startMovWrapper = startMovWrapper();
        }
        if (startMovWrapper) {
            ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.2
                @Override // java.lang.Runnable
                public void run() {
                    if (VideoRecord.this.onStreamRecordListener != null) {
                        VideoRecord.this.onStreamRecordListener.onPrepared();
                    }
                }
            });
        }
    }

    public void prepare(OnRecordStateListener onRecordStateListener) {
        prepare(false, onRecordStateListener);
    }

    private boolean startMovWrapper() {
        long availableExternalMemorySize = AppUtils.getAvailableExternalMemorySize();
        if (availableExternalMemorySize < MIN_STORAGE_LIMIT) {
            Dbug.e(this.tag, "Not enough storage space");
            dispatchErrorMessage("Not enough storage space");
            return false;
        }
        AviWrapper aviWrapper = this.mAviWrapper;
        if (aviWrapper != null) {
            if (aviWrapper.isRecording()) {
                this.mAviWrapper.stopRecording();
            }
            this.mAviWrapper = null;
        }
        MovWrapper movWrapper = this.mMovWrapper;
        if (movWrapper != null) {
            movWrapper.close();
            this.mMovWrapper = null;
        }
        int i = availableExternalMemorySize / DEFAULT_VIDEO_SIZE > 35 ? 30 : ((int) r0) - 5;
        MovWrapper movWrapper2 = new MovWrapper();
        this.mMovWrapper = movWrapper2;
        movWrapper2.setVideoDuration(i);
        this.mMovWrapper.setOnRecordListener(this.onRecordListener);
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (2 == deviceSettingInfo.getCameraType()) {
            MediaInfo mediaInfo = this.mMediaInfo;
            if (mediaInfo == null) {
                if (!this.mMovWrapper.setFrameRate(deviceSettingInfo.getRearRate())) {
                    Dbug.e(this.tag, "Set frame rate failed");
                }
                if (!this.mMovWrapper.setAudioTrack(deviceSettingInfo.getRearSampleRate(), 1, 16)) {
                    Dbug.e(this.tag, "Set audio track failed");
                }
            } else {
                if (!this.mMovWrapper.setFrameRate(mediaInfo.getFrameRate())) {
                    Dbug.e(this.tag, "Set frame rate failed");
                }
                if (!this.mMovWrapper.setAudioTrack(this.mMediaInfo.getSampleRate(), 1, 16)) {
                    Dbug.e(this.tag, "Set audio track failed");
                }
            }
        } else {
            MediaInfo mediaInfo2 = this.mMediaInfo;
            if (mediaInfo2 == null) {
                if (!this.mMovWrapper.setFrameRate(deviceSettingInfo.getFrontRate())) {
                    Dbug.e(this.tag, "Set frame rate failed");
                }
                if (!this.mMovWrapper.setAudioTrack(deviceSettingInfo.getFrontSampleRate(), 1, 16)) {
                    Dbug.e(this.tag, "Set audio track failed");
                }
            } else {
                if (!this.mMovWrapper.setFrameRate(mediaInfo2.getFrameRate())) {
                    Dbug.e(this.tag, "Set frame rate failed");
                }
                if (!this.mMovWrapper.setAudioTrack(this.mMediaInfo.getSampleRate(), 1, 16)) {
                    Dbug.e(this.tag, "Set audio track failed");
                }
            }
        }
        int i2 = this.fileType;
        String str = AppUtils.splicingFilePath("MergeCamera", "Media", "Video", "") + File.separator + AppUtils.createFilenameWidthTime(this.fileType, ".mov");
        if (TextUtils.isEmpty(str)) {
            Dbug.e(this.tag, "Output path is incorrect");
            dispatchErrorMessage("Output path is incorrect");
            return false;
        }
        Dbug.i(this.tag, "output path " + str);
        this.mCurrentFilePath = str;
        if (this.mMovWrapper.create(str)) {
            return true;
        }
        Dbug.e(this.tag, "Create MOV wrapper failed");
        dispatchErrorMessage("Create MOV wrapper failed");
        return false;
    }

    public String thumbPath() {
        return this.path.replace("avi", "jpg");
    }

    private boolean startAviWrapper() {
        int frameRate;
        if (AppUtils.getAvailableExternalMemorySize() < MIN_STORAGE_LIMIT) {
            Dbug.e(this.tag, "Not enough storage space");
            dispatchErrorMessage("Not enough storage space");
            return false;
        }
        MovWrapper movWrapper = this.mMovWrapper;
        if (movWrapper != null) {
            movWrapper.close();
            this.mMovWrapper = null;
        }
        AviWrapper aviWrapper = this.mAviWrapper;
        if (aviWrapper != null) {
            if (aviWrapper.isRecording()) {
                this.mAviWrapper.stopRecording();
            }
            this.mAviWrapper = null;
        }
        MainApplication.getApplication();
        int i = this.fileType;
        this.path = AppUtils.createFilenameWidthTime(this.fileType, ".avi");
        String str = this.outputDirectory + File.separator + this.path;
        if (TextUtils.isEmpty(str)) {
            Dbug.e(this.tag, "Output path is incorrect");
            dispatchErrorMessage("Output path is incorrect");
            return false;
        }
        Dbug.i(this.tag, "Output path " + str);
        this.mCurrentFilePath = str;
        AviWrapper aviWrapper2 = new AviWrapper();
        this.mAviWrapper = aviWrapper2;
        if (this.isLoopRecording) {
            if (!aviWrapper2.create(this.outputDirectory)) {
                dispatchErrorMessage("Create failed");
                return false;
            }
        } else {
            if (!aviWrapper2.create()) {
                dispatchErrorMessage("Create failed");
                return false;
            }
            this.mAviWrapper.setPath(str);
        }
        this.mAviWrapper.setVideoDuration(300L);
        this.mAviWrapper.setOnRecordListener(this.onAviWrapperListener);
        MediaInfo mediaInfo = this.mMediaInfo;
        if (mediaInfo == null) {
            this.mAviWrapper.setAudioTrack(IConstant.AUDIO_SAMPLE_RATE_DEFAULT, 1, 16);
        } else {
            this.mAviWrapper.setAudioTrack(mediaInfo.getSampleRate(), 1, 16);
        }
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (2 == deviceSettingInfo.getCameraType()) {
            MediaInfo mediaInfo2 = this.mMediaInfo;
            if (mediaInfo2 == null) {
                frameRate = deviceSettingInfo.getRearRate();
            } else {
                frameRate = mediaInfo2.getFrameRate();
            }
        } else {
            MediaInfo mediaInfo3 = this.mMediaInfo;
            if (mediaInfo3 == null) {
                frameRate = deviceSettingInfo.getFrontRate();
            } else {
                frameRate = mediaInfo3.getFrameRate();
            }
        }
        int[] rtsResolution = AppUtils.getRtsResolution(AppUtils.getStreamResolutionLevel());
        int i2 = rtsResolution[0];
        int i3 = rtsResolution[1];
        if (frameRate > 0 && i2 > 0 && i3 > 0) {
            this.mAviWrapper.configureVideo(frameRate, i2, i3);
            this.mAviWrapper.startRecording();
            return true;
        }
        dispatchErrorMessage("params is incorrect");
        return false;
    }

    public synchronized boolean write(int i, byte[] bArr) {
        boolean write;
        if (this.mMovWrapper == null && this.mAviWrapper == null) {
            write = false;
        } else if (this.mAviWrapper != null) {
            write = this.mAviWrapper.write(i, bArr);
        } else {
            write = this.mMovWrapper.write(i, bArr);
        }
        if (!write) {
            Dbug.w(this.tag, "write data failed.");
            close();
        }
        return write;
    }

    public static ContentValues getVideoContentValues(Context context, File file, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", file.getName());
        contentValues.put("_display_name", file.getName());
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("date_modified", Long.valueOf(j));
        contentValues.put("date_added", Long.valueOf(j));
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("_size", Long.valueOf(file.length()));
        return contentValues;
    }

    public void close() {
        if (this.mAviWrapper == null && this.mMovWrapper == null) {
            Dbug.w(this.tag, "mAviWrapper or mMovWrapper not init.");
        } else {
            AviWrapper aviWrapper = this.mAviWrapper;
            if (aviWrapper != null) {
                if (aviWrapper.isRecording()) {
                    this.mAviWrapper.stopRecording();
                }
                ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VideoRecord.this.onStreamRecordListener != null) {
                            VideoRecord.this.onStreamRecordListener.onStop();
                        }
                        VideoRecord.this.onStreamRecordListener = null;
                        try {
                            ClientContext.get().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + VideoRecord.this.mCurrentFilePath)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.mAviWrapper.destroy();
                this.mAviWrapper = null;
            } else {
                if (this.mMovWrapper.close()) {
                    ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.data.VideoRecord.4
                        @Override // java.lang.Runnable
                        public void run() {
                            if (VideoRecord.this.onStreamRecordListener != null) {
                                VideoRecord.this.onStreamRecordListener.onStop();
                            }
                            VideoRecord.this.onStreamRecordListener = null;
                        }
                    });
                } else {
                    Dbug.e(this.tag, "Mov close failed");
                }
                this.mMovWrapper = null;
            }
        }
        this.mCurrentFilePath = null;
        this.isLoopRecording = false;
    }

    public String getCurrentFilePath() {
        return this.mCurrentFilePath;
    }
}
