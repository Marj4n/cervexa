package com.baidu.trace;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
final class an extends Thread {
    an() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        SQLiteDatabase sQLiteDatabase;
        u uVar;
        sQLiteDatabase = am.b;
        if (sQLiteDatabase != null) {
            return;
        }
        try {
            uVar = am.a;
            SQLiteDatabase unused = am.b = uVar.getWritableDatabase();
        } catch (Exception unused2) {
            SQLiteDatabase unused3 = am.b = null;
        } finally {
            am.b(false);
        }
    }
}
