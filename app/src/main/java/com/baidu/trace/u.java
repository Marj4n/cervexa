package com.baidu.trace;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public final class u extends SQLiteOpenHelper {
    private SQLiteOpenHelper a;
    private Context b;

    public u(Context context) {
        super(context, "BAIDUTRACE.db", (SQLiteDatabase.CursorFactory) null, 3);
        this.a = null;
        this.b = null;
        String[] databaseList = context.databaseList();
        if (databaseList == null || databaseList.length == 0) {
            return;
        }
        int length = databaseList.length;
        boolean z = false;
        int i = 0;
        boolean z2 = false;
        while (true) {
            if (i >= length) {
                z = z2;
                break;
            }
            String str = databaseList[i];
            if ("BAIDUTRACE.db".equals(str)) {
                context.deleteDatabase("LBSTRACE.db");
                break;
            } else {
                if ("LBSTRACE.db".equals(str)) {
                    z2 = true;
                }
                i++;
            }
        }
        if (z) {
            this.b = context;
            this.a = new x(this, context, "LBSTRACE.db", null, 1);
        }
    }

    static /* synthetic */ Context a(u uVar, Context context) {
        uVar.b = null;
        return null;
    }

    static /* synthetic */ SQLiteOpenHelper a(u uVar, SQLiteOpenHelper sQLiteOpenHelper) {
        uVar.a = null;
        return null;
    }

    protected static void a(SQLiteDatabase sQLiteDatabase, String str, String[] strArr) {
        try {
            sQLiteDatabase.execSQL(str, strArr);
        } catch (Exception unused) {
        }
    }

    protected static Cursor b(SQLiteDatabase sQLiteDatabase, String str, String[] strArr) {
        try {
            return sQLiteDatabase.rawQuery(str, strArr);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        StringBuffer stringBuffer = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer.append("trace_location (");
        stringBuffer.append("loc_time INTEGER, ");
        stringBuffer.append("entity_name VARCHAR(128), ");
        stringBuffer.append("location_data BLOB);");
        StringBuffer stringBuffer2 = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer2.append("trace_fence (");
        stringBuffer2.append("fence_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuffer2.append("fence_name VARCHAR(128), ");
        stringBuffer2.append("monitored_person VARCHAR(128), ");
        stringBuffer2.append("coord_type VARCHAR(16), ");
        stringBuffer2.append("denoise INTEGER, ");
        stringBuffer2.append("fence_shape VARCHAR(16), ");
        stringBuffer2.append("create_time VARCHAR(32), ");
        stringBuffer2.append("modify_time VARCHAR(32), ");
        stringBuffer2.append("monitored_status VARCHAR(16), ");
        stringBuffer2.append("fence_extern_info TEXT);");
        StringBuffer stringBuffer3 = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer3.append("trace_fence_alarm (");
        stringBuffer3.append("fence_id INTEGER, ");
        stringBuffer3.append("fence_name VARCHAR(128), ");
        stringBuffer3.append("monitored_person VARCHAR(128), ");
        stringBuffer3.append("monitored_action VARCHAR(16), ");
        stringBuffer3.append("create_time INTEGER, ");
        stringBuffer3.append("cur_point TEXT, ");
        stringBuffer3.append("pre_point TEXT);");
        try {
            sQLiteDatabase.execSQL(stringBuffer.toString());
            sQLiteDatabase.execSQL(stringBuffer2.toString());
            sQLiteDatabase.execSQL(stringBuffer3.toString());
        } catch (Exception unused) {
        }
        if (this.a == null) {
            return;
        }
        com.baidu.trace.c.a.a.execute(new v(this, sQLiteDatabase));
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        super.onDowngrade(sQLiteDatabase, i, i2);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        com.baidu.trace.c.a.a.execute(new w(this, i, i2, sQLiteDatabase));
    }
}
