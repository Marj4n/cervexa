package com.jieli.stream.dv.running2.audio;

import android.media.AudioTrack;

/* loaded from: classes.dex */
public class AudioPlayer {
    private static AudioPlayer instance;
    private AudioConfig audioConfig = new AudioConfig();
    private int bufferSize;
    private AudioTrack mAudioTrack;

    public AudioPlayer() {
        createAudioPlayer();
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            synchronized (AudioPlayer.class) {
                if (instance == null) {
                    instance = new AudioPlayer();
                }
            }
        }
        return instance;
    }

    public void createAudioPlayer() {
        AudioConfig audioConfig = this.audioConfig;
        if (audioConfig == null) {
            return;
        }
        try {
            int minBufferSize = AudioTrack.getMinBufferSize(audioConfig.getAudioSampleRate(), this.audioConfig.getOutputChannel(), this.audioConfig.getAudioFormat());
            this.bufferSize = minBufferSize;
            if (minBufferSize != -2) {
                this.mAudioTrack = new AudioTrack(3, this.audioConfig.getAudioSampleRate(), this.audioConfig.getOutputChannel(), this.audioConfig.getAudioFormat(), this.bufferSize, 1);
            }
            if (this.mAudioTrack == null || this.mAudioTrack.getState() != 1) {
                return;
            }
            this.mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x003a -> B:15:0x003d). Please report as a decompilation issue!!! */
    public void startPlay(byte[] bArr) {
        if (bArr != null) {
            AudioTrack audioTrack = this.mAudioTrack;
            if (audioTrack == null || audioTrack.getState() == 0) {
                createAudioPlayer();
            }
            try {
                if (this.mAudioTrack != null && this.mAudioTrack.getState() == 1) {
                    if (this.mAudioTrack.getPlayState() == 3) {
                        this.mAudioTrack.write(bArr, 0, bArr.length);
                    } else {
                        this.mAudioTrack.play();
                        this.mAudioTrack.write(bArr, 0, bArr.length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pausePlay() {
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null && audioTrack.getState() == 1 && this.mAudioTrack.getPlayState() == 3) {
            this.mAudioTrack.pause();
            this.mAudioTrack.flush();
        }
    }

    public void stopPlay() {
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null) {
            if (audioTrack.getState() == 1) {
                this.mAudioTrack.pause();
                this.mAudioTrack.flush();
                this.mAudioTrack.stop();
            }
            this.mAudioTrack.release();
            this.mAudioTrack = null;
        }
        instance = null;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(int i) {
        this.bufferSize = i;
    }

    public boolean isPlaying() {
        AudioTrack audioTrack = this.mAudioTrack;
        return audioTrack != null && audioTrack.getState() == 1 && this.mAudioTrack.getPlayState() == 3;
    }
}
