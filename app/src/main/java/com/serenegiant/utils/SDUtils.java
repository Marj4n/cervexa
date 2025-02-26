package com.serenegiant.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

@Deprecated
/* loaded from: classes2.dex */
public class SDUtils {
    private static final String TAG = SDUtils.class.getSimpleName();

    public interface FileFilter {
        boolean accept(DocumentFile documentFile);
    }

    public interface handleOnResultDelegater {
        void onFailed(int i, Intent intent);

        boolean onResult(int i, Uri uri, Intent intent);
    }

    public static boolean handleOnResult(Context context, int i, int i2, Intent intent, handleOnResultDelegater handleonresultdelegater) {
        Uri data;
        if (intent != null && i2 == -1 && (data = intent.getData()) != null) {
            try {
                return handleonresultdelegater.onResult(i, data, intent);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
        try {
            clearUri(context, getKey(i));
            handleonresultdelegater.onFailed(i, intent);
            return false;
        } catch (Exception e2) {
            Log.w(TAG, e2);
            return false;
        }
    }

    private static String getKey(int i) {
        return String.format(Locale.US, "SDUtils-%d", Integer.valueOf(i));
    }

    private static void saveUri(Context context, String str, Uri uri) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(str, uri.toString()).apply();
        }
    }

    private static Uri loadUri(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return null;
        }
        try {
            return Uri.parse(sharedPreferences.getString(str, null));
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
    }

