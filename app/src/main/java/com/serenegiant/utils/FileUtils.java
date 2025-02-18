package com.serenegiant.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.serenegiant.media.Recorder;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/* loaded from: classes2.dex */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    public static String DIR_NAME = "UsbWebCamera";
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    public static float FREE_RATIO = 0.03f;
    public static float FREE_SIZE_OFFSET = 2.097152E7f;
    public static float FREE_SIZE = 3.145728E8f;
    public static float FREE_SIZE_MINUTE = 4.194304E7f;
    public static long CHECK_INTERVAL = Recorder.CHECK_INTERVAL;

    public static String getDirName() {
        return TextUtils.isEmpty(DIR_NAME) ? "Serenegiant" : DIR_NAME;
    }

    public static final File getCaptureFile(Context context, String str, String str2, int i) {
        return getCaptureFile(context, str, null, str2, i);
    }

    public static final File getCaptureFile(Context context, String str, String str2, String str3, int i) {
        String str4;
        File captureDir;
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(str2)) {
            str4 = getDateTimeString();
        } else {
            str4 = str2 + getDateTimeString();
        }
        sb.append(str4);
        sb.append(str3);
        String sb2 = sb.toString();
        File file = null;
        if (i > 0 && SAFUtils.hasStorageAccess(context, i)) {
            File createStorageDir = SAFUtils.createStorageDir(context, i);
            if (createStorageDir == null || !createStorageDir.canWrite()) {
                Log.w(TAG, "なんでか書き込めん");
            } else {
                file = createStorageDir;
            }
            if (file != null) {
                file = new File(file, getDirName());
            }
        }
        if (file == null && (captureDir = getCaptureDir(context, str, 0)) != null) {
            captureDir.mkdirs();
            if (captureDir.canWrite()) {
                file = captureDir;
            }
        }
        return file != null ? new File(file, sb2) : file;
    }

    public static final File getCaptureDir(Context context, String str, int i) {
        File file;
        File createStorageDir = (i <= 0 || !SAFUtils.hasStorageAccess(context, i)) ? null : SAFUtils.createStorageDir(context, i);
        if (createStorageDir != null) {
            file = new File(createStorageDir, getDirName());
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(str), getDirName());
        }
        file.mkdirs();
        if (file.canWrite()) {
            return file;
        }
        return null;
    }

    public static final String getDateTimeString() {
        return mDateTimeFormat.format(new GregorianCalendar().getTime());
    }

    public static String getExternalMounts() {
        String[] split;
        String str = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("mount").getInputStream()));
            String str2 = "";
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                if (!readLine.contains("secure") && !readLine.contains("asec")) {
                    if (readLine.contains("fat")) {
                        String[] split2 = readLine.split(" ");
                        if (split2 != null && split2.length > 1 && !TextUtils.isEmpty(split2[1])) {
                            str = split2[1];
                            if (!str.endsWith("/")) {
                                str = str + "/";
                            }
                        }
                    } else if (readLine.contains("fuse") && (split = readLine.split(" ")) != null && split.length > 1) {
                        str2 = str2.concat("[" + split[1] + "]");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static StorageInfo getStorageInfo(Context context, String str, int i) {
        if (context == null) {
            return null;
        }
        try {
            File captureDir = getCaptureDir(context, str, i);
            if (captureDir != null) {
                return new StorageInfo(captureDir.getTotalSpace(), (long) (captureDir.canWrite() ? captureDir.getUsableSpace() : 0.0f));
            }
            return null;
        } catch (Exception e) {
            Log.w("getStorageInfo:", e);
            return null;
        }
    }

    public static final boolean checkFreeSpace(Context context, long j, long j2, int i) {
        if (context == null) {
            return false;
        }
        return checkFreeSpace(context, FREE_RATIO, j > 0 ? (((j - (System.currentTimeMillis() - j2)) / 60000.0f) * FREE_SIZE_MINUTE) + FREE_SIZE_OFFSET : FREE_SIZE, i);
    }

    public static final boolean checkFreeSpace(Context context, float f, float f2, int i) {
        if (context == null) {
            return false;
        }
        try {
            File captureDir = getCaptureDir(context, Environment.DIRECTORY_DCIM, i);
            if (captureDir == null) {
                return false;
            }
            float usableSpace = captureDir.canWrite() ? captureDir.getUsableSpace() : 0.0f;
            if (captureDir.getTotalSpace() > 0) {
                return usableSpace / ((float) captureDir.getTotalSpace()) > f || usableSpace > f2;
            }
            return false;
        } catch (Exception e) {
            Log.w("checkFreeSpace:", e);
            return false;
        }
    }

    public static final long getAvailableFreeSpace(Context context, String str, int i) {
        File captureDir;
        if (context == null || (captureDir = getCaptureDir(context, str, i)) == null || !captureDir.canWrite()) {
            return 0L;
        }
        return captureDir.getUsableSpace();
    }

    public static final float getFreeRatio(Context context, String str, int i) {
        File captureDir;
        if (context != null && (captureDir = getCaptureDir(context, str, i)) != null) {
            float usableSpace = captureDir.canWrite() ? captureDir.getUsableSpace() : 0.0f;
            if (captureDir.getTotalSpace() > 0) {
                return usableSpace / captureDir.getTotalSpace();
            }
        }
        return 0.0f;
    }

    public static final String removeFileExtension(String str) {
        int lastIndexOf = !TextUtils.isEmpty(str) ? str.lastIndexOf(".") : -1;
        return lastIndexOf > 0 ? str.substring(0, lastIndexOf) : str;
    }

    public static final String replaceFileExtension(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf > 0) {
            return str.substring(0, lastIndexOf) + str2;
        }
        return str + str2;
    }
}
