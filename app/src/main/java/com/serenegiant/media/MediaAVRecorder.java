package com.serenegiant.media;

import android.content.Context;
import androidx.documentfile.provider.DocumentFile;
import com.serenegiant.media.IRecorder;
import java.io.IOException;

/* loaded from: classes2.dex */
public class MediaAVRecorder extends AbstractMediaAVRecorder {
    private static final String TAG = MediaAVRecorder.class.getSimpleName();

    public MediaAVRecorder(Context context, IRecorder.RecorderCallback recorderCallback, String str, int i) throws IOException {
        super(context, recorderCallback, (String) null, str, i);
    }

    public MediaAVRecorder(Context context, IRecorder.RecorderCallback recorderCallback, String str, String str2, int i) throws IOException {
        super(context, recorderCallback, str, str2, i);
    }

    public MediaAVRecorder(Context context, IRecorder.RecorderCallback recorderCallback, int i, String str, String str2) throws IOException {
        super(context, recorderCallback, i, str, str2);
    }

    public MediaAVRecorder(Context context, IRecorder.RecorderCallback recorderCallback, DocumentFile documentFile) throws IOException {
        super(context, recorderCallback, documentFile);
    }

    public MediaAVRecorder(Context context, IRecorder.RecorderCallback recorderCallback, String str) throws IOException {
        super(context, recorderCallback, str);
    }

    @Override // com.serenegiant.media.AbstractMediaAVRecorder
    protected void setupMuxer(int i) throws IOException {
        setMuxer(createMuxer(i));
    }

    @Override // com.serenegiant.media.AbstractMediaAVRecorder
    protected void setupMuxer(String str) throws IOException {
        setMuxer(createMuxer(str));
    }

    @Override // com.serenegiant.media.AbstractMediaAVRecorder
    protected void setupMuxer(Context context, DocumentFile documentFile) throws IOException {
        setMuxer(createMuxer(context, documentFile));
    }
}
