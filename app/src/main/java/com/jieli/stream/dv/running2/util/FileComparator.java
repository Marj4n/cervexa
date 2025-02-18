package com.jieli.stream.dv.running2.util;

import androidx.core.app.NotificationManagerCompat;
import com.jieli.stream.dv.running2.bean.SDFileInfo;
import java.util.Comparator;

/* loaded from: classes.dex */
public class FileComparator implements Comparator<SDFileInfo> {
    @Override // java.util.Comparator
    public int compare(SDFileInfo sDFileInfo, SDFileInfo sDFileInfo2) {
        if (sDFileInfo.IsDirectory && !sDFileInfo2.IsDirectory) {
            return NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        }
        if (sDFileInfo.IsDirectory || !sDFileInfo2.IsDirectory) {
            return sDFileInfo.Name.compareTo(sDFileInfo2.Name);
        }
        return 1000;
    }
}
