package com.jieli.stream.dv.running2.ui.dialog;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.jieli.stream.dv.running2.bean.SDFileInfo;
import com.jieli.stream.dv.running2.interfaces.OnSelectedListener;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BrowseFirmwareDialog extends BaseDialogFragment implements IConstant, View.OnClickListener, AdapterView.OnItemClickListener {
    private String currentPath;
    private TextView currentPathTv;
    private NotifyDialog deleteDirDialog;
    private List<SDFileInfo> fileInfoList;
    private FileListAdapter mAdapter;
    private ListView mListView;
    private NotifyDialog movingDialog;
    private OnSelectedListener<String> onSelectResultListener;
    private String tag = getClass().getSimpleName();
    private SDFileInfo mSelectedFile = null;

    public void setOnSelectedListener(OnSelectedListener<String> onSelectedListener) {
        this.onSelectResultListener = onSelectedListener;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(com.jieli.stream.dv.running2.R.layout.dialog_browse_firmware, viewGroup, false);
        this.currentPathTv = (TextView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_file_path);
        ImageView imageView = (ImageView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_return_path);
        this.mListView = (ListView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_file_list);
        Button button = (Button) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_file_cancel_btn);
        Button button2 = (Button) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_file_confirm_btn);
        this.mListView.setOnItemClickListener(this);
        imageView.setOnClickListener(this);
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
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.BrowseFirmwareDialog.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                BrowseFirmwareDialog.this.onKeyBack();
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
        this.mSelectedFile = null;
        super.onDetach();
    }

    private void viewFiles(String str) {
        ArrayList<SDFileInfo> firmwareFile = AppUtils.getFirmwareFile(str);
        if (firmwareFile != null) {
            this.fileInfoList.clear();
            this.fileInfoList.addAll(firmwareFile);
            firmwareFile.clear();
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
        if (id == com.jieli.stream.dv.running2.R.id.dialog_return_path) {
            onKeyBack();
            return;
        }
        if (id == com.jieli.stream.dv.running2.R.id.dialog_file_cancel_btn) {
            dismiss();
            return;
        }
        if (id == com.jieli.stream.dv.running2.R.id.dialog_file_confirm_btn) {
            SDFileInfo sDFileInfo = this.mSelectedFile;
            if (sDFileInfo != null) {
                ToastUtil.showToastShort(sDFileInfo.Name);
                dismiss();
                OnSelectedListener<String> onSelectedListener = this.onSelectResultListener;
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(this.mSelectedFile.Path);
                    return;
                }
                return;
            }
            ToastUtil.showToastShort(getString(com.jieli.stream.dv.running2.R.string.selected_file_empty_tip));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        FileListAdapter fileListAdapter = this.mAdapter;
        if (fileListAdapter == null || i >= fileListAdapter.getCount()) {
            return;
        }
        SDFileInfo sDFileInfo = (SDFileInfo) this.mAdapter.getItem(i);
        if (sDFileInfo != null) {
            File file = new File(sDFileInfo.Path);
            if (file.isFile()) {
                Dbug.i(this.tag, "File name=" + file.getAbsolutePath());
                this.mSelectedFile = sDFileInfo;
                this.mAdapter.notifyDataSetChanged();
                return;
            }
            this.currentPath = sDFileInfo.Path;
            viewFiles(sDFileInfo.Path);
            return;
        }
        Dbug.w(this.tag, "file is null");
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
                view = this.inflater.inflate(com.jieli.stream.dv.running2.R.layout.item_file_path, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) view.findViewById(com.jieli.stream.dv.running2.R.id.file_icon);
                viewHolder.name = (TextView) view.findViewById(com.jieli.stream.dv.running2.R.id.file_name);
                viewHolder.flag = (ImageView) view.findViewById(com.jieli.stream.dv.running2.R.id.file_selected);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            SDFileInfo sDFileInfo = (SDFileInfo) getItem(i);
            if (sDFileInfo != null) {
                viewHolder.name.setText(sDFileInfo.Name);
                if (sDFileInfo.IsDirectory) {
                    viewHolder.icon.setImageResource(com.jieli.stream.dv.running2.R.mipmap.ic_folder);
                } else {
                    viewHolder.icon.setImageResource(com.jieli.stream.dv.running2.R.mipmap.ic_file_icon);
                }
                if (sDFileInfo.equals(BrowseFirmwareDialog.this.mSelectedFile)) {
                    viewHolder.flag.setVisibility(0);
                } else {
                    viewHolder.flag.setVisibility(4);
                }
            }
            return view;
        }

        private class ViewHolder {
            ImageView flag;
            ImageView icon;
            TextView name;

            private ViewHolder() {
            }
        }
    }
}
