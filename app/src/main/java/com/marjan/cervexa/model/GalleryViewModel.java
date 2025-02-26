package com.marjan.cervexa.model;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

/* loaded from: classes.dex */
public class GalleryViewModel implements Observable {
    private int editVisible = 8;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    public void setEditVisible(int i) {
        this.editVisible = i;
        notifyChange(2);
    }

    @Bindable
    public int getEditVisible() {
        return this.editVisible;
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
