package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;

/* loaded from: classes.dex */
public class DeviceCameraModeFragment extends BaseFragment {
    private RadioGroup radioGroup;
    private String tag = getClass().getSimpleName();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_setting_camera_mode, viewGroup, false);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.camera_mode_radiogroup);
        this.radioGroup = radioGroup;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceCameraModeFragment.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                if (i == R.id.full_screen_mode_rbtn) {
                    return;
                }
                int i2 = R.id.width_screen_mode_rbtn;
            }
        });
        return inflate;
    }
}
