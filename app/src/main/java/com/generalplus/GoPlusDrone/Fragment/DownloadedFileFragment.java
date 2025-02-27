package com.generalplus.GoPlusDrone.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gizthon.camera.R;
import com.jiangdg.usbcamera.UVCCameraHelper;
import generalplus.com.GPCamLib.CamWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class DownloadedFileFragment extends BaseFragment {
    private static final String TAG = "DownloadedFileFragment";
    private GridView m_GridView = null;
    private ListViewItemAdapter m_Adapter = null;
    private String strSaveDirectory = "";
    private String strDevicePICLocation = "";
    private ArrayList<HashMap<String, Object>> listImageItem = new ArrayList<>();
    private Context mContext = null;
    private String title = "";
    private boolean m_bSelect = false;
    private HashMap<Integer, Integer> m_HashMap = new HashMap<>();

    public static DownloadedFileFragment newInstance(String str) {
        DownloadedFileFragment downloadedFileFragment = new DownloadedFileFragment();
        downloadedFileFragment.setTitle(str);
        Bundle bundle = new Bundle();
        bundle.putString(BaseFragment.DATA_NAME, str);
        downloadedFileFragment.setArguments(bundle);
        return downloadedFileFragment;
    }

    @Override // com.generalplus.GoPlusDrone.Fragment.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_downloadedfile, viewGroup, false);
        this.mContext = getActivity();
        this.m_GridView = (GridView) inflate.findViewById(R.id.gridView);
        this.listImageItem.clear();
        this.strSaveDirectory = Environment.getExternalStorageDirectory().getPath() + "/GoPlus_Drone/Photo/";
        this.strDevicePICLocation = Environment.getExternalStorageDirectory().getPath() + CamWrapper.SaveFileToDevicePath;
        String[] list = new File(this.strSaveDirectory).list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String str = list[i];
                if (str.contains(UVCCameraHelper.SUFFIX_JPEG)) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("KEY_ThumbnailFilePath", this.strSaveDirectory + str);
                    hashMap.put("KEY_FilePath", this.strSaveDirectory + str);
                    hashMap.put("KEY_FileType", 0);
                    hashMap.put("KEY_FileName", str);
                    hashMap.put("KEY_FileIndex", String.valueOf(i));
                    this.listImageItem.add(hashMap);
                }
            }
        }
        this.strSaveDirectory = Environment.getExternalStorageDirectory().getPath() + "/GoPlus_Drone/Video/";
        String[] list2 = new File(this.strSaveDirectory).list();
        if (list2 != null) {
            for (int i2 = 0; i2 < list2.length; i2++) {
                String str2 = list2[i2];
                if (str2.contains(UVCCameraHelper.SUFFIX_MP4)) {
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("KEY_ThumbnailFilePath", this.strSaveDirectory + "thumbnails/" + str2.replace("mp4", "jpg"));
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.strSaveDirectory);
                    sb.append(str2);
                    hashMap2.put("KEY_FilePath", sb.toString());
                    hashMap2.put("KEY_FileType", 1);
                    hashMap2.put("KEY_FileName", str2);
                    hashMap2.put("KEY_FileIndex", String.valueOf(i2));
                    this.listImageItem.add(hashMap2);
                }
            }
        }
        ListViewItemAdapter listViewItemAdapter = new ListViewItemAdapter(this.mContext, this.listImageItem, R.layout.downloaded_filelist, new String[]{"KEY_ThumbnailFilePath", "KEY_FileName", "KEY_FileIndex"}, new int[]{R.id.imageView1, R.id.textView1});
        this.m_Adapter = listViewItemAdapter;
        this.m_GridView.setAdapter((ListAdapter) listViewItemAdapter);
        this.m_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.DownloadedFileFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.toggle();
                if (checkBox.isChecked()) {
                    DownloadedFileFragment.this.m_HashMap.put(Integer.valueOf(i3), Integer.valueOf(i3));
                } else {
                    DownloadedFileFragment.this.m_HashMap.remove(Integer.valueOf(i3));
                }
            }
        });
        this.m_Adapter.notifyDataSetChanged();
        return inflate;
    }

    @Override // com.generalplus.GoPlusDrone.Fragment.BaseFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_downloaded, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // com.generalplus.GoPlusDrone.Fragment.BaseFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_Select) {
            this.m_HashMap.clear();
            if (!this.m_bSelect) {
                for (int i = 0; i < this.listImageItem.size(); i++) {
                    this.m_HashMap.put(Integer.valueOf(i), Integer.valueOf(i));
                }
            }
            this.m_Adapter.notifyDataSetChanged();
            this.m_bSelect = !this.m_bSelect;
        } else if (itemId == R.id.action_delete) {
            ShowDeleteDialog();
        } else if (itemId == R.id.action_copy) {
            ShowCopyDialog();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean copyFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + file.getAbsolutePath())));
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return false;
        } catch (Exception e2) {
            Log.e(TAG, e2.getMessage());
            return false;
        }
    }

    private void ShowCopyDialog() {
        if (this.m_HashMap.size() == 0) {
            Toast.makeText(this.mContext, "請選擇檔案.", 0).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle("確定要複製檔案到手機相簿?");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.DownloadedFileFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Iterator it = DownloadedFileFragment.this.m_HashMap.keySet().iterator();
                boolean z = true;
                while (it.hasNext()) {
                    int intValue = ((Integer) it.next()).intValue();
                    DownloadedFileFragment downloadedFileFragment = DownloadedFileFragment.this;
                    if (!downloadedFileFragment.copyFile((String) ((HashMap) downloadedFileFragment.listImageItem.get(intValue)).get("KEY_FilePath"), DownloadedFileFragment.this.strDevicePICLocation + "/" + ((String) ((HashMap) DownloadedFileFragment.this.listImageItem.get(intValue)).get("KEY_FileName")))) {
                        z = false;
                    }
                }
                if (true == z) {
                    Toast.makeText(DownloadedFileFragment.this.mContext, "檔案全部複製成功.", 0).show();
                } else {
                    Toast.makeText(DownloadedFileFragment.this.mContext, "檔案部分複製成功.", 0).show();
                }
                DownloadedFileFragment.this.m_bSelect = false;
                DownloadedFileFragment.this.m_HashMap.clear();
                DownloadedFileFragment.this.m_Adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    private void ShowDeleteDialog() {
        if (this.m_HashMap.size() == 0) {
            Toast.makeText(this.mContext, "Please select file.", 0).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle("Delete File");
        builder.setMessage("Do you sure to delete file?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.DownloadedFileFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                int size = DownloadedFileFragment.this.m_HashMap.size();
                ArrayList arrayList = new ArrayList(DownloadedFileFragment.this.m_HashMap.entrySet());
                Collections.sort(arrayList, new Comparator<Map.Entry<Integer, Integer>>() { // from class: com.generalplus.GoPlusDrone.Fragment.DownloadedFileFragment.3.1
                    @Override // java.util.Comparator
                    public int compare(Map.Entry<Integer, Integer> entry, Map.Entry<Integer, Integer> entry2) {
                        return entry2.getValue().intValue() - entry.getValue().intValue();
                    }
                });
                int i2 = 0;
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    int intValue = ((Integer) ((Map.Entry) arrayList.get(i3)).getKey()).intValue();
                    String str = (String) ((HashMap) DownloadedFileFragment.this.listImageItem.get(intValue)).get("KEY_FilePath");
                    Log.e(DownloadedFileFragment.TAG, "Path = " + str);
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                    if (!file.exists()) {
                        i2++;
                        File file2 = new File((String) ((HashMap) DownloadedFileFragment.this.listImageItem.get(intValue)).get("KEY_ThumbnailFilePath"));
                        if (file2.exists()) {
                            file2.delete();
                        }
                    }
                    DownloadedFileFragment.this.listImageItem.remove(intValue);
                }
                if (i2 == size) {
                    Toast.makeText(DownloadedFileFragment.this.mContext, "檔案刪除成功.", 0).show();
                } else {
                    Toast.makeText(DownloadedFileFragment.this.mContext, "檔案部分刪除成功.", 0).show();
                }
                DownloadedFileFragment.this.m_bSelect = false;
                DownloadedFileFragment.this.m_HashMap.clear();
                DownloadedFileFragment.this.m_Adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    public class ListViewItemAdapter extends SimpleAdapter {
        private boolean[] click;
        private String[] componentTagAry;
        private int[] componentXMLIDAry;
        private Context context;
        private ArrayList<HashMap> data;
        private int itemLayout;

        public ListViewItemAdapter(Context context, ArrayList<HashMap<String, Object>> arrayList, int i, String[] strArr, int[] iArr) {
            super(context, arrayList, i, strArr, iArr);
            this.context = null;
            this.data = null;
            this.componentTagAry = null;
            this.componentXMLIDAry = null;
            this.click = null;
            this.context = context;
            this.itemLayout = i;
            this.componentTagAry = strArr;
            this.componentXMLIDAry = iArr;
            this.click = null;
        }

        @Override // android.widget.SimpleAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = LayoutInflater.from(this.context).inflate(R.layout.downloaded_filelist, viewGroup, false);
                viewHolder.myImage = (ImageView) view2.findViewById(R.id.imageView1);
                viewHolder.checkBox = (CheckBox) view2.findViewById(R.id.checkBox);
                viewHolder.m_iVideo = (ImageView) view2.findViewById(R.id.ivVideo);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            File file = new File((String) ((HashMap) DownloadedFileFragment.this.listImageItem.get(i)).get("KEY_ThumbnailFilePath"));
            if (file.exists()) {
                viewHolder.myImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            if (DownloadedFileFragment.this.m_HashMap.get(Integer.valueOf(i)) == null) {
                viewHolder.checkBox.setChecked(false);
            } else {
                viewHolder.checkBox.setChecked(true);
            }
            if (((Integer) ((HashMap) DownloadedFileFragment.this.listImageItem.get(i)).get("KEY_FileType")).intValue() == 0) {
                viewHolder.m_iVideo.setVisibility(4);
            } else {
                viewHolder.m_iVideo.setVisibility(0);
            }
            return view2;
        }

        class ViewHolder {
            public CheckBox checkBox;
            public ImageView m_iVideo;
            public ImageView myImage;

            ViewHolder() {
            }
        }
    }
}
