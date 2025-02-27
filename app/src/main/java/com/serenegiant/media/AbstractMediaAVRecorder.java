package com.serenegiant.media;

import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.media.IRecorder;
import com.serenegiant.utils.FileUtils;
import com.serenegiant.utils.SAFUtils;
import com.serenegiant.utils.StorageInfo;
import com.serenegiant.utils.UriHelper;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class AbstractMediaAVRecorder extends Recorder {
    protected DocumentFile mOutputFile;
    protected String mOutputPath;
    protected final int mSaveTreeId;
    protected final WeakReference<Context> mWeakContext;

    protected abstract void setupMuxer(int i) throws IOException;

    protected abstract void setupMuxer(Context context, DocumentFile documentFile) throws IOException;

    protected abstract void setupMuxer(String str) throws IOException;

    public AbstractMediaAVRecorder(Context context, RecorderCallback recorderCallback, String str, int i) throws IOException {
        this(context, recorderCallback, (String) null, str, i);
    }

    public AbstractMediaAVRecorder(Context context, RecorderCallback recorderCallback, String str, String str2, int i) throws IOException {
        super(recorderCallback);
        String str3;
        this.mWeakContext = new WeakReference<>(context);
        this.mSaveTreeId = i;
        str2 = TextUtils.isEmpty(str2) ? UVCCameraHelper.SUFFIX_MP4 : str2;
        if (i > 0 && SAFUtils.hasStorageAccess(context, i)) {
            this.mOutputPath = FileUtils.getCaptureFile(context, Environment.DIRECTORY_MOVIES, str, str2, i).toString();
            StringBuilder sb = new StringBuilder();
            if (TextUtils.isEmpty(str)) {
                str3 = FileUtils.getDateTimeString();
            } else {
                str3 = str + FileUtils.getDateTimeString();
            }
            sb.append(str3);
            sb.append(str2);
            setupMuxer(SAFUtils.createStorageFileFD(context, i, "*/*", sb.toString()));
            return;
        }
        try {
            String file = FileUtils.getCaptureFile(context, Environment.DIRECTORY_MOVIES, str, str2, 0).toString();
            this.mOutputPath = file;
            setupMuxer(file);
        } catch (Exception unused) {
            throw new IOException("This app has no permission of writing external storage");
        }
    }

    public AbstractMediaAVRecorder(Context context, RecorderCallback recorderCallback, int i, String str, String str2) throws IOException {
        super(recorderCallback);
        DocumentFile storageFile;
        this.mWeakContext = new WeakReference<>(context);
        this.mSaveTreeId = i;
        if (i > 0 && SAFUtils.hasStorageAccess(context, i) && (storageFile = SAFUtils.getStorageFile(context, i, str, "*/*", str2)) != null) {
            this.mOutputPath = UriHelper.getPath(context, storageFile.getUri());
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(storageFile.getUri(), "rw");
            try {
                if (openFileDescriptor != null) {
                    setupMuxer(openFileDescriptor.getFd());
                    return;
                }
                throw new IOException("could not create ParcelFileDescriptor");
            } catch (Exception e) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                throw e;
            }
        }
        throw new IOException("path not found/can't write");
    }

    public AbstractMediaAVRecorder(Context context, RecorderCallback recorderCallback, DocumentFile documentFile) throws IOException {
        super(recorderCallback);
        this.mWeakContext = new WeakReference<>(context);
        this.mSaveTreeId = 0;
        this.mOutputFile = documentFile;
        this.mOutputPath = UriHelper.getPath(context, documentFile.getUri());
        setupMuxer(context, documentFile);
    }

    public AbstractMediaAVRecorder(Context context, RecorderCallback recorderCallback, String str) throws IOException {
        super(recorderCallback);
        this.mWeakContext = new WeakReference<>(context);
        this.mSaveTreeId = 0;
        this.mOutputPath = str;
        if (TextUtils.isEmpty(str)) {
            try {
                this.mOutputPath = FileUtils.getCaptureFile(context, Environment.DIRECTORY_MOVIES, null, UVCCameraHelper.SUFFIX_MP4, 0).toString();
            } catch (Exception unused) {
                throw new IOException("This app has no permission of writing external storage");
            }
        }
        setupMuxer(this.mOutputPath);
    }

    @Override // com.serenegiant.media.IRecorder
    public String getOutputPath() {
        return this.mOutputPath;
    }

    @Override // com.serenegiant.media.IRecorder
    public DocumentFile getOutputFile() {
        return this.mOutputFile;
    }

    @Override // com.serenegiant.media.Recorder
    protected boolean check() {
        Context context = this.mWeakContext.get();
        DocumentFile documentFile = this.mOutputFile;
        StorageInfo storageInfo = documentFile != null ? SAFUtils.getStorageInfo(context, documentFile) : null;
        return (storageInfo == null || storageInfo.totalBytes == 0) ? context == null || (this.mOutputFile == null && !FileUtils.checkFreeSpace(context, VideoConfig.maxDuration, this.mStartTime, this.mSaveTreeId)) : ((float) storageInfo.freeBytes) / ((float) storageInfo.totalBytes) < FileUtils.FREE_RATIO || ((float) storageInfo.freeBytes) < FileUtils.FREE_SIZE;
    }

    protected Context getContext() {
        return this.mWeakContext.get();
    }
}
