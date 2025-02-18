package com.gizthon.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.gizthon.camera.core.CameraManager;
import com.gizthon.camera.view.MyContextWrapper;
import java.util.Locale;

/* loaded from: classes.dex */
public class CameraBaseActivity extends AppCompatActivity {
    public CameraManager cameraManager;

    public void onClickGallery() {
    }

    public void onClickTakePhoto() {
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.cameraManager = new CameraManager(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.cameraManager.onActivityResult(i, i2, intent);
    }

    public void onClickBack() {
        finish();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(MyContextWrapper.wrap(context, Locale.getDefault()));
    }
}
