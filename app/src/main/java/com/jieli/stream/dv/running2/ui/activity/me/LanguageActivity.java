package com.jieli.stream.dv.running2.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.activity.MainActivity;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.util.Locale;

/* loaded from: classes.dex */
public class LanguageActivity extends BaseActivity {
    private int mSelectLanguage;
    private RadioGroup radioGroup;
    private String tag = getClass().getSimpleName();
    private String mLastLanguage = "-1";
    private Handler mHandler = new Handler();
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.LanguageActivity.2
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(LanguageActivity.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == -830962856 && topic.equals(Topic.LANGUAGE)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            PreferencesHelper.putStringValue(LanguageActivity.this.getApplicationContext(), IConstant.KEY_APP_LANGUAGE_CODE, notifyInfo.getParams().get(TopicKey.LAG));
            LanguageActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.activity.me.LanguageActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    LanguageActivity.this.goToMainActivity();
                    LanguageActivity.this.finish();
                }
            }, 300L);
        }
    };

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) LanguageActivity.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_language);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.language_radio_group);
        this.radioGroup = radioGroup;
        radioGroup.removeAllViews();
        initLanguage();
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.LanguageActivity.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                LanguageActivity.this.mSelectLanguage = i + 1;
            }
        });
    }

    private void initLanguage() {
        int i;
        String string = PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstant.KEY_APP_LANGUAGE_CODE, "-1");
        this.mLastLanguage = string;
        if (TextUtils.isDigitsOnly(string)) {
            i = Integer.parseInt(string);
            if (i > 0) {
                i--;
            }
        } else {
            i = 0;
        }
        LayoutInflater from = LayoutInflater.from(this);
        int i2 = 0;
        for (String str : getResources().getStringArray(R.array.language)) {
            if (i2 == 0 || i2 == 4) {
                RadioButton radioButton = (RadioButton) from.inflate(R.layout.item_radiobuttom, (ViewGroup) this.radioGroup, false);
                radioButton.setText(str);
                if (i == i2) {
                    radioButton.setChecked(true);
                }
                radioButton.setId(i2);
                this.radioGroup.addView(radioButton);
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToMainActivity() {
        Intent intent = new Intent(MainApplication.getApplication(), (Class<?>) MainActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);
        Process.killProcess(Process.myPid());
    }

    public void returnBtnClick(View view) {
        onBackPressed();
    }

    public void confirmBtnClick(View view) {
        if (this.mSelectLanguage > 0) {
            if (ClientManager.getClient().isConnected()) {
                ClientManager.getClient().tryToSetLanguage(this.mSelectLanguage, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.me.LanguageActivity.3
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            ToastUtil.showToastShort(LanguageActivity.this.getString(R.string.setting_failed));
                        }
                    }
                });
                return;
            }
            if (!PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstant.KEY_APP_LANGUAGE_CODE, "-1").equals(this.mLastLanguage)) {
                AppUtils.changeAppLanguage(getApplicationContext(), this.mLastLanguage);
                sendBroadcast(new Intent(IActions.ACTION_LANGUAAGE_CHANGE));
                PreferencesHelper.putStringValue(getApplicationContext(), IConstant.KEY_APP_LANGUAGE_CODE, this.mLastLanguage);
            }
            ToastUtil.showToastShort(getString(R.string.setting_successed));
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }

    private void setLanguage(Locale locale) {
        if (locale == null) {
            return;
        }
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getApplicationContext().getResources().updateConfiguration(configuration, null);
    }
}
