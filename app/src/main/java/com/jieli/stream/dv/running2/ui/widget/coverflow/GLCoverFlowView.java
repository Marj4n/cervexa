package com.jieli.stream.dv.running2.ui.widget.coverflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.utils.Utils;
import com.jieli.stream.dv.running2.bean.ThumbnailInfo;
import com.serenegiant.glutils.ShaderConst;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class GLCoverFlowView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final float FLANK_SPREAD = 0.3f;
    private static final float FRICTION = 10.0f;
    private static final int IMAGE_SIZE = 128;
    private static final float MAX_SPEED = 6.0f;
    private static final int MAX_TILES = 48;
    private static final float SCALE = 0.4f;
    private static final float SPREAD_IMAGE = 0.14f;
    private static final int TOUCH_MINIMUM_MOVE = 5;
    private static final int VISIBLE_TILES = 3;
    static int myH;
    static int myW;
    private List<ThumbnailInfo> bitmapList;
    private Runnable mAnimationRunnable;
    private int mBgBitmapId;
    private int mBgTexture;
    private FloatBuffer mBgTexturesBuffer;
    private FloatBuffer mBgVerticesBuffer;
    private DataCache<Integer, CoverFlowRecord> mCache;
    private float mDuration;
    private GL10 mGLContext;
    private boolean mInitBackground;
    private int mLastOffset;
    private OnCoverFlowListener mListener;
    private float[] mMatrix;
    private float mOffset;
    private float mStartOffset;
    private float mStartSpeed;
    private long mStartTime;
    private boolean mStopBackgroundThread;
    private FloatBuffer mTexturesBuffer;
    private boolean mTouchMoved;
    private RectF mTouchRect;
    private float mTouchStartPos;
    private float mTouchStartX;
    private float mTouchStartY;
    private VelocityTracker mVelocity;
    private FloatBuffer mVerticesBuffer;
    private int mWidth;
    private static final String tag = GLCoverFlowView.class.getSimpleName();
    private static final float[] GVertices = {-1.4f, -1.4f, 0.0f, 1.4f, -1.4f, 0.0f, -1.4f, 1.4f, 0.0f, 1.4f, 1.4f, 0.0f};
    private static final float[] GTextures = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};

    public interface OnCoverFlowListener {
        void onTileClicked(GLCoverFlowView gLCoverFlowView, int i);
    }

    public GLCoverFlowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bitmapList = new ArrayList();
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(this);
        setRenderMode(0);
        getHolder().setFormat(-3);
        this.mCache = new DataCache<>(48);
        this.mLastOffset = 0;
        this.mOffset = 0.0f;
        this.mInitBackground = false;
        this.mBgBitmapId = 0;
    }

    public GLCoverFlowView(Context context) {
        super(context);
        this.bitmapList = new ArrayList();
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(this);
        setRenderMode(0);
        getHolder().setFormat(-3);
        this.mCache = new DataCache<>(48);
        this.mLastOffset = 0;
        this.mOffset = 0.0f;
        this.mInitBackground = false;
        this.mBgBitmapId = 0;
    }

    public void setCoverFlowListener(OnCoverFlowListener onCoverFlowListener) {
        this.mListener = onCoverFlowListener;
    }

    private float checkValid(float f) {
        int size = this.bitmapList.size() - 1;
        if (f < 0.0f) {
            return 0.0f;
        }
        float f2 = size;
        return f > f2 ? f2 : f;
    }

    public void setSelection(int i) {
        endAnimation();
        this.mOffset = i;
        requestRender();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            touchBegan(motionEvent);
            return true;
        }
        if (action == 1) {
            touchEnded(motionEvent);
            return true;
        }
        if (action != 2) {
            return false;
        }
        touchMoved(motionEvent);
        return true;
    }

    private void touchBegan(MotionEvent motionEvent) {
        endAnimation();
        float x = motionEvent.getX();
        this.mTouchStartX = x;
        this.mTouchStartY = motionEvent.getY();
        this.mStartTime = System.currentTimeMillis();
        this.mStartOffset = this.mOffset;
        this.mTouchMoved = false;
        float f = ((x / this.mWidth) * 10.0f) - 5.0f;
        this.mTouchStartPos = f;
        this.mTouchStartPos = f / 2.0f;
        VelocityTracker obtain = VelocityTracker.obtain();
        this.mVelocity = obtain;
        obtain.addMovement(motionEvent);
    }

    private void touchMoved(MotionEvent motionEvent) {
        float x = (((motionEvent.getX() / this.mWidth) * 10.0f) - 5.0f) / 2.0f;
        if (!this.mTouchMoved) {
            float abs = Math.abs(motionEvent.getX() - this.mTouchStartX);
            float abs2 = Math.abs(motionEvent.getY() - this.mTouchStartY);
            if (abs < 5.0f && abs2 < 5.0f) {
                return;
            } else {
                this.mTouchMoved = true;
            }
        }
        this.mOffset = checkValid((this.mStartOffset + this.mTouchStartPos) - x);
        requestRender();
        this.mVelocity.addMovement(motionEvent);
    }

    private void touchEnded(MotionEvent motionEvent) {
        float x = (((motionEvent.getX() / this.mWidth) * 10.0f) - 5.0f) / 2.0f;
        if (this.mTouchMoved) {
            float f = this.mStartOffset + (this.mTouchStartPos - x);
            this.mStartOffset = f;
            float checkValid = checkValid(f);
            this.mStartOffset = checkValid;
            this.mOffset = checkValid;
            this.mVelocity.addMovement(motionEvent);
            this.mVelocity.computeCurrentVelocity(1000);
            double xVelocity = (this.mVelocity.getXVelocity() / this.mWidth) * 10.0d;
            if (xVelocity > 6.0d) {
                xVelocity = 6.0d;
            } else if (xVelocity < -6.0d) {
                xVelocity = -6.0d;
            }
            startAnimation(-xVelocity);
            return;
        }
        if (motionEvent.getX() <= (this.mWidth / 2) - (myW / 2) || motionEvent.getX() >= (this.mWidth / 2) + (myW / 2) || motionEvent.getY() <= (getHeight() / 2) - (myH / 2) || motionEvent.getY() >= (getHeight() / 2) + (myH / 2)) {
            return;
        }
        Log.e(tag, "mOffset=" + this.mOffset + ", pos=" + x + ", x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
        OnCoverFlowListener onCoverFlowListener = this.mListener;
        if (onCoverFlowListener != null) {
            onCoverFlowListener.onTileClicked(this, (int) (this.mOffset + 0.01d));
        }
    }

    private void startAnimation(double d) {
        if (this.mAnimationRunnable != null) {
            return;
        }
        double d2 = (d * d) / 20.0d;
        if (d < Utils.DOUBLE_EPSILON) {
            d2 = -d2;
        }
        double checkValid = checkValid((float) Math.floor(this.mStartOffset + d2 + 0.5d));
        float sqrt = (float) Math.sqrt(Math.abs(checkValid - this.mStartOffset) * 10.0d * 2.0d);
        this.mStartSpeed = sqrt;
        if (checkValid < this.mStartOffset) {
            this.mStartSpeed = -sqrt;
        }
        this.mDuration = Math.abs(this.mStartSpeed / 10.0f);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        Runnable runnable = new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.coverflow.GLCoverFlowView.1
            @Override // java.lang.Runnable
            public void run() {
                GLCoverFlowView.this.driveAnimation();
            }
        };
        this.mAnimationRunnable = runnable;
        post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void driveAnimation() {
        float currentAnimationTimeMillis = (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime) / 1000.0f;
        if (currentAnimationTimeMillis >= this.mDuration) {
            endAnimation();
        } else {
            updateAnimationAtElapsed(currentAnimationTimeMillis);
            post(this.mAnimationRunnable);
        }
    }

    private void endAnimation() {
        if (this.mAnimationRunnable != null) {
            float floor = (float) Math.floor(this.mOffset + 0.5d);
            this.mOffset = floor;
            this.mOffset = checkValid(floor);
            requestRender();
            removeCallbacks(this.mAnimationRunnable);
            this.mAnimationRunnable = null;
        }
    }

    private void updateAnimationAtElapsed(float f) {
        float f2 = this.mDuration;
        if (f > f2) {
            f = f2;
        }
        float abs = (Math.abs(this.mStartSpeed) * f) - (((10.0f * f) * f) / 2.0f);
        if (this.mStartSpeed < 0.0f) {
            abs = -abs;
        }
        this.mOffset = checkValid(this.mStartOffset + abs);
        requestRender();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.mCache.clear();
        this.mGLContext = gl10;
        this.mVerticesBuffer = makeFloatBuffer(GVertices);
        this.mTexturesBuffer = makeFloatBuffer(GTextures);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.mWidth = i;
        float f = i;
        float f2 = ((f * 0.45f) / SCALE) / 2.0f;
        float f3 = i2;
        float f4 = ((0.45f * f3) / SCALE) / 2.0f;
        float f5 = i / 2;
        float f6 = i2 / 2;
        this.mTouchRect = new RectF(f5 - f2, f6 - f4, f5 + f2, f6 + f4);
        gl10.glViewport(0, 0, i, i2);
        float f7 = f / f3;
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        float f8 = (-f7) * SCALE;
        float f9 = f7 * SCALE;
        gl10.glOrthof(f8, f9, -0.4f, SCALE, 1.0f, 3.0f);
        this.mBgVerticesBuffer = makeFloatBuffer(new float[]{f8, -0.4f, 0.0f, f9, -0.4f, 0.0f, f8, SCALE, 0.0f, f9, SCALE, 0.0f});
    }

    public void setBackgroundTexture(int i) {
        this.mBgBitmapId = i;
        this.mInitBackground = true;
    }

    public void clearTileCache() {
        this.mCache.clear();
    }

    private void initBg() {
        this.mInitBackground = false;
        if (this.mBgBitmapId == 0) {
            return;
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), this.mBgBitmapId);
        this.mBgBitmapId = 0;
        int width = decodeResource.getWidth();
        int height = decodeResource.getHeight();
        int i = 1;
        while (true) {
            if (width <= i && height <= i) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.RGB_565);
                new Canvas(createBitmap).drawBitmap(decodeResource, r8 / 2, r10 / 2, new Paint());
                GL10 gl10 = this.mGLContext;
                int[] iArr = new int[1];
                gl10.glGenTextures(1, iArr, 0);
                int i2 = iArr[0];
                this.mBgTexture = i2;
                gl10.glBindTexture(ShaderConst.GL_TEXTURE_2D, i2);
                GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createBitmap, 0);
                decodeResource.recycle();
                createBitmap.recycle();
                gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10241, 9728.0f);
                gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10240, 9729.0f);
                gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10242, 33071.0f);
                gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10243, 33071.0f);
                gl10.glTexEnvf(8960, 8704, 7681.0f);
                float f = (i - width) / 2.0f;
                float f2 = i;
                float f3 = f / f2;
                float f4 = ((i - height) / 2.0f) / f2;
                float f5 = ((width + i) / 2.0f) / f2;
                float f6 = ((i + height) / 2.0f) / f2;
                this.mBgTexturesBuffer = makeFloatBuffer(new float[]{f3, f4, f5, f4, f3, f6, f5, f6});
                return;
            }
            i <<= 1;
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        gl10.glDisable(2929);
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl10.glClear(16640);
        gl10.glEnableClientState(32884);
        gl10.glEnableClientState(32888);
        drawBg(gl10);
        draw(gl10);
    }

    public void drawBg(GL10 gl10) {
        if (this.mInitBackground) {
            initBg();
        }
        if (this.mBgTexture != 0) {
            gl10.glPushMatrix();
            gl10.glVertexPointer(3, 5126, 0, this.mBgVerticesBuffer);
            gl10.glTexCoordPointer(2, 5126, 0, this.mBgTexturesBuffer);
            gl10.glEnable(ShaderConst.GL_TEXTURE_2D);
            gl10.glBindTexture(ShaderConst.GL_TEXTURE_2D, this.mBgTexture);
            gl10.glDrawArrays(5, 0, 4);
            gl10.glPopMatrix();
        }
    }

    private void draw(GL10 gl10) {
        this.mStopBackgroundThread = true;
        gl10.glPushMatrix();
        gl10.glVertexPointer(3, 5126, 0, this.mVerticesBuffer);
        gl10.glTexCoordPointer(2, 5126, 0, this.mTexturesBuffer);
        gl10.glEnable(ShaderConst.GL_TEXTURE_2D);
        gl10.glEnable(3042);
        gl10.glBlendFunc(770, 771);
        float f = this.mOffset;
        int size = this.bitmapList.size() - 1;
        int floor = (int) Math.floor(f + 0.5d);
        int i = floor - 3;
        if (i < 0) {
            i = 0;
        }
        while (i < floor) {
            drawTile(i, i - f, gl10);
            i++;
        }
        int i2 = floor + 3;
        if (i2 <= size) {
            size = i2;
        }
        while (size >= floor) {
            drawTile(size, size - f, gl10);
            size--;
        }
        gl10.glPopMatrix();
        this.mStopBackgroundThread = false;
    }

    private void drawTile(int i, float f, GL10 gl10) {
        CoverFlowRecord tileAtIndex;
        if (i < 0 || (tileAtIndex = getTileAtIndex(i, gl10)) == null || tileAtIndex.mTexture == 0) {
            return;
        }
        if (this.mMatrix == null) {
            float[] fArr = new float[16];
            this.mMatrix = fArr;
            fArr[15] = 1.0f;
            fArr[10] = 1.0f;
            fArr[5] = 1.0f;
            fArr[0] = 1.0f;
        }
        float f2 = SPREAD_IMAGE * f;
        float f3 = FLANK_SPREAD;
        float f4 = f * FLANK_SPREAD;
        if (f4 <= FLANK_SPREAD) {
            f3 = f4 < -0.3f ? -0.3f : f4;
        }
        float[] fArr2 = this.mMatrix;
        fArr2[3] = -f3;
        fArr2[0] = 1.0f - Math.abs(f3);
        float f5 = this.mMatrix[0] * 0.38f;
        gl10.glPushMatrix();
        gl10.glBindTexture(ShaderConst.GL_TEXTURE_2D, tileAtIndex.mTexture);
        gl10.glTranslatef(f2 + (f3 * 1.0f), 0.0f, 0.0f);
        gl10.glScalef(f5, f5, 1.0f);
        gl10.glMultMatrixf(this.mMatrix, 0);
        gl10.glDrawArrays(5, 0, 4);
        gl10.glPopMatrix();
    }

    public void setDataList(List<ThumbnailInfo> list) {
        this.bitmapList = list;
    }

    public ThumbnailInfo getSelected(int i) {
        List<ThumbnailInfo> list = this.bitmapList;
        if (list == null || list.size() <= i) {
            return null;
        }
        return this.bitmapList.get(i);
    }

    public void addData(ThumbnailInfo thumbnailInfo) {
        this.bitmapList.add(thumbnailInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CoverFlowRecord getTileAtIndex(int i, GL10 gl10) {
        synchronized (this) {
            CoverFlowRecord objectForKey = this.mCache.objectForKey(Integer.valueOf(i));
            if (objectForKey == null) {
                Bitmap bitmap = this.bitmapList.get(i).getBitmap();
                if (bitmap == null) {
                    return null;
                }
                CoverFlowRecord coverFlowRecord = new CoverFlowRecord(imageToTexture(bitmap, gl10), gl10);
                this.mCache.putObjectForKey(Integer.valueOf(i), coverFlowRecord);
                objectForKey = coverFlowRecord;
            }
            return objectForKey;
        }
    }

    private static Bitmap createTextureBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        if (width > 128 || height > 128) {
            Rect rect = new Rect(0, 0, width, height);
            float f = 128.0f / (width > height ? width : height);
            int i = (int) (width * f);
            int i2 = (int) (height * f);
            float f2 = (128 - i) / 2.0f;
            float f3 = (128 - i2) / 2.0f;
            canvas.drawBitmap(bitmap, rect, new RectF(f2, f3, i + f2, i2 + f3), new Paint());
        } else {
            float f4 = (128 - width) / 2.0f;
            float f5 = (128 - height) / 2.0f;
            Log.e(tag, "left=" + f4 + ", top=" + f5 + ", w=" + width + ", h=" + height);
            canvas.drawBitmap(bitmap, f4, f5, new Paint());
        }
        myW = (int) (bitmap.getWidth() / SCALE);
        myH = (int) (bitmap.getHeight() / SCALE);
        return createBitmap;
    }

    private int imageToTexture(Bitmap bitmap, GL10 gl10) {
        int[] iArr = new int[1];
        gl10.glGenTextures(1, iArr, 0);
        gl10.glBindTexture(ShaderConst.GL_TEXTURE_2D, iArr[0]);
        Bitmap createTextureBitmap = createTextureBitmap(bitmap);
        bitmap.recycle();
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createTextureBitmap, 0);
        createTextureBitmap.recycle();
        gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10241, 9728.0f);
        gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10240, 9729.0f);
        gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10242, 33071.0f);
        gl10.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10243, 33071.0f);
        gl10.glTexEnvf(8960, 8704, 8448.0f);
        return iArr[0];
    }

    public void preLoadCache(final int i, final int i2) {
        this.mStopBackgroundThread = false;
        if (this.mGLContext != null) {
            new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.coverflow.GLCoverFlowView.2
                @Override // java.lang.Runnable
                public void run() {
                    int i3 = i;
                    if (i3 < 0) {
                        i3 = 0;
                    }
                    int size = GLCoverFlowView.this.bitmapList.size();
                    int i4 = i2;
                    if (i4 <= size) {
                        size = i4;
                    }
                    while (i3 < size && !GLCoverFlowView.this.mStopBackgroundThread) {
                        GLCoverFlowView gLCoverFlowView = GLCoverFlowView.this;
                        gLCoverFlowView.getTileAtIndex(i3, gLCoverFlowView.mGLContext);
                        i3++;
                    }
                }
            }).run();
        }
    }

    private static FloatBuffer makeFloatBuffer(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    public static class CoverFlowRecord {
        private GL10 gl;
        private int mTexture;

        public CoverFlowRecord(int i, GL10 gl10) {
            this.mTexture = i;
            this.gl = gl10;
        }

        protected void finalize() throws Throwable {
            int i = this.mTexture;
            if (i != 0) {
                this.gl.glDeleteTextures(1, new int[]{i}, 0);
            }
            super.finalize();
        }
    }
}
