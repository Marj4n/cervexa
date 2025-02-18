package com.baidu.platform.comapi;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
public class NativeLoader {
    private static Context a;
    private static NativeLoader d;
    private static final Set<String> b = new HashSet();
    private static final Set<String> c = new HashSet();
    private static a e = a.ARMEABI;

    /* renamed from: com.baidu.platform.comapi.NativeLoader$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[a.values().length];
            a = iArr;
            try {
                iArr[a.ARM64.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[a.ARMV7.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[a.ARMEABI.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[a.X86_64.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[a.X86.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private enum a {
        ARMEABI("armeabi"),
        ARMV7("armeabi-v7a"),
        ARM64("arm64-v8a"),
        X86("x86"),
        X86_64("x86_64");

        private String f;

        a(String str) {
            this.f = str;
        }

        public String a() {
            return this.f;
        }
    }

    private NativeLoader() {
    }

    private static a a() {
        String str = Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
        if (str == null) {
            return a.ARMEABI;
        }
        if (str.contains("arm") && str.contains("v7")) {
            e = a.ARMV7;
        }
        if (str.contains("arm") && str.contains("64")) {
            e = a.ARM64;
        }
        if (str.contains("x86")) {
            e = str.contains("64") ? a.X86_64 : a.X86;
        }
        return e;
    }

    private String a(a aVar) {
        return "lib/" + aVar.a() + "/";
    }

    private void a(Throwable th) {
        Log.e(NativeLoader.class.getSimpleName(), "loadException", th);
        for (String str : c) {
            Log.e(NativeLoader.class.getSimpleName(), str + " Failed to load.");
        }
    }

    private boolean a(String str, String str2) {
        return !copyNativeLibrary(str2, a.ARMV7) ? b(str, str2) : f(str2, str);
    }

    private boolean b(String str, String str2) {
        if (copyNativeLibrary(str2, a.ARMEABI)) {
            return f(str2, str);
        }
        Log.e(NativeLoader.class.getSimpleName(), "found lib" + str + ".so error");
        return false;
    }

    private boolean c(String str, String str2) {
        return !copyNativeLibrary(str2, a.ARM64) ? a(str, str2) : f(str2, str);
    }

    private boolean d(String str, String str2) {
        return !copyNativeLibrary(str2, a.X86) ? a(str, str2) : f(str2, str);
    }

    private boolean e(String str, String str2) {
        return !copyNativeLibrary(str2, a.X86_64) ? d(str, str2) : f(str2, str);
    }

    private boolean f(String str, String str2) {
        try {
            System.load(new File(getCustomizeNativePath(), str).getAbsolutePath());
            synchronized (b) {
                b.add(str2);
            }
            return true;
        } catch (Throwable th) {
            synchronized (c) {
                c.add(str2);
                a(th);
                return false;
            }
        }
    }

    public static synchronized NativeLoader getInstance() {
        NativeLoader nativeLoader;
        synchronized (NativeLoader.class) {
            if (d == null) {
                d = new NativeLoader();
                e = a();
            }
            nativeLoader = d;
        }
        return nativeLoader;
    }

    public static void setContext(Context context) {
        a = context;
    }

    protected boolean copyNativeLibrary(String str, a aVar) {
        ZipFile zipFile;
        String str2 = a(aVar) + str;
        ZipFile zipFile2 = null;
        try {
            try {
                zipFile = new ZipFile(getCodePath());
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            File file = new File(getCustomizeNativePath(), str);
            ZipEntry entry = zipFile.getEntry(str2);
            if (entry == null) {
                try {
                    zipFile.close();
                } catch (IOException unused) {
                }
                return false;
            }
            copyStream(zipFile.getInputStream(entry), new FileOutputStream(file));
            try {
                zipFile.close();
                return true;
            } catch (IOException unused2) {
                return false;
            }
        } catch (Exception e3) {
            e = e3;
            zipFile2 = zipFile;
            Log.e(NativeLoader.class.getSimpleName(), "copyError", e);
            if (zipFile2 != null) {
                try {
                    zipFile2.close();
                } catch (IOException unused3) {
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            zipFile2 = zipFile;
            if (zipFile2 != null) {
                try {
                    zipFile2.close();
                } catch (IOException unused4) {
                    return false;
                }
            }
            throw th;
        }
    }

    protected final void copyStream(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    fileOutputStream.flush();
                    try {
                        inputStream.close();
                        fileOutputStream.close();
                        return;
                    } catch (IOException unused) {
                        return;
                    }
                }
                fileOutputStream.write(bArr, 0, read);
            } catch (Throwable th) {
                try {
                    inputStream.close();
                    fileOutputStream.close();
                    throw th;
                } catch (IOException unused2) {
                    return;
                }
            }
        }
    }

    protected String getCodePath() {
        return 8 <= Build.VERSION.SDK_INT ? a.getPackageCodePath() : "";
    }

    protected String getCustomizeNativePath() {
        File file = new File(a.getFilesDir(), "libs");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    protected boolean loadCustomizeNativeLibrary(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        int i = AnonymousClass1.a[e.ordinal()];
        if (i == 1) {
            return c(str, mapLibraryName);
        }
        if (i == 2) {
            return a(str, mapLibraryName);
        }
        if (i == 3) {
            return b(str, mapLibraryName);
        }
        if (i == 4) {
            return e(str, mapLibraryName);
        }
        if (i != 5) {
            return false;
        }
        return d(str, mapLibraryName);
    }

    public synchronized boolean loadLibrary(String str) {
        try {
            synchronized (b) {
                if (b.contains(str)) {
                    return true;
                }
                System.loadLibrary(str);
                synchronized (b) {
                    b.add(str);
                }
                return true;
            }
        } catch (Throwable unused) {
            return loadCustomizeNativeLibrary(str);
        }
    }
}
