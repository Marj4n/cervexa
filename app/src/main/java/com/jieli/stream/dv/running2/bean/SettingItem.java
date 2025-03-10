package com.jieli.stream.dv.running2.bean;

import com.google.android.material.materialswitch.MaterialSwitch;

/* loaded from: classes.dex */
public class SettingItem<T> {
    private int marginTop;
    private String name;
    private OnSwitchListener onSwitchListener;
    private int type;
    private T value;

    public interface OnSwitchListener {
        void onSwitchListener(MaterialSwitch switchButton, SettingItem<Boolean> settingItem, boolean z);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T t) {
        this.value = t;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int i) {
        this.marginTop = i;
    }

    public OnSwitchListener getOnSwitchListener() {
        return this.onSwitchListener;
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }
}
