package com.xyzlf.share.library.bean;

/* loaded from: classes2.dex */
public class ChannelEntity {
    private int channel;
    private int icon;
    private String name;

    public ChannelEntity(int i, int i2, String str) {
        this.channel = i;
        this.icon = i2;
        this.name = str;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int i) {
        this.icon = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getchannel() {
        return this.channel;
    }

    public void setchannel(int i) {
        this.channel = i;
    }
}
