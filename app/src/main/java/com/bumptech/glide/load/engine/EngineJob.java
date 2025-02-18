package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new MainThreadCallback());
    private static final int MSG_CANCELLED = 3;
    private static final int MSG_COMPLETE = 1;
    private static final int MSG_EXCEPTION = 2;
    private final List<ResourceCallback> cbs;
    private DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    private EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    private GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private List<ResourceCallback> ignoredCallbacks;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private Key key;
    private final EngineJobListener listener;
    private final Pools.Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier;
    private boolean useUnlimitedSourceGeneratorPool;

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, EngineJobListener engineJobListener, Pools.Pool<EngineJob<?>> pool) {
        this(glideExecutor, glideExecutor2, glideExecutor3, engineJobListener, pool, DEFAULT_FACTORY);
    }

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, EngineJobListener engineJobListener, Pools.Pool<EngineJob<?>> pool, EngineResourceFactory engineResourceFactory) {
        this.cbs = new ArrayList(2);
        this.stateVerifier = StateVerifier.newInstance();
        this.diskCacheExecutor = glideExecutor;
        this.sourceExecutor = glideExecutor2;
        this.sourceUnlimitedExecutor = glideExecutor3;
        this.listener = engineJobListener;
        this.pool = pool;
        this.engineResourceFactory = engineResourceFactory;
    }

    EngineJob<R> init(Key key, boolean z, boolean z2) {
        this.key = key;
        this.isCacheable = z;
        this.useUnlimitedSourceGeneratorPool = z2;
        return this;
    }

    public void start(DecodeJob<R> decodeJob) {
        this.decodeJob = decodeJob;
        (decodeJob.willDecodeFromCache() ? this.diskCacheExecutor : getActiveSourceExecutor()).execute(decodeJob);
    }

    public void addCallback(ResourceCallback resourceCallback) {
        Util.assertMainThread();
        this.stateVerifier.throwIfRecycled();
        if (this.hasResource) {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource);
        } else if (this.hasLoadFailed) {
            resourceCallback.onLoadFailed(this.exception);
        } else {
            this.cbs.add(resourceCallback);
        }
    }

    public void removeCallback(ResourceCallback resourceCallback) {
        Util.assertMainThread();
        this.stateVerifier.throwIfRecycled();
        if (this.hasResource || this.hasLoadFailed) {
            addIgnoredCallback(resourceCallback);
            return;
        }
        this.cbs.remove(resourceCallback);
        if (this.cbs.isEmpty()) {
            cancel();
        }
    }

    private GlideExecutor getActiveSourceExecutor() {
        return this.useUnlimitedSourceGeneratorPool ? this.sourceUnlimitedExecutor : this.sourceExecutor;
    }

    private void addIgnoredCallback(ResourceCallback resourceCallback) {
        if (this.ignoredCallbacks == null) {
            this.ignoredCallbacks = new ArrayList(2);
        }
        if (this.ignoredCallbacks.contains(resourceCallback)) {
            return;
        }
        this.ignoredCallbacks.add(resourceCallback);
    }

    private boolean isInIgnoredCallbacks(ResourceCallback resourceCallback) {
        List<ResourceCallback> list = this.ignoredCallbacks;
        return list != null && list.contains(resourceCallback);
    }

    void cancel() {
        if (this.hasLoadFailed || this.hasResource || this.isCancelled) {
            return;
        }
        this.isCancelled = true;
        this.decodeJob.cancel();
        this.listener.onEngineJobCancelled(this, this.key);
    }

    boolean isCancelled() {
        return this.isCancelled;
    }

    void handleResultOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCancelled) {
            this.resource.recycle();
            release(false);
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received a resource without any callbacks to notify");
        }
        if (this.hasResource) {
            throw new IllegalStateException("Already have resource");
        }
        EngineResource<?> build = this.engineResourceFactory.build(this.resource, this.isCacheable);
        this.engineResource = build;
        this.hasResource = true;
        build.acquire();
        this.listener.onEngineJobComplete(this.key, this.engineResource);
        for (ResourceCallback resourceCallback : this.cbs) {
            if (!isInIgnoredCallbacks(resourceCallback)) {
                this.engineResource.acquire();
                resourceCallback.onResourceReady(this.engineResource, this.dataSource);
            }
        }
        this.engineResource.release();
        release(false);
    }

    void handleCancelledOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (!this.isCancelled) {
            throw new IllegalStateException("Not cancelled");
        }
        this.listener.onEngineJobCancelled(this, this.key);
        release(false);
    }

    private void release(boolean z) {
        Util.assertMainThread();
        this.cbs.clear();
        this.key = null;
        this.engineResource = null;
        this.resource = null;
        List<ResourceCallback> list = this.ignoredCallbacks;
        if (list != null) {
            list.clear();
        }
        this.hasLoadFailed = false;
        this.isCancelled = false;
        this.hasResource = false;
        this.decodeJob.release(z);
        this.decodeJob = null;
        this.exception = null;
        this.dataSource = null;
        this.pool.release(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.bumptech.glide.load.engine.DecodeJob.Callback
    public void onResourceReady(Resource<R> resource, DataSource dataSource) {
        this.resource = resource;
        this.dataSource = dataSource;
        MAIN_THREAD_HANDLER.obtainMessage(1, this).sendToTarget();
    }

    @Override // com.bumptech.glide.load.engine.DecodeJob.Callback
    public void onLoadFailed(GlideException glideException) {
        this.exception = glideException;
        MAIN_THREAD_HANDLER.obtainMessage(2, this).sendToTarget();
    }

    @Override // com.bumptech.glide.load.engine.DecodeJob.Callback
    public void reschedule(DecodeJob<?> decodeJob) {
        getActiveSourceExecutor().execute(decodeJob);
    }

    void handleExceptionOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCancelled) {
            release(false);
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received an exception without any callbacks to notify");
        }
        if (this.hasLoadFailed) {
            throw new IllegalStateException("Already failed once");
        }
        this.hasLoadFailed = true;
        this.listener.onEngineJobComplete(this.key, null);
        for (ResourceCallback resourceCallback : this.cbs) {
            if (!isInIgnoredCallbacks(resourceCallback)) {
                resourceCallback.onLoadFailed(this.exception);
            }
        }
        release(false);
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean z) {
            return new EngineResource<>(resource, z);
        }
    }

    private static class MainThreadCallback implements Handler.Callback {
        MainThreadCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            EngineJob engineJob = (EngineJob) message.obj;
            int i = message.what;
            if (i == 1) {
                engineJob.handleResultOnMainThread();
            } else if (i == 2) {
                engineJob.handleExceptionOnMainThread();
            } else if (i == 3) {
                engineJob.handleCancelledOnMainThread();
            } else {
                throw new IllegalStateException("Unrecognized message: " + message.what);
            }
            return true;
        }
    }
}
