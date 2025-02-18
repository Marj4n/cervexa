package com.gizthon.camera.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhotoFragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;

    public PhotoFragmentAdapter(FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.fragments = new ArrayList();
        this.fragments = list;
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        return this.fragments.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.fragments.size();
    }
}
