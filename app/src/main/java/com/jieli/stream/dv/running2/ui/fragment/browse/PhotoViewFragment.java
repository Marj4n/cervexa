package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bm.library.PhotoView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhotoViewFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private PhotoViewAdapter mAdapter;
    private ViewPager mViewPager;
    private TextView tvCounter;
    private TextView tvTitle;

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_photo_view, viewGroup, false);
        this.mViewPager = (ViewPager) inflate.findViewById(R.id.photo_view_pager);
        this.tvCounter = (TextView) inflate.findViewById(R.id.photo_view_counter);
        this.tvTitle = (TextView) inflate.findViewById(R.id.photo_view_title);
        this.mViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15.0f));
        this.mViewPager.setOnPageChangeListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        Bundle bundle2;
        super.onActivityCreated(bundle);
        if (getActivity() == null || (bundle2 = getBundle()) == null) {
            return;
        }
        ArrayList<String> stringArrayList = bundle2.getStringArrayList(IConstant.KEY_PATH_LIST);
        int i = bundle2.getInt(IConstant.KEY_POSITION);
        PhotoViewAdapter photoViewAdapter = new PhotoViewAdapter(getActivity().getApplicationContext(), stringArrayList);
        this.mAdapter = photoViewAdapter;
        this.mViewPager.setAdapter(photoViewAdapter);
        if (i < 0 || i >= this.mAdapter.getCount()) {
            return;
        }
        this.mViewPager.setCurrentItem(i);
        setCounter(i, this.mAdapter.getCount());
        String item = this.mAdapter.getItem(i);
        if (TextUtils.isEmpty(item)) {
            return;
        }
        setTitle(item);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        ImageLoader.getInstance().release();
    }

    private void setCounter(int i, int i2) {
        String string = getString(R.string.counter_format, Integer.valueOf(i + 1), Integer.valueOf(i2));
        TextView textView = this.tvCounter;
        if (textView != null) {
            textView.setText(string);
        }
    }

    private void setTitle(String str) {
        if (TextUtils.isEmpty(str) || this.tvTitle == null) {
            return;
        }
        this.tvTitle.setText(formatTitle(str));
    }

    private String formatTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.contains(File.separator)) {
            return str;
        }
        return str.split(File.separator)[r2.length - 1];
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        PhotoViewAdapter photoViewAdapter = this.mAdapter;
        if (photoViewAdapter != null) {
            setCounter(i, photoViewAdapter.getCount());
            setTitle(this.mAdapter.getItem(i));
        }
    }

    private class PhotoViewAdapter extends PagerAdapter {
        private List<String> dataList;
        private Context mContext;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        PhotoViewAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.dataList = list;
        }

        public String getItem(int i) {
            List<String> list = this.dataList;
            if (list == null || i >= list.size()) {
                return null;
            }
            return this.dataList.get(i);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            List<String> list = this.dataList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            PhotoView photoView = new PhotoView(this.mContext);
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            String item = getItem(i);
            if (!TextUtils.isEmpty(item)) {
                loadThumbs(photoView, item);
            } else {
                photoView.setImageResource(R.mipmap.ic_default_picture);
            }
            viewGroup.addView(photoView);
            return photoView;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        private void loadThumbs(PhotoView photoView, String str) {
            photoView.setImageBitmap(ImageLoader.getInstance().loadImage(this.mContext, str));
        }
    }
}
