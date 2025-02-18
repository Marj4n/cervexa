package com.jieli.stream.dv.running2.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.widget.SwitchButton;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class APPAdvancedSettingActivity extends BaseActivity {
    private ListView listView;
    private Button returnBtn;
    private String tag = getClass().getSimpleName();
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.APPAdvancedSettingActivity.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SettingItem settingItem = (SettingItem) APPAdvancedSettingActivity.this.listView.getAdapter().getItem(i);
            if (settingItem.getType() == 0) {
                settingItem.getName().equals(APPAdvancedSettingActivity.this.getString(R.string.time_format));
            }
        }
    };
    private SettingItem.OnSwitchListener usingHardCodecListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.APPAdvancedSettingActivity.2
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(SwitchButton switchButton, SettingItem<Boolean> settingItem, boolean z) {
            PreferencesHelper.putBooleanValue(APPAdvancedSettingActivity.this.getApplicationContext(), IConstant.KEY_HARD_CODEC, z);
        }
    };
    private SettingItem.OnSwitchListener openDebugOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.APPAdvancedSettingActivity.3
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(SwitchButton switchButton, SettingItem<Boolean> settingItem, boolean z) {
            PreferencesHelper.putBooleanValue(APPAdvancedSettingActivity.this.getApplicationContext(), IConstant.KEY_OPEN_DEBUG, z);
        }
    };

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) APPAdvancedSettingActivity.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_advanced_settings);
        this.listView = (ListView) findViewById(R.id.app_advanced_setting_list_view);
        initUI();
    }

    private void initUI() {
        String[] stringArray = getResources().getStringArray(R.array.app_advanced_setting_list);
        ArrayList arrayList = new ArrayList();
        int dimension = (int) getResources().getDimension(R.dimen.list_marginTop);
        for (int i = 0; i < stringArray.length; i++) {
            String str = stringArray[i];
            if (!TextUtils.isEmpty(str)) {
                if (str.equals(getString(R.string.time_format))) {
                    SettingItem settingItem = new SettingItem();
                    settingItem.setName(str);
                    settingItem.setType(0);
                    arrayList.add(settingItem);
                } else if (str.equals(getString(R.string.using_hard_codec))) {
                    SettingItem settingItem2 = new SettingItem();
                    settingItem2.setName(str);
                    settingItem2.setType(1);
                    settingItem2.setValue(Boolean.valueOf(PreferencesHelper.getSharedPreferences(getApplicationContext()).getBoolean(IConstant.KEY_HARD_CODEC, true)));
                    arrayList.add(settingItem2);
                    settingItem2.setOnSwitchListener(this.usingHardCodecListener);
                } else if (str.equals(getString(R.string.open_debug))) {
                    SettingItem settingItem3 = new SettingItem();
                    settingItem3.setName(str);
                    settingItem3.setType(1);
                    settingItem3.setValue(Boolean.valueOf(PreferencesHelper.getSharedPreferences(getApplicationContext()).getBoolean(IConstant.KEY_OPEN_DEBUG, false)));
                    arrayList.add(settingItem3);
                    settingItem3.setOnSwitchListener(this.openDebugOnSwitchListener);
                }
                if (i == 0 || i == 2) {
                    ((SettingItem) arrayList.get(i)).setMarginTop(dimension);
                }
            }
        }
        this.listView.setAdapter((ListAdapter) new SettingAdapter(getApplicationContext(), arrayList));
        this.listView.setOnItemClickListener(this.onItemClickListener);
    }

    public void returnBtnClick(View view) {
        onBackPressed();
    }
}
