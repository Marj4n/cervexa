package com.serenegiant.glutils;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;

/* loaded from: classes.dex */
public class StaticTextureSource {
    private static final boolean DEBUG = false;
    private static final int REQUEST_ADD_SURFACE = 3;
    private static final int REQUEST_DRAW = 1;
    private static final int REQUEST_REMOVE_SURFACE = 4;
    private static final int REQUEST_SET_BITMAP = 7;
    private static final String TAG = StaticTextureSource.class.getSimpleName();
    private volatile boolean isRunning;
    private Runnable mOnFrameTask;
    private RendererTask mRendererTask;
    private final Object mSync;

    public StaticTextureSource(float f) {
        this(null, f);
    }

    public StaticTextureSource(Bitmap bitmap) {
        this(bitmap, 10.0f);
    }

    public StaticTextureSource(Bitmap bitmap, float f) {
        this.mSync = new Object();
        this.mOnFrameTask = new Runnable() { // from class: com.serenegiant.glutils.StaticTextureSource.1
            @Override // java.lang.Runnable
            public void run() {
                long j = StaticTextureSource.this.mRendererTask.mIntervalsNs / 1000000;
                int i = (int) (StaticTextureSource.this.mRendererTask.mIntervalsNs % 1000000);
                while (StaticTextureSource.this.isRunning && StaticTextureSource.this.mRendererTask != null) {
                    synchronized (StaticTextureSource.this.mSync) {
                        try {
                            StaticTextureSource.this.mSync.wait(j, i);
                            if (StaticTextureSource.this.mRendererTask.mImageSource != null) {
                                StaticTextureSource.this.mRendererTask.removeRequest(1);
                                StaticTextureSource.this.mRendererTask.offer(1);
                                StaticTextureSource.this.mSync.notify();
                            }
                        } catch (Exception e) {
                            Log.w(StaticTextureSource.TAG, e);
                        }
                    }
                }
            }
        };
        this.mRendererTask = new RendererTask(this, bitmap != null ? bitmap.getWidth() : 1, bitmap != null ? bitmap.getHeight() : 1, f);
        new Thread(this.mRendererTask, TAG).start();
        if (!this.mRendererTask.waitReady()) {
            throw new RuntimeException("failed to start renderer thread");
        }
        setBitmap(bitmap);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void release() {
        this.isRunning = false;
        synchronized (this.mSync) {
            this.mSync.notifyAll();
        }
        RendererTask rendererTask = this.mRendererTask;
        if (rendererTask != null) {
            rendererTask.release();
        }
        synchronized (this.mSync) {
            this.mRendererTask = null;
            this.mSync.notifyAll();
        }
    }

    public void addSurface(int i, Object obj, boolean z) throws InterruptedException {
        synchronized (this.mSync) {
            this.mRendererTask.addSurface(i, obj);
        }
    }

    public void addSurface(int i, Object obj, boolean z, int i2) throws InterruptedException {
        synchronized (this.mSync) {
            this.mRendererTask.addSurface(i, obj, i2);
        }
    }

    public void removeSurface(int i) throws InterruptedException {
        synchronized (this.mSync) {
            this.mRendererTask.removeSurface(i);
        }
    }

    public void requestFrame() {
        synchronized (this.mSync) {
            this.mRendererTask.removeRequest(1);
            this.mRendererTask.offer(1);
            this.mSync.notify();
        }
    }

    public int getCount() {
        return this.mRendererTask.getCount();
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (this.mSync) {
                this.mRendererTask.setBitmap(bitmap);
            }
        }
    }

    public int getWidth() {
        int i;
        synchronized (this.mSync) {
            i = this.mRendererTask != null ? this.mRendererTask.mVideoWidth : 0;
        }
        return i;
    }

    public int getHeight() {
        int i;
        synchronized (this.mSync) {
            i = this.mRendererTask != null ? this.mRendererTask.mVideoHeight : 0;
        }
        return i;
    }

    private static class RendererTask extends EglTask {
        private final Object mClientSync;
        private final SparseArray<RendererSurfaceRec> mClients;
        private GLDrawer2D mDrawer;
        private TextureOffscreen mImageSource;
        private final long mIntervalsNs;
        private final StaticTextureSource mParent;
        private int mVideoHeight;
        private int mVideoWidth;

        @Override // com.serenegiant.utils.MessageTask
        protected boolean onError(Exception exc) {
            return false;
        }

        public RendererTask(StaticTextureSource staticTextureSource, int i, int i2, float f) {
            super(3, null, 0);
            this.mClientSync = new Object();
            this.mClients = new SparseArray<>();
            this.mParent = staticTextureSource;
            this.mVideoWidth = i;
            this.mVideoHeight = i2;
            this.mIntervalsNs = f <= 0.0f ? 100000000L : (long) (1.0E9f / f);
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStart() {
            this.mDrawer = new GLDrawer2D(false);
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = true;
                this.mParent.mSync.notifyAll();
            }
            new Thread(this.mParent.mOnFrameTask, StaticTextureSource.TAG).start();
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStop() {
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = false;
                this.mParent.mSync.notifyAll();
            }
            makeCurrent();
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
            TextureOffscreen textureOffscreen = this.mImageSource;
            if (textureOffscreen != null) {
                textureOffscreen.release();
                this.mImageSource = null;
            }
            handleRemoveAll();
        }

