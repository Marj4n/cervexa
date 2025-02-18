package com.serenegiant.mediastore;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import androidx.core.internal.view.SupportMenu;
import com.serenegiant.utils.ThreadPool;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.FutureTask;

/* loaded from: classes2.dex */
public class MediaStoreHelper {
    private static final boolean DEBUG = false;
    public static final int MEDIA_ALL = 0;
    public static final int MEDIA_IMAGE = 1;
    protected static final int MEDIA_TYPE_NUM = 3;
    public static final int MEDIA_VIDEO = 2;
    protected static final int PROJ_INDEX_DATA = 4;
    protected static final int PROJ_INDEX_DISPLAY_NAME = 5;
    protected static final int PROJ_INDEX_HEIGHT = 7;
    protected static final int PROJ_INDEX_ID = 0;
    protected static final int PROJ_INDEX_MEDIA_TYPE = 2;
    protected static final int PROJ_INDEX_MIME_TYPE = 3;
    protected static final int PROJ_INDEX_TITLE = 1;
    protected static final int PROJ_INDEX_WIDTH = 6;
    private static final String TAG = MediaStoreHelper.class.getSimpleName();
    protected static final String[] PROJ_MEDIA = {"_id", "title", "media_type", "mime_type", "_data", "_display_name", "width", "height"};
    protected static final String SELECTION_MEDIA_ALL = "media_type=1 OR media_type=3";
    protected static final String SELECTION_MEDIA_IMAGE = "media_type=1";
    protected static final String SELECTION_MEDIA_VIDEO = "media_type=3";
    protected static final String[] SELECTIONS = {SELECTION_MEDIA_ALL, SELECTION_MEDIA_IMAGE, SELECTION_MEDIA_VIDEO};
    protected static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    public static class MediaInfo {
        public String data;
        public String displayName;
        public int height;
        public long id;
        public int mediaType;
        public String mime;
        public String title;
        public int width;

        protected MediaInfo loadFromCursor(Cursor cursor) {
            if (cursor != null) {
                this.id = cursor.getLong(0);
                this.data = cursor.getString(4);
                this.title = cursor.getString(1);
                this.mime = cursor.getString(3);
                this.displayName = cursor.getString(5);
                this.mediaType = cursor.getInt(2);
                try {
                    this.width = cursor.getInt(6);
                    this.height = cursor.getInt(7);
                } catch (Exception unused) {
                }
            }
            return this;
        }

        public String toString() {
            return String.format("MediaInfo(title=%s,displayName=%s, mediaType=%s,mime=%s,data=%s)", this.title, this.displayName, mediaType(), this.mime, this.data);
        }

        private String mediaType() {
            int i = this.mediaType;
            return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? String.format(Locale.US, "unknown:%d", Integer.valueOf(this.mediaType)) : "playlist" : "video" : "audio" : "image" : "none";
        }
    }

    protected static abstract class LoaderDrawable extends Drawable implements Runnable {
        private Bitmap mBitmap;
        private final ContentResolver mContentResolver;
        private final int mHeight;
        private ImageLoader mLoader;
        private final int mWidth;
        private final Paint mPaint = new Paint();
        private final Paint mDebugPaint = new Paint();
        private final Matrix mDrawMatrix = new Matrix();
        private int mRotation = 0;

        protected abstract Bitmap checkBitmapCache(int i, long j);

        protected abstract ImageLoader createThumbnailLoader();

        public LoaderDrawable(ContentResolver contentResolver, int i, int i2) {
            this.mContentResolver = contentResolver;
            this.mDebugPaint.setColor(SupportMenu.CATEGORY_MASK);
            this.mDebugPaint.setTextSize(18.0f);
            this.mWidth = i;
            this.mHeight = i2;
        }

        @Override // android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect rect) {
            super.onBoundsChange(rect);
            updateDrawMatrix(getBounds());
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            if (this.mBitmap != null) {
                canvas.save();
                canvas.clipRect(bounds);
                canvas.concat(this.mDrawMatrix);
                canvas.rotate(this.mRotation, bounds.centerX(), bounds.centerY());
                canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
                canvas.restore();
                return;
            }
            this.mPaint.setColor(-3355444);
            canvas.drawRect(bounds, this.mPaint);
        }

