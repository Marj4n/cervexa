package com.jieli.stream.dv.running2.util;

import android.os.Environment;
import com.jieli.stream.dv.running2.ui.MainApplication;

/* loaded from: classes.dex */
public interface IConstant {
    public static final int ABOUT_FRAGMENT = 9;
    public static final int ADD_DEVICE_FRAGMENT = 1;
    public static final String ANDROID_DIR = "android";
    public static final int AP_SEARCH_MODE = 0;
    public static final int ARGS_DISMISS_DIALOG = 1;
    public static final int ARGS_SHOW_DIALOG = 0;
    public static final int AUDIO_CHANNEL = 1;
    public static final int AUDIO_FORMAT = 16;
    public static final int AUDIO_SAMPLE_RATE_DEFAULT = 8000;
    public static final String AUD_DEFAULT_NAME = "AUD_RECORD.pcm";
    public static final int AUTO_TIME = 10000;
    public static final int BROWSE_FILE_FEAGMENT = 2;
    public static final String CAMERA_TYPE_FRONT = "0";
    public static final String CAMERA_TYPE_REAR = "1";
    public static final int CHECK_GPS_CODE = 4356;
    public static final int CODE_ADD_DEVICE = 4150;
    public static final int CODE_BROWSE_FILE = 4133;
    public static final int CODE_DEVICE_LIST = 4151;
    public static final int CODE_PLAYBACK = 4135;
    public static final int CODE_SHARE_FILES = 4134;
    public static final int CODE_UPGRADE = 4152;
    public static final int CODE_UPGRADE_APK = 4104;
    public static final int COME_FORM_DEV = 0;
    public static final int COME_FORM_LOCAL = 1;
    public static final int CTP_TCP_PORT = 3333;
    public static final int CTP_UDP_PORT = 2228;
    public static final String CURRENT_WIFI_SSID = "current_wifi_ssid";
    public static final String DEBUG_SETTINGS = "debug_settings";
    public static final int DEFAULT_CACHE_SIZE = 209715200;
    public static final String DEFAULT_DEV_IP = "192.168.1.1";
    public static final int DEFAULT_FTP_PORT = 21;
    public static final int DEFAULT_HTTP_PORT = 8080;
    public static final String DEFAULT_PATH = "null";
    public static final String DEVICE_DESCRIPTION = "dev_desc.txt";
    public static final int DEVICE_LIST_FRAGMENT = 3;
    public static final String DEV_APP_LIST = "app_list";
    public static final int DEV_AP_MODE = 0;
    public static final String DEV_DEVICE_TYPE = "device_type";
    public static final String DEV_FIRMWARE_VERSION = "firmware_version";
    public static final String DEV_FRONT_SUPPORT = "forward_support";
    public static final String DEV_LIST = "dev_list";
    public static final String DEV_MATCH_ANDROID_VER = "match_android_ver";
    public static final String DEV_MATCH_APP = "match_app_type";
    public static final int DEV_PHOTO_FRAGMENT = 6;
    public static final String DEV_PRODUCT = "product_type";
    public static final String DEV_REAR_SUPPORT = "behind_support";
    public static final String DEV_REC_DUAL = "1";
    public static final String DEV_REC_FRONT_SUPPORT = "forward_record_support";
    public static final String DEV_REC_REAR_SUPPORT = "behind_record_support";
    public static final String DEV_REC_SINGLE = "0";
    public static final String DEV_RTSP_FRONT_SUPPORT = "rtsp_forward_support";
    public static final String DEV_RTSP_REAR_SUPPORT = "rtsp_behind_support";
    public static final String DEV_RTS_NET_TYPE = "net_type";
    public static final String DEV_RTS_TYPE = "rts_type";
    public static final int DEV_STA_MODE = 1;
    public static final String DEV_SUPPORT_BUMPING = "support_bumping";
    public static final String DEV_SUPPORT_PROJECTION = "support_projection";
    public static final String DEV_TYPE = "dev_type";
    public static final String DEV_UUID = "uuid";
    public static final String DEV_WORKSPACE_FRONT = "/DCIM/1";
    public static final String DEV_WORKSPACE_REAR = "/DCIM/2";
    public static final String DIR_DOWNLOAD = "Download";
    public static final String DIR_FRONT = "FMedia";
    public static final String DIR_REAR = "RMedia";
    public static final String DIR_RECORD = "Record";
    public static final String DIR_THUMB = ".thumbnail";
    public static final int DOWNLOAD_FILE_PROGRESS = 85;
    public static final int ERROR_CLOSE_MOV = 3810;
    public static final int ERROR_DATA_IS_NULL = 3813;
    public static final int ERROR_DEVICE_OFFLINE = 3812;
    public static final int ERROR_INIT_MOV = 3809;
    public static final int ERROR_NETWORK = 3811;
    public static final int ERROR_NETWORK_INFO_EMPTY = 61168;
    public static final int ERROR_NETWORK_NOT_OPEN = 61172;
    public static final int ERROR_NETWORK_TYPE_NOT_WIFI = 61169;
    public static final int ERROR_STORAGE = 3814;
    public static final int ERROR_WIFI_INFO_EMPTY = 61170;
    public static final int ERROR_WIFI_IS_CONNECTED = 61173;
    public static final int ERROR_WIFI_PWD_NOT_MATCH = 61171;
    public static final int FILE_DESC_TXT = 1;
    public static final int FILE_TYPE_INVALID = 0;
    public static final int FILE_TYPE_LATENCY = 3;
    public static final int FILE_TYPE_NORMAL = 1;
    public static final int FILE_TYPE_PIC = 1;
    public static final int FILE_TYPE_SOS = 2;
    public static final int FILE_TYPE_UNKNOWN = 0;
    public static final int FILE_TYPE_UPGRADE = 2;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final String FIRMWARE_DIR = "firmware";
    public static final int FRAME_TYPE_B = 46003;
    public static final int FRAME_TYPE_I = 41377;
    public static final int FRAME_TYPE_P = 49858;
    public static final int FRAME_TYPE_UNKNOWN = 0;
    public static final int FRONT_EMERGENCY_VIDEO_PORT = 10000;
    public static final String FTP_HOST_NAME = "cam.jieli.net";
    public static final String FTP_PASSWORD = "wifi123456";
    public static final String FTP_USER_NAME = "wifi@baidu.com";
    public static final String INSIDE_FTP_PASSWORD = "12345678";
    public static final String INSIDE_FTP_USER_NAME = "FTPX";
    public static final String JPG_PREFIX = "JPG";
    public static final String KEY_ALLOW_SAVE_DRIVING_DATA = "allow_save_driving_data";
    public static final String KEY_APP_LANGUAGE_CODE = "language_code";
    public static final String KEY_AUTO_DOWNLOAG_PICTURE = "auto_download_picture";
    public static final String KEY_BROWSE_OPERATION = "browse_operation";
    public static final String KEY_CONNECT_IP = "connect_ip";
    public static final String KEY_DATA = "key_data";
    public static final String KEY_DIR_PATH = "key_dir_path";
    public static final String KEY_FILE_INFO = "file_info";
    public static final String KEY_FRAGMENT_TAG = "key_fragment_tag";
    public static final String KEY_FRONT_RES_LEVEL = "rt_front_res_level";
    public static final String KEY_HARD_CODEC = "hard_codec";
    public static final String KEY_HAS_AGREED = "has_agreed_with_agreement";
    public static final String KEY_NONE = "NONE";
    public static final String KEY_OPEN_DEBUG = "open_debug";
    public static final String KEY_PATH_LIST = "path_list";
    public static final String KEY_POSITION = "position";
    public static final String KEY_PROJECTION_STATUS = "projection_status";
    public static final String KEY_REAR_RES_LEVEL = "rt_rear_res_level";
    public static final String KEY_ROOT_PATH_NAME = "key_root_path_name";
    public static final String KEY_RTSP = "rtsp_state";
    public static final String KEY_RTSP_FRONT_RES_LEVEL = "rtsp_front_res_level";
    public static final String KEY_RTSP_REAR_RES_LEVEL = "rtsp_rear_res_level";
    public static final String KEY_SAVE_PICTURE = "save_picture";
    public static final String KEY_SEARCH_MODE = "search_mode";
    public static final String KEY_SELECT_FILES_NUM = "select_files_num";
    public static final String KEY_SELECT_STATE = "select_state";
    public static final String KEY_STATE_TYPE = "state_type";
    public static final String KEY_TIME_FORMAT = "time_format";
    public static final String KEY_VIDEO_LIST = "video_list";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_WIFI_PWD = "wifi_pwd";
    public static final String KEY_WIFI_SSID = "wifi_ssid";
    public static final String KEY_WPA = "WPA_PSK";
    public static final String MEDIA_TASK = "media_task";
    public static final int MSG_UPDATE_DOWNLOAD_PROGRESS = 21862;
    public static final int MSG_UPDATE_UPLOAD_PROGRESS = 21863;
    public static final int MSG_UPGRADE_FILE = 81;
    public static final int OP_CANCEL_SELECT_ALL = 166;
    public static final int OP_CANCEL_TASK = 168;
    public static final int OP_DELETE_FILES = 164;
    public static final int OP_DOWNLOAD_FILES = 163;
    public static final int OP_ENTER_EDIT_MODE = 161;
    public static final int OP_EXIT_EDIT_MODE = 162;
    public static final int OP_SELECT_ALL = 165;
    public static final int OP_SHARE_FILES = 167;
    public static final int PAGE_LIMIT_COUNT = 18;
    public static final int PAGE_SIZE = 10;
    public static final int PERMISSION_CAMERA_CODE = 113;
    public static final int PERMISSION_LOCATION_CODE = 110;
    public static final int PERMISSION_MICROPHONE_CODE = 114;
    public static final int PERMISSION_SETTING_CODE = 112;
    public static final int PERMISSION_STORAGE_CODE = 111;
    public static final int PHOTO_VIEW_FRAGMENT = 7;
    public static final int PROJECTION_HEIGHT = 272;
    public static final int PROJECTION_WIDTH = 480;
    public static final int REAR_EMERGENCY_VIDEO_PORT = 10001;
    public static final String RECONNECT_TYPE = "reconnect_type";
    public static final int RECONNECT_TYPE_WIFI_DIRECT = 1;
    public static final String REC_PREFIX = "REC";
    public static final int REQUEST_MEDIA_PROJECTION = 4169;
    public static final int RESULT_CANCEL = 3;
    public static final int RESULT_DELETE_FILE = 84;
    public static final int RESULT_DOWNLOAD_FILE = 83;
    public static final int RESULT_FALSE = 0;
    public static final int RESULT_FILE_EXIST = 2;
    public static final int RESULT_SUCCESS = 1;
    public static final int RES_HD_HEIGHT = 720;
    public static final int RES_HD_WIDTH = 1280;
    public static final String ROOT_PATH;
    public static final int RTP_AUDIO_PORT1 = 1234;
    public static final int RTP_AUDIO_PORT2 = 1236;
    public static final int RTP_VIDEO_PORT1 = 6666;
    public static final int RTP_VIDEO_PORT2 = 6668;
    public static final int RTS_LEVEL_FHD = 2;
    public static final int RTS_LEVEL_HD = 1;
    public static final int RTS_LEVEL_SD = 0;
    public static final String RTS_NET_TYPE_TCP = "0";
    public static final String RTS_NET_TYPE_UDP = "1";
    public static final int RTS_TCP_PORT = 2223;
    public static final String RTS_TYPE_H264 = "1";
    public static final String RTS_TYPE_JPEG = "0";
    public static final int RTS_UDP_PORT = 2224;
    public static final int RTS_UDP_REAR_PORT = 2225;
    public static final String SCREEN_ORIENTATION = "screen_orientation";
    public static final int SDP_PORT = 6789;
    public static final int SDP_PORT2 = 6880;
    public static final String SDP_URL = "tcp://127.0.0.1:6789";
    public static final String SDP_URL2 = "tcp://127.0.0.1:6880";
    public static final String SERVICE_CMD = "service_command";
    public static final int SERVICE_CMD_CLOSE_SCREEN_TASK = 4;
    public static final int SERVICE_CMD_CONNECT_CTP = 1;
    public static final int SERVICE_CMD_DISCONNECT_CTP = 2;
    public static final int SERVICE_CMD_OPEN_SCREEN_TASK = 3;
    public static final int SERVICE_CMD_SCREEN_CHANGE = 5;
    public static final int SETTING_FRAGMENT = 4;
    public static final String SOS_PREFIX = "EME";
    public static final int STATE_END = 3;
    public static final int STATE_PROGRESS = 2;
    public static final int STATE_START = 1;
    public static final int STATUS_NOT_RECORD = 2;
    public static final int STATUS_PREPARE = 0;
    public static final int STATUS_RECORDING = 1;
    public static final int STA_SEARCH_MODE = 1;
    public static final String STR_RTSP = "RTSP";
    public static final String SUPPORT_BUMPING = "1";
    public static final String SUPPORT_PROJECTION = "1";
    public static final String TAG_BROWSE_FILE = "browse_file";
    public static final int THUMBNAIL_TCP_PORT = 2226;
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_SELECT_ALL = 2;
    public static final String UPDATE_PATH = "update_path";
    public static final String UPDATE_TYPE = "update_type";
    public static final String UPGRADE = "upgrade";
    public static final int UPGRADE_APK_TYPE = 1;
    public static final int UPGRADE_FRAGMENT = 5;
    public static final int UPGRADE_SDK_TYPE = 2;
    public static final String VERSION = "version";
    public static final String VERSION_JSON = "version.json";
    public static final String VIDEO_CREATE_TIME = "video_create_time";
    public static final int VIDEO_FRAME_RATE_DEFAULT = 30;
    public static final String VIDEO_OFFSET = "video_offset";
    public static final String VIDEO_PATH = "video_path";
    public static final int VIDEO_PLAYER_FRAGMENT = 8;
    public static final int VIDEO_SERVER_PORT = 2229;
    public static final int WIFI_CONNECTED = 1;
    public static final int WIFI_CONNECTING = 0;
    public static final int WIFI_CONNECT_FAILED = 2;
    public static final String WIFI_PREFIX;
    public static final int WIFI_UNKNOWN_ERROR = -1;
    public static final boolean isWifiP2pEnable = true;

    static {
        WIFI_PREFIX = MainApplication.isFactoryMode ? "" : "wifi_camera_";
        ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    }
}
