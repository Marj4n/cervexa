package com.xyzlf.share.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ChannelEntity;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ChannelUtil;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ShareDialogActivity extends ShareBaseActivity implements AdapterView.OnItemClickListener {
    protected List<ChannelEntity> channelEntities;
    protected ShareEntity data;
    protected SparseArray<ShareEntity> sparseArray;
    private String tag = getClass().getSimpleName();

    @Override // com.xyzlf.share.library.ShareBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        Object data;
        super.onCreate(bundle);
        setContentView(R.layout.share_activity_dialog);
        if (getIntent().hasExtra(ShareConstant.EXTRA_SHARE_DATA)) {
            Bundle bundleExtra = getIntent().getBundleExtra(ShareConstant.EXTRA_SHARE_DATA);
            if (bundleExtra != null) {
                data = bundleExtra.get(ShareConstant.EXTRA_SHARE_DATA);
            } else {
                try {
                    data = getIntent().getParcelableExtra(ShareConstant.EXTRA_SHARE_DATA);
                } catch (Exception unused) {
                    data = null;
                }
                if (data == null) {
                    data = getIntent().getSerializableExtra(ShareConstant.EXTRA_SHARE_DATA);
                }
            }
        } else {
            data = getIntent().getData();
        }
        if (data == null) {
            ToastUtil.showToast((Context) this, getString(R.string.share_empty_tip), true);
            finish();
            return;
        }
        if (data instanceof ShareEntity) {
            this.data = (ShareEntity) data;
        } else if (data instanceof SparseArray) {
            this.sparseArray = (SparseArray) data;
        }
        if (this.data == null && this.sparseArray == null) {
            ToastUtil.showToast((Context) this, getString(R.string.share_empty_tip), true);
            finish();
            return;
        }
        initChannelData();
        if (this.channelEntities.isEmpty()) {
            finish();
        } else {
            initView();
        }
    }

    private void initChannelData() {
        this.channelEntities = new ArrayList();
        if (ChannelUtil.isWeixinInstall(this)) {
            if ((this.channel & 1) > 0 && isShowChannel(1)) {
                this.channelEntities.add(new ChannelEntity(1, R.drawable.share_wechat, getString(R.string.share_channel_weixin_friend)));
            }
            if ((this.channel & 2) <= 0 || !isShowChannel(2)) {
                return;
            }
            this.channelEntities.add(new ChannelEntity(2, R.drawable.share_wxcircle, getString(R.string.share_channel_weixin_circle)));
        }
    }

    private boolean isShowChannel(int i) {
        SparseArray<ShareEntity> sparseArray = this.sparseArray;
        return sparseArray == null || sparseArray.get(i) != null;
    }

    private void initView() {
        AppGridAdapter appGridAdapter = new AppGridAdapter();
        GridView gridView = (GridView) findViewById(R.id.share_grid);
        gridView.setAdapter((ListAdapter) appGridAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 20112 && intent != null) {
            finishWithResult(intent.getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1), intent.getIntExtra(ShareConstant.EXTRA_SHARE_STATUS, -1));
        } else {
            finish();
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [android.widget.Adapter] */
    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ChannelEntity channelEntity = (ChannelEntity) adapterView.getAdapter().getItem(i);
        if (channelEntity == null) {
            return;
        }
        Log.e(this.tag, "OnItemClick=" + i + ", channel=" + channelEntity.getchannel());
        Intent intent = new Intent(ShareConstant.ACTION_SHARE_CHANNEL);
        intent.putExtra(ShareConstant.KEY_SHARE_CHANNEL, channelEntity.getchannel());
        sendBroadcast(intent);
        handleShare(channelEntity.getchannel());
    }

    protected void handleShare(int i) {
        if (i == 1) {
            shareByWeixinFriend();
            return;
        }
        if (i == 2) {
            shareByWeixinCircle();
            return;
        }
        if (i == 4) {
            shareBySinaWeibo();
            return;
        }
        if (i == 8) {
            shareByQQ();
            return;
        }
        if (i == 16) {
            shareByQZone();
            return;
        }
        if (i == 32) {
            shareBySms();
            return;
        }
        if (i == 64) {
            shareByEmail();
        } else {
            if (i != 1024) {
                return;
            }
            shareBySystem();
            finish();
        }
    }

    protected void shareByQQ() {
        ShareUtil.startShare(this, 8, getShareData(8), ShareConstant.REQUEST_CODE);
    }

    protected void shareByQZone() {
        ShareUtil.startShare(this, 16, getShareData(16), ShareConstant.REQUEST_CODE);
    }

    protected void shareByWeixinFriend() {
        ShareUtil.startShare(this, 1, getShareData(1), ShareConstant.REQUEST_CODE);
    }

    protected void shareByWeixinCircle() {
        ShareUtil.startShare(this, 2, getShareData(2), ShareConstant.REQUEST_CODE);
    }

    protected void shareBySystem() {
        ShareUtil.startShare(this, 1024, getShareData(1024), ShareConstant.REQUEST_CODE);
    }

    protected void shareBySms() {
        ShareUtil.startShare(this, 32, getShareData(32), ShareConstant.REQUEST_CODE);
    }

    protected void shareByEmail() {
        ShareUtil.startShare(this, 64, getShareData(64), ShareConstant.REQUEST_CODE);
    }

    protected void shareBySinaWeibo() {
        ShareUtil.startShare(this, 4, getShareData(4), ShareConstant.REQUEST_CODE);
    }

    protected ShareEntity getShareData(int i) {
        ShareEntity shareEntity = this.data;
        if (shareEntity != null) {
            return shareEntity;
        }
        SparseArray<ShareEntity> sparseArray = this.sparseArray;
        if (sparseArray != null) {
            return sparseArray.get(i);
        }
        return null;
    }

    private class AppGridAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        private AppGridAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return ShareDialogActivity.this.channelEntities.size();
        }

        @Override // android.widget.Adapter
        public ChannelEntity getItem(int i) {
            return ShareDialogActivity.this.channelEntities.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = ShareDialogActivity.this.getLayoutInflater().inflate(R.layout.share_gridview_item, viewGroup, false);
                viewHolder.textView = (TextView) view2.findViewById(R.id.text);
                viewHolder.imageView = (ImageView) view2.findViewById(R.id.image);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            ChannelEntity item = getItem(i);
            viewHolder.imageView.setImageResource(item.getIcon());
            viewHolder.textView.setText(item.getName());
            return view2;
        }
    }

    private class ViewHolder {
        public ImageView imageView;
        public TextView textView;

        private ViewHolder() {
        }
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT < 11 && motionEvent.getAction() == 0 && isOutOfBounds(this, motionEvent)) {
            finish();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean isOutOfBounds(Activity activity, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int scaledWindowTouchSlop = ViewConfiguration.get(activity).getScaledWindowTouchSlop();
        View decorView = activity.getWindow().getDecorView();
        int i = -scaledWindowTouchSlop;
        return x < i || y < i || x > decorView.getWidth() + scaledWindowTouchSlop || y > decorView.getHeight() + scaledWindowTouchSlop;
    }
}
