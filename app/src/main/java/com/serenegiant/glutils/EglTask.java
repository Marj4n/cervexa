package com.serenegiant.glutils;

import com.serenegiant.glutils.EGLBase;
import com.serenegiant.utils.MessageTask;

/* loaded from: classes.dex */
public abstract class EglTask extends MessageTask {
    public static final int EGL_FLAG_DEPTH_BUFFER = 1;
    public static final int EGL_FLAG_RECORDABLE = 2;
    public static final int EGL_FLAG_STENCIL_1BIT = 4;
    public static final int EGL_FLAG_STENCIL_8BIT = 32;
    private EGLBase mEgl = null;
    private EGLBase.IEglSurface mEglHolder;

    public EglTask(EGLBase.IContext iContext, int i) {
        init(i, 3, iContext);
    }

    public EglTask(int i, EGLBase.IContext iContext, int i2) {
        init(i2, i, iContext);
    }

    @Override // com.serenegiant.utils.MessageTask
    protected void onInit(int i, int i2, Object obj) {
        if (obj == null || (obj instanceof EGLBase.IContext)) {
            this.mEgl = EGLBase.createFrom(i2, (EGLBase.IContext) obj, (i & 1) == 1, (i & 4) == 4 ? 1 : (i & 32) == 32 ? 8 : 0, (i & 2) == 2);
        }
        EGLBase eGLBase = this.mEgl;
        if (eGLBase == null) {
            callOnError(new RuntimeException("failed to create EglCore"));
            releaseSelf();
        } else {
            EGLBase.IEglSurface createOffscreen = eGLBase.createOffscreen(1, 1);
            this.mEglHolder = createOffscreen;
            createOffscreen.makeCurrent();
        }
    }

    @Override // com.serenegiant.utils.MessageTask
    protected MessageTask.Request takeRequest() throws InterruptedException {
        MessageTask.Request takeRequest = super.takeRequest();
        this.mEglHolder.makeCurrent();
        return takeRequest;
    }

    @Override // com.serenegiant.utils.MessageTask
    protected void onBeforeStop() {
        this.mEglHolder.makeCurrent();
    }

    @Override // com.serenegiant.utils.MessageTask
    protected void onRelease() {
        this.mEglHolder.release();
        this.mEgl.release();
    }

    protected EGLBase getEgl() {
        return this.mEgl;
    }

    protected EGLBase.IContext getEGLContext() {
        return this.mEgl.getContext();
    }

    protected EGLBase.IConfig getConfig() {
        return this.mEgl.getConfig();
    }

    protected EGLBase.IContext getContext() {
        EGLBase eGLBase = this.mEgl;
        if (eGLBase != null) {
            return eGLBase.getContext();
        }
        return null;
    }

    protected void makeCurrent() {
        this.mEglHolder.makeCurrent();
    }

    protected boolean isGLES3() {
        EGLBase eGLBase = this.mEgl;
        return eGLBase != null && eGLBase.getGlVersion() > 2;
    }
}
