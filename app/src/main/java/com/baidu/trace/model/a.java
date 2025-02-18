package com.baidu.trace.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class a implements Parcelable.Creator<TraceLocation> {
    a() {
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ TraceLocation createFromParcel(Parcel parcel) {
        return new TraceLocation(parcel, (byte) 0);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ TraceLocation[] newArray(int i) {
        return new TraceLocation[i];
    }
}
