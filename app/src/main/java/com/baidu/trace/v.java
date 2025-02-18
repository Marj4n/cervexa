package com.baidu.trace;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/* loaded from: classes.dex */
final class v implements Runnable {
    private /* synthetic */ SQLiteDatabase a;
    private /* synthetic */ u b;

    v(u uVar, SQLiteDatabase sQLiteDatabase) {
        this.b = uVar;
        this.a = sQLiteDatabase;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Cursor cursor;
        SQLiteDatabase sQLiteDatabase;
        SQLiteOpenHelper sQLiteOpenHelper;
        Context context;
        Context context2;
        SQLiteOpenHelper sQLiteOpenHelper2;
        SQLiteOpenHelper sQLiteOpenHelper3;
        Context context3;
        SQLiteOpenHelper sQLiteOpenHelper4;
        Context context4;
        SQLiteOpenHelper sQLiteOpenHelper5;
        SQLiteOpenHelper sQLiteOpenHelper6;
        Context context5;
        SQLiteOpenHelper sQLiteOpenHelper7;
        try {
            sQLiteOpenHelper5 = this.b.a;
            sQLiteDatabase = sQLiteOpenHelper5.getWritableDatabase();
            try {
                cursor = sQLiteDatabase.rawQuery("select * from trace_locationinfo;", null);
                while (cursor != null) {
                    try {
                        if (!cursor.moveToNext()) {
                            break;
                        }
                        String string = cursor.getString(cursor.getColumnIndex("location_data"));
                        if (!TextUtils.isEmpty(string) && string.length() >= 8) {
                            byte[] a = com.baidu.trace.c.d.a(string);
                            long parseLong = Long.parseLong(string.substring(string.length() - 8, string.length()), 16);
                            StringBuffer stringBuffer = new StringBuffer("insert into ");
                            stringBuffer.append("trace_location(");
                            stringBuffer.append("loc_time, ");
                            stringBuffer.append("entity_name, ");
                            stringBuffer.append("location_data) values(?,?,?);");
                            this.a.execSQL(stringBuffer.toString(), new Object[]{Long.valueOf(parseLong), Trace.e, a});
                        }
                    } catch (Exception unused) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (sQLiteDatabase != null) {
                            sQLiteDatabase.close();
                        }
                        sQLiteOpenHelper3 = this.b.a;
                        if (sQLiteOpenHelper3 != null) {
                            sQLiteOpenHelper4 = this.b.a;
                            sQLiteOpenHelper4.close();
                            u.a(this.b, (SQLiteOpenHelper) null);
                        }
                        context3 = this.b.b;
                        if (context3 == null) {
                            return;
                        }
                        context4 = this.b.b;
                        context4.deleteDatabase("LBSTRACE.db");
                        u.a(this.b, (Context) null);
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (sQLiteDatabase != null) {
                            sQLiteDatabase.close();
                        }
                        sQLiteOpenHelper = this.b.a;
                        if (sQLiteOpenHelper != null) {
                            sQLiteOpenHelper2 = this.b.a;
                            sQLiteOpenHelper2.close();
                            u.a(this.b, (SQLiteOpenHelper) null);
                        }
                        context = this.b.b;
                        if (context != null) {
                            context2 = this.b.b;
                            context2.deleteDatabase("LBSTRACE.db");
                            u.a(this.b, (Context) null);
                        }
                        throw th;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                sQLiteOpenHelper6 = this.b.a;
                if (sQLiteOpenHelper6 != null) {
                    sQLiteOpenHelper7 = this.b.a;
                    sQLiteOpenHelper7.close();
                    u.a(this.b, (SQLiteOpenHelper) null);
                }
                context5 = this.b.b;
                if (context5 == null) {
                    return;
                }
            } catch (Exception unused2) {
                cursor = null;
            } catch (Throwable th2) {
                th = th2;
                cursor = null;
            }
        } catch (Exception unused3) {
            cursor = null;
            sQLiteDatabase = null;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            sQLiteDatabase = null;
        }
        context4 = this.b.b;
        context4.deleteDatabase("LBSTRACE.db");
        u.a(this.b, (Context) null);
    }
}
