package com.marjan.cervexa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.marjan.cervexa.adapter.ResolutionListAdapter;
import com.marjan.cervexa.application.CameraApplication;
import com.marjan.cervexa.core.OnCameraConnectedListener;
import com.marjan.cervexa.databinding.UsbPreviewActivityBinding;
import com.jaeger.library.StatusBarUtil;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jiangdg.usbcamera.utils.FileUtils;
import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.encoder.RecordParams;
import com.weioa.GoPlusDrone.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UVCUSBCameraActivity extends CameraBaseActivity {
    private ResolutionListAdapter adapter;
    private UsbPreviewActivityBinding binding;
    private UVCCameraHelper mCameraHelper;
    private int position;
    private long startTime;
    protected Handler handler = new Handler();
    protected Runnable updateTimer = new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.14
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override // java.lang.Runnable
        public void run() {
            Long valueOf = Long.valueOf(System.currentTimeMillis() - UVCUSBCameraActivity.this.startTime);
            Long valueOf2 = Long.valueOf(((valueOf.longValue() / 1000) / 60) / 60);
            Long valueOf3 = Long.valueOf(((valueOf.longValue() / 1000) / 60) % 60);
            Long valueOf4 = Long.valueOf((valueOf.longValue() / 1000) % 60);
            UVCUSBCameraActivity.this.binding.tvRecordTime.setText(
                    String.format("%02d", valueOf2) + ":" +
                            String.format("%02d", valueOf3) + ":" +
                            String.format("%02d", valueOf4)
            );
            UVCUSBCameraActivity.this.handler.postDelayed(this, 1000L);
        }
    };

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) UVCUSBCameraActivity.class));
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // com.marjan.cervexa.activity.CameraBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UsbPreviewActivityBinding usbPreviewActivityBinding = (UsbPreviewActivityBinding) DataBindingUtil.setContentView(this, R.layout.usb_preview_activity);
        this.binding = usbPreviewActivityBinding;
        usbPreviewActivityBinding.setEventHandler(this);
        connectService();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#4A4A4A"));
        initDate();
    }

    void initDate() {
        this.binding.ivResolution.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity.this.binding.llResolution.setVisibility(0);
                UVCUSBCameraActivity.this.adapter.setData(UVCUSBCameraActivity.this.getResolutionList());
            }
        });
        this.binding.llResolution.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity.this.binding.llResolution.setVisibility(8);
            }
        });
        this.binding.ivZoomIn.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity uVCUSBCameraActivity = UVCUSBCameraActivity.this;
                uVCUSBCameraActivity.zoom(true, uVCUSBCameraActivity.binding.llConent);
            }
        });
        this.binding.ivZoomOut.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity uVCUSBCameraActivity = UVCUSBCameraActivity.this;
                uVCUSBCameraActivity.zoom(false, uVCUSBCameraActivity.binding.llConent);
            }
        });
        this.binding.ivZoomFocus.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (UVCUSBCameraActivity.this.binding.ivFocusImg.getVisibility() == 8) {
                    UVCUSBCameraActivity.this.binding.ivFocusImg.setVisibility(0);
                } else {
                    UVCUSBCameraActivity.this.binding.ivFocusImg.setVisibility(8);
                }
            }
        });
        this.binding.ivRecord.setOnClickListener(new AnonymousClass6());
        this.binding.ivPicture.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity.this.onClickGallery();
            }
        });
        this.binding.takePhoto.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity.this.onClickTakePhoto();
            }
        });
        this.binding.back.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UVCUSBCameraActivity.this.finish();
            }
        });
        this.binding.rcResolution.setLayoutManager(new LinearLayoutManager(this));
        ResolutionListAdapter resolutionListAdapter = new ResolutionListAdapter(this);
        this.adapter = resolutionListAdapter;
        resolutionListAdapter.setOnClickItem(new ResolutionListAdapter.OnClickItem() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.10
            @Override // com.marjan.cervexa.adapter.ResolutionListAdapter.OnClickItem
            public void onClick(int i) {
                String[] split;
                UVCUSBCameraActivity.this.binding.llResolution.setVisibility(8);
                if (UVCUSBCameraActivity.this.mCameraHelper == null || !UVCUSBCameraActivity.this.mCameraHelper.isCameraOpened() || (split = ((String) UVCUSBCameraActivity.this.getResolutionList().get(i)).split("x")) == null || split.length < 2) {
                    return;
                }
                UVCUSBCameraActivity.this.mCameraHelper.updateResolution(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue());
            }
        });
        this.binding.rcResolution.setAdapter(this.adapter);
    }

    /* renamed from: com.marjan.cervexa.activity.UVCUSBCameraActivity$6, reason: invalid class name */
    class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (UVCUSBCameraActivity.this.mCameraHelper == null || !UVCUSBCameraActivity.this.mCameraHelper.isCameraOpened()) {
                UVCUSBCameraActivity.this.showShortMsg("sorry,camera open failed");
                return;
            }
            if (!UVCUSBCameraActivity.this.mCameraHelper.isPushing()) {
                String str = UVCCameraHelper.ROOT_PATH + "/MergeCamera/Media/Video/" + System.currentTimeMillis();
                RecordParams recordParams = new RecordParams();
                recordParams.setRecordPath(str);
                recordParams.setVoiceClose(true);
                recordParams.setRecordDuration(0);
                recordParams.setSupportOverlay(true);
                UVCUSBCameraActivity.this.mCameraHelper.startPusher(recordParams, new AbstractUVCCameraHandler.OnEncodeResultListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.6.1
                    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler.OnEncodeResultListener
                    public void onEncodeResult(byte[] bArr, int i, int i2, long j, int i3) {
                        if (i3 == 1) {
                            FileUtils.putFileStream(bArr, i, i2);
                        }
                    }

                    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler.OnEncodeResultListener
                    public void onRecordResult(final String str2) {
                        if (TextUtils.isEmpty(str2)) {
                            return;
                        }
                        new Handler(UVCUSBCameraActivity.this.getMainLooper()).post(new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.6.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(UVCUSBCameraActivity.this, "save videoPath:" + str2, 0).show();
                                UVCUSBCameraActivity.this.refresh(str2);
                            }
                        });
                    }
                });
                UVCUSBCameraActivity.this.showVideoUI();
                return;
            }
            FileUtils.releaseFile();
            UVCUSBCameraActivity.this.mCameraHelper.stopPusher();
            UVCUSBCameraActivity.this.hideVideoUI();
        }
    }

    @Override // com.marjan.cervexa.activity.CameraBaseActivity
    public void onClickTakePhoto() {
        final String str = UVCCameraHelper.ROOT_PATH + CameraApplication.DIRECTORY_NAME + System.currentTimeMillis() + UVCCameraHelper.SUFFIX_JPEG;
        this.mCameraHelper.capturePicture(str, new AbstractUVCCameraHandler.OnCaptureListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.11
            @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler.OnCaptureListener
            public void onCaptureResult(String str2) {
                if (TextUtils.isEmpty(str2)) {
                    return;
                }
                new Handler(UVCUSBCameraActivity.this.getMainLooper()).post(new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(UVCUSBCameraActivity.this, "保存图片成功" + str, 0).show();
                        UVCUSBCameraActivity.this.refresh(str);
                    }
                });
            }
        });
    }

    private void connectService() {
        this.cameraManager.getUVCUSBCamera().initDevice(this);
        this.cameraManager.getUVCUSBCamera().setIButtonCallback(new IButtonCallback() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.12
            @Override // com.serenegiant.usb.IButtonCallback
            public void onButton(int i, int i2) {
            }
        });
        this.binding.cameraView.setCallback(this.cameraManager.getUVCUSBCamera());
        this.cameraManager.getUVCUSBCamera().setCameraView(this.binding.cameraView);
        this.cameraManager.getUVCUSBCamera().setListener(new OnCameraConnectedListener() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.13
            @Override // com.marjan.cervexa.core.OnCameraConnectedListener
            public void onConnectedSuccess(int i) {
                if (i == 20003) {
                    new Handler(UVCUSBCameraActivity.this.getMainLooper()).postDelayed(new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.13.1
                        @Override // java.lang.Runnable
                        public void run() {
                            UVCUSBCameraActivity.this.getResolutionList();
                            UVCUSBCameraActivity.this.binding.ivBroken.setVisibility(8);
                            UVCUSBCameraActivity.this.mCameraHelper.updateResolution(UVCCamera.DEFAULT_PREVIEW_WIDTH, 480);
                            UVCUSBCameraActivity.this.adapter.selected(UVCUSBCameraActivity.this.position);
                        }
                    }, 2000L);
                } else if (20004 == i) {
                    new Handler(UVCUSBCameraActivity.this.getMainLooper()).postDelayed(new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.13.2
                        @Override // java.lang.Runnable
                        public void run() {
                        }
                    }, 2000L);
                }
            }

            @Override // com.marjan.cervexa.core.OnCameraConnectedListener
            public void onConnectedFailed(int i) {
                new Handler(UVCUSBCameraActivity.this.getMainLooper()).postDelayed(new Runnable() { // from class: com.marjan.cervexa.activity.UVCUSBCameraActivity.13.3
                    @Override // java.lang.Runnable
                    public void run() {
                        UVCUSBCameraActivity.this.binding.ivBroken.setVisibility(0);
                    }
                }, 2000L);
            }
        });
        this.cameraManager.getUVCUSBCamera().connectDevice();
        this.cameraManager.getUVCUSBCamera().onStart();
        this.mCameraHelper = this.cameraManager.getUVCUSBCamera().getCameraHelper();
    }

    @Override // com.marjan.cervexa.activity.CameraBaseActivity
    public void onClickGallery() {
        GalleryListActivity.start(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.releaseFile();
        this.cameraManager.getUVCUSBCamera().destroyDevice();
    }

    public void zoom(boolean z, View view) {
        float scaleX = view.getScaleX();
        float scaleY = view.getScaleY();
        if (z) {
            if (scaleX > 1.0f) {
                view.setScaleX(scaleX - 0.5f);
                view.setScaleY(scaleY - 0.5f);
                return;
            }
            return;
        }
        if (scaleX < 4.0f) {
            view.setScaleX(scaleX + 0.5f);
            view.setScaleY(scaleY + 0.5f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showShortMsg(String str) {
        Toast.makeText(this, str, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showVideoUI() {
        this.binding.ivRecord.setImageResource(R.mipmap.icon_record_stop);
        this.binding.tvRecordTime.setVisibility(0);
        this.startTime = System.currentTimeMillis();
        this.handler.removeCallbacks(this.updateTimer);
        this.handler.postDelayed(this.updateTimer, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideVideoUI() {
        this.binding.ivRecord.setImageResource(R.mipmap.icon_record);
        this.handler.removeCallbacks(this.updateTimer);
        this.binding.tvRecordTime.setVisibility(8);
        this.binding.tvRecordTime.setText("00:00:00");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> getResolutionList() {
        List<Size> supportedPreviewSizes = this.mCameraHelper.getSupportedPreviewSizes();
        if (supportedPreviewSizes == null || supportedPreviewSizes.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            Size size = supportedPreviewSizes.get(i);
            if (size != null) {
                if (size.width == 640) {
                    this.position = i;
                }
                arrayList.add(size.width + "x" + size.height);
            }
        }
        return arrayList;
    }

    void refresh(String str) {
        File file = new File(str);
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
}
