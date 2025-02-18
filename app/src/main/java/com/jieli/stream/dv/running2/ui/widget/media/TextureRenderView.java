package com.jieli.stream.dv.running2.ui.widget.media;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.jieli.stream.dv.running2.ui.widget.media.IRenderView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder;
import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

/* loaded from: classes.dex */
public class TextureRenderView extends TextureView implements IRenderView {
    private static final String TAG = "TextureRenderView";
    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public View getView() {
        return this;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public boolean shouldWaitForResize() {
        return false;
    }

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        SurfaceCallback surfaceCallback = new SurfaceCallback(this);
        this.mSurfaceCallback = surfaceCallback;
        setSurfaceTextureListener(surfaceCallback);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.mSurfaceCallback.willDetachFromWindow();
        super.onDetachedFromWindow();
        this.mSurfaceCallback.didDetachFromWindow();
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void setVideoSize(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            return;
        }
        this.mMeasureHelper.setVideoSize(i, i2);
        requestLayout();
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void setVideoSampleAspectRatio(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            return;
        }
        this.mMeasureHelper.setVideoSampleAspectRatio(i, i2);
        requestLayout();
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void setVideoRotation(int i) {
        this.mMeasureHelper.setVideoRotation(i);
        setRotation(i);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void setAspectRatio(int i) {
        this.mMeasureHelper.setAspectRatio(i);
        requestLayout();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.mMeasureHelper.doMeasure(i, i2);
        setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public IRenderView.ISurfaceHolder getSurfaceHolder() {
        return new InternalSurfaceHolder(this, this.mSurfaceCallback.mSurfaceTexture, this.mSurfaceCallback);
    }

    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        private SurfaceTexture mSurfaceTexture;
        private ISurfaceTextureHost mSurfaceTextureHost;
        private TextureRenderView mTextureView;

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView.ISurfaceHolder
        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        public InternalSurfaceHolder(TextureRenderView textureRenderView, SurfaceTexture surfaceTexture, ISurfaceTextureHost iSurfaceTextureHost) {
            this.mTextureView = textureRenderView;
            this.mSurfaceTexture = surfaceTexture;
            this.mSurfaceTextureHost = iSurfaceTextureHost;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView.ISurfaceHolder
        public void bindToMediaPlayer(IMediaPlayer iMediaPlayer) {
            if (iMediaPlayer == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 16 && (iMediaPlayer instanceof ISurfaceTextureHolder)) {
                ISurfaceTextureHolder iSurfaceTextureHolder = (ISurfaceTextureHolder) iMediaPlayer;
                this.mTextureView.mSurfaceCallback.setOwnSurfaceTexture(false);
                SurfaceTexture surfaceTexture = iSurfaceTextureHolder.getSurfaceTexture();
                if (surfaceTexture != null) {
                    this.mTextureView.setSurfaceTexture(surfaceTexture);
                    return;
                } else {
                    iSurfaceTextureHolder.setSurfaceTexture(this.mSurfaceTexture);
                    iSurfaceTextureHolder.setSurfaceTextureHost(this.mTextureView.mSurfaceCallback);
                    return;
                }
            }
            iMediaPlayer.setSurface(openSurface());
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView.ISurfaceHolder
        public IRenderView getRenderView() {
            return this.mTextureView;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView.ISurfaceHolder
        public SurfaceTexture getSurfaceTexture() {
            return this.mSurfaceTexture;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView.ISurfaceHolder
        public Surface openSurface() {
            if (this.mSurfaceTexture == null) {
                return null;
            }
            return new Surface(this.mSurfaceTexture);
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void addRenderCallback(IRenderView.IRenderCallback iRenderCallback) {
        this.mSurfaceCallback.addRenderCallback(iRenderCallback);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IRenderView
    public void removeRenderCallback(IRenderView.IRenderCallback iRenderCallback) {
        this.mSurfaceCallback.removeRenderCallback(iRenderCallback);
    }

    private static final class SurfaceCallback implements TextureView.SurfaceTextureListener, ISurfaceTextureHost {
        private int mHeight;
        private boolean mIsFormatChanged;
        private SurfaceTexture mSurfaceTexture;
        private WeakReference<TextureRenderView> mWeakRenderView;
        private int mWidth;
        private boolean mOwnSurfaceTexture = true;
        private boolean mWillDetachFromWindow = false;
        private boolean mDidDetachFromWindow = false;
        private Map<IRenderView.IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        public SurfaceCallback(TextureRenderView textureRenderView) {
            this.mWeakRenderView = new WeakReference<>(textureRenderView);
        }

        public void setOwnSurfaceTexture(boolean z) {
            this.mOwnSurfaceTexture = z;
        }

        public void addRenderCallback(IRenderView.IRenderCallback iRenderCallback) {
            InternalSurfaceHolder internalSurfaceHolder;
            this.mRenderCallbackMap.put(iRenderCallback, iRenderCallback);
            if (this.mSurfaceTexture != null) {
                internalSurfaceHolder = new InternalSurfaceHolder(this.mWeakRenderView.get(), this.mSurfaceTexture, this);
                iRenderCallback.onSurfaceCreated(internalSurfaceHolder, this.mWidth, this.mHeight);
            } else {
                internalSurfaceHolder = null;
            }
            if (this.mIsFormatChanged) {
                if (internalSurfaceHolder == null) {
                    internalSurfaceHolder = new InternalSurfaceHolder(this.mWeakRenderView.get(), this.mSurfaceTexture, this);
                }
                iRenderCallback.onSurfaceChanged(internalSurfaceHolder, 0, this.mWidth, this.mHeight);
            }
        }

        public void removeRenderCallback(IRenderView.IRenderCallback iRenderCallback) {
            this.mRenderCallbackMap.remove(iRenderCallback);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            this.mSurfaceTexture = surfaceTexture;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            InternalSurfaceHolder internalSurfaceHolder = new InternalSurfaceHolder(this.mWeakRenderView.get(), surfaceTexture, this);
            Iterator<IRenderView.IRenderCallback> it = this.mRenderCallbackMap.keySet().iterator();
            while (it.hasNext()) {
                it.next().onSurfaceCreated(internalSurfaceHolder, 0, 0);
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            this.mSurfaceTexture = surfaceTexture;
            this.mIsFormatChanged = true;
            this.mWidth = i;
            this.mHeight = i2;
            InternalSurfaceHolder internalSurfaceHolder = new InternalSurfaceHolder(this.mWeakRenderView.get(), surfaceTexture, this);
            Iterator<IRenderView.IRenderCallback> it = this.mRenderCallbackMap.keySet().iterator();
            while (it.hasNext()) {
                it.next().onSurfaceChanged(internalSurfaceHolder, 0, i, i2);
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            this.mSurfaceTexture = surfaceTexture;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            InternalSurfaceHolder internalSurfaceHolder = new InternalSurfaceHolder(this.mWeakRenderView.get(), surfaceTexture, this);
            Iterator<IRenderView.IRenderCallback> it = this.mRenderCallbackMap.keySet().iterator();
            while (it.hasNext()) {
                it.next().onSurfaceDestroyed(internalSurfaceHolder);
            }
            Log.d(TextureRenderView.TAG, "onSurfaceTextureDestroyed: destroy: " + this.mOwnSurfaceTexture);
            return this.mOwnSurfaceTexture;
        }

        @Override // tv.danmaku.ijk.media.player.ISurfaceTextureHost
        public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: null");
                return;
            }
            if (this.mDidDetachFromWindow) {
                if (surfaceTexture != this.mSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                    return;
                } else if (!this.mOwnSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
                    surfaceTexture.release();
                    return;
                } else {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
                    return;
                }
            }
            if (this.mWillDetachFromWindow) {
                if (surfaceTexture != this.mSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                    return;
                } else if (!this.mOwnSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                    return;
                } else {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
                    return;
                }
            }
            if (surfaceTexture != this.mSurfaceTexture) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
                surfaceTexture.release();
            } else if (!this.mOwnSurfaceTexture) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
                setOwnSurfaceTexture(true);
            } else {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: will released by TextureView");
            }
        }

        public void willDetachFromWindow() {
            Log.d(TextureRenderView.TAG, "willDetachFromWindow()");
            this.mWillDetachFromWindow = true;
        }

        public void didDetachFromWindow() {
            Log.d(TextureRenderView.TAG, "didDetachFromWindow()");
            this.mDidDetachFromWindow = true;
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(TextureRenderView.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(TextureRenderView.class.getName());
    }
}
