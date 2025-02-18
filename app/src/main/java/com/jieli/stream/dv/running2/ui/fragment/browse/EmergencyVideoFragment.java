package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.ItemBean;
import com.jieli.stream.dv.running2.bean.MediaTaskInfo;
import com.jieli.stream.dv.running2.task.MediaTask;
import com.jieli.stream.dv.running2.ui.activity.GenericActivity;
import com.jieli.stream.dv.running2.ui.adapter.TimeLineAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.view.ExpandFooterView;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class EmergencyVideoFragment extends BaseFragment implements BaseFooterView.OnLoadListener, TimeLineAdapter.OnSubViewItemClickListener {
    private static final int MSG_LOAD_DATA = 257;
    private List<FileInfo> allDataList;
    private BrowseFileFragment browseFileFragment;
    private List<FileInfo> dataList;
    private MediaTask emergencyVideoTask;
    private LinearLayout emptyView;
    private ExpandFooterView footerView;
    private boolean isLoading;
    private boolean isOpenTask;
    private boolean isSelectAll;
    private TimeLineAdapter mAdapter;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.EmergencyVideoFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (EmergencyVideoFragment.this.getActivity() != null && message != null) {
                int i = message.what;
                if (i == 84) {
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        EmergencyVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                    } else if (i2 == 1) {
                        EmergencyVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, true);
                    }
                } else if (i == 257) {
                    if (EmergencyVideoFragment.this.dataList != null) {
                        EmergencyVideoFragment emergencyVideoFragment = EmergencyVideoFragment.this;
                        emergencyVideoFragment.loadMoreData(emergencyVideoFragment.dataList.size());
                    }
                    EmergencyVideoFragment.this.onStopLoad();
                }
            }
            return false;
        }
    });
    private ListView mListView;
    private int mOp;
    private EmergencyVideoBroadCastReceiver mReceiver;
    private NotifyDialog notifyDialog;
    private int retryNum;
    private List<FileInfo> selectedList;

    private class EmergencyVideoBroadCastReceiver extends BroadcastReceiver {
        private EmergencyVideoBroadCastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            int size;
            if (EmergencyVideoFragment.this.getActivity() == null || context == null || intent == null || !EmergencyVideoFragment.this.isVisible() || EmergencyVideoFragment.this.browseFileFragment == null || !(EmergencyVideoFragment.this.browseFileFragment.currentFragment() instanceof EmergencyVideoFragment)) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (EmergencyVideoFragment.this.emergencyVideoTask != null) {
                EmergencyVideoFragment.this.emergencyVideoTask.setUIHandler(EmergencyVideoFragment.this.mHandler);
            }
            int hashCode = action.hashCode();
            if (hashCode != -922755823) {
                if (hashCode == 1702481103 && action.equals(IActions.ACTION_LANGUAAGE_CHANGE)) {
                    c = 1;
                }
                c = 65535;
            } else {
                if (action.equals(IActions.ACTION_BROWSE_FILE_OPERATION)) {
                    c = 0;
                }
                c = 65535;
            }
            if (c != 0) {
                if (c != 1) {
                    return;
                }
                EmergencyVideoFragment.this.updateTextUI();
                return;
            }
            int intExtra = intent.getIntExtra(IConstant.KEY_BROWSE_OPERATION, -1);
            Dbug.w(EmergencyVideoFragment.this.TAG, "receive op : " + intExtra);
            switch (intExtra) {
                case IConstant.OP_ENTER_EDIT_MODE /* 161 */:
                    if (EmergencyVideoFragment.this.allDataList != null) {
                        if (EmergencyVideoFragment.this.allDataList.size() > 0) {
                            EmergencyVideoFragment.this.sendStateChange(1, true);
                            if (EmergencyVideoFragment.this.mAdapter != null) {
                                EmergencyVideoFragment.this.mAdapter.setEditMode(true);
                                EmergencyVideoFragment.this.mAdapter.notifyDataSetChanged();
                                break;
                            }
                        } else {
                            ToastUtil.showToastShort(EmergencyVideoFragment.this.getString(R.string.no_data_tip));
                            EmergencyVideoFragment.this.sendStateChange(1, false);
                            break;
                        }
                    }
                    break;
                case IConstant.OP_EXIT_EDIT_MODE /* 162 */:
                    EmergencyVideoFragment.this.isSelectAll = false;
                    EmergencyVideoFragment.this.isOpenTask = false;
                    EmergencyVideoFragment.this.selectedList.clear();
                    EmergencyVideoFragment.this.sendStateChange(2, false);
                    if (EmergencyVideoFragment.this.mAdapter != null) {
                        EmergencyVideoFragment.this.mAdapter.setEditMode(false);
                        if (EmergencyVideoFragment.this.mOp == 164) {
                            EmergencyVideoFragment.this.mAdapter.clear();
                            EmergencyVideoFragment.this.loadMoreData(0);
                        } else {
                            EmergencyVideoFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (EmergencyVideoFragment.this.browseFileFragment != null) {
                        EmergencyVideoFragment.this.browseFileFragment.dismissWaitingDialog();
                    }
                    EmergencyVideoFragment.this.mOp = 0;
                    break;
                case IConstant.OP_DELETE_FILES /* 164 */:
                    if (EmergencyVideoFragment.this.selectedList != null && EmergencyVideoFragment.this.selectedList.size() > 0) {
                        EmergencyVideoFragment.this.showNotifyDialog();
                        break;
                    } else {
                        ToastUtil.showToastShort(EmergencyVideoFragment.this.getString(R.string.selected_file_empty_tip));
                        break;
                    }
                case IConstant.OP_SELECT_ALL /* 165 */:
                    EmergencyVideoFragment.this.isSelectAll = true;
                    EmergencyVideoFragment.this.selectedList.clear();
                    if (EmergencyVideoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo : EmergencyVideoFragment.this.allDataList) {
                            if (fileInfo != null) {
                                fileInfo.setSelected(true);
                                EmergencyVideoFragment.this.selectedList.add(fileInfo);
                            }
                        }
                    }
                    if (EmergencyVideoFragment.this.dataList != null) {
                        for (FileInfo fileInfo2 : EmergencyVideoFragment.this.dataList) {
                            if (fileInfo2 != null) {
                                fileInfo2.setSelected(true);
                            }
                        }
                    }
                    EmergencyVideoFragment emergencyVideoFragment = EmergencyVideoFragment.this;
                    emergencyVideoFragment.sendMsg(emergencyVideoFragment.selectedList.size());
                    if (EmergencyVideoFragment.this.mAdapter != null) {
                        EmergencyVideoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_CANCEL_SELECT_ALL /* 166 */:
                    EmergencyVideoFragment.this.isSelectAll = false;
                    EmergencyVideoFragment.this.selectedList.clear();
                    if (EmergencyVideoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo3 : EmergencyVideoFragment.this.allDataList) {
                            if (fileInfo3 != null) {
                                fileInfo3.setSelected(false);
                            }
                        }
                    }
                    if (EmergencyVideoFragment.this.dataList != null) {
                        for (FileInfo fileInfo4 : EmergencyVideoFragment.this.dataList) {
                            if (fileInfo4 != null) {
                                fileInfo4.setSelected(false);
                            }
                        }
                    }
                    EmergencyVideoFragment emergencyVideoFragment2 = EmergencyVideoFragment.this;
                    emergencyVideoFragment2.sendMsg(emergencyVideoFragment2.selectedList.size());
                    if (EmergencyVideoFragment.this.mAdapter != null) {
                        EmergencyVideoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_SHARE_FILES /* 167 */:
                    if (EmergencyVideoFragment.this.selectedList != null && (size = EmergencyVideoFragment.this.selectedList.size()) > 0) {
                        if (size == 1) {
                            FileInfo fileInfo5 = (FileInfo) EmergencyVideoFragment.this.selectedList.get(0);
                            Intent intent2 = new Intent("android.intent.action.SEND");
                            intent2.setType("video/*");
                            intent2.putExtra("android.intent.extra.STREAM", Uri.parse(fileInfo5.getPath()));
                            EmergencyVideoFragment emergencyVideoFragment3 = EmergencyVideoFragment.this;
                            emergencyVideoFragment3.startActivityForResult(Intent.createChooser(intent2, emergencyVideoFragment3.getString(R.string.tab_share)), IConstant.CODE_SHARE_FILES);
                            break;
                        } else {
                            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                            Iterator it = EmergencyVideoFragment.this.selectedList.iterator();
                            while (it.hasNext()) {
                                arrayList.add(Uri.parse(((FileInfo) it.next()).getPath()));
                            }
                            Intent intent3 = new Intent("android.intent.action.SEND_MULTIPLE");
                            intent3.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                            intent3.setType("video/*");
                            EmergencyVideoFragment emergencyVideoFragment4 = EmergencyVideoFragment.this;
                            emergencyVideoFragment4.startActivityForResult(Intent.createChooser(intent3, emergencyVideoFragment4.getString(R.string.tab_share)), IConstant.CODE_SHARE_FILES);
                            break;
                        }
                    }
                    break;
                case IConstant.OP_CANCEL_TASK /* 168 */:
                    if (EmergencyVideoFragment.this.selectedList != null) {
                        EmergencyVideoFragment.this.selectedList.clear();
                    }
                    if (EmergencyVideoFragment.this.emergencyVideoTask != null) {
                        EmergencyVideoFragment.this.emergencyVideoTask.tryToStopTask();
                    }
                    EmergencyVideoFragment emergencyVideoFragment5 = EmergencyVideoFragment.this;
                    emergencyVideoFragment5.handlerTaskList(emergencyVideoFragment5.mOp, true);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTextUI() {
        LinearLayout linearLayout = this.emptyView;
        if (linearLayout != null) {
            ((TextView) linearLayout.findViewById(R.id.text_empty_tips)).setText(R.string.no_data_tip);
        }
    }

    public void setParentFragment(BrowseFileFragment browseFileFragment) {
        this.browseFileFragment = browseFileFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_emergency_video, viewGroup, false);
        this.mListView = (ListView) inflate.findViewById(R.id.emergency_video_view);
        this.emptyView = (LinearLayout) inflate.findViewById(R.id.view_empty);
        ExpandFooterView expandFooterView = (ExpandFooterView) inflate.findViewById(R.id.emergency_video_footer);
        this.footerView = expandFooterView;
        expandFooterView.setOnLoadListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            this.selectedList = new ArrayList();
            initListView();
            registerBroadCast();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        BrowseFileFragment browseFileFragment = this.browseFileFragment;
        if (browseFileFragment != null && this.emergencyVideoTask == null) {
            MediaTask mediaTask = browseFileFragment.getMediaTask();
            this.emergencyVideoTask = mediaTask;
            if (mediaTask != null && (this.browseFileFragment.currentFragment() instanceof EmergencyVideoFragment)) {
                this.emergencyVideoTask.setUIHandler(this.mHandler);
            }
        }
        updateTextUI();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.isLoading = false;
        this.isOpenTask = false;
        TimeLineAdapter timeLineAdapter = this.mAdapter;
        if (timeLineAdapter != null) {
            timeLineAdapter.cancelTasks();
        }
        MediaTask mediaTask = this.emergencyVideoTask;
        if (mediaTask != null) {
            mediaTask.setUIHandler(null);
            this.emergencyVideoTask = null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        this.browseFileFragment = null;
        super.onDetach();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        NotifyDialog notifyDialog = this.notifyDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.notifyDialog.dismiss();
            }
            this.notifyDialog = null;
        }
        unRegisterBroadCast();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4134) {
            sendStateChange(1, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopLoad() {
        ExpandFooterView expandFooterView = this.footerView;
        if (expandFooterView != null) {
            expandFooterView.stopLoad();
        }
        this.isLoading = false;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView.OnLoadListener
    public void onLoad(BaseFooterView baseFooterView) {
        Handler handler = this.mHandler;
        if (handler == null || this.isLoading) {
            return;
        }
        this.isLoading = true;
        handler.sendEmptyMessageDelayed(257, 1500L);
    }

    private void initListView() {
        new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.EmergencyVideoFragment.2
            @Override // java.lang.Runnable
            public void run() {
                EmergencyVideoFragment.this.allDataList = AppUtils.queryAllLocalFileList(AppUtils.splicingFilePath(EmergencyVideoFragment.this.mApplication.getAppName(), EmergencyVideoFragment.this.mApplication.getUUID(), null, null), IConstant.DIR_RECORD);
                if (EmergencyVideoFragment.this.allDataList != null) {
                    EmergencyVideoFragment emergencyVideoFragment = EmergencyVideoFragment.this;
                    emergencyVideoFragment.allDataList = AppUtils.selectTypeList(emergencyVideoFragment.allDataList, 2);
                    Dbug.i(EmergencyVideoFragment.this.TAG, "allDataList size = " + EmergencyVideoFragment.this.allDataList.size());
                    if (EmergencyVideoFragment.this.getActivity() != null) {
                        EmergencyVideoFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.EmergencyVideoFragment.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (EmergencyVideoFragment.this.mAdapter != null) {
                                    EmergencyVideoFragment.this.mAdapter.clear();
                                }
                                EmergencyVideoFragment.this.loadMoreData(0);
                            }
                        });
                        return;
                    }
                    return;
                }
                Dbug.e(EmergencyVideoFragment.this.TAG, "allDataList is null");
            }
        }).start();
    }

    private void registerBroadCast() {
        if (getActivity() != null) {
            if (this.mReceiver == null) {
                this.mReceiver = new EmergencyVideoBroadCastReceiver();
            }
            IntentFilter intentFilter = new IntentFilter(IActions.ACTION_BROWSE_FILE_OPERATION);
            intentFilter.addAction(IActions.ACTION_LANGUAAGE_CHANGE);
            getActivity().getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
        }
    }

    private void unRegisterBroadCast() {
        if (getActivity() == null || this.mReceiver == null) {
            return;
        }
        getActivity().getApplicationContext().unregisterReceiver(this.mReceiver);
        this.mReceiver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMoreData(int i) {
        List<FileInfo> list = this.allDataList;
        if (list != null) {
            int size = list.size();
            int i2 = size - i;
            this.emptyView.setVisibility(8);
            if (i2 <= 0) {
                if (i2 == 0) {
                    if (size > 0) {
                        ToastUtil.showToastShort(getString(R.string.no_more_data));
                        return;
                    } else {
                        this.emptyView.setVisibility(0);
                        return;
                    }
                }
                return;
            }
            if (i2 > 18) {
                this.dataList = this.allDataList.subList(0, i + 18);
            } else {
                this.dataList = this.allDataList;
            }
            List<ItemBean> convertDataList = AppUtils.convertDataList(this.dataList);
            if (convertDataList != null) {
                if (this.mAdapter == null) {
                    TimeLineAdapter timeLineAdapter = new TimeLineAdapter(getActivity().getApplicationContext());
                    this.mAdapter = timeLineAdapter;
                    timeLineAdapter.setOnSubViewItemClickListener(this);
                }
                this.mAdapter.clear();
                this.mListView.setAdapter((ListAdapter) this.mAdapter);
                this.mAdapter.setDataList(convertDataList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(int i) {
        if (getActivity() != null) {
            Intent intent = new Intent(IActions.ACTION_SELECT_FILES);
            intent.putExtra(IConstant.KEY_SELECT_FILES_NUM, i);
            getActivity().sendBroadcast(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStateChange(int i, boolean z) {
        if (getActivity() != null) {
            Intent intent = new Intent(IActions.ACTION_SELECT_STATE_CHANGE);
            intent.putExtra(IConstant.KEY_STATE_TYPE, i);
            intent.putExtra(IConstant.KEY_SELECT_STATE, z);
            getActivity().sendBroadcast(intent);
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.adapter.TimeLineAdapter.OnSubViewItemClickListener
    public void onSubItemClick(int i, int i2, FileInfo fileInfo) {
        TimeLineAdapter timeLineAdapter;
        if (fileInfo == null || (timeLineAdapter = this.mAdapter) == null || this.allDataList == null) {
            return;
        }
        if (timeLineAdapter.isEditMode()) {
            fileInfo.setSelected(!fileInfo.isSelected());
            if (fileInfo.isSelected()) {
                if (!this.selectedList.contains(fileInfo)) {
                    this.selectedList.add(fileInfo);
                }
                if (!this.isSelectAll && this.selectedList.size() == this.allDataList.size()) {
                    this.isSelectAll = true;
                    sendStateChange(2, true);
                }
            } else {
                this.selectedList.remove(fileInfo);
                if (this.isSelectAll) {
                    this.isSelectAll = false;
                    sendStateChange(2, false);
                }
            }
            sendMsg(this.selectedList.size());
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        String path = fileInfo.getPath();
        if (AppUtils.checkFileExist(path)) {
            Dbug.i(this.TAG, "play video url : " + path);
            Intent intent = new Intent(getActivity(), (Class<?>) GenericActivity.class);
            intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 8);
            Bundle bundle = new Bundle();
            bundle.putString(IConstant.KEY_PATH_LIST, path);
            intent.putExtra(IConstant.KEY_DATA, bundle);
            startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerTaskList(int i, boolean z) {
        FileInfo remove;
        Dbug.i(this.TAG, "- handlerTaskList - isOpenTask : " + this.isOpenTask);
        List<FileInfo> list = this.selectedList;
        if (list == null || !this.isOpenTask) {
            return;
        }
        this.mOp = i;
        int size = list.size();
        if (z) {
            if (size > 0 && (remove = this.selectedList.remove(0)) != null && this.mOp == 164) {
                updateDeleteUI(remove);
            }
            this.retryNum = 0;
        } else {
            int i2 = this.retryNum + 1;
            this.retryNum = i2;
            if (i2 > 2) {
                this.retryNum = 0;
                if (size > 0) {
                    this.selectedList.remove(0);
                }
            }
        }
        int size2 = this.selectedList.size();
        if (size2 > 0) {
            Dbug.w(this.TAG, "handler task size = " + size2);
            sendMsg(this.selectedList.size());
            FileInfo fileInfo = this.selectedList.get(0);
            if (fileInfo == null || this.emergencyVideoTask == null) {
                return;
            }
            MediaTaskInfo mediaTaskInfo = new MediaTaskInfo();
            mediaTaskInfo.setInfo(fileInfo);
            mediaTaskInfo.setOp(i);
            this.emergencyVideoTask.tryToStartTask(mediaTaskInfo);
            return;
        }
        sendMsg(this.selectedList.size());
        sendStateChange(1, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotifyDialog() {
        if (this.notifyDialog == null) {
            this.notifyDialog = NotifyDialog.newInstance(R.string.dialog_warning, R.string.delete_emergency_video_tip, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.EmergencyVideoFragment.3
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    if (EmergencyVideoFragment.this.notifyDialog != null) {
                        EmergencyVideoFragment.this.notifyDialog.dismiss();
                        EmergencyVideoFragment.this.notifyDialog = null;
                    }
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.EmergencyVideoFragment.4
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    EmergencyVideoFragment.this.isOpenTask = true;
                    EmergencyVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                    if (EmergencyVideoFragment.this.notifyDialog != null) {
                        EmergencyVideoFragment.this.notifyDialog.dismiss();
                        EmergencyVideoFragment.this.notifyDialog = null;
                    }
                    if (EmergencyVideoFragment.this.browseFileFragment != null) {
                        EmergencyVideoFragment.this.browseFileFragment.showWaitingDialog();
                    }
                }
            });
        }
        if (this.notifyDialog.isShowing()) {
            return;
        }
        this.notifyDialog.show(getFragmentManager(), "notify_dialog");
    }

    private void updateDeleteUI(FileInfo fileInfo) {
        if (fileInfo != null) {
            ThumbLoader.getInstance().removeBitmap(fileInfo.getPath());
            List<FileInfo> list = this.allDataList;
            if (list != null) {
                list.remove(fileInfo);
            }
        }
    }
}
