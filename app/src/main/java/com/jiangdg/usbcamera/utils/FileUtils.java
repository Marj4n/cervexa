package com.jiangdg.usbcamera.utils;

import android.os.Environment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileUtils {
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private static BufferedOutputStream outputStream;

    public static void createfile(String str) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void releaseFile() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void putFileStream(byte[] bArr, int i, int i2) {
        BufferedOutputStream bufferedOutputStream = outputStream;
        if (bufferedOutputStream != null) {
            try {
                bufferedOutputStream.write(bArr, i, i2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putFileStream(byte[] bArr) {
        BufferedOutputStream bufferedOutputStream = outputStream;
        if (bufferedOutputStream != null) {
            try {
                bufferedOutputStream.write(bArr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
