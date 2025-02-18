package com.github.mikephil.charting.utils;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FileUtils {
    private static final String LOG = "MPChart-FileUtils";

    public static List<Entry> loadEntriesFromFile(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split("#");
                if (split.length <= 2) {
                    arrayList.add(new Entry(Float.parseFloat(split[0]), Integer.parseInt(split[1])));
                } else {
                    int length = split.length - 1;
                    float[] fArr = new float[length];
                    for (int i = 0; i < length; i++) {
                        fArr[i] = Float.parseFloat(split[i]);
                    }
                    arrayList.add(new BarEntry(Integer.parseInt(split[split.length - 1]), fArr));
                }
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());
        }
        return arrayList;
    }

    /* JADX WARN: Type inference failed for: r2v10, types: [float[]] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v13, types: [float] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:42:0x0081 -> B:19:0x0088). Please report as a decompilation issue!!! */
    public static List<Entry> loadEntriesFromAssets(AssetManager assetManager, String str) {
        BufferedReader bufferedReader;
        ?? r2;
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader2 = null;
        BufferedReader bufferedReader3 = null;
        BufferedReader bufferedReader4 = null;
        bufferedReader2 = null;
        try {
            try {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(str), Key.STRING_CHARSET_NAME));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
                e = e;
            }
        } catch (IOException e2) {
            Log.e(LOG, e2.toString());
            bufferedReader2 = bufferedReader2;
        }
        try {
            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                String[] split = readLine.split("#");
                if (split.length <= 2) {
                    r2 = Float.parseFloat(split[1]);
                    arrayList.add(new Entry(r2, Float.parseFloat(split[0])));
                } else {
                    int length = split.length - 1;
                    r2 = new float[length];
                    for (int i = 0; i < length; i++) {
                        r2[i] = Float.parseFloat(split[i]);
                    }
                    arrayList.add(new BarEntry(Integer.parseInt(split[split.length - 1]), (float[]) r2));
                }
                readLine = bufferedReader.readLine();
                bufferedReader3 = r2;
            }
            bufferedReader.close();
            bufferedReader2 = bufferedReader3;
        } catch (IOException e3) {
            e = e3;
            bufferedReader4 = bufferedReader;
            Log.e(LOG, e.toString());
            bufferedReader2 = bufferedReader4;
            if (bufferedReader4 != null) {
                bufferedReader4.close();
                bufferedReader2 = bufferedReader4;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e4) {
                    Log.e(LOG, e4.toString());
                }
            }
            throw th;
        }
        return arrayList;
    }

    public static void saveToSdCard(List<Entry> list, String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(LOG, e.toString());
            }
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            for (Entry entry : list) {
                bufferedWriter.append((CharSequence) (entry.getY() + "#" + entry.getX()));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e2) {
            Log.e(LOG, e2.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v9 */
    public static List<BarEntry> loadBarEntriesFromAssets(AssetManager assetManager, String str) {
        BufferedReader bufferedReader;
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader2 = null;
        float f = 0;
        bufferedReader2 = null;
        bufferedReader2 = null;
        try {
            try {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(str), Key.STRING_CHARSET_NAME));
                } catch (IOException e) {
                    Log.e(LOG, e.toString());
                }
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                String[] split = readLine.split("#");
                f = Float.parseFloat(split[1]);
                arrayList.add(new BarEntry(f, Float.parseFloat(split[0])));
            }
            bufferedReader.close();
            bufferedReader2 = f;
        } catch (IOException e3) {
            e = e3;
            bufferedReader2 = bufferedReader;
            Log.e(LOG, e.toString());
            if (bufferedReader2 != null) {
                bufferedReader2.close();
                bufferedReader2 = bufferedReader2;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e4) {
                    Log.e(LOG, e4.toString());
                }
            }
            throw th;
        }
        return arrayList;
    }
}
