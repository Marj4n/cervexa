package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public class RecordQualityFragment extends BaseFragment {
    private RadioGroup mFrontRadioGroup;
    private TextView mRearDeviceName;
    private RadioGroup mRearRadioGroup;
    private int mRearSelectedLevel;
    private int mSelectedLevel;
    private String tag = getClass().getSimpleName();
    private boolean isRecoding = false;
    private boolean isLastRecoding = false;
    private boolean isModified = false;
    private boolean isRearModified = false;
    private final RadioGroup.OnCheckedChangeListener mRearDeviceOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.2
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int indexOfChild = radioGroup.indexOfChild(RecordQualityFragment.this.mRearRadioGroup.findViewById(i));
            Dbug.e(RecordQualityFragment.this.tag, "rear index " + indexOfChild);
            if (RecordQualityFragment.this.isRearModified) {
                return;
            }
            RecordQualityFragment.this.isRearModified = true;
            RecordQualityFragment.this.mRearSelectedLevel = indexOfChild;
            RecordQualityFragment.this.switchResolution();
        }
    };
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.4
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(RecordQualityFragment.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            switch (topic.hashCode()) {
                case -1103702065:
                    if (topic.equals(Topic.VIDEO_CTRL)) {
                        c = 0;
                        break;
                    }
                    break;
                case 156413833:
                    if (topic.equals(Topic.VIDEO_PARAM)) {
                        c = 2;
                        break;
                    }
                    break;
                case 342652047:
                    if (topic.equals(Topic.PULL_VIDEO_PARAM)) {
                        c = 3;
                        break;
                    }
                    break;
                case 2135225360:
                    if (topic.equals(Topic.PULL_VIDEO_STATUS)) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                if (notifyInfo.getParams() == null) {
                    return;
                }
                String str = notifyInfo.getParams().get("status");
                if (TextUtils.isEmpty(str) || "1".equals(str) || !RecordQualityFragment.this.isLastRecoding) {
                    return;
                }
                if (RecordQualityFragment.this.isModified || RecordQualityFragment.this.isRearModified) {
                    RecordQualityFragment.this.sendRecordParam();
                    return;
                }
                return;
            }
            if (c == 1) {
                Dbug.e(RecordQualityFragment.this.tag, "PULL_VIDEO_STATUS:" + notifyInfo);
                Dbug.e(RecordQualityFragment.this.tag, "width: " + notifyInfo.getParams().get(TopicKey.WIDTH));
                if ("1".equals(notifyInfo.getParams().get("status"))) {
                    RecordQualityFragment.this.showRearCameraUI();
                    return;
                } else {
                    RecordQualityFragment.this.hideRearCameraUI();
                    return;
                }
            }
            if (c == 2 || c == 3) {
                if ((!RecordQualityFragment.this.isModified || RecordQualityFragment.this.isRearModified) && (RecordQualityFragment.this.isModified || !RecordQualityFragment.this.isRearModified)) {
                    Dbug.w(RecordQualityFragment.this.tag, "Not modified ");
                    return;
                }
                if (RecordQualityFragment.this.isModified) {
                    RecordQualityFragment.this.isModified = false;
                }
                if (RecordQualityFragment.this.isRearModified) {
                    RecordQualityFragment.this.isRearModified = false;
                }
                String str2 = notifyInfo.getParams().get(TopicKey.WIDTH);
                String str3 = notifyInfo.getParams().get(TopicKey.HEIGHT);
                Dbug.w(RecordQualityFragment.this.tag, "isLastRecoding " + RecordQualityFragment.this.isLastRecoding + ", set success: w " + str2 + ", h " + str3);
                if (RecordQualityFragment.this.isLastRecoding) {
                    ClientManager.getClient().tryToRecordVideo(true, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.4.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                Dbug.e(RecordQualityFragment.this.tag, "Send failed");
                            } else {
                                RecordQualityFragment.this.isLastRecoding = false;
                            }
                        }
                    });
                }
            }
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_record_resolution, viewGroup, false);
        this.mFrontRadioGroup = (RadioGroup) inflate.findViewById(R.id.record_quality_radio_group);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.rear_record_res_radio_group);
        this.mRearRadioGroup = radioGroup;
        radioGroup.setOnCheckedChangeListener(this.mRearDeviceOnCheckedChangeListener);
        TextView textView = (TextView) inflate.findViewById(R.id.front_camera_name);
        this.mRearDeviceName = (TextView) inflate.findViewById(R.id.rear_camera_name);
        DeviceSettingInfo deviceSettingInfo = this.mApplication.getDeviceSettingInfo();
        if (deviceSettingInfo != null && deviceSettingInfo.isExistRearView()) {
            showRearCameraUI();
        } else {
            hideRearCameraUI();
        }
        String[] recordFrontSupport = this.mApplication.getDeviceDesc().getRecordFrontSupport();
        if (recordFrontSupport != null && recordFrontSupport.length > 0) {
            int i = 0;
            for (String str : recordFrontSupport) {
                if (TextUtils.isDigitsOnly(str)) {
                    this.mFrontRadioGroup.getChildAt(i).setVisibility(0);
                }
                i++;
            }
        } else {
            textView.setVisibility(8);
        }
        int cameraLevel = getCameraLevel(1);
        if (this.mFrontRadioGroup.getChildCount() > 0) {
            ((RadioButton) this.mFrontRadioGroup.getChildAt(cameraLevel)).setChecked(true);
        }
        this.mFrontRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i2) {
                int indexOfChild = radioGroup2.indexOfChild(RecordQualityFragment.this.mFrontRadioGroup.findViewById(i2));
                Dbug.e(RecordQualityFragment.this.tag, "index " + indexOfChild);
                if (RecordQualityFragment.this.isModified) {
                    return;
                }
                RecordQualityFragment.this.isModified = true;
                RecordQualityFragment.this.mSelectedLevel = indexOfChild;
                RecordQualityFragment.this.switchResolution();
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchResolution() {
        boolean z = this.isRecoding;
        this.isLastRecoding = z;
        if (z) {
            ClientManager.getClient().tryToRecordVideo(false, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.3
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(RecordQualityFragment.this.tag, "Send failed");
                    }
                }
            });
        } else {
            sendRecordParam();
        }
    }

    private int getCameraLevel(int i) {
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (deviceSettingInfo == null) {
            return 1;
        }
        if (i == 2) {
            return deviceSettingInfo.getRearLevel();
        }
        return deviceSettingInfo.getFrontLevel();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        DeviceSettingInfo deviceSettingInfo = MainApplication.getApplication().getDeviceSettingInfo();
        if (deviceSettingInfo != null && deviceSettingInfo.getRecordState() == 1) {
            this.isRecoding = true;
        } else {
            this.isRecoding = false;
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
    public void sendRecordParam() {
        if (this.isRearModified) {
            int[] rtsResolution = AppUtils.getRtsResolution(this.mRearSelectedLevel);
            ClientManager.getClient().tryToSetRearVideoParams(rtsResolution[0], rtsResolution[1], this.mApplication.getDeviceSettingInfo().getRearFormat(), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.5
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(RecordQualityFragment.this.tag, "Send failed");
                    }
                }
            });
        } else {
            if (this.isModified) {
                int[] rtsResolution2 = AppUtils.getRtsResolution(this.mSelectedLevel);
                ClientManager.getClient().tryToSetFrontVideoParams(rtsResolution2[0], rtsResolution2[1], this.mApplication.getDeviceSettingInfo().getFrontFormat(), this.mApplication.getDeviceSettingInfo().getFrontRate(), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.RecordQualityFragment.6
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(RecordQualityFragment.this.tag, "Send failed");
                        }
                    }
                });
                return;
            }
            Dbug.e(this.tag, "Not supported");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideRearCameraUI() {
        this.mRearDeviceName.setVisibility(8);
        this.mRearRadioGroup.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRearCameraUI() {
        String[] recordRearSupport = this.mApplication.getDeviceDesc().getRecordRearSupport();
        if (recordRearSupport != null && recordRearSupport.length > 0) {
            int i = 0;
            for (String str : recordRearSupport) {
                if (TextUtils.isDigitsOnly(str)) {
                    this.mRearRadioGroup.getChildAt(i).setVisibility(0);
                }
                i++;
            }
        }
        int cameraLevel = getCameraLevel(2);
        if (this.mRearRadioGroup.getChildCount() > 0) {
            ((RadioButton) this.mRearRadioGroup.getChildAt(cameraLevel)).setChecked(true);
        }
        this.mRearRadioGroup.setOnCheckedChangeListener(this.mRearDeviceOnCheckedChangeListener);
        this.mRearDeviceName.setVisibility(0);
    }
}
