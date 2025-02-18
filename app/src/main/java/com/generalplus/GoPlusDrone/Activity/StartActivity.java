package com.generalplus.GoPlusDrone.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import com.generalplus.GoPlusDrone.Activity.StartCardActivity;
import com.generalplus.GoPlusDrone.R;
import java.util.Locale;

/* loaded from: classes.dex */
public class StartActivity extends StartCardActivity {
    private static final String Language_KEY = "Language_KEY";
    private static final String SharedPreferences_KEY = "SharedPreferences_KEY";
    private SharedPreferences m_SharedPreferences = null;
    private int m_iLocale = 0;
    private Locale[] m_locale = {Locale.ENGLISH, Locale.TRADITIONAL_CHINESE, Locale.CHINA, Locale.getDefault()};
    private Button m_bnStart = null;
    private boolean m_bIsCard = false;

    @Override // com.generalplus.GoPlusDrone.Activity.StartCardActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        this.m_bnStart = (Button) findViewById(R.id.bnStart);
        this.m_SharedPreferences = getSharedPreferences(SharedPreferences_KEY, 0);
        ((TextView) findViewById(R.id.tvName)).setText(getResources().getString(R.string.main_name));
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(1024, 1024);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(4);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        if (!shouldAskPermission() || ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.locale = this.m_locale[this.m_iLocale];
        resources.updateConfiguration(configuration, displayMetrics);
        this.m_bnStart.setText(getResources().getString(R.string.start_button));
    }

    private void showLanDialog() {
        new AlertDialog.Builder(this).setItems(new String[]{getString(R.string.item_Language1), getString(R.string.item_Language2), getString(R.string.item_Language3), getString(R.string.item_Language0)}, new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.StartActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (StartActivity.this.m_iLocale == i) {
                    return;
                }
                StartActivity.this.m_iLocale = i;
                StartActivity.this.updateLanguage();
                StartActivity.this.m_SharedPreferences.edit().putInt(StartActivity.Language_KEY, StartActivity.this.m_iLocale).commit();
            }
        }).show();
    }

    public void pressLanguage(View view) {
        showLanDialog();
    }

    private void Start() {
        if (this.m_bIsCard) {
            if (m_connectGPWifiDeviceThread == null) {
                if (this.m_Dialog == null) {
                    this.m_Dialog = new ProgressDialog(this);
                    this.m_Dialog.setCanceledOnTouchOutside(false);
                    this.m_Dialog.setMessage("Please wait ...");
                }
                this.m_Dialog.show();
                m_connectGPWifiDeviceThread = new Thread(new StartCardActivity.ConnectGPWifiDeviceRunnable());
                m_connectGPWifiDeviceThread.start();
                return;
            }
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsCard", false);
        intent.putExtras(bundle);
        intent.setClass(this, MainOldActivity.class);
        startActivity(intent);
    }

    public void pressLowDelayStart(View view) {
        Start();
    }

    public void pressStart(View view) {
        Start();
    }

    public void pressRTPStart(View view) {
        Start();
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 200) {
            return;
        }
        if ((iArr[0] == 0) || !shouldAskPermission()) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
    }

    private boolean shouldAskPermission() {
        return Build.VERSION.SDK_INT > 22;
    }
}
