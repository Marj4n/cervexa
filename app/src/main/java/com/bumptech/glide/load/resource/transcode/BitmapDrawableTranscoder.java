package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

/* loaded from: classes.dex */
public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {
    private final BitmapPool bitmapPool;
    private final Resources resources;

    public BitmapDrawableTranscoder(Context context) {
        this(context.getResources(), Glide.get(context).getBitmapPool());
    }

    public BitmapDrawableTranscoder(Resources resources, BitmapPool bitmapPool) {
        this.resources = (Resources) Preconditions.checkNotNull(resources);
        this.bitmapPool = (BitmapPool) Preconditions.checkNotNull(bitmapPool);
    }

    @Override // com.bumptech.glide.load.resource.transcode.ResourceTranscoder
    public Resource<BitmapDrawable> transcode(Resource<Bitmap> resource) {
        return LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, resource.get());
    }
}
