package com.generalplus.GoPlusDrone.Activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;
import com.generalplus.GoPlusDrone.Fragment.PhotoListFragment;
import com.generalplus.GoPlusDrone.Fragment.VideoListFragment;
import com.generalplus.GoPlusDrone.R;
import java.util.Locale;

/* loaded from: classes.dex */
public class GalleryActivity extends AppCompatActivity {
    public static boolean m_bEdit = false;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery);
        m_bEdit = false;
        final FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(getResources().getString(R.string.tab_photo)).setIndicator(getResources().getString(R.string.tab_photo)), PhotoListFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(getResources().getString(R.string.tab_video)).setIndicator(getResources().getString(R.string.tab_video)), VideoListFragment.class, null);
        fragmentTabHost.getTabWidget().getChildTabViewAt(0).setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.GalleryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GalleryActivity.m_bEdit && fragmentTabHost.getCurrentTab() != 0) {
                    GalleryActivity galleryActivity = GalleryActivity.this;
                    Toast.makeText(galleryActivity, galleryActivity.getResources().getString(R.string.tab_DoneTip), 0).show();
                } else {
                    fragmentTabHost.setCurrentTab(0);
                }
            }
        });
        fragmentTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.GalleryActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GalleryActivity.m_bEdit && 1 != fragmentTabHost.getCurrentTab()) {
                    GalleryActivity galleryActivity = GalleryActivity.this;
                    Toast.makeText(galleryActivity, galleryActivity.getResources().getString(R.string.tab_DoneTip), 0).show();
                } else {
                    fragmentTabHost.setCurrentTab(1);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(-1);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(getResources().getString(R.string.tab_back));
        }
    }

    public boolean isCN() {
        return getApplicationContext().getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    private void updateLanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.locale = Locale.ENGLISH;
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
