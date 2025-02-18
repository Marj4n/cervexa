package com.jieli.stream.dv.running2.ui.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;

/* loaded from: classes.dex */
public class FlashActivity extends BaseActivity implements View.OnClickListener {
    private Button mStartButton;
    private TextView mUserAgreementText;
    private NotifyDialog notifyDialog;
    private NotifyDialog notifyGpsDialog;
    private NotifyDialog requestPermissionDialog;
    private Intent toWriteSettingIntent;
    private Handler handler = new Handler();
    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.9
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            FlashActivity.this.mStartButton.setEnabled(z);
            PreferencesHelper.putBooleanValue(FlashActivity.this.mApplication, IConstant.KEY_HAS_AGREED, z);
            if (z) {
                FlashActivity.this.mStartButton.setBackground(FlashActivity.this.getResources().getDrawable(R.drawable.bg_orange_gradient));
            } else {
                FlashActivity.this.mStartButton.setBackground(FlashActivity.this.getResources().getDrawable(R.drawable.bg_gray_gradient));
            }
        }
    };

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_flash);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.agreement_layout);
        ImageView imageView = (ImageView) findViewById(R.id.welcome_view);
        if (PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.KEY_HAS_AGREED, false)) {
            linearLayout.setVisibility(8);
            imageView.setVisibility(0);
        } else {
            linearLayout.setVisibility(0);
            imageView.setVisibility(8);
        }
        this.mUserAgreementText = (TextView) findViewById(R.id.user_agreement_text);
        ((CheckBox) findViewById(R.id.agreement_checkbox)).setOnCheckedChangeListener(this.onCheckedChangeListener);
        Button button = (Button) findViewById(R.id.start_button);
        this.mStartButton = button;
        button.setEnabled(false);
        this.mUserAgreementText.getPaint().setFlags(8);
        this.mUserAgreementText.getPaint().setAntiAlias(true);
        this.mUserAgreementText.setOnClickListener(this);
        this.mStartButton.setOnClickListener(this);
        if (this.mWifiHelper.isWifiOpen()) {
            checkPermissions();
        } else {
            showNotifyDialog();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    private void toMainActivity() {
        enter(2000L);
    }

    private void enter(long j) {
        if (!PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.KEY_HAS_AGREED, false)) {
            Dbug.e(getClass().getSimpleName(), "You are not allowed to enter without agree with the user agreement");
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (!((LocationManager) getSystemService("location")).isProviderEnabled("gps")) {
                showNotifyGPSDialog();
                return;
            } else {
                this.handler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FlashActivity.this.startActivity(new Intent(FlashActivity.this, (Class<?>) MainActivity.class));
                        FlashActivity.this.finish();
                    }
                }, j);
                return;
            }
        }
        this.handler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.2
            @Override // java.lang.Runnable
            public void run() {
                FlashActivity.this.startActivity(new Intent(FlashActivity.this, (Class<?>) MainActivity.class));
                FlashActivity.this.finish();
            }
        }, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermissions() {
        String[] strArr;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionGranted("android.permission.ACCESS_FINE_LOCATION")) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 110);
                return;
            }
            if (!checkPermissionGranted("android.permission.WRITE_EXTERNAL_STORAGE")) {
                if (Build.VERSION.SDK_INT >= 16) {
                    strArr = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
                } else {
                    strArr = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                }
                ActivityCompat.requestPermissions(this, strArr, 111);
                return;
            }
            if (!checkPermissionGranted("android.permission.CAMERA")) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 113);
                return;
            } else if (!checkPermissionGranted("android.permission.RECORD_AUDIO")) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 114);
                return;
            } else {
                toMainActivity();
                return;
            }
        }
        toMainActivity();
    }

    private boolean checkPermissionGranted(String str) {
        return ContextCompat.checkSelfPermission(getApplicationContext(), str) == 0;
    }

    private void showNotifyGPSDialog() {
        if (this.notifyGpsDialog == null) {
            this.notifyGpsDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.open_gpg_tip, R.string.dialog_exit, R.string.comfirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.3
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    FlashActivity.this.notifyGpsDialog.dismiss();
                    FlashActivity.this.notifyGpsDialog = null;
                    FlashActivity.this.finish();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.4
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    FlashActivity.this.notifyGpsDialog.dismiss();
                    FlashActivity.this.notifyGpsDialog = null;
                    FlashActivity.this.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), IConstant.CHECK_GPS_CODE);
                }
            });
        }
        if (this.notifyGpsDialog.isShowing()) {
            return;
        }
        this.notifyGpsDialog.show(getSupportFragmentManager(), "notify_gps_dialog");
    }

    private void showNotifyDialog() {
        if (this.notifyDialog == null) {
            this.notifyDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.open_wifi, R.string.dialog_no, R.string.dialog_yes, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.5
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    FlashActivity.this.notifyDialog.dismiss();
                    FlashActivity.this.notifyDialog = null;
                    FlashActivity.this.checkPermissions();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.6
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    FlashActivity.this.notifyDialog.dismiss();
                    FlashActivity.this.notifyDialog = null;
                    FlashActivity.this.mWifiHelper.openWifi();
                    FlashActivity.this.checkPermissions();
                }
            });
        }
        if (this.notifyDialog.isShowing()) {
            return;
        }
        this.notifyDialog.show(getSupportFragmentManager(), "openWifi");
    }

    private void showRequestPermissionDialog(String str, int i) {
        String string;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        switch (i) {
            case 110:
                string = getString(R.string.request_location_permission);
                break;
            case 111:
                string = getString(R.string.request_sdcard_permission);
                break;
            case 112:
                string = getString(R.string.request_write_setting_permission);
                break;
            case 113:
                string = getString(R.string.request_camera_permission);
                break;
            case 114:
                string = getString(R.string.request_microphone_permission);
                break;
            default:
                string = "";
                break;
        }
        if (this.requestPermissionDialog == null) {
            this.requestPermissionDialog = NotifyDialog.newInstance(getString(R.string.dialog_tips), string, R.string.dialog_exit, R.string.grant, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.7
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    FlashActivity.this.requestPermissionDialog.dismiss();
                    FlashActivity.this.requestPermissionDialog = null;
                    FlashActivity.this.finish();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.FlashActivity.8
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    FlashActivity.this.requestPermissionDialog.dismiss();
                    FlashActivity.this.requestPermissionDialog = null;
                    FlashActivity.this.checkPermissions();
                }
            });
        }
        if (this.requestPermissionDialog.isShowing() || isFinishing()) {
            return;
        }
        this.requestPermissionDialog.setContent(string);
        this.requestPermissionDialog.show(getSupportFragmentManager(), "request_permission_dialog");
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacksAndMessages(null);
        this.handler = null;
        NotifyDialog notifyDialog = this.notifyDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.notifyDialog.dismiss();
            }
            this.notifyDialog = null;
        }
        NotifyDialog notifyDialog2 = this.notifyGpsDialog;
        if (notifyDialog2 != null) {
            if (notifyDialog2.isShowing()) {
                this.notifyGpsDialog.dismiss();
            }
            this.notifyGpsDialog = null;
        }
        NotifyDialog notifyDialog3 = this.requestPermissionDialog;
        if (notifyDialog3 != null) {
            if (notifyDialog3.isShowing()) {
                this.requestPermissionDialog.dismiss();
            }
            this.requestPermissionDialog = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4356) {
            toMainActivity();
            return;
        }
        if (i == 112) {
            this.toWriteSettingIntent = null;
            if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(getApplicationContext())) {
                checkPermissions();
            } else {
                showRequestPermissionDialog("android.permission.WRITE_SETTINGS", i);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (iArr.length == 0) {
            return;
        }
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= iArr.length) {
                z = true;
                break;
            }
            int i3 = iArr[i2];
            Dbug.w("FlashActivity", "requestCode : " + i + " ,result = " + i3);
            if (i3 != 0) {
                String str = i2 < strArr.length ? strArr[i2] : null;
                if (!TextUtils.isEmpty(str)) {
                    Dbug.w("FlashActivity", "permission : " + str);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
                        finish();
                    } else {
                        showRequestPermissionDialog(str, i);
                    }
                }
            }
            i2++;
        }
        if (z) {
            checkPermissions();
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mUserAgreementText) {
            startActivity(new Intent(this, (Class<?>) UserAgreementActivity.class));
        } else if (this.mStartButton == view) {
            enter(100L);
        }
    }
}
