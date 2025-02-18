package com.serenegiant.mediastore;

import android.app.ActivityManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.collection.LruCache;
import androidx.cursoradapter.widget.CursorAdapter;
import com.serenegiant.common.R;
import com.serenegiant.mediastore.MediaStoreHelper;
import com.serenegiant.utils.ThreadPool;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class MediaStoreAdapter extends CursorAdapter {
    private static final int CACHE_RATE = 8;
    private static final boolean DEBUG = false;
    private static final String TAG = MediaStoreAdapter.class.getSimpleName();
    private static LruCache<String, Bitmap> sThumbnailCache;
    private final MediaStoreHelper.MediaInfo info;
    private final ContentResolver mCr;
    private final int mHashCode;
    private final LayoutInflater mInflater;
    private final int mLayoutId;
    private Cursor mMediaInfoCursor;
    private int mMediaType;
    private final int mMemClass;
    private final MyAsyncQueryHandler mQueryHandler;
    private String mSelection;
    private String[] mSelectionArgs;
    private boolean mShowTitle;
    private int mThumbnailHeight;
    private int mThumbnailWidth;

    public MediaStoreAdapter(Context context, int i) {
        super(context, (Cursor) null, 2);
        this.mThumbnailWidth = 200;
        this.mThumbnailHeight = 200;
        this.mHashCode = hashCode();
        this.mShowTitle = false;
        this.mMediaType = 0;
        this.info = new MediaStoreHelper.MediaInfo();
        this.mInflater = LayoutInflater.from(context);
        this.mCr = context.getContentResolver();
        this.mQueryHandler = new MyAsyncQueryHandler(this.mCr, this);
        this.mMemClass = ((ActivityManager) context.getSystemService("activity")).getMemoryClass();
        this.mLayoutId = i;
        onContentChanged();
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View inflate = this.mInflater.inflate(this.mLayoutId, viewGroup, false);
        getViewHolder(inflate);
        return inflate;
    }

    protected MediaStoreHelper.LoaderDrawable createLoaderDrawable(ContentResolver contentResolver) {
        return new ThumbnailLoaderDrawable(contentResolver, this.mThumbnailWidth, this.mThumbnailHeight);
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = getViewHolder(view);
        ImageView imageView = viewHolder.mImageView;
        TextView textView = viewHolder.mTitleView;
        Drawable drawable = imageView.getDrawable();
        if (!(drawable instanceof MediaStoreHelper.LoaderDrawable)) {
            drawable = createLoaderDrawable(this.mCr);
            imageView.setImageDrawable(drawable);
        }
        ((MediaStoreHelper.LoaderDrawable) drawable).startLoad(cursor.getInt(2), this.mHashCode, cursor.getLong(0));
        if (textView != null) {
            textView.setVisibility(this.mShowTitle ? 0 : 8);
            if (this.mShowTitle) {
                textView.setText(cursor.getString(1));
            }
        }
    }

    private ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.mediastorephotoadapter);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            if (view instanceof ImageView) {
                viewHolder.mImageView = (ImageView) view;
                view.setTag(R.id.mediastorephotoadapter, viewHolder);
            } else {
                View findViewById = view.findViewById(R.id.thumbnail);
                if (findViewById instanceof ImageView) {
                    viewHolder.mImageView = (ImageView) findViewById;
                }
                View findViewById2 = view.findViewById(R.id.title);
                if (findViewById2 instanceof TextView) {
                    viewHolder.mTitleView = (TextView) findViewById2;
                }
                view.setTag(R.id.mediastorephotoadapter, viewHolder);
            }
        }
        return viewHolder;
    }

    protected void finalize() throws Throwable {
        changeCursor(null);
        Cursor cursor = this.mMediaInfoCursor;
        if (cursor != null) {
            cursor.close();
            this.mMediaInfoCursor = null;
        }
        super.finalize();
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter
    protected void onContentChanged() {
        createBitmapCache(false);
        this.mQueryHandler.requery();
    }

    @Override // androidx.cursoradapter.widget.CursorAdapter, android.widget.Adapter
    public Bitmap getItem(int i) {
        getMediaInfo(i, this.info);
        if (this.info.mediaType == 1) {
            try {
                return getImageThumbnail(this.mCr, this.mHashCode, getItemId(i), this.mThumbnailWidth, this.mThumbnailHeight);
            } catch (FileNotFoundException e) {
                Log.w(TAG, e);
            } catch (IOException e2) {
                Log.w(TAG, e2);
            }
        } else if (this.info.mediaType == 3) {
            try {
                return getVideoThumbnail(this.mCr, this.mHashCode, getItemId(i), this.mThumbnailWidth, this.mThumbnailHeight);
            } catch (FileNotFoundException e3) {
                Log.w(TAG, e3);
            } catch (IOException e4) {
                Log.w(TAG, e4);
            }
        }
        return null;
    }

    public int getPositionFromId(long j) {
        int count = getCount();
        MediaStoreHelper.MediaInfo mediaInfo = new MediaStoreHelper.MediaInfo();
        for (int i = 0; i < count; i++) {
            getMediaInfo(i, mediaInfo);
            if (mediaInfo.id == j) {
                return i;
            }
        }
        return -1;
    }

    public Bitmap getImage(int i, int i2, int i3) throws FileNotFoundException, IOException {
        return MediaStoreHelper.getImage(this.mCr, getItemId(i), i2, i3);
    }

    public MediaStoreHelper.MediaInfo getMediaInfo(int i) {
        return getMediaInfo(i, null);
    }

    public synchronized MediaStoreHelper.MediaInfo getMediaInfo(int i, MediaStoreHelper.MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            mediaInfo = new MediaStoreHelper.MediaInfo();
        }
        if (this.mMediaInfoCursor == null) {
            this.mMediaInfoCursor = this.mCr.query(MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mSelection, this.mSelectionArgs, null);
        }
        if (this.mMediaInfoCursor.moveToPosition(i)) {
            mediaInfo.loadFromCursor(this.mMediaInfoCursor);
        }
        return mediaInfo;
    }

    public void setThumbnailSize(int i) {
        if (this.mThumbnailWidth == i && this.mThumbnailHeight == i) {
            return;
        }
        this.mThumbnailHeight = i;
        this.mThumbnailWidth = i;
        createBitmapCache(true);
        onContentChanged();
    }

    public void setThumbnailSize(int i, int i2) {
        if (this.mThumbnailWidth == i && this.mThumbnailHeight == i2) {
            return;
        }
        this.mThumbnailWidth = i;
        this.mThumbnailHeight = i2;
        createBitmapCache(true);
        onContentChanged();
    }

    public void setShowTitle(boolean z) {
        if (this.mShowTitle != z) {
            this.mShowTitle = z;
            onContentChanged();
        }
    }

    public boolean getShowTitle() {
        return this.mShowTitle;
    }

    public int getMediaType() {
        return this.mMediaType % 3;
    }

    public void setMediaType(int i) {
        int i2 = i % 3;
        if (this.mMediaType != i2) {
            this.mMediaType = i2;
            onContentChanged();
        }
    }

    public static void destroy() {
        LruCache<String, Bitmap> lruCache = sThumbnailCache;
        if (lruCache != null) {
            lruCache.evictAll();
            sThumbnailCache = null;
        }
    }

    private static final class MyAsyncQueryHandler extends AsyncQueryHandler {
        private final MediaStoreAdapter mAdapter;

        public MyAsyncQueryHandler(ContentResolver contentResolver, MediaStoreAdapter mediaStoreAdapter) {
            super(contentResolver);
            this.mAdapter = mediaStoreAdapter;
        }

        public void requery() {
            synchronized (this.mAdapter) {
                if (this.mAdapter.mMediaInfoCursor != null) {
                    this.mAdapter.mMediaInfoCursor.close();
                    this.mAdapter.mMediaInfoCursor = null;
                }
                this.mAdapter.mSelection = MediaStoreHelper.SELECTIONS[this.mAdapter.mMediaType % 3];
                this.mAdapter.mSelectionArgs = null;
                startQuery(0, this.mAdapter, MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mAdapter.mSelection, this.mAdapter.mSelectionArgs, null);
            }
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
            Cursor swapCursor = this.mAdapter.swapCursor(cursor);
            if (swapCursor == null || swapCursor.isClosed()) {
                return;
            }
            swapCursor.close();
        }
    }

    private final void createBitmapCache(boolean z) {
        if (z && sThumbnailCache != null) {
            clearBitmapCache(hashCode());
        }
        if (sThumbnailCache == null) {
            sThumbnailCache = new LruCache<String, Bitmap>((this.mMemClass * 1048576) / 8) { // from class: com.serenegiant.mediastore.MediaStoreAdapter.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.collection.LruCache
                public int sizeOf(String str, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
        ThreadPool.preStartAllCoreThreads();
    }

    private static void clearBitmapCache(int i) {
        LruCache<String, Bitmap> lruCache = sThumbnailCache;
        if (lruCache != null) {
            if (i != 0) {
                Map<String, Bitmap> snapshot = lruCache.snapshot();
                String format = String.format(Locale.US, "%d_", Integer.valueOf(i));
                for (String str : snapshot.keySet()) {
                    if (str.startsWith(format)) {
                        sThumbnailCache.remove(str);
                    }
                }
            } else {
                lruCache.evictAll();
            }
            System.gc();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getKey(long j, long j2) {
        return String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Bitmap getImageThumbnail(ContentResolver contentResolver, long j, long j2, int i, int i2) throws IOException {
        Bitmap image;
        String key = getKey(j, j2);
        Bitmap bitmap = sThumbnailCache.get(key);
        if (bitmap == null) {
            if (i <= 0 || i2 <= 0) {
                image = MediaStoreHelper.getImage(contentResolver, j2, i, i2);
            } else {
                try {
                    image = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, j2, (i > 96 || i2 > 96 || i * i2 > 16384) ? 1 : 3, null);
                } catch (Exception unused) {
                }
            }
            bitmap = image;
            if (bitmap != null) {
                sThumbnailCache.put(key, bitmap);
            }
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Bitmap getVideoThumbnail(ContentResolver contentResolver, long j, long j2, int i, int i2) throws FileNotFoundException, IOException {
        String key = getKey(j, j2);
        Bitmap bitmap = sThumbnailCache.get(key);
        if (bitmap == null) {
            try {
                bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, j2, (i > 96 || i2 > 96 || i * i2 > 16384) ? 1 : 3, null);
            } catch (Exception unused) {
            }
            if (bitmap != null) {
                sThumbnailCache.put(key, bitmap);
            } else {
                Log.w(TAG, "failed to get video thumbnail ofr id=" + j2);
            }
        }
        return bitmap;
    }

    private static final class ViewHolder {
        ImageView mImageView;
        TextView mTitleView;

        private ViewHolder() {
        }
    }

    private static class ThumbnailLoaderDrawable extends MediaStoreHelper.LoaderDrawable {
        public ThumbnailLoaderDrawable(ContentResolver contentResolver, int i, int i2) {
            super(contentResolver, i, i2);
        }

        @Override // com.serenegiant.mediastore.MediaStoreHelper.LoaderDrawable
        protected MediaStoreHelper.ImageLoader createThumbnailLoader() {
            return new ThumbnailLoader(this);
        }

        @Override // com.serenegiant.mediastore.MediaStoreHelper.LoaderDrawable
        protected Bitmap checkBitmapCache(int i, long j) {
            return (Bitmap) MediaStoreAdapter.sThumbnailCache.get(MediaStoreAdapter.getKey(i, j));
        }
    }

    private static class ThumbnailLoader extends MediaStoreHelper.ImageLoader {
        public ThumbnailLoader(ThumbnailLoaderDrawable thumbnailLoaderDrawable) {
            super(thumbnailLoaderDrawable);
        }

        @Override // com.serenegiant.mediastore.MediaStoreHelper.ImageLoader
        protected Bitmap loadBitmap(ContentResolver contentResolver, int i, int i2, long j, int i3, int i4) {
            Bitmap bitmap = null;
            try {
            } catch (IOException e) {
                Log.w(MediaStoreAdapter.TAG, e);
            }
            if (i != 1) {
                if (i == 3) {
                    bitmap = MediaStoreAdapter.getVideoThumbnail(contentResolver, i2, j, i3, i4);
                }
                return bitmap;
            }
            bitmap = MediaStoreAdapter.getImageThumbnail(contentResolver, i2, j, i3, i4);
            return bitmap;
        }
    }
}
