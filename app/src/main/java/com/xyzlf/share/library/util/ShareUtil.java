package com.xyzlf.share.library.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.ShareDialogActivity;
import com.xyzlf.share.library.ShareHandlerActivity;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import java.io.File;
import java.io.FileOutputStream;

/* loaded from: classes2.dex */
public class ShareUtil {
    public static void startShare(Activity activity, int i, ShareEntity shareEntity, int i2) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) ShareHandlerActivity.class);
        intent.putExtra(ShareConstant.EXTRA_SHARE_CHANNEL, i);
        intent.putExtra(ShareConstant.EXTRA_SHARE_DATA, shareEntity);
        activity.startActivityForResult(intent, i2);
    }

    public static void showShareDialog(Activity activity, ShareEntity shareEntity, int i) {
        showShareDialog(activity, -1, shareEntity, i);
    }

    public static void showShareDialog(Activity activity, int i, ShareEntity shareEntity, int i2) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) ShareDialogActivity.class);
        intent.putExtra(ShareConstant.EXTRA_SHARE_DATA, shareEntity);
        intent.putExtra(ShareConstant.EXTRA_SHARE_CHANNEL, i);
        activity.startActivityForResult(intent, i2);
    }

    public static void showShareDialog(Activity activity, SparseArray<ShareEntity> sparseArray, int i) {
        showShareDialog(activity, -1, sparseArray, i);
    }

    public static void showShareDialog(Activity activity, int i, SparseArray<ShareEntity> sparseArray, int i2) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) ShareDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(ShareConstant.EXTRA_SHARE_DATA, sparseArray);
        intent.putExtra(ShareConstant.EXTRA_SHARE_DATA, bundle);
        intent.putExtra(ShareConstant.EXTRA_SHARE_CHANNEL, i);
        activity.startActivityForResult(intent, i2);
    }

    public static boolean startActivity(Context context, Class<?> cls) {
        try {
            context.startActivity(new Intent(context, cls));
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean startActivity(Context context, Intent intent) {
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String saveBitmapToSDCard(Context context, Bitmap bitmap) {
        String str = null;
        if (context == null) {
            return null;
        }
        if (bitmap == null) {
            ToastUtil.showToast(context, R.string.share_save_bitmap_failed, true);
            return null;
        }
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            ToastUtil.showToast(context, R.string.share_save_bitmap_no_sdcard, true);
            return null;
        }
        File externalFilesDir = context.getExternalFilesDir(null);
        String absolutePath = externalFilesDir != null ? externalFilesDir.getAbsolutePath() : null;
        String packageName = context.getPackageName();
        if (!TextUtils.isEmpty(absolutePath)) {
            if (!absolutePath.endsWith(File.separator)) {
                str = absolutePath + File.separator + packageName + "_share_pic.png";
            } else {
                str = absolutePath + packageName + "_share_pic.png";
            }
            try {
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                ToastUtil.showToast(context, e.getMessage(), true);
            }
        }
        return str;
    }
}
