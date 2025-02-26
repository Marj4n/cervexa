package com.marjan.cervexa.generated.callback;

import android.view.View;

/* loaded from: classes.dex */
public final class OnLongClickListener implements View.OnLongClickListener {
    final Listener mListener;
    final int mSourceId;

    public interface Listener {
        boolean _internalCallbackOnLongClick(int i, View view);
    }

    public OnLongClickListener(Listener listener, int i) {
        this.mListener = listener;
        this.mSourceId = i;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        return this.mListener._internalCallbackOnLongClick(this.mSourceId, view);
    }
}
