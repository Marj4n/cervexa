package com.baidu.platform.comapi.map;

import android.os.Handler;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.UIMsg;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.jieli.lib.dv.control.utils.TopicKey;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class u {
    private static final String a = u.class.getSimpleName();
    private static u c;
    private com.baidu.platform.comjni.map.basemap.a b;
    private z d;
    private Handler e;

    private u() {
    }

    public static u a() {
        if (c == null) {
            u uVar = new u();
            c = uVar;
            uVar.g();
        }
        return c;
    }

    private void g() {
        h();
        this.d = new z();
        v vVar = new v(this);
        this.e = vVar;
        MessageCenter.registMessage(UIMsg.m_AppUI.V_WM_VDATAENGINE, vVar);
    }

    private void h() {
        EnvironmentUtilities.initAppDirectory(BMapManager.getContext());
        com.baidu.platform.comjni.map.basemap.a aVar = new com.baidu.platform.comjni.map.basemap.a();
        this.b = aVar;
        aVar.a();
        String moduleFileName = SysOSUtil.getModuleFileName();
        String appSDCardPath = EnvironmentUtilities.getAppSDCardPath();
        String appCachePath = EnvironmentUtilities.getAppCachePath();
        String appSecondCachePath = EnvironmentUtilities.getAppSecondCachePath();
        int mapTmpStgMax = EnvironmentUtilities.getMapTmpStgMax();
        int domTmpStgMax = EnvironmentUtilities.getDomTmpStgMax();
        int itsTmpStgMax = EnvironmentUtilities.getItsTmpStgMax();
        String str = SysOSUtil.getDensityDpi() >= 180 ? "/h/" : "/l/";
        String str2 = moduleFileName + "/cfg";
        String str3 = appSDCardPath + "/vmp";
        String str4 = str3 + str;
        String str5 = str3 + str;
        String str6 = appCachePath + "/tmp/";
        this.b.a(str2 + str, str4, str6, appSecondCachePath + "/tmp/", str5, str2 + "/a/", null, str2 + "/idrres/", SysOSUtil.getScreenSizeX(), SysOSUtil.getScreenSizeY(), SysOSUtil.getDensityDpi(), mapTmpStgMax, domTmpStgMax, itsTmpStgMax, 0);
        this.b.f();
    }

    public ArrayList<t> a(String str) {
        com.baidu.platform.comjni.map.basemap.a aVar;
        JSONArray optJSONArray;
        if (!str.equals("") && (aVar = this.b) != null) {
            String a2 = aVar.a(str);
            if (a2 == null || a2.equals("")) {
                return null;
            }
            ArrayList<t> arrayList = new ArrayList<>();
            try {
                JSONObject jSONObject = new JSONObject(a2);
                if (jSONObject.length() == 0 || (optJSONArray = jSONObject.optJSONArray("dataset")) == null) {
                    return null;
                }
                for (int i = 0; i < optJSONArray.length(); i++) {
                    t tVar = new t();
                    JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                    tVar.a = jSONObject2.optInt("id");
                    tVar.b = jSONObject2.optString(BaseFragment.DATA_NAME);
                    tVar.c = jSONObject2.optInt("mapsize");
                    tVar.d = jSONObject2.optInt("cty");
                    if (jSONObject2.has("child")) {
                        JSONArray optJSONArray2 = jSONObject2.optJSONArray("child");
                        ArrayList<t> arrayList2 = new ArrayList<>();
                        for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                            t tVar2 = new t();
                            JSONObject optJSONObject = optJSONArray2.optJSONObject(i2);
                            tVar2.a = optJSONObject.optInt("id");
                            tVar2.b = optJSONObject.optString(BaseFragment.DATA_NAME);
                            tVar2.c = optJSONObject.optInt("mapsize");
                            tVar2.d = optJSONObject.optInt("cty");
                            arrayList2.add(tVar2);
                        }
                        tVar.a(arrayList2);
                    }
                    arrayList.add(tVar);
                }
                return arrayList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void a(y yVar) {
        z zVar = this.d;
        if (zVar != null) {
            zVar.a(yVar);
        }
    }

    public boolean a(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null || i < 0) {
            return false;
        }
        return aVar.b(i);
    }

    public boolean a(boolean z, boolean z2) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null) {
            return false;
        }
        return aVar.a(z, z2);
    }

    public void b() {
        MessageCenter.unregistMessage(UIMsg.m_AppUI.V_WM_VDATAENGINE, this.e);
        this.b.b();
        c = null;
    }

    public void b(y yVar) {
        z zVar = this.d;
        if (zVar != null) {
            zVar.b(yVar);
        }
    }

    public boolean b(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null || i < 0) {
            return false;
        }
        return aVar.a(i, false, 0);
    }

    public ArrayList<t> c() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null) {
            return null;
        }
        String o = aVar.o();
        ArrayList<t> arrayList = new ArrayList<>();
        try {
            JSONArray optJSONArray = new JSONObject(o).optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                t tVar = new t();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                tVar.a = optJSONObject.optInt("id");
                tVar.b = optJSONObject.optString(BaseFragment.DATA_NAME);
                tVar.c = optJSONObject.optInt("mapsize");
                tVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList<t> arrayList2 = new ArrayList<>();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        t tVar2 = new t();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                        tVar2.a = optJSONObject2.optInt("id");
                        tVar2.b = optJSONObject2.optString(BaseFragment.DATA_NAME);
                        tVar2.c = optJSONObject2.optInt("mapsize");
                        tVar2.d = optJSONObject2.optInt("cty");
                        arrayList2.add(tVar2);
                    }
                    tVar.a(arrayList2);
                }
                arrayList.add(tVar);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean c(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null || i < 0) {
            return false;
        }
        return aVar.b(i, false, 0);
    }

    public ArrayList<t> d() {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        ArrayList<t> arrayList = null;
        if (aVar == null) {
            return null;
        }
        String a2 = aVar.a("");
        ArrayList<t> arrayList2 = new ArrayList<>();
        try {
            JSONArray optJSONArray = new JSONObject(a2).optJSONArray("dataset");
            int i = 0;
            while (i < optJSONArray.length()) {
                t tVar = new t();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                tVar.a = optJSONObject.optInt("id");
                tVar.b = optJSONObject.optString(BaseFragment.DATA_NAME);
                tVar.c = optJSONObject.optInt("mapsize");
                tVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList<t> arrayList3 = new ArrayList<>();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        t tVar2 = new t();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                        try {
                            tVar2.a = optJSONObject2.optInt("id");
                            tVar2.b = optJSONObject2.optString(BaseFragment.DATA_NAME);
                            tVar2.c = optJSONObject2.optInt("mapsize");
                            tVar2.d = optJSONObject2.optInt("cty");
                            arrayList3.add(tVar2);
                        } catch (JSONException unused) {
                            return null;
                        } catch (Exception unused2) {
                            return null;
                        }
                    }
                    tVar.a(arrayList3);
                }
                arrayList2.add(tVar);
                i++;
                arrayList = null;
            }
            return arrayList2;
        } catch (JSONException unused3) {
            return arrayList;
        } catch (Exception unused4) {
            return arrayList;
        }
    }

    public boolean d(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null) {
            return false;
        }
        return aVar.b(0, true, i);
    }

    public ArrayList<x> e() {
        String n;
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar != null && (n = aVar.n()) != null && !n.equals("")) {
            ArrayList<x> arrayList = new ArrayList<>();
            try {
                JSONObject jSONObject = new JSONObject(n);
                if (jSONObject.length() == 0) {
                    return null;
                }
                JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
                for (int i = 0; i < optJSONArray.length(); i++) {
                    x xVar = new x();
                    w wVar = new w();
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    wVar.a = optJSONObject.optInt("id");
                    wVar.b = optJSONObject.optString(BaseFragment.DATA_NAME);
                    wVar.c = optJSONObject.optString("pinyin");
                    wVar.h = optJSONObject.optInt("mapoldsize");
                    wVar.i = optJSONObject.optInt("ratio");
                    wVar.l = optJSONObject.optInt("status");
                    wVar.g = new GeoPoint(optJSONObject.optInt("y"), optJSONObject.optInt("x"));
                    if (optJSONObject.optInt("up") == 1) {
                        wVar.j = true;
                    } else {
                        wVar.j = false;
                    }
                    wVar.e = optJSONObject.optInt("lev");
                    if (wVar.j) {
                        wVar.k = optJSONObject.optInt("mapsize");
                    } else {
                        wVar.k = 0;
                    }
                    xVar.a(wVar);
                    arrayList.add(xVar);
                }
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean e(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null || i < 0) {
            return false;
        }
        return aVar.b(i, false);
    }

    public boolean f(int i) {
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar == null || i < 0) {
            return false;
        }
        return aVar.a(i, false);
    }

    public x g(int i) {
        String c2;
        com.baidu.platform.comjni.map.basemap.a aVar = this.b;
        if (aVar != null && i >= 0 && (c2 = aVar.c(i)) != null && !c2.equals("")) {
            x xVar = new x();
            w wVar = new w();
            try {
                JSONObject jSONObject = new JSONObject(c2);
                if (jSONObject.length() == 0) {
                    return null;
                }
                wVar.a = jSONObject.optInt("id");
                wVar.b = jSONObject.optString(BaseFragment.DATA_NAME);
                wVar.c = jSONObject.optString("pinyin");
                wVar.d = jSONObject.optString("headchar");
                wVar.h = jSONObject.optInt("mapoldsize");
                wVar.i = jSONObject.optInt("ratio");
                wVar.l = jSONObject.optInt("status");
                wVar.g = new GeoPoint(jSONObject.optInt("y"), jSONObject.optInt("x"));
                if (jSONObject.optInt("up") == 1) {
                    wVar.j = true;
                } else {
                    wVar.j = false;
                }
                wVar.e = jSONObject.optInt("lev");
                if (wVar.j) {
                    wVar.k = jSONObject.optInt("mapsize");
                } else {
                    wVar.k = 0;
                }
                wVar.f = jSONObject.optInt(TopicKey.VERSION);
                xVar.a(wVar);
                return xVar;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
