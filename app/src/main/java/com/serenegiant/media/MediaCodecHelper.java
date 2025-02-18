package com.serenegiant.media;

import android.media.MediaCodecInfo;
import android.util.Log;
import com.serenegiant.utils.BufferHelper;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.MediaInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaCodecHelper {
    public static final int BUFFER_FLAG_KEY_FRAME;
    public static final String MIME_AUDIO_AAC = "audio/mp4a-latm";
    public static final String MIME_VIDEO_AVC = "video/avc";
    public static final int OMX_COLOR_Format12bitRGB444 = 3;
    public static final int OMX_COLOR_Format16bitARGB1555 = 5;
    public static final int OMX_COLOR_Format16bitARGB4444 = 4;
    public static final int OMX_COLOR_Format16bitBGR565 = 7;
    public static final int OMX_COLOR_Format16bitRGB565 = 6;
    public static final int OMX_COLOR_Format18BitBGR666 = 41;
    public static final int OMX_COLOR_Format18bitARGB1665 = 9;
    public static final int OMX_COLOR_Format18bitRGB666 = 8;
    public static final int OMX_COLOR_Format19bitARGB1666 = 10;
    public static final int OMX_COLOR_Format24BitABGR6666 = 43;
    public static final int OMX_COLOR_Format24BitARGB6666 = 42;
    public static final int OMX_COLOR_Format24bitARGB1887 = 13;
    public static final int OMX_COLOR_Format24bitBGR888 = 12;
    public static final int OMX_COLOR_Format24bitRGB888 = 11;
    public static final int OMX_COLOR_Format25bitARGB1888 = 14;
    public static final int OMX_COLOR_Format32bitARGB8888 = 16;
    public static final int OMX_COLOR_Format32bitBGRA8888 = 15;
    public static final int OMX_COLOR_Format8bitRGB332 = 2;
    public static final int OMX_COLOR_FormatAndroidOpaque = 2130708361;
    public static final int OMX_COLOR_FormatCbYCrY = 27;
    public static final int OMX_COLOR_FormatCrYCbY = 28;
    public static final int OMX_COLOR_FormatKhronosExtensions = 1862270976;
    public static final int OMX_COLOR_FormatL16 = 36;
    public static final int OMX_COLOR_FormatL2 = 33;
    public static final int OMX_COLOR_FormatL24 = 37;
    public static final int OMX_COLOR_FormatL32 = 38;
    public static final int OMX_COLOR_FormatL4 = 34;
    public static final int OMX_COLOR_FormatL8 = 35;
    public static final int OMX_COLOR_FormatMax = Integer.MAX_VALUE;
    public static final int OMX_COLOR_FormatMonochrome = 1;
    public static final int OMX_COLOR_FormatRawBayer10bit = 31;
    public static final int OMX_COLOR_FormatRawBayer8bit = 30;
    public static final int OMX_COLOR_FormatRawBayer8bitcompressed = 32;
    public static final int OMX_COLOR_FormatUnused = 0;
    public static final int OMX_COLOR_FormatVendorStartUnused = 2130706432;
    public static final int OMX_COLOR_FormatYCbYCr = 25;
    public static final int OMX_COLOR_FormatYCrYCb = 26;
    public static final int OMX_COLOR_FormatYUV411PackedPlanar = 18;
    public static final int OMX_COLOR_FormatYUV411Planar = 17;
    public static final int OMX_COLOR_FormatYUV420PackedPlanar = 20;
    public static final int OMX_COLOR_FormatYUV420PackedSemiPlanar = 39;
    public static final int OMX_COLOR_FormatYUV420Planar = 19;
    public static final int OMX_COLOR_FormatYUV420SemiPlanar = 21;
    public static final int OMX_COLOR_FormatYUV422PackedPlanar = 23;
    public static final int OMX_COLOR_FormatYUV422PackedSemiPlanar = 40;
    public static final int OMX_COLOR_FormatYUV422Planar = 22;
    public static final int OMX_COLOR_FormatYUV422SemiPlanar = 24;
    public static final int OMX_COLOR_FormatYUV444Interleaved = 29;
    public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m = 2141391876;
    public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka = 2141391875;
    public static final int OMX_QCOM_COLOR_FormatYVU420SemiPlanar = 2141391872;
    public static final int OMX_SEC_COLOR_FormatNV12Tiled = 2143289346;
    public static final int OMX_TI_COLOR_FormatYUV420PackedSemiPlanar = 2130706688;
    public static final byte[] START_MARKER;
    private static final String TAG = MediaCodecHelper.class.getSimpleName();
    public static int[] recognizedFormats;

    static {
        BuildCheck.isLollipop();
        BUFFER_FLAG_KEY_FRAME = 1;
        START_MARKER = BufferHelper.ANNEXB_START_MARK;
        recognizedFormats = new int[]{19, 21, OMX_QCOM_COLOR_FormatYVU420SemiPlanar};
    }

    @Deprecated
    public static MediaCodecInfo selectVideoCodec(String str) {
        return selectVideoEncoder(str);
    }

    public static MediaCodecInfo selectVideoEncoder(String str) {
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str) && selectColorFormat(codecInfoAt, str) > 0) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    public static List<MediaCodecInfo> getVideoEncoderInfos(String str) {
        ArrayList arrayList = new ArrayList();
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str) && selectColorFormat(codecInfoAt, str) > 0) {
                        arrayList.add(codecInfoAt);
                    }
                }
            }
        }
        return arrayList;
    }

    public static final boolean isRecognizedVideoFormat(int i) {
        int[] iArr = recognizedFormats;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats[i2] == i) {
                return true;
            }
        }
        return false;
    }

    public static final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        for (int i : getCodecCapabilities(mediaCodecInfo, str).colorFormats) {
            if (isRecognizedVideoFormat(i)) {
                return i;
            }
        }
        return 0;
    }

    @Deprecated
    public static final void dumpVideoCodecEncoders() {
        dumpEncoders();
    }

    public static final void dumpEncoders() {
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    Log.i(TAG, "codec:" + codecInfoAt.getName() + ",MIME:" + supportedTypes[i2]);
                    selectColorFormat(codecInfoAt, supportedTypes[i2]);
                }
            }
        }
    }

    public static final void dumpDecoders() {
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (!codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    Log.i(TAG, "codec:" + codecInfoAt.getName() + ",MIME:" + supportedTypes[i2]);
                    selectColorFormat(codecInfoAt, supportedTypes[i2]);
                }
            }
        }
    }

    public static final boolean isSemiPlanarYUV(int i) {
        if (i == 39 || i == 2130706688 || i == 2141391872) {
            return true;
        }
        switch (i) {
            case 19:
            case 20:
                return false;
            case 21:
                return true;
            default:
                throw new RuntimeException("unknown format " + i);
        }
    }

    @Deprecated
    public static final MediaCodecInfo selectAudioCodec(String str) {
        return selectAudioEncoder(str);
    }

    public static final MediaCodecInfo selectAudioEncoder(String str) {
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    public static List<MediaCodecInfo> getAudioEncoderInfos(String str) {
        ArrayList arrayList = new ArrayList();
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        arrayList.add(codecInfoAt);
                    }
                }
            }
        }
        return arrayList;
    }

    public static final int getCodecCount() {
        return MediaInfo.getCodecCount();
    }

    public static final List<MediaCodecInfo> getCodecs() {
        return MediaInfo.getCodecs();
    }

    public static final MediaCodecInfo getCodecInfoAt(int i) {
        return MediaInfo.getCodecInfoAt(i);
    }

    public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(MediaCodecInfo mediaCodecInfo, String str) {
        return MediaInfo.getCodecCapabilities(mediaCodecInfo, str);
    }

    public static boolean checkProfileLevel(String str, MediaCodecInfo mediaCodecInfo) {
        if (mediaCodecInfo == null || !str.equalsIgnoreCase("video/avc")) {
            return true;
        }
        for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : getCodecCapabilities(mediaCodecInfo, str).profileLevels) {
            if (codecProfileLevel.level >= 16384) {
                return false;
            }
        }
        return true;
    }

    public static String getProfileLevelString(String str, MediaCodecInfo.CodecProfileLevel codecProfileLevel) {
        String str2;
        if (str.equalsIgnoreCase("video/avc")) {
            int i = codecProfileLevel.profile;
            if (i == 1) {
                str2 = "profile:AVCProfileBaseline";
            } else if (i == 2) {
                str2 = "profile:AVCProfileMain";
            } else if (i == 4) {
                str2 = "profile:AVCProfileExtended";
            } else if (i == 8) {
                str2 = "profile:AVCProfileHigh";
            } else if (i == 16) {
                str2 = "profile:AVCProfileHigh10";
            } else if (i == 32) {
                str2 = "profile:AVCProfileHigh422";
            } else if (i != 64) {
                str2 = "profile:unknown " + codecProfileLevel.profile;
            } else {
                str2 = "profile:AVCProfileHigh444";
            }
            int i2 = codecProfileLevel.level;
            if (i2 == 1) {
                return str2 + ",level=AVCLevel1";
            }
            if (i2 == 2) {
                return str2 + ",level=AVCLevel1b";
            }
            switch (i2) {
                case 4:
                    return str2 + ",level=AVCLevel11";
                case 8:
                    return str2 + ",level=AVCLevel12";
                case 16:
                    return str2 + ",level=AVCLevel13";
                case 32:
                    return str2 + ",level=AVCLevel2";
                case 64:
                    return str2 + ",level=AVCLevel21";
                case 128:
                    return str2 + ",level=AVCLevel22";
                case 256:
                    return str2 + ",level=AVCLevel3";
                case 512:
                    return str2 + ",level=AVCLevel31";
                case 1024:
                    return str2 + ",level=AVCLevel32";
                case 2048:
                    return str2 + ",level=AVCLevel4";
                case 4096:
                    return str2 + ",level=AVCLevel41";
                case 8192:
                    return str2 + ",level=AVCLevel42";
                case 16384:
                    return str2 + ",level=AVCLevel5";
                case 32768:
                    return str2 + ",level=AVCLevel51";
                default:
                    return str2 + ",level=unknown " + codecProfileLevel.level;
            }
        }
        if (str.equalsIgnoreCase("video/h263")) {
            int i3 = codecProfileLevel.profile;
            String str3 = "profile:unknown " + codecProfileLevel.profile;
            int i4 = codecProfileLevel.level;
            return str3 + ",level=unknown " + codecProfileLevel.level;
        }
        if (str.equalsIgnoreCase("video/mpeg4")) {
            int i5 = codecProfileLevel.profile;
            String str4 = "profile:unknown " + codecProfileLevel.profile;
            int i6 = codecProfileLevel.level;
            return str4 + ",level=unknown " + codecProfileLevel.level;
        }
        if (str.equalsIgnoreCase("audio/aac")) {
            int i7 = codecProfileLevel.level;
            return "profile:unknown " + codecProfileLevel.profile;
        }
        if (!str.equalsIgnoreCase("video/vp8")) {
            return null;
        }
        int i8 = codecProfileLevel.profile;
        String str5 = "profile:unknown " + codecProfileLevel.profile;
        int i9 = codecProfileLevel.level;
        return str5 + ",level=unknown " + codecProfileLevel.level;
    }

    public static final int findStartMarker(byte[] bArr, int i) {
        byte[] bArr2 = START_MARKER;
        return BufferHelper.byteComp(bArr, i, bArr2, bArr2.length);
    }
}
