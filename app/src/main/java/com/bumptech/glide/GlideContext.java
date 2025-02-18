package com.bumptech.glide;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;

/* loaded from: classes.dex */
public class GlideContext extends ContextWrapper implements ComponentCallbacks2 {
    private final ComponentCallbacks2 componentCallbacks;
    private final RequestOptions defaultRequestOptions;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final int logLevel;
    private final Handler mainHandler;
    private final Registry registry;

    public GlideContext(Context context, Registry registry, ImageViewTargetFactory imageViewTargetFactory, RequestOptions requestOptions, Engine engine, ComponentCallbacks2 componentCallbacks2, int i) {
        super(context.getApplicationContext());
        this.registry = registry;
        this.imageViewTargetFactory = imageViewTargetFactory;
        this.defaultRequestOptions = requestOptions;
        this.engine = engine;
        this.componentCallbacks = componentCallbacks2;
        this.logLevel = i;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public RequestOptions getDefaultRequestOptions() {
        return this.defaultRequestOptions;
    }

    public <X> Target<X> buildImageViewTarget(ImageView imageView, Class<X> cls) {
        return this.imageViewTargetFactory.buildTarget(imageView, cls);
    }

    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        this.componentCallbacks.onTrimMemory(i);
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.componentCallbacks.onConfigurationChanged(configuration);
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        this.componentCallbacks.onLowMemory();
    }
}
