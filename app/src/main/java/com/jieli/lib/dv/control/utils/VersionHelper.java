package com.jieli.lib.dv.control.utils;

import android.content.Context;

import com.gizthon.camera.R;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class VersionHelper {
    public static String getSdkVersionName(Context context) {
        byte[] bArr = new byte[0];
        InputStream openRawResource = context.getResources().openRawResource(R.raw.version);
        try {
            try {
                try {
                    bArr = new byte[openRawResource.available()];
                } finally {
                    try {
                        Dlog.i("===", "InputStream closed");
                        openRawResource.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                Dlog.i("===", "InputStream closed");
                openRawResource.close();
            }
            if (openRawResource.read(bArr) > 0) {
                return new String(bArr);
            }
            Dlog.i("===", "InputStream closed");
            openRawResource.close();
            return "";
        } catch (IOException e3) {
            e3.printStackTrace();
            return "";
        }
    }
}
