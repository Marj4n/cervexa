package com.jieli.stream.dv.running2.ui.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.WifiHelper;

/* loaded from: classes.dex */
public class BaseFragment extends Fragment implements IConstant, IActions {
    public String TAG = getClass().getSimpleName();
    private Bundle bundle;
    public MainApplication mApplication;
    public WifiHelper mWifiHelper;

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getActivity() == null) {
            return;
        }
        MainApplication application = MainApplication.getApplication();
        this.mApplication = application;
        this.mWifiHelper = WifiHelper.getInstance(application);
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
