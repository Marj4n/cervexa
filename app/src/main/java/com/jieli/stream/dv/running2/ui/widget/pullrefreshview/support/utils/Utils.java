package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils;

/* loaded from: classes.dex */
public class Utils {
    public static final boolean isClassExists(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
