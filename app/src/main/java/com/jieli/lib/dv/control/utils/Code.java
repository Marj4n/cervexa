package com.jieli.lib.dv.control.utils;

/* loaded from: classes.dex */
public final class Code {
    public static final int ERROR_ACCESS_REFUSE = 3;
    public static final int ERROR_IN_BROWSE = 9;
    public static final int ERROR_IN_PARKING = 10;
    public static final int ERROR_IN_USB_MODE = 7;
    public static final int ERROR_IN_VIDEO = 8;
    public static final int ERROR_NETWORK = 14;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NO_TOPIC = 6;
    public static final int ERROR_OPEN_FILE = 11;
    public static final int ERROR_REAR_CAMERA_NOT_SUPPORT = 16;
    public static final int ERROR_REAR_CAMERA_OFFLINE = 15;
    public static final int ERROR_REQUEST = 4;
    public static final int ERROR_RT_STREAM_OPEN_FAILED = 17;
    public static final int ERROR_SET_PARAMETER = 13;
    public static final int ERROR_SYSTEM_EXCEPTION = 12;
    public static final int ERROR_TF_CARD = 1;
    public static final int ERROR_TF_CARD_OFFLINE = 2;
    public static final int ERROR_VERSION_NOT_MATCH = 5;

    public static String getCodeDescription(int i) {
        switch (i) {
            case 0:
                return "No error";
            case 1:
                return "TF card occurred an error";
            case 2:
                return "TF card is offline";
            case 3:
                return "Device refuse access";
            case 4:
                return "Request occurred an error";
            case 5:
                return "App version not match with device";
            case 6:
                return "Maybe the topic has not been implemented yet";
            case 7:
                return "Device is in USB mode";
            case 8:
                return "Device is in video";
            case 9:
                return "Device is in browsing";
            case 10:
                return "Device is in parking";
            case 11:
                return "Device open file occurred an error";
            case 12:
                return "System occurred an exception";
            case 13:
                return "Failed to set parameter";
            case 14:
                return "Maybe APP disconnected from device";
            case 15:
                return "Rear camera offline";
            case 16:
                return "Rear camera not supported by device";
            case 17:
                return "open real time stream filed";
            default:
                return "Unknown error code:" + i;
        }
    }
}
