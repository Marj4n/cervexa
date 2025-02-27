package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.generalplus.GoPlusDrone.View.MultiTouchZoomableImageView;
import com.gizthon.camera.R;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class FullImageActivity extends Activity {
    private FullImageAdapter adapter;
    private ArrayList<String> m_ayFilePath = null;
    private ViewPager viewPager;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fullscreen_view);
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        Intent intent = getIntent();
        int i = intent.getExtras().getInt(IConstant.KEY_POSITION);
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("FilePath");
        this.m_ayFilePath = stringArrayListExtra;
        FullImageAdapter fullImageAdapter = new FullImageAdapter(this, stringArrayListExtra);
        this.adapter = fullImageAdapter;
        this.viewPager.setAdapter(fullImageAdapter);
        this.viewPager.setCurrentItem(i);
    }

    public class FullImageAdapter extends PagerAdapter {
        private Activity _activity;
        private ArrayList<String> _imagePaths;
        private LayoutInflater inflater;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public FullImageAdapter(Activity activity, ArrayList<String> arrayList) {
            this._activity = activity;
            this._imagePaths = arrayList;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = (LayoutInflater) this._activity.getSystemService("layout_inflater");
            this.inflater = layoutInflater;
            View inflate = layoutInflater.inflate(R.layout.layout_fullscreen_image, viewGroup, false);
            ((MultiTouchZoomableImageView) inflate.findViewById(R.id.imgDisplay)).setImageBitmap(BitmapFactory.decodeFile(this._imagePaths.get(i)));
            ((ViewPager) viewGroup).addView(inflate);
            return inflate;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            ((ViewPager) viewGroup).removeView((RelativeLayout) obj);
        }
    }
}
