package com.jieli.stream.dv.running2.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.camera.SurfaceViewCallback;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;

/* loaded from: classes.dex */
public class CameraActivity extends BaseActivity {
    private SurfaceViewCallback mCallback;
    private SurfaceView surfaceView;
    private String tag = getClass().getSimpleName();
    private final int CAMERA_OK = 100;
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.activity.CameraActivity.2
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(CameraActivity.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == -1733257664 && topic.equals(Topic.NET_SCR)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            Dbug.w(CameraActivity.this.tag, " getTopic=" + notifyInfo.getTopic());
            if (notifyInfo.getParams() != null) {
                boolean equals = "1".equals(notifyInfo.getParams().get("status"));
                Dbug.w(CameraActivity.this.tag, "isOpen " + equals);
                if (equals) {
                    CameraActivity.this.mCallback.setDeviceState(true);
                } else {
                    CameraActivity.this.mCallback.setDeviceState(false);
                }
            }
        }
    };

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_camera);
        this.surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 100);
                return;
            } else {
                initCamera();
                return;
            }
        }
        initCamera();
    }

    private void initCamera() {
        this.mCallback = new SurfaceViewCallback(this);
        this.surfaceView.getHolder().addCallback(this.mCallback);
        ClientManager.getClient().tryToStreamingPush(true, 480, IConstant.PROJECTION_HEIGHT, 30, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.CameraActivity.1
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(CameraActivity.this.tag, "send failed!!!");
                }
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 100) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            initCamera();
        } else {
            ToastUtil.showToastLong("请手动打开相机权限");
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        ClientManager.getClient().tryToStreamingPush(false, 0, 0, 0, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.CameraActivity.3
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(CameraActivity.this.tag, "send failed!!!");
                }
            }
        });
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }
}
