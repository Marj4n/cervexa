package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
public class LocalVideoFragment extends BaseFragment implements BaseFooterView.OnLoadListener, TimeLineAdapter.OnSubViewItemClickListener {
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
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalVideoFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (LocalVideoFragment.this.getActivity() != null && message != null) {
                int i = message.what;
                if (i == 84) {
                    int i2 = message.arg1;
                    if (i2 == 0) {
                        LocalVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                    } else if (i2 == 1) {
                        LocalVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, true);
                    }
                } else if (i == 257) {
                    if (LocalVideoFragment.this.dataList != null) {
                        LocalVideoFragment localVideoFragment = LocalVideoFragment.this;
                        localVideoFragment.loadMoreData(localVideoFragment.dataList.size());
                    }
                    LocalVideoFragment.this.onStopLoad();
                }
            }
            return false;
        }
    });
    private ListView mListView;
    private int mOp;
    private LocalVideoBroadcast mReceiver;
    private int retryNum;
    private List<FileInfo> selectedList;
    private MediaTask videoTask;

    private class LocalVideoBroadcast extends BroadcastReceiver {
        private LocalVideoBroadcast() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            int size;
            if (LocalVideoFragment.this.getActivity() == null || context == null || intent == null || !LocalVideoFragment.this.isVisible() || LocalVideoFragment.this.browseFileFragment == null || !(LocalVideoFragment.this.browseFileFragment.currentFragment() instanceof LocalVideoFragment)) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (LocalVideoFragment.this.videoTask != null) {
                LocalVideoFragment.this.videoTask.setUIHandler(LocalVideoFragment.this.mHandler);
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
                LocalVideoFragment.this.updateTextUI();
                return;
            }
            int intExtra = intent.getIntExtra(IConstant.KEY_BROWSE_OPERATION, -1);
            Dbug.w(LocalVideoFragment.this.TAG, "receive op : " + intExtra);
            switch (intExtra) {
                case IConstant.OP_ENTER_EDIT_MODE /* 161 */:
                    if (LocalVideoFragment.this.allDataList != null) {
                        if (LocalVideoFragment.this.allDataList.size() > 0) {
                            LocalVideoFragment.this.sendStateChange(1, true);
                            if (LocalVideoFragment.this.mAdapter != null) {
                                LocalVideoFragment.this.mAdapter.setEditMode(true);
                                LocalVideoFragment.this.mAdapter.notifyDataSetChanged();
                                break;
                            }
                        } else {
                            ToastUtil.showToastShort(LocalVideoFragment.this.getString(R.string.no_data_tip));
                            LocalVideoFragment.this.sendStateChange(1, false);
                            break;
                        }
                    }
                    break;
                case IConstant.OP_EXIT_EDIT_MODE /* 162 */:
                    LocalVideoFragment.this.isSelectAll = false;
                    LocalVideoFragment.this.isOpenTask = false;
                    LocalVideoFragment.this.selectedList.clear();
                    LocalVideoFragment.this.sendStateChange(2, false);
                    if (LocalVideoFragment.this.mAdapter != null) {
                        LocalVideoFragment.this.mAdapter.setEditMode(false);
                        if (LocalVideoFragment.this.mOp == 164) {
                            LocalVideoFragment.this.mAdapter.clear();
                            LocalVideoFragment.this.loadMoreData(0);
                        } else {
                            LocalVideoFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                    if (LocalVideoFragment.this.browseFileFragment != null) {
                        LocalVideoFragment.this.browseFileFragment.dismissWaitingDialog();
                    }
                    LocalVideoFragment.this.mOp = 0;
                    break;
                case IConstant.OP_DELETE_FILES /* 164 */:
                    if (LocalVideoFragment.this.selectedList != null && LocalVideoFragment.this.selectedList.size() > 0) {
                        LocalVideoFragment.this.isOpenTask = true;
                        if (LocalVideoFragment.this.browseFileFragment != null) {
                            LocalVideoFragment.this.browseFileFragment.showWaitingDialog();
                        }
                        LocalVideoFragment.this.handlerTaskList(IConstant.OP_DELETE_FILES, false);
                        break;
                    } else {
                        ToastUtil.showToastShort(LocalVideoFragment.this.getString(R.string.selected_file_empty_tip));
                        break;
                    }
                case IConstant.OP_SELECT_ALL /* 165 */:
                    LocalVideoFragment.this.isSelectAll = true;
                    LocalVideoFragment.this.selectedList.clear();
                    if (LocalVideoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo : LocalVideoFragment.this.allDataList) {
                            if (fileInfo != null) {
                                fileInfo.setSelected(true);
                                LocalVideoFragment.this.selectedList.add(fileInfo);
                            }
                        }
                    }
                    if (LocalVideoFragment.this.dataList != null) {
                        for (FileInfo fileInfo2 : LocalVideoFragment.this.dataList) {
                            if (fileInfo2 != null) {
                                fileInfo2.setSelected(true);
                            }
                        }
                    }
                    LocalVideoFragment localVideoFragment = LocalVideoFragment.this;
                    localVideoFragment.sendMsg(localVideoFragment.selectedList.size());
                    if (LocalVideoFragment.this.mAdapter != null) {
                        LocalVideoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_CANCEL_SELECT_ALL /* 166 */:
                    LocalVideoFragment.this.isSelectAll = false;
                    LocalVideoFragment.this.selectedList.clear();
                    if (LocalVideoFragment.this.allDataList != null) {
                        for (FileInfo fileInfo3 : LocalVideoFragment.this.allDataList) {
                            if (fileInfo3 != null) {
                                fileInfo3.setSelected(false);
                            }
                        }
                    }
                    if (LocalVideoFragment.this.dataList != null) {
                        for (FileInfo fileInfo4 : LocalVideoFragment.this.dataList) {
                            if (fileInfo4 != null) {
                                fileInfo4.setSelected(false);
                            }
                        }
                    }
                    LocalVideoFragment localVideoFragment2 = LocalVideoFragment.this;
                    localVideoFragment2.sendMsg(localVideoFragment2.selectedList.size());
                    if (LocalVideoFragment.this.mAdapter != null) {
                        LocalVideoFragment.this.mAdapter.notifyDataSetChanged();
                        break;
                    }
                    break;
                case IConstant.OP_SHARE_FILES /* 167 */:
                    if (LocalVideoFragment.this.selectedList != null && (size = LocalVideoFragment.this.selectedList.size()) > 0) {
                        if (size == 1) {
                            FileInfo fileInfo5 = (FileInfo) LocalVideoFragment.this.selectedList.get(0);
                            Intent intent2 = new Intent("android.intent.action.SEND");
                            intent2.setType("video/*");
                            intent2.putExtra("android.intent.extra.STREAM", Uri.parse(fileInfo5.getPath()));
                            LocalVideoFragment localVideoFragment3 = LocalVideoFragment.this;
                            localVideoFragment3.startActivityForResult(Intent.createChooser(intent2, localVideoFragment3.getString(R.string.tab_share)), IConstant.CODE_SHARE_FILES);
                            break;
                        } else {
                            ArrayList<Uri> arrayList = new ArrayList<>();
                            Iterator it = LocalVideoFragment.this.selectedList.iterator();
                            while (it.hasNext()) {
                                arrayList.add(Uri.parse(((FileInfo) it.next()).getPath()));
                            }
                            Intent intent3 = new Intent("android.intent.action.SEND_MULTIPLE");
                            intent3.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                            intent3.setType("video/*");
                            LocalVideoFragment localVideoFragment4 = LocalVideoFragment.this;
                            localVideoFragment4.startActivityForResult(Intent.createChooser(intent3, localVideoFragment4.getString(R.string.tab_share)), IConstant.CODE_SHARE_FILES);
                            break;
                        }
                    }
                    break;
                case IConstant.OP_CANCEL_TASK /* 168 */:
                    if (LocalVideoFragment.this.selectedList != null) {
                        LocalVideoFragment.this.selectedList.clear();
                    }
                    if (LocalVideoFragment.this.videoTask != null) {
                        LocalVideoFragment.this.videoTask.tryToStopTask();
                    }
                    LocalVideoFragment localVideoFragment5 = LocalVideoFragment.this;
                    localVideoFragment5.handlerTaskList(localVideoFragment5.mOp, true);
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
        View inflate = layoutInflater.inflate(R.layout.fragment_local_video, viewGroup, false);
        this.mListView = (ListView) inflate.findViewById(R.id.local_video_view);
        this.emptyView = (LinearLayout) inflate.findViewById(R.id.view_empty);
        ExpandFooterView expandFooterView = (ExpandFooterView) inflate.findViewById(R.id.local_video_footer);
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
            if (this.mReceiver == null) {
                this.mReceiver = new LocalVideoBroadcast();
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
        if (browseFileFragment != null && this.videoTask == null) {
            MediaTask mediaTask = browseFileFragment.getMediaTask();
            this.videoTask = mediaTask;
            if (mediaTask != null && (this.browseFileFragment.currentFragment() instanceof LocalVideoFragment)) {
                this.videoTask.setUIHandler(this.mHandler);
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
        MediaTask mediaTask = this.videoTask;
        if (mediaTask != null) {
            mediaTask.setUIHandler(null);
            this.videoTask = null;
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
        new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalVideoFragment.2
            @Override // java.lang.Runnable
            public void run() {
                String splicingFilePath = AppUtils.splicingFilePath(LocalVideoFragment.this.mApplication.getAppName(), LocalVideoFragment.this.mApplication.getUUID(), null, null);
                Dbug.w(LocalVideoFragment.this.TAG, "start query srcPath=" + splicingFilePath);
                LocalVideoFragment.this.allDataList = AppUtils.queryAllLocalFileList(splicingFilePath, IConstant.DIR_DOWNLOAD);
                if (LocalVideoFragment.this.allDataList != null) {
                    LocalVideoFragment localVideoFragment = LocalVideoFragment.this;
                    localVideoFragment.allDataList = AppUtils.selectTypeList(localVideoFragment.allDataList, 2);
                    Dbug.i(LocalVideoFragment.this.TAG, "allDataList size = " + LocalVideoFragment.this.allDataList.size());
                    if (LocalVideoFragment.this.getActivity() != null) {
                        LocalVideoFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.LocalVideoFragment.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (LocalVideoFragment.this.mAdapter != null) {
                                    LocalVideoFragment.this.mAdapter.clear();
                                }
                                LocalVideoFragment.this.loadMoreData(0);
                            }
                        });
                        return;
                    }
                    return;
                }
                Dbug.e(LocalVideoFragment.this.TAG, "allDataList is null");
            }
        }).start();
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
        if (this.selectedList.size() > 0) {
            sendMsg(this.selectedList.size());
            FileInfo fileInfo = this.selectedList.get(0);
            if (fileInfo == null || this.videoTask == null) {
                return;
            }
            MediaTaskInfo mediaTaskInfo = new MediaTaskInfo();
            mediaTaskInfo.setInfo(fileInfo);
            mediaTaskInfo.setOp(i);
            this.videoTask.tryToStartTask(mediaTaskInfo);
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
