package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LocalPhotoFragment extends BaseFragment implements BaseFooterView.OnLoadListener, TimeLineAdapter.OnSubViewItemClickListener {
    private static final int MSG_LOAD_DATA = 257;
    private List<FileInfo> allDataList;
    private BrowseFileFragment browseFileFragment;
    private List<FileInfo> dataList;
    private LinearLayout emptyView;
    private ExpandFooterView footerView;
    private boolean isLoading;
    private boolean isOpenTask;
    private boolean isSelectAll;
    private TimeLineAdapter mAdapter;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalPhotoFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (LocalPhotoFragment.this.getActivity() != null && message != null) {
                int i = message.what;
                if (i == 84) {
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        LocalPhotoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                    } else if (i2 == 1) {
                        LocalPhotoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, true);
                    }
                } else if (i == 257) {
                    if (LocalPhotoFragment.this.dataList != null) {
                        LocalPhotoFragment localPhotoFragment = LocalPhotoFragment.this;
                        localPhotoFragment.loadMoreData(localPhotoFragment.dataList.size());
                    } else {
                        Dbug.e(LocalPhotoFragment.this.TAG, "======no load===list=");
                    }
                    LocalPhotoFragment.this.onStopLoad();
                }
            }
            return false;
        }
    });
    private ListView mListView;
    private NotifyDialog mLoadingDialog;
    private int mOp;
    private LocalPhotoBroadCast mReceiver;
    private MediaTask photoTask;
    private int retryNum;
    private List<FileInfo> selectedList;

    private class LocalPhotoBroadCast extends BroadcastReceiver {
        private LocalPhotoBroadCast() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            if (LocalPhotoFragment.this.getActivity() == null || context == null || intent == null || !LocalPhotoFragment.this.isVisible() || LocalPhotoFragment.this.browseFileFragment == null || !(LocalPhotoFragment.this.browseFileFragment.currentFragment() instanceof LocalPhotoFragment)) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (LocalPhotoFragment.this.photoTask != null) {
                LocalPhotoFragment.this.photoTask.setUIHandler(LocalPhotoFragment.this.mHandler);
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
                LocalPhotoFragment.this.updateTextUI();
                return;
            }
            int intExtra = intent.getIntExtra(IConstant.KEY_BROWSE_OPERATION, -1);
            Dbug.w(LocalPhotoFragment.this.TAG, "receive op : " + intExtra);
            switch (intExtra) {
                case IConstant.OP_ENTER_EDIT_MODE /* 161 */:
                    if (LocalPhotoFragment.this.allDataList != null) {
                        if (LocalPhotoFragment.this.allDataList.size() > 0) {
                            LocalPhotoFragment.this.sendStateChange(1, true);
                            if (LocalPhotoFragment.this.mAdapter != null) {
                                LocalPhotoFragment.this.mAdapter.setEditMode(true);
                                LocalPhotoFragment.this.mAdapter.notifyDataSetChanged();
                                break;
                            }
                        } else {
                            ToastUtil.showToastShort(LocalPhotoFragment.this.getString(R.string.no_data_tip));
                            LocalPhotoFragment.this.sendStateChange(1, false);
                            break;
                        }
                    }
                    break;
                case IConstant.OP_EXIT_EDIT_MODE /* 162 */:
                    LocalPhotoFragment.this.isSelectAll = false;
                    LocalPhotoFragment.this.isOpenTask = false;
                    LocalPhotoFragment.this.selectedList.clear();
                    LocalPhotoFragment.this.sendStateChange(2, false);
                    if (LocalPhotoFragment.this.mAdapter != null) {
                        LocalPhotoFragment.this.mAdapter.setEditMode(false);
                        if (LocalPhotoFragment.this.mOp == 164) {
                            LocalPhotoFragment.this.mAdapter.clear();
                            LocalPhotoFragment.this.loadMoreData(0);
                        } else {
                            LocalPhotoFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (LocalPhotoFragment.this.browseFileFragment != null) {
                        LocalPhotoFragment.this.browseFileFragment.dismissWaitingDialog();
                    }
                    LocalPhotoFragment.this.mOp = 0;
                    break;
                case IConstant.OP_DELETE_FILES /* 164 */:
                    if (LocalPhotoFragment.this.selectedList != null && LocalPhotoFragment.this.selectedList.size() > 0) {
                        LocalPhotoFragment.this.isOpenTask = true;
                        if (LocalPhotoFragment.this.browseFileFragment != null) {
                            LocalPhotoFragment.this.browseFileFragment.showWaitingDialog();
                        }
                        LocalPhotoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                        break;
                    } else {
                        ToastUtil.showToastShort(LocalPhotoFragment.this.getString(R.string.selected_file_empty_tip));
                        break;
                    }
                case IConstant.OP_SELECT_ALL /* 165 */:
                    LocalPhotoFragment.this.isSelectAll = true;
                    LocalPhotoFragment.this.selectedList.clear();
                    if (LocalPhotoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo : LocalPhotoFragment.this.allDataList) {
                            if (fileInfo != null) {
                                fileInfo.setSelected(true);
                                LocalPhotoFragment.this.selectedList.add(fileInfo);
                            }
                        }
                    }
                    if (LocalPhotoFragment.this.dataList != null) {
                        for (FileInfo fileInfo2 : LocalPhotoFragment.this.dataList) {
                            if (fileInfo2 != null) {
                                fileInfo2.setSelected(true);
                            }
                        }
                    }
                    LocalPhotoFragment localPhotoFragment = LocalPhotoFragment.this;
                    localPhotoFragment.sendMsg(localPhotoFragment.selectedList.size());
                    if (LocalPhotoFragment.this.mAdapter != null) {
                        LocalPhotoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_CANCEL_SELECT_ALL /* 166 */:
                    LocalPhotoFragment.this.isSelectAll = false;
                    LocalPhotoFragment.this.selectedList.clear();
                    if (LocalPhotoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo3 : LocalPhotoFragment.this.allDataList) {
                            if (fileInfo3 != null) {
                                fileInfo3.setSelected(false);
                            }
                        }
                    }
                    if (LocalPhotoFragment.this.dataList != null) {
                        for (FileInfo fileInfo4 : LocalPhotoFragment.this.dataList) {
                            if (fileInfo4 != null) {
                                fileInfo4.setSelected(false);
                            }
                        }
                    }
                    LocalPhotoFragment localPhotoFragment2 = LocalPhotoFragment.this;
                    localPhotoFragment2.sendMsg(localPhotoFragment2.selectedList.size());
                    if (LocalPhotoFragment.this.mAdapter != null) {
                        LocalPhotoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_SHARE_FILES /* 167 */:
                    if (LocalPhotoFragment.this.selectedList != null) {
                        int size = LocalPhotoFragment.this.selectedList.size();
                        if (size > 0) {
                            if (size == 1) {
                                FileInfo fileInfo5 = (FileInfo) LocalPhotoFragment.this.selectedList.get(0);
                                Dbug.i(LocalPhotoFragment.this.TAG, "share===" + fileInfo5);
                                ShareEntity shareEntity = new ShareEntity("", "");
                                shareEntity.setImgUrl(fileInfo5.getPath());
                                shareEntity.setShareBigImg(true);
                                ShareUtil.showShareDialog(LocalPhotoFragment.this.getActivity(), shareEntity, ShareConstant.REQUEST_CODE);
                                break;
                            } else {
                                ToastUtil.showToastShort(LocalPhotoFragment.this.getString(R.string.only_support_one_sharing));
                                break;
                            }
                        } else {
                            ToastUtil.showToastShort(LocalPhotoFragment.this.getString(R.string.selected_file_empty_tip));
                            break;
                        }
                    }
                    break;
                case IConstant.OP_CANCEL_TASK /* 168 */:
                    if (LocalPhotoFragment.this.selectedList != null) {
                        LocalPhotoFragment.this.selectedList.clear();
                    }
                    if (LocalPhotoFragment.this.photoTask != null) {
                        LocalPhotoFragment.this.photoTask.tryToStopTask();
                    }
                    LocalPhotoFragment localPhotoFragment3 = LocalPhotoFragment.this;
                    localPhotoFragment3.handlerTaskList(localPhotoFragment3.mOp, true);
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

    @Override // com.jieli.stream.dv.running2.ui.base.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_local_photo, viewGroup, false);
        this.mListView = (ListView) inflate.findViewById(R.id.local_photo_view);
        this.emptyView = (LinearLayout) inflate.findViewById(R.id.view_empty);
        ExpandFooterView expandFooterView = (ExpandFooterView) inflate.findViewById(R.id.local_photo_footer);
        this.footerView = expandFooterView;
        expandFooterView.setOnLoadListener(this);
        ((LinearLayout) getActivity().findViewById(R.id.pop_bottom_bar_share)).setVisibility(0);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            this.selectedList = new ArrayList();
            initListView();
            if (this.mReceiver == null) {
                this.mReceiver = new LocalPhotoBroadCast();
            }
            IntentFilter intentFilter = new IntentFilter(IActions.ACTION_BROWSE_FILE_OPERATION);
            intentFilter.addAction(IActions.ACTION_LANGUAAGE_CHANGE);
            getActivity().getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        BrowseFileFragment browseFileFragment = this.browseFileFragment;
        if (browseFileFragment != null && this.photoTask == null) {
            MediaTask mediaTask = browseFileFragment.getMediaTask();
            this.photoTask = mediaTask;
            if (mediaTask != null && (this.browseFileFragment.currentFragment() instanceof LocalPhotoFragment)) {
                this.photoTask.setUIHandler(this.mHandler);
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
        MediaTask mediaTask = this.photoTask;
        if (mediaTask != null) {
            mediaTask.setUIHandler(null);
            this.photoTask = null;
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
        dismissWaitingDialog();
        if (getActivity() == null || this.mReceiver == null) {
            return;
        }
        getActivity().getApplicationContext().unregisterReceiver(this.mReceiver);
        this.mReceiver = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4134) {
            sendStateChange(1, false);
        }
        Dbug.e(this.TAG, "onActivityResult=" + i + ", resultCode=" + i2);
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
        if (this.mHandler == null || this.isLoading) {
            return;
        }
        this.isLoading = true;
        TimeLineAdapter timeLineAdapter = this.mAdapter;
        if (timeLineAdapter != null && timeLineAdapter.getCount() > 0) {
            this.mHandler.removeMessages(257);
            this.mHandler.sendEmptyMessageDelayed(257, 1500L);
        } else {
            ToastUtil.showToastShort(getString(R.string.no_more_data));
            onStopLoad();
        }
    }

    private void initListView() {
        if (getActivity() != null) {
            showWaitingDialog();
            new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalPhotoFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    LocalPhotoFragment.this.allDataList = AppUtils.queryAllLocalFileList(AppUtils.splicingFilePath(LocalPhotoFragment.this.mApplication.getAppName(), LocalPhotoFragment.this.mApplication.getUUID(), null, null), IConstant.DIR_DOWNLOAD);
                    if (LocalPhotoFragment.this.allDataList != null) {
                        LocalPhotoFragment localPhotoFragment = LocalPhotoFragment.this;
                        localPhotoFragment.allDataList = AppUtils.selectTypeList(localPhotoFragment.allDataList, 1);
                        Dbug.i(LocalPhotoFragment.this.TAG, "allDataList size = " + LocalPhotoFragment.this.allDataList.size());
                        if (LocalPhotoFragment.this.getActivity() != null) {
                            LocalPhotoFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalPhotoFragment.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (LocalPhotoFragment.this.mAdapter != null) {
                                        LocalPhotoFragment.this.mAdapter.clear();
                                    }
                                    LocalPhotoFragment.this.loadMoreData(0);
                                    LocalPhotoFragment.this.dismissWaitingDialog();
                                }
                            });
                            return;
                        }
                        return;
                    }
                    Dbug.e(LocalPhotoFragment.this.TAG, "allDataList is null");
                }
            }).start();
        }
    }

    private void showWaitingDialog() {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            this.mLoadingDialog = NotifyDialog.newInstance(R.string.dialog_tips, true, R.string.loading, 0, new NotifyDialog.OnConfirmClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalPhotoFragment.3
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnConfirmClickListener
                public void onClick() {
                    LocalPhotoFragment.this.mLoadingDialog.dismiss();
                }
            });
        }
        NotifyDialog notifyDialog = this.mLoadingDialog;
        if (notifyDialog == null || notifyDialog.isShowing() || getActivity() == null) {
            return;
        }
        this.mLoadingDialog.show(getActivity().getSupportFragmentManager(), "LoadingDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWaitingDialog() {
        NotifyDialog notifyDialog = this.mLoadingDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.mLoadingDialog.dismiss();
            }
            this.mLoadingDialog = null;
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
        ArrayList<String> arrayList = new ArrayList<>();
        for (FileInfo fileInfo2 : this.allDataList) {
            if (fileInfo2 != null) {
                String path2 = fileInfo2.getPath();
                if (AppUtils.checkFileExist(path2)) {
                    arrayList.add(path2);
                }
            }
        }
        int indexOf = arrayList.indexOf(path);
        if (arrayList.size() > 0) {
            Intent intent = new Intent(getActivity(), (Class<?>) GenericActivity.class);
            intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 7);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(IConstant.KEY_PATH_LIST, arrayList);
            bundle.putInt(IConstant.KEY_POSITION, indexOf);
            intent.putExtra(IConstant.KEY_DATA, bundle);
            startActivity(intent);
        }
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
                    if (size == 0) {
                        this.emptyView.setVisibility(0);
                        return;
                    } else {
                        ToastUtil.showToastShort(getString(R.string.no_more_data));
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
            if (fileInfo == null || this.photoTask == null) {
                return;
            }
            MediaTaskInfo mediaTaskInfo = new MediaTaskInfo();
            mediaTaskInfo.setInfo(fileInfo);
            mediaTaskInfo.setOp(i);
            this.photoTask.tryToStartTask(mediaTaskInfo);
            return;
        }
        sendMsg(this.selectedList.size());
        sendStateChange(1, false);
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
