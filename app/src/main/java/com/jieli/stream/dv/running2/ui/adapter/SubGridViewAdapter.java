package com.jieli.stream.dv.running2.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.widget.NoScrollGridView;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.TimeFormate;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class SubGridViewAdapter extends BaseAdapter implements IConstant {
    private NoScrollGridView gridView;
    private boolean isCancelTask;
    private boolean isEditMode;
    private Context mContext;
    private List<FileInfo> mDataList;
    private int viewHeight;
    private int viewWidth;
    private int parentPost = -1;
    private Map<String, LoadCover> taskCollection = new HashMap();
    private String mIP = ClientManager.getClient().getConnectedIP();
    private MainApplication mApplication = MainApplication.getApplication();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    SubGridViewAdapter(Context context, NoScrollGridView noScrollGridView) {
        this.mContext = context;
        this.gridView = noScrollGridView;
        int screenWidth = (AppUtils.getScreenWidth(this.mContext) - (AppUtils.dp2px(this.mContext, 3) * 4)) / 3;
        this.viewWidth = screenWidth;
        this.viewHeight = (screenWidth * 9) / 16;
    }

    public void setDataList(int i, List<FileInfo> list) {
        this.parentPost = i;
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void setEditMode(boolean z) {
        this.isEditMode = z;
    }

    public int getParentPost() {
        return this.parentPost;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<FileInfo> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        List<FileInfo> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mDataList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        FileInfo fileInfo;
        String str;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_media, viewGroup, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!this.gridView.isMeasure() && (fileInfo = (FileInfo) getItem(i)) != null) {
            String name = fileInfo.getName();
            String downloadFilename = AppUtils.getDownloadFilename(fileInfo);
            if (!TextUtils.isEmpty(name)) {
                int judgeFileType = AppUtils.judgeFileType(name);
                if (fileInfo.getSource() == 1) {
                    str = fileInfo.getPath();
                } else {
                    str = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD) + File.separator + downloadFilename;
                }
                if (judgeFileType == 1) {
                    viewHolder.layoutVideo.setVisibility(8);
                    viewHolder.ivVideoState.setVisibility(8);
                    if (AppUtils.checkFileExist(str)) {
                        viewHolder.ivPicState.setVisibility(8);
                        viewHolder.ivThumb.setImageBitmap(ThumbLoader.getInstance().loadLocalThumbnail(this.mContext, str, this.viewWidth, this.viewHeight));
                    } else {
                        viewHolder.ivPicState.setVisibility(0);
                        viewHolder.ivThumb.setTag(str);
                        getPictureThumb(fileInfo, i);
                    }
                } else if (judgeFileType != 2) {
                    viewHolder.ivThumb.setImageResource(R.mipmap.ic_default_picture);
                } else {
                    viewHolder.ivPicState.setVisibility(8);
                    viewHolder.layoutVideo.setVisibility(0);
                    viewHolder.tvDuration.setTag(Integer.valueOf(i));
                    if (AppUtils.checkFileExist(str)) {
                        viewHolder.ivVideoState.setVisibility(0);
                        viewHolder.ivThumb.setTag(str);
                        getLoadVideoThumb(str, i);
                    } else {
                        viewHolder.ivVideoState.setVisibility(8);
                        getVideoThumb(viewHolder.ivThumb, fileInfo);
                    }
                    viewHolder.tvDuration.setText(TimeFormate.getTimeFormatValue(fileInfo.getDuration()));
                }
                if (!this.isEditMode) {
                    fileInfo.setSelected(false);
                    viewHolder.ivSelectState.setVisibility(8);
                } else {
                    viewHolder.ivSelectState.setVisibility(0);
                    if (fileInfo.isSelected()) {
                        viewHolder.ivSelectState.setImageResource(R.mipmap.ic_check_round_blue);
                    } else {
                        viewHolder.ivSelectState.setImageResource(R.mipmap.ic_uncheck_round);
                    }
                }
            }
        }
        return view;
    }

    private class ViewHolder {
        private ImageView ivPicState;
        private ImageView ivSelectState;
        private ImageView ivThumb;
        private ImageView ivVideoState;
        private RelativeLayout layoutVideo;
        private TextView tvDuration;

        ViewHolder(View view) {
            ImageView imageView = (ImageView) view.findViewById(R.id.item_media_thumb);
            this.ivThumb = imageView;
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(SubGridViewAdapter.this.viewWidth, SubGridViewAdapter.this.viewHeight));
            this.ivSelectState = (ImageView) view.findViewById(R.id.item_media_select_state);
            this.ivPicState = (ImageView) view.findViewById(R.id.item_media_picture_state);
            this.ivVideoState = (ImageView) view.findViewById(R.id.item_media_video_state);
            this.layoutVideo = (RelativeLayout) view.findViewById(R.id.item_media_video_layout);
            this.tvDuration = (TextView) view.findViewById(R.id.item_media_duration);
            view.setTag(this);
        }
    }

    private void getPictureThumb(FileInfo fileInfo, int i) {
        String str = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_DOWNLOAD) + File.separator + AppUtils.getDownloadFilename(fileInfo);
        if (this.taskCollection.containsKey(str)) {
            return;
        }
        LoadCover loadCover = new LoadCover();
        this.taskCollection.put(str, loadCover);
        loadCover.execute(Integer.valueOf(i));
    }

    private void getVideoThumb(ImageView imageView, FileInfo fileInfo) {
        String str = AppUtils.splicingFilePath(this.mApplication.getAppName(), this.mApplication.getUUID(), AppUtils.checkCameraDir(fileInfo), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(fileInfo);
        if (AppUtils.checkFileExist(str)) {
            ThumbLoader.getInstance().loadLocalThumbnail(this.mContext, str, this.viewWidth, this.viewHeight);
            Bitmap bitmap = ThumbLoader.getInstance().getBitmap(str);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            } else {
                imageView.setImageResource(R.mipmap.ic_default_picture);
                return;
            }
        }
        imageView.setImageResource(R.mipmap.ic_default_picture);
    }

    private void getLoadVideoThumb(String str, int i) {
        if (this.taskCollection.containsKey(str)) {
            return;
        }
        LoadCover loadCover = new LoadCover();
        this.taskCollection.put(str, loadCover);
        loadCover.execute(Integer.valueOf(i));
    }

    public void cancelAllTasks() {
        this.isCancelTask = true;
        Map<String, LoadCover> map = this.taskCollection;
        if (map != null) {
            Set<String> keySet = map.keySet();
            if (keySet.size() > 0) {
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    LoadCover loadCover = this.taskCollection.get(it.next());
                    if (loadCover != null) {
                        loadCover.cancel(true);
                    }
                }
            }
            this.taskCollection.clear();
        }
        this.isCancelTask = false;
    }

    private class LoadCover extends AsyncTask<Integer, Void, Bitmap> {
        private Bitmap bmp;
        private String imageUrl;
        private FileInfo info;
        private int position;

        private LoadCover() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Integer... numArr) {
            int i = 0;
            int intValue = numArr[0].intValue();
            this.position = intValue;
            FileInfo fileInfo = (FileInfo) SubGridViewAdapter.this.getItem(intValue);
            this.info = fileInfo;
            Bitmap bitmap = null;
            if (fileInfo != null) {
                if (fileInfo.getSource() == 1) {
                    this.imageUrl = this.info.getPath();
                } else {
                    this.imageUrl = AppUtils.splicingFilePath(SubGridViewAdapter.this.mApplication.getAppName(), SubGridViewAdapter.this.mApplication.getUUID(), AppUtils.checkCameraDir(this.info), IConstant.DIR_DOWNLOAD) + File.separator + AppUtils.getDownloadFilename(this.info);
                }
                if (this.info.isVideo()) {
                    ThumbLoader.getInstance().loadLocalVideoThumb(SubGridViewAdapter.this.mContext, this.imageUrl, SubGridViewAdapter.this.viewWidth, SubGridViewAdapter.this.viewHeight, new ThumbLoader.OnLoadVideoThumbListener() { // from class: com.jieli.stream.dv.running2.ui.adapter.SubGridViewAdapter.LoadCover.1
                        @Override // com.jieli.stream.dv.running2.util.ThumbLoader.OnLoadVideoThumbListener
                        public void onComplete(Bitmap bitmap2, int i2) {
                            LoadCover.this.bmp = bitmap2;
                            LoadCover.this.info.setDuration(i2);
                        }
                    });
                } else {
                    String str = AppUtils.splicingFilePath(SubGridViewAdapter.this.mApplication.getAppName(), SubGridViewAdapter.this.mApplication.getUUID(), AppUtils.checkCameraDir(this.info), IConstant.DIR_THUMB) + File.separator + AppUtils.getVideoThumbName(this.info);
                    if (new File(str).exists()) {
                        ThumbLoader.getInstance().loadLocalThumbnail(SubGridViewAdapter.this.mContext, str, SubGridViewAdapter.this.viewWidth, SubGridViewAdapter.this.viewHeight);
                        bitmap = ThumbLoader.getInstance().getBitmap(str);
                    }
                    if (bitmap == null) {
                        ThumbLoader.getInstance().loadWebThumbnail(SubGridViewAdapter.this.mContext, AppUtils.formatUrl(SubGridViewAdapter.this.mIP, 8080, this.info.getPath()), str, SubGridViewAdapter.this.viewWidth, SubGridViewAdapter.this.viewHeight, new ThumbLoader.OnLoadThumbListener() { // from class: com.jieli.stream.dv.running2.ui.adapter.SubGridViewAdapter.LoadCover.2
                            @Override // com.jieli.stream.dv.running2.util.ThumbLoader.OnLoadThumbListener
                            public void onComplete(Bitmap bitmap2) {
                                if (bitmap2 != null) {
                                    LoadCover.this.bmp = bitmap2;
                                }
                            }
                        });
                    } else {
                        this.bmp = bitmap;
                    }
                }
                while (this.bmp == null && !SubGridViewAdapter.this.isCancelTask) {
                    try {
                        Thread.sleep(5L);
                        i += 5;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 2000) {
                        break;
                    }
                }
            } else {
                this.bmp = null;
            }
            return this.bmp;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            TextView textView;
            ImageView imageView = (ImageView) SubGridViewAdapter.this.gridView.findViewWithTag(this.imageUrl);
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.mipmap.ic_default_picture);
                }
            }
            FileInfo fileInfo = this.info;
            if (fileInfo != null && fileInfo.isVideo() && (textView = (TextView) SubGridViewAdapter.this.gridView.findViewWithTag(Integer.valueOf(this.position))) != null) {
                textView.setText(TimeFormate.getTimeFormatValue(this.info.getDuration()));
            }
            SubGridViewAdapter.this.taskCollection.remove(this.imageUrl);
        }
    }
}
