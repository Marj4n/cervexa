package com.jieli.stream.dv.running2.ui.fragment.browse;

import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.ItemBean;
import com.jieli.stream.dv.running2.bean.MediaTaskInfo;
import com.jieli.stream.dv.running2.task.MediaTask;
import com.jieli.stream.dv.running2.ui.activity.GenericActivity;
import com.jieli.stream.dv.running2.ui.adapter.TimeLineAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.view.ExpandFooterView;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.json.JSonManager;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DevPhotoFragment extends BaseFragment implements View.OnClickListener, TimeLineAdapter.OnSubViewItemClickListener, BaseFooterView.OnLoadListener, OnNotifyListener {
    private static final int MSG_LOAD_DATE = 1;
    private LinearLayout bottomBar;
    private List<FileInfo> dataList;
    private String downloadDir;
    private RelativeLayout editLayout;
    private LinearLayout emptyView;
    private int fileOp;
    private ExpandFooterView footerView;
    private boolean isEditMode;
    private boolean isLoading;
    private boolean isSelectAll;
    private TimeLineAdapter mAdapter;
    private ListView mListView;
    private String msgContent;
    private RelativeLayout normalLayout;
    private List<FileInfo> photoInfoList;
    private MediaTask photoTask;
    private int retryNum;
    private TextView tvExitMode;
    private TextView tvSelectAll;
    private TextView tvSelected;
    private WaitingDialog waitingDialog;
    private List<FileInfo> selectedList = new ArrayList();
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.DevPhotoFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (DevPhotoFragment.this.getActivity() != null && message != null) {
                int i = message.what;
                if (i == 1) {
                    if (DevPhotoFragment.this.dataList != null) {
                        DevPhotoFragment devPhotoFragment = DevPhotoFragment.this;
                        devPhotoFragment.loadMoreData(devPhotoFragment.dataList.size(), 18);
                    }
                    DevPhotoFragment.this.stopLoad();
                } else if (i == 83) {
                    int i2 = message.arg1;
                    Dbug.i(DevPhotoFragment.this.TAG, "download file result : " + i2);
                    if (i2 == 0) {
                        DevPhotoFragment.this.handlerTaskList(false);
                    } else if (i2 == 1 || i2 == 2 || i2 == 3) {
                        DevPhotoFragment.this.handlerTaskList(true);
                    }
                }
            }
            return false;
        }
    });

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dev_photo, viewGroup, false);
        this.normalLayout = (RelativeLayout) inflate.findViewById(R.id.dev_photo_normal_layout);
        this.editLayout = (RelativeLayout) inflate.findViewById(R.id.dev_photo_edit_layout);
        this.bottomBar = (LinearLayout) inflate.findViewById(R.id.dev_photo_bottom_bar);
        this.tvSelectAll = (TextView) inflate.findViewById(R.id.dev_photo_select_all);
        this.tvSelected = (TextView) inflate.findViewById(R.id.dev_photo_select_num);
        this.tvExitMode = (TextView) inflate.findViewById(R.id.dev_photo_exit_mode);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.dev_photo_return);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.dev_photo_edit);
        this.emptyView = (LinearLayout) inflate.findViewById(R.id.view_empty);
        this.mListView = (ListView) inflate.findViewById(R.id.dev_photo_view);
        this.footerView = (ExpandFooterView) inflate.findViewById(R.id.dev_photo_footer);
        ((LinearLayout) inflate.findViewById(R.id.pop_bottom_bar_share)).setVisibility(8);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.pop_bottom_bar_download);
        linearLayout.setVisibility(0);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.pop_bottom_bar_delete);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        this.tvSelectAll.setOnClickListener(this);
        this.tvExitMode.setOnClickListener(this);
        this.footerView.setOnLoadListener(this);
        linearLayout.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            Bundle bundle2 = getBundle();
            if (bundle2 != null) {
                this.msgContent = bundle2.getString(IConstant.KEY_VIDEO_LIST);
            }
            if (this.mApplication.getUUID() != null && TextUtils.isEmpty(this.downloadDir)) {
                this.downloadDir = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), this.mApplication.getCameraDir(), IConstant.DIR_DOWNLOAD);
            }
            TimeLineAdapter timeLineAdapter = new TimeLineAdapter(getActivity().getApplicationContext());
            this.mAdapter = timeLineAdapter;
            this.mListView.setAdapter((ListAdapter) timeLineAdapter);
            this.mAdapter.setOnSubViewItemClickListener(this);
            tryToParseData(this.msgContent);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        MediaTask mediaTask = this.photoTask;
        if (mediaTask == null || mediaTask.isInterrupted()) {
            MediaTask mediaTask2 = new MediaTask(getContext(), "photo_task");
            this.photoTask = mediaTask2;
            mediaTask2.setUIHandler(this.mHandler);
            this.photoTask.start();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.isLoading = false;
        ClientManager.getClient().unregisterNotifyListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        dismissWaitingDialog();
        TimeLineAdapter timeLineAdapter = this.mAdapter;
        if (timeLineAdapter != null) {
            timeLineAdapter.cancelTasks();
            this.mAdapter = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        MediaTask mediaTask = this.photoTask;
        if (mediaTask != null) {
            mediaTask.tryToStopTask();
            List<FileInfo> list = this.selectedList;
            if (list != null) {
                list.clear();
            }
            this.photoTask.interrupt();
            this.photoTask.quit();
        }
        super.onDetach();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null || view == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.dev_photo_return) {
            getActivity().onBackPressed();
            return;
        }
        if (id == R.id.dev_photo_edit) {
            List<FileInfo> list = this.photoInfoList;
            if (list != null && list.size() > 0) {
                this.isEditMode = true;
                List<FileInfo> list2 = this.selectedList;
                if (list2 != null) {
                    list2.clear();
                }
                handlerEditUI();
                TimeLineAdapter timeLineAdapter = this.mAdapter;
                if (timeLineAdapter != null) {
                    timeLineAdapter.setEditMode(true);
                    this.mAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            }
            ToastUtil.showToastShort(getString(R.string.no_data_tip));
            return;
        }
        if (id == R.id.dev_photo_exit_mode) {
            this.isEditMode = false;
            this.isSelectAll = false;
            List<FileInfo> list3 = this.selectedList;
            if (list3 != null) {
                list3.clear();
            }
            handlerEditUI();
            handlerSelectAllUI();
            dismissWaitingDialog();
            TimeLineAdapter timeLineAdapter2 = this.mAdapter;
            if (timeLineAdapter2 != null) {
                timeLineAdapter2.setEditMode(false);
                this.mAdapter.notifyDataSetChanged();
            }
            List<FileInfo> list4 = this.photoInfoList;
            if (list4 != null && list4.size() == 0) {
                this.emptyView.setVisibility(0);
            } else {
                this.emptyView.setVisibility(8);
            }
            this.fileOp = 0;
            return;
        }
        if (id == R.id.dev_photo_select_all) {
            boolean z = !this.isSelectAll;
            this.isSelectAll = z;
            if (z) {
                this.selectedList.clear();
                for (FileInfo fileInfo : this.photoInfoList) {
                    if (fileInfo != null) {
                        fileInfo.setSelected(true);
                        this.selectedList.add(fileInfo);
                    }
                }
                for (FileInfo fileInfo2 : this.dataList) {
                    if (fileInfo2 != null) {
                        fileInfo2.setSelected(true);
                    }
                }
            } else {
                this.selectedList.clear();
                for (FileInfo fileInfo3 : this.photoInfoList) {
                    if (fileInfo3 != null) {
                        fileInfo3.setSelected(false);
                    }
                }
                for (FileInfo fileInfo4 : this.dataList) {
                    if (fileInfo4 != null) {
                        fileInfo4.setSelected(false);
                    }
                }
            }
            handlerSelectAllUI();
            TimeLineAdapter timeLineAdapter3 = this.mAdapter;
            if (timeLineAdapter3 != null) {
                timeLineAdapter3.notifyDataSetChanged();
                return;
            }
            return;
        }
        if (id == R.id.pop_bottom_bar_download) {
            List<FileInfo> list5 = this.selectedList;
            if (list5 != null && list5.size() > 0) {
                this.fileOp = IConstant.OP_DOWNLOAD_FILES;
                showWaitingDialog(getString(R.string.downloading_tip));
                handlerTaskList(false);
                return;
            }
            ToastUtil.showToastShort(getString(R.string.selected_file_empty_tip));
            return;
        }
        if (id == R.id.pop_bottom_bar_delete) {
            List<FileInfo> list6 = this.selectedList;
            if (list6 != null && list6.size() > 0) {
                showWaitingDialog(getString(R.string.deleting_tip));
                handlerDeleteFiles();
            } else {
                ToastUtil.showToastShort(getString(R.string.selected_file_empty_tip));
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.adapter.TimeLineAdapter.OnSubViewItemClickListener
    public void onSubItemClick(int i, int i2, FileInfo fileInfo) {
        TimeLineAdapter timeLineAdapter;
        if (fileInfo == null || (timeLineAdapter = this.mAdapter) == null || this.photoInfoList == null) {
            return;
        }
        if (timeLineAdapter.isEditMode()) {
            fileInfo.setSelected(!fileInfo.isSelected());
            if (fileInfo.isSelected()) {
                if (!this.selectedList.contains(fileInfo)) {
                    this.selectedList.add(fileInfo);
                }
                if (!this.isSelectAll && this.selectedList.size() == this.photoInfoList.size()) {
                    this.isSelectAll = true;
                    handlerSelectAllUI();
                }
            } else {
                this.selectedList.remove(fileInfo);
                if (this.isSelectAll) {
                    this.isSelectAll = false;
                    handlerSelectAllUI();
                }
            }
            setSelectNum(this.selectedList.size());
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        if (this.mApplication.getUUID() != null && TextUtils.isEmpty(this.downloadDir)) {
            this.downloadDir = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD);
        }
        String str = this.downloadDir + File.separator + AppUtils.getDownloadFilename(fileInfo);
        if (!AppUtils.checkFileExist(str)) {
            showWaitingDialog(getString(R.string.downloading_tip));
            this.selectedList.add(fileInfo);
            this.fileOp = IConstant.OP_DOWNLOAD_FILES;
            handlerTaskList(false);
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (FileInfo fileInfo2 : this.photoInfoList) {
            if (fileInfo2 != null) {
                String str2 = this.downloadDir + File.separator + AppUtils.getDownloadFilename(fileInfo2);
                if (AppUtils.checkFileExist(str2)) {
                    arrayList.add(str2);
                }
            }
        }
        int indexOf = arrayList.indexOf(str);
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

    private void setSelectNum(int i) {
        TextView textView = this.tvSelected;
        if (textView != null) {
            textView.setText(getString(R.string.selected_num, Integer.valueOf(i)));
        }
    }

    private void tryToParseData(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        JSonManager.getInstance().parseJSonData(str, new OnCompletedListener<Boolean>() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.DevPhotoFragment.2
            @Override // com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener
            public void onCompleted(Boolean bool) {
                if (bool.booleanValue()) {
                    DevPhotoFragment.this.photoInfoList = JSonManager.getInstance().getPictureInfoList();
                    if (DevPhotoFragment.this.photoInfoList != null && DevPhotoFragment.this.photoInfoList.size() > 0) {
                        Collections.sort(DevPhotoFragment.this.photoInfoList, DevPhotoFragment.this.new FileComparator());
                        if (DevPhotoFragment.this.dataList != null) {
                            DevPhotoFragment.this.dataList.clear();
                        }
                        if (DevPhotoFragment.this.mAdapter != null) {
                            DevPhotoFragment.this.mAdapter.clear();
                        }
                        DevPhotoFragment.this.loadMoreData(0, 18);
                        return;
                    }
                    Dbug.e(DevPhotoFragment.this.TAG, "-tryToParseData- parseJSonData photoInfoList is null!!!");
                    return;
                }
                Dbug.e(DevPhotoFragment.this.TAG, "-tryToParseData- parseJSonData failed!!!");
            }
        });
    }

    class FileComparator implements Comparator<FileInfo> {
        FileComparator() {
        }

        @Override // java.util.Comparator
        public int compare(FileInfo fileInfo, FileInfo fileInfo2) {
            if (fileInfo.getStartTime().getTimeInMillis() > fileInfo2.getStartTime().getTimeInMillis()) {
                return -1;
            }
            return fileInfo.getStartTime().getTimeInMillis() == fileInfo2.getStartTime().getTimeInMillis() ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMoreData(int i, int i2) {
        List<FileInfo> list = this.photoInfoList;
        if (list == null || list.size() <= 0) {
            return;
        }
        int size = this.photoInfoList.size();
        Dbug.w(this.TAG, "-loadMoreData- total size : " + size + ", startOffset = " + i);
        int i3 = size - i;
        this.emptyView.setVisibility(8);
        if (i3 <= 0) {
            if (i3 == 0) {
                if (size == 0) {
                    TimeLineAdapter timeLineAdapter = this.mAdapter;
                    if (timeLineAdapter != null) {
                        timeLineAdapter.clear();
                    }
                    this.emptyView.setVisibility(0);
                    return;
                }
                ToastUtil.showToastShort(getString(R.string.no_more_data));
                return;
            }
            return;
        }
        if (i3 > i2) {
            this.dataList = this.photoInfoList.subList(0, i + i2);
        } else {
            this.dataList = this.photoInfoList;
        }
        Dbug.w(this.TAG, "-loadMoreData- dataList size : " + this.dataList.size());
        List<ItemBean> convertDataList = AppUtils.convertDataList(this.dataList);
        if (convertDataList != null) {
            if (this.mAdapter == null) {
                this.mAdapter = new TimeLineAdapter(getActivity().getApplicationContext());
            }
            this.mListView.setAdapter((ListAdapter) this.mAdapter);
            this.mAdapter.clear();
            this.mAdapter.setDataList(convertDataList);
        }
    }

    private void handlerEditUI() {
        if (getActivity() == null) {
            return;
        }
        if (this.isEditMode) {
            RelativeLayout relativeLayout = this.normalLayout;
            if (relativeLayout != null && relativeLayout.getVisibility() != 8) {
                this.normalLayout.setVisibility(8);
            }
            RelativeLayout relativeLayout2 = this.editLayout;
            if (relativeLayout2 != null && relativeLayout2.getVisibility() != 0) {
                this.editLayout.setVisibility(0);
            }
            setSelectNum(0);
            LinearLayout linearLayout = this.bottomBar;
            if (linearLayout == null || linearLayout.getVisibility() == 0) {
                return;
            }
            this.bottomBar.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_bottom));
            this.bottomBar.setVisibility(0);
            return;
        }
        if (this.editLayout.getVisibility() != 8) {
            this.editLayout.setVisibility(8);
        }
        LinearLayout linearLayout2 = this.bottomBar;
        if (linearLayout2 != null && linearLayout2.getVisibility() != 8) {
            this.bottomBar.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom));
            this.bottomBar.setVisibility(8);
        }
        if (this.normalLayout.getVisibility() != 0) {
            this.normalLayout.setVisibility(0);
        }
    }

    private void handlerSelectAllUI() {
        if (this.isSelectAll) {
            this.tvSelectAll.setText(getString(R.string.cancel_all_select));
            List<FileInfo> list = this.selectedList;
            if (list != null) {
                setSelectNum(list.size());
                return;
            }
            return;
        }
        this.tvSelectAll.setText(getString(R.string.all_select));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerTaskList(boolean z) {
        List<FileInfo> list = this.selectedList;
        if (list != null) {
            int size = list.size();
            if (z) {
                if (size > 0) {
                    FileInfo remove = this.selectedList.remove(0);
                    if (this.fileOp == 164) {
                        updateDeleteUI(remove);
                    }
                }
                this.retryNum = 0;
            } else {
                int i = this.retryNum + 1;
                this.retryNum = i;
                if (i > 2) {
                    this.retryNum = 0;
                    if (size > 0) {
                        this.selectedList.remove(0);
                    }
                }
            }
            if (this.selectedList.size() > 0) {
                setSelectNum(this.selectedList.size());
                FileInfo fileInfo = this.selectedList.get(0);
                if (fileInfo == null || this.photoTask == null) {
                    return;
                }
                MediaTaskInfo mediaTaskInfo = new MediaTaskInfo();
                mediaTaskInfo.setInfo(fileInfo);
                mediaTaskInfo.setOp(this.fileOp);
                this.photoTask.tryToStartTask(mediaTaskInfo);
                return;
            }
            setSelectNum(this.selectedList.size());
            this.tvExitMode.performClick();
        }
    }

    private void handlerDeleteFiles() {
        List<FileInfo> list = this.selectedList;
        if (list != null && list.size() > 0) {
            ArrayList arrayList = new ArrayList();
            Iterator<FileInfo> it = this.selectedList.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getPath());
            }
            ClientManager.getClient().tryToDeleteFile(arrayList, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.DevPhotoFragment.3
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        DevPhotoFragment.this.dismissWaitingDialog();
                    }
                }
            });
            return;
        }
        dismissWaitingDialog();
    }

    private void showWaitingDialog(String str) {
        if (this.waitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.waitingDialog = waitingDialog;
            waitingDialog.setCancelable(false);
            this.waitingDialog.setNotifyContent(str);
            this.waitingDialog.setOnWaitingDialog(new WaitingDialog.OnWaitingDialog() { // from class: com.jieli.stream.dv.running2.ui.fragment.browse.DevPhotoFragment.4
                @Override // com.jieli.stream.dv.running2.ui.dialog.WaitingDialog.OnWaitingDialog
                public void onCancelDialog() {
                    if (DevPhotoFragment.this.selectedList != null) {
                        DevPhotoFragment.this.cancelLoading();
                        DevPhotoFragment.this.selectedList.clear();
                    }
                    if (DevPhotoFragment.this.photoTask != null) {
                        DevPhotoFragment.this.photoTask.tryToStopTask();
                    }
                    DevPhotoFragment.this.handlerTaskList(true);
                }
            });
        }
        if (!TextUtils.isEmpty(str)) {
            this.waitingDialog.updateNotifyContent(str);
        }
        if (this.waitingDialog.isShowing()) {
            return;
        }
        this.waitingDialog.show(getFragmentManager(), "wait_dialog");
    }

    public void dismissWaitingDialog() {
        if (this.waitingDialog != null) {
            if (getActivity() != null && this.waitingDialog.isShowing()) {
                this.waitingDialog.dismiss();
            }
            this.waitingDialog.setOnWaitingDialog(null);
            this.waitingDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLoad() {
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
        handler.sendEmptyMessageDelayed(1, 1500L);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
    public void onNotify(NotifyInfo notifyInfo) {
        int count;
        List<FileInfo> list;
        int count2;
        if (notifyInfo != null) {
            int errorType = notifyInfo.getErrorType();
            String topic = notifyInfo.getTopic();
            if (errorType == 0) {
                if (TextUtils.isEmpty(topic)) {
                    return;
                }
                char c = 65535;
                switch (topic.hashCode()) {
                    case -747326317:
                        if (topic.equals(Topic.FILES_DELETE)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -573527648:
                        if (topic.equals(Topic.TF_STATUS)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 6563960:
                        if (topic.equals(Topic.PHOTO_CTRL)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 2079517687:
                        if (topic.equals(Topic.FORMAT_TF_CARD)) {
                            c = 0;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                        return;
                    }
                    return;
                }
                if (c == 1) {
                    if (notifyInfo.getParams() == null || !"0".equals(notifyInfo.getParams().get(TopicKey.ONLINE)) || getActivity() == null) {
                        return;
                    }
                    dismissWaitingDialog();
                    getActivity().onBackPressed();
                    return;
                }
                int i = 18;
                if (c == 2) {
                    if (notifyInfo.getParams() == null) {
                        return;
                    }
                    String str = notifyInfo.getParams().get("desc");
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    String replaceAll = str.replaceAll("\\\\", "");
                    Dbug.w(this.TAG, "-PHOTO_CTRL- photoDesc = " + replaceAll);
                    FileInfo parseFileInfo = JSonManager.parseFileInfo(replaceAll);
                    if (parseFileInfo != null) {
                        if (("1".equals(parseFileInfo.getCameraType()) ? 2 : 1) == this.mApplication.getDeviceSettingInfo().getCameraType()) {
                            if (this.photoInfoList == null) {
                                this.photoInfoList = new ArrayList();
                            }
                            this.photoInfoList.add(0, parseFileInfo);
                            this.msgContent = JSonManager.convertJson(this.photoInfoList);
                            TimeLineAdapter timeLineAdapter = this.mAdapter;
                            if (timeLineAdapter != null && (count = timeLineAdapter.getCount()) > 18) {
                                i = count;
                            }
                            loadMoreData(0, i);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (c == 3 && notifyInfo.getParams() != null) {
                    String str2 = notifyInfo.getParams().get(TopicKey.PATH);
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                    Dbug.w(this.TAG, "-FILES_DELETE- delPath = " + str2);
                    FileInfo findFileInfo = findFileInfo(str2);
                    if (findFileInfo == null || (list = this.photoInfoList) == null || !list.remove(findFileInfo)) {
                        return;
                    }
                    List<FileInfo> list2 = this.selectedList;
                    if (list2 != null) {
                        list2.remove(findFileInfo);
                        setSelectNum(this.selectedList.size());
                    }
                    TimeLineAdapter timeLineAdapter2 = this.mAdapter;
                    if (timeLineAdapter2 != null && (count2 = timeLineAdapter2.getCount()) > 18) {
                        i = count2;
                    }
                    loadMoreData(0, i);
                    List<FileInfo> list3 = this.selectedList;
                    if (list3 != null && list3.size() == 0) {
                        dismissWaitingDialog();
                        this.msgContent = JSonManager.convertJson(this.photoInfoList);
                        this.tvExitMode.performClick();
                        return;
                    }
                    this.msgContent = JSonManager.convertJson(this.photoInfoList);
                    return;
                }
                return;
            }
            if (Topic.PHOTO_CTRL.equals(topic) || Topic.FILES_DELETE.equals(topic)) {
                dismissWaitingDialog();
                List<FileInfo> list4 = this.selectedList;
                if (list4 == null || list4.size() <= 0) {
                    return;
                }
                this.selectedList.clear();
                this.tvExitMode.performClick();
            }
        }
    }

    private void updateDeleteUI(FileInfo fileInfo) {
        TimeLineAdapter timeLineAdapter;
        if (fileInfo == null || (timeLineAdapter = this.mAdapter) == null || this.photoInfoList == null) {
            return;
        }
        int count = timeLineAdapter.getCount();
        ThumbLoader.getInstance().removeBitmap(fileInfo.getPath());
        int i = count - 1;
        this.photoInfoList.remove(fileInfo);
        if (i >= this.photoInfoList.size()) {
            this.dataList = this.photoInfoList.subList(0, i);
        } else {
            this.dataList = this.photoInfoList;
        }
        List<ItemBean> convertDataList = AppUtils.convertDataList(this.dataList);
        this.mAdapter.clear();
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mAdapter.setDataList(convertDataList);
    }

    private FileInfo findFileInfo(String str) {
        List<FileInfo> list;
        if (!TextUtils.isEmpty(str) && (list = this.photoInfoList) != null) {
            for (FileInfo fileInfo : list) {
                if (str.equals(fileInfo.getPath())) {
                    return fileInfo;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelLoading() {
        List<FileInfo> list = this.selectedList;
        if (list == null || list.size() <= 0) {
            return;
        }
        FileInfo fileInfo = this.selectedList.get(0);
        if (this.mApplication.getUUID() != null && TextUtils.isEmpty(this.downloadDir)) {
            this.downloadDir = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD);
        }
        String str = this.downloadDir + File.separator + AppUtils.getDownloadFilename(fileInfo);
        if (AppUtils.checkFileExist(str)) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
