package com.marjan.cervexa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.marjan.cervexa.adapter.PhotoFragmentAdapter;
import com.marjan.cervexa.databinding.GalleryActivityBinding;
import com.marjan.cervexa.fragment.PhotoListFragment;
import com.marjan.cervexa.fragment.VideoListFragment;
import com.marjan.cervexa.model.GalleryViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jaeger.library.StatusBarUtil;
import com.weioa.GoPlusDrone.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GalleryListActivity extends CameraBaseActivity {
    private GalleryActivityBinding binding;
    private ArrayList<Fragment> fragments;
    private GalleryViewModel viewModel;

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) GalleryListActivity.class));
    }

    @Override // com.marjan.cervexa.activity.CameraBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (GalleryActivityBinding) DataBindingUtil.setContentView(this, R.layout.gallery_activity);
        GalleryViewModel galleryViewModel = new GalleryViewModel();
        this.viewModel = galleryViewModel;
        this.binding.setViewModel(galleryViewModel);
        this.binding.setEventHandler(this);
        ArrayList<Fragment> arrayList = new ArrayList<>();
        this.fragments = arrayList;
        arrayList.add(PhotoListFragment.newInstance());
        this.fragments.add(VideoListFragment.newInstance());
        this.binding.pager.setAdapter(new PhotoFragmentAdapter(this, this.fragments));
        new TabLayoutMediator(this.binding.tabLayout, this.binding.pager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.marjan.cervexa.activity.GalleryListActivity.1
            @Override // com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText(GalleryListActivity.this.getResources().getString(R.string.photo));
                } else if (i == 1) {
                    tab.setText(GalleryListActivity.this.getResources().getString(R.string.video));
                }
            }
        }).attach();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#09B0F3"));
    }

    @Override // com.marjan.cervexa.activity.CameraBaseActivity
    public void onClickBack() {
        finish();
    }

    public void onClickDelete() {
        Fragment fragment = this.fragments.get(this.binding.pager.getCurrentItem());
        if (fragment instanceof PhotoListFragment) {
            ((PhotoListFragment) fragment).deleteSelected();
            onShowDone(false);
        }
        if (fragment instanceof VideoListFragment) {
            ((VideoListFragment) fragment).deleteSelected();
            onShowDone(false);
        }
    }

    public void onClickDone() {
        Fragment fragment = this.fragments.get(this.binding.pager.getCurrentItem());
        if (fragment instanceof PhotoListFragment) {
            ((PhotoListFragment) fragment).resetStatus(this.binding.done.isChecked());
        }
        if (fragment instanceof VideoListFragment) {
            ((VideoListFragment) fragment).resetStatus(this.binding.done.isChecked());
        }
    }

    public void onClickComplete() {
        onShowDone(false);
        Fragment fragment = this.fragments.get(this.binding.pager.getCurrentItem());
        if (fragment instanceof PhotoListFragment) {
            ((PhotoListFragment) fragment).resetComplete();
        }
        if (fragment instanceof VideoListFragment) {
            ((VideoListFragment) fragment).resetComplete();
        }
    }

    public void onShowDone(boolean z) {
        this.viewModel.setEditVisible(z ? 0 : 8);
        if (z) {
            return;
        }
        this.binding.done.setChecked(false);
    }
}
