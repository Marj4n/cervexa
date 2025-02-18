package com.baidu.trace;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class s {
    private SharedPreferences a;
    private Map<String, String> b = new HashMap();
    private Set<String> c = new HashSet();

    public s(Context context) {
        this.a = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public final String a(String str) {
        if (!this.b.containsKey(str)) {
            this.b.put(str, this.a.getString(str, null));
        }
        return this.b.get(str);
    }

    public final void a() {
        SharedPreferences.Editor edit = this.a.edit();
        for (String str : this.c) {
            if (this.b.get(str) == null) {
                edit.remove(str);
            } else {
                edit.putString(str, this.b.get(str));
            }
        }
        edit.apply();
        this.c.clear();
    }

    public final void a(String str, String str2) {
        this.b.put(str, str2);
        if (this.c.contains(str)) {
            return;
        }
        this.c.add(str);
    }

    protected final void b() {
        Map<String, String> map = this.b;
        if (map != null) {
            map.clear();
            this.b = null;
        }
        Set<String> set = this.c;
        if (set != null) {
            set.clear();
            this.c = null;
        }
    }
}
