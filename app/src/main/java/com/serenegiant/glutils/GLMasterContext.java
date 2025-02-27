package com.serenegiant.glutils;

import com.serenegiant.glutils.EGLBase;
import com.serenegiant.utils.MessageTask;

/* loaded from: classes.dex */
public class GLMasterContext {
    private static final String TAG = GLMasterContext.class.getSimpleName();
    private MasterTask mMasterTask;

    public GLMasterContext(int i, int i2) {
        this.mMasterTask = new MasterTask(i, i2);
        new Thread(this.mMasterTask, TAG).start();
        this.mMasterTask.waitReady();
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public synchronized void release() {
        if (this.mMasterTask != null) {
            this.mMasterTask.release();
            this.mMasterTask = null;
        }
    }

    public synchronized EGLBase.IContext getContext() throws IllegalStateException {
        if (this.mMasterTask != null) {
        } else {
            throw new IllegalStateException("already released");
        }
        return this.mMasterTask.getContext();
    }

    private static class MasterTask extends EglTask {
        @Override // com.serenegiant.utils.MessageTask
        protected void onStart() {
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStop() {
        }

        @Override // com.serenegiant.utils.MessageTask
        protected Object processRequest(int i, int i2, int i3, Object obj) throws TaskBreak {
            return null;
        }

        public MasterTask(int i, int i2) {
            super(i, null, i2);
        }
    }
}
