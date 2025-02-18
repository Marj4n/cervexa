package com.serenegiant.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class MemInfo {
    public static JSONObject get(Context context) throws JSONException {
        String readLine;
        JSONObject jSONObject = new JSONObject();
        try {
            try {
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("availMem", memoryInfo.availMem);
                jSONObject2.put("totalMem", memoryInfo.totalMem);
                jSONObject2.put("threshold", memoryInfo.threshold);
                jSONObject2.put("lowMemory", memoryInfo.lowMemory);
                jSONObject.put("ACTIVITYMANAGER_MEMORYINFO", jSONObject2);
            } catch (Exception e) {
                jSONObject.put("ACTIVITYMANAGER_MEMORYINFO", e.getMessage());
            }
            try {
                Debug.MemoryInfo memoryInfo2 = new Debug.MemoryInfo();
                Debug.getMemoryInfo(memoryInfo2);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("TotalPss", memoryInfo2.getTotalPss());
                jSONObject3.put("TotalPrivateDirty", memoryInfo2.getTotalPrivateDirty());
                jSONObject3.put("TotalSharedDirty", memoryInfo2.getTotalSharedDirty());
                if (BuildCheck.isAndroid4_4()) {
                    jSONObject3.put("TotalPrivateClean", memoryInfo2.getTotalPrivateClean());
                    jSONObject3.put("TotalSharedClean", memoryInfo2.getTotalSharedClean());
                    jSONObject3.put("TotalSwappablePss", memoryInfo2.getTotalSwappablePss());
                }
                jSONObject.put("DEBUG_MEMORYINFO", jSONObject3);
            } catch (Exception e2) {
                jSONObject.put("DEBUG_MEMORYINFO", e2.getMessage());
            }
            try {
                JSONObject jSONObject4 = new JSONObject();
                int i = 0;
                BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 512);
                do {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    if (!TextUtils.isEmpty(readLine)) {
                        jSONObject4.put(Integer.toString(i), readLine);
                        i++;
                    }
                } while (readLine != null);
                bufferedReader.close();
                jSONObject.put("PROC_MEMINFO", jSONObject4);
            } catch (Exception e3) {
                jSONObject.put("PROC_MEMINFO", e3.getMessage());
            }
        } catch (Exception e4) {
            jSONObject.put("EXCEPTION", e4.getMessage());
        }
        return jSONObject;
    }
}
