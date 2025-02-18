package com.baidu.platform.comapi.favrite;

import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a {
    private static a b;
    private com.baidu.platform.comjni.map.favorite.a a = null;
    private boolean c = false;
    private boolean d = false;
    private Vector<String> e = null;
    private Vector<String> f = null;
    private boolean g = false;
    private c h;
    private b i;

    /* renamed from: com.baidu.platform.comapi.favrite.a$a, reason: collision with other inner class name */
    class C0012a implements Comparator<String> {
        C0012a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String str, String str2) {
            return str2.compareTo(str);
        }
    }

    private class b {
        private long b;
        private long c;

        private b() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a() {
            this.b = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b() {
            this.c = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean c() {
            return this.c - this.b > 1000;
        }
    }

    private class c {
        private String b;
        private long c;
        private long d;

        private c() {
            this.c = 5000L;
            this.d = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String a() {
            return this.b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(String str) {
            this.b = str;
            this.d = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean b() {
            return TextUtils.isEmpty(this.b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean c() {
            return true;
        }
    }

    private a() {
        this.h = new c();
        this.i = new b();
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    a aVar = new a();
                    b = aVar;
                    aVar.h();
                }
            }
        }
        return b;
    }

    public static boolean g() {
        com.baidu.platform.comjni.map.favorite.a aVar;
        a aVar2 = b;
        return (aVar2 == null || (aVar = aVar2.a) == null || !aVar.d()) ? false : true;
    }

    private boolean h() {
        if (this.a == null) {
            com.baidu.platform.comjni.map.favorite.a aVar = new com.baidu.platform.comjni.map.favorite.a();
            this.a = aVar;
            if (aVar.a() == 0) {
                this.a = null;
                return false;
            }
            j();
            i();
        }
        return true;
    }

    private boolean i() {
        if (this.a == null) {
            return false;
        }
        String str = SysOSUtil.getModuleFileName() + "/";
        this.a.a(1);
        return this.a.a(str, "fav_poi", "fifo", 10, 501, -1);
    }

    private void j() {
        this.c = false;
        this.d = false;
    }

    public synchronized int a(String str, FavSyncPoi favSyncPoi) {
        if (this.a == null) {
            return 0;
        }
        if (str != null && !str.equals("") && favSyncPoi != null) {
            j();
            ArrayList<String> e = e();
            if ((e != null ? e.size() : 0) + 1 > 500) {
                return -2;
            }
            if (e != null && e.size() > 0) {
                Iterator<String> it = e.iterator();
                while (it.hasNext()) {
                    FavSyncPoi b2 = b(it.next());
                    if (b2 != null && str.equals(b2.b)) {
                        return -1;
                    }
                }
            }
            try {
                JSONObject jSONObject = new JSONObject();
                favSyncPoi.b = str;
                String valueOf = String.valueOf(System.currentTimeMillis());
                String str2 = valueOf + "_" + favSyncPoi.hashCode();
                favSyncPoi.h = valueOf;
                favSyncPoi.a = str2;
                jSONObject.put("bdetail", favSyncPoi.i);
                jSONObject.put("uspoiname", favSyncPoi.b);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("x", favSyncPoi.c.getmPtx());
                jSONObject2.put("y", favSyncPoi.c.getmPty());
                jSONObject.put("pt", jSONObject2);
                jSONObject.put("ncityid", favSyncPoi.e);
                jSONObject.put("npoitype", favSyncPoi.g);
                jSONObject.put("uspoiuid", favSyncPoi.f);
                jSONObject.put("addr", favSyncPoi.d);
                jSONObject.put("addtimesec", favSyncPoi.h);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("Fav_Sync", jSONObject);
                jSONObject3.put("Fav_Content", favSyncPoi.j);
                if (!this.a.a(str2, jSONObject3.toString())) {
                    return 0;
                }
                j();
                return 1;
            } catch (JSONException unused) {
                return 0;
            } finally {
                g();
            }
        }
        return -1;
    }

    public synchronized boolean a(String str) {
        if (this.a == null) {
            return false;
        }
        if (str != null && !str.equals("")) {
            if (!c(str)) {
                return false;
            }
            j();
            return this.a.a(str);
        }
        return false;
    }

    public FavSyncPoi b(String str) {
        if (this.a != null && str != null && !str.equals("")) {
            try {
                if (!c(str)) {
                    return null;
                }
                FavSyncPoi favSyncPoi = new FavSyncPoi();
                String b2 = this.a.b(str);
                if (b2 != null && !b2.equals("")) {
                    JSONObject jSONObject = new JSONObject(b2);
                    JSONObject optJSONObject = jSONObject.optJSONObject("Fav_Sync");
                    String optString = jSONObject.optString("Fav_Content");
                    favSyncPoi.b = optJSONObject.optString("uspoiname");
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("pt");
                    favSyncPoi.c = new Point(optJSONObject2.optInt("x"), optJSONObject2.optInt("y"));
                    favSyncPoi.e = optJSONObject.optString("ncityid");
                    favSyncPoi.f = optJSONObject.optString("uspoiuid");
                    favSyncPoi.g = optJSONObject.optInt("npoitype");
                    favSyncPoi.d = optJSONObject.optString("addr");
                    favSyncPoi.h = optJSONObject.optString("addtimesec");
                    favSyncPoi.i = optJSONObject.optBoolean("bdetail");
                    favSyncPoi.j = optString;
                    favSyncPoi.a = str;
                    return favSyncPoi;
                }
                return null;
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void b() {
        a aVar = b;
        if (aVar != null) {
            com.baidu.platform.comjni.map.favorite.a aVar2 = aVar.a;
            if (aVar2 != null) {
                aVar2.b();
                b.a = null;
            }
            b = null;
        }
    }

    public synchronized boolean b(String str, FavSyncPoi favSyncPoi) {
        boolean z = false;
        if (this.a != null && str != null && !str.equals("") && favSyncPoi != null) {
            if (!c(str)) {
                return false;
            }
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("uspoiname", favSyncPoi.b);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("x", favSyncPoi.c.getmPtx());
                jSONObject2.put("y", favSyncPoi.c.getmPty());
                jSONObject.put("pt", jSONObject2);
                jSONObject.put("ncityid", favSyncPoi.e);
                jSONObject.put("npoitype", favSyncPoi.g);
                jSONObject.put("uspoiuid", favSyncPoi.f);
                jSONObject.put("addr", favSyncPoi.d);
                favSyncPoi.h = String.valueOf(System.currentTimeMillis());
                jSONObject.put("addtimesec", favSyncPoi.h);
                jSONObject.put("bdetail", false);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("Fav_Sync", jSONObject);
                jSONObject3.put("Fav_Content", favSyncPoi.j);
                j();
                if (this.a != null) {
                    if (this.a.b(str, jSONObject3.toString())) {
                        z = true;
                    }
                }
                return z;
            } catch (JSONException unused) {
                return false;
            }
        }
        return false;
    }

    public synchronized boolean c() {
        if (this.a == null) {
            return false;
        }
        j();
        boolean c2 = this.a.c();
        g();
        return c2;
    }

    public boolean c(String str) {
        return (this.a == null || str == null || str.equals("") || !this.a.c(str)) ? false : true;
    }

    public ArrayList<String> d() {
        String b2;
        if (this.a == null) {
            return null;
        }
        if (this.d && this.f != null) {
            return new ArrayList<>(this.f);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                if (this.f == null) {
                    this.f = new Vector<>();
                } else {
                    this.f.clear();
                }
                for (int i = 0; i < stringArray.length; i++) {
                    if (!stringArray[i].equals("data_version") && (b2 = this.a.b(stringArray[i])) != null && !b2.equals("")) {
                        this.f.add(stringArray[i]);
                    }
                }
                if (this.f.size() > 0) {
                    try {
                        Collections.sort(this.f, new C0012a());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.d = true;
                }
            } else if (this.f != null) {
                this.f.clear();
                this.f = null;
            }
            if (this.f != null && !this.f.isEmpty()) {
                return new ArrayList<>(this.f);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public ArrayList<String> e() {
        if (this.a == null) {
            return null;
        }
        if (this.c && this.e != null) {
            return new ArrayList<>(this.e);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                if (this.e == null) {
                    this.e = new Vector<>();
                } else {
                    this.e.clear();
                }
                for (String str : stringArray) {
                    if (!str.equals("data_version")) {
                        this.e.add(str);
                    }
                }
                if (this.e.size() > 0) {
                    try {
                        Collections.sort(this.e, new C0012a());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.c = true;
                }
            } else if (this.e != null) {
                this.e.clear();
                this.e = null;
            }
            Vector<String> vector = this.e;
            if (vector == null || vector.size() == 0) {
                return null;
            }
            return new ArrayList<>(this.e);
        } catch (Exception unused) {
            return null;
        }
    }

    public String f() {
        String b2;
        if (this.i.c() && !this.h.c() && !this.h.b()) {
            return this.h.a();
        }
        this.i.a();
        if (this.a == null) {
            return null;
        }
        ArrayList<String> d = d();
        JSONObject jSONObject = new JSONObject();
        if (d != null) {
            try {
                JSONArray jSONArray = new JSONArray();
                int i = 0;
                Iterator<String> it = d.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (next != null && !next.equals("data_version") && (b2 = this.a.b(next)) != null && !b2.equals("")) {
                        JSONObject optJSONObject = new JSONObject(b2).optJSONObject("Fav_Sync");
                        optJSONObject.put("key", next);
                        jSONArray.put(i, optJSONObject);
                        i++;
                    }
                }
                if (i > 0) {
                    jSONObject.put("favcontents", jSONArray);
                    jSONObject.put("favpoinum", i);
                }
            } catch (JSONException unused) {
                return null;
            }
        }
        this.i.b();
        this.h.a(jSONObject.toString());
        return this.h.a();
    }
}
