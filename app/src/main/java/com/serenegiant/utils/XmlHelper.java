package com.serenegiant.utils;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes2.dex */
public class XmlHelper {
    public static final int getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, int i) {
        try {
            return ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), i);
        } catch (Exception unused) {
            return i;
        }
    }

    public static final boolean getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, boolean z) {
        try {
            return ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), z);
        } catch (Exception unused) {
            return z;
        }
    }

    public static final String getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, String str3) {
        try {
            return ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), str3);
        } catch (Exception unused) {
            return str3;
        }
    }

    public static final CharSequence getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, CharSequence charSequence) {
        try {
            return ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), charSequence);
        } catch (Exception unused) {
            return charSequence;
        }
    }

    public static final int[] getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, int[] iArr) {
        String attribute = getAttribute(context, xmlPullParser, str, str2, "");
        if (!TextUtils.isEmpty(attribute)) {
            String[] split = attribute.split(",");
            ArrayList arrayList = new ArrayList();
            int length = split.length;
            int i = 0;
            for (String str3 : split) {
                try {
                    arrayList.add(Integer.valueOf(ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), 0)));
                } catch (Exception unused) {
                }
            }
            if (arrayList.size() > 0) {
                iArr = new int[arrayList.size()];
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    iArr[i] = ((Integer) it.next()).intValue();
                    i++;
                }
            }
        }
        return iArr;
    }

    public static final boolean[] getAttribute(Context context, XmlPullParser xmlPullParser, String str, String str2, boolean[] zArr) {
        String attribute = getAttribute(context, xmlPullParser, str, str2, "");
        if (!TextUtils.isEmpty(attribute)) {
            String[] split = attribute.split(",");
            ArrayList arrayList = new ArrayList();
            int length = split.length;
            int i = 0;
            for (String str3 : split) {
                try {
                    arrayList.add(Boolean.valueOf(ResourceHelper.get(context, xmlPullParser.getAttributeValue(str, str2), false)));
                } catch (Exception unused) {
                }
            }
            if (arrayList.size() > 0) {
                zArr = new boolean[arrayList.size()];
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    zArr[i] = ((Boolean) it.next()).booleanValue();
                    i++;
                }
            }
        }
        return zArr;
    }
}
