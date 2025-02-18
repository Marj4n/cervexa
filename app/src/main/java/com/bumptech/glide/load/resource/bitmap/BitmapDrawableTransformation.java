package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class BitmapDrawableTransformation implements Transformation<BitmapDrawable> {
    private final Transformation<Bitmap> wrapped;

    public BitmapDrawableTransformation(Transformation<Bitmap> transformation) {
        this.wrapped = (Transformation) Preconditions.checkNotNull(transformation);
    }

    @Deprecated
    public BitmapDrawableTransformation(Context context, Transformation<Bitmap> transformation) {
        this(transformation);
    }

    @Deprecated
    public BitmapDrawableTransformation(Context context, BitmapPool bitmapPool, Transformation<Bitmap> transformation) {
        this(transformation);
    }

    @Override // com.bumptech.glide.load.Transformation
    public Resource<BitmapDrawable> transform(Context context, Resource<BitmapDrawable> resource, int i, int i2) {
        BitmapResource obtain = BitmapResource.obtain(resource.get().getBitmap(), Glide.get(context).getBitmapPool());
        Resource<Bitmap> transform = this.wrapped.transform(context, obtain, i, i2);
        return transform.equals(obtain) ? resource : LazyBitmapDrawableResource.obtain(context, transform.get());
    }

    @Override // com.bumptech.glide.load.Transformation, com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (obj instanceof BitmapDrawableTransformation) {
            return this.wrapped.equals(((BitmapDrawableTransformation) obj).wrapped);
        }
        return false;
    }

    @Override // com.bumptech.glide.load.Transformation, com.bumptech.glide.load.Key
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}
