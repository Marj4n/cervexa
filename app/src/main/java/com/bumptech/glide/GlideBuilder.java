package com.bumptech.glide;

import android.content.Context;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes.dex */
public final class GlideBuilder {
    private ArrayPool arrayPool;
    private BitmapPool bitmapPool;
    private ConnectivityMonitorFactory connectivityMonitorFactory;
    private GlideExecutor diskCacheExecutor;
    private DiskCache.Factory diskCacheFactory;
    private Engine engine;
    private MemoryCache memoryCache;
    private MemorySizeCalculator memorySizeCalculator;
    private RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
    private GlideExecutor sourceExecutor;
    private int logLevel = 4;
    private RequestOptions defaultRequestOptions = new RequestOptions();

    public GlideBuilder setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
        return this;
    }

    public GlideBuilder setArrayPool(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
        return this;
    }

    public GlideBuilder setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
        return this;
    }

    @Deprecated
    public GlideBuilder setDiskCache(final DiskCache diskCache) {
        return setDiskCache(new DiskCache.Factory() { // from class: com.bumptech.glide.GlideBuilder.1
            @Override // com.bumptech.glide.load.engine.cache.DiskCache.Factory
            public DiskCache build() {
                return diskCache;
            }
        });
    }

    public GlideBuilder setDiskCache(DiskCache.Factory factory) {
        this.diskCacheFactory = factory;
        return this;
    }

    public GlideBuilder setResizeExecutor(GlideExecutor glideExecutor) {
        this.sourceExecutor = glideExecutor;
        return this;
    }

    public GlideBuilder setDiskCacheExecutor(GlideExecutor glideExecutor) {
        this.diskCacheExecutor = glideExecutor;
        return this;
    }

    public GlideBuilder setDefaultRequestOptions(RequestOptions requestOptions) {
        this.defaultRequestOptions = requestOptions;
        return this;
    }

    @Deprecated
    public GlideBuilder setDecodeFormat(DecodeFormat decodeFormat) {
        this.defaultRequestOptions.apply(new RequestOptions().format(decodeFormat));
        return this;
    }

    public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator.Builder builder) {
        return setMemorySizeCalculator(builder.build());
    }

    public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator memorySizeCalculator) {
        this.memorySizeCalculator = memorySizeCalculator;
        return this;
    }

    public GlideBuilder setConnectivityMonitorFactory(ConnectivityMonitorFactory connectivityMonitorFactory) {
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        return this;
    }

    public GlideBuilder setLogLevel(int i) {
        if (i < 2 || i > 6) {
            throw new IllegalArgumentException("Log level must be one of Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, or Log.ERROR");
        }
        this.logLevel = i;
        return this;
    }

    GlideBuilder setRequestManagerFactory(RequestManagerRetriever.RequestManagerFactory requestManagerFactory) {
        this.requestManagerFactory = requestManagerFactory;
        return this;
    }

    GlideBuilder setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    public Glide build(Context context) {
        if (this.sourceExecutor == null) {
            this.sourceExecutor = GlideExecutor.newSourceExecutor();
        }
        if (this.diskCacheExecutor == null) {
            this.diskCacheExecutor = GlideExecutor.newDiskCacheExecutor();
        }
        if (this.memorySizeCalculator == null) {
            this.memorySizeCalculator = new MemorySizeCalculator.Builder(context).build();
        }
        if (this.connectivityMonitorFactory == null) {
            this.connectivityMonitorFactory = new DefaultConnectivityMonitorFactory();
        }
        if (this.bitmapPool == null) {
            this.bitmapPool = new LruBitmapPool(this.memorySizeCalculator.getBitmapPoolSize());
        }
        if (this.arrayPool == null) {
            this.arrayPool = new LruArrayPool(this.memorySizeCalculator.getArrayPoolSizeInBytes());
        }
        if (this.memoryCache == null) {
            this.memoryCache = new LruResourceCache(this.memorySizeCalculator.getMemoryCacheSize());
        }
        if (this.diskCacheFactory == null) {
            this.diskCacheFactory = new InternalCacheDiskCacheFactory(context);
        }
        if (this.engine == null) {
            this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheExecutor, this.sourceExecutor, GlideExecutor.newUnlimitedSourceExecutor());
        }
        return new Glide(context, this.engine, this.memoryCache, this.bitmapPool, this.arrayPool, new RequestManagerRetriever(this.requestManagerFactory), this.connectivityMonitorFactory, this.logLevel, this.defaultRequestOptions.lock());
    }
}
