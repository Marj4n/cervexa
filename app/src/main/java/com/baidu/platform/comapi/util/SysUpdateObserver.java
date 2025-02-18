package com.baidu.platform.comapi.util;

import android.content.Context;

/* loaded from: classes.dex */
public interface SysUpdateObserver {
    void init();

    void updateNetworkInfo(Context context);

    void updateNetworkProxy(Context context);

    void updatePhoneInfo();
}
