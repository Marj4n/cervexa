package com.jieli.lib.dv.control.json.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CmdInfo implements Parcelable {
    public static final Creator<CmdInfo> CREATOR = new Creator<CmdInfo>() { // from class: com.jieli.lib.dv.control.json.bean.CmdInfo.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CmdInfo createFromParcel(Parcel parcel) {
            return new CmdInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CmdInfo[] newArray(int i) {
            return new CmdInfo[i];
        }
    };
    private int b;
    private String c;
    private String d;
    private String a = getClass().getSimpleName();
    private ArrayMap<String, String> e = null;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    CmdInfo(Parcel parcel) {
        this.b = -100;
        this.d = null;
        if (parcel != null) {
            this.b = parcel.readInt();
            this.c = parcel.readString();
            this.d = parcel.readString();
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                this.e.put(parcel.readString(), parcel.readString());
            }
        }
    }

    public int getErrorType() {
        return this.b;
    }

    public void setErrorType(int i) {
        this.b = i;
    }

    public ArrayMap<String, String> getParams() {
        return this.e;
    }

    public void setParams(String str, String str2) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(str, str2);
        setParams(arrayMap);
    }

    public void setParams(String str, String str2, String str3, String str4) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(str, str2);
        arrayMap.put(str3, str4);
        setParams(arrayMap);
    }

    public void setParams(String str, String str2, String str3, String str4, String str5, String str6) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(str, str2);
        arrayMap.put(str3, str4);
        arrayMap.put(str5, str6);
        setParams(arrayMap);
    }

    public void setParams(ArrayMap<String, String> arrayMap) {
        this.e = arrayMap;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Topic:");
        sb.append(this.c);
        sb.append(", Error:");
        sb.append(this.b);
        sb.append(", Operation:");
        sb.append(this.d);
        sb.append(", params:");
        ArrayMap<String, String> arrayMap = this.e;
        sb.append(arrayMap == null ? null : arrayMap.toString());
        return sb.toString();
    }

    public String getOperation() {
        return this.d;
    }

    public void setOperation(String str) {
        this.d = str;
    }

    public String getTopic() {
        return this.c;
    }

    public void setTopic(String str) {
        this.c = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        ArrayMap<String, String> arrayMap = this.e;
        if (arrayMap != null) {
            parcel.writeInt(arrayMap.size());
            for (Map.Entry<String, String> entry : this.e.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }
    }
}
