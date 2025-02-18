package com.jieli.stream.dv.running2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.fragment.AboutFragment;
import com.jieli.stream.dv.running2.ui.fragment.AddDeviceFragment;
import com.jieli.stream.dv.running2.ui.fragment.UpgradeFragment;
import com.jieli.stream.dv.running2.ui.fragment.browse.DevPhotoFragment;
import com.jieli.stream.dv.running2.ui.fragment.browse.PhotoViewFragment;
import com.jieli.stream.dv.running2.ui.fragment.browse.VideoPlayerFragment;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;

/* loaded from: classes.dex */
public class GenericActivity extends BaseActivity {
    String tag = getClass().getSimpleName();
    private OnConnectStateListener connectStateListener = new OnConnectStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.GenericActivity.1
        @Override // com.jieli.lib.dv.control.connect.listener.ConnectStateListener
        public void onStateChanged(Integer num) {
            int intValue = num.intValue();
            if (intValue == -1 || intValue == 1 || intValue == 3 || intValue == 4) {
                Dbug.e(GenericActivity.this.tag, "state=" + num);
                BaseFragment baseFragment = (BaseFragment) GenericActivity.this.getSupportFragmentManager().findFragmentById(R.id.generic_fragment_layout);
                if (baseFragment instanceof DevPhotoFragment) {
                    ((DevPhotoFragment) baseFragment).dismissWaitingDialog();
                }
                if (baseFragment instanceof AboutFragment) {
                    ((AboutFragment) baseFragment).dismissWaitingDialog();
                }
                GenericActivity.this.finish();
            }
        }
    };

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_generic);
        ClientManager.getClient().registerConnectStateListener(this.connectStateListener);
        Intent intent = getIntent();
        if (intent != null) {
            switchFragmentByTag(intent.getIntExtra(IConstant.KEY_FRAGMENT_TAG, 0), intent.getBundleExtra(IConstant.KEY_DATA));
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ClientManager.getClient().unregisterConnectStateListener(this.connectStateListener);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (MainApplication.isFactoryMode) {
            PreferencesHelper.remove(this.mApplication, IConstant.CURRENT_WIFI_SSID);
            sendBroadcast(new Intent(IActions.ACTION_ACCOUT_CHANGE));
        } else {
            setResult(-1);
        }
        finish();
    }

    private void switchFragmentByTag(int i, Bundle bundle) {
        String simpleName;
        BaseFragment baseFragment = null;
        if (i == 1) {
            BaseFragment baseFragment2 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(AddDeviceFragment.class.getSimpleName());
            if (baseFragment2 == null) {
                baseFragment2 = new AddDeviceFragment();
            }
            baseFragment = baseFragment2;
            simpleName = AddDeviceFragment.class.getSimpleName();
        } else {
            switch (i) {
                case 5:
                    BaseFragment baseFragment3 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(UpgradeFragment.class.getSimpleName());
                    if (baseFragment3 == null) {
                        baseFragment3 = new UpgradeFragment();
                    }
                    baseFragment = baseFragment3;
                    simpleName = UpgradeFragment.class.getSimpleName();
                    break;
                case 6:
                    BaseFragment baseFragment4 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(DevPhotoFragment.class.getSimpleName());
                    if (baseFragment4 == null) {
                        baseFragment4 = new DevPhotoFragment();
                    }
                    baseFragment = baseFragment4;
                    simpleName = DevPhotoFragment.class.getSimpleName();
                    break;
                case 7:
                    BaseFragment baseFragment5 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(PhotoViewFragment.class.getSimpleName());
                    if (baseFragment5 == null) {
                        baseFragment5 = new PhotoViewFragment();
                    }
                    baseFragment = baseFragment5;
                    simpleName = PhotoViewFragment.class.getSimpleName();
                    break;
                case 8:
                    BaseFragment baseFragment6 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(VideoPlayerFragment.class.getSimpleName());
                    if (baseFragment6 == null) {
                        baseFragment6 = new VideoPlayerFragment();
                    }
                    baseFragment = baseFragment6;
                    simpleName = VideoPlayerFragment.class.getSimpleName();
                    break;
                case 9:
                    BaseFragment baseFragment7 = (BaseFragment) getSupportFragmentManager().findFragmentByTag(AboutFragment.class.getSimpleName());
                    if (baseFragment7 == null) {
                        baseFragment7 = AboutFragment.newInstance();
                    }
                    baseFragment = baseFragment7;
                    simpleName = AboutFragment.class.getSimpleName();
                    break;
                default:
                    simpleName = null;
                    break;
            }
        }
        if (baseFragment != null) {
            if (bundle != null) {
                baseFragment.setBundle(bundle);
            }
            if (!TextUtils.isEmpty(simpleName)) {
                changeFragment(R.id.generic_fragment_layout, baseFragment, simpleName);
            } else {
                changeFragment(R.id.generic_fragment_layout, baseFragment);
            }
        }
    }
}
