package com.serenegiant.glutils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.serenegiant.glutils.EGLBase;
import com.serenegiant.utils.BuildCheck;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/* loaded from: classes.dex */
class EGLBase10 extends EGLBase {
    private static final Context EGL_NO_CONTEXT = new Context(EGL10.EGL_NO_CONTEXT);
    private static final String TAG = "EGLBase10";
    private EGL10 mEgl = null;
    private EGLDisplay mEglDisplay = null;
    private Config mEglConfig = null;
    private int mGlVersion = 2;
    private Context mContext = EGL_NO_CONTEXT;

    public static class Context extends IContext {
        public final EGLContext eglContext;

        @Override // com.serenegiant.glutils.EGLBase.IContext
        public long getNativeHandle() {
            return 0L;
        }

        private Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }

        @Override // com.serenegiant.glutils.EGLBase.IContext
        public Object getEGLContext() {
            return this.eglContext;
        }
    }

    public static class Config extends IConfig {
        public final EGLConfig eglConfig;

        private Config(EGLConfig eGLConfig) {
            this.eglConfig = eGLConfig;
        }
    }

    public static class MySurfaceHolder implements SurfaceHolder {
        private final Surface surface;

        @Override // android.view.SurfaceHolder
        public void addCallback(Callback callback) {
        }

        @Override // android.view.SurfaceHolder
        public Rect getSurfaceFrame() {
            return null;
        }

        @Override // android.view.SurfaceHolder
        public boolean isCreating() {
            return false;
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas() {
            return null;
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas(Rect rect) {
            return null;
        }

        @Override // android.view.SurfaceHolder
        public void removeCallback(Callback callback) {
        }

        @Override // android.view.SurfaceHolder
        public void setFixedSize(int i, int i2) {
        }

        @Override // android.view.SurfaceHolder
        public void setFormat(int i) {
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(boolean z) {
        }

        @Override // android.view.SurfaceHolder
        public void setSizeFromLayout() {
        }

        @Override // android.view.SurfaceHolder
        public void setType(int i) {
        }

        @Override // android.view.SurfaceHolder
        public void unlockCanvasAndPost(Canvas canvas) {
        }

        public MySurfaceHolder(Surface surface) {
            this.surface = surface;
        }

        @Override // android.view.SurfaceHolder
        public Surface getSurface() {
            return this.surface;
        }
    }

    public static class EglSurface implements IEglSurface {
        private final EGLBase10 mEglBase;
        private EGLSurface mEglSurface;

        public void setPresentationTime(long j) {
        }

        private EglSurface(EGLBase10 eGLBase10, Object obj) throws IllegalArgumentException {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
            this.mEglBase = eGLBase10;
            boolean z = obj instanceof Surface;
            if (z && !BuildCheck.isAndroid4_2()) {
                this.mEglSurface = this.mEglBase.createWindowSurface(new MySurfaceHolder((Surface) obj));
            } else {
                if (z || (obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceView)) {
                    this.mEglSurface = this.mEglBase.createWindowSurface(obj);
                    return;
                }
                throw new IllegalArgumentException("unsupported surface");
            }
        }

        private EglSurface(EGLBase10 eGLBase10, int i, int i2) {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
            this.mEglBase = eGLBase10;
            if (i <= 0 || i2 <= 0) {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
            } else {
                this.mEglSurface = eGLBase10.createOffscreenSurface(i, i2);
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

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public IContext getContext() {
            return this.mEglBase.getContext();
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public boolean isValid() {
            EGLSurface eGLSurface = this.mEglSurface;
            return eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        @Override // com.serenegiant.glutils.EGLBase.IEglSurface
        public void release() {
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    public EGLBase10(int i, Context context, boolean z, int i2, boolean z2) {
        init(i, context, z, i2, z2);
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void release() {
        destroyContext();
        this.mContext = EGL_NO_CONTEXT;
        EGL10 egl10 = this.mEgl;
        if (egl10 == null) {
            return;
        }
        egl10.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        this.mEgl.eglTerminate(this.mEglDisplay);
        this.mEglDisplay = null;
        this.mEglConfig = null;
        this.mEgl = null;
    }

    @Override // com.serenegiant.glutils.EGLBase
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
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public Config getConfig() {
        return this.mEglConfig;
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void makeDefault() {
        if (this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
            return;
        }
        Log.w(TAG, "makeDefault:eglMakeCurrent:err=" + this.mEgl.eglGetError());
    }

    @Override // com.serenegiant.glutils.EGLBase
    public void sync() {
        this.mEgl.eglWaitGL();
        this.mEgl.eglWaitNative(12379, null);
    }

    @Override // com.serenegiant.glutils.EGLBase
    public String queryString(int i) {
        return this.mEgl.eglQueryString(this.mEglDisplay, i);
    }

    @Override // com.serenegiant.glutils.EGLBase
    public int getGlVersion() {
        return this.mGlVersion;
    }

    private final void init(int i, Context context, boolean z, int i2, boolean z2) {
        Context context2;
        EGLConfig config;
        if (context == null) {
            context = EGL_NO_CONTEXT;
        }
        if (this.mEgl == null) {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.mEgl = egl10;
            EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            this.mEglDisplay = eglGetDisplay;
            if (eglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (!this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                this.mEglDisplay = null;
                throw new RuntimeException("eglInitialize failed");
            }
        }
        if (i >= 3 && (config = getConfig(3, z, i2, z2)) != null) {
            EGLContext createContext = createContext(context, config, 3);
            if (this.mEgl.eglGetError() == 12288) {
                this.mEglConfig = new Config(config);
                this.mContext = new Context(createContext);
                this.mGlVersion = 3;
            }
        }
        if (i >= 2 && ((context2 = this.mContext) == null || context2.eglContext == EGL10.EGL_NO_CONTEXT)) {
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
        if (context3 == null || context3.eglContext == EGL10.EGL_NO_CONTEXT) {
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
        int[] iArr = new int[1];
        this.mEgl.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, iArr);
        Log.d(TAG, "EGLContext created, client version " + iArr[0]);
        makeDefault();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean makeCurrent(EGLSurface eGLSurface) {
        if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
            if (this.mEgl.eglGetError() == 12299) {
                Log.e(TAG, "makeCurrent:EGL_BAD_NATIVE_WINDOW");
            }
            return false;
        }
        if (this.mEgl.eglMakeCurrent(this.mEglDisplay, eGLSurface, eGLSurface, this.mContext.eglContext)) {
            return true;
        }
        Log.w("TAG", "eglMakeCurrent" + this.mEgl.eglGetError());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int swap(EGLSurface eGLSurface) {
        if (this.mEgl.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return 12288;
        }
        return this.mEgl.eglGetError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int swap(EGLSurface eGLSurface, long j) {
        if (this.mEgl.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return 12288;
        }
        return this.mEgl.eglGetError();
    }

    private final EGLContext createContext(Context context, EGLConfig eGLConfig, int i) {
        return this.mEgl.eglCreateContext(this.mEglDisplay, eGLConfig, context.eglContext, new int[]{EGLBase.EGL_CONTEXT_CLIENT_VERSION, i, 12344});
    }

    private final void destroyContext() {
        if (!this.mEgl.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
            Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
            StringBuilder sb = new StringBuilder();
            sb.append("eglDestroyContext:");
            sb.append(this.mEgl.eglGetError());
            Log.e(TAG, sb.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getSurfaceWidth(EGLSurface eGLSurface) {
        int[] iArr = new int[1];
        if (!this.mEgl.eglQuerySurface(this.mEglDisplay, eGLSurface, 12375, iArr)) {
            iArr[0] = 0;
        }
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getSurfaceHeight(EGLSurface eGLSurface) {
        int[] iArr = new int[1];
        if (!this.mEgl.eglQuerySurface(this.mEglDisplay, eGLSurface, 12374, iArr)) {
            iArr[0] = 0;
        }
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final EGLSurface createWindowSurface(Object obj) {
        try {
            EGLSurface eglCreateWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, obj, new int[]{12344});
            if (eglCreateWindowSurface != null && eglCreateWindowSurface != EGL10.EGL_NO_SURFACE) {
                makeCurrent(eglCreateWindowSurface);
                return eglCreateWindowSurface;
            }
            int eglGetError = this.mEgl.eglGetError();
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
        this.mEgl.eglWaitGL();
        EGLSurface eGLSurface = null;
        try {
            eGLSurface = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, iArr);
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
    public final void destroyWindowSurface(EGLSurface eGLSurface) {
        if (eGLSurface != EGL10.EGL_NO_SURFACE) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroySurface(this.mEglDisplay, eGLSurface);
        }
        EGLSurface eGLSurface2 = EGL10.EGL_NO_SURFACE;
    }

    private final void checkEglError(String str) {
        int eglGetError = this.mEgl.eglGetError();
        if (eglGetError == 12288) {
            return;
        }
        throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
    }

    private final EGLConfig getConfig(int i, boolean z, int i2, boolean z2) {
        int i3 = 10;
        int i4 = 12;
        int[] iArr = {12352, i >= 3 ? 68 : 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        if (i2 > 0) {
            iArr[10] = 12326;
            iArr[11] = 8;
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
        if (this.mEgl.eglChooseConfig(this.mEglDisplay, iArr, eGLConfigArr, 1, new int[1])) {
            return eGLConfigArr[0];
        }
        return null;
    }
}
