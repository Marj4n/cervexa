package com.serenegiant.usb;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Size implements Parcelable {
    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() { // from class: com.serenegiant.usb.Size.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Size createFromParcel(Parcel parcel) {
            return new Size(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Size[] newArray(int i) {
            return new Size[i];
        }
    };
    public float[] fps;
    public int frameIntervalIndex;
    public int frameIntervalType;
    private String frameRates;
    public int frame_type;
    public int height;
    public int index;
    public int[] intervals;
    public int type;
    public int width;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Size(int i, int i2, int i3, int i4, int i5) {
        this.type = i;
        this.frame_type = i2;
        this.index = i3;
        this.width = i4;
        this.height = i5;
        this.frameIntervalType = -1;
        this.frameIntervalIndex = 0;
        this.intervals = null;
        updateFrameRate();
    }

    public Size(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.type = i;
        this.frame_type = i2;
        this.index = i3;
        this.width = i4;
        this.height = i5;
        this.frameIntervalType = 0;
        this.frameIntervalIndex = 0;
        this.intervals = new int[]{i6, i7, i8};
        updateFrameRate();
    }

    public Size(int i, int i2, int i3, int i4, int i5, int[] iArr) {
        this.type = i;
        this.frame_type = i2;
        this.index = i3;
        this.width = i4;
        this.height = i5;
        int length = iArr != null ? iArr.length : -1;
        if (length > 0) {
            this.frameIntervalType = length;
            int[] iArr2 = new int[length];
            this.intervals = iArr2;
            System.arraycopy(iArr, 0, iArr2, 0, length);
        } else {
            this.frameIntervalType = -1;
            this.intervals = null;
        }
        this.frameIntervalIndex = 0;
        updateFrameRate();
    }

    public Size(Size size) {
        this.type = size.type;
        this.frame_type = size.frame_type;
        this.index = size.index;
        this.width = size.width;
        this.height = size.height;
        this.frameIntervalType = size.frameIntervalType;
        this.frameIntervalIndex = size.frameIntervalIndex;
        int[] iArr = size.intervals;
        int length = iArr != null ? iArr.length : -1;
        if (length > 0) {
            int[] iArr2 = new int[length];
            this.intervals = iArr2;
            System.arraycopy(size.intervals, 0, iArr2, 0, length);
        } else {
            this.intervals = null;
        }
        updateFrameRate();
    }

    private Size(Parcel parcel) {
        this.type = parcel.readInt();
        this.frame_type = parcel.readInt();
        this.index = parcel.readInt();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.frameIntervalType = parcel.readInt();
        this.frameIntervalIndex = parcel.readInt();
        int i = this.frameIntervalType;
        if (i >= 0) {
            if (i > 0) {
                this.intervals = new int[i];
            } else {
                this.intervals = new int[3];
            }
            parcel.readIntArray(this.intervals);
        } else {
            this.intervals = null;
        }
        updateFrameRate();
    }

    public Size set(Size size) {
        if (size != null) {
            this.type = size.type;
            this.frame_type = size.frame_type;
            this.index = size.index;
            this.width = size.width;
            this.height = size.height;
            this.frameIntervalType = size.frameIntervalType;
            this.frameIntervalIndex = size.frameIntervalIndex;
            int[] iArr = size.intervals;
            int length = iArr != null ? iArr.length : -1;
            if (length > 0) {
                int[] iArr2 = new int[length];
                this.intervals = iArr2;
                System.arraycopy(size.intervals, 0, iArr2, 0, length);
            } else {
                this.intervals = null;
            }
            updateFrameRate();
        }
        return this;
    }

    public float getCurrentFrameRate() throws IllegalStateException {
        float[] fArr = this.fps;
        int length = fArr != null ? fArr.length : 0;
        int i = this.frameIntervalIndex;
        if (i >= 0 && i < length) {
            return this.fps[i];
        }
        throw new IllegalStateException("unknown frame rate or not ready");
    }

    public void setCurrentFrameRate(float f) {
        float[] fArr = this.fps;
        int i = 0;
        int length = fArr != null ? fArr.length : 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (this.fps[i] <= f) {
                break;
            } else {
                i++;
            }
        }
        this.frameIntervalIndex = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.type);
        parcel.writeInt(this.frame_type);
        parcel.writeInt(this.index);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeInt(this.frameIntervalType);
        parcel.writeInt(this.frameIntervalIndex);
        int[] iArr = this.intervals;
        if (iArr != null) {
            parcel.writeIntArray(iArr);
        }
    }

    public void updateFrameRate() {
        int i = this.frameIntervalType;
        if (i > 0) {
            this.fps = new float[i];
            for (int i2 = 0; i2 < i; i2++) {
                this.fps[i2] = 1.0E7f / this.intervals[i2];
            }
        } else if (i == 0) {
            try {
                int min = Math.min(this.intervals[0], this.intervals[1]);
                int max = Math.max(this.intervals[0], this.intervals[1]);
                int i3 = this.intervals[2];
                if (i3 > 0) {
                    int i4 = 0;
                    for (int i5 = min; i5 <= max; i5 += i3) {
                        i4++;
                    }
                    this.fps = new float[i4];
                    int i6 = 0;
                    while (min <= max) {
                        this.fps[i6] = 1.0E7f / min;
                        min += i3;
                        i6++;
                    }
                } else {
                    float f = 1.0E7f / min;
                    int i7 = 0;
                    for (float f2 = f; f2 <= f; f2 += 1.0f) {
                        i7++;
                    }
                    this.fps = new float[i7];
                    float f3 = f;
                    int i8 = 0;
                    while (f3 <= f) {
                        int i9 = i8 + 1;
                        this.fps[i8] = f3;
                        f3 += 1.0f;
                        i8 = i9;
                    }
                }
            } catch (Exception unused) {
                this.fps = null;
            }
        }
        float[] fArr = this.fps;
        int length = fArr != null ? fArr.length : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i10 = 0; i10 < length; i10++) {
            sb.append(String.format(Locale.US, "%4.1f", Float.valueOf(this.fps[i10])));
            if (i10 < length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.frameRates = sb.toString();
        if (this.frameIntervalIndex > length) {
            this.frameIntervalIndex = 0;
        }
    }

    public String toString() {
        float f;
        try {
            f = getCurrentFrameRate();
        } catch (Exception unused) {
            f = 0.0f;
        }
        return String.format(Locale.US, "Size(%dx%d@%4.1f,type:%d,frame:%d,index:%d,%s)", Integer.valueOf(this.width), Integer.valueOf(this.height), Float.valueOf(f), Integer.valueOf(this.type), Integer.valueOf(this.frame_type), Integer.valueOf(this.index), this.frameRates);
    }
}
