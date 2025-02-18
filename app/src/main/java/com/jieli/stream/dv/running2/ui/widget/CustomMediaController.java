package com.jieli.stream.dv.running2.ui.widget;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import com.jieli.stream.dv.running2.ui.widget.media.IMediaController;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CustomMediaController extends MediaController implements IMediaController {
    private ActionBar mActionBar;
    private ArrayList<View> mShowOnceArray;

    public CustomMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowOnceArray = new ArrayList<>();
    }

    public CustomMediaController(Context context, boolean z) {
        super(context, z);
        this.mShowOnceArray = new ArrayList<>();
    }

    public CustomMediaController(Context context) {
        super(context);
        this.mShowOnceArray = new ArrayList<>();
    }

    public void setSupportActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void showOnce(View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(0);
        show();
    }

    @Override // android.widget.MediaController, com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void show() {
        super.show();
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override // android.widget.MediaController, com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void hide() {
        super.hide();
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.hide();
        }
        Iterator<View> it = this.mShowOnceArray.iterator();
        while (it.hasNext()) {
            it.next().setVisibility(8);
        }
        this.mShowOnceArray.clear();
    }
}
