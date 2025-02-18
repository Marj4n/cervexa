package com.zh_jieli.juson.netcheck;

import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class OuterChecker {
    public static final int TIMEOUT_MS_DEFAULT = 500;

    private static native int na_check(int i);

    private static native int na_checkWithAddresses(List list, int i);

    static {
        System.loadLibrary("outerchecker");
    }

    public static boolean check(int i) {
        return OuterCheckerImpl.check(i);
    }

    public static boolean check(List<HashMap> list, int i) {
        return OuterCheckerImpl.check(list, i);
    }
}
