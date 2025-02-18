package com.jieli.stream.dv.running2.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.activity.GenericActivity;
import com.jieli.stream.dv.running2.ui.activity.me.APPAdvancedSettingActivity;
import com.jieli.stream.dv.running2.ui.activity.me.AppStorageManageActivity;
import com.jieli.stream.dv.running2.ui.activity.me.DeviceSettingActivity;
import com.jieli.stream.dv.running2.ui.activity.me.LanguageActivity;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.widget.SwitchButton;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SettingFragment extends BaseFragment {
    private static String tag = SettingFragment.class.getSimpleName();
    private ListView listView;
    private View mView;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.jieli.stream.dv.running2.ui.fragment.SettingFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            char c = 65535;
            if (action.hashCode() == 1702481103 && action.equals(IActions.ACTION_LANGUAAGE_CHANGE)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            SettingFragment.this.initUI();
        }
    };
    private SettingItem.OnSwitchListener savePictureInPhoneOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.SettingFragment.2
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(SwitchButton switchButton, SettingItem<Boolean> settingItem, boolean z) {
            PreferencesHelper.putBooleanValue(SettingFragment.this.getContext(), IConstant.KEY_SAVE_PICTURE, z);
        }
    };
    private SettingItem.OnSwitchListener downLoadPhotoOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.SettingFragment.3
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(SwitchButton switchButton, SettingItem<Boolean> settingItem, boolean z) {
            PreferencesHelper.putBooleanValue(SettingFragment.this.getContext(), IConstant.KEY_AUTO_DOWNLOAG_PICTURE, z);
        }
    };
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.SettingFragment.4
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SettingItem settingItem = (SettingItem) SettingFragment.this.listView.getAdapter().getItem(i);
            if (settingItem.getType() != 0) {
                Dbug.e(SettingFragment.tag, "item.getType() " + settingItem.getType() + ", position " + i);
                return;
            }
            if (settingItem.getName().equals(SettingFragment.this.getString(R.string.device_setting))) {
                if (ClientManager.getClient().isConnected()) {
                    DeviceSettingActivity.start(SettingFragment.this.getActivity());
                    return;
                } else {
                    ToastUtil.showToastShort(SettingFragment.this.getString(R.string.please_connect_device_to_use));
                    return;
                }
            }
            if (settingItem.getName().equals(SettingFragment.this.getString(R.string.language))) {
                if (ClientManager.getClient().isConnected()) {
                    LanguageActivity.start(SettingFragment.this.getContext());
                    return;
                } else {
                    ToastUtil.showToastShort(SettingFragment.this.getString(R.string.please_connect_device_to_use));
                    return;
                }
            }
            if (settingItem.getName().equals(SettingFragment.this.getString(R.string.app_advanced_settings))) {
                if (ClientManager.getClient().isConnected()) {
                    APPAdvancedSettingActivity.start(SettingFragment.this.getContext());
                    return;
                } else {
                    ToastUtil.showToastShort(SettingFragment.this.getString(R.string.please_connect_device_to_use));
                    return;
                }
            }
            if (settingItem.getName().equals(SettingFragment.this.getString(R.string.about_app))) {
                SettingFragment.this.goToAbout();
            } else if (!settingItem.getName().equals(SettingFragment.this.getString(R.string.help)) && settingItem.getName().equals(SettingFragment.this.getString(R.string.app_storage_manager))) {
                AppStorageManageActivity.start(SettingFragment.this.getContext());
            }
        }
    };

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        MainApplication.getApplication().registerReceiver(this.mBroadcastReceiver, new IntentFilter(IActions.ACTION_LANGUAAGE_CHANGE));
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        MainApplication.getApplication().unregisterReceiver(this.mBroadcastReceiver);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);
        this.mView = inflate;
        this.listView = (ListView) inflate.findViewById(R.id.setting_list_view);
        return this.mView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        initUI();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initUI() {
        ((TextView) this.mView.findViewById(R.id.setting_top_tv)).setText(R.string.tab_me);
        String[] stringArray = getResources().getStringArray(R.array.setting_list);
        ArrayList arrayList = new ArrayList();
        int dimension = (int) getResources().getDimension(R.dimen.list_marginTop);
        for (int i = 0; i < stringArray.length; i++) {
            String str = stringArray[i];
            if (!TextUtils.isEmpty(str)) {
                SettingItem settingItem = new SettingItem();
                if (str.equals(getString(R.string.device_setting)) || str.equals(getString(R.string.app_storage_manager)) || str.equals(getString(R.string.about_app)) || str.equals(getString(R.string.help))) {
                    settingItem.setType(0);
                } else if (str.equals(getString(R.string.language))) {
                    settingItem.setType(0);
                    if (ClientManager.getClient().isConnected()) {
                        String[] stringArray2 = getResources().getStringArray(R.array.language);
                        String string = PreferencesHelper.getSharedPreferences(this.mApplication).getString(IConstant.KEY_APP_LANGUAGE_CODE, "-1");
                        int parseInt = TextUtils.isDigitsOnly(string) ? Integer.parseInt(string) - 1 : 0;
                        if (parseInt > -1 && parseInt < stringArray2.length) {
                            settingItem.setValue(stringArray2[parseInt]);
                        }
                    }
                } else if (str.equals(getString(R.string.save_picture_in_phone))) {
                    settingItem.setType(1);
                    settingItem.setValue(Boolean.valueOf(PreferencesHelper.getSharedPreferences(getContext()).getBoolean(IConstant.KEY_SAVE_PICTURE, false)));
                    settingItem.setOnSwitchListener(this.savePictureInPhoneOnSwitchListener);
                } else if (str.equals(getString(R.string.auto_download_the_photo_files))) {
                    settingItem.setType(1);
                    settingItem.setValue(Boolean.valueOf(PreferencesHelper.getSharedPreferences(getContext()).getBoolean(IConstant.KEY_AUTO_DOWNLOAG_PICTURE, false)));
                    settingItem.setOnSwitchListener(this.downLoadPhotoOnSwitchListener);
                } else if (getString(R.string.app_advanced_settings).equals(str)) {
                    settingItem.setType(0);
                }
                if (i == 0 || i == 1 || i == 3 || i == 4 || i == 5) {
                    settingItem.setMarginTop(dimension);
                }
                settingItem.setName(str);
                arrayList.add(settingItem);
            }
        }
        this.listView.setAdapter((ListAdapter) new SettingAdapter(getActivity().getApplicationContext(), arrayList));
        this.listView.setOnItemClickListener(this.onItemClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToAbout() {
        Intent intent = new Intent(getActivity(), (Class<?>) GenericActivity.class);
        intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 9);
        startActivity(intent);
    }
}
