package com.baidu.mapapi.favorite;

import android.util.Log;
import com.baidu.mapapi.BMapManager;
import com.baidu.platform.comapi.favrite.FavSyncPoi;
import com.baidu.platform.comapi.map.C0078i;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class FavoriteManager {
    private static FavoriteManager a;
    private static com.baidu.platform.comapi.favrite.a b;

    private FavoriteManager() {
    }

    public static FavoriteManager getInstance() {
        if (a == null) {
            a = new FavoriteManager();
        }
        return a;
    }

    public int add(FavoritePoiInfo favoritePoiInfo) {
        String str;
        if (b == null) {
            str = "you may have not call init method!";
        } else {
            if (favoritePoiInfo != null && favoritePoiInfo.c != null) {
                if (favoritePoiInfo.b == null || favoritePoiInfo.b.equals("")) {
                    Log.e("baidumapsdk", "poiName can not be null or empty!");
                    return -1;
                }
                FavSyncPoi a2 = a.a(favoritePoiInfo);
                int a3 = b.a(a2.b, a2);
                if (a3 == 1) {
                    favoritePoiInfo.a = a2.a;
                    favoritePoiInfo.g = Long.parseLong(a2.h);
                }
                return a3;
            }
            str = "object or pt can not be null!";
        }
        Log.e("baidumapsdk", str);
        return 0;
    }

    public boolean clearAllFavPois() {
        com.baidu.platform.comapi.favrite.a aVar = b;
        if (aVar != null) {
            return aVar.c();
        }
        Log.e("baidumapsdk", "you may have not call init method!");
        return false;
    }

    public boolean deleteFavPoi(String str) {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return false;
        }
        if (str == null || str.equals("")) {
            return false;
        }
        return b.a(str);
    }

    public void destroy() {
        com.baidu.platform.comapi.favrite.a aVar = b;
        if (aVar != null) {
            aVar.b();
            b = null;
            BMapManager.destroy();
            C0078i.b();
        }
    }

    public List<FavoritePoiInfo> getAllFavPois() {
        JSONArray optJSONArray;
        com.baidu.platform.comapi.favrite.a aVar = b;
        if (aVar == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        }
        String f = aVar.f();
        if (f != null && !f.equals("")) {
            try {
                JSONObject jSONObject = new JSONObject(f);
                if (jSONObject.optInt("favpoinum") != 0 && (optJSONArray = jSONObject.optJSONArray("favcontents")) != null && optJSONArray.length() > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                        if (jSONObject2 != null) {
                            arrayList.add(a.a(jSONObject2));
                        }
                    }
                    return arrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FavoritePoiInfo getFavPoi(String str) {
        FavSyncPoi b2;
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        }
        if (str == null || str.equals("") || (b2 = b.b(str)) == null) {
            return null;
        }
        return a.a(b2);
    }

    public void init() {
        if (b == null) {
            C0078i.a();
            BMapManager.init();
            b = com.baidu.platform.comapi.favrite.a.a();
        }
    }

    public boolean updateFavPoi(String str, FavoritePoiInfo favoritePoiInfo) {
        String str2;
        if (b == null) {
            str2 = "you may have not call init method!";
        } else {
            if (str == null || str.equals("") || favoritePoiInfo == null) {
                return false;
            }
            if (favoritePoiInfo == null || favoritePoiInfo.c == null) {
                str2 = "object or pt can not be null!";
            } else {
                if (favoritePoiInfo.b != null && !favoritePoiInfo.b.equals("")) {
                    favoritePoiInfo.a = str;
                    return b.b(str, a.a(favoritePoiInfo));
                }
                str2 = "poiName can not be null or empty!";
            }
        }
        Log.e("baidumapsdk", str2);
        return false;
    }
}
