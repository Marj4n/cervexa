package com.serenegiant.glutils;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;

import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.glutils.EGLBase;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.utils.BuildCheck;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public abstract class AbstractRendererHolder implements IRendererHolder {
    private static final String CAPTURE_THREAD_NAME = "CaptureTask";
    private static final boolean DEBUG = false;
    private static final String RENDERER_THREAD_NAME = "RendererHolder";
    protected static final int REQUEST_ADD_SURFACE = 3;
    protected static final int REQUEST_CLEAR = 8;
    protected static final int REQUEST_CLEAR_ALL = 9;
    protected static final int REQUEST_DRAW = 1;
    protected static final int REQUEST_MIRROR = 6;
    protected static final int REQUEST_RECREATE_MASTER_SURFACE = 5;
    protected static final int REQUEST_REMOVE_SURFACE = 4;
    protected static final int REQUEST_REMOVE_SURFACE_ALL = 12;
    protected static final int REQUEST_ROTATE = 7;
    protected static final int REQUEST_SET_MVP = 10;
    protected static final int REQUEST_UPDATE_SIZE = 2;
    private static final String TAG = AbstractRendererHolder.class.getSimpleName();
    private volatile boolean isRunning;
    private final RenderHolderCallback mCallback;
    private int mCaptureCompression;
    private int mCaptureFormat;
    private OutputStream mCaptureStream;
    private final Runnable mCaptureTask;
    protected final RendererTask mRendererTask;
    protected final Object mSync;

    protected abstract RendererTask createRendererTask(int i, int i2, int i3, EGLBase.IContext iContext, int i4);

    protected void setupCaptureDrawer(GLDrawer2D gLDrawer2D) {
    }

    protected AbstractRendererHolder(int i, int i2, RenderHolderCallback renderHolderCallback) {
        this(i, i2, 3, null, 2, renderHolderCallback);
    }

    protected AbstractRendererHolder(int i, int i2, int i3, EGLBase.IContext iContext, int i4, RenderHolderCallback renderHolderCallback) {
        this.mSync = new Object();
        this.mCaptureCompression = 80;
        this.mCaptureTask = new Runnable() { // from class: com.serenegiant.glutils.AbstractRendererHolder.1
            EGLBase.IEglSurface captureSurface;
            GLDrawer2D drawer;
            EGLBase eglBase;
            final float[] mMvpMatrix = new float[16];

            @Override // java.lang.Runnable
            public void run() {
                synchronized (AbstractRendererHolder.this.mSync) {
                    while (!AbstractRendererHolder.this.isRunning && !AbstractRendererHolder.this.mRendererTask.isFinished()) {
                        try {
                            AbstractRendererHolder.this.mSync.wait(1000L);
                        } catch (InterruptedException unused) {
                        }
                    }
                }
                if (AbstractRendererHolder.this.isRunning) {
                    init();
                    try {
                        try {
                            if (this.eglBase.getGlVersion() > 2 && BuildCheck.isAndroid4_3()) {
                                captureLoopGLES3();
                            } else {
                                captureLoopGLES2();
                            }
                        } finally {
                            release();
                        }
                    } catch (Exception e) {
                        Log.w(AbstractRendererHolder.TAG, e);
                    }
                }
            }

            private final void init() {
                EGLBase createFrom = EGLBase.createFrom(3, AbstractRendererHolder.this.mRendererTask.getContext(), false, 0, false);
                this.eglBase = createFrom;
                this.captureSurface = createFrom.createOffscreen(AbstractRendererHolder.this.mRendererTask.width(), AbstractRendererHolder.this.mRendererTask.height());
                Matrix.setIdentityM(this.mMvpMatrix, 0);
                GLDrawer2D gLDrawer2D = new GLDrawer2D(true);
                this.drawer = gLDrawer2D;
                AbstractRendererHolder.this.setupCaptureDrawer(gLDrawer2D);
            }

            private final void captureLoopGLES2() throws IOException {
                int i5 = -1;
                ByteBuffer byteBuffer = null;
                int i6 = -1;
                int i7 = 80;
                while (AbstractRendererHolder.this.isRunning) {
                    synchronized (AbstractRendererHolder.this.mSync) {
                        if (AbstractRendererHolder.this.mCaptureStream == null) {
                            try {
                                AbstractRendererHolder.this.mSync.wait();
                                if (AbstractRendererHolder.this.mCaptureStream != null) {
                                    i7 = AbstractRendererHolder.this.mCaptureCompression;
                                    if (i7 <= 0 || i7 >= 100) {
                                        i7 = 90;
                                    }
                                }
                            } catch (InterruptedException unused) {
                            }
                        }
                        if (byteBuffer == null || i5 != AbstractRendererHolder.this.mRendererTask.width() || i6 != AbstractRendererHolder.this.mRendererTask.height()) {
                            i5 = AbstractRendererHolder.this.mRendererTask.width();
                            i6 = AbstractRendererHolder.this.mRendererTask.height();
                            byteBuffer = ByteBuffer.allocateDirect(i5 * i6 * 4);
                            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            if (this.captureSurface != null) {
                                this.captureSurface.release();
                                this.captureSurface = null;
                            }
                            this.captureSurface = this.eglBase.createOffscreen(i5, i6);
                        }
                        if (!AbstractRendererHolder.this.isRunning || i5 <= 0 || i6 <= 0) {
                            if (AbstractRendererHolder.this.isRunning) {
                                Log.w(AbstractRendererHolder.TAG, "#captureLoopGLES3:unexpectedly width/height is zero");
                            }
                        } else {
                            AbstractRendererHolder.setMirror(this.mMvpMatrix, AbstractRendererHolder.this.mRendererTask.mirror());
                            float[] fArr = this.mMvpMatrix;
                            fArr[5] = fArr[5] * (-1.0f);
                            this.drawer.setMvpMatrix(this.mMvpMatrix, 0);
                            this.captureSurface.makeCurrent();
                            this.drawer.draw(AbstractRendererHolder.this.mRendererTask.mTexId, AbstractRendererHolder.this.mRendererTask.mTexMatrix, 0);
                            this.captureSurface.swap();
                            byteBuffer.clear();
                            GLES20.glReadPixels(0, 0, i5, i6, 6408, 5121, byteBuffer);
                            Bitmap.CompressFormat captureFormat = AbstractRendererHolder.getCaptureFormat(AbstractRendererHolder.this.mCaptureFormat);
                            try {
                                Bitmap createBitmap = Bitmap.createBitmap(i5, i6, Bitmap.Config.ARGB_8888);
                                byteBuffer.clear();
                                createBitmap.copyPixelsFromBuffer(byteBuffer);
                                createBitmap.compress(captureFormat, i7, AbstractRendererHolder.this.mCaptureStream);
                                createBitmap.recycle();
                                AbstractRendererHolder.this.mCaptureStream.flush();
                                try {
                                    AbstractRendererHolder.this.mCaptureStream.close();
                                } catch (IOException e) {
                                    Log.w(AbstractRendererHolder.TAG, "failed to save file", e);
                                }
                            } catch (Throwable th) {
                                try {
                                    AbstractRendererHolder.this.mCaptureStream.close();
                                } catch (IOException e) {
                                    Log.w(AbstractRendererHolder.TAG, "failed to close stream", e);
                                }
                                throw th;
                            }
                        }
                        AbstractRendererHolder.this.mCaptureStream = null;
                        AbstractRendererHolder.this.mSync.notifyAll();
                    }
                }
                synchronized (AbstractRendererHolder.this.mSync) {
                    AbstractRendererHolder.this.mSync.notifyAll();
                }
            }

            private final void captureLoopGLES3() throws IOException {
                int i5 = -1;
                ByteBuffer byteBuffer = null;
                int i6 = -1;
                int i7 = 90;
                while (AbstractRendererHolder.this.isRunning) {
                    synchronized (AbstractRendererHolder.this.mSync) {
                        if (AbstractRendererHolder.this.mCaptureStream == null) {
                            try {
                                AbstractRendererHolder.this.mSync.wait();
                                if (AbstractRendererHolder.this.mCaptureStream != null) {
                                    i7 = AbstractRendererHolder.this.mCaptureCompression;
                                    if (i7 <= 0 || i7 >= 100) {
                                        i7 = 90;
                                    }
                                }
                            } catch (InterruptedException unused) {
                            }
                        }
                        if (byteBuffer == null || i5 != AbstractRendererHolder.this.mRendererTask.width() || i6 != AbstractRendererHolder.this.mRendererTask.height()) {
                            i5 = AbstractRendererHolder.this.mRendererTask.width();
                            i6 = AbstractRendererHolder.this.mRendererTask.height();
                            byteBuffer = ByteBuffer.allocateDirect(i5 * i6 * 4);
                            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            if (this.captureSurface != null) {
                                this.captureSurface.release();
                                this.captureSurface = null;
                            }
                            this.captureSurface = this.eglBase.createOffscreen(i5, i6);
                        }
                        if (!AbstractRendererHolder.this.isRunning || i5 <= 0 || i6 <= 0) {
                            if (AbstractRendererHolder.this.isRunning) {
                                Log.w(AbstractRendererHolder.TAG, "#captureLoopGLES3:unexpectedly width/height is zero");
                            }
                        } else {
                            AbstractRendererHolder.setMirror(this.mMvpMatrix, AbstractRendererHolder.this.mRendererTask.mirror());
                            float[] fArr = this.mMvpMatrix;
                            fArr[5] = fArr[5] * (-1.0f);
                            this.drawer.setMvpMatrix(this.mMvpMatrix, 0);
                            this.captureSurface.makeCurrent();
                            this.drawer.draw(AbstractRendererHolder.this.mRendererTask.mTexId, AbstractRendererHolder.this.mRendererTask.mTexMatrix, 0);
                            this.captureSurface.swap();
                            byteBuffer.clear();
                            GLES30.glReadPixels(0, 0, i5, i6, 6408, 5121, byteBuffer);
                            Bitmap.CompressFormat captureFormat = AbstractRendererHolder.getCaptureFormat(AbstractRendererHolder.this.mCaptureFormat);
                            try {
                                Bitmap createBitmap = Bitmap.createBitmap(i5, i6, Bitmap.Config.ARGB_8888);
                                byteBuffer.clear();
                                createBitmap.copyPixelsFromBuffer(byteBuffer);
                                createBitmap.compress(captureFormat, i7, AbstractRendererHolder.this.mCaptureStream);
                                createBitmap.recycle();
                                AbstractRendererHolder.this.mCaptureStream.flush();
                                try {
                                    AbstractRendererHolder.this.mCaptureStream.close();
                                } catch (IOException e) {
                                    Log.w(AbstractRendererHolder.TAG, "failed to save file", e);
                                }
                            } catch (Throwable th) {
                                try {
                                    AbstractRendererHolder.this.mCaptureStream.close();
                                } catch (IOException e) {
                                    Log.w(AbstractRendererHolder.TAG, "failed to close stream", e);
                                }
                                throw th;
                            }
                        }
                        AbstractRendererHolder.this.mCaptureStream = null;
                        AbstractRendererHolder.this.mSync.notifyAll();
                    }
                }
                synchronized (AbstractRendererHolder.this.mSync) {
                    AbstractRendererHolder.this.mSync.notifyAll();
                }
            }

            private final void release() {
                EGLBase.IEglSurface iEglSurface = this.captureSurface;
                if (iEglSurface != null) {
                    iEglSurface.makeCurrent();
                    this.captureSurface.release();
                    this.captureSurface = null;
                }
                GLDrawer2D gLDrawer2D = this.drawer;
                if (gLDrawer2D != null) {
                    gLDrawer2D.release();
                    this.drawer = null;
                }
                EGLBase eGLBase = this.eglBase;
                if (eGLBase != null) {
                    eGLBase.release();
                    this.eglBase = null;
                }
            }
        };
        this.mCallback = renderHolderCallback;
        this.mRendererTask = createRendererTask(i, i2, i3, iContext, i4);
        new Thread(this.mRendererTask, RENDERER_THREAD_NAME).start();
        if (!this.mRendererTask.waitReady()) {
            throw new RuntimeException("failed to start renderer thread");
        }
        startCaptureTask();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void release() {
        this.mRendererTask.release();
        synchronized (this.mSync) {
            this.isRunning = false;
            this.mSync.notifyAll();
        }
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public EGLBase.IContext getContext() {
        return this.mRendererTask.getContext();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public Surface getSurface() {
        return this.mRendererTask.getSurface();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public SurfaceTexture getSurfaceTexture() {
        return this.mRendererTask.getSurfaceTexture();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void reset() {
        this.mRendererTask.checkMasterSurface();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void resize(int i, int i2) throws IllegalStateException {
        this.mRendererTask.resize(i, i2);
    }

    @Override // com.serenegiant.glutils.IRendererCommon
    public void setMirror(int i) {
        this.mRendererTask.mirror(i % 4);
    }

    @Override // com.serenegiant.glutils.IRendererCommon
    public int getMirror() {
        return this.mRendererTask.mirror();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void addSurface(int i, Object obj, boolean z) throws IllegalStateException, IllegalArgumentException {
        this.mRendererTask.addSurface(i, obj);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void addSurface(int i, Object obj, boolean z, int i2) throws IllegalStateException, IllegalArgumentException {
        this.mRendererTask.addSurface(i, obj, i2);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void removeSurface(int i) {
        this.mRendererTask.removeSurface(i);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void removeSurfaceAll() {
        this.mRendererTask.removeSurfaceAll();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void clearSurface(int i, int i2) {
        this.mRendererTask.clearSurface(i, i2);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void clearSurfaceAll(int i) {
        this.mRendererTask.clearSurfaceAll(i);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void setMvpMatrix(int i, int i2, float[] fArr) {
        this.mRendererTask.setMvpMatrix(i, i2, fArr);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public boolean isEnabled(int i) {
        return this.mRendererTask.isEnabled(i);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void setEnabled(int i, boolean z) {
        this.mRendererTask.setEnabled(i, z);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void requestFrame() {
        this.mRendererTask.removeRequest(1);
        this.mRendererTask.offer(1);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public int getCount() {
        return this.mRendererTask.getCount();
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    @Deprecated
    public void captureStillAsync(String str) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), 80, false);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    @Deprecated
    public void captureStillAsync(String str, int i) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), i, false);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void captureStill(String str) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), 80, true);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void captureStill(String str, int i) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), i, true);
    }

    @Override // com.serenegiant.glutils.IRendererHolder
    public void captureStill(OutputStream outputStream, int i, int i2) throws IllegalStateException {
        captureStill(outputStream, i, i2, true);
    }

    private void captureStill(OutputStream outputStream, int i, int i2, boolean z) throws IllegalStateException {
        synchronized (this.mSync) {
            if (!this.isRunning) {
                throw new IllegalStateException("already released?");
            }
            if (this.mCaptureStream != null) {
                throw new IllegalStateException("already run still capturing now");
            }
            this.mCaptureStream = outputStream;
            this.mCaptureFormat = i;
            this.mCaptureCompression = i2;
            this.mSync.notifyAll();
            if (z) {
                while (this.isRunning && this.mCaptureStream != null) {
                    try {
                        this.mSync.wait(1000L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
    }

    private static int getCaptureFormat(String str) throws IllegalArgumentException {
        str.toLowerCase();
        if (str.endsWith(UVCCameraHelper.SUFFIX_JPEG) || str.endsWith(".jpeg")) {
            return 0;
        }
        if (str.endsWith(".png")) {
            return 1;
        }
        if (str.endsWith(".webp")) {
            return 2;
        }
        throw new IllegalArgumentException("unknown compress format(extension)");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap.CompressFormat getCaptureFormat(int i) {
        if (i == 0) {
            return Bitmap.CompressFormat.JPEG;
        }
        if (i == 1) {
            return Bitmap.CompressFormat.PNG;
        }
        if (i == 2) {
            return Bitmap.CompressFormat.WEBP;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    protected void startCaptureTask() {
        new Thread(this.mCaptureTask, CAPTURE_THREAD_NAME).start();
        synchronized (this.mSync) {
            if (!this.isRunning) {
                try {
                    this.mSync.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    protected void notifyCapture() {
        synchronized (this.mCaptureTask) {
            this.mCaptureTask.notify();
        }
    }

    protected void callOnCreate(Surface surface) {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onCreate(surface);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnFrameAvailable() {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onFrameAvailable();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnDestroy() {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onDestroy();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    protected static abstract class BaseRendererTask extends EglTask {
        private final SparseArray<RendererSurfaceRec> mClients;
        private volatile boolean mIsFirstFrameRendered;
        private Surface mMasterSurface;
        private SurfaceTexture mMasterTexture;
        private int mMirror;
        protected final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener;
        private final AbstractRendererHolder mParent;
        private int mRotation;
        int mTexId;
        final float[] mTexMatrix;
        private int mVideoHeight;
        private int mVideoWidth;

        protected void handleRotate(int i, int i2) {
        }

        protected abstract void internalOnStart();

        protected abstract void internalOnStop();

        protected abstract void onDrawClient(RendererSurfaceRec rendererSurfaceRec, int i, float[] fArr);

        @Override // com.serenegiant.utils.MessageTask
        protected boolean onError(Exception exc) {
            return false;
        }

        protected abstract void preprocess();

        public BaseRendererTask(AbstractRendererHolder abstractRendererHolder, int i, int i2) {
            this(abstractRendererHolder, i, i2, 3, null, 2);
        }

        public BaseRendererTask(AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, EGLBase.IContext iContext, int i4) {
            super(i3, iContext, i4);
            this.mClients = new SparseArray<>();
            this.mTexMatrix = new float[16];
            this.mMirror = 0;
            this.mRotation = 0;
            this.mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() { // from class: com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask.1
                @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    BaseRendererTask.this.removeRequest(1);
                    BaseRendererTask.this.mIsFirstFrameRendered = true;
                    BaseRendererTask.this.offer(1);
                }
            };
            this.mParent = abstractRendererHolder;
            this.mVideoWidth = i <= 0 ? UVCCamera.DEFAULT_PREVIEW_WIDTH : i;
            this.mVideoHeight = i2 <= 0 ? 480 : i2;
        }

        @Override // com.serenegiant.utils.MessageTask
        protected final void onStart() {
            handleReCreateMasterSurface();
            internalOnStart();
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = true;
                this.mParent.mSync.notifyAll();
            }
        }

        @Override // com.serenegiant.utils.MessageTask
        protected void onStop() {
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = false;
                this.mParent.mSync.notifyAll();
            }
            makeCurrent();
            internalOnStop();
            handleReleaseMasterSurface();
            handleRemoveAll();
        }

        @Override // com.serenegiant.utils.MessageTask
        protected Object processRequest(int i, int i2, int i3, Object obj) {
            switch (i) {
                case 1:
                    handleDraw();
                    break;
                case 2:
                    handleResize(i2, i3);
                    break;
                case 3:
                    handleAddSurface(i2, obj, i3);
                    break;
                case 4:
                    handleRemoveSurface(i2);
                    break;
                case 5:
                    handleReCreateMasterSurface();
                    break;
                case 6:
                    handleMirror(i2);
                    break;
                case 7:
                    handleRotate(i2, i3);
                    break;
                case 8:
                    handleClear(i2, i3);
                    break;
                case 9:
                    handleClearAll(i2);
                    break;
                case 10:
                    handleSetMvp(i2, i3, obj);
                    break;
                case 12:
                    handleRemoveAll();
                    break;
            }
            return null;
        }

        public Surface getSurface() {
            checkMasterSurface();
            return this.mMasterSurface;
        }

        public SurfaceTexture getSurfaceTexture() {
            checkMasterSurface();
            return this.mMasterTexture;
        }

        public void addSurface(int i, Object obj) throws IllegalStateException, IllegalArgumentException {
            addSurface(i, obj, -1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x0030, code lost:
        
            r4.mClients.wait();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void addSurface(int r5, Object r6, int r7) throws IllegalStateException, IllegalArgumentException {
            /*
                r4 = this;
                r4.checkFinished()
                boolean r0 = r6 instanceof android.graphics.SurfaceTexture
                if (r0 != 0) goto L18
                boolean r0 = r6 instanceof android.view.Surface
                if (r0 != 0) goto L18
                boolean r0 = r6 instanceof android.view.SurfaceHolder
                if (r0 == 0) goto L10
                goto L18
            L10:
                java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
                java.lang.String r6 = "Surface should be one of Surface, SurfaceTexture or SurfaceHolder"
                r5.<init>(r6)
                throw r5
            L18:
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.Throwable -> L40
                java.lang.Object r1 = r1.get(r5)     // Catch: java.lang.Throwable -> L40
                if (r1 != 0) goto L3e
            L23:
                boolean r1 = r4.isRunning()     // Catch: java.lang.Throwable -> L40
                if (r1 == 0) goto L3e
                r1 = 3
                boolean r1 = r4.offer(r1, r5, r7, r6)     // Catch: java.lang.Throwable -> L40
                if (r1 == 0) goto L36
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r5 = r4.mClients     // Catch: java.lang.InterruptedException -> L3e java.lang.Throwable -> L40
                r5.wait()     // Catch: java.lang.InterruptedException -> L3e java.lang.Throwable -> L40
                goto L3e
            L36:
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.InterruptedException -> L3e java.lang.Throwable -> L40
                r2 = 5
                r1.wait(r2)     // Catch: java.lang.InterruptedException -> L3e java.lang.Throwable -> L40
                goto L23
            L3e:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L40
                return
            L40:
                r5 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L40
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask.addSurface(int, java.lang.Object, int):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x0018, code lost:
        
            r4.mClients.wait();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void removeSurface(int r5) {
            /*
                r4 = this;
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.Throwable -> L28
                java.lang.Object r1 = r1.get(r5)     // Catch: java.lang.Throwable -> L28
                if (r1 == 0) goto L26
            Lb:
                boolean r1 = r4.isRunning()     // Catch: java.lang.Throwable -> L28
                if (r1 == 0) goto L26
                r1 = 4
                boolean r1 = r4.offer(r1, r5)     // Catch: java.lang.Throwable -> L28
                if (r1 == 0) goto L1e
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r5 = r4.mClients     // Catch: java.lang.InterruptedException -> L26 java.lang.Throwable -> L28
                r5.wait()     // Catch: java.lang.InterruptedException -> L26 java.lang.Throwable -> L28
                goto L26
            L1e:
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.InterruptedException -> L26 java.lang.Throwable -> L28
                r2 = 5
                r1.wait(r2)     // Catch: java.lang.InterruptedException -> L26 java.lang.Throwable -> L28
                goto Lb
            L26:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L28
                return
            L28:
                r5 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L28
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask.removeSurface(int):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0011, code lost:
        
            r4.mClients.wait();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void removeSurfaceAll() {
            /*
                r4 = this;
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
            L3:
                boolean r1 = r4.isRunning()     // Catch: java.lang.Throwable -> L21
                if (r1 == 0) goto L1f
                r1 = 12
                boolean r1 = r4.offer(r1)     // Catch: java.lang.Throwable -> L21
                if (r1 == 0) goto L17
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.InterruptedException -> L1f java.lang.Throwable -> L21
                r1.wait()     // Catch: java.lang.InterruptedException -> L1f java.lang.Throwable -> L21
                goto L1f
            L17:
                android.util.SparseArray<com.serenegiant.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch: java.lang.InterruptedException -> L1f java.lang.Throwable -> L21
                r2 = 5
                r1.wait(r2)     // Catch: java.lang.InterruptedException -> L1f java.lang.Throwable -> L21
                goto L3
            L1f:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L21
                return
            L21:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L21
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask.removeSurfaceAll():void");
        }

        public void clearSurface(int i, int i2) {
            checkFinished();
            offer(8, i, i2);
        }

        public void clearSurfaceAll(int i) {
            checkFinished();
            offer(9, i);
        }

        public void setMvpMatrix(int i, int i2, float[] fArr) {
            checkFinished();
            offer(10, i, i2, fArr);
        }

        public boolean isEnabled(int i) {
            boolean z;
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                z = rendererSurfaceRec != null && rendererSurfaceRec.isEnabled();
            }
            return z;
        }

        public void setEnabled(int i, boolean z) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    rendererSurfaceRec.setEnabled(z);
                }
            }
        }

        public int getCount() {
            int size;
            synchronized (this.mClients) {
                size = this.mClients.size();
            }
            return size;
        }

        public void resize(int i, int i2) throws IllegalStateException {
            checkFinished();
            if (i <= 0 || i2 <= 0) {
                return;
            }
            if (this.mVideoWidth == i && this.mVideoHeight == i2) {
                return;
            }
            offer(2, i, i2);
        }

        protected int width() {
            return this.mVideoWidth;
        }

        protected int height() {
            return this.mVideoHeight;
        }

        public void mirror(int i) {
            checkFinished();
            if (this.mMirror != i) {
                offer(6, i);
            }
        }

        public int mirror() {
            return this.mMirror;
        }

        public void checkMasterSurface() {
            checkFinished();
            Surface surface = this.mMasterSurface;
            if (surface == null || !surface.isValid()) {
                Log.d(AbstractRendererHolder.TAG, "checkMasterSurface:invalid master surface");
                offerAndWait(5, 0, 0, null);
            }
        }

        protected void checkFinished() throws IllegalStateException {
            if (isFinished()) {
                throw new IllegalStateException("already finished");
            }
        }

        protected AbstractRendererHolder getParent() {
            return this.mParent;
        }

        protected void handleDraw() {
            Surface surface = this.mMasterSurface;
            if (surface == null || !surface.isValid()) {
                Log.e(AbstractRendererHolder.TAG, "checkMasterSurface:invalid master surface");
                offer(5);
                return;
            }
            if (this.mIsFirstFrameRendered) {
                try {
                    makeCurrent();
                    handleUpdateTexture();
                    this.mParent.notifyCapture();
                    preprocess();
                    handleDrawClients();
                    this.mParent.callOnFrameAvailable();
                } catch (Exception e) {
                    Log.e(AbstractRendererHolder.TAG, "draw:thread id =" + Thread.currentThread().getId(), e);
                    offer(5);
                    return;
                }
            }
            makeCurrent();
            GLES20.glClear(16384);
            GLES20.glFlush();
        }

        protected void handleUpdateTexture() {
            this.mMasterTexture.updateTexImage();
            this.mMasterTexture.getTransformMatrix(this.mTexMatrix);
        }

        protected void handleDrawClients() {
            synchronized (this.mClients) {
                for (int size = this.mClients.size() - 1; size >= 0; size--) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(size);
                    if (valueAt != null && valueAt.canDraw()) {
                        try {
                            onDrawClient(valueAt, this.mTexId, this.mTexMatrix);
                        } catch (Exception unused) {
                            this.mClients.removeAt(size);
                            valueAt.release();
                        }
                    }
                }
            }
        }

        protected void handleAddSurface(int i, Object obj, int i2) {
            checkSurface();
            synchronized (this.mClients) {
                if (this.mClients.get(i) != null) {
                    Log.w(AbstractRendererHolder.TAG, "surface is already added: id=" + i);
                } else {
                    try {
                        RendererSurfaceRec newInstance = RendererSurfaceRec.newInstance(getEgl(), obj, i2);
                        setMirror(newInstance, this.mMirror);
                        this.mClients.append(i, newInstance);
                    } catch (Exception e) {
                        Log.w(AbstractRendererHolder.TAG, "invalid surface: surface=" + obj, e);
                    }
                }
                this.mClients.notifyAll();
            }
        }

        protected void handleRemoveSurface(int i) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    this.mClients.remove(i);
                    if (rendererSurfaceRec.isValid()) {
                        rendererSurfaceRec.clear(0);
                    }
                    rendererSurfaceRec.release();
                }
                checkSurface();
                this.mClients.notifyAll();
            }
        }

        protected void handleRemoveAll() {
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(i);
                    if (valueAt != null) {
                        if (valueAt.isValid()) {
                            valueAt.clear(0);
                        }
                        valueAt.release();
                    }
                }
                this.mClients.clear();
                this.mClients.notifyAll();
            }
        }

        protected void checkSurface() {
            synchronized (this.mClients) {
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

        protected void handleClear(int i, int i2) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                if (rendererSurfaceRec != null && rendererSurfaceRec.isValid()) {
                    rendererSurfaceRec.clear(i2);
                }
            }
        }

        protected void handleClearAll(int i) {
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i2 = 0; i2 < size; i2++) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(i2);
                    if (valueAt != null && valueAt.isValid()) {
                        valueAt.clear(i);
                    }
                }
            }
        }

        protected void handleSetMvp(int i, int i2, Object obj) {
            if (obj instanceof float[]) {
                float[] fArr = (float[]) obj;
                if (fArr.length >= i2 + 16) {
                    synchronized (this.mClients) {
                        RendererSurfaceRec rendererSurfaceRec = this.mClients.get(i);
                        if (rendererSurfaceRec != null && rendererSurfaceRec.isValid()) {
                            System.arraycopy(fArr, i2, rendererSurfaceRec.mMvpMatrix, 0, 16);
                        }
                    }
                }
            }
        }

        protected void handleReCreateMasterSurface() {
            makeCurrent();
            handleReleaseMasterSurface();
            makeCurrent();
            this.mTexId = GLHelper.initTex(ShaderConst.GL_TEXTURE_EXTERNAL_OES, 9728);
            this.mMasterTexture = new SurfaceTexture(this.mTexId);
            this.mMasterSurface = new Surface(this.mMasterTexture);
            if (BuildCheck.isAndroid4_1()) {
                this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);
            }
            this.mMasterTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener);
            this.mParent.callOnCreate(this.mMasterSurface);
        }

        protected void handleReleaseMasterSurface() {
            Surface surface = this.mMasterSurface;
            if (surface != null) {
                try {
                    surface.release();
                } catch (Exception e) {
                    Log.w(AbstractRendererHolder.TAG, e);
                }
                this.mMasterSurface = null;
                this.mParent.callOnDestroy();
            }
            SurfaceTexture surfaceTexture = this.mMasterTexture;
            if (surfaceTexture != null) {
                try {
                    surfaceTexture.release();
                } catch (Exception e2) {
                    Log.w(AbstractRendererHolder.TAG, e2);
                }
                this.mMasterTexture = null;
            }
            int i = this.mTexId;
            if (i != 0) {
                GLHelper.deleteTex(i);
                this.mTexId = 0;
            }
        }

        protected void handleResize(int i, int i2) {
            this.mVideoWidth = i;
            this.mVideoHeight = i2;
            if (BuildCheck.isAndroid4_1()) {
                this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);
            }
        }

        protected void handleMirror(int i) {
            this.mMirror = i;
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i2 = 0; i2 < size; i2++) {
                    RendererSurfaceRec valueAt = this.mClients.valueAt(i2);
                    if (valueAt != null) {
                        setMirror(valueAt, i);
                    }
                }
            }
        }

        protected void setMirror(RendererSurfaceRec rendererSurfaceRec, int i) {
            RendererHolder.setMirror(rendererSurfaceRec.mMvpMatrix, i);
        }
    }

    protected static abstract class RendererTask extends BaseRendererTask {
        protected GLDrawer2D mDrawer;

        @Override // com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask
        protected void preprocess() {
        }

        public RendererTask(AbstractRendererHolder abstractRendererHolder, int i, int i2) {
            super(abstractRendererHolder, i, i2);
        }

        public RendererTask(AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, EGLBase.IContext iContext, int i4) {
            super(abstractRendererHolder, i, i2, i3, iContext, i4);
        }

        @Override // com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask
        protected void internalOnStart() {
            this.mDrawer = new GLDrawer2D(true);
        }

        @Override // com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask
        protected void internalOnStop() {
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
        }

        @Override // com.serenegiant.glutils.AbstractRendererHolder.BaseRendererTask
        protected void onDrawClient(RendererSurfaceRec rendererSurfaceRec, int i, float[] fArr) {
            rendererSurfaceRec.draw(this.mDrawer, i, fArr);
        }
    }

    protected static void setMirror(float[] fArr, int i) {
        if (i == 0) {
            fArr[0] = Math.abs(fArr[0]);
            fArr[5] = Math.abs(fArr[5]);
            return;
        }
        if (i == 1) {
            fArr[0] = -Math.abs(fArr[0]);
            fArr[5] = Math.abs(fArr[5]);
        } else if (i == 2) {
            fArr[0] = Math.abs(fArr[0]);
            fArr[5] = -Math.abs(fArr[5]);
        } else {
            if (i != 3) {
                return;
            }
            fArr[0] = -Math.abs(fArr[0]);
            fArr[5] = -Math.abs(fArr[5]);
        }
    }

    protected static void rotate(float[] fArr, int i) {
        if (i % 180 != 0) {
            Matrix.rotateM(fArr, 0, i, 0.0f, 0.0f, 1.0f);
        }
    }

    protected static void setRotation(float[] fArr, int i) {
        Matrix.setIdentityM(fArr, 0);
        if (i % 180 != 0) {
            Matrix.rotateM(fArr, 0, i, 0.0f, 0.0f, 1.0f);
        }
    }
}
