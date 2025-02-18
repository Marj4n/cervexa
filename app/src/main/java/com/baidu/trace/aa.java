package com.baidu.trace;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.baidu.trace.api.entity.AddEntityResponse;
import com.baidu.trace.api.entity.AroundSearchRequest;
import com.baidu.trace.api.entity.AroundSearchResponse;
import com.baidu.trace.api.entity.BoundSearchRequest;
import com.baidu.trace.api.entity.BoundSearchResponse;
import com.baidu.trace.api.entity.DeleteEntityResponse;
import com.baidu.trace.api.entity.DistrictSearchRequest;
import com.baidu.trace.api.entity.DistrictSearchResponse;
import com.baidu.trace.api.entity.EntityListRequest;
import com.baidu.trace.api.entity.EntityListResponse;
import com.baidu.trace.api.entity.FilterCondition;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.entity.PolygonSearchRequest;
import com.baidu.trace.api.entity.PolygonSearchResponse;
import com.baidu.trace.api.entity.SearchRequest;
import com.baidu.trace.api.entity.SearchResponse;
import com.baidu.trace.api.entity.SortBy;
import com.baidu.trace.api.entity.UpdateEntityResponse;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.LocType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class aa {
    private static String a = "";
    private static f b;

    private static String a(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : list) {
            try {
                stringBuffer.append(URLEncoder.encode(str, Key.STRING_CHARSET_NAME));
                stringBuffer.append(",");
            } catch (Exception unused) {
                stringBuffer.append(str);
                stringBuffer.append(",");
            }
        }
        return TextUtils.isEmpty(stringBuffer.toString()) ? "" : stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    private static String a(Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                stringBuffer.append(entry.getKey());
                stringBuffer.append(":");
                stringBuffer.append(URLEncoder.encode(entry.getValue(), Key.STRING_CHARSET_NAME));
                stringBuffer.append("|");
            } catch (Exception unused) {
                stringBuffer.append(entry.getKey());
                stringBuffer.append(":");
                stringBuffer.append(entry.getValue());
                stringBuffer.append("|");
            }
        }
        return TextUtils.isEmpty(stringBuffer.toString()) ? "" : stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    protected static void a(Context context, LocRequest locRequest, OnEntityListener onEntityListener) {
        if (b == null) {
            b = new f(context);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            boolean e = com.baidu.trace.c.e.e(context);
            boolean f = com.baidu.trace.c.e.f(context);
            boolean g = com.baidu.trace.c.e.g(context);
            if ((!f || !g) && !e) {
                a.a("BaiduTraceSDK", "Need ACCESS_WIFI_STATE or ACCESS_FINE_LOCATION permission to get wifi information");
                onEntityListener.onReceiveLocation(new TraceLocation(locRequest.getTag(), StatusCodes.REQUEST_FAILED, StatusCodes.MSG_REQUEST_FAILED));
                return;
            }
        }
        if (!com.baidu.trace.c.e.d(context) || TextUtils.isEmpty(a)) {
            a = b.a();
        }
        if (TextUtils.isEmpty(a)) {
            onEntityListener.onReceiveLocation(new TraceLocation(locRequest.getTag(), StatusCodes.REQUEST_FAILED, StatusCodes.MSG_REQUEST_FAILED));
        } else {
            a.a(locRequest, a, onEntityListener);
        }
    }

    protected static void a(FilterCondition filterCondition, TreeMap<String, String> treeMap) {
        if (filterCondition == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer("");
        if (filterCondition.getEntityNames() != null && !filterCondition.getEntityNames().isEmpty()) {
            stringBuffer.append("entity_names:");
            stringBuffer.append(a(filterCondition.getEntityNames()));
        }
        if (filterCondition.getActiveTime() > 0) {
            stringBuffer.append("|active_time");
            stringBuffer.append(":");
            stringBuffer.append(filterCondition.getActiveTime());
        }
        if (filterCondition.getInActiveTime() > 0) {
            stringBuffer.append("|inactive_time");
            stringBuffer.append(":");
            stringBuffer.append(filterCondition.getInActiveTime());
        }
        if (filterCondition.getColumns() != null && !filterCondition.getColumns().isEmpty()) {
            stringBuffer.append("|");
            stringBuffer.append(a(filterCondition.getColumns()));
        }
        if (stringBuffer.length() > 0 && stringBuffer.charAt(0) == '|') {
            stringBuffer.replace(0, 1, "");
        }
        if (TextUtils.isEmpty(stringBuffer.toString())) {
            return;
        }
        treeMap.put("filter", stringBuffer.toString());
    }

    protected static void a(SortBy sortBy, TreeMap<String, String> treeMap) {
        if (sortBy == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sortBy.getFieldName());
        stringBuffer.append(":");
        stringBuffer.append(sortBy.getSortType().name());
        try {
            treeMap.put("sortby", URLEncoder.encode(stringBuffer.toString(), Key.STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException unused) {
            treeMap.put("sortby", stringBuffer.toString());
        }
    }

    public static void a(BaseRequest baseRequest, int i, boolean z, int i2, String str, OnEntityListener onEntityListener) {
        String str2;
        int tag = baseRequest.getTag();
        switch (i) {
            case 1:
                EntityListResponse entityListResponse = new EntityListResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((EntityListRequest) baseRequest).getCoordTypeOutput(), entityListResponse);
                }
                onEntityListener.onEntityListCallback(entityListResponse);
                return;
            case 2:
                AddEntityResponse addEntityResponse = new AddEntityResponse(tag, i2, str);
                if (z) {
                    a.b(str, addEntityResponse);
                }
                onEntityListener.onAddEntityCallback(addEntityResponse);
                return;
            case 3:
                DeleteEntityResponse deleteEntityResponse = new DeleteEntityResponse(tag, i2, str);
                if (z) {
                    a.b(str, deleteEntityResponse);
                }
                onEntityListener.onDeleteEntityCallback(deleteEntityResponse);
                return;
            case 4:
                UpdateEntityResponse updateEntityResponse = new UpdateEntityResponse(tag, i2, str);
                if (z) {
                    a.b(str, updateEntityResponse);
                }
                onEntityListener.onUpdateEntityCallback(updateEntityResponse);
                return;
            case 5:
                TraceLocation traceLocation = new TraceLocation(tag, i2, str);
                if (z) {
                    traceLocation.setLocType(LocType.NET_WORK);
                    try {
                        if (a.b(str, traceLocation)) {
                            JSONObject jSONObject = new JSONObject(str);
                            if (jSONObject.has("result")) {
                                JSONObject jSONObject2 = jSONObject.getJSONObject("result");
                                if (jSONObject2.has("content")) {
                                    JSONObject jSONObject3 = jSONObject2.getJSONObject("content");
                                    if (jSONObject3.has("point")) {
                                        JSONObject jSONObject4 = jSONObject3.getJSONObject("point");
                                        if (!jSONObject4.has("y") || TextUtils.isEmpty(jSONObject4.getString("y"))) {
                                            str2 = "result";
                                        } else {
                                            str2 = "result";
                                            traceLocation.setLatitude(Double.parseDouble(jSONObject4.getString("y")));
                                        }
                                        if (jSONObject4.has("x") && !TextUtils.isEmpty(jSONObject4.getString("x"))) {
                                            traceLocation.setLongitude(Double.parseDouble(jSONObject4.getString("x")));
                                        }
                                    } else {
                                        str2 = "result";
                                    }
                                    if (jSONObject3.has("floor")) {
                                        traceLocation.setFloor(jSONObject3.getString("floor"));
                                    }
                                    if (jSONObject3.has("indoor")) {
                                        traceLocation.setIndoor(jSONObject3.getString("indoor"));
                                    }
                                    if (jSONObject3.has("bldg")) {
                                        traceLocation.setBuilding(jSONObject3.getString("bldg"));
                                    }
                                    if (jSONObject3.has("radius") && !TextUtils.isEmpty(jSONObject3.getString("radius"))) {
                                        traceLocation.setRadius(Double.parseDouble(jSONObject3.getString("radius")));
                                    }
                                } else {
                                    str2 = "result";
                                }
                                String str3 = str2;
                                if (jSONObject2.has(str3)) {
                                    JSONObject jSONObject5 = jSONObject2.getJSONObject(str3);
                                    if (jSONObject5.has("time")) {
                                        traceLocation.setTime(jSONObject5.getString("time"));
                                    }
                                }
                                traceLocation.setCoordType(CoordType.bd09ll);
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                onEntityListener.onReceiveLocation(traceLocation);
                return;
            case 6:
                SearchResponse searchResponse = new SearchResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((SearchRequest) baseRequest).getCoordTypeOutput(), searchResponse);
                }
                onEntityListener.onSearchEntityCallback(searchResponse);
                return;
            case 7:
                BoundSearchResponse boundSearchResponse = new BoundSearchResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((BoundSearchRequest) baseRequest).getCoordTypeOutput(), boundSearchResponse);
                }
                onEntityListener.onBoundSearchCallback(boundSearchResponse);
                return;
            case 8:
                AroundSearchResponse aroundSearchResponse = new AroundSearchResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((AroundSearchRequest) baseRequest).getCoordTypeOutput(), aroundSearchResponse);
                }
                onEntityListener.onAroundSearchCallback(aroundSearchResponse);
                return;
            case 9:
                PolygonSearchResponse polygonSearchResponse = new PolygonSearchResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((PolygonSearchRequest) baseRequest).getCoordTypeOutput(), polygonSearchResponse);
                }
                onEntityListener.onPolygonSearchCallback(polygonSearchResponse);
                return;
            case 10:
                DistrictSearchResponse districtSearchResponse = new DistrictSearchResponse(tag, i2, str);
                if (z) {
                    a.a(str, ((DistrictSearchRequest) baseRequest).getCoordTypeOutput(), districtSearchResponse);
                }
                onEntityListener.onDistrictSearchCallback(districtSearchResponse);
                return;
            default:
                return;
        }
    }

    protected static void a(List<LatLng> list, TreeMap<String, String> treeMap) {
        if (list == null || list.isEmpty()) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer("");
        for (LatLng latLng : list) {
            stringBuffer.append(latLng.getLatitude());
            stringBuffer.append(",");
            stringBuffer.append(latLng.getLongitude());
            stringBuffer.append(";");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            treeMap.put("vertexes", stringBuffer.toString());
        }
    }
}
