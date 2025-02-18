package com.generalplus.GoPlusDrone.Activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.generalplus.GoPlusDrone.Fragment.TabFragment;
import com.generalplus.GoPlusDrone.R;
import java.util.Locale;

/* loaded from: classes.dex */
public class MainOldActivity extends AppCompatActivity {
    private TabFragment m_TabFragment = null;
    private boolean m_bIsCard = true;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.content_main);
        this.m_bIsCard = getIntent().getExtras().getBoolean("IsCard");
        displayView(R.id.nav_camera);
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

    public void displayView(int i) {
        TabFragment tabFragment;
        if (i == R.id.nav_slideshow) {
            if (this.m_TabFragment == null) {
                this.m_TabFragment = new TabFragment();
            }
            tabFragment = this.m_TabFragment;
        } else {
            tabFragment = null;
        }
        if (tabFragment != null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.content_frame, tabFragment);
            beginTransaction.commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }
}
