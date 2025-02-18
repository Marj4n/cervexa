package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;

/* loaded from: classes.dex */
public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");

    /* JADX WARN: Removed duplicated region for block: B:17:0x007b A[Catch: all -> 0x00cb, TRY_LEAVE, TryCatch #5 {all -> 0x00cb, blocks: (B:3:0x0038, B:12:0x0056, B:15:0x0075, B:17:0x007b, B:36:0x00c7, B:34:0x00ca, B:30:0x0071), top: B:2:0x0038 }] */
    @Override // com.bumptech.glide.load.Encoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean encode(com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> r9, java.io.File r10, com.bumptech.glide.load.Options r11) {
        /*
            r8 = this;
            java.lang.String r0 = "BitmapEncoder"
            java.lang.Object r9 = r9.get()
            android.graphics.Bitmap r9 = (android.graphics.Bitmap) r9
            android.graphics.Bitmap$CompressFormat r1 = r8.getFormat(r9, r11)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "encode: ["
            r2.append(r3)
            int r3 = r9.getWidth()
            r2.append(r3)
            java.lang.String r3 = "x"
            r2.append(r3)
            int r3 = r9.getHeight()
            r2.append(r3)
            java.lang.String r3 = "] "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            androidx.core.os.TraceCompat.beginSection(r2)
            long r2 = com.bumptech.glide.util.LogTime.getLogTime()     // Catch: java.lang.Throwable -> Lcb
            com.bumptech.glide.load.Option<java.lang.Integer> r4 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_QUALITY     // Catch: java.lang.Throwable -> Lcb
            java.lang.Object r4 = r11.get(r4)     // Catch: java.lang.Throwable -> Lcb
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch: java.lang.Throwable -> Lcb
            int r4 = r4.intValue()     // Catch: java.lang.Throwable -> Lcb
            r5 = 0
            r6 = 0
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L60 java.io.IOException -> L62
            r7.<init>(r10)     // Catch: java.lang.Throwable -> L60 java.io.IOException -> L62
            r9.compress(r1, r4, r7)     // Catch: java.lang.Throwable -> L5a java.io.IOException -> L5d
            r7.close()     // Catch: java.lang.Throwable -> L5a java.io.IOException -> L5d
            r5 = 1
            r7.close()     // Catch: java.io.IOException -> L74 java.lang.Throwable -> Lcb
            goto L74
        L5a:
            r9 = move-exception
            r6 = r7
            goto Lc5
        L5d:
            r10 = move-exception
            r6 = r7
            goto L63
        L60:
            r9 = move-exception
            goto Lc5
        L62:
            r10 = move-exception
        L63:
            r4 = 3
            boolean r4 = android.util.Log.isLoggable(r0, r4)     // Catch: java.lang.Throwable -> L60
            if (r4 == 0) goto L6f
            java.lang.String r4 = "Failed to encode Bitmap"
            android.util.Log.d(r0, r4, r10)     // Catch: java.lang.Throwable -> L60
        L6f:
            if (r6 == 0) goto L74
            r6.close()     // Catch: java.io.IOException -> L74 java.lang.Throwable -> Lcb
        L74:
            r10 = 2
            boolean r10 = android.util.Log.isLoggable(r0, r10)     // Catch: java.lang.Throwable -> Lcb
            if (r10 == 0) goto Lc1
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lcb
            r10.<init>()     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r4 = "Compressed with type: "
            r10.append(r4)     // Catch: java.lang.Throwable -> Lcb
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r1 = " of size "
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            int r1 = com.bumptech.glide.util.Util.getBitmapByteSize(r9)     // Catch: java.lang.Throwable -> Lcb
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r1 = " in "
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            double r1 = com.bumptech.glide.util.LogTime.getElapsedMillis(r2)     // Catch: java.lang.Throwable -> Lcb
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r1 = ", options format: "
            r10.append(r1)     // Catch: java.lang.Throwable -> Lcb
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r1 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_FORMAT     // Catch: java.lang.Throwable -> Lcb
            java.lang.Object r11 = r11.get(r1)     // Catch: java.lang.Throwable -> Lcb
            r10.append(r11)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r11 = ", hasAlpha: "
            r10.append(r11)     // Catch: java.lang.Throwable -> Lcb
            boolean r9 = r9.hasAlpha()     // Catch: java.lang.Throwable -> Lcb
            r10.append(r9)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r9 = r10.toString()     // Catch: java.lang.Throwable -> Lcb
            android.util.Log.v(r0, r9)     // Catch: java.lang.Throwable -> Lcb
        Lc1:
            androidx.core.os.TraceCompat.endSection()
            return r5
        Lc5:
            if (r6 == 0) goto Lca
            r6.close()     // Catch: java.io.IOException -> Lca java.lang.Throwable -> Lcb
        Lca:
            throw r9     // Catch: java.lang.Throwable -> Lcb
        Lcb:
            r9 = move-exception
            androidx.core.os.TraceCompat.endSection()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        Bitmap.CompressFormat compressFormat = (Bitmap.CompressFormat) options.get(COMPRESSION_FORMAT);
        if (compressFormat != null) {
            return compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    @Override // com.bumptech.glide.load.ResourceEncoder
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
