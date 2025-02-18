package com.baidu.platform.comjni.tools;

import android.os.Bundle;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BundleKeySet {
    public String[] getBundleKeys(Bundle bundle) {
        String[] strArr = null;
        if (bundle == null) {
            return null;
        }
        if (!bundle.isEmpty()) {
            strArr = new String[bundle.size()];
            int i = 0;
            Iterator<String> it = bundle.keySet().iterator();
            while (it.hasNext()) {
                strArr[i] = it.next().toString();
                i++;
            }
        }
        return strArr;
    }
}