        @Override // com.serenegiant.utils.MessageTask
        protected Object processRequest(int i, int i2, int i3, Object obj) {
            if (i == 1) {
                handleDraw();
                return null;
            }
            if (i == 7) {
                handleSetBitmap((Bitmap) obj);
                return null;
            }
            if (i == 3) {
                handleAddSurface(i2, obj, i3);
                return null;
            }
            if (i != 4) {
                return null;
            }
            handleRemoveSurface(i2);
            return null;
        }

        public void addSurface(int i, Object obj) throws InterruptedException {
            addSurface(i, obj, -1);
        }

        public void addSurface(int i, Object obj, int i2) throws InterruptedException {
            checkFinished();
            if (!(obj instanceof SurfaceTexture) && !(obj instanceof Surface) && !(obj instanceof SurfaceHolder)) {
                throw new IllegalArgumentException("Surface should be one of Surface, SurfaceTexture or SurfaceHolder");
            }
            synchronized (this.mClientSync) {
                if (this.mClients.get(i) == null) {
                    while (!offer(3, i, i2, obj)) {
                        try {
                            this.mClientSync.wait(10L);
                        } catch (InterruptedException unused) {
                        }
                    }
                    this.mClientSync.wait();
                }
            }
        }

        public void removeSurface(int i) throws InterruptedException {
            synchronized (this.mClientSync) {
                if (this.mClients.get(i) != null) {
                    while (!offer(4, i)) {
                        try {
                            this.mClientSync.wait(10L);
                        } catch (InterruptedException unused) {
                        }
                    }
                    this.mClientSync.wait();
                }
            }
        }

        public void setBitmap(Bitmap bitmap) {
            offer(7, bitmap);
        }

        public int getCount() {
            int size;
            synchronized (this.mClientSync) {
                size = this.mClients.size();
            }
            return size;
        }

        private void checkFinished() {
            if (isFinished()) {
                throw new RuntimeException("already finished");
            }
        }

        private void handleDraw() {
            makeCurrent();
            TextureOffscreen textureOffscreen = this.mImageSource;
            if (textureOffscreen == null) {
                Log.w(StaticTextureSource.TAG, "mImageSource is not ready");
            } else {
                int texture = textureOffscreen.getTexture();
                synchronized (this.mClientSync) {
                    for (int size = this.mClients.size() - 1; size >= 0; size--) {
                        RendererSurfaceRec valueAt = this.mClients.valueAt(size);
                        if (valueAt != null && valueAt.canDraw()) {
                            try {
                                valueAt.draw(this.mDrawer, texture, null);
                                GLHelper.checkGlError("handleSetBitmap");
                            } catch (Exception unused) {
                                this.mClients.removeAt(size);
                                valueAt.release();
                            }
                        }
                    }
                }
            }
            GLES20.glClear(16384);
            GLES20.glFlush();
        }

        private void handleAddSurface(int i, Object obj, int i2) {
            checkSurface();
            synchronized (this.mClientSync) {
                if (this.mClients.get(i) != null) {
                    Log.w(StaticTextureSource.TAG, "surface is already added: id=" + i);
                } else {
                    try {
                        this.mClients.append(i, RendererSurfaceRec.newInstance(getEgl(), obj, i2));
                    } catch (Exception e) {
                        Log.w(StaticTextureSource.TAG, "invalid surface: surface=" + obj, e);
                    }
                }
                this.mClientSync.notifyAll();
            }
        }

        private void handleRemoveSurface(int i) {
            synchronized (this.mClientSync) {
                RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    this.mClients.remove(i);
                    rendererSurfaceRec.release();
                }
                checkSurface();
                this.mClientSync.notifyAll();
            }
        }

        private void handleRemoveAll() {
            synchronized (this.mClientSync) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(i);
                    if (valueAt != null) {
                        makeCurrent();
                        valueAt.release();
                    }
                }
                this.mClients.clear();
            }
        }

        private void checkSurface() {
            synchronized (this.mClientSync) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(i);
                    if (valueAt != null && !valueAt.isValid()) {
                        int keyAt = this.mClients.keyAt(i);
                        this.mClients.valueAt(i).release();
                        this.mClients.remove(keyAt);
                    }
                }
            }
        }

        private void handleSetBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            TextureOffscreen textureOffscreen = this.mImageSource;
            if (textureOffscreen == null) {
                this.mImageSource = new TextureOffscreen(width, height, false);
                GLHelper.checkGlError("handleSetBitmap");
                this.mImageSource.loadBitmap(bitmap);
            } else {
                textureOffscreen.loadBitmap(bitmap);
            }
            this.mVideoWidth = width;
            this.mVideoHeight = height;
        }
    }
}
