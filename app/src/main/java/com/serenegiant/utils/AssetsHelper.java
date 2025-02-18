package com.serenegiant.utils;

import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: classes2.dex */
public class AssetsHelper {
    public static String loadString(AssetManager assetManager, String str) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        char[] cArr = new char[1024];
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(str)));
        for (int read = bufferedReader.read(cArr); read > 0; read = bufferedReader.read(cArr)) {
            stringBuffer.append(cArr, 0, read);
        }
        return stringBuffer.toString();
    }
}
