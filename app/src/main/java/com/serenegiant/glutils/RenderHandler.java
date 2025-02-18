package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.serenegiant.glutils.EGLBase;

@Deprecated
/* loaded from: classes.dex */
public final class RenderHandler extends Handler {
    private static final int MSG_CHECK_VALID = 3;
    private static final int MSG_RENDER_DRAW = 2;
    private static final int MSG_RENDER_QUIT = 9;
    private static final int MSG_RENDER_SET_GLCONTEXT = 1;
    private static final String TAG = "RenderHandler";
    private int mTexId;
    private final RenderThread mThread;

    public static RenderHandler createHandler() {
        return createHandler("RenderThread");
    }

    public static final RenderHandler createHandler(String str) {
        RenderThread renderThread = new RenderThread(str);
        renderThread.start();
        return renderThread.getHandler();
    }

    public final void setEglContext(EGLBase.IContext iContext, int i, Object obj, boolean z) {
        if (!(obj instanceof Surface) && !(obj instanceof SurfaceTexture) && !(obj instanceof SurfaceHolder)) {
            throw new RuntimeException("unsupported window type:" + obj);
        }
        this.mTexId = i;
        sendMessage(obtainMessage(1, z ? 1 : 0, 0, new ContextParams(iContext, obj)));
    }

    public final void draw() {
        sendMessage(obtainMessage(2, this.mTexId, 0, null));
    }

    public final void draw(int i) {
        sendMessage(obtainMessage(2, i, 0, null));
    }

    public final void draw(float[] fArr) {
        sendMessage(obtainMessage(2, this.mTexId, 0, fArr));
    }

    public final void draw(int i, float[] fArr) {
        sendMessage(obtainMessage(2, i, 0, fArr));
    }

    public boolean isValid() {
        boolean z;
        synchronized (this.mThread.mSync) {
            sendEmptyMessage(3);
            try {
                this.mThread.mSync.wait();
            } catch (InterruptedException unused) {
            }
            z = this.mThread.mSurface != null && this.mThread.mSurface.isValid();
        }
        return z;
    }

    public final void release() {
        removeMessages(1);
        removeMessages(2);
        sendEmptyMessage(9);
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            ContextParams contextParams = (ContextParams) message.obj;
            this.mThread.handleSetEglContext(contextParams.sharedContext, contextParams.surface, message.arg1 != 0);
            return;
        }
        if (i == 2) {
            this.mThread.handleDraw(message.arg1, (float[]) message.obj);
            return;
        }
        if (i == 3) {
            synchronized (this.mThread.mSync) {
                this.mThread.mSync.notify();
            }
        } else if (i == 9) {
            Looper.myLooper().quit();
        } else {
            super.handleMessage(message);
        }
    }

    private RenderHandler(RenderThread renderThread) {
        this.mTexId = -1;
        this.mThread = renderThread;
    }

    private static final class ContextParams {
        final EGLBase.IContext sharedContext;
        final Object surface;

        public ContextParams(EGLBase.IContext iContext, Object obj) {
            this.sharedContext = iContext;
            this.surface = obj;
        }
    }

    private static final class RenderThread extends Thread {
        private static final String TAG_THREAD = "RenderThread";
        private GLDrawer2D mDrawer;
        private EGLBase mEgl;
        private RenderHandler mHandler;
        private Surface mSurface;
        private final Object mSync;
        private EGLBase.IEglSurface mTargetSurface;

        public RenderThread(String str) {
            super(str);
            this.mSync = new Object();
        }

        public final RenderHandler getHandler() {
            synchronized (this.mSync) {
                try {
                    this.mSync.wait();
                } catch (InterruptedException unused) {
                }
            }
            return this.mHandler;
        }

        public final void handleSetEglContext(EGLBase.IContext iContext, Object obj, boolean z) {
            release();
            synchronized (this.mSync) {
                this.mSurface = obj instanceof Surface ? (Surface) obj : obj instanceof SurfaceTexture ? new Surface((SurfaceTexture) obj) : null;
            }
            EGLBase createFrom = EGLBase.createFrom(3, iContext, false, 0, z);
            this.mEgl = createFrom;
            try {
                this.mTargetSurface = createFrom.createFromSurface(obj);
                this.mDrawer = new GLDrawer2D(z);
            } catch (Exception e) {
                Log.w(RenderHandler.TAG, e);
                EGLBase.IEglSurface iEglSurface = this.mTargetSurface;
                if (iEglSurface != null) {
                    iEglSurface.release();
                    this.mTargetSurface = null;
                }
                GLDrawer2D gLDrawer2D = this.mDrawer;
                if (gLDrawer2D != null) {
                    gLDrawer2D.release();
                    this.mDrawer = null;
                }
            }
        }

        public void handleDraw(int i, float[] fArr) {
            EGLBase.IEglSurface iEglSurface;
            if (i < 0 || (iEglSurface = this.mTargetSurface) == null) {
                return;
            }
            iEglSurface.makeCurrent();
            this.mDrawer.draw(i, fArr, 0);
            this.mTargetSurface.swap();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            Looper.prepare();
            synchronized (this.mSync) {
                this.mHandler = new RenderHandler(this);
                this.mSync.notify();
            }
            Looper.loop();
            release();
            synchronized (this.mSync) {
                this.mHandler = null;
            }
        }

        private final void release() {
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
            synchronized (this.mSync) {
                this.mSurface = null;
            }
            if (this.mTargetSurface != null) {
                clear();
                this.mTargetSurface.release();
                this.mTargetSurface = null;
            }
            EGLBase eGLBase = this.mEgl;
            if (eGLBase != null) {
                eGLBase.release();
                this.mEgl = null;
            }
        }

        private final void clear() {
            this.mTargetSurface.makeCurrent();
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            this.mTargetSurface.swap();
        }
    }
}
