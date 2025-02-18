package com.baidu.mapapi.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class a implements Parcelable.Creator<LatLng> {
    a() {
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LatLng createFromParcel(Parcel parcel) {
        return new LatLng(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LatLng[] newArray(int i) {
        return new LatLng[i];
    }
}
