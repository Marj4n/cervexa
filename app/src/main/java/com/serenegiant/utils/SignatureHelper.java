package com.serenegiant.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class SignatureHelper {
    public static boolean checkSignature(Context context, String str) throws IllegalArgumentException, PackageManager.NameNotFoundException {
        if (context == null || TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("context or key is null");
        }
        Signature signature = new Signature(str);
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
        boolean z = true;
        for (int i = 0; i < packageInfo.signatures.length; i++) {
            z &= signature.equals(packageInfo.signatures[i]);
        }
        return z;
    }

    public static String getSignature(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < packageInfo.signatures.length; i++) {
                Signature signature = packageInfo.signatures[i];
                if (signature != null) {
                    sb.append(signature.toCharsString());
                }
            }
            return sb.toString();
        } catch (Exception unused) {
            return null;
        }
    }

    public static byte[] getSignatureBytes(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            for (int i = 0; i < packageInfo.signatures.length; i++) {
                Signature signature = packageInfo.signatures[i];
                if (signature != null) {
                    byte[] byteArray = signature.toByteArray();
                    int length = byteArray != null ? byteArray.length : 0;
                    if (length > 0) {
                        if (length > allocate.remaining()) {
                            allocate.flip();
                            ByteBuffer allocate2 = ByteBuffer.allocate(allocate.capacity() + (length * 2));
                            allocate2.put(allocate);
                            allocate = allocate2;
                        }
                        allocate.put(byteArray);
                    }
                }
            }
            allocate.flip();
            int limit = allocate.limit();
            if (limit <= 0) {
                return null;
            }
            byte[] bArr = new byte[limit];
            allocate.get(bArr);
            return bArr;
        } catch (Exception unused) {
            return null;
        }
    }
}
