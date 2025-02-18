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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.generalplus.GoPlusDrone.Activity.GalleryActivity;
import com.generalplus.GoPlusDrone.R;
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
public class BaseFragment extends Fragment {
    public static final String DATA_NAME = "name";
    protected static final String KEY_FileIndex = "KEY_FileIndex";
    protected static final String KEY_FileName = "KEY_FileName";
    protected static final String KEY_FilePath = "KEY_FilePath";
    protected static final String KEY_FileType = "KEY_FileType";
    protected static final String KEY_ThumbnailFilePath = "KEY_ThumbnailFilePath";
    private static final String TAG = "BaseFragment";
    protected Context mContext = null;
    private String title = "";
    private int indicatorColor = -16776961;
    private int dividerColor = -7829368;
    private int iconResId = 0;
    protected HashMap<Integer, Integer> m_HashMap = new HashMap<>();
    protected ArrayList<HashMap<String, Object>> listImageItem = new ArrayList<>();
    protected boolean m_bSelect = false;
    protected ListViewItemAdapter m_Adapter = null;
    private String strDevicePICLocation = "";
    public boolean m_bEdit = false;
    protected ArrayList<String> m_ayFilePath = new ArrayList<>();

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorColor(int i) {
        this.indicatorColor = i;
    }

    public int getDividerColor() {
        return this.dividerColor;
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
    }

    public int getIconResId() {
        return this.iconResId;
    }