        private void updateDrawMatrix(Rect rect) {
            int i;
            float f;
            if (this.mBitmap == null || rect.isEmpty()) {
                this.mDrawMatrix.reset();
                return;
            }
            float width = this.mBitmap.getWidth();
            float height = this.mBitmap.getHeight();
            int width2 = rect.width();
            float height2 = rect.height();
            float f2 = width2;
            int i2 = 0;
            if (width * height2 > f2 * height) {
                f = height2 / height;
                i2 = (int) (((f2 - (width * f)) * 0.5f) + 0.5f);
                i = 0;
            } else {
                float f3 = f2 / width;
                i = (int) (((height2 - (height * f3)) * 0.5f) + 0.5f);
                f = f3;
            }
            this.mDrawMatrix.setScale(f, f);
            this.mDrawMatrix.postTranslate(i2, i);
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            if (i != this.mPaint.getAlpha()) {
                this.mPaint.setAlpha(i);
                invalidateSelf();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.mWidth;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.mHeight;
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            Bitmap bitmap = this.mBitmap;
            return (bitmap == null || bitmap.hasAlpha() || this.mPaint.getAlpha() < 255) ? -3 : -1;
        }

        @Override // java.lang.Runnable
        public void run() {
            setBitmap(this.mLoader.getBitmap());
        }

        public void startLoad(int i, int i2, long j) {
            ImageLoader imageLoader = this.mLoader;
            if (imageLoader != null) {
                imageLoader.cancelLoad();
            }
            Bitmap checkBitmapCache = checkBitmapCache(i2, j);
            if (checkBitmapCache == null) {
                this.mBitmap = null;
                ImageLoader createThumbnailLoader = createThumbnailLoader();
                this.mLoader = createThumbnailLoader;
                createThumbnailLoader.startLoad(i, i2, j);
            } else {
                setBitmap(checkBitmapCache);
            }
            invalidateSelf();
        }

        private void setBitmap(Bitmap bitmap) {
            if (bitmap != this.mBitmap) {
                this.mBitmap = bitmap;
                updateDrawMatrix(getBounds());
            }
        }
    }

    protected static abstract class ImageLoader implements Runnable {
        private Bitmap mBitmap;
        private int mHashCode;
        private long mId;
        private int mMediaType;
        protected final LoaderDrawable mParent;
        private final FutureTask<Bitmap> mTask = new FutureTask<>(this, null);

        protected abstract Bitmap loadBitmap(ContentResolver contentResolver, int i, int i2, long j, int i3, int i4);

        public ImageLoader(LoaderDrawable loaderDrawable) {
            this.mParent = loaderDrawable;
        }

        public synchronized void startLoad(int i, int i2, long j) {
            this.mMediaType = i;
            this.mHashCode = i2;
            this.mId = j;
            this.mBitmap = null;
            ThreadPool.queueEvent(this.mTask);
        }

        public void cancelLoad() {
            this.mTask.cancel(true);
        }

        @Override // java.lang.Runnable
        public void run() {
            int i;
            int i2;
            long j;
            synchronized (this) {
                i = this.mMediaType;
                i2 = this.mHashCode;
                j = this.mId;
            }
            if (!this.mTask.isCancelled()) {
                this.mBitmap = loadBitmap(this.mParent.mContentResolver, i, i2, j, this.mParent.mWidth, this.mParent.mHeight);
            }
            if (this.mTask.isCancelled() || j != this.mId || this.mBitmap == null) {
                return;
            }
            LoaderDrawable loaderDrawable = this.mParent;
            loaderDrawable.scheduleSelf(loaderDrawable, 0L);
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }
    }

    protected static final Bitmap getImage(ContentResolver contentResolver, long j, int i, int i2) throws IOException {
        ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, j), "r");
        Bitmap bitmap = null;
        if (openFileDescriptor != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
                options.inSampleSize = calcSampleSize(options, i, i2);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
            } finally {
                openFileDescriptor.close();
            }
        }
        return bitmap;
    }

    protected static final int calcSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        int i5 = i <= 0 ? i2 > 0 ? (int) ((i3 * i2) / i4) : i3 : i;
        if (i2 <= 0) {
            i2 = i > 0 ? (int) ((i * i4) / i4) : i4;
        }
        if (i4 <= i2 && i3 <= i5) {
            return 1;
        }
        if (i3 > i4) {
            return Math.round(i4 / i2);
        }
        return Math.round(i3 / i5);
    }
}
