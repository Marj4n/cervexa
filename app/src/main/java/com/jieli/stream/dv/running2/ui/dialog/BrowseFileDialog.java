package com.jieli.stream.dv.running2.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SDFileInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.ui.dialog.InputContentDialog;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.FileUtil;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BrowseFileDialog extends BaseDialogFragment implements IConstant, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final int MOVE_DIR_MSG = 41120;
    private String currentPath;
    private TextView currentPathTv;
    private NotifyDialog deleteDirDialog;
    private List<SDFileInfo> fileInfoList;
    private FileListAdapter mAdapter;
    private ListView mListView;
    private NotifyDialog movingDialog;
    private MovingDirThread movingDirThread;
    private OnSelectResultListener onSelectResultListener;
    private String tag = getClass().getSimpleName();
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != BrowseFileDialog.MOVE_DIR_MSG) {
                return false;
            }
            int i = message.arg1;
            BrowseFileDialog.this.controlDialog(i);
            if (i != 1) {
                return false;
            }
            if (message.arg2 == 1) {
                if (BrowseFileDialog.this.onSelectResultListener != null) {
                    BrowseFileDialog.this.onSelectResultListener.onResult(BrowseFileDialog.this.currentPath);
                }
                Dbug.w("BrowseFileDialog", "select document path :" + BrowseFileDialog.this.currentPath);
                BrowseFileDialog.this.dismiss();
                return false;
            }
            ToastUtil.showToastShort(BrowseFileDialog.this.getString(R.string.modify_storage_url_failed));
            return false;
        }
    });

    public interface OnSelectResultListener {
        void onResult(String str);
    }

    public void setOnSelectResultListener(OnSelectResultListener onSelectResultListener) {
        this.onSelectResultListener = onSelectResultListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, android.R.style.Theme.Light.NoTitleBar);
        setCancelable(false);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_browse_file, viewGroup, false);
        this.currentPathTv = (TextView) inflate.findViewById(R.id.dialog_file_path);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.create_dir_btn);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.dialog_return_path);
        this.mListView = (ListView) inflate.findViewById(R.id.dialog_file_list);
        Button button = (Button) inflate.findViewById(R.id.dialog_file_cancel_btn);
        Button button2 = (Button) inflate.findViewById(R.id.dialog_file_confirm_btn);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnItemLongClickListener(this);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null || getActivity().getWindow() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        getDialog().getWindow().setAttributes(attributes);
        if (this.fileInfoList == null) {
            this.fileInfoList = new ArrayList();
        }
        this.currentPath = ROOT_PATH;
        if (this.mAdapter == null) {
            this.mAdapter = new FileListAdapter(getActivity(), this.fileInfoList);
        }
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        viewFiles(this.currentPath);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                BrowseFileDialog.this.onKeyBack();
                return false;
            }
        });
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        NotifyDialog notifyDialog = this.movingDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.movingDialog.dismiss();
            }
            this.movingDialog = null;
        }
        NotifyDialog notifyDialog2 = this.deleteDirDialog;
        if (notifyDialog2 != null) {
            if (notifyDialog2.isShowing()) {
                this.deleteDirDialog.dismiss();
            }
            this.deleteDirDialog = null;
        }
        super.onDetach();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void viewFiles(String str) {
        ArrayList<SDFileInfo> files = AppUtils.getFiles(str);
        if (files != null) {
            this.fileInfoList.clear();
            this.fileInfoList.addAll(files);
            files.clear();
            this.currentPath = str;
            this.currentPathTv.setText(str);
            FileListAdapter fileListAdapter = this.mAdapter;
            if (fileListAdapter != null) {
                fileListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null || getActivity().getWindow() == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.create_dir_btn) {
            InputContentDialog inputContentDialog = new InputContentDialog();
            inputContentDialog.setOnContentListener(new InputContentDialog.OnContentListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.3
                @Override // com.jieli.stream.dv.running2.ui.dialog.InputContentDialog.OnContentListener
                public void onInput(String str) {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    String str2 = BrowseFileDialog.this.currentPath + File.separator + str;
                    if (new File(str2).mkdir()) {
                        ToastUtil.showToastShort(BrowseFileDialog.this.getString(R.string.create_dir_success));
                        BrowseFileDialog.this.currentPath = str2;
                        BrowseFileDialog browseFileDialog = BrowseFileDialog.this;
                        browseFileDialog.viewFiles(browseFileDialog.currentPath);
                    }
                }
            });
            inputContentDialog.show(getActivity().getSupportFragmentManager(), "input_content_dialog");
            return;
        }
        if (id == R.id.dialog_return_path) {
            onKeyBack();
            return;
        }
        if (id == R.id.dialog_file_cancel_btn) {
            dismiss();
            return;
        }
        if (id == R.id.dialog_file_confirm_btn) {
            if (ROOT_PATH.equals(this.currentPath)) {
                ToastUtil.showToastShort(getString(R.string.select_dir_error));
                return;
            }
            if (AppUtils.checkIsEmptyFolder(this.currentPath)) {
                String str = ROOT_PATH + File.separator + MainApplication.getApplication().getAppName();
                if (this.movingDirThread == null) {
                    MovingDirThread movingDirThread = new MovingDirThread(str, this.currentPath, this.mHandler);
                    this.movingDirThread = movingDirThread;
                    movingDirThread.start();
                    return;
                }
                return;
            }
            ToastUtil.showToastShort(getString(R.string.select_dir_tip));
        }
    }

    public void onKeyBack() {
        if (ROOT_PATH.equals(this.currentPath)) {
            dismiss();
            return;
        }
        String str = this.currentPath;
        String substring = str.substring(0, str.lastIndexOf("/"));
        this.currentPath = substring;
        viewFiles(substring);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        SDFileInfo sDFileInfo;
        FileListAdapter fileListAdapter = this.mAdapter;
        if (fileListAdapter == null || i >= fileListAdapter.getCount() || (sDFileInfo = (SDFileInfo) this.mAdapter.getItem(i)) == null) {
            return;
        }
        String str = sDFileInfo.Path;
        this.currentPath = str;
        viewFiles(str);
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        SDFileInfo sDFileInfo;
        FileListAdapter fileListAdapter = this.mAdapter;
        if (fileListAdapter == null || i >= fileListAdapter.getCount() || (sDFileInfo = (SDFileInfo) this.mAdapter.getItem(i)) == null) {
            return true;
        }
        controlDeleteDirDialog(sDFileInfo.Path, 0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void controlDialog(int i) {
        if (getActivity() == null) {
            return;
        }
        if (this.movingDialog == null) {
            this.movingDialog = NotifyDialog.newInstance(R.string.moving_dir_tip, true);
        }
        if (i == 0) {
            if (this.movingDialog.isShowing() || getActivity().isFinishing()) {
                return;
            }
            this.movingDialog.show(getActivity().getSupportFragmentManager(), "moving_dialog");
            return;
        }
        if (i == 1 && this.movingDialog.isShowing()) {
            this.movingDialog.dismiss();
            this.movingDialog = null;
        }
    }

    private void controlDeleteDirDialog(String str, int i) {
        if (getActivity() == null) {
            return;
        }
        String format = String.format(getString(R.string.delete_dir_tip), str);
        if (this.deleteDirDialog == null) {
            this.deleteDirDialog = NotifyDialog.newInstance(getString(R.string.dialog_tips), format, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.4
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    BrowseFileDialog.this.deleteDirDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.5
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    Bundle bundle = BrowseFileDialog.this.deleteDirDialog.getBundle();
                    if (bundle != null) {
                        String string = bundle.getString(IConstant.KEY_DIR_PATH, null);
                        if (!TextUtils.isEmpty(string)) {
                            if (FileUtil.deleteFile(new File(string))) {
                                ToastUtil.showToastShort(BrowseFileDialog.this.getString(R.string.delete_dir_success));
                                BrowseFileDialog browseFileDialog = BrowseFileDialog.this;
                                browseFileDialog.viewFiles(browseFileDialog.currentPath);
                            } else {
                                ToastUtil.showToastShort(BrowseFileDialog.this.getString(R.string.delete_dir_failed));
                            }
                        }
                    }
                    BrowseFileDialog.this.deleteDirDialog.dismiss();
                }
            });
        }
        Bundle bundle = new Bundle();
        bundle.putString(IConstant.KEY_DIR_PATH, str);
        this.deleteDirDialog.setBundle(bundle);
        this.deleteDirDialog.setContent(format);
        if (i == 0) {
            if (this.deleteDirDialog.isShowing() || getActivity().isFinishing()) {
                return;
            }
            this.deleteDirDialog.show(getActivity().getSupportFragmentManager(), "delete_dir_dialog");
            return;
        }
        if (i == 1 && this.deleteDirDialog.isShowing()) {
            this.deleteDirDialog.dismiss();
            this.deleteDirDialog = null;
        }
    }

    private class MovingDirThread extends Thread {
        private String destPath;
        private SoftReference<Handler> softReference;
        private String srcPath;

        MovingDirThread(String str, String str2, Handler handler) {
            this.srcPath = str;
            this.destPath = str2;
            this.softReference = new SoftReference<>(handler);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            Handler handler = this.softReference.get();
            if (handler != null) {
                handler.sendEmptyMessageDelayed(BrowseFileDialog.MOVE_DIR_MSG, 0L);
                if (FileUtil.moveDirectory(this.srcPath, this.destPath)) {
                    handler.sendMessage(handler.obtainMessage(BrowseFileDialog.MOVE_DIR_MSG, 1, 1, this.destPath));
                } else {
                    handler.sendMessage(handler.obtainMessage(BrowseFileDialog.MOVE_DIR_MSG, 1, 0, this.srcPath));
                }
            }
        }
    }

    private class FileListAdapter extends BaseAdapter {
        private List<SDFileInfo> fileInfoList;
        private LayoutInflater inflater;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        FileListAdapter(Context context, List<SDFileInfo> list) {
            this.fileInfoList = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<SDFileInfo> list = this.fileInfoList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            List<SDFileInfo> list = this.fileInfoList;
            if (list == null || list.size() < 0 || i >= this.fileInfoList.size()) {
                return null;
            }
            return this.fileInfoList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.item_file_path, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) view.findViewById(R.id.file_icon);
                viewHolder.name = (TextView) view.findViewById(R.id.file_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            SDFileInfo sDFileInfo = (SDFileInfo) getItem(i);
            if (sDFileInfo != null) {
                viewHolder.name.setText(sDFileInfo.Name);
                if (sDFileInfo.IsDirectory) {
                    viewHolder.icon.setImageResource(R.mipmap.ic_folder);
                } else {
                    viewHolder.icon.setImageResource(R.mipmap.ic_file_icon);
                }
            }
            return view;
        }

        private class ViewHolder {
            ImageView icon;
            TextView name;

            private ViewHolder() {
            }
        }
    }
}
