package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public final class SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback, FactoryPools.Poolable {
    private static final String GLIDE_TAG = "Glide";
    private static final String TAG = "Request";
    private TransitionFactory<? super R> animationFactory;
    private Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private GlideContext glideContext;
    private int height;
    private Engine.LoadStatus loadStatus;
    private Object model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<R> requestListener;
    private RequestOptions requestOptions;
    private Resource<R> resource;
    private long startTime;
    private Status status;
    private Target<R> target;
    private Class<R> transcodeClass;
    private int width;
    private static final Pools.Pool<SingleRequest<?>> POOL = FactoryPools.simple(FTPReply.FILE_STATUS_OK, new FactoryPools.Factory<SingleRequest<?>>() { // from class: com.bumptech.glide.request.SingleRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
        public SingleRequest<?> create() {
            return new SingleRequest<>();
        }
    });
    private static boolean shouldCallAppCompatResources = true;
    private final String tag = String.valueOf(hashCode());
    private final StateVerifier stateVerifier = StateVerifier.newInstance();

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED
    }

    public static <R> SingleRequest<R> obtain(GlideContext glideContext, Object obj, Class<R> cls, RequestOptions requestOptions, int i, int i2, Priority priority, Target<R> target, RequestListener<R> requestListener, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory) {
        SingleRequest<R> singleRequest = (SingleRequest) POOL.acquire();
        if (singleRequest == null) {
            singleRequest = new SingleRequest<>();
        }
        singleRequest.init(glideContext, obj, cls, requestOptions, i, i2, priority, target, requestListener, requestCoordinator, engine, transitionFactory);
        return singleRequest;
    }

    SingleRequest() {
    }

    private void init(GlideContext glideContext, Object obj, Class<R> cls, RequestOptions requestOptions, int i, int i2, Priority priority, Target<R> target, RequestListener<R> requestListener, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory) {
        this.glideContext = glideContext;
        this.model = obj;
        this.transcodeClass = cls;
        this.requestOptions = requestOptions;
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.priority = priority;
        this.target = target;
        this.requestListener = requestListener;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.animationFactory = transitionFactory;
        this.status = Status.PENDING;
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    @Override // com.bumptech.glide.request.Request
    public void recycle() {
        this.glideContext = null;
        this.model = null;
        this.transcodeClass = null;
        this.requestOptions = null;
        this.overrideWidth = -1;
        this.overrideHeight = -1;
        this.target = null;
        this.requestListener = null;
        this.requestCoordinator = null;
        this.animationFactory = null;
        this.loadStatus = null;
        this.errorDrawable = null;
        this.placeholderDrawable = null;
        this.fallbackDrawable = null;
        this.width = -1;
        this.height = -1;
        POOL.release(this);
    }

    @Override // com.bumptech.glide.request.Request
    public void begin() {
        this.stateVerifier.throwIfRecycled();
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.width = this.overrideWidth;
                this.height = this.overrideHeight;
            }
            onLoadFailed(new GlideException("Received null model"), getFallbackDrawable() == null ? 5 : 3);
            return;
        }
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            onSizeReady(this.overrideWidth, this.overrideHeight);
        } else {
            this.target.getSize(this);
        }
        if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged()) {
            this.target.onLoadStarted(getPlaceholderDrawable());
        }
        if (Log.isLoggable(TAG, 2)) {
            logV("finished run method in " + LogTime.getElapsedMillis(this.startTime));
        }
    }

    void cancel() {
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        this.status = Status.CANCELLED;
        Engine.LoadStatus loadStatus = this.loadStatus;
        if (loadStatus != null) {
            loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    @Override // com.bumptech.glide.request.Request
    public void clear() {
        Util.assertMainThread();
        if (this.status == Status.CLEARED) {
            return;
        }
        cancel();
        Resource<R> resource = this.resource;
        if (resource != null) {
            releaseResource(resource);
        }
        if (canNotifyStatusChanged()) {
            this.target.onLoadCleared(getPlaceholderDrawable());
        }
        this.status = Status.CLEARED;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isPaused() {
        return this.status == Status.PAUSED;
    }

    @Override // com.bumptech.glide.request.Request
    public void pause() {
        clear();
        this.status = Status.PAUSED;
    }

    private void releaseResource(Resource<?> resource) {
        this.engine.release(resource);
        this.resource = null;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isResourceSet() {
        return isComplete();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isFailed() {
        return this.status == Status.FAILED;
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            Drawable errorPlaceholder = this.requestOptions.getErrorPlaceholder();
            this.errorDrawable = errorPlaceholder;
            if (errorPlaceholder == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            Drawable placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            this.placeholderDrawable = placeholderDrawable;
            if (placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            Drawable fallbackDrawable = this.requestOptions.getFallbackDrawable();
            this.fallbackDrawable = fallbackDrawable;
            if (fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    private Drawable loadDrawable(int i) {
        if (shouldCallAppCompatResources) {
            return loadDrawableV7(i);
        }
        return loadDrawableBase(i);
    }

    private Drawable loadDrawableV7(int i) {
        try {
            return AppCompatResources.getDrawable(this.glideContext, i);
        } catch (NoClassDefFoundError unused) {
            shouldCallAppCompatResources = false;
            return loadDrawableBase(i);
        }
    }

    private Drawable loadDrawableBase(int i) {
        return ResourcesCompat.getDrawable(this.glideContext.getResources(), i, this.requestOptions.getTheme());
    }

    private void setErrorPlaceholder() {
        if (canNotifyStatusChanged()) {
            Drawable fallbackDrawable = this.model == null ? getFallbackDrawable() : null;
            if (fallbackDrawable == null) {
                fallbackDrawable = getErrorDrawable();
            }
            if (fallbackDrawable == null) {
                fallbackDrawable = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(fallbackDrawable);
        }
    }

    @Override // com.bumptech.glide.request.target.SizeReadyCallback
    public void onSizeReady(int i, int i2) {
        this.stateVerifier.throwIfRecycled();
        if (Log.isLoggable(TAG, 2)) {
            logV("Got onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
        }
        if (this.status != Status.WAITING_FOR_SIZE) {
            return;
        }
        this.status = Status.RUNNING;
        float sizeMultiplier = this.requestOptions.getSizeMultiplier();
        this.width = maybeApplySizeMultiplier(i, sizeMultiplier);
        this.height = maybeApplySizeMultiplier(i2, sizeMultiplier);
        if (Log.isLoggable(TAG, 2)) {
            logV("finished setup for calling load in " + LogTime.getElapsedMillis(this.startTime));
        }
        this.loadStatus = this.engine.load(this.glideContext, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getOnlyRetrieveFromCache(), this);
        if (Log.isLoggable(TAG, 2)) {
            logV("finished onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
        }
    }

    private static int maybeApplySizeMultiplier(int i, float f) {
        return i == Integer.MIN_VALUE ? i : Math.round(f * i);
    }

    private boolean canSetResource() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        return requestCoordinator == null || requestCoordinator.canSetImage(this);
    }

    private boolean canNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        return requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        return requestCoordinator == null || !requestCoordinator.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        RequestCoordinator requestCoordinator = this.requestCoordinator;
        if (requestCoordinator != null) {
            requestCoordinator.onRequestSuccess(this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.bumptech.glide.request.ResourceCallback
    public void onResourceReady(Resource<?> resource, DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        this.loadStatus = null;
        if (resource == null) {
            onLoadFailed(new GlideException("Expected to receive a Resource<R> with an object of " + this.transcodeClass + " inside, but instead got null."));
            return;
        }
        Object obj = resource.get();
        if (obj == null || !this.transcodeClass.isAssignableFrom(obj.getClass())) {
            releaseResource(resource);
            StringBuilder sb = new StringBuilder();
            sb.append("Expected to receive an object of ");
            sb.append(this.transcodeClass);
            sb.append(" but instead got ");
            sb.append(obj != null ? obj.getClass() : "");
            sb.append("{");
            sb.append(obj);
            sb.append("} inside Resource{");
            sb.append(resource);
            sb.append("}.");
            sb.append(obj == null ? " To indicate failure return a null Resource object, rather than a Resource object containing null data." : "");
            onLoadFailed(new GlideException(sb.toString()));
            return;
        }
        if (!canSetResource()) {
            releaseResource(resource);
            this.status = Status.COMPLETE;
        } else {
            onResourceReady(resource, obj, dataSource);
        }
    }

    private void onResourceReady(Resource<R> resource, R r, DataSource dataSource) {
        boolean isFirstReadyResource = isFirstReadyResource();
        this.status = Status.COMPLETE;
        this.resource = resource;
        if (this.glideContext.getLogLevel() <= 3) {
            Log.d(GLIDE_TAG, "Finished loading " + r.getClass().getSimpleName() + " from " + dataSource + " for " + this.model + " with size [" + this.width + "x" + this.height + "] in " + LogTime.getElapsedMillis(this.startTime) + " ms");
        }
        RequestListener<R> requestListener = this.requestListener;
        if (requestListener == null || !requestListener.onResourceReady(r, this.model, this.target, dataSource, isFirstReadyResource)) {
            this.target.onResourceReady(r, this.animationFactory.build(dataSource, isFirstReadyResource));
        }
        notifyLoadSuccess();
    }

    @Override // com.bumptech.glide.request.ResourceCallback
    public void onLoadFailed(GlideException glideException) {
        onLoadFailed(glideException, 5);
    }

    private void onLoadFailed(GlideException glideException, int i) {
        this.stateVerifier.throwIfRecycled();
        int logLevel = this.glideContext.getLogLevel();
        if (logLevel <= i) {
            Log.w(GLIDE_TAG, "Load failed for " + this.model + " with size [" + this.width + "x" + this.height + "]", glideException);
            if (logLevel <= 4) {
                glideException.logRootCauses(GLIDE_TAG);
            }
        }
        this.loadStatus = null;
        this.status = Status.FAILED;
        RequestListener<R> requestListener = this.requestListener;
        if (requestListener == null || !requestListener.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource())) {
            setErrorPlaceholder();
        }
    }

    private void logV(String str) {
        Log.v(TAG, str + " this: " + this.tag);
    }
}
