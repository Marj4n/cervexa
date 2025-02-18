package com.serenegiant.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import java.io.IOException;

@Deprecated
/* loaded from: classes2.dex */
public class DocumentFileHelper {
    public static DocumentFile getDocumentFile(Context context, DocumentFile documentFile, String str) throws IOException {
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

    public static DocumentFile getDocumentFile(Context context, DocumentFile documentFile, Uri uri) throws IOException {
        if (uri == null) {
            return null;
        }
        String path = UriHelper.getPath(context, documentFile.getUri());
        String path2 = UriHelper.getPath(context, uri);
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(path2) || !path2.startsWith(path)) {
            return null;
        }
        return getDocumentFile(context, documentFile, path2.substring(path.length()));
    }
}
