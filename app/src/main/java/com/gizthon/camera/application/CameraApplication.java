package com.gizthon.camera.application;

import com.jieli.stream.dv.running2.ui.MainApplication;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class CameraApplication extends MainApplication {
    public static final String DIRECTORY_NAME = "/MergeCamera/Media/Photo/Screen/";

    @Override // com.jieli.stream.dv.running2.ui.MainApplication, android.app.Application
    public void onCreate() {
        super.onCreate();
        closeAndroid10Dialog();
    }

    public void closeAndroid10Dialog() {
        try {
            Class.forName("android.content.pm.PackageParser$Package").getDeclaredConstructor(String.class).setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mHiddenApiWarningShown");
            declaredField.setAccessible(true);
            declaredField.setBoolean(invoke, true);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
