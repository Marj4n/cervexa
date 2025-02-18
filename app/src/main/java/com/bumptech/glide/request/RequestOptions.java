package com.bumptech.glide.request;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class RequestOptions implements Cloneable {
    private static final int DISK_CACHE_STRATEGY = 4;
    private static final int ERROR_ID = 32;
    private static final int ERROR_PLACEHOLDER = 16;
    private static final int FALLBACK = 8192;
    private static final int FALLBACK_ID = 16384;
    private static final int IS_CACHEABLE = 256;
    private static final int ONLY_RETRIEVE_FROM_CACHE = 524288;
    private static final int OVERRIDE = 512;
    private static final int PLACEHOLDER = 64;
    private static final int PLACEHOLDER_ID = 128;
    private static final int PRIORITY = 8;
    private static final int RESOURCE_CLASS = 4096;
    private static final int SIGNATURE = 1024;
    private static final int SIZE_MULTIPLIER = 2;
    private static final int THEME = 32768;
    private static final int TRANSFORMATION = 2048;
    private static final int TRANSFORMATION_ALLOWED = 65536;
    private static final int TRANSFORMATION_REQUIRED = 131072;
    private static final int UNSET = -1;
    private static final int USE_UNLIMITED_SOURCE_GENERATORS_POOL = 262144;
    private static RequestOptions centerCropOptions;
    private static RequestOptions centerInsideOptions;
    private static RequestOptions circleCropOptions;
    private static RequestOptions fitCenterOptions;
    private static RequestOptions noAnimationOptions;
    private static RequestOptions noTransformOptions;
    private static RequestOptions skipMemoryCacheFalseOptions;
    private static RequestOptions skipMemoryCacheTrueOptions;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackId;
    private int fields;
    private boolean isAutoCloneEnabled;
    private boolean isLocked;
    private boolean isTransformationRequired;
    private boolean onlyRetrieveFromCache;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Resources.Theme theme;
    private boolean useUnlimitedSourceGeneratorsPool;
    private float sizeMultiplier = 1.0f;
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
    private Priority priority = Priority.NORMAL;
    private boolean isCacheable = true;
    private int overrideHeight = -1;
    private int overrideWidth = -1;
    private Key signature = EmptySignature.obtain();
    private boolean isTransformationAllowed = true;
    private Options options = new Options();
    private Map<Class<?>, Transformation<?>> transformations = new HashMap();
    private Class<?> resourceClass = Object.class;

    private static boolean isSet(int i, int i2) {
        return (i & i2) != 0;
    }

    public static RequestOptions sizeMultiplierOf(float f) {
        return new RequestOptions().sizeMultiplier(f);
    }

    public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy diskCacheStrategy) {
        return new RequestOptions().diskCacheStrategy(diskCacheStrategy);
    }

    public static RequestOptions priorityOf(Priority priority) {
        return new RequestOptions().priority(priority);
    }

    public static RequestOptions placeholderOf(Drawable drawable) {
        return new RequestOptions().placeholder(drawable);
    }

    public static RequestOptions placeholderOf(int i) {
        return new RequestOptions().placeholder(i);
    }

    public static RequestOptions errorOf(Drawable drawable) {
        return new RequestOptions().error(drawable);
    }

    public static RequestOptions errorOf(int i) {
        return new RequestOptions().error(i);
    }

    public static RequestOptions skipMemoryCacheOf(boolean z) {
        if (z) {
            if (skipMemoryCacheTrueOptions == null) {
                skipMemoryCacheTrueOptions = new RequestOptions().skipMemoryCache(true).autoClone();
            }
            return skipMemoryCacheTrueOptions;
        }
        if (skipMemoryCacheFalseOptions == null) {
            skipMemoryCacheFalseOptions = new RequestOptions().skipMemoryCache(false).autoClone();
        }
        return skipMemoryCacheFalseOptions;
    }

    public static RequestOptions overrideOf(int i, int i2) {
        return new RequestOptions().override(i, i2);
    }

    public static RequestOptions overrideOf(int i) {
        return overrideOf(i, i);
    }

    public static RequestOptions signatureOf(Key key) {
        return new RequestOptions().signature(key);
    }

    public static RequestOptions fitCenterTransform() {
        if (fitCenterOptions == null) {
            fitCenterOptions = new RequestOptions().fitCenter().autoClone();
        }
        return fitCenterOptions;
    }

    public static RequestOptions centerInsideTransform() {
        if (centerInsideOptions == null) {
            centerInsideOptions = new RequestOptions().centerInside().autoClone();
        }
        return centerInsideOptions;
    }

    public static RequestOptions centerCropTransform() {
        if (centerCropOptions == null) {
            centerCropOptions = new RequestOptions().centerCrop().autoClone();
        }
        return centerCropOptions;
    }

    public static RequestOptions circleCropTransform() {
        if (circleCropOptions == null) {
            circleCropOptions = new RequestOptions().circleCrop().autoClone();
        }
        return circleCropOptions;
    }

    public static RequestOptions bitmapTransform(Transformation<Bitmap> transformation) {
        return new RequestOptions().transform(transformation);
    }

    @Deprecated
    public static RequestOptions noTransform() {
        return noTransformation();
    }

    public static RequestOptions noTransformation() {
        if (noTransformOptions == null) {
            noTransformOptions = new RequestOptions().dontTransform().autoClone();
        }
        return noTransformOptions;
    }

    public static <T> RequestOptions option(Option<T> option, T t) {
        return new RequestOptions().set(option, t);
    }

    public static RequestOptions decodeTypeOf(Class<?> cls) {
        return new RequestOptions().decode(cls);
    }

    public static RequestOptions formatOf(DecodeFormat decodeFormat) {
        return new RequestOptions().format(decodeFormat);
    }

    public static RequestOptions frameOf(long j) {
        return new RequestOptions().frame(j);
    }

    public static RequestOptions downsampleOf(DownsampleStrategy downsampleStrategy) {
        return new RequestOptions().downsample(downsampleStrategy);
    }

    public static RequestOptions encodeQualityOf(int i) {
        return new RequestOptions().encodeQuality(i);
    }

    public static RequestOptions encodeFormatOf(Bitmap.CompressFormat compressFormat) {
        return new RequestOptions().encodeFormat(compressFormat);
    }

    public static RequestOptions noAnimation() {
        if (noAnimationOptions == null) {
            noAnimationOptions = new RequestOptions().dontAnimate().autoClone();
        }
        return noAnimationOptions;
    }

    public RequestOptions sizeMultiplier(float f) {
        if (this.isAutoCloneEnabled) {
            return m9clone().sizeMultiplier(f);
        }
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.sizeMultiplier = f;
        this.fields |= 2;
        return selfOrThrowIfLocked();
    }

    public RequestOptions useUnlimitedSourceGeneratorsPool(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m9clone().useUnlimitedSourceGeneratorsPool(z);
        }
        this.useUnlimitedSourceGeneratorsPool = z;
        this.fields |= 262144;
        return selfOrThrowIfLocked();
    }

    public RequestOptions onlyRetrieveFromCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m9clone().onlyRetrieveFromCache(z);
        }
        this.onlyRetrieveFromCache = z;
        this.fields |= 524288;
        return selfOrThrowIfLocked();
    }

    public RequestOptions diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        if (this.isAutoCloneEnabled) {
            return m9clone().diskCacheStrategy(diskCacheStrategy);
        }
        this.diskCacheStrategy = (DiskCacheStrategy) Preconditions.checkNotNull(diskCacheStrategy);
        this.fields |= 4;
        return selfOrThrowIfLocked();
    }

    public RequestOptions priority(Priority priority) {
        if (this.isAutoCloneEnabled) {
            return m9clone().priority(priority);
        }
        this.priority = (Priority) Preconditions.checkNotNull(priority);
        this.fields |= 8;
        return selfOrThrowIfLocked();
    }

    public RequestOptions placeholder(Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m9clone().placeholder(drawable);
        }
        this.placeholderDrawable = drawable;
        this.fields |= 64;
        return selfOrThrowIfLocked();
    }

    public RequestOptions placeholder(int i) {
        if (this.isAutoCloneEnabled) {
            return m9clone().placeholder(i);
        }
        this.placeholderId = i;
        this.fields |= 128;
        return selfOrThrowIfLocked();
    }

    public RequestOptions fallback(Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m9clone().fallback(drawable);
        }
        this.fallbackDrawable = drawable;
        this.fields |= 8192;
        return selfOrThrowIfLocked();
    }

    public RequestOptions fallback(int i) {
        if (this.isAutoCloneEnabled) {
            return m9clone().fallback(i);
        }
        this.fallbackId = i;
        this.fields |= 16384;
        return selfOrThrowIfLocked();
    }

    public RequestOptions error(Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m9clone().error(drawable);
        }
        this.errorPlaceholder = drawable;
        this.fields |= 16;
        return selfOrThrowIfLocked();
    }

    public RequestOptions error(int i) {
        if (this.isAutoCloneEnabled) {
            return m9clone().error(i);
        }
        this.errorId = i;
        this.fields |= 32;
        return selfOrThrowIfLocked();
    }

    public RequestOptions theme(Resources.Theme theme) {
        if (this.isAutoCloneEnabled) {
            return m9clone().theme(theme);
        }
        this.theme = theme;
        this.fields |= 32768;
        return selfOrThrowIfLocked();
    }

    public RequestOptions skipMemoryCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m9clone().skipMemoryCache(true);
        }
        this.isCacheable = !z;
        this.fields |= 256;
        return selfOrThrowIfLocked();
    }

    public RequestOptions override(int i, int i2) {
        if (this.isAutoCloneEnabled) {
            return m9clone().override(i, i2);
        }
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.fields |= 512;
        return selfOrThrowIfLocked();
    }

    public RequestOptions override(int i) {
        return override(i, i);
    }

    public RequestOptions signature(Key key) {
        if (this.isAutoCloneEnabled) {
            return m9clone().signature(key);
        }
        this.signature = (Key) Preconditions.checkNotNull(key);
        this.fields |= 1024;
        return selfOrThrowIfLocked();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public RequestOptions m9clone() {
        try {
            RequestOptions requestOptions = (RequestOptions) super.clone();
            Options options = new Options();
            requestOptions.options = options;
            options.putAll(this.options);
            HashMap hashMap = new HashMap();
            requestOptions.transformations = hashMap;
            hashMap.putAll(this.transformations);
            requestOptions.isLocked = false;
            requestOptions.isAutoCloneEnabled = false;
            return requestOptions;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> RequestOptions set(Option<T> option, T t) {
        if (this.isAutoCloneEnabled) {
            return m9clone().set(option, t);
        }
        Preconditions.checkNotNull(option);
        Preconditions.checkNotNull(t);
        this.options.set(option, t);
        return selfOrThrowIfLocked();
    }

    public RequestOptions decode(Class<?> cls) {
        if (this.isAutoCloneEnabled) {
            return m9clone().decode(cls);
        }
        this.resourceClass = (Class) Preconditions.checkNotNull(cls);
        this.fields |= 4096;
        return selfOrThrowIfLocked();
    }

    public final boolean isTransformationAllowed() {
        return this.isTransformationAllowed;
    }

    public final boolean isTransformationSet() {
        return isSet(2048);
    }

    public final boolean isLocked() {
        return this.isLocked;
    }

    public RequestOptions encodeFormat(Bitmap.CompressFormat compressFormat) {
        return set(BitmapEncoder.COMPRESSION_FORMAT, Preconditions.checkNotNull(compressFormat));
    }

    public RequestOptions encodeQuality(int i) {
        return set(BitmapEncoder.COMPRESSION_QUALITY, Integer.valueOf(i));
    }

    public RequestOptions format(DecodeFormat decodeFormat) {
        return set(Downsampler.DECODE_FORMAT, Preconditions.checkNotNull(decodeFormat));
    }

    public RequestOptions frame(long j) {
        return set(VideoBitmapDecoder.TARGET_FRAME, Long.valueOf(j));
    }

    public RequestOptions downsample(DownsampleStrategy downsampleStrategy) {
        return set(Downsampler.DOWNSAMPLE_STRATEGY, Preconditions.checkNotNull(downsampleStrategy));
    }

    public RequestOptions optionalCenterCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    @Deprecated
    public RequestOptions centerCrop(Context context) {
        return centerCrop();
    }

    public RequestOptions centerCrop() {
        return transform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    public RequestOptions optionalFitCenter() {
        return optionalTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    public RequestOptions fitCenter() {
        return transform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    @Deprecated
    public RequestOptions optionalCenterInside(Context context) {
        return optionalCenterInside();
    }

    public RequestOptions optionalCenterInside() {
        return optionalTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public RequestOptions centerInside() {
        return transform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public RequestOptions optionalCircleCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CircleCrop());
    }

    @Deprecated
    public RequestOptions circleCrop(Context context) {
        return circleCrop();
    }

    public RequestOptions circleCrop() {
        return transform(DownsampleStrategy.CENTER_INSIDE, new CircleCrop());
    }

    final RequestOptions optionalTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().optionalTransform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return optionalTransform(transformation);
    }

    final RequestOptions transform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().transform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return transform(transformation);
    }

    public RequestOptions transform(Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().transform(transformation);
        }
        optionalTransform(transformation);
        this.isTransformationRequired = true;
        this.fields |= 131072;
        return selfOrThrowIfLocked();
    }

    @Deprecated
    public RequestOptions optionalTransform(Context context, Transformation<Bitmap> transformation) {
        return optionalTransform(transformation);
    }

    public RequestOptions optionalTransform(Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().optionalTransform(transformation);
        }
        optionalTransform(Bitmap.class, transformation);
        optionalTransform(BitmapDrawable.class, new BitmapDrawableTransformation(transformation));
        optionalTransform(GifDrawable.class, new GifDrawableTransformation(transformation));
        return selfOrThrowIfLocked();
    }

    public <T> RequestOptions optionalTransform(Class<T> cls, Transformation<T> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().optionalTransform(cls, transformation);
        }
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(transformation);
        this.transformations.put(cls, transformation);
        int i = this.fields | 2048;
        this.fields = i;
        this.isTransformationAllowed = true;
        this.fields = i | 65536;
        return selfOrThrowIfLocked();
    }

    public <T> RequestOptions transform(Class<T> cls, Transformation<T> transformation) {
        if (this.isAutoCloneEnabled) {
            return m9clone().transform(cls, transformation);
        }
        optionalTransform(cls, transformation);
        this.isTransformationRequired = true;
        this.fields |= 131072;
        return selfOrThrowIfLocked();
    }

    public RequestOptions dontTransform() {
        if (this.isAutoCloneEnabled) {
            return m9clone().dontTransform();
        }
        this.transformations.clear();
        int i = this.fields & (-2049);
        this.fields = i;
        this.isTransformationRequired = false;
        int i2 = i & (-131073);
        this.fields = i2;
        this.isTransformationAllowed = false;
        this.fields = i2 | 65536;
        return selfOrThrowIfLocked();
    }

    public RequestOptions dontAnimate() {
        if (this.isAutoCloneEnabled) {
            return m9clone().dontAnimate();
        }
        set(ByteBufferGifDecoder.DISABLE_ANIMATION, true);
        set(StreamGifDecoder.DISABLE_ANIMATION, true);
        return selfOrThrowIfLocked();
    }

    public RequestOptions apply(RequestOptions requestOptions) {
        if (this.isAutoCloneEnabled) {
            return m9clone().apply(requestOptions);
        }
        if (isSet(requestOptions.fields, 2)) {
            this.sizeMultiplier = requestOptions.sizeMultiplier;
        }
        if (isSet(requestOptions.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = requestOptions.useUnlimitedSourceGeneratorsPool;
        }
        if (isSet(requestOptions.fields, 4)) {
            this.diskCacheStrategy = requestOptions.diskCacheStrategy;
        }
        if (isSet(requestOptions.fields, 8)) {
            this.priority = requestOptions.priority;
        }
        if (isSet(requestOptions.fields, 16)) {
            this.errorPlaceholder = requestOptions.errorPlaceholder;
        }
        if (isSet(requestOptions.fields, 32)) {
            this.errorId = requestOptions.errorId;
        }
        if (isSet(requestOptions.fields, 64)) {
            this.placeholderDrawable = requestOptions.placeholderDrawable;
        }
        if (isSet(requestOptions.fields, 128)) {
            this.placeholderId = requestOptions.placeholderId;
        }
        if (isSet(requestOptions.fields, 256)) {
            this.isCacheable = requestOptions.isCacheable;
        }
        if (isSet(requestOptions.fields, 512)) {
            this.overrideWidth = requestOptions.overrideWidth;
            this.overrideHeight = requestOptions.overrideHeight;
        }
        if (isSet(requestOptions.fields, 1024)) {
            this.signature = requestOptions.signature;
        }
        if (isSet(requestOptions.fields, 4096)) {
            this.resourceClass = requestOptions.resourceClass;
        }
        if (isSet(requestOptions.fields, 8192)) {
            this.fallbackDrawable = requestOptions.fallbackDrawable;
        }
        if (isSet(requestOptions.fields, 16384)) {
            this.fallbackId = requestOptions.fallbackId;
        }
        if (isSet(requestOptions.fields, 32768)) {
            this.theme = requestOptions.theme;
        }
        if (isSet(requestOptions.fields, 65536)) {
            this.isTransformationAllowed = requestOptions.isTransformationAllowed;
        }
        if (isSet(requestOptions.fields, 131072)) {
            this.isTransformationRequired = requestOptions.isTransformationRequired;
        }
        if (isSet(requestOptions.fields, 2048)) {
            this.transformations.putAll(requestOptions.transformations);
        }
        if (isSet(requestOptions.fields, 524288)) {
            this.onlyRetrieveFromCache = requestOptions.onlyRetrieveFromCache;
        }
        if (!this.isTransformationAllowed) {
            this.transformations.clear();
            int i = this.fields & (-2049);
            this.fields = i;
            this.isTransformationRequired = false;
            this.fields = i & (-131073);
        }
        this.fields |= requestOptions.fields;
        this.options.putAll(requestOptions.options);
        return selfOrThrowIfLocked();
    }

    public RequestOptions lock() {
        this.isLocked = true;
        return this;
    }

    public RequestOptions autoClone() {
        if (this.isLocked && !this.isAutoCloneEnabled) {
            throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
        }
        this.isAutoCloneEnabled = true;
        return lock();
    }

    private RequestOptions selfOrThrowIfLocked() {
        if (this.isLocked) {
            throw new IllegalStateException("You cannot modify locked RequestOptions, consider clone()");
        }
        return this;
    }

    public final Map<Class<?>, Transformation<?>> getTransformations() {
        return this.transformations;
    }

    public final boolean isTransformationRequired() {
        return this.isTransformationRequired;
    }

    public final Options getOptions() {
        return this.options;
    }

    public final Class<?> getResourceClass() {
        return this.resourceClass;
    }

    public final DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }

    public final Drawable getErrorPlaceholder() {
        return this.errorPlaceholder;
    }

    public final int getErrorId() {
        return this.errorId;
    }

    public final int getPlaceholderId() {
        return this.placeholderId;
    }

    public final Drawable getPlaceholderDrawable() {
        return this.placeholderDrawable;
    }

    public final int getFallbackId() {
        return this.fallbackId;
    }

    public final Drawable getFallbackDrawable() {
        return this.fallbackDrawable;
    }

    public final Resources.Theme getTheme() {
        return this.theme;
    }

    public final boolean isMemoryCacheable() {
        return this.isCacheable;
    }

    public final Key getSignature() {
        return this.signature;
    }

    public final boolean isPrioritySet() {
        return isSet(8);
    }

    public final Priority getPriority() {
        return this.priority;
    }

    public final int getOverrideWidth() {
        return this.overrideWidth;
    }

    public final boolean isValidOverride() {
        return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
    }

    public final int getOverrideHeight() {
        return this.overrideHeight;
    }

    public final float getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    private boolean isSet(int i) {
        return isSet(this.fields, i);
    }

    public final boolean getUseUnlimitedSourceGeneratorsPool() {
        return this.useUnlimitedSourceGeneratorsPool;
    }

    public final boolean getOnlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }
}
