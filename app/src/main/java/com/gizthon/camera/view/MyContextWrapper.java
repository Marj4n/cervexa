package com.gizthon.camera.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

/* loaded from: classes.dex */
public class MyContextWrapper extends ContextWrapper {
    public MyContextWrapper(Context context) {
        super(context);
    }

    public static MyContextWrapper wrap(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= 19) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        }
        return new MyContextWrapper(context);
    }
}
