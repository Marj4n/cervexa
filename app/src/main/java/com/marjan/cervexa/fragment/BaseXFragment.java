package com.marjan.cervexa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public class BaseXFragment extends Fragment {
    protected ViewGroup container;
    private View contentView;
    private Context context;
    protected LayoutInflater inflater;

    protected void onCreateView(Bundle bundle) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = getActivity().getApplicationContext();
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.inflater = layoutInflater;
        this.container = viewGroup;
        onCreateView(bundle);
        View view = this.contentView;
        return view == null ? super.onCreateView(layoutInflater, viewGroup, bundle) : view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.contentView = null;
        this.container = null;
        this.inflater = null;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setContentView(int i) {
        setContentView((ViewGroup) this.inflater.inflate(i, this.container, false));
    }

    public void setContentView(View view) {
        this.contentView = view;
    }

    public View getContentView() {
        return this.contentView;
    }

    public View findViewById(int i) {
        View view = this.contentView;
        if (view != null) {
            return view.findViewById(i);
        }
        return null;
    }
}
