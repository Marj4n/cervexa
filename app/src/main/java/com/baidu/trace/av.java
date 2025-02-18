package com.baidu.trace;

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
public class av {
    private static Context a;
    private static av d;
    private static final Set<String> b = new HashSet();
    private static final Set<String> c = new HashSet();
    private static a e = a.ARMEABI;

    /* renamed from: com.baidu.trace.av$1, reason: invalid class name */
    final /* synthetic */ class AnonymousClass1 {
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

    enum a {
        ARMEABI("armeabi"),
        ARMV7("armeabi-v7a"),
        ARM64("arm64-v8a"),
        X86("x86"),
        X86_64("x86_64");

        private String f;

        a(String str) {
            this.f = str;
        }

        public final String b() {
            return this.f;
        }
    }

    private av() {
    }

    public static synchronized av a() {
        av avVar;
        a aVar;
        synchronized (av.class) {
            if (d == null) {
                d = new av();
                String str = Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
                if (str == null) {
                    aVar = a.ARMEABI;
                } else {
                    if (str.contains("arm") && str.contains("v7")) {
                        e = a.ARMV7;
                    }
                    if (str.contains("arm") && str.contains("64")) {
                        e = a.ARM64;
                    }
                    if (str.contains("x86")) {
                        e = str.contains("64") ? a.X86_64 : a.X86;
                    }
                    aVar = e;
                }
                e = aVar;
            }
            avVar = d;
        }
        return avVar;
    }

    public static void a(Context context) {
        if (a == null) {
            a = context;
        }
    }

    private static void a(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
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

    private static void a(Throwable th) {
        Log.e(av.class.getSimpleName(), "loadException", th);
        for (String str : c) {
            Log.e(av.class.getSimpleName(), str + " Failed to load.");
        }
    }

    private boolean a(String str, a aVar) {
        ZipFile zipFile;
        StringBuilder sb = new StringBuilder();
        sb.append("lib/" + aVar.b() + "/");
        sb.append(str);
        String sb2 = sb.toString();
        ZipFile zipFile2 = null;
        try {
            try {
                zipFile = new ZipFile(8 <= Build.VERSION.SDK_INT ? a.getPackageCodePath() : "");
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            File file = new File(b(), str);
            ZipEntry entry = zipFile.getEntry(sb2);
            if (entry == null) {
                try {
                    zipFile.close();
                } catch (IOException unused) {
                }
                return false;
            }
            a(zipFile.getInputStream(entry), new FileOutputStream(file));
            try {
                zipFile.close();
                return true;
            } catch (IOException unused2) {
                return false;
            }
        } catch (Exception e3) {
            e = e3;
            zipFile2 = zipFile;
            Log.e(av.class.getSimpleName(), "copyError", e);
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

    private boolean a(String str, String str2) {
        return !a(str2, a.ARMV7) ? b(str, str2) : d(str2, str);
    }

    private static String b() {
        File file = new File(a.getFilesDir(), "libs");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    private boolean b(String str, String str2) {
        if (a(str2, a.ARMEABI)) {
            return d(str2, str);
        }
        return false;
    }

    private boolean c(String str, String str2) {
        return !a(str2, a.X86) ? a(str, str2) : d(str2, str);
    }

    private boolean d(String str, String str2) {
        try {
            System.load(new File(b(), str).getAbsolutePath());
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

    /* JADX WARN: Removed duplicated region for block: B:33:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0065 A[Catch: all -> 0x0074, TryCatch #1 {, blocks: (B:31:0x0025, B:43:0x0043, B:45:0x0048, B:47:0x0050, B:50:0x0055, B:51:0x005b, B:53:0x0060, B:55:0x0065, B:57:0x006d), top: B:6:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized boolean a(java.lang.String r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 1
            java.util.Set<java.lang.String> r1 = com.baidu.trace.av.b     // Catch: java.lang.Throwable -> L25
            monitor-enter(r1)     // Catch: java.lang.Throwable -> L25
            java.util.Set<java.lang.String> r2 = com.baidu.trace.av.b     // Catch: java.lang.Throwable -> L22
            boolean r2 = r2.contains(r6)     // Catch: java.lang.Throwable -> L22
            if (r2 == 0) goto L10
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L22
            monitor-exit(r5)
            return r0
        L10:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L25
            java.lang.System.loadLibrary(r6)     // Catch: java.lang.Throwable -> L25
            java.util.Set<java.lang.String> r1 = com.baidu.trace.av.b     // Catch: java.lang.Throwable -> L25
            monitor-enter(r1)     // Catch: java.lang.Throwable -> L25
            java.util.Set<java.lang.String> r2 = com.baidu.trace.av.b     // Catch: java.lang.Throwable -> L1f
            r2.add(r6)     // Catch: java.lang.Throwable -> L1f
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L1f
            monitor-exit(r5)
            return r0
        L1f:
            r2 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L25
            throw r2     // Catch: java.lang.Throwable -> L25
        L22:
            r2 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L25
            throw r2     // Catch: java.lang.Throwable -> L25
        L25:
            java.lang.String r1 = java.lang.System.mapLibraryName(r6)     // Catch: java.lang.Throwable -> L74
            r2 = 0
            int[] r3 = com.baidu.trace.av.AnonymousClass1.a     // Catch: java.lang.Throwable -> L74
            com.baidu.trace.av$a r4 = com.baidu.trace.av.e     // Catch: java.lang.Throwable -> L74
            int r4 = r4.ordinal()     // Catch: java.lang.Throwable -> L74
            r3 = r3[r4]     // Catch: java.lang.Throwable -> L74
            if (r3 == r0) goto L65
            r0 = 2
            if (r3 == r0) goto L60
            r0 = 3
            if (r3 == r0) goto L5b
            r0 = 4
            if (r3 == r0) goto L48
            r0 = 5
            if (r3 == r0) goto L43
            goto L72
        L43:
            boolean r2 = r5.c(r6, r1)     // Catch: java.lang.Throwable -> L74
            goto L72
        L48:
            com.baidu.trace.av$a r0 = com.baidu.trace.av.a.X86_64     // Catch: java.lang.Throwable -> L74
            boolean r0 = r5.a(r1, r0)     // Catch: java.lang.Throwable -> L74
            if (r0 != 0) goto L55
            boolean r6 = r5.c(r6, r1)     // Catch: java.lang.Throwable -> L74
            goto L59
        L55:
            boolean r6 = r5.d(r1, r6)     // Catch: java.lang.Throwable -> L74
        L59:
            r2 = r6
            goto L72
        L5b:
            boolean r2 = r5.b(r6, r1)     // Catch: java.lang.Throwable -> L74
            goto L72
        L60:
            boolean r2 = r5.a(r6, r1)     // Catch: java.lang.Throwable -> L74
            goto L72
        L65:
            com.baidu.trace.av$a r0 = com.baidu.trace.av.a.ARM64     // Catch: java.lang.Throwable -> L74
            boolean r0 = r5.a(r1, r0)     // Catch: java.lang.Throwable -> L74
            if (r0 != 0) goto L55
            boolean r6 = r5.a(r6, r1)     // Catch: java.lang.Throwable -> L74
            goto L59
        L72:
            monitor-exit(r5)
            return r2
        L74:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.av.a(java.lang.String):boolean");
    }
}
