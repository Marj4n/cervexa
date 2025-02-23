package com.baidu.trace;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.baidu.trace.api.fence.CircleFence;
import com.baidu.trace.api.fence.FenceShape;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
final class w implements Runnable {
    private /* synthetic */ int a;
    private /* synthetic */ int b;
    private /* synthetic */ SQLiteDatabase c;

    w(u uVar, int i, int i2, SQLiteDatabase sQLiteDatabase) {
        this.a = i;
        this.b = i2;
        this.c = sQLiteDatabase;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v5 */
    @Override // java.lang.Runnable
    public final void run() {
        String str;
        String str2;
        String str3;
        StringBuffer stringBuffer;
        StringBuffer stringBuffer2;
        int i;
        StringBuffer stringBuffer3;
        int r3;
        StringBuffer stringBuffer4;
        int i2;
        StringBuffer stringBuffer5;
        String str4 = "select * from ";
        String str5 = "ALTER TABLE ";
        String str6 = "trace_location_TEMP;";
        StringBuffer stringBuffer6 = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer6.append("trace_location (");
        stringBuffer6.append("loc_time INTEGER, ");
        stringBuffer6.append("entity_name VARCHAR(128), ");
        stringBuffer6.append("location_data BLOB);");
        StringBuffer stringBuffer7 = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer7.append("trace_fence (");
        stringBuffer7.append("fence_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuffer7.append("fence_name VARCHAR(128), ");
        stringBuffer7.append("monitored_person VARCHAR(128), ");
        stringBuffer7.append("coord_type VARCHAR(16), ");
        stringBuffer7.append("denoise INTEGER, ");
        stringBuffer7.append("fence_shape VARCHAR(16), ");
        stringBuffer7.append("create_time VARCHAR(32), ");
        stringBuffer7.append("modify_time VARCHAR(32), ");
        stringBuffer7.append("monitored_status VARCHAR(16), ");
        stringBuffer7.append("fence_extern_info TEXT);");
        StringBuffer stringBuffer8 = new StringBuffer("CREATE TABLE if not exists ");
        stringBuffer8.append("trace_fence_alarm (");
        stringBuffer8.append("fence_id INTEGER, ");
        stringBuffer8.append("fence_name VARCHAR(128), ");
        stringBuffer8.append("monitored_person VARCHAR(128), ");
        stringBuffer8.append("monitored_action VARCHAR(16), ");
        stringBuffer8.append("create_time INTEGER, ");
        stringBuffer8.append("cur_point TEXT, ");
        stringBuffer8.append("pre_point TEXT);");
        Cursor cursor = null;
        String[] strArr = null;
        cursor = null;
        try {
            int i3 = this.a;
            Cursor cursor2 = null;
            while (i3 < this.b) {
                try {
                    if (this.a != 2) {
                        str = str4;
                        str2 = str5;
                        str3 = str6;
                        stringBuffer = stringBuffer6;
                        r3 = strArr;
                        stringBuffer2 = stringBuffer7;
                        i = i3;
                        stringBuffer3 = stringBuffer8;
                    } else {
                        this.c.execSQL(stringBuffer8.toString());
                        StringBuffer stringBuffer9 = new StringBuffer(str5);
                        stringBuffer9.append("trace_location RENAME TO ");
                        stringBuffer9.append(str6);
                        this.c.execSQL(stringBuffer9.toString());
                        this.c.execSQL(stringBuffer6.toString());
                        StringBuffer stringBuffer10 = new StringBuffer(str4);
                        stringBuffer10.append(str6);
                        Cursor rawQuery = this.c.rawQuery(stringBuffer10.toString(), strArr);
                        if (rawQuery != null) {
                            while (rawQuery.moveToNext()) {
                                String string = rawQuery.getString(rawQuery.getColumnIndex("location_data"));
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.length() >= 8) {
                                        byte[] a = com.baidu.trace.c.d.a(string);
                                        long parseLong = Long.parseLong(string.substring(string.length() - 8, string.length()), 16);
                                        StringBuffer stringBuffer11 = new StringBuffer("insert into ");
                                        stringBuffer11.append("trace_location(");
                                        stringBuffer11.append("loc_time, ");
                                        stringBuffer11.append("entity_name, ");
                                        stringBuffer11.append("location_data) values(?,?,?);");
                                        this.c.execSQL(stringBuffer11.toString(), new Object[]{Long.valueOf(parseLong), Trace.e, a});
                                    }
                                }
                            }
                            rawQuery.close();
                        }
                        StringBuffer stringBuffer12 = new StringBuffer("drop table ");
                        stringBuffer12.append(str6);
                        this.c.execSQL(stringBuffer12.toString());
                        StringBuffer stringBuffer13 = new StringBuffer(str5);
                        stringBuffer13.append("trace_fence RENAME TO ");
                        stringBuffer13.append("trace_fence_TEMP;");
                        this.c.execSQL(stringBuffer13.toString());
                        this.c.execSQL(stringBuffer7.toString());
                        StringBuffer stringBuffer14 = new StringBuffer(str4);
                        stringBuffer14.append("trace_fence_TEMP;");
                        cursor = this.c.rawQuery(stringBuffer14.toString(), null);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                try {
                                    try {
                                        long j = cursor.getLong(0);
                                        String string2 = cursor.getString(1);
                                        int indexOf = string2.indexOf("[");
                                        int indexOf2 = string2.indexOf("]");
                                        if (indexOf > 0 && indexOf2 > 0) {
                                            String[] split = string2.substring(indexOf + 1, indexOf2).split(";");
                                            ContentValues contentValues = new ContentValues();
                                            String str7 = str4;
                                            contentValues.put("monitored_person", Trace.e);
                                            contentValues.put("fence_shape", FenceShape.circle.name());
                                            contentValues.put("coord_type", CoordType.bd09ll.name());
                                            contentValues.put("create_time", com.baidu.trace.c.e.c());
                                            contentValues.put("modify_time", com.baidu.trace.c.e.c());
                                            CircleFence buildLocalFence = CircleFence.buildLocalFence(j, null, null, null, Utils.DOUBLE_EPSILON, 0, null);
                                            int length = split.length;
                                            String str8 = str5;
                                            String str9 = str6;
                                            int i4 = 0;
                                            while (true) {
                                                stringBuffer4 = stringBuffer6;
                                                if (i4 >= length) {
                                                    break;
                                                }
                                                StringBuffer stringBuffer15 = stringBuffer7;
                                                String[] split2 = split[i4].split("=");
                                                String[] strArr2 = split;
                                                if (split2.length == 2) {
                                                    String trim = split2[0].trim();
                                                    String trim2 = split2[1].trim();
                                                    if (BaseFragment.DATA_NAME.equals(trim)) {
                                                        contentValues.put("fence_name", trim2);
                                                    } else {
                                                        if ("center".equals(trim)) {
                                                            String[] split3 = trim2.split(",");
                                                            if (split3.length == 2) {
                                                                stringBuffer5 = stringBuffer8;
                                                                i2 = i3;
                                                                buildLocalFence.setCenter(new LatLng(Double.parseDouble(split3[0]), Double.parseDouble(split3[1])));
                                                            }
                                                        } else {
                                                            i2 = i3;
                                                            stringBuffer5 = stringBuffer8;
                                                            if ("radius".equals(trim)) {
                                                                buildLocalFence.setRadius(Double.parseDouble(trim2));
                                                            } else if ("precision".equals(trim)) {
                                                                contentValues.put("denoise", trim2);
                                                            }
                                                        }
                                                        i4++;
                                                        stringBuffer8 = stringBuffer5;
                                                        stringBuffer6 = stringBuffer4;
                                                        stringBuffer7 = stringBuffer15;
                                                        split = strArr2;
                                                        i3 = i2;
                                                    }
                                                }
                                                i2 = i3;
                                                stringBuffer5 = stringBuffer8;
                                                i4++;
                                                stringBuffer8 = stringBuffer5;
                                                stringBuffer6 = stringBuffer4;
                                                stringBuffer7 = stringBuffer15;
                                                split = strArr2;
                                                i3 = i2;
                                            }
                                            StringBuffer stringBuffer16 = stringBuffer7;
                                            int i5 = i3;
                                            StringBuffer stringBuffer17 = stringBuffer8;
                                            StringBuffer stringBuffer18 = new StringBuffer();
                                            a.a(buildLocalFence, (StringBuffer) null, stringBuffer18);
                                            contentValues.put("fence_extern_info", stringBuffer18.toString());
                                            if (j > 0) {
                                                try {
                                                    ContentValues contentValues2 = new ContentValues();
                                                    contentValues2.put("seq", Long.valueOf(j - 1));
                                                    StringBuffer stringBuffer19 = new StringBuffer(BaseFragment.DATA_NAME);
                                                    stringBuffer19.append(" like ? ");
                                                    this.c.update("sqlite_sequence", contentValues2, stringBuffer19.toString(), new String[]{"trace_fence"});
                                                } catch (Exception unused) {
                                                }
                                            }
                                            this.c.insert("trace_fence", null, contentValues);
                                            stringBuffer8 = stringBuffer17;
                                            str5 = str8;
                                            str6 = str9;
                                            stringBuffer6 = stringBuffer4;
                                            stringBuffer7 = stringBuffer16;
                                            i3 = i5;
                                            str4 = str7;
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                        throw th;
                                    }
                                } catch (Exception unused2) {
                                    if (cursor != null) {
                                        cursor.close();
                                        return;
                                    }
                                    return;
                                }
                            }
                            str = str4;
                            str2 = str5;
                            str3 = str6;
                            stringBuffer = stringBuffer6;
                            stringBuffer2 = stringBuffer7;
                            i = i3;
                            stringBuffer3 = stringBuffer8;
                            r3 = null;
                            cursor.close();
                            cursor2 = null;
                        } else {
                            str = str4;
                            str2 = str5;
                            str3 = str6;
                            stringBuffer = stringBuffer6;
                            stringBuffer2 = stringBuffer7;
                            i = i3;
                            stringBuffer3 = stringBuffer8;
                            r3 = null;
                            cursor2 = cursor;
                        }
                        StringBuffer stringBuffer20 = new StringBuffer("drop table ");
                        stringBuffer20.append("trace_fence_TEMP;");
                        this.c.execSQL(stringBuffer20.toString());
                    }
                    i3 = i + 1;
                    strArr = r3;
                    stringBuffer8 = stringBuffer3;
                    str5 = str2;
                    str6 = str3;
                    stringBuffer6 = stringBuffer;
                    stringBuffer7 = stringBuffer2;
                    str4 = str;
                } catch (Exception unused3) {
                    cursor = cursor2;
                } catch (Throwable th2) {
                    th = th2;
                    cursor = cursor2;
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (Exception unused4) {
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
