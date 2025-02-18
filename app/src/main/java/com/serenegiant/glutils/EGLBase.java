package com.serenegiant.glutils;

import android.os.Build;
import com.serenegiant.glutils.EGLBase10;
import com.serenegiant.glutils.EGLBase14;

/* loaded from: classes.dex */
public abstract class EGLBase {
    public static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    public static final Object EGL_LOCK = new Object();
    public static final int EGL_OPENGL_ES2_BIT = 4;
    public static final int EGL_OPENGL_ES3_BIT_KHR = 64;
    public static final int EGL_RECORDABLE_ANDROID = 12610;

    public static abstract class IConfig {
    }

    public static abstract class IContext {
        public abstract Object getEGLContext();

        public abstract long getNativeHandle();
    }

    public interface IEglSurface {
        IContext getContext();

        boolean isValid();

        void makeCurrent();

        void release();

        void swap();

        void swap(long j);
    }

    public abstract IEglSurface createFromSurface(Object obj);

    public abstract IEglSurface createOffscreen(int i, int i2);

    public abstract IConfig getConfig();

    public abstract IContext getContext();

    public abstract int getGlVersion();

    public abstract void makeDefault();

    public abstract String queryString(int i);

    public abstract void release();

    public abstract void sync();

    public static EGLBase createFrom(IContext iContext, boolean z, boolean z2) {
        return createFrom(3, iContext, z, 0, z2);
    }

    public static EGLBase createFrom(IContext iContext, boolean z, int i, boolean z2) {
        return createFrom(3, iContext, z, i, z2);
    }

    public static EGLBase createFrom(int i, IContext iContext, boolean z, int i2, boolean z2) {
        if (isEGL14Supported() && (iContext == null || (iContext instanceof EGLBase14.Context))) {
            return new EGLBase14(i, (EGLBase14.Context) iContext, z, i2, z2);
        }
        return new EGLBase10(i, (EGLBase10.Context) iContext, z, i2, z2);
    }

    public static boolean isEGL14Supported() {
        return Build.VERSION.SDK_INT >= 18;
    }
}
