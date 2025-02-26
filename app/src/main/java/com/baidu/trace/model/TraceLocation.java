package com.baidu.trace.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class TraceLocation extends BaseResponse implements Parcelable {
    public static final Parcelable.Creator<TraceLocation> CREATOR = new a();
    private LocType a;
    private String b;
    private String c;
    private String d;
    private double e;
    private double f;
    private CoordType g;
    private double h;
    private float i;
    private float j;
    private int k;
    private String l;

    public TraceLocation() {
        this.a = LocType.NONE;
        this.b = "";
        this.c = "";
        this.d = "";
        this.g = CoordType.bd09ll;
        this.i = 0.0f;
        this.j = 0.0f;
        this.k = 0;
        this.l = "";
    }

    public TraceLocation(int i, int i2, String str) {
        super(i, i2, str);
        this.a = LocType.NONE;
        this.b = "";
        this.c = "";
        this.d = "";
        this.g = CoordType.bd09ll;
        this.i = 0.0f;
        this.j = 0.0f;
        this.k = 0;
        this.l = "";
    }

    public TraceLocation(int i, int i2, String str, String str2, String str3, String str4, double d, double d2, CoordType coordType, double d3, float f, float f2, int i3, String str5) {
        super(i, i2, str);
        this.a = LocType.NONE;
        this.b = "";
        this.c = "";
        this.d = "";
        this.g = CoordType.bd09ll;
        this.i = 0.0f;
        this.j = 0.0f;
        this.k = 0;
        this.l = "";
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = d;
        this.f = d2;
        this.g = coordType;
        this.h = d3;
        this.i = f;
        this.j = f2;
        this.k = i3;
        this.l = str5;
    }

    private TraceLocation(Parcel parcel) {
        this.a = LocType.NONE;
        this.b = "";
        this.c = "";
        this.d = "";
        this.g = CoordType.bd09ll;
        this.i = 0.0f;
        this.j = 0.0f;
        this.k = 0;
        this.l = "";
        readFromParcel(parcel);
    }

    /* synthetic */ TraceLocation(Parcel parcel, byte b) {
        this(parcel);
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final int getAltitude() {
        return this.k;
    }

    public final String getBuilding() {
        return this.d;
    }

    public final CoordType getCoordType() {
        return this.g;
    }

    public final float getDirection() {
        return this.i;
    }

    public final String getFloor() {
        return this.b;
    }

    public final String getIndoor() {
        return this.c;
    }

    public final double getLatitude() {
        return this.e;
    }

    public final LocType getLocType() {
        return this.a;
    }

    public final double getLongitude() {
        return this.f;
    }

    public final double getRadius() {
        return this.h;
    }

    public final float getSpeed() {
        return this.j;
    }

    public final String getTime() {
        return this.l;
    }

    public final void readFromParcel(Parcel parcel) {
        CoordType coordType;
        this.b = parcel.readString();
        this.c = parcel.readString();
        this.d = parcel.readString();
        this.e = parcel.readDouble();
        this.f = parcel.readDouble();
        int readInt = parcel.readInt();
        if (readInt == 0) {
            coordType = CoordType.wgs84;
        } else {
            if (readInt != 1) {
                if (readInt == 2) {
                    coordType = CoordType.bd09ll;
                }
                this.h = parcel.readDouble();
                this.i = parcel.readFloat();
                this.j = parcel.readFloat();
                this.k = parcel.readInt();
                this.l = parcel.readString();
            }
            coordType = CoordType.gcj02;
        }
        this.g = coordType;
        this.h = parcel.readDouble();
        this.i = parcel.readFloat();
        this.j = parcel.readFloat();
        this.k = parcel.readInt();
        this.l = parcel.readString();
    }

    public final void setAltitude(int i) {
        this.k = i;
    }

    public final void setBuilding(String str) {
        this.d = str;
    }

    public final void setCoordType(CoordType coordType) {
        this.g = coordType;
    }

    public final void setDirection(float f) {
        this.i = f;
    }

    public final void setFloor(String str) {
        this.b = str;
    }

    public final void setIndoor(String str) {
        this.c = str;
    }

    public final void setLatitude(double d) {
        this.e = d;
    }

    public final void setLocType(LocType locType) {
        this.a = locType;
    }

    public final void setLongitude(double d) {
        this.f = d;
    }

    public final void setRadius(double d) {
        this.h = d;
    }

    public final void setSpeed(float f) {
        this.j = f;
    }

    public final void setTime(String str) {
        this.l = str;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("TraceLocation{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", status=");
        stringBuffer.append(this.status);
        stringBuffer.append(", message='");
        stringBuffer.append(this.message);
        stringBuffer.append('\'');
        stringBuffer.append(", locType=");
        stringBuffer.append(this.a);
        stringBuffer.append(", floor='");
        stringBuffer.append(this.b);
        stringBuffer.append('\'');
        stringBuffer.append(", indoor='");
        stringBuffer.append(this.c);
        stringBuffer.append('\'');
        stringBuffer.append(", building='");
        stringBuffer.append(this.d);
        stringBuffer.append('\'');
        stringBuffer.append(", latitude=");
        stringBuffer.append(this.e);
        stringBuffer.append(", longitude=");
        stringBuffer.append(this.f);
        stringBuffer.append(", coordType=");
        stringBuffer.append(this.g);
        stringBuffer.append(", radius=");
        stringBuffer.append(this.h);
        stringBuffer.append(", direction=");
        stringBuffer.append(this.i);
        stringBuffer.append(", speed=");
        stringBuffer.append(this.j);
        stringBuffer.append(", altitude=");
        stringBuffer.append(this.k);
        stringBuffer.append(", time='");
        stringBuffer.append(this.l);
        stringBuffer.append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeDouble(this.e);
        parcel.writeDouble(this.f);
        parcel.writeInt(this.g.ordinal());
        parcel.writeDouble(this.h);
        parcel.writeFloat(this.i);
        parcel.writeFloat(this.j);
        parcel.writeInt(this.k);
        parcel.writeString(this.l);
    }
}
