package com.jieli.stream.dv.running2.bean;

import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public class DeviceSettingInfo {
    private boolean antiTremor;
    private int autoShutdown;
    private int batStatus;
    private boolean burstShot;
    private boolean doubleVideo;
    private int frontLevel;
    private int frontRecordLevel;
    private int gravitySensor;
    private boolean isExistRearView;
    private boolean isOpenBootSound;
    private boolean isOpenProjection;
    private boolean isRTVoice;
    private boolean keyVoice;
    private int lanIndex;
    private int leftStorage;
    private int lightFrequece;
    private boolean moveCheck;
    private boolean photoDate;
    private int photoExp;
    private int photoIso;
    private int photoQualityIndex;
    private int photoReso;
    private int photoSharpness;
    private int rearLevel;
    private int rearRecordLevel;
    private int recordState;
    private int screenOn;
    private int selfTime;
    private int totalStorage;
    private int tvMode;
    private boolean videoDate;
    private int videoExp;
    private int videoInv;
    private int videoLoop;
    private boolean videoMic;
    private boolean videoParCar;
    private boolean videoWdr;
    private int cameraType = 1;
    private int frontFormat = 1;
    private int frontRate = 30;
    private int rearFormat = 1;
    private int rearRate = 30;
    private int rearSampleRate = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
    private int frontSampleRate = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;

    public boolean isPhotoDate() {
        return this.photoDate;
    }

    public void setPhotoDate(boolean z) {
        this.photoDate = z;
    }

    public boolean isAntiTremor() {
        return this.antiTremor;
    }

    public void setAntiTremor(boolean z) {
        this.antiTremor = z;
    }

    public void setPhotoExp(int i) {
        this.photoExp = i;
    }

    public int getPhotoExp() {
        return this.photoExp;
    }

    public void setPhotoIso(int i) {
        this.photoIso = i;
    }

    public int getPhotoIso() {
        return this.photoIso;
    }

    public void setPhotoSharpness(int i) {
        this.photoSharpness = i;
    }

    public int getPhotoSharpness() {
        return this.photoSharpness;
    }

    public boolean isBurstShot() {
        return this.burstShot;
    }

    public void setBurstShot(boolean z) {
        this.burstShot = z;
    }

    public void setSelfTime(int i) {
        this.selfTime = i;
    }

    public int getSelfTime() {
        return this.selfTime;
    }

    public void setPhotoReso(int i) {
        this.photoReso = i;
    }

    public int getPhotoReso() {
        return this.photoReso;
    }

    public void setVideoInv(int i) {
        this.videoInv = i;
    }

    public int getVideoInv() {
        return this.videoInv;
    }

    public boolean isMoveCheck() {
        return this.moveCheck;
    }

    public void setMoveCheck(boolean z) {
        this.moveCheck = z;
    }

    public void setVideoExp(int i) {
        this.videoExp = i;
    }

    public int getVideoExp() {
        return this.videoExp;
    }

    public boolean isVideoWdr() {
        return this.videoWdr;
    }

    public void setVideoWdr(boolean z) {
        this.videoWdr = z;
    }

    public void setVideoLoop(int i) {
        this.videoLoop = i;
    }

    public int getVideoLoop() {
        return this.videoLoop;
    }

    public boolean isDoubleVideo() {
        return this.doubleVideo;
    }

    public void setDoubleVideo(boolean z) {
        this.doubleVideo = z;
    }

    public void setTvMode(int i) {
        this.tvMode = i;
    }

    public int getTvMode() {
        return this.tvMode;
    }

    public void setScreenOn(int i) {
        this.screenOn = i;
    }

    public int getScreenOn() {
        return this.screenOn;
    }

    public void setAutoShutdown(int i) {
        this.autoShutdown = i;
    }

    public int getAutoShutdown() {
        return this.autoShutdown;
    }

    public void setLightFrequece(int i) {
        this.lightFrequece = i;
    }

    public int getLightFrequece() {
        return this.lightFrequece;
    }

    public void setBatStatus(int i) {
        this.batStatus = i;
    }

    public int getBatStatus() {
        return this.batStatus;
    }

    public boolean isKeyVoice() {
        return this.keyVoice;
    }

    public void setKeyVoice(boolean z) {
        this.keyVoice = z;
    }

    public boolean isVideoParCar() {
        return this.videoParCar;
    }

    public void setVideoParCar(boolean z) {
        this.videoParCar = z;
    }

    public boolean isVideoDate() {
        return this.videoDate;
    }

    public void setVideoDate(boolean z) {
        this.videoDate = z;
    }

    public int getGravitySensor() {
        return this.gravitySensor;
    }

    public void setGravitySensor(int i) {
        this.gravitySensor = i;
    }

    public boolean isVideoMic() {
        return this.videoMic;
    }

    public void setVideoMic(boolean z) {
        this.videoMic = z;
    }

    public int getLanIndex() {
        return this.lanIndex;
    }

    public void setLanIndex(int i) {
        this.lanIndex = i;
    }

    public int getPhotoQualityIndex() {
        return this.photoQualityIndex;
    }

    public void setPhotoQualityIndex(int i) {
        this.photoQualityIndex = i;
    }

    public int getTotalStorage() {
        return this.totalStorage;
    }

    public void setTotalStorage(int i) {
        this.totalStorage = i;
    }

    public int getLeftStorage() {
        return this.leftStorage;
    }

    public void setLeftStorage(int i) {
        this.leftStorage = i;
    }

    public int getRecordState() {
        return this.recordState;
    }

    public void setRecordState(int i) {
        this.recordState = i;
    }

    public boolean isOpenProjection() {
        return this.isOpenProjection;
    }

    public void setOpenProjection(boolean z) {
        this.isOpenProjection = z;
    }

    public boolean isRTVoice() {
        return this.isRTVoice;
    }

    public void setRTVoice(boolean z) {
        this.isRTVoice = z;
    }

    public boolean isExistRearView() {
        return this.isExistRearView;
    }

    public void setExistRearView(boolean z) {
        this.isExistRearView = z;
    }

    public int getCameraType() {
        return this.cameraType;
    }

    public void setCameraType(int i) {
        this.cameraType = i;
    }

    public int getFrontLevel() {
        return this.frontLevel;
    }

    public void setFrontLevel(int i) {
        this.frontLevel = i;
    }

    public int getFrontFormat() {
        return this.frontFormat;
    }

    public void setFrontFormat(int i) {
        this.frontFormat = i;
    }

    public int getFrontRate() {
        return this.frontRate;
    }

    public void setFrontRate(int i) {
        this.frontRate = i;
    }

    public int getRearLevel() {
        return this.rearLevel;
    }

    public void setRearLevel(int i) {
        this.rearLevel = i;
    }

    public int getRearFormat() {
        return this.rearFormat;
    }

    public void setRearFormat(int i) {
        this.rearFormat = i;
    }

    public int getRearRate() {
        return this.rearRate;
    }

    public void setRearRate(int i) {
        this.rearRate = i;
    }

    public int getFrontRecordLevel() {
        return this.frontRecordLevel;
    }

    public void setFrontRecordLevel(int i) {
        this.frontRecordLevel = i;
    }

    public int getRearRecordLevel() {
        return this.rearRecordLevel;
    }

    public void setRearRecordLevel(int i) {
        this.rearRecordLevel = i;
    }

    public String toString() {
        return "{\"lanIndex\":" + this.lanIndex + ",\n\"photoQualityIndex\": " + this.photoQualityIndex + ",\n\"videoMic\":" + this.videoMic + ",\n\"totalStorage\":" + this.totalStorage + ",\n\"leftStorage\":" + this.leftStorage + ",\n\"videoParCar\":" + this.videoParCar + ",\n\"videoDate\":" + this.videoDate + ",\n\"gravitySensor\":" + this.gravitySensor + ",\n\"keyVoice\":" + this.keyVoice + ",\n\"batStatus\":" + this.batStatus + ",\n\"lightFrequece\":" + this.lightFrequece + ",\n\"autoShutdown\":" + this.autoShutdown + ",\n\"screenOn\":" + this.screenOn + ",\n\"tvMode\":" + this.tvMode + ",\n\"doubleVideo\":" + this.doubleVideo + ",\n\"videoLoop\":" + this.videoLoop + ",\n\"videoWdr\":" + this.videoWdr + ",\n\"videoExp\":" + this.videoExp + ",\n\"moveCheck\":" + this.moveCheck + ",\n\"videoInv\":" + this.videoInv + ",\n\"photoReso\":" + this.photoReso + ",\n\"selfTime\":" + this.selfTime + ",\n\"burstShot\":" + this.burstShot + ",\n\"photoSharpness\":" + this.photoSharpness + ",\n\"photoIso\":" + this.photoIso + ",\n\"photoExp\":" + this.photoExp + ",\n\"antiTremor\":" + this.antiTremor + ",\n\"photoDate\":" + this.photoDate + ",\n\"recordState\":" + this.recordState + ",\n\"isOpenProjection\":" + this.isOpenProjection + ",\n\"isRTVoice\":" + this.isRTVoice + ",\n\"isExistRearView\":" + this.isExistRearView + ",\n\"cameraType\":" + this.cameraType + ",\n\"frontRecordLevel\":" + this.frontRecordLevel + "\"frontLevel\":" + this.frontLevel + ",\n\"frontFormat\":" + this.frontFormat + ",\n\"frontRate\":" + this.frontRate + ",\n\"rearRecordLevel\":" + this.rearRecordLevel + "\"rearLevel\":" + this.rearLevel + ",\n\"rearFormat\":" + this.rearFormat + ",\n\"rearRate\":" + this.rearRate + "\"rearSampleRate\":" + this.rearSampleRate + ",\n\"frontSampleRate\":" + this.frontSampleRate + '}';
    }

    public int getRearSampleRate() {
        return this.rearSampleRate;
    }

    public void setRearSampleRate(int i) {
        this.rearSampleRate = i;
    }

    public int getFrontSampleRate() {
        return this.frontSampleRate;
    }

    public void setFrontSampleRate(int i) {
        this.frontSampleRate = i;
    }

    public boolean isOpenBootSound() {
        return this.isOpenBootSound;
    }

    public void setOpenBootSound(boolean z) {
        this.isOpenBootSound = z;
    }
}
