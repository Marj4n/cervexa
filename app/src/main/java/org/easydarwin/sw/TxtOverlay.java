package org.easydarwin.sw;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class TxtOverlay {
    private static TxtOverlay instance;
    private final Context context;
    private long ctx;

    private static native void txtOverlay(long j, byte[] bArr, String str);

    private static native long txtOverlayInit(int i, int i2, String str);

    private static native void txtOverlayRelease(long j);

    static {
        System.loadLibrary("TxtOverlay");
    }

    private TxtOverlay(Context context) {
        this.context = context;
    }

    public static TxtOverlay getInstance() {
        TxtOverlay txtOverlay = instance;
        if (txtOverlay != null) {
            return txtOverlay;
        }
        throw new IllegalArgumentException("please call install in your application!");
    }

    public static void install(Context context) {
        if (instance != null) {
            return;
        }
        instance = new TxtOverlay(context.getApplicationContext());
        if (context.getFileStreamPath("SIMYOU.ttf").exists()) {
            return;
        }
        try {
            InputStream open = context.getAssets().open("zk/SIMYOU.ttf");
            FileOutputStream openFileOutput = context.openFileOutput("SIMYOU.ttf", 0);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    openFileOutput.write(bArr, 0, read);
                } else {
                    openFileOutput.close();
                    open.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int i, int i2) {
        File fileStreamPath = this.context.getFileStreamPath("SIMYOU.ttf");
        if (!fileStreamPath.exists()) {
            throw new IllegalArgumentException("the font file must be exists,please call install before!");
        }
        this.ctx = txtOverlayInit(i, i2, fileStreamPath.getAbsolutePath());
    }

    public void overlay(byte[] bArr, String str) {
        long j = this.ctx;
        if (j == 0) {
            return;
        }
        txtOverlay(j, bArr, str);
    }

    public void release() {
        long j = this.ctx;
        if (j == 0) {
            return;
        }
        txtOverlayRelease(j);
        this.ctx = 0L;
    }
}
