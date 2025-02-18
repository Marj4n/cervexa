package com.serenegiant.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class BluetoothDeviceInfo implements Parcelable {
    public static final Parcelable.Creator<BluetoothDeviceInfo> CREATOR = new Parcelable.Creator<BluetoothDeviceInfo>() { // from class: com.serenegiant.bluetooth.BluetoothDeviceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothDeviceInfo createFromParcel(Parcel parcel) {
            return new BluetoothDeviceInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothDeviceInfo[] newArray(int i) {
            return new BluetoothDeviceInfo[i];
        }
    };
    public final String address;
    public final int bondState;
    public final int deviceClass;
    public final String name;
    public final int type;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    BluetoothDeviceInfo(BluetoothDevice bluetoothDevice) {
        this.name = bluetoothDevice.getName();
        this.address = bluetoothDevice.getAddress();
        if (Build.VERSION.SDK_INT >= 18) {
            this.type = bluetoothDevice.getType();
        } else {
            this.type = 0;
        }
        BluetoothClass bluetoothClass = bluetoothDevice.getBluetoothClass();
        this.deviceClass = bluetoothClass != null ? bluetoothClass.getDeviceClass() : 0;
        this.bondState = bluetoothDevice.getBondState();
    }

    protected BluetoothDeviceInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.address = parcel.readString();
        this.type = parcel.readInt();
        this.deviceClass = parcel.readInt();
        this.bondState = parcel.readInt();
    }

    public boolean isPaired() {
        return this.bondState == 12;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.address);
        parcel.writeInt(this.type);
        parcel.writeInt(this.deviceClass);
        parcel.writeInt(this.bondState);
    }

    public String toString() {
        return String.format("BluetoothDeviceInfo(%s/%s)", this.name, this.address);
    }
}
