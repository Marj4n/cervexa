package com.baidu.android.bbalbs.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.android.bbalbs.common.a.d;
import com.jieli.lib.dv.control.utils.TopicKey;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import kotlin.UByte;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.nntp.NNTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class b {
    private static final String a = new String(com.baidu.android.bbalbs.common.a.b.a(new byte[]{77, 122, 65, 121, 77, 84, 73, 120, 77, 68, 73, 61})) + new String(com.baidu.android.bbalbs.common.a.b.a(new byte[]{90, 71, 108, 106, 100, 87, 82, 112, 89, 87, 73, 61}));
    private static C0007b e;
    private final Context b;
    private int c = 0;
    private PublicKey d;

    /* JADX INFO: Access modifiers changed from: private */
    static class a {
        public ApplicationInfo a;
        public int b;
        public boolean c;
        public boolean d;

        private a() {
            this.b = 0;
            this.c = false;
            this.d = false;
        }

        /* synthetic */ a(com.baidu.android.bbalbs.common.util.c cVar) {
            this();
        }
    }

    /* renamed from: com.baidu.android.bbalbs.common.util.b$b, reason: collision with other inner class name */
    private static class C0007b {
        public String a;
        public String b;
        public int c;

        private C0007b() {
            this.c = 2;
        }

        /* synthetic */ C0007b(com.baidu.android.bbalbs.common.util.c cVar) {
            this();
        }

        public static C0007b a(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("deviceid");
                String string2 = jSONObject.getString("imei");
                int i = jSONObject.getInt(TopicKey.VERSION);
                if (!TextUtils.isEmpty(string) && string2 != null) {
                    C0007b c0007b = new C0007b();
                    c0007b.a = string;
                    c0007b.b = string2;
                    c0007b.c = i;
                    return c0007b;
                }
            } catch (JSONException e) {
                b.b(e);
            }
            return null;
        }

        public String a() {
            try {
                return new JSONObject().put("deviceid", this.a).put("imei", this.b).put(TopicKey.VERSION, this.c).toString();
            } catch (JSONException e) {
                b.b(e);
                return null;
            }
        }

        public String b() {
            String str = this.b;
            if (TextUtils.isEmpty(str)) {
                str = "0";
            }
            return this.a + "|" + new StringBuffer(str).reverse().toString();
        }
    }

    static class c {
        static boolean a(String str, int i) {
            try {
                Os.chmod(str, i);
                return true;
            } catch (ErrnoException e) {
                b.b(e);
                return false;
            }
        }
    }

    private b(Context context) {
        this.b = context.getApplicationContext();
        a();
    }

    public static String a(Context context) {
        return c(context).b();
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x003f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String a(java.io.File r5) {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            r5 = 8192(0x2000, float:1.148E-41)
            char[] r5 = new char[r5]     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
            java.io.CharArrayWriter r2 = new java.io.CharArrayWriter     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
            r2.<init>()     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
        Lf:
            int r3 = r1.read(r5)     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
            if (r3 <= 0) goto L1a
            r4 = 0
            r2.write(r5, r4, r3)     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
            goto Lf
        L1a:
            java.lang.String r5 = r2.toString()     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3b
            r1.close()     // Catch: java.lang.Exception -> L22
            goto L26
        L22:
            r0 = move-exception
            b(r0)
        L26:
            return r5
        L27:
            r5 = move-exception
            goto L2d
        L29:
            r5 = move-exception
            goto L3d
        L2b:
            r5 = move-exception
            r1 = r0
        L2d:
            b(r5)     // Catch: java.lang.Throwable -> L3b
            if (r1 == 0) goto L3a
            r1.close()     // Catch: java.lang.Exception -> L36
            goto L3a
        L36:
            r5 = move-exception
            b(r5)
        L3a:
            return r0
        L3b:
            r5 = move-exception
            r0 = r1
        L3d:
            if (r0 == 0) goto L47
            r0.close()     // Catch: java.lang.Exception -> L43
            goto L47
        L43:
            r0 = move-exception
            b(r0)
        L47:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.bbalbs.common.util.b.a(java.io.File):java.lang.String");
    }

    private static String a(byte[] bArr) {
        StringBuilder sb;
        if (bArr == null) {
            throw new IllegalArgumentException("Argument b ( byte array ) is null! ");
        }
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb = new StringBuilder();
                sb.append(str);
                str = "0";
            } else {
                sb = new StringBuilder();
            }
            sb.append(str);
            sb.append(hexString);
            str = sb.toString();
        }
        return str.toLowerCase();
    }

    private List<a> a(Intent intent, boolean z) {
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = this.b.getPackageManager();
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers != null) {
            for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
                if (resolveInfo.activityInfo != null && resolveInfo.activityInfo.applicationInfo != null) {
                    try {
                        Bundle bundle = packageManager.getReceiverInfo(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name), 128).metaData;
                        if (bundle != null) {
                            String string = bundle.getString("galaxy_data");
                            if (!TextUtils.isEmpty(string)) {
                                byte[] a2 = com.baidu.android.bbalbs.common.a.b.a(string.getBytes("utf-8"));
                                JSONObject jSONObject = new JSONObject(new String(a2));
                                a aVar = new a(null);
                                aVar.b = jSONObject.getInt("priority");
                                aVar.a = resolveInfo.activityInfo.applicationInfo;
                                if (this.b.getPackageName().equals(resolveInfo.activityInfo.applicationInfo.packageName)) {
                                    aVar.d = true;
                                }
                                if (z) {
                                    String string2 = bundle.getString("galaxy_sf");
                                    if (!TextUtils.isEmpty(string2)) {
                                        PackageInfo packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName, 64);
                                        JSONArray jSONArray = jSONObject.getJSONArray("sigs");
                                        int length = jSONArray.length();
                                        String[] strArr = new String[length];
                                        for (int i = 0; i < length; i++) {
                                            strArr[i] = jSONArray.getString(i);
                                        }
                                        if (a(strArr, a(packageInfo.signatures))) {
                                            byte[] a3 = a(com.baidu.android.bbalbs.common.a.b.a(string2.getBytes()), this.d);
                                            if (a3 != null && Arrays.equals(a3, d.a(a2))) {
                                                aVar.c = true;
                                            }
                                        }
                                    }
                                }
                                arrayList.add(aVar);
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
        Collections.sort(arrayList, new com.baidu.android.bbalbs.common.util.c(this));
        return arrayList;
    }

    private void a() {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        ByteArrayInputStream byteArrayInputStream2 = null;
        try {
            try {
                byteArrayInputStream = new ByteArrayInputStream(com.baidu.android.bbalbs.common.util.a.a());
                try {
                    this.d = CertificateFactory.getInstance("X.509").generateCertificate(byteArrayInputStream).getPublicKey();
                    byteArrayInputStream.close();
                } catch (Exception unused) {
                    byteArrayInputStream2 = byteArrayInputStream;
                    if (byteArrayInputStream2 != null) {
                        byteArrayInputStream2.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e2) {
                            b(e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                b(e3);
            }
        } catch (Exception unused2) {
        } catch (Throwable th3) {
            byteArrayInputStream = null;
            th = th3;
        }
    }

    private boolean a(String str) {
        int i = Build.VERSION.SDK_INT >= 24 ? 0 : 1;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = this.b.openFileOutput("libcuid.so", i);
                fileOutputStream.write(str.getBytes());
                fileOutputStream.flush();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        b(e2);
                    }
                }
                if (i == 0) {
                    return c.a(new File(this.b.getFilesDir(), "libcuid.so").getAbsolutePath(), NNTPReply.TRANSFER_FAILED);
                }
                return true;
            } catch (Throwable th) {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e3) {
                        b(e3);
                    }
                }
                throw th;
            }
        } catch (Exception e4) {
            b(e4);
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e5) {
                    b(e5);
                }
            }
            return false;
        }
    }

    private boolean a(String str, String str2) {
        try {
            return Settings.System.putString(this.b.getContentResolver(), str, str2);
        } catch (Exception e2) {
            b(e2);
            return false;
        }
    }

    private boolean a(String[] strArr, String[] strArr2) {
        if (strArr == null || strArr2 == null || strArr.length != strArr2.length) {
            return false;
        }
        HashSet hashSet = new HashSet();
        for (String str : strArr) {
            hashSet.add(str);
        }
        HashSet hashSet2 = new HashSet();
        for (String str2 : strArr2) {
            hashSet2.add(str2);
        }
        return hashSet.equals(hashSet2);
    }

    private static byte[] a(byte[] bArr, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, publicKey);
        return cipher.doFinal(bArr);
    }

    private String[] a(Signature[] signatureArr) {
        int length = signatureArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = a(d.a(signatureArr[i].toByteArray()));
        }
        return strArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x01bc, code lost:
    
        if (android.text.TextUtils.isEmpty(b("com.baidu.deviceid.v2")) != false) goto L73;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [com.baidu.android.bbalbs.common.util.c] */
    /* JADX WARN: Type inference failed for: r8v18 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.baidu.android.bbalbs.common.util.b.C0007b b() {
        /*
            Method dump skipped, instructions count: 583
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.bbalbs.common.util.b.b():com.baidu.android.bbalbs.common.util.b$b");
    }

    public static String b(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(string) ? "" : string;
    }

    private String b(String str) {
        try {
            return Settings.System.getString(this.b.getContentResolver(), str);
        } catch (Exception e2) {
            b(e2);
            return null;
        }
    }

    private static void b(String str, String str2) {
        File file;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File file2 = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
        File file3 = new File(file2, ".cuid");
        try {
            if (file2.exists() && !file2.isDirectory()) {
                Random random = new Random();
                File parentFile = file2.getParentFile();
                String name = file2.getName();
                do {
                    file = new File(parentFile, name + random.nextInt() + ".tmp");
                } while (file.exists());
                file2.renameTo(file);
                file.delete();
            }
            file2.mkdirs();
            FileWriter fileWriter = new FileWriter(file3, false);
            fileWriter.write(com.baidu.android.bbalbs.common.a.b.a(com.baidu.android.bbalbs.common.a.a.a(a, a, (str + "=" + str2).getBytes()), "utf-8"));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException | Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(Throwable th) {
    }

    private static C0007b c(Context context) {
        if (e == null) {
            synchronized (C0007b.class) {
                if (e == null) {
                    SystemClock.uptimeMillis();
                    e = new b(context).b();
                    SystemClock.uptimeMillis();
                }
            }
        }
        return e;
    }

    private boolean c() {
        return c("android.permission.WRITE_SETTINGS");
    }

    private boolean c(String str) {
        return this.b.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    private C0007b d() {
        String b = b("com.baidu.deviceid");
        String b2 = b("bd_setting_i");
        if (TextUtils.isEmpty(b2)) {
            b2 = h("");
            if (!TextUtils.isEmpty(b2)) {
                a("bd_setting_i", b2);
            }
        }
        if (TextUtils.isEmpty(b)) {
            b = b(com.baidu.android.bbalbs.common.a.c.a(("com.baidu" + b2 + b(this.b)).getBytes(), true));
        }
        com.baidu.android.bbalbs.common.util.c cVar = null;
        if (TextUtils.isEmpty(b)) {
            return null;
        }
        C0007b c0007b = new C0007b(cVar);
        c0007b.a = b;
        c0007b.b = b2;
        return c0007b;
    }

    private C0007b d(String str) {
        boolean z;
        String str2;
        boolean z2 = Build.VERSION.SDK_INT < 23;
        com.baidu.android.bbalbs.common.util.c cVar = null;
        if (z2 && TextUtils.isEmpty(str)) {
            return null;
        }
        String str3 = "";
        File file = new File(Environment.getExternalStorageDirectory(), "baidu/.cuid");
        if (file.exists()) {
            z = false;
        } else {
            file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid");
            z = true;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append(SocketClient.NETASCII_EOL);
            }
            bufferedReader.close();
            String[] split = new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(sb.toString().getBytes()))).split("=");
            if (split != null && split.length == 2) {
                if (z2 && str.equals(split[0])) {
                    str2 = split[1];
                } else if (!z2) {
                    if (TextUtils.isEmpty(str)) {
                        str = split[1];
                    }
                    str2 = split[1];
                }
                str3 = str2;
            }
            if (!z) {
                b(str, str3);
            }
        } catch (FileNotFoundException | IOException | Exception unused) {
        }
        if (TextUtils.isEmpty(str3)) {
            return null;
        }
        C0007b c0007b = new C0007b(cVar);
        c0007b.a = str3;
        c0007b.b = str;
        return c0007b;
    }

    private C0007b e() {
        File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid2");
        if (!file.exists()) {
            return null;
        }
        String a2 = a(file);
        if (TextUtils.isEmpty(a2)) {
            return null;
        }
        try {
            return C0007b.a(new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(a2.getBytes()))));
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String e(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return com.baidu.android.bbalbs.common.a.b.a(com.baidu.android.bbalbs.common.a.a.a(a, a, str.getBytes()), "utf-8");
        } catch (UnsupportedEncodingException | Exception e2) {
            b(e2);
            return "";
        }
    }

    private static String f(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(str.getBytes())));
        } catch (Exception e2) {
            b(e2);
            return "";
        }
    }

    private static void g(String str) {
        File file;
        File file2 = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
        File file3 = new File(file2, ".cuid2");
        try {
            if (file2.exists() && !file2.isDirectory()) {
                Random random = new Random();
                File parentFile = file2.getParentFile();
                String name = file2.getName();
                do {
                    file = new File(parentFile, name + random.nextInt() + ".tmp");
                } while (file.exists());
                file2.renameTo(file);
                file.delete();
            }
            file2.mkdirs();
            FileWriter fileWriter = new FileWriter(file3, false);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException | Exception unused) {
        }
    }

    private String h(String str) {
        String str2 = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.b.getSystemService("phone");
            if (telephonyManager != null) {
                str2 = telephonyManager.getDeviceId();
            }
        } catch (Exception e2) {
            Log.e("DeviceId", "Read IMEI failed", e2);
        }
        String i = i(str2);
        return TextUtils.isEmpty(i) ? str : i;
    }

    private static String i(String str) {
        return (str == null || !str.contains(":")) ? str : "";
    }
}
