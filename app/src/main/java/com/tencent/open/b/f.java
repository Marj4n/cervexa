package com.tencent.open.b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: ProGuard */
/* loaded from: classes2.dex */
public class f extends SQLiteOpenHelper {
    protected static final String[] a = {"key"};
    protected static f b;

    public static synchronized f a() {
        f fVar;
        synchronized (f.class) {
            if (b == null) {
                b = new f(com.tencent.open.utils.d.a());
            }
            fVar = b;
        }
        return fVar;
    }

    public f(Context context) {
        super(context, "sdk_report.db", (SQLiteDatabase.CursorFactory) null, 2);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS via_cgi_report( _id INTEGER PRIMARY KEY,key TEXT,type TEXT,blob BLOB);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS via_cgi_report");
        onCreate(sQLiteDatabase);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x008a, code lost:
    
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x008c, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00a3, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x009f, code lost:
    
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0075 A[Catch: all -> 0x007f, Exception -> 0x0082, TRY_ENTER, TryCatch #13 {Exception -> 0x0082, all -> 0x007f, blocks: (B:23:0x0030, B:25:0x0036, B:26:0x0039, B:32:0x0053, B:34:0x0056, B:36:0x0075, B:37:0x0078, B:68:0x0062, B:61:0x0065, B:62:0x0068, B:54:0x006c, B:49:0x006f), top: B:22:0x0030 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized java.util.List<java.io.Serializable> a(java.lang.String r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Laf
            r0.<init>()     // Catch: java.lang.Throwable -> Laf
            java.util.List r0 = java.util.Collections.synchronizedList(r0)     // Catch: java.lang.Throwable -> Laf
            boolean r1 = android.text.TextUtils.isEmpty(r12)     // Catch: java.lang.Throwable -> Laf
            if (r1 == 0) goto L12
            monitor-exit(r11)
            return r0
        L12:
            android.database.sqlite.SQLiteDatabase r1 = r11.getReadableDatabase()     // Catch: java.lang.Throwable -> Laf
            if (r1 != 0) goto L1a
            monitor-exit(r11)
            return r0
        L1a:
            r10 = 0
            java.lang.String r3 = "via_cgi_report"
            r4 = 0
            java.lang.String r5 = "type = ?"
            r2 = 1
            java.lang.String[] r6 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            r2 = 0
            r6[r2] = r12     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            r7 = 0
            r8 = 0
            r9 = 0
            r2 = r1
            android.database.Cursor r12 = r2.query(r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            if (r12 == 0) goto L85
            int r2 = r12.getCount()     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            if (r2 <= 0) goto L85
            r12.moveToFirst()     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
        L39:
            java.lang.String r2 = "blob"
            int r2 = r12.getColumnIndex(r2)     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            byte[] r2 = r12.getBlob(r2)     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L69
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L69
            java.lang.Object r4 = r2.readObject()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L6a
            java.io.Serializable r4 = (java.io.Serializable) r4     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L6a
            r2.close()     // Catch: java.io.IOException -> L56 java.lang.Throwable -> L7f java.lang.Exception -> L82
        L56:
            r3.close()     // Catch: java.io.IOException -> L5a java.lang.Throwable -> L7f java.lang.Exception -> L82
            goto L73
        L5a:
            goto L73
        L5c:
            r4 = move-exception
            r10 = r2
            goto L60
        L5f:
            r4 = move-exception
        L60:
            if (r10 == 0) goto L65
            r10.close()     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L7f java.lang.Exception -> L82
        L65:
            r3.close()     // Catch: java.io.IOException -> L68 java.lang.Throwable -> L7f java.lang.Exception -> L82
        L68:
            throw r4     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
        L69:
            r2 = r10
        L6a:
            if (r2 == 0) goto L6f
            r2.close()     // Catch: java.io.IOException -> L6f java.lang.Throwable -> L7f java.lang.Exception -> L82
        L6f:
            r3.close()     // Catch: java.io.IOException -> L72 java.lang.Throwable -> L7f java.lang.Exception -> L82
        L72:
            r4 = r10
        L73:
            if (r4 == 0) goto L78
            r0.add(r4)     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
        L78:
            boolean r2 = r12.moveToNext()     // Catch: java.lang.Throwable -> L7f java.lang.Exception -> L82
            if (r2 != 0) goto L39
            goto L85
        L7f:
            r0 = move-exception
            r10 = r12
            goto La4
        L82:
            r2 = move-exception
            r10 = r12
            goto L93
        L85:
            if (r12 == 0) goto L8a
            r12.close()     // Catch: java.lang.Throwable -> Laf
        L8a:
            if (r1 == 0) goto La2
        L8c:
            r1.close()     // Catch: java.lang.Throwable -> Laf
            goto La2
        L90:
            r0 = move-exception
            goto La4
        L92:
            r2 = move-exception
        L93:
            java.lang.String r12 = "openSDK_LOG.ReportDatabaseHelper"
            java.lang.String r3 = "getReportItemFromDB has exception."
            com.tencent.open.a.f.b(r12, r3, r2)     // Catch: java.lang.Throwable -> L90
            if (r10 == 0) goto L9f
            r10.close()     // Catch: java.lang.Throwable -> Laf
        L9f:
            if (r1 == 0) goto La2
            goto L8c
        La2:
            monitor-exit(r11)
            return r0
        La4:
            if (r10 == 0) goto La9
            r10.close()     // Catch: java.lang.Throwable -> Laf
        La9:
            if (r1 == 0) goto Lae
            r1.close()     // Catch: java.lang.Throwable -> Laf
        Lae:
            throw r0     // Catch: java.lang.Throwable -> Laf
        Laf:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.open.b.f.a(java.lang.String):java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:69:0x0096, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0092, code lost:
    
        if (r1 == null) goto L55;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void a(java.lang.String r9, java.util.List<java.io.Serializable> r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            int r0 = r10.size()     // Catch: java.lang.Throwable -> La0
            if (r0 != 0) goto L9
            monitor-exit(r8)
            return
        L9:
            r1 = 20
            if (r0 > r1) goto Le
            goto L10
        Le:
            r0 = 20
        L10:
            boolean r1 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Throwable -> La0
            if (r1 == 0) goto L18
            monitor-exit(r8)
            return
        L18:
            r8.b(r9)     // Catch: java.lang.Throwable -> La0
            android.database.sqlite.SQLiteDatabase r1 = r8.getWritableDatabase()     // Catch: java.lang.Throwable -> La0
            if (r1 != 0) goto L23
            monitor-exit(r8)
            return
        L23:
            r1.beginTransaction()     // Catch: java.lang.Throwable -> La0
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r2.<init>()     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r3 = 0
        L2c:
            if (r3 >= r0) goto L7a
            java.lang.Object r4 = r10.get(r3)     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            java.io.Serializable r4 = (java.io.Serializable) r4     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            if (r4 == 0) goto L74
            java.lang.String r5 = "type"
            r2.put(r5, r9)     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r6 = 512(0x200, float:7.17E-43)
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r6 = 0
            java.io.ObjectOutputStream r7 = new java.io.ObjectOutputStream     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L5f
            r7.<init>(r5)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L5f
            r7.writeObject(r4)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L60
            r7.close()     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L86 java.lang.Exception -> L88
        L4e:
            r5.close()     // Catch: java.io.IOException -> L66 java.lang.Throwable -> L86 java.lang.Exception -> L88
            goto L66
        L52:
            r9 = move-exception
            r6 = r7
            goto L56
        L55:
            r9 = move-exception
        L56:
            if (r6 == 0) goto L5b
            r6.close()     // Catch: java.io.IOException -> L5b java.lang.Throwable -> L86 java.lang.Exception -> L88
        L5b:
            r5.close()     // Catch: java.io.IOException -> L5e java.lang.Throwable -> L86 java.lang.Exception -> L88
        L5e:
            throw r9     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
        L5f:
            r7 = r6
        L60:
            if (r7 == 0) goto L4e
            r7.close()     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L86 java.lang.Exception -> L88
            goto L4e
        L66:
            java.lang.String r4 = "blob"
            byte[] r5 = r5.toByteArray()     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r2.put(r4, r5)     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            java.lang.String r4 = "via_cgi_report"
            r1.insert(r4, r6, r2)     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
        L74:
            r2.clear()     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            int r3 = r3 + 1
            goto L2c
        L7a:
            r1.setTransactionSuccessful()     // Catch: java.lang.Throwable -> L86 java.lang.Exception -> L88
            r1.endTransaction()     // Catch: java.lang.Throwable -> La0
            if (r1 == 0) goto L95
        L82:
            r1.close()     // Catch: java.lang.Throwable -> La0
            goto L95
        L86:
            r9 = move-exception
            goto L97
        L88:
            java.lang.String r9 = "openSDK_LOG.ReportDatabaseHelper"
            java.lang.String r10 = "saveReportItemToDB has exception."
            com.tencent.open.a.f.e(r9, r10)     // Catch: java.lang.Throwable -> L86
            r1.endTransaction()     // Catch: java.lang.Throwable -> La0
            if (r1 == 0) goto L95
            goto L82
        L95:
            monitor-exit(r8)
            return
        L97:
            r1.endTransaction()     // Catch: java.lang.Throwable -> La0
            if (r1 == 0) goto L9f
            r1.close()     // Catch: java.lang.Throwable -> La0
        L9f:
            throw r9     // Catch: java.lang.Throwable -> La0
        La0:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.open.b.f.a(java.lang.String, java.util.List):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0032, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002e, code lost:
    
        if (r0 == null) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void b(java.lang.String r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> L39
            if (r0 == 0) goto L9
            monitor-exit(r5)
            return
        L9:
            android.database.sqlite.SQLiteDatabase r0 = r5.getWritableDatabase()     // Catch: java.lang.Throwable -> L39
            if (r0 != 0) goto L11
            monitor-exit(r5)
            return
        L11:
            java.lang.String r1 = "via_cgi_report"
            java.lang.String r2 = "type = ?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            r4 = 0
            r3[r4] = r6     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            r0.delete(r1, r2, r3)     // Catch: java.lang.Throwable -> L24 java.lang.Exception -> L26
            if (r0 == 0) goto L31
        L20:
            r0.close()     // Catch: java.lang.Throwable -> L39
            goto L31
        L24:
            r6 = move-exception
            goto L33
        L26:
            r6 = move-exception
            java.lang.String r1 = "openSDK_LOG.ReportDatabaseHelper"
            java.lang.String r2 = "clearReportItem has exception."
            com.tencent.open.a.f.b(r1, r2, r6)     // Catch: java.lang.Throwable -> L24
            if (r0 == 0) goto L31
            goto L20
        L31:
            monitor-exit(r5)
            return
        L33:
            if (r0 == 0) goto L38
            r0.close()     // Catch: java.lang.Throwable -> L39
        L38:
            throw r6     // Catch: java.lang.Throwable -> L39
        L39:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.open.b.f.b(java.lang.String):void");
    }
}
