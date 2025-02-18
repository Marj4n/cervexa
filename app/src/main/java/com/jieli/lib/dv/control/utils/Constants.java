package com.jieli.lib.dv.control.utils;

/* loaded from: classes.dex */
public class Constants {
    public static final String CTP_SIGNATURE = "CTP:";
    public static final int DEVICE_STATE_CONNECTED = 0;
    public static final int DEVICE_STATE_CONNECTING = 2;
    public static final int DEVICE_STATE_CONNECTION_TIMEOUT = 3;
    public static final int DEVICE_STATE_DISCONNECTED = 1;
    public static final int DEVICE_STATE_EXCEPTION = 4;
    public static final int DEVICE_STATE_UDP_EXCEPTION = 5;
    public static final int DEVICE_STATE_UDP_TIMEOUT = 6;
    public static final int DEVICE_STATE_UNREADY = -1;
    public static final String EXTRA_CMD_INFO = "extra.cmd.info";
    public static final String JSON_ERROR_NUMBER = "errno";
    public static final String JSON_OP = "op";
    public static final String JSON_PARAM = "param";
    public static final int REQUEST_FAILED = -1;
    public static final int REQUEST_SUCCESS = 1;
    public static final int SEND_FAILED = -1;
    public static final int SEND_SUCCESS = 1;

    public static String getConnectDescription(int i) {
        switch (i) {
            case -1:
                return "Network not ready";
            case 0:
                return "Connected";
            case 1:
                return "Disconnect";
            case 2:
                return "Connecting...";
            case 3:
                return "Connection timeout";
            case 4:
                return "Network occurred exception";
            case 5:
                return "Udp Channel occurred exception";
            case 6:
                return "Udp Connection timeout";
            default:
                return "Unknown state:" + i;
        }
    }
}
