package com.baidu.trace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.media.session.PlaybackStateCompat;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.CircleFence;
import com.baidu.trace.api.fence.FenceAlarmInfo;
import com.baidu.trace.api.fence.FenceInfo;
import com.baidu.trace.api.fence.FenceShape;
import com.baidu.trace.api.fence.HistoryAlarmRequest;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.fence.MonitoredStatus;
import com.baidu.trace.api.fence.MonitoredStatusInfo;
import com.baidu.trace.api.track.CacheTrackInfo;
import com.baidu.trace.ar;
import com.baidu.trace.model.CoordType;
import com.github.mikephil.charting.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class am extends Thread {
    private static u a = null;
    private static SQLiteDatabase b = null;
    private static boolean c = false;
    private static File d;
    private static int e;
    private int f;
    private String g;

    public am(int i, String str) {
        this.f = 0;
        this.g = "";
        this.f = i;
        this.g = str;
    }

    protected static long a(Context context, String str, String str2, CoordType coordType, int i, FenceShape fenceShape, String str3) {
        a(context);
        long j = -1;
        if (b == null) {
            return -1L;
        }
        d();
        b.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fence_name", str);
            contentValues.put("monitored_person", str2);
            contentValues.put("coord_type", coordType.name());
            contentValues.put("denoise", Integer.valueOf(i));
            contentValues.put("fence_shape", fenceShape.name());
            contentValues.put("create_time", com.baidu.trace.c.e.c());
            contentValues.put("modify_time", com.baidu.trace.c.e.c());
            contentValues.put("monitored_status", MonitoredStatus.unknown.name());
            contentValues.put("fence_extern_info", str3);
            j = b.insert("trace_fence", null, contentValues);
            b.setTransactionSuccessful();
        } catch (Exception unused) {
        } catch (Throwable th) {
            b.endTransaction();
            throw th;
        }
        b.endTransaction();
        return j;
    }

    protected static CircleFence a(Context context, long j) {
        a(context);
        CircleFence circleFence = null;
        if (b == null) {
            return null;
        }
        String[] strArr = {String.valueOf(j)};
        StringBuffer stringBuffer = new StringBuffer("fence_id");
        stringBuffer.append(" = ? ");
        Cursor query = b.query("trace_fence", new String[]{"fence_id", "fence_name", "monitored_person", "coord_type", "denoise", "fence_shape", "fence_extern_info"}, stringBuffer.toString(), strArr, null, null, null);
        if (query == null) {
            return null;
        }
        if (query.moveToNext()) {
            long j2 = query.getLong(0);
            String string = query.getString(1);
            String string2 = query.getString(2);
            CoordType valueOf = CoordType.valueOf(query.getString(3));
            int i = query.getInt(4);
            String string3 = query.getString(5);
            String string4 = query.getString(6);
            if (!FenceShape.circle.name().equals(string3)) {
                if (query != null) {
                    query.close();
                }
                return null;
            }
            circleFence = CircleFence.buildLocalFence(j2, string, string2, null, Utils.DOUBLE_EPSILON, i, valueOf);
            try {
                a.a(new JSONObject(string4), circleFence);
            } catch (JSONException unused) {
            }
        }
        if (query != null) {
            query.close();
        }
        return circleFence;
    }

    protected static String a(String str, int i, Deque<ar.a> deque) {
        String[] strArr;
        String str2 = null;
        if (b == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer("select * from ");
        stringBuffer.append("trace_location");
        if (!com.baidu.trace.b.a.d()) {
            return null;
        }
        if (bd.g) {
            stringBuffer.append(" where entity_name");
            stringBuffer.append(" like ?  limit 0,?;");
            strArr = new String[]{str, String.valueOf(10)};
        } else if (bd.h) {
            stringBuffer.append(" where entity_name");
            stringBuffer.append(" like (select entity_name");
            stringBuffer.append(" from trace_location");
            stringBuffer.append(" limit 1) limit 0,?;");
            strArr = new String[]{String.valueOf(10)};
        } else {
            strArr = null;
        }
        Cursor b2 = u.b(b, stringBuffer.toString(), strArr);
        if (b2 == null) {
            return null;
        }
        int count = b2.getCount();
        aw.c = count;
        if (count == 0) {
            if (bd.g) {
                bd.g = false;
            } else if (bd.h) {
                bd.h = false;
            }
            b2.close();
            return null;
        }
        while (b2.moveToNext()) {
            long j = b2.getLong(b2.getColumnIndex("loc_time"));
            String string = b2.getString(b2.getColumnIndex("entity_name"));
            byte[] blob = b2.getBlob(b2.getColumnIndex("location_data"));
            if (deque != null) {
                deque.offer(new ar.a(string, blob, j));
            }
            str2 = string;
        }
        b2.close();
        return str2;
    }

    protected static void a() {
        c = false;
        if (a == null && b == null) {
            return;
        }
        try {
            if (a != null) {
                a.close();
                a = null;
            }
            if (b != null) {
                b.close();
                b = null;
            }
        } catch (Exception unused) {
        }
        d = null;
    }

    protected static void a(int i) {
        if (i < 50) {
            return;
        }
        e = i;
    }

    protected static void a(long j, MonitoredStatus monitoredStatus) {
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return;
        }
        sQLiteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("monitored_status", monitoredStatus.name());
            StringBuffer stringBuffer = new StringBuffer("fence_id");
            stringBuffer.append(" = ? ");
            b.update("trace_fence", contentValues, stringBuffer.toString(), new String[]{String.valueOf(j)});
            b.setTransactionSuccessful();
        } catch (Exception unused) {
        } catch (Throwable th) {
            b.endTransaction();
            throw th;
        }
        b.endTransaction();
    }

    protected static void a(Context context) {
        if (context != null) {
            if (a == null || b == null) {
                synchronized (am.class) {
                    if (a == null) {
                        a = new u(context);
                    }
                    if (b == null && !c) {
                        c = true;
                        new an().start();
                    }
                }
            }
        }
    }

    protected static void a(Context context, HistoryAlarmRequest historyAlarmRequest, List<FenceAlarmInfo> list) {
        a(context);
        if (b == null) {
            return;
        }
        String[] strArr = {"fence_id", "fence_name", "monitored_person", "monitored_action", "cur_point", "pre_point"};
        ArrayList arrayList = new ArrayList();
        String monitoredPerson = historyAlarmRequest.getMonitoredPerson();
        StringBuffer stringBuffer = new StringBuffer();
        if (com.baidu.trace.c.e.a(monitoredPerson)) {
            stringBuffer.append("monitored_person like ? ");
            arrayList.add(monitoredPerson);
        }
        List<Long> fenceIds = historyAlarmRequest.getFenceIds();
        if (historyAlarmRequest.getStartTime() > 0) {
            stringBuffer.append(" and create_time");
            stringBuffer.append(" >= ? ");
            arrayList.add(String.valueOf(historyAlarmRequest.getStartTime()));
        }
        if (historyAlarmRequest.getEndTime() > 0) {
            stringBuffer.append(" and create_time");
            stringBuffer.append(" <= ? ");
            arrayList.add(String.valueOf(historyAlarmRequest.getEndTime()));
        }
        if (fenceIds != null && !fenceIds.isEmpty()) {
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i = 0; i < fenceIds.size(); i++) {
                stringBuffer2.append('?');
                stringBuffer2.append(',');
                arrayList.add(String.valueOf(fenceIds.get(i)));
            }
            stringBuffer2.deleteCharAt(stringBuffer2.length() - 1);
            stringBuffer.append(" and fence_id");
            stringBuffer.append(" in( ");
            stringBuffer.append(stringBuffer2.toString());
            stringBuffer.append(")");
        }
        String[] strArr2 = new String[arrayList.size()];
        arrayList.toArray(strArr2);
        Cursor query = b.query("trace_fence_alarm", strArr, stringBuffer.toString(), strArr2, null, null, null);
        if (query == null) {
            return;
        }
        while (query.moveToNext()) {
            long j = query.getLong(0);
            String string = query.getString(1);
            String string2 = query.getString(2);
            if (historyAlarmRequest.getMonitoredPerson().equals(string2)) {
                MonitoredAction valueOf = MonitoredAction.valueOf(query.getString(3));
                String string3 = query.getString(4);
                String string4 = query.getString(5);
                AlarmPoint alarmPoint = new AlarmPoint();
                AlarmPoint alarmPoint2 = new AlarmPoint();
                try {
                    a.a(new JSONObject(string3), CoordType.bd09ll, alarmPoint, String.class);
                    a.a(new JSONObject(string4), CoordType.bd09ll, alarmPoint2, String.class);
                    list.add(new FenceAlarmInfo(j, string, string2, valueOf, alarmPoint, alarmPoint2));
                } catch (JSONException unused) {
                }
            }
        }
        if (query != null) {
            query.close();
        }
    }

    protected static void a(Context context, String str, List<Long> list, List<FenceInfo> list2) {
        String[] strArr;
        a(context);
        if (b == null) {
            return;
        }
        String[] strArr2 = {"fence_id", "fence_name", "monitored_person", "coord_type", "denoise", "fence_shape", "create_time", "modify_time", "fence_extern_info"};
        StringBuffer stringBuffer = new StringBuffer();
        if (list != null && !list.isEmpty()) {
            StringBuffer stringBuffer2 = new StringBuffer();
            String[] strArr3 = new String[com.baidu.trace.c.e.a(str) ? list.size() + 1 : list.size()];
            for (int i = 0; i < list.size(); i++) {
                stringBuffer2.append('?');
                stringBuffer2.append(',');
                strArr3[i] = String.valueOf(list.get(i));
            }
            stringBuffer2.deleteCharAt(stringBuffer2.length() - 1);
            stringBuffer.append("fence_id in( ");
            stringBuffer.append(stringBuffer2.toString());
            stringBuffer.append(")");
            if (com.baidu.trace.c.e.a(str)) {
                stringBuffer.append(" and ");
                stringBuffer.append("monitored_person like ? ");
                strArr3[strArr3.length - 1] = str;
            }
            strArr = strArr3;
        } else if (com.baidu.trace.c.e.a(str)) {
            stringBuffer.append("monitored_person like ? ");
            strArr = new String[]{str};
        } else {
            strArr = null;
        }
        Cursor query = b.query("trace_fence", strArr2, stringBuffer.toString(), strArr, null, null, null);
        if (query == null) {
            return;
        }
        while (query.moveToNext()) {
            try {
                long j = query.getLong(0);
                String string = query.getString(1);
                String string2 = query.getString(2);
                CoordType valueOf = CoordType.valueOf(query.getString(3));
                int i2 = query.getInt(4);
                String string3 = query.getString(5);
                String string4 = query.getString(6);
                String string5 = query.getString(7);
                JSONObject jSONObject = new JSONObject(query.getString(8));
                if (FenceShape.circle.name().equals(string3)) {
                    CircleFence buildLocalFence = CircleFence.buildLocalFence(j, string, string2, null, Utils.DOUBLE_EPSILON, i2, valueOf);
                    a.a(jSONObject, buildLocalFence);
                    list2.add(new FenceInfo(FenceShape.circle, buildLocalFence, null, null, null, string4, string5));
                }
            } catch (Exception unused) {
                if (query != null) {
                    query.close();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
    }

    protected static void a(Context context, String str, String[] strArr) {
        a(context);
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return;
        }
        try {
            Cursor query = sQLiteDatabase.query("trace_fence", new String[]{"fence_id", "fence_name", "monitored_person", "coord_type", "denoise", "fence_shape", "monitored_status", "fence_extern_info"}, null, null, null, null, null);
            if (query == null) {
                return;
            }
            while (query.moveToNext()) {
                try {
                    long j = query.getLong(0);
                    String string = query.getString(1);
                    String string2 = query.getString(2);
                    CoordType valueOf = CoordType.valueOf(query.getString(3));
                    int i = query.getInt(4);
                    String string3 = query.getString(5);
                    MonitoredStatus valueOf2 = MonitoredStatus.valueOf(query.getString(6));
                    String string4 = query.getString(7);
                    if (FenceShape.circle.name().equals(string3)) {
                        JSONObject jSONObject = new JSONObject(string4);
                        CircleFence buildLocalFence = CircleFence.buildLocalFence(j, string, string2, null, Utils.DOUBLE_EPSILON, i, valueOf);
                        a.a(jSONObject, buildLocalFence);
                        ar.a.a(Long.valueOf(j).longValue(), buildLocalFence);
                        ar.a.a(Long.valueOf(j).longValue(), valueOf2);
                    }
                } catch (Exception unused) {
                    if (query != null) {
                        query.close();
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception unused2) {
        }
    }

    protected static void a(Context context, List<Long> list, String str, List<MonitoredStatusInfo> list2) {
        a(context);
        if (b == null) {
            return;
        }
        String[] strArr = {"fence_id", "monitored_status"};
        ArrayList arrayList = new ArrayList();
        StringBuffer stringBuffer = new StringBuffer("monitored_person");
        stringBuffer.append(" like ? ");
        arrayList.add(str);
        if (list != null && !list.isEmpty()) {
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                stringBuffer2.append('?');
                stringBuffer2.append(',');
                arrayList.add(String.valueOf(list.get(i)));
            }
            stringBuffer2.deleteCharAt(stringBuffer2.length() - 1);
            stringBuffer.append(" and fence_id");
            stringBuffer.append(" in( ");
            stringBuffer.append(stringBuffer2.toString());
            stringBuffer.append(")");
        }
        String[] strArr2 = new String[arrayList.size()];
        arrayList.toArray(strArr2);
        Cursor query = b.query("trace_fence", strArr, stringBuffer.toString(), strArr2, null, null, null);
        if (query == null) {
            return;
        }
        while (query.moveToNext()) {
            MonitoredStatusInfo monitoredStatusInfo = new MonitoredStatusInfo();
            monitoredStatusInfo.setFenceId(query.getLong(0));
            if (com.baidu.trace.c.e.a(query.getString(1))) {
                monitoredStatusInfo.setMonitoredStatus(MonitoredStatus.valueOf(query.getString(1)));
            }
            list2.add(monitoredStatusInfo);
        }
        if (query != null) {
            query.close();
        }
    }

    protected static void a(CircleFence circleFence, MonitoredAction monitoredAction, String str, String str2) {
        if (b == null) {
            return;
        }
        d();
        b.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fence_id", Long.valueOf(circleFence.getFenceId()));
            contentValues.put("fence_name", circleFence.getFenceName());
            contentValues.put("monitored_person", circleFence.getMonitoredPerson());
            contentValues.put("monitored_action", monitoredAction.name());
            contentValues.put("create_time", Long.valueOf(com.baidu.trace.c.e.b()));
            contentValues.put("cur_point", str);
            contentValues.put("pre_point", str2);
            b.insert("trace_fence_alarm", null, contentValues);
            b.setTransactionSuccessful();
        } catch (Exception unused) {
        } catch (Throwable th) {
            b.endTransaction();
            throw th;
        }
        b.endTransaction();
    }

    private static void a(String str) {
        if (b == null) {
            return;
        }
        Cursor b2 = u.b(b, "select distinct entity_name from trace_location", null);
        if (b2 == null) {
            return;
        }
        bd.h = b2.getCount() > 0;
        while (true) {
            if (!b2.moveToNext()) {
                break;
            } else if (str.equals(b2.getString(0))) {
                bd.g = true;
                break;
            }
        }
        b2.close();
    }

    protected static void a(String str, int i) {
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return;
        }
        sQLiteDatabase.beginTransaction();
        try {
            StringBuffer stringBuffer = new StringBuffer("delete from ");
            stringBuffer.append("trace_location where rowid in(select rowid from ");
            stringBuffer.append("trace_location where ");
            stringBuffer.append("entity_name like ? limit 0,?);");
            u.a(b, stringBuffer.toString(), new String[]{str, String.valueOf(i)});
            b.setTransactionSuccessful();
        } catch (Exception unused) {
        } catch (Throwable th) {
            b.endTransaction();
            throw th;
        }
        b.endTransaction();
    }

    private static void a(Queue<ar.a> queue) {
        if (b == null) {
            return;
        }
        d();
        b.beginTransaction();
        try {
            Iterator<ar.a> it = queue.iterator();
            while (it.hasNext()) {
                ar.a next = it.next();
                StringBuffer stringBuffer = new StringBuffer("insert into ");
                stringBuffer.append("trace_location(");
                stringBuffer.append("loc_time, ");
                stringBuffer.append("entity_name, ");
                stringBuffer.append("location_data) values(?,?,?);");
                b.execSQL(stringBuffer.toString(), new Object[]{Long.valueOf(next.c), next.a, next.b});
                it.remove();
            }
            b.setTransactionSuccessful();
        } catch (Exception unused) {
        } catch (Throwable th) {
            b.endTransaction();
            throw th;
        }
        b.endTransaction();
    }

    protected static void a(boolean z) {
        LinkedList linkedList = new LinkedList();
        if (!bd.g && aw.a != null && !aw.a.isEmpty()) {
            linkedList.addAll(aw.a);
            aw.a.removeAll(linkedList);
        }
        if (bd.c != null) {
            linkedList.addAll(bd.c);
            bd.c.removeAll(linkedList);
        }
        if (!linkedList.isEmpty()) {
            a(linkedList);
        }
        linkedList.clear();
        if (z) {
            a();
        }
    }

    protected static boolean a(Context context, long j, String str) {
        int i;
        a(context);
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return false;
        }
        sQLiteDatabase.beginTransaction();
        try {
            try {
                String[] strArr = {String.valueOf(j), str};
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("fence_id = ? ");
                stringBuffer.append(" and ");
                stringBuffer.append("monitored_person like ? ");
                i = b.delete("trace_fence", stringBuffer.toString(), strArr);
                try {
                    b.setTransactionSuccessful();
                } catch (Exception unused) {
                }
            } catch (Exception unused2) {
                i = 0;
            }
            return i > 0;
        } finally {
            b.endTransaction();
        }
    }

    protected static boolean a(Context context, long j, String str, String str2, CoordType coordType, int i, FenceShape fenceShape, String str3) {
        int i2;
        a(context);
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return false;
        }
        sQLiteDatabase.beginTransaction();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("fence_name", str);
                contentValues.put("monitored_person", str2);
                contentValues.put("coord_type", coordType.name());
                contentValues.put("denoise", Integer.valueOf(i));
                contentValues.put("fence_shape", fenceShape.name());
                contentValues.put("fence_extern_info", str3);
                contentValues.put("modify_time", com.baidu.trace.c.e.c());
                StringBuffer stringBuffer = new StringBuffer("fence_id");
                stringBuffer.append(" = ? ");
                i2 = b.update("trace_fence", contentValues, stringBuffer.toString(), new String[]{String.valueOf(j)});
                try {
                    b.setTransactionSuccessful();
                } catch (Exception unused) {
                }
            } finally {
                b.endTransaction();
            }
        } catch (Exception unused2) {
            i2 = 0;
        }
        return i2 > 0;
    }

    protected static boolean a(Context context, String str) {
        String[] strArr;
        a(context);
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return false;
        }
        sQLiteDatabase.beginTransaction();
        try {
            String str2 = null;
            if (com.baidu.trace.c.e.a(str)) {
                StringBuffer stringBuffer = new StringBuffer("monitored_person");
                stringBuffer.append(" like ? ");
                str2 = stringBuffer.toString();
                strArr = new String[]{str};
            } else {
                strArr = null;
            }
            b.delete("trace_fence", str2, strArr);
            b.setTransactionSuccessful();
            return true;
        } catch (Exception unused) {
            return false;
        } finally {
            b.endTransaction();
        }
    }

    protected static boolean a(Context context, String str, List<CacheTrackInfo> list) {
        CacheTrackInfo cacheTrackInfo;
        a(context);
        if (b == null) {
            return false;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select entity_name");
        stringBuffer.append(", count(*), min(loc_time");
        stringBuffer.append("), max(loc_time");
        stringBuffer.append(") from trace_location");
        String[] strArr = null;
        if (com.baidu.trace.c.e.a(str)) {
            stringBuffer.append(" where entity_name");
            stringBuffer.append(" like ? ");
            strArr = new String[]{str};
        } else {
            stringBuffer.append(" group by entity_name");
        }
        Cursor rawQuery = b.rawQuery(stringBuffer.toString(), strArr);
        if (rawQuery == null) {
            return true;
        }
        while (rawQuery.moveToNext()) {
            if (com.baidu.trace.c.e.a(rawQuery.getString(0))) {
                cacheTrackInfo = new CacheTrackInfo(rawQuery.getString(0), rawQuery.getInt(1), rawQuery.getLong(2), rawQuery.getLong(3));
            } else if (com.baidu.trace.c.e.a(str)) {
                cacheTrackInfo = new CacheTrackInfo(str, rawQuery.getInt(1), rawQuery.getLong(2), rawQuery.getLong(3));
            }
            list.add(cacheTrackInfo);
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return true;
    }

    protected static boolean a(Context context, List<String> list, List<CacheTrackInfo> list2) {
        int i;
        a(context);
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase == null) {
            return false;
        }
        sQLiteDatabase.beginTransaction();
        if (list2 != null) {
            try {
                if (!list2.isEmpty()) {
                    for (CacheTrackInfo cacheTrackInfo : list2) {
                        StringBuffer stringBuffer = new StringBuffer();
                        String entityName = cacheTrackInfo.getEntityName();
                        long startTime = cacheTrackInfo.getStartTime();
                        long endTime = cacheTrackInfo.getEndTime();
                        ArrayList arrayList = new ArrayList();
                        if (com.baidu.trace.c.e.a(entityName)) {
                            stringBuffer.append("entity_name like ? ");
                            arrayList.add(entityName);
                            if (startTime > 0) {
                                i = 2;
                                stringBuffer.append(" and loc_time");
                                stringBuffer.append(" >= ? ");
                                arrayList.add(String.valueOf(startTime));
                            } else {
                                i = 1;
                            }
                            if (endTime > 0) {
                                i++;
                                stringBuffer.append(" and loc_time");
                                stringBuffer.append(" <= ? ");
                                arrayList.add(String.valueOf(endTime));
                            }
                            String stringBuffer2 = stringBuffer.toString();
                            String[] strArr = new String[i];
                            arrayList.toArray(strArr);
                            b.delete("trace_location", stringBuffer2, strArr);
                            arrayList.clear();
                        }
                    }
                    b.setTransactionSuccessful();
                    b.endTransaction();
                    return true;
                }
            } catch (Exception unused) {
                b.endTransaction();
                return false;
            } catch (Throwable th) {
                b.endTransaction();
                throw th;
            }
        }
        if (list == null || list.isEmpty()) {
            b.delete("trace_location", null, null);
        } else {
            StringBuffer stringBuffer3 = new StringBuffer();
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (i2 < size - 1) {
                    stringBuffer3.append("entity_name like ? or ");
                } else {
                    stringBuffer3.append("entity_name like ? ");
                }
            }
            String stringBuffer4 = stringBuffer3.toString();
            String[] strArr2 = new String[list.size()];
            list.toArray(strArr2);
            b.delete("trace_location", stringBuffer4, strArr2);
        }
        b.setTransactionSuccessful();
        b.endTransaction();
        return true;
    }

    static /* synthetic */ boolean b(boolean z) {
        c = false;
        return false;
    }

    private static void d() {
        SQLiteDatabase sQLiteDatabase;
        if (b == null || e < 50) {
            return;
        }
        if (d == null) {
            d = new File(b.getPath());
        }
        long e2 = e();
        if (e2 > 0 && e2 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED > e && (sQLiteDatabase = b) != null) {
            sQLiteDatabase.beginTransaction();
            try {
                StringBuffer stringBuffer = new StringBuffer("delete from ");
                stringBuffer.append("trace_location where rowid in(select rowid from ");
                stringBuffer.append("trace_location limit 0,?);");
                u.a(b, stringBuffer.toString(), new String[]{String.valueOf(1000)});
                b.setTransactionSuccessful();
            } catch (Exception unused) {
            } catch (Throwable th) {
                b.endTransaction();
                throw th;
            }
            b.endTransaction();
        }
    }

    private static long e() {
        if (b == null) {
            return 0L;
        }
        try {
            if (d == null) {
                d = new File(b.getPath());
            }
            return d.length();
        } catch (Exception unused) {
            return 0L;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i = this.f;
        if (i != 0) {
            if (i != 1) {
                return;
            }
            a((Context) null, (String) null, (String[]) null);
            return;
        }
        String str = this.g;
        SQLiteDatabase sQLiteDatabase = b;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.beginTransaction();
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("entity_name", str);
                StringBuffer stringBuffer = new StringBuffer("entity_name");
                stringBuffer.append(" like ? ");
                String[] strArr = {""};
                b.update("trace_location", contentValues, stringBuffer.toString(), strArr);
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("monitored_person", str);
                StringBuffer stringBuffer2 = new StringBuffer("monitored_person");
                stringBuffer2.append(" like ? ");
                b.update("trace_fence", contentValues2, stringBuffer2.toString(), strArr);
                b.setTransactionSuccessful();
            } catch (Exception unused) {
            } catch (Throwable th) {
                b.endTransaction();
                throw th;
            }
            b.endTransaction();
        }
        a(this.g);
        SQLiteDatabase sQLiteDatabase2 = b;
        if (sQLiteDatabase2 == null) {
            return;
        }
        sQLiteDatabase2.beginTransaction();
        try {
            StringBuffer stringBuffer3 = new StringBuffer("create_time");
            stringBuffer3.append(" < ? ");
            b.delete("trace_fence_alarm", stringBuffer3.toString(), new String[]{String.valueOf(com.baidu.trace.c.e.b() - 604800)});
            b.setTransactionSuccessful();
        } catch (Exception unused2) {
        } catch (Throwable th2) {
            b.endTransaction();
            throw th2;
        }
        b.endTransaction();
    }
}
