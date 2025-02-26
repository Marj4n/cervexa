package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.serenegiant.glutils.EGLBase;
import com.serenegiant.utils.BuildCheck;

/* loaded from: classes.dex */
class EGLBase14 extends EGLBase {
    private static final Context EGL_NO_CONTEXT = new Context(EGL14.EGL_NO_CONTEXT);
    private static final String TAG = "EGLBase14";
    private Config mEglConfig = null;
    private Context mContext = EGL_NO_CONTEXT;
    private EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mDefaultContext = EGL14.EGL_NO_CONTEXT;
    private int mGlVersion = 2;
    private final int[] mSurfaceDimension = new int[2];

    public static class Context extends EGLBase.IContext {
        public final EGLContext eglContext;

        private Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }

        @Override // com.serenegiant.glutils.EGLBase.IContext
        public long getNativeHandle() {
            if (this.eglContext != null) {
                return BuildCheck.isLollipop() ? this.eglContext.getNativeHandle() : this.eglContext.getHandle();
            }
            return 0L;
        }

        @Override // com.serenegiant.glutils.EGLBase.IContext
        public Object getEGLContext() {
            return this.eglContext;
        }
    }

    public static class Config extends EGLBase.IConfig {
        public final EGLConfig eglConfig;

        private Config(EGLConfig eGLConfig) {
            this.eglConfig = eGLConfig;
        }
    }

    public static class EglSurface implements EGLBase.IEglSurface {
        private final EGLBase14 mEglBase;
        private EGLSurface mEglSurface;

        private EglSurface(EGLBase14 eGLBase14, Object obj) throws IllegalArgumentException {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            this.mEglBase = eGLBase14;
            if ((obj instanceof Surface) || (obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceView)) {
                this.mEglSurface = this.mEglBase.createWindowSurface(obj);
                return;
            }
            throw new IllegalArgumentException("unsupported surface");
        }

        private EglSurface(EGLBase14 eGLBase14, int i, int i2) {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            this.mEglBase = eGLBase14;
            if (i <= 0 || i2 <= 0) {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
            } else {
                this.mEglSurface = eGLBase14.createOffscreenSurface(i, i2);
            }
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public void makeCurrent() {
            this.mEglBase.makeCurrent(this.mEglSurface);
            if (this.mEglBase.getGlVersion() >= 2) {
                GLES20.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            } else {
                GLES10.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            }
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public void swap() {
            this.mEglBase.swap(this.mEglSurface);
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public void swap(long j) {
            this.mEglBase.swap(this.mEglSurface, j);
        }

        public void setPresentationTime(long j) {
            EGLExt.eglPresentationTimeANDROID(this.mEglBase.mEglDisplay, this.mEglSurface, j);
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public EGLBase.IContext getContext() {
            return this.mEglBase.getContext();
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public boolean isValid() {
            EGLSurface eGLSurface = this.mEglSurface;
            return eGLSurface != null && eGLSurface != EGL14.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public void release() {
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    public EGLBase14(int i, Context context, boolean z, int i2, boolean z2) {
        init(i, context, z, i2, z2);
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void release() {
        if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            destroyContext();
            EGL14.eglTerminate(this.mEglDisplay);
            EGL14.eglReleaseThread();
        }
        this.mEglDisplay = EGL14.EGL_NO_DISPLAY;
        this.mContext = EGL_NO_CONTEXT;
    }

    public EglSurface createFromSurface(Object obj) {
        EglSurface eglSurface = new EglSurface(this, obj);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public EglSurface createOffscreen(int i, int i2) {
        EglSurface eglSurface = new EglSurface(this, i, i2);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public String queryString(int i) {
        return EGL14.eglQueryString(this.mEglDisplay, i);
    }

    @Override // com.serenegiant.glutils.EGLBase
    public int getGlVersion() {
        return this.mGlVersion;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public Config getConfig() {
        return this.mEglConfig;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void makeDefault() {
        if (EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            return;
        }
        Log.w("TAG", "makeDefault" + EGL14.eglGetError());
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void sync() {
        EGL14.eglWaitGL();
        EGL14.eglWaitNative(12379);
    }

    private void init(int i, Context context, boolean z, int i2, boolean z2) {
        Context context2;
        EGLConfig config;
        if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("EGL already set up");
        }
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        this.mEglDisplay = eglGetDisplay;
        if (eglGetDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }
        int[] iArr = new int[2];
        if (!EGL14.eglInitialize(this.mEglDisplay, iArr, 0, iArr, 1)) {
            this.mEglDisplay = null;
            throw new RuntimeException("eglInitialize failed");
        }
        if (context == null) {
            context = EGL_NO_CONTEXT;
        }
        if (i >= 3 && (config = getConfig(3, z, i2, z2)) != null) {
            EGLContext createContext = createContext(context, config, 3);
            if (EGL14.eglGetError() == 12288) {
                this.mEglConfig = new Config(config);
                this.mContext = new Context(createContext);
                this.mGlVersion = 3;
            }
        }
        if (i >= 2 && ((context2 = this.mContext) == null || context2.eglContext == EGL14.EGL_NO_CONTEXT)) {
            EGLConfig config2 = getConfig(2, z, i2, z2);
            if (config2 == null) {
                throw new RuntimeException("chooseConfig failed");
            }
            try {
                EGLContext createContext2 = createContext(context, config2, 2);
                checkEglError("eglCreateContext");
                this.mEglConfig = new Config(config2);
                this.mContext = new Context(createContext2);
                this.mGlVersion = 2;
            } catch (Exception unused) {
                if (z2) {
                    EGLConfig config3 = getConfig(2, z, i2, false);
                    if (config3 == null) {
                        throw new RuntimeException("chooseConfig failed");
                    }
                    EGLContext createContext3 = createContext(context, config3, 2);
                    checkEglError("eglCreateContext");
                    this.mEglConfig = new Config(config3);
                    this.mContext = new Context(createContext3);
                    this.mGlVersion = 2;
                }
            }
        }
        Context context3 = this.mContext;
        if (context3 == null || context3.eglContext == EGL14.EGL_NO_CONTEXT) {
            EGLConfig config4 = getConfig(1, z, i2, z2);
            if (config4 == null) {
                throw new RuntimeException("chooseConfig failed");
            }
            EGLContext createContext4 = createContext(context, config4, 1);
            checkEglError("eglCreateContext");
            this.mEglConfig = new Config(config4);
            this.mContext = new Context(createContext4);
            this.mGlVersion = 1;
        }
        int[] iArr2 = new int[1];
        EGL14.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, iArr2, 0);
        Log.d(TAG, "EGLContext created, client version " + iArr2[0]);
        makeDefault();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean makeCurrent(EGLSurface eGLSurface) {
        if (eGLSurface == null || eGLSurface == EGL14.EGL_NO_SURFACE) {
            if (EGL14.eglGetError() == 12299) {
                Log.e(TAG, "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        }
        if (EGL14.eglMakeCurrent(this.mEglDisplay, eGLSurface, eGLSurface, this.mContext.eglContext)) {
            return true;
        }
        Log.w("TAG", "eglMakeCurrent" + EGL14.eglGetError());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface eGLSurface) {
        if (EGL14.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return 12288;
        }
        return EGL14.eglGetError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface eGLSurface, long j) {
        EGLExt.eglPresentationTimeANDROID(this.mEglDisplay, eGLSurface, j);
        if (EGL14.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return 12288;
        }
        return EGL14.eglGetError();
    }

    private EGLContext createContext(Context context, EGLConfig eGLConfig, int i) {
        return EGL14.eglCreateContext(this.mEglDisplay, eGLConfig, context.eglContext, new int[]{EGLBase.EGL_CONTEXT_CLIENT_VERSION, i, 12344}, 0);
    }

    private void destroyContext() {
        if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
            Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
            StringBuilder sb = new StringBuilder();
            sb.append("eglDestroyContext:");
            sb.append(EGL14.eglGetError());
            Log.e(TAG, sb.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
        if (this.mDefaultContext != EGL14.EGL_NO_CONTEXT) {
            if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mDefaultContext)) {
                Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mDefaultContext);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("eglDestroyContext:");
                sb2.append(EGL14.eglGetError());
                Log.e(TAG, sb2.toString());
            }
            this.mDefaultContext = EGL14.EGL_NO_CONTEXT;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getSurfaceWidth(EGLSurface eGLSurface) {
        if (!EGL14.eglQuerySurface(this.mEglDisplay, eGLSurface, 12375, this.mSurfaceDimension, 0)) {
            this.mSurfaceDimension[0] = 0;
        }
        return this.mSurfaceDimension[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getSurfaceHeight(EGLSurface eGLSurface) {
        if (!EGL14.eglQuerySurface(this.mEglDisplay, eGLSurface, 12374, this.mSurfaceDimension, 1)) {
            this.mSurfaceDimension[1] = 0;
        }
        return this.mSurfaceDimension[1];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final EGLSurface createWindowSurface(Object obj) {
        try {
            EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, obj, new int[]{12344}, 0);
            if (eglCreateWindowSurface != null && eglCreateWindowSurface != EGL14.EGL_NO_SURFACE) {
                makeCurrent(eglCreateWindowSurface);
                return eglCreateWindowSurface;
            }
            int eglGetError = EGL14.eglGetError();
            if (eglGetError == 12299) {
                Log.e(TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            throw new RuntimeException("createWindowSurface failed error=" + eglGetError);
        } catch (Exception e) {
            Log.e(TAG, "eglCreateWindowSurface", e);
            throw new IllegalArgumentException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final EGLSurface createOffscreenSurface(int i, int i2) {
        int[] iArr = {12375, i, 12374, i2, 12344};
        EGLSurface eGLSurface = null;
        try {
            eGLSurface = EGL14.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, iArr, 0);
            checkEglError("eglCreatePbufferSurface");
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "createOffscreenSurface", e);
        } catch (RuntimeException e2) {
            Log.e(TAG, "createOffscreenSurface", e2);
        }
        if (eGLSurface != null) {
            return eGLSurface;
        }
        throw new RuntimeException("surface was null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyWindowSurface(EGLSurface eGLSurface) {
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.mEglDisplay, eGLSurface);
        }
        EGLSurface eGLSurface2 = EGL14.EGL_NO_SURFACE;
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError == 12288) {
            return;
        }
        throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
    }

    private EGLConfig getConfig(int i, boolean z, int i2, boolean z2) {
        int i3 = 10;
        int i4 = 12;
        int[] iArr = {12352, i >= 3 ? 68 : 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        if (i2 > 0) {
            iArr[10] = 12326;
            iArr[11] = i2;
        } else {
            i4 = 10;
        }
        if (z) {
            int i5 = i4 + 1;
            iArr[i4] = 12325;
            i4 = i5 + 1;
            iArr[i5] = 16;
        }
        if (z2 && BuildCheck.isAndroid4_3()) {
            int i6 = i4 + 1;
            iArr[i4] = 12610;
            i4 = i6 + 1;
            iArr[i6] = 1;
        }
        for (int i7 = 16; i7 >= i4; i7--) {
            iArr[i7] = 12344;
        }
        EGLConfig internalGetConfig = internalGetConfig(iArr);
        if (internalGetConfig == null && i == 2 && z2) {
            while (true) {
                if (i3 >= 16) {
                    break;
                }
                if (iArr[i3] == 12610) {
                    while (i3 < 17) {
                        iArr[i3] = 12344;
                        i3++;
                    }
                } else {
                    i3 += 2;
                }
            }
            internalGetConfig = internalGetConfig(iArr);
        }
        if (internalGetConfig != null) {
            return internalGetConfig;
        }
        Log.w(TAG, "try to fallback to RGB565");
        iArr[3] = 5;
        iArr[5] = 6;
        iArr[7] = 5;
        return internalGetConfig(iArr);
    }

    private EGLConfig internalGetConfig(int[] iArr) {
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        if (EGL14.eglChooseConfig(this.mEglDisplay, iArr, 0, eGLConfigArr, 0, 1, new int[1], 0)) {
            return eGLConfigArr[0];
        }
        return null;
    }
}
