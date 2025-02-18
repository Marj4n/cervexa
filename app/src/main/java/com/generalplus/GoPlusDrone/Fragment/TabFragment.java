package com.generalplus.GoPlusDrone.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.generalplus.GoPlusDrone.R;
import com.generalplus.GoPlusDrone.View.SlidingTabLayout;
import com.generalplus.GoPlusDrone.View.TabFragmentPagerAdapter;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TabFragment extends Fragment {
    private FragmentPagerAdapter adapter;
    private int m_iSelect = 0;
    private ViewPager pager;
    private SlidingTabLayout tabs;

    public static Fragment newInstance() {
        return new TabFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_sample, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), getFragments());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        this.pager = viewPager;
        viewPager.setAdapter(this.adapter);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        this.tabs = slidingTabLayout;
        slidingTabLayout.setViewPager(this.pager);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(-1);
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private LinkedList<BaseFragment> getFragments() {
        LinkedList<BaseFragment> linkedList = new LinkedList<>();
        linkedList.add(PhotoListFragment.newInstance("Photo"));
        linkedList.add(VideoListFragment.newInstance("Video"));
        return linkedList;
    }
}
