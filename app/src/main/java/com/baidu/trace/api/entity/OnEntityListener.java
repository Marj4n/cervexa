package com.baidu.trace.api.entity;

import com.baidu.trace.model.TraceLocation;

/* loaded from: classes.dex */
public abstract class OnEntityListener {
    public void onAddEntityCallback(AddEntityResponse addEntityResponse) {
    }

    public void onAroundSearchCallback(AroundSearchResponse aroundSearchResponse) {
    }

    public void onBoundSearchCallback(BoundSearchResponse boundSearchResponse) {
    }

    public void onDeleteEntityCallback(DeleteEntityResponse deleteEntityResponse) {
    }

    public void onDistrictSearchCallback(DistrictSearchResponse districtSearchResponse) {
    }

    public void onEntityListCallback(EntityListResponse entityListResponse) {
    }

    public void onPolygonSearchCallback(PolygonSearchResponse polygonSearchResponse) {
    }

    public void onReceiveLocation(TraceLocation traceLocation) {
    }

    public void onSearchEntityCallback(SearchResponse searchResponse) {
    }

    public void onUpdateEntityCallback(UpdateEntityResponse updateEntityResponse) {
    }
}
