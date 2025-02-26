package com.marjan.cervexa.dialog;

import android.os.Bundle;
import android.view.View;
import androidx.databinding.ViewDataBinding;
import com.marjan.cervexa.databinding.DialogShareBinding;
import com.weioa.GoPlusDrone.R;
import java.io.FileNotFoundException;

/* loaded from: classes.dex */
public class ShareDialog extends CenterDialog {
    private DialogShareBinding binding;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void shareFecebook() throws FileNotFoundException;

        void shareQq() throws FileNotFoundException;

        void shareTwiter() throws FileNotFoundException;

        void shareWchat() throws FileNotFoundException;

        void shareYoutube() throws FileNotFoundException;
    }

    @Override // com.marjan.cervexa.dialog.CenterDialog
    public int getLayoutId() {
        return R.layout.dialog_share;
    }

    @Override // com.marjan.cervexa.dialog.CenterDialog
    public boolean isBottom() {
        return false;
    }

    @Override // com.marjan.cervexa.dialog.CenterDialog
    public boolean isCancele() {
        return false;
    }

    public ShareDialog setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public static ShareDialog newInstance() {
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.setArguments(new Bundle());
        return shareDialog;
    }

    @Override // com.marjan.cervexa.dialog.CenterDialog
    public void initView(ViewDataBinding viewDataBinding) {
        DialogShareBinding dialogShareBinding = (DialogShareBinding) viewDataBinding;
        this.binding = dialogShareBinding;
        dialogShareBinding.tvCancel.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShareDialog.this.dismiss();
            }
        });
        this.binding.tvWchat.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    ShareDialog.this.onClickListener.shareWchat();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ShareDialog.this.dismiss();
            }
        });
        this.binding.tvQq.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    ShareDialog.this.onClickListener.shareQq();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ShareDialog.this.dismiss();
            }
        });
        this.binding.tvFecebook.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    ShareDialog.this.onClickListener.shareFecebook();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ShareDialog.this.dismiss();
            }
        });
        this.binding.tvTwitter.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    ShareDialog.this.onClickListener.shareTwiter();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ShareDialog.this.dismiss();
            }
        });
        this.binding.tvYoutube.setOnClickListener(new View.OnClickListener() { // from class: com.marjan.cervexa.dialog.ShareDialog.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    ShareDialog.this.onClickListener.shareYoutube();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ShareDialog.this.dismiss();
            }
        });
    }
}
