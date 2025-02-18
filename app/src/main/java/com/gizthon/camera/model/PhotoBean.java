package com.gizthon.camera.model;

import android.graphics.Bitmap;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

/* loaded from: classes.dex */
public class PhotoBean implements Observable {
    private String aviPngName;
    private Bitmap bitmap;
    private int index;
    private String name;
    private String path;
    private boolean selected;
    private int selectedCoverVisible = 8;
    private int videoVisible = 8;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyChange(1);
    }

    public void setAviPngName(String str) {
        this.aviPngName = str;
    }

    public String getAviPngName() {
        return this.aviPngName;
    }

    @Bindable
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setSelected(boolean z) {
        this.selected = z;
        notifyChange(7);
    }

    public void setSelectedCoverVisible(int i) {
        this.selectedCoverVisible = i;
        notifyChange(8);
    }

    public void setVideoVisible(int i) {
        this.videoVisible = i;
        notifyChange(9);
    }

    @Bindable
    public int getVideoVisible() {
        return this.videoVisible;
    }

    @Bindable
    public int getSelectedCoverVisible() {
        return this.selectedCoverVisible;
    }

    @Bindable
    public boolean isSelected() {
        return this.selected;
    }

    @Bindable
    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
        notifyChange(6);
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange(5);
    }

    @Bindable
    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
        notifyChange(4);
    }

    private synchronized void notifyChange(int i) {
        if (this.propertyChangeRegistry == null) {
            this.propertyChangeRegistry = new PropertyChangeRegistry();
        }
        this.propertyChangeRegistry.notifyChange(this, i);
    }

    @Override // androidx.databinding.Observable
    public synchronized void addOnPropertyChangedCallback(Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        if (this.propertyChangeRegistry == null) {
            this.propertyChangeRegistry = new PropertyChangeRegistry();
        }
        this.propertyChangeRegistry.add(onPropertyChangedCallback);
    }

    @Override // androidx.databinding.Observable
    public synchronized void removeOnPropertyChangedCallback(Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        if (this.propertyChangeRegistry != null) {
            this.propertyChangeRegistry.remove(onPropertyChangedCallback);
        }
    }
}
