package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.glutils.EGLBase;
import com.serenegiant.utils.MessageTask;

/* loaded from: classes.dex */
public class DumbRenderer implements IRenderer {
    private static final int REQUEST_DRAW = 2;
    private static final int REQUEST_MIRROR = 4;
    private static final int REQUEST_RESIZE = 3;
    private static final int REQUEST_SET_SURFACE = 1;
    private static final String TAG = DumbRenderer.class.getSimpleName();
    private int mMirror;
    private RendererTask mRendererTask;
    private final Object mSync;

    public interface RendererDelegater {
        void onDraw(EGLBase eGLBase, Object... objArr);

        void onMirror(EGLBase eGLBase, int i);

        void onResize(EGLBase eGLBase, int i, int i2);

        void onSetSurface(EGLBase eGLBase, Object obj);

        void onStart(EGLBase eGLBase);

        void onStop(EGLBase eGLBase);
    }

    public DumbRenderer(EGLBase.IContext iContext, int i, RendererDelegater rendererDelegater) {
        this(3, iContext, i, rendererDelegater);
    }

    public DumbRenderer(int i, EGLBase.IContext iContext, int i2, RendererDelegater rendererDelegater) {
        this.mSync = new Object();
        this.mMirror = 0;
        this.mRendererTask = new RendererTask(i, iContext, i2, rendererDelegater);
        new Thread(this.mRendererTask, TAG).start();
        if (!this.mRendererTask.waitReady()) {
            throw new RuntimeException("failed to start renderer thread");
        }
    }

    @Override // com.serenegiant.glutils.IRenderer
    public void release() {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.release();
                this.mRendererTask = null;
            }
        }
    }

    @Override // com.serenegiant.glutils.IRenderer
    public void setSurface(Surface surface) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(1, surface);
            }
        }
    }

    @Override // com.serenegiant.glutils.IRenderer
    public void setSurface(SurfaceTexture surfaceTexture) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(1, surfaceTexture);
            }
        }
    }

    @Override // com.serenegiant.glutils.IRendererCommon
    public void setMirror(int i) {
        synchronized (this.mSync) {
            if (this.mMirror != i) {
                this.mMirror = i;
                if (this.mRendererTask != null) {
                    this.mRendererTask.offer(4, i % 4);
                }
            }
        }
    }

    @Override // com.serenegiant.glutils.IRendererCommon
    public int getMirror() {
        return this.mMirror;
    }

    @Override // com.serenegiant.glutils.IRenderer
    public void resize(int i, int i2) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(3, i, i2);
            }
        }
    }

    @Override // com.serenegiant.glutils.IRenderer
    public void requestRender(Object... objArr) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(2, objArr);
            }
        }
    }

    private static class RendererTask extends EglTask {
        private int frameHeight;
        private int frameRotation;
        private int frameWidth;
        private final RendererDelegater mDelegater;
        private boolean mirror;
        private int surfaceHeight;
        private int surfaceWidth;

        public RendererTask(EGLBase.IContext iContext, int i, RendererDelegater rendererDelegater) {
            this(3, iContext, i, rendererDelegater);
        }

        public RendererTask(int i, EGLBase.IContext iContext, int i2, RendererDelegater rendererDelegater) {
            super(i, iContext, i2);
            this.mDelegater = rendererDelegater;
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStart() {
            makeCurrent();
            try {
                this.mDelegater.onStart(getEgl());
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStop() {
            makeCurrent();
            try {
                this.mDelegater.onStop(getEgl());
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        @Override // com.serenegiant.utils.MessageTask
        protected Object processRequest(int i, int i2, int i3, Object obj) throws MessageTask.TaskBreak {
            if (i == 1) {
                handleSetSurface(obj);
                return null;
            }
            if (i == 2) {
                handleDraw(obj);
                return null;
            }
            if (i == 3) {
                handleResize(i2, i3);
                return null;
            }
            if (i != 4) {
                return null;
            }
            handleMirror(i2);
            return null;
        }

        private void handleSetSurface(Object obj) {
            makeCurrent();
            try {
                this.mDelegater.onSetSurface(getEgl(), obj);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        private void handleResize(int i, int i2) {
            if (this.surfaceWidth == i && this.surfaceHeight == i2) {
                return;
            }
            this.surfaceWidth = i;
            this.surfaceHeight = i2;
            makeCurrent();
            try {
                this.mDelegater.onResize(getEgl(), i, i2);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
            handleDraw(new Object[0]);
        }

        private void handleDraw(Object... objArr) {
            makeCurrent();
            try {
                this.mDelegater.onDraw(getEgl(), objArr);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        private void handleMirror(int i) {
            makeCurrent();
            try {
                this.mDelegater.onMirror(getEgl(), i);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }
    }
}
