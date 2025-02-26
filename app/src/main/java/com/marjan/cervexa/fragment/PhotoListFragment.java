package com.marjan.cervexa.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.marjan.cervexa.activity.GalleryListActivity;
import com.marjan.cervexa.adapter.PhotoAdapter;
import com.marjan.cervexa.application.CameraApplication;
import com.marjan.cervexa.databinding.GalleryPhotoFragmentBinding;
import com.marjan.cervexa.model.PhotoBean;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.weioa.GoPlusDrone.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhotoListFragment extends BaseXFragment {
    private PhotoAdapter adapter;
    private GalleryPhotoFragmentBinding binding;

    public static PhotoListFragment newInstance() {
        Bundle bundle = new Bundle();
        PhotoListFragment photoListFragment = new PhotoListFragment();
        photoListFragment.setArguments(bundle);
        return photoListFragment;
    }

    @Override // com.marjan.cervexa.fragment.BaseXFragment
    protected void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        GalleryPhotoFragmentBinding galleryPhotoFragmentBinding = (GalleryPhotoFragmentBinding) DataBindingUtil.inflate(this.inflater, R.layout.gallery_photo_fragment, this.container, false);
        this.binding = galleryPhotoFragmentBinding;
        setContentView(galleryPhotoFragmentBinding.getRoot());
        this.binding.rcPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        PhotoAdapter photoAdapter = new PhotoAdapter(getPhotoBeans(), R.layout.gallery_photo_item);
        this.adapter = photoAdapter;
        photoAdapter.setOnChangeListener(new PhotoAdapter.OnChangeListener() { // from class: com.marjan.cervexa.fragment.PhotoListFragment.1
            @Override // com.marjan.cervexa.adapter.PhotoAdapter.OnChangeListener
            public void onCheck(boolean z) {
                if (PhotoListFragment.this.getActivity() instanceof GalleryListActivity) {
                    ((GalleryListActivity) PhotoListFragment.this.getActivity()).onShowDone(z);
                }
            }
        });
        this.binding.rcPhoto.setAdapter(this.adapter);
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
            return PhotoListFragment.this.getPhotoBeans();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(ArrayList<PhotoBean> arrayList) {
            PhotoListFragment.this.adapter = new PhotoAdapter(arrayList, R.layout.gallery_photo_item);
            PhotoListFragment.this.adapter.setOnChangeListener(new PhotoAdapter.OnChangeListener() { // from class: com.marjan.cervexa.fragment.PhotoListFragment.DownloadFilesTask.1
                @Override // com.marjan.cervexa.adapter.PhotoAdapter.OnChangeListener
                public void onCheck(boolean z) {
                    if (PhotoListFragment.this.getActivity() instanceof GalleryListActivity) {
                        ((GalleryListActivity) PhotoListFragment.this.getActivity()).onShowDone(z);
                    }
                }
            });
            PhotoListFragment.this.binding.rcPhoto.setAdapter(PhotoListFragment.this.adapter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<PhotoBean> getPhotoBeans() {
        String str = Environment.getExternalStorageDirectory().getPath() + CameraApplication.DIRECTORY_NAME;
        File file = new File(str);
        ArrayList<PhotoBean> arrayList = new ArrayList<>();
        String[] list = file.list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String str2 = list[i];
                if (str2.contains(UVCCameraHelper.SUFFIX_JPEG)) {
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setIndex(i);
                    photoBean.setName(str2);
                    photoBean.setPath(str + str2);
                    arrayList.add(photoBean);
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
            List<PhotoBean> selected = PhotoListFragment.this.getSelected();
            for (int i = 0; i < selected.size(); i++) {
                PhotoListFragment.this.adapter.dataModelList.remove(selected.get(i));
            }
            PhotoListFragment.this.adapter.changeSelectedStatus();
            PhotoListFragment.this.adapter.notifySelectedStatus();
        }
    }
}
