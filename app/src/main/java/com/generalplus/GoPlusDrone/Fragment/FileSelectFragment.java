package com.generalplus.GoPlusDrone.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.generalplus.GoPlusDrone.R;

/* loaded from: classes.dex */
public class FileSelectFragment extends Fragment {
    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.activity_fileselect, viewGroup, false);
        if (bundle == null) {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.sample_content_fragment, TabFragment.newInstance());
            beginTransaction.commit();
        }
        return inflate;
    }
}
