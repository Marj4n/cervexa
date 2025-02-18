package com.serenegiant.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

/* loaded from: classes.dex */
public abstract class DialogFragmentEx extends DialogFragment {
    protected static final String ARGS_KEY_ID_MESSAGE = "message";
    protected static final String ARGS_KEY_ID_TITLE = "title";
    protected static final String ARGS_KEY_REQUEST_CODE = "requestCode";
    protected static final String ARGS_KEY_TAG = "tag";

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            bundle.putAll(arguments);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.Fragment
    public Bundle requireArguments() throws IllegalStateException {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments;
        }
        throw new IllegalStateException();
    }
}
