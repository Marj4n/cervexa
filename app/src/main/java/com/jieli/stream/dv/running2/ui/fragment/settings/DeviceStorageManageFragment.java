package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public class DeviceStorageManageFragment extends BaseFragment {
    private ImageButton delPhotoIbtn;
    private ImageButton delVideoIbtn;
    private Button formatBtn;
    private NotifyDialog formatDialog;
    private NotifyDialog mNotifyDialog;
    private TextView tfCapTextView;
    private String tag = getClass().getSimpleName();
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.5
        /* JADX WARN: Code restructure failed: missing block: B:47:0x006f, code lost:
        
            if (r0.equals(com.jieli.lib.dv.control.utils.Topic.VIDEO_CTRL) != false) goto L31;
         */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo r10) {
            /*
                Method dump skipped, instructions count: 275
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.AnonymousClass5.onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo):void");
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_storage_manage, viewGroup, false);
        this.delPhotoIbtn = (ImageButton) inflate.findViewById(R.id.photo_del_ibtn);
        this.delVideoIbtn = (ImageButton) inflate.findViewById(R.id.video_del_ibtn);
        this.formatBtn = (Button) inflate.findViewById(R.id.device_storage_format_btn);
        this.tfCapTextView = (TextView) inflate.findViewById(R.id.tf_cap_tv);
        int leftStorage = this.mApplication.getDeviceSettingInfo().getLeftStorage();
        int totalStorage = this.mApplication.getDeviceSettingInfo().getTotalStorage();
        if (totalStorage > 1024) {
            this.tfCapTextView.setText(String.format(Locale.getDefault(), "%.2f", Float.valueOf(totalStorage / 1024.0f)) + "GB");
        } else {
            this.tfCapTextView.setText(totalStorage + "MB");
        }
        this.formatBtn.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (1 == DeviceStorageManageFragment.this.mApplication.getDeviceSettingInfo().getRecordState()) {
                    DeviceStorageManageFragment.this.showStopRecordingDialog();
                } else {
                    DeviceStorageManageFragment.this.showFormatDevice();
                }
            }
        });
        this.delPhotoIbtn.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ToastUtil.showToastShort(DeviceStorageManageFragment.this.getString(R.string.save_success));
            }
        });
        this.delVideoIbtn.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ToastUtil.showToastShort(DeviceStorageManageFragment.this.getString(R.string.save_success));
            }
        });
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        NotifyDialog notifyDialog = this.mNotifyDialog;
        if (notifyDialog != null && notifyDialog.isShowing()) {
            this.mNotifyDialog.dismiss();
        }
        this.mNotifyDialog = null;
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFormatDevice() {
        if (this.formatDialog == null) {
            this.formatDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.format, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.6
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DeviceStorageManageFragment.this.formatDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.7
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    ClientManager.getClient().tryToFormatTFCard(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.7.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                ToastUtil.showToastShort(DeviceStorageManageFragment.this.getString(R.string.format_failed));
                            }
                        }
                    });
                    DeviceStorageManageFragment.this.formatDialog.dismiss();
                }
            });
        }
        if (this.formatDialog.isShowing()) {
            return;
        }
        this.formatDialog.show(getFragmentManager(), "formatDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStopRecordingDialog() {
        if (this.mNotifyDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.stop_recording_tips, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.8
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DeviceStorageManageFragment.this.mNotifyDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.9
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    ClientManager.getClient().tryToRecordVideo(false, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStorageManageFragment.9.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num) {
                            if (num.intValue() != 1) {
                                DeviceStorageManageFragment.this.mNotifyDialog.dismiss();
                                ToastUtil.showToastShort(DeviceStorageManageFragment.this.getString(R.string.operation_failed_and_try_again));
                                Dbug.e(DeviceStorageManageFragment.this.tag, "Send failed");
                            }
                        }
                    });
                }
            });
            this.mNotifyDialog = newInstance;
            newInstance.setCancelable(false);
        }
        if (this.mNotifyDialog.isShowing()) {
            return;
        }
        this.mNotifyDialog.show(getActivity().getSupportFragmentManager(), "notify_dialog");
    }
}
