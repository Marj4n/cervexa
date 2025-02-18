package com.gizthon.camera.view;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.jieli.stream.dv.running2.ui.widget.media.IMediaController;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class AndroidMediaController extends PlayerMediaController implements IMediaController {
    private ActionBar mActionBar;
    private ArrayList<View> mShowOnceArray;

    protected void initView(Context context) {
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void setAnchorView(View view) {
    }

    public void setLiveRoomCount(String str) {
    }

    public AndroidMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context, boolean z) {
        super(context);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context) {
        super(context);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public void setSupportActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override // com.gizthon.camera.view.PlayerMediaController, com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void show() {
        super.show();
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override // com.gizthon.camera.view.PlayerMediaController, com.jieli.stream.dv.running2.ui.widget.media.IMediaController
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

    @Override // com.jieli.stream.dv.running2.ui.widget.media.IMediaController
    public void showOnce(View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(0);
        show();
    }

    @Override // com.gizthon.camera.view.PlayerMediaController
    public void setAnchorView(ViewGroup viewGroup) {
        super.setAnchorView(viewGroup);
    }
}
