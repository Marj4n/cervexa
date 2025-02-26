package com.jieli.stream.dv.running2.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class AppStorageManageActivity extends BaseActivity implements BrowseFileDialog.OnSelectResultListener, AdapterView.OnItemClickListener {
    private BrowseFileDialog browseFileDialog;
    private SettingItem cacheSizeItem;
    private NotifyDialog cleanCacheDialog;
    private SettingAdapter mAdapter;
    private ListView settingListView;
    private SettingItem storagePathItem;
    private String tag = getClass().getSimpleName();

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) AppStorageManageActivity.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_storage_manage);
        ((TextView) findViewById(R.id.app_storage_tv)).setText(AppUtils.getExternalMemorySize(getApplicationContext()));
        ListView listView = (ListView) findViewById(R.id.app_storage_view);
        this.settingListView = listView;
        listView.setOnItemClickListener(this);
        initListView();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        dismissClearCacheDialog();
    }

    public void returnBtnClick(View view) {
        onBackPressed();
    }

    @Override // com.jieli.stream.dv.running2.ui.dialog.BrowseFileDialog.OnSelectResultListener
    public void onResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String substring = str.substring(ROOT_PATH.length());
        if (substring.startsWith(File.separator)) {
            substring = substring.substring(substring.indexOf(File.separator) + 1);
        }
        Dbug.i(this.tag, " ============= newPathName : " + substring + "================");
        if (substring.equals(this.mApplication.getAppName())) {
            return;
        }
        PreferencesHelper.putStringValue(getApplicationContext(), IConstant.KEY_ROOT_PATH_NAME, substring);
        this.mApplication.setAppName(substring);
        SettingItem settingItem = this.storagePathItem;
        if (settingItem != null) {
            settingItem.setValue(ROOT_PATH + File.separator + this.mApplication.getAppName());
            this.mAdapter.notifyDataSetChanged();
        }
        ToastUtil.showToastLong(getString(R.string.modify_storage_url_success));
    }

    private void initListView() {
        String str;
        String[] stringArray = getResources().getStringArray(R.array.storage_operation);
        ArrayList arrayList = new ArrayList();
        for (String str2 : stringArray) {
            SettingItem settingItem = new SettingItem();
            settingItem.setName(str2);
            if (str2.equals(getString(R.string.storage_size))) {
                str = AppUtils.getFormatSize(2.097152E8d);
                settingItem.setType(2);
            } else if (str2.equals(getString(R.string.storage_path))) {
                str = ROOT_PATH + File.separator + this.mApplication.getAppName();
                this.storagePathItem = settingItem;
            } else if (str2.equals(getString(R.string.clean_cache))) {
                str = getCache();
                this.cacheSizeItem = settingItem;
            } else {
                str = "";
            }
            settingItem.setValue(str);
            arrayList.add(settingItem);
        }
        SettingAdapter settingAdapter = new SettingAdapter(this, arrayList);
        this.mAdapter = settingAdapter;
        this.settingListView.setAdapter((ListAdapter) settingAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCache() {
        List<String> queryThumbDirPath = AppUtils.queryThumbDirPath(AppUtils.splicingFilePath(this.mApplication.getAppName(), null, null, null));
        long j = 0;
        if (queryThumbDirPath != null && queryThumbDirPath.size() > 0) {
            Iterator<String> it = queryThumbDirPath.iterator();
            while (it.hasNext()) {
                File file = new File(it.next());
                if (file.exists()) {
                    try {
                        j += AppUtils.getFolderSize(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return AppUtils.getFormatSize(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCache() {
        List<String> queryThumbDirPath = AppUtils.queryThumbDirPath(AppUtils.splicingFilePath(this.mApplication.getAppName(), null, null, null));
        if (queryThumbDirPath == null || queryThumbDirPath.size() <= 0) {
            return;
        }
        Iterator<String> it = queryThumbDirPath.iterator();
        while (it.hasNext()) {
            File file = new File(it.next());
            if (file.exists()) {
                AppUtils.deleteFile(file);
            }
        }
        ThumbLoader.getInstance().clearCache();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        SettingItem settingItem;
        SettingAdapter settingAdapter = this.mAdapter;
        if (settingAdapter == null || (settingItem = (SettingItem) settingAdapter.getItem(i)) == null) {
            return;
        }
        String name = settingItem.getName();
        if (name.equals(getString(R.string.storage_size))) {
            return;
        }
        if (name.equals(getString(R.string.storage_path))) {
            BrowseFileDialog browseFileDialog = new BrowseFileDialog();
            this.browseFileDialog = browseFileDialog;
            browseFileDialog.setOnSelectResultListener(this);
            this.browseFileDialog.show(getSupportFragmentManager(), "browse_file_dialog");
            return;
        }
        if (name.equals(getString(R.string.clean_cache))) {
            showClearCacheDialog();
        }
    }

    private void showClearCacheDialog() {
        if (this.cleanCacheDialog == null) {
            this.cleanCacheDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.clean_cache_content, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.AppStorageManageActivity.2
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    AppStorageManageActivity.this.cleanCacheDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.activity.me.AppStorageManageActivity.3
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    AppStorageManageActivity.this.cleanCacheDialog.dismiss();
                    AppStorageManageActivity.this.clearCache();
                    AppStorageManageActivity.this.cacheSizeItem.setValue(AppStorageManageActivity.this.getCache());
                    AppStorageManageActivity.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
        if (this.cleanCacheDialog.isShowing()) {
            return;
        }
        this.cleanCacheDialog.show(getSupportFragmentManager(), "clean_cache");
    }

    private void dismissClearCacheDialog() {
        NotifyDialog notifyDialog = this.cleanCacheDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing()) {
                this.cleanCacheDialog.dismiss();
            }
            this.cleanCacheDialog = null;
        }
    }
}
