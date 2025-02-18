package com.jieli.stream.dv.running2.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceCameraModeFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DevicePhotoQualityFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStaModeFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.DeviceVolumeFragment;
import com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public class DeviceSettingActivity extends BaseActivity {
    private String tag = getClass().getSimpleName();

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) DeviceSettingActivity.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_generic);
        toDeviceSettingFragment();
    }

    public void toDeviceSettingFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceSettingFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceSettingFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceSettingFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById);
    }

    public void toDeviceNameFragment() {
        Dbug.e(this.tag, "toDeviceNameFragment");
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceNameFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceNameFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceNameFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDevicePwdFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DevicePwdFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DevicePwdFragment.class.getSimpleName())) == null) {
            findFragmentById = new DevicePwdFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceVolumeFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceVolumeFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceVolumeFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceVolumeFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDevicePictureQualityFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DevicePhotoQualityFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DevicePhotoQualityFragment.class.getSimpleName())) == null) {
            findFragmentById = new DevicePhotoQualityFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceRecordQualityFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof RecordQualityFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(RecordQualityFragment.class.getSimpleName())) == null) {
            findFragmentById = new RecordQualityFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceCameraModeFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceCameraModeFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceCameraModeFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceCameraModeFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceAdvancedSettingFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceAdvancedSettingFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceAdvancedSettingFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceAdvancedSettingFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceStorageManageFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceStorageManageFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceStorageManageFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceStorageManageFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void toDeviceStaModeFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
        if (!(findFragmentById instanceof DeviceStaModeFragment) && (findFragmentById = getSupportFragmentManager().findFragmentByTag(DeviceStaModeFragment.class.getSimpleName())) == null) {
            findFragmentById = new DeviceStaModeFragment();
        }
        changeFragment(R.id.generic_fragment_layout, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    public void returnBtnClick(View view) {
        onBackPressed();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
