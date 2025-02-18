package com.jieli.stream.dv.running2.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.model.MediaInfo;
import com.jieli.lib.dv.control.player.OnDownloadListener;
import com.jieli.lib.dv.control.player.PlaybackStream;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.data.OnRecordStateListener;
import com.jieli.stream.dv.running2.data.VideoRecord;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.io.File;

/* loaded from: classes.dex */
public class DownloadDialog extends BaseDialogFragment implements View.OnClickListener {
    private static final int MAX_PROGRESS = 100;
    private VideoRecord mRecordVideo;
    private PlaybackStream mStreamPlayer;
    private NumberProgressBar pbNumber;
    private int progress;
    private String title;
    private TextView tvContent;
    private TextView tvTitle;
    final String tag = getClass().getSimpleName();
    private int mFileType = 1;
    private boolean isRecordPrepared = false;
    private int mFrameCount = 0;
    private final OnNotifyListener mOnNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.DownloadDialog.2
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(DownloadDialog.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == 1021829664 && topic.equals(Topic.PLAYBACK)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            if (DownloadDialog.this.mStreamPlayer == null) {
                DownloadDialog.this.mStreamPlayer = new PlaybackStream();
            }
            Dbug.i(DownloadDialog.this.tag, "create playback..................");
            DownloadDialog.this.mStreamPlayer.setOnDownloadListener(DownloadDialog.this.onDownloadListener);
            DownloadDialog.this.mStreamPlayer.create(IConstant.RTS_TCP_PORT, ClientManager.getClient().getConnectedIP(), 2);
        }
    };
    private final OnDownloadListener onDownloadListener = new OnDownloadListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.DownloadDialog.3
        @Override // com.jieli.lib.dv.control.player.OnDownloadListener
        public void onStart() {
            Dbug.w(DownloadDialog.this.tag, "start download");
            DownloadDialog.this.mFrameCount = 0;
            DownloadDialog downloadDialog = DownloadDialog.this;
            downloadDialog.mRecordVideo = new VideoRecord(downloadDialog.mStreamPlayer.getCurrentMediaInfo());
            DownloadDialog.this.mRecordVideo.setFileType(DownloadDialog.this.mFileType);
            DownloadDialog.this.mRecordVideo.prepare(new OnRecordStateListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.DownloadDialog.3.1
                @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
                public void onPrepared() {
                    DownloadDialog.this.isRecordPrepared = true;
                }

                @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
                public void onCompletion(String str) {
                    ToastUtil.showToastShort(DownloadDialog.this.getString(R.string.record_success));
                }

                @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
                public void onStop() {
                    Dbug.i(DownloadDialog.this.tag, "Record onStop");
                }

                @Override // com.jieli.stream.dv.running2.data.OnRecordStateListener
                public void onError(String str) {
                    Dbug.e(DownloadDialog.this.tag, "Record error:" + str);
                    if (DownloadDialog.this.mRecordVideo != null) {
                        String currentFilePath = DownloadDialog.this.mRecordVideo.getCurrentFilePath();
                        if (!TextUtils.isEmpty(currentFilePath)) {
                            File file = new File(currentFilePath);
                            if (file.exists() && !file.delete()) {
                                Dbug.w(DownloadDialog.this.tag, "Delete failed:" + file.getAbsolutePath());
                            }
                        }
                    }
                    ToastUtil.showToastShort(DownloadDialog.this.getString(R.string.record_fail));
                    DownloadDialog.this.closeMovWrapper();
                }
            });
        }

        @Override // com.jieli.lib.dv.control.player.OnDownloadListener
        public void onReceived(int i, byte[] bArr) {
            if (DownloadDialog.this.mRecordVideo == null || !DownloadDialog.this.isRecordPrepared || DownloadDialog.this.mRecordVideo.write(i, bArr)) {
                return;
            }
            Dbug.e(DownloadDialog.this.tag, "Write failed");
        }

        @Override // com.jieli.lib.dv.control.player.OnDownloadListener
        public void onProgress(float f) {
            Dbug.w(DownloadDialog.this.tag, "Progress " + f);
            DownloadDialog.this.updateNumberPb((int) (f * 100.0f));
        }

        @Override // com.jieli.lib.dv.control.player.OnDownloadListener
        public void onStop() {
            Dbug.w(DownloadDialog.this.tag, "onStop " + DownloadDialog.this.progress);
            DownloadDialog.this.mFrameCount = 0;
            DownloadDialog.this.stopDownload();
        }

        @Override // com.jieli.lib.dv.control.player.OnDownloadListener
        public void onError(int i, String str) {
            Dbug.e(DownloadDialog.this.tag, "Error: code " + i + ", msg=" + str);
            DownloadDialog.this.mFrameCount = 0;
            DownloadDialog.this.closeMovWrapper();
            DownloadDialog.this.dismissDialog();
        }
    };

    public static DownloadDialog newInstance(FileInfo fileInfo) {
        DownloadDialog downloadDialog = new DownloadDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IConstant.KEY_FILE_INFO, fileInfo);
        downloadDialog.setArguments(bundle);
        return downloadDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_download, viewGroup, false);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        this.tvTitle = (TextView) inflate.findViewById(R.id.dialog_download_title);
        this.tvContent = (TextView) inflate.findViewById(R.id.dialog_download_content);
        this.pbNumber = (NumberProgressBar) inflate.findViewById(R.id.dialog_download_progress_bar);
        ((Button) inflate.findViewById(R.id.dialog_download_cancel_btn)).setOnClickListener(this);
        this.pbNumber.setMax(100);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null || getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        FileInfo fileInfo = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            fileInfo = (FileInfo) arguments.getSerializable(IConstant.KEY_FILE_INFO);
            if (fileInfo == null || TextUtils.isEmpty(fileInfo.getPath())) {
                Dbug.e(this.tag, "File info error");
                return;
            }
            this.mFileType = fileInfo.getType();
            PlaybackStream playbackStream = new PlaybackStream();
            this.mStreamPlayer = playbackStream;
            playbackStream.setDownloadDuration(fileInfo.getDuration());
            Dbug.e(this.tag, "path " + fileInfo.getPath() + ", offset " + fileInfo.getOffset());
            ClientManager.getClient().registerNotifyListener(this.mOnNotifyListener);
            ClientManager.getClient().tryToStartPlayback(fileInfo.getPath(), fileInfo.getOffset(), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.dialog.DownloadDialog.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(DownloadDialog.this.tag, "Send failed");
                    }
                }
            });
        }
        initUI(fileInfo);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = 200;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 4) / 5;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 4) / 5;
        }
        attributes.height = -2;
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        closeMovWrapper();
        ClientManager.getClient().unregisterNotifyListener(this.mOnNotifyListener);
        PlaybackStream playbackStream = this.mStreamPlayer;
        if (playbackStream != null) {
            playbackStream.release();
            this.mStreamPlayer = null;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null || getDialog() == null || view == null || view.getId() != R.id.dialog_download_cancel_btn) {
            return;
        }
        MediaInfo currentMediaInfo = this.mStreamPlayer.getCurrentMediaInfo();
        if (currentMediaInfo != null && this.mFrameCount >= currentMediaInfo.getFrameRate()) {
            stopDownload();
        } else {
            ToastUtil.showToastLong(getString(R.string.dialod_wait));
        }
    }

    private void initUI(FileInfo fileInfo) {
        if (getActivity() == null || getDialog() == null) {
            return;
        }
        if (!TextUtils.isEmpty(this.title)) {
            this.tvTitle.setText(this.title);
        } else {
            this.tvTitle.setText(R.string.download_file);
        }
        if (fileInfo != null && !TextUtils.isEmpty(fileInfo.getPath())) {
            this.tvContent.setText(fileInfo.getPath());
        } else {
            this.tvContent.setText("");
        }
        int i = this.progress;
        if (i > 0) {
            this.pbNumber.setProgress(i);
        }
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setProgress(int i) {
        if (i > 100) {
            i = 100;
        }
        this.progress = i;
    }

    public void updateNumberPb(int i) {
        setProgress(i);
        NumberProgressBar numberProgressBar = this.pbNumber;
        if (numberProgressBar != null) {
            numberProgressBar.setProgress(this.progress);
        }
    }

    public int getProgress() {
        return this.progress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDownload() {
        ClientManager.getClient().tryToChangePlaybackState(2, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.dialog.DownloadDialog.4
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(DownloadDialog.this.tag, "Send failed");
                }
            }
        });
        Dbug.w(this.tag, "stop download");
        closeMovWrapper();
        dismissDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeMovWrapper() {
        VideoRecord videoRecord = this.mRecordVideo;
        if (videoRecord != null) {
            videoRecord.close();
            this.mRecordVideo = null;
        }
        this.isRecordPrepared = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }
}
