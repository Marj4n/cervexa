package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.task.MediaTask;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.roughike.bottombar.BottomBar;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

/* loaded from: classes.dex */
public class BrowseFileFragment extends BaseFragment implements View.OnClickListener, IndicatorViewPager.OnIndicatorPageChangeListener {
    private static final int MSG_DISCONNECT_DEVICE = 256;
    private BottomBar bottomBar;
    private LinearLayout editLayout;
    private RelativeLayout editModeLayout;
    private IndicatorViewPager indicatorViewPager;
    private boolean isEditMode;
    private boolean isSelectAll;
    private FileBrowseAdapter mAdapter;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.BrowseFileFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 256) {
                return false;
            }
            BrowseFileFragment.this.sendOperation(IConstant.OP_SHARE_FILES);
            return false;
        }
    });
    private BrowseBroadcast mReceiver;
    private NotifyDialog mShareTipsDialog;
    private MediaTask mediaTask;
    private RelativeLayout normalModeLayout;
    private String[] tabNames;
    private TextView tvAllSelect;
    private TextView tvCenter;
    private TextView tvExit;
    private WaitingDialog waitingDialog;

    private class BrowseBroadcast extends BroadcastReceiver {
        private BrowseBroadcast() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (BrowseFileFragment.this.getActivity() == null || context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != -1087783975) {
                if (hashCode != 1414742556) {
                    if (hashCode == 1702481103 && action.equals(IActions.ACTION_LANGUAAGE_CHANGE)) {
                        c = 2;
                    }
                } else if (action.equals(IActions.ACTION_SELECT_FILES)) {
                    c = 0;
                }
            } else if (action.equals(IActions.ACTION_SELECT_STATE_CHANGE)) {
                c = 1;
            }
            if (c == 0) {
                BrowseFileFragment.this.tvCenter.setText(BrowseFileFragment.this.getString(R.string.selected_num, Integer.valueOf(intent.getIntExtra(IConstant.KEY_SELECT_FILES_NUM, 0))));
                return;
            }
            if (c != 1) {
                if (c != 2) {
                    return;
                }
                BrowseFileFragment.this.initViewPager();
                return;
            }
            int intExtra = intent.getIntExtra(IConstant.KEY_STATE_TYPE, 0);
            if (intExtra != 1) {
                if (intExtra != 2) {
                    return;
                }
                BrowseFileFragment.this.isSelectAll = intent.getBooleanExtra(IConstant.KEY_SELECT_STATE, false);
                BrowseFileFragment.this.updateAllSelectUI(false);
                return;
            }
            BrowseFileFragment.this.isEditMode = intent.getBooleanExtra(IConstant.KEY_SELECT_STATE, false);
            BrowseFileFragment browseFileFragment = BrowseFileFragment.this;
            browseFileFragment.updateTopBarUI(browseFileFragment.isEditMode);
            if (BrowseFileFragment.this.isEditMode) {
                return;
            }
            BrowseFileFragment.this.tvExit.performClick();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_browse_file, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.operation_edit_btn);
        ViewPager viewPager = (ViewPager) inflate.findViewById(R.id.file_view_pager);
        this.normalModeLayout = (RelativeLayout) inflate.findViewById(R.id.top_normal_mode_layout);
        this.editModeLayout = (RelativeLayout) inflate.findViewById(R.id.top_edit_mode_layout);
        this.tvAllSelect = (TextView) inflate.findViewById(R.id.edit_all_select);
        this.tvCenter = (TextView) inflate.findViewById(R.id.edit_center_tv);
        this.tvExit = (TextView) inflate.findViewById(R.id.edit_exit);
        FixedIndicatorView fixedIndicatorView = (FixedIndicatorView) inflate.findViewById(R.id.file_indicator);
        fixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColorId(getActivity(), R.color.text_orange, R.color.text_white).setSizeId(getActivity(), R.dimen.text_normal, R.dimen.text_smaller_x));
        fixedIndicatorView.setDividerPadding(5);
        fixedIndicatorView.setScrollBar(new ColorBar(getActivity(), -32985, 5));
        viewPager.setOffscreenPageLimit(3);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(fixedIndicatorView, viewPager);
        this.indicatorViewPager = indicatorViewPager;
        indicatorViewPager.setOnIndicatorPageChangeListener(this);
        imageView.setOnClickListener(this);
        this.tvAllSelect.setOnClickListener(this);
        this.tvExit.setOnClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.pop_bottom_bar_share);
            LinearLayout linearLayout2 = (LinearLayout) getActivity().findViewById(R.id.pop_bottom_bar_delete);
            linearLayout.setOnClickListener(this);
            linearLayout2.setOnClickListener(this);
            initViewPager();
            if (this.mReceiver == null) {
                this.mReceiver = new BrowseBroadcast();
            }
            IntentFilter intentFilter = new IntentFilter(IActions.ACTION_SELECT_FILES);
            intentFilter.addAction(IActions.ACTION_SELECT_STATE_CHANGE);
            intentFilter.addAction(IActions.ACTION_LANGUAAGE_CHANGE);
            getActivity().getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        MediaTask mediaTask = this.mediaTask;
        if (mediaTask == null || mediaTask.isInterrupted()) {
            MediaTask mediaTask2 = new MediaTask(getContext(), "media_thread");
            this.mediaTask = mediaTask2;
            mediaTask2.start();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null && this.mReceiver != null) {
            getActivity().getApplication().unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
        dismissWaitingDialog();
        MediaTask mediaTask = this.mediaTask;
        if (mediaTask != null) {
            mediaTask.tryToStopTask();
            this.mediaTask.interrupt();
            this.mediaTask.release();
            this.mediaTask = null;
        }
    }

    public MediaTask getMediaTask() {
        return this.mediaTask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initViewPager() {
        if (getActivity() == null) {
            return;
        }
        this.tabNames = getActivity().getResources().getStringArray(R.array.browse_file_list);
        if (this.mAdapter == null) {
            FileBrowseAdapter fileBrowseAdapter = new FileBrowseAdapter(getActivity().getApplicationContext(), getChildFragmentManager());
            this.mAdapter = fileBrowseAdapter;
            this.indicatorViewPager.setAdapter(fileBrowseAdapter);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null || view == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.operation_edit_btn) {
            sendOperation(IConstant.OP_ENTER_EDIT_MODE);
            return;
        }
        if (id == R.id.edit_exit) {
            updateTopBarUI(false);
            sendOperation(IConstant.OP_EXIT_EDIT_MODE);
            return;
        }
        if (id == R.id.edit_all_select) {
            this.isSelectAll = !this.isSelectAll;
            updateAllSelectUI(true);
        } else {
            if (id == R.id.pop_bottom_bar_share) {
                if (ClientManager.getClient().isConnected()) {
                    showShareTipsDialog();
                    return;
                } else {
                    sendOperation(IConstant.OP_SHARE_FILES);
                    return;
                }
            }
            if (id == R.id.pop_bottom_bar_delete) {
                sendOperation(IConstant.OP_DELETE_FILES);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTopBarUI(boolean z) {
        this.isEditMode = z;
        if (z) {
            RelativeLayout relativeLayout = this.normalModeLayout;
            if (relativeLayout != null && relativeLayout.getVisibility() != 8) {
                this.normalModeLayout.setVisibility(8);
            }
            this.tvCenter.setText(getString(R.string.selected_num, 0));
            RelativeLayout relativeLayout2 = this.editModeLayout;
            if (relativeLayout2 != null && relativeLayout2.getVisibility() != 0) {
                this.editModeLayout.setVisibility(0);
            }
        } else {
            RelativeLayout relativeLayout3 = this.editModeLayout;
            if (relativeLayout3 != null && relativeLayout3.getVisibility() != 8) {
                this.editModeLayout.setVisibility(8);
            }
            RelativeLayout relativeLayout4 = this.normalModeLayout;
            if (relativeLayout4 != null && relativeLayout4.getVisibility() != 0) {
                this.normalModeLayout.setVisibility(0);
            }
        }
        updateBottomBar();
    }

    private void updateBottomBar() {
        if (getActivity() == null) {
            return;
        }
        if (this.isEditMode) {
            BottomBar bottomBar = this.bottomBar;
            if (bottomBar != null && bottomBar.getVisibility() != 8) {
                this.bottomBar.setVisibility(8);
            }
            LinearLayout linearLayout = this.editLayout;
            if (linearLayout == null || linearLayout.getVisibility() == 0) {
                return;
            }
            this.editLayout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom));
            this.editLayout.setVisibility(0);
            return;
        }
        LinearLayout linearLayout2 = this.editLayout;
        if (linearLayout2 != null && linearLayout2.getVisibility() != 8) {
            this.editLayout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom));
            this.editLayout.setVisibility(8);
        }
        BottomBar bottomBar2 = this.bottomBar;
        if (bottomBar2 == null || bottomBar2.getVisibility() == 0) {
            return;
        }
        this.bottomBar.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllSelectUI(boolean z) {
        if (this.isSelectAll) {
            this.tvAllSelect.setText(getString(R.string.cancel_all_select));
            if (z) {
                sendOperation(IConstant.OP_SELECT_ALL);
                return;
            }
            return;
        }
        this.tvAllSelect.setText(getString(R.string.all_select));
        if (z) {
            sendOperation(IConstant.OP_CANCEL_SELECT_ALL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOperation(int i) {
        if (getActivity() != null) {
            Intent intent = new Intent(IActions.ACTION_BROWSE_FILE_OPERATION);
            intent.putExtra(IConstant.KEY_BROWSE_OPERATION, i);
            getActivity().sendBroadcast(intent);
        }
    }

    public Fragment currentFragment() {
        FileBrowseAdapter fileBrowseAdapter = this.mAdapter;
        if (fileBrowseAdapter != null) {
            return fileBrowseAdapter.getCurrentFragment();
        }
        return null;
    }

    @Override // com.shizhefei.view.indicator.IndicatorViewPager.OnIndicatorPageChangeListener
    public void onIndicatorPageChange(int i, int i2) {
        Fragment fragmentForPage = this.mAdapter.getFragmentForPage(i2);
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.pop_bottom_bar_share);
        if (fragmentForPage != null && (fragmentForPage instanceof LocalPhotoFragment)) {
            linearLayout.setVisibility(0);
        } else {
            linearLayout.setVisibility(8);
        }
        if (this.isEditMode) {
            this.tvExit.performClick();
        }
    }

    private class FileBrowseAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private Context mContext;

        FileBrowseAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mContext = context;
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter
        public int getCount() {
            if (BrowseFileFragment.this.tabNames == null) {
                return 0;
            }
            return BrowseFileFragment.this.tabNames.length;
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter
        public View getViewForTab(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.main_tab_view, viewGroup, false);
            }
            TextView textView = (TextView) view;
            if (BrowseFileFragment.this.tabNames != null && i < BrowseFileFragment.this.tabNames.length) {
                textView.setText(BrowseFileFragment.this.tabNames[i]);
            }
            textView.setWidth(((int) (getTextWidth(textView) * 1.3f)) + AppUtils.dp2px(this.mContext, 8));
            textView.setPadding(0, 5, 0, 0);
            return view;
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter
        public Fragment getFragmentForPage(int i) {
            BaseFragment baseFragment = new BaseFragment();
            if (BrowseFileFragment.this.tabNames == null || i >= BrowseFileFragment.this.tabNames.length) {
                return baseFragment;
            }
            String str = BrowseFileFragment.this.tabNames[i];
            if (str.equals(BrowseFileFragment.this.getString(R.string.tab_image))) {
                LocalPhotoFragment localPhotoFragment = new LocalPhotoFragment();
                localPhotoFragment.setParentFragment(BrowseFileFragment.this);
                return localPhotoFragment;
            }
            if (str.equals(BrowseFileFragment.this.getString(R.string.tab_video))) {
                LocalVideoFragment localVideoFragment = new LocalVideoFragment();
                localVideoFragment.setParentFragment(BrowseFileFragment.this);
                return localVideoFragment;
            }
            if (!str.equals(BrowseFileFragment.this.getString(R.string.tab_protected))) {
                return baseFragment;
            }
            EmergencyVideoFragment emergencyVideoFragment = new EmergencyVideoFragment();
            emergencyVideoFragment.setParentFragment(BrowseFileFragment.this);
            return emergencyVideoFragment;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect rect = new Rect();
            String charSequence = textView.getText().toString();
            textView.getPaint().getTextBounds(charSequence, 0, charSequence.length(), rect);
            return rect.left + rect.width();
        }
    }

    public void showWaitingDialog() {
        if (this.waitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.waitingDialog = waitingDialog;
            waitingDialog.setCancelable(false);
            this.waitingDialog.setNotifyContent(getString(R.string.deleting_tip));
            this.waitingDialog.setOnWaitingDialog(new WaitingDialog.OnWaitingDialog() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.BrowseFileFragment.2
                @Override // com.jieli.stream.dv.running2.ui.dialog.WaitingDialog.OnWaitingDialog
                public void onCancelDialog() {
                    BrowseFileFragment.this.sendOperation(IConstant.OP_CANCEL_TASK);
                }
            });
        }
        if (this.waitingDialog.isShowing()) {
            return;
        }
        this.waitingDialog.show(getFragmentManager(), "wait_dialog");
    }

    public void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.waitingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing()) {
                this.waitingDialog.dismiss();
            }
            this.waitingDialog = null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (ClientManager.getClient().isConnected()) {
            updateTopBarUI(false);
        }
    }

    private void showShareTipsDialog() {
        NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.sharing_needs_disconnect, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.BrowseFileFragment.3
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
            public void onClick() {
                BrowseFileFragment.this.mShareTipsDialog.dismiss();
            }
        }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.BrowseFileFragment.4
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
            public void onClick() {
                BrowseFileFragment.this.mShareTipsDialog.dismiss();
                ClientManager.getClient().close();
                BrowseFileFragment.this.mApplication.switchWifi();
                BrowseFileFragment.this.mHandler.sendEmptyMessageDelayed(256, 500L);
            }
        });
        this.mShareTipsDialog = newInstance;
        if (newInstance.isShowing()) {
            return;
        }
        this.mShareTipsDialog.show(getActivity().getSupportFragmentManager(), "Share_Tips");
    }
}
