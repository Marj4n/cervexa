package com.jieli.stream.dv.running2.util;

import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import com.jieli.stream.dv.running2.bean.SDFileInfo;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public class FileUtil {
    public static String getSDPath() {
        return Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory().getPath() : "/sdcard";
    }

    public static SDFileInfo getFileInfo(File file) {
        SDFileInfo sDFileInfo = new SDFileInfo();
        sDFileInfo.Name = file.getName();
        sDFileInfo.IsDirectory = file.isDirectory();
        calcFileContent(sDFileInfo, file);
        return sDFileInfo;
    }

    private static void calcFileContent(SDFileInfo sDFileInfo, File file) {
        File[] listFiles;
        if (file.isFile()) {
            sDFileInfo.Size += file.length();
        }
        if (!file.isDirectory() || (listFiles = file.listFiles()) == null || listFiles.length <= 0) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                sDFileInfo.FolderCount++;
            } else if (file2.isFile()) {
                sDFileInfo.FileCount++;
            }
            if (sDFileInfo.FileCount + sDFileInfo.FolderCount >= 10000) {
                return;
            }
            calcFileContent(sDFileInfo, file2);
        }
    }

    public static String formetFileSize(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (j < 1024) {
            return j + " B";
        }
        if (j < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            return decimalFormat.format(j / 1024.0d) + " K";
        }
        if (j < IjkMediaMeta.AV_CH_STEREO_RIGHT) {
            return decimalFormat.format(j / 1048576.0d) + " M";
        }
        return decimalFormat.format(j / 1.073741824E9d) + " G";
    }

    public static String combinPath(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str.endsWith(File.separator) ? "" : File.separator);
        sb.append(str2);
        return sb.toString();
    }

    public static boolean copyFile(File file, File file2) throws Exception {
        if (file.isFile()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bArr = new byte[8192];
            for (int read = bufferedInputStream.read(bArr); read != -1; read = bufferedInputStream.read(bArr)) {
                bufferedOutputStream.write(bArr, 0, read);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
        }
        if (!file.isDirectory()) {
            return true;
        }
        File[] listFiles = file.listFiles();
        file2.mkdir();
        for (File file3 : listFiles) {
            copyFile(file3.getAbsoluteFile(), new File(file2.getAbsoluteFile() + File.separator + file3.getName()));
        }
        return true;
    }

    public static boolean moveFile(String str, String str2) {
        File file = new File(str);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
        File file2 = new File(str2);
        if (!file2.exists() && !file2.mkdirs()) {
            return false;
        }
        return file.renameTo(new File(str2 + File.separator + file.getName()));
    }

    public static boolean deleteFile(File file) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                deleteFile(file2);
            }
        }
        return file.delete();
    }

    public static String getMIMEType(String str) {
        String str2;
        String lowerCase = str.substring(str.lastIndexOf(".") + 1, str.length()).toLowerCase();
        if (lowerCase.equals("apk")) {
            return "application/vnd.android.package-archive";
        }
        if (lowerCase.equals("mp4") || lowerCase.equals("avi") || lowerCase.equals("3gp") || lowerCase.equals("rmvb")) {
            str2 = "video";
        } else if (lowerCase.equals("m4a") || lowerCase.equals("mp3") || lowerCase.equals("mid") || lowerCase.equals("xmf") || lowerCase.equals("ogg") || lowerCase.equals("wav")) {
            str2 = "audio";
        } else if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg") || lowerCase.equals("bmp")) {
            str2 = "image";
        } else {
            str2 = (lowerCase.equals("txt") || lowerCase.equals("log")) ? "text" : "*";
        }
        return str2 + "/*";
    }

    public static boolean moveDirectory(String str, String str2) {
        File file = new File(str);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        File file2 = new File(str2);
        if (!file2.exists() && !file2.mkdirs()) {
            return false;
        }
        for (File file3 : file.listFiles()) {
            if (file3.isFile()) {
                moveFile(file3.getAbsolutePath(), file2.getAbsolutePath());
            } else if (file3.isDirectory()) {
                moveDirectory(file3.getAbsolutePath(), file2.getAbsolutePath() + File.separator + file3.getName());
            }
        }
        return file.delete();
    }
}
