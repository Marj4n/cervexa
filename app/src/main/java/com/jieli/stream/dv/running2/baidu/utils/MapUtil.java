package com.jieli.stream.dv.running2.baidu.utils;

import android.graphics.Color;
import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.TraceLocation;
import com.jieli.stream.dv.running2.util.Dbug;
import java.util.Iterator;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public class MapUtil {
    private static MapUtil INSTANCE = new MapUtil();
    String tag = getClass().getSimpleName();
    private MapStatus mapStatus = null;
    private Marker mMoveMarker = null;
    public MapView mapView = null;
    public BaiduMap baiduMap = null;
    public LatLng lastPoint = null;
    public Overlay polylineOverlay = null;

    private MapUtil() {
    }

    public static MapUtil getInstance() {
        return INSTANCE;
    }

    public void init(MapView mapView) {
        this.mapView = mapView;
        this.baiduMap = mapView.getMap();
        this.mapView.showZoomControls(false);
    }

    public void onPause() {
        MapView mapView = this.mapView;
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void onResume() {
        MapView mapView = this.mapView;
        if (mapView != null) {
            mapView.onResume();
        }
    }

    public void clear() {
        this.lastPoint = null;
        Marker marker = this.mMoveMarker;
        if (marker != null) {
            marker.remove();
            this.mMoveMarker = null;
        }
        Overlay overlay = this.polylineOverlay;
        if (overlay != null) {
            overlay.remove();
            this.polylineOverlay = null;
        }
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.clear();
            this.baiduMap = null;
        }
        this.mapStatus = null;
        MapView mapView = this.mapView;
        if (mapView != null) {
            mapView.onDestroy();
            this.mapView = null;
        }
    }

    public static LatLng convertTraceLocation2Map(TraceLocation traceLocation) {
        if (traceLocation == null) {
            return null;
        }
        double latitude = traceLocation.getLatitude();
        double longitude = traceLocation.getLongitude();
        if (Math.abs(latitude - 1.0E-10) < 1.0E-6d && Math.abs(longitude - 1.0E-10) < 1.0E-6d) {
            return null;
        }
        LatLng latLng = new LatLng(latitude, longitude);
        if (CoordType.wgs84 != traceLocation.getCoordType()) {
            return latLng;
        }
        CoordinateConverter coordinateConverter = new CoordinateConverter();
        coordinateConverter.from(CoordinateConverter.CoordType.GPS);
        coordinateConverter.coord(latLng);
        return coordinateConverter.convert();
    }

    public static com.baidu.trace.model.LatLng convertMap2Trace(LatLng latLng) {
        return new com.baidu.trace.model.LatLng(latLng.latitude, latLng.longitude);
    }

    public static LatLng convertTrace2Map(com.baidu.trace.model.LatLng latLng) {
        return new LatLng(latLng.latitude, latLng.longitude);
    }

    public Marker addOverlay(LatLng latLng, BitmapDescriptor bitmapDescriptor, Bundle bundle) {
        Marker marker = (Marker) this.baiduMap.addOverlay(new MarkerOptions().position(latLng).icon(bitmapDescriptor).zIndex(9).draggable(true));
        if (bundle != null) {
            marker.setExtraInfo(bundle);
        }
        return marker;
    }

    public void addMarker(LatLng latLng) {
        Marker marker = this.mMoveMarker;
        if (marker == null) {
            this.mMoveMarker = addOverlay(latLng, BitmapUtil.bmArrowPoint, null);
        } else if (this.lastPoint != null) {
            moveLooper(latLng);
        } else {
            this.lastPoint = latLng;
            marker.setPosition(latLng);
        }
    }

    public void moveLooper(LatLng latLng) {
        LatLng latLng2;
        this.mMoveMarker.setPosition(this.lastPoint);
        this.mMoveMarker.setRotate((float) CommonUtil.getAngle(this.lastPoint, latLng));
        double slope = CommonUtil.getSlope(this.lastPoint, latLng);
        boolean z = this.lastPoint.latitude > latLng.latitude;
        double interception = CommonUtil.getInterception(slope, this.lastPoint);
        double xMoveDistance = z ? CommonUtil.getXMoveDistance(slope) : (-1.0d) * CommonUtil.getXMoveDistance(slope);
        double d = this.lastPoint.latitude;
        while (true) {
            if ((d > latLng.latitude) != z) {
                return;
            }
            if (slope != Double.MAX_VALUE) {
                latLng2 = new LatLng(d, (d - interception) / slope);
            } else {
                latLng2 = new LatLng(d, this.lastPoint.longitude);
            }
            this.mMoveMarker.setPosition(latLng2);
            d -= xMoveDistance;
        }
    }

    public void drawHistoryTrack(List<LatLng> list, SortType sortType) {
        LatLng latLng;
        this.baiduMap.clear();
        if (list == null || list.size() == 0) {
            Overlay overlay = this.polylineOverlay;
            if (overlay != null) {
                overlay.remove();
                this.polylineOverlay = null;
                return;
            }
            return;
        }
        if (list.size() == 1) {
            this.baiduMap.addOverlay(new MarkerOptions().position(list.get(0)).icon(BitmapUtil.bmStart).zIndex(9).draggable(true));
            animateMapStatus(list.get(0), 18.0f);
            return;
        }
        if (sortType == SortType.asc) {
            latLng = list.get(0);
        } else {
            latLng = list.get(list.size() - 1);
        }
        MarkerOptions draggable = new MarkerOptions().position(latLng).icon(BitmapUtil.bmStart).zIndex(9).draggable(true);
        PolylineOptions points = new PolylineOptions().width(10).color(Color.rgb(30, IjkMediaMeta.FF_PROFILE_H264_HIGH_444, 255)).points(list);
        this.baiduMap.addOverlay(draggable);
        this.polylineOverlay = this.baiduMap.addOverlay(points);
        this.mMoveMarker = (Marker) this.baiduMap.addOverlay(new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(BitmapUtil.bmArrowPoint).position(list.get(list.size() - 1)).rotate((float) CommonUtil.getAngle(list.get(0), list.get(1))));
        animateMapStatus(list);
    }

    public void animateMapStatus(List<LatLng> list) {
        if (list == null || list.isEmpty()) {
            Dbug.e(this.tag, "animateMapStatus:null error");
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Iterator<LatLng> it = list.iterator();
        while (it.hasNext()) {
            builder.include(it.next());
        }
        MapStatusUpdate newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(builder.build());
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.animateMapStatus(newLatLngBounds);
        }
    }

    public void animateMapStatus(LatLng latLng, float f) {
        MapStatus build = new MapStatus.Builder().target(latLng).zoom(f).build();
        this.mapStatus = build;
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(build));
        }
    }

    public void setMapStatus(LatLng latLng, float f) {
        MapStatus build = new MapStatus.Builder().target(latLng).zoom(f).build();
        this.mapStatus = build;
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(build));
        }
    }

    public void refresh() {
        setMapStatus(this.baiduMap.getMapStatus().target, this.baiduMap.getMapStatus().zoom - 1.0f);
    }

    public void addEndPointOverlay(List<LatLng> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        MarkerOptions draggable = new MarkerOptions().position(list.get(list.size() - 1)).icon(BitmapUtil.bmEnd).zIndex(9).draggable(true);
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.addOverlay(draggable);
        }
    }

    public void clearMap() {
        BaiduMap baiduMap = this.baiduMap;
        if (baiduMap != null) {
            baiduMap.clear();
        }
    }
}
