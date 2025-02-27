package com.jieli.stream.dv.running2.util;

import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.gizthon.camera.core.OnCameraConnectedListener;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public class FTPClientUtil implements IConstant {
    private static int failedNum;
    private static FTPClientUtil instance;
    private String currentFTPPath;
    private String tag = "FTPClientUtil";
    private FTPClient mFTPClient = new FTPClient();

    public FTPClient getFTPClient() {
        if (this.mFTPClient == null) {
            this.mFTPClient = new FTPClient();
        }
        return this.mFTPClient;
    }

    public static FTPClientUtil getInstance() {
        if (instance == null) {
            synchronized (FTPClientUtil.class) {
                if (instance == null) {
                    instance = new FTPClientUtil();
                }
            }
        }
        return instance;
    }

    public boolean connectAndLoginFTP(String str, int i, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            Dbug.e(this.tag, "-connectAndLoginFTP- parameter is empty!");
            return false;
        }
        try {
            if (this.mFTPClient != null) {
                this.mFTPClient.setDefaultPort(i);
                this.mFTPClient.setDataTimeout(40000);
                this.mFTPClient.setConnectTimeout(OnCameraConnectedListener.ADMIN_ACTIVE);
                this.mFTPClient.connect(str);
                if (FTPReply.isPositiveCompletion(this.mFTPClient.getReplyCode()) && this.mFTPClient.login(str2, str3)) {
                    this.mFTPClient.setControlEncoding(Key.STRING_CHARSET_NAME);
                    this.mFTPClient.enterLocalPassiveMode();
                    this.currentFTPPath = this.mFTPClient.printWorkingDirectory();
                    Dbug.w(this.tag, "connect Ftp server success, root Path : " + this.currentFTPPath);
                    if (!TextUtils.isEmpty(str4)) {
                        if (checkExistPath(str4)) {
                            String str5 = this.currentFTPPath + str4;
                            if (this.mFTPClient.changeWorkingDirectory(str5)) {
                                Dbug.e(this.tag, "connect Ftp server success!  currentFTPPath : " + str5);
                                this.currentFTPPath = str5;
                                return true;
                            }
                        } else {
                            Dbug.e(this.tag, "The path does not exist in the ftp server, changePath : " + str4);
                        }
                    }
                    if (TextUtils.isEmpty(str4)) {
                        Dbug.e(this.tag, "connect Ftp server success!");
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            Dbug.e(this.tag, "connectFTP SocketException ===> " + e.getMessage());
        } catch (IOException e2) {
            Dbug.e(this.tag, "connectFTP IOException ===> " + e2.getMessage());
        }
        disconnect();
        return false;
    }

    public String getCurrentFTPPath() {
        return this.currentFTPPath;
    }

    public boolean changeWorkPath(String str) {
        if (TextUtils.isEmpty(str)) {
            Dbug.e(this.tag, "-connectAndLoginFTP- parameter is empty!");
            return false;
        }
        FTPClient fTPClient = this.mFTPClient;
        if (fTPClient != null && fTPClient.isConnected()) {
            try {
                boolean changeWorkingDirectory = this.mFTPClient.changeWorkingDirectory(str);
                if (changeWorkingDirectory) {
                    this.currentFTPPath = str;
                }
                return changeWorkingDirectory;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkExistPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String str2 = "";
        for (String str3 : str.contains("/") ? str.split("/") : new String[]{str}) {
            if (!TextUtils.isEmpty(str3)) {
                try {
                    if (this.mFTPClient == null) {
                        return false;
                    }
                    FTPFile[] listDirectories = this.mFTPClient.listDirectories("/" + str2);
                    if (listDirectories != null) {
                        int length = listDirectories.length;
                        int i = 0;
                        while (true) {
                            if (i < length) {
                                FTPFile fTPFile = listDirectories[i];
                                if (!fTPFile.isDirectory() || !str3.equals(fTPFile.getName())) {
                                    i++;
                                } else if (TextUtils.isEmpty(str2)) {
                                    str2 = str3;
                                } else {
                                    str2 = str2 + "/" + str3;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str.equals(str2);
    }

    public void disconnect() {
        FTPClient fTPClient = this.mFTPClient;
        if (fTPClient == null || !fTPClient.isConnected()) {
            return;
        }
        try {
            try {
                this.mFTPClient.logout();
                this.mFTPClient.disconnect();
            } catch (IOException e) {
                Dbug.e(this.tag, "disconnect IOException --> " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            this.currentFTPPath = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0197 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x019f A[Catch: IOException -> 0x019b, TRY_LEAVE, TryCatch #5 {IOException -> 0x019b, blocks: (B:106:0x0197, B:97:0x019f), top: B:105:0x0197 }] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v12, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v22, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void downloadTxt(java.util.List<String> r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 432
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.FTPClientUtil.downloadTxt(java.util.List, boolean):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 3, insn: 0x02b7: MOVE (r10 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:203:0x02b6 */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x029b A[Catch: IOException -> 0x0297, TRY_LEAVE, TryCatch #2 {IOException -> 0x0297, blocks: (B:124:0x0293, B:118:0x029b), top: B:123:0x0293 }] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0293 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00cc A[Catch: all -> 0x0283, IOException -> 0x0287, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0283, blocks: (B:9:0x001c, B:11:0x002e, B:13:0x0036, B:17:0x0041, B:19:0x0045, B:23:0x0073, B:24:0x004e, B:26:0x006a, B:30:0x00c2, B:34:0x00cc, B:36:0x011f, B:38:0x012a, B:40:0x0130, B:42:0x0136, B:44:0x013d, B:154:0x024f, B:165:0x0076, B:167:0x007a, B:171:0x00be, B:172:0x0083, B:174:0x0099, B:176:0x00a1, B:184:0x00ac, B:186:0x00b4, B:193:0x025c, B:196:0x0263), top: B:8:0x001c }] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v9, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<String> downLoadUpdateFile(String r18, int r19, int r20, android.os.Handler r21) {
        /*
            Method dump skipped, instructions count: 717
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.FTPClientUtil.downLoadUpdateFile(java.lang.String, int, int, android.os.Handler):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0062 A[Catch: all -> 0x010d, IOException -> 0x0112, TryCatch #13 {IOException -> 0x0112, all -> 0x010d, blocks: (B:12:0x0033, B:13:0x004a, B:15:0x0062, B:17:0x007c, B:18:0x0082, B:20:0x0089, B:23:0x0091, B:26:0x009a, B:29:0x00b2, B:36:0x00bc, B:67:0x0046), top: B:11:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0109 A[Catch: IOException -> 0x0129, TRY_ENTER, TRY_LEAVE, TryCatch #8 {IOException -> 0x0129, blocks: (B:57:0x0109, B:75:0x0125), top: B:7:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00ff A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0125 A[Catch: IOException -> 0x0129, TRY_ENTER, TRY_LEAVE, TryCatch #8 {IOException -> 0x0129, blocks: (B:57:0x0109, B:75:0x0125), top: B:7:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x013b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0131 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean uploadFile(String r13, String r14, android.os.Handler r15) {
        /*
            Method dump skipped, instructions count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.FTPClientUtil.uploadFile(java.lang.String, java.lang.String, android.os.Handler):boolean");
    }
}
