package com.jieli.stream.dv.running2.audio;

import android.os.Environment;
import android.text.TextUtils;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.File;

/* loaded from: classes.dex */
public class AudioConfig {
    private int audioInput = 1;
    private int audioSampleRate = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
    private int channelConfig = 16;
    private int audioFormat = 2;
    private int outputChannel = 4;

    public int getAudioInputWay() {
        return this.audioInput;
    }

    public void setAudioInputWay(int i) {
        this.audioInput = i;
    }

    public int getAudioSampleRate() {
        return this.audioSampleRate;
    }

    public void setAudioSampleRate(int i) {
        this.audioSampleRate = i;
    }

    public int getChannelConfig() {
        return this.channelConfig;
    }

    public void setChannelConfig(int i) {
        this.channelConfig = i;
    }

    public int getOutputChannel() {
        return this.outputChannel;
    }

    public void setOutputChannel(int i) {
        this.outputChannel = i;
    }

    public int getAudioFormat() {
        return this.audioFormat;
    }

    public void setAudioFormat(int i) {
        this.audioFormat = i;
    }

    public static boolean isSdcardExit() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getRawFilePath() {
        if (!isSdcardExit()) {
            return "";
        }
        MainApplication application = MainApplication.getApplication();
        return AppUtils.splicingFilePath(application.getAppName(), application.getUUID(), application.getCameraDir(), IConstant.DIR_RECORD) + File.separator + IConstant.AUD_DEFAULT_NAME;
    }

    public static long getFileSize(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        File file = new File(str);
        if (file.exists()) {
            return file.length();
        }
        return -1L;
    }
}
