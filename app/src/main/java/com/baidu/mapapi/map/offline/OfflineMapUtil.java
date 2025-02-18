package com.baidu.mapapi.map.offline;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.t;
import com.baidu.platform.comapi.map.w;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OfflineMapUtil {
    public static MKOLSearchRecord getSearchRecordFromLocalCityInfo(t tVar) {
        if (tVar == null) {
            return null;
        }
        MKOLSearchRecord mKOLSearchRecord = new MKOLSearchRecord();
        mKOLSearchRecord.cityID = tVar.a;
        mKOLSearchRecord.cityName = tVar.b;
        mKOLSearchRecord.cityType = tVar.d;
        int i = 0;
        if (tVar.a() != null) {
            ArrayList<MKOLSearchRecord> arrayList = new ArrayList<>();
            Iterator<t> it = tVar.a().iterator();
            while (it.hasNext()) {
                t next = it.next();
                arrayList.add(getSearchRecordFromLocalCityInfo(next));
                i += next.c;
                mKOLSearchRecord.childCities = arrayList;
            }
        }
        if (mKOLSearchRecord.cityType == 1) {
            mKOLSearchRecord.size = i;
        } else {
            mKOLSearchRecord.size = tVar.c;
        }
        return mKOLSearchRecord;
    }

    public static MKOLUpdateElement getUpdatElementFromLocalMapElement(w wVar) {
        if (wVar == null) {
            return null;
        }
        MKOLUpdateElement mKOLUpdateElement = new MKOLUpdateElement();
        mKOLUpdateElement.cityID = wVar.a;
        mKOLUpdateElement.cityName = wVar.b;
        if (wVar.g != null) {
            mKOLUpdateElement.geoPt = CoordUtil.mc2ll(wVar.g);
        }
        mKOLUpdateElement.level = wVar.e;
        mKOLUpdateElement.ratio = wVar.i;
        mKOLUpdateElement.serversize = wVar.h;
        mKOLUpdateElement.size = wVar.i == 100 ? wVar.h : (wVar.h / 100) * wVar.i;
        mKOLUpdateElement.status = wVar.l;
        mKOLUpdateElement.update = wVar.j;
        return mKOLUpdateElement;
    }
}
