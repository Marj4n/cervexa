package com.bumptech.glide;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class Glide implements ComponentCallbacks2 {
    private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
    private static final String TAG = "Glide";
    private static volatile Glide glide;
    private final ArrayPool arrayPool;
    private final BitmapPool bitmapPool;
    private final BitmapPreFiller bitmapPreFiller;
    private final ConnectivityMonitorFactory connectivityMonitorFactory;
    private final Engine engine;
    private final GlideContext glideContext;
    private final MemoryCache memoryCache;
    private final Registry registry;
    private final RequestManagerRetriever requestManagerRetriever;
    private final List<RequestManager> managers = new ArrayList();
    private MemoryCategory memoryCategory = MemoryCategory.NORMAL;

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
    }

    public static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, "image_manager_disk_cache");
    }

    public static File getPhotoCacheDir(Context context, String str) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            File file = new File(cacheDir, str);
            if (file.mkdirs() || (file.exists() && file.isDirectory())) {
                return file;
            }
            return null;
        }
        if (Log.isLoggable(TAG, 6)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    public static Glide get(Context context) {
        if (glide == null) {
            synchronized (Glide.class) {
                if (glide == null) {
                    initGlide(context);
                }
            }
        }
        return glide;
    }

    public static void init(Glide glide2) {
        glide = glide2;
    }

    public static void tearDown() {
        glide = null;
    }

    private static void initGlide(Context context) {
        Context applicationContext = context.getApplicationContext();
        GeneratedAppGlideModule annotationGeneratedGlideModules = getAnnotationGeneratedGlideModules();
        List<GlideModule> emptyList = Collections.emptyList();
        if (annotationGeneratedGlideModules == null || annotationGeneratedGlideModules.isManifestParsingEnabled()) {
            emptyList = new ManifestParser(applicationContext).parse();
        }
        if (annotationGeneratedGlideModules != null && !annotationGeneratedGlideModules.getExcludedModuleClasses().isEmpty()) {
            Set<Class<?>> excludedModuleClasses = annotationGeneratedGlideModules.getExcludedModuleClasses();
            Iterator<GlideModule> it = emptyList.iterator();
            while (it.hasNext()) {
                GlideModule next = it.next();
                if (excludedModuleClasses.contains(next.getClass())) {
                    if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "AppGlideModule excludes manifest GlideModule: " + next);
                    }
                    it.remove();
                }
            }
        }
        if (Log.isLoggable(TAG, 3)) {
            Iterator<GlideModule> it2 = emptyList.iterator();
            while (it2.hasNext()) {
                Log.d(TAG, "Discovered GlideModule from manifest: " + it2.next().getClass());
            }
        }
        GlideBuilder requestManagerFactory = new GlideBuilder().setRequestManagerFactory(annotationGeneratedGlideModules != null ? annotationGeneratedGlideModules.getRequestManagerFactory() : null);
        Iterator<GlideModule> it3 = emptyList.iterator();
        while (it3.hasNext()) {
            it3.next().applyOptions(applicationContext, requestManagerFactory);
        }
        if (annotationGeneratedGlideModules != null) {
            annotationGeneratedGlideModules.applyOptions(applicationContext, requestManagerFactory);
        }
        glide = requestManagerFactory.build(applicationContext);
        Iterator<GlideModule> it4 = emptyList.iterator();
        while (it4.hasNext()) {
            it4.next().registerComponents(applicationContext, glide.registry);
        }
        if (annotationGeneratedGlideModules != null) {
            annotationGeneratedGlideModules.registerComponents(applicationContext, glide.registry);
        }
    }

    private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules() {
        try {
            return (GeneratedAppGlideModule) Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").newInstance();
        } catch (ClassNotFoundException unused) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", e);
        } catch (InstantiationException e2) {
            throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", e2);
        }
    }

    Glide(Context context, Engine engine, MemoryCache memoryCache, BitmapPool bitmapPool, ArrayPool arrayPool, RequestManagerRetriever requestManagerRetriever, ConnectivityMonitorFactory connectivityMonitorFactory, int i, RequestOptions requestOptions) {
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
        this.memoryCache = memoryCache;
        this.requestManagerRetriever = requestManagerRetriever;
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        this.bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, (DecodeFormat) requestOptions.getOptions().get(Downsampler.DECODE_FORMAT));
        Resources resources = context.getResources();
        Registry registry = new Registry();
        this.registry = registry;
        registry.register(new DefaultImageHeaderParser());
        Downsampler downsampler = new Downsampler(this.registry.getImageHeaderParsers(), resources.getDisplayMetrics(), bitmapPool, arrayPool);
        ByteBufferGifDecoder byteBufferGifDecoder = new ByteBufferGifDecoder(context, this.registry.getImageHeaderParsers(), bitmapPool, arrayPool);
        this.registry.register(ByteBuffer.class, new ByteBufferEncoder()).register(InputStream.class, new StreamEncoder(arrayPool)).append(ByteBuffer.class, Bitmap.class, new ByteBufferBitmapDecoder(downsampler)).append(InputStream.class, Bitmap.class, new StreamBitmapDecoder(downsampler, arrayPool)).append(ParcelFileDescriptor.class, Bitmap.class, new VideoBitmapDecoder(bitmapPool)).register(Bitmap.class, (ResourceEncoder) new BitmapEncoder()).append(ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, bitmapPool, new ByteBufferBitmapDecoder(downsampler))).append(InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, bitmapPool, new StreamBitmapDecoder(downsampler, arrayPool))).append(ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, bitmapPool, new VideoBitmapDecoder(bitmapPool))).register(BitmapDrawable.class, (ResourceEncoder) new BitmapDrawableEncoder(bitmapPool, new BitmapEncoder())).prepend(InputStream.class, GifDrawable.class, new StreamGifDecoder(this.registry.getImageHeaderParsers(), byteBufferGifDecoder, arrayPool)).prepend(ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder).register(GifDrawable.class, (ResourceEncoder) new GifDrawableEncoder()).append(GifDecoder.class, GifDecoder.class, new UnitModelLoader.Factory()).append(GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool)).register(new ByteBufferRewinder.Factory()).append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory()).append(File.class, InputStream.class, new FileLoader.StreamFactory()).append(File.class, File.class, new FileDecoder()).append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory()).append(File.class, File.class, new UnitModelLoader.Factory()).register(new InputStreamRewinder.Factory(arrayPool)).append(Integer.TYPE, InputStream.class, new ResourceLoader.StreamFactory(resources)).append(Integer.TYPE, ParcelFileDescriptor.class, new ResourceLoader.FileDescriptorFactory(resources)).append(Integer.class, InputStream.class, new ResourceLoader.StreamFactory(resources)).append(Integer.class, ParcelFileDescriptor.class, new ResourceLoader.FileDescriptorFactory(resources)).append(String.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(String.class, InputStream.class, new StringLoader.StreamFactory()).append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory()).append(Uri.class, InputStream.class, new HttpUriLoader.Factory()).append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets())).append(Uri.class, ParcelFileDescriptor.class, new AssetUriLoader.FileDescriptorFactory(context.getAssets())).append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new UriLoader.StreamFactory(context.getContentResolver())).append(Uri.class, ParcelFileDescriptor.class, new UriLoader.FileDescriptorFactory(context.getContentResolver())).append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory()).append(URL.class, InputStream.class, new UrlLoader.StreamFactory()).append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context)).append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory()).append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory()).append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory()).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources, bitmapPool)).register(Bitmap.class, byte[].class, new BitmapBytesTranscoder()).register(GifDrawable.class, byte[].class, new GifDrawableBytesTranscoder());
        this.glideContext = new GlideContext(context, this.registry, new ImageViewTargetFactory(), requestOptions, engine, this, i);
    }

    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }

    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public Context getContext() {
        return this.glideContext.getBaseContext();
    }

    ConnectivityMonitorFactory getConnectivityMonitorFactory() {
        return this.connectivityMonitorFactory;
    }

    GlideContext getGlideContext() {
        return this.glideContext;
    }

    public void preFillBitmapPool(PreFillType.Builder... builderArr) {
        this.bitmapPreFiller.preFill(builderArr);
    }

    public void clearMemory() {
        Util.assertMainThread();
        this.memoryCache.clearMemory();
        this.bitmapPool.clearMemory();
        this.arrayPool.clearMemory();
    }

    public void trimMemory(int i) {
        Util.assertMainThread();
        this.memoryCache.trimMemory(i);
        this.bitmapPool.trimMemory(i);
        this.arrayPool.trimMemory(i);
    }

    public void clearDiskCache() {
        Util.assertBackgroundThread();
        this.engine.clearDiskCache();
    }

    public RequestManagerRetriever getRequestManagerRetriever() {
        return this.requestManagerRetriever;
    }

    public MemoryCategory setMemoryCategory(MemoryCategory memoryCategory) {
        Util.assertMainThread();
        this.memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
        this.bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
        MemoryCategory memoryCategory2 = this.memoryCategory;
        this.memoryCategory = memoryCategory;
        return memoryCategory2;
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        Preconditions.checkNotNull(context, "You cannot start a load on a not yet attached View or a  Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return get(context).getRequestManagerRetriever();
    }

    public static RequestManager with(Context context) {
        return getRetriever(context).get(context);
    }

    public static RequestManager with(Activity activity) {
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(FragmentActivity fragmentActivity) {
        return getRetriever(fragmentActivity).get(fragmentActivity);
    }

    public static RequestManager with(Fragment fragment) {
        return getRetriever(fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(androidx.fragment.app.Fragment fragment) {
        return getRetriever(fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(View view) {
        return getRetriever(view.getContext()).get(view);
    }

    public Registry getRegistry() {
        return this.registry;
    }

    void removeFromManagers(Target<?> target) {
        synchronized (this.managers) {
            Iterator<RequestManager> it = this.managers.iterator();
            while (it.hasNext()) {
                if (it.next().untrack(target)) {
                    return;
                }
            }
            throw new IllegalStateException("Failed to remove target from managers");
        }
    }

    void registerRequestManager(RequestManager requestManager) {
        synchronized (this.managers) {
            if (this.managers.contains(requestManager)) {
                throw new IllegalStateException("Cannot register already registered manager");
            }
            this.managers.add(requestManager);
        }
    }

    void unregisterRequestManager(RequestManager requestManager) {
        synchronized (this.managers) {
            if (!this.managers.contains(requestManager)) {
                throw new IllegalStateException("Cannot register not yet registered manager");
            }
            this.managers.remove(requestManager);
        }
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        trimMemory(i);
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        clearMemory();
    }
}
