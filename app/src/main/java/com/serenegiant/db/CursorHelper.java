package com.serenegiant.db;

import android.database.Cursor;
import android.util.Log;

/* loaded from: classes.dex */
public final class CursorHelper {
    private static final String TAG = CursorHelper.class.getSimpleName();

    public static String getString(Cursor cursor, String str, String str2) {
        try {
            return cursor.getString(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return str2;
        }
    }

    public static CharSequence getCharSequence(Cursor cursor, String str, CharSequence charSequence) {
        try {
            return cursor.getString(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return charSequence;
        }
    }

    public static String getString(Cursor cursor, String str, CharSequence charSequence) {
        CharSequence charSequence2 = getCharSequence(cursor, str, charSequence);
        if (charSequence2 != null) {
            return charSequence2.toString();
        }
        return null;
    }

    public static int getInt(Cursor cursor, String str, int i) {
        try {
            return cursor.getInt(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return i;
        }
    }

    public static short getShort(Cursor cursor, String str, short s) {
        try {
            return cursor.getShort(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return s;
        }
    }

    public static long getLong(Cursor cursor, String str, long j) {
        try {
            return cursor.getLong(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return j;
        }
    }

    public static float getFloat(Cursor cursor, String str, float f) {
        try {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return f;
        }
    }

    public static double getDouble(Cursor cursor, String str, double d) {
        try {
            return cursor.getDouble(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return d;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0019, code lost:
    
        r0 = r5.getPosition();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int findPositionFromId(Cursor r5, long r6) {
        /*
            r0 = -1
            if (r5 == 0) goto L2e
            int r1 = r5.getPosition()
            boolean r2 = r5.moveToFirst()     // Catch: java.lang.Throwable -> L29
            if (r2 == 0) goto L25
        Ld:
            java.lang.String r2 = "_id"
            r3 = 0
            long r2 = getLong(r5, r2, r3)     // Catch: java.lang.Throwable -> L29
            int r4 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r4 != 0) goto L1f
            int r6 = r5.getPosition()     // Catch: java.lang.Throwable -> L29
            r0 = r6
            goto L25
        L1f:
            boolean r2 = r5.moveToNext()     // Catch: java.lang.Throwable -> L29
            if (r2 != 0) goto Ld
        L25:
            r5.moveToPosition(r1)
            goto L2e
        L29:
            r6 = move-exception
            r5.moveToPosition(r1)
            throw r6
        L2e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.db.CursorHelper.findPositionFromId(android.database.Cursor, long):int");
    }

    public static void dumpCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            int columnCount = cursor.getColumnCount();
            String[] columnNames = cursor.getColumnNames();
            int i = 0;
            do {
                sb.setLength(0);
                sb.append("row=");
                sb.append(i);
                sb.append(", ");
                for (int i2 = 0; i2 < columnCount; i2++) {
                    int type = cursor.getType(i2);
                    if (type == 0) {
                        sb.append(columnNames[i2]);
                        sb.append("=");
                        sb.append("NULL");
                    } else {
                        if (type == 1) {
                            sb.append(columnNames[i2]);
                            sb.append("=");
                            sb.append(cursor.getLong(i2));
                        } else if (type == 2) {
                            sb.append(columnNames[i2]);
                            sb.append("=");
                            sb.append(cursor.getDouble(i2));
                        } else if (type == 3) {
                            sb.append(columnNames[i2]);
                            sb.append("=");
                            sb.append(cursor.getString(i2));
                        } else if (type == 4) {
                            sb.append(columnNames[i2]);
                            sb.append("=");
                            sb.append("BLOB");
                        } else {
                            sb.append(columnNames[i2]);
                            sb.append("=");
                            sb.append("UNKNOWN");
                        }
                        sb.append(", ");
                    }
                }
                Log.v("CursorHelper#dumpCursor:", sb.toString());
                i++;
            } while (cursor.moveToNext());
        }
    }
}
