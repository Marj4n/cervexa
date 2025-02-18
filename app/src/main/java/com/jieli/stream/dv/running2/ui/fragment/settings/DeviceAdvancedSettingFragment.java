package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.widget.SwitchButton;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DeviceAdvancedSettingFragment extends BaseFragment {
    private TextView hourTextView;
    private ListView listView;
    private TextView senorTextView;
    private String[] senors;
    private String tag = getClass().getSimpleName();
    private SettingItem.OnSwitchListener parkingMonitoringOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.3
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(final SwitchButton switchButton, SettingItem<Boolean> settingItem, final boolean z) {
            ClientManager.getClient().tryToSetVideoParkCar(z ? 1 : 0, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.3.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        ToastUtil.showToastShort(DeviceAdvancedSettingFragment.this.getString(R.string.setting_failed));
                        switchButton.setCheckedImmediatelyNoEvent(true ^ z);
                    }
                }
            });
        }
    };
    private SettingItem.OnSwitchListener timeTheWatermarkOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.4
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(final SwitchButton switchButton, SettingItem<Boolean> settingItem, final boolean z) {
            ClientManager.getClient().tryToSetTimeWatermark(z, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.4.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        ToastUtil.showToastShort(DeviceAdvancedSettingFragment.this.getString(R.string.setting_failed));
                        switchButton.setCheckedImmediatelyNoEvent(true ^ z);
                    }
                }
            });
        }
    };
    private SettingItem.OnSwitchListener bootPromptoOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.5
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(final SwitchButton switchButton, SettingItem<Boolean> settingItem, final boolean z) {
            ClientManager.getClient().tryToToggleBootSound(z, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.5.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        ToastUtil.showToastShort(DeviceAdvancedSettingFragment.this.getString(R.string.setting_failed));
                        switchButton.setCheckedImmediatelyNoEvent(true ^ z);
                    }
                }
            });
        }
    };
    private SettingItem.OnSwitchListener rtspOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.6
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(SwitchButton switchButton, SettingItem<Boolean> settingItem, boolean z) {
            PreferencesHelper.putBooleanValue(DeviceAdvancedSettingFragment.this.mApplication, IConstant.KEY_RTSP, z);
        }
    };
    private SettingItem.OnSwitchListener recordingOnSwitchListener = new SettingItem.OnSwitchListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.7
        @Override // com.jieli.stream.dv.running2.bean.SettingItem.OnSwitchListener
        public void onSwitchListener(final SwitchButton switchButton, SettingItem<Boolean> settingItem, final boolean z) {
            ClientManager.getClient().tryToSetVideoMic(z, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.7.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        ToastUtil.showToastShort(DeviceAdvancedSettingFragment.this.getString(R.string.setting_failed));
                        switchButton.setCheckedImmediatelyNoEvent(true ^ z);
                    }
                }
            });
        }
    };
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.8
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x003f, code lost:
        
            if (r15.equals(com.jieli.lib.dv.control.utils.Topic.GRA_SEN) != false) goto L23;
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x008b, code lost:
        
            if (r0.equals(com.jieli.lib.dv.control.utils.Topic.GRA_SEN) != false) goto L53;
         */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo r15) {
            /*
                Method dump skipped, instructions count: 410
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.AnonymousClass8.onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo):void");
        }
    };

    private interface OnselectedListener {
        void onSelected(String str, int i);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_advanced_settings, viewGroup, false);
        this.listView = (ListView) inflate.findViewById(R.id.device_advanced_setting_list_view);
        this.hourTextView = (TextView) inflate.findViewById(R.id.hour_tv);
        this.senorTextView = (TextView) inflate.findViewById(R.id.senor_tv);
        initUI();
        return inflate;
    }

    private void initUI() {
        String[] stringArray = getResources().getStringArray(R.array.device_advanced_setting_list);
        ArrayList arrayList = new ArrayList();
        int dimension = (int) getResources().getDimension(R.dimen.list_marginTop);
        for (String str : stringArray) {
            if (!TextUtils.isEmpty(str)) {
                SettingItem settingItem = new SettingItem();
                settingItem.setName(str);
                settingItem.setType(1);
                settingItem.setValue(true);
                arrayList.add(settingItem);
                if (str.equals(getString(R.string.parking_monitoring))) {
                    settingItem.setMarginTop(dimension);
                    settingItem.setValue(Boolean.valueOf(this.mApplication.getDeviceSettingInfo().isVideoParCar()));
                    settingItem.setOnSwitchListener(this.parkingMonitoringOnSwitchListener);
                } else if (str.equals(getString(R.string.time_the_watermark))) {
                    settingItem.setValue(Boolean.valueOf(this.mApplication.getDeviceSettingInfo().isVideoDate()));
                    settingItem.setOnSwitchListener(this.timeTheWatermarkOnSwitchListener);
                } else if (str.equals(getString(R.string.boot_prompt))) {
                    settingItem.setValue(Boolean.valueOf(this.mApplication.getDeviceSettingInfo().isOpenBootSound()));
                    settingItem.setOnSwitchListener(this.bootPromptoOnSwitchListener);
                } else if (IConstant.STR_RTSP.equals(str)) {
                    settingItem.setValue(Boolean.valueOf(PreferencesHelper.getSharedPreferences(this.mApplication).getBoolean(IConstant.KEY_RTSP, false)));
                    settingItem.setOnSwitchListener(this.rtspOnSwitchListener);
                } else if (str.equals(getString(R.string.sound_recording))) {
                    settingItem.setValue(Boolean.valueOf(this.mApplication.getDeviceSettingInfo().isVideoMic()));
                    settingItem.setOnSwitchListener(this.recordingOnSwitchListener);
                }
            }
        }
        this.listView.setAdapter((ListAdapter) new SettingAdapter(getActivity().getApplicationContext(), arrayList));
        this.hourTextView.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TextView textView = (TextView) view;
                DeviceAdvancedSettingFragment.this.showPopupWindow(textView, DeviceAdvancedSettingFragment.this.getResources().getStringArray(R.array.after_parking_recorder_time), new OnselectedListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.1.1
                    @Override // com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.OnselectedListener
                    public void onSelected(String str2, int i) {
                        DeviceAdvancedSettingFragment.this.hourTextView.setText(String.format(DeviceAdvancedSettingFragment.this.getString(R.string.hour), str2));
                    }
                });
                Drawable drawable = DeviceAdvancedSettingFragment.this.getResources().getDrawable(R.mipmap.ic_up);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                textView.setCompoundDrawables(null, null, drawable, null);
            }
        });
        String[] stringArray2 = getResources().getStringArray(R.array.collision_induction_sensitivity_value);
        this.senors = stringArray2;
        this.senorTextView.setText(stringArray2[this.mApplication.getDeviceSettingInfo().getGravitySensor()]);
        this.senorTextView.setOnClickListener(new AnonymousClass2());
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment$2, reason: invalid class name */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DeviceAdvancedSettingFragment deviceAdvancedSettingFragment = DeviceAdvancedSettingFragment.this;
            TextView textView = (TextView) view;
            deviceAdvancedSettingFragment.showPopupWindow(textView, deviceAdvancedSettingFragment.senors, new OnselectedListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.2.1
                @Override // com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.OnselectedListener
                public void onSelected(String str, int i) {
                    ClientManager.getClient().tryToSetGravitySenor(i, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.2.1.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                ToastUtil.showToastShort(DeviceAdvancedSettingFragment.this.getString(R.string.setting_failed));
                            }
                        }
                    });
                }
            });
            Drawable drawable = DeviceAdvancedSettingFragment.this.getResources().getDrawable(R.mipmap.ic_up);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupWindow(final TextView textView, String[] strArr, final OnselectedListener onselectedListener) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        int i = -2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getWidth(), -2);
        int i2 = 10;
        layoutParams.setMargins(10, 10, 10, 10);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(layoutParams);
        final PopupWindow popupWindow = new PopupWindow((View) linearLayout, -2, -2, true);
        int length = strArr.length;
        int i3 = 0;
        int i4 = 0;
        while (i3 < length) {
            final String str = strArr[i3];
            TextView textView2 = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(textView.getWidth(), i);
            layoutParams2.setMargins(0, 0, 0, 1);
            layoutParams2.gravity = 17;
            textView2.setLayoutParams(layoutParams2);
            textView2.setText(str);
            textView2.setTextSize(16.0f);
            textView2.setGravity(17);
            textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            textView2.setPadding(i2, i2, i2, i2);
            textView2.setBackgroundColor(-1);
            linearLayout.addView(textView2);
            final int i5 = i4;
            textView2.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.9
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    onselectedListener.onSelected(str, i5);
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            i4++;
            i3++;
            i2 = 10;
            i = -2;
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_pupop_window));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceAdvancedSettingFragment.10
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                Drawable drawable = DeviceAdvancedSettingFragment.this.getResources().getDrawable(R.mipmap.ic_down);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                textView.setCompoundDrawables(null, null, drawable, null);
            }
        });
        popupWindow.showAsDropDown(textView);
    }
}