    private static void clearUri(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        if (sharedPreferences == null || !sharedPreferences.contains(str)) {
            return;
        }
        try {
            sharedPreferences.edit().remove(str).apply();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    public static void requestOpenDocument(Activity activity, String str, int i) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareOpenDocumentIntent(str), i);
        }
    }

    public static void requestOpenDocument(FragmentActivity fragmentActivity, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragmentActivity.startActivityForResult(prepareOpenDocumentIntent(str), i);
        }
    }

    @Deprecated
    public static void requestOpenDocument(Fragment fragment, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareOpenDocumentIntent(str), i);
        }
    }

    public static void requestOpenDocument(androidx.fragment.app.Fragment fragment, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareOpenDocumentIntent(str), i);
        }
    }

    private static Intent prepareOpenDocumentIntent(String str) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(str);
        return intent;
    }

    public static void requestCreateDocument(Activity activity, String str, int i) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(str, null), i);
        }
    }

    public static void requestCreateDocument(Activity activity, String str, String str2, int i) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(str, str2), i);
        }
    }

    public static void requestCreateDocument(FragmentActivity fragmentActivity, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragmentActivity.startActivityForResult(prepareCreateDocument(str, null), i);
        }
    }

    public static void requestCreateDocument(FragmentActivity fragmentActivity, String str, String str2, int i) {
        if (BuildCheck.isKitKat()) {
            fragmentActivity.startActivityForResult(prepareCreateDocument(str, str2), i);
        }
    }

    public static void requestCreateDocument(Fragment fragment, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(str, null), i);
        }
    }

    public static void requestCreateDocument(Fragment fragment, String str, String str2, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(str, str2), i);
        }
    }

    public static void requestCreateDocument(androidx.fragment.app.Fragment fragment, String str, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(str, null), i);
        }
    }

    public static void requestCreateDocument(androidx.fragment.app.Fragment fragment, String str, String str2, int i) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(str, str2), i);
        }
    }

    private static Intent prepareCreateDocument(String str, String str2) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(str);
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("android.intent.extra.TITLE", str2);
        }
        return intent;
    }

    public static boolean requestDeleteDocument(Context context, Uri uri) {
        try {
            if (BuildCheck.isKitKat()) {
                return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
            }
            return false;
        } catch (FileNotFoundException unused) {
            return false;
        }
    }

    public static boolean hasStorageAccess(Context context, int i) {
        Uri loadUri;
        if (BuildCheck.isLollipop() && (loadUri = loadUri(context, getKey(i))) != null) {
            Iterator<UriPermission> it = context.getContentResolver().getPersistedUriPermissions().iterator();
            while (it.hasNext()) {
                if (it.next().getUri().equals(loadUri)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Uri requestStorageAccess(Activity activity, int i) {
        if (!BuildCheck.isLollipop()) {
            return null;
        }
        Uri storageUri = getStorageUri(activity, i);
        if (storageUri == null) {
            activity.startActivityForResult(prepareStorageAccessPermission(), i);
        }
        return storageUri;
    }

    public static Uri requestStorageAccess(FragmentActivity fragmentActivity, int i) {
        if (!BuildCheck.isLollipop()) {
            return null;
        }
        Uri storageUri = getStorageUri(fragmentActivity, i);
        if (storageUri == null) {
            fragmentActivity.startActivityForResult(prepareStorageAccessPermission(), i);
        }
        return storageUri;
    }

    @Deprecated
    public static Uri requestStorageAccess(Fragment fragment, int i) {
        Uri storageUri = getStorageUri(fragment.getActivity(), i);
        if (storageUri == null) {
            fragment.startActivityForResult(prepareStorageAccessPermission(), i);
        }
        return storageUri;
    }

    public static Uri requestStorageAccess(androidx.fragment.app.Fragment fragment, int i) {
        Uri r1 = null;
        if (BuildCheck.isLollipop()) {
            FragmentActivity activity = fragment.getActivity();
            r1 = activity != null ? getStorageUri(activity, i) : null;
            if (r1 == null) {
                fragment.startActivityForResult(prepareStorageAccessPermission(), i);
            }
        }
        return r1;
    }

    public static Uri getStorageUri(Context context, int i) {
        Uri loadUri;
        if (!BuildCheck.isLollipop() || (loadUri = loadUri(context, getKey(i))) == null) {
            return null;
        }
        boolean z = false;
        Iterator<UriPermission> it = context.getContentResolver().getPersistedUriPermissions().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (it.next().getUri().equals(loadUri)) {
                z = true;
                break;
            }
        }
        if (z) {
            return loadUri;
        }
        return null;
    }

    private static Intent prepareStorageAccessPermission() {
        return new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
    }

    public static Uri requestStorageAccessPermission(Context context, int i, Uri uri) {
        return requestStorageAccessPermission(context, i, uri, 3);
    }

    public static Uri requestStorageAccessPermission(Context context, int i, Uri uri, int i2) {
        if (!BuildCheck.isLollipop()) {
            return null;
        }
        context.getContentResolver().takePersistableUriPermission(uri, i2);
        saveUri(context, getKey(i), uri);
        return uri;
    }

    public static void releaseStorageAccessPermission(Context context, int i) {
        String key;
        Uri loadUri;
        if (!BuildCheck.isLollipop() || (loadUri = loadUri(context, (key = getKey(i)))) == null) {
            return;
        }
        context.getContentResolver().releasePersistableUriPermission(loadUri, 3);
        clearUri(context, key);
    }

    public static DocumentFile getStorage(Context context, int i) throws IOException {
        return getStorage(context, i, (String) null);
    }

    public static DocumentFile getStorage(Context context, int i, String str) throws IOException {
        Uri storageUri;
        if (!BuildCheck.isLollipop() || (storageUri = getStorageUri(context, i)) == null) {
            return null;
        }
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, storageUri);
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split("/")) {
                if (!TextUtils.isEmpty(str2)) {
                    DocumentFile findFile = fromTreeUri.findFile(str2);
                    if (findFile != null && findFile.isDirectory()) {
                        fromTreeUri = findFile;
                    } else if (findFile == null) {
                        if (fromTreeUri.canWrite()) {
                            fromTreeUri = fromTreeUri.createDirectory(str2);
                        } else {
                            throw new IOException("can't create directory");
                        }
                    } else {
                        throw new IOException("can't create directory, file with same name already exists");
                    }
                }
            }
        }
        return fromTreeUri;
    }

    public static DocumentFile getStorage(Context context, DocumentFile documentFile, String str) throws IOException {
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split("/")) {
                if (!TextUtils.isEmpty(str2)) {
                    DocumentFile findFile = documentFile.findFile(str2);
                    if (findFile != null && findFile.isDirectory()) {
                        documentFile = findFile;
                    } else if (findFile == null) {
                        if (documentFile.canWrite()) {
                            documentFile = documentFile.createDirectory(str2);
                        } else {
                            throw new IOException("can't create directory");
                        }
                    } else {
                        throw new IOException("can't create directory, file with same name already exists");
                    }
                }
            }
        }
        return documentFile;
    }

    public static Collection<DocumentFile> listFiles(Context context, DocumentFile documentFile, FileFilter fileFilter) throws IOException {
        ArrayList arrayList = new ArrayList();
        if (documentFile.isDirectory()) {
            for (DocumentFile documentFile2 : documentFile.listFiles()) {
                if (fileFilter == null || fileFilter.accept(documentFile2)) {
                    arrayList.add(documentFile2);
                }
            }
        }
        return arrayList;
    }

    public static StorageInfo getStorageInfo(Context context, DocumentFile documentFile) {
        try {
            String path = UriHelper.getPath(context, documentFile.getUri());
            if (path != null) {
                File file = new File(path);
                if (file.isDirectory() && file.canRead()) {
                    long totalSpace = file.getTotalSpace();
                    long freeSpace = file.getFreeSpace();
                    if (freeSpace < file.getUsableSpace()) {
                        freeSpace = file.getUsableSpace();
                    }
                    return new StorageInfo(totalSpace, freeSpace);
                }
            }
        } catch (Exception unused) {
        }
        if (!BuildCheck.isJellyBeanMR2()) {
            return null;
        }
        try {
            StatFs statFs = new StatFs(UriHelper.getPath(context, documentFile.getUri()));
            return new StorageInfo(statFs.getTotalBytes(), statFs.getAvailableBytes());
        } catch (Exception unused2) {
            return null;
        }
    }

    public static DocumentFile getStorageFile(Context context, int i, String str, String str2) throws IOException {
        return getStorageFile(context, i, (String) null, str, str2);
    }

    public static DocumentFile getStorageFile(Context context, int i, String str, String str2, String str3) throws IOException {
        DocumentFile storage;
        if (!BuildCheck.isLollipop() || (storage = getStorage(context, i, str)) == null) {
            return null;
        }
        DocumentFile findFile = storage.findFile(str3);
        if (findFile != null) {
            if (findFile.isFile()) {
                return findFile;
            }
            throw new IOException("directory with same name already exists");
        }
        return storage.createFile(str2, str3);
    }

    public static DocumentFile getStorageFile(Context context, DocumentFile documentFile, String str, String str2, String str3) throws IOException {
        DocumentFile storage = getStorage(context, documentFile, str);
        if (storage == null) {
            return null;
        }
        DocumentFile findFile = storage.findFile(str3);
        if (findFile != null) {
            if (findFile.isFile()) {
                return findFile;
            }
            throw new IOException("directory with same name already exists");
        }
        return storage.createFile(str2, str3);
    }

    public static OutputStream getStorageOutputStream(Context context, int i, String str, String str2) throws IOException {
        return getStorageOutputStream(context, i, (String) null, str, str2);
    }

    public static OutputStream getStorageOutputStream(Context context, int i, String str, String str2, String str3) throws IOException {
        DocumentFile storage;
        if (BuildCheck.isLollipop() && (storage = getStorage(context, i, str)) != null) {
            DocumentFile findFile = storage.findFile(str3);
            if (findFile != null) {
                if (findFile.isFile()) {
                    return context.getContentResolver().openOutputStream(findFile.getUri());
                }
                throw new IOException("directory with same name already exists");
            }
            return context.getContentResolver().openOutputStream(storage.createFile(str2, str3).getUri());
        }
        throw new FileNotFoundException();
    }

    public static OutputStream getStorageOutputStream(Context context, DocumentFile documentFile, String str, String str2, String str3) throws IOException {
        DocumentFile storage = getStorage(context, documentFile, str);
        if (storage != null) {
            DocumentFile findFile = storage.findFile(str3);
            if (findFile != null) {
                if (findFile.isFile()) {
                    return context.getContentResolver().openOutputStream(findFile.getUri());
                }
                throw new IOException("directory with same name already exists");
            }
            return context.getContentResolver().openOutputStream(storage.createFile(str2, str3).getUri());
        }
        throw new FileNotFoundException();
    }

    public static InputStream getStorageInputStream(Context context, int i, String str, String str2) throws IOException {
        return getStorageInputStream(context, i, (String) null, str, str2);
    }

    public static InputStream getStorageInputStream(Context context, int i, String str, String str2, String str3) throws IOException {
        DocumentFile storage;
        DocumentFile findFile;
        if (BuildCheck.isLollipop() && (storage = getStorage(context, i, str)) != null && (findFile = storage.findFile(str3)) != null) {
            if (findFile.isFile()) {
                return context.getContentResolver().openInputStream(findFile.getUri());
            }
            throw new IOException("directory with same name already exists");
        }
        throw new FileNotFoundException();
    }

    public static InputStream getStorageInputStream(Context context, DocumentFile documentFile, String str, String str2, String str3) throws IOException {
        DocumentFile findFile;
        DocumentFile storage = getStorage(context, documentFile, str);
        if (storage != null && (findFile = storage.findFile(str3)) != null) {
            if (findFile.isFile()) {
                return context.getContentResolver().openInputStream(findFile.getUri());
            }
            throw new IOException("directory with same name already exists");
        }
        throw new FileNotFoundException();
    }

    public static ParcelFileDescriptor getStorageFileFD(Context context, int i, String str, String str2, String str3) throws IOException {
        DocumentFile storage;
        if (BuildCheck.isLollipop() && (storage = getStorage(context, i, str)) != null) {
            DocumentFile findFile = storage.findFile(str3);
            if (findFile != null) {
                if (findFile.isFile()) {
                    return context.getContentResolver().openFileDescriptor(findFile.getUri(), "rw");
                }
                throw new IOException("directory with same name already exists");
            }
            return context.getContentResolver().openFileDescriptor(storage.createFile(str2, str3).getUri(), "rw");
        }
        throw new FileNotFoundException();
    }

    public static ParcelFileDescriptor getStorageFileFD(Context context, DocumentFile documentFile, String str, String str2, String str3) throws IOException {
        DocumentFile storage = getStorage(context, documentFile, str);
        if (storage != null) {
            DocumentFile findFile = storage.findFile(str3);
            if (findFile != null) {
                if (findFile.isFile()) {
                    return context.getContentResolver().openFileDescriptor(findFile.getUri(), "rw");
                }
                throw new IOException("directory with same name already exists");
            }
            return context.getContentResolver().openFileDescriptor(storage.createFile(str2, str3).getUri(), "rw");
        }
        throw new FileNotFoundException();
    }

    public static File createStorageDir(Context context, int i) {
        Uri storageUri;
        if (!BuildCheck.isLollipop() || (storageUri = getStorageUri(context, i)) == null) {
            return null;
        }
        String path = UriHelper.getPath(context, DocumentFile.fromTreeUri(context, storageUri).getUri());
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return new File(path);
    }

    public static File createStorageFile(Context context, int i, String str, String str2) {
        return createStorageFile(context, getStorageUri(context, i), str, str2);
    }

    public static File createStorageFile(Context context, Uri uri, String str, String str2) {
        Log.i(TAG, "createStorageFile:" + str2);
        if (!BuildCheck.isLollipop() || uri == null || TextUtils.isEmpty(str2)) {
            return null;
        }
        String path = UriHelper.getPath(context, DocumentFile.fromTreeUri(context, uri).createFile(str, str2).getUri());
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return new File(path);
    }

    public static int createStorageFileFD(Context context, int i, String str, String str2) {
        Log.i(TAG, "createStorageFileFD:" + str2);
        return createStorageFileFD(context, getStorageUri(context, i), str, str2);
    }

    public static int createStorageFileFD(Context context, Uri uri, String str, String str2) {
        Log.i(TAG, "createStorageFileFD:" + str2);
        if (BuildCheck.isLollipop() && uri != null && !TextUtils.isEmpty(str2)) {
            try {
                ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(DocumentFile.fromTreeUri(context, uri).createFile(str, str2).getUri(), "rw");
                if (openFileDescriptor != null) {
                    return openFileDescriptor.getFd();
                }
                return 0;
            } catch (FileNotFoundException e) {
                Log.w(TAG, e);
            }
        }
        return 0;
    }
}
