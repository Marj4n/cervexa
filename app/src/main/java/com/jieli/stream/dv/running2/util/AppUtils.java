package com.jieli.stream.dv.running2.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.AppInfo;
import com.jieli.stream.dv.running2.bean.DeviceDesc;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.ItemBean;
import com.jieli.stream.dv.running2.bean.SDFileInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.serenegiant.usb.UVCCamera;
import com.zh_jieli.juson.netcheck.OuterChecker;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class AppUtils implements IConstant {
    private static final String TAG = "AppUtils";
    private static long lastClickTime;

    public static int adjustRtsResolution(int i, int i2) {
        if (i == 1920 && i2 == 1080) {
            return 2;
        }
        return (i == 640 && i2 == 480) ? 0 : 1;
    }

    public static int[] getRtsResolution(int i) {
        int i2;
        int i3;
        int[] iArr = new int[2];
        if (2 == i) {
            i2 = 1920;
            i3 = 1080;
        } else if (i == 0) {
            i2 = UVCCamera.DEFAULT_PREVIEW_WIDTH;
            i3 = 480;
        } else {
            i2 = IConstant.RES_HD_WIDTH;
            i3 = IConstant.RES_HD_HEIGHT;
        }
        iArr[0] = i2;
        iArr[1] = i3;
        return iArr;
    }

    public static int getResourceId(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return 0;
        }
        return context.getResources().getIdentifier(str2, str, context.getPackageName());
    }

    public static int[] getWindowParams(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int[] iArr = new int[3];
        iArr[0] = (int) displayMetrics.density;
        iArr[1] = (int) ((displayMetrics.widthPixels * iArr[0]) + 0.5f);
        iArr[2] = (int) ((displayMetrics.heightPixels * iArr[0]) + 0.5f);
        return iArr;
    }

    public static int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static int dp2px(Context context, int i) {
        if (context == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }

    public static boolean isAppInBackground(Context context) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        boolean z = true;
        if (Build.VERSION.SDK_INT <= 20) {
            return !activityManager.getRunningTasks(1).get(0).topActivity.getPackageName().equals(context.getPackageName());
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.importance == 100) {
                for (String str : runningAppProcessInfo.pkgList) {
                    if (str.equals(context.getPackageName())) {
                        z = false;
                    }
                }
            }
        }
        return z;
    }

    public static boolean checkNetworkIsAvailable() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        hashMap.put(BaseFragment.DATA_NAME, "baidu.com");
        hashMap.put("host", "111.13.101.208");
        hashMap.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap);
        HashMap hashMap2 = new HashMap();
        hashMap2.put(BaseFragment.DATA_NAME, "baidu.com");
        hashMap2.put("host", "123.125.114.144");
        hashMap2.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap2);
        HashMap hashMap3 = new HashMap();
        hashMap3.put(BaseFragment.DATA_NAME, "baidu.com");
        hashMap3.put("host", "180.149.132.47");
        hashMap3.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap3);
        HashMap hashMap4 = new HashMap();
        hashMap4.put(BaseFragment.DATA_NAME, "baidu.com");
        hashMap4.put("host", "220.181.57.217");
        hashMap4.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap4);
        HashMap hashMap5 = new HashMap();
        hashMap5.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap5.put("host", "125.39.240.113");
        hashMap5.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap5);
        HashMap hashMap6 = new HashMap();
        hashMap6.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap6.put("host", "61.135.157.156");
        hashMap6.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        arrayList.add(hashMap6);
        return OuterChecker.check(arrayList, 1000);
    }

    public static String formatUrl(String str, int i, String str2) {
        if (TextUtils.isEmpty(str) || i <= 0) {
            return null;
        }
        String str3 = "http://" + str + ":" + i + "/";
        if (TextUtils.isEmpty(str2)) {
            return str3;
        }
        return str3 + str2;
    }

    public static boolean checkFileExist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new File(str).exists();
    }

    public static List<FileInfo> queryLocalFileList(String str) {
        FileInfo fileInfo;
        ArrayList arrayList;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        ArrayList arrayList2 = null;
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            File file = new File(str);
            if (!file.exists()) {
                return null;
            }
            ArrayList arrayList3 = new ArrayList();
            try {
                int i = 2;
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    if (listFiles != null && file.length() > 0) {
                        HashMap hashMap = new HashMap();
                        ArrayList arrayList4 = new ArrayList();
                        int length = listFiles.length;
                        int i2 = 0;
                        while (i2 < length) {
                            File file2 = listFiles[i2];
                            if (file2 == null || !file2.isFile()) {
                                arrayList = arrayList4;
                            } else {
                                ArrayList arrayList5 = arrayList4;
                                String format = simpleDateFormat.format(new Date(file2.lastModified()));
                                FileInfo fileInfo2 = new FileInfo();
                                if (file2.getName().contains(IConstant.SOS_PREFIX)) {
                                    fileInfo2.setType(i);
                                } else {
                                    fileInfo2.setType(1);
                                }
                                fileInfo2.setName(file2.getName());
                                fileInfo2.setPath(file2.getAbsolutePath());
                                fileInfo2.setSize(file2.length());
                                fileInfo2.setCreateTime(format);
                                fileInfo2.setVideo(judgeFileType(file2.getName()) == 2);
                                fileInfo2.setSource(1);
                                if (!TextUtils.isEmpty(fileInfo2.getPath()) && fileInfo2.getPath().contains(IConstant.DIR_REAR)) {
                                    fileInfo2.setCameraType("1");
                                } else {
                                    fileInfo2.setCameraType("0");
                                }
                                hashMap.put(file2.getName(), fileInfo2);
                                arrayList = arrayList5;
                                arrayList.add(file2.getName());
                            }
                            i2++;
                            arrayList4 = arrayList;
                            i = 2;
                        }
                        ArrayList<String> arrayList6 = arrayList4;
                        if (arrayList6.size() > 0) {
                            descSort(arrayList6);
                            for (String str2 : arrayList6) {
                                if (!TextUtils.isEmpty(str2) && (fileInfo = (FileInfo) hashMap.remove(str2)) != null) {
                                    arrayList3.add(fileInfo);
                                }
                            }
                        }
                    }
                } else {
                    String formatYMD_HMS = TimeFormate.formatYMD_HMS(file.lastModified());
                    if (!TextUtils.isEmpty(formatYMD_HMS)) {
                        FileInfo fileInfo3 = new FileInfo();
                        if (file.getName().contains(IConstant.SOS_PREFIX)) {
                            fileInfo3.setType(2);
                        } else {
                            fileInfo3.setType(1);
                        }
                        fileInfo3.setName(file.getName());
                        fileInfo3.setPath(file.getAbsolutePath());
                        fileInfo3.setSize(file.length());
                        fileInfo3.setCreateTime(formatYMD_HMS);
                        fileInfo3.setVideo(judgeFileType(file.getName()) == 2);
                        fileInfo3.setSource(1);
                        if (!TextUtils.isEmpty(fileInfo3.getPath()) && fileInfo3.getPath().contains(IConstant.DIR_REAR)) {
                            fileInfo3.setCameraType("1");
                        } else {
                            fileInfo3.setCameraType("0");
                        }
                        arrayList3.add(fileInfo3);
                    }
                }
                return arrayList3;
            } catch (Exception e) {
                e = e;
                arrayList2 = arrayList3;
                e.printStackTrace();
                return arrayList2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static void descSort(List<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        Collections.sort(list, new Comparator<String>() { // from class: com.jieli.stream.dv.running2.util.AppUtils.1
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                if (str.compareTo(str2) > 0) {
                    return -1;
                }
                return str.compareTo(str2) < 0 ? 1 : 0;
            }
        });
    }

    public static void descSortWay(List<FileInfo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        Collections.sort(list, new Comparator<FileInfo>() { // from class: com.jieli.stream.dv.running2.util.AppUtils.2
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // java.util.Comparator
            public int compare(FileInfo fileInfo, FileInfo fileInfo2) {
                String fileEndTime = fileInfo.getFileEndTime();
                String fileEndTime2 = fileInfo2.getFileEndTime();
                if (fileEndTime.compareTo(fileEndTime2) > 0) {
                    return -1;
                }
                return fileEndTime.compareTo(fileEndTime2) < 0 ? 1 : 0;
            }
        });
    }

    public static List<FileInfo> queryAllLocalFileList(String str, String str2) {
        List<FileInfo> queryAllLocalFileList;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        List arrayList = new ArrayList();
        if (file.isDirectory()) {
            if (str2.equals(file.getName())) {
                arrayList = queryLocalFileList(file.getAbsolutePath());
            } else {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (file2.isDirectory() && (queryAllLocalFileList = queryAllLocalFileList(file2.getAbsolutePath(), str2)) != null && queryAllLocalFileList.size() > 0) {
                            arrayList.addAll(queryAllLocalFileList);
                        }
                    }
                }
            }
        }
        if (arrayList.size() > 0) {
            descSortWay(arrayList);
        }
        return arrayList;
    }

    public static List<ItemBean> mergeList(List<ItemBean> list, List<ItemBean> list2) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list2 != null && list2.size() > 0) {
            if (list.size() == 0) {
                list.addAll(list2);
            } else {
                for (ItemBean itemBean : list2) {
                    String data = itemBean.getData();
                    boolean z = false;
                    if (data.compareTo(list.get(0).getData()) > 0) {
                        list.add(0, itemBean);
                    } else {
                        Iterator<ItemBean> it = list.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ItemBean next = it.next();
                            if (data.equals(next.getData())) {
                                z = true;
                                next.getInfoList().addAll(itemBean.getInfoList());
                                break;
                            }
                        }
                        if (!z) {
                            list.add(itemBean);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static List<ItemBean> convertDataList(List<FileInfo> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        descSortWay(list);
        TreeSet treeSet = new TreeSet(new Comparator<String>() { // from class: com.jieli.stream.dv.running2.util.AppUtils.3
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                if (str.compareTo(str2) > 0) {
                    return -1;
                }
                return str.compareTo(str2) < 0 ? 1 : 0;
            }
        });
        Iterator<FileInfo> it = list.iterator();
        while (it.hasNext()) {
            String subDateFlag = subDateFlag(it.next().getCreateTime());
            if (!TextUtils.isEmpty(subDateFlag)) {
                treeSet.add(subDateFlag);
            }
        }
        ArrayList arrayList = new ArrayList();
        if (treeSet.size() > 0) {
            Iterator it2 = treeSet.iterator();
            while (it2.hasNext()) {
                String str = (String) it2.next();
                ArrayList arrayList2 = new ArrayList();
                boolean z = false;
                for (FileInfo fileInfo : list) {
                    if (fileInfo != null) {
                        String createTime = fileInfo.getCreateTime();
                        if (TextUtils.isEmpty(createTime) || !createTime.startsWith(str)) {
                            if (z) {
                                break;
                            }
                        } else {
                            z = true;
                            arrayList2.add(fileInfo);
                        }
                    }
                }
                if (arrayList2.size() > 0) {
                    ItemBean itemBean = new ItemBean();
                    itemBean.setData(str);
                    itemBean.setInfoList(arrayList2);
                    arrayList.add(itemBean);
                }
            }
        }
        return arrayList;
    }

    private static String subDateFlag(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 8) {
            return null;
        }
        return str.substring(0, 8);
    }

    public static String formatDate(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 8) {
            return null;
        }
        return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
    }

    public static String splicingFilePath(String str, String str2, String str3, String str4) {
        if (!TextUtils.isEmpty(str)) {
            String str5 = ROOT_PATH;
            if (str.contains(File.separator)) {
                for (String str6 : str.split(File.separator)) {
                    if (!TextUtils.isEmpty(str6)) {
                        str5 = str5 + File.separator + str6;
                        File file = new File(str5);
                        if (!file.exists() && file.mkdir()) {
                            Dbug.w(TAG, "create root dir success! path : " + str5);
                        }
                    }
                }
            } else {
                str5 = str5 + File.separator + str;
                File file2 = new File(str5);
                if (!file2.exists() && file2.mkdir()) {
                    Dbug.w(TAG, "create root dir success! path : " + str5);
                }
            }
            if (TextUtils.isEmpty(str2)) {
                return str5;
            }
            String str7 = str5 + File.separator + str2;
            File file3 = new File(str7);
            if (!file3.exists() && file3.mkdir()) {
                Dbug.w(TAG, "create one dir success!");
            }
            if (TextUtils.isEmpty(str3)) {
                return str7;
            }
            String str8 = str7 + File.separator + str3;
            File file4 = new File(str8);
            if (!file4.exists() && file4.mkdir()) {
                Dbug.w(TAG, "create two dir success!");
            }
            if (TextUtils.isEmpty(str4)) {
                return str8;
            }
            String str9 = str8 + File.separator + str4;
            File file5 = new File(str9);
            if (!file5.exists() && file5.mkdir()) {
                Dbug.w(TAG, "create three sub dir success!");
            }
            return str9;
        }
        return ROOT_PATH;
    }

    public static int judgeFileType(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".JPEG") || str.endsWith(".jpeg") || str.endsWith(UVCCameraHelper.SUFFIX_JPEG) || str.endsWith(".JPG")) {
            return 1;
        }
        return (str.endsWith(".mov") || str.endsWith(".MOV") || str.endsWith(UVCCameraHelper.SUFFIX_MP4) || str.endsWith(".MP4") || str.endsWith(".avi") || str.endsWith(".AVI")) ? 2 : 0;
    }

    public static String getDownloadFilename(FileInfo fileInfo) {
        if (fileInfo == null) {
            return null;
        }
        String name = fileInfo.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (fileInfo.getSource() == 1) {
            return name;
        }
        String createTime = fileInfo.getCreateTime();
        if (name.contains(".")) {
            String[] split = name.split("\\.");
            if (split.length <= 1) {
                return null;
            }
            String str = split[split.length - 1];
            String str2 = "";
            for (int i = 0; i < split.length - 1; i++) {
                str2 = str2 + split[i];
            }
            return str2 + "_" + createTime + "." + str;
        }
        return name + "_" + createTime;
    }

    public static String getVideoThumbName(FileInfo fileInfo) {
        if (fileInfo == null) {
            return null;
        }
        String name = fileInfo.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        String createTime = fileInfo.getCreateTime();
        if (name.contains(".")) {
            String[] split = name.split("\\.");
            if (split.length <= 0) {
                return null;
            }
            String str = "";
            for (int i = 0; i < split.length - 1; i++) {
                str = str + split[i];
            }
            return fileInfo.isVideo() ? str + "_" + createTime + "_" + fileInfo.getDuration() + ".jpg" : str + "_" + createTime + ".jpg";
        }
        return name + "_" + createTime + UVCCameraHelper.SUFFIX_JPEG;
    }

    public static List<FileInfo> selectTypeList(List<FileInfo> list, int i) {
        if (list == null || list.size() == 0) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (FileInfo fileInfo : list) {
            if (fileInfo != null) {
                if (i == 0) {
                    arrayList.add(fileInfo);
                } else if (judgeFileType(fileInfo.getName()) == i) {
                    arrayList.add(fileInfo);
                }
            }
        }
        return arrayList;
    }

    public static boolean bitmapToFile(Bitmap bitmap, String str, int i) {
        FileOutputStream fileOutputStream;
        boolean z = false;
        if (bitmap == null || TextUtils.isEmpty(str)) {
            return false;
        }
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream(str);
                } catch (IOException e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, i, fileOutputStream);
            z = true;
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e3) {
            e = e3;
            fileOutputStream2 = fileOutputStream;
            e.printStackTrace();
            if (fileOutputStream2 != null) {
                fileOutputStream2.flush();
                fileOutputStream2.close();
            }
            return z;
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
        return z;
    }

    public static boolean bytesToFile(byte[] bArr, String str) {
        FileOutputStream fileOutputStream;
        if (bArr == null || TextUtils.isEmpty(str)) {
            return false;
        }
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(str);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            fileOutputStream.write(bArr);
            try {
                fileOutputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return true;
        } catch (IOException e3) {
            e = e3;
            fileOutputStream2 = fileOutputStream;
            e.printStackTrace();
            if (fileOutputStream2 == null) {
                return false;
            }
            try {
                fileOutputStream2.close();
                return false;
            } catch (IOException e4) {
                e4.printStackTrace();
                return false;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0077 A[Catch: Exception -> 0x012a, TRY_ENTER, TryCatch #0 {Exception -> 0x012a, blocks: (B:5:0x0006, B:10:0x001f, B:11:0x011c, B:13:0x0126, B:18:0x0035, B:20:0x003f, B:23:0x0048, B:25:0x0050, B:28:0x0059, B:30:0x0061, B:32:0x0071, B:35:0x0077, B:36:0x00ad, B:40:0x00d6, B:43:0x00dc, B:44:0x0103), top: B:4:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00ad A[Catch: Exception -> 0x012a, TryCatch #0 {Exception -> 0x012a, blocks: (B:5:0x0006, B:10:0x001f, B:11:0x011c, B:13:0x0126, B:18:0x0035, B:20:0x003f, B:23:0x0048, B:25:0x0050, B:28:0x0059, B:30:0x0061, B:32:0x0071, B:35:0x0077, B:36:0x00ad, B:40:0x00d6, B:43:0x00dc, B:44:0x0103), top: B:4:0x0006 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void browseFileWithOther(com.jieli.stream.dv.running2.ui.base.BaseActivity r9, java.lang.String r10, java.lang.String r11) {
        /*
            Method dump skipped, instructions count: 327
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AppUtils.browseFileWithOther(com.jieli.stream.dv.running2.ui.base.BaseActivity, java.lang.String, java.lang.String):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0042 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getFromRaw(android.content.Context r3, int r4) {
        /*
            r0 = 0
            if (r3 != 0) goto L4
            return r0
        L4:
            android.content.res.Resources r3 = r3.getResources()     // Catch: java.lang.Throwable -> L31 java.lang.Exception -> L33
            java.io.InputStream r3 = r3.openRawResource(r4)     // Catch: java.lang.Throwable -> L31 java.lang.Exception -> L33
            r4 = 0
            if (r3 == 0) goto L16
            int r4 = r3.available()     // Catch: java.lang.Exception -> L14 java.lang.Throwable -> L3e
            goto L16
        L14:
            r4 = move-exception
            goto L35
        L16:
            if (r4 <= 0) goto L26
            byte[] r1 = new byte[r4]     // Catch: java.lang.Exception -> L14 java.lang.Throwable -> L3e
            int r2 = r3.read(r1)     // Catch: java.lang.Exception -> L14 java.lang.Throwable -> L3e
            if (r2 < r4) goto L26
            java.lang.String r4 = new java.lang.String     // Catch: java.lang.Exception -> L14 java.lang.Throwable -> L3e
            r4.<init>(r1)     // Catch: java.lang.Exception -> L14 java.lang.Throwable -> L3e
            r0 = r4
        L26:
            if (r3 == 0) goto L3d
            r3.close()     // Catch: java.io.IOException -> L2c
            goto L3d
        L2c:
            r3 = move-exception
            r3.printStackTrace()
            goto L3d
        L31:
            r4 = move-exception
            goto L40
        L33:
            r4 = move-exception
            r3 = r0
        L35:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> L3e
            if (r3 == 0) goto L3d
            r3.close()     // Catch: java.io.IOException -> L2c
        L3d:
            return r0
        L3e:
            r4 = move-exception
            r0 = r3
        L40:
            if (r0 == 0) goto L4a
            r0.close()     // Catch: java.io.IOException -> L46
            goto L4a
        L46:
            r3 = move-exception
            r3.printStackTrace()
        L4a:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AppUtils.getFromRaw(android.content.Context, int):java.lang.String");
    }

    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (file.delete()) {
                Dbug.i(TAG, "delete file success!");
                return;
            }
            return;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (file.delete()) {
                    Dbug.i(TAG, "delete empty file success!");
                    return;
                }
                return;
            }
            for (File file2 : listFiles) {
                deleteFile(file2);
            }
            if (file.delete()) {
                Dbug.i(TAG, "delete empty file success!");
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String readTxtFile(java.lang.String r8) {
        /*
            java.lang.String r0 = " IOException : "
            java.lang.String r1 = "AppUtils"
            java.lang.String r2 = ""
            if (r8 == 0) goto Lcd
            boolean r3 = r8.isEmpty()
            if (r3 == 0) goto L10
            goto Lcd
        L10:
            r3 = 0
            java.lang.String r4 = "UTF-8"
            java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            r5.<init>(r8)     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            boolean r8 = r5.isFile()     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            if (r8 == 0) goto L55
            boolean r8 = r5.exists()     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            if (r8 == 0) goto L55
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            r6.<init>(r5)     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            r8.<init>(r6, r4)     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r3.<init>(r8)     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
        L33:
            java.lang.String r4 = r3.readLine()     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            if (r4 == 0) goto L4e
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r5.<init>()     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r5.append(r2)     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r5.append(r4)     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r4 = 10
            r5.append(r4)     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            java.lang.String r2 = r5.toString()     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            goto L33
        L4e:
            r8.close()     // Catch: java.lang.Exception -> L53 java.lang.Throwable -> Laa
            r3 = r8
            goto L5a
        L53:
            r3 = move-exception
            goto L81
        L55:
            java.lang.String r8 = "Cannot find the specified file"
            com.jieli.stream.dv.running2.util.Dbug.e(r1, r8)     // Catch: java.lang.Throwable -> L7b java.lang.Exception -> L7d
        L5a:
            if (r3 == 0) goto La9
            r3.close()     // Catch: java.io.IOException -> L60
            goto La9
        L60:
            r8 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
        L66:
            r3.append(r0)
            java.lang.String r0 = r8.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            com.jieli.stream.dv.running2.util.Dbug.e(r1, r0)
            r8.printStackTrace()
            goto La9
        L7b:
            r2 = move-exception
            goto Lac
        L7d:
            r8 = move-exception
            r7 = r3
            r3 = r8
            r8 = r7
        L81:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laa
            r4.<init>()     // Catch: java.lang.Throwable -> Laa
            java.lang.String r5 = " err : "
            r4.append(r5)     // Catch: java.lang.Throwable -> Laa
            java.lang.String r5 = r3.getMessage()     // Catch: java.lang.Throwable -> Laa
            r4.append(r5)     // Catch: java.lang.Throwable -> Laa
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Laa
            com.jieli.stream.dv.running2.util.Dbug.e(r1, r4)     // Catch: java.lang.Throwable -> Laa
            r3.printStackTrace()     // Catch: java.lang.Throwable -> Laa
            if (r8 == 0) goto La9
            r8.close()     // Catch: java.io.IOException -> La2
            goto La9
        La2:
            r8 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            goto L66
        La9:
            return r2
        Laa:
            r2 = move-exception
            r3 = r8
        Lac:
            if (r3 == 0) goto Lcc
            r3.close()     // Catch: java.io.IOException -> Lb2
            goto Lcc
        Lb2:
            r8 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = r8.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            com.jieli.stream.dv.running2.util.Dbug.e(r1, r0)
            r8.printStackTrace()
        Lcc:
            throw r2
        Lcd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AppUtils.readTxtFile(java.lang.String):java.lang.String");
    }

    public static AppInfo parseApplicationMsg(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        AppInfo appInfo = new AppInfo();
        appInfo.setAppName(MainApplication.getApplication().getAppName());
        appInfo.setAppPlatform("0");
        appInfo.setAppVersion(MainApplication.getApplication().getAppVersion());
        ArrayList arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has(IConstant.DEV_TYPE)) {
                String string = jSONObject.getString(IConstant.DEV_TYPE);
                if (!TextUtils.isEmpty(string)) {
                    JSONArray jSONArray = new JSONArray(string);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string2 = jSONArray.getString(i);
                        if (!TextUtils.isEmpty(string2)) {
                            arrayList.add(string2);
                        }
                    }
                    if (arrayList.size() > 0) {
                        appInfo.setDev_type(arrayList);
                    }
                }
            }
            if (jSONObject.has(IConstant.DEV_LIST)) {
                JSONObject jSONObject2 = new JSONObject(jSONObject.getString(IConstant.DEV_LIST));
                if (arrayList.size() > 0) {
                    HashMap hashMap = new HashMap();
                    for (String str2 : arrayList) {
                        if (!TextUtils.isEmpty(str2) && jSONObject2.has(str2)) {
                            String string3 = jSONObject2.getString(str2);
                            if (!TextUtils.isEmpty(string3)) {
                                JSONArray jSONArray2 = new JSONArray(string3);
                                ArrayList arrayList2 = new ArrayList();
                                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                                    String string4 = jSONArray2.getString(i2);
                                    if (!TextUtils.isEmpty(string4)) {
                                        arrayList2.add(string4);
                                    }
                                }
                                if (arrayList2.size() > 0) {
                                    Collections.sort(arrayList2, new Comparator<String>() { // from class: com.jieli.stream.dv.running2.util.AppUtils.4
                                        @Override // java.util.Comparator
                                        public int compare(String str3, String str4) {
                                            if (str3.compareTo(str4) > 0) {
                                                return -1;
                                            }
                                            return str3.compareTo(str4) < 0 ? 1 : 0;
                                        }
                                    });
                                    hashMap.put(str2, arrayList2);
                                }
                            }
                        }
                    }
                    if (hashMap.size() > 0) {
                        appInfo.setDev_list(hashMap);
                    }
                }
            }
            return appInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DeviceDesc parseDeviceDescTxt(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DeviceDesc deviceDesc = new DeviceDesc();
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("uuid")) {
                String string = jSONObject.getString("uuid");
                if (!TextUtils.isEmpty(string)) {
                    deviceDesc.setUuid(string);
                }
            }
            if (jSONObject.has(IConstant.DEV_PRODUCT)) {
                String string2 = jSONObject.getString(IConstant.DEV_PRODUCT);
                if (!TextUtils.isEmpty(string2)) {
                    deviceDesc.setProduct_type(string2);
                }
            }
            if (jSONObject.has(IConstant.DEV_MATCH_APP)) {
                String string3 = jSONObject.getString(IConstant.DEV_MATCH_APP);
                if (!TextUtils.isEmpty(string3)) {
                    deviceDesc.setMatch_app_type(string3);
                }
            }
            if (jSONObject.has(IConstant.DEV_FIRMWARE_VERSION)) {
                String string4 = jSONObject.getString(IConstant.DEV_FIRMWARE_VERSION);
                if (!TextUtils.isEmpty(string4)) {
                    deviceDesc.setFirmware_version(string4);
                }
            }
            if (jSONObject.has(IConstant.DEV_DEVICE_TYPE)) {
                String string5 = jSONObject.getString(IConstant.DEV_DEVICE_TYPE);
                if (!TextUtils.isEmpty(string5)) {
                    deviceDesc.setDevice_type(string5);
                }
            }
            if (jSONObject.has(IConstant.DEV_SUPPORT_BUMPING)) {
                if ("1".equals(jSONObject.getString(IConstant.DEV_SUPPORT_BUMPING))) {
                    deviceDesc.setSupport_bumping(true);
                } else {
                    deviceDesc.setSupport_bumping(false);
                }
            }
            if (jSONObject.has(IConstant.DEV_RTS_TYPE)) {
                if ("0".equals(jSONObject.getString(IConstant.DEV_RTS_TYPE))) {
                    deviceDesc.setVideoType(0);
                } else {
                    deviceDesc.setVideoType(1);
                }
            }
            if (jSONObject.has(IConstant.DEV_RTS_NET_TYPE)) {
                String string6 = jSONObject.getString(IConstant.DEV_RTS_NET_TYPE);
                if ("0".equals(string6)) {
                    deviceDesc.setNetMode(0);
                } else if ("1".equals(string6)) {
                    deviceDesc.setNetMode(1);
                }
            }
            if (jSONObject.has(IConstant.DEV_SUPPORT_PROJECTION)) {
                if ("1".equals(jSONObject.getString(IConstant.DEV_SUPPORT_PROJECTION))) {
                    deviceDesc.setSupport_projection(true);
                } else {
                    deviceDesc.setSupport_projection(false);
                }
            }
            if (jSONObject.has(IConstant.DEV_FRONT_SUPPORT)) {
                String string7 = jSONObject.getString(IConstant.DEV_FRONT_SUPPORT);
                if (!TextUtils.isEmpty(string7)) {
                    JSONArray jSONArray = new JSONArray(string7);
                    String[] strArr = new String[jSONArray.length()];
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string8 = jSONArray.getString(i);
                        if (!TextUtils.isEmpty(string8)) {
                            strArr[i] = string8;
                        }
                    }
                    deviceDesc.setFront_support(strArr);
                }
            }
            if (jSONObject.has(IConstant.DEV_REAR_SUPPORT)) {
                String string9 = jSONObject.getString(IConstant.DEV_REAR_SUPPORT);
                if (!TextUtils.isEmpty(string9)) {
                    JSONArray jSONArray2 = new JSONArray(string9);
                    String[] strArr2 = new String[jSONArray2.length()];
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        String string10 = jSONArray2.getString(i2);
                        if (!TextUtils.isEmpty(string10)) {
                            strArr2[i2] = string10;
                        }
                    }
                    deviceDesc.setRear_support(strArr2);
                }
            }
            if (jSONObject.has(IConstant.DEV_RTSP_FRONT_SUPPORT)) {
                String string11 = jSONObject.getString(IConstant.DEV_RTSP_FRONT_SUPPORT);
                if (!TextUtils.isEmpty(string11)) {
                    JSONArray jSONArray3 = new JSONArray(string11);
                    String[] strArr3 = new String[jSONArray3.length()];
                    for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                        String string12 = jSONArray3.getString(i3);
                        if (!TextUtils.isEmpty(string12)) {
                            strArr3[i3] = string12;
                        }
                    }
                    deviceDesc.setRtspFrontSupport(strArr3);
                }
            }
            if (jSONObject.has(IConstant.DEV_RTSP_REAR_SUPPORT)) {
                String string13 = jSONObject.getString(IConstant.DEV_RTSP_REAR_SUPPORT);
                if (!TextUtils.isEmpty(string13)) {
                    JSONArray jSONArray4 = new JSONArray(string13);
                    String[] strArr4 = new String[jSONArray4.length()];
                    for (int i4 = 0; i4 < jSONArray4.length(); i4++) {
                        String string14 = jSONArray4.getString(i4);
                        if (!TextUtils.isEmpty(string14)) {
                            strArr4[i4] = string14;
                        }
                    }
                    deviceDesc.setRtspRearSupport(strArr4);
                }
            }
            if (jSONObject.has(IConstant.DEV_REC_FRONT_SUPPORT)) {
                String string15 = jSONObject.getString(IConstant.DEV_REC_FRONT_SUPPORT);
                if (!TextUtils.isEmpty(string15)) {
                    JSONArray jSONArray5 = new JSONArray(string15);
                    String[] strArr5 = new String[jSONArray5.length()];
                    for (int i5 = 0; i5 < jSONArray5.length(); i5++) {
                        String string16 = jSONArray5.getString(i5);
                        if (!TextUtils.isEmpty(string16)) {
                            strArr5[i5] = string16;
                        }
                    }
                    deviceDesc.setRecordFrontSupport(strArr5);
                }
            }
            if (jSONObject.has(IConstant.DEV_REC_REAR_SUPPORT)) {
                String string17 = jSONObject.getString(IConstant.DEV_REC_REAR_SUPPORT);
                if (!TextUtils.isEmpty(string17)) {
                    JSONArray jSONArray6 = new JSONArray(string17);
                    String[] strArr6 = new String[jSONArray6.length()];
                    for (int i6 = 0; i6 < jSONArray6.length(); i6++) {
                        String string18 = jSONArray6.getString(i6);
                        if (!TextUtils.isEmpty(string18)) {
                            strArr6[i6] = string18;
                        }
                    }
                    deviceDesc.setRecordRearSupport(strArr6);
                }
            }
            if (!jSONObject.has(IConstant.DEV_APP_LIST)) {
                return deviceDesc;
            }
            String string19 = jSONObject.getString(IConstant.DEV_APP_LIST);
            if (TextUtils.isEmpty(string19)) {
                return deviceDesc;
            }
            JSONObject jSONObject2 = new JSONObject(string19);
            if (!jSONObject2.has(IConstant.DEV_MATCH_ANDROID_VER)) {
                return deviceDesc;
            }
            String string20 = jSONObject2.getString(IConstant.DEV_MATCH_ANDROID_VER);
            if (TextUtils.isEmpty(string20)) {
                return deviceDesc;
            }
            JSONArray jSONArray7 = new JSONArray(string20);
            ArrayList arrayList = new ArrayList();
            for (int i7 = 0; i7 < jSONArray7.length(); i7++) {
                String string21 = jSONArray7.getString(i7);
                if (!TextUtils.isEmpty(string21)) {
                    arrayList.add(string21);
                }
            }
            if (arrayList.size() <= 0) {
                return deviceDesc;
            }
            Collections.sort(arrayList, new Comparator<String>() { // from class: com.jieli.stream.dv.running2.util.AppUtils.5
                @Override // java.util.Comparator
                public int compare(String str2, String str3) {
                    if (str2.compareTo(str3) > 0) {
                        return -1;
                    }
                    return str2.compareTo(str3) < 0 ? 1 : 0;
                }
            });
            DeviceDesc.AppListBean appListBean = new DeviceDesc.AppListBean();
            appListBean.setMatch_android_ver(arrayList);
            deviceDesc.setApp_list(appListBean);
            return deviceDesc;
        } catch (JSONException e) {
            e.printStackTrace();
            return deviceDesc;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.jieli.stream.dv.running2.bean.ServerInfo parseServerTxtInfo(java.lang.String r9, java.lang.String r10) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            r1 = 0
            if (r0 != 0) goto L8b
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: org.json.JSONException -> L78
            r2.<init>(r9)     // Catch: org.json.JSONException -> L78
            java.util.Iterator r9 = r2.keys()     // Catch: org.json.JSONException -> L78
            if (r9 == 0) goto L7c
        L17:
            boolean r3 = r9.hasNext()     // Catch: org.json.JSONException -> L78
            if (r3 == 0) goto L7c
            java.lang.Object r3 = r9.next()     // Catch: org.json.JSONException -> L78
            java.lang.String r3 = (java.lang.String) r3     // Catch: org.json.JSONException -> L78
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch: org.json.JSONException -> L78
            if (r4 != 0) goto L17
            java.lang.String r4 = r2.getString(r3)     // Catch: org.json.JSONException -> L78
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: org.json.JSONException -> L78
            if (r5 != 0) goto L17
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch: org.json.JSONException -> L78
            r5.<init>()     // Catch: org.json.JSONException -> L78
            org.json.JSONArray r6 = new org.json.JSONArray     // Catch: org.json.JSONException -> L78
            r6.<init>(r4)     // Catch: org.json.JSONException -> L78
            r4 = 0
        L3e:
            int r7 = r6.length()     // Catch: org.json.JSONException -> L78
            if (r4 >= r7) goto L66
            java.lang.String r7 = r6.getString(r4)     // Catch: org.json.JSONException -> L78
            boolean r8 = android.text.TextUtils.isEmpty(r7)     // Catch: org.json.JSONException -> L78
            if (r8 != 0) goto L63
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Exception -> L57 org.json.JSONException -> L78
            int r7 = r7.intValue()     // Catch: java.lang.Exception -> L57 org.json.JSONException -> L78
            goto L5c
        L57:
            r7 = move-exception
            r7.printStackTrace()     // Catch: org.json.JSONException -> L78
            r7 = 0
        L5c:
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: org.json.JSONException -> L78
            r5.add(r7)     // Catch: org.json.JSONException -> L78
        L63:
            int r4 = r4 + 1
            goto L3e
        L66:
            int r4 = r5.size()     // Catch: org.json.JSONException -> L78
            if (r4 <= 0) goto L17
            com.jieli.stream.dv.running2.util.AppUtils$6 r4 = new com.jieli.stream.dv.running2.util.AppUtils$6     // Catch: org.json.JSONException -> L78
            r4.<init>()     // Catch: org.json.JSONException -> L78
            java.util.Collections.sort(r5, r4)     // Catch: org.json.JSONException -> L78
            r0.put(r3, r5)     // Catch: org.json.JSONException -> L78
            goto L17
        L78:
            r9 = move-exception
            r9.printStackTrace()
        L7c:
            int r9 = r0.size()
            if (r9 <= 0) goto L8b
            com.jieli.stream.dv.running2.bean.ServerInfo r9 = new com.jieli.stream.dv.running2.bean.ServerInfo
            r9.<init>()
            r9.setAndroidVersionMap(r0)
            goto L8c
        L8b:
            r9 = 0
        L8c:
            boolean r0 = android.text.TextUtils.isEmpty(r10)
            if (r0 != 0) goto L105
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: org.json.JSONException -> Lf1
            r2.<init>(r10)     // Catch: org.json.JSONException -> Lf1
            java.util.Iterator r10 = r2.keys()     // Catch: org.json.JSONException -> Lf1
            if (r10 == 0) goto Lf5
        La2:
            boolean r3 = r10.hasNext()     // Catch: org.json.JSONException -> Lf1
            if (r3 == 0) goto Lf5
            java.lang.Object r3 = r10.next()     // Catch: org.json.JSONException -> Lf1
            java.lang.String r3 = (java.lang.String) r3     // Catch: org.json.JSONException -> Lf1
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch: org.json.JSONException -> Lf1
            if (r4 != 0) goto La2
            java.lang.String r4 = r2.getString(r3)     // Catch: org.json.JSONException -> Lf1
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: org.json.JSONException -> Lf1
            if (r5 != 0) goto La2
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch: org.json.JSONException -> Lf1
            r5.<init>()     // Catch: org.json.JSONException -> Lf1
            org.json.JSONArray r6 = new org.json.JSONArray     // Catch: org.json.JSONException -> Lf1
            r6.<init>(r4)     // Catch: org.json.JSONException -> Lf1
            r4 = 0
        Lc9:
            int r7 = r6.length()     // Catch: org.json.JSONException -> Lf1
            if (r4 >= r7) goto Ldf
            java.lang.String r7 = r6.getString(r4)     // Catch: org.json.JSONException -> Lf1
            boolean r8 = android.text.TextUtils.isEmpty(r7)     // Catch: org.json.JSONException -> Lf1
            if (r8 != 0) goto Ldc
            r5.add(r7)     // Catch: org.json.JSONException -> Lf1
        Ldc:
            int r4 = r4 + 1
            goto Lc9
        Ldf:
            int r4 = r5.size()     // Catch: org.json.JSONException -> Lf1
            if (r4 <= 0) goto La2
            com.jieli.stream.dv.running2.util.AppUtils$7 r4 = new com.jieli.stream.dv.running2.util.AppUtils$7     // Catch: org.json.JSONException -> Lf1
            r4.<init>()     // Catch: org.json.JSONException -> Lf1
            java.util.Collections.sort(r5, r4)     // Catch: org.json.JSONException -> Lf1
            r0.put(r3, r5)     // Catch: org.json.JSONException -> Lf1
            goto La2
        Lf1:
            r10 = move-exception
            r10.printStackTrace()
        Lf5:
            int r10 = r0.size()
            if (r10 <= 0) goto L105
            if (r9 != 0) goto L102
            com.jieli.stream.dv.running2.bean.ServerInfo r9 = new com.jieli.stream.dv.running2.bean.ServerInfo
            r9.<init>()
        L102:
            r9.setFirmwareVersionMap(r0)
        L105:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AppUtils.parseServerTxtInfo(java.lang.String, java.lang.String):com.jieli.stream.dv.running2.bean.ServerInfo");
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x01ad, code lost:
    
        if (r15.equals(r3) != false) goto L63;
     */
    /* JADX WARN: Removed duplicated region for block: B:61:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02d3 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String checkUpdateFilePath(android.content.Context r14, int r15) {
        /*
            Method dump skipped, instructions count: 724
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AppUtils.checkUpdateFilePath(android.content.Context, int):java.lang.String");
    }

    public static void setScreenManualMode(Activity activity) {
        setScreenMode(activity, 0);
    }

    public static void setScreenMode(Activity activity, int i) {
        if (activity != null) {
            ContentResolver contentResolver = activity.getContentResolver();
            int screenMode = getScreenMode(activity);
            if (screenMode == -1 || screenMode == i) {
                return;
            }
            Settings.System.putInt(contentResolver, "screen_brightness_mode", i);
        }
    }

    public static int getScreenMode(Activity activity) {
        if (activity != null) {
            try {
                return Settings.System.getInt(activity.getContentResolver(), "screen_brightness_mode");
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static int getScreenBrightness(Activity activity) {
        return activity != null ? Settings.System.getInt(activity.getContentResolver(), "screen_brightness", FTPReply.DATA_CONNECTION_ALREADY_OPEN) : FTPReply.DATA_CONNECTION_ALREADY_OPEN;
    }

    public static void setBrightness(Activity activity, int i) {
        Window window;
        WindowManager.LayoutParams attributes;
        if (activity == null || (window = activity.getWindow()) == null || (attributes = window.getAttributes()) == null) {
            return;
        }
        attributes.screenBrightness = i / 255.0f;
        window.setAttributes(attributes);
        Settings.System.putInt(activity.getContentResolver(), "screen_brightness", i);
    }

    public static Context changeAppLanguage(Context context, String str) {
        if (context == null) {
            Dbug.e(TAG, "context is null");
            return null;
        }
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale language = LangUtil.getLanguage(str);
        if (language != null) {
            Dbug.i(TAG, "changeAppLanguage index=" + str + ", " + language);
            if (Build.VERSION.SDK_INT >= 24) {
                configuration.setLocale(language);
                configuration.setLocales(new LocaleList(language));
                return context.createConfigurationContext(configuration);
            }
            if (Build.VERSION.SDK_INT >= 17) {
                configuration.setLocale(language);
            } else {
                configuration.locale = language;
            }
            resources.updateConfiguration(configuration, displayMetrics);
        } else {
            Dbug.e(TAG, "No language match:" + str);
        }
        return context;
    }

    public static long getAvailableExternalMemorySize() {
        long blockSize;
        long availableBlocks;
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    public static long getExternalMemorySize() {
        long blockSize;
        long blockCount;
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            blockCount = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            blockCount = statFs.getBlockCount();
        }
        return blockSize * blockCount;
    }

    public static String getExternalMemorySize(Context context) {
        return Formatter.formatFileSize(context, getExternalMemorySize());
    }

    public static String getAvailableExternalMemorySize(Context context) {
        return Formatter.formatFileSize(context, getAvailableExternalMemorySize());
    }

    public static Bitmap scaleImage(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static ArrayList<SDFileInfo> getFiles(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null) {
            return null;
        }
        ArrayList<SDFileInfo> arrayList = new ArrayList<>();
        for (File file : listFiles) {
            SDFileInfo sDFileInfo = new SDFileInfo();
            sDFileInfo.Name = file.getName();
            sDFileInfo.IsDirectory = file.isDirectory();
            sDFileInfo.Path = file.getPath();
            sDFileInfo.Size = file.length();
            arrayList.add(sDFileInfo);
        }
        Collections.sort(arrayList, new FileComparator());
        return arrayList;
    }

    static class HiddenFilter implements FileFilter {
        HiddenFilter() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return !file.isHidden();
        }
    }

    public static ArrayList<SDFileInfo> getFirmwareFile(String str) {
        File[] listFiles = new File(str).listFiles(new HiddenFilter());
        if (listFiles == null) {
            return null;
        }
        ArrayList<SDFileInfo> arrayList = new ArrayList<>();
        for (File file : listFiles) {
            String name = file.getName();
            if (file.isDirectory() || name.endsWith(".bfu") || name.endsWith(".BFU") || name.endsWith(".BIN") || name.endsWith(".bin")) {
                SDFileInfo sDFileInfo = new SDFileInfo();
                sDFileInfo.Name = file.getName();
                sDFileInfo.IsDirectory = file.isDirectory();
                sDFileInfo.Path = file.getPath();
                sDFileInfo.Size = file.length();
                arrayList.add(sDFileInfo);
            }
        }
        Collections.sort(arrayList, new FileComparator());
        return arrayList;
    }

    public static boolean checkIsEmptyFolder(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        File[] listFiles = file.listFiles();
        return listFiles == null || listFiles.length == 0;
    }

    public static boolean isFastDoubleClick(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        long j = lastClickTime;
        if (j == 0) {
            lastClickTime = currentTimeMillis;
            return false;
        }
        if (currentTimeMillis - j >= i) {
            lastClickTime = currentTimeMillis;
            return false;
        }
        lastClickTime = currentTimeMillis;
        return true;
    }

    public static String getCrashVideoName() {
        return "EME_" + Calendar.getInstance().getTimeInMillis() + ".mov";
    }

    public static String createFilenameWidthTime(int i, String str) {
        String str2;
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (i != 1) {
            if (i == 2) {
                str2 = IConstant.SOS_PREFIX;
                return str2 + "_" + timeInMillis + str;
            }
            if (i != 3) {
                Dbug.e(TAG, "Invalid type=" + i);
                throw new IllegalArgumentException("Invalid type:" + i);
            }
        }
        str2 = IConstant.REC_PREFIX;
        return str2 + "_" + timeInMillis + str;
    }

    public static String getRecordVideoName() {
        return "REC_" + Calendar.getInstance().getTimeInMillis() + ".mov";
    }

    public static String getRecordAviName() {
        return "REC_" + Calendar.getInstance().getTimeInMillis() + ".avi";
    }

    public static String getLocalPhotoName() {
        return "JPG_" + Calendar.getInstance().getTimeInMillis() + UVCCameraHelper.SUFFIX_JPEG;
    }

    public static List<String> queryThumbDirPath(String str) {
        List<String> queryThumbDirPath;
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists()) {
                ArrayList arrayList = new ArrayList();
                if (!file.isDirectory()) {
                    return arrayList;
                }
                if (IConstant.DIR_THUMB.equals(file.getName())) {
                    arrayList.add(file.getAbsolutePath());
                    return arrayList;
                }
                File[] listFiles = file.listFiles();
                if (listFiles == null || listFiles.length <= 0) {
                    return arrayList;
                }
                for (File file2 : listFiles) {
                    if (file2.isDirectory() && (queryThumbDirPath = queryThumbDirPath(file2.getAbsolutePath())) != null && queryThumbDirPath.size() > 0) {
                        arrayList.addAll(queryThumbDirPath);
                    }
                }
                return arrayList;
            }
        }
        return null;
    }

    public static List<FileInfo> queryThumbInfoList(String str) {
        List<String> queryThumbDirPath;
        if (str == null || (queryThumbDirPath = queryThumbDirPath(str)) == null || queryThumbDirPath.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = queryThumbDirPath.iterator();
        while (it.hasNext()) {
            List<FileInfo> queryLocalFileList = queryLocalFileList(it.next());
            if (queryLocalFileList != null) {
                arrayList.addAll(queryLocalFileList);
            }
        }
        if (arrayList.size() <= 0) {
            return arrayList;
        }
        descSortWay(arrayList);
        return arrayList;
    }

    public static String queryThumbPath(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf != -1) {
                str = str.substring(0, lastIndexOf);
            }
            List<FileInfo> queryThumbInfoList = queryThumbInfoList(str2);
            if (queryThumbInfoList != null) {
                Iterator<FileInfo> it = queryThumbInfoList.iterator();
                while (it.hasNext()) {
                    String path = it.next().getPath();
                    if (!TextUtils.isEmpty(path) && path.contains(str)) {
                        return path;
                    }
                }
            }
        }
        return "";
    }

    public static int parseThumbPathForDuration(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf != -1) {
            str = str.substring(0, lastIndexOf);
        }
        if (!str.contains("_")) {
            return 0;
        }
        String[] split = str.split("_");
        if (split.length <= 1) {
            return 0;
        }
        try {
            return Integer.valueOf(split[split.length - 1]).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getFolderSize(File file) throws Exception {
        long length;
        long j = 0;
        if (file != null && file.exists()) {
            try {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file2 : listFiles) {
                        if (file2.isDirectory()) {
                            length = getFolderSize(file2);
                        } else {
                            length = file2.length();
                        }
                        j += length;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return j;
    }

    public static String getFormatSize(double d) {
        double d2 = d / 1024.0d;
        if (d2 < 1.0d) {
            return d + "Byte";
        }
        double d3 = d2 / 1024.0d;
        if (d3 < 1.0d) {
            return new BigDecimal(Double.toString(d2)).setScale(2, 4).toPlainString() + "KB";
        }
        double d4 = d3 / 1024.0d;
        if (d4 < 1.0d) {
            return new BigDecimal(Double.toString(d3)).setScale(2, 4).toPlainString() + "MB";
        }
        double d5 = d4 / 1024.0d;
        if (d5 < 1.0d) {
            return new BigDecimal(Double.toString(d4)).setScale(2, 4).toPlainString() + "GB";
        }
        return new BigDecimal(d5).setScale(2, 4).toPlainString() + "TB";
    }

    public static String checkCameraDir(FileInfo fileInfo) {
        return (fileInfo == null || !"1".equals(fileInfo.getCameraType())) ? IConstant.DIR_FRONT : IConstant.DIR_REAR;
    }

    public static int checkFrameType(byte[] bArr) {
        if (bArr != null && bArr.length > 5) {
            byte[] bArr2 = new byte[5];
            System.arraycopy(bArr, 0, bArr2, 0, 5);
            if (bArr2[0] == 0 && bArr2[1] == 0) {
                if (bArr2[2] == 1) {
                    if (bArr2[3] != 103) {
                        if (bArr2[3] == 65) {
                            return IConstant.FRAME_TYPE_P;
                        }
                    }
                    return IConstant.FRAME_TYPE_I;
                }
                if (bArr2[2] == 0 && bArr2[3] == 1) {
                    if (bArr2[4] != 103) {
                        if (bArr2[4] == 65) {
                            return IConstant.FRAME_TYPE_P;
                        }
                    }
                    return IConstant.FRAME_TYPE_I;
                }
            }
        }
        return 0;
    }

    public static int getStreamResolutionLevel() {
        int i;
        String[] rear_support;
        MainApplication application = MainApplication.getApplication();
        boolean z = true;
        if (application.getDeviceSettingInfo().getCameraType() == 1) {
            i = PreferencesHelper.getSharedPreferences(application).getInt(IConstant.KEY_FRONT_RES_LEVEL, 1);
            rear_support = application.getDeviceDesc().getFront_support();
        } else {
            i = PreferencesHelper.getSharedPreferences(application).getInt(IConstant.KEY_REAR_RES_LEVEL, 1);
            rear_support = application.getDeviceDesc().getRear_support();
        }
        if (rear_support == null || rear_support.length <= 0) {
            return i;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= rear_support.length) {
                z = false;
                break;
            }
            if (TextUtils.isDigitsOnly(rear_support[i2]) && i == Integer.parseInt(rear_support[i2])) {
                break;
            }
            i2++;
        }
        return !z ? Integer.parseInt(rear_support[0]) : i;
    }

    public static void saveStreamResolutionLevel(int i) {
        MainApplication application = MainApplication.getApplication();
        if (application.getDeviceSettingInfo().getCameraType() == 2) {
            PreferencesHelper.putIntValue(application, IConstant.KEY_REAR_RES_LEVEL, i);
        } else {
            PreferencesHelper.putIntValue(application, IConstant.KEY_FRONT_RES_LEVEL, i);
        }
    }

    public static int getRtsFormat() {
        DeviceDesc deviceDesc = MainApplication.getApplication().getDeviceDesc();
        if (deviceDesc != null) {
            return deviceDesc.getVideoType();
        }
        Dbug.e(TAG, "getRtsFormat: settingInfo is null");
        return 1;
    }

    public static int getRtspResolutionLevel() {
        int i;
        String[] rtspRearSupport;
        MainApplication application = MainApplication.getApplication();
        boolean z = true;
        if (application.getDeviceSettingInfo().getCameraType() == 1) {
            i = PreferencesHelper.getSharedPreferences(application).getInt(IConstant.KEY_RTSP_FRONT_RES_LEVEL, 0);
            rtspRearSupport = application.getDeviceDesc().getRtspFrontSupport();
        } else {
            i = PreferencesHelper.getSharedPreferences(application).getInt(IConstant.KEY_RTSP_REAR_RES_LEVEL, 0);
            rtspRearSupport = application.getDeviceDesc().getRtspRearSupport();
        }
        if (rtspRearSupport == null || rtspRearSupport.length <= 0) {
            return i;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= rtspRearSupport.length) {
                z = false;
                break;
            }
            if (i == Integer.parseInt(rtspRearSupport[i2])) {
                break;
            }
            i2++;
        }
        return !z ? Integer.parseInt(rtspRearSupport[0]) : i;
    }

    public static void saveRtspResolutionLevel(int i) {
        MainApplication application = MainApplication.getApplication();
        if (application.getDeviceSettingInfo().getCameraType() == 2) {
            PreferencesHelper.putIntValue(application, IConstant.KEY_RTSP_REAR_RES_LEVEL, i);
        } else {
            PreferencesHelper.putIntValue(application, IConstant.KEY_RTSP_FRONT_RES_LEVEL, i);
        }
    }

    public static String getRtspUrl() {
        String[] stringArray;
        int rtspResolutionLevel = getRtspResolutionLevel();
        MainApplication application = MainApplication.getApplication();
        int cameraType = application.getDeviceSettingInfo().getCameraType();
        if (1 == getRtsFormat()) {
            if (cameraType == 1) {
                stringArray = application.getResources().getStringArray(R.array.rtsp_h264_front_url);
            } else {
                stringArray = application.getResources().getStringArray(R.array.rtsp_h264_rear_url);
            }
        } else if (cameraType == 1) {
            stringArray = application.getResources().getStringArray(R.array.rtsp_jpeg_front_url);
        } else {
            stringArray = application.getResources().getStringArray(R.array.rtsp_jpeg_rear_url);
        }
        return String.format(stringArray[rtspResolutionLevel], ClientManager.getClient().getAddress());
    }

    public static String[] getRtspResolutions() {
        MainApplication application = MainApplication.getApplication();
        if (application.getDeviceSettingInfo().getCameraType() == 2) {
            return application.getDeviceDesc().getRtspRearSupport();
        }
        return application.getDeviceDesc().getRtspFrontSupport();
    }

    public static String[] getStreamResolutions() {
        MainApplication application = MainApplication.getApplication();
        if (application.getDeviceSettingInfo().getCameraType() == 2) {
            return application.getDeviceDesc().getRear_support();
        }
        return application.getDeviceDesc().getFront_support();
    }

    public static String getMediaDirectory(String str) {
        return (TextUtils.isEmpty(str) || !"1".equals(str)) ? IConstant.DIR_FRONT : IConstant.DIR_REAR;
    }

    public static int getCameraType(String str) {
        return (TextUtils.isEmpty(str) || !str.contains(IConstant.DEV_WORKSPACE_REAR)) ? 1 : 2;
    }

    public static String getAutoRearCameraKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str + "_rear_camera";
    }
}
