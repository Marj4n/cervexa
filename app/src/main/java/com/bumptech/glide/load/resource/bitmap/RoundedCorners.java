package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public final class RoundedCorners extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final int roundingRadius;

    public RoundedCorners(int i) {
        Preconditions.checkArgument(i > 0, "roundingRadius must be greater than 0.");
        this.roundingRadius = i;
    }

    @Deprecated
    public RoundedCorners(BitmapPool bitmapPool, int i) {
        this(i);
    }

    @Deprecated
    public RoundedCorners(Context context, int i) {
        this(i);
    }

    @Override // com.bumptech.glide.load.resource.bitmap.BitmapTransformation
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, i, i2, this.roundingRadius);
    }

    @Override // com.bumptech.glide.load.Transformation, com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        return (obj instanceof RoundedCorners) && ((RoundedCorners) obj).roundingRadius == this.roundingRadius;
    }

    @Override // com.bumptech.glide.load.Transformation, com.bumptech.glide.load.Key
    public int hashCode() {
        return (-569625254) + this.roundingRadius;
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
    }
}
