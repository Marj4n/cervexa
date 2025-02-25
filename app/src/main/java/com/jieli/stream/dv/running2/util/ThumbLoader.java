package com.jieli.stream.dv.running2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.LruCache;

import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.media.codec.FrameCodec;
import com.jieli.media.codec.bean.MediaMeta;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.interfaces.OnAviThumbListener;
import com.jieli.stream.dv.running2.ui.MainApplication;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/* loaded from: classes.dex */
public class ThumbLoader {
    private static String TAG = "ThumbLoader";
    private static ThumbLoader instance;
    private LruCache<String, Bitmap> mThumbnailCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public int sizeOf(String str, Bitmap bitmap) {
            return ThumbLoader.this.sizeOfBitmap(bitmap);
        }
    };
    private Map<String, Integer> mDurationMap = new HashMap();

    public interface OnDownloadListener {
        void onResult(boolean z, String str);
    }

    public interface OnLoadThumbListener {
        void onComplete(Bitmap bitmap);
    }

    public interface OnLoadVideoThumbListener {
        void onComplete(Bitmap bitmap, int i);
    }

    private ThumbLoader() {
    }

    public static ThumbLoader getInstance() {
        if (instance == null) {
            synchronized (ThumbLoader.class) {
                if (instance == null) {
                    instance = new ThumbLoader();
                }
            }
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sizeOfBitmap(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount() / 1024;
        }
        return bitmap.getByteCount() / 1024;
    }

    public void loadWebThumbnail(final Context context, final String str, final int i, final int i2, final OnLoadThumbListener onLoadThumbListener) {
        final Handler handler = new Handler(context.getMainLooper());
        if (TextUtils.isEmpty(str)) {
            Bitmap bitmap = this.mThumbnailCache.get(IConstant.DEFAULT_PATH);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_default_picture);
                this.mThumbnailCache.put(IConstant.DEFAULT_PATH, bitmap);
            }
            handler.post(new OnCompleteRunnable(bitmap, onLoadThumbListener));
            return;
        }
        Bitmap bitmap2 = this.mThumbnailCache.get(str);
        if (bitmap2 == null) {
            HttpManager.downloadFile(str, new Callback() { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.2
                @Override // okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    Dlog.e(ThumbLoader.TAG, "-loadThumbnail- error = " + iOException.getMessage() + ", url = " + str);
                    ThumbLoader.this.loadWebThumbnail(context, null, i, i2, onLoadThumbListener);
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            byte[] bytes = body.bytes();
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            if (decodeByteArray != null) {
                                Bitmap extractThumbnail = ThumbnailUtils.extractThumbnail(decodeByteArray, i, i2, 2);
                                ThumbLoader.this.mThumbnailCache.put(str, extractThumbnail);
                                handler.post(ThumbLoader.this.new OnCompleteRunnable(extractThumbnail, onLoadThumbListener));
                            } else {
                                ThumbLoader.this.loadWebThumbnail(context, null, i, i2, onLoadThumbListener);
                            }
                        } else {
                            ThumbLoader.this.loadWebThumbnail(context, null, i, i2, onLoadThumbListener);
                        }
                    } else {
                        ThumbLoader.this.loadWebThumbnail(context, null, i, i2, onLoadThumbListener);
                    }
                    response.close();
                }
            });
        } else {
            handler.post(new OnCompleteRunnable(bitmap2, onLoadThumbListener));
        }
    }

    public void loadWebThumbnail(final Context context, final String str, final String str2, final int i, final int i2, final OnLoadThumbListener onLoadThumbListener) {
        final Handler handler = new Handler(context.getMainLooper());
        if (TextUtils.isEmpty(str)) {
            Bitmap bitmap = this.mThumbnailCache.get(IConstant.DEFAULT_PATH);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_default_picture);
                this.mThumbnailCache.put(IConstant.DEFAULT_PATH, bitmap);
            }
            handler.post(new OnCompleteRunnable(bitmap, onLoadThumbListener));
            return;
        }
        Bitmap bitmap2 = this.mThumbnailCache.get(str);
        Dlog.w(TAG, "-loadWebThumbnail- bitmap = " + bitmap2 + " ,url = " + str);
        if (bitmap2 == null) {
            HttpManager.downloadFile(str, new Callback() { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.3
                @Override // okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    Dlog.e(ThumbLoader.TAG, "-loadThumbnail- error = " + iOException.getMessage() + ", url = " + str);
                    ThumbLoader.this.loadWebThumbnail(context, null, null, i, i2, onLoadThumbListener);
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            byte[] bytes = body.bytes();
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            if (decodeByteArray != null) {
                                Bitmap extractThumbnail = ThumbnailUtils.extractThumbnail(decodeByteArray, i, i2, 2);
                                ThumbLoader.this.mThumbnailCache.put(str, extractThumbnail);
                                try {
                                    if (AppUtils.bitmapToFile(extractThumbnail, str2, 100)) {
                                        Dbug.d(ThumbLoader.TAG, "save thumdNail ok");
                                    } else {
                                        Dbug.d(ThumbLoader.TAG, "save thumdNail fail");
                                    }
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                                handler.post(ThumbLoader.this.new OnCompleteRunnable(extractThumbnail, onLoadThumbListener));
                            } else {
                                ThumbLoader.this.loadWebThumbnail(context, null, null, i, i2, onLoadThumbListener);
                            }
                        } else {
                            ThumbLoader.this.loadWebThumbnail(context, null, null, i, i2, onLoadThumbListener);
                        }
                    } else {
                        ThumbLoader.this.loadWebThumbnail(context, null, null, i, i2, onLoadThumbListener);
                    }
                    response.close();
                }
            });
        } else {
            handler.post(new OnCompleteRunnable(bitmap2, onLoadThumbListener));
        }
    }

    private class OnCompleteRunnable implements Runnable {
        private WeakReference<OnLoadThumbListener> listenerReference;
        private WeakReference<Bitmap> weakReference;

        OnCompleteRunnable(Bitmap bitmap, OnLoadThumbListener onLoadThumbListener) {
            this.weakReference = new WeakReference<>(bitmap);
            this.listenerReference = new WeakReference<>(onLoadThumbListener);
        }

        @Override // java.lang.Runnable
        public void run() {
            Bitmap bitmap = this.weakReference.get();
            OnLoadThumbListener onLoadThumbListener = this.listenerReference.get();
            if (bitmap == null || onLoadThumbListener == null) {
                return;
            }
            onLoadThumbListener.onComplete(bitmap);
        }
    }

    public Bitmap loadLocalThumbnail(Context context, String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            Bitmap bitmap = this.mThumbnailCache.get(IConstant.DEFAULT_PATH);
            if (bitmap != null) {
                return bitmap;
            }
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_default_picture);
            this.mThumbnailCache.put(IConstant.DEFAULT_PATH, decodeResource);
            return decodeResource;
        }
        Bitmap bitmap2 = this.mThumbnailCache.get(str);
        if (bitmap2 == null) {
            bitmap2 = getImageThumbnail(str, i, i2);
            if (bitmap2 == null) {
                return loadLocalThumbnail(context, null, i, i2);
            }
            this.mThumbnailCache.put(str, bitmap2);
        }
        return bitmap2;
    }

    public void loadLocalVideoThumb(Context context, String str, int i, int i2, OnLoadVideoThumbListener onLoadVideoThumbListener) {
        String str2 = "";
        Handler handler = new Handler(context.getMainLooper());
        if (TextUtils.isEmpty(str)) {
            Bitmap bitmap = this.mThumbnailCache.get(IConstant.DEFAULT_PATH);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_default_picture);
                this.mThumbnailCache.put(IConstant.DEFAULT_PATH, bitmap);
            }
            handler.post(new OnLoadVideoThumbRunnable(bitmap, 0, onLoadVideoThumbListener));
            return;
        }
        Bitmap bitmap2 = this.mThumbnailCache.get(str);
        if (bitmap2 == null) {
            if (str.contains("/")) {
                String[] r1 = str.split("/");
            } else {
                str2 = "";
            }
            if (!TextUtils.isEmpty(str2)) {
                String queryThumbPath = AppUtils.queryThumbPath(str2, AppUtils.splicingFilePath(MainApplication.getApplication().getAppName(), null, null, null));
                if (!TextUtils.isEmpty(queryThumbPath)) {
                    bitmap2 = getImageThumbnail(queryThumbPath, i, i2);
                }
                if (bitmap2 != null) {
                    int parseThumbPathForDuration = AppUtils.parseThumbPathForDuration(queryThumbPath);
                    this.mThumbnailCache.put(str, bitmap2);
                    this.mDurationMap.put(str, Integer.valueOf(parseThumbPathForDuration));
                    handler.post(new OnLoadVideoThumbRunnable(bitmap2, parseThumbPathForDuration, onLoadVideoThumbListener));
                    return;
                }
                if (checkIsAvi(str)) {
                    getThumbForAvi(context, str, i, i2, handler, onLoadVideoThumbListener);
                    return;
                } else {
                    getThumbFromMov(context, str, i, i2, handler, onLoadVideoThumbListener);
                    return;
                }
            }
            loadLocalVideoThumb(context, null, i, i2, onLoadVideoThumbListener);
            return;
        }
        Integer num = this.mDurationMap.get(str);
        if (num == null) {
            num = 0;
        }
        handler.post(new OnLoadVideoThumbRunnable(bitmap2, num.intValue(), onLoadVideoThumbListener));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String replaceFilePath(String str, int i) {
        String str2 = "";
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            str2 = str.substring(lastIndexOf);
            str = substring;
        }
        String replace = str.replace(IConstant.DIR_DOWNLOAD, IConstant.DIR_THUMB).replace(IConstant.DIR_RECORD, IConstant.DIR_THUMB);
        File file = new File(replace);
        if (!file.exists()) {
            file.mkdir();
        }
        if (TextUtils.isEmpty(str2)) {
            return replace;
        }
        int lastIndexOf2 = str2.lastIndexOf(".");
        if (lastIndexOf2 != -1) {
            str2 = str2.substring(0, lastIndexOf2);
        }
        return replace + File.separator + str2 + "_" + i + UVCCameraHelper.SUFFIX_JPEG;
    }

    private class OnLoadVideoThumbRunnable implements Runnable {
        private WeakReference<Bitmap> bitmapWeakReference;
        private int duration;
        private WeakReference<OnLoadVideoThumbListener> weakReference;

        OnLoadVideoThumbRunnable(Bitmap bitmap, int i, OnLoadVideoThumbListener onLoadVideoThumbListener) {
            this.duration = i;
            this.bitmapWeakReference = new WeakReference<>(bitmap);
            this.weakReference = new WeakReference<>(onLoadVideoThumbListener);
        }

        @Override // java.lang.Runnable
        public void run() {
            Bitmap bitmap = this.bitmapWeakReference.get();
            OnLoadVideoThumbListener onLoadVideoThumbListener = this.weakReference.get();
            if (bitmap == null || onLoadVideoThumbListener == null) {
                return;
            }
            onLoadVideoThumbListener.onComplete(bitmap, this.duration);
        }
    }

    public void downloadWebImage(Context context, String str, final String str2, final OnDownloadListener onDownloadListener) {
        final Handler handler = new Handler(context.getMainLooper());
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        File file = new File(str2);
        if (file.exists() && file.isFile() && file.delete()) {
            Dbug.w(TAG, "delete file ok!");
        }
        HttpManager.downloadFile(str, new Callback() { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.4
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                Dlog.e(ThumbLoader.TAG, "-downloadWebImage- onErrorResponse = " + iOException.getMessage());
                handler.post(ThumbLoader.this.new OnResultRunnable(false, str2, onDownloadListener));
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body;
                if (response.code() == 200 && (body = response.body()) != null) {
                    byte[] bytes = body.bytes();
                    Dbug.i(ThumbLoader.TAG, "downloadWebImage:saveUrl=" + str2);
                    try {
                        if (AppUtils.bytesToFile(bytes, str2)) {
                            handler.post(ThumbLoader.this.new OnResultRunnable(true, str2, onDownloadListener));
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                response.close();
            }
        });
    }

    private class OnResultRunnable implements Runnable {
        private String obj;
        private boolean result;
        private WeakReference<OnDownloadListener> weakReference;

        OnResultRunnable(boolean z, String str, OnDownloadListener onDownloadListener) {
            this.result = z;
            this.obj = str;
            this.weakReference = new WeakReference<>(onDownloadListener);
        }

        @Override // java.lang.Runnable
        public void run() {
            OnDownloadListener onDownloadListener = this.weakReference.get();
            if (onDownloadListener != null) {
                onDownloadListener.onResult(this.result, this.obj);
            }
        }
    }

    private Bitmap getImageThumbnail(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i3 = options.outHeight;
        int i4 = options.outWidth / i;
        int i5 = i3 / i2;
        if (i4 >= i5) {
            i4 = i5;
        }
        options.inSampleSize = i4 > 0 ? i4 : 1;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(str, options), i, i2, 2);
    }

    public void addBitmap(String str, Bitmap bitmap) {
        LruCache<String, Bitmap> lruCache;
        if (TextUtils.isEmpty(str) || bitmap == null || (lruCache = this.mThumbnailCache) == null) {
            return;
        }
        lruCache.put(str, bitmap);
    }

    public Bitmap getBitmap(String str) {
        LruCache<String, Bitmap> lruCache = this.mThumbnailCache;
        if (lruCache != null) {
            return lruCache.get(str);
        }
        return null;
    }

    public void removeBitmap(String str) {
        Bitmap remove;
        LruCache<String, Bitmap> lruCache = this.mThumbnailCache;
        if (lruCache == null || (remove = lruCache.remove(str)) == null || remove.isRecycled()) {
            return;
        }
        remove.recycle();
    }

    public void clearCache() {
        LruCache<String, Bitmap> lruCache = this.mThumbnailCache;
        if (lruCache != null) {
            lruCache.evictAll();
        }
        Map<String, Integer> map = this.mDurationMap;
        if (map != null) {
            map.clear();
        }
        System.gc();
    }

    public void release() {
        instance = null;
        clearCache();
    }

    private boolean checkIsAvi(String str) {
        return !TextUtils.isEmpty(str) && (str.endsWith(".AVI") || str.endsWith(".avi"));
    }

    private void getThumbForAvi(final Context context, final String str, final int i, final int i2, final Handler handler, final OnLoadVideoThumbListener onLoadVideoThumbListener) {
        AviThumbUtil.getRecordVideoThumb(str, new OnAviThumbListener() { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.5
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:12:0x004d  */
            /* JADX WARN: Removed duplicated region for block: B:23:0x00d2  */
            /* JADX WARN: Removed duplicated region for block: B:37:0x00e5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String] */
            /* JADX WARN: Type inference failed for: r1v17 */
            /* JADX WARN: Type inference failed for: r1v18, types: [java.io.ByteArrayInputStream] */
            /* JADX WARN: Type inference failed for: r1v19 */
            /* JADX WARN: Type inference failed for: r1v2 */
            /* JADX WARN: Type inference failed for: r1v20, types: [java.io.ByteArrayInputStream, java.io.InputStream] */
            /* JADX WARN: Type inference failed for: r1v21 */
            /* JADX WARN: Type inference failed for: r1v22 */
            /* JADX WARN: Type inference failed for: r1v3 */
            /* JADX WARN: Type inference failed for: r1v4 */
            @Override // com.jieli.stream.dv.running2.interfaces.OnAviThumbListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onCompleted(byte[] r8, MediaMeta r9) {
                /*
                    r7 = this;
                    if (r8 == 0) goto Lee
                    if (r9 == 0) goto Lee
                    java.lang.String r0 = com.jieli.stream.dv.running2.util.ThumbLoader.access$100()
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "getThumbForAvi -onCompleted- bytes size="
                    r1.append(r2)
                    int r2 = r8.length
                    r1.append(r2)
                    java.lang.String r2 = "\n mediaMeta : "
                    r1.append(r2)
                    java.lang.String r2 = r9.toString()
                    r1.append(r2)
                    java.lang.String r1 = r1.toString()
                    com.jieli.stream.dv.running2.util.Dbug.w(r0, r1)
                    r0 = 0
                    java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
                    r1.<init>(r8)     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
                    android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r1)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> Le1
                    r1.close()     // Catch: java.io.IOException -> L37
                    goto L4b
                L37:
                    r8 = move-exception
                    r8.printStackTrace()
                    goto L4b
                L3c:
                    r8 = move-exception
                    goto L43
                L3e:
                    r8 = move-exception
                    goto Le3
                L41:
                    r8 = move-exception
                    r1 = r0
                L43:
                    r8.printStackTrace()     // Catch: java.lang.Throwable -> Le1
                    if (r1 == 0) goto L4b
                    r1.close()     // Catch: java.io.IOException -> L37
                L4b:
                    if (r0 == 0) goto Ld2
                    int r8 = r0.getWidth()
                    int r1 = r2
                    if (r8 > r1) goto L5d
                    int r8 = r0.getHeight()
                    int r1 = r3
                    if (r8 <= r1) goto Lee
                L5d:
                    int r8 = r2
                    int r1 = r3
                    r2 = 2
                    android.graphics.Bitmap r8 = android.media.ThumbnailUtils.extractThumbnail(r0, r8, r1, r2)
                    com.jieli.stream.dv.running2.util.ThumbLoader r0 = com.jieli.stream.dv.running2.util.ThumbLoader.this
                    android.util.LruCache r0 = com.jieli.stream.dv.running2.util.ThumbLoader.access$200(r0)
                    java.lang.String r1 = r4
                    r0.put(r1, r8)
                    int r9 = r9.getDuration()
                    java.lang.String r0 = com.jieli.stream.dv.running2.util.ThumbLoader.access$100()
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "-getThumbForAvi- duration = "
                    r1.append(r2)
                    r1.append(r9)
                    java.lang.String r1 = r1.toString()
                    com.jieli.stream.dv.running2.util.Dbug.w(r0, r1)
                    com.jieli.stream.dv.running2.util.ThumbLoader r0 = com.jieli.stream.dv.running2.util.ThumbLoader.this
                    java.util.Map r0 = com.jieli.stream.dv.running2.util.ThumbLoader.access$300(r0)
                    java.lang.String r1 = r4
                    java.lang.Integer r2 = java.lang.Integer.valueOf(r9)
                    r0.put(r1, r2)
                    android.os.Handler r0 = r5
                    com.jieli.stream.dv.running2.util.ThumbLoader$OnLoadVideoThumbRunnable r1 = new com.jieli.stream.dv.running2.util.ThumbLoader$OnLoadVideoThumbRunnable
                    com.jieli.stream.dv.running2.util.ThumbLoader r2 = com.jieli.stream.dv.running2.util.ThumbLoader.this
                    com.jieli.stream.dv.running2.util.ThumbLoader$OnLoadVideoThumbListener r3 = r6
                    r1.<init>(r8, r9, r3)
                    r0.post(r1)
                    if (r9 <= 0) goto Lee
                    com.jieli.stream.dv.running2.util.ThumbLoader r0 = com.jieli.stream.dv.running2.util.ThumbLoader.this
                    java.lang.String r1 = r4
                    java.lang.String r9 = com.jieli.stream.dv.running2.util.ThumbLoader.access$400(r0, r1, r9)
                    java.lang.String r0 = com.jieli.stream.dv.running2.util.ThumbLoader.access$100()
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "-getThumbForAvi- saveThumbPath="
                    r1.append(r2)
                    r1.append(r9)
                    java.lang.String r1 = r1.toString()
                    com.jieli.stream.dv.running2.util.Dbug.i(r0, r1)
                    r0 = 100
                    com.jieli.stream.dv.running2.util.AppUtils.bitmapToFile(r8, r9, r0)
                    goto Lee
                Ld2:
                    com.jieli.stream.dv.running2.util.ThumbLoader r1 = com.jieli.stream.dv.running2.util.ThumbLoader.this
                    android.content.Context r2 = r7
                    r3 = 0
                    int r4 = r2
                    int r5 = r3
                    com.jieli.stream.dv.running2.util.ThumbLoader$OnLoadVideoThumbListener r6 = r6
                    r1.loadLocalVideoThumb(r2, r3, r4, r5, r6)
                    goto Lee
                Le1:
                    r8 = move-exception
                    r0 = r1
                Le3:
                    if (r0 == 0) goto Led
                    r0.close()     // Catch: java.io.IOException -> Le9
                    goto Led
                Le9:
                    r9 = move-exception
                    r9.printStackTrace()
                Led:
                    throw r8
                Lee:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.ThumbLoader.AnonymousClass5.onCompleted(byte[], com.jieli.media.codec.bean.MediaMeta):void");
            }

            @Override // com.jieli.stream.dv.running2.interfaces.OnAviThumbListener
            public void onError(String str2) {
                ThumbLoader.this.loadLocalVideoThumb(context, null, i, i2, onLoadVideoThumbListener);
            }
        });
    }

    private void getThumbFromMov(final Context context, final String str, final int i, final int i2, final Handler handler, final OnLoadVideoThumbListener onLoadVideoThumbListener) {
        FrameCodec frameCodec = new FrameCodec();
        frameCodec.setOnFrameCodecListener(new FrameCodec.OnFrameCodecListener() { // from class: com.jieli.stream.dv.running2.util.ThumbLoader.6
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:12:0x004d  */
            /* JADX WARN: Removed duplicated region for block: B:23:0x00d4  */
            /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String] */
            /* JADX WARN: Type inference failed for: r1v19 */
            /* JADX WARN: Type inference failed for: r1v2 */
            /* JADX WARN: Type inference failed for: r1v20, types: [java.io.ByteArrayInputStream, java.io.InputStream] */
            /* JADX WARN: Type inference failed for: r1v21 */
            /* JADX WARN: Type inference failed for: r1v22 */
            /* JADX WARN: Type inference failed for: r1v3 */
            /* JADX WARN: Type inference failed for: r1v4 */
            /* JADX WARN: Type inference failed for: r1v5, types: [java.io.ByteArrayInputStream] */
            /* JADX WARN: Type inference failed for: r1v6 */
            @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onCompleted(byte[] r8, MediaMeta r9) {
                /*
                    Method dump skipped, instructions count: 241
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.ThumbLoader.AnonymousClass6.onCompleted(byte[], com.jieli.media.codec.bean.MediaMeta):void");
            }

            @Override // com.jieli.media.codec.FrameCodec.OnFrameCodecListener
            public void onError(String str2) {
                ThumbLoader.this.loadLocalVideoThumb(context, null, i, i2, onLoadVideoThumbListener);
            }
        });
        try {
            try {
                frameCodec.convertToJPG(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            frameCodec.destroy();
        }
    }
}
