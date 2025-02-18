package com.bumptech.glide.load;

import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class ImageHeaderParserUtils {
    private static final int MARK_POSITION = 5242880;

    private ImageHeaderParserUtils() {
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(MARK_POSITION);
        Iterator<ImageHeaderParser> it = list.iterator();
        while (it.hasNext()) {
            try {
                ImageHeaderParser.ImageType type = it.next().getType(inputStream);
                if (type != ImageHeaderParser.ImageType.UNKNOWN) {
                    return type;
                }
            } finally {
                inputStream.reset();
            }
        }
        return ImageHeaderParser.ImageType.UNKNOWN;
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        Iterator<ImageHeaderParser> it = list.iterator();
        while (it.hasNext()) {
            ImageHeaderParser.ImageType type = it.next().getType(byteBuffer);
            if (type != ImageHeaderParser.ImageType.UNKNOWN) {
                return type;
            }
        }
        return ImageHeaderParser.ImageType.UNKNOWN;
    }

    public static int getOrientation(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return -1;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(MARK_POSITION);
        Iterator<ImageHeaderParser> it = list.iterator();
        while (it.hasNext()) {
            try {
                int orientation = it.next().getOrientation(inputStream, arrayPool);
                if (orientation != -1) {
                    return orientation;
                }
            } finally {
                inputStream.reset();
            }
        }
        return -1;
    }
}
