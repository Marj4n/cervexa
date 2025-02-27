package com.gizthon.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.gizthon.camera.R;
import com.gizthon.camera.adapter.PhotoFragmentAdapter;
import com.gizthon.camera.databinding.GalleryActivityBinding;
import com.gizthon.camera.fragment.PhotoListFragment;
import com.gizthon.camera.fragment.VideoListFragment;
import com.gizthon.camera.model.GalleryViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jaeger.library.StatusBarUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GalleryListActivity extends CameraBaseActivity {
    private GalleryActivityBinding binding;
    private ArrayList<Fragment> fragments;
    private GalleryViewModel viewModel;

    public static void start(Context context) {
        context.startActivity(new Intent(context, GalleryListActivity.class));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = DataBindingUtil.setContentView(this, R.layout.gallery_activity);
        GalleryViewModel galleryViewModel = new GalleryViewModel();
        this.viewModel = galleryViewModel;
        this.binding.setViewModel(galleryViewModel);
        this.binding.setEventHandler(this);
        ArrayList<Fragment> arrayList = new ArrayList<>();
        this.fragments = arrayList;

        this.binding.back.setOnClickListener(v -> onClickBack());

        arrayList.add(PhotoListFragment.newInstance());
        this.fragments.add(VideoListFragment.newInstance());
        this.binding.pager.setAdapter(new PhotoFragmentAdapter(this, this.fragments));
        new TabLayoutMediator(this.binding.tabLayout, this.binding.pager, (tab, i) -> {
            if (i == 0) {
                tab.setText(GalleryListActivity.this.getResources().getString(R.string.photo));
            } else if (i == 1) {
                tab.setText(GalleryListActivity.this.getResources().getString(R.string.video));
            }
        }).attach();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#09B0F3"));
    }
}
