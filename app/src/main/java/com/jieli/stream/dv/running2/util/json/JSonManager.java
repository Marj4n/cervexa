package com.jieli.stream.dv.running2.util.json;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class JSonManager {
    private static final String TXT_CAMERA_TYPE = "c";
    private static final String TXT_CREATE_TIME = "t";
    private static final String TXT_DURATION = "d";
    private static final String TXT_FILE_END_TIME = "e";
    private static final String TXT_FILE_LIST = "file_list";
    private static final String TXT_HEIGHT = "h";
    private static final String TXT_PATH = "f";
    private static final String TXT_RATE = "p";
    private static final String TXT_SIZE = "s";
    private static final String TXT_TYPE = "y";
    private static final String TXT_WIDTH = "w";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    private static JSonManager instance = null;
    private static String mJSonData = null;
    private static String tag = "JSonManager";
    private List<FileInfo> mList = new ArrayList();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private JSonManager() {
    }

    public static JSonManager getInstance() {
        if (instance == null) {
            instance = new JSonManager();
        }
        return instance;
    }

    public void parseJSonData(String str, final OnCompletedListener<Boolean> onCompletedListener) {
        if (!TextUtils.isEmpty(str)) {
            mJSonData = str;
            new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.util.json.JSonManager.1
                @Override // java.lang.Runnable
                public void run() {
                    JSonManager.this.parseJSon(onCompletedListener);
                }
            }).start();
        } else {
            dispatchParseJSonState(onCompletedListener, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
    
        r6 = r5.getJSONArray(com.jieli.stream.dv.running2.util.json.JSonManager.TXT_FILE_LIST);
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void parseJSon(com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener<java.lang.Boolean> r17) {
        /*
            Method dump skipped, instructions count: 516
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.json.JSonManager.parseJSon(com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener):void");
    }

    private void setFileInfos(List<FileInfo> list) {
        synchronized (this.mList) {
            this.mList = list;
        }
    }

    private void dispatchParseJSonState(final OnCompletedListener<Boolean> onCompletedListener, final Boolean bool) {
        if (onCompletedListener != null) {
            this.mHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.json.JSonManager.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!bool.booleanValue()) {
                        JSonManager.this.mList.clear();
                    }
                    onCompletedListener.onCompleted(bool);
                }
            });
        }
    }

    public List<FileInfo> getInfoList() {
        List<FileInfo> list;
        synchronized (this.mList) {
            list = this.mList;
        }
        return list;
    }

    public List<FileInfo> getVideoInfoList() {
        ArrayList arrayList;
        synchronized (this.mList) {
            if (TextUtils.isEmpty(mJSonData)) {
                throw new NullPointerException("JSon data is null");
            }
            arrayList = new ArrayList();
            for (int i = 0; i < this.mList.size(); i++) {
                if (this.mList.get(i).isVideo()) {
                    arrayList.add(this.mList.get(i));
                }
            }
        }
        return arrayList;
    }

    public List<FileInfo> getPictureInfoList() {
        ArrayList arrayList;
        synchronized (this.mList) {
            if (TextUtils.isEmpty(mJSonData)) {
                throw new NullPointerException("JSon data is null");
            }
            arrayList = new ArrayList();
            for (int i = 0; i < this.mList.size(); i++) {
                if (!this.mList.get(i).isVideo()) {
                    arrayList.add(this.mList.get(i));
                }
            }
        }
        return arrayList;
    }

    public static FileInfo parseFileInfo(String str) {
        String lowerCase;
        FileInfo fileInfo;
        Date date;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString(TXT_PATH);
                if (!TextUtils.isEmpty(optString)) {
                    lowerCase = optString.substring(optString.lastIndexOf(".") + 1).toLowerCase();
                    fileInfo = new FileInfo();
                    switch (lowerCase) {
                        case "jpeg":
                        case "JPEG":
                        case "jpg":
                        case "JPG":
                            fileInfo.setVideo(false);
                            break;
                        case "mov":
                        case "MOV":
                        case "avi":
                            fileInfo.setVideo(true);
                            break;
                        default:
                            Dlog.e(tag, "error:" + lowerCase);
                            return null;
                    }
                    fileInfo.setType(jSONObject.optInt(TXT_TYPE));
                    fileInfo.setDuration(jSONObject.optInt(TXT_DURATION));
                    fileInfo.setHeight(jSONObject.optInt("h"));
                    fileInfo.setWidth(jSONObject.optInt("w"));
                    fileInfo.setRate(jSONObject.optInt(TXT_RATE));
                    fileInfo.setName(optString.substring(optString.lastIndexOf("/") + 1));
                    fileInfo.setPath(jSONObject.optString(TXT_PATH));
                    fileInfo.setCreateTime(jSONObject.optString("t"));
                    fileInfo.setFileEndTime(jSONObject.optString(TXT_FILE_END_TIME));
                    fileInfo.setSize(jSONObject.optLong(TXT_SIZE));
                    if (jSONObject.has(TXT_CAMERA_TYPE)) {
                        fileInfo.setCameraType(jSONObject.optString(TXT_CAMERA_TYPE));
                    }
                    if (fileInfo.isVideo()) {
                        try {
                            date = dateFormat.parse(jSONObject.optString("t"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date = null;
                        }
                        Calendar calendar = Calendar.getInstance();
                        if (date != null) {
                            calendar.setTime(date);
                            fileInfo.setStartTime(calendar);
                        } else {
                            Dlog.e(tag, "Parse start time string fail!");
                        }
                        Calendar calendar2 = Calendar.getInstance();
                        if (date != null) {
                            calendar2.setTime(date);
                            calendar2.set(13, calendar2.get(13) + fileInfo.getDuration());
                            fileInfo.setEndTime(calendar2);
                        } else {
                            Dlog.e(tag, "Parse end time string fail!");
                        }
                    }
                    return fileInfo;
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static String convertJson(List<FileInfo> list) {
        String str;
        if (list == null) {
            str = "";
        } else if (list.size() > 0) {
            String str2 = "{\"file_list\": [";
            for (int i = 0; i < list.size(); i++) {
                FileInfo fileInfo = list.get(i);
                if (fileInfo != null) {
                    str2 = str2 + "{\n\"y\" : " + fileInfo.getType() + ",\n\"" + TXT_PATH + "\" : \"" + fileInfo.getPath() + "\",\n\"t\" : \"" + fileInfo.getCreateTime() + "\",\n\"" + TXT_FILE_END_TIME + "\" : \"" + fileInfo.getFileEndTime() + "\",\n\"" + TXT_DURATION + "\" : \"" + fileInfo.getDuration() + "\",\n\"h\" : " + fileInfo.getHeight() + ",\n\"w\" : " + fileInfo.getWidth() + ",\n\"" + TXT_RATE + "\" : " + fileInfo.getRate() + ",\n\"" + TXT_CAMERA_TYPE + "\" : " + fileInfo.getCameraType() + ",\n\"" + TXT_SIZE + "\" : \"" + fileInfo.getSize() + "\"\n}";
                    if (i != list.size() - 1) {
                        str2 = str2 + ",\n";
                    }
                }
            }
            str = str2 + "\n]}";
        } else {
            str = "{\"file_list\": []}";
        }
        mJSonData = str;
        return str;
    }

    public String getVideosDescription() {
        return mJSonData;
    }

    public void release() {
        instance = null;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        clearData();
    }

    public void clearData() {
        mJSonData = null;
        List<FileInfo> list = this.mList;
        if (list != null) {
            list.clear();
        }
    }
}
