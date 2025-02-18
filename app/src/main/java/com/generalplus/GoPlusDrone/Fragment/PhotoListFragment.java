package com.generalplus.GoPlusDrone.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.generalplus.GoPlusDrone.Activity.FullImageActivity;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.generalplus.GoPlusDrone.R;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PhotoListFragment extends BaseFragment {
    private String strSaveDirectory;
    private GridView m_GridView = null;
    private String title = "";

    public static PhotoListFragment newInstance(String str) {
        PhotoListFragment photoListFragment = new PhotoListFragment();
        photoListFragment.setTitle(str);
        Bundle bundle = new Bundle();
        bundle.putString(BaseFragment.DATA_NAME, str);
        photoListFragment.setArguments(bundle);
        return photoListFragment;
    }

    @Override // com.generalplus.GoPlusDrone.Fragment.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            getFile();
        }
    }

    public class FileComparator implements Comparator<String> {
        public FileComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            return (str == null || str.compareTo(str2) < 0) ? 1 : -1;
        }
    }

    private void getFile() {
        this.listImageItem.clear();
        this.strSaveDirectory = Environment.getExternalStorageDirectory().getPath() + "/GoPlus_Drone/Photo/";
        String[] list = new File(this.strSaveDirectory).list();
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : list) {
                arrayList.add(str);
            }
            Collections.sort(arrayList, new FileComparator());
            for (int i = 0; i < arrayList.size(); i++) {
                String str2 = (String) arrayList.get(i);
                if (str2.contains(UVCCameraHelper.SUFFIX_JPEG)) {
                    this.m_ayFilePath.add(this.strSaveDirectory + "/" + str2);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("KEY_ThumbnailFilePath", this.strSaveDirectory + str2);
                    hashMap.put("KEY_FilePath", this.strSaveDirectory + str2);
                    hashMap.put("KEY_FileType", 0);
                    hashMap.put("KEY_FileName", str2);
                    hashMap.put("KEY_FileIndex", String.valueOf(i));
                    this.listImageItem.add(hashMap);
                }
            }
        }
        if (this.m_Adapter != null) {
            this.m_Adapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.activity_filelist, viewGroup, false);
        this.mContext = getActivity();
        GridView gridView = (GridView) inflate.findViewById(R.id.gridView);
        this.m_GridView = gridView;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.PhotoListFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (PhotoListFragment.this.m_bEdit) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                    checkBox.toggle();
                    if (checkBox.isChecked()) {
                        PhotoListFragment.this.m_HashMap.put(Integer.valueOf(i), Integer.valueOf(i));
                        return;
                    } else {
                        PhotoListFragment.this.m_HashMap.remove(Integer.valueOf(i));
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(IConstant.KEY_POSITION, i);
                intent.putStringArrayListExtra("FilePath", PhotoListFragment.this.m_ayFilePath);
                intent.setClass(PhotoListFragment.this.getActivity(), FullImageActivity.class);
                PhotoListFragment.this.startActivity(intent);
            }
        });
        getFile();
        this.m_Adapter = new BaseFragment.ListViewItemAdapter(this.mContext, this.listImageItem, R.layout.downloaded_filelist, new String[]{"KEY_ThumbnailFilePath", "KEY_FileName", "KEY_FileIndex"}, new int[]{R.id.imageView1, R.id.textView1});
        this.m_GridView.setAdapter((ListAdapter) this.m_Adapter);
        this.m_Adapter.notifyDataSetChanged();
        return inflate;
    }
}
