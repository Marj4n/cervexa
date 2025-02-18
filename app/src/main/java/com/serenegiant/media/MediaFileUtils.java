package com.serenegiant.media;

import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaFileUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaFileUtils";
    private static final SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    public static final File getCaptureFile(String str, String str2, String str3) {
        File file = new File(Environment.getExternalStoragePublicDirectory(str2), str);
        file.mkdirs();
        if (!file.canWrite()) {
            return null;
        }
        return new File(file, getDateTimeString() + str3);
    }

    private static final String getDateTimeString() {
        return sDateTimeFormat.format(new GregorianCalendar().getTime());
    }
}
