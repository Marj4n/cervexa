package com.jieli.stream.dv.running2.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.service.CommunicationService;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.HttpManager;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/* loaded from: classes.dex */
public final class DeviceDescription extends Thread {
    private WeakReference<CommunicationService> mServiceRef;
    private String tag = getClass().getSimpleName();

    public DeviceDescription(CommunicationService communicationService) {
        this.mServiceRef = new WeakReference<>(communicationService);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        String connectedIP = ClientManager.getClient().getConnectedIP();
        if (TextUtils.isEmpty(connectedIP)) {
            return;
        }
        String formatUrl = AppUtils.formatUrl(connectedIP, 8080, "mnt/spiflash/res/dev_desc.txt");
        Dbug.i(this.tag, "download url = " + formatUrl);
        HttpManager.downloadFile(formatUrl, new Callback() { // from class: com.jieli.stream.dv.running2.task.DeviceDescription.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                Dbug.w(DeviceDescription.this.tag, "download failed, reason = " + iOException.getMessage());
                if (call.isExecuted()) {
                    return;
                }
                call.enqueue(this);
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body;
                byte[] bytes;
                Dbug.i(DeviceDescription.this.tag, "download code = " + response.code());
                if (response.code() == 200 && (body = response.body()) != null && (bytes = body.bytes()) != null) {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(AppUtils.splicingFilePath(MainApplication.getApplication().getAppName(), IConstant.VERSION, null, null) + File.separator + IConstant.DEVICE_DESCRIPTION));
                    try {
                        try {
                            fileOutputStream.write(bytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (ClientManager.getClient().isConnected()) {
                            if (DeviceDescription.this.mServiceRef == null) {
                                Dbug.e(DeviceDescription.this.tag, "context is null");
                                return;
                            }
                            String checkUpdateFilePath = AppUtils.checkUpdateFilePath((Context) DeviceDescription.this.mServiceRef.get(), 2);
                            if (!TextUtils.isEmpty(checkUpdateFilePath) && !checkUpdateFilePath.equals(((CommunicationService) DeviceDescription.this.mServiceRef.get()).getString(R.string.latest_version))) {
                                Dbug.w(DeviceDescription.this.tag, "sdk upgradePath = " + checkUpdateFilePath);
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add(checkUpdateFilePath);
                                Intent intent = new Intent(IActions.ACTION_UPGRADE_FILE);
                                Bundle bundle = new Bundle();
                                bundle.putInt(IConstant.UPDATE_TYPE, 2);
                                bundle.putStringArrayList(IConstant.UPDATE_PATH, arrayList);
                                intent.putExtra(IConstant.KEY_DATA, bundle);
                                if (DeviceDescription.this.mServiceRef != null && DeviceDescription.this.mServiceRef.get() != null) {
                                    ((CommunicationService) DeviceDescription.this.mServiceRef.get()).sendBroadcast(intent);
                                }
                            }
                        }
                    } finally {
                        fileOutputStream.close();
                    }
                }
                response.close();
            }
        });
    }
}
