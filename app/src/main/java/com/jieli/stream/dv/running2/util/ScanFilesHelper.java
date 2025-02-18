package com.jieli.stream.dv.running2.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;

/* loaded from: classes.dex */
public class ScanFilesHelper implements MediaScannerConnection.MediaScannerConnectionClient {
    private Context mContext;
    private String mFilePath;
    private MediaScannerConnection mMediaScannerConnection;

    public ScanFilesHelper(Context context) {
        this.mContext = context;
        if (this.mMediaScannerConnection == null) {
            this.mMediaScannerConnection = new MediaScannerConnection(context, this);
        }
    }

    private void scanFile(String str) {
        MediaScannerConnection mediaScannerConnection;
        if (TextUtils.isEmpty(str) || (mediaScannerConnection = this.mMediaScannerConnection) == null || !mediaScannerConnection.isConnected()) {
            return;
        }
        Dbug.e("ScanFilesHelper", "scan file absolutePath = " + str);
        File file = new File(str);
        if (file.exists()) {
            if (file.isFile()) {
                this.mMediaScannerConnection.scanFile(file.getAbsolutePath(), null);
                return;
            }
            if (file.listFiles() == null) {
                return;
            }
            for (File file2 : file.listFiles()) {
                if (file2 != null) {
                    scanFile(file2.getAbsolutePath());
                }
            }
        }
    }

    public void updateToDeleteFile(String str) {
        MediaScannerConnection.scanFile(this.mContext, new String[]{str}, null, null);
    }

    public void scanFiles(String str) {
        this.mFilePath = str;
        MediaScannerConnection mediaScannerConnection = this.mMediaScannerConnection;
        if (mediaScannerConnection != null && !mediaScannerConnection.isConnected()) {
            this.mMediaScannerConnection.connect();
            return;
        }
        MediaScannerConnection mediaScannerConnection2 = new MediaScannerConnection(this.mContext, this);
        this.mMediaScannerConnection = mediaScannerConnection2;
        mediaScannerConnection2.connect();
    }

    @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
    public void onMediaScannerConnected() {
        if (TextUtils.isEmpty(this.mFilePath)) {
            return;
        }
        scanFile(this.mFilePath);
    }

    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
    public void onScanCompleted(String str, Uri uri) {
        MediaScannerConnection mediaScannerConnection = this.mMediaScannerConnection;
        if (mediaScannerConnection != null) {
            mediaScannerConnection.disconnect();
        }
    }

    public void release() {
        MediaScannerConnection mediaScannerConnection = this.mMediaScannerConnection;
        if (mediaScannerConnection != null) {
            if (mediaScannerConnection.isConnected()) {
                this.mMediaScannerConnection.disconnect();
            }
            this.mMediaScannerConnection = null;
        }
        if (!TextUtils.isEmpty(this.mFilePath)) {
            this.mFilePath = null;
        }
        if (this.mContext != null) {
            this.mContext = null;
        }
    }
}
