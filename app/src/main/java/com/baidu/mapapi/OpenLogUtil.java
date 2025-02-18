package com.baidu.mapapi;

import com.baidu.platform.comjni.tools.a;

/* loaded from: classes.dex */
public class OpenLogUtil {
    private static ModuleName a;

    public static void setModuleLogEnable(ModuleName moduleName, boolean z) {
        a = moduleName;
        a.a(z, moduleName.ordinal());
    }
}
