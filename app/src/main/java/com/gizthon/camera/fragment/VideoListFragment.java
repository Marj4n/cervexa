package com.gizthon.camera.fragment;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.gizthon.camera.R;
import com.gizthon.camera.activity.GalleryListActivity;
import com.gizthon.camera.adapter.PhotoAdapter;
import com.gizthon.camera.databinding.GalleryPhotoFragmentBinding;
import com.gizthon.camera.model.PhotoBean;
import com.jiangdg.usbcamera.UVCCameraHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class VideoListFragment extends BaseXFragment {
    private PhotoAdapter adapter;
    private GalleryPhotoFragmentBinding binding;

    public static VideoListFragment newInstance() {
        Bundle bundle = new Bundle();
        VideoListFragment videoListFragment = new VideoListFragment();
        videoListFragment.setArguments(bundle);
        return videoListFragment;
    }

    @Override // com.gizthon.camera.fragment.BaseXFragment
    protected void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        GalleryPhotoFragmentBinding galleryPhotoFragmentBinding = (GalleryPhotoFragmentBinding) DataBindingUtil.inflate(this.inflater, R.layout.gallery_photo_fragment, this.container, false);
        this.binding = galleryPhotoFragmentBinding;
        setContentView(galleryPhotoFragmentBinding.getRoot());
        this.binding.rcPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        new DownloadFilesTask().execute(new Void[0]);
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, ArrayList<PhotoBean>> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Void... voidArr) {
        }

        private DownloadFilesTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public ArrayList<PhotoBean> doInBackground(Void... voidArr) {
            return VideoListFragment.this.getPhotoBeans();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(ArrayList<PhotoBean> arrayList) {
            VideoListFragment.this.adapter = new PhotoAdapter(arrayList, R.layout.gallery_photo_item);
            VideoListFragment.this.adapter.setOnChangeListener(new PhotoAdapter.OnChangeListener() { // from class: com.gizthon.camera.fragment.VideoListFragment.DownloadFilesTask.1
                @Override // com.gizthon.camera.adapter.PhotoAdapter.OnChangeListener
                public void onCheck(boolean z) {
                    if (VideoListFragment.this.getActivity() instanceof GalleryListActivity) {
                        ((GalleryListActivity) VideoListFragment.this.getActivity()).onShowDone(z);
                    }
                }
            });
            VideoListFragment.this.binding.rcPhoto.setAdapter(VideoListFragment.this.adapter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<PhotoBean> getPhotoBeans() {
        String str = Environment.getExternalStorageDirectory().getPath() + "/MergeCamera/Media/Video/";
        File file = new File(str);
        ArrayList<PhotoBean> arrayList = new ArrayList<>();
        String[] list = file.list();
        Log.e("luoming", "getPhotoBeans: " + list);
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String str2 = list[i];
                if (str2.contains(".avi") || str2.contains(UVCCameraHelper.SUFFIX_MP4) || str2.contains("mov")) {
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setIndex(i);
                    photoBean.setName(str2);
                    if (str2.contains(".avi")) {
                        photoBean.setAviPngName(str + "thumbnails/" + str2.replace("avi", "jpg"));
                    } else if (str2.contains(UVCCameraHelper.SUFFIX_MP4)) {
                        photoBean.setAviPngName(str + "thumbnails/" + str2.replace("mp4", "jpg"));
                    }
                    try {
                        photoBean.setPath(str + str2);
                        photoBean.setVideoVisible(0);
                        arrayList.add(photoBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return arrayList;
    }

    public List<PhotoBean> getSelected() {
        ArrayList arrayList = new ArrayList();
        int size = this.adapter.dataModelList.size();
        for (int i = 0; i < size; i++) {
            PhotoBean photoBean = (PhotoBean) this.adapter.dataModelList.get(i);
            if (photoBean.isSelected()) {
                arrayList.add(photoBean);
            }
        }
        return arrayList;
    }

    public void onRefreshData() {
        this.adapter.setDataModelList(getPhotoBeans());
    }

    public void resetStatus(boolean z) {
        int size = this.adapter.dataModelList.size();
        for (int i = 0; i < size; i++) {
            ((PhotoBean) this.adapter.dataModelList.get(i)).setSelected(z);
        }
        this.adapter.notifyDataSetChanged();
    }

    public void resetComplete() {
        int size = this.adapter.dataModelList.size();
        for (int i = 0; i < size; i++) {
            PhotoBean photoBean = (PhotoBean) this.adapter.dataModelList.get(i);
            if (photoBean.isSelected()) {
                photoBean.setSelected(false);
            }
            photoBean.setSelectedCoverVisible(8);
        }
        this.adapter.notifySelectedStatus();
    }

    public void deleteSelected() {
        new DeleteFilesTask().execute(getSelected());
    }

    private class DeleteFilesTask extends AsyncTask<List<PhotoBean>, Void, List<PhotoBean>> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Void... voidArr) {
        }

        private DeleteFilesTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<PhotoBean> doInBackground(List<PhotoBean>... listArr) {
            List<PhotoBean> list = listArr[0];
            for (int i = 0; i < list.size(); i++) {
                try {
                    new File(list.get(i).getPath()).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<PhotoBean> list) {
            List<PhotoBean> selected = VideoListFragment.this.getSelected();
            for (int i = 0; i < selected.size(); i++) {
                VideoListFragment.this.adapter.dataModelList.remove(selected.get(i));
            }
            VideoListFragment.this.adapter.changeSelectedStatus();
            VideoListFragment.this.adapter.notifySelectedStatus();
        }
    }

    public Bitmap getVideoThumb(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return mediaMetadataRetriever.getFrameAtTime();
    }
}
