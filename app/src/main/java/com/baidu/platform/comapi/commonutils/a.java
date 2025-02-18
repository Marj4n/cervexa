package com.baidu.platform.comapi.commonutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
public class a {
    private static final boolean a;

    static {
        a = Build.VERSION.SDK_INT >= 8;
    }

    public static Bitmap a(String str, Context context) {
        try {
            InputStream open = context.getAssets().open(str);
            if (open != null) {
                return BitmapFactory.decodeStream(open);
            }
            return null;
        } catch (Exception unused) {
            return BitmapFactory.decodeFile(b("assets/" + str, str, context));
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

    /* JADX WARN: Removed duplicated region for block: B:36:0x0097 A[Catch: IOException -> 0x0093, TRY_LEAVE, TryCatch #2 {IOException -> 0x0093, blocks: (B:43:0x008f, B:36:0x0097), top: B:42:0x008f }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x008f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void a(java.lang.String r6, java.lang.String r7, android.content.Context r8) {
        /*
            r0 = 0
            android.content.res.AssetManager r1 = r8.getAssets()     // Catch: java.lang.Throwable -> L69 java.lang.Exception -> L6c
            java.io.InputStream r1 = r1.open(r6)     // Catch: java.lang.Throwable -> L69 java.lang.Exception -> L6c
            if (r1 == 0) goto L57
            int r2 = r1.available()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r1.read(r2)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.<init>()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.io.File r5 = r8.getFilesDir()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.lang.String r5 = r5.getAbsolutePath()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.append(r5)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.lang.String r5 = "/"
            r4.append(r5)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.append(r7)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            boolean r4 = r3.exists()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            if (r4 == 0) goto L3e
            r3.delete()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
        L3e:
            r3.createNewFile()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.<init>(r3)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.write(r2)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L55
            r4.close()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L55
            r0 = r4
            goto L57
        L4e:
            r6 = move-exception
            goto L52
        L50:
            r6 = move-exception
            r4 = r0
        L52:
            r0 = r1
            goto L8d
        L54:
            r4 = r0
        L55:
            r0 = r1
            goto L6d
        L57:
            if (r1 == 0) goto L5f
            r1.close()     // Catch: java.io.IOException -> L5d
            goto L5f
        L5d:
            r6 = move-exception
            goto L65
        L5f:
            if (r0 == 0) goto L8b
            r0.close()     // Catch: java.io.IOException -> L5d
            goto L8b
        L65:
            r6.printStackTrace()
            goto L8b
        L69:
            r6 = move-exception
            r4 = r0
            goto L8d
        L6c:
            r4 = r0
        L6d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8c
            r1.<init>()     // Catch: java.lang.Throwable -> L8c
            java.lang.String r2 = "assets/"
            r1.append(r2)     // Catch: java.lang.Throwable -> L8c
            r1.append(r6)     // Catch: java.lang.Throwable -> L8c
            java.lang.String r6 = r1.toString()     // Catch: java.lang.Throwable -> L8c
            b(r6, r7, r8)     // Catch: java.lang.Throwable -> L8c
            if (r0 == 0) goto L86
            r0.close()     // Catch: java.io.IOException -> L5d
        L86:
            if (r4 == 0) goto L8b
            r4.close()     // Catch: java.io.IOException -> L5d
        L8b:
            return
        L8c:
            r6 = move-exception
        L8d:
            if (r0 == 0) goto L95
            r0.close()     // Catch: java.io.IOException -> L93
            goto L95
        L93:
            r7 = move-exception
            goto L9b
        L95:
            if (r4 == 0) goto L9e
            r4.close()     // Catch: java.io.IOException -> L93
            goto L9e
        L9b:
            r7.printStackTrace()
        L9e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.commonutils.a.a(java.lang.String, java.lang.String, android.content.Context):void");
    }

    private static String b(String str, String str2, Context context) {
        ZipFile zipFile;
        File file;
        File file2;
        ZipEntry entry;
        StringBuilder sb = new StringBuilder(context.getFilesDir().getAbsolutePath());
        ZipFile zipFile2 = null;
        try {
            try {
                try {
                    zipFile = new ZipFile(a ? context.getPackageCodePath() : "");
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException unused) {
        }
        try {
            int lastIndexOf = str2.lastIndexOf("/");
            if (lastIndexOf > 0) {
                file = new File(context.getFilesDir().getAbsolutePath());
                String substring = str2.substring(0, lastIndexOf);
                file2 = new File(file.getAbsolutePath() + "/" + substring, str2.substring(lastIndexOf + 1, str2.length()));
            } else {
                file = new File(context.getFilesDir(), "assets");
                file2 = new File(file.getAbsolutePath(), str2);
            }
            file.mkdirs();
            entry = zipFile.getEntry(str);
        } catch (Exception e2) {
            e = e2;
            zipFile2 = zipFile;
            Log.e(a.class.getSimpleName(), "copyAssetsError", e);
            if (zipFile2 != null) {
                zipFile2.close();
            }
            return sb.toString();
        } catch (Throwable th2) {
            th = th2;
            zipFile2 = zipFile;
            if (zipFile2 != null) {
                try {
                    zipFile2.close();
                } catch (IOException unused2) {
                }
            }
            throw th;
        }
        if (entry == null) {
            try {
                zipFile.close();
            } catch (IOException unused3) {
            }
            return null;
        }
        a(zipFile.getInputStream(entry), new FileOutputStream(file2));
        sb.append("/");
        sb.append(str);
        zipFile.close();
        return sb.toString();
    }
}