    public void setIconResId(int i) {
        this.iconResId = i;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (this.m_bEdit) {
            menuInflater.inflate(R.menu.menu_downloaded, menu);
        } else {
            menuInflater.inflate(R.menu.menu_edit, menu);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        this.strDevicePICLocation = Environment.getExternalStorageDirectory().getPath() + CamWrapper.SaveFileToDevicePath;
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_edit) {
            this.m_bEdit = true;
            GalleryActivity.m_bEdit = true;
            getActivity().invalidateOptionsMenu();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.m_Adapter.notifyDataSetChanged();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        } else {
            if (itemId == R.id.action_done) {
                this.m_bSelect = false;
                this.m_HashMap.clear();
                this.m_bEdit = false;
                GalleryActivity.m_bEdit = false;
                getActivity().invalidateOptionsMenu();
                this.m_Adapter.notifyDataSetChanged();
            } else if (itemId == R.id.action_Select) {
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
            Toast.makeText(this.mContext, !isCN() ? "Please select file." : "请选择文件.", 0).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(!isCN() ? "Are you sure to copy the selected file to Album?" : "是否复制选择的文件到相册?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.BaseFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Iterator<Integer> it = BaseFragment.this.m_HashMap.keySet().iterator();
                boolean z = true;
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    BaseFragment baseFragment = BaseFragment.this;
                    if (!baseFragment.copyFile((String) baseFragment.listImageItem.get(intValue).get(BaseFragment.KEY_FilePath), BaseFragment.this.strDevicePICLocation + "/" + ((String) BaseFragment.this.listImageItem.get(intValue).get(BaseFragment.KEY_FileName)))) {
                        z = false;
                    }
                }
                if (true == z) {
                    Toast.makeText(BaseFragment.this.mContext, !BaseFragment.this.isCN() ? "Copy to Album Successfully." : "复制到相机成功!", 0).show();
                } else {
                    Toast.makeText(BaseFragment.this.mContext, !BaseFragment.this.isCN() ? "Copy part files Successfully." : "复制选择的文件成功!", 0).show();
                }
                BaseFragment.this.m_bSelect = false;
                BaseFragment.this.m_HashMap.clear();
                BaseFragment.this.m_Adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    public boolean isCN() {
        return getActivity().getApplicationContext().getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    private void ShowDeleteDialog() {
        if (this.m_HashMap.size() == 0) {
            Toast.makeText(this.mContext, !isCN() ? "Please select file." : "请选择文件.", 0).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(!isCN() ? "Are you sure to delete the selected file?" : "确定删除选择的文件?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Fragment.BaseFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                int size = BaseFragment.this.m_HashMap.size();
                ArrayList arrayList = new ArrayList(BaseFragment.this.m_HashMap.entrySet());
                Collections.sort(arrayList, new Comparator<Map.Entry<Integer, Integer>>() { // from class: com.generalplus.GoPlusDrone.Fragment.BaseFragment.2.1
                    @Override // java.util.Comparator
                    public int compare(Map.Entry<Integer, Integer> entry, Map.Entry<Integer, Integer> entry2) {
                        return entry2.getValue().intValue() - entry.getValue().intValue();
                    }
                });
                int i2 = 0;
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    int intValue = ((Integer) ((Map.Entry) arrayList.get(i3)).getKey()).intValue();
                    String str = (String) BaseFragment.this.listImageItem.get(intValue).get(BaseFragment.KEY_FilePath);
                    Log.e(BaseFragment.TAG, "Path = " + str);
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                    if (!file.exists()) {
                        i2++;
                        File file2 = new File((String) BaseFragment.this.listImageItem.get(intValue).get(BaseFragment.KEY_ThumbnailFilePath));
                        if (file2.exists()) {
                            file2.delete();
                        }
                    }
                    BaseFragment.this.listImageItem.remove(intValue);
                    BaseFragment.this.m_ayFilePath.remove(intValue);
                }
                if (i2 == size) {
                    Toast.makeText(BaseFragment.this.mContext, !BaseFragment.this.isCN() ? "File deleted successfully." : "删除文件成功!", 0).show();
                } else {
                    Toast.makeText(BaseFragment.this.mContext, !BaseFragment.this.isCN() ? "Delete part files successfully." : "删除选择的文件成功!", 0).show();
                }
                BaseFragment.this.m_bSelect = false;
                BaseFragment.this.m_HashMap.clear();
                BaseFragment.this.m_Adapter.notifyDataSetChanged();
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

        public String getFileName(String str) {
            int lastIndexOf = str.lastIndexOf("/");
            if (lastIndexOf < 0) {
                lastIndexOf = str.lastIndexOf("\\");
            }
            return lastIndexOf > -1 ? str.substring(lastIndexOf + 1) : str;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
            int i3 = options.outHeight;
            int i4 = options.outWidth;
            if (i3 <= i2 && i4 <= i) {
                return 1;
            }
            int round = Math.round(i3 / i2);
            int round2 = Math.round(i4 / i);
            return round < round2 ? round : round2;
        }

        @Override // android.widget.SimpleAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = LayoutInflater.from(this.context).inflate(R.layout.downloaded_filelist, viewGroup, false);
                viewHolder.myImage = (ImageView) view2.findViewById(R.id.imageView1);
                viewHolder.txtFileName = (TextView) view2.findViewById(R.id.textView1);
                viewHolder.checkBox = (CheckBox) view2.findViewById(R.id.checkBox);
                viewHolder.m_iVideo = (ImageView) view2.findViewById(R.id.ivVideo);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            String str = (String) BaseFragment.this.listImageItem.get(i).get(BaseFragment.KEY_ThumbnailFilePath);
            if (((Integer) BaseFragment.this.listImageItem.get(i).get(BaseFragment.KEY_FileType)).intValue() != 0) {
                viewHolder.txtFileName.setText(getFileName(str).replace(UVCCameraHelper.SUFFIX_JPEG, UVCCameraHelper.SUFFIX_MP4));
            } else {
                viewHolder.txtFileName.setText(getFileName(str));
            }
            File file = new File(str);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                options.inSampleSize = calculateInSampleSize(options, 128, 100);
                options.inJustDecodeBounds = false;
                try {
                    viewHolder.myImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath(), options));
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            } else {
                viewHolder.myImage.setImageResource(R.drawable.broken);
            }
            if (BaseFragment.this.m_bEdit) {
                viewHolder.checkBox.setVisibility(0);
            } else {
                viewHolder.checkBox.setVisibility(4);
            }
            if (BaseFragment.this.m_HashMap.get(Integer.valueOf(i)) == null) {
                viewHolder.checkBox.setChecked(false);
            } else {
                viewHolder.checkBox.setChecked(true);
            }
            if (((Integer) BaseFragment.this.listImageItem.get(i).get(BaseFragment.KEY_FileType)).intValue() == 0) {
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
            public TextView txtFileName;

            ViewHolder() {
            }
        }
    }
}
