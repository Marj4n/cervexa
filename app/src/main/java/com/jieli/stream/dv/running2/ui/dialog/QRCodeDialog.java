package com.jieli.stream.dv.running2.ui.dialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.QRCode;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;

/* loaded from: classes.dex */
public class QRCodeDialog extends BaseDialogFragment implements View.OnClickListener {
    private static final String HOT_SPOT = "hot_spot";
    private static final String SAVE_INFO = "save_to_dev";
    private static final String WIFI_NAME = "wifi_name";
    private OnCompletedListener<Boolean> mOnCompletedListener;
    private Button mStartSearchButton;
    private String tag = getClass().getSimpleName();

    public static QRCodeDialog newInstance(String str, String str2) {
        return newInstance(str, str2, false);
    }

    public static QRCodeDialog newInstance(String str, String str2, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString(WIFI_NAME, str);
        bundle.putString(HOT_SPOT, str2);
        bundle.putBoolean(SAVE_INFO, z);
        QRCodeDialog qRCodeDialog = new QRCodeDialog();
        qRCodeDialog.setArguments(bundle);
        return qRCodeDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        View inflate = layoutInflater.inflate(R.layout.dialog_qr_code, viewGroup, false);
        Button button = (Button) inflate.findViewById(R.id.start_search_btn);
        this.mStartSearchButton = button;
        button.setOnClickListener(this);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.qr_code_view);
        Bundle arguments = getArguments();
        if (arguments != null) {
            imageView.setImageBitmap(QRCode.createQRCode("{\"SSID\":\"" + arguments.getString(WIFI_NAME) + "\",\"PWD\":\"" + arguments.getString(HOT_SPOT) + "\",\"SAVE\":" + (arguments.getBoolean(SAVE_INFO, false) ? 1 : 0) + ",\"AUTH\":\"JL_ONLY\"}"));
        }
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = 100;
        attributes.height = 50;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 9) / 10;
            attributes.height = displayMetrics.heightPixels;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 9) / 10;
            attributes.height = displayMetrics.widthPixels;
        }
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mStartSearchButton) {
            dismiss();
            OnCompletedListener<Boolean> onCompletedListener = this.mOnCompletedListener;
            if (onCompletedListener != null) {
                onCompletedListener.onCompleted(true);
            }
        }
    }

    public void setOnCompletedListener(OnCompletedListener<Boolean> onCompletedListener) {
        this.mOnCompletedListener = onCompletedListener;
    }
}
