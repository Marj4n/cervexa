package rx.internal.util.unsafe;

import java.lang.reflect.Field;

import io.netty.channel.Channel;

/* loaded from: classes2.dex */
public final class UnsafeAccess {
    private static final boolean DISABLED_BY_USER;
    public static final Channel.Unsafe UNSAFE;

    private UnsafeAccess() {
        throw new IllegalStateException("No instances!");
    }

    static {
        DISABLED_BY_USER = System.getProperty("rx.unsafe-disable") != null;
        Channel.Unsafe unsafe = null;
        try {
            Field declaredField = Channel.Unsafe.class.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            unsafe = (Channel.Unsafe) declaredField.get(null);
        } catch (Throwable unused) {
        }
        UNSAFE = unsafe;
    }

    public static boolean isUnsafeAvailable() {
        return (UNSAFE == null || DISABLED_BY_USER) ? false : true;
    }
}
