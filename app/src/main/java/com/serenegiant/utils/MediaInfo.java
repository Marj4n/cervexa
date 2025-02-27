package com.serenegiant.utils;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.text.TextUtils;
import com.serenegiant.media.MediaCodecHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final class MediaInfo {
    private static final List<MediaCodecInfo> sCodecList = new ArrayList();
    private static final HashMap<String, HashMap<MediaCodecInfo, MediaCodecInfo.CodecCapabilities>> sCapabilities = new HashMap<>();

    public static JSONObject get() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("VIDEO", getVideo());
        } catch (Exception e) {
            jSONObject.put("VIDEO", e.getMessage());
        }
        try {
            jSONObject.put("AUDIO", getAudio());
        } catch (Exception e2) {
            jSONObject.put("AUDIO", e2.getMessage());
        }
        return jSONObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00b4 A[Catch: Exception -> 0x00d7, TryCatch #2 {Exception -> 0x00d7, blocks: (B:32:0x00b0, B:34:0x00b4, B:36:0x00b9, B:38:0x00c1, B:40:0x00d3), top: B:31:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b9 A[Catch: Exception -> 0x00d7, TryCatch #2 {Exception -> 0x00d7, blocks: (B:32:0x00b0, B:34:0x00b4, B:36:0x00b9, B:38:0x00c1, B:40:0x00d3), top: B:31:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b6  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final JSONObject getVideo() throws JSONException {
        /*
            Method dump skipped, instructions count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.utils.MediaInfo.getVideo():org.json.JSONObject");
    }

    private static final JSONObject getAudio() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        int codecCount = getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = getCodecInfoAt(i);
            JSONObject jSONObject2 = new JSONObject();
            String[] supportedTypes = codecInfoAt.getSupportedTypes();
            int length = supportedTypes.length;
            boolean z = false;
            for (int i2 = 0; i2 < length; i2++) {
                if (supportedTypes[i2].startsWith("audio/")) {
                    jSONObject2.put(Integer.toString(i2), supportedTypes[i2]);
                    z = true;
                }
            }
            if (z) {
                jSONObject.put(codecInfoAt.getName(), jSONObject2);
            }
        }
        return jSONObject;
    }

    public static final String getColorFormatName(int i) {
        switch (i) {
            case 1:
                return "COLOR_FormatMonochrome";
            case 2:
                return "COLOR_Format8bitRGB332";
            case 3:
                return "COLOR_Format12bitRGB444";
            case 4:
                return "COLOR_Format16bitARGB4444";
            case 5:
                return "COLOR_Format16bitARGB1555";
            case 6:
                return "COLOR_Format16bitRGB565";
            case 7:
                return "COLOR_Format16bitBGR565";
            case 8:
                return "COLOR_Format18bitRGB666";
            case 9:
                return "COLOR_Format18bitARGB1665";
            case 10:
                return "COLOR_Format19bitARGB1666";
            case 11:
                return "COLOR_Format24bitRGB888";
            case 12:
                return "COLOR_Format24bitBGR888";
            case 13:
                return "COLOR_Format24bitARGB1887";
            case 14:
                return "COLOR_Format25bitARGB1888";
            case 15:
                return "COLOR_Format32bitBGRA8888";
            case 16:
                return "COLOR_Format32bitARGB8888";
            case 17:
                return "COLOR_FormatYUV411Planar";
            case 18:
                return "COLOR_FormatYUV411PackedPlanar";
            case 19:
                return "COLOR_FormatYUV420Planar";
            case 20:
                return "COLOR_FormatYUV420PackedPlanar";
            case 21:
                return "COLOR_FormatYUV420SemiPlanar";
            case 22:
                return "COLOR_FormatYUV422Planar";
            case 23:
                return "COLOR_FormatYUV422PackedPlanar";
            case 24:
                return "COLOR_FormatYUV422SemiPlanar";
            case 25:
                return "COLOR_FormatYCbYCr";
            case 26:
                return "COLOR_FormatYCrYCb";
            case 27:
                return "COLOR_FormatCbYCrY";
            case 28:
                return "COLOR_FormatCrYCbY";
            case 29:
                return "COLOR_FormatYUV444Interleaved";
            case 30:
                return "COLOR_FormatRawBayer8bit";
            case 31:
                return "COLOR_FormatRawBayer10bit";
            case 32:
                return "COLOR_FormatRawBayer8bitcompressed";
            case 33:
                return "COLOR_FormatL2";
            case 34:
                return "COLOR_FormatL4";
            case 35:
                return "COLOR_FormatL8";
            case 36:
                return "COLOR_FormatL16";
            case 37:
                return "COLOR_FormatL24";
            case 38:
                return "COLOR_FormatL32";
            case 39:
                return "COLOR_FormatYUV420PackedSemiPlanar";
            case 40:
                return "COLOR_FormatYUV422PackedSemiPlanar";
            case 41:
                return "COLOR_Format18BitBGR666";
            case 42:
                return "COLOR_Format24BitARGB6666";
            case 43:
                return "COLOR_Format24BitABGR6666";
            default:
                switch (i) {
                    case MediaCodecHelper.OMX_COLOR_FormatKhronosExtensions /* 1862270976 */:
                        return "OMX_COLOR_FormatKhronosExtensions";
                    case MediaCodecHelper.OMX_TI_COLOR_FormatYUV420PackedSemiPlanar /* 2130706688 */:
                        return "COLOR_TI_FormatYUV420PackedSemiPlanar";
                    case MediaCodecHelper.OMX_COLOR_FormatAndroidOpaque /* 2130708361 */:
                        return "COLOR_FormatSurface_COLOR_FormatAndroidOpaque";
                    case 2135033992:
                        return "COLOR_FormatYUV420Flexible";
                    case MediaCodecHelper.OMX_QCOM_COLOR_FormatYVU420SemiPlanar /* 2141391872 */:
                        return "COLOR_QCOM_FormatYUV420SemiPlanar";
                    case MediaCodecHelper.OMX_SEC_COLOR_FormatNV12Tiled /* 2143289346 */:
                        return "OMX_SEC_COLOR_FormatNV12Tiled";
                    default:
                        switch (i) {
                            case MediaCodecHelper.OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka /* 2141391875 */:
                                return "OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka";
                            case MediaCodecHelper.OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m /* 2141391876 */:
                                return "OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m";
                            default:
                                return String.format(Locale.getDefault(), "COLOR_Format_Unknown(%d)", Integer.valueOf(i));
                        }
                }
        }
    }

    public static String getProfileLevelString(String str, MediaCodecInfo.CodecProfileLevel codecProfileLevel) {
        String str2;
        String str3;
        String str4;
        String str5;
        if (TextUtils.isEmpty(str)) {
            return "mime type is null";
        }
        if (str.equalsIgnoreCase("video/avc")) {
            int i = codecProfileLevel.profile;
            if (i == 1) {
                str5 = "AVCProfileBaseline";
            } else if (i == 2) {
                str5 = "AVCProfileMain";
            } else if (i == 4) {
                str5 = "AVCProfileExtended";
            } else if (i == 8) {
                str5 = "AVCProfileHigh";
            } else if (i == 16) {
                str5 = "AVCProfileHigh10";
            } else if (i == 32) {
                str5 = "AVCProfileHigh422";
            } else if (i != 64) {
                str5 = "unknown profile " + codecProfileLevel.profile;
            } else {
                str5 = "AVCProfileHigh444";
            }
            int i2 = codecProfileLevel.level;
            if (i2 == 1) {
                return str5 + ".AVCLevel1";
            }
            if (i2 == 2) {
                return str5 + ".AVCLevel1b";
            }
            switch (i2) {
                case 4:
                    return str5 + ".AVCLevel11";
                case 8:
                    return str5 + ".AVCLevel12";
                case 16:
                    return str5 + ".AVCLevel13";
                case 32:
                    return str5 + ".AVCLevel2";
                case 64:
                    return str5 + ".AVCLevel21";
                case 128:
                    return str5 + ".AVCLevel22";
                case 256:
                    return str5 + ".AVCLevel3";
                case 512:
                    return str5 + ".AVCLevel31";
                case 1024:
                    return str5 + ".AVCLevel32";
                case 2048:
                    return str5 + ".AVCLevel4";
                case 4096:
                    return str5 + ".AVCLevel41";
                case 8192:
                    return str5 + ".AVCLevel42";
                case 16384:
                    return str5 + ".AVCLevel5";
                case 32768:
                    return str5 + ".AVCLevel51";
                default:
                    return str5 + ".unknown level " + codecProfileLevel.level;
            }
        }
        if (str.equalsIgnoreCase("video/h263")) {
            int i3 = codecProfileLevel.profile;
            if (i3 == 1) {
                str4 = "H263ProfileBaseline";
            } else if (i3 == 2) {
                str4 = "H263ProfileH320Coding";
            } else if (i3 == 4) {
                str4 = "H263ProfileBackwardCompatible";
            } else if (i3 == 8) {
                str4 = "H263ProfileISWV2";
            } else if (i3 == 16) {
                str4 = "H263ProfileISWV3";
            } else if (i3 == 32) {
                str4 = "H263ProfileHighCompression";
            } else if (i3 == 64) {
                str4 = "H263ProfileInternet";
            } else if (i3 == 128) {
                str4 = "H263ProfileInterlace";
            } else if (i3 != 256) {
                str4 = "unknown profile " + codecProfileLevel.profile;
            } else {
                str4 = "H263ProfileHighLatency";
            }
            int i4 = codecProfileLevel.level;
            if (i4 == 1) {
                return str4 + ".H263Level10";
            }
            if (i4 == 2) {
                return str4 + ".H263Level20";
            }
            if (i4 == 4) {
                return str4 + ".H263Level30";
            }
            if (i4 == 8) {
                return str4 + ".H263Level40";
            }
            if (i4 == 16) {
                return str4 + ".H263Level45";
            }
            if (i4 == 32) {
                return str4 + ".H263Level50";
            }
            if (i4 == 64) {
                return str4 + ".H263Level60";
            }
            if (i4 == 128) {
                return str4 + ".H263Level70";
            }
            return str4 + ".unknown level " + codecProfileLevel.level;
        }
        if (str.equalsIgnoreCase("video/mpeg4")) {
            int i5 = codecProfileLevel.profile;
            if (i5 == 1) {
                str3 = "MPEG4ProfileSimple";
            } else if (i5 != 2) {
                switch (i5) {
                    case 4:
                        str3 = "MPEG4ProfileCore";
                        break;
                    case 8:
                        str3 = "MPEG4ProfileMain";
                        break;
                    case 16:
                        str3 = "MPEG4ProfileNbit";
                        break;
                    case 32:
                        str3 = "MPEG4ProfileScalableTexture";
                        break;
                    case 64:
                        str3 = "MPEG4ProfileSimpleFace";
                        break;
                    case 128:
                        str3 = "MPEG4ProfileSimpleFBA";
                        break;
                    case 256:
                        str3 = "MPEG4ProfileBasicAnimated";
                        break;
                    case 512:
                        str3 = "MPEG4ProfileHybrid";
                        break;
                    case 1024:
                        str3 = "MPEG4ProfileAdvancedRealTime";
                        break;
                    case 2048:
                        str3 = "MPEG4ProfileCoreScalable";
                        break;
                    case 4096:
                        str3 = "MPEG4ProfileAdvancedCoding";
                        break;
                    case 8192:
                        str3 = "MPEG4ProfileAdvancedCore";
                        break;
                    case 16384:
                        str3 = "MPEG4ProfileAdvancedScalable";
                        break;
                    case 32768:
                        str3 = "MPEG4ProfileAdvancedSimple";
                        break;
                    default:
                        str3 = "unknown profile " + codecProfileLevel.profile;
                        break;
                }
            } else {
                str3 = "MPEG4ProfileSimpleScalable";
            }
            int i6 = codecProfileLevel.level;
            if (i6 == 1) {
                return str3 + ".MPEG4Level0";
            }
            if (i6 == 2) {
                return str3 + ".MPEG4Level0b";
            }
            if (i6 == 4) {
                return str3 + ".MPEG4Level1";
            }
            if (i6 == 8) {
                return str3 + ".MPEG4Level2";
            }
            if (i6 == 16) {
                return str3 + ".MPEG4Level3";
            }
            if (i6 == 32) {
                return str3 + ".MPEG4Level4";
            }
            if (i6 == 64) {
                return str3 + ".MPEG4Level4a";
            }
            if (i6 == 128) {
                return str3 + ".MPEG4Level5";
            }
            return str3 + ".unknown level " + codecProfileLevel.level;
        }
        if (str.equalsIgnoreCase("ausio/aac")) {
            int i7 = codecProfileLevel.level;
            if (i7 == 17) {
                return "AACObjectERLC";
            }
            if (i7 == 23) {
                return "AACObjectLD";
            }
            if (i7 == 29) {
                return "AACObjectHE_PS";
            }
            if (i7 == 39) {
                return "AACObjectELD";
            }
            switch (i7) {
                case 1:
                    return "AACObjectMain";
                case 2:
                    return "AACObjectLC";
                case 3:
                    return "AACObjectSSR";
                case 4:
                    return "AACObjectLTP";
                case 5:
                    return "AACObjectHE";
                case 6:
                    return "AACObjectScalable";
                default:
                    return "profile:unknown " + codecProfileLevel.profile;
            }
        }
        if (str.equalsIgnoreCase("video/vp8")) {
            if (codecProfileLevel.profile != 1) {
                str2 = "unknown profile " + codecProfileLevel.profile;
            } else {
                str2 = "VP8ProfileMain";
            }
            int i8 = codecProfileLevel.level;
            if (i8 == 1) {
                return str2 + ".VP8Level_Version0";
            }
            if (i8 == 2) {
                return str2 + ".VP8Level_Version1";
            }
            if (i8 == 4) {
                return str2 + ".VP8Level_Version2";
            }
            if (i8 == 8) {
                return str2 + ".VP8Level_Version3";
            }
            return str2 + ".unknown level" + codecProfileLevel.level;
        }
        return "unknown profile " + codecProfileLevel.profile;
    }

    private static final void updateCodecs() {
        if (sCodecList.size() == 0) {
            int codecCount = MediaCodecList.getCodecCount();
            for (int i = 0; i < codecCount; i++) {
                sCodecList.add(MediaCodecList.getCodecInfoAt(i));
            }
        }
    }

    public static final int getCodecCount() {
        updateCodecs();
        return sCodecList.size();
    }

    public static final List<MediaCodecInfo> getCodecs() {
        updateCodecs();
        return sCodecList;
    }

    public static final MediaCodecInfo getCodecInfoAt(int i) {
        updateCodecs();
        return sCodecList.get(i);
    }

    public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(MediaCodecInfo mediaCodecInfo, String str) {
        HashMap<MediaCodecInfo, MediaCodecInfo.CodecCapabilities> hashMap = sCapabilities.get(str);
        if (hashMap == null) {
            hashMap = new HashMap<>();
            sCapabilities.put(str, hashMap);
        }
        MediaCodecInfo.CodecCapabilities codecCapabilities = hashMap.get(mediaCodecInfo);
        if (codecCapabilities != null) {
            return codecCapabilities;
        }
        Thread.currentThread().setPriority(10);
        try {
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            hashMap.put(mediaCodecInfo, capabilitiesForType);
            return capabilitiesForType;
        } finally {
            Thread.currentThread().setPriority(5);
        }
    }
}
