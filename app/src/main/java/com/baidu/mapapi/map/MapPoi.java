package com.baidu.mapapi.map;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class MapPoi {
    private static final String d = MapPoi.class.getSimpleName();
    String a;
    LatLng b;
    String c;

    void a(JSONObject jSONObject) {
        this.a = jSONObject.optString("tx");
        this.b = CoordUtil.decodeNodeLocation(jSONObject.optString("geo"));
        this.c = jSONObject.optString("ud");
    }

    public String getName() {
        return this.a;
    }

    public LatLng getPosition() {
        return this.b;
    }

    public String getUid() {
        return this.c;
    }
}
