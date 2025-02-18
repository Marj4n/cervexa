package com.jieli.stream.dv.running2.audio;

import android.media.AudioRecord;

/* loaded from: classes.dex */
public class AudioRecordManager {
    public static final int ERR_AUDIO_IS_RECORDING = -1;
    public static final int ERR_SD_CARD_NOT_EXIST = -2;
    public static final int START_RECORD_OK = 1;
    private static AudioRecordManager instance;
    private AudioRecord audioRecord;
    private RecorderListener listener;
    private String tag = getClass().getSimpleName();
    private int bufferSizeInBytes = 0;
    private boolean isRecord = false;
    private int voiceLevel = 1;
    private AudioConfig config = new AudioConfig();

    public interface RecorderListener {
        void onRecord(byte[] bArr, int i);
    }

    public void setRecordListener(RecorderListener recorderListener) {
        this.listener = recorderListener;
    }

    public AudioRecordManager() {
        createAudioRecord();
    }

    public static AudioRecordManager getInstance() {
        if (instance == null) {
            synchronized (AudioRecordManager.class) {
                if (instance == null) {
                    instance = new AudioRecordManager();
                }
            }
        }
        return instance;
    }

    private void createAudioRecord() {
        AudioConfig audioConfig = this.config;
        if (audioConfig == null) {
            return;
        }
        try {
            int minBufferSize = AudioRecord.getMinBufferSize(audioConfig.getAudioSampleRate(), this.config.getChannelConfig(), this.config.getAudioFormat());
            this.bufferSizeInBytes = minBufferSize;
            if (minBufferSize != -2) {
                this.audioRecord = new AudioRecord(this.config.getAudioInputWay(), this.config.getAudioSampleRate(), this.config.getChannelConfig(), this.config.getAudioFormat(), this.bufferSizeInBytes);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public boolean isRecord() {
        return this.isRecord;
    }

    public int startRecord() {
        if (this.isRecord) {
            return -1;
        }
        if (this.audioRecord == null) {
            createAudioRecord();
        }
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.startRecording();
            this.isRecord = true;
        }
        new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.audio.AudioRecordManager.1
            @Override // java.lang.Runnable
            public void run() {
                AudioRecordManager.this.readData();
            }
        }).start();
        return 1;
    }

    public void stopRecord() {
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            this.isRecord = false;
            if (audioRecord.getState() == 3) {
                this.audioRecord.stop();
            }
            this.audioRecord.release();
            this.audioRecord = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readData() {
        int i = this.bufferSizeInBytes;
        byte[] bArr = new byte[i];
        while (this.isRecord) {
            int read = this.audioRecord.read(bArr, 0, this.bufferSizeInBytes);
            if (-3 != read) {
                long j = 0;
                for (int i2 = 0; i2 < i; i2++) {
                    byte b = bArr[i2];
                    j += b * b;
                }
                int log10 = (int) (Math.log10(j / read) * 10.0d);
                this.voiceLevel = log10;
                RecorderListener recorderListener = this.listener;
                if (recorderListener != null) {
                    recorderListener.onRecord(bArr, log10);
                }
            }
        }
    }

    public long getRecordFileSize(String str) {
        return AudioConfig.getFileSize(str);
    }

    public int getVoiceLevel() {
        return this.voiceLevel;
    }

    public void release() {
        stopRecord();
        instance = null;
    }
}
