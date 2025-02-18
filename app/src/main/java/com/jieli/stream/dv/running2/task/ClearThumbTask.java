package com.jieli.stream.dv.running2.task;

import android.text.TextUtils;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.AppUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ClearThumbTask extends Thread {
    private String appRootPath = AppUtils.splicingFilePath(MainApplication.getApplication().getAppName(), null, null, null);
    private boolean isExitClear;
    private OnClearThumbTaskListener listener;

    public interface OnClearThumbTaskListener {
        void onFinish();
    }

    public void stopClear() {
        this.isExitClear = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        long cache = getCache();
        while (!this.isExitClear && cache >= 209715200 && !TextUtils.isEmpty(this.appRootPath)) {
            clearOldThumbCache();
            cache = getCache();
        }
        OnClearThumbTaskListener onClearThumbTaskListener = this.listener;
        if (onClearThumbTaskListener != null) {
            onClearThumbTaskListener.onFinish();
        }
    }

    public void setOnClearThumbTaskListener(OnClearThumbTaskListener onClearThumbTaskListener) {
        this.listener = onClearThumbTaskListener;
    }

    private long getCache() {
        List<String> queryThumbDirPath = AppUtils.queryThumbDirPath(this.appRootPath);
        long j = 0;
        if (queryThumbDirPath != null && queryThumbDirPath.size() > 0) {
            Iterator<String> it = queryThumbDirPath.iterator();
            while (it.hasNext()) {
                File file = new File(it.next());
                if (file.exists()) {
                    try {
                        j += AppUtils.getFolderSize(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return j;
    }

    private boolean deleteFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    private void clearOldThumbCache() {
        int size;
        int i;
        List<FileInfo> queryThumbInfoList = AppUtils.queryThumbInfoList(this.appRootPath);
        if (queryThumbInfoList == null || (size = queryThumbInfoList.size()) <= 0 || (i = size / 3) <= 0) {
            return;
        }
        ArrayList<FileInfo> arrayList = new ArrayList();
        arrayList.addAll(queryThumbInfoList.subList(size - i, size));
        for (FileInfo fileInfo : arrayList) {
            if (fileInfo != null) {
                deleteFile(fileInfo.getPath());
            }
        }
    }
}
