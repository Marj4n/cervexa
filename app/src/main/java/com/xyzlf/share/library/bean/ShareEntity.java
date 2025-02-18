package com.xyzlf.share.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class ShareEntity implements Parcelable {
    public static final Parcelable.Creator<ShareEntity> CREATOR = new Parcelable.Creator<ShareEntity>() { // from class: com.xyzlf.share.library.bean.ShareEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareEntity createFromParcel(Parcel parcel) {
            return new ShareEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareEntity[] newArray(int i) {
            return new ShareEntity[i];
        }
    };
    private String content;
    private int drawableId;
    private String imgUrl;
    private boolean isShareBigImg;
    private String title;
    private String url;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ShareEntity(String str, String str2) {
        this(str, str2, null);
    }

    public ShareEntity(String str, String str2, String str3) {
        this(str, str2, str3, null);
    }

    public ShareEntity(String str, String str2, String str3, String str4) {
        this.title = str;
        this.content = str2;
        this.url = str3;
        this.imgUrl = str4;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String str) {
        this.imgUrl = str;
    }

    public void setDrawableId(int i) {
        this.drawableId = i;
    }

    public int getDrawableId() {
        return this.drawableId;
    }

    public void setShareBigImg(boolean z) {
        this.isShareBigImg = z;
    }

    public boolean isShareBigImg() {
        return this.isShareBigImg;
    }

    protected ShareEntity(Parcel parcel) {
        this.title = parcel.readString();
        this.content = parcel.readString();
        this.url = parcel.readString();
        this.imgUrl = parcel.readString();
        this.drawableId = parcel.readInt();
        this.isShareBigImg = parcel.readByte() != 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.content);
        parcel.writeString(this.url);
        parcel.writeString(this.imgUrl);
        parcel.writeInt(this.drawableId);
        parcel.writeByte(this.isShareBigImg ? (byte) 1 : (byte) 0);
    }
}
